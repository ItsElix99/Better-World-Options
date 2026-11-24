package com.itselix99.betterworldoptions.world.worldtypes;

import java.util.*;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.world.feature.InfdevCavesFeature;
import com.itselix99.betterworldoptions.world.feature.old.OldOreFeature;
import com.itselix99.betterworldoptions.world.feature.old.OldPlantPatchFeature;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;

public class Infdev227ChunkGenerator extends OldChunkGenerator {
    protected final Map<Long, List<int[]>> flowersMap = new HashMap<>();
    protected final Map<Long, Integer> maxTerrainHeightMap = new HashMap<>();
    protected final Random brickRandom = new Random();
    protected boolean betterDecoration;

    public Infdev227ChunkGenerator(World world, long seed) {
        super(world, seed);
        this.betterDecoration = this.bwoProperties.bwo_getOptionValue("BetterDecoration", OptionType.WORLD_TYPE_OPTION, false);
        this.initNoise(world, seed);

        BWOChunkGenerator.setSizeLimits(-this.width / 2, this.width / 2, -this.length / 2, this.length / 2);

        if (!this.finiteWorldType.equals("MCPE")) {
            this.finiteWorldType = null;
        }
    }

    protected void initNoise(World world, long seed) {
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(new Random(seed), 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(new Random(seed), 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(new Random(seed), 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(new Random(seed), 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(new Random(seed), 4, true);
        this.scaleNoise = new BWOOctavePerlinNoiseSampler(new Random(seed), 5, true);
        new BWOOctavePerlinNoiseSampler(new Random(seed), 3, true);
        new BWOOctavePerlinNoiseSampler(new Random(seed), 3, true);
        new BWOOctavePerlinNoiseSampler(new Random(seed), 3, true);

        if (this.betterDecoration) {
            this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 5, true);
        }
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 2097152);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];
        List<int[]> flowers = new ArrayList<>();
        int maxTerrainHeight = 0;

        int var5 = chunkX << 4;
        int var6 = chunkZ << 4;
        int var7 = 0;

        int[] surfaceHeights = new int[256];
        for(int var8 = var5; var8 < var5 + 16; ++var8) {
            for(int var9 = var6; var9 < var6 + 16; ++var9) {
                int var10 = var8 / 1024;
                int var11 = var9 / 1024;
                float var12 = (float)(this.minLimitPerlinNoise.sampleInverse((float)var8 / 0.03125F, 0.0F, (float)var9 / 0.03125F) - this.maxLimitPerlinNoise.sampleInverse((float)var8 / 0.015625F, 0.0F, (float)var9 / 0.015625F)) / 512.0F / 4.0F;
                float var13 = (float)this.perlinNoise3.sampleInverse((float)var8 / 4.0F, (float)var9 / 4.0F);
                float var14 = (float)this.scaleNoise.sampleInverse((float)var8 / 8.0F, (float)var9 / 8.0F) / 8.0F;
                var13 = var13 > 0.0F ? (float)(this.perlinNoise1.sampleInverse((float)var8 * 0.25714284F * 2.0F, (float)var9 * 0.25714284F * 2.0F) * (double)var14 / 4.0D) : (float)(this.perlinNoise2.sampleInverse((float)var8 * 0.25714284F, (float)var9 * 0.25714284F) * (double)var14);
                int var15 = (int)(var12 + 64.0F + var13);
                if ((float)this.perlinNoise3.sampleInverse(var8, var9) < 0.0F) {
                    var15 = var15 / 2 << 1;
                    if ((float)this.perlinNoise3.sampleInverse((double) var8 / 5, (double) var9 / 5) < 0.0F) {
                        ++var15;
                    }
                }

                if (var15 > maxTerrainHeight) {
                    maxTerrainHeight = var15;
                }

                int localX = var8 & 15;
                int localZ = var9 & 15;

                surfaceHeights[localX + localZ * 16] = var15;

                int index = (var8 - var5) * 16 + var9 - var6;
                double temperature = temperatures[index];

                for(int var16 = this.world.getBottomY(); var16 < this.world.getTopY(); ++var16) {
                    int var17 = 0;
                    if (this.generateObsidianWall() && (var8 == 0 || var9 == 0) && var16 <= var15 + 2) {
                        var17 = Block.OBSIDIAN.id;
                    }else if (var16 == var15 + 1 && var15 >= 64 && Math.random() < 0.02D) {
                        flowers.add(new int[]{var8, var16, var9});
                    } else if (var16 == var15 && var15 >= 64) {
                        if (!this.oldFeatures) {
                            var17 = Block.STONE.id;
                        } else {
                            var17 = this.theme.getTopBlock() != -1 ? this.theme.getTopBlock() : Block.GRASS_BLOCK.id;
                        }
                    } else if (var16 <= var15 - 2) {
                        var17 = Block.STONE.id;
                    } else if (var16 <= var15) {
                        if (!this.oldFeatures) {
                            var17 = Block.STONE.id;
                        } else {
                            var17 = this.theme.getSoilBlock() != -1 ? this.theme.getSoilBlock() : Block.DIRT.id;
                        }
                    } else if (var16 < 65) {
                        var17 = this.getWaterBlock(temperature, var16, false);
                    }

                    if (this.bwoProperties.bwo_isGenerateStructure(BetterWorldOptions.NAMESPACE.id("brickPyramid"))) {
                        this.brickRandom.setSeed(var10 + var11 * 13871L);
                        int var18 = (var10 << 10) + 128 + this.brickRandom.nextInt(512);
                        int var19 = (var11 << 10) + 128 + this.brickRandom.nextInt(512);
                        var18 = var8 - var18;
                        var19 = var9 - var19;
                        if (var18 < 0) {
                            var18 = -var18;
                        }

                        if (var19 < 0) {
                            var19 = -var19;
                        }

                        if (var19 > var18) {
                            var18 = var19;
                        }

                        var18 = 127 - var18;
                        if (var18 == 255) {
                            var18 = 1;
                        }

                        if (var18 < var15) {
                            var18 = var15;
                        }

                        if (var16 <= var18 && (var17 == 0 || var17 == this.theme.getSurfaceLiquidBlock() || var17 == this.theme.getLiquidBlock() || var17 == this.theme.getFlowingLiquidBlock())) {
                            var17 = Block.BRICKS.id;
                        }

                        if (var17 < 0) {
                            var17 = 0;
                        }
                    }

                    blocks[var7++] = (byte)var17;
                }
            }
        }

        this.flowersMap.put(ChunkPos.toLong(chunkX, chunkZ), flowers);
        this.maxTerrainHeightMap.put(ChunkPos.toLong(chunkX, chunkZ), maxTerrainHeight);
        this.setSurfaceHeightForStructures(chunkX, chunkZ, surfaceHeights);
    }

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        if (!this.oldFeatures) {
            super.buildSurfaces(chunkX, chunkZ, blocks, biomes);
        }
    }

