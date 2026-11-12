package com.itselix99.betterworldoptions.world.worldtypes.flat;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

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
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 784426);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        int var5 = this.world.getHeight() / 8;
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
                    double var14 = 0.125D;
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

                    for (int var32 = 0; var32 < 8; ++var32) {
                        double var33 = 0.25D;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for (int var43 = 0; var43 < 4; ++var43) {
                            int var44 = var43 + var11 * 4 << this.shiftX | var12 * 4 << this.shiftZ | var13 * 8 + var32;
                            int var45 = this.world.getHeight();
                            double var46 = 0.25D;
                            double var48 = var35;
                            double var50 = (var37 - var35) * var46;

                            for (int var52 = 0; var52 < 4; ++var52) {
                                double temperature = temperatures[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
                                int var53 = 0;
                                if (var13 * 8 + var32 <= var7) {
                                    if (var13 * 8 + var32 < var7) {
                                        var53 = this.getWaterBlock(temperature, var13 * 8 + var32, false);
                                    }

                                    if (var48 > 0.0D) {
                                        var53 = Block.STONE.id;

                                        int y = var13 * 8 + var32;
                                        int x = var11 * 4 + var43;
                                        int z = var12 * 4 + var52;

                                        surfaceHeights[x + z * 16] = y;
                                    }
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
        int var5 = this.worldType.getSeaLevel();
        double var6 = 0.03125D;

        this.initBuffers(chunkX, chunkZ, var6);
        double[] temperatures = this.getTemperatures(chunkX, chunkZ);

        for(int var8 = 0; var8 < 16; ++var8) {
            for(int var9 = 0; var9 < 16; ++var9) {
                double temperature = temperatures[var9 + var8 * 16];
                Biome var14 = this.getBiome(biomes, var8, var9);
                int[] beachesAndErosion = this.generateBeachesAndErosion(chunkX, chunkZ, var8, var9, var6);
                boolean var15 = beachesAndErosion[0] == 1;
                boolean var16 = beachesAndErosion[1] == 1;
                int var17 = beachesAndErosion[2];
                int var18 = -1;
                int[] topAndSoilBlock = this.getTopAndSoilBlock(var14);
                int var19 = topAndSoilBlock[0];
                int var20 = topAndSoilBlock[1];

                for(int var21 = this.world.getTopY() - 1; var21 >= this.world.getBottomY(); --var21) {
                    int var22 = this.getBlocksIndex(var8, var9, var21);
                    if (this.generateBedrock(var21)) {
                        blocks[var22] = (byte) Block.BEDROCK.id;
                    } else {
                        if (blocks[var22] == 0) {
                            var18 = -1;
                        } else if (blocks[var22] == Block.STONE.id) {
                            if (var18 == -1) {
                                if (var17 <= 0) {
                                    var19 = 0;
                                    var20 = Block.STONE.id;
                                } else if (var21 >= var5 - 4 && var21 <= var5 - 1) {
                                    var19 = topAndSoilBlock[0];
                                    var20 = topAndSoilBlock[1];

                                    if (var16) {
                                        var19 = 0;
                                    }

                                    if (var16) {
                                        var20 = Block.GRAVEL.id;
                                    }

                                    if (var15) {
                                        var19 = this.theme.getBeachTopBlock();
                                    }

                                    if (var15) {
                                        var20 = this.theme.getBeachSoilBlock();
                                    }
                                }

                                if (var21 < var5 && var19 == 0) {
                                    var19 = this.getWaterBlock(temperature, var21, true);
                                }

                                var18 = var17;
                                if (var21 >= var5 - 1) {
                                    blocks[var22] = (byte) var19;
                                } else {
                                    blocks[var22] = (byte) var20;
                                }
                            } else if (var18 > 0) {
                                --var18;
                                blocks[var22] = (byte) var20;
                                int[] sandstone = this.generateSandstone(var18, var20);
                                var18 = sandstone[0];
                                var20 = sandstone[1];
                            }
                        }

                        --var22;

                        this.buildLCEFiniteWorldLimit(chunkX, chunkZ, var8, var21, var9, blocks, var14);
                    }
                }
            }
        }
    }

    protected void initBuffers(int chunkX, int chunkZ, double scale) {
        if (this.superflat && !Config.BWOConfig.world.beachFix) {
            this.depthBuffer = this.perlinNoise3.generateNoise(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, scale * 2.0D, scale * 2.0D, scale * 2.0D);
        }
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        if (this.superflat) {
            return super.generateBeachesAndErosion(chunkX, chunkZ, x, z, scale);
        } else {
            return new int[]{0, 0, 1};
        }
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
        return this.superflat;
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        if (this.superflat) {
            double var8 = 684.412D;
            double var10 = 684.412D;
            double[] var12 = this.world.method_1781().temperatureMap;
            double[] var13 = this.world.method_1781().downfallMap;
            this.scaleNoiseBuffer = this.scaleNoise.generateNoise(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121D, 1.121D, 0.5D);
            this.depthNoiseBuffer = this.depthNoise.generateNoise(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0D, 200.0D, 0.5D);
            this.perlinNoiseBuffer = this.perlinNoise1.generateNoise(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
            this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoise(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
            this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoise(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
            int var14 = 0;
            int var15 = 0;
            int var16 = 16 / sizeX;

            for (int var17 = 0; var17 < sizeX; ++var17) {
                int var18 = var17 * var16 + var16 / 2;

                for (int var19 = 0; var19 < sizeZ; ++var19) {
                    double islandOffset = this.getIslandOffset(x + var17, z + var19, -200.0D);

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
                    if (var29 < 0.0D) {
                        var29 /= 2.0D;
                        if (var29 < -1.0D) {
                            var29 = -1.0D;
                        }

                        var29 /= 1.4D;
                        var29 /= 2.0D;
                        var27 = 0.0D;
                    } else {
                        if (var29 > 1.0D) {
                            var29 = 1.0D;
                        }

                        var29 /= 8.0D;
                    }

                    if (var27 < 0.0D) {
                        var27 = 0.0D;
                    }

                    var27 += 0.5D;
                    var29 = var29 * 17.0D / 16.0D;
                    double var31 = 17.0D / 2.0D + var29 * 4.0D;
                    ++var15;

                    for (int var33 = 0; var33 < sizeY; ++var33) {
                        double var34;
                        double var36 = ((double) var33 - var31) * 12.0D / var27;
                        if (var36 < 0.0D) {
                            var36 *= 4.0D;
                        }

                        double var38 = this.minLimitPerlinNoiseBuffer[var14] / 512.0D;
                        double var40 = this.maxLimitPerlinNoiseBuffer[var14] / 512.0D;
                        double var42 = (this.perlinNoiseBuffer[var14] / 10.0D + 1.0D) / 2.0D;
                        if (var42 < 0.0D) {
                            var34 = var38;

                            if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")) {
                                var34 += islandOffset;
                            }
                        } else if (var42 > 1.0D) {
                            var34 = var40;

                            if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")) {
                                var34 += islandOffset;
                            }
                        } else {
                            if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")) {
                                var38 += islandOffset;
                                var40 += islandOffset;
                            }

                            var34 = var38 + (var40 - var38) * var42;
                        }

                        var34 -= var36;
                        if (var33 > sizeY - 4) {
                            double var44 = (float) (var33 - (sizeY - 4)) / 3.0D;
                            var34 = var34 * (1.0D - var44) + -10.0D * var44;
                        }

                        heightMap[var14] = var34;
                        ++var14;
                    }
                }
            }
        } else {
            int index = 0;

            for (int posX = 0; posX < sizeX; ++posX) {
                for (int posZ = 0; posZ < sizeZ; ++posZ) {
                    for (int posY = 0; posY < sizeY; ++posY) {
                        heightMap[index] = 8.125D - posY;
                        ++index;
                    }
                }
            }
        }

        return heightMap;
    }
}
