package com.itselix99.betterworldoptions.world.feature.notchHouse;

import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.mob.PigZombieEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class NotchHouseFeature extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        while(y > 0 && !world.getMaterial(x, y - 1, z).blocksMovement()) {
            --y;
        }

        int var1 = random.nextInt(7) + 7;
        int var2 = 4 + random.nextInt(3) / 2;
        int var3 = random.nextInt(7) + 7;
        int var4 = x - var1 / 2;
        int var5 = y;
        int var6 = z - var3 / 2;
        int var7 = random.nextInt(4);
        if (var7 < 2) {
            var3 += 2;
        } else {
            var1 += 2;
        }

        for(int var8 = var4; var8 < var4 + var1; ++var8) {
            for(int var9 = var6; var9 < var6 + var3; ++var9) {
                Material var10 = world.getMaterial(var8, y - 1, var9);
                if (!var10.blocksMovement() || var10 == Material.ICE) {
                    return false;
                }

                boolean var11 = false;
                if (var7 == 0 && var8 < var4 + 2) {
                    var11 = true;
                }

                if (var7 == 1 && var8 > var4 + var1 - 1 - 2) {
                    var11 = true;
                }

                if (var7 == 2 && var9 < var6 + 2) {
                    var11 = true;
                }

                if (var7 == 3 && var9 > var6 + var3 - 1 - 2) {
                    var11 = true;
                }

                int var12 = world.getBlockId(var8, y, var9);
                if (var11) {
                    if (var12 != 0) {
                        return false;
                    }
                } else if (var12 == Block.COBBLESTONE.id || var12 == Block.MOSSY_COBBLESTONE.id) {
                    return false;
                }
            }
        }

        if (var7 == 0) {
            ++var4;
            --var1;
        } else if (var7 == 1) {
            --var1;
        } else if (var7 == 2) {
            ++var6;
            --var3;
        } else if (var7 == 3) {
            --var3;
        }

        int var13 = var4;
        int var14 = var4 + var1 - 1;
        int var15 = var6;
        int var16 = var6 + var3 - 1;
        if (var7 >= 2) {
            var13 = var4 + 1;
            --var14;
        } else {
            var15 = var6 + 1;
            --var16;
        }

        for(int var17 = var4; var17 < var4 + var1; ++var17) {
            for(int var18 = var6; var18 < var6 + var3; ++var18) {
                int var19 = var2;
                int var20 = var18 - var6;
                int var21 = var6 + var3 - 1 - var18;
                if (var7 < 2) {
                    var20 = var17 - var4;
                    var21 = var4 + var1 - 1 - var17;
                }

                if (var21 < var20) {
                    var20 = var21;
                }

                var2 += var20;

                for(int var22 = var5 - 1; var22 < var5 + var2; ++var22) {
                    int var23 = -1;
                    if (var22 == var5 + var2 - 1) {
                        var23 = Block.PLANKS.id;
                    } else if (var17 >= var13 && var17 <= var14 && var18 >= var15 && var18 <= var16) {
                        var23 = 0;
                        if (var22 == var5 - 1 || var22 == var5 + var2 - 1 || var17 == var13 || var18 == var15 || var17 == var14 || var18 == var16) {
                            if (var22 <= var5 + random.nextInt(3)) {
                                var23 = Block.MOSSY_COBBLESTONE.id;
                            } else {
                                var23 = Block.COBBLESTONE.id;
                            }
                        }
                    }

                    if (var23 >= 0) {
                        world.setBlockWithoutNotifyingNeighbors(var17, var22, var18, var23);
                    }
                }

                var2 = var19;
            }
        }

        int var24 = var4 + random.nextInt(var1 - 4) + 2;
        int var25 = var6 + random.nextInt(var3 - 4) + 2;
        if (var7 == 0) {
            var24 = var4;
        }

        if (var7 == 1) {
            var24 = var4 + var1 - 1;
        }

        if (var7 == 2) {
            var25 = var6;
        }

        if (var7 == 3) {
            var25 = var6 + var3 - 1;
        }

        world.setBlockWithoutNotifyingNeighbors(var24, var5, var25, 0);
        world.setBlockWithoutNotifyingNeighbors(var24, var5 + 1, var25, 0);
        int var26 = 0;
        if (var7 == 0) {
            var26 = 0;
        }

        if (var7 == 2) {
            var26 = 1;
        }

        if (var7 == 1) {
            var26 = 2;
        }

        if (var7 == 3) {
            var26 = 3;
        }

        StructurePiece.place(world, var24, var5, var25, var26, Block.DOOR);

        for(int var27 = 0; var27 < (var1 * 2 + var3 * 2) * 3; ++var27) {
            var25 = var4 + random.nextInt(var1 - 4) + 2;
            var26 = var6 + random.nextInt(var3 - 4) + 2;
            int var28 = random.nextInt(4);
            if (var28 == 0) {
                var25 = var13;
            }

            if (var28 == 1) {
                var25 = var14;
            }

            if (var28 == 2) {
                var26 = var15;
            }

            if (var28 == 3) {
                var26 = var16;
            }

            if (world.shouldSuffocate(var25, var5 + 1, var26)) {
                int var29 = 0;
                if (world.shouldSuffocate(var25 - 1, var5 + 1, var26) && world.shouldSuffocate(var25 + 1, var5 + 1, var26)) {
                    ++var29;
                }

                if (world.shouldSuffocate(var25, var5 + 1, var26 - 1) && world.shouldSuffocate(var25, var5 + 1, var26 + 1)) {
                    ++var29;
                }

                if (var29 == 1) {
                    world.setBlockWithoutNotifyingNeighbors(var25, var5 + 1, var26, Block.GLASS.id);
                }
            }
        }

        var24 = var14 - var13;
        var25 = var16 - var15;

        for(int var30 = 0; var30 < var24 * 2 + var25 * 2; ++var30) {
            int var31 = var13 + random.nextInt(var24 - 1) + 1;
            int var32 = var15 + random.nextInt(var25 - 1) + 1;
            if (world.getBlockId(var31, var5 + 2, var32) == 0) {
                int var33 = 0;
                if (world.shouldSuffocate(var31 - 1, var5 + 2, var32)) {
                    ++var33;
                }

                if (world.shouldSuffocate(var31 + 1, var5 + 2, var32)) {
                    ++var33;
                }

                if (world.shouldSuffocate(var31, var5 + 2, var32 - 1)) {
                    ++var33;
                }

                if (world.shouldSuffocate(var31, var5 + 2, var32 + 1)) {
                    ++var33;
                }

                if (var33 == 1) {
                    world.setBlockWithoutNotifyingNeighbors(var31, var5 + 2, var32, Block.TORCH.id);
                }
            }
        }

        PigZombieEntity var34 = new PigZombieEntity(world);
        var34.setPositionAndAngles((double)var4 + (double)var1 / (double)2.0F + (double)0.5F, (double)var5 + (double)0.5F, (double)var6 + (double)var3 / (double)2.0F + (double)0.5F, 0.0F, 0.0F);
        world.spawnEntity(var34);
        return true;
    }
}