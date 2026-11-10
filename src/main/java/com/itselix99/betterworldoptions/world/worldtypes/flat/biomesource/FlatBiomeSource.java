package com.itselix99.betterworldoptions.world.worldtypes.flat.biomesource;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOBiomeSource;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FlatBiomeSource extends BiomeSource implements BWOBiomeSource {
    private Biome biome;
    private double temperature;
    private double downfall;

    public FlatBiomeSource(World world) {
        super(world);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        boolean superflat = bwoProperties.bwo_getOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION, false);

        if (!superflat) {
            this.biome = Biome.PLAINS;
            this.temperature = 0.5D;
            this.downfall = 0.5D;
        }
    }

    public FlatBiomeSource(Biome biome, double temperature, double downfall) {
        this.biome = biome;
        this.temperature = temperature;
        this.downfall = downfall;

        if (this.biome == Biome.PLAINS) {
            this.temperature = 0.5D;
            this.downfall = 0.5D;
        }
    }

    public Biome getBiome(ChunkPos chunkPos) {
        if (this.biome != null) {
            return this.biome;
        }

        return super.getBiome(chunkPos);
    }

    public Biome getBiome(int x, int z) {
        if (this.biome != null) {
            return this.biome;
        }

        return super.getBiome(x, z);
    }

    @Environment(EnvType.CLIENT)
    public double getTemperature(int x, int z) {
        if (this.biome != null) {
            return this.temperature;
        }

        return super.getTemperature(x, z);
    }

    public double[] create(double[] map, int x, int z, int width, int depth) {
        if (this.biome != null) {
            if (map == null || map.length < width * depth) {
                map = new double[width * depth];
            }

            Arrays.fill(map, 0, width * depth, this.temperature);
            return map;
        }

        return super.create(map, x, z, width, depth);
    }

    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth) {
        if (this.biome != null) {
            if (biomes == null || biomes.length < width * depth) {
                biomes = new Biome[width * depth];
            }

            if (this.temperatureMap == null || this.temperatureMap.length < width * depth) {
                this.temperatureMap = new double[width * depth];
                this.downfallMap = new double[width * depth];
            }

            Arrays.fill(biomes, 0, width * depth, this.biome);
            Arrays.fill(this.downfallMap, 0, width * depth, this.temperature);
            Arrays.fill(this.temperatureMap, 0, width * depth, this.downfall);
            return biomes;
        }

        return super.getBiomesInArea(biomes, x, z, width, depth);
    }

    public BlockPos bwo_findBiome(int x, int z, int range, List<Biome> biomes, Random random) {
        if (this.biome != null) {
            return biomes.contains(this.biome) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
        } else {
            int var1 = x - range >> 2;
            int var2 = z - range >> 2;
            int var3 = x + range >> 2;
            int var4 = z + range >> 2;

            int var5 = var3 - var1 + 1;
            int var6 = var4 - var2 + 1;

            Biome[] var7 = this.getBiomesInArea(var1, var2, var5, var6);
            BlockPos var8 = null;
            int var9 = 0;

            for(int var10 = 0; var10 < var7.length; ++var10) {
                int var11 = var1 + var10 % var5 << 2;
                int var12 = var2 + var10 / var5 << 2;
                Biome var13 = var7[var10];

                if (biomes.contains(var13) && (var8 == null || random.nextInt(var9 + 1) == 0)) {
                    var8 = new BlockPos(var11, 0, var12);
                    ++var9;
                }
            }

            return var8;
        }
    }

    public boolean bwo_isBiomeWithin(World world, int x, int z, int range, List<Biome> biomes) {
        if (this.biome != null) {
            return biomes.contains(this.biome);
        } else {
            return biomes.contains(this.getBiome(x, z));
        }
    }
}