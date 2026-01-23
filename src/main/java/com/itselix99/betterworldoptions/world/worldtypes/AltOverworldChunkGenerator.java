package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWONoise;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.chunk.EmptyFlattenedChunk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.Random;

public class AltOverworldChunkGenerator implements ChunkSource {
    private final Random random;
    private final OctavePerlinNoiseSampler minLimitPerlinNoise;
    private final OctavePerlinNoiseSampler maxLimitPerlinNoise;
    private final OctavePerlinNoiseSampler perlinNoise1;
    private final OctavePerlinNoiseSampler perlinNoise2;
    private final OctavePerlinNoiseSampler perlinNoise3;
    public OctavePerlinNoiseSampler floatingIslandScale;
    public OctavePerlinNoiseSampler floatingIslandNoise;
    public OctavePerlinNoiseSampler forestNoise;
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
    private final OverworldChunkGenerator defaultChunkGenerator;

    private final String worldType;
    private final String theme;
    private final boolean finiteWorld;
    private final String finiteType;
    private final int sizeX;
    private final int sizeZ;

    public AltOverworldChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.theme = bwoProperties.bwo_getTheme();
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);
        this.sizeX = bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION);
        this.sizeZ = bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION);

        ((BWOWorld) this.world).bwo_setSnow(this.theme.equals("Winter"));
        ((BWOWorld) this.world).bwo_setPrecipitation(!this.theme.equals("Hell") && !this.theme.equals("Paradise"));

        ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        this.minLimitPerlinNoise = new OctavePerlinNoiseSampler(this.random, 16);
        this.maxLimitPerlinNoise = new OctavePerlinNoiseSampler(this.random, 16);
        this.perlinNoise1 = new OctavePerlinNoiseSampler(this.random, 8);
        this.perlinNoise2 = new OctavePerlinNoiseSampler(this.random, 4);
        this.perlinNoise3 = new OctavePerlinNoiseSampler(this.random, 4);
        this.floatingIslandScale = new OctavePerlinNoiseSampler(this.random, 10);
        this.floatingIslandNoise = new OctavePerlinNoiseSampler(this.random, 16);
        this.forestNoise = new OctavePerlinNoiseSampler(this.random, 8);
        this.defaultChunkGenerator = new OverworldChunkGenerator(world, seed);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        if (this.worldType.equals("Farlands")) {
            if (chunkX >= 8) {
                chunkX += 784426;
            } else {
                chunkX -= 784426;
            }
        }

        byte var6 = 4;
        byte var7 = 64;
        int var8 = var6 + 1;
        int vertical = Config.BWOConfig.world.worldHeightLimit.getIntValue() / 8;
        int var9 = vertical + 1;
        int var10 = var6 + 1;
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var6, chunkZ * var6, var8, var9, var10);

        for (int var11 = 0; var11 < var6; ++var11) {
            for (int var12 = 0; var12 < var6; ++var12) {
                for (int var13 = 0; var13 < vertical; ++var13) {
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

                    for (int var32 = 0; var32 < 8; ++var32) {
                        double var33 = 0.25F;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for (int var43 = 0; var43 < 4; ++var43) {
                            int shiftZ = MathHelper.ceilLog2(Config.BWOConfig.world.worldHeightLimit.getIntValue());
                            int shiftX = shiftZ + 4;
                            int var44 = var43 + var11 * 4 << shiftX | var12 * 4 << shiftZ | var13 * 8 + var32;
                            int var45 = Config.BWOConfig.world.worldHeightLimit.getIntValue();
                            double var46 = 0.25F;
                            double var48 = var35;
                            double var50 = (var37 - var35) * var46;

                            for (int var52 = 0; var52 < 4; ++var52) {
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

                                if (var48 > (double) 0.0F) {
                                    var55 = Block.STONE.id;
                                }

                                blocks[var44] = (byte) var55;
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

        for (int var8 = 0; var8 < 16; ++var8) {
            for (int var9 = 0; var9 < 16; ++var9) {
                Biome var10 = biomes[beachFix ? var9 + var8 * 16 : var8 + var9 * 16];
                double x2 = (chunkX << 4) + var8;
                double z2 = (chunkZ << 4) + var9;
                boolean var11;
                boolean var12;
                int var13;
                if (beachFix) {
                    var11 = ((BWONoise)this.perlinNoise2).bwo_generateNoise(x2 * var6, z2 * var6, 0.0D) + this.random.nextDouble() * 0.2 > (double) 0.0F;
                    var12 = ((BWONoise)this.perlinNoise2).bwo_generateNoise(z2 * var6, var6, x2 * var6) + this.random.nextDouble() * 0.2 > (double) 3.0F;
                    var13 = (int)(this.perlinNoise3.sample(x2 * var6 * 2.0D, z2 * var6 * 2.0D) / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                } else {
                    var11 = this.sandBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > (double) 0.0F;
                    var12 = this.gravelBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > (double) 3.0F;
                    var13 = (int) (this.depthBuffer[var8 + var9 * 16] / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                }
                int var14 = -1;
                int var15 = this.theme.equals("Hell") ? (var10.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var10.topBlockId) : var10.topBlockId;
                int var16 = var10.soilBlockId;

                for (int var17 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var17 >= 0; --var17) {
                    int var18 = (beachFix ? var8 * 16 + var9 : var9 * 16 + var8) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var17;
                    if (var17 <= this.random.nextInt(5)) {
                        blocks[var18] = (byte) Block.BEDROCK.id;
                    } else {
                        byte var19 = blocks[var18];
                        if (var19 == 0) {
                            var14 = -1;
                        } else if (var19 == Block.STONE.id) {
                            if (var14 == -1) {
                                if (var13 <= 0) {
                                    var15 = 0;
                                    var16 = (byte) Block.STONE.id;
                                } else if (var17 >= var5 - 4 && var17 <= var5 + 1) {
                                    var15 = this.theme.equals("Hell") ? (var10.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var10.topBlockId) : var10.topBlockId;
                                    var16 = var10.soilBlockId;
                                    if (var12) {
                                        var15 = 0;
                                    }

                                    if (var12) {
                                        var16 = (byte) Block.GRAVEL.id;
                                    }

                                    if (var11) {
                                        var15 = (byte) (this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id);
                                    }

                                    if (var11) {
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
                                if (var14 == 0 && var16 == Block.SAND.id) {
                                    var14 = this.random.nextInt(4);
                                    var16 = (byte) Block.SANDSTONE.id;
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
        this.random.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var5 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, var5);
        this.buildSurfaces(chunkX, chunkZ, var3, this.biomes);
        this.cave.place(this, this.world, chunkX, chunkZ, var3);

        if (Config.BWOConfig.world.ravineGeneration) {
            this.ravine.place(this, this.world, chunkX, chunkZ, var3);
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();

        if (this.finiteWorld) {
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

        boolean isAmplified = this.worldType.equals("Amplified");
        double var8 = 684.412;
        double var10 = 684.412;
        double[] var12 = this.world.method_1781().temperatureMap;
        double[] var13 = this.world.method_1781().downfallMap;
        this.scaleNoiseBuffer = this.floatingIslandScale.create(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121, 1.121, 0.5F);
        this.depthNoiseBuffer = this.floatingIslandNoise.create(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0F, 200.0F, 0.5F);
        this.perlinNoiseBuffer = this.perlinNoise1.create(this.perlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8 / (isAmplified ? (double) 160.0F : (double)80.0F), var10 / (isAmplified ? (double) 240.0F : (double)160.0F), var8 / (isAmplified ? (double) 160.0F : (double)80.0F));
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
                if (var29 < (double) 0.0F) {
                    var29 /= 2.0F;
                    if (var29 < (double) -1.0F) {
                        var29 = -1.0F;
                    }

                    var29 /= 1.4;
                    var29 /= 2.0F;
                    var27 = 0.0F;
                } else {
                    if (var29 > (double) 1.0F) {
                        var29 = 1.0F;
                    }

                    var29 /= 8.0F;
                }

                if (var27 < (double) 0.0F) {
                    var27 = 0.0F;
                }

                var27 += 0.5F;
                if (this.worldType.equals("Amplified")) {
                    double depth = var29;
                    if (depth < 0.0D) depth *= -1.0D;

                    double heightScale = this.world.getHeight() / 128.0D;

                    double factor = 1.0D + depth * ((this.world.getHeight() >> 4) * 2) * heightScale;

                    var27 *= factor;
                    if (var27 < 0.0D) var27 *= -1.0D;
                }
                var29 = var29 * (double) 17 / (double) 16.0F;
                double var31 = (double) 17 / (double) 2.0F + var29 * (double) 4.0F;
                ++var15;

                for (int var33 = 0; var33 < sizeY; ++var33) {
                    double var34;
                    double var36 = ((double) var33 - var31) * (isAmplified ? (double) 20.0F : (double)12.0F) / var27;
                    if (var36 < (double) 0.0F) {
                        var36 *= 4.0F;
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

                    var34 -= var36;
                    if (var33 > sizeY - 4) {
                        double var44 = (float) (var33 - (sizeY - 4)) / 3.0F;
                        var34 = var34 * ((double) 1.0F - var44) + (double) -10.0F * var44;
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
        if (this.finiteWorld) {
            int blockX = x * 16;
            int blockZ = z * 16;

            if (blockX < 0 || blockX >= this.sizeX || blockZ < 0 || blockZ >= this.sizeZ) {
                return;
            }
        }

        this.defaultChunkGenerator.decorate(source, x, z);
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