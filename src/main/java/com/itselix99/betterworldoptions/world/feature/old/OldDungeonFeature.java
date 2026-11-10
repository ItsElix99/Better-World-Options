package com.itselix99.betterworldoptions.world.feature.old;

import com.itselix99.betterworldoptions.world.feature.AbstractFeature;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class OldDungeonFeature extends AbstractFeature {
    public OldDungeonFeature() {
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        byte var6 = 3;
        int var7 = random.nextInt(2) + 2;
        int var8 = random.nextInt(2) + 2;
        int var9 = 0;

        for(int var10 = x - var7 - 1; var10 <= x + var7 + 1; ++var10) {
            for(int var11 = y - 1; var11 <= y + var6 + 1; ++var11) {
                for(int var12 = z - var8 - 1; var12 <= z + var8 + 1; ++var12) {
                    Material var13 = world.getMaterial(var10, var11, var12);
                    if (var11 == y - 1 && !var13.isSolid()) {
                        return false;
                    }

                    if (var11 == y + var6 + 1 && !var13.isSolid()) {
                        return false;
                    }

                    if ((var10 == x - var7 - 1 || var10 == x + var7 + 1 || var12 == z - var8 - 1 || var12 == z + var8 + 1) && var11 == y && world.isAir(var10, var11, var12) && world.isAir(var10, var11 + 1, var12)) {
                        ++var9;
                    }
                }
            }
        }

        if (var9 >= 1 && var9 <= 5) {
            for(int var19 = x - var7 - 1; var19 <= x + var7 + 1; ++var19) {
                for(int var22 = y + var6; var22 >= y - 1; --var22) {
                    for(int var24 = z - var8 - 1; var24 <= z + var8 + 1; ++var24) {
                        if (var19 != x - var7 - 1 && var22 != y - 1 && var24 != z - var8 - 1 && var19 != x + var7 + 1 && var22 != y + var6 + 1 && var24 != z + var8 + 1) {
                            this.setBlock(var19, var22, var24, 0);
                        } else if (var22 >= 0 && !world.getMaterial(var19, var22 - 1, var24).isSolid()) {
                            this.setBlock(var19, var22, var24, 0);
                        } else if (world.getMaterial(var19, var22, var24).isSolid()) {
                            if (var22 == y - 1 && random.nextInt(4) != 0) {
//                                world.setBlock(var19, var22, var24, Block.MOSSY_COBBLESTONE.id);
                            } else {
//                                world.setBlock(var19, var22, var24, Block.COBBLESTONE.id);
                            }
                        }
                    }
                }
            }

            for(int var20 = 0; var20 < 2; ++var20) {
                for(int var23 = 0; var23 < 3; ++var23) {
                    int var25 = x + random.nextInt(var7 * 2 + 1) - var7;
                    int var14 = z + random.nextInt(var8 * 2 + 1) - var8;
                    if (this.getBlockId(var25, y, var14) == 0) {
                        int var15 = 0;
                        if (world.getMaterial(var25 - 1, y, var14).isSolid()) {
                            ++var15;
                        }

                        if (world.getMaterial(var25 + 1, y, var14).isSolid()) {
                            ++var15;
                        }

                        if (world.getMaterial(var25, y, var14 - 1).isSolid()) {
                            ++var15;
                        }

                        if (world.getMaterial(var25, y, var14 + 1).isSolid()) {
                            ++var15;
                        }

                        if (var15 == 1) {
//                            world.setBlock(var25, y, var14, Block.CHEST.id);
                            ChestBlockEntity var16 = new ChestBlockEntity();

                            for(int var17 = 0; var17 < 8; ++var17) {
                                ItemStack var18 = this.getRandomChestItem(random);
                                if (var18 != null) {
                                    var16.setStack(random.nextInt(var16.size()), var18);
                                }
                            }
                            break;
                        }
                    }
                }
            }

//            world.setBlock(x, y, z, Block.SPAWNER.id);
            MobSpawnerBlockEntity var21 = new MobSpawnerBlockEntity();
            var21.setSpawnedEntityId(this.getRandomEntity(random));
            return true;
        } else {
            return false;
        }
    }

    private ItemStack getRandomChestItem(Random random) {
        int var2 = random.nextInt(11);
        if (var2 == 0) {
            return new ItemStack(Item.SADDLE);
        } else if (var2 == 1) {
            return new ItemStack(Item.IRON_INGOT, random.nextInt(4) + 1);
        } else if (var2 == 2) {
            return new ItemStack(Item.BREAD);
        } else if (var2 == 3) {
            return new ItemStack(Item.WHEAT, random.nextInt(4) + 1);
        } else if (var2 == 4) {
            return new ItemStack(Item.GUNPOWDER, random.nextInt(4) + 1);
        } else if (var2 == 5) {
            return new ItemStack(Item.STRING, random.nextInt(4) + 1);
        } else if (var2 == 6) {
            return new ItemStack(Item.BUCKET);
        } else if (var2 == 7 && random.nextInt(100) == 0) {
            return new ItemStack(Item.GOLDEN_APPLE);
        } else if (var2 == 8 && random.nextInt(2) == 0) {
            return new ItemStack(Item.REDSTONE, random.nextInt(4) + 1);
        } else {
            return var2 == 9 && random.nextInt(10) == 0 ? new ItemStack(Item.ITEMS[Item.RECORD_THIRTEEN.id + random.nextInt(2)]) : null;
        }
    }

    private String getRandomEntity(Random random) {
        int var2 = random.nextInt(4);
        if (var2 == 0) {
            return "Skeleton";
        } else if (var2 == 1) {
            return "Zombie";
        } else if (var2 == 2) {
            return "Zombie";
        } else {
            return var2 == 3 ? "Spider" : "";
        }
    }
}