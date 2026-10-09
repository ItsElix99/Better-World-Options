package com.itselix99.betterworldoptions.world.carver;

import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Generator;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;

import java.util.Random;

public class RavineWorldCarver extends Generator implements CaveGenBaseImpl {
    private final float[] buffer = new float[1024];
    private World world;

    public void stationapi_setWorld(World world) {
        this.world = world;
    }

    public World stationapi_getWorld() {
        if (this.world == null) {
            throw new RuntimeException("stationapi_world is null, use CaveGenBaseImpl.stationapi_setWorld in your custom ChunkSource constructor to fix.");
        }
        return this.world;
    }

    protected void carveRavine(long seed, int chunkX, int chunkZ, byte[] blocks, double x, double y, double z, float baseWidth, float yaw, float pitch, int tunnel, int tunnelCount, double widthHeightRatio) {
        Random var19 = new Random(seed);
        double var20 = chunkX * 16 + 8;
        double var22 = chunkZ * 16 + 8;
        float var24 = 0.0F;
        float var25 = 0.0F;
        if(tunnelCount <= 0) {
            int var26 = this.range * 16 - 16;
            tunnelCount = var26 - var19.nextInt(var26 / 4);
        }

        boolean var53 = false;
        if(tunnel == -1) {
            tunnel = tunnelCount / 2;
            var53 = true;
        }

        float var27 = 1.0F;

        for(int var28 = this.world.getBottomY(); var28 < this.world.getTopY(); ++var28) {
            if(var28 == this.world.getBottomY() || var19.nextInt(3) == 0) {
                var27 = 1.0F + var19.nextFloat() * var19.nextFloat() * 1.0F;
            }

            this.buffer[var28] = var27 * var27;
        }

        for(; tunnel < tunnelCount; ++tunnel) {
            double var54 = 1.5D + (double)(MathHelper.sin((float)tunnel * (float)Math.PI / (float)tunnelCount) * baseWidth * 1.0F);
            double var30 = var54 * widthHeightRatio;
            var54 *= (double)var19.nextFloat() * 0.25D + 0.75D;
            var30 *= (double)var19.nextFloat() * 0.25D + 0.75D;
            float var32 = MathHelper.cos(pitch);
            float var33 = MathHelper.sin(pitch);
            x += MathHelper.cos(yaw) * var32;
            y += var33;
            z += MathHelper.sin(yaw) * var32;
            pitch *= 0.7F;
            pitch += var25 * 0.05F;
            yaw += var24 * 0.05F;
            var25 *= 0.8F;
            var24 *= 0.5F;
            var25 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0F;
            var24 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0F;
            if(var53 || var19.nextInt(4) != 0) {
                double var34 = x - var20;
                double var36 = z - var22;
                double var38 = tunnelCount - tunnel;
                double var40 = baseWidth + 2.0F + 16.0F;
                if(var34 * var34 + var36 * var36 - var38 * var38 > var40 * var40) {
                    return;
                }

                if(x >= var20 - 16.0D - var54 * 2.0D && z >= var22 - 16.0D - var54 * 2.0D && x <= var20 + 16.0D + var54 * 2.0D && z <= var22 + 16.0D + var54 * 2.0D) {
                    int var55 = MathHelper.floor(x - var54) - chunkX * 16 - 1;
                    int var35 = MathHelper.floor(x + var54) - chunkX * 16 + 1;
                    int var56 = MathHelper.floor(y - var30) - 1;
                    int var37 = MathHelper.floor(y + var30) + 1;
                    int var57 = MathHelper.floor(z - var54) - chunkZ * 16 - 1;
                    int var39 = MathHelper.floor(z + var54) - chunkZ * 16 + 1;
                    if(var55 < 0) {
                        var55 = 0;
                    }

                    if(var35 > 16) {
                        var35 = 16;
                    }

                    if(var56 < Math.max(this.world.getBottomY() + 1, 1)) {
                        var56 = Math.max(this.world.getBottomY() + 1, 1);
                    }

                    if(var37 > Math.min(this.world.getTopY() - 8, 120)) {
                        var37 = Math.min(this.world.getTopY() - 8, 120);
                    }

                    if(var57 < 0) {
                        var57 = 0;
                    }

                    if(var39 > 16) {
                        var39 = 16;
                    }

                    boolean var58 = false;

                    int var41;
                    int var44;
                    for(var41 = var55; !var58 && var41 < var35; ++var41) {
                        for(int var42 = var57; !var58 && var42 < var39; ++var42) {
                            for(int var43 = var37 + 1; !var58 && var43 >= var56 - 1; --var43) {
                                var44 = (var41 * 16 + var42) * this.world.getHeight() + var43;
                                if(var43 >= this.world.getBottomY() && var43 < this.world.getTopY()) {
                                    if(blocks[var44] == Block.FLOWING_WATER.id || blocks[var44] == Block.WATER.id) {
                                        var58 = true;
                                    }

                                    if(var43 != var56 - 1 && var41 != var55 && var41 != var35 - 1 && var42 != var57 && var42 != var39 - 1) {
                                        var43 = var56;
                                    }
                                }
                            }
                        }
                    }

                    if(!var58) {
                        for(var41 = var55; var41 < var35; ++var41) {
                            double var59 = ((double)(var41 + chunkX * 16) + 0.5D - x) / var54;

                            for(var44 = var57; var44 < var39; ++var44) {
                                double var45 = ((double)(var44 + chunkZ * 16) + 0.5D - z) / var54;
                                int var47 = (var41 * 16 + var44) * this.world.getHeight() + var37;
                                boolean var48 = false;
                                if(var59 * var59 + var45 * var45 < 1.0D) {
                                    for(int var49 = var37 - 1; var49 >= var56; --var49) {
                                        double var50 = ((double)var49 + 0.5D - y) / var30;
                                        if((var59 * var59 + var45 * var45) * (double)this.buffer[var49] + var50 * var50 / 6.0D < 1.0D) {
                                            byte var52 = blocks[var47];
                                            if(var52 == Block.GRASS_BLOCK.id) {
                                                var48 = true;
                                            }

                                            if(var52 == Block.STONE.id || var52 == Block.DIRT.id || var52 == Block.GRASS_BLOCK.id) {
                                                if(var49 < this.world.getBottomY() + 10) {
                                                    blocks[var47] = (byte)Block.FLOWING_LAVA.id;
                                                } else {
                                                    blocks[var47] = 0;
                                                    if(var48 && blocks[var47 - 1] == Block.DIRT.id) {
                                                        blocks[var47 - 1] = this.world.method_1781().getBiome(var41 + chunkX * 16, var44 + chunkZ * 16).topBlockId;
                                                    }
                                                }
                                            }
                                        }

                                        --var47;
                                    }
                                }
                            }
                        }

                        if(var53) {
                            break;
                        }
                    }
                }
            }
        }

    }

    protected void place(World world, int startChunkX, int startChunkZ, int chunkX, int chunkZ, byte[] blocks) {
        if(this.random.nextInt(50) == 0) {
            double var7 = startChunkX * 16 + this.random.nextInt(16);
            double var9 = this.random.nextInt(this.random.nextInt(40) + 8) + 20;
            double var11 = startChunkZ * 16 + this.random.nextInt(16);
            byte var13 = 1;

            for(int var14 = 0; var14 < var13; ++var14) {
                float var15 = this.random.nextFloat() * (float)Math.PI * 2.0F;
                float var16 = (this.random.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var17 = (this.random.nextFloat() * 2.0F + this.random.nextFloat()) * 2.0F;
                this.carveRavine(this.random.nextLong(), chunkX, chunkZ, blocks, var7, var9, var11, var17, var15, var16, 0, 0, 3.0D);
            }

        }
    }
}