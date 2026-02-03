import os
import sys
import uuid
import random
import gc
from datetime import datetime, timedelta
from faker import Faker
from dotenv import load_dotenv
import psycopg2
from psycopg2.extras import execute_values

# .env 파일 로드 
load_dotenv()

DEFAULT_DB = {
    "host": os.getenv("DB_HOST", "localhost"),
    "port": int(os.getenv("DB_PORT", "5432")),
    "dbname": os.getenv("DB_NAME", "flashbid"),
    "user": os.getenv("DB_USER", "postgres"),
    "password": os.getenv("DB_PASSWORD", "postgres"),
}

faker = Faker("ko_KR")
RANDOM_SEED = 8884844
Faker.seed(RANDOM_SEED)
random.seed(RANDOM_SEED)

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


def generate_goods_row(faker_instance, idx):
    """goods 데이터 한 row 생성"""
    product_types = [
        "노트북", "스마트폰", "태블릿", "이어폰", "키보드", "마우스", 
        "모니터", "카메라", "시계", "가방", "신발", "의류", "가구", 
        "책", "게임기", "자전거", "운동기구", "악기", "식기", "조명"
    ]
    brands = ["삼성", "LG", "애플", "소니", "나이키", "아디다스", "프리미엄", "베스트"]

    greetings = [
    "판매합니다 😊",
    "팝니다 🙌",
    "급처합니다 😭",
    "싸게 팔아요 ✨",
    "오늘만 싸게 드려요 🔥",
    "급하게 내놔요 💨",
    "득템 기회 드립니다 🤩",
    "좋은 가격에 놓고가요 🙏",
    "관심 있으신 분~? 👀",
    "필요하신 분 가져가세요 💛",
    "쿨거래 원합니다 ❄️",
    "찐으로 싸게 드려요 😤",
    "진짜 급해요 제발 가져가세요 😭",
    "이 가격에 못 사요 😎",
    "상태 좋아요 ✨",
    "가져가시면 행복함 +1 💖",
    "가격 협의 가능해요 🤝",
    "미개봉이에요 🎁",
    "거의 새거예요 😳",
    "사용감 적어요 🌱",
    "필요한 분 계신가요? 🙇",
    "꼭 필요한 분한테 갔으면 좋겠어요 🌈",
    "관심 있으시면 연락주세요 📩",
    "가볍게 문의만 주세요 ☁️",
    "오늘 올리자마자 팔고 싶어요 🥺",
    "가성비 미쳤어요 🤯",
    "정말 좋은 물건이에요 ⭐",
    "이건 바로 가져가셔야 돼요 ⏰",
    "빠르게 가져가실 분! 💨",
    "선착순입니다 🏃‍♂️",
    "문의 폭주 중입니다 (?) 😂",
    "이상하게 안 팔려서 가격 내립니다 😢",
    "집 정리 중이라 싸게 드려요 🧹",
    "공간이 부족해요… 제발 가져가세요 🏠",
    "이사 가기 전에 정리해요 🚚",
    "사용감 거의 제로 👍",
    "상태는 사진 참고해주세요 📸",
    "작동 100% 문제 없습니다 💯",
    "애지중지 하던 아이예요 💕",
    "예쁘게 써주실 분 찾습니다 🌸",
    "한 번 써보고 안 썼어요 😅",
    "비흡연 / 반려동물 없음 🐶",
    "필요하시면 추가 사진 보내드려요 📷",
    "방금 세척 완료했습니다 🧼",
    "하자 없어요 확인하세요 👌",
    "직거래 환영합니다 🤗",
    "택배도 가능해요 📦",
    "택배비는 협의 가능 🛵",
    "특가로 올려요 🎉",
    "내려갈 생각 없습니다 😤",
    "적당히 네고 가능해요 🙏",
    "찔러보기 환영?! 😆",
    "진심으로 사는 분만 연락 주세요 🥲",
    "구매하시면 복 받으실 거예요 😇",
    "이거 아시는 분만 알죠 😉",
    "아직 있어요! 살아있어요! 🧞",
    "제 인생템이었어요 🩷",
    "정말 아끼던 거라 잘 가갔으면 🥹",
    "가격 미쳤습니다 그냥 가져가세요 😵",
    "선물로도 좋아요 🎀",
    "소장용으로도 좋습니다 🗂️",
    "오늘 안에 팔면 할인합니다 💸",
    "이번 주까지만 판매해요 📅",
    "상태 깔끔합니다 🧽",
    "구매 시 바로 사용 가능해요 ⚡",
    "구매자분 꼭 행복하세요 🌟",
    "이 가격이면 그냥 이득이에요 😎",
    "가져가면 진짜 후회 안 해요 👍",
    "정말 좋은 제품이에요 🍀",
    "문의만 해도 친절히 답해요 💬",
    "급매라 싸게 내놓아요 🎯",
    "직거래는 편하게 시간 맞춰드려요 😊",
    "이렇게 싼 건 처음 보실 걸요 🥴",
    "사진보다 실물이 더 좋아요 😳",
    "저도 아깝지만 내놓습니다 🥲",
    "정가 생각하면 눈물나요 😭",
    "반값 이하로 드려요 💥",
    "그냥 가져가세요(제발) 🙏😂",
    "빠른 거래 원해요 💨",
    "좋은 주인 만나길 🙌",
    "궁금한 점 있으면 아무거나 물어보세요 ☕",
    "딱 지금이 기회예요 ⏳",
    "절대 후회 안 합니다 🔥",
    "사용 횟수 거의 없음 ㅎㅎ 🙄",
    "선물 받고 안 썼어요 🎁",
    "급하게 정리합니다 🧹",
    "사진보다 실물이 더 예뻐요 💖",
    "평소 가격보다 훨씬 싸요 📉",
    "정말 추천합니다 👍✨",
    "관심만 주셔도 기뻐요 🥹",
    "오늘 안에 팔고 싶어요 😭",
    "새 상품급 상태 🆕",
    "조금만 쓰고 보관만 했어요 📦",
    "필요하신 분이 쓰면 좋겠어요 😊",
    "편하게 문의 주세요 💌",
    "이번에 정리하면서 팝니다 📦",
    "작동 테스트 완료! 🧪",
    "한 번 써보면 생각보다 좋습니다 😎",
    "가격 대비 성능 최고 🤯",
    "좋은 분에게 갔으면 좋겠어요 💗",
      "판매합니다",
    "팝니다",
    "급매합니다",
    "정리 중이라 판매합니다",
    "사용하지 않아 판매합니다",
    "상태 양호하여 판매합니다",
    "단순 보관만 하여 판매합니다",
    "필요하신 분께 판매합니다",
    "가격 조정 가능합니다",
    "빠른 거래 원합니다",
    "정상 작동합니다",
    "하자 없습니다",
    "실사용 적습니다",
    "이번 주 내 판매 희망합니다",
    "오늘 중 판매 희망합니다",
    "거래 가능합니다",
    "직거래 가능합니다",
    "택배 가능합니다",
    "정가 대비 저렴하게 판매합니다",
    "구성품 모두 포함되어 있습니다",
    "사진 참조 부탁드립니다",
    "실물 상태 양호합니다",
    "상세 설명 참고 바랍니다",
    "구매 의사 있으신 분 연락 바랍니다",
    "문의 주시면 답변 드리겠습니다",
    "사용 기간 짧습니다",
    "용도 변경으로 판매합니다",
    "정리 목적의 판매입니다",
    "보관 상태 좋습니다",
    "교환 불가합니다",
    "환불 불가합니다",
    "물량 한정으로 판매합니다",
    "가능한 빠르게 가져가실 분 찾습니다",
    "구매 후 즉시 사용 가능합니다",
    "실물 확인 가능합니다",
    "구매자 신중히 결정 부탁드립니다",
    "가격 협의 가능합니다",
    "상태 민감하신 분 참고 바랍니다",
    "판매 완료 시 글 삭제합니다",
]
    contact = [
    "관심 있으시면 편하게 연락 주세요 😊",
    "궁금하시면 언제든지 톡 주세요 💬",
    "조금이라도 관심 생기면 연락 주세요 ✨",
    "문의는 편하게 주세요 🙌",
    "살짝 고민돼도 톡 주세요 👀",
    "궁금한 점 있으시면 남겨주세요 🤗",
    "연락 주시면 빠르게 답변드릴게요 ⚡",
    "필요하시면 바로 연락 주세요 💛",
    "편하게 말씀 주세요 ☁️",
    "문의만 해도 괜찮아요 🌿",
    "살짝만 관심 있으셔도 연락 주세요 🌈",
    "궁금한 거 있으면 아무 때나 톡 주세요 📩",
    "연락 기다리고 있을게요 🤍",
    "혹시 관심 있으시면 알려주세요 🥺",
    "가볍게 문의 주세요 🍀",
    "생각 있으시면 연락만 주세요 🙏",
    "연락만 주시면 상세히 안내드릴게요 📘",
    "관심 생기면 주저 말고 연락 주세요 🔥",
    "문의 주시면 친절하게 답해드릴게요 💛",
    "조금이라도 궁금하시면 연락 주세요 🌟", "관심 있으시면 연락 바랍니다.",
    "문의가 필요하시면 연락 주십시오.",
    "필요 시 연락 부탁드립니다.",
    "관심 있으신 분은 연락해 주시기 바랍니다.",
    "문의하실 사항이 있으면 연락 바랍니다.",
    "구매 의사가 있으신 경우 연락 바랍니다.",
    "거래 의사 있으시면 연락 주시기 바랍니다.",
    "질문이 있으시면 연락 부탁드립니다.",
    "확인 후 연락 주시기 바랍니다.",
    "연락 주시면 안내드리겠습니다.",
    ]
    product = random.choice(product_types)
    brand = random.choice(brands)
    condition = random.choice(["새상품", "중고", "리퍼", "전시품"])

    title = f"{brand} {product} {condition} - {faker_instance.catch_phrase()}"

    descriptions = [
        f"{product} {random.choice(greetings)}",
        faker_instance.text(max_nb_chars=150),
        f"구매 시기: {faker_instance.date_between(start_date='-2y', end_date='today').strftime('%Y년 %m월')}",
        f"사용 기간: 약 {random.randint(1, 24)}개월",
        faker_instance.text(max_nb_chars=100),
        f"상태: {random.choice(['매우 좋음', '좋음', '보통', '사용감 있음'])}",
        f"직거래 가능 지역: {faker_instance.address()}",
        f"{random.choice(contact)}"
    ]

    description = "\n".join(descriptions)

    delivery_type = random.choice([0, 1, 2])

    now = datetime.utcnow()
    created_at = now - timedelta(days=random.randint(0, 365), seconds=random.randint(0, 86400))
    updated_at = created_at + timedelta(seconds=random.randint(0, 86400))

    goods_uuid = uuid.uuid4().hex
    price = random.randint(10000, 5000000)
    view_count = random.randint(0, 1000)
    like_count = random.randint(0, 200)

    return (
        title,
        description,
        delivery_type
    )


