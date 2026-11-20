package com.itselix99.betterworldoptions.api.worldtype;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.structure.LootEntry;
import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.feature.BonusChestFeature;
import net.minecraft.block.Block;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;

import java.util.Random;

public class SpawnPoint {
    protected final BWOProperties bwoProperties;

    protected final boolean oldFeatures;
    protected final boolean finiteWorld;
    protected final String finiteWorldType;
    protected final int width;
    protected final int length;

    protected final boolean farlands;
    protected final String farlandsShape;
    protected final int farlandsDistance;

    protected final boolean starterHouse;
    protected final boolean bonusChest;

    protected LootEntry[] lootEntries;

    public SpawnPoint(BWOProperties bwoProperties) {
        this.bwoProperties = bwoProperties;
        this.oldFeatures = bwoProperties.bwo_isOldFeatures();
        this.finiteWorld = bwoProperties.bwo_getOptionValue("FiniteWorld", OptionType.GENERAL_OPTION, false);
        this.finiteWorldType = bwoProperties.bwo_getOptionValue("FiniteWorldType", OptionType.GENERAL_OPTION, "");
        this.width = bwoProperties.bwo_getOptionValue("Width", OptionType.GENERAL_OPTION, 0);
        this.length = bwoProperties.bwo_getOptionValue("Length", OptionType.GENERAL_OPTION, 0);
        this.farlands = bwoProperties.bwo_getOptionValue("Farlands", OptionType.GENERAL_OPTION, false);
        this.farlandsShape = bwoProperties.bwo_getOptionValue("FarlandsShape", OptionType.GENERAL_OPTION, "");
        this.farlandsDistance = bwoProperties.bwo_getOptionValue("FarlandsDistance", OptionType.GENERAL_OPTION, 0) / 2;
        this.starterHouse = bwoProperties.bwo_getOptionValue("StarterHouse", OptionType.GENERAL_OPTION, false);
        this.bonusChest = bwoProperties.bwo_getOptionValue("BonusChest", OptionType.GENERAL_OPTION, false);
        this.lootEntries = new LootEntry[]{new LootEntry(Item.STICK.id, 0, 1, 3, 10), new LootEntry(Block.PLANKS.id, 0, 1, 3, 10), new LootEntry(Block.LOG.id, 0, 1, 3, 10), new LootEntry(Item.STONE_AXE.id, 0, 1, 1, 3), new LootEntry(Item.WOODEN_AXE.id, 0, 1, 1, 5), new LootEntry(Item.STONE_PICKAXE.id, 0, 1, 1, 3), new LootEntry(Item.WOODEN_PICKAXE.id, 0, 1, 1, 5), new LootEntry(Item.APPLE.id, 0, 2, 3, 5), new LootEntry(Item.BREAD.id, 0, 2, 3, 3)};
    }

    public void init(World world, Dimension dimension, Random random, WorldProperties properties) {
        boolean finiteWorldMCPE = this.finiteWorld && this.finiteWorldType.equals("MCPE");
        int x = finiteWorldMCPE ? this.width / 2 : 0;
        int y = 64;
        int z;

        int min = -this.farlandsDistance * 16;
        int max = (this.farlandsDistance * 16) + 15;

        for(z = finiteWorldMCPE ? this.length / 2 : 0; !dimension.isValidSpawnPoint(x, z); z += random.nextInt(64) - random.nextInt(64)) {
            x += random.nextInt(64) - random.nextInt(64);

            if (this.farlands) {
                if (this.farlandsShape.equals("Linear")) {
                    x = Math.max(min, Math.min(max, x));
                } else if (this.farlandsShape.equals("Square")) {
                    x = Math.max(min, Math.min(max, x));
                    z = Math.max(min, Math.min(max, z));
                }
            }
        }

        properties.setSpawn(x, y, z);
    }

    public void generateStarterHouseAndBonusChest(World world, WorldProperties properties, Random random, int x, int y, int z) {
        if (this.starterHouse) {
            this.placeStarterHouse(world, random, x, y, z);
        } else if (this.bonusChest) {
            this.placeBonusChest(world, properties, random);
        }
    }

