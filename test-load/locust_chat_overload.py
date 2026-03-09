import itertools
import json
import os
import random
import threading
import time
import base64
import hashlib
import hmac

import websocket
import psycopg2
from locust import User, constant_pacing, events, task
from dotenv import load_dotenv


load_dotenv(os.path.join(os.path.dirname(__file__), ".env"))


AUCTION_START = int(os.getenv("AUCTION_START", "1"))
AUCTION_END = int(os.getenv("AUCTION_END", "100"))
AUCTION_COUNT = AUCTION_END - AUCTION_START + 1

WS_BASE_URL = os.getenv("WS_BASE_URL", "ws://localhost:8080/ws")
TOKENS_FILE = os.getenv("CHAT_TOKENS_FILE", "")
DEFAULT_TOKEN = os.getenv("CHAT_ACCESS_TOKEN", "")
CHAT_TOKEN_SOURCE = os.getenv("CHAT_TOKEN_SOURCE", "db_first").strip().lower()
JWT_SECRET_KEY = os.getenv("JWT_SECRET_KEY", "")
WS_CONNECT_TIMEOUT = float(os.getenv("WS_CONNECT_TIMEOUT", "10"))
WS_CONNECT_RETRY = int(os.getenv("WS_CONNECT_RETRY", "3"))
WS_CONNECT_RETRY_INTERVAL = float(os.getenv("WS_CONNECT_RETRY_INTERVAL", "0.2"))

DB_HOST = os.getenv("DB_HOST", "localhost")
DB_PORT = int(os.getenv("DB_PORT", "5432"))
DB_NAME = os.getenv("DB_NAME", "flash_bid")
DB_USER = os.getenv("DB_USER", "postgres")
DB_PASSWORD = os.getenv("DB_PASSWORD", "postgres")

TOKEN_SUB_START = int(os.getenv("TOKEN_SUB_START", "1"))
TOKEN_SUB_END = int(os.getenv("TOKEN_SUB_END", "1000"))
TOKEN_SUB_MODE = os.getenv("TOKEN_SUB_MODE", "uuid").strip().lower()

ROLE_BY_ORDINAL = {
    0: "CUSTOMER",
    1: "SELLER",
    2: "ADMIN",
    3: "UN_REGISTER",
}

_user_seq = itertools.count(1)
_seq_lock = threading.Lock()
_loaded_tokens = None
_loaded_token_source = None


def _b64url(data: bytes) -> str:
    return base64.urlsafe_b64encode(data).rstrip(b"=").decode("utf-8")


def _create_hs256_jwt(claims: dict, secret: str) -> str:
    header = {"alg": "HS256", "typ": "JWT"}
    header_b64 = _b64url(
        json.dumps(header, separators=(",", ":"), ensure_ascii=False).encode("utf-8")
    )
    payload_b64 = _b64url(
        json.dumps(claims, separators=(",", ":"), ensure_ascii=False).encode("utf-8")
    )
    signing_input = f"{header_b64}.{payload_b64}".encode("utf-8")
    signature = hmac.new(secret.encode("utf-8"), signing_input, hashlib.sha256).digest()
    return f"{header_b64}.{payload_b64}.{_b64url(signature)}"


def _load_tokens_from_db():
    if not JWT_SECRET_KEY:
        return []

    conn = psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD,
    )
    try:
        with conn.cursor() as cur:
            cur.execute(
                """
                SELECT id, uuid, nickname, profile_url, email, user_type
                FROM account
                WHERE id BETWEEN %s AND %s
                ORDER BY id
                """,
                (TOKEN_SUB_START, TOKEN_SUB_END),
            )
            rows = cur.fetchall()

        now = int(time.time())
        exp = now + (30 * 60)
        tokens = []

        for row in rows:
            account_id, uuid, nickname, profile_url, email, user_type = row
            role = ROLE_BY_ORDINAL.get(user_type, "CUSTOMER")
            sub_value = str(account_id) if TOKEN_SUB_MODE == "id" else str(uuid)
            claims = {
                "sub": sub_value,
                "iat": now,
                "exp": exp,
                "nickname": nickname or "",
                "profileUrl": profile_url or "",
                "email": email or "",
                "id": str(account_id),
                "role": role,
            }
            tokens.append(_create_hs256_jwt(claims, JWT_SECRET_KEY))

        return tokens
    finally:
        conn.close()


