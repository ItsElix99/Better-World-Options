package com.itselix99.betterworldoptions.interfaces;

import net.minecraft.world.chunk.ChunkSource;

import java.util.Random;

public interface BWOWorld {
    Random bwo_setRandomSeed(int x, int z, int seed);
    int bwo_getSurfaceHeight(int x, int z);
    ChunkSource bwo_getChunkGenerator();
}