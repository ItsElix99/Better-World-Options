package com.itselix99.betterworldoptions.world.worldtypes.indev223;

import com.itselix99.betterworldoptions.api.chunk.FiniteChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.feature.InfiniteIndevFeatures;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSamplerDistort;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class Indev223ChunkGenerator extends FiniteChunkGenerator {
    private final BWOOctavePerlinNoiseSamplerDistort bwoOctavePerlinNoiseSamplerDistortA;
    private final BWOOctavePerlinNoiseSamplerDistort bwoOctavePerlinNoiseSamplerDistortB;
    private final BWOOctavePerlinNoiseSampler noiseGen1;
    private final BWOOctavePerlinNoiseSampler noiseGen2;
    private final BWOOctavePerlinNoiseSamplerDistort bwoOctavePerlinNoiseSamplerDistortC;
    private final BWOOctavePerlinNoiseSampler noiseGen3;
    private final BWOOctavePerlinNoiseSampler noiseGen4;
    private final BWOOctavePerlinNoiseSampler noiseGen5;
    private final BWOOctavePerlinNoiseSampler noiseGen6;
    private Biome[] biomes;

    private final String singleBiome;
    private final String indevWorldType;

    public Indev223ChunkGenerator(World world, long seed) {
        super(world, seed);
        this.singleBiome = this.bwoProperties.bwo_getSingleBiome();
        this.indevWorldType = this.bwoProperties.bwo_getOptionValue("IndevWorldType", OptionType.WORLD_TYPE_OPTION, "");
        this.bwoOctavePerlinNoiseSamplerDistortA = new BWOOctavePerlinNoiseSamplerDistort(new BWOOctavePerlinNoiseSampler(this.random, 8, false), new BWOOctavePerlinNoiseSampler(this.random, 8, false));
        this.bwoOctavePerlinNoiseSamplerDistortB = new BWOOctavePerlinNoiseSamplerDistort(new BWOOctavePerlinNoiseSampler(this.random, 8, false), new BWOOctavePerlinNoiseSampler(this.random, 8, false));
        this.noiseGen1 = new BWOOctavePerlinNoiseSampler(this.random, 6, false);
        this.noiseGen2 = new BWOOctavePerlinNoiseSampler(this.random, 2, false);
        this.bwoOctavePerlinNoiseSamplerDistortC = new BWOOctavePerlinNoiseSamplerDistort(new BWOOctavePerlinNoiseSampler(this.random, 8, false), new BWOOctavePerlinNoiseSampler(this.random, 8, false));
        this.noiseGen3 = new BWOOctavePerlinNoiseSampler(this.random, 8, false);
        this.noiseGen4 = new BWOOctavePerlinNoiseSampler(this.random, 8, false);
        this.noiseGen5 = new BWOOctavePerlinNoiseSampler(this.random, 8, false);
        this.noiseGen6 = new BWOOctavePerlinNoiseSampler(this.random, 8, false);

        if (this.finiteWorld && this.world.newWorld) {
            this.initLoading("Pregenerating level", 13);
            this.pregenerateTerrain();
        }

        if (!this.finiteWorldType.equals("MCPE")) {
            this.finiteWorldType = null;
        } else {
            this.finiteWorldType = this.indevWorldType;
        }
    }

    protected void pregenerateTerrain() {
        this.setCurrentStage("Raising");
        int[] heightMap = new int[this.width * this.length];

        int surroundingWaterHeight = 32;

        if (this.indevWorldType.equals("Flat")) {
            for (int i = 0; i < heightMap.length; i++) heightMap[i] = 0;

            this.setCurrentStage("Eroding");
        } else {
            for (int x = 0; x < this.width; ++x) {
                this.setPhasePercentage((float)x * 100.0F / (float)(this.width - 1));

                for (int z = 0; z < this.length; ++z) {
                    double ePower = this.bwoOctavePerlinNoiseSamplerDistortB.sample(x << 1, z << 1) / 8.0D;
                    int sharp = this.bwoOctavePerlinNoiseSamplerDistortC.sample(x << 1, z << 1) > 0.0D ? 1 : 0;

                    if (ePower > 2.0D) {
                        int h = ((heightMap[x + z * this.width] - sharp) / 2 << 1) + sharp;
                        heightMap[x + z * this.width] = h;
                    }
                }
            }

            this.setCurrentStage("Eroding");

            for (int x = 0; x < this.width; ++x) {
                double nx = ((double) x / ((double) this.width - 1.0D) - 0.5D) * 2.0D;
                this.setPhasePercentage((float)x * 100.0F / (float)(this.width - 1));

                for (int z = 0; z < this.length; ++z) {
                    double nz = ((double) z / ((double) this.length - 1.0D) - 0.5D) * 2.0D;

                    double low = this.bwoOctavePerlinNoiseSamplerDistortA.sample((float)x * 1.3F, (float)z * 1.3F) / 6.0D - 4.0D;
                    double high = this.bwoOctavePerlinNoiseSamplerDistortB.sample((float)x * 1.3F, (float)z * 1.3F) / 5.0D + 6.0D;
                    if (this.noiseGen1.sampleInverse(x, z) / 8.0D > 0.0D) high = low;

                    double h = Math.max(low, high) / 2.0D;

                    double distance = Math.max(Math.abs(nx), Math.abs(nz));

                    if (this.indevWorldType.equals("Island")) {
                        double radius = Math.sqrt(nx * nx + nz * nz) * (double) 1.2F;
                        double falloff = this.noiseGen2.sampleInverse((float)x * 0.05F, (float)z * 0.05F) / 4.0D + 1.0D;
                        radius = Math.min(Math.max(distance, Math.min(radius, falloff)), 1.0D);
                        radius *= radius;
                        h = h * (1.0D - radius) - radius * 10.0D + 5.0D;
                        if (h < 0.0D) h -= h * h * (double) 0.2F;
                    } else {
                        if (h < 0.0D) {
                            h *= 0.8D;
                        }
                    }

                    heightMap[x + z * this.width] = (int) h;
                }
            }
        }

        this.setCurrentStage("Soiling");

        for (int x = 0; x < this.width; ++x) {
            double nx = ((double) x / ((double) this.width - 1.0D) - 0.5D) * 2.0D;
            this.setPhasePercentage((float)x * 100.0F / (float)(this.width - 1));

            for (int z = 0; z < this.length; ++z) {
                double nz = ((double) z / ((double) this.length - 1.0D) - 0.5D) * 2.0D;

                double radial = Math.max(Math.abs(nx), Math.abs(nz));
                radial *= radial;

                int offset = (int) (this.noiseGen3.sampleInverse(x, z) / 24.0D) - 4;
                int var108;
                int baseHeight = (var108 = heightMap[x + z * this.width] + surroundingWaterHeight) + offset;
                heightMap[x + z * this.width] = Math.max(var108, baseHeight);
                if (heightMap[x + z * this.width] > this.world.getTopY() - 2) {
                    heightMap[x + z * this.width] = this.world.getTopY() - 2;
                }

                if (heightMap[x + z * this.width] <= this.world.getBottomY()) {
                    heightMap[x + z * this.width] = this.world.getBottomY() + 1;
                }

                double var105 = this.noiseGen4.sampleInverse((double) x * 2.3D, (double) z * 2.3D) / 24.0D;
                int var112 = (int) (Math.sqrt(Math.abs(var105)) * Math.signum(var105) * 20.0D) + surroundingWaterHeight;
                var112 = (int) ((double) var112 * (1.0D - radial) + radial * (double) 64);
                if (var112 > surroundingWaterHeight) {
                    var112 = 64;
                }

                for (int y = 0; y < 64; ++y) {
                    int index = (y * this.length + z) * this.width + x;
                    int blockId = 0;

                    if (y <= var108) {
                        blockId = Block.DIRT.id;
                    }

                    if (y <= baseHeight) {
                        blockId = Block.STONE.id;
                    }

                    if (this.indevWorldType.equals("Floating") && y < var112) {
                        blockId = 0;
                    }

                    if (this.fullWorldBlocks.get(index) == 0) {
                        this.fullWorldBlocks.put(index, (byte) blockId);
                    }
                }
            }
        }

        this.setCurrentStage("Growing");

        int beachHeight = surroundingWaterHeight - 1;
        if (this.theme.isFloweryLand()) {
            beachHeight += 2;
        }

        for (int x = 0; x < this.width; ++x) {
            this.setPhasePercentage((float)x * 100.0F / (float)(this.width - 1));

            for (int z = 0; z < this.length; ++z) {
                boolean sand = this.noiseGen5.sampleInverse(x, z) > 8.0D;
                if (this.indevWorldType.equals("Island")) {
                    sand = this.noiseGen5.sampleInverse(x, z) > -8.0D;
                }

                if (this.theme.isFloweryLand()) {
                    sand = this.noiseGen5.sampleInverse(x, z) > -32.0D;
                }

                boolean gravel = this.noiseGen3.sampleInverse(x, z) > 12.0D;
                if ((this.theme.isHot() || this.theme.isDenseWoods()) || (this.singleBiome.equals("Rainforest") || this.singleBiome.equals("Seasonal Forest") || this.singleBiome.equals("Forest") || this.singleBiome.equals("Taiga"))) {
                    sand = this.noiseGen5.sampleInverse(x, z) > -8.0D;
                }

                int surfaceY = Math.max(0, Math.min(63, heightMap[x + z * this.width]));
                int blockIndex = (surfaceY * this.length + z) * this.width + x;
                int aboveBlock = 0;

                if (surfaceY < 63) {
                    aboveBlock = this.fullWorldBlocks.get(((surfaceY + 1) * this.length + z) * this.width + x) & 255;
                }

                if ((aboveBlock == Block.WATER.id || aboveBlock == Block.FLOWING_WATER.id || aboveBlock == 0) && surfaceY <= surroundingWaterHeight - 1 && gravel) {
                    this.fullWorldBlocks.put(blockIndex, (byte) Block.GRAVEL.id);
                }

                if (aboveBlock == 0) {
                    if (surfaceY <= beachHeight && sand) {
                        this.fullWorldBlocks.put(blockIndex, (byte) (this.theme.getBeachTopBlock()));
                    }
                }
            }
        }

        this.setCurrentStage("Carving");

        if (this.oldFeatures) {
            int var56 = this.width * this.length * 64 / 256 / 64 << 1;

            for(int var21 = 0; var21 < var56; ++var21) {
                this.setPhasePercentage((float)var21 * 100.0F / (float)(var56 - 1));
                float var59 = this.random.nextFloat() * (float)this.width;
                float var63 = this.random.nextFloat() * (float)64;
                float var62 = this.random.nextFloat() * (float)this.length;
                int var25 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 200.0F);
                float var66 = this.random.nextFloat() * (float)Math.PI * 2.0F;
                float var68 = 0.0F;
                float var71 = this.random.nextFloat() * (float)Math.PI * 2.0F;
                float var70 = 0.0F;
                float var73 = this.random.nextFloat() * this.random.nextFloat();

                for(int var31 = 0; var31 < var25; ++var31) {
                    var59 += MathHelper.sin(var66) * MathHelper.cos(var71);
                    var62 += MathHelper.cos(var66) * MathHelper.cos(var71);
                    var63 += MathHelper.sin(var71);
                    var66 += var68 * 0.2F;
                    var68 *= 0.9F;
                    var68 += this.random.nextFloat() - this.random.nextFloat();
                    var71 += var70 * 0.5F;
                    var71 *= 0.5F;
                    var70 *= 12.0F / 16.0F;
                    var70 += this.random.nextFloat() - this.random.nextFloat();
                    if(this.random.nextFloat() >= 0.25F) {
                        float var74 = var59 + (this.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
                        float var33 = var63 + (this.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
                        float var77 = var62 + (this.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
                        float var75 = ((float)64 - var33) / (float)64;
                        float var80 = 1.2F + (var75 * 3.5F + 1.0F) * var73;
                        float var78 = MathHelper.sin((float)var31 * (float)Math.PI / (float)var25) * var80;

                        for(int var5 = (int)(var74 - var78); var5 <= (int)(var74 + var78); ++var5) {
                            for(int var81 = (int)(var33 - var78); var81 <= (int)(var33 + var78); ++var81) {
                                for(int var40 = (int)(var77 - var78); var40 <= (int)(var77 + var78); ++var40) {
                                    float var41 = (float)var5 - var74;
                                    float var42 = (float)var81 - var33;
                                    float var48 = (float)var40 - var77;
                                    var41 = var41 * var41 + var42 * var42 * 2.0F + var48 * var48;
                                    if(var41 < var78 * var78 && var5 > 0 && var81 > 0 && var40 > 0 && var5 < this.width - 1 && var81 < 64 - 1 && var40 < this.length - 1) {
                                        int var7 = (var81 * this.length + var40) * this.width + var5;
                                        if(this.fullWorldBlocks.get(var7) == Block.STONE.id) {
                                            this.fullWorldBlocks.put(var7, (byte) 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.oldFeatures) {
            this.generateOres(Block.COAL_ORE.id, 1000, 10, (64 << 2) / 5);
            this.generateOres(Block.IRON_ORE.id, 800, 8, 64 * 3 / 5);
            this.generateOres(Block.GOLD_ORE.id, 500, 6, (64 << 1) / 5);
            this.generateOres(Block.DIAMOND_ORE.id, 800, 2, 64 / 5);
        }

        this.setCurrentStage("Melting");

        if (this.oldFeatures) {
            this.placeUndergroundLakes();
        }

        if(this.indevWorldType.equals("Floating")) {
            surroundingWaterHeight = 2;
        } else if(!this.indevWorldType.equals("Island")) {
            surroundingWaterHeight = 17;
        }

        this.setCurrentStage("Watering");

        if (this.oldFeatures) {
            this.placeLakes();
        }

        if (!this.indevWorldType.equals("Floating")) {
            int liquid = this.theme.getLiquidBlock();

            for (int x = 0; x < this.width; ++x) {
                this.floodFill(x, surroundingWaterHeight - 1, 0, 0, liquid);
                this.floodFill(x, surroundingWaterHeight - 1, this.length - 1, 0, liquid);
            }

            for (int z = 0; z < this.length; ++z) {
                this.floodFill(this.width - 1, surroundingWaterHeight - 1, z, 0, liquid);
                this.floodFill(0, surroundingWaterHeight - 1, z, 0, liquid);
            }
        }

        this.setCurrentStage("Assembling");
        this.calculateLighting(this.width, 64, this.length);

        this.setCurrentStage("Planting");
        this.placeBlockOnDirt(this.oldFeatures ? (this.theme.getTopBlock() != -1 ? this.theme.getTopBlock() : Block.GRASS_BLOCK.id) : Block.SANDSTONE.id);

        if (this.oldFeatures) {
            this.generateTrees();
            if (this.theme.isDenseWoods()) {
                for (int count = 0; count < 50; ++count) {
                    this.generateTrees();
                }
            }
        }

        short var43 = 100;
        if(this.theme.isFloweryLand()) {
            var43 = 1000;
        }

        this.setCurrentStage("Planting");
        if (this.oldFeatures) {
            this.generateFlowersAndMushrooms(Block.DANDELION, var43);
        }
        this.setCurrentStage("Planting");
        if (this.oldFeatures) {
            this.generateFlowersAndMushrooms(Block.ROSE, var43);
        }
        this.setCurrentStage("Planting");
        if (this.oldFeatures) {
            this.generateFlowersAndMushrooms(Block.BROWN_MUSHROOM, 50);
        }
        this.setCurrentStage("Planting");
        if (this.oldFeatures) {
            this.generateFlowersAndMushrooms(Block.RED_MUSHROOM, 50);
        }
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int surroundingWaterHeight = 64;

        if (this.finiteWorld) {
            this.copyFullWorldToChunks(chunkX, chunkZ, blocks, this.indevWorldType.equals("Floating") ? 0 : (!this.oldFeatures ? Block.STONE.id : Block.BEDROCK.id));
        } else {
            int[] heightMap = new int[16 * 16];

            if (this.indevWorldType.equals("Flat")) {
                for (int i = 0; i < heightMap.length; i++) heightMap[i] = 0;
            } else {
                for (int x = 0; x < 16; x++) {
                    int worldX = chunkX * 16 + x;
                    for (int z = 0; z < 16; z++) {
                        int worldZ = chunkZ * 16 + z;

                        double ePower = this.bwoOctavePerlinNoiseSamplerDistortB.sample(worldX << 1, worldZ << 1) / 8.0D;
                        int sharp = this.bwoOctavePerlinNoiseSamplerDistortC.sample(worldX << 1, worldZ << 1) > 0.0D ? 1 : 0;

                        if (ePower > 2.0D) {
                            int h = ((heightMap[x + z * 16] - sharp) / 2 << 1) + sharp;
                            heightMap[x + z * 16] = h;
                        }
                    }
                }

                for (int x = 0; x < 16; x++) {
                    int worldX = chunkX * 16 + x;
                    double nx = 0.0D;

                    for (int z = 0; z < 16; z++) {
                        int worldZ = chunkZ * 16 + z;
                        double nz = 0.0D;

                        double low = this.bwoOctavePerlinNoiseSamplerDistortA.sample((float)worldX * 1.3F, (float)worldZ * 1.3F) / 6.0D - 4.0D;
                        double high = this.bwoOctavePerlinNoiseSamplerDistortB.sample((float)worldX * 1.3F, (float)worldZ * 1.3F) / 5.0D + 6.0D;
                        if (this.noiseGen1.sampleInverse(worldX, worldZ) / 8.0D > 0.0D) high = low;

                        double h = Math.max(low, high) / 2.0D;

                        double distance = 0.0D;

                        if (this.indevWorldType.equals("Island")) {
                            double radius = Math.sqrt(nx * nx + nz * nz) * (double) 1.2F;
                            double falloff = this.noiseGen2.sampleInverse((float)worldX * 0.05F, (float)worldZ * 0.05F) / 4.0D + 1.0D;
                            radius = Math.min(Math.max(distance, Math.min(radius, falloff)), 1.0D);
                            radius *= radius;
                            h = h * (1.0D - radius) - radius * 10.0D + 5.0D;
                            if (h < 0.0D) h -= h * h * (double) 0.2F;
                        } else {
                            if (h < 0.0D) {
                                h *= 0.8D;
                            }
                        }

                        heightMap[x + z * 16] = (int) h;
                    }
                }
            }

            for (int x = 0; x < 16; x++) {
                int worldX = chunkX * 16 + x;
                double nx = 0.0D;

                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;
                    double nz = 0.0D;

                    double radial = Math.max(Math.abs(nx), Math.abs(nz));
                    radial *= radial;

                    int offset = (int) (this.noiseGen3.sampleInverse(worldX, worldZ) / 24.0D) - 4;
                    int var108;
                    int baseHeight = (var108 = heightMap[x + z * 16] + surroundingWaterHeight) + offset;
                    heightMap[x + z * 16] = Math.max(var108, baseHeight);
                    if (heightMap[x + z * 16] > this.world.getTopY() - 2) {
                        heightMap[x + z * 16] = this.world.getTopY() - 2;
                    }

                    if (heightMap[x + z * 16] <= this.world.getBottomY()) {
                        heightMap[x + z * 16] = this.world.getBottomY() + 1;
                    }

                    double var105 = this.noiseGen4.sampleInverse((double) worldX * 2.3D, (double) worldZ * 2.3D) / 24.0D;
                    int var112 = (int) (Math.sqrt(Math.abs(var105)) * Math.signum(var105) * 20.0D) + surroundingWaterHeight;
                    var112 = (int) ((double) var112 * (1.0D - radial) + radial * (double) this.world.getTopY());
                    if (var112 > this.world.getTopY()) {
                        var112 = this.world.getTopY();
                    }

                    int i = x * 16 + z;
                    Biome var18 = biomes[i];

                    for (int y = this.world.getBottomY(); y < this.world.getTopY(); y++) {
                        int index = (x * 16 + z) * this.world.getHeight() + y;
                        int blockId = 0;

                        int waterHeight = surroundingWaterHeight;

                        if (this.indevWorldType.equals("Floating")) {
                            waterHeight = 2;
                        } else if (this.indevWorldType.equals("Inland")) {
                            waterHeight = 49;
                        }

                        if (y == var108 && y >= waterHeight) {
                            if (!this.oldFeatures) {
                                blockId = this.theme.getTopBlock() != -1 ? (byte) (var18.topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : var18.topBlockId) : var18.topBlockId;
                            } else {
                                blockId = this.theme.getTopBlock() != -1 ? this.theme.getTopBlock() : Block.GRASS_BLOCK.id;
                            }
                        } else if (y < var108) {
                            if (!this.oldFeatures) {
                                blockId = this.theme.getSoilBlock() != -1 ? (var18.soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : var18.soilBlockId) : var18.soilBlockId;
                            } else {
                                blockId = this.theme.getSoilBlock() != -1 ? this.theme.getSoilBlock() : Block.DIRT.id;
                            }
                        }

                        if (y <= baseHeight) {
                            blockId = Block.STONE.id;
                        }

                        if (this.indevWorldType.equals("Floating") && y < var112) {
                            blockId = 0;
                        }

                        if (blocks[index] == 0) {
                            blocks[index] = (byte) blockId;
                        }
                    }
                }
            }

            int beachHeight = surroundingWaterHeight - 1;
            if (this.theme.isFloweryLand()) {
                beachHeight += 2;
            }

            for (int x = 0; x < 16; x++) {
                double worldX = chunkX * 16 + x;
                for (int z = 0; z < 16; z++) {
                    double worldZ = chunkZ * 16 + z;
                    boolean sand = this.noiseGen5.sampleInverse(worldX, worldZ) > 8.0D;
                    if (this.indevWorldType.equals("Island")) {
                        sand = this.noiseGen5.sampleInverse(worldX, worldZ) > -8.0D;
                    }

                    if (this.theme.isFloweryLand()) {
                        sand = this.noiseGen5.sampleInverse(worldX, worldZ) > -32.0D;
                    }

                    boolean gravel = this.noiseGen3.sampleInverse(worldX, worldZ) > 12.0D;
                    if ((this.theme.isHot() || this.theme.isDenseWoods()) || (this.singleBiome.equals("Rainforest") || this.singleBiome.equals("Seasonal Forest") || this.singleBiome.equals("Forest") || this.singleBiome.equals("Taiga"))) {
                        sand = this.noiseGen5.sampleInverse(worldX, worldZ) > -8.0D;
                    }

                    int surfaceY = heightMap[x + z * 16];
                    int blockIndex = (x * 16 + z) * this.world.getHeight() + surfaceY;
                    int aboveIndex = blockIndex + 1;
                    int aboveBlock = (aboveIndex < blocks.length) ? (blocks[aboveIndex] & 0xFF) : 0;

                    if ((aboveBlock == Block.WATER.id || aboveBlock == Block.FLOWING_WATER.id || aboveBlock == 0) && surfaceY <= surroundingWaterHeight - 1 && gravel) {
                        blocks[blockIndex] = (byte) Block.GRAVEL.id;
                    }

                    if (aboveBlock == 0) {
                        if (surfaceY <= beachHeight && sand) {
                            blocks[blockIndex] = (byte) (this.theme.getBeachTopBlock());
                        }
                    }
                }
            }

            if (this.oldFeatures) {
                int var83 = 16;

                for (int i = 0; i < var83; i++) {
                    float caveX = this.random.nextFloat() * 16.0F;
                    float caveY = this.random.nextFloat() * 128.0F;
                    float caveZ = this.random.nextFloat() * 16.0F;

                    int steps = (int)((this.random.nextFloat() + this.random.nextFloat()) * 200.0F);

                    float directionYaw = this.random.nextFloat() * (float)Math.PI * 2.0F;
                    float directionPitch = this.random.nextFloat() * (float)Math.PI * 2.0F;

                    float yawOffset = 0.0F;
                    float pitchOffset = 0.0F;
                    float caveSizeRand = this.random.nextFloat() * this.random.nextFloat();

                    for (int step = 0; step < steps; step++) {
                        caveX += MathHelper.sin(directionYaw) * MathHelper.cos(directionPitch);
                        caveZ += MathHelper.cos(directionYaw) * MathHelper.cos(directionPitch);
                        caveY += MathHelper.sin(directionPitch);

                        directionYaw += yawOffset * 0.2F;
                        yawOffset = yawOffset * 0.9F + (this.random.nextFloat() - this.random.nextFloat());

                        directionPitch = (directionPitch + pitchOffset * 0.5F) * 0.5F;
                        pitchOffset = pitchOffset * 0.75F + (this.random.nextFloat() - this.random.nextFloat());

                        if (this.random.nextFloat() < 0.25F) continue;

                        float offsetX = caveX + (this.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
                        float offsetY = caveY + (this.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
                        float offsetZ = caveZ + (this.random.nextFloat() * 4.0F - 2.0F) * 0.2F;

                        float verticalScale = ((float)128 - offsetY) / 128;
                        float radius = 1.2F + (verticalScale * 3.5F + 1.0F) * caveSizeRand;
                        float radiusSin = MathHelper.sin((float)step * (float)Math.PI / steps) * radius;
                        float radiusSq = radiusSin * radiusSin;

                        int minX = (int)(offsetX - radiusSin);
                        int maxX = (int)(offsetX + radiusSin);
                        int minY = (int)(offsetY - radiusSin);
                        int maxY = (int)(offsetY + radiusSin);
                        int minZ = (int)(offsetZ - radiusSin);
                        int maxZ = (int)(offsetZ + radiusSin);

                        for (int cx = minX; cx <= maxX; cx++) {
                            for (int cy = minY; cy <= maxY; cy++) {
                                for (int cz = minZ; cz <= maxZ; cz++) {
                                    if (cx < 0 || cx >= 16) continue;
                                    if (cz < 0 || cz >= 16) continue;
                                    if (cy < this.world.getBottomY() + 1 || cy >= this.world.getTopY() - 1) continue;

                                    float dx = cx - offsetX;
                                    float dy = cy - offsetY;
                                    float dz = cz - offsetZ;

                                    if ((dx * dx + dy * dy * 2.0F + dz * dz) < radiusSq) {
                                        int index = (cx * 16 + cz) * this.world.getHeight() + cy;

                                        if (blocks[index] == Block.STONE.id) {
                                            blocks[index] = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                InfiniteIndevFeatures.placeUndergroundLakes(this.world, this.random, blocks);
                InfiniteIndevFeatures.placeLakes(this.world, this.random, blocks, this.theme);
            }

            if(this.indevWorldType.equals("Floating")) {
                surroundingWaterHeight = 2;
            } else if(!this.indevWorldType.equals("Island")) {
                surroundingWaterHeight = 49;
            }

            int liquid = this.theme.getLiquidBlock();

            for (int x = 0; x < 16; ++x) {
                InfiniteIndevFeatures.floodFill(this.world, x, surroundingWaterHeight - 1, 0, 0, liquid, blocks);
                InfiniteIndevFeatures.floodFill(this.world, x, surroundingWaterHeight - 1, 15, 0, liquid, blocks);
            }

            for (int z = 0; z < 16; ++z) {
                InfiniteIndevFeatures.floodFill(this.world, 15, surroundingWaterHeight - 1, z, 0, liquid, blocks);
                InfiniteIndevFeatures.floodFill(this.world, 0, surroundingWaterHeight - 1, z, 0, liquid, blocks);
            }
        }

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int index = (x * 16 + z) * this.world.getHeight() + (surroundingWaterHeight - 1);
                double var10 = temperatures[x * 16 + z];
                double temp = this.theme.isCold() ? 1.1D : 0.5D;

                if (!this.theme.isHot() && (var10 < temp && !this.oldFeatures || this.theme.isCold()) && blocks[index] == Block.WATER.id) {
                    blocks[index] = (byte) this.theme.getSurfaceLiquidBlock();
                }

                if (this.indevWorldType.equals("Floating")) {
                    blocks[(x * 16 + z) * this.world.getHeight() + 1] = (byte) Block.WATER.id;
                    blocks[(x * 16 + z) * this.world.getHeight()] = (byte) Block.BEDROCK.id;
                }
            }
        }

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for(int y = this.world.getTopY() - 1; y >= this.world.getBottomY(); --y) {
                    int index = (x * 16 + z) * this.world.getHeight() + y;

                    if (y <= this.random.nextInt(5) && !this.indevWorldType.equals("Floating")) {
                        blocks[index] = (byte) Block.BEDROCK.id;
                    }

                    if (blocks[index] == Block.SANDSTONE.id && y > 0) {
                        blocks[index] = this.theme.getTopBlock() != -1 ? (byte) (biomes[x * 16 + z].topBlockId == Block.GRASS_BLOCK.id ? this.theme.getTopBlock() : biomes[x * 16 + z].topBlockId) : biomes[x * 16 + z].topBlockId;

                        int belowIndex = index - 1;
                        int columnStart = (x * 16 + z) * this.world.getHeight();

                        while (belowIndex >= columnStart && blocks[belowIndex] == Block.DIRT.id) {
                            blocks[belowIndex] = this.theme.getSoilBlock() != -1 ? (byte) (biomes[x * 16 + z].soilBlockId == Block.DIRT.id ? this.theme.getSoilBlock() : biomes[x * 16 + z].soilBlockId) : biomes[x * 16 + z].soilBlockId;
                            belowIndex--;
                        }
                    }
                }
            }
        }
    }

    public boolean useSurfaceHeightForStructures() {
        return false;
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * this.world.getHeight() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var4 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var4);
        this.generateCaves(chunkX, chunkZ, var3);

        if (this.oldFeatures && !this.finiteWorld) {
            InfiniteIndevFeatures.placeOre(this.random, Block.COAL_ORE.id, 1000, 10, (128 << 2) / 5, var3);
            InfiniteIndevFeatures.placeOre(this.random, Block.IRON_ORE.id, 800, 8, 128 * 3 / 5, var3);
            InfiniteIndevFeatures.placeOre(this.random, Block.GOLD_ORE.id, 500, 6, (128 << 1) / 5, var3);
            InfiniteIndevFeatures.placeOre(this.random, Block.DIAMOND_ORE.id, 800, 2, 128 / 5, var3);
        }

        this.buildStructures(this, this.world, chunkX, chunkZ, var3);

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();

        return this.getLimitChunkFiniteWorld(chunkX, chunkZ, var3, this.finiteWorldType, flattenedChunk);
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

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
            this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
            int var4 = x << 4;
            int var5 = z << 4;

            this.placeStructures(this.world, this.random, x, z);

            if (!this.finiteWorld) {
                InfiniteIndevFeatures.generateTrees(this.world, this.random, this.theme.isDenseWoods() ? 10 : 1, var4, var5);

                int plantChance = this.theme.isFloweryLand() ? 20 : 4;

                InfiniteIndevFeatures.generatePlant(this.world, this.random, Block.DANDELION, plantChance, var4, var5);
                InfiniteIndevFeatures.generatePlant(this.world, this.random, Block.ROSE, plantChance, var4, var5);
                InfiniteIndevFeatures.generatePlant(this.world, this.random, Block.BROWN_MUSHROOM, 2, var4, var5);
                InfiniteIndevFeatures.generatePlant(this.world, this.random, Block.RED_MUSHROOM, 2, var4, var5);
            }

            if (this.theme.isCold()) {
                for (int var6 = var4 + 8; var6 < var4 + 8 + 16; ++var6) {
                    for (int var7 = var5 + 8; var7 < var5 + 8 + 16; ++var7) {
                        int var8 = this.world.getTopSolidBlockY(var6, var7);
                        if (var8 > this.world.getBottomY() && var8 < this.world.getTopY() && this.world.getBlockId(var6, var8, var7) == 0 && this.world.getMaterial(var6, var8 - 1, var7).blocksMovement() && this.world.getMaterial(var6, var8 - 1, var7) != Material.ICE) {
                            this.world.setBlock(var6, var8, var7, Block.SNOW.id);
                        }
                    }
                }
            }
        } else {
            super.decorate(source, x, z);
        }
    }
}