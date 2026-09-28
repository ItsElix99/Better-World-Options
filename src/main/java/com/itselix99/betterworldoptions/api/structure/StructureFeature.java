package com.itselix99.betterworldoptions.api.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class StructureFeature extends Generator {
    protected HashMap<Long, StructureStart> structures = new HashMap<>();
    protected World world;

    public void place(ChunkSource source, World world, int chunkX, int chunkZ, byte[] blocks) {
        int var1 = this.range;
        this.world = world;
        this.random.setSeed(world.getSeed());
        long var2 = this.random.nextLong();
        long var3 = this.random.nextLong();

        for(int var4 = chunkX - var1; var4 <= chunkX + var1; ++var4) {
            for(int var5 = chunkZ - var1; var5 <= chunkZ + var1; ++var5) {
                long var6 = (long)var4 * var2;
                long var7 = (long)var5 * var3;
                this.random.setSeed(var6 ^ var7 ^ world.getSeed());
                this.place(world, var4, var5, chunkX, chunkZ, blocks);
            }
        }
    }

    protected void place(World world, int startChunkX, int startChunkZ, int chunkX, int chunkZ, byte[] blocks) {
        if (!this.structures.containsKey(ChunkPos.toLong(startChunkX, startChunkZ))) {
            this.random.nextInt();
            if (this.isFeatureChunk(startChunkX, startChunkZ)) {
                StructureStart structureStart = this.createStructure(startChunkX, startChunkZ);
                this.structures.put(ChunkPos.toLong(startChunkX, startChunkZ), structureStart);
            }

        }
    }

    public boolean place(World world, Random random, int chunkX, int chunkZ) {
        int var1 = (chunkX << 4) + 8;
        int var2 = (chunkZ << 4) + 8;
        boolean var3 = false;

        for(StructureStart structureStart : this.structures.values()) {
            if (structureStart.isValid() && structureStart.getBounds().intersects(var1, var2, var1 + 15, var2 + 15)) {
                structureStart.postProcess(world, random, new StructureBox(var1, var2, var1 + 15, var2 + 15));
                var3 = true;
            }
        }

        return var3;
    }

    public boolean isInside(int x, int y, int z) {
        for(StructureStart structureStart : this.structures.values()) {
            if (structureStart.isValid() && structureStart.getBounds().intersects(x, z, x, z)) {
                for(StructurePiece structurePiece : structureStart.getPieces()) {
                    if (structurePiece.getBounds().contains(x, y, z)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public BlockPos findNearestPosition(World world, int x, int y, int z) {
        this.world = world;
        this.random.setSeed(world.getSeed());
        long var1 = this.random.nextLong();
        long var3 = this.random.nextLong();
        long var5 = (long)(x >> 4) * var1;
        long var7 = (long)(z >> 4) * var3;
        this.random.setSeed(var5 ^ var7 ^ world.getSeed());
        this.place(world, x >> 4, z >> 4, 0, 0, null);
        double var9 = Double.MAX_VALUE;
        BlockPos var11 = null;

        for(StructureStart structureStart : this.structures.values()) {
            if (structureStart.isValid()) {
                StructurePiece structurePiece = structureStart.getPieces().get(0);
                BlockPos var12 = structurePiece.getCenterPos();
                int var13 = var12.x - x;
                int var14 = var12.y - y;
                int var15 = var12.z - z;
                double var16 = var13 + var13 * var14 * var14 + var15 * var15;
                if (var16 < var9) {
                    var9 = var16;
                    var11 = var12;
                }
            }
        }

        if (var11 != null) {
            return var11;
        } else {
            List<BlockPos> list = this.getPotentialPositions();
            if (list != null) {
                BlockPos var18 = null;

                for(BlockPos var19 : list) {
                    int var20 = var19.x - x;
                    int var21 = var19.y - y;
                    int var22 = var19.z - z;
                    double var23 = var20 + var20 * var21 * var21 + var22 * var22;
                    if (var23 < var9) {
                        var9 = var23;
                        var18 = var19;
                    }
                }

                return var18;
            } else {
                return null;
            }
        }
    }

    protected List<BlockPos> getPotentialPositions() {
        return null;
    }

    protected abstract boolean isFeatureChunk(int chunkX, int chunkZ);

    protected abstract StructureStart createStructure(int x, int z);
}