package com.itselix99.betterworldoptions.api.options.storage;

public class IntOptionStorage extends OptionStorage {
    public int value;

    public IntOptionStorage(String name, int value) {
        super(name);
        this.value = value;
    }
}