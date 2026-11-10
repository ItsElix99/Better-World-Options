package com.itselix99.betterworldoptions.world.worldtypes;

import java.util.Random;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class EarlyInfdevChunkGenerator extends OldChunkGenerator {
    private final Random brickRandom = new Random();

    public EarlyInfdevChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.scaleNoise = new BWOOctavePerlinNoiseSampler(this.random, 5, true);
        new BWOOctavePerlinNoiseSampler(this.random, 3, true);
        new BWOOctavePerlinNoiseSampler(this.random, 3, true);
        new BWOOctavePerlinNoiseSampler(this.random, 3, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 5, true);

        BWOChunkGenerator.setSizeLimits(-this.width / 2, this.width / 2, -this.length / 2, this.length / 2);

        if (!this.finiteWorldType.equals("MCPE")) {
            this.finiteWorldType = null;
        }
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 2097152);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        int var5 = chunkX << 4;
        int var6 = chunkZ << 4;
        int var7 = 0;

        int[] surfaceHeights = new int[256];
        for(int var8 = var5; var8 < var5 + 16; ++var8) {
            for(int var9 = var6; var9 < var6 + 16; ++var9) {
                int var10 = var8 / 1024;
                int var11 = var9 / 1024;
                float var12 = (float)(this.minLimitPerlinNoise.sampleInverse((float)var8 / 0.03125F, 0.0D, (float)var9 / 0.03125F) - this.maxLimitPerlinNoise.sampleInverse((float)var8 / 0.015625F, 0.0D, (float)var9 / 0.015625F)) / 512.0F / 4.0F;
                float var13 = (float)this.perlinNoise3.sampleInverse((float)var8 / 4.0F, (float)var9 / 4.0F);
                float var14 = (float)this.scaleNoise.sampleInverse((float)var8 / 8.0F, (float)var9 / 8.0F) / 8.0F;
                var13 = var13 > 0.0F ? (float)(this.perlinNoise1.sampleInverse((float)var8 * 0.25714284F * 2.0F, (float)var9 * 0.25714284F * 2.0F) * (double)var14 / 4.0D) : (float)(this.perlinNoise2.sampleInverse((float)var8 * 0.25714284F, (float)var9 * 0.25714284F) * (double)var14);
                int var15 = (int)(var12 + 64.0F + var13);
                if((float)this.perlinNoise3.sampleInverse(var8, var9) < 0.0F) {
                    var15 = var15 / 2 << 1;
                    if((float)this.perlinNoise3.sampleInverse((double) var8 / 5, (double) var9 / 5) < 0.0F) {
                        ++var15;
                    }
                }

                int localX = var8 & 15;
                int localZ = var9 & 15;

                surfaceHeights[localX + localZ * 16] = var15;

                int index = (var8 - var5) * 16 + var9 - var6;
                double temperature = temperatures[index];

                for(int var16 = this.world.getBottomY(); var16 < this.world.getTopY(); ++var16) {
                    int var17 = 0;
                    if((var8 == 0 || var9 == 0) && var16 <= var15 + 2) {
                        var17 = Block.OBSIDIAN.id;
                    } else if(this.oldFeatures && var16 == var15 + 1 && var15 >= 64 && Math.random() < 0.02D) {
                        var17 = Block.DANDELION.id;
                        if (this.theme.isFloweryLand() && this.random.nextInt(2) == 0) {
                            var17 = Block.ROSE.id;
                        }
                    } else if(var16 == var15 && var15 >= 64) {
                        if (!this.oldFeatures) {
                            var17 = Block.STONE.id;
                        } else {
                            var17 = this.theme.getTopBlock() != -1 ? this.theme.getTopBlock() : Block.GRASS_BLOCK.id;
                        }
                    } else if(var16 <= var15 - 2) {
                        var17 = Block.STONE.id;
                    } else if(var16 <= var15) {
                        if (!this.oldFeatures) {
                            var17 = Block.STONE.id;
                        } else {
                            var17 = this.theme.getSoilBlock() != -1 ? this.theme.getSoilBlock() : Block.DIRT.id;
                        }
                    } else if(var16 < 65) {
                        var17 = this.getWaterBlock(temperature, var16, false);
                    }

                    this.brickRandom.setSeed(var10 + var11 * 13871L);
                    int var18 = (var10 << 10) + 128 + this.brickRandom.nextInt(512);
                    int var19 = (var11 << 10) + 128 + this.brickRandom.nextInt(512);
                    var18 = var8 - var18;
                    var19 = var9 - var19;
                    if(var18 < 0) {
                        var18 = -var18;
                    }

                    if(var19 < 0) {
                        var19 = -var19;
                    }

                    if(var19 > var18) {
                        var18 = var19;
                    }

                    var18 = 127 - var18;
                    if(var18 == 255) {
                        var18 = 1;
                    }

                    if(var18 < var15) {
                        var18 = var15;
                    }

                    if(var16 <= var18 && (var17 == 0 || var17 == this.theme.getSurfaceLiquidBlock() || var17 == this.theme.getLiquidBlock() || var17 == this.theme.getFlowingLiquidBlock())) {
                        var17 = Block.BRICKS.id;
                    }

                    if(var17 < 0) {
                        var17 = 0;
                    }

                    blocks[var7++] = (byte)var17;
                }
            }
        }

        this.setSurfaceHeightForStructures(chunkX, chunkZ, surfaceHeights);
    }

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        if (!this.oldFeatures) {
            super.buildSurfaces(chunkX, chunkZ, blocks, biomes);
        }
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        double worldX = (chunkX << 4) + x;
        double worldZ = (chunkZ << 4) + z;
        int sandBeach = this.perlinNoise2.sampleInverse(worldX * scale, worldZ * scale, 0.0D) + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
        int gravelBeach = this.perlinNoise2.sampleInverse(worldZ * scale, 109.0134D, worldX * scale) + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
        int erosion = (int)(this.perlinNoise3.sampleInverse(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);

        return new int[]{sandBeach, gravelBeach, erosion};
    }

    protected int getForestNoiseValue(int x, int z) {
        int value = (int)this.forestNoise.sampleInverse((double)x * 0.0625D, (double)z * 0.0625D) << 3;

        if (this.theme.isDenseWoods() && value <= 4) {
            value = 10;
        }

        return value;
    }
}