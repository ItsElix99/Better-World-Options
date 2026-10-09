package com.itselix99.betterworldoptions.world.feature.adventureupdate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class SwampTreeFeature extends Feature {
    private static int vineBlock = 0;

    public boolean generate(World world, Random random, int x, int y, int z) {
        int var6;
        for(var6 = random.nextInt(4) + 5; world.getMaterial(x, y - 1, z) == Material.WATER; --y) {
        }

        boolean var7 = true;
        if(y >= 1) {
            int var10000 = y + var6 + 1;
            if(var10000 <= world.getTopY()) {
                int var8;
                int var10;
                int var11;
                int var12;
                for(var8 = y; var8 <= y + 1 + var6; ++var8) {
                    byte var9 = 1;
                    if(var8 == y) {
                        var9 = 0;
                    }

                    if(var8 >= y + 1 + var6 - 2) {
                        var9 = 3;
                    }

                    for(var10 = x - var9; var10 <= x + var9 && var7; ++var10) {
                        for(var11 = z - var9; var11 <= z + var9 && var7; ++var11) {
                            if(var8 >= world.getBottomY()) {
                                if(var8 < world.getTopY()) {
                                    var12 = world.getBlockId(var10, var8, var11);
                                    if(var12 == 0 || var12 == Block.LEAVES.id) {
                                        continue;
                                    }

                                    if(var12 != Block.WATER.id && var12 != Block.FLOWING_WATER.id) {
                                        var7 = false;
                                        continue;
                                    }

                                    if(var8 > y) {
                                        var7 = false;
                                    }
                                    continue;
                                }
                            }

                            var7 = false;
                        }
                    }
                }

                if(!var7) {
                    return false;
                }

                var8 = world.getBlockId(x, y - 1, z);
                if(var8 == Block.GRASS_BLOCK.id || var8 == Block.DIRT.id) {
                    if(y < world.getTopY() - var6 - 1) {
                        world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.DIRT.id);

                        int var13;
                        int var16;
                        for(var16 = y - 3 + var6; var16 <= y + var6; ++var16) {
                            var10 = var16 - (y + var6);
                            var11 = 2 - var10 / 2;

                            for(var12 = x - var11; var12 <= x + var11; ++var12) {
                                var13 = var12 - x;

                                for(int var14 = z - var11; var14 <= z + var11; ++var14) {
                                    int var15 = var14 - z;
                                    if((Math.abs(var13) != var11 || Math.abs(var15) != var11 || random.nextInt(2) != 0 && var10 != 0) && !Block.BLOCKS_OPAQUE[world.getBlockId(var12, var16, var14)]) {
                                        world.setBlockWithoutNotifyingNeighbors(var12, var16, var14, Block.LEAVES.id);
                                    }
                                }
                            }
                        }

                        for(var16 = 0; var16 < var6; ++var16) {
                            var10 = world.getBlockId(x, y + var16, z);
                            if(var10 == 0 || var10 == Block.LEAVES.id || var10 == Block.FLOWING_WATER.id || var10 == Block.WATER.id) {
                                world.setBlockWithoutNotifyingNeighbors(x, y + var16, z, Block.LOG.id);
                            }
                        }

                        for(var16 = y - 3 + var6; var16 <= y + var6; ++var16) {
                            var10 = var16 - (y + var6);
                            var11 = 2 - var10 / 2;

                            for(var12 = x - var11; var12 <= x + var11; ++var12) {
                                for(var13 = z - var11; var13 <= z + var11; ++var13) {
                                    if(world.getBlockId(var12, var16, var13) == Block.LEAVES.id) {
                                        if(random.nextInt(4) == 0 && world.getBlockId(var12 - 1, var16, var13) == 0) {
                                            this.placeVine(world, var12 - 1, var16, var13, 8);
                                        }

                                        if(random.nextInt(4) == 0 && world.getBlockId(var12 + 1, var16, var13) == 0) {
                                            this.placeVine(world, var12 + 1, var16, var13, 2);
                                        }

                                        if(random.nextInt(4) == 0 && world.getBlockId(var12, var16, var13 - 1) == 0) {
                                            this.placeVine(world, var12, var16, var13 - 1, 1);
                                        }

                                        if(random.nextInt(4) == 0 && world.getBlockId(var12, var16, var13 + 1) == 0) {
                                            this.placeVine(world, var12, var16, var13 + 1, 4);
                                        }
                                    }
                                }
                            }
                        }

                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    public static void setVineBlock(int blockId) {
        vineBlock = blockId;
    }

    private void placeVine(World world, int x, int y, int z, int metadata) {
        world.setBlock(x, y, z, vineBlock, metadata);
        int var6 = 4;

        while(true) {
            --y;
            if (world.getBlockId(x, y, z) != 0 || var6 <= 0) {
                return;
            }

            world.setBlock(x, y, z, vineBlock, metadata);
            --var6;
        }
    }
}
