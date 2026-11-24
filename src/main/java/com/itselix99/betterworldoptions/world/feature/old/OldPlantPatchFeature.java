package com.itselix99.betterworldoptions.world.feature.old;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class OldPlantPatchFeature extends Feature {
    private final int plantBlockId;

    public OldPlantPatchFeature(int plantBlockId) {
        this.plantBlockId = plantBlockId;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        if (world.isAir(x, y, z) && Block.BLOCKS[this.plantBlockId].canGrow(world, x, y, z)) {
            world.setBlockWithoutNotifyingNeighbors(x, y, z, this.plantBlockId);
        }

        return true;
    }
}