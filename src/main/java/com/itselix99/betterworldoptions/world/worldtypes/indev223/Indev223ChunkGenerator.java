package com.itselix99.betterworldoptions.world.worldtypes.indev223;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.feature.IndevFeatures;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise.Distort;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise.OctavePerlinNoiseSamplerIndev223;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class Indev223ChunkGenerator extends BWOChunkGenerator {
    private final Distort distortA;
    private final Distort distortB;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen1;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen2;
    private final Distort distortC;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen3;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen4;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen5;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen6;
    private Biome[] biomes;

    private final String singleBiome;
    private final String indevWorldType;

    public Indev223ChunkGenerator(World world, long seed) {
        super(world, seed);
        this.singleBiome = this.bwoProperties.bwo_getSingleBiome();
        this.indevWorldType = this.bwoProperties.bwo_getStringOptionValue("IndevWorldType", OptionType.WORLD_TYPE_OPTION);
        this.distortA = new Distort(new OctavePerlinNoiseSamplerIndev223(this.random, 8), new OctavePerlinNoiseSamplerIndev223(this.random, 8));
        this.distortB = new Distort(new OctavePerlinNoiseSamplerIndev223(this.random, 8), new OctavePerlinNoiseSamplerIndev223(this.random, 8));
        this.noiseGen1 = new OctavePerlinNoiseSamplerIndev223(this.random, 6);
        this.noiseGen2 = new OctavePerlinNoiseSamplerIndev223(this.random, 2);
        this.distortC = new Distort(new OctavePerlinNoiseSamplerIndev223(this.random, 8), new OctavePerlinNoiseSamplerIndev223(this.random, 8));
        this.noiseGen3 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
        this.noiseGen5 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
        this.noiseGen6 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int[] heightMap = new int[16 * 16];
        int surroundingWaterHeight = 64;

        if (this.indevWorldType.equals("Flat")) {
            for (int i = 0; i < heightMap.length; i++) heightMap[i] = 0;
        } else {
            for (int x = 0; x < 16; x++) {
                int worldX = chunkX * 16 + x;
                double nx = ((double) worldX / (this.sizeX - 1) - 0.5) * 2.0;

                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;
                    double nz = ((double) worldZ / (this.sizeZ - 1) - 0.5) * 2.0;

                    double low = this.distortA.create(worldX * 1.3, worldZ * 1.3) / 6.0 - 4.0;
                    double high = this.distortB.create(worldX * 1.3, worldZ * 1.3) / 5.0 + 6.0;
                    if (this.noiseGen1.create(worldX, worldZ) / 8.0 > 0.0) high = low;

                    double h = Math.max(low, high) / 2.0;

                    double distance = Math.max(Math.abs(nx), Math.abs(nz));

                    if (this.indevWorldType.equals("Island")) {
                        double radius = Math.sqrt(nx * nx + nz * nz) * 1.2;
                        double falloff = this.noiseGen2.create(worldX * 0.05, worldZ * 0.05) / 4.0 + 1.0;
                        radius = Math.min(Math.max(distance, Math.min(radius, falloff)), 1.0);
                        radius *= radius;
                        h = h * (1.0 - radius) - radius * 10.0 + 5.0;
                        if (h < 0.0) h -= h * h * 0.2;
                    } else {
                        if (distance > 1.0 && this.finiteWorld) {
                            h = 0;
                        } else {
                            if (h < 0.0) {
                                h *= 0.8;
                            }
                        }
                    }

                    heightMap[x + z * 16] = (int) h;
                }
            }

            for (int x = 0; x < 16; x++) {
                int worldX = chunkX * 16 + x;
                double nx = ((double) worldX / (this.sizeX - 1) - 0.5) * 2.0;

                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;
                    double nz = ((double) worldZ / (this.sizeZ - 1) - 0.5) * 2.0;

                    double distance = Math.max(Math.abs(nx), Math.abs(nz));

                    double ePower = this.distortB.create(worldX << 1, worldZ << 1) / 8.0;
                    int sharp = this.distortC.create(worldX << 1, worldZ << 1) > 0.0 ? 1 : 0;

                    if (distance > 1.0 && this.finiteWorld) {
                        heightMap[x + z * 16] = 0;
                    } else {
                        if (ePower > 2.0) {
                            int h = ((heightMap[x + z * 16] - sharp) / 2 << 1) + sharp;
                            heightMap[x + z * 16] = h;
                        }
                    }
                }
            }
        }

        for (int x = 0; x < 16; x++) {
            int worldX = chunkX * 16 + x;
            double nx = ((double) worldX / (this.sizeX - 1) - 0.5) * 2.0;

            for (int z = 0; z < 16; z++) {
                int worldZ = chunkZ * 16 + z;
                double nz = ((double) worldZ / (this.sizeZ - 1) - 0.5) * 2.0;

                double radial;
                radial = (radial = Math.max(nx, nz)) * radial * radial;

                int offset = (int) (this.noiseGen3.create(worldX, worldZ) / 24.0) - 4;
                int var108;
                int baseHeight = (var108 = heightMap[x + z * 16] + surroundingWaterHeight) + offset;
                heightMap[x + z * 16] = Math.max(var108, baseHeight);
                if (heightMap[x + z * 16] > Config.BWOConfig.world.worldHeightLimit.getIntValue() - 2) {
                    heightMap[x + z * 16] = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 2;
                }

                if (heightMap[x + z * 16] <= 0) {
                    heightMap[x + z * 16] = 1;
                }

                double var105;
                int var112;
                if ((var112 = (int) ((double) ((int) (Math.sqrt(Math.abs(var105 = this.noiseGen4.create((double) worldX * 2.3, (double) worldZ * 2.3) / (double) 24.0F)) * Math.signum(var105) * (double) 20.0F) + surroundingWaterHeight) * ((double) 1.0F - radial) + radial * (double) Config.BWOConfig.world.worldHeightLimit.getIntValue())) > surroundingWaterHeight) {
                    var112 = Config.BWOConfig.world.worldHeightLimit.getIntValue();
                }

                int lastSandY = -1;

                int i = x * 16 + z;
                Biome var18 = biomes[i];

                for (int y = 0; y < Config.BWOConfig.world.worldHeightLimit.getIntValue(); y++) {
                    int index = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;
                    int blockId = 0;

                    if (y <= var108) {
                        if (!this.oldFeatures) {
                            blockId = var18.soilBlockId;
                        } else {
                            blockId = Block.DIRT.id;
                        }

                    }

                    if (y <= baseHeight) {
                        blockId = Block.STONE.id;
                        lastSandY = y + 1;
                    }

                    if (this.indevWorldType.equals("Floating") && y < var112) {
                        blockId = 0;
                    }

                    if (blocks[index] == 0) {
                        blocks[index] = (byte) blockId;
                    }
                }

                if (lastSandY != -1) {
                    int lastIndex = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + lastSandY;
                    if (!this.oldFeatures && var18 == Biome.DESERT || var18 == Biome.ICE_DESERT) {
                        blocks[lastIndex] = (byte) Block.SANDSTONE.id;
                    }
                }
            }
        }

        int beachHeight = surroundingWaterHeight - 1;
        if (this.theme.equals("Paradise")) {
            beachHeight += 2;
        }

        for (int x = 0; x < 16; x++) {
            double worldX = chunkX * 16 + x;
            for (int z = 0; z < 16; z++) {
                double worldZ = chunkZ * 16 + z;
                boolean sand = this.noiseGen5.create(worldX, worldZ) > 8.0;
                if (this.indevWorldType.equals("Island")) {
                    sand = this.noiseGen5.create(worldX, worldZ) > (double) -8.0F;
                }

                if (this.theme.equals("Paradise")) {
                    sand = this.noiseGen5.create(worldX, worldZ) > (double) -32.0F;
                }

                boolean gravel = this.noiseGen3.create(worldX, worldZ) > 12.0;
                if ((this.theme.equals("Hell") || this.theme.equals("Woods")) || (this.singleBiome.equals("Rainforest") || this.singleBiome.equals("Seasonal Forest") || this.singleBiome.equals("Forest") || this.singleBiome.equals("Taiga"))) {
                    sand = this.noiseGen5.create(worldX, worldZ) > (double) -8.0F;
                }

                int surfaceY = heightMap[x + z * 16];
                int blockIndex = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + surfaceY;
                int aboveIndex = blockIndex + 1;
                int aboveBlock = (aboveIndex < blocks.length) ? (blocks[aboveIndex] & 0xFF) : 0;

                if ((aboveBlock == Block.WATER.id || aboveBlock == Block.FLOWING_WATER.id || aboveBlock == 0) && surfaceY <= surroundingWaterHeight - 1 && gravel) {
                    blocks[blockIndex] = (byte) Block.GRAVEL.id;
                }

                if (aboveBlock == 0) {
                    if (surfaceY <= beachHeight && sand) {
                        blocks[blockIndex] = (byte) (this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id);
                    }
                }
            }
        }

        if (this.oldFeatures) {
            int var83 = 16 * 16 * 128 / 256 / 64 << 1;

            for (int i = 0; i < var83; i++) {
                float caveX = this.random.nextFloat() * 16;
                float caveY = this.random.nextFloat() * 128;
                float caveZ = this.random.nextFloat() * 16;

                int steps = (int) ((this.random.nextFloat() + this.random.nextFloat()) * 200);
                float directionYaw = this.random.nextFloat() * (float) Math.PI * 2.0F;
                float directionPitch = this.random.nextFloat() * (float) Math.PI * 2.0F;

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

                    float verticalScale = ((float) 128 - offsetY) / 128;
                    float radius = 1.2F + (verticalScale * 3.5F + 1.0F) * caveSizeRand;
                    float radiusSq = MathHelper.sin((float) step * (float) Math.PI / steps) * radius;
                    float radiusSq2 = radiusSq * radiusSq;

                    for (int cx = (int) (offsetX - radiusSq); cx <= (int) (offsetX + radiusSq); cx++) {
                        if (cx < 1 || cx >= 15) continue;

                        for (int cy = (int) (offsetY - radiusSq); cy <= (int) (offsetY + radiusSq); cy++) {
                            if (cy < 1 || cy >= Config.BWOConfig.world.worldHeightLimit.getIntValue()) continue;

                            for (int cz = (int) (offsetZ - radiusSq); cz <= (int) (offsetZ + radiusSq); cz++) {
                                if (cz < 1 || cz >= 15) continue;

                                float dx = cx - offsetX;
                                float dy = cy - offsetY;
                                float dz = cz - offsetZ;

                                if ((dx * dx + dy * dy * 2.0F + dz * dz) < radiusSq2) {
                                    int index = (cx * 16 + cz) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + cy;

                                    if (blocks[index] == Block.STONE.id) {
                                        blocks[index] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            IndevFeatures.placeOre(this.random, Block.COAL_ORE.id, 1000, 10, (128 << 2) / 5, blocks);
            IndevFeatures.placeOre(this.random, Block.IRON_ORE.id, 800, 8, 128 * 3 / 5, blocks);
            IndevFeatures.placeOre(this.random, Block.GOLD_ORE.id, 500, 6, (128 << 1) / 5, blocks);
            IndevFeatures.placeOre(this.random, Block.DIAMOND_ORE.id, 800, 2, 128 / 5, blocks);
            IndevFeatures.placeUndergroundLakes(this.random, blocks);
            IndevFeatures.placeLakes(this.random, blocks, this.theme);
        }

        if(this.indevWorldType.equals("Floating")) {
            surroundingWaterHeight = -127;
        } else if(!this.indevWorldType.equals("Island")) {
            surroundingWaterHeight = 49;
        }

        int liquid = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;

        for(int var7 = 0; var7 < 16; ++var7) {
            IndevFeatures.floodFill(var7, surroundingWaterHeight - 1, 0, 0, liquid, blocks);
            IndevFeatures.floodFill(var7, surroundingWaterHeight - 1, 15, 0, liquid, blocks);
        }

        for(int var7 = 0; var7 < 16; ++var7) {
            IndevFeatures.floodFill(15, surroundingWaterHeight - 1, var7, 0, liquid, blocks);
            IndevFeatures.floodFill(0, surroundingWaterHeight - 1, var7, 0, liquid, blocks);
        }
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var4 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var4);

        if (this.finiteWorld && !this.finiteType.equals("MCPE")) {
            BWOChunkGenerator.setSizeLimits(0, this.sizeX, 0, this.sizeZ);
        }

        if (!this.oldFeatures){
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

        String limitMode;
        if (this.finiteType.equals("MCPE")) {
            limitMode = this.finiteType;
        } else {
            limitMode = this.indevWorldType;
        }

        return this.getLimitChunkFiniteWorld(chunkX, chunkZ, 0, this.sizeX, 0, this.sizeZ, var3, limitMode, flattenedChunk);
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
            this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
            int var4 = x << 4;
            int var5 = z << 4;

            IndevFeatures.placeTopBlockOnDirt(this.world, var4, var5, null, this.theme);
            IndevFeatures.generateTrees(this.world, this.random, this.theme.equals("Woods") ? 10 : 1, var4, var5);

            int plantChance = this.theme.equals("Paradise") ? 20 : 4;

            IndevFeatures.generatePlant(this.world, this.random, Block.DANDELION, plantChance, var4, var5);
            IndevFeatures.generatePlant(this.world, this.random, Block.ROSE, plantChance, var4, var5);
            IndevFeatures.generatePlant(this.world, this.random, Block.BROWN_MUSHROOM, 2, var4, var5);
            IndevFeatures.generatePlant(this.world, this.random, Block.RED_MUSHROOM, 2, var4, var5);

            for(int var6 = var4 + 8; var6 < var4 + 8 + 16; ++var6) {
                for(int var7 = var5 + 8; var7 < var5 + 8 + 16; ++var7) {
                    int var8 = this.world.getTopSolidBlockY(var6, var7);
                    if(this.theme.equals("Winter") && var8 > 0 && var8 < this.world.dimension.getHeight() && this.world.getBlockId(var6, var8, var7) == 0 && this.world.getMaterial(var6, var8 - 1, var7).isSolid() && this.world.getMaterial(var6, var8 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var6, var8, var7, Block.SNOW.id);
                    }
                }
            }
        } else {
            int var4 = x * 16;
            int var5 = z * 16;
            Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
            IndevFeatures.placeTopBlockOnDirt(this.world, var4, var5, var6, this.theme);
            super.decorate(source, x, z);
        }
    }
}