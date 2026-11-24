package com.itselix99.betterworldoptions.world.feature;

import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class InfdevCavesFeature extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        float var6 = random.nextFloat() * (float) Math.PI;
        double var7 = (float) (x + 8) + MathHelper.sin(var6) * 7.0F;
        double var9 = (float) (x + 8) - MathHelper.sin(var6) * 7.0F;
        double var11 = (float) (z + 8) + MathHelper.cos(var6) * 7.0F;
        double var133 = (float) (z + 8) - MathHelper.cos(var6) * 7.0F;
        double var15 = y + random.nextInt(8) + 2;
        double var17 = y + random.nextInt(8) + 2;
        double var19 = random.nextDouble() * 4.0D + 2.0D;
        double var21 = random.nextDouble() * 0.6D;
        long var23 = random.nextLong();
        random.setSeed(var23);

        for (int var25 = 0; var25 <= 16; ++var25) {
            double var26 = var7 + (var9 - var7) * (double) var25 / 16.0D;
            double var28 = var15 + (var17 - var15) * (double) var25 / 16.0D;
            double var30 = var11 + (var133 - var11) * (double) var25 / 16.0D;
            double var32 = random.nextDouble();
            double var34 = ((double) MathHelper.sin((float) var25 / 16.0F * (float) Math.PI) * var19 + 1.0D) * var32 + 1.0D;
            double var36 = ((double) MathHelper.sin((float) var25 / 16.0F * (float) Math.PI) * var19 + 1.0D) * var32 + 1.0D;

            for (int var38 = (int) (var26 - var34 / 2.0D); var38 <= (int) (var26 + var34 / 2.0D); ++var38) {
                for (int var39 = (int) (var28 - var36 / 2.0D); var39 <= (int) (var28 + var36 / 2.0D); ++var39) {
                    for (int var40 = (int) (var30 - var34 / 2.0D); var40 <= (int) (var30 + var34 / 2.0D); ++var40) {
                        double var41 = ((double) var38 + 0.5D - var26) / (var34 / 2.0D);
                        double var43 = ((double) var39 + 0.5D - var28) / (var36 / 2.0D);
                        double var45 = ((double) var40 + 0.5D - var30) / (var34 / 2.0D);
                        if (var41 * var41 + var43 * var43 + var45 * var45 < random.nextDouble() * var21 + (1.0D - var21)) {
                            for (int var47 = var38 - 2; var47 <= var38 + 1; ++var47) {
                                for (int var48 = var39 - 1; var48 <= var39 + 1; ++var48) {
                                    for (int var49 = var40 - 1; var49 <= var40 + 1; ++var49) {
                                        if (world.getMaterial(var47, var48, var49).isFluid()) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        random.setSeed(var23);

        for (int var50 = 0; var50 <= 16; ++var50) {
            double var51 = var7 + (var9 - var7) * (double) var50 / 16.0D;
            double var53 = var15 + (var17 - var15) * (double) var50 / 16.0D;
            double var55 = var11 + (var133 - var11) * (double) var50 / 16.0D;
            double var57 = random.nextDouble();
            double var59 = ((double) MathHelper.sin((float) var50 / 16.0F * (float) Math.PI) * var19 + 1.0D) * var57 + 1.0D;
            double var61 = ((double) MathHelper.sin((float) var50 / 16.0F * (float) Math.PI) * var19 + 1.0D) * var57 + 1.0D;

            for (int var63 = (int) (var51 - var59 / 2.0D); var63 <= (int) (var51 + var59 / 2.0D); ++var63) {
                for (int var64 = (int) (var53 - var61 / 2.0D); var64 <= (int) (var53 + var61 / 2.0D); ++var64) {
                    for (int var65 = (int) (var55 - var59 / 2.0D); var65 <= (int) (var55 + var59 / 2.0D); ++var65) {
                        double var66 = ((double) var63 + 0.5D - var51) / (var59 / 2.0D);
                        double var68 = ((double) var64 + 0.5D - var53) / (var61 / 2.0D);
                        double var70 = ((double) var65 + 0.5D - var55) / (var59 / 2.0D);
                        if (var66 * var66 + var68 * var68 + var70 * var70 < random.nextDouble() * var21 + (1.0D - var21)) {
                            int blockId = world.getBlockId(var63, var64, var65);
                            boolean isGrassBlock = blockId == Block.GRASS_BLOCK.id;

                            if(blockId == Block.STONE.id || blockId == Block.DIRT.id || blockId == Block.GRASS_BLOCK.id) {
                                world.setBlockWithoutNotifyingNeighbors(var63, var64, var65, 0);

                                if (isGrassBlock && world.getBlockId(var63, var64 - 1, var65) == Block.DIRT.id) {
                                    world.setBlockWithoutNotifyingNeighbors(var63, var64 - 1, var65, Block.GRASS_BLOCK.id);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}