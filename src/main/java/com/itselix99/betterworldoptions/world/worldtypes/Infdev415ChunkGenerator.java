package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.world.feature.old.OldLargeOakTreeFeature;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class Infdev415ChunkGenerator extends Infdev327ChunkGenerator {
    public Infdev415ChunkGenerator(World world, long seed) {
        super(world, seed);
    }

    protected int[] generateBeachesAndErosion(int chunkX, int chunkZ, int x, int z, double scale) {
        double worldX = (chunkX << 4) + x;
        double worldZ = (chunkZ << 4) + z;
        int sandBeach = this.perlinNoise2.sampleInverse(worldX * scale, worldZ * scale, 0.0D) + this.random.nextDouble() * 0.2D > 0.0D ? 1 : 0;
        int gravelBeach = this.perlinNoise2.sampleInverse(worldZ * scale, 109.0134D, worldX * scale) + this.random.nextDouble() * 0.2D > 3.0D ? 1 : 0;
        int erosion = (int)(this.perlinNoise3.sampleInverse(worldX * scale * 2.0D, worldZ * scale * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);

        return new int[]{sandBeach, gravelBeach, erosion};
    }

    protected Feature getOldTreeFeature() {
        return new OldLargeOakTreeFeature();
    }

    protected void generateTrees(int x, int z, int treeCount, Feature tree) {
        for(int var5 = 0; var5 < treeCount; ++var5) {
            int var6 = x + this.random.nextInt(16) + 8;
            int var7 = z + this.random.nextInt(16) + 8;
            tree.prepare(1.0D, 1.0D, 1.0D);
            tree.generate(this.world, this.random, var6, this.world.getTopY(var6, var7), var7);
        }
    }
}