def insert_goods_batch(conn, rows):
    """goods 배치 삽입"""

    cols = (
        "title",
        "description",
        "delivery_type"
    )
    sql = f"INSERT INTO public.goods ({', '.join(cols)}) VALUES %s"
    
    with conn.cursor() as cur:
        execute_values(cur, sql, rows, page_size=500)


def generate_and_insert_goods(total_count=100000, commit_unit=5000, chunk=500):
    """
    * total_count: 총 생성할 데이터 수
    * commit_unit: 한 세션에서 몇 건을 커밋할지
    * chunk: INSERT 한 번에 넣을 레코드 수
    """

    print(f"\n=== Goods 데이터 생성 시작: {total_count}건 ===\n")

    created = 0

    try:
        while created < total_count:
            faker_instance = Faker("ko_KR")

            conn = make_connection()
            print(f"새 세션 시작 (진행: {created}/{total_count})")

            try:
                batch_count = 0

                while batch_count < commit_unit and created < total_count:
                    batch_size = min(chunk, commit_unit - batch_count, total_count - created)
                    rows = [generate_goods_row(faker_instance, created + i + 1) for i in range(batch_size)]

                    insert_goods_batch(conn, rows)

                    batch_count += batch_size
                    created += batch_size
                    progress = (created / total_count) * 100
                    print(f"진행: {created}/{total_count} ({progress:.1f}%) | 현재 TX: {batch_count}건", end='\r')

                conn.commit()
                print(f"\n✔ 커밋 완료: {batch_count}건 (총 {created}/{total_count})")

            except Exception as e:
                print(f"\n롤백 발생: {e}")
                conn.rollback()
                raise

            finally:
                conn.close()
                del faker_instance
                gc.collect()

        print(f"\n=== ✅ Goods 데이터 생성 완료: {created}건 ===\n")

    except Exception as e:
        print(f"\n❌ 오류 발생: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
from datetime import datetime, timedelta

def generate_auction_row(idx):
    """
    idx: goods_id = auction_id 매핑 (1 ~ 50000)
    """

    now = datetime.utcnow()

    # ---------- created_at ----------
    created_at = now - timedelta(
        days=random.randint(0, 30),
        seconds=random.randint(0, 86400)
    )
    updated_at = created_at

    # ---------- start_time ----------
    # -30일 ~ +14일
    start_base_day = random.randint(-30, 14)
    start_hour = random.randint(0, 23)
    start_minute = random.choice([0, 30])

    start_time = (now + timedelta(days=start_base_day)).replace(
        hour=start_hour,
        minute=start_minute,
        second=0,
        microsecond=0
    )

    # ---------- end_time ----------
    end_time = start_time + timedelta(days=random.randint(1, 10))

    # ---------- auction_status ----------
    # 0: 예정 / 1: 진행 / 2: 종료
    if start_time > now:
        auction_status = 0
    elif end_time < now:
        auction_status = 2
    else:
        auction_status = 1

    # ---------- auction_type ----------
    auction_type = random.choice([0, 1])

    # ---------- bid_unit ----------
    bid_unit = random.randint(1, 10) * 1000

    # ---------- count ----------
    count = 0

    # ---------- start_price ----------
    start_price = random.randint(5, 500) * 10000

    # ---------- view_count ----------
    view_count = random.randint(0, 50000)

    # ---------- category_id ----------
    category_id = random.randint(1, 8)

    # ---------- user_id ----------
    user_id = random.randint(1, 1_000_000)

    # ---------- goods_id ----------
    goods_id = idx  # 1~50000 순차

    return (
        auction_status,
        auction_type,
        bid_unit,
        count,
        start_price,
        category_id,
        created_at,
        None,                # delivery_info_id
        end_time,
        goods_id,
        idx,                 # auction.id = idx (직접 넣을 경우)
        start_time,
        None,                # trading_area_id
        updated_at,
        user_id
    )

def insert_auction_batch(conn, rows):
    cols = (
        "auction_status",
        "auction_type",
        "bid_unit",
        "count",
        "start_price",
        "category_id",
        "created_at",
        "delivery_info_id",
        "end_time",
        "goods_id",
        "id",
        "start_time",
        "trading_area_id",
        "updated_at",
        "user_id"
    )

    sql = f"INSERT INTO auction ({', '.join(cols)}) VALUES %s"

    with conn.cursor() as cur:
        execute_values(cur, sql, rows, page_size=1000)
def generate_and_insert_auction(total_count=50000, chunk=5000):
    print(f"\n=== Auction 생성 시작: {total_count}건 ===\n")

    created = 0

    while created < total_count:
        conn = make_connection()
        rows = []

        batch_size = min(chunk, total_count - created)

        for i in range(batch_size):
            idx = created + i + 1
            row = generate_auction_row(idx)
            rows.append(row)

        insert_auction_batch(conn, rows)
        conn.commit()
        conn.close()

        created += batch_size
        print(f"진행: {created}/{total_count} ({(created/total_count)*100:.1f}%)")

    print("\n=== 🎉 Auction 생성 완료 ===\n")

def generate_and_insert_files(total_goods, batch_size=10000):
    """
    total_goods: 처리할 goods 총 개수 (예: 50000)
    batch_size: 한 루프에서 처리하는 goods 개수 (트랜잭션 단위)
    동작:
      - batch 단위로 새 DB 연결 생성
      - batch 단위로 Faker.seed(RANDOM_SEED + batch_start) 호출 후 faker 인스턴스 생성
      - 각 goods 당 3~5개의 file 레코드 생성 (extension='jpg', file_name=uuid, url=faker.image_url())
      - execute_values로 한 번에 INSERT, 커밋, 연결 닫음
    """
    assert batch_size > 0
    print(f"=== File 데이터 생성 시작: Goods {total_goods}개, batch_size={batch_size} ===")

    total_inserted = 0
    file_type_fixed = 3
    extension = "jpg"

    today = datetime.now()
    min_date = today - timedelta(days=30)

    # 루프: batch 단위로 처리
    for batch_start in range(1, total_goods + 1, batch_size):
        batch_end = min(batch_start + batch_size - 1, total_goods)
        current_batch_count = batch_end - batch_start + 1

        # 배치 단위로 faker 재시드 + 새 인스턴스 생성
        Faker.seed(RANDOM_SEED + batch_start)
        faker_instance = Faker()

        # 새 커넥션 열기 (트랜잭션 단위)
        conn = make_connection()
        with conn.cursor() as cur:
            rows = []  # INSERT할 튜플 모음

            for goods_id in range(batch_start, batch_end + 1):
                file_count = random.randint(3, 5)
                for _ in range(file_count):
                    created = faker_instance.date_time_between(start_date=min_date, end_date=today)
                    updated = created
                    file_name = str(uuid.uuid4())
                    url = faker_instance.image_url()
                    user_id = random.randint(1, 1_000_000)

                    rows.append((
                        file_type_fixed,   # file_type
                        created,           # created_at
                        goods_id,          # file_id (goods_id)
                        updated,           # updated_at
                        user_id,           # user_id
                        extension,         # extension
                        file_name,         # file_name
                        url                # url
                    ))

            if rows:
                sql = """
                    INSERT INTO file (
                        file_type,
                        created_at,
                        file_id,
                        updated_at,
                        user_id,
                        extension,
                        file_name,
                        url
                    ) VALUES %s
                """
                # execute_values로 한 번에 삽입 (page_size는 적절히 조정 가능)
                execute_values(cur, sql, rows, page_size=1000)

            conn.commit()
            total_inserted += len(rows)

        # 커넥션은 with 구문 벗어나기 전에 닫히므로 안전하게 닫힘
        conn.close()

        print(f"▶ 처리: goods {batch_start}-{batch_end} ({current_batch_count}개) / 누적 파일 {total_inserted}")

        # 메모리 해제, 가비지 콜렉트
        del faker_instance
        del rows
        gc.collect()

    print(f"=== File 데이터 생성 완료! 총 삽입된 파일: {total_inserted}개 ===")


if __name__ == "__main__":      
#     generate_and_insert_goods(
#         total_count=5_000_00,
#         commit_unit=50000,
#         chunk=1000
#     )
    generate_and_insert_auction(
        total_count=5_000_00,
        chunk=50000
    )
    generate_and_insert_files(
        total_goods=5_000_00,
        batch_size=10000
    )