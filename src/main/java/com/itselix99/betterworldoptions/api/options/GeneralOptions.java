package com.itselix99.betterworldoptions.api.options;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;

import java.util.*;

public class GeneralOptions {
    private static final List<Option<?>> generalOptionsList = new ArrayList<>();

    public static List<Option<?>> getGeneralOptionsList() {
        return generalOptionsList;
    }

    public static Option<?> getGeneralOptionByName(String generalOptionName) {
        return getGeneralOptionsList().stream().filter(generalOptions -> generalOptionName.equals(generalOptions.getName())).findFirst().orElse(null);
    }

    public static void addDependentOption(Option<?> parent, Option<?> dependent) {
        parent.addDependentOptions(dependent);
        dependent.setParentOption(parent);
    }

    static {
        StringOption WorldTypeOption = new StringOption("selectWorld.worldtype", "WorldType", null, OptionType.GENERAL_OPTION, WorldType.defaultWorldType.getId().toString());
        WorldTypeOption.setVisible(false);

        BooleanOption Hardcore = new BooleanOption("options.difficulty.hardcore", "Hardcore", null, OptionType.GENERAL_OPTION,false);
        Hardcore.setVisible(false);

        StringOption SingleBiome = new StringOption("selectWorld.singleBiome", "SingleBiome", null, OptionType.GENERAL_OPTION, "Off");
        SingleBiome.setVisible(false);
        SingleBiome.setCompatibleType("Overworld");

        StringOption Theme = new StringOption("selectWorld.theme", "Theme", null, OptionType.GENERAL_OPTION, new ArrayList<>(Arrays.asList("Normal", "Hell", "Paradise", "Woods", "Winter")), 0);
        Theme.setVisible(false);
        Theme.setCompatibleType("Overworld");

        BooleanOption OldFeatures = new BooleanOption("bwoMoreOptions.oldFeatures", "OldFeatures", new String[]{"bwoMoreOptions.oldFeatures.line1", "bwoMoreOptions.oldFeatures.line2"}, OptionType.GENERAL_OPTION, false);
        OldFeatures.setCompatibleType("WorldType");
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.2.0"));
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.1.2_01"));
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100611"));
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100420"));
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100415"));
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("early_infdev"));
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("indev_20100223"));
        OldFeatures.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("mcpe"));
        OldFeatures.addValuesForWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.2.0"), true);

        BooleanOption FiniteWorld = new BooleanOption("bwoMoreOptions.finiteWorld", "FiniteWorld", null, OptionType.GENERAL_OPTION, false);
        FiniteWorld.setVisible(false);
        FiniteWorld.setCompatibleType("Overworld");

        StringOption FiniteWorldType = new StringOption("bwoMoreOptions.finiteWorldType", "FiniteWorldType", null, OptionType.GENERAL_OPTION, new ArrayList<>(Arrays.asList("MCPE", "LCE", "Island")), 0);
        FiniteWorldType.setCompatibleType("Overworld");
        FiniteWorldType.setVisible(false);
        FiniteWorldType.addValuesForWorldType(BetterWorldOptions.NAMESPACE.id("skylands"), new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.addValuesForWorldType(BetterWorldOptions.NAMESPACE.id("flat"), new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.addValuesForWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100415"), new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.addValuesForWorldType(BetterWorldOptions.NAMESPACE.id("early_infdev"), new ArrayList<>(List.of("MCPE")));
        FiniteWorldType.addValuesForWorldType(BetterWorldOptions.NAMESPACE.id("indev_20100223"), new ArrayList<>(Arrays.asList("MCPE", "Custom")));
        addDependentOption(FiniteWorld, FiniteWorldType);

        StringOption Size = new StringOption("bwoMoreOptions.size", "Size", null, OptionType.GENERAL_OPTION, new ArrayList<>(Arrays.asList("Small", "Normal", "Huge", "Classic LCE", "Small LCE", "Medium LCE", "Large LCE")), 1);
        Size.setCompatibleType("Overworld");
        Size.setVisible(false);
        Size.setSave(false);
        addDependentOption(FiniteWorld, Size);

        StringOption Shape = new StringOption("bwoMoreOptions.shape", "Shape", null, OptionType.GENERAL_OPTION, new ArrayList<>(Arrays.asList("Square", "Long")), 0);
        Shape.setCompatibleType("Overworld");
        Shape.setVisible(false);
        Shape.setSave(false);
        addDependentOption(FiniteWorld, Shape);

        IntOption Width = new IntOption("bwoMoreOptions.width", "Width", null, OptionType.GENERAL_OPTION, 256, 64, 10240);
        Width.setStep(64);
        Width.setCompatibleType("Overworld");
        Width.setVisible(false);
        addDependentOption(FiniteWorld, Width);

        IntOption Length = new IntOption("bwoMoreOptions.length", "Length", null, OptionType.GENERAL_OPTION, 256, 64, 10240);
        Length.setStep(64);
        Length.setCompatibleType("Overworld");
        Length.setVisible(false);
        addDependentOption(FiniteWorld, Length);

        BooleanOption Farlands = new BooleanOption("bwoMoreOptions.farlands", "Farlands", null, OptionType.GENERAL_OPTION, false);
        Farlands.setCompatibleType("WorldType");
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("default"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("amplified"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("skylands"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.2.0"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.1.2_01"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100611"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100420"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100415"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("early_infdev"));
        Farlands.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("mcpe"));

        StringOption FarlandsShape = new StringOption("bwoMoreOptions.farlandsShape", "FarlandsShape", null, OptionType.GENERAL_OPTION, new ArrayList<>(Arrays.asList("Linear", "Square")), 0);
        FarlandsShape.setCompatibleType("WorldType");
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("default"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("amplified"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("skylands"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.2.0"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.1.2_01"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100611"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100420"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100415"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("early_infdev"));
        FarlandsShape.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("mcpe"));
        addDependentOption(Farlands, FarlandsShape);

        IntOption FarlandsDistance = new IntOption("bwoMoreOptions.farlandsDistance", "FarlandsDistance", new String[]{"bwoMoreOptions.farlandsDistance.line1", "bwoMoreOptions.farlandsDistance.line2"}, OptionType.GENERAL_OPTION, 8, 2, 32);
        FarlandsDistance.setCompatibleType("WorldType");
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("default"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("amplified"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("skylands"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.2.0"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("alpha_1.1.2_01"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100611"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100420"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("infdev_20100415"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("early_infdev"));
        FarlandsDistance.addCompatibleWorldType(BetterWorldOptions.NAMESPACE.id("mcpe"));
        FarlandsDistance.setStep(2);
        addDependentOption(Farlands, FarlandsDistance);
    }
}