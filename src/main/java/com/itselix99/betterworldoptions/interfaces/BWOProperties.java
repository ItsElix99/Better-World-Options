package com.itselix99.betterworldoptions.interfaces;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;

import java.util.Map;

public interface BWOProperties {
    void bwo_setWorldType(String name);
    String bwo_getWorldType();

    void bwo_setHardcore(boolean hardcore);
    boolean bwo_isHardcore();

    String bwo_getSingleBiome();
    String bwo_getTheme();
    boolean bwo_isOldFeatures();

    String bwo_getStringOptionValue(String optionName, OptionType optionType);
    boolean bwo_getBooleanOptionValue(String optionName, OptionType optionType);

    Map<String, OptionStorage> bwo_getOptionsMap(OptionType optionType);
}
