package com.itselix99.betterworldoptions.world.worldtypes;

import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.Random;

public class FlatChunkGenerator implements ChunkSource {
    private final Random random;
    private final World world;

    public FlatChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
    }

    public void buildTerrain(byte[] blocks) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < 128; ++y) {
                    int blockId;

                    if (y == 0) {
                        blockId = Block.BEDROCK.id;
                    } else if (y <= 60) {
                        blockId = Block.STONE.id;
                    } else if (y <= 63) {
                        blockId = Block.DIRT.id;
                    } else if (y == 64) {
                        blockId = Block.GRASS_BLOCK.id;
                    } else
                        blockId = 0;

                    int index = (x * 16 + z) * 128 + y;
                    blocks[index] = (byte) blockId;
                }
            }
        }
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte['耀'];
        Chunk var4 = new Chunk(this.world, var3, chunkX, chunkZ);
        this.buildTerrain(var3);
        FlattenedChunk flattenedChunk = new FlattenedChunk(world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var4.blocks);
        flattenedChunk.populateHeightMap();
        return flattenedChunk;
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
    }

    public boolean save(boolean saveEntities, LoadingDisplay display) {
        return true;
    }

    public boolean tick() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public String getDebugInfo() {
        return "RandomLevelSource";
    }
}
