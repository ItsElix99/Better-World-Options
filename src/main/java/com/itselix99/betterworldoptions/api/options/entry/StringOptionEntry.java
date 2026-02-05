package com.itselix99.betterworldoptions.api.options.entry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringOptionEntry extends OptionEntry {
    public String defaultValue;
    public List<String> stringList;
    public int ordinalDefaultValue = 0;
    public Map<String, List<String>> worldTypeDefaultValue = new HashMap<>();
}