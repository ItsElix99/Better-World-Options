package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.config.Config;
import net.minecraft.block.Block;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class SkylandsChunkGenerator extends BWOChunkGenerator {
    private final OctavePerlinNoiseSampler minLimitPerlinNoise;
    private final OctavePerlinNoiseSampler maxLimitPerlinNoise;
    private final OctavePerlinNoiseSampler perlinNoise1;
    private final OctavePerlinNoiseSampler perlinNoise2;
    private final OctavePerlinNoiseSampler perlinNoise3;
    public OctavePerlinNoiseSampler floatingIslandScale;
    public OctavePerlinNoiseSampler floatingIslandNoise;
    public OctavePerlinNoiseSampler forestNoise;
    private double[] heightMap;
    private double[] depthBuffer = new double[256];
    private Biome[] biomes;
    double[] perlinNoiseBuffer;
    double[] minLimitPerlinNoiseBuffer;
    double[] maxLimitPerlinNoiseBuffer;
    double[] scaleNoiseBuffer;
    double[] depthNoiseBuffer;

    public SkylandsChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new OctavePerlinNoiseSampler(this.random, 16);
        this.maxLimitPerlinNoise = new OctavePerlinNoiseSampler(this.random, 16);
        this.perlinNoise1 = new OctavePerlinNoiseSampler(this.random, 8);
        this.perlinNoise2 = new OctavePerlinNoiseSampler(this.random, 4);
        this.perlinNoise3 = new OctavePerlinNoiseSampler(this.random, 4);
        this.floatingIslandScale = new OctavePerlinNoiseSampler(this.random, 10);
        this.floatingIslandNoise = new OctavePerlinNoiseSampler(this.random, 16);
        this.forestNoise = new OctavePerlinNoiseSampler(this.random, 8);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 784426);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        byte var6 = 2;
        int var7 = var6 + 1;
        int var8 = 33;
        int var9 = var6 + 1;
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var6, chunkZ * var6, var7, var8, var9);

        for (int var10 = 0; var10 < var6; ++var10) {
            for (int var11 = 0; var11 < var6; ++var11) {
                for (int var12 = 0; var12 < 32; ++var12) {
                    double var13 = 0.25F;
                    int var100 = ((var10) * var9 + var11 + 1) * var8;
                    int var101 = ((var10 + 1) * var9 + var11) * var8;
                    int var102 = ((var10 + 1) * var9 + var11 + 1) * var8;
                    double var15 = this.heightMap[((var10) * var9 + var11) * var8 + var12];
                    double var17 = this.heightMap[var100 + var12];
                    double var19 = this.heightMap[var101 + var12];
                    double var21 = this.heightMap[var102 + var12];
                    double var23 = (this.heightMap[((var10) * var9 + var11) * var8 + var12 + 1] - var15) * var13;
                    double var25 = (this.heightMap[var100 + var12 + 1] - var17) * var13;
                    double var27 = (this.heightMap[var101 + var12 + 1] - var19) * var13;
                    double var29 = (this.heightMap[var102 + var12 + 1] - var21) * var13;

                    for (int var31 = 0; var31 < 4; ++var31) {
                        double var32 = 0.125F;
                        double var34 = var15;
                        double var36 = var17;
                        double var38 = (var19 - var15) * var32;
                        double var40 = (var21 - var17) * var32;

                        for (int var42 = 0; var42 < 8; ++var42) {
                            int shiftZ = MathHelper.ceilLog2(Config.BWOConfig.world.worldHeightLimit.getIntValue());
                            int shiftX = shiftZ + 4;
                            int var43 = var42 + var10 * 8 << shiftX | var11 * 8 << shiftZ | var12 * 4 + var31;
                            int var44 = Config.BWOConfig.world.worldHeightLimit.getIntValue();
                            double var45 = 0.125F;
                            double var47 = var34;
                            double var49 = (var36 - var34) * var45;

                            for (int var51 = 0; var51 < 8; ++var51) {
                                int var52 = 0;
                                if (var47 > (double) 0.0F) {
                                    var52 = Block.STONE.id;
                                }

                                blocks[var43] = (byte) var52;
                                var43 += var44;
                                var47 += var49;
                            }

                            var34 += var38;
                            var36 += var40;
                        }

                        var15 += var23;
                        var17 += var25;
                        var19 += var27;
                        var21 += var29;
                    }
                }
            }
        }

    }

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        double var5 = 0.03125F;
        boolean beachFix = Config.BWOConfig.world.beachFix;

        if (!beachFix) {
            this.depthBuffer = this.perlinNoise3.create(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, var5 * (double) 2.0F, var5 * (double) 2.0F, var5 * (double) 2.0F);
        }

        for (int var7 = 0; var7 < 16; ++var7) {
            for (int var8 = 0; var8 < 16; ++var8) {
                Biome var9 = biomes[beachFix ? var8 + var7 * 16 : var7 + var8 * 16];
                double x2 = (chunkX << 4) + var7;
                double z2 = (chunkZ << 4) + var8;
                int var10;
                if (beachFix) {
                    var10 = (int)(this.perlinNoise3.sample(x2 * var5 * 2.0D, z2 * var5 * 2.0D) / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                } else {
                    var10 = (int) (this.depthBuffer[var7 + var8 * 16] / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                }
                int var11 = -1;
                int var12 = this.theme.equals("Hell") ? (var9.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var9.topBlockId) : var9.topBlockId;
                int var13 = var9.soilBlockId;

                for (int var14 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var14 >= 0; --var14) {
                    int var15 = (beachFix ? var7 * 16 + var8 : var8 * 16 + var7) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var14;
                    byte var16 = blocks[var15];
                    if (var16 == 0) {
                        var11 = -1;
                    } else if (var16 == Block.STONE.id) {
                        if (var11 == -1) {
                            if (var10 <= 0) {
                                var12 = 0;
                                var13 = (byte) Block.STONE.id;
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
                            if (var11 == 0 && var13 == Block.SAND.id) {
                                var11 = this.random.nextInt(4);
                                var13 = (byte) Block.SANDSTONE.id;
                            }
                        }
                    }
                }
            }
        }

    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        this.buildTerrain(chunkX, chunkZ, var3);
        this.buildSurfaces(chunkX, chunkZ, var3, this.biomes);
        this.cave.place(this, this.world, chunkX, chunkZ, var3);

        if (Config.BWOConfig.world.ravineGeneration) {
            this.ravine.place(this, this.world, chunkX, chunkZ, var3);
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

    private double[] generateHeightMap(double[] heightMap, int x, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412;
        double var10 = 684.412;
        double[] var12 = this.world.method_1781().temperatureMap;
        double[] var13 = this.world.method_1781().downfallMap;
        this.scaleNoiseBuffer = this.floatingIslandScale.create(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121, 1.121, 0.5F);
        this.depthNoiseBuffer = this.floatingIslandNoise.create(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0F, 200.0F, 0.5F);
        var8 *= 2.0F;
        this.perlinNoiseBuffer = this.perlinNoise1.create(this.perlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8 / (double) 80.0F, var10 / (double) 160.0F, var8 / (double) 80.0F);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.create(this.minLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.create(this.maxLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / sizeX;

        for (int var17 = 0; var17 < sizeX; ++var17) {
            int var18 = var17 * var16 + var16 / 2;

            for (int var19 = 0; var19 < sizeZ; ++var19) {
                int var20 = var19 * var16 + var16 / 2;
                double var21 = var12[var18 * 16 + var20];
                double var23 = var13[var18 * 16 + var20] * var21;
                double var25 = (double) 1.0F - var23;
                var25 *= var25;
                var25 *= var25;
                var25 = (double) 1.0F - var25;
                double var27 = (this.scaleNoiseBuffer[var15] + (double) 256.0F) / (double) 512.0F;
                var27 *= var25;
                if (var27 > (double) 1.0F) {
                    var27 = 1.0F;
                }

                double var29 = this.depthNoiseBuffer[var15] / (double) 8000.0F;
                if (var29 < (double) 0.0F) {
                    var29 = -var29 * 0.3;
                }

                var29 = var29 * (double) 3.0F - (double) 2.0F;
                if (var29 > (double) 1.0F) {
                    var29 = 1.0F;
                }

                var29 /= 8.0F;
                var29 = 0.0F;
                if (var27 < (double) 0.0F) {
                    var27 = 0.0F;
                }

                var27 += 0.5F;
                var29 = var29 * (double) sizeY / (double) 16.0F;
                ++var15;
                double var31 = (double) sizeY / (double) 2.0F;

                for (int var33 = 0; var33 < sizeY; ++var33) {
                    double var34;
                    double var36 = ((double) var33 - var31) * (double) 8.0F / var27;
                    if (var36 < (double) 0.0F) {
                        var36 *= -1.0F;
                    }

                    double var38 = this.minLimitPerlinNoiseBuffer[var14] / (double) 512.0F;
                    double var40 = this.maxLimitPerlinNoiseBuffer[var14] / (double) 512.0F;
                    double var42 = (this.perlinNoiseBuffer[var14] / (double) 10.0F + (double) 1.0F) / (double) 2.0F;
                    if (var42 < (double) 0.0F) {
                        var34 = var38;
                    } else if (var42 > (double) 1.0F) {
                        var34 = var40;
                    } else {
                        var34 = var38 + (var40 - var38) * var42;
                    }

                    var34 -= 8.0F;
                    byte var44 = 32;
                    if (var33 > sizeY - var44) {
                        double var45 = (float) (var33 - (sizeY - var44)) / ((float) var44 - 1.0F);
                        var34 = var34 * ((double) 1.0F - var45) + (double) -30.0F * var45;
                    }

                    var44 = 8;
                    if (var33 < var44) {
                        double var61 = (float) (var44 - var33) / ((float) var44 - 1.0F);
                        var34 = var34 * ((double) 1.0F - var61) + (double) -30.0F * var61;
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