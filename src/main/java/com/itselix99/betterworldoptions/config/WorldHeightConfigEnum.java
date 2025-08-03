package com.itselix99.betterworldoptions.config;

public enum WorldHeightConfigEnum {
    H128(128),
    H256(256),
    H512(512);

    final int intValue;

    WorldHeightConfigEnum(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}