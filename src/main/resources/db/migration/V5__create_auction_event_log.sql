create table auction_event_log
(
    id                 bigint
        primary key,
    auction_id         bigint constraint fk_auction_event_log_auction
            references auction,
    auction_event_type smallint     not null,
    event_time         timestamp(6),
    is_processed       boolean,
    created_at         timestamp(6) not null
);