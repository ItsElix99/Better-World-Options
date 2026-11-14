package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOBiome;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.world.carver.AdvUpdCaveWorldCarver;
import com.itselix99.betterworldoptions.world.feature.adventureupdate.AdvUpdLakeFeature;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.Release11BiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source.AdvUpdBiomeSource;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.DungeonFeature;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;

public class AdvUpdChunkGenerator extends BWOChunkGenerator {
    protected float[] biomeWeights;
    protected final AdvUpdBiomeProperties advUpdBiomeProperties;
    private final boolean amplified;

    public AdvUpdChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.scaleNoise = new BWOOctavePerlinNoiseSampler(this.random, 10, true);
        this.depthNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.advUpdBiomeProperties = (AdvUpdBiomeProperties) this.biomeProperties;
        this.amplified = this.bwoProperties.bwo_getOptionValue("Amplified", OptionType.WORLD_TYPE_OPTION, false);

        this.cave = new AdvUpdCaveWorldCarver();
        ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        this.biomes = ((AdvUpdBiomeSource) this.world.method_1781()).getNoiseBiomes(this.biomes, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);
        super.buildTerrain(chunkX, chunkZ, blocks, temperatures);
    }

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        super.buildSurfaces(chunkX, chunkZ, blocks, this.biomes);
    }

    protected void initBuffers(int chunkX, int chunkZ, double scale) {
        if (!Config.BWOConfig.world.beachFix) {
            this.depthBuffer = this.perlinNoise3.generateNoiseAdvUpd(this.depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, scale * 2.0D, scale * 2.0D, scale * 2.0D);
        }
    }

    protected Biome getBiome(Biome[] biomes, int x, int z) {
        return biomes[Config.BWOConfig.world.beachFix ? x + z * 16 : z + x * 16];
    }

    protected int[] getTopAndSoilBlock(Biome biome) {
        int topBlock = this.theme.getTopBlock() != -1 ? (biome.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : biome.topBlockId) : biome.topBlockId;
        int soilBlock = this.theme.getSoilBlock() != -1 ? (biome.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : biome.soilBlockId) : biome.soilBlockId;

        if (biome == Release11BiomeProperties.BEACH) {
            topBlock = this.theme.getBeachTopBlock();
            soilBlock = this.theme.getBeachSoilBlock();
        }

        return new int[]{topBlock, soilBlock};
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        int erosion;

        if (!Config.BWOConfig.world.beachFix) {
            erosion = (int)(this.depthBuffer[x + z * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
        } else {
            double worldX = (chunkX << 4) + x;
            double worldZ = (chunkZ << 4) + z;
            erosion = (int)(this.perlinNoise3.sample(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
        }

        return new int[]{0, 0, erosion};
    }

    protected int getWaterBlock(double temperature, int y, boolean buildSurfaces) {
        double temp = this.theme.isCold() ? 10.0D : 0.15D;
        if (buildSurfaces) {
            if (temperature < temp) {
                return this.theme.getSurfaceLiquidBlock();
            } else {
                return this.theme.getLiquidBlock();
            }
        } else {
            return this.theme.getLiquidBlock();
        }
    }

    protected Biome[] getBiomes(int chunkX, int chunkZ) {
        return new Biome[]{};
    }

    protected double[] getTemperatures(int chunkX, int chunkZ) {
        return ((AdvUpdBiomeSource) this.world.method_1781()).getTemperaturesD(chunkX * 16, chunkZ * 16, 16, 16);
    }

    protected void generateCaves(int chunkX, int chunkZ, byte[] blocks) {
        this.cave.place(this, this.world, chunkX, chunkZ, blocks);
        this.ravine.place(this, this.world, chunkX, chunkZ, blocks);
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
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
        this.scaleNoiseBuffer = this.scaleNoise.generateNoiseAdvUpd(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121D, 1.121D, 0.5D);
        this.depthNoiseBuffer = this.depthNoise.generateNoiseAdvUpd(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0D, 200.0D, 0.5D);
        this.perlinNoiseBuffer = this.perlinNoise1.generateNoiseAdvUpd(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var11 / 80.0D, var13 / 160.0D, var11 / 80.0D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoiseAdvUpd(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var11, var13, var11);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoiseAdvUpd(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var11, var13, var11);
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
                        float baseHeight = ((BWOBiome) var26).bwo_getBaseHeight();
                        float heightVariation = ((BWOBiome) var26).bwo_getHeightVariation();

                        if (this.amplified && baseHeight > 0.0F) {
                            baseHeight = 1.0F + baseHeight * 2.0F;
                            heightVariation = 1.0F + heightVariation * 4.0F;
                        }

                        float var27 = this.biomeWeights[var24 + 2 + (var25 + 2) * 5] / (baseHeight + 2.0F);
                        if (((BWOBiome) var26).bwo_getBaseHeight() > ((BWOBiome) var23).bwo_getBaseHeight()) {
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
                    var31 = var31 * 17.0D / 16.0D;
                    double var35 = 17.0D / 2.0D + var31 * 4.0D;
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
                        double var47 = (float) (var30 - (sizeY - 4)) / 3.0D;
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

        ((BWOBiome) var6).bwo_decorate(this.world, this.random, var4, var5);
        AdvUpdNaturalSpawner.populateChunk(this.world, var6, var4 + 8, var5 + 8, 16, 16, this.random);

        for(int var22 = var4 + 8; var22 < var4 + 8 + 16; ++var22) {
            for(int var23 = var5 + 8; var23 < var5 + 8 + 16; ++var23) {
                float temperature = (float) this.world.method_1781().getTemperature(var22, var23);
                int var24 = this.world.getTopSolidBlockY(var22, var23);
                float temp = this.theme.isCold() ? 10.0F : 0.15F;

                if (!this.theme.isHot() && temperature <= temp) {
                    if (var24 > this.world.getBottomY() && var24 < this.world.getTopY() && this.world.getBrightness(LightType.BLOCK, var22, var24 - 1, var23) < 10) {
                        int water = this.world.getBlockId(var22, var24 - 1, var23);
                        if ((water == Block.WATER.id || water == Block.FLOWING_WATER.id) && this.world.getBlockMeta(var22, var24 - 1, var23) == 0) {
                            this.world.setBlock(var22, var24 - 1, var23, Block.ICE.id);
                        }
                    }

                    if (var24 > this.world.getBottomY() && var24 < this.world.getTopY() && this.world.getBrightness(LightType.BLOCK, var22, var24, var23) < 10) {
                        if (this.world.isAir(var22, var24, var23) && this.world.getBlockId(var22, var24 - 1, var23) != 0 && this.world.getBlockId(var22, var24 - 1, var23) != Block.ICE.id && Block.BLOCKS[this.world.getBlockId(var22, var24 - 1, var23)].material.blocksMovement()) {
                            this.world.setBlock(var22, var24, var23, Block.SNOW.id);
                        }
                    }
                }
            }
        }

        SandBlock.fallInstantly = false;
    }
}