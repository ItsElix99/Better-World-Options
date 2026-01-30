package com.itselix99.betterworldoptions.world.worldtypes.mcpe;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.chunk.EmptyFlattenedChunk;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.MTRandom;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.math.noise.OctavePerlinNoiseSamplerMCPE;
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
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.Random;

public class MCPEChunkGenerator implements ChunkSource {
    private final Random random;
    private final OctavePerlinNoiseSamplerMCPE minLimitPerlinNoise;
    private final OctavePerlinNoiseSamplerMCPE maxLimitPerlinNoise;
    private final OctavePerlinNoiseSamplerMCPE perlinNoise1;
    private final OctavePerlinNoiseSamplerMCPE perlinNoise2;
    private final OctavePerlinNoiseSamplerMCPE perlinNoise3;
    public OctavePerlinNoiseSamplerMCPE floatingIslandScale;
    public OctavePerlinNoiseSamplerMCPE floatingIslandNoise;
    public OctavePerlinNoiseSamplerMCPE forestNoise;
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
    private OverworldChunkGenerator defaultChunkGenerator;

    private final boolean oldFeatures;
    private final String theme;
    private final boolean finiteWorld;
    private String finiteType;
    private int sizeX;
    private int sizeZ;

    public MCPEChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new MTRandom((int) seed);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.oldFeatures = bwoProperties.bwo_isOldFeatures();
        this.theme = bwoProperties.bwo_getTheme();
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);
        this.sizeX = bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION);
        this.sizeZ = bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION);

        ((BWOWorld) this.world).bwo_setSnow(this.theme.equals("Winter"));
        ((BWOWorld) this.world).bwo_setPrecipitation(!this.theme.equals("Hell") && !this.theme.equals("Paradise"));

        if (!this.oldFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        }

        this.minLimitPerlinNoise = new OctavePerlinNoiseSamplerMCPE(this.random, 16);
        this.maxLimitPerlinNoise = new OctavePerlinNoiseSamplerMCPE(this.random, 16);
        this.perlinNoise1 = new OctavePerlinNoiseSamplerMCPE(this.random, 8);
        this.perlinNoise2 = new OctavePerlinNoiseSamplerMCPE(this.random, 4);
        this.perlinNoise3 = new OctavePerlinNoiseSamplerMCPE(this.random, 4);
        this.floatingIslandScale = new OctavePerlinNoiseSamplerMCPE(this.random, 10);
        this.floatingIslandNoise = new OctavePerlinNoiseSamplerMCPE(this.random, 16);
        this.forestNoise = new OctavePerlinNoiseSamplerMCPE(this.random, 8);
        this.defaultChunkGenerator = new OverworldChunkGenerator(world, seed);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        byte var6 = 4;
        byte var7 = 64;
        int var8 = var6 + 1;
        int vertical = Config.BWOConfig.world.worldHeightLimit.getIntValue() / 8;
        byte var9 = (byte) (vertical + 1);
        int var10 = var6 + 1;
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var6, chunkZ * var6, var8, var9, var10);

        for(int var11 = 0; var11 < var6; ++var11) {
            for(int var12 = 0; var12 < var6; ++var12) {
                for(int var13 = 0; var13 < 16; ++var13) {
                    double var14 = 0.125F;
                    double var16 = this.heightMap[((var11) * var10 + var12) * var9 + var13];
                    int var100 = ((var11) * var10 + var12 + 1) * var9;
                    int var101 = ((var11 + 1) * var10 + var12) * var9;
                    int var102 = ((var11 + 1) * var10 + var12 + 1) * var9;
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
        byte var5 = 64;
        double var6 = 0.03125F;
        boolean beachFix = Config.BWOConfig.world.beachFix;

        if (!beachFix) {
            this.sandBuffer = this.perlinNoise2.create(this.sandBuffer, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, var6, var6, 1.0F);
            this.gravelBuffer = this.perlinNoise2.create(this.gravelBuffer, chunkX * 16, 109.0134, chunkZ * 16, 16, 1, 16, var6, 1.0F, var6);
            this.depthBuffer = this.perlinNoise3.create(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, var6 * (double) 2.0F, var6 * (double) 2.0F, var6 * (double) 2.0F);
        }

        for(int var8 = 0; var8 < 16; ++var8) {
            for(int var9 = 0; var9 < 16; ++var9) {
                Biome var10 = biomes[beachFix ? var9 + var8 * 16 : var8 + var9 * 16];
                double x2 = (chunkX << 4) + var8;
                double z2 = (chunkZ << 4) + var9;
                boolean var11;
                boolean var12;
                int var13;
                if (beachFix) {
                    var11 = this.perlinNoise2.bwo_generateNoise(x2 * var6, z2 * var6, 0.0D) + this.random.nextDouble() * 0.2 > (double) 0.0F;
                    var12 = this.perlinNoise2.bwo_generateNoise(z2 * var6, var6, x2 * var6) + this.random.nextDouble() * 0.2 > (double) 3.0F;
                    var13 = (int) (this.perlinNoise3.sample(x2 * var6 * 2.0D, z2 * var6 * 2.0D) / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                } else {
                    var11 = this.sandBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > (double) 0.0F;
                    var12 = this.gravelBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > (double) 3.0F;
                    var13 = (int) (this.depthBuffer[var8 + var9 * 16] / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                }
                int var14 = -1;
                byte var15 = this.theme.equals("Hell") ? (byte) (var10.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var10.topBlockId) : var10.topBlockId;
                byte var16 = var10.soilBlockId;

                for(int var17 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var17 >= 0; --var17) {
                    int var18 = (beachFix ? var8 * 16 + var9 : var9 * 16 + var8) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var17;
                    if (var17 <= this.random.nextInt(5)) {
                        blocks[var18] = (byte)Block.BEDROCK.id;
                    } else {
                        byte var19 = blocks[var18];
                        if (var19 == 0) {
                            var14 = -1;
                        } else if (var19 == Block.STONE.id) {
                            if (var14 == -1) {
                                if (var13 <= 0) {
                                    var15 = 0;
                                    var16 = (byte)Block.STONE.id;
                                } else if (var17 >= var5 - 4 && var17 <= var5 + 1) {
                                    var15 = this.theme.equals("Hell") ? (byte) (var10.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var10.topBlockId) : var10.topBlockId;
                                    var16 = var10.soilBlockId;
                                    if (var12) {
                                        var15 = 0;
                                    }

                                    if (var12) {
                                        var16 = (byte)Block.GRAVEL.id;
                                    }

                                    if (var11) {
                                        var15 = (byte) (this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id);
                                    }

                                    if (var11) {
                                        var16 = (byte) (this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id);
                                    }
                                }

                                double[] temperatureMap = this.world.method_1781().temperatureMap;
                                double temperature = temperatureMap[var9 + var8 * 16];

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
                                    blocks[var18] = var15;
                                } else {
                                    blocks[var18] = var16;
                                }
                            } else if (var14 > 0) {
                                --var14;
                                blocks[var18] = var16;
                                if (var14 == 0 && var16 == Block.SAND.id) {
                                    var14 = this.random.nextInt(4);
                                    var16 = (byte)Block.SANDSTONE.id;
                                }
                            }
                        }
                    }

                    if (this.finiteWorld && this.finiteType.equals("LCE")) {
                        int index = (var8 * 16 + var9) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var17;
                        double minX = -this.sizeX / 2.0D;
                        double maxX = this.sizeX / 2.0D - 1.0D;
                        double minZ = -this.sizeZ / 2.0D;
                        double maxZ = this.sizeZ / 2.0D - 1.0D;

                        boolean limit = (x2 == minX && z2 >= minZ && z2 <= maxZ) || (x2 == maxX && z2 >= minZ && z2 <= maxZ) || (z2 == minZ && x2 >= minX && x2 <= maxX) || (z2 == maxZ && x2 >= minX && x2 <= maxX);
                        boolean limit2 = x2 < minX || x2 > maxX || z2 < minZ || z2 > maxZ;
                        if (var17 <= 55) {
                            if (limit) {
                                if (var17 >= 53) {
                                    blocks[index] = var10.soilBlockId;
                                } else {
                                    blocks[index] = (byte) Block.STONE.id;
                                }
                            } else if (limit2) {
                                blocks[index] = (byte) Block.STONE.id;
                            }
                        }

                        if (var17 <= this.random.nextInt(5)) {
                            if (limit) {
                                blocks[index] = (byte) Block.BEDROCK.id;
                            } else if (limit2) {
                                blocks[index] = (byte) Block.BEDROCK.id;
                            }
                        }

                        boolean limit3 = x2 <= minX || x2 >= maxX || z2 <= minZ || z2 >= maxZ;
                        if (var17 > 55 && var17 <= 63 && limit3) {
                            blocks[index] = (byte) (this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id);

                            if (this.theme.equals("Winter") && var17 == 63) {
                                blocks[index] = (byte) Block.ICE.id;
                            }
                        }

                        if (var17 >= 64 && limit3) {
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
        this.buildTerrain(chunkX, chunkZ, var3, var5);
        this.buildSurfaces(chunkX, chunkZ, var3, this.biomes);

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
                double worldEdgeFactor = 1.0D;

                int nx = (x + var17) * 4;
                int nz = (z + var19) * 4;

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

                        if (this.finiteWorld && !this.finiteType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else if (var42 > (double)1.0F) {
                        var34 = var40;

                        if (this.finiteWorld && !this.finiteType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteType.equals("MCPE")){
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

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
            SandBlock.fallInstantly = true;
            int var4 = x * 16;
            int var5 = z * 16;
            Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
            this.random.setSeed(this.world.getSeed());
            int var7 = this.random.nextInt() / 2 * 2 + 1;
            int var9 = this.random.nextInt() / 2 * 2 + 1;
            this.random.setSeed((long) x * var7 + (long)z * var9 ^ (int)this.world.getSeed());
            double var11;

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
                var49 += var37 + (this.theme.equals("Woods") ? 5 : 2);
            }

            if (var6 == Biome.RAINFOREST) {
                var49 += var37 + (this.theme.equals("Woods") ? 5 : 2);
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var49 += var37 + (this.theme.equals("Woods") ? 5 : 1);
            }

            if (var6 == Biome.TAIGA) {
                var49 += var37 + (this.theme.equals("Woods") ? 5 : 1);
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
                Feature var18 = ((BWOWorld) var6).bwo_getRandomTreeFeatureMCPE(this.random);
                var18.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
            }

            if (this.theme.equals("Paradise")) {
                for(int var73 = 0; var73 < 12; ++var73) {
                    int var76 = var4 + this.random.nextInt(16) + 8;
                    int var85 = this.random.nextInt(128);
                    int var19 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
                }

                for (int var120 = 0; var120 < 12; var120++) {
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

            int var84 = 0;
            if (var6 == Biome.DESERT) {
                var84 += 5;
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
                    if (!this.theme.equals("Hell") && var23 < (double)temp && var22 > 0 && var22 < this.world.dimension.getHeight() && this.world.isAir(var96, var22, var107) && this.world.getMaterial(var96, var22 - 1, var107).blocksMovement() && this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
                        this.world.setBlock(var96, var22, var107, Block.SNOW.id);
                    }
                }
            }

            SandBlock.fallInstantly = false;
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