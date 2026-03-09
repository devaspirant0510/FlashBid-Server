package com.choing.flashbid.domain.auction.dto.response;


import lombok.Getter;

@Getter
public class ViewCountIncreasedDto {
    private boolean increased;

    public static ViewCountIncreasedDto create(boolean increased) {
        ViewCountIncreasedDto result = new ViewCountIncreasedDto();
        result.increased = increased;
        return result;
    }
}
