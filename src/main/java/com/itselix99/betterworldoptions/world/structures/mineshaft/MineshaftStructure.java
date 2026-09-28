package com.itselix99.betterworldoptions.world.structures.mineshaft;

import com.itselix99.betterworldoptions.api.structure.StructureFeature;
import com.itselix99.betterworldoptions.api.structure.StructureStart;

public class MineshaftStructure extends StructureFeature {
    protected boolean isFeatureChunk(int chunkX, int chunkZ) {
        return this.random.nextInt(100) == 0 && this.random.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
    }

    protected StructureStart createStructure(int x, int z) {
        return new MineshaftStart(this.world, this.random, x, z);
    }
}