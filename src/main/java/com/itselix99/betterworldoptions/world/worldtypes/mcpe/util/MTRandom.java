package com.itselix99.betterworldoptions.world.worldtypes.mcpe.util;

import java.io.Serial;
import java.util.Random;

public class MTRandom extends Random {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = -1727483681;
    private static final int UPPER_MASK = Integer.MIN_VALUE;
    private static final int LOWER_MASK = Integer.MAX_VALUE;
    private static final int[] MAG_01 = new int[]{0, -1727483681};
    private static final double TWO_POW_M32 = 2.3283064365386963E-10D;
    private static final int DEFAULT_SEED = 5489;
    private static final int MAGIC_MASK1 = -1658038656;
    private static final int MAGIC_MASK2 = -272236544;
    private static final int MAGIC_FACTOR1 = 1812433253;
    private final int[] mt;
    private int seed;
    private int mti;
    private boolean haveNextNextGaussian;
    private float nextNextGaussian;
    private int mtiFast;
    private boolean valid;

    public MTRandom() {
        this((new Random()).nextInt());
    }

    public MTRandom(int var1) {
        this.mt = new int[624];
        this.valid = false;
        this.valid = true;
        this.setSeed_p(var1);
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(long var1) {
        if(this.valid) {
            this.setSeed((int)var1);
        }

    }

    public void setSeed(int var1) {
        this.setSeed_p(var1);
    }

    public int nextInt() {
        return this.genRandInt32() >>> 1;
    }

    public int nextInt(int var1) {
        return var1 > 0 ? (int)(Integer.toUnsignedLong(this.genRandInt32()) % (long)var1) : 0;
    }

    public int nextInt(int var1, int var2) {
        return var1 < var2 ? var1 + this.nextInt(var2 - var1) : var1;
    }

    public int nextIntInclusive(int var1, int var2) {
        return this.nextInt(var1, var2 + 1);
    }

    public long nextUnsignedInt() {
        return Integer.toUnsignedLong(this.genRandInt32());
    }

    public short nextUnsignedChar() {
        return (short)(this.genRandInt32() & 255);
    }

    public boolean nextBoolean() {
        return (this.genRandInt32() & Integer.MIN_VALUE) != 0;
    }

    public float nextFloat() {
        return (float)this.genRandReal2();
    }

    public float nextFloat(float var1) {
        return this.nextFloat() * var1;
    }

    public float nextFloat(float var1, float var2) {
        return var1 + this.nextFloat() * (var2 - var1);
    }

    public double nextDouble() {
        return this.genRandReal2();
    }

    public double nextGaussian() {
        if(this.haveNextNextGaussian) {
            this.haveNextNextGaussian = false;
            return (double)this.nextNextGaussian;
        } else {
            float var1;
            float var2;
            float var3;
            do {
                do {
                    var1 = this.nextFloat() * 2.0F - 1.0F;
                    var2 = this.nextFloat() * 2.0F - 1.0F;
                    var3 = var1 * var1 + var2 * var2;
                } while(var3 == 0.0F);
            } while(var3 > 1.0F);

            float var4 = (float)Math.sqrt((double)(-2.0F * (float)Math.log((double)var3) / var3));
            this.nextNextGaussian = var2 * var4;
            this.haveNextNextGaussian = true;
            return (double)(var1 * var4);
        }
    }

    public int nextGaussianInt(int var1) {
        return this.nextInt(var1) - this.nextInt(var1);
    }

    public float nextGaussianFloat() {
        return this.nextFloat() - this.nextFloat();
    }

    protected int next(int var1) {
        return this.genRandInt32() >>> 32 - var1;
    }

    private void setSeed_p(int var1) {
        this.seed = var1;
        this.mti = 625;
        this.haveNextNextGaussian = false;
        this.nextNextGaussian = 0.0F;
        this.initGenRandFast(var1);
    }

    private void initGenRand(int var1) {
        this.mt[0] = var1;

        for(this.mti = 1; this.mti < 624; ++this.mti) {
            this.mt[this.mti] = 1812433253 * (this.mt[this.mti - 1] >>> 30 ^ this.mt[this.mti - 1]) + this.mti;
        }

        this.mtiFast = 624;
    }

    private void initGenRandFast(int var1) {
        this.mt[0] = var1;

        for(this.mtiFast = 1; this.mtiFast <= 397; ++this.mtiFast) {
            this.mt[this.mtiFast] = 1812433253 * (this.mt[this.mtiFast - 1] >>> 30 ^ this.mt[this.mtiFast - 1]) + this.mtiFast;
        }

        this.mti = 624;
    }

    private int genRandInt32() {
        if(this.mti == 624) {
            this.mti = 0;
        } else if(this.mti > 624) {
            this.initGenRand(5489);
            this.mti = 0;
        }

        if(this.mti >= 227) {
            if(this.mti >= 623) {
                this.mt[623] = MAG_01[this.mt[0] & 1] ^ (this.mt[0] & Integer.MAX_VALUE | this.mt[623] & Integer.MIN_VALUE) >>> 1 ^ this.mt[396];
            } else {
                this.mt[this.mti] = MAG_01[this.mt[this.mti + 1] & 1] ^ (this.mt[this.mti + 1] & Integer.MAX_VALUE | this.mt[this.mti] & Integer.MIN_VALUE) >>> 1 ^ this.mt[this.mti - 227];
            }
        } else {
            this.mt[this.mti] = MAG_01[this.mt[this.mti + 1] & 1] ^ (this.mt[this.mti + 1] & Integer.MAX_VALUE | this.mt[this.mti] & Integer.MIN_VALUE) >>> 1 ^ this.mt[this.mti + 397];
            if(this.mtiFast < 624) {
                this.mt[this.mtiFast] = 1812433253 * (this.mt[this.mtiFast - 1] >>> 30 ^ this.mt[this.mtiFast - 1]) + this.mtiFast;
                ++this.mtiFast;
            }
        }

        int var1 = this.mt[this.mti++];
        var1 = (var1 ^ var1 >>> 11) << 7 & -1658038656 ^ var1 ^ var1 >>> 11;
        var1 = var1 << 15 & -272236544 ^ var1 ^ (var1 << 15 & -272236544 ^ var1) >>> 18;
        return var1;
    }

    private double genRandReal2() {
        return (double)Integer.toUnsignedLong(this.genRandInt32()) * (double)2.3283064E-10F;
    }
}