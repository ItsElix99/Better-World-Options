package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.interfaces.BWOBiomeSource;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.biome.BiomeCache;
import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release.ReleaseLayer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdvUpdBiomeSource extends BiomeSource implements BWOBiomeSource {
    protected Layer noiseBiomes;
    protected Layer biomes;
    protected Layer temperatures;
    protected Layer downfalls;
    protected final BiomeCache cache;
    protected List<Biome> biomesForSpawnPoint;
    @Environment(EnvType.CLIENT)
    public Biome[] biomesBuffer;
    public float[] temperaturesBuffer;

    protected AdvUpdBiomeSource() {
        this.cache = new BiomeCache(this);
        this.biomesForSpawnPoint = new ArrayList<>();
        this.biomesForSpawnPoint.add(Biome.FOREST);
        this.biomesForSpawnPoint.add(Biome.SWAMPLAND);
        this.biomesForSpawnPoint.add(Biome.TAIGA);
    }

    public AdvUpdBiomeSource(World world) {
        this();
        Layer[] var2;
        WorldType worldType = ((BWOWorld) world).bwo_getWorldType();

        if (worldType.getId().equals(BetterWorldOptions.NAMESPACE.id("beta_1.8.1"))) {
            var2 = Layer.init(world);
        } else {
            var2 = ReleaseLayer.init(world);
        }

        this.noiseBiomes = var2[0];
        this.biomes = var2[1];
        this.temperatures = var2[2];
        this.downfalls = var2[3];
    }

    public List<Biome> getBiomesForSpawnPoint() {
        return this.biomesForSpawnPoint;
    }

    public Biome getBiome(ChunkPos pos) {
        return this.getBiome(pos.x << 4, pos.z << 4);
    }

    public Biome getBiome(int x, int z) {
        return this.cache.getBiome(x, z);
    }

    @Environment(EnvType.CLIENT)
    public float getDownfall(int x, int z) {
        return this.cache.getDownfall(x, z);
    }

    public float[] getDownfalls(float[] downfalls, int x, int z, int width, int depth) {
        IntCache.next();
        if (downfalls == null || downfalls.length < width * depth) {
            downfalls = new float[width * depth];
        }

        int[] var6 = this.downfalls.getArea(x, z, width, depth);

        for(int var7 = 0; var7 < width * depth; ++var7) {
            float var8 = (float)var6[var7] / 65536.0F;
            if (var8 > 1.0F) {
                var8 = 1.0F;
            }

            downfalls[var7] = var8;
        }

        return downfalls;
    }

    public double[] getDownfalls(double[] downfalls, int x, int z, int width, int depth) {
        IntCache.next();
        if (downfalls == null || downfalls.length < width * depth) {
            downfalls = new double[width * depth];
        }

        int[] var6 = this.downfalls.getArea(x, z, width, depth);

        for(int var7 = 0; var7 < width * depth; ++var7) {
            float var8 = (float)var6[var7] / 65536.0F;
            if (var8 > 1.0F) {
                var8 = 1.0F;
            }

            downfalls[var7] = var8;
        }

        return downfalls;
    }

    @Environment(EnvType.CLIENT)
    public double getTemperature(int x, int z) {
        this.temperatureMap = this.getTemperatures(this.temperatureMap, x, z, 1, 1);
        return this.cache.getTemperature(x, z);
    }

    public float[] getTemperatures(int x, int z, int width, int depth) {
        this.temperaturesBuffer = this.getTemperatures(this.temperaturesBuffer, x, z, width, depth);
        return this.temperaturesBuffer;
    }

    public float[] getTemperatures(float[] temperatures, int x, int z, int width, int depth) {
        IntCache.next();
        if (temperatures == null || temperatures.length < width * depth) {
            temperatures = new float[width * depth];
        }

        int[] var6 = this.temperatures.getArea(x, z, width, depth);

        for(int var7 = 0; var7 < width * depth; ++var7) {
            float var8 = (float)var6[var7] / 65536.0F;
            if (var8 > 1.0F) {
                var8 = 1.0F;
            }

            temperatures[var7] = var8;
        }

        return temperatures;
    }

    public double[] getTemperatures(double[] temperatures, int x, int z, int width, int depth) {
        IntCache.next();
        if (temperatures == null || temperatures.length < width * depth) {
            temperatures = new double[width * depth];
        }

        int[] var6 = this.temperatures.getArea(x, z, width, depth);

        for(int var7 = 0; var7 < width * depth; ++var7) {
            float var8 = (float)var6[var7] / 65536.0F;
            if (var8 > 1.0F) {
                var8 = 1.0F;
            }

            temperatures[var7] = var8;
        }

        return temperatures;
    }

    public Biome[] getNoiseBiomes(Biome[] noiseBiomes, int x, int z, int width, int depth) {
        IntCache.next();
        if (noiseBiomes == null || noiseBiomes.length < width * depth) {
            noiseBiomes = new Biome[width * depth];
        }

        int[] var6 = this.noiseBiomes.getArea(x, z, width, depth);

        for(int var7 = 0; var7 < width * depth; ++var7) {
            noiseBiomes[var7] = AdvUpdBiomeProperties.BY_ID[var6[var7]];
        }

        return noiseBiomes;
    }

    public Biome[] getBiomesInArea(int x, int z, int width, int depth) {
        if (width == 16 && depth == 16 && (x & 15) == 0 && (z & 15) == 0) {
            return this.cache.getBiomes(x, z);
        } else {
            this.biomesBuffer = this.getBiomesInArea(this.biomesBuffer, x, z, width, depth);
            return this.biomesBuffer;
        }
    }

    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth) {
        return this.getBiomesInArea(biomes, x, z, width, depth, true);
    }

    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth, boolean useCache) {
        IntCache.next();
        if (biomes == null || biomes.length < width * depth) {
            biomes = new Biome[width * depth];
        }

        this.temperatureMap = this.getTemperatures(this.temperatureMap, x, z, width, depth);
        this.downfallMap = this.getDownfalls(this.downfallMap, x, z, width, depth);

        if (useCache && width == 16 && depth == 16 && (x & 15) == 0 && (z & 15) == 0) {
            Biome[] var9 = this.cache.getBiomes(x, z);
            System.arraycopy(var9, 0, biomes, 0, width * depth);
            return biomes;
        } else {
            int[] var7 = this.biomes.getArea(x, z, width, depth);

            for(int var8 = 0; var8 < width * depth; ++var8) {
                biomes[var8] = AdvUpdBiomeProperties.BY_ID[var7[var8]];
            }

            return biomes;
        }
    }

    public boolean bwo_isBiomeWithin(World world, int x, int z, int range, List<Biome> biomes) {
        int var6 = x - range >> 2;
        int var7 = z - range >> 2;
        int var8 = x + range >> 2;
        int var9 = z + range >> 2;
        int var10 = var8 - var6 + 1;
        int var11 = var9 - var7 + 1;
        int[] var12 = this.noiseBiomes.getArea(var6, var7, var10, var11);

        for(int var13 = 0; var13 < var10 * var11; ++var13) {
            Biome var14 = AdvUpdBiomeProperties.BY_ID[var12[var13]];
            if (!biomes.contains(var14)) {
                return false;
            }
        }

        return true;
    }

    public BlockPos bwo_findBiome(int x, int z, int range, List<Biome> biomes, Random random) {
        int var6 = x - range >> 2;
        int var7 = z - range >> 2;
        int var8 = x + range >> 2;
        int var9 = z + range >> 2;
        int var10 = var8 - var6 + 1;
        int var11 = var9 - var7 + 1;
        int[] var12 = this.noiseBiomes.getArea(var6, var7, var10, var11);
        BlockPos var13 = null;
        int var14 = 0;

        for(int var15 = 0; var15 < var12.length; ++var15) {
            int var16 = var6 + var15 % var10 << 2;
            int var17 = var7 + var15 / var10 << 2;
            Biome var18 = AdvUpdBiomeProperties.BY_ID[var12[var15]];
            if (biomes.contains(var18) && (var13 == null || random.nextInt(var14 + 1) == 0)) {
                var13 = new BlockPos(var16, 0, var17);
                ++var14;
            }
        }

        return var13;
    }

    public void tick() {
        this.cache.tick();
    }
}