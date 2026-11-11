package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

public class Infdev327ChunkGenerator extends OldChunkGenerator {
    public Infdev327ChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        new BWOOctavePerlinNoiseSampler(this.random, 5, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 5, true);

        if (!this.finiteWorldType.equals("MCPE")) {
            this.finiteWorldType = null;
        }
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 784426);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        int var5 = 32;
        int var6 = 4;
        int var7 = this.worldType.getSeaLevel();
        int var8 = var6 + 1;
        int var9 = var5 + 1;
        int var10 = var6 + 1;
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var6, 0, chunkZ * var6, var8, var9, var10);

        int[] surfaceHeights = new int[256];
        for (int var11 = 0; var11 < var6; ++var11) {
            for (int var12 = 0; var12 < var6; ++var12) {
                for (int var13 = 0; var13 < var5; ++var13) {
                    double var14 = 0.25D;
                    int var54 = (var11 * var10 + var12 + 1) * (var9);
                    int var55 = ((var11 + 1) * var10 + var12) * (var9);
                    int var56 = ((var11 + 1) * var10 + var12 + 1) * (var9);
                    double var16 = this.heightMap[(var11 * var10 + var12) * (var9) + var13];
                    double var18 = this.heightMap[var54 + var13];
                    double var20 = this.heightMap[var55 + var13];
                    double var22 = this.heightMap[var56 + var13];
                    double var24 = (this.heightMap[(var11 * var10 + var12) * (var9) + var13 + 1] - var16) * var14;
                    double var26 = (this.heightMap[var54 + var13 + 1] - var18) * var14;
                    double var28 = (this.heightMap[var55 + var13 + 1] - var20) * var14;
                    double var30 = (this.heightMap[var56 + var13 + 1] - var22) * var14;

                    for (int var32 = 0; var32 < 4; ++var32) {
                        double var33 = 0.25D;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for (int var43 = 0; var43 < 4; ++var43) {
                            int var44 = var43 + var11 * 4 << this.shiftX | var12 * 4 << this.shiftZ | var13 * 4 + var32;
                            int var45 = this.world.getHeight();
                            double var46 = 0.25D;
                            double var48 = var35;
                            double var50 = (var37 - var35) * var46;

                            for (int var52 = 0; var52 < 4; ++var52) {
                                double temperature = temperatures[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
                                int var53 = 0;
                                if (var13 * 4 + var32 < var7) {
                                    var53 = this.getWaterBlock(temperature, var13 * 4 + var32, false);
                                }

                                if (var48 > 0.0D) {
                                    var53 = Block.STONE.id;

                                    int y = var13 * 4 + var32;
                                    int x = var11 * 4 + var43;
                                    int z = var12 * 4 + var52;

                                    surfaceHeights[x + z * 16] = y;
                                }

                                blocks[var44] = (byte) var53;
                                var44 += var45;
                                var48 += var50;
                            }

                            var35 += var39;
                            var37 += var41;
                        }

                        var16 += var24;
                        var18 += var26;
                        var20 += var28;
                        var22 += var30;
                    }
                }
            }
        }

        this.setSurfaceHeightForStructures(chunkX, chunkZ, surfaceHeights);
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        if (this.oldFeatures) {
            return new int[]{0, 0, 1};
        } else {
            double worldX = (chunkX << 4) + x;
            double worldZ = (chunkZ << 4) + z;
            int sandBeach = this.perlinNoise2.sampleInverse(worldX * scale, worldZ * scale, 0.0D) + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
            int gravelBeach = this.perlinNoise2.sampleInverse(worldZ * scale, 109.0134D, worldX * scale) + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
            int erosion = (int) (this.perlinNoise3.sampleInverse(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);

            return new int[]{sandBeach, gravelBeach, erosion};
        }
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        int var12 = 0;

        for(int var13 = 0; var13 < sizeX; ++var13) {
            for(int var14 = 0; var14 < sizeZ; ++var14) {
                for(int var15 = 0; var15 < sizeY; ++var15) {
                    double var16 = var15 * 4.0D - 64.0D;
                    if(var16 < 0.0D) {
                        var16 *= 3.0D;
                    }

                    double var18 = this.perlinNoise1.sampleInverse((x + var13) * var8 / 80.0D, var15 * var10 / 400.0D, (z + var14) * var8 / 80.0D) / 2.0D;
                    double var20;
                    double var22;
                    if(var18 < -1.0D) {
                        var20 = this.minLimitPerlinNoise.sampleInverse((x + var13) * var8, var15 * (var10 + 300.000D), (z + var14) * var8) / 512.0D;
                        var22 = var20 - var16;
                        if(var22 < -10.0D) {
                            var22 = -10.0D;
                        }

                        if(var22 > 10.0D) {
                            var22 = 10.0D;
                        }
                    } else if(var18 > 1.0D) {
                        var20 = this.maxLimitPerlinNoise.sampleInverse((x + var13) * var8, var15 * (var10 + 300.000D), (z + var14) * var8) / 512.0D;
                        var22 = var20 - var16;
                        if(var22 < -10.0D) {
                            var22 = -10.0D;
                        }

                        if(var22 > 10.0D) {
                            var22 = 10.0D;
                        }
                    } else {
                        double var24 = this.minLimitPerlinNoise.sampleInverse((x + var13) * var8, var15 * (var10 + 300.000D), (z + var14) * var8) / 512.0D - var16;
                        double var26 = this.maxLimitPerlinNoise.sampleInverse((x + var13) * var8, var15 * (var10 + 300.000D), (z + var14) * var8) / 512.0D - var16;
                        if(var24 < -10.0D) {
                            var24 = -10.0D;
                        }

                        if(var24 > 10.0D) {
                            var24 = 10.0D;
                        }

                        if(var26 < -10.0D) {
                            var26 = -10.0D;
                        }

                        if(var26 > 10.0D) {
                            var26 = 10.0D;
                        }

                        double var28 = (var18 + 1.0D) / 2.0D;
                        var20 = var24 + (var26 - var24) * var28;
                        var22 = var20;
                    }

                    heightMap[var12] = var22;
                    ++var12;
                }
            }
        }

        return heightMap;
    }

    protected int getForestNoiseValue(int x, int z) {
        int value = (int)this.forestNoise.sampleInverse((double)x * 0.25D, (double)z * 0.25D) << 3;

        if (this.theme.isDenseWoods() && value <= 4) {
            value = 10;
        }

        return value;
    }

    protected void generateTrees(int x, int z, int treeCount, Feature tree) {
        for(int var5 = 0; var5 < treeCount; ++var5) {
            int var6 = x + this.random.nextInt(16);
            int var7 = z + this.random.nextInt(16);
            int var9 = this.world.getTopY(var6, var7);
            int var8 = var6 + 2;
            int var10 = var7 + 2;
            int var11 = this.random.nextInt(3) + 4;
            boolean var12 = true;
            if(var9 > this.world.getBottomY() && var9 + var11 + 1 <= this.world.getTopY()) {
                int var13;
                int var15;
                int var16;
                int var17;
                for(var13 = var9; var13 <= var9 + 1 + var11; ++var13) {
                    byte var14 = 1;
                    if(var13 == var9) {
                        var14 = 0;
                    }

                    if(var13 >= var9 + 1 + var11 - 2) {
                        var14 = 2;
                    }

                    for(var15 = var8 - var14; var15 <= var8 + var14 && var12; ++var15) {
                        for(var16 = var10 - var14; var16 <= var10 + var14 && var12; ++var16) {
                            if(var13 >= this.world.getBottomY() && var13 < this.world.getTopY()) {
                                var17 = this.world.getBlockId(var15, var13, var16);
                                if(var17 != 0) {
                                    var12 = false;
                                }
                            } else {
                                var12 = false;
                            }
                        }
                    }
                }

                if(!var12) {
                } else {
                    var13 = this.world.getBlockId(var8, var9 - 1, var10);
                    if((var13 == Block.GRASS_BLOCK.id|| var13 == Block.DIRT.id) && var9 < this.world.getTopY() - var11 - 1) {
                        this.world.setBlockWithoutNotifyingNeighbors(var8, var9 - 1, var10, Block.DIRT.id);

                        int var20;
                        for(var20 = var9 - 3 + var11; var20 <= var9 + var11; ++var20) {
                            var15 = var20 - (var9 + var11);
                            var16 = 1 - var15 / 2;

                            for(var17 = var8 - var16; var17 <= var8 + var16; ++var17) {
                                int var19 = var17 - var8;

                                for(var13 = var10 - var16; var13 <= var10 + var16; ++var13) {
                                    int var18 = var13 - var10;
                                    if((Math.abs(var19) != var16 || Math.abs(var18) != var16 || this.random.nextInt(2) != 0 && var15 != 0) && !Block.BLOCKS_OPAQUE[this.world.getBlockId(var17, var20, var13)]) {
                                        this.world.setBlockWithoutNotifyingNeighbors(var17, var20, var13, Block.LEAVES.id);
                                    }
                                }
                            }
                        }

                        for(var20 = 0; var20 < var11; ++var20) {
                            int var21 = this.world.getBlockId(var8, var9 + var20, var10);
                            if(var21 == 0 || var21 == Block.LEAVES.id) {
                                this.world.setBlockWithoutNotifyingNeighbors(var8, var9 + var20, var10, Block.LOG.id);
                            }
                        }
                    } else {
                    }
                }
            } else {
            }
        }
    }
}