package com.itselix99.betterworldoptions.api.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import net.minecraft.world.World;

public abstract class StructureStart {
    protected LinkedList<StructurePiece> pieces = new LinkedList<>();
    protected StructureBox bounds;

    protected StructureStart() {
    }

    public StructureBox getBounds() {
        return this.bounds;
    }

    public LinkedList<StructurePiece> getPieces() {
        return this.pieces;
    }

    public void postProcess(World world, Random random, StructureBox chunkBounds) {
        Iterator<StructurePiece> iterator = this.pieces.iterator();

        while (iterator.hasNext()) {
            StructurePiece structurePiece = iterator.next();
            if (structurePiece.getBounds().intersects(chunkBounds) && !structurePiece.postProcess(world, random, chunkBounds)) {
                iterator.remove();
            }
        }

    }

    protected void findBounds() {
        this.bounds = StructureBox.infinite();

        for (StructurePiece structurePiece : this.pieces) {
            this.bounds.union(structurePiece.getBounds());
        }

    }

    protected void moveBelowSeaLevel(World world, Random random, int amount) {
        int var1 = 64 - amount;
        int var2 = this.bounds.getSizeY() + 1;
        if (var2 < var1) {
            var2 += random.nextInt(var1 - var2);
        }

        int var3 = var2 - this.bounds.maxY;
        this.bounds.move(0, var3, 0);

        for (StructurePiece structurePiece : this.pieces) {
            structurePiece.getBounds().move(0, var3, 0);
        }

    }

    protected void moveBetweenYCoords(World world, Random random, int minY, int maxY) {
        int var1 = maxY - minY + 1 - this.bounds.getSizeY();
        int var2;
        if (var1 > 1) {
            var2 = minY + random.nextInt(var1);
        } else {
            var2 = minY;
        }

        int var3 = var2 - this.bounds.minY;
        this.bounds.move(0, var3, 0);

        for (StructurePiece structurePiece : this.pieces) {
            structurePiece.getBounds().move(0, var3, 0);
        }

    }

    public boolean isValid() {
        return true;
    }
}