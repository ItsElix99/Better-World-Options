package com.itselix99.betterworldoptions.api.worldtype;

import net.minecraft.world.biome.Biome;

import java.util.function.Supplier;

public class OldFeaturesProperties {
    public Supplier<Biome> oldFeaturesBiomeSupplier;
    public boolean oldFeaturesHasVanillaBiomes;
    public int defaultSkyColor;
    public int defaultFogColor;
    public boolean oldStars;
    public boolean sunriseAndSunsetColors;

    public OldFeaturesProperties(Supplier<Biome> biomeSupplier, boolean oldFeaturesHasVanillaBiomes, int defaultSkyColor, int defaultFogColor, boolean oldStars, boolean sunriseAndSunsetColors) {
        this.oldFeaturesBiomeSupplier = biomeSupplier;
        this.oldFeaturesHasVanillaBiomes = oldFeaturesHasVanillaBiomes;
        this.defaultSkyColor = defaultSkyColor;
        this.defaultFogColor = defaultFogColor;
        this.oldStars = oldStars;
        this.sunriseAndSunsetColors = sunriseAndSunsetColors;
    }
}