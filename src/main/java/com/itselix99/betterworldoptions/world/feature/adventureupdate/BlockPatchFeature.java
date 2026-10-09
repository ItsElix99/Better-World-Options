package com.itselix99.betterworldoptions.world.feature.adventureupdate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class BlockPatchFeature extends Feature {
    private final int block;
    private final int size;

    public BlockPatchFeature(int size, int block) {
        this.block = block;
        this.size = size;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        if (world.getMaterial(x, y, z) != Material.WATER) {
            return false;
        } else {
            int var6 = random.nextInt(this.size - 2) + 2;
            byte var7 = 2;

            for(int var8 = x - var6; var8 <= x + var6; ++var8) {
                for(int var9 = z - var6; var9 <= z + var6; ++var9) {
                    int var10 = var8 - x;
                    int var11 = var9 - z;
                    if (var10 * var10 + var11 * var11 <= var6 * var6) {
                        for(int var12 = y - var7; var12 <= y + var7; ++var12) {
                            int var13 = world.getBlockId(var8, var12, var9);
                            if (var13 == Block.DIRT.id || var13 == Block.GRASS_BLOCK.id) {
                                world.setBlockWithoutNotifyingNeighbors(var8, var12, var9, this.block);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}