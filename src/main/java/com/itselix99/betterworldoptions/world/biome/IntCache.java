package com.itselix99.betterworldoptions.world.biome;

import java.util.ArrayList;
import java.util.List;

public class IntCache {
    private static int maxSize = 256;
    private static final List<int[]> tcache = new ArrayList<>();
    private static final List<int[]> tallocated = new ArrayList<>();
    private static final List<int[]> cache = new ArrayList<>();
    private static final List<int[]> allocated = new ArrayList<>();

    public static int[] get(int size) {
        if (size <= 256) {
            if (tcache.isEmpty()) {
                int[] var2 = new int[256];
                tallocated.add(var2);
                return var2;
            } else {
                int[] var3 = tcache.remove(tcache.size() - 1);
                tallocated.add(var3);
                return var3;
            }
        } else if (size > maxSize) {
            System.out.println("New max size: " + size);
            maxSize = size;
            cache.clear();
            allocated.clear();
            int[] var4 = new int[maxSize];
            allocated.add(var4);
            return var4;
        } else if (cache.isEmpty()) {
            int[] var5 = new int[maxSize];
            allocated.add(var5);
            return var5;
        } else {
            int[] var6 = cache.remove(cache.size() - 1);
            allocated.add(var6);
            return var6;
        }
    }

    public static void next() {
        if (!cache.isEmpty()) {
            cache.remove(cache.size() - 1);
        }

        if (!tcache.isEmpty()) {
            tcache.remove(tcache.size() - 1);
        }

        cache.addAll(allocated);
        tcache.addAll(tallocated);
        allocated.clear();
        tallocated.clear();
    }
}