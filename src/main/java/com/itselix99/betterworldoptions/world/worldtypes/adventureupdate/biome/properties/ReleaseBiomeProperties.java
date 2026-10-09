package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties;

import com.itselix99.betterworldoptions.BetterWorldOptions;
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

        this.setHeight(OCEAN, -1.0F, 0.4F);

        FROZEN_OCEAN = BetterWorldOptions.FROZEN_OCEAN;
        this.initBiome(FROZEN_OCEAN, 10, FROZEN_OCEAN.canSnow(), FROZEN_OCEAN.canRain(), true);
        this.setGrassColor(FROZEN_OCEAN, 9474208);
        this.setHeight(FROZEN_OCEAN, -1.0F, 0.5F);
        this.setTemperatureAndDownfall(FROZEN_OCEAN, 0.0F, 0.5F);

        FROZEN_RIVER = BetterWorldOptions.FROZEN_RIVER;
        this.initBiome(FROZEN_RIVER, 11, FROZEN_RIVER.canSnow(), FROZEN_RIVER.canRain(), true);
        this.setGrassColor(FROZEN_RIVER, 10526975);
        this.setHeight(FROZEN_RIVER, -0.5F, 0.0F);
        this.setTemperatureAndDownfall(FROZEN_RIVER, 0.0F, 0.5F);

        ICE_PLAINS = BetterWorldOptions.ICE_PLAINS;
        this.initBiome(ICE_PLAINS, 12, ICE_PLAINS.canSnow(), ICE_PLAINS.canRain(), true);
        this.setGrassColor(ICE_PLAINS, 16777215);
        this.setTemperatureAndDownfall(ICE_PLAINS, 0.0F, 0.5F);

        ICE_MOUNTAINS = BetterWorldOptions.ICE_MOUNTAINS;
        this.initBiome(ICE_MOUNTAINS, 13, ICE_MOUNTAINS.canSnow(), ICE_MOUNTAINS.canRain(), true);
        this.setGrassColor(ICE_MOUNTAINS, 10526880);
        this.setHeight(ICE_MOUNTAINS, 0.2F, 1.8F);
        this.setTemperatureAndDownfall(ICE_MOUNTAINS, 0.0F, 0.5F);

        MUSHROOM_ISLAND = BetterWorldOptions.MUSHROOM_ISLAND;
        this.initBiome(MUSHROOM_ISLAND, 14, MUSHROOM_ISLAND.canSnow(), MUSHROOM_ISLAND.canRain(), true);
        this.setGrassColor(MUSHROOM_ISLAND, 16711935);
        this.setHeight(MUSHROOM_ISLAND, 0.2F, 1.0F);
        this.setTemperatureAndDownfall(MUSHROOM_ISLAND, 0.9F, 1.0F);
        featureDecorator = this.getFeatureDecorator(MUSHROOM_ISLAND);
        featureDecorator.treeAttempts = -100;
        featureDecorator.flowerAttempts = -100;
        featureDecorator.grassAttempts = -100;
        featureDecorator.mushroomAttempts = 1;
        featureDecorator.hugeMushroomAttempts = 1;

        MUSHROOM_ISLAND_SHORE = BetterWorldOptions.MUSHROOM_ISLAND_SHORE;
        this.initBiome(MUSHROOM_ISLAND_SHORE, 15, MUSHROOM_ISLAND_SHORE.canSnow(), MUSHROOM_ISLAND_SHORE.canRain(), true);
        this.setGrassColor(MUSHROOM_ISLAND_SHORE, 10486015);
        this.setHeight(MUSHROOM_ISLAND_SHORE, -1.0F, 0.1F);
        this.setTemperatureAndDownfall(MUSHROOM_ISLAND_SHORE, 0.9F, 1.0F);
        featureDecorator = this.getFeatureDecorator(MUSHROOM_ISLAND_SHORE);
        featureDecorator.treeAttempts = -100;
        featureDecorator.flowerAttempts = -100;
        featureDecorator.grassAttempts = -100;
        featureDecorator.mushroomAttempts = 1;
        featureDecorator.hugeMushroomAttempts = 1;

        this.initModdedBiome();
    }

    public Identifier getId() {
        return BetterWorldOptions.NAMESPACE.id("release");
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