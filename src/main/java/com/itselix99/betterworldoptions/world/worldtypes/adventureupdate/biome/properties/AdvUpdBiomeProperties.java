package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.BiomeProperties;
import com.itselix99.betterworldoptions.world.feature.FeatureDecorator;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;

import java.util.*;

public class AdvUpdBiomeProperties extends BiomeProperties {
    public static final Biome[] BY_ID = new Biome[256];
    protected final Map<Biome, Integer> biomeId = new HashMap<>();
    protected final Map<Biome, Integer> grassColor = new HashMap<>();
    protected final Map<Biome, Integer> foliageColor = new HashMap<>();
    protected final Map<Biome, float[]> height = new HashMap<>();
    protected final Map<Biome, FeatureDecorator> decorator = new HashMap<>();
    protected final Map<Biome, Boolean> hasSnow = new HashMap<>();
    protected final Map<Biome, Boolean> canRain = new HashMap<>();

    public static Biome OCEAN;
    public static Biome EXTREME_HILLS;
    public static Biome RIVER;

    public AdvUpdBiomeProperties(Identifier biomePropertiesId) {
        super(biomePropertiesId);
    }

    public AdvUpdBiomeProperties() {
        super();
        this.clearBaseBiomes();

        FeatureDecorator featureDecorator;

        OCEAN = BetterWorldOptions.OCEAN;
        this.initBiome(OCEAN, 0, OCEAN.canSnow(), OCEAN.canRain(), true);
        this.setGrassColor(OCEAN, 112);
        this.setHeight(OCEAN, -1.0F, 0.5F);

        this.initBiome(Biome.PLAINS, 1, Biome.PLAINS.canSnow(), Biome.PLAINS.canRain(), true);
        this.setGrassColor(Biome.PLAINS, 9286496);
        this.setTemperatureAndDownfall(Biome.PLAINS, 0.8F, 0.4F);
        featureDecorator = this.getFeatureDecorator(Biome.PLAINS);
        featureDecorator.treeAttempts = -999;
        featureDecorator.flowerAttempts = 4;
        featureDecorator.grassAttempts = 10;

        this.initBiome(Biome.DESERT, 2, Biome.DESERT.canSnow(), Biome.DESERT.canRain(), true);
        this.setGrassColor(Biome.DESERT, Biome.DESERT.grassColor);
        this.setTemperatureAndDownfall(Biome.DESERT, 2.0F, 0.0F);
        this.setHeight(Biome.DESERT, 0.1F, 0.2F);
        featureDecorator = this.getFeatureDecorator(Biome.DESERT);
        featureDecorator.treeAttempts = -999;
        featureDecorator.deadBushAttempts = 2;
        featureDecorator.sugarcaneAttempts = 50;
        featureDecorator.cactusAttempts = 10;

        EXTREME_HILLS = BetterWorldOptions.EXTREME_HILLS;
        this.initBiome(EXTREME_HILLS, 3, EXTREME_HILLS.canSnow(), EXTREME_HILLS.canRain(), true);
        this.setGrassColor(EXTREME_HILLS, 6316128);
        this.setHeight(EXTREME_HILLS, 0.2F, 1.8F);
        this.setTemperatureAndDownfall(EXTREME_HILLS, 0.2F, 0.3F);

        this.initBiome(Biome.FOREST, 4, Biome.FOREST.canSnow(), Biome.FOREST.canRain(), true);
        this.setGrassColor(Biome.FOREST, Biome.FOREST.grassColor);
        this.setFoliageColor(Biome.FOREST, Biome.FOREST.foliageColor);
        this.setTemperatureAndDownfall(Biome.FOREST, 0.7F, 0.8F);
        featureDecorator = this.getFeatureDecorator(Biome.FOREST);
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 2;

        this.initBiome(Biome.TAIGA, 5, false, Biome.TAIGA.canRain(), true);
        this.setGrassColor(Biome.TAIGA, 747097);
        this.setFoliageColor(Biome.TAIGA, 5159473);
        this.setTemperatureAndDownfall(Biome.TAIGA, 0.3F, 0.8F);
        this.setHeight(Biome.TAIGA, 0.1F, 0.4F);
        featureDecorator = this.getFeatureDecorator(Biome.TAIGA);
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 1;

        this.initBiome(Biome.SWAMPLAND, 6, Biome.SWAMPLAND.canSnow(), Biome.SWAMPLAND.canRain(), true);
        this.setGrassColor(Biome.SWAMPLAND, Biome.SWAMPLAND.grassColor);
        this.setFoliageColor(Biome.SWAMPLAND, Biome.SWAMPLAND.foliageColor);
        this.setTemperatureAndDownfall(Biome.SWAMPLAND, 0.8F, 0.9F);
        this.setHeight(Biome.SWAMPLAND, -0.2F, 0.1F);
        featureDecorator = this.getFeatureDecorator(Biome.SWAMPLAND);
        featureDecorator.treeAttempts = 2;
        featureDecorator.flowerAttempts = -999;
        featureDecorator.deadBushAttempts = 1;
        featureDecorator.mushroomAttempts = 8;
        featureDecorator.sugarcaneAttempts = 10;
        featureDecorator.clayPatchAttempts = 1;
        featureDecorator.lilyPadAttempts = 4;

        RIVER = BetterWorldOptions.RIVER;
        this.initBiome(RIVER, 7, RIVER.canSnow(), RIVER.canRain(), true);
        this.setGrassColor(RIVER, 255);
        this.setHeight(RIVER, -0.5F, 0.0F);

        this.initBiome(Biome.HELL, 8, Biome.HELL.canSnow(), Biome.HELL.canRain(), false);
        this.setGrassColor(Biome.HELL, Biome.HELL.grassColor);

        this.initBiome(Biome.SKY, 9, Biome.SKY.canSnow(), Biome.SKY.canRain(), false);
        this.setGrassColor(Biome.SKY, Biome.SKY.grassColor);

        this.initModdedBiome();
    }

