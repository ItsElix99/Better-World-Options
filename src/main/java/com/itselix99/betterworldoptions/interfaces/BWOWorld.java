package com.itselix99.betterworldoptions.interfaces;

import com.itselix99.betterworldoptions.api.BiomeProperties;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import net.minecraft.world.chunk.ChunkSource;

import java.util.Random;

public interface BWOWorld {
    Random bwo_setRandomSeed(int x, int z, int seed);
    int bwo_getSurfaceHeight(int x, int z);
    ChunkSource bwo_getChunkGenerator();
    WorldType bwo_getWorldType();
    Theme bwo_getTheme();
    BiomeProperties bwo_getBiomeProperties();
    int bwo_getSeaLevel();
}