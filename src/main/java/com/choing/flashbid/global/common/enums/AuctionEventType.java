package com.choing.flashbid.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuctionEventType implements CodeEnum {
    START_EVENT(0),END_EVENT(1);
    private final int code;

}
