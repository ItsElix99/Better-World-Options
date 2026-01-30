package com.itselix99.betterworldoptions.world.worldtypes.infdev611;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.chunk.EmptyFlattenedChunk;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.infdev611.util.math.noise.OctavePerlinNoiseSamplerInfdev611;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
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
    private Biome[] biomes;
    private OverworldChunkGenerator defaultChunkGenerator;

    private final String worldType;
    private final boolean oldFeatures;
    private final String theme;
    private final boolean finiteWorld;
    private final String finiteType;
    private final int sizeX;
    private final int sizeZ;

    public Infdev611ChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        new Random(seed);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.oldFeatures = bwoProperties.bwo_isOldFeatures();
        this.theme = bwoProperties.bwo_getTheme();
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);
        this.sizeX = bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION);
        this.sizeZ = bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION);

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

        if (!this.oldFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        } else {
            switch (this.theme) {
                case "Hell" -> BetterWorldOptions.Infdev.setFogColor(1049600);
                case "Paradise" -> BetterWorldOptions.Infdev.setFogColor(13033215);
                case "Woods" -> BetterWorldOptions.Infdev.setFogColor(5069403);
                default -> BetterWorldOptions.Infdev.setFogColor(11587839);
            }
        }

        this.minLimitPerlinNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 16);
        this.maxLimitPerlinNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 16);
        this.perlinNoise1 = new OctavePerlinNoiseSamplerInfdev611(this.random, 8);
        this.perlinNoise2 = new OctavePerlinNoiseSamplerInfdev611(this.random, 4);
        this.perlinNoise3 = new OctavePerlinNoiseSamplerInfdev611(this.random, 4);
        this.scaleNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 10);
        this.depthNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 16);
        this.forestNoise = new OctavePerlinNoiseSamplerInfdev611(this.random, 8);
        this.defaultChunkGenerator = new OverworldChunkGenerator(world, seed);
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
                double worldEdgeFactor = 1.0D;

                int nx = ((chunkX << 2) + var8) * 4;
                int nz = ((chunkZ << 2) + var9) * 4;

                double dx = Math.abs(nx);
                double dz = Math.abs(nz);

                int halfSizeX = this.sizeX / 2;
                int halfSizeZ = this.sizeZ / 2;

                double limitX = halfSizeX + 18.0D;
                double limitZ = halfSizeZ + 18.0D;

                if (halfSizeX == 32) limitX += 12.0D;
                if (halfSizeZ == 32) limitZ += 12.0D;

                double falloff = 50.0D;

                if (this.finiteType.equals("LCE")) {
                    double edgeX = limitX - dx;
                    double edgeZ = limitZ - dz;

                    double factorX = edgeX / falloff;
                    double factorZ = edgeZ / falloff;

                    factorX = Math.max(0.0D, Math.min(1.0D, factorX));
                    factorZ = Math.max(0.0D, Math.min(1.0D, factorZ));

                    worldEdgeFactor = Math.min(factorX, factorZ);
                } else if (this.finiteType.equals("Indev Island")) {
                    falloff = 100.0D;

                    double nxNorm = dx / limitX;
                    double nzNorm = dz / limitZ;

                    double radial = Math.sqrt(nxNorm * nxNorm + nzNorm * nzNorm);
                    double falloffRadial = falloff / (Math.sqrt(limitX * limitX + limitZ * limitZ));

                    double start = 1.0D - falloffRadial;

                    double t = (radial - start) / (1.0D - start);
                    t = Math.max(0.0D, Math.min(1.0D, t));

                    worldEdgeFactor = 1.0D - t;
                }

                double islandOffset = -200.0D * (1.0D - worldEdgeFactor);

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

                        if (this.finiteWorld && !this.finiteType.equals("MCPE")){
                            var17 += islandOffset;
                        }
                    } else if (var18 > (double)1.0F) {
                        var17 = var16;

                        if (this.finiteWorld && !this.finiteType.equals("MCPE")){
                            var17 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteType.equals("MCPE")){
                            var15 += islandOffset;
                            var16 += islandOffset;
                        }

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
                                    if (!this.theme.equals("Hell") && (var53 < temp && !this.oldFeatures || this.theme.equals("Winter")) && var87 * 8 + var27 >= 63) {
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

        boolean beachFix = Config.BWOConfig.world.beachFix;

        for(int var83 = 0; var83 < 16; ++var83) {
            for(int var42 = 0; var42 < 16; ++var42) {
                double var88 = (chunkX << 4) + var83;
                double var91 = (chunkZ << 4) + var42;
                Biome var82 = biomes[var42 + var83 * 16];
                boolean var43 = this.perlinNoise2.create(var88 * (double)0.03125F, var91 * (double)0.03125F, 0.0F) + this.random.nextDouble() * 0.2D > (double)0.0F;
                boolean var44 = this.perlinNoise2.create(var91 * (double)0.03125F, 109.0134D, var88 * (double)0.03125F) + this.random.nextDouble() * 0.2D > (double)3.0F;
                int var45 = (int)(this.perlinNoise3.sample(var88 * (double)0.03125F * (double)2.0F, var91 * (double)0.03125F * (double)2.0F) / (double)3.0F + (double)3.0F + this.random.nextDouble() * (double)0.25F);
                int var47 = -1;
                int var48;
                int var49;

                if (!this.oldFeatures) {
                    var48 = this.theme.equals("Hell") ? (var82.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var82.topBlockId) : var82.topBlockId;
                    var49 = var82.soilBlockId;
                } else {
                    var48 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                    var49 = Block.DIRT.id;
                }

                for(int var50 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var50 >= 0; --var50) {
                    int var46 = (var83 * 16 + var42) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var50;
                    if (blocks[var46] == 0) {
                        var47 = -1;
                    } else if (blocks[var46] == Block.STONE.id) {
                        if (var47 == -1) {
                            if (var45 <= 0) {
                                var48 = 0;
                                var49 = (byte)Block.STONE.id;
                            } else if (var50 >= 60 && var50 <= 65) {
                                if (!this.oldFeatures) {
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

                            double[] temperatureMap = this.world.method_1781().temperatureMap;
                            double temperature = temperatureMap[var42 + var83 * 16];

                            if (var50 < 64 && var48 == 0) {
                                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                if (beachFix && !this.theme.equals("Hell") && (temperature < temp && !this.oldFeatures || this.theme.equals("Winter")) && var50 == 63) {
                                    var48 = Block.ICE.id;
                                } else {
                                    var48 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                }
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
                            if (var47 == 0 && var49 == Block.SAND.id && !this.oldFeatures) {
                                var47 = this.random.nextInt(4);
                                var49 = (byte)Block.SANDSTONE.id;
                            }
                        }
                    }

                    --var46;

                    if (this.finiteWorld && this.finiteType.equals("LCE")) {
                        int index = (var83 * 16 + var42) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var50;
                        double minX = -this.sizeX / 2.0D;
                        double maxX = this.sizeX / 2.0D - 1.0D;
                        double minZ = -this.sizeZ / 2.0D;
                        double maxZ = this.sizeZ / 2.0D - 1.0D;

                        boolean limit = (var88 == minX && var91 >= minZ && var91 <= maxZ) || (var88 == maxX && var91 >= minZ && var91 <= maxZ) || (var91 == minZ && var88 >= minX && var88 <= maxX) || (var91 == maxZ && var88 >= minX && var88 <= maxX);
                        boolean limit2 = var88 < minX || var88 > maxX || var91 < minZ || var91 > maxZ;
                        if (var50 <= 55) {
                            if (limit) {
                                if (var50 >= 53) {
                                    blocks[index] = this.oldFeatures ? (byte) Block.DIRT.id : var82.soilBlockId;
                                } else {
                                    blocks[index] = (byte) Block.STONE.id;
                                }
                            } else if (limit2) {
                                blocks[index] = (byte) Block.STONE.id;
                            }
                        }

                        if (var50 <= this.random.nextInt(5)) {
                            if (limit) {
                                blocks[index] = (byte) Block.BEDROCK.id;
                            } else if (limit2) {
                                blocks[index] = (byte) Block.BEDROCK.id;
                            }
                        }

                        boolean limit3 = var88 <= minX || var88 >= maxX || var91 <= minZ || var91 >= maxZ;
                        if (var50 > 55 && var50 <= 63 && limit3) {
                            blocks[index] = (byte) (this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id);

                            if (this.theme.equals("Winter") && var50 == 63) {
                                blocks[index] = (byte) Block.ICE.id;
                            }
                        }

                        if (var50 >= 64 && limit3) {
                            blocks[index] = (byte) 0;
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

        if (this.finiteWorld && this.finiteType.equals("MCPE")) {
            int blockX = chunkX * 16;
            int blockZ = chunkZ * 16;

            if (blockX < 0 || blockX >= this.sizeX || blockZ < 0 || blockZ >= this.sizeZ) {
                return new EmptyFlattenedChunk(this.world, chunkX, chunkZ);
            }
        }

        return flattenedChunk;
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.finiteWorld) {
            int blockX = x * 16;
            int blockZ = z * 16;

            if (blockX < 0 || blockX >= this.sizeX || blockZ < 0 || blockZ >= this.sizeZ) {
                return;
            }
        }

        if (this.oldFeatures) {
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
                if ((z = (int) (this.forestNoise.sample((double) var8 * (double) 0.5F, (double) x * (double) 0.5F) / (double) 8.0F + this.random.nextDouble() * (double) 4.0F + (double) 4.0F)) <= 4) {
                    z = 10;
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
                    if(this.theme.equals("Winter") && var6 > 0 && var6 < this.world.dimension.getHeight() && this.world.getBlockId(var5, var6, var7) == 0 && this.world.getMaterial(var5, var6 - 1, var7).isSolid() && this.world.getMaterial(var5, var6 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var5, var6, var7, Block.SNOW.id);
                    }
                }
            }
        } else {
            this.defaultChunkGenerator.decorate(source, x, z);
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