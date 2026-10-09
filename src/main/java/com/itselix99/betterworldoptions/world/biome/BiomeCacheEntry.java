package com.itselix99.betterworldoptions.world.biome;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.biome.Biome;

class BiomeCacheEntry {
    public float[] temperatures;
    public float[] downfalls;
    public Biome[] biomes;
    public int chunkX;
    public int chunkZ;
    public long timeOfCreation;
    final BiomeCache cache;

    public BiomeCacheEntry(BiomeCache biomeCache, int chunkX, int chunkZ) {
        this.cache = biomeCache;
        this.temperatures = new float[256];
        this.downfalls = new float[256];
        this.biomes = new Biome[256];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        BiomeCache.getBiomeSource(biomeCache).getTemperatures(this.temperatures, chunkX << 4, chunkZ << 4, 16, 16);
        BiomeCache.getBiomeSource(biomeCache).getDownfalls(this.downfalls, chunkX << 4, chunkZ << 4, 16, 16);
        BiomeCache.getBiomeSource(biomeCache).getBiomesInArea(this.biomes, chunkX << 4, chunkZ << 4, 16, 16, false);
    }

    public Biome getBiome(int x, int z) {
        return this.biomes[x & 15 | (z & 15) << 4];
    }

    @Environment(EnvType.CLIENT)
    public float getTemperature(int x, int z) {
        return this.temperatures[x & 15 | (z & 15) << 4];
    }

    @Environment(EnvType.CLIENT)
    public float getDownfall(int x, int z) {
        return this.downfalls[x & 15 | (z & 15) << 4];
    }
}
