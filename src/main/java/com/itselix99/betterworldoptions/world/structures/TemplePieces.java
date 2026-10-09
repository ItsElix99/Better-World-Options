package com.itselix99.betterworldoptions.world.structures;

import com.itselix99.betterworldoptions.api.structure.LootEntry;
import com.itselix99.betterworldoptions.api.structure.StructureBox;
import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TemplePieces {
    public static Map<Integer, Integer> blocks = new HashMap<>();

    static {
        blocks.put(Block.SANDSTONE.id, Block.SANDSTONE.id);
    }

    abstract static class ScatteredStructurePiece extends StructurePiece {
        protected final int width;
        protected final int height;
        protected final int depth;
        protected int hPos = -1;

        protected ScatteredStructurePiece(Random random, int x, int y, int z, int width, int height, int depth) {
            super(0);
            this.width = width;
            this.height = height;
            this.depth = depth;
            this.facing = random.nextInt(4);
            switch (this.facing) {
                case 0:
                case 2:
                    this.bounds = new StructureBox(x, y, z, x + width - 1, y + height - 1, z + depth - 1);
                    break;
                default:
                    this.bounds = new StructureBox(x, y, z, x + depth - 1, y + height - 1, z + width - 1);
            }

        }

        protected boolean updateHPos(World world, StructureBox bounds, int offset) {
            if (this.hPos >= 0) {
                return true;
            } else {
                int var1 = 0;
                int var2 = 0;

                for (int var3 = this.bounds.minZ; var3 <= this.bounds.maxZ; ++var3) {
                    for (int var4 = this.bounds.minX; var4 <= this.bounds.maxX; ++var4) {
                        if (bounds.contains(var4, 64, var3)) {
                            var1 += Math.max(((BWOWorld) world).bwo_getSurfaceHeight(var4, var3), ((BWOWorld) world).bwo_getSeaLevel());
                            ++var2;
                        }
                    }
                }

                if (var2 == 0) {
                    return false;
                } else {
                    this.hPos = var1 / var2;
                    this.bounds.move(0, this.hPos - this.bounds.minY + offset, 0);
                    return true;
                }
            }
        }

        protected boolean updateHPosToLowestGroundHeight(World world, int offset) {
            if (this.hPos >= 0) {
                return true;
            } else {
                int var1 = world.getTopY();
                boolean var2 = false;

                for(int var3 = this.bounds.minZ; var3 <= this.bounds.maxZ; ++var3) {
                    for(int var4 = this.bounds.minX; var4 <= this.bounds.maxX; ++var4) {
                        var1 = Math.min(var1, ((BWOWorld) world).bwo_getSurfaceHeight(var4, var3));
                        var2 = true;
                    }
                }

                if (!var2) {
                    return false;
                } else {
                    this.hPos = var1;
                    this.bounds.move(0, this.hPos - this.bounds.minY + offset, 0);
                    return true;
                }
            }
        }
    }

    public static class DesertPyramid extends ScatteredStructurePiece {
        private static final LootEntry[] LOOT_ENTRIES;
        private static final int[] X_OFFSET = new int[]{0, -1, 0, 1};
        private static final int[] Z_OFFSET = new int[]{1, 0, -1, 0};

        public DesertPyramid(Random random, int x, int z) {
            super(random, x, 64, z, 21, 15, 21);
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            this.fillWithOutline(world, bounds, 0, -4, 0, this.width - 1, 0, this.depth - 1, Block.SANDSTONE.id, Block.SANDSTONE.id, false);

            for (int var1 = 1; var1 <= 9; ++var1) {
                this.fillWithOutline(world, bounds, var1, var1, var1, this.width - 1 - var1, var1, this.depth - 1 - var1, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
                this.fillWithOutline(world, bounds, var1 + 1, var1, var1 + 1, this.width - 2 - var1, var1, this.depth - 2 - var1, 0, 0, false);
            }

            for (int var2 = 0; var2 < this.width; ++var2) {
                for (int var3 = 0; var3 < this.depth; ++var3) {
                    this.fillColumnDown(world, Block.SANDSTONE.id, 0, var2, -5, var3, bounds);
                }
            }

            int var4 = this.postProcessBlockMetadata(blocks.get(Block.SANDSTONE.id), 3);
            int var5 = this.postProcessBlockMetadata(blocks.get(Block.SANDSTONE.id), 2);
            int var6 = this.postProcessBlockMetadata(blocks.get(Block.SANDSTONE.id), 0);
            int var7 = this.postProcessBlockMetadata(blocks.get(Block.SANDSTONE.id), 1);
            int var8 = 1;
            int var9 = 11;
            this.fillWithOutline(world, bounds, 0, 0, 0, 4, 9, 4, Block.SANDSTONE.id, 0, false);
            this.fillWithOutline(world, bounds, 1, 10, 1, 3, 10, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var4, 2, 10, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var5, 2, 10, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var6, 0, 10, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var7, 4, 10, 2, bounds);
            this.fillWithOutline(world, bounds, this.width - 5, 0, 0, this.width - 1, 9, 4, Block.SANDSTONE.id, 0, false);
            this.fillWithOutline(world, bounds, this.width - 4, 10, 1, this.width - 2, 10, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var4, this.width - 3, 10, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var5, this.width - 3, 10, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var6, this.width - 5, 10, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var7, this.width - 1, 10, 2, bounds);
            this.fillWithOutline(world, bounds, 8, 0, 0, 12, 4, 4, Block.SANDSTONE.id, 0, false);
            this.fillWithOutline(world, bounds, 9, 1, 0, 11, 3, 4, 0, 0, false);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 9, 1, 1, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 9, 2, 1, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 9, 3, 1, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 10, 3, 1, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 11, 3, 1, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 11, 2, 1, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 11, 1, 1, bounds);
            this.fillWithOutline(world, bounds, 4, 1, 1, 8, 3, 3, Block.SANDSTONE.id, 0, false);
            this.fillWithOutline(world, bounds, 4, 1, 2, 8, 2, 2, 0, 0, false);
            this.fillWithOutline(world, bounds, 12, 1, 1, 16, 3, 3, Block.SANDSTONE.id, 0, false);
            this.fillWithOutline(world, bounds, 12, 1, 2, 16, 2, 2, 0, 0, false);
            this.fillWithOutline(world, bounds, 5, 4, 5, this.width - 6, 4, this.depth - 6, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, 9, 4, 9, 11, 4, 11, 0, 0, false);
            this.fillWithOutline(world, bounds, 8, 1, 8, 8, 3, 8, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.fillWithOutline(world, bounds, 12, 1, 8, 12, 3, 8, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.fillWithOutline(world, bounds, 8, 1, 12, 8, 3, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.fillWithOutline(world, bounds, 12, 1, 12, 12, 3, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.fillWithOutline(world, bounds, 1, 1, 5, 4, 4, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, this.width - 5, 1, 5, this.width - 2, 4, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, 6, 7, 9, 6, 7, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, this.width - 7, 7, 9, this.width - 7, 7, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, 5, 5, 9, 5, 7, 11, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.fillWithOutline(world, bounds, this.width - 6, 5, 9, this.width - 6, 7, 11, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.setBlockWithMetadata(world, 0, 0, 5, 5, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, 5, 6, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, 6, 6, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, this.width - 6, 5, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, this.width - 6, 6, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, this.width - 7, 6, 10, bounds);
            this.fillWithOutline(world, bounds, 2, 4, 4, 2, 6, 4, 0, 0, false);
            this.fillWithOutline(world, bounds, this.width - 3, 4, 4, this.width - 3, 6, 4, 0, 0, false);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var4, 2, 4, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var4, 2, 3, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var4, this.width - 3, 4, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var4, this.width - 3, 3, 4, bounds);
            this.fillWithOutline(world, bounds, 1, 1, 3, 2, 2, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, this.width - 3, 1, 3, this.width - 2, 2, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), 0, 1, 1, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), 0, this.width - 2, 1, 2, bounds);
            this.setBlockWithMetadata(world, Block.SLAB.id, 1, 1, 2, 2, bounds);
            this.setBlockWithMetadata(world, Block.SLAB.id, 1, this.width - 2, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var7, 2, 1, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.SANDSTONE.id), var6, this.width - 3, 1, 2, bounds);
            this.fillWithOutline(world, bounds, 4, 3, 5, 4, 3, 18, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, this.width - 5, 3, 5, this.width - 5, 3, 17, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, 3, 1, 5, 4, 2, 16, 0, 0, false);
            this.fillWithOutline(world, bounds, this.width - 6, 1, 5, this.width - 5, 2, 16, 0, 0, false);

            for (int var10 = 5; var10 <= 17; var10 += 2) {
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 4, 1, var10, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, 4, 2, var10, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, this.width - 5, 1, var10, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, this.width - 5, 2, var10, bounds);
            }

            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 10, 0, 7, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 10, 0, 8, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 9, 0, 9, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 11, 0, 9, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 8, 0, 10, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 12, 0, 10, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 7, 0, 10, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 13, 0, 10, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 9, 0, 11, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 11, 0, 11, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 10, 0, 12, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 10, 0, 13, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var9, 10, 0, 10, bounds);

            for (int var11 = 0; var11 <= this.width - 1; var11 += this.width - 1) {
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 2, 1, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 2, 2, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 2, 3, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 3, 1, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 3, 2, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 3, 3, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 4, 1, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, var11, 4, 2, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 4, 3, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 5, 1, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 5, 2, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 5, 3, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 6, 1, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, var11, 6, 2, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 6, 3, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 7, 1, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 7, 2, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var11, 7, 3, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 8, 1, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 8, 2, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var11, 8, 3, bounds);
            }

            for (int var12 = 2; var12 <= this.width - 3; var12 += this.width - 3 - 2) {
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 - 1, 2, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12, 2, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 + 1, 2, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 - 1, 3, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12, 3, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 + 1, 3, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12 - 1, 4, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, var12, 4, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12 + 1, 4, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 - 1, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 + 1, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12 - 1, 6, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, var12, 6, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12 + 1, 6, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12 - 1, 7, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12, 7, 0, bounds);
                this.setBlockWithMetadata(world, Block.WOOL.id, var8, var12 + 1, 7, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 - 1, 8, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12, 8, 0, bounds);
                this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, var12 + 1, 8, 0, bounds);
            }

            this.fillWithOutline(world, bounds, 8, 4, 0, 12, 6, 0, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.setBlockWithMetadata(world, 0, 0, 8, 6, 0, bounds);
            this.setBlockWithMetadata(world, 0, 0, 12, 6, 0, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 9, 5, 0, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, 10, 5, 0, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, var8, 11, 5, 0, bounds);
            this.fillWithOutline(world, bounds, 8, -14, 8, 12, -11, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.fillWithOutline(world, bounds, 8, -10, 8, 12, -10, 12, Block.SANDSTONE.id, 1, Block.SANDSTONE.id, 1, false);
            this.fillWithOutline(world, bounds, 8, -9, 8, 12, -9, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
            this.fillWithOutline(world, bounds, 8, -8, 8, 12, -1, 12, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.fillWithOutline(world, bounds, 9, -11, 9, 11, -1, 11, 0, 0, false);
            this.setBlockWithMetadata(world, Block.STONE_PRESSURE_PLATE.id, 0, 10, -11, 10, bounds);
            this.fillWithOutline(world, bounds, 9, -13, 9, 11, -13, 11, Block.TNT.id, 0, false);
            this.setBlockWithMetadata(world, 0, 0, 8, -11, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, 8, -10, 10, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, 7, -10, 10, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 7, -11, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, 12, -11, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, 12, -10, 10, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, 13, -10, 10, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 13, -11, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, 10, -11, 8, bounds);
            this.setBlockWithMetadata(world, 0, 0, 10, -10, 8, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, 10, -10, 7, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 10, -11, 7, bounds);
            this.setBlockWithMetadata(world, 0, 0, 10, -11, 12, bounds);
            this.setBlockWithMetadata(world, 0, 0, 10, -10, 12, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 1, 10, -10, 13, bounds);
            this.setBlockWithMetadata(world, Block.SANDSTONE.id, 2, 10, -11, 13, bounds);

            for (int var13 = 0; var13 < 4; ++var13) {
                int var14 = X_OFFSET[var13] * 2;
                int var15 = Z_OFFSET[var13] * 2;
                this.placeChestWithLoot(world, bounds, random, 10 + var14, -11, 10 + var15, LOOT_ENTRIES, 2 + random.nextInt(5));
            }

            return true;
        }

        static {
            LOOT_ENTRIES = new LootEntry[]{new LootEntry(Item.DIAMOND.id, 0, 1, 3, 3), new LootEntry(Item.IRON_INGOT.id, 0, 1, 5, 10), new LootEntry(Item.GOLD_INGOT.id, 0, 2, 7, 15), new LootEntry(Item.BONE.id, 0, 4, 6, 20)};
        }
    }
}