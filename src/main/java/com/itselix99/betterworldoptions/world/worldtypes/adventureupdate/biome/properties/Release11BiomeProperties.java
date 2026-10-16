package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.world.feature.FeatureDecorator;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;

import java.util.ArrayList;
import java.util.List;

public class Release11BiomeProperties extends ReleaseBiomeProperties {
    public static Biome BEACH;
    public static Biome DESERT_HILLS;
    public static Biome FOREST_HILLS;
    public static Biome TAIGA_HILLS;
    public static Biome EXTREME_HILLS_EDGE;

    public Release11BiomeProperties(Identifier biomePropertiesId) {
        super(biomePropertiesId);
    }

    public Release11BiomeProperties() {
        super();
        FeatureDecorator featureDecorator;

        this.setHeight(EXTREME_HILLS, 0.2F, 1.3F);
        this.setTemperatureAndDownfall(Biome.TAIGA, 0.05F, 0.8F);
        Biome.TAIGA.setSnow(true);
        snowBaseBiomes.get(this.getId()).put(Biome.TAIGA, true);

        this.setHeight(ICE_MOUNTAINS, 0.2F, 1.2F);

        BEACH = BetterWorldOptions.BEACH;
        this.initBiome(BEACH, 16, BEACH.canSnow(), true);
        this.setHeight(BEACH, 0.0F, 0.1F);
        this.setTemperatureAndDownfall(BEACH, 0.8F, 0.4F);
        featureDecorator = this.getFeatureDecorator(BEACH);
        featureDecorator.treeAttempts = -999;
        featureDecorator.deadBushAttempts = 0;
        featureDecorator.sugarcaneAttempts = 0;
        featureDecorator.cactusAttempts = 0;

        DESERT_HILLS = BetterWorldOptions.DESERT_HILLS;
        this.initBiome(DESERT_HILLS, 17, DESERT_HILLS.canSnow(), true);
        this.setHeight(DESERT_HILLS, 0.2F, 0.7F);
        this.setTemperatureAndDownfall(DESERT_HILLS, 2.0F, 0.0F);
        featureDecorator = this.getFeatureDecorator(DESERT_HILLS);
        featureDecorator.treeAttempts = -999;
        featureDecorator.deadBushAttempts = 2;
        featureDecorator.sugarcaneAttempts = 50;
        featureDecorator.cactusAttempts = 10;

        FOREST_HILLS = BetterWorldOptions.FOREST_HILLS;
        this.initBiome(FOREST_HILLS, 18, FOREST_HILLS.canSnow(), true);
        this.setHeight(FOREST_HILLS, 0.2F, 0.6F);
        this.setTemperatureAndDownfall(FOREST_HILLS, 0.7F, 0.8F);
        featureDecorator = this.getFeatureDecorator(FOREST_HILLS);
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 2;

        TAIGA_HILLS = BetterWorldOptions.TAIGA_HILLS;
        this.initBiome(TAIGA_HILLS, 19, TAIGA_HILLS.canSnow(), true);
        this.setHeight(TAIGA_HILLS, 0.2F, 0.7F);
        this.setTemperatureAndDownfall(TAIGA_HILLS, 0.05F, 0.8F);
        featureDecorator = this.getFeatureDecorator(TAIGA_HILLS);
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 1;

        EXTREME_HILLS_EDGE = BetterWorldOptions.EXTREME_HILLS_EDGE;
        this.initBiome(EXTREME_HILLS_EDGE, 20, EXTREME_HILLS_EDGE.canSnow(), true);
        this.setHeight(EXTREME_HILLS_EDGE, 0.2F, 0.8F);
        this.setTemperatureAndDownfall(EXTREME_HILLS_EDGE, 0.2F, 0.3F);
    }

    public Identifier getId() {
        return BetterWorldOptions.NAMESPACE.id("release_1.1");
    }

    public List<Biome> getAllBiomesForSingleBiome() {
        List<Biome> allBiomes = super.getAllBiomesForSingleBiome();
        List<Biome> moddedBiomes = new ArrayList<>(BiomeAPI.getOverworldProvider().getBiomes());

        for (Biome biome : moddedBiomes) {
            if (allBiomes.contains(biome) && !OverworldBiomeProviderImpl.getInstance().getBiomes().contains(biome)) {
                allBiomes.remove(biome);
            }
        }

        allBiomes.add(BetterWorldOptions.BEACH);
        allBiomes.add(BetterWorldOptions.DESERT_HILLS);
        allBiomes.add(BetterWorldOptions.FOREST_HILLS);
        allBiomes.add(BetterWorldOptions.TAIGA_HILLS);
        allBiomes.add(BetterWorldOptions.EXTREME_HILLS_EDGE);

        for (Biome biome : moddedBiomes) {
            if (!allBiomes.contains(biome) && !OverworldBiomeProviderImpl.getInstance().getBiomes().contains(biome)) {
                allBiomes.add(biome);
            }
        }

        return allBiomes;
    }
}