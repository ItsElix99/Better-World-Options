package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;

public class SkylandsChunkGenerator extends BWOChunkGenerator {
    public SkylandsChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.scaleNoise = new BWOOctavePerlinNoiseSampler(this.random, 10, true);
        this.depthNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 8, true);

        if (!this.finiteWorldType.equals("MCPE")) {
            this.finiteWorldType = null;
        }
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 784426);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        int var5 = 32;
        int var6 = 2;
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
                        double var33 = 0.125D;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for (int var43 = 0; var43 < 8; ++var43) {
                            int var44 = var43 + var11 * 8 << this.shiftX | var12 * 8 << this.shiftZ | var13 * 4 + var32;
                            int var45 = this.world.getHeight();
                            double var46 = 0.125D;
                            double var48 = var35;
                            double var50 = (var37 - var35) * var46;

                            for (int var52 = 0; var52 < 8; ++var52) {
                                int var53 = 0;
                                if (var48 > 0.0D) {
                                    var53 = Block.STONE.id;

                                    int y = var13 * 4 + var32;
                                    int x = var11 * 8 + var43;
                                    int z = var12 * 8 + var52;

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

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        double var5 = 0.03125D;

        this.initBuffers(chunkX, chunkZ, var5);

        for (int var7 = 0; var7 < 16; ++var7) {
            for (int var8 = 0; var8 < 16; ++var8) {
                Biome var9 = this.getBiome(biomes, var7, var8);
                int[] beachesAndErosion = this.generateBeachesAndErosion(chunkX, chunkZ, var7, var8, var5);
                int var10 = beachesAndErosion[2];
                int var11 = -1;
                int[] topAndSoilBlock = this.getTopAndSoilBlock(var9);
                int var12 = topAndSoilBlock[0];
                int var13 = topAndSoilBlock[1];

                for (int var14 = this.world.getTopY() - 1; var14 >= this.world.getBottomY(); --var14) {
                    int var15 = this.getBlocksIndex(var7, var8, var14);
                    byte var16 = blocks[var15];
                    if (var16 == 0) {
                        var11 = -1;
                    } else if (var16 == Block.STONE.id) {
                        if (var11 == -1) {
                            if (var10 <= 0) {
                                var12 = 0;
                                var13 = Block.STONE.id;
                            }

                            var11 = var10;
                            if (var14 >= 0) {
                                blocks[var15] = (byte) var12;
                            } else {
                                blocks[var15] = (byte) var13;
                            }
                        } else if (var11 > 0) {
                            --var11;
                            blocks[var15] = (byte) var13;
                            int[] sandstone = this.generateSandstone(var11, var13);
                            var11 = sandstone[0];
                            var13 = sandstone[1];
                        }
                    }
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

    public int getMinSurfaceHeight() {
        return 40;
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        double[] var12 = this.world.method_1781().temperatureMap;
        double[] var13 = this.world.method_1781().downfallMap;
        this.scaleNoiseBuffer = this.scaleNoise.generateNoise(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121D, 1.121D, 0.5D);
        this.depthNoiseBuffer = this.depthNoise.generateNoise(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0D, 200.0D, 0.5D);
        var8 *= 2.0D;
        this.perlinNoiseBuffer = this.perlinNoise1.generateNoise(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoise(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoise(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / sizeX;

        for (int var17 = 0; var17 < sizeX; ++var17) {
            int var18 = var17 * var16 + var16 / 2;

            for (int var19 = 0; var19 < sizeZ; ++var19) {
                int var20 = var19 * var16 + var16 / 2;
                double var21 = var12[var18 * 16 + var20];
                double var23 = var13[var18 * 16 + var20] * var21;
                double var25 = 1.0D - var23;
                var25 *= var25;
                var25 *= var25;
                var25 = 1.0D - var25;
                double var27 = (this.scaleNoiseBuffer[var15] + 256.0D) / 512.0D;
                var27 *= var25;
                if (var27 > 1.0D) {
                    var27 = 1.0D;
                }

                double var29 = this.depthNoiseBuffer[var15] / 8000.0D;
                if (var29 < 0.0D) {
                    var29 = -var29 * 0.3D;
                }

                var29 = var29 * 3.0D - 2.0D;
                if (var29 > 1.0D) {
                    var29 = 1.0D;
                }

                var29 /= 8.0D;
                var29 = 0.0D;
                if (var27 < 0.0D) {
                    var27 = 0.0D;
                }

                var27 += 0.5D;
                var29 = var29 * (double) sizeY / 16.0D;
                ++var15;
                double var31 = (double) sizeY / 2.0D;

                for (int var33 = 0; var33 < sizeY; ++var33) {
                    double var34;
                    double var36 = ((double) var33 - var31) * 8.0D / var27;
                    if (var36 < 0.0D) {
                        var36 *= -1.0D;
                    }

                    double var38 = this.minLimitPerlinNoiseBuffer[var14] / 512.0D;
                    double var40 = this.maxLimitPerlinNoiseBuffer[var14] / 512.0D;
                    double var42 = (this.perlinNoiseBuffer[var14] / 10.0D + 1.0D) / 2.0D;
                    if (var42 < 0.0D) {
                        var34 = var38;
                    } else if (var42 > 1.0D) {
                        var34 = var40;
                    } else {
                        var34 = var38 + (var40 - var38) * var42;
                    }

                    var34 -= 8.0D;
                    byte var44 = 32;
                    if (var33 > sizeY - var44) {
                        double var45 = (float) (var33 - (sizeY - var44)) / ((float) var44 - 1.0D);
                        var34 = var34 * (1.0D - var45) + -30.0D * var45;
                    }

                    var44 = 8;
                    if (var33 < var44) {
                        double var61 = (float) (var44 - var33) / ((float) var44 - 1.0D);
                        var34 = var34 * (1.0D - var61) + -30.0D * var61;
                    }

                    heightMap[var14] = var34;
                    ++var14;
                }
            }
        }

        return heightMap;
    }

    public void decorate(ChunkSource source, int x, int z) {
        super.decorate(source, x, z);
    }
}