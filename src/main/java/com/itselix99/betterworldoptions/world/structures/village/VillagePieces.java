package com.itselix99.betterworldoptions.world.structures.village;

import java.util.*;

import com.itselix99.betterworldoptions.api.structure.LootEntry;
import com.itselix99.betterworldoptions.api.structure.StructureBox;
import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import com.itselix99.betterworldoptions.util.MathUtil;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;

public class VillagePieces {
    public static Map<Integer, Integer> blocks = new HashMap<>();

    public static ArrayList<VillagePieceWeight> getPieceWeights(Random random, int size) {
        ArrayList<VillagePieceWeight> var1 = new ArrayList<>();
        var1.add(new VillagePieceWeight(SmallHouse.class, 4, MathUtil.nextInt(random, 2 + size, 4 + size * 2)));
        var1.add(new VillagePieceWeight(PriestHouse.class, 20, MathUtil.nextInt(random, size, 1 + size)));
        var1.add(new VillagePieceWeight(LibrarianHouse.class, 20, MathUtil.nextInt(random, size, 2 + size)));
        var1.add(new VillagePieceWeight(FarmerHouse.class, 3, MathUtil.nextInt(random, 2 + size, 5 + size * 3)));
        var1.add(new VillagePieceWeight(ButcherHouse.class, 15, MathUtil.nextInt(random, size, 2 + size)));
        var1.add(new VillagePieceWeight(DoubleFarm.class, 3, MathUtil.nextInt(random, 1 + size, 4 + size)));
        var1.add(new VillagePieceWeight(Farm.class, 3, MathUtil.nextInt(random, 2 + size, 4 + size * 2)));
        var1.add(new VillagePieceWeight(BlacksmithHouse.class, 15, MathUtil.nextInt(random, 0, 1 + size)));
        var1.add(new VillagePieceWeight(LargeHouse.class, 8, MathUtil.nextInt(random, size, 3 + size * 2)));
        Iterator<VillagePieceWeight> var2 = var1.iterator();

        while(var2.hasNext()) {
            if (var2.next().maxAmount == 0) {
                var2.remove();
            }
        }

        return var1;
    }

    private static int updateTotalWeight(ArrayList<VillagePieceWeight> weights) {
        int var1 = 0;
        int var2 = 0;

        for(VillagePieceWeight villagePieceWeight : weights) {
            if (villagePieceWeight.maxAmount > 0 && villagePieceWeight.amountGenerated < villagePieceWeight.maxAmount) {
                var1 = 1;
            }

            var2 += villagePieceWeight.weight;
        }

        return var1 != 0 ? var2 : -1;
    }

