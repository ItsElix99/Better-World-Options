package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOBiome;
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

        ((BWOBiome) EXTREME_HILLS).bwo_setHeight(0.2F, 1.3F);
        ((BWOBiome) ICE_MOUNTAINS).bwo_setHeight(0.2F, 1.2F);
        ((BWOBiome) Biome.TAIGA).bwo_setTemperatureAndDownfall(0.05F, 0.8F);
        Biome.TAIGA.setSnow(true);
        snowBaseBiomes.get(this.getId()).put(Biome.TAIGA, true);

        BEACH = this.initBiome(BetterWorldOptions.BEACH, 16, BetterWorldOptions.BEACH.canSnow(), true);
        ((BWOBiome) BEACH).bwo_setHeight(0.0F, 0.1F);
        ((BWOBiome) BEACH).bwo_setTemperatureAndDownfall(0.8F, 0.4F);
        featureDecorator = ((BWOBiome) BEACH).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = -999;
        featureDecorator.deadBushAttempts = 0;
        featureDecorator.sugarcaneAttempts = 0;
        featureDecorator.cactusAttempts = 0;

        DESERT_HILLS = this.initBiome(BetterWorldOptions.DESERT_HILLS, 17, BetterWorldOptions.DESERT_HILLS.canSnow(), true);
        ((BWOBiome) DESERT_HILLS).bwo_setHeight(0.2F, 0.7F);
        ((BWOBiome) DESERT_HILLS).bwo_setTemperatureAndDownfall(2.0F, 0.0F);
        featureDecorator = ((BWOBiome) DESERT_HILLS).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = -999;
        featureDecorator.deadBushAttempts = 2;
        featureDecorator.sugarcaneAttempts = 50;
        featureDecorator.cactusAttempts = 10;

        FOREST_HILLS = this.initBiome(BetterWorldOptions.FOREST_HILLS, 18, BetterWorldOptions.FOREST_HILLS.canSnow(), true);
        ((BWOBiome) FOREST_HILLS).bwo_setHeight(0.2F, 0.6F);
        ((BWOBiome) FOREST_HILLS).bwo_setTemperatureAndDownfall(0.7F, 0.8F);
        featureDecorator = ((BWOBiome) FOREST_HILLS).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 2;

        TAIGA_HILLS = this.initBiome(BetterWorldOptions.TAIGA_HILLS, 19, BetterWorldOptions.TAIGA_HILLS.canSnow(), true);
        ((BWOBiome) TAIGA_HILLS).bwo_setHeight(0.2F, 0.7F);
        ((BWOBiome) TAIGA_HILLS).bwo_setTemperatureAndDownfall(0.05F, 0.8F);
        featureDecorator = ((BWOBiome) TAIGA_HILLS).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 1;

        EXTREME_HILLS_EDGE = this.initBiome(BetterWorldOptions.EXTREME_HILLS_EDGE, 20, BetterWorldOptions.EXTREME_HILLS_EDGE.canSnow(), true);
        ((BWOBiome) EXTREME_HILLS_EDGE).bwo_setHeight(0.2F, 0.8F);
        ((BWOBiome) EXTREME_HILLS_EDGE).bwo_setTemperatureAndDownfall(0.2F, 0.3F);
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