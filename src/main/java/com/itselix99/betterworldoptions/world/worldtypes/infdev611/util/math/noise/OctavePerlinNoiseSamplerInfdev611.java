package com.itselix99.betterworldoptions.world.worldtypes.infdev611.util.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public final class OctavePerlinNoiseSamplerInfdev611 extends NoiseSampler {
    private PerlinNoiseSamplerInfdev611[] octaveSamplers;
    private int octaves;

    public OctavePerlinNoiseSamplerInfdev611(Random random, int i) {
        this.octaves = i;
        this.octaveSamplers = new PerlinNoiseSamplerInfdev611[i];

        for(int var3 = 0; var3 < i; ++var3) {
            this.octaveSamplers[var3] = new PerlinNoiseSamplerInfdev611(random);
        }

    }

    public double sample(double x, double y) {
        double var5 = 0.0D;
        double var7 = 1.0D;

        for(int i = 0; i < this.octaves; ++i) {
            var5 += this.octaveSamplers[i].sample(x * var7, y * var7) / var7;
            var7 /= 2.0D;
        }

        return var5;
    }

    public double create(double x, double y, double z) {
        double var7 = 0.0D;
        double var9 = 1.0D;

        for(int i = 0; i < this.octaves; ++i) {
            var7 += this.octaveSamplers[i].sampleD(x * var9, y * var9, z * var9) / var9;
            var9 /= 2.0D;
        }

        return var7;
    }

    public double[] create(double[] map, int x, int y, int z, int width, int height, int depth, double d, double e, double f) {
        if (map == null) {
            map = new double[width * height * depth];
        } else {
            for(int var14 = 0; var14 < map.length; ++var14) {
                map[var14] = 0.0D;
            }
        }

        double var17 = 1.0D;

        for(int var16 = 0; var16 < this.octaves; ++var16) {
            this.octaveSamplers[var16].create(map, x, y, z, width, height, depth, d * var17, e * var17, f * var17, var17);
            var17 /= 2.0D;
        }

        return map;
    }
}