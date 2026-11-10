package com.itselix99.betterworldoptions.util.noise;

import net.minecraft.util.math.noise.NoiseSampler;

public class BWOOctavePerlinNoiseSamplerDistort extends NoiseSampler {
    private final BWOOctavePerlinNoiseSampler source;
    private final BWOOctavePerlinNoiseSampler distort;

    public BWOOctavePerlinNoiseSamplerDistort(BWOOctavePerlinNoiseSampler source, BWOOctavePerlinNoiseSampler distort) {
        this.source = source;
        this.distort = distort;
    }

    public double sample(double x, double y) {
        return this.source.sampleInverse(x + this.distort.sampleInverse(x, y), y);
    }
}