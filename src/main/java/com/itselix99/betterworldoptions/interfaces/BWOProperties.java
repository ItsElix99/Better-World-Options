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

    <T> T bwo_getOptionValue(String optionName, OptionType optionType, T fallback);

    void bwo_setPregeneratingFiniteWorld(boolean isDone);
    boolean bwo_isPregeneratingFiniteWorld();

    Map<String, OptionStorage<?>> bwo_getOptionsMap(OptionType optionType);
}
