package com.itselix99.betterworldoptions.api.worldtype;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.world.worldtypes.AltOverworldChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.flat.FlatChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.SkylandsChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.alpha112.Alpha112ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.Alpha120ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.EarlyInfdevChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.flat.biomesource.FlatBiomeSource;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.Indev223ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.Infdev415ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.Infdev420ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev611.Infdev611ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.MCPEChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.biomesource.MCPEBiomeSource;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.NetherChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.chunk.SkyChunkGenerator;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.*;

public class WorldType {
    private static final List<WorldType> worldTypeList = new ArrayList<>();
    public static final WorldType defaultWorldType = new WorldType(BetterWorldOptions.NAMESPACE.id("default"), "Default", "/assets/betterworldoptions/gui/default.png", new String[]{"Minecraft's default world generator"});
    private static final Map<String, String[]> dimensionWorldTypeInfo = new HashMap<>();
    private static final Map<String, String[]> dimensionWorldTypeDesc = new HashMap<>();

    private Map<Integer, Class<? extends ChunkSource>> chunkGenerators = new HashMap<>();
    private final Identifier id;
    private final String name;
    private final String icon;
    private final String[] description;
    private Class<? extends BiomeSource> biomeSource = BiomeSource.class;
    private Map<String, Integer> oldTextures = new HashMap<>();
    private OldFeaturesProperties oldFeaturesProperties;
    private final Map<String, OptionEntry> worldTypeOptions = new LinkedHashMap<>();
    private boolean isDimension;
    private int dimensionId;
    private boolean pregenerateFiniteWorld = false;
    private int blockToSpawn = Block.SAND.id;

    public WorldType(Identifier id, String name, String icon, String[] description) {
        this.chunkGenerators.put(0, OverworldChunkGenerator.class);
        this.chunkGenerators.put(-1, NetherChunkGenerator.class);
        this.chunkGenerators.put(1, SkyChunkGenerator.class);
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.isDimension = false;
        this.dimensionId = 0;
        worldTypeList.add(this);
    }

    public WorldType(Class<? extends ChunkSource> overworldChunkGenerator, Identifier id, String name, String icon, String[] description) {
        this.chunkGenerators.put(0, overworldChunkGenerator);
        this.chunkGenerators.put(-1, NetherChunkGenerator.class);
        this.chunkGenerators.put(1, SkyChunkGenerator.class);
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.isDimension = false;
        this.dimensionId = 0;
        worldTypeList.add(this);
    }

    public WorldType(Map<Integer, Class<? extends ChunkSource>> chunkGenerators, Identifier id, String name, String icon, String[] description) {
        this.chunkGenerators = chunkGenerators;
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.isDimension = false;
        this.dimensionId = 0;
        worldTypeList.add(this);
    }

    public void setBiomeSource(Class<? extends BiomeSource> biomeSource) {
        this.biomeSource = biomeSource;
    }

    public void setOldFeaturesProperties(Biome biome, boolean oldFeaturesHasVanillaBiomes, int defaultSkyColor, int defaultFogColor, boolean oldStars, boolean sunriseAndSunsetColors) {
        this.oldFeaturesProperties = new OldFeaturesProperties(() -> biome, oldFeaturesHasVanillaBiomes, defaultSkyColor, defaultFogColor, oldStars, sunriseAndSunsetColors);
    }

    public void setIsDimension(boolean bl) {
        this.isDimension = bl;
    }

    public void setDimensionId(int id) {
        this.dimensionId = id;
    }

    public void setPregenerateFiniteWorld(boolean bl) {
        this.pregenerateFiniteWorld = bl;
    }

    public void setBlockToSpawn(int blockId) {
        this.blockToSpawn = blockId;
    }

    public Class<? extends ChunkSource> getChunkGenerator(int dimensionId) {
        return this.chunkGenerators.get(dimensionId);
    }

    public Identifier getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public String[] getDescription() {
        return this.description;
    }

    public Class<? extends BiomeSource> getBiomeSource() {
        return this.biomeSource;
    }

    public Map<String, OptionEntry> getWorldTypeOptions() {
        return this.worldTypeOptions;
    }

