package com.itselix99.betterworldoptions.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.Feature;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFeature extends Feature {
    private final Map<BlockPos, Integer> blockIdPos = new HashMap<>();

    protected void setBlock(int x, int y, int z, int blockId) {
        this.blockIdPos.put(new BlockPos(x, y, z), blockId);
    }

    protected int getBlockId(int x, int y, int z) {
        if (this.blockIdPos.containsKey(new BlockPos(x, y, z))) {
            int blockId = this.blockIdPos.get(new BlockPos(x, y, z));
            this.blockIdPos.remove(new BlockPos(x, y, z));
            return blockId;
        } else {
            return -1;
        }
    }
}