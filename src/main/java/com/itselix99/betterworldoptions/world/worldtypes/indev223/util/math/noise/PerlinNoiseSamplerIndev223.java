package com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public final class PerlinNoiseSamplerIndev223 extends NoiseSamplerIndev223 {
    private final int[] perm;

    public PerlinNoiseSamplerIndev223() {
        this(new Random());
    }

    public PerlinNoiseSamplerIndev223(Random random) {
        this.perm = new int[512];

        for (int i = 0; i < 256; this.perm[i] = i++) {
        }

        for (int var5 = 0; var5 < 256; ++var5) {
            int j = random.nextInt(256 - var5) + var5;
            int k = this.perm[var5];
            this.perm[var5] = this.perm[j];
            this.perm[j] = k;
            this.perm[var5 + 256] = this.perm[var5];
        }

    }

    private static double smootherstep(double x) {
        return x * x * x * (x * (x * (double) 6.0F - (double) 15.0F) + (double) 10.0F);
    }

    private static double lerp(double delta, double min, double max) {
        return min + delta * (max - min);
    }

    private static double gradCoord(int hash, double x, double y, double z) {
        int var12;
        double d = (var12 = hash & 15) < 8 ? x : y;
        double e = var12 < 4 ? y : (var12 != 12 && var12 != 14 ? z : x);
        return ((var12 & 1) == 0 ? d : -d) + ((var12 & 2) == 0 ? e : -e);
    }

    public double create(double x, double y) {
        double f;
        double e = y;
        int var2 = MathHelper.floor(x) & 255;
        int var23 = MathHelper.floor(y) & 255;
        int var4 = MathHelper.floor((double) 0.0F) & 255;
        double d = x - (double) MathHelper.floor(x);
        e -= MathHelper.floor(e);
        f = (double) 0.0F - (double) MathHelper.floor((double) 0.0F);
        double g = smootherstep(d);
        double h = smootherstep(e);
        double i10 = smootherstep(f);
        int i3 = this.perm[var2] + var23;
        int j = this.perm[i3] + var4;
        i3 = this.perm[i3 + 1] + var4;
        var2 = this.perm[var2 + 1] + var23;
        var23 = this.perm[var2] + var4;
        var2 = this.perm[var2 + 1] + var4;
        return lerp(i10, lerp(h, lerp(g, gradCoord(this.perm[j], d, e, f), gradCoord(this.perm[var23], d - (double) 1.0F, e, f)), lerp(g, gradCoord(this.perm[i3], d, e - (double) 1.0F, f), gradCoord(this.perm[var2], d - (double) 1.0F, e - (double) 1.0F, f))), lerp(h, lerp(g, gradCoord(this.perm[j + 1], d, e, f - (double) 1.0F), gradCoord(this.perm[var23 + 1], d - (double) 1.0F, e, f - (double) 1.0F)), lerp(g, gradCoord(this.perm[i3 + 1], d, e - (double) 1.0F, f - (double) 1.0F), gradCoord(this.perm[var2 + 1], d - (double) 1.0F, e - (double) 1.0F, f - (double) 1.0F))));
    }
}