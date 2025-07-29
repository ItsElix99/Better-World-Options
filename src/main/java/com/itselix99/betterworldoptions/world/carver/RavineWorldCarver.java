package com.itselix99.betterworldoptions.world.carver;

import com.itselix99.betterworldoptions.BWOConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Generator;

import java.util.Random;

public class RavineWorldCarver extends Generator {
    private final float[] buffer = new float[1024];

    protected void carveRavine(long seed, int chunkX, int chunkZ, byte[] blocks, double x5, double y6, double z7, float baseWidth, float yaw, float pitch, int tunnel, int tunnelCount, double widthHeightRatio) {
        Random random = new Random(seed);
        double var1 = (double)(chunkX * 16 + 8);
        double var2 = (double)(chunkZ * 16 + 8);
        float var3 = 0.0F;
        float var4 = 0.0F;
        if (tunnelCount <= 0) {
            int var5 = this.range * 16 - 16;
            tunnelCount = var5 - random.nextInt(var5 / 4);
        }

        int var6 = 0;
        if (tunnel == -1) {
            tunnel = tunnelCount / 2;
            var6 = 1;
        }

        float var7 = 1.0F;
        int var8 = 0;

        while(true) {
            if (var8 >= BWOConfig.WORLD_CONFIG.worldHeightLimit) {
                for(; tunnel < tunnelCount; ++tunnel) {
                    double var9 = (double)1.5F + (double)(MathHelper.sin((float)tunnel * (float)Math.PI / (float)tunnelCount) * baseWidth * 1.0F);
                    double var10 = var9 * widthHeightRatio;
                    var9 *= (double)random.nextFloat() * (double)0.25F + (double)0.75F;
                    var10 *= (double)random.nextFloat() * (double)0.25F + (double)0.75F;
                    float var11 = MathHelper.cos(pitch);
                    float var12 = MathHelper.sin(pitch);
                    x5 += (double)(MathHelper.cos(yaw) * var11);
                    y6 += (double)var12;
                    z7 += (double)(MathHelper.sin(yaw) * var11);
                    pitch *= 0.7F;
                    pitch += var4 * 0.05F;
                    yaw += var3 * 0.05F;
                    var4 *= 0.8F;
                    var3 *= 0.5F;
                    var4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
                    var3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
                    if (var6 != 0 || random.nextInt(4) != 0) {
                        double var13 = x5 - var1;
                        double var14 = z7 - var2;
                        double var15 = (double)(tunnelCount - tunnel);
                        double var16 = (double)(baseWidth + 2.0F + 16.0F);
                        if (var13 * var13 + var14 * var14 - var15 * var15 > var16 * var16) {
                            return;
                        }

                        if (!(x5 < var1 - (double)16.0F - var9 * (double)2.0F) && !(z7 < var2 - (double)16.0F - var9 * (double)2.0F) && !(x5 > var1 + (double)16.0F + var9 * (double)2.0F) && !(z7 > var2 + (double)16.0F + var9 * (double)2.0F)) {
                            int var17 = MathHelper.floor(x5 - var9) - chunkX * 16 - 1;
                            int var18 = MathHelper.floor(x5 + var9) - chunkX * 16 + 1;
                            int var19 = MathHelper.floor(y6 - var10) - 1;
                            int var20 = MathHelper.floor(y6 + var10) + 1;
                            int var21 = MathHelper.floor(z7 - var9) - chunkZ * 16 - 1;
                            int var22 = MathHelper.floor(z7 + var9) - chunkZ * 16 + 1;
                            if (var17 < 0) {
                                var17 = 0;
                            }

                            if (var18 > 16) {
                                var18 = 16;
                            }

                            if (var19 < 1) {
                                var19 = 1;
                            }

                            if (var20 > BWOConfig.WORLD_CONFIG.worldHeightLimit - 8) {
                                var20 = BWOConfig.WORLD_CONFIG.worldHeightLimit - 8;
                            }

                            if (var21 < 0) {
                                var21 = 0;
                            }

                            if (var22 > 16) {
                                var22 = 16;
                            }

                            int var23 = 0;

                            for(int var24 = var17; var23 == 0 && var24 < var18; ++var24) {
                                for(int var25 = var21; var23 == 0 && var25 < var22; ++var25) {
                                    for(int var26 = var20 + 1; var23 == 0 && var26 >= var19 - 1; --var26) {
                                        int var10000 = var24 * 16 + var25;
                                        int var27 = var10000 * BWOConfig.WORLD_CONFIG.worldHeightLimit + var26;
                                        if (var26 >= 0) {
                                            if (var26 < BWOConfig.WORLD_CONFIG.worldHeightLimit) {
                                                if (blocks[var27] == Block.FLOWING_WATER.id || blocks[var27] == Block.WATER.id) {
                                                    var23 = 1;
                                                }

                                                if (var26 != var19 - 1 && var24 != var17 && var24 != var18 - 1 && var25 != var21 && var25 != var22 - 1) {
                                                    var26 = var19;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (var23 == 0) {
                                for(int var28 = var17; var28 < var18; ++var28) {
                                    double var29 = ((double)(var28 + chunkX * 16) + (double)0.5F - x5) / var9;

                                    for(int var30 = var21; var30 < var22; ++var30) {
                                        double var31 = ((double)(var30 + chunkZ * 16) + (double)0.5F - z7) / var9;
                                        int var32 = var28 * 16 + var30;
                                        int var33 = var32 * BWOConfig.WORLD_CONFIG.worldHeightLimit + var20;
                                        int var34 = 0;
                                        if (var29 * var29 + var31 * var31 < (double)1.0F) {
                                            for(int var35 = var20 - 1; var35 >= var19; --var35) {
                                                double var36 = ((double)var35 + (double)0.5F - y6) / var10;
                                                if ((var29 * var29 + var31 * var31) * (double)this.buffer[var35] + var36 * var36 / (double)6.0F < (double)1.0F) {
                                                    int var37 = blocks[var33];
                                                    if (var37 == Block.GRASS_BLOCK.id) {
                                                        var34 = 1;
                                                    }

                                                    if (var37 == Block.STONE.id || var37 == Block.DIRT.id || var37 == Block.GRASS_BLOCK.id) {
                                                        if (var35 < 10) {
                                                            blocks[var33] = (byte)Block.FLOWING_LAVA.id;
                                                        } else {
                                                            blocks[var33] = 0;
                                                            if (var34 != 0 && blocks[var33 - 1] == Block.DIRT.id) {
                                                                blocks[var33 - 1] = (byte)Block.GRASS_BLOCK.id;
                                                            }
                                                        }
                                                    }
                                                }

                                                --var33;
                                            }
                                        }
                                    }
                                }

                                if (var6 != 0) {
                                    break;
                                }
                            }
                        }
                    }
                }

                return;
            }

            if (var8 == 0 || random.nextInt(3) == 0) {
                var7 = 1.0F + random.nextFloat() * random.nextFloat() * 1.0F;
            }

            this.buffer[var8] = var7 * var7;
            ++var8;
        }
    }

    protected void place(World world, int startChunkX, int startChunkZ, int chunkX, int chunkZ, byte[] blocks) {
        if (this.random.nextInt(50) == 0) {
            double var1 = (double)(startChunkX * 16 + this.random.nextInt(16));
            double var2 = (double)(this.random.nextInt(this.random.nextInt(40) + 8) + 20);
            double var3 = (double)(startChunkZ * 16 + this.random.nextInt(16));
            int var4 = 1;

            for(int var5 = 0; var5 < var4; ++var5) {
                float var6 = this.random.nextFloat() * (float)Math.PI * 2.0F;
                float var7 = (this.random.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var8 = (this.random.nextFloat() * 2.0F + this.random.nextFloat()) * 2.0F;
                this.carveRavine(this.random.nextLong(), chunkX, chunkZ, blocks, var1, var2, var3, var8, var6, var7, 0, 0, (double)3.0F);
            }

        }
    }
}