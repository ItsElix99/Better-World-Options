package com.itselix99.betterworldoptions.api.options.entry;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;

public class OptionEntry {
    public int id = GeneralOptions.getList().size();
    public String displayName;
    public String name;
    public String[] description;
    public OptionType optionType;
    public boolean visible = true;
    public boolean save = true;
}