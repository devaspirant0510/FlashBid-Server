package seoil.capstone.flashbid.global.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuctionStatus implements CodeEnum {
    BEFORE_START(0),
    IN_PROGRESS(1),
    ENDED(2);
    private final int code;


}
