package com.itselix99.betterworldoptions.api.worldtype;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.world.worldtypes.AltOverworldChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.FlatChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.SkylandsChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.alpha112.Alpha112ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.Alpha120ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.EarlyInfdevChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.Indev223ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.Infdev415ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.Infdev420ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev611.Infdev611ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.MCPEChunkGenerator;
import net.minecraft.world.chunk.ChunkSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldTypes {
    private static final List<WorldTypeEntry> WORLD_TYPE_LIST = new ArrayList<>();

    public static List<WorldTypeEntry> getList() {
        return WORLD_TYPE_LIST;
    }

    public static WorldTypeEntry getWorldTypeByName(String worldTypeName) {
        return getList().stream().filter(worldTypeEntry -> worldTypeName.equals(worldTypeEntry.name)).toList().get(0);
    }

    public static OldFeaturesProperties getOldFeaturesProperties(String worldTypeName) {
        return getWorldTypeByName(worldTypeName).oldFeaturesProperties;
    }

    public static int getOldTexture(String worldTypeName, String textureName, int originalTexture) {
        return getWorldTypeByName(worldTypeName).oldTextures.getOrDefault(textureName, originalTexture);
    }

    public static WorldTypeEntry createWorldType(Class<? extends ChunkSource> overworldChunkGenerator, String displayName, String name, String icon, String[] description) {
        WorldTypeEntry worldType = new WorldTypeEntry();
        worldType.overworldChunkGenerator = overworldChunkGenerator;
        worldType.displayName = displayName;
        worldType.name = name;
        worldType.icon = icon;
        worldType.description = description;
        return worldType;
    }

    public static StringOptionEntry createStringOptionWithStringList(int id, String displayName, String name, String[] description, List<String> stringList, int defaultValue) {
        StringOptionEntry stringOption = new StringOptionEntry();
        stringOption.id = id;
        stringOption.displayName = displayName;
        stringOption.name = name;
        stringOption.description = description;
        stringOption.optionType = OptionType.WORLD_TYPE_OPTION;
        stringOption.stringList = stringList;
        stringOption.defaultValue = stringOption.stringList.get(defaultValue);
        stringOption.ordinalDefaultValue = defaultValue;
        return stringOption;
    }

    public static BooleanOptionEntry createBooleanOption(int id, String displayName, String name, String[] description, boolean defaultValue) {
        BooleanOptionEntry booleanOption = new BooleanOptionEntry();
        booleanOption.id = id;
        booleanOption.displayName = displayName;
        booleanOption.name = name;
        booleanOption.description = description;
        booleanOption.optionType = OptionType.WORLD_TYPE_OPTION;
        booleanOption.defaultValue = defaultValue;
        return booleanOption;
    }

    public static IntOptionEntry createIntOption(int id, String displayName, String name, String[] description, int defaultValue, int minValue, int maxValue) {
        IntOptionEntry intOption = new IntOptionEntry();
        intOption.id = id;
        intOption.displayName = displayName;
        intOption.name = name;
        intOption.description = description;
        intOption.optionType = OptionType.WORLD_TYPE_OPTION;
        intOption.defaultValue = defaultValue;
        intOption.minValue = minValue;
        intOption.maxValue = maxValue;
        return intOption;
    }

    static {
        WorldTypeEntry Default = createWorldType(null, "Default", "Default", "/assets/betterworldoptions/gui/default.png", new String[]{"Minecraft's default world generator"});
        WORLD_TYPE_LIST.add(Default);

        WorldTypeEntry Amplified = createWorldType(AltOverworldChunkGenerator.class, "Amplified", "Amplified", "/assets/betterworldoptions/gui/default.png", new String[]{"Minecraft's default world generator", "but AMPLIFIED"});
        WORLD_TYPE_LIST.add(Amplified);

        WorldTypeEntry Nether = createWorldType(null, "Nether", "Nether", "/assets/betterworldoptions/gui/nether.png", new String[]{"Start the world in the Nether", "dimension"});
        Nether.isDimension = true;
        WORLD_TYPE_LIST.add(Nether);

        WorldTypeEntry Skylands = createWorldType(SkylandsChunkGenerator.class, "Skylands", "Skylands", "/assets/betterworldoptions/gui/skylands.png", new String[]{"Start the world on the floating", "islands"});
        Skylands.worldTypeOptions.put("Sky Dimension", createBooleanOption(Skylands.worldTypeOptions.size(), "bwoMoreOptions.skyDimension", "SkyDimension", null, false));
        WORLD_TYPE_LIST.add(Skylands);

        WorldTypeEntry Flat = createWorldType(FlatChunkGenerator.class, "Flat", "Flat", "/assets/betterworldoptions/gui/flat.png", new String[]{"A completely flat world, perfect for", "building"});
        Flat.worldTypeOptions.put("Superflat", createBooleanOption(Flat.worldTypeOptions.size(), "bwoMoreOptions.superflat", "Superflat", null, false));
        WORLD_TYPE_LIST.add(Flat);

        WorldTypeEntry Alpha120 = createWorldType(Alpha120ChunkGenerator.class, "Alpha 1.2.0", "Alpha 1.2.0", "/assets/betterworldoptions/gui/alpha_1.2.0.png", new String[]{"Start the world with Alpha 1.2.0", "generation"});
        Alpha120.oldFeaturesProperties = new OldFeaturesProperties(() -> null, true, -1, -1);
        WORLD_TYPE_LIST.add(Alpha120);

        WorldTypeEntry Alpha112 = createWorldType(Alpha112ChunkGenerator.class, "Alpha 1.1.2_01", "Alpha 1.1.2_01", "/assets/betterworldoptions/gui/alpha_1.1.2_01.png", new String[]{"Start the world with Alpha 1.1.2_01", "generation"});
        Alpha112.oldFeaturesProperties = new OldFeaturesProperties(() -> BetterWorldOptions.Alpha, false, 8961023, 12638463);
        WORLD_TYPE_LIST.add(Alpha112);

        WorldTypeEntry Infdev611 = createWorldType(Infdev611ChunkGenerator.class, "Infdev 20100611", "Infdev 611", "/assets/betterworldoptions/gui/infdev_20100611.png", new String[]{"Start the world with Infdev 611", "generation"});
        Infdev611.oldFeaturesProperties = new OldFeaturesProperties(() -> BetterWorldOptions.Infdev, false, 10079487, 11587839);
        WORLD_TYPE_LIST.add(Infdev611);

        WorldTypeEntry Infdev420 = createWorldType(Infdev420ChunkGenerator.class, "Infdev 20100420", "Infdev 420", "/assets/betterworldoptions/gui/infdev_20100420.png", new String[]{"Start the world with Infdev 420", "generation"});
        Infdev420.oldFeaturesProperties = new OldFeaturesProperties(() -> BetterWorldOptions.Infdev, false, 10079487, 11587839);
        WORLD_TYPE_LIST.add(Infdev420);

        WorldTypeEntry Infdev415 = createWorldType(Infdev415ChunkGenerator.class, "Infdev 20100415", "Infdev 415", "/assets/betterworldoptions/gui/infdev_20100415.png", new String[]{"Start the world with Infdev 415", "generation"});
        Infdev415.oldFeaturesProperties = new OldFeaturesProperties(() -> BetterWorldOptions.Infdev, false, 10079487, 11587839);
        WORLD_TYPE_LIST.add(Infdev415);

        WorldTypeEntry EarlyInfdev = createWorldType(EarlyInfdevChunkGenerator.class, "Early Infdev", "Early Infdev", "/assets/betterworldoptions/gui/early_infdev.png", new String[]{"Start the world with Infdev 227-325", "generation"});
        EarlyInfdev.oldFeaturesProperties = new OldFeaturesProperties(() -> BetterWorldOptions.EarlyInfdev, false, 200, 11842815);
        WORLD_TYPE_LIST.add(EarlyInfdev);

        WorldTypeEntry Indev223 = createWorldType(Indev223ChunkGenerator.class, "Indev 20100223", "Indev 223", "/assets/betterworldoptions/gui/indev_20100223.png", new String[]{"Start the world with Indev 223", "generation"});
        Indev223.oldFeaturesProperties = new OldFeaturesProperties(() -> BetterWorldOptions.Indev, false, 10079487, 16777215);
        Indev223.worldTypeOptions.put("IndevWorldType", createStringOptionWithStringList(Indev223.worldTypeOptions.size(), "bwoMoreOptions.indevWorldType", "IndevWorldType", null, new ArrayList<>(Arrays.asList("Island", "Floating", "Flat", "Inland")), 0));
        Indev223.worldTypeOptions.put("GenerateIndevHouse", createBooleanOption(Indev223.worldTypeOptions.size(), "bwoMoreOptions.generateIndevHouse", "GenerateIndevHouse", null, true));
        WORLD_TYPE_LIST.add(Indev223);

        WorldTypeEntry MCPE = createWorldType(MCPEChunkGenerator.class, "MCPE", "MCPE", "/assets/betterworldoptions/gui/mcpe.png", new String[]{"Start the world with MCPE 0.1.0-0.8.1", "generation"});
        MCPE.oldFeaturesProperties = new OldFeaturesProperties(() -> null, true, 2907587, 6731007);
        WORLD_TYPE_LIST.add(MCPE);

        if (CompatMods.AetherLoaded()) {
            WorldTypeEntry Aether = createWorldType(null, "Aether", "Aether", "/assets/betterworldoptions/gui/aether.png", new String[]{"Start the world in a hostile paradise"});
            Aether.isDimension = true;
            WORLD_TYPE_LIST.add(Aether);
        }
    }
}