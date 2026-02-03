#!/usr/bin/env python3
import os
import sys
import uuid
import random
import argparse
import gc
from datetime import datetime, timedelta
from faker import Faker
from dotenv import load_dotenv
import psycopg2
from psycopg2.extras import execute_values
import uuid
# .env 파일 로드
load_dotenv()

KOREAN_VERBS = [
    "달리는", "걷는", "웃는", "노래하는", "춤추는", "읽는", "쓰는",
    "먹는", "마시는", "생각하는", "달려가는", "빛나는", "달콤한",
    "빠른", "느린", "행복한", "슬픈", "용감한", "멋진", "조용한"
]

DEFAULT_DB = {
    "host": os.getenv("DB_HOST", "localhost"),
    "port": int(os.getenv("DB_PORT", "5432")),
    "dbname": os.getenv("DB_NAME", "flashbid"),
    "user": os.getenv("DB_USER", "postgres"),
    "password": os.getenv("DB_PASSWORD", "postgres"),
}

LOGIN_TYPES = [0, 1, 2, 3, 4]

def make_connection():
    conn = psycopg2.connect(
        host=DEFAULT_DB["host"],
        port=DEFAULT_DB["port"],
        dbname=DEFAULT_DB["dbname"],
        user=DEFAULT_DB["user"],
        password=DEFAULT_DB["password"],
    )
    conn.set_session(autocommit=False)
    return conn

aaid=1
def generate_account_row(faker_instance, idx):
    global aaid
    """각 배치마다 새로운 faker 인스턴스 사용"""
    aid = aaid
    aaid+=1
    name = faker_instance.first_name()
    verb = random.choice(KOREAN_VERBS)
    # ★ 인덱스 기반 고유성 보장 (메모리 사용 제로)
    nickname = f"{verb}{name}{idx}"

    # ★ unique 사용 안 함 (캐시 누적 방지)
    email = faker_instance.unique.email()
    short_uuid = random.randint(1, 999999)
    local, domain = email.split('@', 1)
    email = f"{local}{short_uuid}@{domain}"
    u = uuid.uuid4().hex
    point = random.randint(50_000, 1_000_000)

    now = datetime.utcnow()
    created_at = now - timedelta(days=random.randint(0, 365), seconds=random.randint(0, 86400))
    updated_at = created_at + timedelta(seconds=random.randint(0, 86400))

    profile_url = None if random.random() < 0.3 else f"https://picsum.photos/640/480?random={idx}"

    is_verified = False
    deleted_at = None
    description = None
    password = None
    login_type = random.choice(LOGIN_TYPES)
    user_status = 4
    user_type = 0

    return (
        aid,is_verified, point, created_at, deleted_at, updated_at,
        description, email, nickname, password, profile_url,
        u, login_type, user_status, user_type
    )


def insert_batch(conn, rows):
    cols = (
        "id","is_verified", "point", "created_at", "deleted_at", "updated_at",
        "description", "email", "nickname", "password", "profile_url",
        "uuid", "login_type", "user_status", "user_type"
    )

    sql = f"INSERT INTO public.account ({', '.join(cols)}) VALUES %s"

    with conn.cursor() as cur:
        execute_values(cur, sql, rows, template=None, page_size=500)


def main():
    parser = argparse.ArgumentParser(description="Bulk generate accounts into Postgres")
    parser.add_argument("--count", type=int, default=1_000_000, help="Number of accounts to create")
    parser.add_argument("--chunk", type=int, default=2000, help="Batch size per INSERT")
    args = parser.parse_args()

    total = args.count
    chunk = args.chunk

    print(f"DB 연결: {DEFAULT_DB['host']}:{DEFAULT_DB['port']} / DB={DEFAULT_DB['dbname']} / USER={DEFAULT_DB['user']}")
    
    created = 0
    COMMIT_UNIT = 10000

    try:
        while created < total:
            # ★★★ 매 커밋 단위마다 새로운 Faker 인스턴스 생성 ★★★
            faker_instance = Faker("ko_KR")
            conn = make_connection()
            print(f"\n새 세션 시작 (진행: {created}/{total})")
            
            try:
                batch_count = 0
                
                while batch_count < COMMIT_UNIT and created < total:
                    batch_size = min(chunk, COMMIT_UNIT - batch_count, total - created)
                    rows = []

                    for i in range(batch_size):
                        # ★ 전역 인덱스 사용 (created + i + 1)
                        row = generate_account_row(faker_instance, created + i + 1)
                        rows.append(row)

                    insert_batch(conn, rows)
                    batch_count += batch_size
                    created += batch_size

                    # 진행률 표시
                    progress = (created / total) * 100
                    print(f"진행: {created}/{total} ({progress:.1f}%) | 현재 TX: {batch_count}건", end='\r')

                conn.commit()
                print(f"\n✔ 커밋: {batch_count}건 (총 {created}/{total})")
                    
            except Exception as e:
                print(f"\n롤백 발생: {e}")
                conn.rollback()
                raise
            finally:
                conn.close()
                # ★★★ 명시적 메모리 정리 ★★★
                del faker_instance
                gc.collect()

        print("\n✅ 전체 작업 완료!")

    except KeyboardInterrupt:
        print("\n\n⚠ 사용자 중단")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ 오류: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == '__main__':
    main()