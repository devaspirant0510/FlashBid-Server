package com.choing.flashbid.global.common.enums;


import java.util.Arrays;

public class EnumConvertor {
    public static <E extends Enum<E> & CodeEnum> E fromCode(Class<E> enumClass, int code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e->e.getCode()==code)
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("Invalid enum code " + code));
    }
}
