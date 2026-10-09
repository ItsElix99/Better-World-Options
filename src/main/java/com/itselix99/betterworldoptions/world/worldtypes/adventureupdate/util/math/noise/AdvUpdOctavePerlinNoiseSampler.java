package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.util.math.noise;

import com.itselix99.betterworldoptions.util.MathUtil;
import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public class AdvUpdOctavePerlinNoiseSampler extends NoiseSampler {
    private final AdvUpdPerlinNoiseSampler[] octaveSamplers;
    private final int octaves;

    public AdvUpdOctavePerlinNoiseSampler(Random random, int i) {
        this.octaves = i;
        this.octaveSamplers = new AdvUpdPerlinNoiseSampler[i];

        for(int var3 = 0; var3 < i; ++var3) {
            this.octaveSamplers[var3] = new AdvUpdPerlinNoiseSampler(random);
        }

    }

    public double sample(double x, double y) {
        double var5 = 0.0F;
        double var7 = 1.0F;

        for(int var9 = 0; var9 < this.octaves; ++var9) {
            var5 += this.octaveSamplers[var9].sample(x * var7, y * var7) / var7;
            var7 /= 2.0F;
        }

        return var5;
    }

    public double[] create(double[] map, int x, int y, int z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ) {
        if (map == null) {
            map = new double[width * height * depth];
        } else {
            for(int var17 = 0; var17 < map.length; ++var17) {
                map[var17] = 0.0F;
            }
        }

        double var20 = 1.0F;

        for(int var22 = 0; var22 < this.octaves; ++var22) {
            double var23 = (double)x * var20 * scaleX;
            double var25 = (double)y * var20 * scaleY;
            double var27 = (double)z * var20 * scaleZ;
            long var29 = MathUtil.lfloor(var23);
            long var30 = MathUtil.lfloor(var27);
            var23 -= (double)var29;
            var27 -= (double)var30;
            var29 %= 16777216L;
            var30 %= 16777216L;
            var23 += (double)var29;
            var27 += (double)var30;
            this.octaveSamplers[var22].create(map, var23, var25, var27, width, height, depth, scaleX * var20, scaleY * var20, scaleZ * var20, var20);
            var20 /= 2.0F;
        }

        return map;
    }

    public double[] create(double[] values, int x, int z, int sizeX, int sizeZ, double scaleX, double scaleZ, double scaleExponent) {
        return this.create(values, x, 10, z, sizeX, 1, sizeZ, scaleX, 1.0F, scaleZ);
    }
}