    public boolean isDimension() {
        return this.isDimension;
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public boolean isPregenerateFiniteWorld() {
        return this.pregenerateFiniteWorld;
    }

    public int getBlockToSpawn() {
        return this.blockToSpawn;
    }

    public static List<WorldType> getWorldTypeList() {
        return worldTypeList;
    }

    public static WorldType getWorldTypeById(Identifier id) {
        return getWorldTypeList().stream().filter(worldType -> id.equals(worldType.id)).findFirst().orElse(defaultWorldType);
    }

    public static int getOldTexture(Identifier worldTypeId, String textureName, int originalTexture) {
        return getWorldTypeById(worldTypeId).oldTextures.getOrDefault(textureName, originalTexture);
    }

    public static OldFeaturesProperties getOldFeaturesProperties(Identifier worldTypeId) {
        return getWorldTypeById(worldTypeId).oldFeaturesProperties;
    }

    public static Map<String, String[]>[] getDimensionWorldTypeInfo() {
        return new Map[]{dimensionWorldTypeInfo, dimensionWorldTypeDesc};
    }

    public void addOldTexture(String name, int textureId) {
        this.oldTextures.put(name, textureId);
    }

    public void addStringWorldTypeOption(String displayName, String name, String[] description, List<String> stringList, int defaultValue) {
        StringOptionEntry stringOption = new StringOptionEntry();
        stringOption.id = this.worldTypeOptions.size();
        stringOption.displayName = displayName;
        stringOption.name = name;
        stringOption.description = description;
        stringOption.optionType = OptionType.WORLD_TYPE_OPTION;
        stringOption.stringList = stringList;
        stringOption.defaultValue = stringOption.stringList.get(defaultValue);
        stringOption.ordinalDefaultValue = defaultValue;
        this.worldTypeOptions.put(stringOption.name, stringOption);
    }

    public void addBooleanWorldTypeOption(String displayName, String name, String[] description, boolean defaultValue) {
        BooleanOptionEntry booleanOption = new BooleanOptionEntry();
        booleanOption.id = this.worldTypeOptions.size();
        booleanOption.displayName = displayName;
        booleanOption.name = name;
        booleanOption.description = description;
        booleanOption.optionType = OptionType.WORLD_TYPE_OPTION;
        booleanOption.defaultValue = defaultValue;
        this.worldTypeOptions.put(booleanOption.name, booleanOption);
    }

    public void addIntWorldTypeOption(String displayName, String name, String[] description, int defaultValue, int minValue, int maxValue) {
        IntOptionEntry intOption = new IntOptionEntry();
        intOption.id = this.worldTypeOptions.size();
        intOption.displayName = displayName;
        intOption.name = name;
        intOption.description = description;
        intOption.optionType = OptionType.WORLD_TYPE_OPTION;
        intOption.defaultValue = defaultValue;
        intOption.minValue = minValue;
        intOption.maxValue = maxValue;
        this.worldTypeOptions.put(intOption.name, intOption);
    }

    public static void addDimensionWorldTypeInfo(Identifier id, String name, String icon, String[] desc) {
        dimensionWorldTypeInfo.put(id.getNamespace().toString(), new String[]{id.getPath(), name, icon});
        dimensionWorldTypeDesc.put(id.getNamespace().toString(), desc);
    }

    static {
        WorldType Amplified = new WorldType(AltOverworldChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("amplified"), "Amplified", "/assets/betterworldoptions/gui/amplified.png", new String[]{"Minecraft's default world generator", "but AMPLIFIED"});

        WorldType Nether = new WorldType(BetterWorldOptions.NAMESPACE.id("nether"), "Nether", "/assets/betterworldoptions/gui/nether.png", new String[]{"Start the world in the Nether", "dimension"});
        Nether.setIsDimension(true);
        Nether.setDimensionId(-1);

        WorldType Skylands = new WorldType(SkylandsChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("skylands"), "Skylands", "/assets/betterworldoptions/gui/skylands.png", new String[]{"Start the world on the floating", "islands"});
        Skylands.addBooleanWorldTypeOption("bwoMoreOptions.skyDimension", "SkyDimension", null, false);
        Skylands.setBlockToSpawn(Block.GRASS_BLOCK.id);

        WorldType Flat = new WorldType(FlatChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("flat"), "Flat", "/assets/betterworldoptions/gui/flat.png", new String[]{"A completely flat world, perfect for", "building"});
        Flat.setBiomeSource(FlatBiomeSource.class);
        Flat.addBooleanWorldTypeOption("bwoMoreOptions.superflat", "Superflat", null, false);
        Flat.setBlockToSpawn(Block.GRASS_BLOCK.id);

        WorldType Alpha120 = new WorldType(Alpha120ChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("alpha_1.2.0"), "Alpha 1.2.0", "/assets/betterworldoptions/gui/alpha_1.2.0.png", new String[]{"Start the world with Alpha 1.2.0", "generation"});
        Alpha120.setOldFeaturesProperties(null, true, -1, -1, true, true);

        WorldType Alpha112 = new WorldType(Alpha112ChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("alpha_1.1.2_01"), "Alpha 1.1.2_01", "/assets/betterworldoptions/gui/alpha_1.1.2_01.png", new String[]{"Start the world with Alpha 1.1.2_01", "generation"});
        Alpha112.setOldFeaturesProperties(BetterWorldOptions.Alpha, false, 8961023, 12638463, true, false);

        WorldType Infdev611 = new WorldType(Infdev611ChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("infdev_20100611"), "Infdev 611", "/assets/betterworldoptions/gui/infdev_20100611.png", new String[]{"Start the world with Infdev 611", "generation"});
        Infdev611.setOldFeaturesProperties(BetterWorldOptions.Infdev, false, 10079487, 11587839, true, false);

        WorldType Infdev420 = new WorldType(Infdev420ChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("infdev_20100420"), "Infdev 420", "/assets/betterworldoptions/gui/infdev_20100420.png", new String[]{"Start the world with Infdev 420", "generation"});
        Infdev420.setOldFeaturesProperties(BetterWorldOptions.Infdev, false, 10079487, 11587839, true, false);

        WorldType Infdev415 = new WorldType(Infdev415ChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("infdev_20100415"), "Infdev 415", "/assets/betterworldoptions/gui/infdev_20100415.png", new String[]{"Start the world with Infdev 415", "generation"});
        Infdev415.setOldFeaturesProperties(BetterWorldOptions.Infdev, false, 10079487, 11587839, true, false);

        WorldType EarlyInfdev = new WorldType(EarlyInfdevChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("early_infdev"), "Early Infdev", "/assets/betterworldoptions/gui/early_infdev.png", new String[]{"Start the world with Infdev 227-325", "generation"});
        EarlyInfdev.setOldFeaturesProperties(BetterWorldOptions.EarlyInfdev, false, 200, 11842815, true, false);
        EarlyInfdev.setBlockToSpawn(Block.GRASS_BLOCK.id);

        WorldType Indev223 = new WorldType(Indev223ChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("indev_20100223"), "Indev 223", "/assets/betterworldoptions/gui/indev_20100223.png", new String[]{"Start the world with Indev 223", "generation"});
        Indev223.setOldFeaturesProperties(BetterWorldOptions.Indev, false, 10079487, 16777215, true, false);
        Indev223.setPregenerateFiniteWorld(true);
        Indev223.addStringWorldTypeOption("bwoMoreOptions.indevWorldType", "IndevWorldType", null, new ArrayList<>(Arrays.asList("Island", "Floating", "Flat", "Inland")), 0);
        Indev223.addBooleanWorldTypeOption("bwoMoreOptions.generateIndevHouse", "GenerateIndevHouse", null, true);
        Indev223.setBlockToSpawn(Block.GRASS_BLOCK.id);

        WorldType MCPE = new WorldType(MCPEChunkGenerator.class, BetterWorldOptions.NAMESPACE.id("mcpe"), "MCPE", "/assets/betterworldoptions/gui/mcpe.png", new String[]{"Start the world with MCPE 0.1.0-0.8.1", "generation"});
        MCPE.setBiomeSource(MCPEBiomeSource.class);
        MCPE.setOldFeaturesProperties(null, true, 2907587, 6731007, false, true);

        addDimensionWorldTypeInfo(Identifier.of(Namespace.of("aether"), "the_aether"), "Aether", "/assets/betterworldoptions/gui/aether.png", new String[]{"Start the world in a hostile paradise"});
    }
}