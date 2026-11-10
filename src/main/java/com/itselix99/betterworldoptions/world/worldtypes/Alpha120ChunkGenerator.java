package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.world.feature.old.*;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;

import java.util.Random;

public class Alpha120ChunkGenerator extends OldChunkGenerator {
    public Alpha120ChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.scaleNoise = new BWOOctavePerlinNoiseSampler(this.random, 10, true);
        this.depthNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
    }

    protected void initBuffers(int chunkX, int chunkZ, double scale) {
        if (!Config.BWOConfig.world.beachFix) {
            this.sandBuffer = this.perlinNoise2.generateNoise(this.sandBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, scale, scale, 1.0D);
            this.gravelBuffer = this.perlinNoise2.generateNoise(this.gravelBuffer, chunkZ * 16, 109.0134D, chunkX * 16, 16, 1, 16, scale, 1.0D, scale);
            this.depthBuffer = this.perlinNoise3.generateNoise(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, scale * 2.0D, scale * 2.0D, scale * 2.0D);
        }
    }

    protected int[] getTopAndSoilBlock(Biome biome) {
        int topBlock = this.theme.getTopBlock() != -1 ? (biome.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : biome.topBlockId) : biome.topBlockId;
        int soilBlock = this.theme.getSoilBlock() != -1 ? (biome.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : biome.soilBlockId) : biome.soilBlockId;

        return new int[]{topBlock, soilBlock};
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        if (!Config.BWOConfig.world.beachFix) {
            int sandBeach = this.sandBuffer[x + z * 16] + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
            int gravelBeach = this.gravelBuffer[x + z * 16] + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
            int erosion = (int)(this.depthBuffer[x + z * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);

            return new int[]{sandBeach, gravelBeach, erosion};
        } else {
            return super.generateBeachesAndErosion(chunkX, chunkZ, x, z, scale);
        }
    }

    protected boolean generateBedrock(int y) {
        return y <= this.world.getBottomY() + this.random.nextInt(5);
    }

    protected int getWaterBlock(double temperature, int y, boolean buildSurfaces) {
        double temp = this.theme.isCold() ? 1.1D : 0.5D;
        if (buildSurfaces) {
            if (Config.BWOConfig.world.beachFix && temperature < temp && y == this.worldType.getSeaLevel() - 1) {
                return this.theme.getSurfaceLiquidBlock();
            } else {
                return this.theme.getLiquidBlock();
            }
        } else {
            if (temperature < temp && y == this.worldType.getSeaLevel() - 1) {
                return this.theme.getSurfaceLiquidBlock();
            } else {
                return this.theme.getLiquidBlock();
            }
        }
    }

    protected int[] generateSandstone(int remainingSurfaceDepth, int soilBlock) {
        return new int[]{remainingSurfaceDepth, soilBlock};
    }

    protected void generateCaves(int chunkX, int chunkZ, byte[] blocks) {
        this.cave.place(this, this.world, chunkX, chunkZ, blocks);

        if (Config.BWOConfig.world.ravineGeneration && Config.BWOConfig.world.ravineGenWithOldFeatures) {
            this.ravine.place(this, this.world, chunkX, chunkZ, blocks);
        }
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        double[] var12 = this.world.method_1781().temperatureMap;
        double[] var13 = this.world.method_1781().downfallMap;
        this.scaleNoiseBuffer = this.scaleNoise.generateNoise(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121D, 1.121D, 0.5D);
        this.depthNoiseBuffer = this.depthNoise.generateNoise(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0D, 200.0D, 0.5D);
        this.perlinNoiseBuffer = this.perlinNoise1.generateNoise(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoise(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoise(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / sizeX;

        for(int var17 = 0; var17 < sizeX; ++var17) {
            int var18 = var17 * var16 + var16 / 2;

            for(int var19 = 0; var19 < sizeZ; ++var19) {
                double islandOffset = this.getIslandOffset(x + var17, z + var19, -200.0D);

                int var20 = var19 * var16 + var16 / 2;
                double var21 = var12[var18 * 16 + var20];
                double var23 = var13[var18 * 16 + var20] * var21;
                double var25 = 1.0D - var23;
                var25 *= var25;
                var25 *= var25;
                var25 = 1.0D - var25;
                double var27 = (this.scaleNoiseBuffer[var15] + 256.0D) / 512.0D;
                var27 *= var25;
                if (var27 > 1.0D) {
                    var27 = 1.0D;
                }

                double var29 = this.depthNoiseBuffer[var15] / 8000.0D;
                if (var29 < 0.0D) {
                    var29 = -var29 * 0.3D;
                }

                var29 = var29 * 3.0D - 2.0D;
                if (var29 < 0.0D) {
                    var29 /= 2.0D;
                    if (var29 < -1.0D) {
                        var29 = -1.0D;
                    }

                    var29 /= 1.4D;
                    var29 /= 2.0D;
                    var27 = 0.0D;
                } else {
                    if (var29 > 1.0D) {
                        var29 = 1.0D;
                    }

                    var29 /= 8.0D;
                }

                if (var27 < 0.0D) {
                    var27 = 0.0D;
                }

                var27 += 0.5D;
                var29 = var29 * 17.0D / 16.0D;
                double var31 = 17.0D / 2.0D + var29 * 4.0D;
                ++var15;

                for(int var33 = 0; var33 < sizeY; ++var33) {
                    double var34;
                    double var36 = ((double)var33 - var31) * 12.0D / var27;
                    if (var36 < 0.0D) {
                        var36 *= 4.0D;
                    }

                    double var38 = this.minLimitPerlinNoiseBuffer[var14] / 512.0D;
                    double var40 = this.maxLimitPerlinNoiseBuffer[var14] / 512.0D;
                    double var42 = (this.perlinNoiseBuffer[var14] / 10.0D + 1.0D) / 2.0D;
                    if (var42 < 0.0D) {
                        var34 = var38;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else if (var42 > 1.0D) {
                        var34 = var40;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var38 += islandOffset;
                            var40 += islandOffset;
                        }

                        var34 = var38 + (var40 - var38) * var42;
                    }

                    var34 -= var36;
                    if (var33 > sizeY - 4) {
                        double var44 = (float)(var33 - (sizeY - 4)) / 3.0D;
                        var34 = var34 * (1.0D - var44) + -10.0D * var44;
                    }

                    heightMap[var14] = var34;
                    ++var14;
                }
            }
        }

        return heightMap;
    }

    protected int getForestNoiseValue(int x, int z) {
        return (int)((this.forestNoise.sample((double)x * 0.5D, (double)z * 0.5D) / 8.0D + this.random.nextDouble() * 4.0D + 4.0D) / 3.0D);
    }

    protected Feature getOldTreeFeature(Biome biome) {
        Feature tree = new OakTreeFeature();
        if(this.random.nextInt(10) == 0) {
            tree = new OldLargeOakTreeFeature();
        }

        if(biome == Biome.RAINFOREST && this.random.nextInt(3) == 0) {
            tree = new OldLargeOakTreeFeature();
        }

        return tree;
    }

    protected void preOldFeaturesDecorate(ChunkSource source, int x, int z) {
        Random random = new Random();
        int var4 = x * 16;
        int var5 = z * 16;
        random.setSeed(this.world.getSeed());
        long var7 = random.nextLong() / 2L * 2L + 1L;
        long var9 = random.nextLong() / 2L * 2L + 1L;
        random.setSeed((long)x * var7 + (long)z * var9 ^ this.world.getSeed());

        int var13;
        int var14;
        int var15;
        int var16;
        for(var13 = 0; var13 < 8; ++var13) {
            var14 = var4 + random.nextInt(16) + 8;
            var15 = random.nextInt(128);
            var16 = var5 + random.nextInt(16) + 8;
            (new DungeonFeature()).generate(this.world, random, var14, var15, var16);
        }
    }

    protected void oldFeaturesDecorate(ChunkSource source, int x, int z) {
        SandBlock.fallInstantly = true;
        this.preOldFeaturesDecorate(source, x, z);
        int var4 = x * 16;
        int var5 = z * 16;
        Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
        this.random.setSeed(this.world.getSeed());
        long var7 = this.random.nextLong() / 2L * 2L + 1L;
        long var9 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)x * var7 + (long)z * var9 ^ this.world.getSeed());
        this.placeStructures(this.world, this.random, x, z);

        int var13;
        int var14;
        int var15;
        int var16;
        for(var13 = 0; var13 < 8; ++var13) {
            var14 = var4 + this.random.nextInt(16) + 8;
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16) + 8;
            (new OldDungeonFeature()).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 10; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new ClayOreFeature(32)).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 20; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.DIRT.id, 32)).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 10; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.GRAVEL.id, 32)).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 20; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.COAL_ORE.id, 16)).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 20; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(64);
            var16 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.IRON_ORE.id, 8)).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 2; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(32);
            var16 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.GOLD_ORE.id, 8)).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 8; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(16);
            var16 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.REDSTONE_ORE.id, 7)).generate(this.world, this.random, var14, var15, var16);
        }

        for(var13 = 0; var13 < 1; ++var13) {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(16);
            var16 = var5 + this.random.nextInt(16);
            (new OreFeature(Block.DIAMOND_ORE.id, 7)).generate(this.world, this.random, var14, var15, var16);
        }

        var13 = this.getForestNoiseValue(var4, var5);
        var14 = 0;
        if(this.random.nextInt(10) == 0) {
            ++var14;
        }

        if(var6 == Biome.FOREST) {
            var14 += var13 + 5;
        }

        if(var6 == Biome.RAINFOREST) {
            var14 += var13 + 5;
        }

        if(var6 == Biome.SEASONAL_FOREST) {
            var14 += var13 + (this.theme.isDenseWoods() ? 5 : 2);
        }

        if(var6 == Biome.TAIGA) {
            var14 += var13 + 5;
        }

        if(var6 == Biome.DESERT) {
            if (this.theme.isDenseWoods()) {
                var14 += var13 + 5;
            } else {
                var14 -= 20;
            }
        }

        if(var6 == Biome.TUNDRA) {
            if (this.theme.isDenseWoods()) {
                var14 += var13 + 5;
            } else {
                var14 -= 20;
            }
        }

        if(var6 == Biome.PLAINS) {
            if (this.theme.isDenseWoods()) {
                var14 += var13 + 5;
            } else {
                var14 -= 20;
            }
        }

        if (var6 == Biome.SWAMPLAND || var6 == Biome.SHRUBLAND || var6 == Biome.SAVANNA) {
            if (this.theme.isDenseWoods()) {
                var14 += var13 + 5;
            }
        }

        Feature var24 = this.getOldTreeFeature(var6);

        int var17;
        int var18;
        for(var16 = 0; var16 < var14; ++var16) {
            var17 = var4 + this.random.nextInt(16) + 8;
            var18 = var5 + this.random.nextInt(16) + 8;
            var24.prepare(1.0D, 1.0D, 1.0D);
            var24.generate(this.world, this.random, var17, this.world.getTopY(var17, var18), var18);
        }

        int var19;
        if (this.theme.isFloweryLand()) {
            for (var16 = 0; var16 < 24; ++var16) {
                var17 = var4 + this.random.nextInt(16) + 8;
                var18 = this.random.nextInt(128);
                var19 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var17, var18, var19);
            }

            for (var16 = 0; var16 < 24; ++var16) {
                var16 = var4 + this.random.nextInt(16) + 8;
                var17 = this.random.nextInt(128);
                var18 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var16, var17, var18);
            }
        } else {
            for (var16 = 0; var16 < 2; ++var16) {
                var17 = var4 + this.random.nextInt(16) + 8;
                var18 = this.random.nextInt(128);
                var19 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var17, var18, var19);
            }

            if (this.random.nextInt(2) == 0) {
                var16 = var4 + this.random.nextInt(16) + 8;
                var17 = this.random.nextInt(128);
                var18 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var16, var17, var18);
            }
        }

        if(this.random.nextInt(4) == 0) {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(128);
            var18 = var5 + this.random.nextInt(16) + 8;
            (new PlantPatchFeature(Block.BROWN_MUSHROOM.id)).generate(this.world, this.random, var16, var17, var18);
        }

        if(this.random.nextInt(8) == 0) {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(128);
            var18 = var5 + this.random.nextInt(16) + 8;
            (new PlantPatchFeature(Block.RED_MUSHROOM.id)).generate(this.world, this.random, var16, var17, var18);
        }

        for(var16 = 0; var16 < 10; ++var16) {
            var17 = var4 + this.random.nextInt(16) + 8;
            var18 = this.random.nextInt(128);
            var19 = var5 + this.random.nextInt(16) + 8;
            (new SugarCanePatchFeature()).generate(this.world, this.random, var17, var18, var19);
        }

        if(this.random.nextInt(32) == 0) {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(128);
            var18 = var5 + this.random.nextInt(16) + 8;
            (new PumpkinPatchFeature()).generate(this.world, this.random, var16, var17, var18);
        }

        var16 = 0;
        if(var6 == Biome.DESERT) {
            var16 += 10;
        }

        int var20;
        for(var17 = 0; var17 < var16; ++var17) {
            var18 = var4 + this.random.nextInt(16) + 8;
            var19 = this.random.nextInt(128);
            var20 = var5 + this.random.nextInt(16) + 8;
            (new CactusPatchFeature()).generate(this.world, this.random, var18, var19, var20);
        }

        for(var17 = 0; var17 < 50; ++var17) {
            var18 = var4 + this.random.nextInt(16) + 8;
            var19 = this.random.nextInt(this.random.nextInt(120) + 8);
            var20 = var5 + this.random.nextInt(16) + 8;
            (new SpringFeature(this.theme.getFlowingLiquidBlock())).generate(this.world, this.random, var18, var19, var20);
        }

        for(var17 = 0; var17 < 20; ++var17) {
            var18 = var4 + this.random.nextInt(16) + 8;
            var19 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
            var20 = var5 + this.random.nextInt(16) + 8;
            (new SpringFeature(Block.FLOWING_LAVA.id)).generate(this.world, this.random, var18, var19, var20);
        }

        this.temperatures = this.world.method_1781().create(this.temperatures, var4 + 8, var5 + 8, 16, 16);

        for(var17 = var4 + 8; var17 < var4 + 8 + 16; ++var17) {
            for(var18 = var5 + 8; var18 < var5 + 8 + 16; ++var18) {
                var19 = var17 - (var4 + 8);
                var20 = var18 - (var5 + 8);
                int var21 = this.world.getTopSolidBlockY(var17, var18);
                double temperature = this.temperatures[var19 * 16 + var20] - (double)(var21 - 64) / 64.0D * 0.3D;
                double temp = this.theme.isCold() ? 1.1D : 0.5D;
                if(!this.theme.isHot() && temperature < temp && var21 > this.world.getBottomY() && var21 < this.world.getTopY() && this.world.getBlockId(var17, var21, var18) == 0 && this.world.getMaterial(var17, var21 - 1, var18).blocksMovement() && this.world.getMaterial(var17, var21 - 1, var18) != Material.ICE) {
                    this.world.setBlock(var17, var21, var18, Block.SNOW.id);
                }
            }
        }

        SandBlock.fallInstantly = false;
    }

    public void decorate(ChunkSource source, int x, int z) {
        WorldDecoratorImpl.decorate(this.world, x, z);
        this.oldFeaturesDecorate(source, x, z);
    }
}
