alter table auction
    drop column view_count;

create table auction_view_count
(
    auction_id  bigint not null
        primary key
        constraint fk_auction_view_count_auction
            references auction (id),
    view_count  bigint,
    backup_time timestamp(6)
);

alter table auction_stats
    drop column chat_count;

create table auction_chat_count
(
    auction_id bigint not null
        primary key
        constraint fk_auction_view_count_auction
            references auction (id),
    chat_count bigint
);

CREATE TABLE feed_view_count
(
    feed_id     bigint not null
        primary key
        constraint fk_feed_view_count_feed
            references feed (id),
    view_count       BIGINT,
    backup_time TIMESTAMP
);

create table feed_like_count
(
    feed_id    bigint not null
        primary key
        constraint fk_feed_like_count_feed
            references feed (id), -- feed 테이블 ID랑 1:1 매핑!
    like_count integer
);