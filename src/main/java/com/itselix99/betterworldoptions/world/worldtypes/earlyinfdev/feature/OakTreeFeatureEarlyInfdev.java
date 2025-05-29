package com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.feature;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class OakTreeFeatureEarlyInfdev extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        int var6 = random.nextInt(3) + 4;
        boolean var7 = true;
        if (y > 0 && y + var6 + 1 <= 128) {
            for(int var8 = y; var8 <= y + 1 + var6; ++var8) {
                byte var9 = 1;
                if (var8 == y) {
                    var9 = 0;
                }

                if (var8 >= y + 1 + var6 - 2) {
                    var9 = 2;
                }

                for(int var10 = x - var9; var10 <= x + var9 && var7; ++var10) {
                    for(int var11 = z - var9; var11 <= z + var9 && var7; ++var11) {
                        if (var8 >= 0 && var8 < 128) {
                            if (world.getBlockId(var10, var8, var11) != 0) {
                                var7 = false;
                            }
                        } else {
                            var7 = false;
                        }
                    }
                }
            }

            if (!var7) {
                return false;
            } else {
                int var16;
                if (((var16 = world.getBlockId(x, y - 1, z)) == Block.GRASS_BLOCK.id || var16 == Block.DIRT.id) && y < 128 - var6 - 1) {
                    world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.DIRT.id);

                    for(int var17 = y - 3 + var6; var17 <= y + var6; ++var17) {
                        int var19 = var17 - (y + var6);
                        int var21 = 1 - var19 / 2;

                        for(int var22 = x - var21; var22 <= x + var21; ++var22) {
                            int var13 = var22 - x;

                            for(int var14 = z - var21; var14 <= z + var21; ++var14) {
                                int var15 = var14 - z;
                                if ((Math.abs(var13) != var21 || Math.abs(var15) != var21 || random.nextInt(2) != 0 && var19 != 0) && !Block.BLOCKS_OPAQUE[world.getBlockId(var22, var17, var14)]) {
                                    world.setBlockWithoutNotifyingNeighbors(var22, var17, var14, BetterWorldOptions.ALPHA_LEAVES.id);
                                }
                            }
                        }
                    }

                    for(int var18 = 0; var18 < var6; ++var18) {
                        if (!Block.BLOCKS_OPAQUE[world.getBlockId(x, y + var18, z)]) {
                            world.setBlockWithoutNotifyingNeighbors(x, y + var18, z, Block.LOG.id);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}