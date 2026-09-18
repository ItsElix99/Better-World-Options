package com.itselix99.betterworldoptions.interfaces;

import net.minecraft.world.chunk.Chunk;

public interface BWOMultiplayerChunk {
    Chunk bwo_loadFiniteWorldLimitChunk(int chunkX, int chunkZ);
}