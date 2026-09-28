package com.itselix99.betterworldoptions.world.feature.desertwell;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class DesertWellFeature extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        while(world.isAir(x, y, z) && y > 2) {
            --y;
        }

        int var1 = world.getBlockId(x, y, z);
        if (var1 != Block.SAND.id) {
            return false;
        } else {
            for(int var2 = -2; var2 <= 2; ++var2) {
                for(int var3 = -2; var3 <= 2; ++var3) {
                    if (world.isAir(x + var2, y - 1, z + var3) && world.isAir(x + var2, y - 2, z + var3)) {
                        return false;
                    }
                }
            }

            for(int var4 = -1; var4 <= 0; ++var4) {
                for(int var5 = -2; var5 <= 2; ++var5) {
                    for(int var6 = -2; var6 <= 2; ++var6) {
                        world.setBlockWithoutNotifyingNeighbors(x + var5, y + var4, z + var6, Block.SANDSTONE.id);
                    }
                }
            }

            world.setBlockWithoutNotifyingNeighbors(x, y, z, Block.FLOWING_WATER.id);
            world.setBlockWithoutNotifyingNeighbors(x - 1, y, z, Block.FLOWING_WATER.id);
            world.setBlockWithoutNotifyingNeighbors(x + 1, y, z, Block.FLOWING_WATER.id);
            world.setBlockWithoutNotifyingNeighbors(x, y, z - 1, Block.FLOWING_WATER.id);
            world.setBlockWithoutNotifyingNeighbors(x, y, z + 1, Block.FLOWING_WATER.id);

            for(int var7 = -2; var7 <= 2; ++var7) {
                for(int var8 = -2; var8 <= 2; ++var8) {
                    if (var7 == -2 || var7 == 2 || var8 == -2 || var8 == 2) {
                        world.setBlockWithoutNotifyingNeighbors(x + var7, y + 1, z + var8, Block.SANDSTONE.id);
                    }
                }
            }

            world.setBlockWithoutNotifyingNeighbors(x + 2, y + 1, z, Block.SLAB.id, 1);
            world.setBlockWithoutNotifyingNeighbors(x - 2, y + 1, z, Block.SLAB.id, 1);
            world.setBlockWithoutNotifyingNeighbors(x, y + 1, z + 2, Block.SLAB.id, 1);
            world.setBlockWithoutNotifyingNeighbors(x, y + 1, z - 2, Block.SLAB.id, 1);

            for(int var9 = -1; var9 <= 1; ++var9) {
                for(int var10 = -1; var10 <= 1; ++var10) {
                    if (var9 == 0 && var10 == 0) {
                        world.setBlockWithoutNotifyingNeighbors(x + var9, y + 4, z + var10, Block.SANDSTONE.id);
                    } else {
                        world.setBlockWithoutNotifyingNeighbors(x + var9, y + 4, z + var10, Block.SLAB.id, 1);
                    }
                }
            }

            for(int var11 = 1; var11 <= 3; ++var11) {
                world.setBlockWithoutNotifyingNeighbors(x - 1, y + var11, z - 1, Block.SANDSTONE.id);
                world.setBlockWithoutNotifyingNeighbors(x - 1, y + var11, z + 1, Block.SANDSTONE.id);
                world.setBlockWithoutNotifyingNeighbors(x + 1, y + var11, z - 1, Block.SANDSTONE.id);
                world.setBlockWithoutNotifyingNeighbors(x + 1, y + var11, z + 1, Block.SANDSTONE.id);
            }

            return true;
        }
    }
}