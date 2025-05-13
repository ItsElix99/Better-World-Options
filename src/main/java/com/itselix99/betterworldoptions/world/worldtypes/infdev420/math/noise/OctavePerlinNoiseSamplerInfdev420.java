package com.itselix99.betterworldoptions.world.worldtypes.infdev420.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public final class OctavePerlinNoiseSamplerInfdev420 extends NoiseSampler {
    private PerlinNoiseSamplerInfdev420[] octaveSamplers;
    private int octaves;

    public OctavePerlinNoiseSamplerInfdev420(Random random, int i) {
        this.octaves = i;
        this.octaveSamplers = new PerlinNoiseSamplerInfdev420[i];

        for(int var3 = 0; var3 < i; ++var3) {
            this.octaveSamplers[var3] = new PerlinNoiseSamplerInfdev420(random);
        }

    }

    public final double sample(double x, double y) {
        double var5 = 0.0D;
        double var7 = 1.0D;

        for(int var9 = 0; var9 < this.octaves; ++var9) {
            var5 += this.octaveSamplers[var9].sample(x * var7, y * var7) / var7;
            var7 /= 2.0D;
        }

        return var5;
    }

    public final double create(double x, double y, double z) {
        double var7 = 0.0D;
        double var9 = 1.0D;

        for(int var11 = 0; var11 < this.octaves; ++var11) {
            var7 += this.octaveSamplers[var11].sampleD(x * var9, y * var9, z * var9) / var9;
            var9 /= 2.0D;
        }

        return var7;
    }

    public final double[] create(double[] map, int x, int y, int z, int width, int height, int depth, double d, double e, double f) {
        if(map == null) {
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