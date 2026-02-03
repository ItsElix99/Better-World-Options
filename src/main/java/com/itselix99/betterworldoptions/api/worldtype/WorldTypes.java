package com.itselix99.betterworldoptions.api.worldtype;

import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
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
import java.util.LinkedHashMap;
import java.util.List;

public class WorldTypes {
    private static final List<WorldTypeEntry> WORLD_TYPE_LIST = new ArrayList<>();

    public static List<WorldTypeEntry> getList() {
        return WORLD_TYPE_LIST;
    }

    public static WorldTypeEntry getWorldTypeByName(String worldTypeName) {
        return getList().stream().filter(worldTypeEntry -> worldTypeName.equals(worldTypeEntry.name)).toList().get(0);
    }

    public static boolean getWorldTypePropertyValue(String worldTypeName, String propertyName) {
        return getWorldTypeByName(worldTypeName).properties.get(propertyName);
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

    public static StringOptionEntry createStringOptionWithStringList(int id, String displayName, String name, String[] description, List<String> stringList) {
        StringOptionEntry stringOption = new StringOptionEntry();
        stringOption.id = id;
        stringOption.displayName = displayName;
        stringOption.name = name;
        stringOption.description = description;
        stringOption.optionType = OptionType.WORLD_TYPE_OPTION;
        stringOption.stringList = stringList;
        stringOption.defaultValue = stringOption.stringList.get(0);
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

    static {
        WorldTypeEntry Default = createWorldType(null, "Default", "Default", "/assets/betterworldoptions/gui/default.png", new String[]{"Minecraft's default world generator"});
        Default.properties.put("Enable Structures", true);
        Default.properties.put("Enable Single Biome", true);
        Default.properties.put("Enable Themes", true);
        Default.properties.put("Enable Old Features", false);
        Default.properties.put("Old Features Has Biomes", false);
        Default.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(Default);

        WorldTypeEntry Amplified = createWorldType(AltOverworldChunkGenerator.class, "Amplified", "Amplified", "/assets/betterworldoptions/gui/default.png", new String[]{"Minecraft's default world generator", "but AMPLIFIED"});
        Amplified.properties.put("Enable Structures", true);
        Amplified.properties.put("Enable Single Biome", true);
        Amplified.properties.put("Enable Themes", true);
        Amplified.properties.put("Enable Old Features", false);
        Amplified.properties.put("Old Features Has Biomes", false);
        Amplified.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(Amplified);

        WorldTypeEntry Nether = createWorldType(null, "Nether", "Nether", "/assets/betterworldoptions/gui/nether.png", new String[]{"Start the world in the Nether", "dimension"});
        Nether.properties.put("Enable Structures", false);
        Nether.properties.put("Enable Single Biome", false);
        Nether.properties.put("Enable Themes", false);
        Nether.properties.put("Enable Old Features", false);
        Nether.properties.put("Old Features Has Biomes", false);
        Nether.properties.put("Enable Finite World", false);
        WORLD_TYPE_LIST.add(Nether);

        WorldTypeEntry Skylands = createWorldType(SkylandsChunkGenerator.class, "Skylands", "Skylands", "/assets/betterworldoptions/gui/skylands.png", new String[]{"Start the world on the floating", "islands"});
        Skylands.properties.put("Enable Structures", true);
        Skylands.properties.put("Enable Single Biome", true);
        Skylands.properties.put("Enable Themes", true);
        Skylands.properties.put("Enable Old Features", false);
        Skylands.properties.put("Old Features Has Biomes", false);
        Skylands.properties.put("Enable Finite World", true);
        Skylands.worldTypeOptions = new LinkedHashMap<>();
        Skylands.worldTypeOptions.put("Sky Dimension", createBooleanOption(Skylands.worldTypeOptions.size(), "bwoMoreOptions.skyDimension", "SkyDimension", null, false));
        WORLD_TYPE_LIST.add(Skylands);

        WorldTypeEntry Flat = createWorldType(FlatChunkGenerator.class, "Flat", "Flat", "/assets/betterworldoptions/gui/flat.png", new String[]{"A completely flat world, perfect for", "building"});
        Flat.properties.put("Enable Structures", true);
        Flat.properties.put("Enable Single Biome", true);
        Flat.properties.put("Enable Themes", true);
        Flat.properties.put("Enable Old Features", false);
        Flat.properties.put("Old Features Has Biomes", false);
        Flat.properties.put("Enable Finite World", true);
        Flat.worldTypeOptions = new LinkedHashMap<>();
        Flat.worldTypeOptions.put("Superflat", createBooleanOption(Flat.worldTypeOptions.size(), "bwoMoreOptions.superflat", "Superflat", null, false));
        WORLD_TYPE_LIST.add(Flat);

        WorldTypeEntry Alpha120 = createWorldType(Alpha120ChunkGenerator.class, "Alpha 1.2.0", "Alpha 1.2.0", "/assets/betterworldoptions/gui/alpha_1.2.0.png", new String[]{"Start the world with Alpha 1.2.0", "generation"});
        Alpha120.properties.put("Enable Structures", true);
        Alpha120.properties.put("Enable Single Biome", true);
        Alpha120.properties.put("Enable Themes", true);
        Alpha120.properties.put("Enable Old Features", true);
        Alpha120.properties.put("Old Features Has Biomes", true);
        Alpha120.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(Alpha120);

        WorldTypeEntry Alpha112 = createWorldType(Alpha112ChunkGenerator.class, "Alpha 1.1.2_01", "Alpha 1.1.2_01", "/assets/betterworldoptions/gui/alpha_1.1.2_01.png", new String[]{"Start the world with Alpha 1.1.2_01", "generation"});
        Alpha112.properties.put("Enable Structures", true);
        Alpha112.properties.put("Enable Single Biome", true);
        Alpha112.properties.put("Enable Themes", true);
        Alpha112.properties.put("Enable Old Features", true);
        Alpha112.properties.put("Old Features Has Biomes", false);
        Alpha112.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(Alpha112);

        WorldTypeEntry Infdev611 = createWorldType(Infdev611ChunkGenerator.class, "Infdev 20100611", "Infdev 611", "/assets/betterworldoptions/gui/infdev_20100611.png", new String[]{"Start the world with Infdev 611", "generation"});
        Infdev611.properties.put("Enable Structures", true);
        Infdev611.properties.put("Enable Single Biome", true);
        Infdev611.properties.put("Enable Themes", true);
        Infdev611.properties.put("Enable Old Features", true);
        Infdev611.properties.put("Old Features Has Biomes", false);
        Infdev611.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(Infdev611);

        WorldTypeEntry Infdev420 = createWorldType(Infdev420ChunkGenerator.class, "Infdev 20100420", "Infdev 420", "/assets/betterworldoptions/gui/infdev_20100420.png", new String[]{"Start the world with Infdev 420", "generation"});
        Infdev420.properties.put("Enable Structures", true);
        Infdev420.properties.put("Enable Single Biome", true);
        Infdev420.properties.put("Enable Themes", true);
        Infdev420.properties.put("Enable Old Features", true);
        Infdev420.properties.put("Old Features Has Biomes", false);
        Infdev420.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(Infdev420);

        WorldTypeEntry Infdev415 = createWorldType(Infdev415ChunkGenerator.class, "Infdev 20100415", "Infdev 415", "/assets/betterworldoptions/gui/infdev_20100415.png", new String[]{"Start the world with Infdev 415", "generation"});
        Infdev415.properties.put("Enable Structures", true);
        Infdev415.properties.put("Enable Single Biome", true);
        Infdev415.properties.put("Enable Themes", true);
        Infdev415.properties.put("Enable Old Features", true);
        Infdev415.properties.put("Old Features Has Biomes", false);
        Infdev415.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(Infdev415);

        WorldTypeEntry EarlyInfdev = createWorldType(EarlyInfdevChunkGenerator.class, "Early Infdev", "Early Infdev", "/assets/betterworldoptions/gui/early_infdev.png", new String[]{"Start the world with Infdev 227-325", "generation"});
        EarlyInfdev.properties.put("Enable Structures", true);
        EarlyInfdev.properties.put("Enable Single Biome", true);
        EarlyInfdev.properties.put("Enable Themes", true);
        EarlyInfdev.properties.put("Enable Old Features", true);
        EarlyInfdev.properties.put("Old Features Has Biomes", false);
        EarlyInfdev.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(EarlyInfdev);

        WorldTypeEntry Indev223 = createWorldType(Indev223ChunkGenerator.class, "Indev 20100223", "Indev 223", "/assets/betterworldoptions/gui/indev_20100223.png", new String[]{"Start the world with Indev 223", "generation"});
        Indev223.properties.put("Enable Structures", true);
        Indev223.properties.put("Enable Single Biome", true);
        Indev223.properties.put("Enable Themes", true);
        Indev223.properties.put("Enable Old Features", true);
        Indev223.properties.put("Old Features Has Biomes", false);
        Indev223.properties.put("Enable Finite World", true);
        Indev223.worldTypeOptions = new LinkedHashMap<>();
        Indev223.worldTypeOptions.put("IndevWorldType", createStringOptionWithStringList(Indev223.worldTypeOptions.size(), "bwoMoreOptions.indevWorldType", "IndevWorldType", null, new ArrayList<>(Arrays.asList("Island", "Floating", "Flat", "Inland"))));
        Indev223.worldTypeOptions.put("GenerateIndevHouse", createBooleanOption(Indev223.worldTypeOptions.size(), "bwoMoreOptions.generateIndevHouse", "GenerateIndevHouse", null, true));
        WORLD_TYPE_LIST.add(Indev223);

        WorldTypeEntry MCPE = createWorldType(MCPEChunkGenerator.class, "MCPE", "MCPE", "/assets/betterworldoptions/gui/mcpe.png", new String[]{"Start the world with MCPE 0.1.0-0.8.1", "generation"});
        MCPE.properties.put("Enable Structures", true);
        MCPE.properties.put("Enable Single Biome", true);
        MCPE.properties.put("Enable Themes", true);
        MCPE.properties.put("Enable Old Features", true);
        MCPE.properties.put("Old Features Has Biomes", true);
        MCPE.properties.put("Enable Finite World", true);
        WORLD_TYPE_LIST.add(MCPE);

        if (CompatMods.AetherLoaded()) {
            WorldTypeEntry Aether = createWorldType(null, "Aether", "Aether", "/assets/betterworldoptions/gui/aether.png", new String[]{"Start the world in a hostile paradise"});
            Aether.properties.put("Enable Structures", false);
            Aether.properties.put("Enable Single Biome", false);
            Aether.properties.put("Enable Themes", false);
            Aether.properties.put("Enable Old Features", false);
            Aether.properties.put("Old Features Has Biomes", false);
            Aether.properties.put("Enable Finite World", false);
            WORLD_TYPE_LIST.add(Aether);
        }
    }
}