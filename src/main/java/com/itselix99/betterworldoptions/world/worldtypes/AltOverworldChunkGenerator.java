package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;

public class AltOverworldChunkGenerator extends BWOChunkGenerator {
    public AltOverworldChunkGenerator(World world, long seed) {
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
        double[] var12 = this.world.method_1781().temperatureMap;
        double[] var13 = this.world.method_1781().downfallMap;
        this.scaleNoiseBuffer = this.scaleNoise.generateNoise(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121D, 1.121D, 0.5D);
        this.depthNoiseBuffer = this.depthNoise.generateNoise(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0D, 200.0D, 0.5D);
        this.perlinNoiseBuffer = this.perlinNoise1.generateNoise(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.generateNoise(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.generateNoise(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / sizeX;

        for (int var17 = 0; var17 < sizeX; ++var17) {
            int var18 = var17 * var16 + var16 / 2;

            for (int var19 = 0; var19 < sizeZ; ++var19) {
                double islandOffset = this.getIslandOffset(x + var17, z + var19, -200.0D);

                int var20 = var19 * var16 + var16 / 2;
                double var21 = var12[var18 * 16 + var20];
                double var23 = var13[var18 * 16 + var20] * var21;
                double var25 = 1.0D - var23;
                var25 *= var25;
                var25 *= var25;
                var25 = 1.0D - var25;
                double var27 = (this.scaleNoiseBuffer[var15] + 256.0D) / 512.0D;
                var27 *= var25;
                if (var27 > 1.0D) {
                    var27 = 1.0D;
                }

                double var29 = this.depthNoiseBuffer[var15] / 8000.0D;
                if (var29 < 0.0D) {
                    var29 = -var29 * 0.3D;
                }

                var29 = var29 * 3.0D - 2.0D;
                if (var29 < 0.0D) {
                    var29 /= 2.0D;
                    if (var29 < -1.0D) {
                        var29 = -1.0D;
                    }

                    var29 /= 1.4D;
                    var29 /= 2.0D;
                    var27 = 0.0D;
                } else {
                    if (var29 > 1.0D) {
                        var29 = 1.0D;
                    }

                    var29 /= 8.0D;
                }

                if (var27 < 0.0D) {
                    var27 = 0.0D;
                }

                var27 += 0.5D;
                if (this.worldType.getId().equals(BetterWorldOptions.NAMESPACE.id("amplified"))) {
                    double heightScale = this.world.getHeight() / 256.0D;
                    if (heightScale < 1.0D) heightScale = 1.0D;

                    if (var27 > 0.0D) {
                        var27 = (1.0D + var27 * 2.0D) * heightScale;
                        var29 = (1.0D + var29 * 4.0D) * heightScale;
                    }
                }
                var29 = var29 * 17.0D / 16.0D;
                double var31 = 17.0D / 2.0D + var29 * 4.0D;
                ++var15;

                for (int var33 = 0; var33 < sizeY; ++var33) {
                    double var34;
                    double var36 = ((double) var33 - var31) * 12.0D / var27;
                    if (var36 < 0.0D) {
                        var36 *= 4.0D;
                    }

                    double var38 = this.minLimitPerlinNoiseBuffer[var14] / 512.0D;
                    double var40 = this.maxLimitPerlinNoiseBuffer[var14] / 512.0D;
                    double var42 = (this.perlinNoiseBuffer[var14] / 10.0D + 1.0D) / 2.0D;
                    if (var42 < 0.0D) {
                        var34 = var38;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else if (var42 > 1.0D) {
                        var34 = var40;

                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var34 += islandOffset;
                        }
                    } else {
                        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
                            var38 += islandOffset;
                            var40 += islandOffset;
                        }

                        var34 = var38 + (var40 - var38) * var42;
                    }

                    var34 -= var36;
                    if (var33 > sizeY - 4) {
                        double var44 = (float) (var33 - (sizeY - 4)) / 3.0D;
                        var34 = var34 * (1.0D - var44) + -10.0D * var44;
                    }

                    heightMap[var14] = var34;
                    ++var14;
                }
            }
        }

        return heightMap;
    }

    public void decorate(ChunkSource source, int x, int z) {
        super.decorate(source, x, z);
    }
}