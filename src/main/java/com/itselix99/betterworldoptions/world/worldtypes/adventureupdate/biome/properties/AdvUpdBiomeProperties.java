package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.BiomeProperties;
import com.itselix99.betterworldoptions.interfaces.BWOBiome;
import com.itselix99.betterworldoptions.world.feature.FeatureDecorator;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;

import java.util.*;

public class AdvUpdBiomeProperties extends BiomeProperties {
    public static final Biome[] BY_ID = new Biome[256];

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

        OCEAN = this.initBiome(BetterWorldOptions.OCEAN, 0, BetterWorldOptions.OCEAN.canSnow(), true);
        ((BWOBiome) OCEAN).bwo_setHeight(-1.0F, 0.5F);

        this.initBiome(Biome.PLAINS, 1, Biome.PLAINS.canSnow(), true);
        ((BWOBiome) Biome.PLAINS).bwo_setTemperatureAndDownfall(0.8F, 0.4F);
        featureDecorator = ((BWOBiome) Biome.PLAINS).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = -999;
        featureDecorator.flowerAttempts = 4;
        featureDecorator.grassAttempts = 10;

        this.initBiome(Biome.DESERT, 2, Biome.DESERT.canSnow(), true);
        ((BWOBiome) Biome.DESERT).bwo_setTemperatureAndDownfall(2.0F, 0.0F);
        ((BWOBiome) Biome.DESERT).bwo_setHeight(0.1F, 0.2F);
        featureDecorator = ((BWOBiome) Biome.DESERT).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = -999;
        featureDecorator.deadBushAttempts = 2;
        featureDecorator.sugarcaneAttempts = 50;
        featureDecorator.cactusAttempts = 10;

        EXTREME_HILLS = this.initBiome(BetterWorldOptions.EXTREME_HILLS, 3, BetterWorldOptions.EXTREME_HILLS.canSnow(), true);
        ((BWOBiome) EXTREME_HILLS).bwo_setHeight(0.2F, 1.8F);
        ((BWOBiome) EXTREME_HILLS).bwo_setTemperatureAndDownfall(0.2F, 0.3F);

        this.initBiome(Biome.FOREST, 4, Biome.FOREST.canSnow(), true);
        ((BWOBiome) Biome.FOREST).bwo_setTemperatureAndDownfall(0.7F, 0.8F);
        featureDecorator = ((BWOBiome) Biome.FOREST).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 2;

        this.initBiome(Biome.TAIGA, 5, false, true);
        ((BWOBiome) Biome.TAIGA).bwo_setTemperatureAndDownfall(0.3F, 0.8F);
        ((BWOBiome) Biome.TAIGA).bwo_setHeight(0.1F, 0.4F);
        featureDecorator = ((BWOBiome) Biome.TAIGA).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = 10;
        featureDecorator.grassAttempts = 1;

        this.initBiome(Biome.SWAMPLAND, 6, Biome.SWAMPLAND.canSnow(), true);
        ((BWOBiome) Biome.SWAMPLAND).bwo_setTemperatureAndDownfall(0.8F, 0.9F);
        ((BWOBiome) Biome.SWAMPLAND).bwo_setHeight(-0.2F, 0.1F);
        featureDecorator = ((BWOBiome) Biome.SWAMPLAND).bwo_getFeatureDecorator();
        featureDecorator.treeAttempts = 2;
        featureDecorator.flowerAttempts = -999;
        featureDecorator.deadBushAttempts = 1;
        featureDecorator.mushroomAttempts = 8;
        featureDecorator.sugarcaneAttempts = 10;
        featureDecorator.clayPatchAttempts = 1;
        featureDecorator.lilyPadAttempts = 4;

        RIVER = this.initBiome(BetterWorldOptions.RIVER, 7, BetterWorldOptions.RIVER.canSnow(), true);
        ((BWOBiome) RIVER).bwo_setHeight(-0.5F, 0.0F);

        this.initBiome(Biome.HELL, 8, Biome.HELL.canSnow(), false);
        this.initBiome(Biome.SKY, 9, Biome.SKY.canSnow(), false);

        this.initModdedBiome();
    }

    protected Biome initBiome(Biome biome, int id, boolean snow, boolean baseBiome) {
        BY_ID[id] = biome;
        ((BWOBiome) biome).bwo_setBiomeId(id);
        ((BWOBiome) biome).bwo_setHeight(0.1F, 0.3F);
        ((BWOBiome) biome).bwo_setTemperatureAndDownfall(0.5F, 0.5F);
        ((BWOBiome) biome).bwo_setFeatureDecorator(new FeatureDecorator(biome));

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

        return biome;
    }

    protected void initModdedBiome() {
        for (Biome moddedBiome : this.moddedBiomes) {
            Biome biome = this.initBiome(moddedBiome, this.getAllBiomes().size(), moddedBiome.canSnow(), false);
            if (moddedBiome.canSnow()) {
                ((BWOBiome) biome).bwo_setTemperatureAndDownfall(0.0F, 0.5F);
            }
        }
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
}