    protected void initBiome(Biome biome, int id, boolean snow, boolean rain, boolean baseBiome) {
        BY_ID[id] = biome;
        this.biomeId.put(biome, id);
        this.foliageColor.put(biome, 5169201);
        this.height.put(biome, new float[]{0.1F, 0.3F});
        this.tempAndDownfall.put(biome, new float[]{0.5F, 0.5F});
        this.decorator.put(biome, new FeatureDecorator(biome));
        this.hasSnow.put(biome, snow);
        this.canRain.put(biome, rain);
        if (baseBiome) {
            this.baseBiomes.add(biome);
            snowBaseBiomes.get(this.getId()).put(biome, snow);
            precipitationBaseBiomes.get(this.getId()).put(biome, snow);
        }
    }

    protected void initModdedBiome() {
        for (Biome moddedBiome : this.moddedBiomes) {
            this.initBiome(moddedBiome, this.biomeId.size(), moddedBiome.canSnow(), moddedBiome.canRain(), false);
            this.setGrassColor(moddedBiome, moddedBiome.grassColor);
            this.setFoliageColor(moddedBiome, moddedBiome.foliageColor);
            if (moddedBiome.canSnow()) {
                this.setTemperatureAndDownfall(moddedBiome, 0.0F, 0.5F);
            }
        }
    }

    public void setGrassColor(Biome biome, int color) {
        this.grassColor.put(biome, color);
    }

    public void setFoliageColor(Biome biome, int color) {
        this.foliageColor.put(biome, color);
    }

    public void setHeight(Biome biome, float baseHeight, float heightVariation) {
        this.height.put(biome, new float[]{baseHeight, heightVariation});
    }

    public Identifier getId() {
        return BetterWorldOptions.NAMESPACE.id("beta_1.8.1");
    }

    public List<Biome> getAllBiomesForSingleBiome() {
        List<Biome> allBiomes = new ArrayList<>();
        allBiomes.add(BetterWorldOptions.OCEAN);
        allBiomes.add(Biome.PLAINS);
        allBiomes.add(Biome.DESERT);
        allBiomes.add(BetterWorldOptions.EXTREME_HILLS);
        allBiomes.add(Biome.FOREST);
        allBiomes.add(Biome.TAIGA);
        allBiomes.add(Biome.SWAMPLAND);
        allBiomes.add(BetterWorldOptions.RIVER);

        List<Biome> moddedBiomes = new ArrayList<>(BiomeAPI.getOverworldProvider().getBiomes());

        for (Biome biome : moddedBiomes) {
            if (!allBiomes.contains(biome) && !OverworldBiomeProviderImpl.getInstance().getBiomes().contains(biome)) {
                allBiomes.add(biome);
            }
        }
        return allBiomes;
    }

    public int getBiomeId(Biome biome) {
        return this.biomeId.get(biome);
    }

    public float[] getHeight(Biome biome) {
        return this.height.get(biome);
    }

    public FeatureDecorator getFeatureDecorator(Biome biome) {
        return this.decorator.get(biome);
    }

    public boolean canSnow(Biome biome) {
        return this.hasSnow.get(biome);
    }

    public boolean canRain(Biome biome) {
        return this.canRain.get(biome);
    }

    public int getScaledDownfall(Biome biome) {
        return (int)(this.tempAndDownfall.get(biome)[1] * 65536.0F);
    }

    public int getScaledTemperature(Biome biome) {
        return (int)(this.tempAndDownfall.get(biome)[0] * 65536.0F);
    }

    public void decorate(World world, Biome biome, Random random, int x, int z) {
        this.decorator.get(biome).decorate(world, random, x, z);
    }
}