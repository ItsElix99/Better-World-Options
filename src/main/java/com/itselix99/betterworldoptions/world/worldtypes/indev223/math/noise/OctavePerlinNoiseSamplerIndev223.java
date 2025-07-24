package com.itselix99.betterworldoptions.world.worldtypes.indev223.math.noise;

import java.util.Random;

public final class OctavePerlinNoiseSamplerIndev223 extends NoiseSamplerIndev223 {
    private PerlinNoiseSamplerIndev223[] octaveSamplers;
    private int octaves;

    public OctavePerlinNoiseSamplerIndev223(Random random, int i) {
        this.octaves = i;
        this.octaveSamplers = new PerlinNoiseSamplerIndev223[i];

        for (int var3 = 0; var3 < i; ++var3) {
            this.octaveSamplers[var3] = new PerlinNoiseSamplerIndev223(random);
        }

    }

    public double create(double x, double y) {
        double var5 = 0.0D;
        double var7 = 1.0D;

        for (int i = 0; i < this.octaves; ++i) {
            var5 += this.octaveSamplers[i].create(x / var7, y / var7) * var7;
            var7 *= 2.0D;
        }

        return var5;
    }
}