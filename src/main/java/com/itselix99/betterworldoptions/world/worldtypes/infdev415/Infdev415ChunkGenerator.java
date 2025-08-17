package com.itselix99.betterworldoptions.world.worldtypes.infdev415;

import java.util.Random;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.util.math.noise.OctavePerlinNoiseSamplerInfdev415;
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

public class Infdev415ChunkGenerator implements ChunkSource {
    private final Random random;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen1;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen2;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen3;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen4;
    private final OctavePerlinNoiseSamplerInfdev415 noiseGen5;
    private final OctavePerlinNoiseSamplerInfdev415 forestNoise;
    private final World world;
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private double[] temperatures;
    private Biome[] biomes;

    private final String worldType;
    private final boolean betaFeatures;
    private final String theme;

    public Infdev415ChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        new Random(seed);
        this.worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        this.betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();

        if (this.theme.equals("Winter")) {
            if (!this.betaFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(true);
            }
        } else {
            if (!this.betaFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(false);
            }
        }

        if (this.betaFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        }

        this.noiseGen1 = new OctavePerlinNoiseSamplerInfdev415(this.random, 16);
        this.noiseGen2 = new OctavePerlinNoiseSamplerInfdev415(this.random, 16);
        this.noiseGen3 = new OctavePerlinNoiseSamplerInfdev415(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerInfdev415(this.random, 4);
        this.noiseGen5 = new OctavePerlinNoiseSamplerInfdev415(this.random, 4);
        new OctavePerlinNoiseSamplerInfdev415(this.random, 5);
        this.forestNoise = new OctavePerlinNoiseSamplerInfdev415(this.random, 5);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
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
                                    if (!this.theme.equals("Hell") && (var53 < temp && this.betaFeatures || this.theme.equals("Winter")) && var8 * 8 + var25 >= 63) {
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

        for(var5 = 0; var5 < 16; ++var5) {
            for(var6 = 0; var6 < 16; ++var6) {
                double var49 = (chunkX << 4) + var5;
                var50 = (chunkZ << 4) + var6;
                Biome var55 = biomes[var5 + var6 * 16];
                boolean var51 = this.noiseGen4.create(var49 * (1.0 / 32.0), var50 * (1.0 / 32.0), 0.0) + this.random.nextDouble() * 0.2D > 0.0;
                boolean var14 = this.noiseGen4.create(var50 * (1.0 / 32.0), 109.0134D, var49 * (1.0 / 32.0)) + this.random.nextDouble() * 0.2D > 0.0;
                int var52 = (int)(this.noiseGen5.sample(var49 * (1.0D / 32.0D) * 2.0D, var50 * (1.0D / 32.0D) * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                int var53 = -1;
                int var18;
                int var54;

                if (this.betaFeatures) {
                    var18 = this.theme.equals("Hell") ? (var55.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var55.topBlockId) : var55.topBlockId;
                    var54 = var55.soilBlockId;
                } else {
                    var18 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                    var54 = Block.DIRT.id;
                }

                for(int var20 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var20 >= 0; --var20) {
                    int var16 = (var6 * 16 + var5) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var20;
                    if(blocks[var16] == 0) {
                        var53 = -1;
                    } else if(blocks[var16] == Block.STONE.id) {
                        if(var53 == -1) {
                            if(var52 <= 0) {
                                var18 = 0;
                                var54 = (byte)Block.STONE.id;
                            } else if(var20 >= 60 && var20 <= 65) {
                                if (this.betaFeatures) {
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

                            if(var20 < 64 && var18 == 0) {
                                var18 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
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
                            if (var53 == 0 && var54 == Block.SAND.id && this.betaFeatures) {
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

    public Chunk loadChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var5 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var5);

        if (this.betaFeatures) {
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (this.betaFeatures || Config.BWOConfig.world.allowGenWithBetaFeaturesOff) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();
        return flattenedChunk;
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

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (!this.betaFeatures) {
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

            if (this.theme.equals("Woods") && z <= 0) {
                z = 10;
            }

            for(var6 = 0; var6 < z; ++var6) {
                var7 = var4 + this.random.nextInt(16) + 8;
                int var8 = x + this.random.nextInt(16) + 8;
                var9.prepare(1.0D, 1.0D, 1.0D);
                var9.generate(this.world, this.random, var7, this.world.getTopY(var7, var8), var8);
            }

            if (this.theme.equals("Paradise")) {
                for(z = 0; z < 12; ++z) {
                    var5 = var4 + this.random.nextInt(16) + 8;
                    var6 = this.random.nextInt(128);
                    var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var5, var6, var7);
                }

                for (z = 0; z < 12; z++) {
                    var5 = var4 + this.random.nextInt(16) + 8;
                    var6 = this.random.nextInt(128);
                    var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var5, var6, var7);
                }
            }

            for(var5 = var4 + 8; var5 < var4 + 8 + 16; ++var5) {
                for(var7 = x + 8; var7 < x + 8 + 16; ++var7) {
                    var6 = this.world.getTopSolidBlockY(var5, var7);
                    if(this.theme.equals("Winter") && var6 > 0 && var6 < 128 && this.world.getBlockId(var5, var6, var7) == 0 && this.world.getMaterial(var5, var6 - 1, var7).isSolid() && this.world.getMaterial(var5, var6 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var5, var6, var7, Block.SNOW.id);
                    }
                }
            }
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
                var49 += var37 + 2;
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
                Feature var18 = ((BWOWorld) var6).bwo_getRandomTreeFeatureInfdev(this.random);
                var18.prepare(1.0F, 1.0F, 1.0F);
                var18.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
            }

            byte var62 = (byte) (this.theme.equals("Paradise") ? 8 : 0);
            if (var6 == Biome.FOREST) {
                var62 += 2;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var62 += 4;
            }

            if (var6 == Biome.TAIGA) {
                var62 += 2;
            }

            if (var6 == Biome.PLAINS) {
                var62 += 3;
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