    protected boolean generateObsidianWall() {
        return true;
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        double worldX = (chunkX << 4) + x;
        double worldZ = (chunkZ << 4) + z;
        int sandBeach = this.perlinNoise2.sampleInverse(worldX * scale, worldZ * scale, 0.0D) + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
        int gravelBeach = this.perlinNoise2.sampleInverse(worldZ * scale, 109.0134D, worldX * scale) + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
        int erosion = (int)(this.perlinNoise3.sampleInverse(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);

        return new int[]{sandBeach, gravelBeach, erosion};
    }

    protected void buildStructures(ChunkSource source, World world, int chunkX, int chunkZ, byte[] blocks) {
        for (Structure structure : Structure.getStructuresList()) {
            if (structure.getStructureFeature() != null && this.bwoProperties.bwo_isGenerateStructure(structure.getId())) {
                if (!structure.getId().equals(BetterWorldOptions.NAMESPACE.id("brickPyramid"))) {
                    structure.getStructureFeature().place(source, world, chunkX, chunkZ, blocks);
                }
            }
        }
    }

    public boolean placeStructures(World world, Random random, int chunkX, int chunkZ) {
        boolean largeStructure = false;

        for (Structure structure : Structure.getStructuresList()) {
            if (structure.getStructureFeature() != null && this.bwoProperties.bwo_isGenerateStructure(structure.getId())) {
                if (!structure.getId().equals(BetterWorldOptions.NAMESPACE.id("brickPyramid"))) {
                    boolean valid = structure.getStructureFeature().place(world, random, chunkX, chunkZ);

                    if (structure.isLargeStructure()) {
                        largeStructure = valid;
                    }
                }
            }
        }

        return largeStructure;
    }

    protected int getForestNoiseValue(int x, int z) {
        int value = (int)this.forestNoise.sampleInverse((double)x * 0.0625D, (double)z * 0.0625D) << 3;

        if (this.theme.isDenseWoods() && value <= 4) {
            value = 10;
        }

        return value;
    }

    protected void generateTrees(int x, int z, int treeCount, Feature tree) {
        for(int var5 = 0; var5 < treeCount; ++var5) {
            int var6 = x + this.random.nextInt(16);
            int var7 = z + this.random.nextInt(16);
            int var9 = this.world.getTopY(var6, var7);
            int var8 = var6 + 2;
            int var10 = var7 + 2;
            int var11 = this.random.nextInt(3) + 4;
            boolean var12 = true;
            if(var9 > this.world.getBottomY() && var9 + var11 + 1 <= this.world.getTopY()) {
                int var13;
                int var15;
                int var16;
                int var17;
                for(var13 = var9; var13 <= var9 + 1 + var11; ++var13) {
                    byte var14 = 1;
                    if(var13 == var9) {
                        var14 = 0;
                    }

                    if(var13 >= var9 + 1 + var11 - 2) {
                        var14 = 2;
                    }

                    for(var15 = var8 - var14; var15 <= var8 + var14 && var12; ++var15) {
                        for(var16 = var10 - var14; var16 <= var10 + var14 && var12; ++var16) {
                            if(var13 >= this.world.getBottomY() && var13 < this.world.getTopY()) {
                                var17 = this.world.getBlockId(var15, var13, var16);
                                if(var17 != 0) {
                                    var12 = false;
                                }
                            } else {
                                var12 = false;
                            }
                        }
                    }
                }

                if(!var12) {
                } else {
                    var13 = this.world.getBlockId(var8, var9 - 1, var10);
                    if((var13 == Block.GRASS_BLOCK.id|| var13 == Block.DIRT.id) && var9 < this.world.getTopY() - var11 - 1) {
                        this.world.setBlockWithoutNotifyingNeighbors(var8, var9 - 1, var10, Block.DIRT.id);

                        int var20;
                        for(var20 = var9 - 3 + var11; var20 <= var9 + var11; ++var20) {
                            var15 = var20 - (var9 + var11);
                            var16 = 1 - var15 / 2;

                            for(var17 = var8 - var16; var17 <= var8 + var16; ++var17) {
                                int var19 = var17 - var8;

                                for(var13 = var10 - var16; var13 <= var10 + var16; ++var13) {
                                    int var18 = var13 - var10;
                                    if((Math.abs(var19) != var16 || Math.abs(var18) != var16 || this.random.nextInt(2) != 0 && var15 != 0) && !Block.BLOCKS_OPAQUE[this.world.getBlockId(var17, var20, var13)]) {
                                        this.world.setBlockWithoutNotifyingNeighbors(var17, var20, var13, Block.LEAVES.id);
                                    }
                                }
                            }
                        }

                        for(var20 = 0; var20 < var11; ++var20) {
                            int var21 = this.world.getBlockId(var8, var9 + var20, var10);
                            if(var21 == 0 || var21 == Block.LEAVES.id) {
                                this.world.setBlockWithoutNotifyingNeighbors(var8, var9 + var20, var10, Block.LOG.id);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void infdev325Decoration(int chunkX, int chunkZ, int x, int z) {
        if (this.betterDecoration) {
            int maxTerrainHeight = this.maxTerrainHeightMap.getOrDefault(ChunkPos.toLong(chunkX, chunkZ), this.world.getTopY());
            this.maxTerrainHeightMap.remove(ChunkPos.toLong(chunkX, chunkZ));

            for (int var6 = this.world.getBottomY(); var6 < maxTerrainHeight; var6 += 16) {
                int var7 = (int) (this.forestNoise.sampleInverse((double) x * 0.0625D, (double) var6 * 0.0625D * 4.0D, (double) z * 0.0625D) + (double) (128 - var6) / 64.0D);

                for (int var8 = 0; var8 < var7; ++var8) {
                    int var9 = x + this.random.nextInt(16);
                    int var10 = var6 + this.random.nextInt(16);
                    int var11 = z + this.random.nextInt(16);
                    new InfdevCavesFeature().generate(this.world, this.random, var9, var10, var11);
                }
            }

            this.placeStructures(this.world, this.random, chunkX, chunkZ);

            for (int var6 = 0; var6 < 20; ++var6) {
                int var7 = x + this.random.nextInt(16);
                int var8 = this.random.nextInt(128);
                int var9 = z + this.random.nextInt(16);
                (new OldOreFeature(Block.COAL_ORE.id)).generate(this.world, this.random, var7, var8, var9);
            }

            for (int var10 = 0; var10 < 10; ++var10) {
                int var11 = x + this.random.nextInt(16);
                int var12 = this.random.nextInt(64);
                int var13 = z + this.random.nextInt(16);
                (new OldOreFeature(Block.IRON_ORE.id)).generate(this.world, this.random, var11, var12, var13);
            }

            if (this.random.nextInt(2) == 0) {
                int var14 = x + this.random.nextInt(16);
                int var15 = this.random.nextInt(32);
                int var16 = z + this.random.nextInt(16);
                (new OldOreFeature(Block.GOLD_ORE.id)).generate(this.world, this.random, var14, var15, var16);
            }

            if (this.random.nextInt(8) == 0) {
                int var17 = x + this.random.nextInt(16);
                int var18 = this.random.nextInt(16);
                int var19 = z + this.random.nextInt(16);
                (new OldOreFeature(Block.DIAMOND_ORE.id)).generate(this.world, this.random, var17, var18, var19);
            }

            int var20 = this.getForestNoiseValue(x, z);
            Feature var21 = this.getOldTreeFeature();

            this.generateTrees(x, z, var20, var21);
        }
    }

    protected void oldFeaturesDecorate(ChunkSource source, int x, int z) {
        this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
        int var4 = x << 4;
        int var5 = z << 4;
        if (this.worldType.getId().equals(BetterWorldOptions.NAMESPACE.id("infdev_20100227")) && !this.betterDecoration) {
            this.placeStructures(this.world, this.random, x, z);
        }

        this.infdev325Decoration(x, z, var4, var5);

        List<int[]> flowers = this.flowersMap.get(ChunkPos.toLong(x, z));

        if (flowers != null) {
            this.flowersMap.remove(ChunkPos.toLong(x, z));

            for (int[] flowerPos : flowers) {
                int flowerBlockId = this.theme.isFloweryLand() && this.random.nextInt(2) == 0 ? Block.ROSE.id : Block.DANDELION.id;
                new OldPlantPatchFeature(flowerBlockId).generate(this.world, this.random, flowerPos[0], flowerPos[1], flowerPos[2]);
            }
        } else {
            for (int var27 = var4; var27 < var4 + 16; ++var27) {
                for (int var28 = var5; var28 < var5 + 16; ++var28) {
                    int flowerBlockId = this.theme.isFloweryLand() && this.random.nextInt(2) == 0 ? Block.ROSE.id : Block.DANDELION.id;
                    if (Math.random() < 0.02D) {
                        new OldPlantPatchFeature(flowerBlockId).generate(this.world, this.random, var27, ((BWOWorld) this.world).bwo_getSurfaceHeight(var27, var28), var28);
                    }
                }
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
}