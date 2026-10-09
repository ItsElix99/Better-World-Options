package com.itselix99.betterworldoptions.world.biome;

import com.itselix99.betterworldoptions.util.Long2ObjectHashMap;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source.AdvUpdBiomeSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.ArrayList;
import java.util.List;

public class BiomeCache {
    private final BiomeSource source;
    private long timeOfLastCleanUp = 0L;
    private final Long2ObjectHashMap entriesByChunkPos = new Long2ObjectHashMap();
    private final List<BiomeCacheEntry> entries = new ArrayList<>();

    public BiomeCache(BiomeSource source) {
        this.source = source;
    }

    private BiomeCacheEntry getEntry(int x, int z) {
        x >>= 4;
        z >>= 4;
        long var3 = (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
        BiomeCacheEntry var5 = (BiomeCacheEntry)this.entriesByChunkPos.get(var3);
        if (var5 == null) {
            var5 = new BiomeCacheEntry(this, x, z);
            this.entriesByChunkPos.put(var3, var5);
            this.entries.add(var5);
        }

        var5.timeOfCreation = System.currentTimeMillis();
        return var5;
    }

    public Biome getBiome(int x, int z) {
        return this.getEntry(x, z).getBiome(x, z);
    }

    @Environment(EnvType.CLIENT)
    public float getTemperature(int x, int z) {
        return this.getEntry(x, z).getTemperature(x, z);
    }

    @Environment(EnvType.CLIENT)
    public float getDownfall(int x, int z) {
        return this.getEntry(x, z).getDownfall(x, z);
    }

    public void tick() {
        long var1 = System.currentTimeMillis();
        long var3 = var1 - this.timeOfLastCleanUp;
        if (var3 > 7500L || var3 < 0L) {
            this.timeOfLastCleanUp = var1;

            for(int var5 = 0; var5 < this.entries.size(); ++var5) {
                BiomeCacheEntry var6 = this.entries.get(var5);
                long var7 = var1 - var6.timeOfCreation;
                if (var7 > 30000L || var7 < 0L) {
                    this.entries.remove(var5--);
                    long var9 = (long)var6.chunkX & 4294967295L | ((long)var6.chunkZ & 4294967295L) << 32;
                    this.entriesByChunkPos.remove(var9);
                }
            }
        }

    }

    public Biome[] getBiomes(int x, int z) {
        return this.getEntry(x, z).biomes;
    }

    static AdvUpdBiomeSource getBiomeSource(BiomeCache var0) {
        return (AdvUpdBiomeSource) var0.source;
    }
}