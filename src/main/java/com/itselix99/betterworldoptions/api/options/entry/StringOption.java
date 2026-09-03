package com.itselix99.betterworldoptions.api.options.entry;

import com.itselix99.betterworldoptions.api.options.OptionType;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringOption extends Option<String> {
    private final String defaultValue;
    private List<String> stringList;
    private int ordinalDefaultValue = 0;
    private final Map<Identifier, List<String>> stringListForWorldType = new HashMap<>();

    public StringOption(String displayName, String name, String[] description, OptionType optionType, String defaultValue) {
        super(displayName, name, description, optionType);
        this.defaultValue = defaultValue;
    }

    public StringOption(int id, String displayName, String name, String[] description, OptionType optionType, String defaultValue) {
        super(id, displayName, name, description, optionType);
        this.defaultValue = defaultValue;
    }

    public StringOption(String displayName, String name, String[] description, OptionType optionType, List<String> stringList, int defaultValue) {
        super(displayName, name, description, optionType);
        this.stringList = stringList;
        this.defaultValue = this.stringList.get(defaultValue);
        this.ordinalDefaultValue = defaultValue;
    }

    public StringOption(int id, String displayName, String name, String[] description, OptionType optionType, List<String> stringList, int defaultValue) {
        super(id, displayName, name, description, optionType);
        this.stringList = stringList;
        this.defaultValue = this.stringList.get(defaultValue);
        this.ordinalDefaultValue = defaultValue;
    }

    public void addValuesForWorldType(Identifier id, List<String> stringList) {
        this.stringListForWorldType.put(id, stringList);
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public List<String> getStringList() {
        return this.stringList;
    }

    public int getOrdinalDefaultValue() {
        return this.ordinalDefaultValue;
    }

    public Map<Identifier, List<String>> getWorldTypeValues() {
        return this.stringListForWorldType;
    }

    public List<String> getValues(Identifier id) {
        return this.stringListForWorldType.getOrDefault(id, this.stringList);
    }
}