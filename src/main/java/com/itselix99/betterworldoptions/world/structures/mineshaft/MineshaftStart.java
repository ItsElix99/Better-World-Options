package com.itselix99.betterworldoptions.world.structures.mineshaft;

import com.itselix99.betterworldoptions.api.structure.StructureStart;
import net.minecraft.world.World;

import java.util.Random;

public class MineshaftStart extends StructureStart {
    public MineshaftStart(World world, Random random, int chunkX, int chunkZ) {
        MineshaftPieces.Room room = new MineshaftPieces.Room(0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
        this.pieces.add(room);
        room.addChildren(room, this.pieces, random);
        this.findBounds();
        this.moveBelowSeaLevel(world, random, 10);
    }
}