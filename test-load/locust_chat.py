from locust import User, task, between, events
import websocket
import json
import time
import random
from faker import Faker

fake = Faker("ko_KR")

# 4명의 유저 토큰 가정
USER_TOKENS = {
    1: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiNGZmMDIyOTQ1MWQ4ZmM0Zjk4YjBjMmE2NTQ1ZGEzMyIsImlhdCI6MTc2MTQ4NDEzMiwiZXhwIjoxNzYxNDg1OTMyLCJuaWNrbmFtZSI6IuyKue2YuCIsInByb2ZpbGVVcmwiOiIvdXBsb2Fkcy8xNzYxNDY0NTg4NzcxcG5nd2luZy5jb20gKDEpLnBuZyIsImVtYWlsIjoibm92YTAyMDUxMEBuYXZlci5jb20iLCJpZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0._vIsAsEb8Ba4KFO8ngMPESoHkA2eHaUckSovbr5Qycs",
    2: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiNGZmMDIyOTQ1MWQ4ZmM0Zjk4YjBjMmE2NTQ1ZGEzMyIsImlhdCI6MTc2MTQ4NDEzMiwiZXhwIjoxNzYxNDg1OTMyLCJuaWNrbmFtZSI6IuyKue2YuCIsInByb2ZpbGVVcmwiOiIvdXBsb2Fkcy8xNzYxNDY0NTg4NzcxcG5nd2luZy5jb20gKDEpLnBuZyIsImVtYWlsIjoibm92YTAyMDUxMEBuYXZlci5jb20iLCJpZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0._vIsAsEb8Ba4KFO8ngMPESoHkA2eHaUckSovbr5Qycs",
    3: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiNGZmMDIyOTQ1MWQ4ZmM0Zjk4YjBjMmE2NTQ1ZGEzMyIsImlhdCI6MTc2MTQ4NDEzMiwiZXhwIjoxNzYxNDg1OTMyLCJuaWNrbmFtZSI6IuyKue2YuCIsInByb2ZpbGVVcmwiOiIvdXBsb2Fkcy8xNzYxNDY0NTg4NzcxcG5nd2luZy5jb20gKDEpLnBuZyIsImVtYWlsIjoibm92YTAyMDUxMEBuYXZlci5jb20iLCJpZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0._vIsAsEb8Ba4KFO8ngMPESoHkA2eHaUckSovbr5Qycs",
    4: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiNGZmMDIyOTQ1MWQ4ZmM0Zjk4YjBjMmE2NTQ1ZGEzMyIsImlhdCI6MTc2MTQ4NDEzMiwiZXhwIjoxNzYxNDg1OTMyLCJuaWNrbmFtZSI6IuyKue2YuCIsInByb2ZpbGVVcmwiOiIvdXBsb2Fkcy8xNzYxNDY0NTg4NzcxcG5nd2luZy5jb20gKDEpLnBuZyIsImVtYWlsIjoibm92YTAyMDUxMEBuYXZlci5jb20iLCJpZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0._vIsAsEb8Ba4KFO8ngMPESoHkA2eHaUckSovbr5Qycs",
}


class AuctionChatUser(User):
    wait_time = between(1, 3)

    def on_start(self):
        # ✅ 유저 랜덤 선택
        self.user_id = random.choice(list(USER_TOKENS.keys()))
        self.access_token = USER_TOKENS[self.user_id]
        self.nickname = f"테스트유저{self.user_id}"
        self.profile_url = f"https://picsum.photos/seed/{self.user_id}/100"
        self.auction_id = 1  # 테스트용 고정 경매방

        # ✅ WebSocket 연결
        self.ws = websocket.WebSocket()
        self.ws.connect("ws://localhost:8080/ws")

        # ✅ STOMP CONNECT 프레임
        connect_frame = (
            f"CONNECT\naccept-version:1.2\nheart-beat:10000,10000\n"
            f"Authorization: Bearer {self.access_token}\n\n\x00"
        )
        self.ws.send(connect_frame)
        connected = self.ws.recv()
        print(f"✅ User {self.user_id} connected:", connected)

        # ✅ 구독
        sub_frame = f"SUBSCRIBE\nid:sub-{self.user_id}\ndestination:/topic/auction/{self.auction_id}\nack:auto\n\n\x00"
        self.ws.send(sub_frame)
        print(f"🎯 User {self.user_id} subscribed to /topic/auction/{self.auction_id}")

    @task
    def send_chat(self):
        """랜덤 메시지 전송"""
        message = fake.sentence(nb_words=5)
        data = {
            "contents": message,
            "nickname": self.nickname,
            "profileUrl": self.profile_url,
            "userId": self.user_id,
        }

        send_frame = (
                f"SEND\ndestination:/app/chat/send/{self.auction_id}\n"
                "content-type:application/json\n\n"
                + json.dumps(data)
                + "\x00"
        )

        try:
            start_time = time.time()
            self.ws.send(send_frame)
            total_time = int((time.time() - start_time) * 1000)

            events.request.fire(
                request_type="WebSocket",
                name=f"chat_message_user_{self.user_id}",
                response_time=total_time,
                response_length=len(send_frame),
                exception=None,
            )
            print(f"💬 [유저 {self.user_id}] 보냄: {message}")

        except Exception as e:
            total_time = int((time.time() - start_time) * 1000)
            events.request.fire(
                request_type="WebSocket",
                name="chat_message_error",
                response_time=total_time,
                response_length=0,
                exception=e,
            )
            print(f"❌ [유저 {self.user_id}] 메시지 전송 실패:", e)

    def on_stop(self):
        try:
            self.ws.send("DISCONNECT\n\n\x00")
            self.ws.close()
            print(f"🛑 User {self.user_id} disconnected")
        except Exception as e:
            print(f"⚠️ Disconnect error (User {self.user_id}):", e)
