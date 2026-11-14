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

public class ReleaseBiomeProperties extends AdvUpdBiomeProperties {
    public static Biome FROZEN_OCEAN;
    public static Biome FROZEN_RIVER;
    public static Biome ICE_PLAINS;
    public static Biome ICE_MOUNTAINS;
    public static Biome MUSHROOM_ISLAND;
    public static Biome MUSHROOM_ISLAND_SHORE;

    public ReleaseBiomeProperties(Identifier biomePropertiesId) {
        super(biomePropertiesId);
    }

    public ReleaseBiomeProperties() {
        super();
        FeatureDecorator featureDecorator;

        ((BWOBiome) OCEAN).bwo_setHeight(-1.0F, 0.4F);

        FROZEN_OCEAN = this.initBiome(BetterWorldOptions.FROZEN_OCEAN, 10, BetterWorldOptions.FROZEN_OCEAN.canSnow(), true);
        ((BWOBiome) FROZEN_OCEAN).bwo_setHeight(-1.0F, 0.5F);
        ((BWOBiome) FROZEN_OCEAN).bwo_setTemperatureAndDownfall(0.0F, 0.5F);

        FROZEN_RIVER = this.initBiome(BetterWorldOptions.FROZEN_RIVER, 11, BetterWorldOptions.FROZEN_RIVER.canSnow(), true);
        ((BWOBiome) FROZEN_RIVER).bwo_setHeight(-0.5F, 0.0F);
        ((BWOBiome) FROZEN_RIVER).bwo_setTemperatureAndDownfall(0.0F, 0.5F);

        ICE_PLAINS = this.initBiome(BetterWorldOptions.ICE_PLAINS, 12, BetterWorldOptions.ICE_PLAINS.canSnow(), true);
        ((BWOBiome) ICE_PLAINS).bwo_setTemperatureAndDownfall(0.0F, 0.5F);

        ICE_MOUNTAINS = this.initBiome(BetterWorldOptions.ICE_MOUNTAINS, 13, BetterWorldOptions.ICE_MOUNTAINS.canSnow(), true);
        ((BWOBiome) ICE_MOUNTAINS).bwo_setHeight(0.2F, 1.8F);
        ((BWOBiome) ICE_MOUNTAINS).bwo_setTemperatureAndDownfall(0.0F, 0.5F);

        MUSHROOM_ISLAND = this.initBiome(BetterWorldOptions.MUSHROOM_ISLAND, 14, BetterWorldOptions.MUSHROOM_ISLAND.canSnow(), true);
        ((BWOBiome) MUSHROOM_ISLAND).bwo_setHeight(0.2F, 1.0F);
        ((BWOBiome) MUSHROOM_ISLAND).bwo_setTemperatureAndDownfall(0.9F, 1.0F);
        featureDecorator = ((BWOBiome) MUSHROOM_ISLAND).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = -100;
        featureDecorator.flowerAttempts = -100;
        featureDecorator.grassAttempts = -100;
        featureDecorator.mushroomAttempts = 1;
        featureDecorator.hugeMushroomAttempts = 1;

        MUSHROOM_ISLAND_SHORE = this.initBiome(BetterWorldOptions.MUSHROOM_ISLAND_SHORE, 15, BetterWorldOptions.MUSHROOM_ISLAND_SHORE.canSnow(), true);
        ((BWOBiome) MUSHROOM_ISLAND_SHORE).bwo_setHeight(-1.0F, 0.1F);
        ((BWOBiome) MUSHROOM_ISLAND_SHORE).bwo_setTemperatureAndDownfall(0.9F, 1.0F);
        featureDecorator = ((BWOBiome) MUSHROOM_ISLAND_SHORE).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = -100;
        featureDecorator.flowerAttempts = -100;
        featureDecorator.grassAttempts = -100;
        featureDecorator.mushroomAttempts = 1;
        featureDecorator.hugeMushroomAttempts = 1;

        this.initModdedBiome();
    }

    public Identifier getId() {
        return BetterWorldOptions.NAMESPACE.id("release_1.0");
    }

    public List<Biome> getAllBiomesForSingleBiome() {
        List<Biome> allBiomes = super.getAllBiomesForSingleBiome();
        List<Biome> moddedBiomes = new ArrayList<>(BiomeAPI.getOverworldProvider().getBiomes());

        for (Biome biome : moddedBiomes) {
            if (allBiomes.contains(biome) && !OverworldBiomeProviderImpl.getInstance().getBiomes().contains(biome)) {
                allBiomes.remove(biome);
            }
        }

        allBiomes.add(BetterWorldOptions.FROZEN_OCEAN);
        allBiomes.add(BetterWorldOptions.FROZEN_RIVER);
        allBiomes.add(BetterWorldOptions.ICE_PLAINS);
        allBiomes.add(BetterWorldOptions.ICE_MOUNTAINS);
        allBiomes.add(BetterWorldOptions.MUSHROOM_ISLAND);
        allBiomes.add(BetterWorldOptions.MUSHROOM_ISLAND_SHORE);

        for (Biome biome : moddedBiomes) {
            if (!allBiomes.contains(biome) && !OverworldBiomeProviderImpl.getInstance().getBiomes().contains(biome)) {
                allBiomes.add(biome);
            }
        }

        return allBiomes;
    }
}