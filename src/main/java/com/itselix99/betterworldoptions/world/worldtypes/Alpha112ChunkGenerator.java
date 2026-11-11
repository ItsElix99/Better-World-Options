package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.world.feature.old.*;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.*;

public class Alpha112ChunkGenerator extends OldChunkGenerator {
    public Alpha112ChunkGenerator(World world, long seed) {
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
            this.sandBuffer = this.perlinNoise2.generateNoiseAlpha(this.sandBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, scale, scale, 1.0D);
            this.gravelBuffer = this.perlinNoise2.generateNoiseAlpha(this.gravelBuffer, chunkZ * 16, 109.0134D, chunkX * 16, 16, 1, 16, scale, 1.0D, scale);
            this.depthBuffer = this.perlinNoise3.generateNoiseAlpha(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, scale * 2.0D, scale * 2.0D, scale * 2.0D);
        }
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
        return y <= this.world.getBottomY() + this.random.nextInt(6) - 1;
    }

    protected int getWaterBlock(double temperature, int y, boolean buildSurfaces) {
        double temp = this.theme.isCold() ? 1.1D : 0.5D;
        if (buildSurfaces) {
            if (Config.BWOConfig.world.beachFix && (temperature < temp && !this.oldFeatures || this.theme.isCold()) && y == this.worldType.getSeaLevel() - 1) {
                return this.theme.getSurfaceLiquidBlock();
            } else {
                return this.theme.getLiquidBlock();
            }
        } else {
            return super.getWaterBlock(temperature, y, false);
        }
    }

    protected void generateCaves(int chunkX, int chunkZ, byte[] blocks) {
        this.cave.place(this, this.world, chunkX, chunkZ, blocks);

        if (Config.BWOConfig.world.ravineGeneration) {
            if (!this.oldFeatures || Config.BWOConfig.world.ravineGenWithOldFeatures) {
                this.ravine.place(this, this.world, chunkX, chunkZ, blocks);
            }
        }
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if(heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        this.scaleNoiseBuffer = this.scaleNoise.generateNoiseAlpha(this.scaleNoiseBuffer, x, 0, z, sizeX, 1, sizeZ, 1.0D, 0.0D, 1.0D);
        this.depthNoiseBuffer = this.depthNoise.generateNoiseAlpha(this.depthNoiseBuffer, x, 0, z, sizeX, 1, sizeZ, 100.0D, 0.0D, 100.0D);
        this.perlinNoiseBuffer = this.perlinNoise1.generateNoiseAlpha(this.perlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoiseAlpha(this.minLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoiseAlpha(this.maxLimitPerlinNoiseBuffer, x, 0, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var12 = 0;
        int var13 = 0;

        for(int var14 = 0; var14 < sizeX; ++var14) {
            for(int var15 = 0; var15 < sizeZ; ++var15) {
                double islandOffset = this.getIslandOffset(x + var14, z + var15, -200.0D);

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
                var20 = var20 * 17.0D / 16.0D;
                double var22 = 17.0D / 2.0D + var20 * 4.0D;
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

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var25 += islandOffset;
                        }
                    } else if(var33 > 1.0D) {
                        var25 = var31;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var25 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var29 += islandOffset;
                            var31 += islandOffset;
                        }

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

    protected int getForestNoiseValue(int x, int z) {
        int value = (int)((this.forestNoise.sample((double)x * 0.5D, (double)z * 0.5D) / 8.0D + this.random.nextDouble() * 4.0D + 4.0D) / 3.0D);

        if (this.theme.isDenseWoods()) {
            if(value <= 4) {
                value = 10;
            }
        } else {
            if (value < 0) {
                value = 0;
            }
        }

        if(this.random.nextInt(10) == 0) {
            ++value;
        }

        return value;
    }

    protected Feature getOldTreeFeature() {
        Feature tree = new OakTreeFeature();
        if(this.random.nextInt(10) == 0) {
            tree = new OldLargeOakTreeFeature();
        }

        return tree;
    }

    public void oldFeaturesDecorate(ChunkSource source, int x, int z) {
        SandBlock.fallInstantly = true;
        int var4 = x * 16;
        int var5 = z * 16;
        this.random.setSeed(this.world.getSeed());
        long var6 = this.random.nextLong() / 2L * 2L + 1L;
        long var8 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)x * var6 + (long)z * var8 ^ this.world.getSeed());
        this.placeStructures(this.world, this.random, x, z);

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

        var12 = this.getForestNoiseValue(var4, var5);
        Feature var18 = this.getOldTreeFeature();

        int var16;
        this.generateTrees(var4, var5, var12, var18);

        int var17;
        if (this.theme.isFloweryLand()) {
            for (var14 = 0; var14 < 24; ++var14) {
                var15 = var4 + this.random.nextInt(16) + 8;
                var16 = this.random.nextInt(128);
                var17 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var15, var16, var17);
            }

            for (var14 = 0; var14 < 24; var14++) {
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
            (new SpringFeature(this.theme.getFlowingLiquidBlock())).generate(this.world, this.random, var15, var16, var17);
        }

        for(var14 = 0; var14 < 20; ++var14) {
            var15 = var4 + this.random.nextInt(16) + 8;
            var16 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
            var17 = var5 + this.random.nextInt(16) + 8;
            (new SpringFeature(Block.FLOWING_LAVA.id)).generate(this.world, this.random, var15, var16, var17);
        }

        for (var14 = var4 + 8; var14 < var4 + 8 + 16; ++var14) {
            for (var15 = var5 + 8; var15 < var5 + 8 + 16; ++var15) {
                var16 = this.world.getTopSolidBlockY(var14, var15);
                if (this.theme.isCold() && var16 > this.world.getBottomY() && var16 < this.world.getTopY() && this.world.getBlockId(var14, var16, var15) == 0 && this.world.getMaterial(var14, var16 - 1, var15).blocksMovement() && this.world.getMaterial(var14, var16 - 1, var15) != Material.ICE) {
                    this.world.setBlock(var14, var16, var15, Block.SNOW.id);
                }
            }
        }

        SandBlock.fallInstantly = false;
    }
}