    protected void placeStarterHouse(World world, Random random, int x, int y, int z) {
        for(int var6 = x - 3; var6 <= x + 3; ++var6) {
            for(int var7 = y - 2; var7 <= y + 2; ++var7) {
                for(int var8 = z - 3; var8 <= z + 3; ++var8) {
                    int var9 = var7 < y - 1 ? Block.OBSIDIAN.id : 0;

                    if (var6 == x - 3 || var8 == z - 3 || var6 == x + 3 || var8 == z + 3 || var7 == y - 2 || var7 == y + 2) {
                        var9 = Block.COBBLESTONE.id;

                        if (var7 >= y - 1) {
                            var9 = Block.PLANKS.id;
                        }
                    }

                    if ((var6 == x - 3 || var6 == x + 3) && (var8 == z - 3 || var8 == z + 3) && var7 >= y - 1) {
                        var9 = Block.LOG.id;
                    }

                    if (var7 == y + 2 && (var6 == x - 3 || var6 == x + 3 || var8 == z - 3 || var8 == z + 3)) {
                        var9 = Block.LOG.id;
                    }

                    if (var8 == z - 3 && var6 == x && var7 >= y - 1 && var7 <= y) {
                        var9 = 0;
                    }

                    world.setBlock(var6, var7, var8, var9);
                }
            }
        }

        StructurePiece.place(world, x, y - 1, z - 3, 1, Block.DOOR);

        if (world.isAir(x, y - 2, z - 4)) {
            int var10 = world.getTopSolidBlockY(x, z - 4);

            int var11 = y - 2;
            int var12 = z - 4;

            while (var11 >= var10) {
                world.setBlock(x, var11, var12, Block.COBBLESTONE_STAIRS.id, 2);

                for (int var13 = var11 - 1; var13 >= var10 - 1; --var13) {
                    if (world.isAir(x, var13, var12)) {
                        world.setBlock(x, var13, var12, Block.COBBLESTONE.id);
                    }
                }

                --var11;
                --var12;
                var10 = world.getTopSolidBlockY(x, var12);
            }
        }

        for (int var14 = 0; var14 < 3; var14++) {
            int var15 = 2 - var14;

            for (int var16 = x - var15; var16 <= x + var15; var16++) {
                world.setBlock(var16, y + 3 + var14, z - 3, Block.PLANKS.id);
                world.setBlock(var16, y + 3 + var14, z + 3, Block.PLANKS.id);
            }
        }

        for (int var17 = x - 4; var17 <= x - 1; ++var17) {
            int var18 = y + (var17 - (x - 4)) + 2;

            for (int var19 = z - 4; var19 <= z + 4; ++var19) {
                world.setBlock(var17, var18, var19, Block.WOODEN_STAIRS.id, 0);

                if (var18 > y + 2 && (var19 == z - 4 || var19 == z + 4)) {
                    world.setBlock(var17, var18 - 1, var19, Block.PLANKS.id);
                }
            }
        }

        for (int var20 = x + 4; var20 >= x + 1; --var20) {
            int var21 = y + ((x + 4) - var20) + 2;

            for (int var22 = z - 4; var22 <= z + 4; ++var22) {
                world.setBlock(var20, var21, var22, Block.WOODEN_STAIRS.id, 1);

                if (var21 > y + 2 && (var22 == z - 4 || var22 == z + 4)) {
                    world.setBlock(var20, var21 - 1, var22, Block.PLANKS.id);
                }
            }
        }

        for (int var23 = z - 4; var23 <= z + 4; ++var23) {
            world.setBlock(x, y + 6, var23, Block.SLAB.id, 2);

            if ((var23 == z - 4 || var23 == z + 4)) {
                world.setBlock(x, y + 5, var23, Block.PLANKS.id);
            }
        }

        for (int var24 = z - 1; var24 <= z + 1; ++var24) {
            world.setBlock(x - 3, y, var24, Block.GLASS.id);
            world.setBlock(x + 3, y, var24, Block.GLASS.id);
        }

        world.setBlock(x, y + 3, z - 4, Block.TORCH.id);
        world.setBlock(x, y + 3, z + 4, Block.TORCH.id);

        for (int var25 = y - 1; var25 <= y + 1; ++var25) {
            if (var25 == y - 1) {
                world.setBlock(x + 2, var25, z - 2, Block.LOG.id);
                world.setBlock(x - 2, var25, z - 2, Block.LOG.id);
            } else {
                world.setBlock(x + 2, var25, z - 2, Block.LEAVES.id);
                world.setBlock(x - 2, var25, z - 2, Block.LEAVES.id);
                world.setBlock(x + 2, var25, z - 2, Block.LEAVES.id);
                world.setBlock(x - 2, var25, z - 2, Block.LEAVES.id);
            }
        }

        world.setBlock(x + 3 - 1, y + 1, z, Block.TORCH.id);
        world.setBlock(x - 3 + 1, y + 1, z, Block.TORCH.id);
        world.setBlock(x, y + 1, z + 2, Block.TORCH.id);
        world.setBlock(x, y + 1, z - 2, Block.TORCH.id);

        world.setBlock(x + 2, y - 1, z + 2, Block.FURNACE.id);
        world.setBlockMeta(x + 2, y - 1, z + 2, 2);

        world.setBlock(x + 1, y - 1, z + 2, Block.CHEST.id);
        world.setBlock(x, y - 1, z + 2, Block.CHEST.id);

        world.setBlock(x - 2, y - 1, z + 1, Block.BED.id, 0);
        world.setBlock(x - 2, y - 1, z + 2, Block.BED.id, 8);

        if (this.bonusChest) {
            world.setBlock(x + 2, y - 1, z, Block.CHEST.id);
            world.setBlock(x + 2, y, z, Block.TORCH.id);
            ChestBlockEntity var26 = (ChestBlockEntity) world.getBlockEntity(x + 2, y - 1, z);

            if (var26 != null) {
                LootEntry.addLoot(random, this.lootEntries, var26, 10);
            }

        }

        for (int var27 = x - 3; var27 <= x + 3; ++var27) {
            for (int var28 = z - 3; var28 <= z + 3; ++var28) {
                int var29 = y - 3;

                while ((world.isAir(var27, var29, var28) || world.getMaterial(var27, var29, var28).isFluid()) && var29 > 1) {
                    world.setBlock(var27, var29, var28, Block.COBBLESTONE.id);
                    --var29;
                }
            }
        }
    }

    protected void placeBonusChest(World world, WorldProperties properties, Random random) {
        BonusChestFeature var4 = new BonusChestFeature(this.lootEntries, 10);

        for(int var5 = 0; var5 < 10; ++var5) {
            int var6 = properties.getSpawnX() + random.nextInt(6) - random.nextInt(6);
            int var7 = properties.getSpawnZ() + random.nextInt(6) - random.nextInt(6);
            int var8 = ((BWOWorld) world).bwo_getSurfaceHeight(var6, var7) + 1;
            if(var4.generate(world, random, var6, var8, var7)) {
                break;
            }
        }
    }
}