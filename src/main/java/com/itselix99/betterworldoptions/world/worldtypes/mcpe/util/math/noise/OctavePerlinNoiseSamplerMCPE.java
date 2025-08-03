package com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.PerlinNoiseSampler;

import java.util.Random;

public class OctavePerlinNoiseSamplerMCPE extends NoiseSampler {
    private PerlinNoiseSampler[] octaveSamplers;
    private int octaves;

    public OctavePerlinNoiseSamplerMCPE(Random random, int i) {
        this.octaves = i;
        this.octaveSamplers = new PerlinNoiseSampler[i];

        for(int var3 = 0; var3 < i; ++var3) {
            this.octaveSamplers[var3] = new PerlinNoiseSampler(random);
        }

    }

    public double sample(double x, double y) {
        double var5 = (double)0.0F;
        double var7 = (double)1.0F;

        for(int var9 = 0; var9 < this.octaves; ++var9) {
            var5 += this.octaveSamplers[var9].sample(x * var7, y * var7) / var7;
            var7 /= (double)2.0F;
        }

        return var5;
    }

    public double[] create(double[] map, double x, double y, double z, int width, int height, int depth, double d, double e, double f) {
        if (map == null || map.length < width * height * depth) {
            map = new double[width * height * depth];
        } else {
            for(int var17 = 0; var17 < map.length; ++var17) {
                map[var17] = (double)0.0F;
            }
        }

        double var20 = (double)1.0F;

        for(int var19 = 0; var19 < this.octaves; ++var19) {
            this.octaveSamplers[var19].create(map, x, y, z, width, height, depth, d * var20, e * var20, f * var20, var20);
            var20 /= (double)2.0F;
        }

        return map;
    }

    public double[] create(double[] map, int x, int z, int width, int depth, double d, double e, double f) {
        return this.create(map, (double)x, (double)10.0F, (double)z, width, 1, depth, d, (double)1.0F, e);
    }
}