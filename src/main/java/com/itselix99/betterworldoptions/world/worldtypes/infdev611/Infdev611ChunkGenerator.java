package com.itselix99.betterworldoptions.world.worldtypes.infdev611;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.infdev611.util.math.noise.OctavePerlinNoiseSamplerInfdev611;
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

import java.util.Random;

public class Infdev611ChunkGenerator implements ChunkSource {
    private final Random random;
    private final OctavePerlinNoiseSamplerInfdev611 minLimitPerlinNoise;
    private final OctavePerlinNoiseSamplerInfdev611 maxLimitPerlinNoise;
    private final OctavePerlinNoiseSamplerInfdev611 perlinNoise1;
    private final OctavePerlinNoiseSamplerInfdev611 perlinNoise2;
    private final OctavePerlinNoiseSamplerInfdev611 perlinNoise3;
    private final OctavePerlinNoiseSamplerInfdev611 scaleNoise;
    private final OctavePerlinNoiseSamplerInfdev611 depthNoise;
    private final OctavePerlinNoiseSamplerInfdev611 forestNoise;
    private final World world;
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private double[] heightMap;
    private double[] perlinNoiseBuffer;
    private double[] minLimitPerlinNoiseBuffer;
    private double[] maxLimitPerlinNoiseBuffer;
    private double[] scaleNoiseBuffer;
    private double[] depthNoiseBuffer;
    private double[] temperatures;
    private Biome[] biomes;

    private final String worldType;
    private final boolean betaFeatures;
    private final String theme;

