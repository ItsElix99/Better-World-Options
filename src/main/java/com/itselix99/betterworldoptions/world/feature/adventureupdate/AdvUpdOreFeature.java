package com.itselix99.betterworldoptions.world.feature.adventureupdate;

import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class AdvUpdOreFeature extends Feature {
    private final int oreBlockId;
    private final int oreCount;

    public AdvUpdOreFeature(int oreBlockId, int oreCount) {
        this.oreBlockId = oreBlockId;
        this.oreCount = oreCount;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        float var6 = random.nextFloat() * (float)Math.PI;
        double var7 = (float)(x + 8) + MathHelper.sin(var6) * (float)this.oreCount / 8.0F;
        double var9 = (float)(x + 8) - MathHelper.sin(var6) * (float)this.oreCount / 8.0F;
        double var11 = (float)(z + 8) + MathHelper.cos(var6) * (float)this.oreCount / 8.0F;
        double var13 = (float)(z + 8) - MathHelper.cos(var6) * (float)this.oreCount / 8.0F;
        double var15 = y + random.nextInt(3) - 2;
        double var17 = y + random.nextInt(3) - 2;

        for(int var19 = 0; var19 <= this.oreCount; ++var19) {
            double var20 = var7 + (var9 - var7) * (double)var19 / (double)this.oreCount;
            double var22 = var15 + (var17 - var15) * (double)var19 / (double)this.oreCount;
            double var24 = var11 + (var13 - var11) * (double)var19 / (double)this.oreCount;
            double var26 = random.nextDouble() * (double)this.oreCount / 16.0D;
            double var28 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)this.oreCount) + 1.0F) * var26 + 1.0D;
            double var30 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)this.oreCount) + 1.0F) * var26 + 1.0D;
            int var32 = MathHelper.floor(var20 - var28 / 2.0D);
            int var33 = MathHelper.floor(var22 - var30 / 2.0D);
            int var34 = MathHelper.floor(var24 - var28 / 2.0D);
            int var35 = MathHelper.floor(var20 + var28 / 2.0D);
            int var36 = MathHelper.floor(var22 + var30 / 2.0D);
            int var37 = MathHelper.floor(var24 + var28 / 2.0D);

            for(int var38 = var32; var38 <= var35; ++var38) {
                double var39 = ((double)var38 + 0.5D - var20) / (var28 / 2.0D);
                if(var39 * var39 < 1.0D) {
                    for(int var41 = var33; var41 <= var36; ++var41) {
                        double var42 = ((double)var41 + 0.5D - var22) / (var30 / 2.0D);
                        if(var39 * var39 + var42 * var42 < 1.0D) {
                            for(int var44 = var34; var44 <= var37; ++var44) {
                                double var45 = ((double)var44 + 0.5D - var24) / (var28 / 2.0D);
                                if(var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && world.getBlockId(var38, var41, var44) == Block.STONE.id) {
                                    world.setBlockWithoutNotifyingNeighbors(var38, var41, var44, this.oreBlockId);
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
