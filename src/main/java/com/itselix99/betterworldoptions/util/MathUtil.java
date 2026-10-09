package com.itselix99.betterworldoptions.util;

import java.util.Random;

public class MathUtil {
    public static long lfloor(double x) {
        long var1 = (long)x;
        return x < (double)var1 ? var1 - 1L : var1;
    }

    public static int nextInt(Random random, int min, int max) {
        return min >= max ? min : random.nextInt(max - min + 1) + min;
    }
}