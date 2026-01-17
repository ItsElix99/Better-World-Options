package com.itselix99.betterworldoptions.api.options.storage;

public class StringOptionStorage extends OptionStorage {
    public String value;

    public StringOptionStorage(String name, String value) {
        super(name);
        this.value = value;
    }
}