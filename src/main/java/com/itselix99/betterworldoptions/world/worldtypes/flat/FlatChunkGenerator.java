package com.itselix99.betterworldoptions.world.worldtypes.flat;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;

public class FlatChunkGenerator extends BWOChunkGenerator {
    private final boolean superflat;

    public FlatChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.scaleNoise = new BWOOctavePerlinNoiseSampler(this.random, 10, true);
        this.depthNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 8, true);

        this.superflat = this.bwoProperties.bwo_getOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION, false);

        if (!this.finiteWorldType.equals("MCPE")) {
            this.finiteWorldType = null;
        }
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                Biome biome = this.getBiome(this.biomes, x, z);

                for (int y = this.world.getBottomY(); y < this.world.getTopY(); ++y) {
                    int blockId;

                    if (y == 0) {
                        blockId = !this.superflat ? Block.BEDROCK.id : Block.STONE.id;
                    } else if (y <= 60) {
                        blockId = Block.STONE.id;
                    } else if (y <= 63) {
                        blockId = !this.superflat ? (this.theme.getSoilBlock() != -1 ? (biome.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : biome.soilBlockId) : biome.soilBlockId) : Block.STONE.id;
                    } else if (y == 64) {
                        blockId = !this.superflat ? (this.theme.getTopBlock() != -1 ? (biome.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : biome.topBlockId) : biome.topBlockId) : Block.STONE.id;
                    } else {
                        blockId = 0;
                    }

                    int index = this.getBlocksIndex(x, z, y);
                    blocks[index] = (byte) blockId;
                }
            }
        }
    }

    protected void initBuffers(int chunkX, int chunkZ, double scale) {
        if (!Config.BWOConfig.world.beachFix) {
            this.depthBuffer = this.perlinNoise3.generateNoise(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, scale * 2.0D, scale * 2.0D, scale * 2.0D);
        }
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        int erosion;

        if (!Config.BWOConfig.world.beachFix) {
            erosion = (int)(this.depthBuffer[x + z * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
        } else {
            double worldX = (chunkX << 4) + x;
            double worldZ = (chunkZ << 4) + z;
            erosion = (int)(this.perlinNoise3.sample(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
        }

        return new int[]{0, 0, erosion};
    }

    protected void generateCaves(int chunkX, int chunkZ, byte[] blocks) {
        if (this.superflat) {
            this.cave.place(this, this.world, chunkX, chunkZ, blocks);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (this.superflat) {
                this.ravine.place(this, this.world, chunkX, chunkZ, blocks);
            }
        }
    }

    public boolean useSurfaceHeightForStructures() {
        return false;
    }

    public void decorate(ChunkSource source, int x, int z) {
        super.decorate(source, x, z);
    }
}
