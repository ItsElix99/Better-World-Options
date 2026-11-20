package com.itselix99.betterworldoptions.world.spawnpoint;

import com.itselix99.betterworldoptions.api.worldtype.SpawnPoint;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source.AdvUpdBiomeSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;

import java.util.List;
import java.util.Random;

public class AdvUpdSpawnPoint extends SpawnPoint {
    public AdvUpdSpawnPoint(BWOProperties bwoProperties) {
        super(bwoProperties);
    }

    public void init(World world, Dimension dimension, Random random, WorldProperties properties) {
        List<Biome> biomesList = ((AdvUpdBiomeSource) world.method_1781()).getBiomesForSpawnPoint();
        Random spawnPointRandom = new Random(world.getSeed());
        BlockPos blockPos = ((AdvUpdBiomeSource) world.method_1781()).bwo_findBiome(0, 0, 256, biomesList, spawnPointRandom);
        boolean finiteWorldMCPE = this.finiteWorld && this.finiteWorldType.equals("MCPE");
        int x = finiteWorldMCPE ? this.width / 2 : 0;
        int y = 64;
        int z = finiteWorldMCPE ? this.length / 2 : 0;

        if (blockPos != null) {
            x = blockPos.x;
            z = blockPos.z;
        } else {
            System.out.println("Unable to find spawn biome");
        }

        int attempts = 0;

        while(!dimension.isValidSpawnPoint(x, z)) {
            x += spawnPointRandom.nextInt(64) - spawnPointRandom.nextInt(64);
            z += spawnPointRandom.nextInt(64) - spawnPointRandom.nextInt(64);
            ++attempts;
            if (attempts == 1000) {
                break;
            }
        }

        properties.setSpawn(x, y, z);
    }
}