    private static VillagePiece createBuildingPiece(VillagePieceWeight weight, List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
        Class<? extends StructurePiece> var1 = weight.type;
        VillagePiece var2 = null;
        if (var1 == SmallHouse.class) {
            var2 = VillagePieces.SmallHouse.of(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == PriestHouse.class) {
            var2 = VillagePieces.PriestHouse.of(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == LibrarianHouse.class) {
            var2 = VillagePieces.LibrarianHouse.of(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == FarmerHouse.class) {
            var2 = VillagePieces.FarmerHouse.of(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == ButcherHouse.class) {
            var2 = VillagePieces.ButcherHouse.of(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == DoubleFarm.class) {
            var2 = VillagePieces.DoubleFarm.of(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == Farm.class) {
            var2 = VillagePieces.Farm.create(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == BlacksmithHouse.class) {
            var2 = VillagePieces.BlacksmithHouse.of(pieces, random, x, y, z, facing, generationDepth);
        } else if (var1 == LargeHouse.class) {
            var2 = VillagePieces.LargeHouse.of(pieces, random, x, y, z, facing, generationDepth);
        }

        return var2;
    }

    private static VillagePiece generateNextBuildingPiece(Start startPiece, List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
        int var1 = updateTotalWeight(startPiece.weights);
        if (var1 <= 0) {
            return null;
        } else {
            int var2 = 0;

            while(var2 < 5) {
                ++var2;
                int var3 = random.nextInt(var1);

                for(VillagePieceWeight var4 : startPiece.weights) {
                    var3 -= var4.weight;
                    if (var3 < 0) {
                        if (!var4.isValid(generationDepth) || var4 == startPiece.previous && startPiece.weights.size() > 1) {
                            break;
                        }

                        VillagePiece var5 = createBuildingPiece(var4, pieces, random, x, y, z, facing, generationDepth);
                        if (var5 != null) {
                            ++var4.amountGenerated;
                            startPiece.previous = var4;
                            if (!var4.isValid()) {
                                startPiece.weights.remove(var4);
                            }

                            return var5;
                        }
                    }
                }
            }

            StructureBox var6 = VillagePieces.LampPost.findSize(pieces, random, x, y, z, facing);
            if (var6 != null) {
                return new LampPost(generationDepth, random, var6, facing);
            } else {
                return null;
            }
        }
    }

    private static StructurePiece generateBuildingPiece(Start startPiece, List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
        if (generationDepth > 50) {
            return null;
        } else if (Math.abs(x - startPiece.getBounds().minX) <= 112 && Math.abs(z - startPiece.getBounds().minZ) <= 112) {
            VillagePiece var1 = generateNextBuildingPiece(startPiece, pieces, random, x, y, z, facing, generationDepth + 1);
            if (var1 != null) {
                pieces.add(var1);
                startPiece.buildingPieces.add(var1);
                return var1;
            }

            return null;
        } else {
            return null;
        }
    }

    private static StructurePiece generateRoadPiece(Start startPiece, List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
        if (generationDepth > 3 + startPiece.size) {
            return null;
        } else if (Math.abs(x - startPiece.getBounds().minX) <= 112 && Math.abs(z - startPiece.getBounds().minZ) <= 112) {
            StructureBox var1 = VillagePieces.Road.findSize(startPiece, pieces, random, x, y, z, facing);
            if (var1 != null && var1.minY > 10) {
                Road var2 = new Road(generationDepth, random, var1, facing);
                pieces.add(var2);
                startPiece.roadPieces.add(var2);
                return var2;
            }

            return null;
        } else {
            return null;
        }
    }

    static {
        blocks.put(Block.GLASS.id, Block.GLASS.id);
        blocks.put(Block.FENCE.id, Block.FENCE.id);
    }

    public static class VillagePieceWeight {
        public Class<? extends StructurePiece> type;
        public final int weight;
        public int amountGenerated;
        public int maxAmount;

        public VillagePieceWeight(Class<? extends StructurePiece> type, int weight, int maxAmount) {
            this.type = type;
            this.weight = weight;
            this.maxAmount = maxAmount;
        }

        public boolean isValid(int generationDepth) {
            return this.maxAmount == 0 || this.amountGenerated < this.maxAmount;
        }

        public boolean isValid() {
            return this.maxAmount == 0 || this.amountGenerated < this.maxAmount;
        }
    }

    public abstract static class VillagePiece extends StructurePiece {
        protected VillagePiece(int i) {
            super(i);
        }

        protected StructurePiece generatePieceLeft(Start startPiece, List<StructurePiece> pieces, Random random, int yOffset, int offset) {
            return switch (this.facing) {
                case 0, 2 ->
                        VillagePieces.generateBuildingPiece(startPiece, pieces, random, this.bounds.minX - 1, this.bounds.minY + yOffset, this.bounds.minZ + offset, 1, this.getGenerationDepth());
                case 1, 3 ->
                        VillagePieces.generateBuildingPiece(startPiece, pieces, random, this.bounds.minX + offset, this.bounds.minY + yOffset, this.bounds.minZ - 1, 2, this.getGenerationDepth());
                default -> null;
            };
        }

        protected StructurePiece generatePieceRight(Start startPiece, List<StructurePiece> pieces, Random random, int yOffset, int offset) {
            return switch (this.facing) {
                case 0, 2 ->
                        VillagePieces.generateBuildingPiece(startPiece, pieces, random, this.bounds.maxX + 1, this.bounds.minY + yOffset, this.bounds.minZ + offset, 3, this.getGenerationDepth());
                case 1, 3 ->
                        VillagePieces.generateBuildingPiece(startPiece, pieces, random, this.bounds.minX + offset, this.bounds.minY + yOffset, this.bounds.maxZ + 1, 0, this.getGenerationDepth());
                default -> null;
            };
        }

        protected int updateHeight(World world, StructureBox bounds) {
            int var1 = 0;
            int var2 = 0;

            for(int var3 = this.bounds.minZ; var3 <= this.bounds.maxZ; ++var3) {
                for(int var4 = this.bounds.minX; var4 <= this.bounds.maxX; ++var4) {
                    if (bounds.contains(var4, 64, var3)) {
                        var1 += Math.max(((BWOWorld) world).bwo_getSurfaceHeight(var4, var3), 64);
                        ++var2;
                    }
                }
            }

            if (var2 == 0) {
                return -1;
            } else {
                return var1 / var2;
            }
        }

        protected static boolean isValidBounds(StructureBox bounds) {
            return bounds != null && bounds.minY > 10;
        }
    }

    public static class Well extends VillagePiece {
        private int hpos = -1;

        public Well(int generationDepth, Random random, int x, int z) {
            super(generationDepth);
            this.facing = random.nextInt(4);
            switch (this.facing) {
                case 0:
                case 2:
                    this.bounds = new StructureBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
                    break;
                default:
                    this.bounds = new StructureBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
            }

        }

        public void addChildren(StructurePiece start, List<StructurePiece> pieces, Random random) {
            VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.minX - 1, this.bounds.maxY - 4, this.bounds.minZ + 1, 1, this.getGenerationDepth());
            VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.maxX + 1, this.bounds.maxY - 4, this.bounds.minZ + 1, 3, this.getGenerationDepth());
            VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.minX + 1, this.bounds.maxY - 4, this.bounds.minZ - 1, 2, this.getGenerationDepth());
            VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.minX + 1, this.bounds.maxY - 4, this.bounds.maxZ + 1, 0, this.getGenerationDepth());
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 3, 0);
            }

            this.fillWithOutline(world, bounds, 1, 0, 1, 4, 12, 4, Block.COBBLESTONE.id, Block.FLOWING_WATER.id, false);
            this.setBlockWithMetadata(world, 0, 0, 2, 12, 2, bounds);
            this.setBlockWithMetadata(world, 0, 0, 3, 12, 2, bounds);
            this.setBlockWithMetadata(world, 0, 0, 2, 12, 3, bounds);
            this.setBlockWithMetadata(world, 0, 0, 3, 12, 3, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 13, 1, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 14, 1, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 13, 1, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 14, 1, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 13, 4, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 14, 4, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 13, 4, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 14, 4, bounds);
            this.fillWithOutline(world, bounds, 1, 15, 1, 4, 15, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);

            for(int var1 = 0; var1 <= 5; ++var1) {
                for(int var2 = 0; var2 <= 5; ++var2) {
                    if (var2 == 0 || var2 == 5 || var1 == 0 || var1 == 5) {
                        int var3 = ((BWOWorld) world).bwo_getSurfaceHeight(var1, var2) - 1;
                        if (var3 < 64) {
                            var3 = 63;
                        }

                        int blockId = world.getBlockId(var1, var3, var2);

                        if (Block.BLOCKS[blockId] != null && Block.BLOCKS[blockId].material.isFluid()) {
                            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, var2, 11, var1, bounds);
                        } else {
                            this.setBlockWithMetadata(world, Block.GRAVEL.id, 0, var2, 11, var1, bounds);
                            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, var2, 10, var1, bounds);
                        }

                        this.fillAirColumnUp(world, var2, 12, var1, bounds);
                    }
                }
            }

            return true;
        }
    }

    public static class Start extends Well {
        public BiomeSource biomeSource;
        public int size;
        public VillagePieceWeight previous;
        public ArrayList<VillagePieceWeight> weights;
        public ArrayList<StructurePiece> buildingPieces = new ArrayList<>();
        public ArrayList<StructurePiece> roadPieces = new ArrayList<>();

        public Start(BiomeSource biomeSource, int i, Random random, int x, int z, ArrayList<VillagePieceWeight> weights, int size) {
            super(0, random, x, z);
            this.biomeSource = biomeSource;
            this.weights = weights;
            this.size = size;
        }

        public BiomeSource getBiomeSource() {
            return this.biomeSource;
        }
    }

    public abstract static class RoadPiece extends VillagePiece {
        protected RoadPiece(int i) {
            super(i);
        }
    }

    public static class Road extends RoadPiece {
        private final int length;

        public Road(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
            this.length = Math.max(bounds.getSizeX(), bounds.getSizeZ());
        }

        public void addChildren(StructurePiece start, List<StructurePiece> pieces, Random random) {
            int var1 = 0;

            for(int var2 = random.nextInt(5); var2 < this.length - 8; var2 += 2 + random.nextInt(5)) {
                StructurePiece var3 = this.generatePieceLeft((Start)start, pieces, random, 0, var2);
                if (var3 != null) {
                    var2 += Math.max(var3.getBounds().getSizeX(), var3.getBounds().getSizeZ());
                    var1 = 1;
                }
            }

            for(int var4 = random.nextInt(5); var4 < this.length - 8; var4 += 2 + random.nextInt(5)) {
                StructurePiece var5 = this.generatePieceRight((Start)start, pieces, random, 0, var4);
                if (var5 != null) {
                    var4 += Math.max(var5.getBounds().getSizeX(), var5.getBounds().getSizeZ());
                    var1 = 1;
                }
            }

            if (var1 != 0 && random.nextInt(3) > 0) {
                switch (this.facing) {
                    case 0:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.minX - 1, this.bounds.minY, this.bounds.maxZ - 2, 1, this.getGenerationDepth());
                        break;
                    case 1:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.minX, this.bounds.minY, this.bounds.minZ - 1, 2, this.getGenerationDepth());
                        break;
                    case 2:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.minX - 1, this.bounds.minY, this.bounds.minZ, 1, this.getGenerationDepth());
                        break;
                    case 3:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.maxX - 2, this.bounds.minY, this.bounds.minZ - 1, 2, this.getGenerationDepth());
                }
            }

            if (var1 != 0 && random.nextInt(3) > 0) {
                switch (this.facing) {
                    case 0:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.maxX + 1, this.bounds.minY, this.bounds.maxZ - 2, 3, this.getGenerationDepth());
                        break;
                    case 1:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.minX, this.bounds.minY, this.bounds.maxZ + 1, 0, this.getGenerationDepth());
                        break;
                    case 2:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.maxX + 1, this.bounds.minY, this.bounds.minZ, 3, this.getGenerationDepth());
                        break;
                    case 3:
                        VillagePieces.generateRoadPiece((Start)start, pieces, random, this.bounds.maxX - 2, this.bounds.minY, this.bounds.maxZ + 1, 0, this.getGenerationDepth());
                }
            }

        }

        public static StructureBox findSize(Start startPiece, List<StructurePiece> pieces, Random random, int x, int y, int z, int facing) {
            for(int var1 = 7 * MathUtil.nextInt(random, 3, 5); var1 >= 7; var1 -= 7) {
                StructureBox var2 = StructureBox.orient(x, y, z, 0, 0, 0, 3, 3, var1, facing);
                if (StructurePiece.getIntersectingPiece(pieces, var2) == null) {
                    return var2;
                }
            }

            return null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            for(int var1 = this.bounds.minX; var1 <= this.bounds.maxX; ++var1) {
                for(int var2 = this.bounds.minZ; var2 <= this.bounds.maxZ; ++var2) {
                    if (bounds.contains(var1, 64, var2)) {
                        int var3 = ((BWOWorld) world).bwo_getSurfaceHeight(var1, var2) - 1;
                        if (var3 < 64) {
                            var3 = 63;
                        }

                        int blockId = world.getBlockId(var1, var3, var2);

                        if (Block.BLOCKS[blockId] != null && Block.BLOCKS[blockId].material.isFluid()) {
                            world.setBlockWithoutNotifyingNeighbors(var1, var3, var2, Block.PLANKS.id);
                        } else {
                            world.setBlockWithoutNotifyingNeighbors(var1, var3, var2, Block.GRAVEL.id);
                            world.setBlockWithoutNotifyingNeighbors(var1, var3 - 1, var2, Block.COBBLESTONE.id);
                        }
                    }
                }
            }

            return true;
        }
    }

    public static class SmallHouse extends VillagePiece {
        private int hpos = -1;
        private final boolean terrace;

        public SmallHouse(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
            this.terrace = random.nextBoolean();
        }

        public static SmallHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 5, 6, 5, facing);
            return StructurePiece.getIntersectingPiece(pieces, var1) != null ? null : new SmallHouse(generationDepth, random, var1, facing);
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 6 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 0, 0, 0, 4, 0, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 4, 0, 4, 4, 4, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 1, 4, 1, 3, 4, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 0, 1, 0, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 0, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 0, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 4, 1, 0, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 4, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 4, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 0, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 0, 2, 4, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 0, 3, 4, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 4, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 4, 2, 4, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 4, 3, 4, bounds);
            this.fillWithOutline(world, bounds, 0, 1, 1, 0, 3, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 4, 1, 1, 4, 3, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 1, 4, 3, 3, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 2, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 2, 2, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 1, 1, 0, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 1, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 1, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 2, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 3, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 3, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 3, 1, 0, bounds);
            if (this.getBlock(world, 2, 0, -1, bounds) == 0 && this.getBlock(world, 2, -1, -1, bounds) != 0) {
                this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 2, 0, -1, bounds);
            }

            this.fillWithOutline(world, bounds, 1, 1, 1, 3, 3, 3, 0, 0, false);
            if (this.terrace) {
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 0, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 2, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 3, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 5, 0, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 0, 5, 4, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 5, 4, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 2, 5, 4, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 3, 5, 4, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 5, 4, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 5, 1, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 5, 2, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 5, 3, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 0, 5, 1, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 0, 5, 2, bounds);
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, 0, 5, 3, bounds);
            }

            if (this.terrace) {
                int var1 = this.postProcessBlockMetadata(Block.LADDER.id, 3);
                this.setBlockWithMetadata(world, Block.LADDER.id, var1, 3, 1, 3, bounds);
                this.setBlockWithMetadata(world, Block.LADDER.id, var1, 3, 2, 3, bounds);
                this.setBlockWithMetadata(world, Block.LADDER.id, var1, 3, 3, 3, bounds);
                this.setBlockWithMetadata(world, Block.LADDER.id, var1, 3, 4, 3, bounds);
            }

            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 2, 3, 1, bounds);

            for(int var2 = 0; var2 < 5; ++var2) {
                for(int var3 = 0; var3 < 5; ++var3) {
                    this.fillAirColumnUp(world, var3, 6, var2, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var3, -1, var2, bounds);
                }
            }
            return true;
        }
    }

    public static class PriestHouse extends VillagePiece {
        private int hpos = -1;

        public PriestHouse(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static PriestHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 5, 12, 9, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new PriestHouse(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 12 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 1, 1, 1, 3, 3, 7, 0, 0, false);
            this.fillWithOutline(world, bounds, 1, 5, 1, 3, 9, 3, 0, 0, false);
            this.fillWithOutline(world, bounds, 1, 0, 0, 3, 0, 8, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 1, 0, 3, 10, 0, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 1, 1, 0, 10, 3, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 4, 1, 1, 4, 10, 3, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 0, 4, 0, 4, 7, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 4, 0, 4, 4, 4, 7, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 1, 8, 3, 4, 8, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 5, 4, 3, 10, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 5, 5, 3, 5, 7, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 9, 0, 4, 9, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 4, 0, 4, 4, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 0, 11, 2, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 4, 11, 2, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 2, 11, 0, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 2, 11, 4, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 1, 1, 6, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 1, 1, 7, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 2, 1, 7, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 3, 1, 6, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 3, 1, 7, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 1, 1, 5, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 2, 1, 6, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 3, 1, 5, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 1), 1, 2, 7, bounds);
            this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 0), 3, 2, 7, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 3, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 3, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 6, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 7, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 6, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 7, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 6, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 7, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 6, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 7, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 3, 6, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 3, 6, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 3, 8, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 2, 4, 7, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 1, 4, 6, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 3, 4, 6, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 2, 4, 5, bounds);
            int var1 = this.postProcessBlockMetadata(Block.LADDER.id, 4);

            for(int var2 = 1; var2 <= 9; ++var2) {
                this.setBlockWithMetadata(world, Block.LADDER.id, var1, 3, var2, 3, bounds);
            }

            this.setBlockWithMetadata(world, 0, 0, 2, 1, 0, bounds);
            this.setBlockWithMetadata(world, 0, 0, 2, 2, 0, bounds);
            this.placeWoodenDoor(world, bounds, random, 2, 1, 0, this.postProcessBlockMetadata(Block.DOOR.id, 1));
            if (this.getBlock(world, 2, 0, -1, bounds) == 0 && this.getBlock(world, 2, -1, -1, bounds) != 0) {
                this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 2, 0, -1, bounds);
            }

            for(int var3 = 0; var3 < 9; ++var3) {
                for(int var4 = 0; var4 < 5; ++var4) {
                    this.fillAirColumnUp(world, var4, 12, var3, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var4, -1, var3, bounds);
                }
            }

            return true;
        }

    }

    public static class LibrarianHouse extends VillagePiece {
        private int hpos = -1;

        public LibrarianHouse(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static LibrarianHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 9, 9, 6, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new LibrarianHouse(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 9 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 1, 1, 1, 7, 5, 4, 0, 0, false);
            this.fillWithOutline(world, bounds, 0, 0, 0, 8, 0, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 5, 0, 8, 5, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 6, 1, 8, 6, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 7, 2, 8, 7, 3, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            int var1 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 3);
            int var2 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 2);

            for(int var3 = -1; var3 <= 2; ++var3) {
                for(int var4 = 0; var4 <= 8; ++var4) {
                    this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var1, var4, 6 + var3, var3, bounds);
                    this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var2, var4, 6 + var3, 5 - var3, bounds);
                }
            }

            this.fillWithOutline(world, bounds, 0, 1, 0, 0, 1, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 1, 5, 8, 1, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 8, 1, 0, 8, 1, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 2, 1, 0, 7, 1, 0, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 2, 0, 0, 4, 0, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 2, 5, 0, 4, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 8, 2, 5, 8, 4, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 8, 2, 0, 8, 4, 0, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 2, 1, 0, 4, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 2, 5, 7, 4, 5, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 8, 2, 1, 8, 4, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 2, 0, 7, 4, 0, Block.PLANKS.id, Block.PLANKS.id, false);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 2, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 5, 2, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 6, 2, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 3, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 5, 3, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 6, 3, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 3, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 3, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 3, 3, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 3, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 3, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 3, 3, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 2, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 3, 2, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 5, 2, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 6, 2, 5, bounds);
            this.fillWithOutline(world, bounds, 1, 4, 1, 7, 4, 1, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 4, 4, 7, 4, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 3, 4, 7, 3, 4, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 7, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 0), 7, 1, 3, bounds);
            int var5 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 3);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var5, 6, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var5, 5, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var5, 4, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var5, 3, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 6, 1, 3, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_PRESSURE_PLATE.id, 0, 6, 2, 3, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 4, 1, 3, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_PRESSURE_PLATE.id, 0, 4, 2, 3, bounds);
            this.setBlockWithMetadata(world, Block.CRAFTING_TABLE.id, 0, 7, 1, 1, bounds);
            this.setBlockWithMetadata(world, 0, 0, 1, 1, 0, bounds);
            this.setBlockWithMetadata(world, 0, 0, 1, 2, 0, bounds);
            this.placeWoodenDoor(world, bounds, random, 1, 1, 0, this.postProcessBlockMetadata(Block.DOOR.id, 1));
            if (this.getBlock(world, 1, 0, -1, bounds) == 0 && this.getBlock(world, 1, -1, -1, bounds) != 0) {
                this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 1, 0, -1, bounds);
            }

            for(int var6 = 0; var6 < 6; ++var6) {
                for(int var7 = 0; var7 < 9; ++var7) {
                    this.fillAirColumnUp(world, var7, 9, var6, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var7, -1, var6, bounds);
                }
            }

            return true;
        }

    }

    public static class FarmerHouse extends VillagePiece {
        private int hpos = -1;
        private final boolean lowCeiling;
        private final int tablePosition;

        public FarmerHouse(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
            this.lowCeiling = random.nextBoolean();
            this.tablePosition = random.nextInt(3);
        }

        public static FarmerHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 4, 6, 5, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new FarmerHouse(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 6 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 1, 1, 1, 3, 5, 4, 0, 0, false);
            this.fillWithOutline(world, bounds, 0, 0, 0, 3, 0, 4, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 1, 2, 0, 3, Block.DIRT.id, Block.DIRT.id, false);
            if (this.lowCeiling) {
                this.fillWithOutline(world, bounds, 1, 4, 1, 2, 4, 3, Block.LOG.id, Block.LOG.id, false);
            } else {
                this.fillWithOutline(world, bounds, 1, 5, 1, 2, 5, 3, Block.LOG.id, Block.LOG.id, false);
            }

            this.setBlockWithMetadata(world, Block.LOG.id, 0, 1, 4, 0, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 2, 4, 0, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 1, 4, 4, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 2, 4, 4, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 0, 4, 1, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 0, 4, 2, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 0, 4, 3, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 3, 4, 1, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 3, 4, 2, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 3, 4, 3, bounds);
            this.fillWithOutline(world, bounds, 0, 1, 0, 0, 3, 0, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 3, 1, 0, 3, 3, 0, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 0, 1, 4, 0, 3, 4, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 3, 1, 4, 3, 3, 4, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 0, 1, 1, 0, 3, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 3, 1, 1, 3, 3, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 1, 0, 2, 3, 0, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 1, 4, 2, 3, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 3, 2, 2, bounds);
            if (this.tablePosition > 0) {
                this.setBlockWithMetadata(world, Block.FENCE.id, 0, this.tablePosition, 1, 3, bounds);
                this.setBlockWithMetadata(world, Block.WOODEN_PRESSURE_PLATE.id, 0, this.tablePosition, 2, 3, bounds);
            }

            this.setBlockWithMetadata(world, 0, 0, 1, 1, 0, bounds);
            this.setBlockWithMetadata(world, 0, 0, 1, 2, 0, bounds);
            this.placeWoodenDoor(world, bounds, random, 1, 1, 0, this.postProcessBlockMetadata(Block.DOOR.id, 1));
            if (this.getBlock(world, 1, 0, -1, bounds) == 0 && this.getBlock(world, 1, -1, -1, bounds) != 0) {
                this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 1, 0, -1, bounds);
            }

            for(int var1 = 0; var1 < 5; ++var1) {
                for(int var2 = 0; var2 < 4; ++var2) {
                    this.fillAirColumnUp(world, var2, 6, var1, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var2, -1, var1, bounds);
                }
            }

            return true;
        }
    }

    public static class ButcherHouse extends VillagePiece {
        private int hpos = -1;

        public ButcherHouse(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static ButcherHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 9, 7, 11, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new ButcherHouse(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 7 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 1, 1, 1, 7, 4, 4, 0, 0, false);
            this.fillWithOutline(world, bounds, 2, 1, 6, 8, 4, 10, 0, 0, false);
            this.fillWithOutline(world, bounds, 2, 0, 6, 8, 0, 10, Block.DIRT.id, Block.DIRT.id, false);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 6, 0, 6, bounds);
            this.fillWithOutline(world, bounds, 2, 1, 6, 2, 1, 10, Block.FENCE.id, Block.FENCE.id, false);
            this.fillWithOutline(world, bounds, 8, 1, 6, 8, 1, 10, Block.FENCE.id, Block.FENCE.id, false);
            this.fillWithOutline(world, bounds, 3, 1, 10, 7, 1, 10, Block.FENCE.id, Block.FENCE.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 1, 7, 0, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 0, 0, 0, 3, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 8, 0, 0, 8, 3, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 0, 7, 1, 0, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 5, 7, 1, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 2, 0, 7, 3, 0, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 2, 5, 7, 3, 5, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 4, 1, 8, 4, 1, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 4, 4, 8, 4, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 5, 2, 8, 5, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 0, 4, 2, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 0, 4, 3, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 8, 4, 2, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 8, 4, 3, bounds);
            int var1 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 3);
            int var2 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 2);

            for(int var3 = -1; var3 <= 2; ++var3) {
                for(int var4 = 0; var4 <= 8; ++var4) {
                    this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var1, var4, 4 + var3, var3, bounds);
                    this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var2, var4, 4 + var3, 5 - var3, bounds);
                }
            }

            this.setBlockWithMetadata(world, Block.LOG.id, 0, 0, 2, 1, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 0, 2, 4, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 8, 2, 1, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 8, 2, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 3, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 3, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 2, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 3, 2, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 5, 2, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 6, 2, 5, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 2, 1, 3, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_PRESSURE_PLATE.id, 0, 2, 2, 3, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 1, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 3), 2, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 1), 1, 1, 3, bounds);
            this.fillWithOutline(world, bounds, 5, 0, 1, 7, 0, 3, Block.DOUBLE_SLAB.id, Block.DOUBLE_SLAB.id, false);
            this.setBlockWithMetadata(world, Block.DOUBLE_SLAB.id, 0, 6, 1, 1, bounds);
            this.setBlockWithMetadata(world, Block.DOUBLE_SLAB.id, 0, 6, 1, 2, bounds);
            this.setBlockWithMetadata(world, 0, 0, 2, 1, 0, bounds);
            this.setBlockWithMetadata(world, 0, 0, 2, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 2, 3, 1, bounds);
            this.placeWoodenDoor(world, bounds, random, 2, 1, 0, this.postProcessBlockMetadata(Block.DOOR.id, 1));
            if (this.getBlock(world, 2, 0, -1, bounds) == 0 && this.getBlock(world, 2, -1, -1, bounds) != 0) {
                this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 2, 0, -1, bounds);
            }

            this.setBlockWithMetadata(world, 0, 0, 6, 1, 5, bounds);
            this.setBlockWithMetadata(world, 0, 0, 6, 2, 5, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 6, 3, 4, bounds);
            this.placeWoodenDoor(world, bounds, random, 6, 1, 5, this.postProcessBlockMetadata(Block.DOOR.id, 1));

            for(int var5 = 0; var5 < 5; ++var5) {
                for(int var6 = 0; var6 < 9; ++var6) {
                    this.fillAirColumnUp(world, var6, 7, var5, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var6, -1, var5, bounds);
                }
            }

            return true;
        }

    }

    public static class LargeHouse extends VillagePiece {
        private int hpos = -1;

        public LargeHouse(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static LargeHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 9, 7, 12, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new LargeHouse(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 7 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 1, 1, 1, 7, 4, 4, 0, 0, false);
            this.fillWithOutline(world, bounds, 2, 1, 6, 8, 4, 10, 0, 0, false);
            this.fillWithOutline(world, bounds, 2, 0, 5, 8, 0, 10, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 1, 7, 0, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 0, 0, 0, 3, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 8, 0, 0, 8, 3, 10, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 0, 7, 2, 0, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 5, 2, 1, 5, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 2, 0, 6, 2, 3, 10, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 3, 0, 10, 7, 3, 10, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 1, 2, 0, 7, 3, 0, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 2, 5, 2, 3, 5, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 4, 1, 8, 4, 1, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 4, 4, 3, 4, 4, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 5, 2, 8, 5, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 0, 4, 2, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 0, 4, 3, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 8, 4, 2, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 8, 4, 3, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 8, 4, 4, bounds);
            int var1 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 3);
            int var2 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 2);

            for(int var3 = -1; var3 <= 2; ++var3) {
                for(int var4 = 0; var4 <= 8; ++var4) {
                    this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var1, var4, 4 + var3, var3, bounds);
                    if ((var3 > -1 || var4 <= 1) && (var3 > 0 || var4 <= 3) && (var3 > 1 || var4 <= 4 || var4 >= 6)) {
                        this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var2, var4, 4 + var3, 5 - var3, bounds);
                    }
                }
            }

            this.fillWithOutline(world, bounds, 3, 4, 5, 3, 4, 10, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 7, 4, 2, 7, 4, 10, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 4, 5, 4, 4, 5, 10, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 6, 5, 4, 6, 5, 10, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 5, 6, 3, 5, 6, 10, Block.PLANKS.id, Block.PLANKS.id, false);
            int var5 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 0);

            for(int var6 = 4; var6 >= 1; --var6) {
                this.setBlockWithMetadata(world, Block.PLANKS.id, 0, var6, 2 + var6, 7 - var6, bounds);

                for(int var7 = 8 - var6; var7 <= 10; ++var7) {
                    this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var5, var6, 2 + var6, var7, bounds);
                }
            }

            int var8 = this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 1);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 6, 6, 3, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 7, 5, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var8, 6, 6, 4, bounds);

            for(int var9 = 6; var9 <= 8; ++var9) {
                for(int var10 = 5; var10 <= 10; ++var10) {
                    this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, var8, var9, 12 - var9, var10, bounds);
                }
            }

            this.setBlockWithMetadata(world, Block.LOG.id, 0, 0, 2, 1, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 0, 2, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 3, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 4, 2, 0, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 5, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 6, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 8, 2, 1, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 3, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 8, 2, 4, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 8, 2, 5, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 8, 2, 6, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 7, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 8, 2, 8, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 8, 2, 9, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 2, 2, 6, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 2, 7, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 2, 8, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 2, 2, 9, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 4, 4, 10, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 5, 4, 10, bounds);
            this.setBlockWithMetadata(world, Block.LOG.id, 0, 6, 4, 10, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 5, 5, 10, bounds);
            this.setBlockWithMetadata(world, 0, 0, 2, 1, 0, bounds);
            this.setBlockWithMetadata(world, 0, 0, 2, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 2, 3, 1, bounds);
            this.placeWoodenDoor(world, bounds, random, 2, 1, 0, this.postProcessBlockMetadata(Block.DOOR.id, 1));
            this.fillWithOutline(world, bounds, 1, 0, -1, 3, 2, -1, 0, 0, false);
            if (this.getBlock(world, 2, 0, -1, bounds) == 0 && this.getBlock(world, 2, -1, -1, bounds) != 0) {
                this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), 2, 0, -1, bounds);
            }

            for(int var11 = 0; var11 < 5; ++var11) {
                for(int var12 = 0; var12 < 9; ++var12) {
                    this.fillAirColumnUp(world, var12, 7, var11, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var12, -1, var11, bounds);
                }
            }

            for(int var13 = 5; var13 < 11; ++var13) {
                for(int var14 = 2; var14 < 9; ++var14) {
                    this.fillAirColumnUp(world, var14, 7, var13, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var14, -1, var13, bounds);
                }
            }

            return true;
        }
    }

    public static class BlacksmithHouse extends VillagePiece {
        private static final LootEntry[] VILLAGE_BLACKSMITH_LOOT = new LootEntry[]{new LootEntry(Item.DIAMOND.id, 0, 1, 3, 3), new LootEntry(Item.IRON_INGOT.id, 0, 1, 5, 10), new LootEntry(Item.GOLD_INGOT.id, 0, 1, 3, 5), new LootEntry(Item.BREAD.id, 0, 1, 3, 15), new LootEntry(Item.APPLE.id, 0, 1, 3, 15), new LootEntry(Item.IRON_PICKAXE.id, 0, 1, 1, 5), new LootEntry(Item.IRON_SWORD.id, 0, 1, 1, 5), new LootEntry(Item.IRON_CHESTPLATE.id, 0, 1, 1, 5), new LootEntry(Item.IRON_HELMET.id, 0, 1, 1, 5), new LootEntry(Item.IRON_LEGGINGS.id, 0, 1, 1, 5), new LootEntry(Item.IRON_BOOTS.id, 0, 1, 1, 5), new LootEntry(Block.OBSIDIAN.id, 0, 3, 7, 5), new LootEntry(Block.SAPLING.id, 0, 3, 7, 5)};
        private int hpos = -1;

        public BlacksmithHouse(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static BlacksmithHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 10, 6, 7, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new BlacksmithHouse(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 6 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 0, 1, 0, 9, 4, 6, 0, 0, false);
            this.fillWithOutline(world, bounds, 0, 0, 0, 9, 0, 6, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 4, 0, 9, 4, 6, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.fillWithOutline(world, bounds, 0, 5, 0, 9, 5, 6, Block.SLAB.id, Block.SLAB.id, false);
            this.fillWithOutline(world, bounds, 1, 5, 1, 8, 5, 5, 0, 0, false);
            this.fillWithOutline(world, bounds, 1, 1, 0, 2, 3, 0, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 1, 0, 0, 4, 0, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 3, 1, 0, 3, 4, 0, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 0, 1, 6, 0, 4, 6, Block.LOG.id, Block.LOG.id, false);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 3, 3, 1, bounds);
            this.fillWithOutline(world, bounds, 3, 1, 2, 3, 3, 2, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 4, 1, 3, 5, 3, 3, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 0, 1, 1, 0, 3, 5, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 1, 1, 6, 5, 3, 6, Block.PLANKS.id, Block.PLANKS.id, false);
            this.fillWithOutline(world, bounds, 5, 1, 0, 5, 3, 0, Block.FENCE.id, Block.FENCE.id, false);
            this.fillWithOutline(world, bounds, 9, 1, 0, 9, 3, 0, Block.FENCE.id, Block.FENCE.id, false);
            this.fillWithOutline(world, bounds, 6, 1, 4, 9, 4, 6, Block.COBBLESTONE.id, Block.COBBLESTONE.id, false);
            this.setBlockWithMetadata(world, Block.FLOWING_LAVA.id, 0, 7, 1, 5, bounds);
            this.setBlockWithMetadata(world, Block.FLOWING_LAVA.id, 0, 8, 1, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.FENCE.id), 0, 9, 2, 5, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.FENCE.id), 0, 9, 2, 4, bounds);
            this.fillWithOutline(world, bounds, 7, 2, 4, 8, 2, 5, 0, 0, false);
            this.setBlockWithMetadata(world, Block.COBBLESTONE.id, 0, 6, 1, 3, bounds);
            this.setBlockWithMetadata(world, Block.FURNACE.id, 0, 6, 2, 3, bounds);
            this.setBlockWithMetadata(world, Block.FURNACE.id, 0, 6, 3, 3, bounds);
            this.setBlockWithMetadata(world, Block.DOUBLE_SLAB.id, 0, 8, 1, 1, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 2, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 0, 2, 4, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 2, 2, 6, bounds);
            this.setBlockWithMetadata(world, blocks.get(Block.GLASS.id), 0, 4, 2, 6, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 2, 1, 4, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_PRESSURE_PLATE.id, 0, 2, 2, 4, bounds);
            this.setBlockWithMetadata(world, Block.PLANKS.id, 0, 1, 1, 5, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 3), 2, 1, 5, bounds);
            this.setBlockWithMetadata(world, Block.WOODEN_STAIRS.id, this.postProcessBlockMetadata(Block.WOODEN_STAIRS.id, 1), 1, 1, 4, bounds);
            if (bounds.contains(this.transformX(5, 5), this.transformY(1), this.transformZ(5, 5))) {
                this.placeChestWithLoot(world, bounds, random, 5, 1, 5, VILLAGE_BLACKSMITH_LOOT, 3 + random.nextInt(6));
            }

            for(int var1 = 6; var1 <= 8; ++var1) {
                if (this.getBlock(world, var1, 0, -1, bounds) == 0 && this.getBlock(world, var1, -1, -1, bounds) != 0) {
                    this.setBlockWithMetadata(world, Block.COBBLESTONE_STAIRS.id, this.postProcessBlockMetadata(Block.COBBLESTONE_STAIRS.id, 3), var1, 0, -1, bounds);
                }
            }

            for(int var2 = 0; var2 < 7; ++var2) {
                for(int var3 = 0; var3 < 10; ++var3) {
                    this.fillAirColumnUp(world, var3, 6, var2, bounds);
                    this.fillColumnDown(world, Block.COBBLESTONE.id, 0, var3, -1, var2, bounds);
                }
            }

            return true;
        }

    }

    public static class Farm extends VillagePiece {
        private int hpos = -1;

        public Farm(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static Farm create(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 7, 4, 9, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new Farm(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 4 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 0, 1, 0, 6, 4, 8, 0, 0, false);
            this.fillWithOutline(world, bounds, 1, 0, 1, 2, 0, 7, Block.FARMLAND.id, Block.FARMLAND.id, false);
            this.fillWithOutline(world, bounds, 4, 0, 1, 5, 0, 7, Block.FARMLAND.id, Block.FARMLAND.id, false);
            this.fillWithOutline(world, bounds, 0, 0, 0, 0, 0, 8, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 6, 0, 0, 6, 0, 8, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 0, 5, 0, 0, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 8, 5, 0, 8, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 3, 0, 1, 3, 0, 7, Block.FLOWING_WATER.id, Block.FLOWING_WATER.id, false);

            for(int var1 = 1; var1 <= 7; ++var1) {
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 1, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 2, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 4, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 5, 1, var1, bounds);
            }

            for(int var2 = 0; var2 < 9; ++var2) {
                for(int var3 = 0; var3 < 7; ++var3) {
                    this.fillAirColumnUp(world, var3, 4, var2, bounds);
                    this.fillColumnDown(world, Block.DIRT.id, 0, var3, -1, var2, bounds);
                }
            }

            return true;
        }
    }

    public static class DoubleFarm extends VillagePiece {
        private int hpos = -1;

        public DoubleFarm(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static DoubleFarm of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 13, 4, 9, facing);
            return isValidBounds(var1) && StructurePiece.getIntersectingPiece(pieces, var1) == null ? new DoubleFarm(generationDepth, random, var1, facing) : null;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 4 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 0, 1, 0, 12, 4, 8, 0, 0, false);
            this.fillWithOutline(world, bounds, 1, 0, 1, 2, 0, 7, Block.FARMLAND.id, Block.FARMLAND.id, false);
            this.fillWithOutline(world, bounds, 4, 0, 1, 5, 0, 7, Block.FARMLAND.id, Block.FARMLAND.id, false);
            this.fillWithOutline(world, bounds, 7, 0, 1, 8, 0, 7, Block.FARMLAND.id, Block.FARMLAND.id, false);
            this.fillWithOutline(world, bounds, 10, 0, 1, 11, 0, 7, Block.FARMLAND.id, Block.FARMLAND.id, false);
            this.fillWithOutline(world, bounds, 0, 0, 0, 0, 0, 8, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 6, 0, 0, 6, 0, 8, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 12, 0, 0, 12, 0, 8, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 0, 11, 0, 0, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 1, 0, 8, 11, 0, 8, Block.LOG.id, Block.LOG.id, false);
            this.fillWithOutline(world, bounds, 3, 0, 1, 3, 0, 7, Block.FLOWING_WATER.id, Block.FLOWING_WATER.id, false);
            this.fillWithOutline(world, bounds, 9, 0, 1, 9, 0, 7, Block.FLOWING_WATER.id, Block.FLOWING_WATER.id, false);

            for(int var1 = 1; var1 <= 7; ++var1) {
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 1, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 2, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 4, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 5, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 7, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 8, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 10, 1, var1, bounds);
                this.setBlockWithMetadata(world, Block.WHEAT.id, MathUtil.nextInt(random, 2, 7), 11, 1, var1, bounds);
            }

            for(int var2 = 0; var2 < 9; ++var2) {
                for(int var3 = 0; var3 < 13; ++var3) {
                    this.fillAirColumnUp(world, var3, 4, var2, bounds);
                    this.fillColumnDown(world, Block.DIRT.id, 0, var3, -1, var2, bounds);
                }
            }

            return true;
        }
    }

    public static class LampPost extends VillagePiece {
        private int hpos = -1;

        public LampPost(int generationDepth, Random random, StructureBox bounds, int facing) {
            super(generationDepth);
            this.facing = facing;
            this.bounds = bounds;
        }

        public static StructureBox findSize(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing) {
            StructureBox var1 = StructureBox.orient(x, y, z, 0, 0, 0, 3, 4, 2, facing);
            return StructurePiece.getIntersectingPiece(pieces, var1) != null ? null : var1;
        }

        public boolean postProcess(World world, Random random, StructureBox bounds) {
            if (this.hpos < 0) {
                this.hpos = this.updateHeight(world, bounds);
                if (this.hpos < 0) {
                    return true;
                }

                this.bounds.move(0, this.hpos - this.bounds.maxY + 4 - 1, 0);
            }

            this.fillWithOutline(world, bounds, 0, 0, 0, 2, 3, 1, 0, 0, false);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 0, 0, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 1, 0, bounds);
            this.setBlockWithMetadata(world, Block.FENCE.id, 0, 1, 2, 0, bounds);
            this.setBlockWithMetadata(world, Block.WOOL.id, 15, 1, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 0, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 1, 3, 1, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 2, 3, 0, bounds);
            this.setBlockWithMetadata(world, Block.TORCH.id, 0, 1, 3, -1, bounds);
            return true;
        }
    }
}