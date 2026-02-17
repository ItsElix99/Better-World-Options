package com.itselix99.betterworldoptions.world.worldtypes.infdev415;

import java.util.Random;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.util.math.noise.OctavePerlinNoiseSamplerInfdev415;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class Infdev415ChunkGenerator extends BWOChunkGenerator {
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen1;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen2;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen3;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen4;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen5;
    private final OctavePerlinNoiseSamplerInfdev415 forestNoise;
    private Biome[] biomes;

    public Infdev415ChunkGenerator(World world, long seed) {
        super(world, seed);
        new Random(seed);
        this.noiseGen1 = new OctavePerlinNoiseSamplerInfdev415(this.random, 16);
        this.noiseGen2 = new OctavePerlinNoiseSamplerInfdev415(this.random, 16);
        this.noiseGen3 = new OctavePerlinNoiseSamplerInfdev415(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerInfdev415(this.random, 4);
        this.noiseGen5 = new OctavePerlinNoiseSamplerInfdev415(this.random, 4);
        new OctavePerlinNoiseSamplerInfdev415(this.random, 5);
        this.forestNoise = new OctavePerlinNoiseSamplerInfdev415(this.random, 5);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 784426);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        int var5;
        int var6;
        double var50;
        for(var5 = 0; var5 < 4; ++var5) {
            for(var6 = 0; var6 < 4; ++var6) {
                double[][] var7 = new double[33][4];
                int var8 = (chunkX << 2) + var5;
                int var9 = (chunkZ << 2) + var6;

                for(int var10 = 0; var10 < var7.length; ++var10) {
                    var7[var10][0] = this.generateHeightMap(var8, var10, var9);
                    var7[var10][1] = this.generateHeightMap(var8, var10, var9 + 1);
                    var7[var10][2] = this.generateHeightMap(var8 + 1, var10, var9);
                    var7[var10][3] = this.generateHeightMap(var8 + 1, var10, var9 + 1);
                }

                for(var8 = 0; var8 < 32; ++var8) {
                    var50 = var7[var8][0];
                    double var11 = var7[var8][1];
                    double var13 = var7[var8][2];
                    double var15 = var7[var8][3];
                    double var17 = var7[var8 + 1][0];
                    double var19 = var7[var8 + 1][1];
                    double var21 = var7[var8 + 1][2];
                    double var23 = var7[var8 + 1][3];

                    for(int var25 = 0; var25 < 4; ++var25) {
                        double var26 = (double)var25 / 4.0D;
                        double var28 = var50 + (var17 - var50) * var26;
                        double var30 = var11 + (var19 - var11) * var26;
                        double var32 = var13 + (var21 - var13) * var26;
                        double var34 = var15 + (var23 - var15) * var26;

                        for(int var55 = 0; var55 < 4; ++var55) {
                            double var37 = (double)var55 / 4.0D;
                            double var39 = var28 + (var32 - var28) * var37;
                            double var41 = var30 + (var34 - var30) * var37;
                            int shiftY = MathHelper.ceilLog2(Config.BWOConfig.world.worldHeightLimit.getIntValue());
                            int shiftXZ = shiftY + 4;
                            int var27 = var55 + (var5 << 2) << shiftXZ | (var6 << 2) << shiftY | (var8 << 2) + var25;

                            for(int var36 = 0; var36 < 4; ++var36) {
                                double var45 = (double)var36 / 4.0D;
                                double var47 = var39 + (var41 - var39) * var45;
                                double var53 = temperatures[(var5 * 4 + var55) * 16 + var6 * 4 + var36];
                                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                int var56 = 0;
                                if((var8 << 2) + var25 < 64) {
                                    if (!this.theme.equals("Hell") && (var53 < temp && !this.oldFeatures || this.theme.equals("Winter")) && var8 * 4 + var25 >= 63) {
                                        var56 = Block.ICE.id;
                                    } else {
                                        var56 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                    }

                                }

                                if(var47 > 0.0D) {
                                    var56 = Block.STONE.id;
                                }

                                blocks[var27] = (byte)var56;
                                var27 += Config.BWOConfig.world.worldHeightLimit.getIntValue();
                            }
                        }
                    }
                }
            }
        }

        boolean beachFix = Config.BWOConfig.world.beachFix;

        for(var5 = 0; var5 < 16; ++var5) {
            for(var6 = 0; var6 < 16; ++var6) {
                double var49 = (chunkX << 4) + var5;
                var50 = (chunkZ << 4) + var6;
                Biome var55 = biomes[var6 + var5 * 16];
                boolean var51 = this.noiseGen4.create(var49 * (1.0D / 32.0D), var50 * (1.0D / 32.0D), 0.0D) + this.random.nextDouble() * 0.2D > 0.0D;
                boolean var14 = this.noiseGen4.create(var50 * (1.0D / 32.0D), 109.0134D, var49 * (1.0D / 32.0D)) + this.random.nextDouble() * 0.2D > 3.0D;
                int var52 = (int)(this.noiseGen5.sample(var49 * (1.0D / 32.0D) * 2.0D, var50 * (1.0D / 32.0D) * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                int var53 = -1;
                int var18;
                int var54;

                if (!this.oldFeatures) {
                    var18 = this.theme.equals("Hell") ? (var55.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var55.topBlockId) : var55.topBlockId;
                    var54 = var55.soilBlockId;
                } else {
                    var18 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                    var54 = Block.DIRT.id;
                }

                for(int var20 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var20 >= 0; --var20) {
                    int var16 = (var5 * 16 + var6) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var20;
                    if(blocks[var16] == 0) {
                        var53 = -1;
                    } else if(blocks[var16] == Block.STONE.id) {
                        if(var53 == -1) {
                            if(var52 <= 0) {
                                var18 = 0;
                                var54 = (byte)Block.STONE.id;
                            } else if(var20 >= 60 && var20 <= 65) {
                                if (!this.oldFeatures) {
                                    var18 = this.theme.equals("Hell") ? (var55.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var55.topBlockId) : var55.topBlockId;
                                    var54 = var55.soilBlockId;
                                } else {
                                    var18 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                                    var54 = Block.DIRT.id;
                                }
                                if(var14) {
                                    var18 = 0;
                                }

                                if(var14) {
                                    var54 = Block.GRAVEL.id;
                                }

                                if(var51) {
                                    var18 = this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id;
                                }

                                if(var51) {
                                    var54 = this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id;
                                }
                            }

                            double[] temperatureMap = this.world.method_1781().temperatureMap;
                            double temperature = temperatureMap[var6 + var5 * 16];

                            if(var20 < 64 && var18 == 0) {
                                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                if (beachFix && !this.theme.equals("Hell") && (temperature < temp && !this.oldFeatures || this.theme.equals("Winter")) && var20 == 63) {
                                    var18 = Block.ICE.id;
                                } else {
                                    var18 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                }
                            }

                            var53 = var52;
                            if(var20 >= 63) {
                                blocks[var16] = (byte)var18;
                            } else {
                                blocks[var16] = (byte)var54;
                            }
                        } else if(var53 > 0) {
                            --var53;
                            blocks[var16] = (byte)var54;
                            if (var53 == 0 && var54 == Block.SAND.id && !this.oldFeatures) {
                                var53 = this.random.nextInt(4);
                                var54 = (byte)Block.SANDSTONE.id;
                            }
                        }
                    }

                    --var16;
                }
            }
        }
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var5 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var5);

        if (!this.oldFeatures) {
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (!this.oldFeatures || Config.BWOConfig.world.allowGenWithOldFeaturesOn) {
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


    private double generateHeightMap(double var1, double var3, double var5) {
        double var7 = var3 * 4.0D - 64.0D;
        if(var7 < 0.0D) {
            var7 *= 3.0D;
        }

        double var9 = this.noiseGen3.create(var1 * 684.412D / 80.0D, var3 * 684.412D / 400.0D, var5 * 684.412D / 80.0D) / 2.0D;
        double var11;
        double var13;
        if(var9 < -1.0D) {
            var11 = this.noiseGen1.create(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D;
            var13 = var11 - var7;
            if(var13 < -10.0D) {
                var13 = -10.0D;
            }

            if(var13 > 10.0D) {
                var13 = 10.0D;
            }
        } else if(var9 > 1.0D) {
            var11 = this.noiseGen2.create(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D;
            var13 = var11 - var7;
            if(var13 < -10.0D) {
                var13 = -10.0D;
            }

            if(var13 > 10.0D) {
                var13 = 10.0D;
            }
        } else {
            double var15 = this.noiseGen1.create(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D - var7;
            double var17 = this.noiseGen2.create(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D - var7;
            if(var15 < -10.0D) {
                var15 = -10.0D;
            }

            if(var15 > 10.0D) {
                var15 = 10.0D;
            }

            if(var17 < -10.0D) {
                var17 = -10.0D;
            }

            if(var17 > 10.0D) {
                var17 = 10.0D;
            }

            double var19 = (var9 + 1.0D) / 2.0D;
            var11 = var15 + (var17 - var15) * var19;
            var13 = var11;
        }

        return var13;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
            this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
            int var4 = x << 4;
            x = z << 4;

            int var5;
            int var6;
            int var7;
            for(z = 0; z < 20; ++z) {
                var5 = var4 + this.random.nextInt(16);
                var6 = this.random.nextInt(128);
                var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.COAL_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            for(z = 0; z < 10; ++z) {
                var5 = var4 + this.random.nextInt(16);
                var6 = this.random.nextInt(64);
                var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.IRON_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            if(this.random.nextInt(2) == 0) {
                z = var4 + this.random.nextInt(16);
                var5 = this.random.nextInt(32);
                var6 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.GOLD_ORE.id)).generate(this.world, this.random, z, var5, var6);
            }

            if(this.random.nextInt(8) == 0) {
                z = var4 + this.random.nextInt(16);
                var5 = this.random.nextInt(16);
                var6 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.DIAMOND_ORE.id)).generate(this.world, this.random, z, var5, var6);
            }

            z = (int) this.forestNoise.sample((double)var4 * 0.25D, (double)x * 0.25D) << 3;
            LargeOakTreeFeature var9 = new LargeOakTreeFeature();

            if (this.theme.equals("Woods") && z <= 4) {
                z = 10;
            }

            for(var6 = 0; var6 < z; ++var6) {
                var7 = var4 + this.random.nextInt(16) + 8;
                int var8 = x + this.random.nextInt(16) + 8;
                var9.prepare(1.0D, 1.0D, 1.0D);
                var9.generate(this.world, this.random, var7, this.world.getTopY(var7, var8), var8);
            }

            if (this.theme.equals("Paradise")) {
                for(z = 0; z < 24; ++z) {
                    var5 = var4 + this.random.nextInt(16) + 8;
                    var6 = this.random.nextInt(128);
                    var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var5, var6, var7);
                }

                for (z = 0; z < 24; z++) {
                    var5 = var4 + this.random.nextInt(16) + 8;
                    var6 = this.random.nextInt(128);
                    var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var5, var6, var7);
                }
            }

            for(var5 = var4 + 8; var5 < var4 + 8 + 16; ++var5) {
                for(var7 = x + 8; var7 < x + 8 + 16; ++var7) {
                    var6 = this.world.getTopSolidBlockY(var5, var7);
                    if(this.theme.equals("Winter") && var6 > 0 && var6 < this.world.dimension.getHeight() && this.world.getBlockId(var5, var6, var7) == 0 && this.world.getMaterial(var5, var6 - 1, var7).isSolid() && this.world.getMaterial(var5, var6 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var5, var6, var7, Block.SNOW.id);
                    }
                }
            }
        } else {
            super.decorate(source, x, z);
        }
    }
}