def _load_tokens():
    global _loaded_tokens
    global _loaded_token_source
    if _loaded_tokens is not None:
        return _loaded_tokens

    file_tokens = []
    if TOKENS_FILE and os.path.exists(TOKENS_FILE):
        with open(TOKENS_FILE, "r", encoding="utf-8") as f:
            file_tokens = [line.strip() for line in f if line.strip()]
    elif DEFAULT_TOKEN:
        file_tokens = [DEFAULT_TOKEN]

    db_tokens = _load_tokens_from_db() if JWT_SECRET_KEY else []
    source = "none"
    tokens = []

    if CHAT_TOKEN_SOURCE == "db":
        tokens = db_tokens
        source = "db"
    elif CHAT_TOKEN_SOURCE == "file":
        tokens = file_tokens
        source = "file"
    elif CHAT_TOKEN_SOURCE == "file_first":
        if file_tokens:
            tokens = file_tokens
            source = "file"
        else:
            tokens = db_tokens
            source = "db"
    else:
        if db_tokens:
            tokens = db_tokens
            source = "db"
        else:
            tokens = file_tokens
            source = "file"

    _loaded_tokens = tokens
    _loaded_token_source = source
    print(
        f"[token-loader] source={_loaded_token_source}, count={len(_loaded_tokens)}, "
        f"sub_range={TOKEN_SUB_START}..{TOKEN_SUB_END}, sub_mode={TOKEN_SUB_MODE}"
    )
    return _loaded_tokens


class AuctionChatOverloadUser(User):
    # 유저당 1초에 1회 전송 => 경매당 유저 10명이면 약 10 msg/s
    wait_time = constant_pacing(1.0)

    def on_start(self):
        tokens = _load_tokens()
        if not tokens:
            raise RuntimeError(
                "토큰이 없습니다. CHAT_ACCESS_TOKEN/CHAT_TOKENS_FILE 또는 JWT_SECRET_KEY+DB 설정을 확인하세요."
            )

        with _seq_lock:
            self.user_idx = next(_user_seq)

        # 1~100번 경매로 라운드로빈 배치
        self.auction_id = AUCTION_START + ((self.user_idx - 1) % AUCTION_COUNT)
        self.user_id = self.user_idx
        self.nickname = f"load-user-{self.user_id}"
        self.profile_url = f"https://picsum.photos/seed/load-{self.user_id}/80"
        self.access_token = tokens[(self.user_idx - 1) % len(tokens)]

        last_error = None
        for attempt in range(1, WS_CONNECT_RETRY + 1):
            try:
                self.ws = websocket.WebSocket()
                self.ws.settimeout(WS_CONNECT_TIMEOUT)
                self.ws.connect(WS_BASE_URL)

                connect_frame = (
                    "CONNECT\n"
                    "accept-version:1.2\n"
                    "heart-beat:10000,10000\n"
                    f"Authorization: Bearer {self.access_token}\n\n\x00"
                )
                self.ws.send(connect_frame)
                connected_frame = self.ws.recv()
                if not str(connected_frame).startswith("CONNECTED"):
                    raise RuntimeError(f"Unexpected STOMP frame: {connected_frame}")
                break
            except Exception as e:
                last_error = e
                try:
                    self.ws.close()
                except Exception:
                    pass
                if attempt < WS_CONNECT_RETRY:
                    time.sleep(WS_CONNECT_RETRY_INTERVAL)
                else:
                    raise RuntimeError(
                        f"WS 연결 실패 url={WS_BASE_URL}, timeout={WS_CONNECT_TIMEOUT}, "
                        f"retry={WS_CONNECT_RETRY}, user_idx={self.user_idx}, error={repr(last_error)}"
                    ) from e

        sub_frame = (
            f"SUBSCRIBE\nid:sub-{self.user_id}\n"
            f"destination:/topic/public/{self.auction_id}\nack:auto\n\n\x00"
        )
        self.ws.send(sub_frame)

    @task
    def send_chat(self):
        message = f"auction-{self.auction_id} message-{random.randint(1, 999999)}"
        payload = {
            "contents": message,
            "nickname": self.nickname,
            "profileUrl": self.profile_url,
            "userId": self.user_id,
        }

        frame = (
            f"SEND\ndestination:/app/chat/send/{self.auction_id}\n"
            "content-type:application/json\n\n"
            f"{json.dumps(payload, ensure_ascii=False)}\x00"
        )

        start = time.time()
        try:
            self.ws.send(frame)
            elapsed = int((time.time() - start) * 1000)
            events.request.fire(
                request_type="WebSocket",
                name=f"auction-{self.auction_id}-chat-send",
                response_time=elapsed,
                response_length=len(frame),
                exception=None,
            )
        except Exception as e:
            elapsed = int((time.time() - start) * 1000)
            events.request.fire(
                request_type="WebSocket",
                name="chat-send-error",
                response_time=elapsed,
                response_length=0,
                exception=e,
            )

    def on_stop(self):
        try:
            self.ws.send("DISCONNECT\n\n\x00")
            self.ws.close()
        except Exception:
            pass
