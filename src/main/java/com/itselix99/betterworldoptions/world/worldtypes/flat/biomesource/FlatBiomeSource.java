package com.itselix99.betterworldoptions.world.worldtypes.flat.biomesource;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.Arrays;

public class FlatBiomeSource extends BiomeSource {
    private final boolean superflat;

    public FlatBiomeSource(World world) {
        super(world);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.superflat = bwoProperties.bwo_getBooleanOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION);
    }

    public Biome getBiome(ChunkPos chunkPos) {
        if (!this.superflat) {
            return Biome.PLAINS;
        }

        return super.getBiome(chunkPos);
    }

    public Biome getBiome(int x, int z) {
        if (!this.superflat) {
            return Biome.PLAINS;
        }

        return super.getBiome(x, z);
    }

    @Environment(EnvType.CLIENT)
    public double getTemperature(int x, int z) {
        if (!this.superflat) {
            return 1.0D;
        }

        return super.getTemperature(x, z);
    }

    public double[] create(double[] map, int x, int z, int width, int depth) {
        if (!this.superflat) {
            if (map == null || map.length < width * depth) {
                map = new double[width * depth];
            }

            Arrays.fill(map, 0, width * depth, 1.0D);
            return map;
        }

        return super.create(map, x, z, width, depth);
    }

    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth) {
        if (!this.superflat) {
            if (biomes == null || biomes.length < width * depth) {
                biomes = new Biome[width * depth];
            }

            if (this.temperatureMap == null || this.temperatureMap.length < width * depth) {
                this.temperatureMap = new double[width * depth];
                this.downfallMap = new double[width * depth];
            }

            Arrays.fill(biomes, 0, width * depth, Biome.PLAINS);
            Arrays.fill(this.downfallMap, 0, width * depth, 0.4D);
            Arrays.fill(this.temperatureMap, 0, width * depth, 1.0D);
            return biomes;
        }

        return super.getBiomesInArea(biomes, x, z, width, depth);
    }
}