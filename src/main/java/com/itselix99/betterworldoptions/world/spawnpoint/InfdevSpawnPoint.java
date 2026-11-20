package com.itselix99.betterworldoptions.world.spawnpoint;

import com.itselix99.betterworldoptions.api.worldtype.SpawnPoint;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;

import java.util.Random;

public class InfdevSpawnPoint extends SpawnPoint {
    public InfdevSpawnPoint(BWOProperties bwoProperties) {
        super(bwoProperties);
    }

    public void init(World world, Dimension dimension, Random random, WorldProperties properties) {
        if (this.oldFeatures) {
            boolean finiteWorldMCPE = this.finiteWorld && this.finiteWorldType.equals("MCPE");
            int x = finiteWorldMCPE ? this.width / 2 : 0;
            int y = 64;
            int z = finiteWorldMCPE ? this.length / 2 : 0;
            properties.setSpawn(x, y, z);
        } else {
            super.init(world, dimension, random, properties);
        }
    }
}