package com.itselix99.betterworldoptions.world.structures.brickPyramid;

import com.itselix99.betterworldoptions.api.structure.StructureFeature;
import com.itselix99.betterworldoptions.api.structure.StructureStart;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.world.World;

import java.util.Random;

public class BrickPyramidStructure extends StructureFeature {
    private int x;
    private int z;

    protected boolean isFeatureChunk(int chunkX, int chunkZ) {
        int regionX = chunkX >> 6;
        int regionZ = chunkZ >> 6;

        Random random1 = ((BWOWorld) this.world).bwo_setRandomSeed(regionX, regionZ, 13871);

        this.x = (regionX << 10) + 128 + random1.nextInt(512);
        this.z = (regionZ << 10) + 128 + random1.nextInt(512);

        int structureChunkX = this.x >> 4;
        int structureChunkZ = this.z >> 4;

        return chunkX == structureChunkX && chunkZ == structureChunkZ;
    }

    protected StructureStart createStructure(int x, int z) {
        return new Start(this.world, this.random, this.x, this.z);
    }

    public static class Start extends StructureStart {
        public Start(World world, Random random, int chunkX, int chunkZ) {
            BrickPyramidPiece piece = new BrickPyramidPiece(0, 0, chunkX, chunkZ);
            this.pieces.add(piece);
            this.findBounds();
        }
    }
}