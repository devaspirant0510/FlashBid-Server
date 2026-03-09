package com.choing.flashbid.global.base;


public abstract class BaseRedisTemplate {
    public abstract String getKeyFormate();
    public String generateKey(Object... args){
        return getKeyFormate().formatted(args);
    }
}
