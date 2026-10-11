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
    protected final Map<Biome, float[]> height = new HashMap<>();
    protected final Map<Biome, FeatureDecorator> decorator = new HashMap<>();

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
        this.initBiome(OCEAN, 0, OCEAN.canSnow(), true);
        this.setHeight(OCEAN, -1.0F, 0.5F);

        this.initBiome(Biome.PLAINS, 1, Biome.PLAINS.canSnow(), true);
        this.setTemperatureAndDownfall(Biome.PLAINS, 0.8F, 0.4F);
        featureDecorator = this.getFeatureDecorator(Biome.PLAINS);
        featureDecorator.treeAttempts = -999;
        featureDecorator.flowerAttempts = 4;
        featureDecorator.grassAttempts = 10;

        this.initBiome(Biome.DESERT, 2, Biome.DESERT.canSnow(), true);
        this.setTemperatureAndDownfall(Biome.DESERT, 2.0F, 0.0F);
        this.setHeight(Biome.DESERT, 0.1F, 0.2F);
        featureDecorator = this.getFeatureDecorator(Biome.DESERT);
        featureDecorator.treeAttempts = -999;
        featureDecorator.deadBushAttempts = 2;
        featureDecorator.sugarcaneAttempts = 50;
        featureDecorator.cactusAttempts = 10;

        EXTREME_HILLS = BetterWorldOptions.EXTREME_HILLS;
        this.initBiome(EXTREME_HILLS, 3, EXTREME_HILLS.canSnow(), true);
        this.setHeight(EXTREME_HILLS, 0.2F, 1.8F);
        this.setTemperatureAndDownfall(EXTREME_HILLS, 0.2F, 0.3F);

        this.initBiome(Biome.FOREST, 4, Biome.FOREST.canSnow(), true);
        this.setTemperatureAndDownfall(Biome.FOREST, 0.7F, 0.8F);
        featureDecorator = this.getFeatureDecorator(Biome.FOREST);
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 2;

        this.initBiome(Biome.TAIGA, 5, false, true);
        this.setTemperatureAndDownfall(Biome.TAIGA, 0.3F, 0.8F);
        this.setHeight(Biome.TAIGA, 0.1F, 0.4F);
        featureDecorator = this.getFeatureDecorator(Biome.TAIGA);
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 1;

        this.initBiome(Biome.SWAMPLAND, 6, Biome.SWAMPLAND.canSnow(), true);
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
        this.initBiome(RIVER, 7, RIVER.canSnow(), true);
        this.setHeight(RIVER, -0.5F, 0.0F);

        this.initBiome(Biome.HELL, 8, Biome.HELL.canSnow(), false);
        this.initBiome(Biome.SKY, 9, Biome.SKY.canSnow(), false);

        this.initModdedBiome();
    }

    protected void initBiome(Biome biome, int id, boolean snow, boolean baseBiome) {
        BY_ID[id] = biome;
        this.biomeId.put(biome, id);
        this.height.put(biome, new float[]{0.1F, 0.3F});
        this.tempAndDownfall.put(biome, new float[]{0.5F, 0.5F});
        this.decorator.put(biome, new FeatureDecorator(biome));

        if (baseBiome) {
            if (OverworldBiomeProviderImpl.getInstance().getBiomes().contains(biome) && biome.canSnow() && !snow) {
                biome.setSnow(false);
            }

            this.baseBiomes.add(biome);

            if (!snowBaseBiomes.get(this.getId()).containsKey(biome)) {
                snowBaseBiomes.get(this.getId()).put(biome, snow);
            }

            if (!precipitationBaseBiomes.get(this.getId()).containsKey(biome)) {
                precipitationBaseBiomes.get(this.getId()).put(biome, biome.canRain());
            }
        }
    }

    protected void initModdedBiome() {
        for (Biome moddedBiome : this.moddedBiomes) {
            this.initBiome(moddedBiome, this.biomeId.size(), moddedBiome.canSnow(), false);
            if (moddedBiome.canSnow()) {
                this.setTemperatureAndDownfall(moddedBiome, 0.0F, 0.5F);
            }
        }
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