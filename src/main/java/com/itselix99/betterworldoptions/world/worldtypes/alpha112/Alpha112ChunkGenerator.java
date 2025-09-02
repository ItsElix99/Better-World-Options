package com.itselix99.betterworldoptions.world.worldtypes.alpha112;

import java.util.Random;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.worldtypes.alpha112.util.math.noise.OctavePerlinNoiseSamplerAlpha112;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class Alpha112ChunkGenerator implements ChunkSource {
    private final Random random;
    private final OctavePerlinNoiseSamplerAlpha112 minLimitPerlinNoise;
    private final OctavePerlinNoiseSamplerAlpha112 maxLimitPerlinNoise;
    private final OctavePerlinNoiseSamplerAlpha112 perlinNoise1;
    private final OctavePerlinNoiseSamplerAlpha112 perlinNoise2;
    private final OctavePerlinNoiseSamplerAlpha112 perlinNoise3;
    public OctavePerlinNoiseSamplerAlpha112 floatingIslandScale;
    public OctavePerlinNoiseSamplerAlpha112 floatingIslandNoise;
    public OctavePerlinNoiseSamplerAlpha112 forestNoise;
    private final World world;
    private double[] heightMap;
    private double[] sandBuffer = new double[256];
    private double[] gravelBuffer = new double[256];
    private double[] depthBuffer = new double[256];
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private Biome[] biomes;
    double[] perlinNoiseBuffer;
    double[] minLimitPerlinNoiseBuffer;
    double[] maxLimitPerlinNoiseBuffer;
    double[] scaleNoiseBuffer;
    double[] depthNoiseBuffer;
    private double[] temperatures;

    private final String worldType;
    private final boolean oldFeatures;
    private final String theme;

    public Alpha112ChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        this.worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        this.oldFeatures = ((BWOProperties) this.world.getProperties()).bwo_isOldFeatures();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();

        if (this.theme.equals("Winter")) {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(true);
            }
        } else {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(false);
            }
        }

        if (this.theme.equals("Hell") || this.theme.equals("Paradise")) {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetPrecipitation(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setPrecipitation(false);
            }
        } else {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetPrecipitation(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setPrecipitation(true);
            }
        }

        ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);

        if (this.oldFeatures) {
            switch (this.theme) {
                case "Hell" -> BetterWorldOptions.Alpha.setFogColor(1049600);
                case "Paradise" -> BetterWorldOptions.Alpha.setFogColor(13033215);
                case "Woods" -> BetterWorldOptions.Alpha.setFogColor(5069403);
                default -> BetterWorldOptions.Alpha.setFogColor(12638463);
            }
        }

        this.minLimitPerlinNoise = new OctavePerlinNoiseSamplerAlpha112(this.random, 16);
        this.maxLimitPerlinNoise = new OctavePerlinNoiseSamplerAlpha112(this.random, 16);
        this.perlinNoise1 = new OctavePerlinNoiseSamplerAlpha112(this.random, 8);
        this.perlinNoise2 = new OctavePerlinNoiseSamplerAlpha112(this.random, 4);
        this.perlinNoise3 = new OctavePerlinNoiseSamplerAlpha112(this.random, 4);
        this.floatingIslandScale = new OctavePerlinNoiseSamplerAlpha112(this.random, 10);
        this.floatingIslandNoise = new OctavePerlinNoiseSamplerAlpha112(this.random, 16);
        this.forestNoise = new OctavePerlinNoiseSamplerAlpha112(this.random, 8);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        byte var4 = 4;
        byte var5 = 64;
        int var6 = var4 + 1;
        int vertical = Config.BWOConfig.world.worldHeightLimit.getIntValue() / 8;
        byte var7 = (byte) (vertical + 1);
        int var8 = var4 + 1;
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var4, chunkZ * var4, var6, var7, var8);

        for(int var9 = 0; var9 < var4; ++var9) {
            for(int var10 = 0; var10 < var4; ++var10) {
                for(int var11 = 0; var11 < vertical; ++var11) {
                    double var12 = 0.125D;
                    int var100 = ((var9) * var8 + var10 + 1) * var7;
                    int var101 = ((var9 + 1) * var8 + var10) * var7;
                    int var102 = ((var9 + 1) * var8 + var10 + 1) * var7;
                    double var14 = this.heightMap[((var9) * var8 + var10) * var7 + var11];
                    double var16 = this.heightMap[var100 + var11];
                    double var18 = this.heightMap[var101 + var11];
                    double var20 = this.heightMap[var102 + var11];
                    double var22 = (this.heightMap[((var9) * var8 + var10) * var7 + var11 + 1] - var14) * var12;
                    double var24 = (this.heightMap[var100 + var11 + 1] - var16) * var12;
                    double var26 = (this.heightMap[var101 + var11 + 1] - var18) * var12;
                    double var28 = (this.heightMap[var102 + var11 + 1] - var20) * var12;

                    for(int var30 = 0; var30 < 8; ++var30) {
                        double var31 = 0.25D;
                        double var33 = var14;
                        double var35 = var16;
                        double var37 = (var18 - var14) * var31;
                        double var39 = (var20 - var16) * var31;

                        for(int var41 = 0; var41 < 4; ++var41) {
                            int shiftY = MathHelper.ceilLog2(Config.BWOConfig.world.worldHeightLimit.getIntValue());
                            int shiftXZ = shiftY + 4;
                            int var42 = var41 + var9 * 4 << shiftXZ | var10 * 4 << shiftY | var11 * 8 + var30;
                            int var43 = Config.BWOConfig.world.worldHeightLimit.getIntValue();
                            double var44 = 0.25D;
                            double var46 = var33;
                            double var48 = (var35 - var33) * var44;

                            for(int var50 = 0; var50 < 4; ++var50) {
                                double var52 = temperatures[(var9 * 4 + var41) * 16 + var10 * 4 + var50];
                                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                int var51 = 0;
                                if(var11 * 8 + var30 < var5) {
                                    if (!this.theme.equals("Hell") && (var52 < temp && !this.oldFeatures || this.theme.equals("Winter")) && var11 * 8 + var30 >= var5 - 1) {
                                        var51 = Block.ICE.id;
                                    } else {
                                        var51 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                    }
                                }

                                if(var46 > 0.0D) {
                                    var51 = Block.STONE.id;
                                }

                                blocks[var42] = (byte)var51;
                                var42 += var43;
                                var46 += var48;
                            }

                            var33 += var37;
                            var35 += var39;
                        }

                        var14 += var22;
                        var16 += var24;
                        var18 += var26;
                        var20 += var28;
                    }
                }
            }
        }

    }

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        byte var4 = 64;
        double var5 = 1.0D / 32.0D;
        this.sandBuffer = this.perlinNoise2.create(this.sandBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, var5, var5, 1.0D);
        this.gravelBuffer = this.perlinNoise2.create(this.gravelBuffer, chunkZ * 16, 109.0134D, chunkX * 16, 16, 1, 16, var5, 1.0D, var5);
        this.depthBuffer = this.perlinNoise3.create(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, var5 * 2.0D, var5 * 2.0D, var5 * 2.0D);

        for(int var7 = 0; var7 < 16; ++var7) {
            for(int var8 = 0; var8 < 16; ++var8) {
                Biome var18 = biomes[var7 + var8 * 16];
                boolean var9 = this.sandBuffer[var7 + var8 * 16] + this.random.nextDouble() * 0.2D > 0.0D;
                boolean var10 = this.gravelBuffer[var7 + var8 * 16] + this.random.nextDouble() * 0.2D > 3.0D;
                int var11 = (int)(this.depthBuffer[var7 + var8 * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                int var12 = -1;
                byte var13;
                byte var14;

                if (!this.oldFeatures) {
                    var13 = this.theme.equals("Hell") ? (byte) (var18.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var18.topBlockId) : var18.topBlockId;
                    var14 = var18.soilBlockId;
                } else {
                    var13 = (byte) (this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id);
                    var14 = (byte) Block.DIRT.id;
                }

                for(int var15 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var15 >= 0; --var15) {
                    int var16 = (var8 * 16 + var7) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var15;
                    if(var15 <= this.random.nextInt(6) - 1) {
                        blocks[var16] = (byte)Block.BEDROCK.id;
                    } else {
                        byte var17 = blocks[var16];
                        if(var17 == 0) {
                            var12 = -1;
                        } else if(var17 == Block.STONE.id) {
                            if(var12 == -1) {
                                if(var11 <= 0) {
                                    var13 = 0;
                                    var14 = (byte)Block.STONE.id;
                                } else if(var15 >= var4 - 4 && var15 <= var4 + 1) {
                                    if (!this.oldFeatures) {
                                        var13 = this.theme.equals("Hell") ? (byte) (var18.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var18.topBlockId) : var18.topBlockId;
                                        var14 = var18.soilBlockId;
                                    } else {
                                        var13 = (byte) (this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id);
                                        var14 = (byte) Block.DIRT.id;
                                    }
                                    if(var10) {
                                        var13 = 0;
                                    }

                                    if(var10) {
                                        var14 = (byte)Block.GRAVEL.id;
                                    }

                                    if(var9) {
                                        var13 = (byte) (this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id);
                                    }

                                    if(var9) {
                                        var14 = (byte) (this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id);
                                    }
                                }

                                if(var15 < var4 && var13 == 0) {
                                    var13 = (byte) (this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id);
                                }

                                var12 = var11;
                                if(var15 >= var4 - 1) {
                                    blocks[var16] = var13;
                                } else {
                                    blocks[var16] = var14;
                                }
                            } else if(var12 > 0) {
                                --var12;
                                blocks[var16] = var14;
                                if (var12 == 0 && var14 == Block.SAND.id && !this.oldFeatures) {
                                    var12 = this.random.nextInt(4);
                                    var14 = (byte)Block.SANDSTONE.id;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var5 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, var5);
        this.buildSurfaces(chunkX, chunkZ, var3, this.biomes);
        this.cave.place(this, this.world, chunkX, chunkZ, var3);

        if (Config.BWOConfig.world.ravineGeneration) {
            if (!this.oldFeatures || Config.BWOConfig.world.allowGenWithOldFeaturesOn) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();
        return flattenedChunk;
    }

    private double[] generateHeightMap(double[] heightMap, int x, int z, int sizeX, int sizeY, int sizeZ) {
        if(heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        this.scaleNoiseBuffer = this.floatingIslandScale.create(this.scaleNoiseBuffer, x, 0, z, sizeX, 1, sizeZ, 1.0D, 0.0D, 1.0D);
        this.depthNoiseBuffer = this.floatingIslandNoise.create(this.depthNoiseBuffer, x, 0, z, sizeX, 1, sizeZ, 100.0D, 0.0D, 100.0D);
        this.perlinNoiseBuffer = this.perlinNoise1.create(this.perlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.create(this.minLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.create(this.maxLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var12 = 0;
        int var13 = 0;

        for(int var14 = 0; var14 < sizeX; ++var14) {
            for(int var15 = 0; var15 < sizeZ; ++var15) {
                double var16 = (this.scaleNoiseBuffer[var13] + 256.0D) / 512.0D;
                if(var16 > 1.0D) {
                    var16 = 1.0D;
                }

                double var18 = 0.0D;
                double var20 = this.depthNoiseBuffer[var13] / 8000.0D;
                if(var20 < 0.0D) {
                    var20 = -var20;
                }

                var20 = var20 * 3.0D - 3.0D;
                if(var20 < 0.0D) {
                    var20 /= 2.0D;
                    if(var20 < -1.0D) {
                        var20 = -1.0D;
                    }

                    var20 /= 1.4D;
                    var20 /= 2.0D;
                    var16 = 0.0D;
                } else {
                    if(var20 > 1.0D) {
                        var20 = 1.0D;
                    }

                    var20 /= 6.0D;
                }

                var16 += 0.5D;
                var20 = var20 * (double)17 / 16.0D;
                double var22 = (double)17 / 2.0D + var20 * 4.0D;
                ++var13;

                for(int var24 = 0; var24 < sizeY; ++var24) {
                    double var25;
                    double var27 = ((double)var24 - var22) * 12.0D / var16;
                    if(var27 < 0.0D) {
                        var27 *= 4.0D;
                    }

                    double var29 = this.minLimitPerlinNoiseBuffer[var12] / 512.0D;
                    double var31 = this.maxLimitPerlinNoiseBuffer[var12] / 512.0D;
                    double var33 = (this.perlinNoiseBuffer[var12] / 10.0D + 1.0D) / 2.0D;
                    if(var33 < 0.0D) {
                        var25 = var29;
                    } else if(var33 > 1.0D) {
                        var25 = var31;
                    } else {
                        var25 = var29 + (var31 - var29) * var33;
                    }

                    var25 -= var27;
                    double var35;
                    if(var24 > sizeY - 4) {
                        var35 = (float)(var24 - (sizeY - 4)) / 3.0F;
                        var25 = var25 * (1.0D - var35) + -10.0D * var35;
                    }

                    if((double)var24 < var18) {
                        var35 = (var18 - (double)var24) / 4.0D;
                        if(var35 < 0.0D) {
                            var35 = 0.0D;
                        }

                        if(var35 > 1.0D) {
                            var35 = 1.0D;
                        }

                        var25 = var25 * (1.0D - var35) + -10.0D * var35;
                    }

                    heightMap[var12] = var25;
                    ++var12;
                }
            }
        }

        return heightMap;
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
            SandBlock.fallInstantly = true;
            int var4 = x * 16;
            int var5 = z * 16;
            this.random.setSeed(this.world.getSeed());
            long var6 = this.random.nextLong() / 2L * 2L + 1L;
            long var8 = this.random.nextLong() / 2L * 2L + 1L;
            this.random.setSeed((long)x * var6 + (long)z * var8 ^ this.world.getSeed());
            double var10;

            int var12;
            int var13;
            int var14;
            int var15;
            for(var12 = 0; var12 < 8; ++var12) {
                var13 = var4 + this.random.nextInt(16) + 8;
                var14 = this.random.nextInt(128);
                var15 = var5 + this.random.nextInt(16) + 8;
                (new DungeonFeature()).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 10; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(128);
                var15 = var5 + this.random.nextInt(16);
                (new ClayOreFeature(32)).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 20; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(128);
                var15 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.DIRT.id, 32)).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 10; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(128);
                var15 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.GRAVEL.id, 32)).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 20; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(128);
                var15 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.COAL_ORE.id, 16)).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 20; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(64);
                var15 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.IRON_ORE.id, 8)).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 2; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(32);
                var15 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.GOLD_ORE.id, 8)).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 8; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(16);
                var15 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.REDSTONE_ORE.id, 7)).generate(this.world, this.random, var13, var14, var15);
            }

            for(var12 = 0; var12 < 1; ++var12) {
                var13 = var4 + this.random.nextInt(16);
                var14 = this.random.nextInt(16);
                var15 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.DIAMOND_ORE.id, 7)).generate(this.world, this.random, var13, var14, var15);
            }

            var10 = 0.5D;
            var12 = (int)((this.forestNoise.sample((double)var4 * var10, (double)var5 * var10) / 8.0D + this.random.nextDouble() * 4.0D + 4.0D) / 3.0D);
            if (this.theme.equals("Woods")) {
                if (var12 <= 4) {
                    var12 = 10;
                }
            } else {
                if (var12 < 0) {
                    var12 = 0;
                }
            }

            if(this.random.nextInt(10) == 0) {
                ++var12;
            }

            Feature var18 = new OakTreeFeature();
            if(this.random.nextInt(10) == 0) {
                var18 = new LargeOakTreeFeature();
            }

            int var16;
            for(var14 = 0; var14 < var12; ++var14) {
                var15 = var4 + this.random.nextInt(16) + 8;
                var16 = var5 + this.random.nextInt(16) + 8;
                var18.prepare(1.0D, 1.0D, 1.0D);
                var18.generate(this.world, this.random, var15, this.world.getTopY(var15, var16), var16);
            }

            int var17;

            if (this.theme.equals("Paradise")) {
                for (var14 = 0; var14 < 12; ++var14) {
                    var15 = var4 + this.random.nextInt(16) + 8;
                    var16 = this.random.nextInt(128);
                    var17 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var15, var16, var17);
                }

                for (var14 = 0; var14 < 12; var14++) {
                    var15 = var4 + this.random.nextInt(16) + 8;
                    var16 = this.random.nextInt(128);
                    var17 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var15, var16, var17);
                }
            } else {
                for (var14 = 0; var14 < 2; ++var14) {
                    var15 = var4 + this.random.nextInt(16) + 8;
                    var16 = this.random.nextInt(128);
                    var17 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var15, var16, var17);
                }

                if (this.random.nextInt(2) == 0) {
                    var14 = var4 + this.random.nextInt(16) + 8;
                    var15 = this.random.nextInt(128);
                    var16 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var14, var15, var16);
                }
            }

            if(this.random.nextInt(4) == 0) {
                var14 = var4 + this.random.nextInt(16) + 8;
                var15 = this.random.nextInt(128);
                var16 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.BROWN_MUSHROOM.id)).generate(this.world, this.random, var14, var15, var16);
            }

            if(this.random.nextInt(8) == 0) {
                var14 = var4 + this.random.nextInt(16) + 8;
                var15 = this.random.nextInt(128);
                var16 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.RED_MUSHROOM.id)).generate(this.world, this.random, var14, var15, var16);
            }

            for(var14 = 0; var14 < 10; ++var14) {
                var15 = var4 + this.random.nextInt(16) + 8;
                var16 = this.random.nextInt(128);
                var17 = var5 + this.random.nextInt(16) + 8;
                (new SugarCanePatchFeature()).generate(this.world, this.random, var15, var16, var17);
            }

            for(var14 = 0; var14 < 1; ++var14) {
                var15 = var4 + this.random.nextInt(16) + 8;
                var16 = this.random.nextInt(128);
                var17 = var5 + this.random.nextInt(16) + 8;
                (new CactusPatchFeature()).generate(this.world, this.random, var15, var16, var17);
            }

            for(var14 = 0; var14 < 50; ++var14) {
                var15 = var4 + this.random.nextInt(16) + 8;
                var16 = this.random.nextInt(this.random.nextInt(120) + 8);
                var17 = var5 + this.random.nextInt(16) + 8;
                (new SpringFeature(this.theme.equals("Hell") ? Block.FLOWING_LAVA.id : Block.FLOWING_WATER.id)).generate(this.world, this.random, var15, var16, var17);
            }

            for(var14 = 0; var14 < 20; ++var14) {
                var15 = var4 + this.random.nextInt(16) + 8;
                var16 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
                var17 = var5 + this.random.nextInt(16) + 8;
                (new SpringFeature(Block.FLOWING_LAVA.id)).generate(this.world, this.random, var15, var16, var17);
            }

            for(var14 = var4 + 8; var14 < var4 + 8 + 16; ++var14) {
                for(var15 = var5 + 8; var15 < var5 + 8 + 16; ++var15) {
                    var16 = this.world.getTopSolidBlockY(var14, var15);
                    if(this.theme.equals("Winter") && var16 > 0 && var16 < 128 && this.world.getBlockId(var14, var16, var15) == 0 && this.world.getMaterial(var14, var16 - 1, var15).isSolid() && this.world.getMaterial(var14, var16 - 1, var15) != Material.ICE) {
                        this.world.setBlock(var14, var16, var15, Block.SNOW.id);
                    }
                }
            }

            SandBlock.fallInstantly = false;
        } else {
            SandBlock.fallInstantly = true;
            int var4 = x * 16;
            int var5 = z * 16;
            Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
            this.random.setSeed(this.world.getSeed());
            long var7 = this.random.nextLong() / 2L * 2L + 1L;
            long var9 = this.random.nextLong() / 2L * 2L + 1L;
            this.random.setSeed((long)x * var7 + (long)z * var9 ^ this.world.getSeed());
            double var11;
            if (this.random.nextInt(4) == 0) {
                int var13 = var4 + this.random.nextInt(16) + 8;
                int var14 = this.random.nextInt(128);
                int var15 = var5 + this.random.nextInt(16) + 8;
                (new LakeFeature(this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id)).generate(this.world, this.random, var13, var14, var15);
            }

            if (this.random.nextInt(8) == 0) {
                int var26 = var4 + this.random.nextInt(16) + 8;
                int var38 = this.random.nextInt(this.random.nextInt(120) + 8);
                int var50 = var5 + this.random.nextInt(16) + 8;
                if (var38 < 64 || this.random.nextInt(10) == 0) {
                    (new LakeFeature(Block.LAVA.id)).generate(this.world, this.random, var26, var38, var50);
                }
            }

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

            for(int var36 = 0; var36 < 1; ++var36) {
                int var48 = var4 + this.random.nextInt(16);
                int var60 = this.random.nextInt(16) + this.random.nextInt(16);
                int var71 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.LAPIS_ORE.id, 6)).generate(this.world, this.random, var48, var60, var71);
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

            for(int var61 = 0; var61 < var49; ++var61) {
                int var72 = var4 + this.random.nextInt(16) + 8;
                int var17 = var5 + this.random.nextInt(16) + 8;
                Feature var18 = var6.getRandomTreeFeature(this.random);
                var18.prepare(1.0F, 1.0F, 1.0F);
                var18.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
            }

            byte var62 = (byte) (this.theme.equals("Paradise") ? 8 : 0);
            if (var6 == Biome.FOREST) {
                var62 = 2;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var62 = 4;
            }

            if (var6 == Biome.TAIGA) {
                var62 = 2;
            }

            if (var6 == Biome.PLAINS) {
                var62 = 3;
            }

            for(int var73 = 0; var73 < var62; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + 8;
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            byte var74 = 0;
            if (var6 == Biome.FOREST) {
                var74 = 2;
            }

            if (var6 == Biome.RAINFOREST) {
                var74 = 10;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var74 = 2;
            }

            if (var6 == Biome.TAIGA) {
                var74 = 1;
            }

            if (var6 == Biome.PLAINS) {
                var74 = 10;
            }

            for(int var77 = 0; var77 < var74; ++var77) {
                byte var86 = 1;
                if (var6 == Biome.RAINFOREST && this.random.nextInt(3) != 0) {
                    var86 = 2;
                }

                int var97 = var4 + this.random.nextInt(16) + 8;
                int var20 = this.random.nextInt(128);
                int var21 = var5 + this.random.nextInt(16) + 8;
                (new GrassPatchFeature(Block.GRASS.id, var86)).generate(this.world, this.random, var97, var20, var21);
            }

            var74 = 0;
            if (var6 == Biome.DESERT) {
                var74 = 2;
            }

            for(int var78 = 0; var78 < var74; ++var78) {
                int var87 = var4 + this.random.nextInt(16) + 8;
                int var98 = this.random.nextInt(128);
                int var108 = var5 + this.random.nextInt(16) + 8;
                (new DeadBushPatchFeature(Block.DEAD_BUSH.id)).generate(this.world, this.random, var87, var98, var108);
            }

            if (this.theme.equals("Paradise")) {
                for (int var120 = 0; var120 < var62; var120++) {
                    int var79 = var4 + this.random.nextInt(16) + 8;
                    int var88 = this.random.nextInt(128);
                    int var99 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
                }
            } else {
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
                    if (!this.theme.equals("Hell") && var23 < (double)temp && var22 > 0 && var22 < 128 && this.world.isAir(var96, var22, var107) && this.world.getMaterial(var96, var22 - 1, var107).blocksMovement() && this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
                        this.world.setBlock(var96, var22, var107, Block.SNOW.id);
                    }
                }
            }

            SandBlock.fallInstantly = false;
        }
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

    @Environment(EnvType.CLIENT)
    public String getDebugInfo() {
        return "RandomLevelSource";
    }
}
