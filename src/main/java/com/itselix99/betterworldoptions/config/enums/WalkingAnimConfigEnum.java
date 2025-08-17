package com.itselix99.betterworldoptions.config.enums;

public enum WalkingAnimConfigEnum {
    DEFAULT("Default"),
    INFDEV("Infdev"),
    CLASSIC("Classic");

    final String stringValue;

    WalkingAnimConfigEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}