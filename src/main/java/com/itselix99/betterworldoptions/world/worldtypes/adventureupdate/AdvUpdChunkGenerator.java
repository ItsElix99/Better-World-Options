package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.carver.AdvUpdCaveWorldCarver;
import com.itselix99.betterworldoptions.world.feature.adventureupdate.AdvUpdLakeFeature;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source.AdvUpdBiomeSource;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.util.math.noise.AdvUpdOctavePerlinNoiseSampler;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.DungeonFeature;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;

public class AdvUpdChunkGenerator extends BWOChunkGenerator {
    private final AdvUpdOctavePerlinNoiseSampler minLimitPerlinNoise;
    private final AdvUpdOctavePerlinNoiseSampler maxLimitPerlinNoise;
    private final AdvUpdOctavePerlinNoiseSampler perlinNoise1;
    private final AdvUpdOctavePerlinNoiseSampler perlinNoise3;
    public AdvUpdOctavePerlinNoiseSampler scaleNoise;
    public AdvUpdOctavePerlinNoiseSampler depthNoise;
    public AdvUpdOctavePerlinNoiseSampler forestNoise;
    private double[] heightMap;
    private double[] depthBuffer = new double[256];
    private Biome[] biomes;
    double[] perlinNoiseBuffer;
    double[] minLimitPerlinNoiseBuffer;
    double[] maxLimitPerlinNoiseBuffer;
    double[] scaleNoiseBuffer;
    double[] depthNoiseBuffer;
    float[] biomeWeights;
    protected final AdvUpdBiomeProperties advUpdBiomeProperties;
    private final boolean amplified;

    public AdvUpdChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new AdvUpdOctavePerlinNoiseSampler(this.random, 16);
        this.maxLimitPerlinNoise = new AdvUpdOctavePerlinNoiseSampler(this.random, 16);
        this.perlinNoise1 = new AdvUpdOctavePerlinNoiseSampler(this.random, 8);
        this.perlinNoise3 = new AdvUpdOctavePerlinNoiseSampler(this.random, 4);
        this.scaleNoise = new AdvUpdOctavePerlinNoiseSampler(this.random, 10);
        this.depthNoise = new AdvUpdOctavePerlinNoiseSampler(this.random, 16);
        this.forestNoise = new AdvUpdOctavePerlinNoiseSampler(this.random, 8);
        this.advUpdBiomeProperties = (AdvUpdBiomeProperties) this.biomeProperties;
        this.amplified = this.bwoProperties.bwo_getOptionValue("Amplified", OptionType.WORLD_TYPE_OPTION, false);

