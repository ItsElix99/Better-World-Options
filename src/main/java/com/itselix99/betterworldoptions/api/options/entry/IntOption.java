package com.itselix99.betterworldoptions.api.options.entry;

import com.itselix99.betterworldoptions.api.options.OptionType;

public class IntOption extends Option<Integer> {
    private final int defaultValue;
    private final int minValue;
    private final int maxValue;
    private int step = 1;

    public IntOption(String displayName, String name, String[] description, OptionType optionType, int defaultValue, int minValue, int maxValue) {
        super(displayName, name, description, optionType);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public IntOption(int id, String displayName, String name, String[] description, OptionType optionType, int defaultValue, int minValue, int maxValue) {
        super(id, displayName, name, description, optionType);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Integer getDefaultValue() {
        return this.defaultValue;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public int getStep() {
        return this.step;
    }
}