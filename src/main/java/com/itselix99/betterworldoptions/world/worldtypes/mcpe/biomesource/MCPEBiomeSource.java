package com.itselix99.betterworldoptions.world.worldtypes.mcpe.biomesource;

import com.itselix99.betterworldoptions.util.MTRandom;
import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;

public class MCPEBiomeSource extends BiomeSource {
    private final BWOOctavePerlinNoiseSampler mcpeTemperatureSampler;
    private final BWOOctavePerlinNoiseSampler mcpeDownfallSampler;
    private final BWOOctavePerlinNoiseSampler mcpeWeirdnessSampler;

    public MCPEBiomeSource(World world) {
        this.mcpeTemperatureSampler = new BWOOctavePerlinNoiseSampler(new MTRandom((int) (world.getSeed() * 9871L)), 4, true);
        this.mcpeDownfallSampler = new BWOOctavePerlinNoiseSampler(new MTRandom((int) (world.getSeed() * 39811L)), 4, true);
        this.mcpeWeirdnessSampler = new BWOOctavePerlinNoiseSampler(new MTRandom((int) (world.getSeed() * 543321L)), 2, true);
    }

    public Biome getBiome(ChunkPos chunkPos) {
        return this.getBiome(chunkPos.x << 4, chunkPos.z << 4);
    }

    public Biome getBiome(int x, int z) {
        return this.getBiomesInArea(x, z, 1, 1)[0];
    }

    @Environment(EnvType.CLIENT)
    public double getTemperature(int x, int z) {
        this.temperatureMap = this.mcpeTemperatureSampler.generateNoise(this.temperatureMap, x, z, 1, 1, 0.025D, 0.025D, 0.5D);
        return this.temperatureMap[0];
    }

    public Biome[] getBiomesInArea(int x, int z, int width, int depth) {
        this.biomes = this.getBiomesInArea(this.biomes, x, z, width, depth);
        return this.biomes;
    }

    public double[] create(double[] map, int x, int z, int width, int depth) {
        if (map == null || map.length < width * depth) {
            map = new double[width * depth];
        }

        map = this.mcpeTemperatureSampler.generateNoise(map, x, z, width, depth, 0.025D, 0.025D, 0.25D);
        this.weirdnessMap = this.mcpeWeirdnessSampler.generateNoise(this.weirdnessMap, x, z, width, depth, 0.25D, 0.25D, 0.5882352941176471D);
        int var6 = 0;

        for(int var7 = 0; var7 < width; ++var7) {
            for(int var8 = 0; var8 < depth; ++var8) {
                double var9 = this.weirdnessMap[var6] * 1.1D + 0.5D;
                double var11 = 0.01D;
                double var13 = 1.0D - var11;
                double var15 = (map[var6] * 0.15D + 0.7D) * var13 + var9 * var11;
                var15 = 1.0D - (1.0D - var15) * (1.0D - var15);
                if (var15 < 0.0D) {
                    var15 = 0.0D;
                }

                if (var15 > 1.0D) {
                    var15 = 1.0D;
                }

                map[var6] = var15;
                ++var6;
            }
        }

        return map;
    }

    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth) {
        if (biomes == null || biomes.length < width * depth) {
            biomes = new Biome[width * depth];
        }

        this.temperatureMap = this.mcpeTemperatureSampler.generateNoise(this.temperatureMap, x, z, width, width, 0.025D, 0.025D, 0.25D);
        this.downfallMap = this.mcpeDownfallSampler.generateNoise(this.downfallMap, x, z, width, width, 0.05D, 0.05D, 0.3333333333333333D);
        this.weirdnessMap = this.mcpeWeirdnessSampler.generateNoise(this.weirdnessMap, x, z, width, width, 0.25D, 0.25D, 0.5882352941176471D);
        int var6 = 0;

        for(int var7 = 0; var7 < width; ++var7) {
            for(int var8 = 0; var8 < depth; ++var8) {
                double var9 = this.weirdnessMap[var6] * 1.1D + 0.5D;
                double var11 = 0.01D;
                double var13 = 1.0D - var11;
                double var15 = (this.temperatureMap[var6] * 0.15D + 0.7D) * var13 + var9 * var11;
                var11 = 0.002D;
                var13 = 1.0D - var11;
                double var17 = (this.downfallMap[var6] * 0.15D + 0.5D) * var13 + var9 * var11;
                var15 = 1.0D - (1.0D - var15) * (1.0D - var15);
                if (var15 < 0.0D) {
                    var15 = 0.0D;
                }

                if (var17 < 0.0D) {
                    var17 = 0.0D;
                }

                if (var15 > 1.0D) {
                    var15 = 1.0D;
                }

                if (var17 > 1.0D) {
                    var17 = 1.0D;
                }

                this.temperatureMap[var6] = var15;
                this.downfallMap[var6] = var17;
                BiomeProvider provider = BiomeAPI.getOverworldProvider();
                biomes[var6++] = provider.getBiome(x + width, z + depth, (float) var15, (float) var17);
            }
        }

        return biomes;
    }
}