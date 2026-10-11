package com.itselix99.betterworldoptions.api;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeProperties {
    protected List<Biome> baseBiomes = new ArrayList<>();
    protected List<Biome> moddedBiomes = new ArrayList<>();
    protected final Map<Biome, float[]> tempAndDownfall = new HashMap<>();
    protected static Map<Identifier, Map<Biome, Boolean>> snowBaseBiomes = new HashMap<>();
    protected static Map<Identifier, Map<Biome, Boolean>> precipitationBaseBiomes = new HashMap<>();
    protected static Map<Biome, Boolean> snowModdedBiomes = new HashMap<>();
    protected static Map<Biome, Boolean> precipitationModdedBiomes = new HashMap<>();

    public BiomeProperties(Identifier biomePropertiesId) {
        snowBaseBiomes.put(biomePropertiesId, new HashMap<>());
        precipitationBaseBiomes.put(biomePropertiesId, new HashMap<>());
    }

    public BiomeProperties() {
        Identifier biomePropertiesId = BetterWorldOptions.NAMESPACE.id("default");

        this.baseBiomes.addAll(OverworldBiomeProviderImpl.getInstance().getBiomes());

        for (Biome baseBiome : this.baseBiomes) {
            if (!snowBaseBiomes.get(biomePropertiesId).containsKey(baseBiome)) {
                snowBaseBiomes.get(biomePropertiesId).put(baseBiome, baseBiome.canSnow());
            }

            if (!precipitationBaseBiomes.get(biomePropertiesId).containsKey(baseBiome)) {
                precipitationBaseBiomes.get(biomePropertiesId).put(baseBiome, baseBiome.canRain());
            }
        }

        for (Biome moddedBiome : BiomeAPI.getOverworldProvider().getBiomes()) {
            if (!OverworldBiomeProviderImpl.getInstance().getBiomes().contains(moddedBiome)) {
                this.moddedBiomes.add(moddedBiome);

                if (!snowModdedBiomes.containsKey(moddedBiome)) {
                    snowModdedBiomes.put(moddedBiome, moddedBiome.canSnow());
                }

                if (!precipitationModdedBiomes.containsKey(moddedBiome)) {
                    precipitationModdedBiomes.put(moddedBiome, moddedBiome.canRain());
                }

                this.setTemperatureAndDownfall(moddedBiome, moddedBiome.canSnow() ? 0.05F : 0.5F, 0.5F);
            }
        }

        this.setTemperatureAndDownfall(Biome.TUNDRA, 0.05F, 0.2F);
        this.setTemperatureAndDownfall(Biome.SAVANNA, 0.7F, 0.1F);
        this.setTemperatureAndDownfall(Biome.DESERT, 1.0F, 0.05F);
        this.setTemperatureAndDownfall(Biome.SWAMPLAND, 0.6F, 0.8F);
        this.setTemperatureAndDownfall(Biome.TAIGA, 0.4F, 0.4F);
        this.setTemperatureAndDownfall(Biome.SHRUBLAND, 0.8F, 0.3F);
        this.setTemperatureAndDownfall(Biome.FOREST, 0.8F, 0.6F);
        this.setTemperatureAndDownfall(Biome.PLAINS, 1.0F, 0.4F);
        this.setTemperatureAndDownfall(Biome.SEASONAL_FOREST, 1.0F, 0.7F);
        this.setTemperatureAndDownfall(Biome.RAINFOREST, 1.0F, 0.85F);
        this.setTemperatureAndDownfall(Biome.ICE_DESERT, 0.05F, 0.05F);
    }

    public void setTemperatureAndDownfall(Biome biome, float temperature, float downfall) {
        this.tempAndDownfall.put(biome, new float[]{temperature, downfall});
    }

    public Identifier getId() {
        return BetterWorldOptions.NAMESPACE.id("default");
    }

    public List<Biome> getBaseBiomes() {
        return this.baseBiomes;
    }

    public List<Biome> getModdedBiomes() {
        return this.moddedBiomes;
    }

    public List<Biome> getAllBiomes() {
        List<Biome> allBiomes = new ArrayList<>();
        allBiomes.addAll(this.baseBiomes);
        allBiomes.addAll(this.moddedBiomes);
        
        return allBiomes;
    }

    public List<Biome> getAllBiomesForSingleBiome() {
        return new ArrayList<>(BiomeAPI.getOverworldProvider().getBiomes());
    }

    public float[] getTemperatureAndDownfall(Biome biome) {
        return this.tempAndDownfall.get(biome);
    }

    public boolean getDefaultSnow(Identifier id, Biome biome) {
        if (snowBaseBiomes.get(id).containsKey(biome)) {
            return snowBaseBiomes.get(id).get(biome);
        }

        return snowModdedBiomes.get(biome);
    }

    public boolean getDefaultPrecipitation(Identifier id, Biome biome) {
        if (precipitationBaseBiomes.get(id).containsKey(biome)) {
            return precipitationBaseBiomes.get(id).get(biome);
        }

        return precipitationModdedBiomes.get(biome);
    }

    protected void clearBaseBiomes() {
        this.baseBiomes.clear();
    }

    public static void resetDefaultSnowAndPrecipitation() {
        OverworldBiomeProviderImpl.getInstance().getBiomes().forEach(biome -> biome.setSnow(snowBaseBiomes.get(BetterWorldOptions.NAMESPACE.id("default")).get(biome)));
        OverworldBiomeProviderImpl.getInstance().getBiomes().forEach(biome -> biome.setPrecipitation(precipitationBaseBiomes.get(BetterWorldOptions.NAMESPACE.id("default")).get(biome)));

        for (Biome biome : BiomeAPI.getOverworldProvider().getBiomes()) {
            if (!OverworldBiomeProviderImpl.getInstance().getBiomes().contains(biome)) {
                biome.setSnow(snowModdedBiomes.get(biome));
                biome.setPrecipitation(precipitationModdedBiomes.get(biome));
            }
        }
    }
}