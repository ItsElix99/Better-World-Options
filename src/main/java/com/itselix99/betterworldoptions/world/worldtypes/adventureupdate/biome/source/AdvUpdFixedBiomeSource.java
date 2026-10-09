package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AdvUpdFixedBiomeSource extends AdvUpdBiomeSource {
    private final Biome biome;
    private final double temperature;
    private final double downfall;

    public AdvUpdFixedBiomeSource(Biome biome, double temperature, double downfall) {
        this.biome = biome;
        this.temperature = temperature;
        this.downfall = downfall;
        this.biomesForSpawnPoint = new ArrayList<>();
        this.biomesForSpawnPoint.add(biome);
    }

    public Biome getBiome(ChunkPos pos) {
        return this.biome;
    }

    public Biome getBiome(int x, int z) {
        return this.biome;
    }

    public Biome[] getBiomesInArea(int x, int z, int width, int depth) {
        this.biomesBuffer = this.getBiomesInArea(this.biomesBuffer, x, z, width, depth);
        return this.biomesBuffer;
    }

    public float[] getTemperatures(float[] temperatures, int x, int z, int width, int depth) {
        if (temperatures == null || temperatures.length < width * depth) {
            temperatures = new float[width * depth];
        }

        Arrays.fill(temperatures, 0, width * depth, (float) this.temperature);
        return temperatures;
    }

    public double[] getTemperatures(double[] temperatures, int x, int z, int width, int depth) {
        if (temperatures == null || temperatures.length < width * depth) {
            temperatures = new double[width * depth];
        }

        Arrays.fill(temperatures, 0, width * depth, this.temperature);
        return temperatures;
    }

    public float[] getTemperatures(int x, int z, int width, int depth) {
        return this.getTemperatures(new float[width * depth], x, z, width, depth);
    }

    @Environment(EnvType.CLIENT)
    public double getTemperature(int x, int z) {
        return this.temperature;
    }

    public float[] getDownfalls(float[] downfalls, int x, int z, int width, int depth) {
        if (downfalls == null || downfalls.length < width * depth) {
            downfalls = new float[width * depth];
        }

        Arrays.fill(downfalls, 0, width * depth, (float) this.downfall);
        return downfalls;
    }

    public double[] getDownfalls(double[] downfalls, int x, int z, int width, int depth) {
        if (downfalls == null || downfalls.length < width * depth) {
            downfalls = new double[width * depth];
        }

        Arrays.fill(downfalls, 0, width * depth, this.downfall);
        return downfalls;
    }

    @Environment(EnvType.CLIENT)
    public float getDownfall(int x, int z) {
        return (float) this.downfall;
    }

    public Biome[] getNoiseBiomes(Biome[] noiseBiomes, int x, int z, int width, int depth) {
        if (noiseBiomes == null || noiseBiomes.length < width * depth) {
            noiseBiomes = new Biome[width * depth];
        }

        Arrays.fill(noiseBiomes, 0, width * depth, this.biome);
        return noiseBiomes;
    }

    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth) {
        if (biomes == null || biomes.length < width * depth) {
            biomes = new Biome[width * depth];
        }

        if (this.temperatureMap == null || this.temperatureMap.length < width * depth) {
            this.temperatureMap = new double[width * depth];
            this.downfallMap = new double[width * depth];
        }

        Arrays.fill(biomes, 0, width * depth, this.biome);
        Arrays.fill(this.downfallMap, 0, width * depth, this.downfall);
        Arrays.fill(this.temperatureMap, 0, width * depth, this.temperature);
        return biomes;
    }

    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth, boolean useCache) {
        return this.getBiomesInArea(biomes, x, z, width, depth);
    }

    public BlockPos bwo_findBiome(int x, int z, int range, List<Biome> biomes, Random random) {
        return biomes.contains(this.biome) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }

    public boolean bwo_isBiomeWithin(World world, int x, int z, int range, List<Biome> biomes) {
        return biomes.contains(this.biome);
    }
}