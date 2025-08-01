package com.itselix99.betterworldoptions.world.worldtypes.infdev420;

import com.itselix99.betterworldoptions.BWOConfig;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.util.math.noise.OctavePerlinNoiseSamplerInfdev420;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.itselix99.betterworldoptions.interfaces.BWOCustomRandomTreeFeature;
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

import java.util.Random;

public class Infdev420ChunkGenerator implements ChunkSource {
    private final Random random;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen1;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen2;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen3;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen4;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen5;
    private final OctavePerlinNoiseSamplerInfdev420 forestNoise;
    private final World world;
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private double[] noiseArray;
    private double[] noise3;
    private double[] noise1;
    private double[] noise2;
    private double[] temperatures;
    private Biome[] biomes;

    private final boolean betaFeatures;

    public Infdev420ChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        new Random(seed);

        if (((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        }

        this.noiseGen1 = new OctavePerlinNoiseSamplerInfdev420(this.random, 16);
        this.noiseGen2 = new OctavePerlinNoiseSamplerInfdev420(this.random, 16);
        this.noiseGen3 = new OctavePerlinNoiseSamplerInfdev420(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerInfdev420(this.random, 4);
        this.noiseGen5 = new OctavePerlinNoiseSamplerInfdev420(this.random, 4);
        new OctavePerlinNoiseSamplerInfdev420(this.random, 5);
        this.forestNoise = new OctavePerlinNoiseSamplerInfdev420(this.random, 5);

        this.betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int var10003 = chunkX << 2;
        int var8 = chunkZ << 2;
        int var7 = var10003;
        double[] var6 = this.noiseArray;
        Infdev420ChunkGenerator var71 = this;
        int vertical = BWOConfig.WORLD_CONFIG.worldHeightLimit / 8;

        if(var6 == null) {
            var6 = new double[5 * 5 * (vertical + 1)];
        }

        this.noise3 = this.noiseGen3.create(this.noise3, var7, 0, var8, 5, vertical + 1, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.noise1 = this.noiseGen1.create(this.noise1, var7, 0, var8, 5, vertical + 1, 5, 684.412D, 684.412D, 684.412D);
        this.noise2 = this.noiseGen2.create(this.noise2, var7, 0, var8, 5, vertical + 1, 5, 684.412D, 684.412D, 684.412D);
        var7 = 0;

        for(var8 = 0; var8 < 5; ++var8) {
            for(int var9 = 0; var9 < 5; ++var9) {
                for(int var10 = 0; var10 < vertical + 1; ++var10) {
                    double var63 = ((double)var10 - 8.5D) * 12.0D;
                    if(var63 < 0.0D) {
                        var63 *= 2.0D;
                    }

                    double var65 = var71.noise1[var7] / 512.0D;
                    double var67 = var71.noise2[var7] / 512.0D;
                    double var69 = (var71.noise3[var7] / 10.0D + 1.0D) / 2.0D;
                    double var61;
                    if(var69 < 0.0D) {
                        var61 = var65;
                    } else if(var69 > 1.0D) {
                        var61 = var67;
                    } else {
                        var61 = var65 + (var67 - var65) * var69;
                    }

                    var61 -= var63;
                    var6[var7] = var61;
                    ++var7;
                }
            }
        }

        this.noiseArray = var6;

        int var72;
        int var73;
        for(var72 = 0; var72 < 4; ++var72) {
            for(var73 = 0; var73 < 4; ++var73) {
                for(var7 = 0; var7 < vertical; ++var7) {
                    double var75 = this.noiseArray[(var72 * 5 + var73) * (vertical + 1) + var7];
                    double var77 = this.noiseArray[(var72 * 5 + var73 + 1) * (vertical + 1) + var7];
                    double var12 = this.noiseArray[((var72 + 1) * 5 + var73) * (vertical + 1) + var7];
                    double var14 = this.noiseArray[((var72 + 1) * 5 + var73 + 1) * (vertical + 1) + var7];
                    double var16 = this.noiseArray[(var72 * 5 + var73) * (vertical + 1) + var7 + 1];
                    double var18 = this.noiseArray[(var72 * 5 + var73 + 1) * (vertical + 1) + var7 + 1];
                    double var20 = this.noiseArray[((var72 + 1) * 5 + var73) * (vertical + 1) + var7 + 1];
                    double var22 = this.noiseArray[((var72 + 1) * 5 + var73 + 1) * (vertical + 1) + var7 + 1];

                    for(int var24 = 0; var24 < 8; ++var24) {
                        double var25 = (double)var24 / 8.0D;
                        double var27 = var75 + (var16 - var75) * var25;
                        double var29 = var77 + (var18 - var77) * var25;
                        double var31 = var12 + (var20 - var12) * var25;
                        double var33 = var14 + (var22 - var14) * var25;

                        for(int var82 = 0; var82 < 4; ++var82) {
                            double var36 = (double)var82 / 4.0D;
                            double var38 = var27 + (var31 - var27) * var36;
                            double var40 = var29 + (var33 - var29) * var36;

                            int shiftY = MathHelper.ceilLog2(BWOConfig.WORLD_CONFIG.worldHeightLimit);
                            int shiftXZ = shiftY + 4;
                            int var26 = var82 + (var72 << 2) << shiftXZ | (var73 << 2) << shiftY | (var7 << 3) + var24;

                            for(int var35 = 0; var35 < 4; ++var35) {
                                double var44 = (double)var35 / 4.0D;
                                double var46 = var38 + (var40 - var38) * var44;
                                double var53 = temperatures[(var72 * 4 + var82) * 16 + var73 * 4 + var35];
                                int var83 = 0;
                                if((var7 << 3) + var24 < 64) {
                                    if (var53 < (double)0.5F && var7 * 8 + var24 >= 63 && this.betaFeatures) {
                                        var83 = Block.ICE.id;
                                    } else {
                                        var83 = Block.WATER.id;
                                    }
                                }

                                if(var46 > 0.0D) {
                                    var83 = Block.STONE.id;
                                }

                                blocks[var26] = (byte)var83;
                                var26 += BWOConfig.WORLD_CONFIG.worldHeightLimit;
                            }
                        }
                    }
                }
            }
        }

        for(var72 = 0; var72 < 16; ++var72) {
            for(var73 = 0; var73 < 16; ++var73) {
                double var74 = (chunkX << 4) + var72;
                double var76 = (chunkZ << 4) + var73;
                Biome var82 = biomes[var72 + var73 * 16];
                boolean var13 = this.noiseGen4.create(var74 * (1.0D / 32.0D), var76 * (1.0D / 32.0D), 0.0D) + this.random.nextDouble() * 0.2D > 0.0D;
                boolean var78 = this.noiseGen4.create(var76 * (1.0D / 32.0D), 109.0134D, var74 * (1.0D / 32.0D)) + this.random.nextDouble() * 0.2D > 3.0D;
                int var15 = (int)(this.noiseGen5.sample(var74 * (1.0D / 32.0D) * 2.0D, var76 * (1.0D / 32.0D) * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                int var17 = -1;
                int var80;
                int var19;

                if (this.betaFeatures) {
                    var80 = var82.topBlockId;
                    var19 = var82.soilBlockId;
                } else {
                    var80 = Block.GRASS_BLOCK.id;
                    var19 = Block.DIRT.id;
                }

                for(int var81 = BWOConfig.WORLD_CONFIG.worldHeightLimit - 1; var81 >= 0; --var81) {
                    int var79 = (var73 * 16 + var72) * BWOConfig.WORLD_CONFIG.worldHeightLimit + var81;
                    if(blocks[var79] == 0) {
                        var17 = -1;
                    } else if(blocks[var79] == Block.STONE.id) {
                        if(var17 == -1) {
                            if(var15 <= 0) {
                                var80 = 0;
                                var19 = (byte)Block.STONE.id;
                            } else if(var81 >= 60 && var81 <= 65) {
                                if (this.betaFeatures) {
                                    var80 = var82.topBlockId;
                                    var19 = var82.soilBlockId;
                                } else {
                                    var80 = Block.GRASS_BLOCK.id;
                                    var19 = Block.DIRT.id;
                                }
                                if(var78) {
                                    var80 = 0;
                                }

                                if(var78) {
                                    var19 = Block.GRAVEL.id;
                                }

                                if(var13) {
                                    var80 = Block.SAND.id;
                                }

                                if(var13) {
                                    var19 = Block.SAND.id;
                                }
                            }

                            if(var81 < 64 && var80 == 0) {
                                var80 = Block.WATER.id;
                            }

                            var17 = var15;
                            if(var81 >= 63) {
                                blocks[var79] = (byte)var80;
                            } else {
                                blocks[var79] = (byte)var19;
                            }
                        } else if(var17 > 0) {
                            --var17;
                            blocks[var79] = (byte)var19;
                            if (var17 == 0 && var19 == Block.SAND.id && this.betaFeatures) {
                                var17 = this.random.nextInt(4);
                                var19 = (byte)Block.SANDSTONE.id;
                            }
                        }
                    }

                    --var79;
                }
            }
        }
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * BWOConfig.WORLD_CONFIG.worldHeightLimit * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var5 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var5);

        if (((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (BWOConfig.WORLD_CONFIG.ravineGeneration) {
            if (this.betaFeatures || BWOConfig.WORLD_CONFIG.allowGenWithBetaFeaturesOff) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();
        return flattenedChunk;
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

            z = (int)(this.forestNoise.sample((double)var4 * 0.05D, (double)x * 0.05D) - this.random.nextDouble());
            if(z < 0) {
                z = 0;
            }

            LargeOakTreeFeature var9 = new LargeOakTreeFeature();
            if(this.random.nextInt(100) == 0) {
                ++z;
            }

            for(var6 = 0; var6 < z; ++var6) {
                var7 = var4 + this.random.nextInt(16) + 8;
                int var8 = x + this.random.nextInt(16) + 8;
                var9.prepare(1.0D, 1.0D, 1.0D);
                var9.generate(this.world, this.random, var7, this.world.getTopY(var7, var8), var8);
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
                (new LakeFeature(Block.WATER.id)).generate(this.world, this.random, var13, var14, var15);
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
                var49 -= 20;
            }

            if (var6 == Biome.TUNDRA) {
                var49 -= 20;
            }

            if (var6 == Biome.PLAINS) {
                var49 -= 20;
            }

            for(int var61 = 0; var61 < var49; ++var61) {
                int var72 = var4 + this.random.nextInt(16) + 8;
                int var17 = var5 + this.random.nextInt(16) + 8;
                Feature var18 = ((BWOCustomRandomTreeFeature) var6).bwo_getRandomTreeFeatureInfdev(this.random);
                var18.prepare(1.0F, 1.0F, 1.0F);
                var18.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
            }

            byte var62 = 0;
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

            if (this.random.nextInt(2) == 0) {
                int var79 = var4 + this.random.nextInt(16) + 8;
                int var88 = this.random.nextInt(128);
                int var99 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
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
                (new SpringFeature(Block.FLOWING_WATER.id)).generate(this.world, this.random, var105, var111, var115);
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
                    if (var23 < (double)0.5F && var22 > 0 && var22 < 128 && this.world.isAir(var96, var22, var107) && this.world.getMaterial(var96, var22 - 1, var107).blocksMovement() && this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
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
