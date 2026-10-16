package com.itselix99.betterworldoptions.api.worldtype;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeColorProvider;

public class OldFeaturesProperties {
    public Biome oldBiome;
    public boolean oldFeaturesHasVanillaBiomes;
    public BiomeColorProvider defaultGrassColor;
    public BiomeColorProvider defaultLeavesColor;
    public BiomeColorProvider defaultFogColor;
    public int defaultSkyColor;
    public boolean oldStars;
    public boolean sunriseAndSunsetColors;

    public OldFeaturesProperties(Biome oldBiome, boolean oldFeaturesHasVanillaBiomes, int defaultSkyColor, boolean oldStars, boolean sunriseAndSunsetColors) {
        this.oldBiome = oldBiome;
        this.oldFeaturesHasVanillaBiomes = oldFeaturesHasVanillaBiomes;
        this.defaultGrassColor = oldBiome.getGrassColor();
        this.defaultLeavesColor = oldBiome.getLeavesColor();
        this.defaultFogColor = oldBiome.getFogColor();
        this.defaultSkyColor = defaultSkyColor;
        this.oldStars = oldStars;
        this.sunriseAndSunsetColors = sunriseAndSunsetColors;
    }

    public OldFeaturesProperties(boolean oldFeaturesHasVanillaBiomes, int defaultSkyColor, boolean oldStars, boolean sunriseAndSunsetColors) {
        this.oldFeaturesHasVanillaBiomes = oldFeaturesHasVanillaBiomes;
        this.defaultSkyColor = defaultSkyColor;
        this.oldStars = oldStars;
        this.sunriseAndSunsetColors = sunriseAndSunsetColors;
    }

    public OldFeaturesProperties(boolean oldFeaturesHasVanillaBiomes, int defaultGrassColor, int defaultLeavesColor, int defaultFogColor, int defaultSkyColor, boolean oldStars, boolean sunriseAndSunsetColors) {
        this.oldFeaturesHasVanillaBiomes = oldFeaturesHasVanillaBiomes;
        this.defaultGrassColor = (BiomeSource source, int x, int z) -> defaultGrassColor;
        this.defaultLeavesColor = (BiomeSource source, int x, int z) -> defaultLeavesColor;
        this.defaultFogColor = (BiomeSource source, int x, int z) -> defaultFogColor;
        this.defaultSkyColor = defaultSkyColor;
        this.oldStars = oldStars;
        this.sunriseAndSunsetColors = sunriseAndSunsetColors;
    }
}