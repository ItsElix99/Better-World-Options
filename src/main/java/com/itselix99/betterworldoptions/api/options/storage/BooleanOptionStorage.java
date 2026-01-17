package com.itselix99.betterworldoptions.api.options.storage;

public class BooleanOptionStorage extends OptionStorage {
    public boolean value;

    public BooleanOptionStorage(String name, boolean value) {
        super(name);
        this.value = value;
    }
}