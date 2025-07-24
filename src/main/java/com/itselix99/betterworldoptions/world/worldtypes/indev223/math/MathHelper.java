package com.itselix99.betterworldoptions.world.worldtypes.indev223.math;

public final class MathHelper {
    private static float[] SINE_TABLE = new float[65536];

    public static final float sin(float x) {
        return SINE_TABLE[(int)(x * 10430.378F) & '\uffff'];
    }

    public static final float cos(float x) {
        return SINE_TABLE[(int)(x * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static final float sqrt(float x) {
        return (float)Math.sqrt((double)x);
    }

    public static int floor(float x) {
        int i = (int)x;
        return x < (float)i ? i - 1 : i;
    }

    public static int floor(double x) {
        int i = (int)x;
        return x < (double)i ? i - 1 : i;
    }

    public static float abs(float x) {
        return x >= 0.0F ? x : -x;
    }

    static {
        for(int i = 0; i < 65536; ++i) {
            SINE_TABLE[i] = (float)Math.sin((double)i * Math.PI * (double)2.0F / (double)65536.0F);
        }

    }
}