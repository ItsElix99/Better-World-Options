package com.itselix99.betterworldoptions.api;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOBiome;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;

import java.util.*;

public class BiomeProperties {
    protected List<Biome> baseBiomes = new ArrayList<>();
    protected List<Biome> moddedBiomes = new ArrayList<>();
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

                ((BWOBiome) moddedBiome).bwo_setTemperatureAndDownfall(moddedBiome.canSnow() ? 0.05F : 0.5F, 0.5F);
            }
        }

        ((BWOBiome) Biome.TUNDRA).bwo_setTemperatureAndDownfall(0.05F, 0.2F);
        ((BWOBiome) Biome.SAVANNA).bwo_setTemperatureAndDownfall(0.7F, 0.1F);
        ((BWOBiome) Biome.DESERT).bwo_setTemperatureAndDownfall(1.0F, 0.05F);
        ((BWOBiome) Biome.SWAMPLAND).bwo_setTemperatureAndDownfall(0.6F, 0.8F);
        ((BWOBiome) Biome.TAIGA).bwo_setTemperatureAndDownfall(0.4F, 0.4F);
        ((BWOBiome) Biome.SHRUBLAND).bwo_setTemperatureAndDownfall(0.8F, 0.3F);
        ((BWOBiome) Biome.FOREST).bwo_setTemperatureAndDownfall(0.8F, 0.6F);
        ((BWOBiome) Biome.PLAINS).bwo_setTemperatureAndDownfall(1.0F, 0.4F);
        ((BWOBiome) Biome.SEASONAL_FOREST).bwo_setTemperatureAndDownfall(1.0F, 0.7F);
        ((BWOBiome) Biome.RAINFOREST).bwo_setTemperatureAndDownfall(1.0F, 0.85F);
        ((BWOBiome) Biome.ICE_DESERT).bwo_setTemperatureAndDownfall(0.05F, 0.05F);
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