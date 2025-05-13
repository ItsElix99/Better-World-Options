package com.itselix99.betterworldoptions.world.worldtypes.infdev415.math.noise;

import com.itselix99.betterworldoptions.world.worldtypes.infdev415.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public class PerlinNoiseSamplerInfdev415 extends NoiseSampler {
    private int[] perm;
    public double offsetX;
    public double offsetY;
    public double offsetZ;

    public PerlinNoiseSamplerInfdev415() {
        this(new Random());
    }

    public PerlinNoiseSamplerInfdev415(Random random) {
        this.perm = new int[512];
        this.offsetX = random.nextDouble() * 256.0D;
        this.offsetY = random.nextDouble() * 256.0D;
        this.offsetZ = random.nextDouble() * 256.0D;

        int var2;
        for(var2 = 0; var2 < 256; this.perm[var2] = var2++) {
        }

        for(var2 = 0; var2 < 256; ++var2) {
            int var3 = random.nextInt(256 - var2) + var2;
            int var4 = this.perm[var2];
            this.perm[var2] = this.perm[var3];
            this.perm[var3] = var4;
            this.perm[var2 + 256] = this.perm[var2];
        }

    }

    public double sample(double x, double y, double z) {
        double var7 = x + this.offsetX;
        double var9 = y + this.offsetY;
        double var11 = z + this.offsetZ;
        int var22 = MathHelper.floor_double(var7) & 255;
        int var2 = MathHelper.floor_double(var9) & 255;
        int var23 = MathHelper.floor_double(var11) & 255;
        var7 -= (double) MathHelper.floor_double(var7);
        var9 -= (double) MathHelper.floor_double(var9);
        var11 -= (double) MathHelper.floor_double(var11);
        double var16 = sample(var7);
        double var18 = sample(var9);
        double var20 = sample(var11);
        int var4 = this.perm[var22] + var2;
        int var24 = this.perm[var4] + var23;
        var4 = this.perm[var4 + 1] + var23;
        var22 = this.perm[var22 + 1] + var2;
        var2 = this.perm[var22] + var23;
        var22 = this.perm[var22 + 1] + var23;
        return lerp(var20, lerp(var18, lerp(var16, gradCoord(this.perm[var24], var7, var9, var11), gradCoord(this.perm[var2], var7 - 1.0D, var9, var11)), lerp(var16, gradCoord(this.perm[var4], var7, var9 - 1.0D, var11), gradCoord(this.perm[var22], var7 - 1.0D, var9 - 1.0D, var11))), lerp(var18, lerp(var16, gradCoord(this.perm[var24 + 1], var7, var9, var11 - 1.0D), gradCoord(this.perm[var2 + 1], var7 - 1.0D, var9, var11 - 1.0D)), lerp(var16, gradCoord(this.perm[var4 + 1], var7, var9 - 1.0D, var11 - 1.0D), gradCoord(this.perm[var22 + 1], var7 - 1.0D, var9 - 1.0D, var11 - 1.0D))));
    }
    public double sample(double var0) {
        return var0 * var0 * var0 * (var0 * (var0 * 6.0D - 15.0D) + 10.0D);
    }

    public double lerp(double x, double y, double t) {
        return y + x * (t - y);
    }

    public double gradCoord(int i, double x, double y, double z) {
        int var8 = i & 15;
        double var9 = var8 < 8 ? x : y;
        double var11 = var8 < 4 ? y : (var8 != 12 && var8 != 14 ? z : x);
        return ((var8 & 1) == 0 ? var9 : -var9) + ((var8 & 2) == 0 ? var11 : -var11);
    }

    public double sample(double x, double y) {
        return this.sample(x, y, 0.0D);
    }

    public double create(double x, double y, double z) {
        return this.sample(x, y, z);
    }
}
