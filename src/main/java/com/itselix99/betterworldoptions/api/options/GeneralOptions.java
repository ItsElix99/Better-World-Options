package com.itselix99.betterworldoptions.api.options;

import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;

import java.util.*;

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

    public static StringOptionEntry createStringGeneralOptionWithStringList(String displayName, String name, String[] description, List<String> stringList, int defaultValue) {
        StringOptionEntry stringOption = new StringOptionEntry();
        stringOption.displayName = displayName;
        stringOption.name = name;
        stringOption.description = description;
        stringOption.optionType = OptionType.GENERAL_OPTION;
        stringOption.stringList = stringList;
        stringOption.defaultValue = stringOption.stringList.get(defaultValue);
        stringOption.ordinalDefaultValue = defaultValue;
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

    public static IntOptionEntry createIntGeneralOption(String displayName, String name, String[] description, int defaultValue, int minValue, int maxValue) {
        IntOptionEntry intOption = new IntOptionEntry();
        intOption.displayName = displayName;
        intOption.name = name;
        intOption.description = description;
        intOption.optionType = OptionType.GENERAL_OPTION;
        intOption.defaultValue = defaultValue;
        intOption.minValue = minValue;
        intOption.maxValue = maxValue;
        return intOption;
    }

    public static void addDependentOption(OptionEntry parent, OptionEntry dependent) {
        parent.dependentOptions.add(dependent);
        dependent.parentOption = parent;
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
        SingleBiome.compatibleWorldTypes = Set.of("Overworld");
        GENERAL_OPTIONS_LIST.add(SingleBiome);

        StringOptionEntry Theme = createStringGeneralOptionWithStringList("selectWorld.theme", "Theme", null, new ArrayList<>(Arrays.asList("Normal", "Hell", "Paradise", "Woods", "Winter")), 0);
        Theme.visible = false;
        Theme.compatibleWorldTypes = Set.of("Overworld");
        GENERAL_OPTIONS_LIST.add(Theme);

        BooleanOptionEntry OldFeatures = createBooleanGeneralOption("bwoMoreOptions.oldFeatures", "OldFeatures", new String[]{"bwoMoreOptions.oldFeatures.line1", "bwoMoreOptions.oldFeatures.line2"}, false);
        OldFeatures.compatibleWorldTypes = Set.of("Alpha 1.2.0", "Alpha 1.1.2_01", "Infdev 611", "Infdev 420", "Infdev 415", "Early Infdev", "Indev 223", "MCPE");
        OldFeatures.worldTypeDefaultValue.put("Alpha 1.2.0", true);
        GENERAL_OPTIONS_LIST.add(OldFeatures);

        BooleanOptionEntry FiniteWorld = createBooleanGeneralOption("bwoMoreOptions.finiteWorld", "FiniteWorld", null, false);
        FiniteWorld.compatibleWorldTypes = Set.of("Overworld");
        FiniteWorld.visible = false;
        GENERAL_OPTIONS_LIST.add(FiniteWorld);

        StringOptionEntry FiniteWorldType = createStringGeneralOptionWithStringList("bwoMoreOptions.finiteWorldType", "FiniteWorldType", null, new ArrayList<>(Arrays.asList("MCPE", "LCE", "Island")), 0);
        FiniteWorldType.compatibleWorldTypes = Set.of("Overworld");
        FiniteWorldType.visible = false;
        FiniteWorldType.worldTypeDefaultValue.put("Skylands", new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.worldTypeDefaultValue.put("Flat", new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.worldTypeDefaultValue.put("Infdev 415", new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.worldTypeDefaultValue.put("Early Infdev", new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.worldTypeDefaultValue.put("Indev 223", new ArrayList<>(Arrays.asList("MCPE", "Custom")));
        addDependentOption(FiniteWorld, FiniteWorldType);
        GENERAL_OPTIONS_LIST.add(FiniteWorldType);

        StringOptionEntry Size = createStringGeneralOptionWithStringList("bwoMoreOptions.size", "Size", null, new ArrayList<>(Arrays.asList("Small", "Normal", "Huge", "Classic LCE", "Small LCE", "Medium LCE", "Large LCE")), 1);
        Size.compatibleWorldTypes = Set.of("Overworld");
        Size.visible = false;
        Size.save = false;
        addDependentOption(FiniteWorld, Size);
        GENERAL_OPTIONS_LIST.add(Size);

        StringOptionEntry Shape = createStringGeneralOptionWithStringList("bwoMoreOptions.shape", "Shape", null, new ArrayList<>(Arrays.asList("Square", "Long")), 0);
        Shape.compatibleWorldTypes = Set.of("Overworld");
        Shape.visible = false;
        Shape.save = false;
        addDependentOption(FiniteWorld, Shape);
        GENERAL_OPTIONS_LIST.add(Shape);

        IntOptionEntry Width = createIntGeneralOption("bwoMoreOptions.width", "Width", null, 256, 64, 10240);
        Width.step = 64;
        Width.compatibleWorldTypes = Set.of("Overworld");
        Width.visible = false;
        addDependentOption(FiniteWorld, Width);
        GENERAL_OPTIONS_LIST.add(Width);

        IntOptionEntry Length = createIntGeneralOption("bwoMoreOptions.length", "Length", null, 256, 64, 10240);
        Length.step = 64;
        Length.compatibleWorldTypes = Set.of("Overworld");
        Length.visible = false;
        addDependentOption(FiniteWorld, Length);
        GENERAL_OPTIONS_LIST.add(Length);

        BooleanOptionEntry Farlands = createBooleanGeneralOption("bwoMoreOptions.farlands", "Farlands", null, false);
        Farlands.compatibleWorldTypes = Set.of("Default", "Amplified", "Skylands", "Alpha 1.2.0", "Alpha 1.1.2_01", "Infdev 611", "Infdev 420", "Infdev 415", "Early Infdev", "MCPE");
        GENERAL_OPTIONS_LIST.add(Farlands);

        StringOptionEntry FarlandsShape = createStringGeneralOptionWithStringList("bwoMoreOptions.farlandsShape", "FarlandsShape", null, new ArrayList<>(Arrays.asList("Linear", "Square")), 0);
        FarlandsShape.compatibleWorldTypes = Set.of("Default", "Amplified", "Skylands", "Alpha 1.2.0", "Alpha 1.1.2_01", "Infdev 611", "Infdev 420", "Infdev 415", "Early Infdev", "MCPE");
        addDependentOption(Farlands, FarlandsShape);
        GENERAL_OPTIONS_LIST.add(FarlandsShape);

        IntOptionEntry FarlandsDistance = createIntGeneralOption("bwoMoreOptions.farlandsDistance", "FarlandsDistance", new String[]{"bwoMoreOptions.farlandsDistance.line1", "bwoMoreOptions.farlandsDistance.line2"}, 8, 2, 32);
        FarlandsDistance.step = 2;
        FarlandsDistance.compatibleWorldTypes = Set.of("Default", "Amplified", "Skylands", "Alpha 1.2.0", "Alpha 1.1.2_01", "Infdev 611", "Infdev 420", "Infdev 415", "Early Infdev", "MCPE");
        addDependentOption(Farlands, FarlandsDistance);
        GENERAL_OPTIONS_LIST.add(FarlandsDistance);
    }
}