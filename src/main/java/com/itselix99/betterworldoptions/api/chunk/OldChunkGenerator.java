package com.itselix99.betterworldoptions.api.chunk;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.feature.old.OldOakTreeFeature;
import com.itselix99.betterworldoptions.world.feature.old.OldOreFeature;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlantPatchFeature;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;

public class OldChunkGenerator extends BWOChunkGenerator {
    public OldChunkGenerator(World world, long seed) {
        super(world, seed);
    }

    protected void initBuffers(int chunkX, int chunkZ, double scale) {
    }

    protected Biome getBiome(Biome[] biomes, int x, int z) {
        return biomes[z + x * 16];
    }

    protected int[] getTopAndSoilBlock(Biome biome) {
        int topBlock;
        int soilBlock;

        if (!this.oldFeatures) {
            topBlock = this.theme.getTopBlock() != -1 ? (biome.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : biome.topBlockId) : biome.topBlockId;
            soilBlock = this.theme.getSoilBlock() != -1 ? (biome.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : biome.soilBlockId) : biome.soilBlockId;
        } else {
            topBlock = this.theme.getTopBlock() != -1 ? this.theme.getTopBlock() : Block.GRASS_BLOCK.id;
            soilBlock = this.theme.getSoilBlock() != -1 ? this.theme.getSoilBlock() : Block.DIRT.id;
        }

        return new int[]{topBlock, soilBlock};
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        double worldX = (chunkX << 4) + x;
        double worldZ = (chunkZ << 4) + z;
        int sandBeach = this.perlinNoise2.sample(worldX * scale, worldZ * scale, 0.0D) + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
        int gravelBeach = this.perlinNoise2.sample(worldZ * scale, 109.0134D, worldX * scale) + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
        int erosion = (int)(this.perlinNoise3.sample(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);

        return new int[]{sandBeach, gravelBeach, erosion};
    }

    protected int getBlocksIndex(int x, int z, int y) {
        return (x * 16 + z) * this.world.getHeight() + y;
    }

    protected boolean generateBedrock(int y) {
        return !this.oldFeatures && y <= this.world.getBottomY() + this.random.nextInt(5);
    }

    protected int getWaterBlock(double temperature, int y, boolean buildSurfaces) {
        double temp = this.theme.isCold() ? 1.1D : 0.5D;
        if ((temperature < temp && !this.oldFeatures || this.theme.isCold()) && y == this.worldType.getSeaLevel() - 1) {
            return this.theme.getSurfaceLiquidBlock();
        } else {
            return this.theme.getLiquidBlock();
        }
    }

    protected int[] generateSandstone(int remainingSurfaceDepth, int soilBlock) {
        if (!this.oldFeatures) {
            return super.generateSandstone(remainingSurfaceDepth, soilBlock);
        }

        return new int[]{remainingSurfaceDepth, soilBlock};
    }

    protected void generateCaves(int chunkX, int chunkZ, byte[] blocks) {
        if (!this.oldFeatures) {
            this.cave.place(this, this.world, chunkX, chunkZ, blocks);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (!this.oldFeatures || Config.BWOConfig.world.ravineGenWithOldFeatures) {
                this.ravine.place(this, this.world, chunkX, chunkZ, blocks);
            }
        }
    }

    protected int getForestNoiseValue(int x, int z) {
        return 0;
    }

    protected Feature getOldTreeFeature(Biome biome) {
        return this.getOldTreeFeature();
    }

    protected Feature getOldTreeFeature() {
        return new OldOakTreeFeature();
    }

    protected void preOldFeaturesDecorate(ChunkSource source, int x, int z) {
    }

    protected void oldFeaturesDecorate(ChunkSource source, int x, int z) {
        this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
        this.placeStructures(this.world, this.random, x, z);
        int var4 = x << 4;
        int var5 = z << 4;

        for(int var6 = 0; var6 < 20; ++var6) {
            int var7 = var4 + this.random.nextInt(16);
            int var8 = this.random.nextInt(128);
            int var9 = var5 + this.random.nextInt(16);
            (new OldOreFeature(Block.COAL_ORE.id)).generate(this.world, this.random, var7, var8, var9);
        }

        for(int var10 = 0; var10 < 10; ++var10) {
            int var11 = var4 + this.random.nextInt(16);
            int var12 = this.random.nextInt(64);
            int var13 = var5 + this.random.nextInt(16);
            (new OldOreFeature(Block.IRON_ORE.id)).generate(this.world, this.random, var11, var12, var13);
        }

        if(this.random.nextInt(2) == 0) {
            int var14 = var4 + this.random.nextInt(16);
            int var15 = this.random.nextInt(32);
            int var16 = var5 + this.random.nextInt(16);
            (new OldOreFeature(Block.GOLD_ORE.id)).generate(this.world, this.random, var14, var15, var16);
        }

        if(this.random.nextInt(8) == 0) {
            int var17 = var4 + this.random.nextInt(16);
            int var18 = this.random.nextInt(16);
            int var19 = var5 + this.random.nextInt(16);
            (new OldOreFeature(Block.DIAMOND_ORE.id)).generate(this.world, this.random, var17, var18, var19);
        }

        int var20 = this.getForestNoiseValue(var4, var5);
        Feature var21 = this.getOldTreeFeature();

        for(int var22 = 0; var22 < var20; ++var22) {
            int var23 = var4 + this.random.nextInt(16) + 8;
            int var24 = var5 + this.random.nextInt(16) + 8;
            var21.prepare(1.0D, 1.0D, 1.0D);
            var21.generate(this.world, this.random, var23, this.world.getTopY(var23, var24), var24);
        }

        if (this.theme.isFloweryLand()) {
            for(int var25 = 0; var25 < 24; ++var25) {
                int var26 = var4 + this.random.nextInt(16) + 8;
                int var27 = this.random.nextInt(128);
                int var28 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var26, var27, var28);
            }

            for(int var25 = 0; var25 < 24; ++var25) {
                int var26 = var4 + this.random.nextInt(16) + 8;
                int var27 = this.random.nextInt(128);
                int var28 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var26, var27, var28);
            }
        }

        if (this.theme.isCold()) {
            for (int var29 = var4 + 8; var29 < var4 + 8 + 16; ++var29) {
                for (int var30 = var5 + 8; var30 < var5 + 8 + 16; ++var30) {
                    int var31 = this.world.getTopSolidBlockY(var29, var30);
                    if (var31 > this.world.getBottomY() && var31 < this.world.getTopY() && this.world.getBlockId(var29, var31, var30) == 0 && this.world.getMaterial(var29, var31 - 1, var30).blocksMovement() && this.world.getMaterial(var29, var31 - 1, var30) != Material.ICE) {
                        this.world.setBlock(var29, var31, var30, Block.SNOW.id);
                    }
                }
            }
        }
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
            WorldDecoratorImpl.decorate(this.world, x, z);
            this.oldFeaturesDecorate(source, x, z);
        } else {
            super.decorate(source, x, z);
        }
    }
}