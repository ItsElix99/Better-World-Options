package com.itselix99.betterworldoptions.world.worldtypes.indev223;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.chunk.EmptyFlattenedChunk;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.feature.IndevFeatures;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise.Distort;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise.OctavePerlinNoiseSamplerIndev223;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.*;

public class Indev223ChunkGenerator implements ChunkSource {
    private final Random random;
    private final World world;
    private final Distort distortA;
    private final Distort distortB;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen1;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen2;
    private final Distort distortC;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen3;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen4;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen5;
    private final OctavePerlinNoiseSamplerIndev223 noiseGen6;
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private Biome[] biomes;
    private double[] temperatures;
    private OverworldChunkGenerator defaultChunkGenerator;

    private final String worldType;
    private final boolean oldFeatures;
    private final String theme;
    private final String singleBiome;
    private final boolean finiteWorld;
    private final String finiteType;
    private final int sizeX;
    private final int sizeZ;

    public Indev223ChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.oldFeatures = bwoProperties.bwo_isOldFeatures();
        this.theme = bwoProperties.bwo_getTheme();
        this.singleBiome = bwoProperties.bwo_getSingleBiome();
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);
        this.sizeX = bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION);
        this.sizeZ = bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION);

        if (this.theme.equals("Winter")) {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(true);
            }
        } else {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(false);
            }
        }

        if (this.theme.equals("Hell") || this.theme.equals("Paradise")) {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetPrecipitation(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setPrecipitation(false);
            }
        } else {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetPrecipitation(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setPrecipitation(true);
            }
        }

        if (!this.oldFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        } else {
            switch (this.theme) {
                case "Hell" -> BetterWorldOptions.Indev.setFogColor(1049600);
                case "Paradise" -> BetterWorldOptions.Indev.setFogColor(13033215);
                case "Woods" -> BetterWorldOptions.Indev.setFogColor(5069403);
                default -> BetterWorldOptions.Indev.setFogColor(16777215);
            }
        }

        this.distortA = new Distort(new OctavePerlinNoiseSamplerIndev223(this.random, 8), new OctavePerlinNoiseSamplerIndev223(this.random, 8));
        this.distortB = new Distort(new OctavePerlinNoiseSamplerIndev223(this.random, 8), new OctavePerlinNoiseSamplerIndev223(this.random, 8));
        this.noiseGen1 = new OctavePerlinNoiseSamplerIndev223(this.random, 6);
        this.noiseGen2 = new OctavePerlinNoiseSamplerIndev223(this.random, 2);
        this.distortC = new Distort(new OctavePerlinNoiseSamplerIndev223(this.random, 8), new OctavePerlinNoiseSamplerIndev223(this.random, 8));
        this.noiseGen3 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
        this.noiseGen5 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
        this.noiseGen6 = new OctavePerlinNoiseSamplerIndev223(this.random, 8);
        this.defaultChunkGenerator = new OverworldChunkGenerator(world, seed);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int[] heightMap = new int[16 * 16];
        String indevWorldType = ((BWOProperties) this.world.getProperties()).bwo_getStringOptionValue("IndevWorldType", OptionType.WORLD_TYPE_OPTION);
        int surroundingWaterHeight = 65;

        if (indevWorldType.equals("Flat")) {
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

                    if (indevWorldType.equals("Island")) {
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
                double distance = Math.max(Math.abs(nx), Math.abs(nz));

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
                double var19 = temperatures[i];
                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;

                for (int y = 0; y < Config.BWOConfig.world.worldHeightLimit.getIntValue(); y++) {
                    int index = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;
                    int blockId = 0;

                    if (distance > 1.0 && this.finiteWorld) {
                        if (!indevWorldType.equals("Island") && !indevWorldType.equals("Floating")) {
                            if (y == surroundingWaterHeight) {
                                blockId = Block.GRASS_BLOCK.id;
                            }
                            if (y <= surroundingWaterHeight - 1) {
                                blockId = Block.BEDROCK.id;
                            }
                        } else if (indevWorldType.equals("Island")) {
                            if (y == 54) {
                                blockId = Block.DIRT.id;
                            }

                            if (y <= 53) {
                                blockId = Block.BEDROCK.id;
                            }

                            if (y >= 55) {

                                if (y == surroundingWaterHeight - 2) {
                                    if (!this.theme.equals("Hell") && (var19 < temp && !this.oldFeatures || this.theme.equals("Winter"))) {
                                        blockId = Block.ICE.id;
                                    } else {
                                        blockId = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                    }
                                } else {
                                    blockId = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                }
                            }

                            if (y > surroundingWaterHeight - 2) {
                                blockId = 0;
                            }
                        }
                    } else {
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

                        if (indevWorldType.equals("Floating") && y < var112) {
                            blockId = 0;
                        }

                        if (!indevWorldType.equals("Floating") && y <= surroundingWaterHeight - 2 && blockId == 0) {
                            if (y == surroundingWaterHeight - 2) {
                                if (!this.theme.equals("Hell") && (var19 < temp && !this.oldFeatures || this.theme.equals("Winter"))) {
                                    blockId = Block.ICE.id;
                                } else {
                                    blockId = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                }
                            } else {
                                blockId = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                            }
                        }
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
                if (indevWorldType.equals("Island")) {
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
                int i = x * 16 + z;
                Biome var18 = biomes[i];

                if ((aboveBlock == Block.WATER.id || aboveBlock == Block.FLOWING_WATER.id || aboveBlock == 0)
                        && surfaceY <= surroundingWaterHeight - 1
                        && gravel) {
                    blocks[blockIndex] = (byte) Block.GRAVEL.id;
                }

                if (aboveBlock == 0 && (blocks[blockIndex] & 0xFF) == Block.DIRT.id) {

                    if (surfaceY <= beachHeight && sand) {
                        blocks[blockIndex] = (byte) (this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id);
                    } else {
                        blocks[blockIndex] = (byte) (this.theme.equals("Hell") ? Block.DIRT.id : var18.topBlockId);
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
        }
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var4 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var4);

        double centerX = ((double)(chunkX * 16) / (this.sizeX - 1) - 0.5) * 2.0;
        double centerZ = ((double)(chunkZ * 16) / (this.sizeZ - 1) - 0.5) * 2.0;
        double distance = Math.max(Math.abs(centerX), Math.abs(centerZ));

        if (!this.oldFeatures && (distance <= 1.0 || !this.finiteWorld)){
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration && (distance <= 1.0 || !this.finiteWorld)) {
            if (!this.oldFeatures || Config.BWOConfig.world.allowGenWithOldFeaturesOn) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        if (this.oldFeatures) {
            //IndevFeatures.placeUndergroundLakes(this.random, var3);
            //IndevFeatures.placeLakes(this.random, var3, this.indevTheme);

            IndevFeatures.placeOre(this.random, Block.COAL_ORE.id, 1000, 10, (128 << 2) / 5, var3);
            IndevFeatures.placeOre(this.random, Block.IRON_ORE.id, 800, 8, 128 * 3 / 5, var3);
            IndevFeatures.placeOre(this.random, Block.GOLD_ORE.id, 500, 6, (128 << 1) / 5, var3);
            IndevFeatures.placeOre(this.random, Block.DIAMOND_ORE.id, 800, 2, 128 / 5, var3);
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();

        if (this.finiteWorld && this.finiteType.equals("MCPE")) {
            int blockX = chunkX * 16;
            int blockZ = chunkZ * 16;

            if (blockX < 0 || blockX >= this.sizeX || blockZ < 0 || blockZ >= this.sizeZ) {
                return new EmptyFlattenedChunk(this.world, chunkX, chunkZ);
            }
        }

        return flattenedChunk;
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
            this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
            int var4 = x << 4;
            int var5 = z << 4;

            byte var62 = (byte) (this.theme.equals("Paradise") ? 12 : 2);

            for(int var73 = 0; var73 < var62; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            for(int var74 = 0; var74 < var62; ++var74) {
                int var79 = var4 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                int var88 = this.random.nextInt(128);
                int var99 = var5 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
            }

            if (this.random.nextInt(4) == 0) {
                int var80 = var4 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                int var89 = this.random.nextInt(128);
                int var100 = var5 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.BROWN_MUSHROOM.id)).generate(this.world, this.random, var80, var89, var100);
            }

            if (this.random.nextInt(8) == 0) {
                int var81 = var4 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                int var90 = this.random.nextInt(128);
                int var101 = var5 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.RED_MUSHROOM.id)).generate(this.world, this.random, var81, var90, var101);
            }

            int var10 = this.theme.equals("Woods") ? (!this.finiteWorld ? 40 : 50) : 8;
            OakTreeFeature var9 = new OakTreeFeature();

            for(int var11 = 0; var11 < var10; ++var11) {
                int var12 = var4 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                int var13 = var5 + this.random.nextInt(16) + (!this.finiteWorld ? 8 : 0);
                int var14 = this.world.getTopY(var12, var13) + this.random.nextInt(3) - this.random.nextInt(6);
                var9.prepare(1.0F, 1.0F, 1.0F);
                var9.generate(this.world, this.random, var12, var14, var13);
            }

            for(int var6 = var4; var6 < var4 + 16; ++var6) {
                for(int var7 = var5; var7 < var5 + 16; ++var7) {
                    int var8 = this.world.getTopSolidBlockY(var6, var7);
                    if(this.theme.equals("Winter") && var8 > 0 && var8 < this.world.dimension.getHeight() && this.world.getBlockId(var6, var8, var7) == 0 && this.world.getMaterial(var6, var8 - 1, var7).isSolid() && this.world.getMaterial(var6, var8 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var6, var8, var7, Block.SNOW.id);
                    }
                }
            }
        } else {
            this.defaultChunkGenerator.decorate(source, x, z);
        }
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