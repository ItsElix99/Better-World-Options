package com.itselix99.betterworldoptions.world.worldtypes.infdev420.feature;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public final class LargeOakTreeFeatureInfdev420 extends Feature {
    private static final byte[] MINOR_AXES = new byte[]{(byte) 2, (byte) 0, (byte) 0, (byte) 1, (byte) 2, (byte) 1};
    private final Random random = new Random();
    private World world;
    private final int[] origin = new int[]{0, 0, 0};
    private int height = 0;
    private int trunkHeight;
    private final double trunkScale = 0.618D;
    private final double branchSlope = 0.381D;
    private double branchLengthScale = 1.0D;
    private double foliageDensity = 1.0D;
    private final int trunkWidth = 1;
    private int maxTrunkHeight = 12;
    private int foliageClusterHeight = 4;
    private int[][] branches;

    private void placeBranch(int[] from, int[] to, int log) {
        int[] var15 = new int[]{0, 0, 0};
        byte var4 = 0;

        byte var5;
        for (var5 = 0; var4 < 3; ++var4) {
            var15[var4] = to[var4] - from[var4];
            if (Math.abs(var15[var4]) > Math.abs(var15[var5])) {
                var5 = var4;
            }
        }

        if (var15[var5] != 0) {
            byte var14 = MINOR_AXES[var5];
            var4 = MINOR_AXES[var5 + 3];
            byte var6;
            if (var15[var5] > 0) {
                var6 = 1;
            } else {
                var6 = -1;
            }

            double var10 = (double) var15[var14] / (double) var15[var5];
            double var12 = (double) var15[var4] / (double) var15[var5];
            int[] var7 = new int[]{0, 0, 0};
            int var8 = 0;

            for (log = var15[var5] + var6; var8 != log; var8 += var6) {
                var7[var5] = MathHelper.floor_double((double) (from[var5] + var8) + 0.5D);
                var7[var14] = MathHelper.floor_double((double) from[var14] + (double) var8 * var10 + 0.5D);
                var7[var4] = MathHelper.floor_double((double) from[var4] + (double) var8 * var12 + 0.5D);
                this.world.setBlockWithoutNotifyingNeighbors(var7[0], var7[1], var7[2], 17);
            }

        }
    }

    private int tryBranch(int[] from, int[] to) {
        int[] var3 = new int[]{0, 0, 0};
        byte var4 = 0;

        byte var5;
        for (var5 = 0; var4 < 3; ++var4) {
            var3[var4] = to[var4] - from[var4];
            if (Math.abs(var3[var4]) > Math.abs(var3[var5])) {
                var5 = var4;
            }
        }

        if (var3[var5] == 0) {
            return -1;
        } else {
            byte var14 = MINOR_AXES[var5];
            var4 = MINOR_AXES[var5 + 3];
            byte var6;
            if (var3[var5] > 0) {
                var6 = 1;
            } else {
                var6 = -1;
            }

            double var9 = (double) var3[var14] / (double) var3[var5];
            double var11 = (double) var3[var4] / (double) var3[var5];
            int[] var7 = new int[]{0, 0, 0};
            int var8 = 0;

            int var15;
            for (var15 = var3[var5] + var6; var8 != var15; var8 += var6) {
                var7[var5] = from[var5] + var8;
                var7[var14] = (int) ((double) from[var14] + (double) var8 * var9);
                var7[var4] = (int) ((double) from[var4] + (double) var8 * var11);
                int var13 = this.world.getBlockId(var7[0], var7[1], var7[2]);
                if (var13 != 0 && var13 != BetterWorldOptions.ALPHA_LEAVES.id) {
                    break;
                }
            }

            return var8 == var15 ? -1 : Math.abs(var8);
        }
    }

    public void prepare(double d0, double d1, double d2) {
        this.maxTrunkHeight = 12;
        this.foliageClusterHeight = 5;
        this.branchLengthScale = 1.0D;
        this.foliageDensity = 1.0D;
    }

    public boolean generate(World var1, Random var2, int x, int y, int z) {
        this.world = var1;
        long var6 = var2.nextLong();
        this.random.setSeed(var6);
        this.origin[0] = x;
        this.origin[1] = y;
        this.origin[2] = z;
        if (this.height == 0) {
            this.height = 5 + this.random.nextInt(this.maxTrunkHeight);
        }

        int[] var39 = new int[]{this.origin[0], this.origin[1], this.origin[2]};
        int[] var41 = new int[]{this.origin[0], this.origin[1] + this.height - 1, this.origin[2]};
        y = this.world.getBlockId(this.origin[0], this.origin[1] - 1, this.origin[2]);
        boolean var10000;
        if (y != Block.GRASS_BLOCK.id && y != 3) {
            var10000 = false;
        } else {
            z = this.tryBranch(var39, var41);
            if (z == -1) {
                var10000 = true;
            } else if (z < 6) {
                var10000 = false;
            } else {
                this.height = z;
                var10000 = true;
            }
        }

        if (!var10000) {
            return false;
        } else {
            LargeOakTreeFeatureInfdev420 var38 = this;
            this.trunkHeight = (int) ((double) this.height * this.trunkScale);
            if (this.trunkHeight >= this.height) {
                this.trunkHeight = this.height - 1;
            }

            int var40 = (int) (1.382D + Math.pow(this.foliageDensity * (double) this.height / 13.0D, 2.0D));
            if (var40 <= 0) {
                var40 = 1;
            }

            int[][] var42 = new int[var40 * this.height][4];
            y = this.origin[1] + this.height - this.foliageClusterHeight;
            z = 1;
            int var43 = this.origin[1] + this.trunkHeight;
            int var7 = y - this.origin[1];
            var42[0][0] = this.origin[0];
            var42[0][1] = y;
            var42[0][2] = this.origin[2];
            var42[0][3] = var43;
            --y;

            int var8;
            int var11;
            while (var7 >= 0) {
                var8 = 0;
                float var61;
                if ((double) var7 < (double) ((float) var38.height) * 0.3D) {
                    var61 = -1.618F;
                } else {
                    float var13 = (float) var38.height / 2.0F;
                    float var14 = (float) var38.height / 2.0F - (float) var7;
                    float var36;
                    if (var14 == 0.0F) {
                        var36 = var13;
                    } else if (Math.abs(var14) >= var13) {
                        var36 = 0.0F;
                    } else {
                        var36 = (float) Math.sqrt(Math.pow(Math.abs(var13), 2.0D) - Math.pow(Math.abs(var14), 2.0D));
                    }

                    var36 *= 0.5F;
                    var61 = var36;
                }

                float var9 = var61;
                if (var9 < 0.0F) {
                    --y;
                    --var7;
                } else {
                    for (; var8 < var40; ++var8) {
                        double var19 = var38.branchLengthScale * (double) var9 * ((double) var38.random.nextFloat() + 0.328D);
                        double var21 = (double) var38.random.nextFloat() * 2.0D * 3.14159D;
                        int var10 = (int) (var19 * Math.sin(var21) + (double) var38.origin[0] + 0.5D);
                        var11 = (int) (var19 * Math.cos(var21) + (double) var38.origin[2] + 0.5D);
                        int[] var12 = new int[]{var10, y, var11};
                        int[] var52 = new int[]{var10, y + var38.foliageClusterHeight, var11};
                        if (var38.tryBranch(var12, var52) == -1) {
                            var52 = new int[]{var38.origin[0], var38.origin[1], var38.origin[2]};
                            double var28 = Math.sqrt(Math.pow(Math.abs(var38.origin[0] - var12[0]), 2.0D) + Math.pow(Math.abs(var38.origin[2] - var12[2]), 2.0D));
                            double var30 = var28 * var38.branchSlope;
                            if ((double) var12[1] - var30 > (double) var43) {
                                var52[1] = var43;
                            } else {
                                var52[1] = (int) ((double) var12[1] - var30);
                            }

                            if (var38.tryBranch(var52, var12) == -1) {
                                var42[z][0] = var10;
                                var42[z][1] = y;
                                var42[z][2] = var11;
                                var42[z][3] = var52[1];
                                ++z;
                            }
                        }
                    }

                    --y;
                    --var7;
                }
            }

            var38.branches = new int[z][4];
            System.arraycopy(var42, 0, var38.branches, 0, z);
            var38 = this;
            var40 = 0;

            for (x = this.branches.length; var40 < x; ++var40) {
                y = var38.branches[var40][0];
                z = var38.branches[var40][1];
                var43 = var38.branches[var40][2];
                int var10001 = y;
                y = var43;
                int var49 = z;
                var8 = var10001;
                LargeOakTreeFeatureInfdev420 var47 = var38;
                z = z;

                for (int var56 = var49 + var38.foliageClusterHeight; z < var56; ++z) {
                    int var22 = z - var49;
                    float var20 = var22 >= 0 && var22 < var47.foliageClusterHeight ? (var22 != 0 && var22 != var47.foliageClusterHeight - 1 ? 3.0F : 2.0F) : -1.0F;
                    boolean var53 = true;
                    var53 = true;
                    float var51 = var20;
                    LargeOakTreeFeatureInfdev420 var57 = var47;
                    int var58 = (int) ((double) var20 + 0.618D);
                    byte var29 = MINOR_AXES[1];
                    byte var59 = MINOR_AXES[4];
                    int[] var31 = new int[]{var8, z, y};
                    int[] var50 = new int[]{0, 0, 0};
                    var11 = -var58;

                    label134:
                    for (var50[1] = var31[1]; var11 <= var58; ++var11) {
                        var50[var29] = var31[var29] + var11;
                        int var55 = -var58;

                        while (true) {
                            while (true) {
                                if (var55 > var58) {
                                    continue label134;
                                }

                                double var60 = Math.sqrt(Math.pow((double) Math.abs(var11) + 0.5D, 2.0D) + Math.pow((double) Math.abs(var55) + 0.5D, 2.0D));
                                if (var60 > (double) var51) {
                                    ++var55;
                                } else {
                                    var50[var59] = var31[var59] + var55;
                                    int var54 = var57.world.getBlockId(var50[0], var50[1], var50[2]);
                                    if (var54 != 0 && var54 != 18) {
                                        ++var55;
                                    } else {
                                        var57.world.setBlockWithoutNotifyingNeighbors(var50[0], var50[1], var50[2], BetterWorldOptions.ALPHA_LEAVES.id);
                                        ++var55;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var40 = this.origin[0];
            x = this.origin[1];
            y = this.origin[1] + this.trunkHeight;
            z = this.origin[2];
            int[] var44 = new int[]{var40, x, z};
            int[] var48 = new int[]{var40, y, z};
            this.placeBranch(var44, var48, 17);
            if (this.trunkWidth == 2) {
                ++var44[0];
                ++var48[0];
                this.placeBranch(var44, var48, 17);
                ++var44[2];
                ++var48[2];
                this.placeBranch(var44, var48, 17);
                var44[0] += -1;
                var48[0] += -1;
                this.placeBranch(var44, var48, 17);
            }

            var38 = this;
            var40 = 0;
            x = this.branches.length;

            for (int[] var45 = new int[]{this.origin[0], this.origin[1], this.origin[2]}; var40 < x; ++var40) {
                int[] var46 = var38.branches[var40];
                var44 = new int[]{var46[0], var46[1], var46[2]};
                var45[1] = var46[3];
                var7 = var45[1] - var38.origin[1];
                if ((double) var7 >= (double) var38.height * 0.2D) {
                    var38.placeBranch(var45, var44, 17);
                }
            }

            return true;
        }
    }
}