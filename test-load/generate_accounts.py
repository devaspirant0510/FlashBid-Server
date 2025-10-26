#!/usr/bin/env python3
"""
대량 계정 생성 스크립트
- PostgreSQL에 연결해 지정한 수의 계정을 INSERT 합니다.
- 한글 이름은 동사+이름 조합으로 생성하여 중복 가능성을 낮춥니다.
- 이메일/uuid는 고유하게 생성합니다.

사용법 예시:
  python3 generate_accounts.py --count 100000 --chunk 5000 --commit

환경변수 (기본값 제공):
  DB_HOST (default: localhost)
  DB_PORT (default: 5432)
  DB_NAME (default: flashbid)
  DB_USER (default: postgres)
  DB_PASSWORD (default: postgres)

주의:
- 기본적으로 트랜잭션을 롤백하도록 되어 있어 안전한 테스트가 가능합니다. 실제로 DB에 반영하려면 --commit 옵션을 사용하세요.
- 스크립트는 `psycopg2-binary`와 `Faker`가 필요합니다.
  pip install psycopg2-binary Faker

가정(중요):
- `account` 테이블의 `id` 컬럼은 시퀀스(자동증가)를 사용한다고 가정하여 INSERT문에서 `id`를 생략합니다.
  (원하시면 id를 직접 넣도록 수정 가능합니다.)
"""

import os
import sys
import uuid
import random
import argparse
from datetime import datetime, timedelta
from io import StringIO

from faker import Faker
from dotenv import load_dotenv
import psycopg2
from psycopg2.extras import execute_values

# .env 파일 로드 (있다면 우선순위로 사용)
load_dotenv()

# 한글 동사 리스트: 간단한 동사들을 조합해 닉네임을 만듭니다.
KOREAN_VERBS = [
    "달리는", "걷는", "웃는", "노래하는", "춤추는", "읽는", "쓰는", "먹는", "마시는", "생각하는",
    "달려가는", "빛나는", "달콤한", "빠른", "느린", "행복한", "슬픈", "용감한", "멋진", "조용한"
]

DEFAULT_DB = {
    "host": os.getenv("DB_HOST", "localhost"),
    "port": int(os.getenv("DB_PORT", "5432")),
    "dbname": os.getenv("DB_NAME", "flashbid"),
    "user": os.getenv("DB_USER", "postgres"),
    "password": os.getenv("DB_PASSWORD", "postgres"),
}

LOGIN_TYPES = ["EMAIL", "KAKAO", "GOOGLE"]

faker = Faker("ko_KR")
Faker.seed(42)
random.seed(42)


def make_connection():
    conn = psycopg2.connect(
        host=DEFAULT_DB["host"],
        port=DEFAULT_DB["port"],
        dbname=DEFAULT_DB["dbname"],
        user=DEFAULT_DB["user"],
        password=DEFAULT_DB["password"],
    )
    return conn


def generate_account_row(existing_nicknames, idx):
    # 한글 이름(실제 이름) 생성
    name = faker.first_name()  # 한국어 성/이름의 한 부분으로 한글 이름 생성
    verb = random.choice(KOREAN_VERBS)
    nickname = f"{verb}{name}"
    # 중복 처리: 같은 nickname이 이미 있으면 인덱스를 붙임
    if nickname in existing_nicknames:
        nickname = f"{nickname}{idx}"
    existing_nicknames.add(nickname)

    # 이메일: Faker의 email을 사용(유니크 확보를 위해 unique 호출)
    try:
        email = faker.unique.email()
    except Exception:
        # unique 제한에 걸리면 fallback으로 닉네임+short_uuid 사용
        short_uuid = uuid.uuid4().hex[:8]
        email = f"{nickname.replace(' ','')}.{short_uuid}@example.com"

    # uuid는 고유값
    u = uuid.uuid4().hex

    # point: 랜덤 분포 (0 ~ 1,000,000)
    point = random.randint(800_000, 1_000_000)

    # created_at: 최근 1년 내 임의 시각
    now = datetime.utcnow()
    created_at = now - timedelta(days=random.randint(0, 365), seconds=random.randint(0, 86400))
    updated_at = created_at + timedelta(seconds=random.randint(0, 86400))

    # profile_url: Faker의 이미지 URL을 사용 (30%는 NULL 유지)
    if random.random() < 0.3:
        profile_url = None
    else:
        # Faker의 이미지 URL 생성 (placeholder 이미지 링크)
        profile_url = faker.image_url(width=640, height=480)

    # 기타 고정값
    is_verified = False
    deleted_at = None
    description = None
    password = None
    login_type = random.choice(LOGIN_TYPES)
    user_status = "UN_LINK"
    user_type = "CUSTOMER"

    return (
        is_verified,
        point,
        created_at,
        deleted_at,
        updated_at,
        description,
        email,
        nickname,
        password,
        profile_url,
        u,
        login_type,
        user_status,
        user_type,
    )


def insert_batch(conn, rows, commit=False):
    # INSERT 대상 컬럼들 (id는 시퀀스 사용 가정으로 제외)
    cols = (
        "is_verified",
        "point",
        "created_at",
        "deleted_at",
        "updated_at",
        "description",
        "email",
        "nickname",
        "password",
        "profile_url",
        "uuid",
        "login_type",
        "user_status",
        "user_type",
    )
    sql = f"INSERT INTO public.account ({', '.join(cols)}) VALUES %s"
    with conn.cursor() as cur:
        execute_values(cur, sql, rows, template=None, page_size=400)
    if commit:
        conn.commit()


def main():
    parser = argparse.ArgumentParser(description="Bulk generate accounts into Postgres")
    parser.add_argument("--count", type=int, default=100000, help="Number of accounts to create (default 100000)")
    parser.add_argument("--chunk", type=int, default=5000, help="Batch size per INSERT (default 5000)")
    parser.add_argument("--commit", action="store_true", help="Actually commit changes to DB. If omitted, transaction will be rolled back.")
    args = parser.parse_args()

    total = args.count
    chunk = args.chunk

    print(f"DB 연결 시도: {DEFAULT_DB['host']}:{DEFAULT_DB['port']} db={DEFAULT_DB['dbname']} user={DEFAULT_DB['user']}")
    conn = make_connection()
    print("연결 성공")

    existing_nicknames = set()
    created = 0
    try:
        while created < total:
            batch_size = min(chunk, total - created)
            rows = []
            for i in range(batch_size):
                row = generate_account_row(existing_nicknames, created + i + 1)
                rows.append(row)

            insert_batch(conn, rows, commit=args.commit)
            created += batch_size
            print(f"생성/삽입 완료: {created}/{total}")

        if not args.commit:
            print("--commit 옵션이 없으므로 롤백합니다 (변경사항 미반영).")
            conn.rollback()
        else:
            print("--commit 옵션이 있어 커밋되었습니다.")

    except Exception as e:
        print("오류 발생:", e)
        conn.rollback()
        raise
    finally:
        conn.close()


if __name__ == '__main__':
    main()
