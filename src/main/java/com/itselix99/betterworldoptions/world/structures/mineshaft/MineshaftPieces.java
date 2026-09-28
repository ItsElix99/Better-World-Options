package com.itselix99.betterworldoptions.world.structures.mineshaft;

import com.itselix99.betterworldoptions.api.structure.LootEntry;
import com.itselix99.betterworldoptions.api.structure.StructureBox;
import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import net.minecraft.block.Block;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MineshaftPieces {
    private static final LootEntry[] LOOT_ENTRIES;

    private static StructurePiece createPiece(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
        int var1 = random.nextInt(100);
        if (var1 >= 80) {
            StructureBox var2 = MineshaftPieces.Crossing.findSize(pieces, random, x, y, z, facing);
            if (var2 != null) {
                return new Crossing(generationDepth, random, var2, facing);
            }
        } else if (var1 >= 70) {
            StructureBox var3 = MineshaftPieces.Stairs.findSize(pieces, random, x, y, z, facing);
            if (var3 != null) {
                return new Stairs(generationDepth, random, var3, facing);
            }
        } else {
            StructureBox var4 = MineshaftPieces.Corridor.findSize(pieces, random, x, y, z, facing);
            if (var4 != null) {
                return new Corridor(generationDepth, random, var4, facing);
            }
        }

        return null;
    }

    private static StructurePiece generateNextPiece(StructurePiece startPiece, List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
        if (generationDepth > 8) {
            return null;
        } else if (Math.abs(x - startPiece.getBounds().minX) <= 80 && Math.abs(z - startPiece.getBounds().minZ) <= 80) {
            StructurePiece var1 = createPiece(pieces, random, x, y, z, facing, generationDepth + 1);
            if (var1 != null) {
                pieces.add(var1);
                var1.addChildren(startPiece, pieces, random);
            }

            return var1;
        } else {
            return null;
        }
    }

    static {
        LOOT_ENTRIES = new LootEntry[]{new LootEntry(Item.IRON_INGOT.id, 0, 1, 5, 10), new LootEntry(Item.GOLD_INGOT.id, 0, 1, 3, 5), new LootEntry(Item.REDSTONE.id, 0, 4, 9, 5), new LootEntry(Item.DYE.id, 4, 4, 9, 5), new LootEntry(Item.DIAMOND.id, 0, 1, 2, 3), new LootEntry(Item.COAL.id, 0, 3, 8, 10), new LootEntry(Item.BREAD.id, 0, 1, 3, 15), new LootEntry(Item.IRON_PICKAXE.id, 0, 1, 1, 1), new LootEntry(Block.RAIL.id, 0, 4, 8, 1), new LootEntry(Item.SEEDS.id, 0, 2, 4, 10)};
    }

    public static class Room extends StructurePiece {
        private final LinkedList<StructureBox> entrances = new LinkedList<>();

        public Room(int generationDepth, Random random, int x, int z) {
            super(generationDepth);
            this.bounds = new StructureBox(x, 50, z, x + 7 + random.nextInt(6), 54 + random.nextInt(6), z + 7 + random.nextInt(6));
        }

        public void addChildren(StructurePiece start, List<StructurePiece> pieces, Random random) {
            int var1 = this.getGenerationDepth();
            int var2 = this.bounds.getSizeY() - 3 - 1;
            if (var2 <= 0) {
                var2 = 1;
            }

            int var3;
            for(var3 = 0; var3 < this.bounds.getSizeX(); var3 += 4) {
                var3 += random.nextInt(this.bounds.getSizeX());
                if (var3 + 3 > this.bounds.getSizeX()) {
                    break;
                }

                StructurePiece var4 = MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + var3, this.bounds.minY + random.nextInt(var2) + 1, this.bounds.minZ - 1, 2, var1);
                if (var4 != null) {
                    StructureBox var5 = var4.getBounds();
                    this.entrances.add(new StructureBox(var5.minX, var5.minY, this.bounds.minZ, var5.maxX, var5.maxY, this.bounds.minZ + 1));
                }
            }

            for(var3 = 0; var3 < this.bounds.getSizeX(); var3 += 4) {
                var3 += random.nextInt(this.bounds.getSizeX());
                if (var3 + 3 > this.bounds.getSizeX()) {
                    break;
                }

                StructurePiece var6 = MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + var3, this.bounds.minY + random.nextInt(var2) + 1, this.bounds.maxZ + 1, 0, var1);
                if (var6 != null) {
                    StructureBox var7 = var6.getBounds();
                    this.entrances.add(new StructureBox(var7.minX, var7.minY, this.bounds.maxZ - 1, var7.maxX, var7.maxY, this.bounds.maxZ));
                }
            }

            for(var3 = 0; var3 < this.bounds.getSizeZ(); var3 += 4) {
                var3 += random.nextInt(this.bounds.getSizeZ());
                if (var3 + 3 > this.bounds.getSizeZ()) {
                    break;
                }

                StructurePiece var8 = MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY + random.nextInt(var2) + 1, this.bounds.minZ + var3, 1, var1);
                if (var8 != null) {
                    StructureBox var9 = var8.getBounds();
                    this.entrances.add(new StructureBox(this.bounds.minX, var9.minY, var9.minZ, this.bounds.minX + 1, var9.maxY, var9.maxZ));
                }
            }

            for(var3 = 0; var3 < this.bounds.getSizeZ(); var3 += 4) {
                var3 += random.nextInt(this.bounds.getSizeZ());
                if (var3 + 3 > this.bounds.getSizeZ()) {
                    break;
                }

                StructurePiece var10 = MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY + random.nextInt(var2) + 1, this.bounds.minZ + var3, 3, var1);
                if (var10 != null) {
                    StructureBox var11 = var10.getBounds();
                    this.entrances.add(new StructureBox(this.bounds.maxX - 1, var11.minY, var11.minZ, this.bounds.maxX, var11.maxY, var11.maxZ));
                }
            }

        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.bordersOnLiquids(world, bounds)) {
                return false;
            } else {
                this.fillWithOutline(world, bounds, this.bounds.minX, this.bounds.minY, this.bounds.minZ, this.bounds.maxX, this.bounds.minY, this.bounds.maxZ, Block.DIRT.id, 0, true);
                this.fillWithOutline(world, bounds, this.bounds.minX, this.bounds.minY + 1, this.bounds.minZ, this.bounds.maxX, Math.min(this.bounds.minY + 3, this.bounds.maxY), this.bounds.maxZ, 0, 0, false);

                for(StructureBox structureBox : this.entrances) {
                    this.fillWithOutline(world, bounds, structureBox.minX, structureBox.maxY - 2, structureBox.minZ, structureBox.maxX, structureBox.maxY, structureBox.maxZ, 0, 0, false);
                }

                this.placeUpperHemisphere(world, bounds, this.bounds.minX, this.bounds.minY + 4, this.bounds.minZ, this.bounds.maxX, this.bounds.maxY, this.bounds.maxZ, 0, false);
                return true;
            }
        }
    }

    public static class Corridor extends StructurePiece {
        private final boolean hasRails;
        private final boolean hasCobwebs;
        private boolean hasSpiderSpawner;
        private final int sections;

        public Corridor(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
            this.hasRails = random.nextInt(3) == 0;
            this.hasCobwebs = !this.hasRails && random.nextInt(23) == 0;
            if (this.facing != 2 && this.facing != 0) {
                this.sections = bounds.getSizeX() / 5;
            } else {
                this.sections = bounds.getSizeZ() / 5;
            }

        }

        public static StructureBox findSize(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing) {
            StructureBox var1 = new StructureBox(x, y, z, x, y + 2, z);

            int var2;
            for(var2 = random.nextInt(3) + 2; var2 > 0; --var2) {
                int var3 = var2 * 5;
                switch (facing) {
                    case 0:
                        var1.maxX = x + 2;
                        var1.maxZ = z + (var3 - 1);
                        break;
                    case 1:
                        var1.minX = x - (var3 - 1);
                        var1.maxZ = z + 2;
                        break;
                    case 2:
                        var1.maxX = x + 2;
                        var1.minZ = z - (var3 - 1);
                        break;
                    case 3:
                        var1.maxX = x + (var3 - 1);
                        var1.maxZ = z + 2;
                }

                if (StructurePiece.getIntersectingPiece(pieces, var1) == null) {
                    break;
                }
            }

            return var2 > 0 ? var1 : null;
        }

        public void addChildren(StructurePiece start, List<StructurePiece> pieces, Random random) {
            int var1 = this.getGenerationDepth();
            int var2 = random.nextInt(4);
            switch (this.facing) {
                case 0:
                    if (var2 <= 1) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX, this.bounds.minY - 1 + random.nextInt(3), this.bounds.maxZ + 1, this.facing, var1);
                    } else if (var2 == 2) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY - 1 + random.nextInt(3), this.bounds.maxZ - 3, 1, var1);
                    } else {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY - 1 + random.nextInt(3), this.bounds.maxZ - 3, 3, var1);
                    }
                    break;
                case 1:
                    if (var2 <= 1) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY - 1 + random.nextInt(3), this.bounds.minZ, this.facing, var1);
                    } else if (var2 == 2) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX, this.bounds.minY - 1 + random.nextInt(3), this.bounds.minZ - 1, 2, var1);
                    } else {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX, this.bounds.minY - 1 + random.nextInt(3), this.bounds.maxZ + 1, 0, var1);
                    }
                    break;
                case 2:
                    if (var2 <= 1) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX, this.bounds.minY - 1 + random.nextInt(3), this.bounds.minZ - 1, this.facing, var1);
                    } else if (var2 == 2) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY - 1 + random.nextInt(3), this.bounds.minZ, 1, var1);
                    } else {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY - 1 + random.nextInt(3), this.bounds.minZ, 3, var1);
                    }
                    break;
                case 3:
                    if (var2 <= 1) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY - 1 + random.nextInt(3), this.bounds.minZ, this.facing, var1);
                    } else if (var2 == 2) {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX - 3, this.bounds.minY - 1 + random.nextInt(3), this.bounds.minZ - 1, 2, var1);
                    } else {
                        MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX - 3, this.bounds.minY - 1 + random.nextInt(3), this.bounds.maxZ + 1, 0, var1);
                    }
            }

            if (var1 < 8) {
                if (this.facing != 2 && this.facing != 0) {
                    for(int var3 = this.bounds.minX + 3; var3 + 3 <= this.bounds.maxX; var3 += 5) {
                        int var4 = random.nextInt(5);
                        if (var4 == 0) {
                            MineshaftPieces.generateNextPiece(start, pieces, random, var3, this.bounds.minY, this.bounds.minZ - 1, 2, var1 + 1);
                        } else if (var4 == 1) {
                            MineshaftPieces.generateNextPiece(start, pieces, random, var3, this.bounds.minY, this.bounds.maxZ + 1, 0, var1 + 1);
                        }
                    }
                } else {
                    for(int var5 = this.bounds.minZ + 3; var5 + 3 <= this.bounds.maxZ; var5 += 5) {
                        int var6 = random.nextInt(5);
                        if (var6 == 0) {
                            MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY, var5, 1, var1 + 1);
                        } else if (var6 == 1) {
                            MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY, var5, 3, var1 + 1);
                        }
                    }
                }
            }

        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.bordersOnLiquids(world, bounds)) {
                return false;
            } else {
                int var1 = this.sections * 5 - 1;
                this.fillWithOutline(world, bounds, 0, 0, 0, 2, 1, var1, 0, 0, false);
                this.fillRandomlyWithOutline(world, bounds, random, 0.8F, 0, 2, 0, 2, 2, var1, 0, 0, false);
                if (this.hasCobwebs) {
                    this.fillRandomlyWithOutline(world, bounds, random, 0.6F, 0, 0, 0, 2, 1, var1, Block.COBWEB.id, 0, false);
                }

                for(int var2 = 0; var2 < this.sections; ++var2) {
                    int var3 = 2 + var2 * 5;
                    this.fillWithOutline(world, bounds, 0, 0, var3, 0, 1, var3, Block.FENCE.id, 0, false);
                    this.fillWithOutline(world, bounds, 2, 0, var3, 2, 1, var3, Block.FENCE.id, 0, false);
                    if (random.nextInt(4) != 0) {
                        this.fillWithOutline(world, bounds, 0, 2, var3, 2, 2, var3, Block.PLANKS.id, 0, false);
                    } else {
                        this.fillWithOutline(world, bounds, 0, 2, var3, 0, 2, var3, Block.PLANKS.id, 0, false);
                        this.fillWithOutline(world, bounds, 2, 2, var3, 2, 2, var3, Block.PLANKS.id, 0, false);
                    }

                    this.setBlockWithThreshold(world, bounds, random, 0.1F, 0, 2, var3 - 1, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.1F, 2, 2, var3 - 1, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.1F, 0, 2, var3 + 1, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.1F, 2, 2, var3 + 1, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.05F, 0, 2, var3 - 2, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.05F, 2, 2, var3 - 2, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.05F, 0, 2, var3 + 2, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.05F, 2, 2, var3 + 2, Block.COBWEB.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.05F, 1, 2, var3 - 1, Block.TORCH.id, 0);
                    this.setBlockWithThreshold(world, bounds, random, 0.05F, 1, 2, var3 + 1, Block.TORCH.id, 0);
                    if (random.nextInt(100) == 0) {
                        this.placeChestWithLoot(world, bounds, random, 2, 0, var3 - 1, MineshaftPieces.LOOT_ENTRIES, 3 + random.nextInt(4));
                    }

                    if (random.nextInt(100) == 0) {
                        this.placeChestWithLoot(world, bounds, random, 0, 0, var3 + 1, MineshaftPieces.LOOT_ENTRIES, 3 + random.nextInt(4));
                    }

                    if (this.hasCobwebs && !this.hasSpiderSpawner) {
                        int var4 = this.transformY(0);
                        int var5 = var3 - 1 + random.nextInt(3);
                        int var6 = this.transformX(1, var5);
                        var5 = this.transformZ(1, var5);
                        if (bounds.contains(var6, var4, var5)) {
                            this.hasSpiderSpawner = true;
                            world.setBlock(var6, var4, var5, Block.SPAWNER.id);
                            MobSpawnerBlockEntity mobSpawnerBlockEntity = (MobSpawnerBlockEntity)world.getBlockEntity(var6, var4, var5);
                            if (mobSpawnerBlockEntity != null) {
                                mobSpawnerBlockEntity.setSpawnedEntityId("Spider");
                            }
                        }
                    }
                }

                for(int var7 = 0; var7 <= 2; ++var7) {
                    for(int var8 = 0; var8 <= var1; ++var8) {
                        int var9 = this.getBlock(world, var7, -1, var8, bounds);
                        if (var9 == 0) {
                            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, var7, -1, var8, bounds);
                        }
                    }
                }

                if (this.hasRails) {
                    for(int var10 = 0; var10 <= var1; ++var10) {
                        int var11 = this.getBlock(world, 1, -1, var10, bounds);
                        if (var11 > 0 && Block.BLOCKS_OPAQUE[var11]) {
                            this.setBlockWithThreshold(world, bounds, random, 0.7F, 1, 0, var10, Block.RAIL.id, this.postProcessBlockMetadata(Block.RAIL.id, 0));
                        }
                    }
                }

                return true;
            }
        }
    }

    public static class Crossing extends StructurePiece {
        private final int direction;
        private final boolean hasTwoFloors;

        public Crossing(int generationDepth, Random random, StructureBox bounds, int direction) {
            super(generationDepth);
            this.direction = direction;
            this.bounds = bounds;
            this.hasTwoFloors = bounds.getSizeY() > 3;
        }

        public static StructureBox findSize(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing) {
            StructureBox var1 = new StructureBox(x, y, z, x, y + 2, z);
            if (random.nextInt(4) == 0) {
                var1.maxY += 4;
            }

            switch (facing) {
                case 0:
                    var1.minX = x - 1;
                    var1.maxX = x + 3;
                    var1.maxZ = z + 4;
                    break;
                case 1:
                    var1.minX = x - 4;
                    var1.minZ = z - 1;
                    var1.maxZ = z + 3;
                    break;
                case 2:
                    var1.minX = x - 1;
                    var1.maxX = x + 3;
                    var1.minZ = z - 4;
                    break;
                case 3:
                    var1.maxX = x + 4;
                    var1.minZ = z - 1;
                    var1.maxZ = z + 3;
            }

            return StructurePiece.getIntersectingPiece(pieces, var1) != null ? null : var1;
        }

        public void addChildren(StructurePiece start, List<StructurePiece> pieces, Random random) {
            int var1 = this.getGenerationDepth();
            switch (this.direction) {
                case 0:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY, this.bounds.maxZ + 1, 0, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY, this.bounds.minZ + 1, 1, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY, this.bounds.minZ + 1, 3, var1);
                    break;
                case 1:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY, this.bounds.minZ - 1, 2, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY, this.bounds.maxZ + 1, 0, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY, this.bounds.minZ + 1, 1, var1);
                    break;
                case 2:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY, this.bounds.minZ - 1, 2, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY, this.bounds.minZ + 1, 1, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY, this.bounds.minZ + 1, 3, var1);
                    break;
                case 3:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY, this.bounds.minZ - 1, 2, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY, this.bounds.maxZ + 1, 0, var1);
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY, this.bounds.minZ + 1, 3, var1);
            }

            if (this.hasTwoFloors) {
                if (random.nextBoolean()) {
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY + 3 + 1, this.bounds.minZ - 1, 2, var1);
                }

                if (random.nextBoolean()) {
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY + 3 + 1, this.bounds.minZ + 1, 1, var1);
                }

                if (random.nextBoolean()) {
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY + 3 + 1, this.bounds.minZ + 1, 3, var1);
                }

                if (random.nextBoolean()) {
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX + 1, this.bounds.minY + 3 + 1, this.bounds.maxZ + 1, 0, var1);
                }
            }

        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.bordersOnLiquids(world, bounds)) {
                return false;
            } else {
                if (this.hasTwoFloors) {
                    this.fillWithOutline(world, bounds, this.bounds.minX + 1, this.bounds.minY, this.bounds.minZ, this.bounds.maxX - 1, this.bounds.minY + 3 - 1, this.bounds.maxZ, 0, 0, false);
                    this.fillWithOutline(world, bounds, this.bounds.minX, this.bounds.minY, this.bounds.minZ + 1, this.bounds.maxX, this.bounds.minY + 3 - 1, this.bounds.maxZ - 1, 0, 0, false);
                    this.fillWithOutline(world, bounds, this.bounds.minX + 1, this.bounds.maxY - 2, this.bounds.minZ, this.bounds.maxX - 1, this.bounds.maxY, this.bounds.maxZ, 0, 0, false);
                    this.fillWithOutline(world, bounds, this.bounds.minX, this.bounds.maxY - 2, this.bounds.minZ + 1, this.bounds.maxX, this.bounds.maxY, this.bounds.maxZ - 1, 0, 0, false);
                    this.fillWithOutline(world, bounds, this.bounds.minX + 1, this.bounds.minY + 3, this.bounds.minZ + 1, this.bounds.maxX - 1, this.bounds.minY + 3, this.bounds.maxZ - 1, 0, 0, false);
                } else {
                    this.fillWithOutline(world, bounds, this.bounds.minX + 1, this.bounds.minY, this.bounds.minZ, this.bounds.maxX - 1, this.bounds.maxY, this.bounds.maxZ, 0, 0, false);
                    this.fillWithOutline(world, bounds, this.bounds.minX, this.bounds.minY, this.bounds.minZ + 1, this.bounds.maxX, this.bounds.maxY, this.bounds.maxZ - 1, 0, 0, false);
                }

                this.fillWithOutline(world, bounds, this.bounds.minX + 1, this.bounds.minY, this.bounds.minZ + 1, this.bounds.minX + 1, this.bounds.maxY, this.bounds.minZ + 1, Block.PLANKS.id, 0, false);
                this.fillWithOutline(world, bounds, this.bounds.minX + 1, this.bounds.minY, this.bounds.maxZ - 1, this.bounds.minX + 1, this.bounds.maxY, this.bounds.maxZ - 1, Block.PLANKS.id, 0, false);
                this.fillWithOutline(world, bounds, this.bounds.maxX - 1, this.bounds.minY, this.bounds.minZ + 1, this.bounds.maxX - 1, this.bounds.maxY, this.bounds.minZ + 1, Block.PLANKS.id, 0, false);
                this.fillWithOutline(world, bounds, this.bounds.maxX - 1, this.bounds.minY, this.bounds.maxZ - 1, this.bounds.maxX - 1, this.bounds.maxY, this.bounds.maxZ - 1, Block.PLANKS.id, 0, false);

                for(int var1 = this.bounds.minX; var1 <= this.bounds.maxX; ++var1) {
                    for(int var2 = this.bounds.minZ; var2 <= this.bounds.maxZ; ++var2) {
                        int var3 = this.getBlock(world, var1, this.bounds.minY - 1, var2, bounds);
                        if (var3 == 0) {
                            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, var1, this.bounds.minY - 1, var2, bounds);
                        }
                    }
                }

                return true;
            }
        }
    }

    public static class Stairs extends StructurePiece {
        public Stairs(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static StructureBox findSize(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing) {
            StructureBox var1 = new StructureBox(x, y - 5, z, x, y + 2, z);
            switch (facing) {
                case 0:
                    var1.maxX = x + 2;
                    var1.maxZ = z + 8;
                    break;
                case 1:
                    var1.minX = x - 8;
                    var1.maxZ = z + 2;
                    break;
                case 2:
                    var1.maxX = x + 2;
                    var1.minZ = z - 8;
                    break;
                case 3:
                    var1.maxX = x + 8;
                    var1.maxZ = z + 2;
            }

            return StructurePiece.getIntersectingPiece(pieces, var1) != null ? null : var1;
        }

        public void addChildren(StructurePiece start, List<StructurePiece> pieces, Random random) {
            int var1 = this.getGenerationDepth();
            switch (this.facing) {
                case 0:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX, this.bounds.minY, this.bounds.maxZ + 1, 0, var1);
                    break;
                case 1:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX - 1, this.bounds.minY, this.bounds.minZ, 1, var1);
                    break;
                case 2:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.minX, this.bounds.minY, this.bounds.minZ - 1, 2, var1);
                    break;
                case 3:
                    MineshaftPieces.generateNextPiece(start, pieces, random, this.bounds.maxX + 1, this.bounds.minY, this.bounds.minZ, 3, var1);
            }

        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.bordersOnLiquids(world, bounds)) {
                return false;
            } else {
                this.fillWithOutline(world, bounds, 0, 5, 0, 2, 7, 1, 0, 0, false);
                this.fillWithOutline(world, bounds, 0, 0, 7, 2, 2, 8, 0, 0, false);

                for(int var1 = 0; var1 < 5; ++var1) {
                    this.fillWithOutline(world, bounds, 0, 5 - var1 - (var1 < 4 ? 1 : 0), 2 + var1, 2, 7 - var1, 2 + var1, 0, 0, false);
                }

                return true;
            }
        }
    }
}