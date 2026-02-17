package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import net.minecraft.block.Block;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class FlatChunkGenerator extends BWOChunkGenerator {
    public OctavePerlinNoiseSampler forestNoise;
    private Biome[] biomes;
    private final boolean superflat;

    public FlatChunkGenerator(World world, long seed) {
        super(world, seed);
        this.superflat = this.bwoProperties.bwo_getBooleanOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION);
        this.forestNoise = new OctavePerlinNoiseSampler(this.random, 8);
    }

    public void buildTerrain(byte[] blocks, Biome[] biomes) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < Config.BWOConfig.world.worldHeightLimit.getIntValue(); ++y) {
                    int blockId;
                    Biome var1 = biomes[x + z * 16];

                    if (y == 0) {
                        blockId = Block.BEDROCK.id;
                    } else if (y <= 60) {
                        blockId = Block.STONE.id;

                        if (y >= 59 && var1 == Biome.DESERT || var1 == Biome.ICE_DESERT) {
                            blockId = Block.SANDSTONE.id;
                        }
                    } else if (y <= 63) {
                        blockId = var1.soilBlockId;
                    } else if (y == 64) {
                        blockId = this.theme.equals("Hell") ? (byte) (var1.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var1.topBlockId) : var1.topBlockId;
                    } else
                        blockId = 0;

                    int index = (z * 16 + x) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;
                    blocks[index] = (byte) blockId;
                }
            }
        }
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        this.buildTerrain(var3, this.biomes);

        if (this.superflat) {
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (this.superflat) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();

        String limitMode = null;
        if (this.finiteWorldType.equals("MCPE")) {
            limitMode = this.finiteWorldType;
        }

        return this.getLimitChunkFiniteWorld(chunkX, chunkZ, var3, limitMode, flattenedChunk);
    }

    public void decorate(ChunkSource source, int x, int z) {
        super.decorate(source, x, z);
    }
}
