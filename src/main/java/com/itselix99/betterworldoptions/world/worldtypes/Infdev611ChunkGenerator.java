package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.world.World;

public class Infdev611ChunkGenerator extends OldChunkGenerator {
    public Infdev611ChunkGenerator(World world, long seed) {
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

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        this.scaleNoiseBuffer = this.scaleNoise.generateNoiseInfdev(this.scaleNoiseBuffer, x, y, z, sizeX, 1, sizeZ, 1.0D, 0.0D, 1.0D);
        this.depthNoiseBuffer = this.depthNoise.generateNoiseInfdev(this.depthNoiseBuffer, x, y, z, sizeX, 1, sizeZ, 100.0D, 0.0D, 100.0D);
        this.perlinNoiseBuffer = this.perlinNoise1.generateNoiseInfdev(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoiseInfdev(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoiseInfdev(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var12 = 0;
        int var13 = 0;

        for(int var14 = 0; var14 < sizeX; ++var14) {
            for(int var15 = 0; var15 < sizeZ; ++var15) {
                double islandOffset = this.getIslandOffset(x + var14, z + var15, -200.0D);

                double var16 = (this.scaleNoiseBuffer[var13] + 256.0D) / 512.0D;
                if (var16 > 1.0D) {
                    var16 = 1.0D;
                }

                double var18 = this.depthNoiseBuffer[var13] / 8000.0D;
                if (var18 < 0.0D) {
                    var18 = -var18;
                }

                var18 = var18 * 3.0D - 3.0D;
                if (var18 < 0.0D) {
                    var18 /= 2.0D;
                    if (var18 < -1.0D) {
                        var18 = -1.0D;
                    }

                    var18 /= 1.4D;
                    var16 = 0.0D;
                } else {
                    if (var18 > 1.0D) {
                        var18 = 1.0D;
                    }

                    var18 /= 6.0D;
                }

                var16 += 0.5D;
                var18 = var18 * 17.0D / 16.0D;
                double var20 = 8.5D + var18 * 4.0D;
                ++var13;

                for(int var22 = 0; var22 < sizeY; ++var22) {
                    double var23 = ((double)var22 - var20) * 12.0D / var16;
                    if (var23 < 0.0D) {
                        var23 *= 4.0D;
                    }

                    double var25 = this.minLimitPerlinNoiseBuffer[var12] / 512.0D;
                    double var27 = this.maxLimitPerlinNoiseBuffer[var12] / 512.0D;
                    double var29 = (this.perlinNoiseBuffer[var12] / 10.0D + 1.0D) / 2.0D;
                    double var31;
                    if (var29 < 0.0D) {
                        var31 = var25;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var31 += islandOffset;
                        }
                    } else if (var29 > 1.0D) {
                        var31 = var27;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var31 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var25 += islandOffset;
                            var27 += islandOffset;
                        }

                        var31 = var25 + (var27 - var25) * var29;
                    }

                    var31 -= var23;
                    heightMap[var12] = var31;
                    ++var12;
                }
            }
        }

        return heightMap;
    }

    protected int getForestNoiseValue(int x, int z) {
        int value = (int)(this.forestNoise.sample((double)x * 0.5D, (double)z * 0.5D) / 8.0D + this.random.nextDouble() * 4.0D + 4.0D);

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
}