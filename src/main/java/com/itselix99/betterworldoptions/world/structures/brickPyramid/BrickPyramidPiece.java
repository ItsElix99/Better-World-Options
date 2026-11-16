package com.itselix99.betterworldoptions.world.structures.brickPyramid;

import com.itselix99.betterworldoptions.api.structure.StructureBox;
import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class BrickPyramidPiece extends StructurePiece {
    private final int centerX;
    private final int centerZ;

    public BrickPyramidPiece(int generationDepth, int facing, int x, int z) {
        super(generationDepth);
        this.facing = facing;
        this.centerX = x;
        this.centerZ = z;
        this.bounds = new StructureBox(x - 127, 0, z - 127, x + 127, 127, z + 127);
    }

    public boolean postProcess(World world, Random random, StructureBox bounds) {
        int minX = Math.max(this.bounds.minX, bounds.minX);
        int maxX = Math.min(this.bounds.maxX, bounds.maxX);

        int minZ = Math.max(this.bounds.minZ, bounds.minZ);
        int maxZ = Math.min(this.bounds.maxZ, bounds.maxZ);

        int minY = Math.max(this.bounds.minY, bounds.minY);
        int maxY = Math.min(this.bounds.maxY, bounds.maxY);

        for (int worldX = minX; worldX <= maxX; ++worldX) {
            for (int worldZ = minZ; worldZ <= maxZ; ++worldZ) {
                int distanceX = Math.abs(worldX - this.centerX);
                int distanceZ = Math.abs(worldZ - this.centerZ);

                int distance = Math.max(distanceX, distanceZ);
                int pyramidHeight = 127 - distance;

                for (int worldY = minY; worldY <= maxY; ++worldY) {
                    int blockId = world.getBlockId(worldX, worldY, worldZ);

                    if (worldY <= pyramidHeight && (blockId == 0 || blockId == ((BWOWorld) world).bwo_getTheme().getSurfaceLiquidBlock() || blockId == ((BWOWorld) world).bwo_getTheme().getLiquidBlock() || blockId == ((BWOWorld) world).bwo_getTheme().getFlowingLiquidBlock())) {
                        this.setBlockWithMetadata(world, Block.BRICKS.id, 0, worldX - this.bounds.minX, worldY - this.bounds.minY, worldZ - this.bounds.minZ, bounds);
                    }
                }
            }
        }

        return true;
    }
}