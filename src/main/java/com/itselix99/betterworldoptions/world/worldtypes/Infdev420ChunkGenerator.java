package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.api.chunk.OldChunkGenerator;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import com.itselix99.betterworldoptions.world.feature.old.OldLargeOakTreeFeature;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class Infdev420ChunkGenerator extends OldChunkGenerator {
    public Infdev420ChunkGenerator(World world, long seed) {
        super(world, seed);
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        new BWOOctavePerlinNoiseSampler(this.random, 5, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 5, true);
    }

    protected double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        if (heightMap == null) {
            heightMap = new double[sizeX * sizeY * sizeZ];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        this.perlinNoiseBuffer = this.perlinNoise1.generateNoiseInfdev(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoiseInfdev(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoiseInfdev(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var12 = 0;

        for(int var13 = 0; var13 < sizeX; ++var13) {
            for(int var14 = 0; var14 < sizeZ; ++var14) {
                for(int var15 = 0; var15 < sizeY; ++var15) {
                    double islandOffset = this.getIslandOffset(x + var13, z + var14, -100.0D);

                    double var16 = (var15 - 8.5D) * 12.0D;
                    if(var16 < 0.0D) {
                        var16 *= 2.0D;
                    }

                    double var18 = this.minLimitPerlinNoiseBuffer[var12] / 512.0D;
                    double var20 = this.maxLimitPerlinNoiseBuffer[var12] / 512.0D;
                    double var22 = (this.perlinNoiseBuffer[var12] / 10.0D + 1.0D) / 2.0D;
                    double var24;
                    if(var22 < 0.0D) {
                        var24 = var18;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var24 += islandOffset;
                        }
                    } else if(var22 > 1.0D) {
                        var24 = var20;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var24 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var18 += islandOffset;
                            var20 += islandOffset;
                        }

                        var24 = var18 + (var20 - var18) * var22;
                    }

                    var24 -= var16;
                    heightMap[var12] = var24;
                    ++var12;
                }
            }
        }

        return heightMap;
    }

    protected int getForestNoiseValue(int x, int z) {
        int value = (int)(this.forestNoise.sample((double)x * 0.05D, (double)z * 0.05D) - this.random.nextDouble());

        if (this.theme.isDenseWoods()) {
            if(value <= 4) {
                value = 10;
            }
        } else {
            if (value < 0) {
                value = 0;
            }
        }

        if(this.random.nextInt(100) == 0) {
            ++value;
        }

        return value;
    }

    protected Feature getOldTreeFeature() {
        return new OldLargeOakTreeFeature();
    }
}
