package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWONoise;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class Alpha120ChunkGenerator extends BWOChunkGenerator {
    private final OctavePerlinNoiseSampler minLimitPerlinNoise;
    private final OctavePerlinNoiseSampler maxLimitPerlinNoise;
    private final OctavePerlinNoiseSampler perlinNoise1;
    private final OctavePerlinNoiseSampler perlinNoise2;
    private final OctavePerlinNoiseSampler perlinNoise3;
    public OctavePerlinNoiseSampler floatingIslandScale;
    public OctavePerlinNoiseSampler floatingIslandNoise;
    public OctavePerlinNoiseSampler forestNoise;
    private double[] heightMap;
    private double[] sandBuffer = new double[256];
    private double[] gravelBuffer = new double[256];
    private double[] depthBuffer = new double[256];
    private Biome[] biomes;
    double[] perlinNoiseBuffer;
    double[] minLimitPerlinNoiseBuffer;
    double[] maxLimitPerlinNoiseBuffer;
    double[] scaleNoiseBuffer;
    double[] depthNoiseBuffer;
    private double[] temperatures;

    public Alpha120ChunkGenerator(World world, long seed) {
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

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 784426);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        int var6 = 4;
        int var7 = 64;
        int var8 = var6 + 1;
        int vertical = Config.BWOConfig.world.worldHeightLimit.getIntValue() / 8;
        int var9 = vertical + 1;
        int var10 = var6 + 1;
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var6, chunkZ * var6, var8, var9, var10);

        for(int var11 = 0; var11 < var6; ++var11) {
            for(int var12 = 0; var12 < var6; ++var12) {
                for(int var13 = 0; var13 < vertical; ++var13) {
                    double var14 = 0.125F;
                    int var100 = ((var11) * var10 + var12 + 1) * var9;
                    int var101 = ((var11 + 1) * var10 + var12) * var9;
                    int var102 = ((var11 + 1) * var10 + var12 + 1) * var9;
                    double var16 = this.heightMap[((var11) * var10 + var12) * var9 + var13];
                    double var18 = this.heightMap[var100 + var13];
                    double var20 = this.heightMap[var101 + var13];
                    double var22 = this.heightMap[var102 + var13];
                    double var24 = (this.heightMap[((var11) * var10 + var12) * var9 + var13 + 1] - var16) * var14;
                    double var26 = (this.heightMap[var100 + var13 + 1] - var18) * var14;
                    double var28 = (this.heightMap[var101 + var13 + 1] - var20) * var14;
                    double var30 = (this.heightMap[var102 + var13 + 1] - var22) * var14;

                    for(int var32 = 0; var32 < 8; ++var32) {
                        double var33 = 0.25F;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for(int var43 = 0; var43 < 4; ++var43) {
                            int shiftY = MathHelper.ceilLog2(Config.BWOConfig.world.worldHeightLimit.getIntValue());
                            int shiftXZ = shiftY + 4;
                            int var44 = var43 + var11 * 4 << shiftXZ | var12 * 4 << shiftY | var13 * 8 + var32;
                            int var45 = Config.BWOConfig.world.worldHeightLimit.getIntValue();
                            double var46 = 0.25F;
                            double var48 = var35;
                            double var50 = (var37 - var35) * var46;

                            for(int var52 = 0; var52 < 4; ++var52) {
                                double var53 = temperatures[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
                                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                int var55 = 0;
                                if (var13 * 8 + var32 < var7) {
                                    if (!this.theme.equals("Hell") && var53 < temp && var13 * 8 + var32 >= var7 - 1) {
                                        var55 = Block.ICE.id;
                                    } else {
                                        var55 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                    }
                                }

                                if (var48 > (double)0.0F) {
                                    var55 = Block.STONE.id;
                                }

                                blocks[var44] = (byte)var55;
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

    }

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        int var5 = 64;
        double var6 = 0.03125F;
        boolean beachFix = Config.BWOConfig.world.beachFix;

        if (!beachFix) {
            this.sandBuffer = this.perlinNoise2.create(this.sandBuffer, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, var6, var6, 1.0F);
            this.gravelBuffer = this.perlinNoise2.create(this.gravelBuffer, chunkZ * 16, 109.0134, chunkX * 16, 16, 1, 16, var6, 1.0F, var6);
            this.depthBuffer = this.perlinNoise3.create(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, var6 * (double) 2.0F, var6 * (double) 2.0F, var6 * (double) 2.0F);
        }

        for(int var8 = 0; var8 < 16; ++var8) {
            for(int var9 = 0; var9 < 16; ++var9) {
                Biome var10 = biomes[var8 * 16 + var9];
                double x2 = (chunkX << 4) + var8;
                double z2 = (chunkZ << 4) + var9;
                int var11;
                int var12;
                int var13;
                if (beachFix) {
                    var11 = ((BWONoise)this.perlinNoise2).bwo_generateNoise(x2 * var6, z2 * var6, 0.0D) + this.random.nextDouble() * 0.2 > (double) 0.0F ? 1 : 0;
                    var12 = ((BWONoise)this.perlinNoise2).bwo_generateNoise(z2 * var6, var6, x2 * var6) + this.random.nextDouble() * 0.2 > (double) 3.0F ? 1 : 0;
                    var13 = (int) (this.perlinNoise3.sample(x2 * var6 * 2.0D, z2 * var6 * 2.0D) / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                } else {
                    var11 = this.sandBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > (double) 0.0F ? 1 : 0;
                    var12 = this.gravelBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > (double) 3.0F ? 1 : 0;
                    var13 = (int) (this.depthBuffer[var8 + var9 * 16] / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                }
                int var14 = -1;
                int var15 = this.theme.equals("Hell") ? (var10.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var10.topBlockId) : var10.topBlockId;
                int var16 = var10.soilBlockId;

                for(int var17 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var17 >= 0; --var17) {
                    int var18 = (var8 * 16 + var9) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var17;
                    if (var17 <= this.random.nextInt(5)) {
                        blocks[var18] = (byte)Block.BEDROCK.id;
                    } else {
                        int var19 = blocks[var18];
                        if (var19 == 0) {
                            var14 = -1;
                        } else if (var19 == Block.STONE.id) {
                            if (var14 == -1) {
                                if (var13 <= 0) {
                                    var15 = 0;
                                    var16 = (byte)Block.STONE.id;
                                } else if (var17 >= var5 - 4 && var17 <= var5 + 1) {
                                    var15 = this.theme.equals("Hell") ? (var10.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var10.topBlockId) : var10.topBlockId;
                                    var16 = var10.soilBlockId;
                                    if (var12 != 0) {
                                        var15 = 0;
                                    }

                                    if (var12 != 0) {
                                        var16 = (byte)Block.GRAVEL.id;
                                    }

                                    if (var11 != 0) {
                                        var15 = (byte) (this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id);
                                    }

                                    if (var11 != 0) {
                                        var16 = (byte) (this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id);
                                    }
                                }

                                double[] temperatureMap = this.world.method_1781().temperatureMap;
                                double temperature = temperatureMap[var8 * 16 + var9];

                                if (var17 < var5 && var15 == 0) {
                                    double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                    if (beachFix && !this.theme.equals("Hell") && (temperature < temp) && var17 >= var5 - 1) {
                                        var15 = (byte) Block.ICE.id;
                                    } else {
                                        var15 = (byte) (this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id);
                                    }
                                }

                                var14 = var13;
                                if (var17 >= var5 - 1) {
                                    blocks[var18] = (byte)var15;
                                } else {
                                    blocks[var18] = (byte)var16;
                                }
                            } else if (var14 > 0) {
                                --var14;
                                blocks[var18] = (byte)var16;
                            }
                        }
                    }

                    this.buildLCEFiniteWorldLimit(chunkX, chunkZ, var8, var17, var9, blocks, var10);
                }
            }
        }

    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var5 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, var5);
        this.buildSurfaces(chunkX, chunkZ, var3, this.biomes);
        this.cave.place(this, this.world, chunkX, chunkZ, var3);

        if (Config.BWOConfig.world.ravineGeneration && Config.BWOConfig.world.ravineGenWithOldFeatures) {
            this.ravine.place(this, this.world, chunkX, chunkZ, var3);
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();

        String limitMode = null;
        if (this.finiteWorldType.equals("MCPE") || this.finiteWorldType.equals("LCE")) {
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
        this.perlinNoiseBuffer = this.perlinNoise1.create(this.perlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8 / (double)80.0F, var10 / (double)160.0F, var8 / (double)80.0F);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.create(this.minLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.create(this.maxLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / sizeX;

        for(int var17 = 0; var17 < sizeX; ++var17) {
            int var18 = var17 * var16 + var16 / 2;

            for(int var19 = 0; var19 < sizeZ; ++var19) {
                double islandOffset = this.getIslandOffset(x + var17, z + var19, -200.0D);

                int var20 = var19 * var16 + var16 / 2;
                double var21 = var12[var18 * 16 + var20];
                double var23 = var13[var18 * 16 + var20] * var21;
                double var25 = (double)1.0F - var23;
                var25 *= var25;
                var25 *= var25;
                var25 = (double)1.0F - var25;
                double var27 = (this.scaleNoiseBuffer[var15] + (double)256.0F) / (double)512.0F;
                var27 *= var25;
                if (var27 > (double)1.0F) {
                    var27 = 1.0F;
                }

                double var29 = this.depthNoiseBuffer[var15] / (double)8000.0F;
                if (var29 < (double)0.0F) {
                    var29 = -var29 * 0.3;
                }

                var29 = var29 * (double)3.0F - (double)2.0F;
                if (var29 < (double)0.0F) {
                    var29 /= 2.0F;
                    if (var29 < (double)-1.0F) {
                        var29 = -1.0F;
                    }

                    var29 /= 1.4;
                    var29 /= 2.0F;
                    var27 = 0.0F;
                } else {
                    if (var29 > (double)1.0F) {
                        var29 = 1.0F;
                    }

                    var29 /= 8.0F;
                }

                if (var27 < (double)0.0F) {
                    var27 = 0.0F;
                }

                var27 += 0.5F;
                var29 = var29 * (double)17 / (double)16.0F;
                double var31 = (double)17 / (double)2.0F + var29 * (double)4.0F;
                ++var15;

                for(int var33 = 0; var33 < sizeY; ++var33) {
                    double var34;
                    double var36 = ((double)var33 - var31) * (double)12.0F / var27;
                    if (var36 < (double)0.0F) {
                        var36 *= 4.0F;
                    }

                    double var38 = this.minLimitPerlinNoiseBuffer[var14] / (double)512.0F;
                    double var40 = this.maxLimitPerlinNoiseBuffer[var14] / (double)512.0F;
                    double var42 = (this.perlinNoiseBuffer[var14] / (double)10.0F + (double)1.0F) / (double)2.0F;
                    if (var42 < (double)0.0F) {
                        var34 = var38;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else if (var42 > (double)1.0F) {
                        var34 = var40;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var38 += islandOffset;
                            var40 += islandOffset;
                        }

                        var34 = var38 + (var40 - var38) * var42;
                    }

                    var34 -= var36;
                    if (var33 > sizeY - 4) {
                        double var44 = (float)(var33 - (sizeY - 4)) / 3.0F;
                        var34 = var34 * ((double)1.0F - var44) + (double)-10.0F * var44;
                    }

                    heightMap[var14] = var34;
                    ++var14;
                }
            }
        }

        return heightMap;
    }

    public void decorate(ChunkSource source, int x, int z) {
        SandBlock.fallInstantly = true;
        int var4 = x * 16;
        int var5 = z * 16;
        Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
        this.random.setSeed(this.world.getSeed());
        long var7 = this.random.nextLong() / 2L * 2L + 1L;
        long var9 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)x * var7 + (long)z * var9 ^ this.world.getSeed());
        double var11;

        for(int var27 = 0; var27 < 8; ++var27) {
            int var39 = var4 + this.random.nextInt(16) + 8;
            int var51 = this.random.nextInt(128);
            int var16 = var5 + this.random.nextInt(16) + 8;
            (new DungeonFeature()).generate(this.world, this.random, var39, var51, var16);
        }

        for(int var28 = 0; var28 < 10; ++var28) {
            int var40 = var4 + this.random.nextInt(16);
            int var52 = this.random.nextInt(128);
            int var63 = var5 + this.random.nextInt(16);
            (new ClayOreFeature(32)).generate(this.world, this.random, var40, var52, var63);
        }

        for(int var29 = 0; var29 < 20; ++var29) {
            int var41 = var4 + this.random.nextInt(16);
            int var53 = this.random.nextInt(128);
            int var64 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.DIRT.id, 32)).generate(this.world, this.random, var41, var53, var64);
        }

        for(int var30 = 0; var30 < 10; ++var30) {
            int var42 = var4 + this.random.nextInt(16);
            int var54 = this.random.nextInt(128);
            int var65 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.GRAVEL.id, 32)).generate(this.world, this.random, var42, var54, var65);
        }

        for(int var31 = 0; var31 < 20; ++var31) {
            int var43 = var4 + this.random.nextInt(16);
            int var55 = this.random.nextInt(128);
            int var66 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.COAL_ORE.id, 16)).generate(this.world, this.random, var43, var55, var66);
        }

        for(int var32 = 0; var32 < 20; ++var32) {
            int var44 = var4 + this.random.nextInt(16);
            int var56 = this.random.nextInt(64);
            int var67 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.IRON_ORE.id, 8)).generate(this.world, this.random, var44, var56, var67);
        }

        for(int var33 = 0; var33 < 2; ++var33) {
            int var45 = var4 + this.random.nextInt(16);
            int var57 = this.random.nextInt(32);
            int var68 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.GOLD_ORE.id, 8)).generate(this.world, this.random, var45, var57, var68);
        }

        for(int var34 = 0; var34 < 8; ++var34) {
            int var46 = var4 + this.random.nextInt(16);
            int var58 = this.random.nextInt(16);
            int var69 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.REDSTONE_ORE.id, 7)).generate(this.world, this.random, var46, var58, var69);
        }

        for(int var35 = 0; var35 < 1; ++var35) {
            int var47 = var4 + this.random.nextInt(16);
            int var59 = this.random.nextInt(16);
            int var70 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.DIAMOND_ORE.id, 7)).generate(this.world, this.random, var47, var59, var70);
        }

        var11 = 0.5F;
        int var37 = (int)((this.forestNoise.sample((double)var4 * var11, (double)var5 * var11) / (double)8.0F + this.random.nextDouble() * (double)4.0F + (double)4.0F) / (double)3.0F);
        int var49 = 0;
        if (this.random.nextInt(10) == 0) {
            ++var49;
        }

        if (var6 == Biome.FOREST) {
            var49 += var37 + 5;
        }

        if (var6 == Biome.RAINFOREST) {
            var49 += var37 + 5;
        }

        if (var6 == Biome.SEASONAL_FOREST) {
            var49 += var37 + (this.theme.equals("Woods") ? 5 : 2);
        }

        if (var6 == Biome.TAIGA) {
            var49 += var37 + 5;
        }

        if (var6 == Biome.DESERT) {
            if (this.theme.equals("Woods")) {
                var49 += var37 + 5;
            } else {
                var49 -= 20;
            }
        }

        if (var6 == Biome.TUNDRA) {
            if (this.theme.equals("Woods")) {
                var49 += var37 + 5;
            } else {
                var49 -= 20;
            }
        }

        if (var6 == Biome.PLAINS) {
            if (this.theme.equals("Woods")) {
                var49 += var37 + 5;
            } else {
                var49 -= 20;
            }
        }

        if (var6 == Biome.SWAMPLAND || var6 == Biome.SHRUBLAND || var6 == Biome.SAVANNA) {
            if (this.theme.equals("Woods")) {
                var49 += var37 + 5;
            }
        }

        Feature feature = new OakTreeFeature();
        if (this.random.nextInt(10) == 0) {
            feature = new LargeOakTreeFeature();
        }

        if (var6 == Biome.RAINFOREST && this.random.nextInt(3) == 0) {
            feature = new LargeOakTreeFeature();
        }

        for(int var61 = 0; var61 < var49; ++var61) {
            int var72 = var4 + this.random.nextInt(16) + 8;
            int var17 = var5 + this.random.nextInt(16) + 8;
            feature.prepare(1.0F, 1.0F, 1.0F);
            feature.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
        }

        if (this.theme.equals("Paradise")) {
            for(int var73 = 0; var73 < 24; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + 8;
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            for(int var74 = 0; var74 < 24; ++var74) {
                int var79 = var4 + this.random.nextInt(16) + 8;
                int var88 = this.random.nextInt(128);
                int var99 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);

            }
        } else {
            for(int var73 = 0; var73 < 2; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + 8;
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            if (this.random.nextInt(2) == 0) {
                int var79 = var4 + this.random.nextInt(16) + 8;
                int var88 = this.random.nextInt(128);
                int var99 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
            }
        }

        if (this.random.nextInt(4) == 0) {
            int var80 = var4 + this.random.nextInt(16) + 8;
            int var89 = this.random.nextInt(128);
            int var100 = var5 + this.random.nextInt(16) + 8;
            (new PlantPatchFeature(Block.BROWN_MUSHROOM.id)).generate(this.world, this.random, var80, var89, var100);
        }

        if (this.random.nextInt(8) == 0) {
            int var81 = var4 + this.random.nextInt(16) + 8;
            int var90 = this.random.nextInt(128);
            int var101 = var5 + this.random.nextInt(16) + 8;
            (new PlantPatchFeature(Block.RED_MUSHROOM.id)).generate(this.world, this.random, var81, var90, var101);
        }

        for(int var82 = 0; var82 < 10; ++var82) {
            int var91 = var4 + this.random.nextInt(16) + 8;
            int var102 = this.random.nextInt(128);
            int var109 = var5 + this.random.nextInt(16) + 8;
            (new SugarCanePatchFeature()).generate(this.world, this.random, var91, var102, var109);
        }

        if (this.random.nextInt(32) == 0) {
            int var83 = var4 + this.random.nextInt(16) + 8;
            int var92 = this.random.nextInt(128);
            int var103 = var5 + this.random.nextInt(16) + 8;
            (new PumpkinPatchFeature()).generate(this.world, this.random, var83, var92, var103);
        }

        int var84 = 0;
        if (var6 == Biome.DESERT) {
            var84 += 10;
        }

        for(int var93 = 0; var93 < var84; ++var93) {
            int var104 = var4 + this.random.nextInt(16) + 8;
            int var110 = this.random.nextInt(128);
            int var114 = var5 + this.random.nextInt(16) + 8;
            (new CactusPatchFeature()).generate(this.world, this.random, var104, var110, var114);
        }

        for(int var94 = 0; var94 < 50; ++var94) {
            int var105 = var4 + this.random.nextInt(16) + 8;
            int var111 = this.random.nextInt(this.random.nextInt(120) + 8);
            int var115 = var5 + this.random.nextInt(16) + 8;
            (new SpringFeature(this.theme.equals("Hell") ? Block.FLOWING_LAVA.id : Block.FLOWING_WATER.id)).generate(this.world, this.random, var105, var111, var115);
        }

        for(int var95 = 0; var95 < 20; ++var95) {
            int var106 = var4 + this.random.nextInt(16) + 8;
            int var112 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
            int var116 = var5 + this.random.nextInt(16) + 8;
            (new SpringFeature(Block.FLOWING_LAVA.id)).generate(this.world, this.random, var106, var112, var116);
        }

        this.temperatures = this.world.method_1781().create(this.temperatures, var4 + 8, var5 + 8, 16, 16);

        for(int var96 = var4 + 8; var96 < var4 + 8 + 16; ++var96) {
            for(int var107 = var5 + 8; var107 < var5 + 8 + 16; ++var107) {
                int var113 = var96 - (var4 + 8);
                int var117 = var107 - (var5 + 8);
                int var22 = this.world.getTopSolidBlockY(var96, var107);
                double var23 = this.temperatures[var113 * 16 + var117] - (double)(var22 - 64) / (double)64.0F * 0.3;
                float temp = this.theme.equals("Winter") ? 1.1F : 0.5F;
                if (!this.theme.equals("Hell") && var23 < (double)temp && var22 > 0 && var22 < this.world.dimension.getHeight() && this.world.getBlockId(var96, var22, var107) == 0 && this.world.getMaterial(var96, var22 - 1, var107).blocksMovement() && this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
                    this.world.setBlock(var96, var22, var107, Block.SNOW.id);
                }
            }
        }

        SandBlock.fallInstantly = false;
    }
}
