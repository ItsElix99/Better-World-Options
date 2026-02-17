package com.itselix99.betterworldoptions.api.chunk;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.chunk.BWOLimitChunk;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;

import java.util.Random;

public class BWOChunkGenerator extends OverworldChunkGenerator {
    protected Random random;
    protected final World world;

    protected final Generator cave = new CaveWorldCarver();
    protected final Generator ravine = new RavineWorldCarver();

    protected final BWOProperties bwoProperties;
    protected final String worldType;
    protected final boolean oldFeatures;
    protected final String theme;
    protected final boolean finiteWorld;
    protected final String finiteWorldType;
    protected final int width;
    protected final int length;
    protected final boolean farlands;
    protected final String farlandsShape;
    protected final int farlandsDistance;

    private static int[] sizeLimits;

    public BWOChunkGenerator(World world, long seed) {
        super(world, seed);
        this.world = world;
        this.random = new Random(seed);

        this.bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = this.bwoProperties.bwo_getWorldType();
        this.oldFeatures = this.bwoProperties.bwo_isOldFeatures();
        this.theme = this.bwoProperties.bwo_getTheme();
        this.finiteWorld = this.bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteWorldType = this.bwoProperties.bwo_getStringOptionValue("FiniteWorldType", OptionType.GENERAL_OPTION);
        this.width = this.bwoProperties.bwo_getIntOptionValue("Width", OptionType.GENERAL_OPTION);
        this.length = this.bwoProperties.bwo_getIntOptionValue("Length", OptionType.GENERAL_OPTION);
        this.farlands = this.bwoProperties.bwo_getBooleanOptionValue("Farlands", OptionType.GENERAL_OPTION);
        this.farlandsShape = this.bwoProperties.bwo_getStringOptionValue("FarlandsShape", OptionType.GENERAL_OPTION);
        this.farlandsDistance = this.bwoProperties.bwo_getIntOptionValue("FarlandsDistance", OptionType.GENERAL_OPTION) / 2;

        if (this.finiteWorldType.equals("MCPE")) {
            setSizeLimits(0, this.width, 0, this.length);
        } else {
            setSizeLimits(-this.width / 2, this.width / 2, -this.length / 2, this.length / 2);
        }

        ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
    }

    protected Chunk getLimitChunkFiniteWorld(int chunkX, int chunkZ, byte[] blocks, String mode, Chunk defaultChunk) {
        if (this.finiteWorld && !this.finiteWorldType.equals("Island")) {
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
            int index = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;
            double minX = -this.width / 2.0D;
            double maxX = this.width / 2.0D - 1.0D;
            double minZ = -this.length / 2.0D;
            double maxZ = this.length / 2.0D - 1.0D;

            boolean limit = (x2 == minX && z2 >= minZ && z2 <= maxZ) || (x2 == maxX && z2 >= minZ && z2 <= maxZ) || (z2 == minZ && x2 >= minX && x2 <= maxX) || (z2 == maxZ && x2 >= minX && x2 <= maxX);
            if (y <= 55) {
                if (limit) {
                    if (y >= 53) {
                        if (WorldTypes.getWorldTypeByName(this.worldType).oldFeaturesProperties != null && WorldTypes.getWorldTypeByName(this.worldType).oldFeaturesProperties.oldFeaturesHasVanillaBiomes) {
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

    public static void setSizeLimits(int minX, int maxX, int minZ, int maxZ) {
        sizeLimits = new int[]{minX, maxX, minZ, maxZ};
    }

    public static int[] getSizeLimits() {
        return sizeLimits;
    }
}