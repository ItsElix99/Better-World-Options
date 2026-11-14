package com.itselix99.betterworldoptions.api.chunk;

import com.itselix99.betterworldoptions.api.BiomeProperties;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.chunk.BWOLimitChunk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BWOChunkGenerator extends OverworldChunkGenerator {
    protected Random random;
    protected BWOOctavePerlinNoiseSampler minLimitPerlinNoise;
    protected BWOOctavePerlinNoiseSampler maxLimitPerlinNoise;
    protected BWOOctavePerlinNoiseSampler perlinNoise1;
    protected BWOOctavePerlinNoiseSampler perlinNoise2;
    protected BWOOctavePerlinNoiseSampler perlinNoise3;
    protected BWOOctavePerlinNoiseSampler scaleNoise;
    protected BWOOctavePerlinNoiseSampler depthNoise;
    protected BWOOctavePerlinNoiseSampler forestNoise;
    protected final World world;
    protected double[] heightMap;
    protected double[] sandBuffer = new double[256];
    protected double[] gravelBuffer = new double[256];
    protected double[] depthBuffer = new double[256];
    protected Biome[] biomes;
    protected double[] perlinNoiseBuffer;
    protected double[] minLimitPerlinNoiseBuffer;
    protected double[] maxLimitPerlinNoiseBuffer;
    protected double[] scaleNoiseBuffer;
    protected double[] depthNoiseBuffer;
    protected double[] temperatures;
    protected int shiftX;
    protected int shiftZ;

    protected Generator cave = new CaveWorldCarver();
    protected final Generator ravine = new RavineWorldCarver();

    protected final BWOProperties bwoProperties;
    protected final WorldType worldType;
    protected final boolean oldFeatures;
    protected final Theme theme;
    protected final BiomeProperties biomeProperties;
    protected final boolean finiteWorld;
    protected String finiteWorldType;
    protected final int width;
    protected final int length;
    protected final boolean farlands;
    protected final String farlandsShape;
    protected final int farlandsDistance;
    protected final Map<Long, int[]> surfaceHeightCache;

    private static int[] sizeLimits;

    public BWOChunkGenerator(World world, long seed) {
        super(world, seed);
        this.world = world;
        this.random = new Random(seed);
        this.shiftZ = 32 - Integer.numberOfLeadingZeros(world.getHeight() - 1);
        this.shiftX = this.shiftZ + 4;

        this.bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = ((BWOWorld) world).bwo_getWorldType();
        this.oldFeatures = this.bwoProperties.bwo_isOldFeatures();
        this.theme = Theme.getThemeById(Identifier.of(this.bwoProperties.bwo_getTheme()));
        this.biomeProperties = ((BWOWorld) world).bwo_getBiomeProperties();
        this.finiteWorld = this.bwoProperties.bwo_getOptionValue("FiniteWorld", OptionType.GENERAL_OPTION, false);
        this.finiteWorldType = this.bwoProperties.bwo_getOptionValue("FiniteWorldType", OptionType.GENERAL_OPTION, "");
        this.width = this.bwoProperties.bwo_getOptionValue("Width", OptionType.GENERAL_OPTION, 0);
        this.length = this.bwoProperties.bwo_getOptionValue("Length", OptionType.GENERAL_OPTION, 0);
        this.farlands = this.bwoProperties.bwo_getOptionValue("Farlands", OptionType.GENERAL_OPTION, false);
        this.farlandsShape = this.bwoProperties.bwo_getOptionValue("FarlandsShape", OptionType.GENERAL_OPTION, "");
        this.farlandsDistance = this.bwoProperties.bwo_getOptionValue("FarlandsDistance", OptionType.GENERAL_OPTION, 0) / 2;
        this.surfaceHeightCache = new HashMap<>();

        if (this.finiteWorldType.equals("MCPE")) {
            setSizeLimits(0, this.width, 0, this.length);
        } else {
            setSizeLimits(-this.width / 2, this.width / 2, -this.length / 2, this.length / 2);
        }

        ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        ((CaveGenBaseImpl) this.ravine).stationapi_setWorld(world);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, double[] temperatures) {
        int[] farlandsChunks = this.getFarlandsChunksOrDefault(chunkX, chunkZ, 784426);
        chunkX = farlandsChunks[0];
        chunkZ = farlandsChunks[1];

        int var5 = this.world.getHeight() / 8;
        int var6 = 4;
        int var7 = this.worldType.getSeaLevel();
        int var8 = var6 + 1;
        int var9 = var5 + 1;
        int var10 = var6 + 1;
        this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var6, 0, chunkZ * var6, var8, var9, var10);

        int[] surfaceHeights = new int[256];
        for (int var11 = 0; var11 < var6; ++var11) {
            for (int var12 = 0; var12 < var6; ++var12) {
                for (int var13 = 0; var13 < var5; ++var13) {
                    double var14 = 0.125D;
                    int var54 = (var11 * var10 + var12 + 1) * (var9);
                    int var55 = ((var11 + 1) * var10 + var12) * (var9);
                    int var56 = ((var11 + 1) * var10 + var12 + 1) * (var9);
                    double var16 = this.heightMap[(var11 * var10 + var12) * (var9) + var13];
                    double var18 = this.heightMap[var54 + var13];
                    double var20 = this.heightMap[var55 + var13];
                    double var22 = this.heightMap[var56 + var13];
                    double var24 = (this.heightMap[(var11 * var10 + var12) * (var9) + var13 + 1] - var16) * var14;
                    double var26 = (this.heightMap[var54 + var13 + 1] - var18) * var14;
                    double var28 = (this.heightMap[var55 + var13 + 1] - var20) * var14;
                    double var30 = (this.heightMap[var56 + var13 + 1] - var22) * var14;

                    for (int var32 = 0; var32 < 8; ++var32) {
                        double var33 = 0.25D;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for (int var43 = 0; var43 < 4; ++var43) {
                            int var44 = var43 + var11 * 4 << this.shiftX | var12 * 4 << this.shiftZ | var13 * 8 + var32;
                            int var45 = this.world.getHeight();
                            double var46 = 0.25D;
                            double var48 = var35;
                            double var50 = (var37 - var35) * var46;

                            for (int var52 = 0; var52 < 4; ++var52) {
                                double temperature = temperatures[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
                                int var53 = 0;
                                if (var13 * 8 + var32 < var7) {
                                    var53 = this.getWaterBlock(temperature, var13 * 8 + var32, false);
                                }

                                if (var48 > 0.0D) {
                                    var53 = Block.STONE.id;

                                    int y = var13 * 8 + var32;
                                    int x = var11 * 4 + var43;
                                    int z = var12 * 4 + var52;

                                    surfaceHeights[x + z * 16] = y;
                                }

                                blocks[var44] = (byte) var53;
                                var44 += var45;
                                var48 += var50;
                            }

                            var35 += var39;
                            var37 += var41;
                        }

                        var16 += var24;
                        var18 += var26;
                        var20 += var28;
                        var22 += var30;
                    }
                }
            }
        }

        this.setSurfaceHeightForStructures(chunkX, chunkZ, surfaceHeights);
    }

    public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
        int var5 = this.worldType.getSeaLevel();
        double var6 = 0.03125D;

        this.initBuffers(chunkX, chunkZ, var6);
        double[] temperatures = this.getTemperatures(chunkX, chunkZ);

        for(int var8 = 0; var8 < 16; ++var8) {
            for(int var9 = 0; var9 < 16; ++var9) {
                double temperature = temperatures[var9 + var8 * 16];
                Biome var14 = this.getBiome(biomes, var8, var9);
                int[] beachesAndErosion = this.generateBeachesAndErosion(chunkX, chunkZ, var8, var9, var6);
                boolean var15 = beachesAndErosion[0] == 1;
                boolean var16 = beachesAndErosion[1] == 1;
                int var17 = beachesAndErosion[2];
                int var18 = -1;
                int[] topAndSoilBlock = this.getTopAndSoilBlock(var14);
                int var19 = topAndSoilBlock[0];
                int var20 = topAndSoilBlock[1];

                for(int var21 = this.world.getTopY() - 1; var21 >= this.world.getBottomY(); --var21) {
                    int var22 = this.getBlocksIndex(var8, var9, var21);
                    if (this.generateBedrock(var21)) {
                        blocks[var22] = (byte) Block.BEDROCK.id;
                    } else {
                        if (blocks[var22] == 0) {
                            var18 = -1;
                        } else if (blocks[var22] == Block.STONE.id) {
                            if (var18 == -1) {
                                if (var17 <= 0) {
                                    var19 = 0;
                                    var20 = Block.STONE.id;
                                } else if (var21 >= var5 - 4 && var21 <= var5 + 1) {
                                    var19 = topAndSoilBlock[0];
                                    var20 = topAndSoilBlock[1];

                                    if (var16) {
                                        var19 = 0;
                                    }

                                    if (var16) {
                                        var20 = Block.GRAVEL.id;
                                    }

                                    if (var15) {
                                        var19 = this.theme.getBeachTopBlock();
                                    }

                                    if (var15) {
                                        var20 = this.theme.getBeachSoilBlock();
                                    }
                                }

                                if (var21 < var5 && var19 == 0) {
                                    var19 = this.getWaterBlock(temperature, var21, true);
                                }

                                var18 = var17;
                                if (var21 >= var5 - 1) {
                                    blocks[var22] = (byte) var19;
                                } else {
                                    blocks[var22] = (byte) var20;
                                }
                            } else if (var18 > 0) {
                                --var18;
                                blocks[var22] = (byte) var20;
                                int[] sandstone = this.generateSandstone(var18, var20);
                                var18 = sandstone[0];
                                var20 = sandstone[1];
                            }
                        }

                        --var22;

                        this.buildLCEFiniteWorldLimit(chunkX, chunkZ, var8, var21, var9, blocks, var14);
                    }
                }
            }
        }
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
            sandBeach = this.sandBuffer[x + z * 16] + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
            gravelBeach = this.gravelBuffer[x + z * 16] + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
            erosion = (int)(this.depthBuffer[x + z * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
        } else {
            double worldX = (chunkX << 4) + x;
            double worldZ = (chunkZ << 4) + z;
            sandBeach = this.perlinNoise2.sample(worldX * scale, worldZ * scale, 0.0D) + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
            gravelBeach = this.perlinNoise2.sample(worldX * scale, 109.0134D, worldZ * scale) + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
            erosion = (int)(this.perlinNoise3.sample(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
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

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.setChunkSeed(chunkX, chunkZ);
        byte[] blocks = new byte[16 * this.world.getHeight() * 16];
        this.biomes = this.getBiomes(chunkX, chunkZ);
        double[] temperatures = this.getTemperatures(chunkX, chunkZ);
        this.buildTerrain(chunkX, chunkZ, blocks, temperatures);
        this.buildSurfaces(chunkX, chunkZ, blocks, this.biomes);
        this.generateCaves(chunkX, chunkZ, blocks);
        this.buildStructures(this, this.world, chunkX, chunkZ, blocks);

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(blocks);
        flattenedChunk.populateHeightMap();

        return this.getLimitChunkFiniteWorld(chunkX, chunkZ, blocks, this.finiteWorldType, flattenedChunk);
    }

    protected void setChunkSeed(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
    }

    protected Biome[] getBiomes(int chunkX, int chunkZ) {
        return this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
    }

    protected double[] getTemperatures(int chunkX, int chunkZ) {
        return this.world.method_1781().temperatureMap;
    }

    protected void generateCaves(int chunkX, int chunkZ, byte[] blocks) {
        this.cave.place(this, this.world, chunkX, chunkZ, blocks);

        if (Config.BWOConfig.world.ravineGeneration) {
            this.ravine.place(this, this.world, chunkX, chunkZ, blocks);
        }
    }

    protected void buildStructures(ChunkSource source, World world, int chunkX, int chunkZ, byte[] blocks) {
        for (Structure structure : Structure.getStructuresList()) {
            if (structure.getStructureFeature() != null && this.bwoProperties.bwo_isGenerateStructure(structure.getId())) {
                structure.getStructureFeature().place(source, world, chunkX, chunkZ, blocks);
            }
        }
    }

    public boolean placeStructures(World world, Random random, int chunkX, int chunkZ) {
        boolean largeStructure = false;

        for (Structure structure : Structure.getStructuresList()) {
            if (structure.getStructureFeature() != null && this.bwoProperties.bwo_isGenerateStructure(structure.getId())) {
                boolean valid = structure.getStructureFeature().place(world, random, chunkX, chunkZ);

                if (structure.isLargeStructure()) {
                    largeStructure = valid;
                }
            }
        }

        return largeStructure;
    }

    public boolean useSurfaceHeightForStructures() {
        return true;
    }

    public int getMinSurfaceHeight() {
        return 59;
    }

    public void setSurfaceHeightForStructures(int chunkX, int chunkZ, int[] surfaceHeights) {
        for(int var1 = chunkX - 8; var1 <= chunkX + 8; ++var1) {
            for (int var2 = chunkZ - 8; var2 <= chunkZ + 8; ++var2) {
                this.surfaceHeightCache.put(ChunkPos.toLong(var1, var2), surfaceHeights);
            }
        }
    }

    public Map<Long, int[]> getSurfaceHeightCache() {
        return this.surfaceHeightCache;
    }

    public Random getRandom() {
        return this.random;
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        return heightMap;
    }

    protected Chunk getLimitChunkFiniteWorld(int chunkX, int chunkZ, byte[] blocks, String mode, Chunk defaultChunk) {
        if (this.finiteWorld) {
            int blockX = chunkX * 16;
            int blockZ = chunkZ * 16;
            int[] sizeLimits = getSizeLimits();

            if (blockX < sizeLimits[0] || blockX >= sizeLimits[1] || blockZ < sizeLimits[2] || blockZ >= sizeLimits[3]) {
                BWOLimitChunk bwoLimitChunk = new BWOLimitChunk(this.world, chunkX, chunkZ, mode);
                bwoLimitChunk.fromLegacy(blocks);
                bwoLimitChunk.populateHeightMap();
                return bwoLimitChunk;
            }
        }

        return defaultChunk;
    }

    public static boolean getMultiplayerLimitChunkFiniteWorld(int chunkX, int chunkZ) {
        int blockX = chunkX * 16;
        int blockZ = chunkZ * 16;
        int[] sizeLimits = getSizeLimits();

        if (blockX < sizeLimits[0] || blockX >= sizeLimits[1] || blockZ < sizeLimits[2] || blockZ >= sizeLimits[3]) {
            return true;
        }

        return false;
    }

    protected int[] getFarlandsChunksOrDefault(int chunkX, int chunkZ, int farlandsChunk) {
        if (this.farlands) {
            if (this.farlandsShape.equals("Linear")) {
                if (chunkX > this.farlandsDistance) chunkX += farlandsChunk;
                if (chunkX < -this.farlandsDistance) chunkX -= farlandsChunk;
            } else if (this.farlandsShape.equals("Square")) {
                if (chunkX > this.farlandsDistance) chunkX += farlandsChunk;
                if (chunkX < -this.farlandsDistance) chunkX -= farlandsChunk;
                if (chunkZ > this.farlandsDistance) chunkZ += farlandsChunk;
                if (chunkZ < -this.farlandsDistance) chunkZ -= farlandsChunk;
            }
        }

        return new int[]{chunkX, chunkZ};
    }

    protected void buildLCEFiniteWorldLimit(int chunkX, int chunkZ, int x, int y, int z, byte[] blocks, Biome biome) {
        if (this.finiteWorld && this.finiteWorldType.equals("LCE")) {
            double x2 = (chunkX << 4) + x;
            double z2 = (chunkZ << 4) + z;
            int index = (x * 16 + z) * this.world.getHeight() + y;
            double minX = -this.width / 2.0D;
            double maxX = this.width / 2.0D - 1.0D;
            double minZ = -this.length / 2.0D;
            double maxZ = this.length / 2.0D - 1.0D;

            boolean limit = (x2 == minX && z2 >= minZ && z2 <= maxZ) || (x2 == maxX && z2 >= minZ && z2 <= maxZ) || (z2 == minZ && x2 >= minX && x2 <= maxX) || (z2 == maxZ && x2 >= minX && x2 <= maxX);
            if (y <= 55) {
                if (limit) {
                    if (y >= 53) {
                        if (this.worldType.getOldFeaturesProperties() != null && this.worldType.getOldFeaturesProperties().oldFeaturesHasVanillaBiomes) {
                            blocks[index] = biome.soilBlockId;
                        } else {
                            blocks[index] = this.oldFeatures ? (byte) Block.DIRT.id : biome.soilBlockId;
                        }
                    } else {
                        blocks[index] = (byte) Block.STONE.id;
                    }
                }
            }

            if (y <= this.random.nextInt(5)) {
                if (limit) {
                    blocks[index] = (byte) Block.BEDROCK.id;
                }
            }
        }
    }

    protected double getIslandOffset(int x, int z, double offset) {
        double worldEdgeFactor = 1.0D;

        int nx = x * 4;
        int nz = z * 4;

        double dx = Math.abs(nx);
        double dz = Math.abs(nz);

        int halfSizeX = this.width / 2;
        int halfSizeZ = this.length / 2;

        double limitX = halfSizeX + 18.0D;
        double limitZ = halfSizeZ + 18.0D;

        if (halfSizeX == 32) limitX += 12.0D;
        if (halfSizeZ == 32) limitZ += 12.0D;

        double falloff = 50.0D;

        if (this.finiteWorldType.equals("LCE")) {
            double edgeX = limitX - dx;
            double edgeZ = limitZ - dz;

            double factorX = edgeX / falloff;
            double factorZ = edgeZ / falloff;

            factorX = Math.max(0.0D, Math.min(1.0D, factorX));
            factorZ = Math.max(0.0D, Math.min(1.0D, factorZ));

            worldEdgeFactor = Math.min(factorX, factorZ);
        } else if (this.finiteWorldType.equals("Island")) {
            falloff = 100.0D;

            double nxNorm = dx / limitX;
            double nzNorm = dz / limitZ;

            double radial = Math.sqrt(nxNorm * nxNorm + nzNorm * nzNorm);
            double falloffRadial = falloff / (Math.sqrt(limitX * limitX + limitZ * limitZ));

            double start = 1.0D - falloffRadial;

            double t = (radial - start) / (1.0D - start);
            t = Math.max(0.0D, Math.min(1.0D, t));

            worldEdgeFactor = 1.0D - t;
        }

        return offset * (1.0D - worldEdgeFactor);
    }

    @Environment(EnvType.CLIENT)
    public String getDebugInfo() {
        if (this.worldType.getId().equals(WorldType.defaultWorldType.getId())) {
            return super.getDebugInfo();
        }

        return this.worldType.getName() + "LevelSource";
    }

    public static void setSizeLimits(int minX, int maxX, int minZ, int maxZ) {
        sizeLimits = new int[]{minX, maxX, minZ, maxZ};
    }

    public static int[] getSizeLimits() {
        return sizeLimits;
    }
}