        this.cave = new AdvUpdCaveWorldCarver();
        ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks) {
        byte var4 = 4;
        int var5 = this.world.getHeight() / 8;
        int var6 = 63;
        int var7 = var4 + 1;
        int var8 = var5 + 1;
        int var9 = var4 + 1;
        this.biomes = ((AdvUpdBiomeSource) this.world.method_1781()).getNoiseBiomes(this.biomes, chunkX * 4 - 2, chunkZ * 4 - 2, var7 + 5, var9 + 5);
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var4, 0, chunkZ * var4, var7, var8, var9);

        for(int var10 = 0; var10 < var4; ++var10) {
            for(int var11 = 0; var11 < var4; ++var11) {
                for(int var12 = 0; var12 < var5; ++var12) {
                    double var13 = 0.125D;
                    double var15 = this.heightMap[((var10) * var9 + var11) * var8 + var12];
                    int var52 = ((var10) * var9 + var11 + 1) * var8;
                    int var53 = ((var10 + 1) * var9 + var11) * var8;
                    int var54 = ((var10 + 1) * var9 + var11 + 1) * var8;
                    double var17 = this.heightMap[var52 + var12];
                    double var19 = this.heightMap[var53 + var12];
                    double var21 = this.heightMap[var54 + var12];
                    double var23 = (this.heightMap[((var10) * var9 + var11) * var8 + var12 + 1] - var15) * var13;
                    double var25 = (this.heightMap[var52 + var12 + 1] - var17) * var13;
                    double var27 = (this.heightMap[var53 + var12 + 1] - var19) * var13;
                    double var29 = (this.heightMap[var54 + var12 + 1] - var21) * var13;

                    for(int var31 = 0; var31 < 8; ++var31) {
                        double var32 = 0.25D;
                        double var34 = var15;
                        double var36 = var17;
                        double var38 = (var19 - var15) * var32;
                        double var40 = (var21 - var17) * var32;

                        for(int var42 = 0; var42 < 4; ++var42) {
                            int shiftZ = MathHelper.ceilLog2(this.world.getHeight());
                            int shiftX = shiftZ + 4;
                            int var43 = var42 + var10 * 4 << shiftX | var11 * 4 << shiftZ | var12 * 8 + var31;
                            int var44 = this.world.getHeight();
                            var43 -= var44;
                            double var45 = 0.25D;
                            double var49 = (var36 - var34) * var45;
                            double var47 = var34 - var49;

                            for(int var51 = 0; var51 < 4; ++var51) {
                                var47 += var49;
                                if(var47 > 0.0D) {
                                    var43 += var44;
                                    blocks[var43] = (byte)Block.STONE.id;
                                } else if(var12 * 8 + var31 < var6) {
                                    var43 += var44;
                                    blocks[var43] = (byte) this.theme.getLiquidBlock();
                                } else {
                                    var43 += var44;
                                    blocks[var43] = 0;
                                }
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
        byte var5 = 63;
        double var6 = 1.0D / 32.0D;
        boolean beachFix = Config.BWOConfig.world.beachFix;

        if (!beachFix) {
            this.depthBuffer = this.perlinNoise3.create(this.depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);
        }

        float[] temperatures = ((AdvUpdBiomeSource) this.world.method_1781()).getTemperatures(chunkX * 16, chunkZ * 16, 16, 16);

        for (int var8 = 0; var8 < 16; ++var8) {
            for (int var9 = 0; var9 < 16; ++var9) {
                float temperature = temperatures[var9 + var8 * 16];
                Biome var10 = biomes[var9 + var8 * 16];
                double x2 = (chunkX << 4) + var8;
                double z2 = (chunkZ << 4) + var9;
                int var11;
                int var12 = -1;
                int var13 = this.theme.getTopBlock() != -1 ? (var10.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : var10.topBlockId) : var10.topBlockId;
                int var14 = this.theme.getSoilBlock() != -1 ? (var10.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : var10.soilBlockId) : var10.soilBlockId;

                if (beachFix) {
                    var11 = (int) (this.perlinNoise3.sample(x2 * var6 * 2.0D, z2 * var6 * 2.0D) / (double) 3.0F + (double) 3.0F + this.random.nextDouble() * (double) 0.25F);
                } else {
                    var11 = (int) (this.depthBuffer[var8 + var9 * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                }

                for (int var15 = this.world.getTopY() - 1; var15 >= this.world.getBottomY(); --var15) {
                    int var16 = (var9 * 16 + var8) * this.world.getHeight() + var15;
                    if (var15 <= this.world.getBottomY() + this.random.nextInt(5)) {
                        blocks[var16] = (byte) Block.BEDROCK.id;
                    } else {
                        int var17 = blocks[var16];
                        if (var17 == 0) {
                            var12 = -1;
                        } else if (var17 == Block.STONE.id) {
                            if (var12 == -1) {
                                if (var11 <= 0) {
                                    var13 = 0;
                                    var14 = (byte) Block.STONE.id;
                                } else if (var15 >= var5 - 4 && var15 <= var5 + 1) {
                                    var13 = this.theme.getTopBlock() != -1 ? (var10.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : var10.topBlockId) : var10.topBlockId;
                                    var14 = this.theme.getSoilBlock() != -1 ? (var10.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : var10.soilBlockId) : var10.soilBlockId;
                                }

                                if (var15 < var5 && var13 == 0) {
                                    if (temperature < 0.15F) {
                                        var13 = (byte) this.theme.getSurfaceLiquidBlock();
                                    } else {
                                        var13 = (byte) this.theme.getLiquidBlock();
                                    }
                                }

                                var12 = var11;
                                if (var15 >= var5 - 1) {
                                    blocks[var16] = (byte) var13;
                                } else {
                                    blocks[var16] = (byte) var14;
                                }
                            } else if (var12 > 0) {
                                --var12;
                                blocks[var16] = (byte) var14;
                                if (var12 == 0 && var14 == Block.SAND.id) {
                                    var12 = this.random.nextInt(4);
                                    var14 = (byte) Block.SANDSTONE.id;
                                }
                            }
                        }
                    }

                    this.buildLCEFiniteWorldLimit(chunkX, chunkZ, var8, var15, var9, blocks, var10);
                }
            }
        }

    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * this.world.getHeight() * 16];
        this.buildTerrain(chunkX, chunkZ, var3);
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        this.buildSurfaces(chunkX, chunkZ, var3, this.biomes);
        this.cave.place(this, this.world, chunkX, chunkZ, var3);
        this.ravine.place(this, this.world, chunkX, chunkZ, var3);

        this.buildStructures(this, this.world, chunkX, chunkZ, var3);

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();

        return this.getLimitChunkFiniteWorld(chunkX, chunkZ, var3, this.finiteWorldType, flattenedChunk);
    }

    private double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        if (this.biomeWeights == null) {
            this.biomeWeights = new float[25];

            for (int var8 = -2; var8 <= 2; ++var8) {
                for (int var9 = -2; var9 <= 2; ++var9) {
                    float var10 = 10.0F / MathHelper.sqrt((float) (var8 * var8 + var9 * var9) + 0.2F);
                    this.biomeWeights[var8 + 2 + (var9 + 2) * 5] = var10;
                }
            }
        }

        double var11 = 684.412D;
        double var13 = 684.412D;
        this.scaleNoiseBuffer = this.scaleNoise.create(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121D, 1.121D, 0.5D);
        this.depthNoiseBuffer = this.depthNoise.create(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0D, 200.0D, 0.5D);
        this.perlinNoiseBuffer = this.perlinNoise1.create(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var11 / 80.0D, var13 / 160.0D, var11 / 80.0D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.create(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var11, var13, var11);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.create(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var11, var13, var11);
        int var15 = 0;
        int var16 = 0;

        for (int var17 = 0; var17 < sizeX; ++var17) {
            for (int var18 = 0; var18 < sizeZ; ++var18) {
                double islandOffset = this.getIslandOffset(x + var17, z + var18, -200.0D);
                float var19 = 0.0F;
                float var20 = 0.0F;
                float var21 = 0.0F;
                byte var22 = 2;
                Biome var23 = this.biomes[var17 + 2 + (var18 + 2) * (sizeX + 5)];

                for (int var24 = -var22; var24 <= var22; ++var24) {
                    for (int var25 = -var22; var25 <= var22; ++var25) {
                        Biome var26 = this.biomes[var17 + var24 + 2 + (var18 + var25 + 2) * (sizeX + 5)];
                        float baseHeight = this.advUpdBiomeProperties.getHeight(var26)[0];
                        float heightVariation = this.advUpdBiomeProperties.getHeight(var26)[1];

                        if (this.amplified && baseHeight > 0.0F) {
                            baseHeight = 1.0F + baseHeight * 2.0F;
                            heightVariation = 1.0F + heightVariation * 4.0F;
                        }

                        float var27 = this.biomeWeights[var24 + 2 + (var25 + 2) * 5] / (baseHeight + 2.0F);
                        if (this.advUpdBiomeProperties.getHeight(var26)[0] > this.advUpdBiomeProperties.getHeight(var23)[0]) {
                            var27 /= 2.0F;
                        }

                        var19 += heightVariation * var27;
                        var20 += baseHeight * var27;
                        var21 += var27;
                    }
                }

                var19 /= var21;
                var20 /= var21;
                var19 = var19 * 0.9F + 0.1F;
                var20 = (var20 * 4.0F - 1.0F) / 8.0F;
                double var28 = this.depthNoiseBuffer[var16] / 8000.0D;
                if (var28 < 0.0D) {
                    var28 = -var28 * 0.3D;
                }

                var28 = var28 * 3.0D - 2.0D;
                if (var28 < 0.0D) {
                    var28 /= 2.0D;
                    if (var28 < -1.0D) {
                        var28 = -1.0D;
                    }

                    var28 /= 1.4D;
                    var28 /= 2.0D;
                } else {
                    if (var28 > 1.0D) {
                        var28 = 1.0D;
                    }

                    var28 /= 8.0D;
                }

                ++var16;

                for (int var30 = 0; var30 < sizeY; ++var30) {
                    double var31 = var20;
                    double var33 = var19;
                    var31 += var28 * 0.2D;
                    var31 = var31 * (double) 17 / 16.0D;
                    double var35 = (double) 17 / 2.0D + var31 * 4.0D;
                    double var37;
                    double var10000 = ((double) var30 - var35) * 12.0D * 128.0D;
                    double var39 = var10000 / 128.0D / var33;
                    if (var39 < 0.0D) {
                        var39 *= 4.0D;
                    }

                    double var41 = this.minLimitPerlinNoiseBuffer[var15] / 512.0D;
                    double var43 = this.maxLimitPerlinNoiseBuffer[var15] / 512.0D;
                    double var45 = (this.perlinNoiseBuffer[var15] / 10.0D + 1.0D) / 2.0D;
                    if (var45 < 0.0D) {
                        var37 = var41;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var37 += islandOffset;
                        }
                    } else if (var45 > 1.0D) {
                        var37 = var43;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var37 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var41 += islandOffset;
                            var43 += islandOffset;
                        }

                        var37 = var41 + (var43 - var41) * var45;
                    }

                    var37 -= var39;
                    if (var30 > sizeY - 4) {
                        double var47 = (float) (var30 - (sizeY - 4)) / 3.0F;
                        var37 = var37 * (1.0D - var47) + -10.0D * var47;
                    }

                    heightMap[var15] = var37;
                    ++var15;
                }
            }
        }

        return heightMap;
    }

    public void decorate(ChunkSource source, int x, int z) {
        WorldDecoratorImpl.decorate(this.world, x, z);
        SandBlock.fallInstantly = true;
        int var4 = x * 16;
        int var5 = z * 16;
        Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
        this.random.setSeed(this.world.getSeed());
        long var7 = this.random.nextLong() / 2L * 2L + 1L;
        long var9 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long) x * var7 + (long) z * var9 ^ this.world.getSeed());
        boolean var11;

        var11 = this.placeStructures(this.world, this.random, x, z);

        if (!var11 && this.random.nextInt(4) == 0) {
            int var12 = var4 + this.random.nextInt(16) + 8;
            int var13 = this.random.nextInt(this.world.getTopY());
            int var14 = var5 + this.random.nextInt(16) + 8;
            (new AdvUpdLakeFeature(this.theme.getLiquidBlock())).generate(this.world, this.random, var12, var13, var14);
        }

        if (!var11 && this.random.nextInt(8) == 0) {
            int var15 = var4 + this.random.nextInt(16) + 8;
            int var16 = this.random.nextInt(this.random.nextInt(this.world.getTopY() - 8) + 8);
            int var17 = var5 + this.random.nextInt(16) + 8;
            if (var16 < 63 || this.random.nextInt(10) == 0) {
                (new AdvUpdLakeFeature(Block.LAVA.id)).generate(this.world, this.random, var15, var16, var17);
            }
        }

        for (int var18 = 0; var18 < 8; ++var18) {
            int var19 = var4 + this.random.nextInt(16) + 8;
            int var20 = this.random.nextInt(this.world.getTopY());
            int var21 = var5 + this.random.nextInt(16) + 8;
            (new DungeonFeature()).generate(this.world, this.random, var19, var20, var21);
        }

        this.advUpdBiomeProperties.decorate(this.world, var6, this.random, var4, var5);

        for(int var22 = var4 + 8; var22 < var4 + 8 + 16; ++var22) {
            for(int var23 = var5 + 8; var23 < var5 + 8 + 16; ++var23) {
                float temperature = (float) this.world.method_1781().getTemperature(var22, var23);
                int var24 = this.world.getTopSolidBlockY(var22, var23);

                if (temperature <= 0.15F || this.theme.isCold()) {
                    if (var24 > this.world.getBottomY() && var24 < this.world.getTopY() && this.world.getBrightness(LightType.BLOCK, var22, var24 - 1, var23) < 10) {
                        int water = this.world.getBlockId(var22, var24 - 1, var23);
                        if ((water == Block.WATER.id || water == Block.FLOWING_WATER.id) && this.world.getBlockMeta(var22, var24 - 1, var23) == 0) {
                            this.world.setBlock(var22, var24 - 1, var23, Block.ICE.id);
                        }
                    }

                    if (var24 > this.world.getBottomY() && var24 < this.world.getTopY() && this.world.getBrightness(LightType.BLOCK, var22, var24, var23) < 10) {
                        if (this.world.isAir(var22, var24, var23) && Block.SNOW.canPlaceAt(this.world, var22, var24, var23) && this.world.getBlockId(var22, var24 - 1, var23) != 0 && this.world.getBlockId(var22, var24 - 1, var23) != Block.ICE.id && Block.BLOCKS[this.world.getBlockId(var22, var24 - 1, var23)].material.blocksMovement()) {
                            this.world.setBlock(var22, var24, var23, Block.SNOW.id);
                        }
                    }
                }
            }
        }

        AdvUpdNaturalSpawner.populateChunk(this.world, var6, var4 + 8, var5 + 8, 16, 16, this.random);
        SandBlock.fallInstantly = false;
    }
}