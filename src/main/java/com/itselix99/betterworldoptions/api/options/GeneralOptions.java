package com.itselix99.betterworldoptions.api.options;

import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GeneralOptions {
    private static final List<OptionEntry> GENERAL_OPTIONS_LIST = new ArrayList<>();

    public static List<OptionEntry> getList() {
        return GENERAL_OPTIONS_LIST;
    }

    public static OptionEntry getOptionByName(String optionName) {
        return getList().stream().filter(generalOptionsEntry -> optionName.equals(generalOptionsEntry.name)).toList().get(0);
    }

    public static StringOptionEntry createStringGeneralOption(String displayName, String name, String[] description, String defaultValue) {
        StringOptionEntry stringOption = new StringOptionEntry();
        stringOption.displayName = displayName;
        stringOption.name = name;
        stringOption.description = description;
        stringOption.optionType = OptionType.GENERAL_OPTION;
        stringOption.defaultValue = defaultValue;
        return stringOption;
    }

    public static StringOptionEntry createStringGeneralOptionWithStringList(String displayName, String name, String[] description, List<String> stringList) {
        StringOptionEntry stringOption = new StringOptionEntry();
        stringOption.displayName = displayName;
        stringOption.name = name;
        stringOption.description = description;
        stringOption.optionType = OptionType.GENERAL_OPTION;
        stringOption.stringList = stringList;
        stringOption.defaultValue = stringOption.stringList.get(0);
        return stringOption;
    }

    public static BooleanOptionEntry createBooleanGeneralOption(String displayName, String name, String[] description, boolean defaultValue) {
        BooleanOptionEntry booleanOption = new BooleanOptionEntry();
        booleanOption.displayName = displayName;
        booleanOption.name = name;
        booleanOption.description = description;
        booleanOption.optionType = OptionType.GENERAL_OPTION;
        booleanOption.defaultValue = defaultValue;
        return booleanOption;
    }

    static {
        StringOptionEntry WorldType = createStringGeneralOption("selectWorld.worldtype", "WorldType", null, WorldTypes.getList().get(0).name);
        WorldType.visible = false;
        GENERAL_OPTIONS_LIST.add(WorldType);

        BooleanOptionEntry Hardcore = createBooleanGeneralOption("options.difficulty.hardcore", "Hardcore", null, false);
        Hardcore.visible = false;
        GENERAL_OPTIONS_LIST.add(Hardcore);

        StringOptionEntry SingleBiome = createStringGeneralOption("selectWorld.singleBiome", "SingleBiome", null, "Off");
        SingleBiome.visible = false;
        GENERAL_OPTIONS_LIST.add(SingleBiome);

        StringOptionEntry Theme = createStringGeneralOptionWithStringList("selectWorld.theme", "Theme", null, new ArrayList<>(Arrays.asList("Normal", "Hell", "Paradise", "Woods", "Winter")));
        Theme.visible = false;
        GENERAL_OPTIONS_LIST.add(Theme);

        BooleanOptionEntry OldFeatures = createBooleanGeneralOption("selectWorld.oldFeatures", "OldFeatures", new String[]{"selectWorld.oldFeatures.info.line1", "selectWorld.oldFeatures.info.line2"}, false);
        OldFeatures.worldTypeDefaultValue = new HashMap<>();
        OldFeatures.worldTypeDefaultValue.put("Alpha 1.2.0", true);
        GENERAL_OPTIONS_LIST.add(OldFeatures);
    }
}