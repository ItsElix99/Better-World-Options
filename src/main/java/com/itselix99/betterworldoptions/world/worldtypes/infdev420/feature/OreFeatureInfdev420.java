package com.itselix99.betterworldoptions.world.worldtypes.infdev420.feature;

import com.itselix99.betterworldoptions.world.worldtypes.infdev415.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public final class OreFeatureInfdev420 extends Feature {
    private final int oreBlockId;

    public OreFeatureInfdev420(int oreBlockId) {
        this.oreBlockId = oreBlockId;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        float var6 = random.nextFloat() * (float) Math.PI;
        double var7 = (float) (x + 8) + MathHelper.sin(var6) * 2.0F;
        double var9 = (float) (x + 8) - MathHelper.sin(var6) * 2.0F;
        double var11 = (float) (z + 8) + MathHelper.cos(var6) * 2.0F;
        double var13 = (float) (z + 8) - MathHelper.cos(var6) * 2.0F;
        double var15 = y + random.nextInt(3) + 2;
        double var17 = y + random.nextInt(3) + 2;

        for (x = 0; x <= 16; ++x) {
            double var20 = var7 + (var9 - var7) * (double) x / 16.0D;
            double var22 = var15 + (var17 - var15) * (double) x / 16.0D;
            double var24 = var11 + (var13 - var11) * (double) x / 16.0D;
            double var26 = random.nextDouble();
            double var28 = (double) (MathHelper.sin((float) x / 16.0F * (float) Math.PI) + 1.0F) * var26 + 1.0D;
            double var30 = (double) (MathHelper.sin((float) x / 16.0F * (float) Math.PI) + 1.0F) * var26 + 1.0D;

            for (y = (int) (var20 - var28 / 2.0D); y <= (int) (var20 + var28 / 2.0D); ++y) {
                for (z = (int) (var22 - var30 / 2.0D); z <= (int) (var22 + var30 / 2.0D); ++z) {
                    for (int var41 = (int) (var24 - var28 / 2.0D); var41 <= (int) (var24 + var28 / 2.0D); ++var41) {
                        double var35 = ((double) y + 0.5D - var20) / (var28 / 2.0D);
                        double var37 = ((double) z + 0.5D - var22) / (var30 / 2.0D);
                        double var39 = ((double) var41 + 0.5D - var24) / (var28 / 2.0D);
                        if (var35 * var35 + var37 * var37 + var39 * var39 < 1.0D && world.getBlockId(y, z, var41) == Block.STONE.id) {
                            world.setBlockWithoutNotifyingNeighbors(y, z, var41, this.oreBlockId);
                        }
                    }
                }
            }
        }
        return true;
    }
}