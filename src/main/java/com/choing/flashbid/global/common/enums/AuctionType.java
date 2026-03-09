package com.choing.flashbid.global.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuctionType implements CodeEnum {
    LIVE(0), BLIND(1);
    private final int code;
}
