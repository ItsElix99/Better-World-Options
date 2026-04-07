package com.itselix99.betterworldoptions.interfaces;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public interface BWOWorld {
    Feature bwo_getRandomTreeFeatureMCPE(Random random);

    Chunk bwo_loadFiniteWorldLimitChunk(int chunkX, int chunkZ);
}