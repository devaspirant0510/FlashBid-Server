package com.choing.flashbid.global.base;



public interface BaseCounter<ID> {
    long increase(ID auctionId);
    long decrease(ID auctionId);
    long getCount(ID auctionId);
}
