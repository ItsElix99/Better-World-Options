package com.itselix99.betterworldoptions.world.worldtypes.mcpe.biomesource;

import com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.MTRandom;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.math.noise.OctavePerlinNoiseSamplerMCPE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;

public class MCPEBiomeSource extends BiomeSource {
    private final OctavePerlinNoiseSamplerMCPE mcpeTemperatureSampler;
    private final OctavePerlinNoiseSamplerMCPE mcpeDownfallSampler;
    private final OctavePerlinNoiseSamplerMCPE mcpeWeirdnessSampler;

    public MCPEBiomeSource(World world) {
        this.mcpeTemperatureSampler = new OctavePerlinNoiseSamplerMCPE(new MTRandom((int) (world.getSeed() * 9871L)), 4);
        this.mcpeDownfallSampler = new OctavePerlinNoiseSamplerMCPE(new MTRandom((int) (world.getSeed() * 39811L)), 4);
        this.mcpeWeirdnessSampler = new OctavePerlinNoiseSamplerMCPE(new MTRandom((int) (world.getSeed() * 543321L)), 2);
    }

    public Biome getBiome(ChunkPos chunkPos) {
        return this.getBiome(chunkPos.x << 4, chunkPos.z << 4);
    }

    public Biome getBiome(int x, int z) {
        return this.getBiomesInArea(x, z, 1, 1)[0];
    }

    @Environment(EnvType.CLIENT)
    public double getTemperature(int x, int z) {
        this.temperatureMap = this.mcpeTemperatureSampler.create(this.temperatureMap, x, z, 1, 1, 0.025F, 0.025F, 0.5F);
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

        map = this.mcpeTemperatureSampler.create(map, x, z, width, depth, 0.025F, 0.025F, 0.25F);
        this.weirdnessMap = this.mcpeWeirdnessSampler.create(this.weirdnessMap, x, z, width, depth, 0.25F, 0.25F, 0.5882352941176471);
        int var6 = 0;

        for(int var7 = 0; var7 < width; ++var7) {
            for(int var8 = 0; var8 < depth; ++var8) {
                double var9 = this.weirdnessMap[var6] * 1.1 + (double)0.5F;
                double var11 = 0.01;
                double var13 = (double)1.0F - var11;
                double var15 = (map[var6] * 0.15 + 0.7) * var13 + var9 * var11;
                var15 = (double)1.0F - ((double)1.0F - var15) * ((double)1.0F - var15);
                if (var15 < (double)0.0F) {
                    var15 = (double)0.0F;
                }

                if (var15 > (double)1.0F) {
                    var15 = (double)1.0F;
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

        this.temperatureMap = this.mcpeTemperatureSampler.create(this.temperatureMap, x, z, width, width, 0.025F, 0.025F, 0.25F);
        this.downfallMap = this.mcpeDownfallSampler.create(this.downfallMap, x, z, width, width, 0.05F, 0.05F, 0.3333333333333333);
        this.weirdnessMap = this.mcpeWeirdnessSampler.create(this.weirdnessMap, x, z, width, width, 0.25F, 0.25F, 0.5882352941176471);
        int var6 = 0;

        for(int var7 = 0; var7 < width; ++var7) {
            for(int var8 = 0; var8 < depth; ++var8) {
                double var9 = this.weirdnessMap[var6] * 1.1 + (double)0.5F;
                double var11 = 0.01;
                double var13 = (double)1.0F - var11;
                double var15 = (this.temperatureMap[var6] * 0.15 + 0.7) * var13 + var9 * var11;
                var11 = 0.002;
                var13 = (double)1.0F - var11;
                double var17 = (this.downfallMap[var6] * 0.15 + (double)0.5F) * var13 + var9 * var11;
                var15 = (double)1.0F - ((double)1.0F - var15) * ((double)1.0F - var15);
                if (var15 < (double)0.0F) {
                    var15 = (double)0.0F;
                }

                if (var17 < (double)0.0F) {
                    var17 = (double)0.0F;
                }

                if (var15 > (double)1.0F) {
                    var15 = (double)1.0F;
                }

                if (var17 > (double)1.0F) {
                    var17 = (double)1.0F;
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