    public Infdev611ChunkGenerator(World world, long seed) {
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

        this.minLimitPerlinNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 16);
        this.maxLimitPerlinNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 16);
        this.perlinNoise1 = new OctavePerlinNoiseSamplerInfdev611(this.random, 8);
        this.perlinNoise2 = new OctavePerlinNoiseSamplerInfdev611(this.random, 4);
        this.perlinNoise3 = new OctavePerlinNoiseSamplerInfdev611(this.random, 4);
        this.scaleNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 10);
        this.depthNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 16);
        this.forestNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 8);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int var10003 = chunkX << 2;
        int var7 = chunkZ << 2;
        int m11 = var10003;
        double[] var6 = this.heightMap;
        Infdev611ChunkGenerator var81 = this;
        int vertical = Config.BWOConfig.world.worldHeightLimit.getIntValue() / 8;

        if (var6 == null) {
            var6 = new double[5 * 5 * (vertical + 1)];
        }

        this.scaleNoiseBuffer = this.scaleNoise.create(this.scaleNoiseBuffer, m11, 0, var7, 5, 1, 5, 1.0F, 0.0F, 1.0F);
        this.depthNoiseBuffer = this.depthNoise.create(this.depthNoiseBuffer, m11, 0, var7, 5, 1, 5, 100.0F, 0.0F, 100.0F);
        this.perlinNoiseBuffer = this.perlinNoise1.create(this.perlinNoiseBuffer, m11, 0, var7, 5, vertical + 1, 5, 8.555150000000001, 4.277575000000001, 8.555150000000001);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.create(this.minLimitPerlinNoiseBuffer, m11, 0, var7, 5, vertical + 1, 5, 684.412, 684.412, 684.412);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.create(this.maxLimitPerlinNoiseBuffer, m11, 0, var7, 5, vertical + 1, 5, 684.412, 684.412, 684.412);
        m11 = 0;
        var7 = 0;

        for(int var8 = 0; var8 < 5; ++var8) {
            for(int var9 = 0; var9 < 5; ++var9) {
                double var10;
                if ((var10 = (var81.scaleNoiseBuffer[var7] + (double)256.0F) / (double)512.0F) > (double)1.0F) {
                    var10 = 1.0F;
                }

                double var11;
                if ((var11 = var81.depthNoiseBuffer[var7] / (double)8000.0F) < (double)0.0F) {
                    var11 = -var11;
                }

                if ((var11 = var11 * (double)3.0F - (double)3.0F) < (double)0.0F) {
                    if ((var11 = var11 / (double)2.0F) < (double)-1.0F) {
                        var11 = -1.0F;
                    }

                    var11 /= 1.4;
                    var10 = 0.0F;
                } else {
                    if (var11 > (double)1.0F) {
                        var11 = 1.0F;
                    }

                    var11 /= 6.0F;
                }

                var10 += 0.5F;
                var11 = var11 * (double)17.0F / (double)16.0F;
                double var12 = (double)8.5F + var11 * (double)4.0F;
                ++var7;

                for(int var13 = 0; var13 < vertical + 1; ++var13) {
                    double var14;
                    if ((var14 = ((double)var13 - var12) * (double)12.0F / var10) < (double)0.0F) {
                        var14 *= 4.0F;
                    }

                    double var15 = var81.minLimitPerlinNoiseBuffer[m11] / (double)512.0F;
                    double var16 = var81.maxLimitPerlinNoiseBuffer[m11] / (double)512.0F;
                    double var17;
                    double var18;
                    if ((var18 = (var81.perlinNoiseBuffer[m11] / (double)10.0F + (double)1.0F) / (double)2.0F) < (double)0.0F) {
                        var17 = var15;
                    } else if (var18 > (double)1.0F) {
                        var17 = var16;
                    } else {
                        var17 = var15 + (var16 - var15) * var18;
                    }

                    var17 -= var14;
                    var6[m11] = var17;
                    ++m11;
                }
            }
        }

        this.heightMap = var6;

        for(int var19 = 0; var19 < 4; ++var19) {
            for(int var20 = 0; var20 < 4; ++var20) {
                for(int var87 = 0; var87 < vertical; ++var87) {
                    double var90 = this.heightMap[(var19 * 5 + var20) * (vertical + 1) + var87];
                    double var92 = this.heightMap[(var19 * 5 + var20 + 1) * (vertical + 1) + var87];
                    double var21 = this.heightMap[((var19 + 1) * 5 + var20) * (vertical + 1) + var87];
                    double var22 = this.heightMap[((var19 + 1) * 5 + var20 + 1) * (vertical + 1) + var87];
                    double var23 = this.heightMap[(var19 * 5 + var20) * (vertical + 1) + var87 + 1];
                    double var24 = this.heightMap[(var19 * 5 + var20 + 1) * (vertical + 1) + var87 + 1];
                    double var25 = this.heightMap[((var19 + 1) * 5 + var20) * (vertical + 1) + var87 + 1];
                    double var26 = this.heightMap[((var19 + 1) * 5 + var20 + 1) * (vertical + 1) + var87 + 1];

                    for(int var27 = 0; var27 < 8; ++var27) {
                        double var28 = (double)var27 / (double)8.0F;
                        double var29 = var90 + (var23 - var90) * var28;
                        double var30 = var92 + (var24 - var92) * var28;
                        double var31 = var21 + (var25 - var21) * var28;
                        double var32 = var22 + (var26 - var22) * var28;

                        for(int var33 = 0; var33 < 4; ++var33) {
                            double var34 = (double)var33 / (double)4.0F;
                            double var35 = var29 + (var31 - var29) * var34;
                            double var36 = var30 + (var32 - var30) * var34;
                            int shiftY = MathHelper.ceilLog2(Config.BWOConfig.world.worldHeightLimit.getIntValue());
                            int shiftXZ = shiftY + 4;
                            int var37 = var33 + (var19 << 2) << shiftXZ | (var20 << 2) << shiftY | (var87 << 3) + var27;

                            for(int var38 = 0; var38 < 4; ++var38) {
                                double var39 = (double)var38 / (double)4.0F;
                                double var40 = var35 + (var36 - var35) * var39;
                                double var53 = temperatures[(var19 * 4 + var33) * 16 + var20 * 4 + var38];
                                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                int var41 = 0;
                                if ((var87 << 3) + var27 < 64) {
                                    if (!this.theme.equals("Hell") && (var53 < temp && this.betaFeatures || this.theme.equals("Winter")) && var87 * 8 + var27 >= 63) {
                                        var41 = Block.ICE.id;
                                    } else {
                                        var41 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                    }
                                }

                                if (var40 > (double)0.0F) {
                                    var41 = Block.STONE.id;
                                }

                                blocks[var37] = (byte)var41;
                                var37 += Config.BWOConfig.world.worldHeightLimit.getIntValue();
                            }
                        }
                    }
                }
            }
        }

        for(int var83 = 0; var83 < 16; ++var83) {
            for(int var42 = 0; var42 < 16; ++var42) {
                double var88 = (chunkX << 4) + var83;
                double var91 = (chunkZ << 4) + var42;
                Biome var82 = biomes[var83 + var42 * 16];
                boolean var43 = this.perlinNoise2.create(var88 * (double)0.03125F, var91 * (double)0.03125F, 0.0F) + this.random.nextDouble() * 0.2 > (double)0.0F;
                boolean var44 = this.perlinNoise2.create(var91 * (double)0.03125F, 109.0134, var88 * (double)0.03125F) + this.random.nextDouble() * 0.2 > (double)3.0F;
                int var45 = (int)(this.perlinNoise3.sample(var88 * (double)0.03125F * (double)2.0F, var91 * (double)0.03125F * (double)2.0F) / (double)3.0F + (double)3.0F + this.random.nextDouble() * (double)0.25F);
                int var47 = -1;
                int var48;
                int var49;

                if (this.betaFeatures) {
                    var48 = this.theme.equals("Hell") ? (var82.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var82.topBlockId) : var82.topBlockId;
                    var49 = var82.soilBlockId;
                } else {
                    var48 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                    var49 = Block.DIRT.id;
                }

                for(int var50 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var50 >= 0; --var50) {
                    int var46 = (var42 * 16 + var83) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var50;
                    if (blocks[var46] == 0) {
                        var47 = -1;
                    } else if (blocks[var46] == Block.STONE.id) {
                        if (var47 == -1) {
                            if (var45 <= 0) {
                                var48 = 0;
                                var49 = (byte)Block.STONE.id;
                            } else if (var50 >= 60 && var50 <= 65) {
                                if (this.betaFeatures) {
                                    var48 = this.theme.equals("Hell") ? (var82.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var82.topBlockId) : var82.topBlockId;
                                    var49 = var82.soilBlockId;
                                } else {
                                    var48 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                                    var49 = Block.DIRT.id;
                                }
                                if (var44) {
                                    var48 = 0;
                                }

                                if (var44) {
                                    var49 = Block.GRAVEL.id;
                                }

                                if (var43) {
                                    var48 = this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id;
                                }

                                if (var43) {
                                    var49 = this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id;
                                }
                            }

                            if (var50 < 64 && var48 == 0) {
                                var48 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                            }

                            var47 = var45;
                            if (var50 >= 63) {
                                blocks[var46] = (byte)var48;
                            } else {
                                blocks[var46] = (byte)var49;
                            }
                        } else if (var47 > 0) {
                            --var47;
                            blocks[var46] = (byte)var49;
                            if (var47 == 0 && var49 == Block.SAND.id && this.betaFeatures) {
                                var47 = this.random.nextInt(4);
                                var49 = (byte)Block.SANDSTONE.id;
                            }
                        }
                    }

                    --var46;
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

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (!this.betaFeatures) {
            this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
            int var8 = x << 4;
            x = z << 4;

            for(int var10 = 0; var10 < 20; ++var10) {
                int var5 = var8 + this.random.nextInt(16);
                int var6 = this.random.nextInt(128);
                int var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.COAL_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            for(int var11 = 0; var11 < 10; ++var11) {
                int var5 = var8 + this.random.nextInt(16);
                int var6 = this.random.nextInt(64);
                int var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.IRON_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            if (this.random.nextInt(2) == 0) {
                z = var8 + this.random.nextInt(16);
                int var6 = this.random.nextInt(32);
                int var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.GOLD_ORE.id)).generate(this.world, this.random, z, var6, var7);
            }

            if (this.random.nextInt(8) == 0) {
                z = var8 + this.random.nextInt(16);
                int var6 = this.random.nextInt(16);
                int var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.DIAMOND_ORE.id)).generate(this.world, this.random, z, var6, var7);
            }

            if (this.theme.equals("Woods")) {
                if ((z = (int) (this.forestNoise.sample((double) var8 * (double) 0.5F, (double) x * (double) 0.5F) / (double) 8.0F + this.random.nextDouble() * (double) 4.0F + (double) 4.0F)) <= 0) {
                    z = 30;
                }
            } else {
                if ((z = (int) (this.forestNoise.sample((double) var8 * (double) 0.5F, (double) x * (double) 0.5F) / (double) 8.0F + this.random.nextDouble() * (double) 4.0F + (double) 4.0F)) < 0) {
                    z = 0;
                }
            }

            OakTreeFeature var9 = new OakTreeFeature();
            if (this.random.nextInt(10) == 0) {
                ++z;
            }

            for(int var12 = 0; var12 < z; ++var12) {
                int var5 = var8 + this.random.nextInt(16) + 8;
                int var7 = x + this.random.nextInt(16) + 8;
                var9.generate(this.world, this.random, var5, this.world.getTopY(var5, var7), var7);
            }

            if (this.theme.equals("Paradise")) {
                for(z = 0; z < 12; ++z) {
                    int var5 = var8 + this.random.nextInt(16) + 8;
                    int var6 = this.random.nextInt(128);
                    int var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var5, var6, var7);
                }

                for (z = 0; z < 12; z++) {
                    int var5 = var8 + this.random.nextInt(16) + 8;
                    int var6 = this.random.nextInt(128);
                    int var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var5, var6, var7);

                }
            }

            for(int var5 = var8 + 8; var5 < var8 + 8 + 16; ++var5) {
                for(int var7 = x + 8; var7 < x + 8 + 16; ++var7) {
                    int var6 = this.world.getTopSolidBlockY(var5, var7);
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
                Feature var18 = ((BWOWorld) var6).bwo_getRandomTreeFeatureInfdev611(this.random);
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