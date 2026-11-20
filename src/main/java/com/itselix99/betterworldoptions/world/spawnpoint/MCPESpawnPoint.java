package com.itselix99.betterworldoptions.world.spawnpoint;

import com.itselix99.betterworldoptions.api.worldtype.SpawnPoint;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;

import java.util.Random;

public class MCPESpawnPoint extends SpawnPoint {
    public MCPESpawnPoint(BWOProperties bwoProperties) {
        super(bwoProperties);
    }

    public void init(World world, Dimension dimension, Random random, WorldProperties properties) {
        int x = this.width / 2;
        int y = 64;
        int z = this.length / 2;
        properties.setSpawn(x, y, z);
    }
}