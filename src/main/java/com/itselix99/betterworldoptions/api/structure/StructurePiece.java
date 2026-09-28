package com.itselix99.betterworldoptions.api.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class StructurePiece {
    protected StructureBox bounds;
    protected int facing;
    protected int generationDepth;

    protected StructurePiece(int generationDepth) {
        this.generationDepth = generationDepth;
        this.facing = -1;
    }

    public void addChildren(StructurePiece start, List<StructurePiece> pieces, Random random) {
    }

    public abstract boolean postProcess(World world, Random random, StructureBox bounds);

    public StructureBox getBounds() {
        return this.bounds;
    }

    public int getGenerationDepth() {
        return this.generationDepth;
    }

    public static StructurePiece getIntersectingPiece(List<StructurePiece> pieces, StructureBox bounds) {
        for(StructurePiece structurePiece : pieces) {
            if (structurePiece.getBounds() != null && structurePiece.getBounds().intersects(bounds)) {
                return structurePiece;
            }
        }

        return null;
    }

    public BlockPos getCenterPos() {
        return new BlockPos(this.bounds.getCenterX(), this.bounds.getCenterY(), this.bounds.getCenterZ());
    }

    protected boolean bordersOnLiquids(World world, StructureBox bounds) {
        int var1 = Math.max(this.bounds.minX - 1, bounds.minX);
        int var2 = Math.max(this.bounds.minY - 1, bounds.minY);
        int var3 = Math.max(this.bounds.minZ - 1, bounds.minZ);
        int var4 = Math.min(this.bounds.maxX + 1, bounds.maxX);
        int var5 = Math.min(this.bounds.maxY + 1, bounds.maxY);
        int var6 = Math.min(this.bounds.maxZ + 1, bounds.maxZ);

        for(int var7 = var1; var7 <= var4; ++var7) {
            for(int var8 = var3; var8 <= var6; ++var8) {
                int var9 = world.getBlockId(var7, var2, var8);
                if (var9 > 0 && Block.BLOCKS[var9].material.isFluid()) {
                    return true;
                }

                var9 = world.getBlockId(var7, var5, var8);
                if (var9 > 0 && Block.BLOCKS[var9].material.isFluid()) {
                    return true;
                }
            }
        }

        for(int var10 = var1; var10 <= var4; ++var10) {
            for(int var11 = var2; var11 <= var5; ++var11) {
                int var12 = world.getBlockId(var10, var11, var3);
                if (var12 > 0 && Block.BLOCKS[var12].material.isFluid()) {
                    return true;
                }

                var12 = world.getBlockId(var10, var11, var6);
                if (var12 > 0 && Block.BLOCKS[var12].material.isFluid()) {
                    return true;
                }
            }
        }

        for(int var13 = var3; var13 <= var6; ++var13) {
            for(int var14 = var2; var14 <= var5; ++var14) {
                int var15 = world.getBlockId(var1, var14, var13);
                if (var15 > 0 && Block.BLOCKS[var15].material.isFluid()) {
                    return true;
                }

                var15 = world.getBlockId(var4, var14, var13);
                if (var15 > 0 && Block.BLOCKS[var15].material.isFluid()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected int transformX(int x, int z) {
        return switch (this.facing) {
            case 0, 2 -> this.bounds.minX + x;
            case 1 -> this.bounds.maxX - z;
            case 3 -> this.bounds.minX + z;
            default -> x;
        };
    }

    protected int transformY(int y) {
        return this.facing == -1 ? y : y + this.bounds.minY;
    }

    protected int transformZ(int x, int z) {
        return switch (this.facing) {
            case 0 -> this.bounds.minZ + z;
            case 1, 3 -> this.bounds.minZ + x;
            case 2 -> this.bounds.maxZ - z;
            default -> z;
        };
    }

    protected int postProcessBlockMetadata(int block, int metadata) {
        if (block == Block.RAIL.id) {
            if (this.facing == 1 || this.facing == 3) {
                if (metadata == 1) {
                    return 0;
                }

                return 1;
            }
        } else if (block != Block.DOOR.id && block != Block.IRON_DOOR.id) {
            if (block != Block.COBBLESTONE_STAIRS.id && block != Block.WOODEN_STAIRS.id) {
                if (block == Block.LADDER.id) {
                    if (this.facing == 0) {
                        if (metadata == 2) {
                            return 3;
                        }

                        if (metadata == 3) {
                            return 2;
                        }
                    } else if (this.facing == 1) {
                        if (metadata == 2) {
                            return 4;
                        }

                        if (metadata == 3) {
                            return 5;
                        }

                        if (metadata == 4) {
                            return 2;
                        }

                        if (metadata == 5) {
                            return 3;
                        }
                    } else if (this.facing == 3) {
                        if (metadata == 2) {
                            return 5;
                        }

                        if (metadata == 3) {
                            return 4;
                        }

                        if (metadata == 4) {
                            return 2;
                        }

                        if (metadata == 5) {
                            return 3;
                        }
                    }
                } else if (block == Block.BUTTON.id) {
                    if (this.facing == 0) {
                        if (metadata == 3) {
                            return 4;
                        }

                        if (metadata == 4) {
                            return 3;
                        }
                    } else if (this.facing == 1) {
                        if (metadata == 3) {
                            return 1;
                        }

                        if (metadata == 4) {
                            return 2;
                        }

                        if (metadata == 2) {
                            return 3;
                        }

                        if (metadata == 1) {
                            return 4;
                        }
                    } else if (this.facing == 3) {
                        if (metadata == 3) {
                            return 2;
                        }

                        if (metadata == 4) {
                            return 1;
                        }

                        if (metadata == 2) {
                            return 3;
                        }

                        if (metadata == 1) {
                            return 4;
                        }
                    }
                }
            } else if (this.facing == 0) {
                if (metadata == 2) {
                    return 3;
                }

                if (metadata == 3) {
                    return 2;
                }
            } else if (this.facing == 1) {
                if (metadata == 0) {
                    return 2;
                }

                if (metadata == 1) {
                    return 3;
                }

                if (metadata == 2) {
                    return 0;
                }

                if (metadata == 3) {
                    return 1;
                }
            } else if (this.facing == 3) {
                if (metadata == 0) {
                    return 2;
                }

                if (metadata == 1) {
                    return 3;
                }

                if (metadata == 2) {
                    return 1;
                }

                if (metadata == 3) {
                    return 0;
                }
            }
        } else if (this.facing == 0) {
            if (metadata == 0) {
                return 2;
            }

            if (metadata == 2) {
                return 0;
            }
        } else {
            if (this.facing == 1) {
                return metadata + 1 & 3;
            }

            if (this.facing == 3) {
                return metadata + 3 & 3;
            }
        }

        return metadata;
    }

    protected void setBlockWithMetadata(World world, int block, int metadata, int x, int y, int z, StructureBox bounds) {
        int var1 = this.transformX(x, z);
        int var2 = this.transformY(y);
        int var3 = this.transformZ(x, z);
        if (bounds.contains(var1, var2, var3)) {
            world.setBlockWithoutNotifyingNeighbors(var1, var2, var3, block, metadata);
        }
    }

    protected int getBlock(World world, int x, int y, int z, StructureBox bounds) {
        int var1 = this.transformX(x, z);
        int var2 = this.transformY(y);
        int var3 = this.transformZ(x, z);
        return !bounds.contains(var1, var2, var3) ? 0 : world.getBlockId(var1, var2, var3);
    }

    protected void fillWithOutline(World world, StructureBox bounds, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int edge, int filler, boolean avoidAir) {
        for(int var1 = minY; var1 <= maxY; ++var1) {
            for(int var2 = minX; var2 <= maxX; ++var2) {
                for(int var3 = minZ; var3 <= maxZ; ++var3) {
                    if (!avoidAir || this.getBlock(world, var2, var1, var3, bounds) != 0) {
                        if (var1 != minY && var1 != maxY && var2 != minX && var2 != maxX && var3 != minZ && var3 != maxZ) {
                            this.setBlockWithMetadata(world, filler, 0, var2, var1, var3, bounds);
                        } else {
                            this.setBlockWithMetadata(world, edge, 0, var2, var1, var3, bounds);
                        }
                    }
                }
            }
        }

    }

    protected void fillWithOutline(World world, StructureBox bounds, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int edgeId, int edgeMetadata, int fillerId, int fillerMetadata, boolean avoidAir) {
        for(int var1 = minY; var1 <= maxY; ++var1) {
            for(int var2 = minX; var2 <= maxX; ++var2) {
                for(int var3 = minZ; var3 <= maxZ; ++var3) {
                    if (!avoidAir || this.getBlock(world, var2, var1, var3, bounds) != 0) {
                        if (var1 != minY && var1 != maxY && var2 != minX && var2 != maxX && var3 != minZ && var3 != maxZ) {
                            this.setBlockWithMetadata(world, fillerId, fillerMetadata, var2, var1, var3, bounds);
                        } else {
                            this.setBlockWithMetadata(world, edgeId, edgeMetadata, var2, var1, var3, bounds);
                        }
                    }
                }
            }
        }

    }

    protected void fill(World world, StructureBox bounds, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean avoidAir, Random random, BlockPicker blocks) {
        for(int var1 = minY; var1 <= maxY; ++var1) {
            for(int var2 = minX; var2 <= maxX; ++var2) {
                for(int var3 = minZ; var3 <= maxZ; ++var3) {
                    if (!avoidAir || this.getBlock(world, var2, var1, var3, bounds) != 0) {
                        blocks.pick(random, var2, var1, var3, var1 == minY || var1 == maxY || var2 == minX || var2 == maxX || var3 == minZ || var3 == maxZ);
                        this.setBlockWithMetadata(world, blocks.getBlock(), blocks.getMetadata(), var2, var1, var3, bounds);
                    }
                }
            }
        }

    }

    protected void fillRandomlyWithOutline(World world, StructureBox bounds, Random random, float threshold, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int edgeId, int fillerId, boolean avoidAir) {
        for(int var1 = minY; var1 <= maxY; ++var1) {
            for(int var2 = minX; var2 <= maxX; ++var2) {
                for(int var3 = minZ; var3 <= maxZ; ++var3) {
                    if (!(random.nextFloat() > threshold) && (!avoidAir || this.getBlock(world, var2, var1, var3, bounds) != 0)) {
                        if (var1 != minY && var1 != maxY && var2 != minX && var2 != maxX && var3 != minZ && var3 != maxZ) {
                            this.setBlockWithMetadata(world, fillerId, 0, var2, var1, var3, bounds);
                        } else {
                            this.setBlockWithMetadata(world, edgeId, 0, var2, var1, var3, bounds);
                        }
                    }
                }
            }
        }

    }

    protected void setBlockWithThreshold(World world, StructureBox bounds, Random random, float threshold, int z, int block, int metadata, int x, int y) {
        if (random.nextFloat() < threshold) {
            this.setBlockWithMetadata(world, x, y, z, block, metadata, bounds);
        }

    }

    protected void placeUpperHemisphere(World world, StructureBox bounds, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int block, boolean avoidAir) {
        float var1 = (float)(maxX - minX + 1);
        float var2 = (float)(maxY - minY + 1);
        float var3 = (float)(maxZ - minZ + 1);
        float var4 = (float)minX + var1 / 2.0F;
        float var5 = (float)minZ + var3 / 2.0F;

        for(int var6 = minY; var6 <= maxY; ++var6) {
            float var7 = (float)(var6 - minY) / var2;

            for(int var8 = minX; var8 <= maxX; ++var8) {
                float var9 = ((float)var8 - var4) / (var1 * 0.5F);

                for(int var10 = minZ; var10 <= maxZ; ++var10) {
                    float var11 = ((float)var10 - var5) / (var3 * 0.5F);
                    if (!avoidAir || this.getBlock(world, var8, var6, var10, bounds) != 0) {
                        float var12 = var9 * var9 + var7 * var7 + var11 * var11;
                        if (var12 <= 1.05F) {
                            this.setBlockWithMetadata(world, block, 0, var8, var6, var10, bounds);
                        }
                    }
                }
            }
        }

    }

    protected void fillAirColumnUp(World world, int x, int y, int z, StructureBox bounds) {
        int var1 = this.transformX(x, z);
        int var2 = this.transformY(y);
        int var3 = this.transformZ(x, z);
        if (bounds.contains(var1, var2, var3)) {
            while(!world.isAir(var1, var2, var3) && var2 < world.getHeight()) {
                world.setBlockWithoutNotifyingNeighbors(var1, var2, var3, 0, 0);
                ++var2;
            }

        }
    }

    protected void fillColumnDown(World world, int block, int metadata, int x, int y, int z, StructureBox bounds) {
        int var1 = this.transformX(x, z);
        int var2 = this.transformY(y);
        int var3 = this.transformZ(x, z);
        if (bounds.contains(var1, var2, var3)) {
            while((world.isAir(var1, var2, var3) || world.getMaterial(var1, var2, var3).isFluid()) && var2 > 1) {
                world.setBlockWithoutNotifyingNeighbors(var1, var2, var3, block, metadata);
                --var2;
            }

        }
    }

    protected void placeChestWithLoot(World world, StructureBox bounds, Random random, int x, int y, int z, LootEntry[] loot, int attempts) {
        int var1 = this.transformX(x, z);
        int var2 = this.transformY(y);
        int var3 = this.transformZ(x, z);
        if (bounds.contains(var1, var2, var3) && world.getBlockId(var1, var2, var3) != Block.CHEST.id) {
            world.setBlock(var1, var2, var3, Block.CHEST.id);
            ChestBlockEntity chestBlockEntity = (ChestBlockEntity)world.getBlockEntity(var1, var2, var3);
            if (chestBlockEntity != null) {
                placeLootInChest(random, loot, chestBlockEntity, attempts);
            }
        }

    }

    private static void placeLootInChest(Random random, LootEntry[] loot, ChestBlockEntity chest, int attempts) {
        for(int var1 = 0; var1 < attempts; ++var1) {
            LootEntry lootEntry = WeightedPicker.getRandom(random, Arrays.stream(loot).toList());
            int var2 = lootEntry.minItemChance + random.nextInt(lootEntry.maxItemChance - lootEntry.minItemChance + 1);
            if (Item.ITEMS[lootEntry.item].getMaxCount() >= var2) {
                chest.setStack(random.nextInt(chest.size()), new ItemStack(lootEntry.item, var2, lootEntry.itemMetadata));
            } else {
                for(int var3 = 0; var3 < var2; ++var3) {
                    chest.setStack(random.nextInt(chest.size()), new ItemStack(lootEntry.item, 1, lootEntry.itemMetadata));
                }
            }
        }

    }

    protected void placeWoodenDoor(World world, StructureBox bounds, Random random, int x, int y, int z, int metadata) {
        int var1 = this.transformX(x, z);
        int var2 = this.transformY(y);
        int var3 = this.transformZ(x, z);
        if (bounds.contains(var1, var2, var3)) {
            place(world, var1, var2, var3, metadata, Block.DOOR);
        }

    }

    public static void place(World world, int x, int y, int z, int metadata, Block block) {
        int var1 = 0;
        int var2 = 0;
        if (metadata == 0) {
            var2 = 1;
        }

        if (metadata == 1) {
            var1 = -1;
        }

        if (metadata == 2) {
            var2 = -1;
        }

        if (metadata == 3) {
            var1 = 1;
        }

        int var3 = (world.shouldSuffocate(x - var1, y, z - var2) ? 1 : 0) + (world.shouldSuffocate(x - var1, y + 1, z - var2) ? 1 : 0);
        int var4 = (world.shouldSuffocate(x + var1, y, z + var2) ? 1 : 0) + (world.shouldSuffocate(x + var1, y + 1, z + var2) ? 1 : 0);
        int var5 = world.getBlockId(x - var1, y, z - var2) != block.id && world.getBlockId(x - var1, y + 1, z - var2) != block.id ? 0 : 1;
        int var6 = world.getBlockId(x + var1, y, z + var2) != block.id && world.getBlockId(x + var1, y + 1, z + var2) != block.id ? 0 : 1;
        int var7 = 0;
        if (var5 != 0 && var6 == 0) {
            var7 = 1;
        } else if (var4 > var3) {
            var7 = 1;
        }

        if (var7 != 0) {
            metadata = metadata - 1 & 3;
            metadata += 4;
        }

        world.pauseTicking = true;
        world.setBlock(x, y, z, block.id, metadata);
        world.setBlock(x, y + 1, z, block.id, metadata + 8);
        world.pauseTicking = false;
        world.notifyNeighbors(x, y, z, block.id);
        world.notifyNeighbors(x, y + 1, z, block.id);
    }

    public abstract static class BlockPicker {
        protected int block;
        protected int metadata;

        protected BlockPicker() {
        }

        public abstract void pick(Random randomm, int x, int y, int z, boolean isNonAir);

        public int getBlock() {
            return this.block;
        }

        public int getMetadata() {
            return this.metadata;
        }
    }
}