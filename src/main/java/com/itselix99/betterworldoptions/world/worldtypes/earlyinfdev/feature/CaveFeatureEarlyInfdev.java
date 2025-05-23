package com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.feature;

import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class CaveFeatureEarlyInfdev extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        float f31 = random.nextFloat() * (float) Math.PI;
        double d38 = (float) (x + 8) + MathHelper.sin(f31) * 7.0F;
        double e41 = (float) (x + 8) - MathHelper.sin(f31) * 7.0F;
        double f46 = (float) (z + 8) + MathHelper.cos(f31) * 7.0F;
        double g51 = (float) (z + 8) - MathHelper.cos(f31) * 7.0F;
        double h54 = y + random.nextInt(8) + 2;
        double i56 = y + random.nextInt(8) + 2;
        double j57 = random.nextDouble() * (double) 4.0F + (double) 2.0F;
        double k58 = random.nextDouble() * 0.6;
        long l59 = random.nextLong();
        random.setSeed(l59);

        for (int var84 = 0; var84 <= 16; ++var84) {
            double l60 = d38 + (e41 - d38) * (double) var84 / (double) 16.0F;
            double n62 = h54 + (i56 - h54) * (double) var84 / (double) 16.0F;
            double p64 = f46 + (g51 - f46) * (double) var84 / (double) 16.0F;
            double r66 = random.nextDouble();
            double t68 = ((double) MathHelper.sin((float) var84 / 16.0F * (float) Math.PI) * j57 + (double) 1.0F) * r66 + (double) 1.0F;
            double v70 = ((double) MathHelper.sin((float) var84 / 16.0F * (float) Math.PI) * j57 + (double) 1.0F) * r66 + (double) 1.0F;

            for (int c28 = (int) (l60 - t68 / (double) 2.0F); c28 <= (int) (l60 + t68 / (double) 2.0F); ++c28) {
                for (int e30 = (int) (n62 - v70 / (double) 2.0F); e30 <= (int) (n62 + v70 / (double) 2.0F); ++e30) {
                    for (int f32 = (int) (p64 - t68 / (double) 2.0F); f32 <= (int) (p64 + t68 / (double) 2.0F); ++f32) {
                        double x72 = ((double) c28 + (double) 0.5F - l60) / (t68 / (double) 2.0F);
                        double z74 = ((double) e30 + (double) 0.5F - n62) / (v70 / (double) 2.0F);
                        double b76 = ((double) f32 + (double) 0.5F - p64) / (t68 / (double) 2.0F);
                        if (x72 * x72 + z74 * z74 + b76 * b76 < random.nextDouble() * k58 + ((double) 1.0F - k58)) {
                            for (int ii = c28 - 2; ii <= c28 + 1; ++ii) {
                                for (int ij = e30 - 1; ij <= e30 + 1; ++ij) {
                                    for (int ik = f32 - 1; ik <= f32 + 1; ++ik) {
                                        if (world.getMaterial(ii, ij, ik).isFluid()) {
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

        random.setSeed(l59);

        for (int var85 = 0; var85 <= 16; ++var85) {
            double m61 = d38 + (e41 - d38) * (double) var85 / (double) 16.0F;
            double o63 = h54 + (i56 - h54) * (double) var85 / (double) 16.0F;
            double q65 = f46 + (g51 - f46) * (double) var85 / (double) 16.0F;
            double s67 = random.nextDouble();
            double u69 = ((double) MathHelper.sin((float) var85 / 16.0F * (float) Math.PI) * j57 + (double) 1.0F) * s67 + (double) 1.0F;
            double w71 = ((double) MathHelper.sin((float) var85 / 16.0F * (float) Math.PI) * j57 + (double) 1.0F) * s67 + (double) 1.0F;

            for (int var88 = (int) (m61 - u69 / (double) 2.0F); var88 <= (int) (m61 + u69 / (double) 2.0F); ++var88) {
                for (int var91 = (int) (o63 - w71 / (double) 2.0F); var91 <= (int) (o63 + w71 / (double) 2.0F); ++var91) {
                    for (int g33 = (int) (q65 - u69 / (double) 2.0F); g33 <= (int) (q65 + u69 / (double) 2.0F); ++g33) {
                        double y73 = ((double) var88 + (double) 0.5F - m61) / (u69 / (double) 2.0F);
                        double a75 = ((double) var91 + (double) 0.5F - o63) / (w71 / (double) 2.0F);
                        double c77 = ((double) g33 + (double) 0.5F - q65) / (u69 / (double) 2.0F);
                        if (y73 * y73 + a75 * a75 + c77 * c77 < random.nextDouble() * k58 + ((double) 1.0F - k58) && world.getBlockId(var88, var91, g33) != 0) {
                            world.setBlock(var88, var91, g33, 0);
                        }
                    }
                }
            }
        }
        return true;
    }
}