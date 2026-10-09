package com.itselix99.betterworldoptions.world.feature.adventureupdate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class AdvUpdLakeFeature extends Feature {
    private final int liquid;

    public AdvUpdLakeFeature(int liquid) {
        this.liquid = liquid;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        x -= 8;

        for(z -= 8; y > 0 && world.isAir(x, y, z); --y) {
        }

        y -= 4;
        boolean[] var6 = new boolean[2048];
        int var7 = random.nextInt(4) + 4;

        for(int var8 = 0; var8 < var7; ++var8) {
            double var9 = random.nextDouble() * 6.0D + 3.0D;
            double var11 = random.nextDouble() * 4.0D + 2.0D;
            double var13 = random.nextDouble() * 6.0D + 3.0D;
            double var15 = random.nextDouble() * (16.0D - var9 - 2.0D) + 1.0D + var9 / 2.0D;
            double var17 = random.nextDouble() * (8.0D - var11 - 4.0D) + 2.0D + var11 / 2.0D;
            double var19 = random.nextDouble() * (16.0D - var13 - 2.0D) + 1.0D + var13 / 2.0D;

            for(int var21 = 1; var21 < 15; ++var21) {
                for(int var22 = 1; var22 < 15; ++var22) {
                    for(int var23 = 1; var23 < 7; ++var23) {
                        double var24 = ((double)var21 - var15) / (var9 / 2.0D);
                        double var26 = ((double)var23 - var17) / (var11 / 2.0D);
                        double var28 = ((double)var22 - var19) / (var13 / 2.0D);
                        double var30 = var24 * var24 + var26 * var26 + var28 * var28;
                        if (var30 < 1.0D) {
                            var6[(var21 * 16 + var22) * 8 + var23] = true;
                        }
                    }
                }
            }
        }

        for(int var32 = 0; var32 < 16; ++var32) {
            for(int var33 = 0; var33 < 16; ++var33) {
                for(int var34 = 0; var34 < 8; ++var34) {
                    int var35 = var6[(var32 * 16 + var33) * 8 + var34] || (var32 >= 15 || !var6[((var32 + 1) * 16 + var33) * 8 + var34]) && (var32 <= 0 || !var6[((var32 - 1) * 16 + var33) * 8 + var34]) && (var33 >= 15 || !var6[(var32 * 16 + var33 + 1) * 8 + var34]) && (var33 <= 0 || !var6[(var32 * 16 + (var33 - 1)) * 8 + var34]) && (var34 >= 7 || !var6[(var32 * 16 + var33) * 8 + var34 + 1]) && (var34 <= 0 || !var6[(var32 * 16 + var33) * 8 + (var34 - 1)]) ? 0 : 1;
                    if (var35 != 0) {
                        Material var36 = world.getMaterial(x + var32, y + var34, z + var33);
                        if (var34 >= 4 && var36.isFluid()) {
                            return false;
                        }

                        if (var34 < 4 && !var36.isSolid() && world.getBlockId(x + var32, y + var34, z + var33) != this.liquid) {
                            return false;
                        }
                    }
                }
            }
        }

        for(int var37 = 0; var37 < 16; ++var37) {
            for(int var38 = 0; var38 < 16; ++var38) {
                for(int var39 = 0; var39 < 8; ++var39) {
                    if (var6[(var37 * 16 + var38) * 8 + var39]) {
                        world.setBlockWithoutNotifyingNeighbors(x + var37, y + var39, z + var38, var39 >= 4 ? 0 : this.liquid);
                    }
                }
            }
        }

        for(int var40 = 0; var40 < 16; ++var40) {
            for(int var41 = 0; var41 < 16; ++var41) {
                for(int var42 = 4; var42 < 8; ++var42) {
                    if (var6[(var40 * 16 + var41) * 8 + var42] && world.getBlockId(x + var40, y + var42 - 1, z + var41) == Block.DIRT.id && world.getBrightness(LightType.SKY, x + var40, y + var42, z + var41) > 0) {
                        world.setBlockWithoutNotifyingNeighbors(x + var40, y + var42 - 1, z + var41, Block.GRASS_BLOCK.id);
                    }
                }
            }
        }

        if (Block.BLOCKS[this.liquid].material == Material.LAVA) {
            for(int var43 = 0; var43 < 16; ++var43) {
                for(int var44 = 0; var44 < 16; ++var44) {
                    for(int var45 = 0; var45 < 8; ++var45) {
                        int var46 = var6[(var43 * 16 + var44) * 8 + var45] || (var43 >= 15 || !var6[((var43 + 1) * 16 + var44) * 8 + var45]) && (var43 <= 0 || !var6[((var43 - 1) * 16 + var44) * 8 + var45]) && (var44 >= 15 || !var6[(var43 * 16 + var44 + 1) * 8 + var45]) && (var44 <= 0 || !var6[(var43 * 16 + (var44 - 1)) * 8 + var45]) && (var45 >= 7 || !var6[(var43 * 16 + var44) * 8 + var45 + 1]) && (var45 <= 0 || !var6[(var43 * 16 + var44) * 8 + (var45 - 1)]) ? 0 : 1;
                        if (var46 != 0 && (var45 < 4 || random.nextInt(2) != 0) && world.getMaterial(x + var43, y + var45, z + var44).isSolid()) {
                            world.setBlockWithoutNotifyingNeighbors(x + var43, y + var45, z + var44, Block.STONE.id);
                        }
                    }
                }
            }
        }

        return true;
    }
}