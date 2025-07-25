package com.itselix99.betterworldoptions.block;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.event.TextureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateLeavesBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class AlphaLeavesBlock extends TemplateLeavesBlock {
    int[] decayRegion;

    public AlphaLeavesBlock(Identifier identifier, int j) {
        super(identifier, j);
        this.setTickRandomly(true);
    }

    @Environment(EnvType.CLIENT)
    public int getColor(int meta) {
        return 16777215;
    }

    @Environment(EnvType.CLIENT)
    public int getColorMultiplier(BlockView blockView, int x, int y, int z) {
        return 16777215;
    }

    public void onBreak(World world, int x, int y, int z) {
        byte var5 = 1;
        int var6 = var5 + 1;
        if (world.isRegionLoaded(x - var6, y - var6, z - var6, x + var6, y + var6, z + var6)) {
            for(int var7 = -var5; var7 <= var5; ++var7) {
                for(int var8 = -var5; var8 <= var5; ++var8) {
                    for(int var9 = -var5; var9 <= var5; ++var9) {
                        int var10 = world.getBlockId(x + var7, y + var8, z + var9);
                        if (var10 == BetterWorldOptions.ALPHA_LEAVES.id) {
                            int var11 = world.getBlockMeta(x + var7, y + var8, z + var9);
                            world.setBlockMetaWithoutNotifyingNeighbors(x + var7, y + var8, z + var9, var11 | 8);
                        }
                    }
                }
            }
        }

    }

    public void onTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            int var6 = world.getBlockMeta(x, y, z);
            if ((var6 & 8) != 0) {
                byte var7 = 4;
                int var8 = var7 + 1;
                byte var9 = 32;
                int var10 = var9 * var9;
                int var11 = var9 / 2;
                if (this.decayRegion == null) {
                    this.decayRegion = new int[var9 * var9 * var9];
                }

                if (world.isRegionLoaded(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                    for(int var12 = -var7; var12 <= var7; ++var12) {
                        for(int var13 = -var7; var13 <= var7; ++var13) {
                            for(int var14 = -var7; var14 <= var7; ++var14) {
                                int var15 = world.getBlockId(x + var12, y + var13, z + var14);
                                if (var15 == Block.LOG.id) {
                                    this.decayRegion[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
                                } else if (var15 == BetterWorldOptions.ALPHA_LEAVES.id) {
                                    this.decayRegion[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
                                } else {
                                    this.decayRegion[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -1;
                                }
                            }
                        }
                    }

                    for(int var16 = 1; var16 <= 4; ++var16) {
                        for(int var18 = -var7; var18 <= var7; ++var18) {
                            for(int var19 = -var7; var19 <= var7; ++var19) {
                                for(int var20 = -var7; var20 <= var7; ++var20) {
                                    if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11] == var16 - 1) {
                                        if (this.decayRegion[(var18 + var11 - 1) * var10 + (var19 + var11) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11 - 1) * var10 + (var19 + var11) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11 + 1) * var10 + (var19 + var11) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11 + 1) * var10 + (var19 + var11) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11 - 1) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11 - 1) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11 + 1) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11 + 1) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + (var20 + var11 - 1)] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + (var20 + var11 - 1)] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11 + 1] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11 + 1] = var16;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int var17 = this.decayRegion[var11 * var10 + var11 * var9 + var11];
                if (var17 >= 0) {
                    world.setBlockMetaWithoutNotifyingNeighbors(x, y, z, var6 & -9);
                } else {
                    this.breakLeaves(world, x, y, z);
                }
            }

        }
    }

    private void breakLeaves(World world, int x, int y, int z) {
        this.dropStacks(world, x, y, z, world.getBlockMeta(x, y, z));
        world.setBlock(x, y, z, 0);
    }

    public int getDroppedItemCount(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public int getDroppedItemId(int blockMeta, Random random) {
        return Block.SAPLING.id;
    }

    public void afterBreak(World world, PlayerEntity playerEntity, int x, int y, int z, int meta) {
        if (!world.isRemote && playerEntity.getHand() != null && playerEntity.getHand().itemId == Item.SHEARS.id) {
            playerEntity.increaseStat(Stats.MINE_BLOCK[this.id], 1);
            this.dropStack(world, x, y, z, new ItemStack(BetterWorldOptions.ALPHA_LEAVES.id, 1, meta & 3));
        } else {
            super.afterBreak(world, playerEntity, x, y, z, meta);
        }

    }

    protected int getDroppedItemMeta(int blockMeta) {
        return blockMeta & 3;
    }

    public boolean isOpaque() {
        return false;
    }

    public int getTexture(int side, int meta) {
        return TextureListener.alphaLeaves;
    }

    public boolean isSideVisible(BlockView iblockaccess, int i, int j, int k, int l) {
        iblockaccess.getBlockId(i, j, k);
        return true;
    }

    public void onSteppedOn(World world, int x, int y, int z, Entity entity) {
        super.onSteppedOn(world, x, y, z, entity);
    }
}