package com.itselix99.betterworldoptions.world.worldtypes.mcpe;

import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.util.MTRandom;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.*;

public class MCPEChunkGenerator extends OldChunkGenerator {
    public MCPEChunkGenerator(World world, long seed) {
        super(world, seed);
        this.random = new MTRandom((int) seed);
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
            this.gravelBuffer = this.perlinNoise2.generateNoise(this.gravelBuffer, chunkX * 16, 109.0134D, chunkZ * 16, 16, 1, 16, scale, 1.0D, scale);
            this.depthBuffer = this.perlinNoise3.generateNoise(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, scale * 2.0D, scale * 2.0D, scale * 2.0D);
        }
    }

    protected Biome getBiome(Biome[] biomes, int x, int z) {
        return biomes[Config.BWOConfig.world.beachFix ? z + x * 16 : x + z * 16];
    }

    protected int[] getTopAndSoilBlock(Biome biome) {
        int topBlock = this.theme.getTopBlock() != -1 ? (biome.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : biome.topBlockId) : biome.topBlockId;
        int soilBlock = this.theme.getSoilBlock() != -1 ? (biome.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : biome.soilBlockId) : biome.soilBlockId;

        return new int[]{topBlock, soilBlock};
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        int sandBeach;
        int gravelBeach;
        int erosion;
        if (!Config.BWOConfig.world.beachFix) {
            sandBeach = this.sandBuffer[x + z * 16] + this.random.nextFloat() * 0.2D > 0.0D ? 1 : 0;
            gravelBeach = this.gravelBuffer[x + z * 16] + this.random.nextFloat() * 0.2D > 3.0D ? 1 : 0;
            erosion = (int)(this.depthBuffer[x + z * 16] / 3.0D + 3.0D + this.random.nextFloat() * 0.25D);
        } else {
            double worldX = (chunkX << 4) + x;
            double worldZ = (chunkZ << 4) + z;
            sandBeach = this.perlinNoise2.sample(worldX * scale, worldZ * scale, 0.0D) + this.random.nextFloat() * 0.2D > 0.0D ? 1 : 0;
            gravelBeach = this.perlinNoise2.sample(worldX * scale, 109.0134D, worldZ * scale) + this.random.nextFloat() * 0.2D > 3.0D ? 1 : 0;
            erosion = (int)(this.perlinNoise3.sample(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextFloat() * 0.25D);
        }

        return new int[]{sandBeach, gravelBeach, erosion};
    }

    protected int getBlocksIndex(int x, int z, int y) {
        return (Config.BWOConfig.world.beachFix ? x * 16 + z : z * 16 + x) * this.world.getHeight() + y;
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
        if (remainingSurfaceDepth == 0 && soilBlock == Block.SAND.id) {
            remainingSurfaceDepth = this.random.nextInt(4);
            soilBlock = Block.SANDSTONE.id;

            return new int[]{remainingSurfaceDepth, soilBlock};
        }

        return new int[]{remainingSurfaceDepth, soilBlock};
    }

    protected void setChunkSeed(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341872712L + (long)chunkZ * 132899541L);
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
        if (biome == Biome.FOREST) {
            if (this.random.nextInt(5) == 0) {
                return new BirchTreeFeature();
            } else {
                this.random.nextInt(3);
                return new OakTreeFeature();
            }
        } else if (biome == Biome.TAIGA) {
            return this.random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
        } else if (biome == Biome.RAINFOREST) {
            this.random.nextInt(3);
            return new OakTreeFeature();
        } else {
            this.random.nextInt(10);
            return new OakTreeFeature();
        }
    }

    public void oldFeaturesDecorate(ChunkSource source, int x, int z) {
        SandBlock.fallInstantly = true;
        int var4 = x * 16;
        int var5 = z * 16;
        Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
        this.random.setSeed(this.world.getSeed());
        int var7 = this.random.nextInt() / 2 * 2 + 1;
        int var9 = this.random.nextInt() / 2 * 2 + 1;
        this.random.setSeed((long) x * var7 + (long)z * var9 ^ (int)this.world.getSeed());

        this.placeStructures(this.world, this.random, x, z);

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

        int var37 = this.getForestNoiseValue(var4, var5);
        int var49 = 0;
        if (this.random.nextInt(10) == 0) {
            ++var49;
        }

        if (var6 == Biome.FOREST) {
            var49 += var37 + (this.theme.isDenseWoods() ? 5 : 2);
        }

        if (var6 == Biome.RAINFOREST) {
            var49 += var37 + (this.theme.isDenseWoods() ? 5 : 2);
        }

        if (var6 == Biome.SEASONAL_FOREST) {
            var49 += var37 + (this.theme.isDenseWoods() ? 5 : 1);
        }

        if (var6 == Biome.TAIGA) {
            var49 += var37 + (this.theme.isDenseWoods() ? 5 : 1);
        }

        if (var6 == Biome.DESERT) {
            if (this.theme.isDenseWoods()) {
                var49 += var37 + 5;
            } else {
                var49 -= 20;
            }
        }

        if (var6 == Biome.TUNDRA) {
            if (this.theme.isDenseWoods()) {
                var49 += var37 + 5;
            } else {
                var49 -= 20;
            }
        }

        if (var6 == Biome.PLAINS) {
            if (this.theme.isDenseWoods()) {
                var49 += var37 + 5;
            } else {
                var49 -= 20;
            }
        }

        if (var6 == Biome.SWAMPLAND || var6 == Biome.SHRUBLAND || var6 == Biome.SAVANNA) {
            if (this.theme.isDenseWoods()) {
                var49 += var37 + 5;
            }
        }

        this.generateTrees(var4, var5, var49, this.getOldTreeFeature(var6));

        if (this.theme.isFloweryLand()) {
            for(int var73 = 0; var73 < 24; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + 8;
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            for (int var120 = 0; var120 < 24; var120++) {
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
            (new SpringFeature(this.theme.getFlowingLiquidBlock())).generate(this.world, this.random, var105, var111, var115);
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
                double temperature = this.temperatures[var113 * 16 + var117] - (double)(var22 - 64) / 64.0D * 0.3D;
                double temp = this.theme.isCold() ? 1.1D : 0.5D;
                if (!this.theme.isHot() && temperature < temp && var22 > this.world.getBottomY() && var22 < this.world.getTopY() && this.world.isAir(var96, var22, var107) && this.world.getMaterial(var96, var22 - 1, var107).blocksMovement() && this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
                    this.world.setBlock(var96, var22, var107, Block.SNOW.id);
                }
            }
        }

        SandBlock.fallInstantly = false;
    }
}