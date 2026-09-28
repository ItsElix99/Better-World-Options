package com.itselix99.betterworldoptions.util;

import java.util.Random;

public class MathUtil {
    public static int nextInt(Random random, int min, int max) {
        return min >= max ? min : random.nextInt(max - min + 1) + min;
    }
}