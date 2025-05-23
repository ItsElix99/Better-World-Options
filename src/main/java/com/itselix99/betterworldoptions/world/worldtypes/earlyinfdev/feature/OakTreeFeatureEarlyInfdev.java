package com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.feature;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class OakTreeFeatureEarlyInfdev extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        int var86 = x + 2;
        int var93 = z + 2;
        int var96 = random.nextInt(3) + 4;
        boolean il = true;
        if (y > 0 && y + var96 + 1 <= 128) {
            for(int im = y; im <= y + 1 + var96; ++im) {
                byte in = 1;
                if (im == y) {
                    in = 0;
                }

                if (im >= y + 1 + var96 - 2) {
                    in = 2;
                }

                for(int ip = var86 - in; ip <= var86 + in && il; ++ip) {
                    for(int ir = var93 - in; ir <= var93 + in && il; ++ir) {
                        if (im >= 0 && im < 128) {
                            if (world.getBlockId(ip, im, ir) != 0) {
                                il = false;
                            }
                        } else {
                            il = false;
                        }
                    }
                }
            }

            if (!il) {
                return false;
            } else {
                int var98;
                if (((var98 = world.getBlockId(var86, y - 1, var93)) == Block.GRASS_BLOCK.id || var98 == Block.DIRT.id) && y < 128 - var96 - 1) {
                    world.setBlock(var86, y - 1, var93, Block.DIRT.id);

                    for(int io = y - 3 + var96; io <= y + var96; ++io) {
                        int var102 = io - (y + var96);
                        int var104 = 1 - var102 / 2;

                        for(int iu = var86 - var104; iu <= var86 + var104; ++iu) {
                            int iv = iu - var86;

                            for(int iw = var93 - var104; iw <= var93 + var104; ++iw) {
                                int ix = iw - var93;
                                if ((Math.abs(iv) != var104 || Math.abs(ix) != var104 || random.nextInt(2) != 0 && var102 != 0) && !Block.BLOCKS_OPAQUE[world.getBlockId(iu, io, iw)]) {
                                    world.setBlock(iu, io, iw, Block.LEAVES.id);
                                }
                            }
                        }
                    }

                    for(int var101 = 0; var101 < var96; ++var101) {
                        if (!Block.BLOCKS_OPAQUE[world.getBlockId(var86, y + var101, var93)]) {
                            world.setBlock(var86, y + var101, var93, Block.LOG.id);
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