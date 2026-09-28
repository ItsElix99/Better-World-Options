package com.itselix99.betterworldoptions.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public interface BWOBiomeSource {
    boolean bwo_isBiomeWithin(World world, int x, int z, int range, List<Biome> biomes);
    BlockPos bwo_findBiome(int x, int z, int range, List<Biome> biomes, Random random);
}