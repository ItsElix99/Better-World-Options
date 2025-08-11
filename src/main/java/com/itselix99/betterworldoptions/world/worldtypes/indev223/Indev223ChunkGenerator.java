package com.itselix99.betterworldoptions.world.worldtypes.indev223;

import com.itselix99.betterworldoptions.BWOConfig;
import com.itselix99.betterworldoptions.interfaces.BWOBiome;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.feature.IndevFeatures;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise.Distort;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise.OctavePerlinNoiseSamplerIndev223;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
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

    private final String worldType;
    private final boolean betaFeatures;
    private final String theme;
    private final String singleBiome;
    private final boolean infiniteWorld;

    private int WORLD_SIZE_X;
    private int WORLD_SIZE_Z;

    public Indev223ChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        this.worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        this.betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();
        this.singleBiome = ((BWOProperties) this.world.getProperties()).bwo_getSingleBiome();
        this.infiniteWorld = ((BWOProperties) this.world.getProperties()).bwo_isInfiniteWorld();

        if (this.theme.equals("Winter")) {
            if (!this.betaFeatures) {
                ((BWOBiome) this.world).bwo_oldBiomeSetSnow(this.worldType, true);
            } else {
                ((BWOBiome) this.world).bwo_setSnow(true);
            }
        } else {
            if (!this.betaFeatures) {
                ((BWOBiome) this.world).bwo_oldBiomeSetSnow(this.worldType, false);
            } else {
                ((BWOBiome) this.world).bwo_setSnow(false);
            }
        }

        if (this.betaFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        }

        String indevWorldSize = ((BWOProperties) this.world.getProperties()).bwo_getSize();
        if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getShape(), "Long")) {
            switch (indevWorldSize) {
                case "Small" -> this.setSizeXZ(256, 64);
                case "Normal" -> this.setSizeXZ(512, 128);
                case "Huge" -> this.setSizeXZ(1024, 256);
                case "Gigantic" -> this.setSizeXZ(2048, 512);
                case "Enormous" -> this.setSizeXZ(4096, 1024);
                default -> this.setSizeXZ(-1);
            }
        } else {
            switch (indevWorldSize) {
                case "Small" -> this.setSizeXZ(128);
                case "Normal" -> this.setSizeXZ(256);
                case "Huge" -> this.setSizeXZ(512);
                case "Gigantic" -> this.setSizeXZ(1024);
                case "Enormous" -> this.setSizeXZ(2048);
                default -> this.setSizeXZ(-1);
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
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int[] heightMap = new int[16 * 16];
        String indevWorldType = ((BWOProperties) this.world.getProperties()).bwo_getIndevWorldType();
        int surroundingWaterHeight = 64;

        if (indevWorldType.equals("Flat")) {
            for (int i = 0; i < heightMap.length; i++) heightMap[i] = 0;
        } else {
            for (int x = 0; x < 16; x++) {
                int worldX = chunkX * 16 + x;
                double nx = ((double) worldX / (WORLD_SIZE_X - 1) - 0.5) * 2.0;

                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;
                    double nz = ((double) worldZ / (WORLD_SIZE_Z - 1) - 0.5) * 2.0;

                    double low = this.distortA.create(worldX * 1.3, worldZ * 1.3) / 6.0 - 4.0;
                    double high = this.distortB.create(worldX * 1.3, worldZ * 1.3) / 5.0 + 6.0;
                    if (this.noiseGen1.create(worldX, worldZ) / 8.0 > 0.0) high = low;

                    double h = Math.max(low, high) / 2.0;

                    double distance = Math.max(Math.abs(nx), Math.abs(nz));

                    if (indevWorldType.equals("Island")) {
                        double radius = Math.sqrt(nx * nx + nz * nz) * 1.2;
                        double falloff = noiseGen2.create(worldX * 0.05, worldZ * 0.05) / 4.0 + 1.0;
                        radius = Math.min(Math.max(distance, Math.min(radius, falloff)), 1.0);
                        radius *= radius;
                        h = h * (1.0 - radius) - radius * 10.0 + 5.0;
                        if (h < 0.0) h -= h * h * 0.2;
                    } else {
                        if (distance > 1.0 && !this.infiniteWorld) {
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
                double nx = ((double) worldX / (WORLD_SIZE_X - 1) - 0.5) * 2.0;

                for (int z = 0; z < 16; z++) {
                    int worldZ = chunkZ * 16 + z;
                    double nz = ((double) worldZ / (WORLD_SIZE_Z - 1) - 0.5) * 2.0;

                    double distance = Math.max(Math.abs(nx), Math.abs(nz));

                    double ePower = this.distortB.create(worldX << 1, worldZ << 1) / 8.0;
                    int sharp = this.distortC.create(worldX << 1, worldZ << 1) > 0.0 ? 1 : 0;

                    if (distance > 1.0 && !this.infiniteWorld) {
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
            double nx = ((double) worldX / (WORLD_SIZE_X - 1) - 0.5) * 2.0;

            for (int z = 0; z < 16; z++) {
                int worldZ = chunkZ * 16 + z;
                double nz = ((double) worldZ / (WORLD_SIZE_Z - 1) - 0.5) * 2.0;
                double distance = Math.max(Math.abs(nx), Math.abs(nz));

                double radial;
                radial = (radial = Math.max(nx, nz)) * radial * radial;

                int offset = (int) (this.noiseGen3.create(worldX, worldZ) / 24.0) - 4;
                int var108;
                int baseHeight = (var108 = heightMap[x + z * 16] + surroundingWaterHeight) + offset;
                heightMap[x + z * 16] = Math.max(var108, baseHeight);
                if (heightMap[x + z * 16] > BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() - 2) {
                    heightMap[x + z * 16] = BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() - 2;
                }

                if (heightMap[x + z * 16] <= 0) {
                    heightMap[x + z * 16] = 1;
                }

                double var105;
                int var112;
                if ((var112 = (int) ((double) ((int) (Math.sqrt(Math.abs(var105 = this.noiseGen4.create((double) worldX * 2.3, (double) worldZ * 2.3) / (double) 24.0F)) * Math.signum(var105) * (double) 20.0F) + surroundingWaterHeight) * ((double) 1.0F - radial) + radial * (double) BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue())) > surroundingWaterHeight) {
                    var112 = BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue();
                }

                int lastSandY = -1;

                int i = x * 16 + z;
                Biome var18 = biomes[i];
                double var19 = temperatures[i];
                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;

                for (int y = 0; y < BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue(); y++) {
                    int index = (x * 16 + z) * BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() + y;
                    int blockId = 0;

                    if (distance > 1.0 && !this.infiniteWorld) {
                        if (!indevWorldType.equals("Island") && !indevWorldType.equals("Floating")) {
                            if (y == 64) {
                                blockId = Block.GRASS_BLOCK.id;
                            }
                            if (y <= 63) {
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

                                if (y == surroundingWaterHeight - 1) {
                                    if (!this.theme.equals("Hell") && (var19 < temp && this.betaFeatures || this.theme.equals("Winter"))) {
                                        blockId = Block.ICE.id;
                                    }
                                } else {
                                    blockId = !this.theme.equals("Hell") ? Block.WATER.id : Block.LAVA.id;
                                }
                            }

                            if (y > surroundingWaterHeight - 1) {
                                blockId = 0;
                            }
                        }
                    } else {
                        if (y <= var108) {
                            if (this.betaFeatures) {
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

                        if (!indevWorldType.equals("Floating") && y <= surroundingWaterHeight - 1 && blockId == 0) {
                            if (y == surroundingWaterHeight - 1) {
                                if (!this.theme.equals("Hell") && (var19 < temp && this.betaFeatures || this.theme.equals("Winter"))) {
                                    blockId = Block.ICE.id;
                                }
                            } else {
                                blockId = !this.theme.equals("Hell") ? Block.WATER.id : Block.LAVA.id;
                            }
                        }
                    }

                    if (blocks[index] == 0) {
                        blocks[index] = (byte) blockId;
                    }
                }

                if (lastSandY != -1) {
                    int lastIndex = (x * 16 + z) * BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() + lastSandY;
                    if (this.betaFeatures && var18 == Biome.DESERT || var18 == Biome.ICE_DESERT) {
                        blocks[lastIndex] = (byte) Block.SANDSTONE.id;
                    }
                }
            }
        }

        int beachHeight = surroundingWaterHeight - 1;
        if (!this.betaFeatures && this.theme.equals("Paradise")) {
            beachHeight += 2;
        }

        for (int x = 0; x < 16; x++) {
            double worldX = chunkX * 16 + x;
            for (int z = 0; z < 16; z++) {
                double worldZ = chunkZ * 16 + z;
                boolean sand = noiseGen5.create(worldX, worldZ) > 8.0;
                if (indevWorldType.equals("Island")) {
                    sand = noiseGen5.create(worldX, worldZ) > (double) -8.0F;
                }

                if (!this.betaFeatures && this.theme.equals("Paradise")) {
                    sand = noiseGen5.create(worldX, worldZ) > (double) -32.0F;
                }

                boolean gravel = noiseGen3.create(worldX, worldZ) > 12.0;
                if ((!this.betaFeatures && "Hell".equals(this.theme) || "Woods".equals(this.theme)) || (this.betaFeatures && "Hell".equals(this.singleBiome) || "Rainforest".equals(this.singleBiome) || "Seasonal Forest".equals(this.singleBiome) || "Forest".equals(this.singleBiome) || "Taiga".equals(this.singleBiome))) {
                    sand = noiseGen5.create(worldX, worldZ) > (double) -8.0F;
                }

                int surfaceY = heightMap[x + z * 16];
                int blockIndex = (x * 16 + z) * BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() + surfaceY;
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

        if (!this.betaFeatures) {
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
                            if (cy < 1 || cy >= BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue()) continue;

                            for (int cz = (int) (offsetZ - radiusSq); cz <= (int) (offsetZ + radiusSq); cz++) {
                                if (cz < 1 || cz >= 15) continue;

                                float dx = cx - offsetX;
                                float dy = cy - offsetY;
                                float dz = cz - offsetZ;

                                if ((dx * dx + dy * dy * 2.0F + dz * dz) < radiusSq2) {
                                    int index = (cx * 16 + cz) * BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() + cy;

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

    private void setSizeXZ(int size) {
        WORLD_SIZE_X = size;
        WORLD_SIZE_Z = size;
    }

    private void setSizeXZ(int sizeX, int sizeZ) {
        WORLD_SIZE_X = sizeX;
        WORLD_SIZE_Z = sizeZ;
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        double[] var4 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var4);

        double centerX = ((double)(chunkX * 16) / (WORLD_SIZE_X - 1) - 0.5) * 2.0;
        double centerZ = ((double)(chunkZ * 16) / (WORLD_SIZE_Z - 1) - 0.5) * 2.0;
        double distance = Math.max(Math.abs(centerX), Math.abs(centerZ));

        if (this.betaFeatures && (distance <= 1.0 || this.infiniteWorld)){
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (BWOConfig.WORLD_CONFIG.ravineGeneration && (distance <= 1.0 || this.infiniteWorld)) {
            if (this.betaFeatures || BWOConfig.WORLD_CONFIG.allowGenWithBetaFeaturesOff) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        if (!this.betaFeatures) {
            IndevFeatures.placeUndergroundLakes(this.random, var3);
            //IndevFeatures.placeLakes(this.random, var3, this.indevTheme);

            IndevFeatures.placeOre(this.random, Block.COAL_ORE.id, 1000, 10, (128 << 2) / 5, var3);
            IndevFeatures.placeOre(this.random, Block.IRON_ORE.id, 800, 8, 128 * 3 / 5, var3);
            IndevFeatures.placeOre(this.random, Block.GOLD_ORE.id, 500, 6, (128 << 1) / 5, var3);
            IndevFeatures.placeOre(this.random, Block.DIAMOND_ORE.id, 800, 2, 128 / 5, var3);
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();
        return flattenedChunk;
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        for (int var1 = 0; var1 < 16; var1++) {
            int blockX = x * 16 + var1;
            if (blockX < 0 || blockX >= WORLD_SIZE_X) {
                return;
            }

            for (int var2 = 0; var2 < 16; var2++) {
                int blockZ = z * 16 + var2;
                if (blockZ < 0 || blockZ >= WORLD_SIZE_Z) {
                    return;
                }
            }
        }

        if (!this.betaFeatures) {
            this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
            int var4 = x << 4;
            int var5 = z << 4;

            byte var62 = (byte) (this.theme.equals("Paradise") ? 12 : 2);

            for(int var73 = 0; var73 < var62; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            for(int var74 = 0; var74 < var62; ++var74) {
                int var79 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var88 = this.random.nextInt(128);
                int var99 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
            }

            if (this.random.nextInt(4) == 0) {
                int var80 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var89 = this.random.nextInt(128);
                int var100 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.BROWN_MUSHROOM.id)).generate(this.world, this.random, var80, var89, var100);
            }

            if (this.random.nextInt(8) == 0) {
                int var81 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var90 = this.random.nextInt(128);
                int var101 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.RED_MUSHROOM.id)).generate(this.world, this.random, var81, var90, var101);
            }

            int var10 = this.theme.equals("Woods") ? (this.infiniteWorld ? 40 : 50) : 8;
            OakTreeFeature var9 = new OakTreeFeature();

            for(int var11 = 0; var11 < var10; ++var11) {
                int var12 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var13 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var14 = this.world.getTopY(var12, var13) + this.random.nextInt(3) - this.random.nextInt(6);
                var9.prepare(1.0F, 1.0F, 1.0F);
                var9.generate(this.world, this.random, var12, var14, var13);
            }

            for(int var6 = var4; var6 < var4 + 16; ++var6) {
                for(int var7 = var5; var7 < var5 + 16; ++var7) {
                    int var8 = this.world.getTopSolidBlockY(var6, var7);
                    if(this.theme.equals("Winter") && var8 > 0 && var8 < 128 && this.world.getBlockId(var6, var8, var7) == 0 && this.world.getMaterial(var6, var8 - 1, var7).isSolid() && this.world.getMaterial(var6, var8 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var6, var8, var7, Block.SNOW.id);
                    }
                }
            }
        } else {
            SandBlock.fallInstantly = true;
            int var4 = x * 16;
            int var5 = z * 16;
            Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
            this.random.setSeed(this.world.getSeed());
            long var7 = this.random.nextLong() / 2L * 2L + 1L;
            long var9 = this.random.nextLong() / 2L * 2L + 1L;
            this.random.setSeed((long)x * var7 + (long)z * var9 ^ this.world.getSeed());
            double var11;
            if (this.random.nextInt(4) == 0) {
                int var13 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var14 = this.random.nextInt(128);
                int var15 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new LakeFeature(this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id)).generate(this.world, this.random, var13, var14, var15);
            }

            if (this.random.nextInt(8) == 0) {
                int var26 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var38 = this.random.nextInt(this.random.nextInt(120) + 8);
                int var50 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                if (var38 < 64 || this.random.nextInt(10) == 0) {
                    (new LakeFeature(Block.LAVA.id)).generate(this.world, this.random, var26, var38, var50);
                }
            }

            for(int var27 = 0; var27 < 8; ++var27) {
                int var39 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var51 = this.random.nextInt(128);
                int var16 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new DungeonFeature()).generate(this.world, this.random, var39, var51, var16);
            }

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

            var11 = 0.5F;
            int var37 = (int)((this.noiseGen6.create((double)var4 * var11, (double)var5 * var11) / (double)8.0F + this.random.nextDouble() * (double)4.0F + (double)4.0F) / (double)3.0F);
            int var49 = 0;
            if (this.random.nextInt(10) == 0) {
                ++var49;
            }

            if (var6 == Biome.FOREST) {
                var49 += var37 + 5;
            }

            if (var6 == Biome.RAINFOREST) {
                var49 += var37 + 5;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var49 += var37 + 2;
            }

            if (var6 == Biome.TAIGA) {
                var49 += var37 + 5;
            }

            if (var6 == Biome.DESERT) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                } else {
                    var49 -= 20;
                }
            }

            if (var6 == Biome.TUNDRA) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                } else {
                    var49 -= 20;
                }
            }

            if (var6 == Biome.PLAINS) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                } else {
                    var49 -= 20;
                }
            }

            if (var6 == Biome.SWAMPLAND || var6 == Biome.SHRUBLAND || var6 == Biome.SAVANNA) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                }
            }

            for(int var61 = 0; var61 < var49; ++var61) {
                int var72 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var17 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                Feature var18 = var6.getRandomTreeFeature(this.random);
                var18.prepare(1.0F, 1.0F, 1.0F);
                var18.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
            }

            byte var62 = (byte) (this.theme.equals("Paradise") ? 8 : 0);
            if (var6 == Biome.FOREST) {
                var62 = 2;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var62 = 4;
            }

            if (var6 == Biome.TAIGA) {
                var62 = 2;
            }

            if (var6 == Biome.PLAINS) {
                var62 = 3;
            }

            for(int var73 = 0; var73 < var62; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            byte var74 = 0;
            if (var6 == Biome.FOREST) {
                var74 = 2;
            }

            if (var6 == Biome.RAINFOREST) {
                var74 = 10;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var74 = 2;
            }

            if (var6 == Biome.TAIGA) {
                var74 = 1;
            }

            if (var6 == Biome.PLAINS) {
                var74 = 10;
            }

            for(int var77 = 0; var77 < var74; ++var77) {
                byte var86 = 1;
                if (var6 == Biome.RAINFOREST && this.random.nextInt(3) != 0) {
                    var86 = 2;
                }

                int var97 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var20 = this.random.nextInt(128);
                int var21 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new GrassPatchFeature(Block.GRASS.id, var86)).generate(this.world, this.random, var97, var20, var21);
            }

            var74 = 0;
            if (var6 == Biome.DESERT) {
                var74 = 2;
            }

            for(int var78 = 0; var78 < var74; ++var78) {
                int var87 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var98 = this.random.nextInt(128);
                int var108 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new DeadBushPatchFeature(Block.DEAD_BUSH.id)).generate(this.world, this.random, var87, var98, var108);
            }

            if (this.theme.equals("Paradise")) {
                for (int var120 = 0; var120 < var62; var120++) {
                    int var79 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                    int var88 = this.random.nextInt(128);
                    int var99 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
                }
            } else {
                if (this.random.nextInt(2) == 0) {
                    int var79 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                    int var88 = this.random.nextInt(128);
                    int var99 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
                }
            }

            if (this.random.nextInt(4) == 0) {
                int var80 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var89 = this.random.nextInt(128);
                int var100 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.BROWN_MUSHROOM.id)).generate(this.world, this.random, var80, var89, var100);
            }

            if (this.random.nextInt(8) == 0) {
                int var81 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var90 = this.random.nextInt(128);
                int var101 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PlantPatchFeature(Block.RED_MUSHROOM.id)).generate(this.world, this.random, var81, var90, var101);
            }

            for(int var82 = 0; var82 < 10; ++var82) {
                int var91 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var102 = this.random.nextInt(128);
                int var109 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new SugarCanePatchFeature()).generate(this.world, this.random, var91, var102, var109);
            }

            if (this.random.nextInt(32) == 0) {
                int var83 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var92 = this.random.nextInt(128);
                int var103 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new PumpkinPatchFeature()).generate(this.world, this.random, var83, var92, var103);
            }

            int var84 = 0;
            if (var6 == Biome.DESERT) {
                var84 += 10;
            }

            for(int var93 = 0; var93 < var84; ++var93) {
                int var104 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var110 = this.random.nextInt(128);
                int var114 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new CactusPatchFeature()).generate(this.world, this.random, var104, var110, var114);
            }

            for(int var94 = 0; var94 < 50; ++var94) {
                int var105 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var111 = this.random.nextInt(this.random.nextInt(120) + 8);
                int var115 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new SpringFeature(this.theme.equals("Hell") ? Block.FLOWING_LAVA.id : Block.FLOWING_WATER.id)).generate(this.world, this.random, var105, var111, var115);
            }

            for(int var95 = 0; var95 < 20; ++var95) {
                int var106 = var4 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                int var112 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
                int var116 = var5 + this.random.nextInt(16) + (this.infiniteWorld ? 8 : 0);
                (new SpringFeature(Block.FLOWING_LAVA.id)).generate(this.world, this.random, var106, var112, var116);
            }

            this.temperatures = this.world.method_1781().create(this.temperatures, var4 + 8, var5 + 8, 16, 16);

            for(int var96 = var4 + (this.infiniteWorld ? 8 : 0); var96 < var4 + 16 + (this.infiniteWorld ? 8 : 0); ++var96) {
                for (int var107 = var5 + (this.infiniteWorld ? 8 : 0); var107 < var5 + 16 + (this.infiniteWorld ? 8 : 0); ++var107) {
                    int var113 = var96 - (var4 + (this.infiniteWorld ? 8 : 0));
                    int var117 = var107 - (var5 + (this.infiniteWorld ? 8 : 0));
                    int var22 = this.world.getTopSolidBlockY(var96, var107);
                    double var23 = this.temperatures[var113 * 16 + var117] - (double) (var22 - 64) / (double) 64.0F * 0.3;
                    float temp = this.theme.equals("Winter") ? 1.1F : 0.5F;
                    if (!this.theme.equals("Hell") && var23 < (double) temp && var22 > 0 && var22 < 128 && this.world.getBlockId(var96, var22, var107) == 0 && this.world.getMaterial(var96, var22 - 1, var107).blocksMovement() && this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
                        this.world.setBlock(var96, var22, var107, Block.SNOW.id);
                    }
                }
            }

            SandBlock.fallInstantly = false;
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