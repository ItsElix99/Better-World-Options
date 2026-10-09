package com.itselix99.betterworldoptions.world.feature.adventureupdate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class ClayPatchFeature extends Feature {
    private final int clayBlockId;
    private final int blockCount;

    public ClayPatchFeature(int blockCount) {
        this.clayBlockId = Block.CLAY.id;
        this.blockCount = blockCount;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        if (world.getMaterial(x, y, z) != Material.WATER) {
            return false;
        } else {
            int var6 = random.nextInt(this.blockCount - 2) + 2;
            byte var7 = 1;

            for(int var8 = x - var6; var8 <= x + var6; ++var8) {
                for(int var9 = z - var6; var9 <= z + var6; ++var9) {
                    int var10 = var8 - x;
                    int var11 = var9 - z;
                    if (var10 * var10 + var11 * var11 <= var6 * var6) {
                        for(int var12 = y - var7; var12 <= y + var7; ++var12) {
                            int var13 = world.getBlockId(var8, var12, var9);
                            if (var13 == Block.DIRT.id || var13 == Block.CLAY.id) {
                                world.setBlockWithoutNotifyingNeighbors(var8, var12, var9, this.clayBlockId);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}