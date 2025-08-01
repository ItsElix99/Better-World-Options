package com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.util.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public final class OctavePerlinNoiseSamplerEarlyInfdev extends NoiseSampler {
    private final PerlinNoiseSamplerEarlyInfdev[] octaveSamplers;
    private final int octaves;

    public OctavePerlinNoiseSamplerEarlyInfdev(Random random, int i) {
        this.octaves = i;
        this.octaveSamplers = new PerlinNoiseSamplerEarlyInfdev[i];

        for(int var3 = 0; var3 < i; ++var3) {
            this.octaveSamplers[var3] = new PerlinNoiseSamplerEarlyInfdev(random);
        }

    }

    public double sample(double x, double y) {
        double var5 = 0.0D;
        double var7 = 1.0D;

        for(int var9 = 0; var9 < this.octaves; ++var9) {
            var5 += this.octaveSamplers[var9].sample(x / var7, y / var7) * var7;
            var7 *= 2.0D;
        }

        return var5;
    }

    public double create(double x, double y, double z) {
        double var7 = 0.0D;
        double var9 = 1.0D;

        for(int var11 = 0; var11 < this.octaves; ++var11) {
            var7 += this.octaveSamplers[var11].sampleD(x / var9, 0.0D / var9, z / var9) * var9;
            var9 *= 2.0D;
        }

        return var7;
    }
}