package com.itselix99.betterworldoptions.api.options.entry;

import com.itselix99.betterworldoptions.api.options.OptionType;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class BooleanOption extends Option<Boolean> {
    private final boolean defaultValue;
    private final Map<Identifier, Boolean> defaultValueForWorldType = new HashMap<>();

    public BooleanOption(String displayName, String name, String[] description, OptionType optionType, boolean defaultValue) {
        super(displayName, name, description, optionType);
        this.defaultValue = defaultValue;
    }

    public BooleanOption(int id, String displayName, String name, String[] description, OptionType optionType, boolean defaultValue) {
        super(id, displayName, name, description, optionType);
        this.defaultValue = defaultValue;
    }

    public void addValuesForWorldType(Identifier id, boolean value) {
        this.defaultValueForWorldType.put(id, value);
    }

    public Boolean getDefaultValue() {
        return this.defaultValue;
    }

    public Map<Identifier, Boolean> getWorldTypeValues() {
        return this.defaultValueForWorldType;
    }

    public boolean getValues(Identifier id) {
        return this.defaultValueForWorldType.getOrDefault(id, this.defaultValue);
    }
}