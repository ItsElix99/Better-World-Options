package com.itselix99.betterworldoptions.api.options.storage;

public record OptionStorage<T>(String name, T value) {
    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }
}