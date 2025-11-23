package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.config.Config;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CaveWorldCarver.class)
public abstract class CaveWorldCarverMixin extends Generator implements CaveGenBaseImpl {

    @Inject(
            method = "placeTunnels",
            at = @At("HEAD"),
            cancellable = true
    )
    private void caveFix(int chunkX, int chunkZ, byte[] blocks, double x, double y, double z, float baseWidth, float yaw, float pitch, int tunnel, int tunnelCount, double widthHeightRatio, CallbackInfo ci) {
        if (Config.BWOConfig.world.caveFix) {
            this.fixedPlaceTunnels(chunkX, chunkZ, blocks, x, y, z, baseWidth, yaw, pitch, tunnel, tunnelCount, (double) widthHeightRatio, new Random(this.random.nextLong()));
            ci.cancel();
        }
    }

    @Unique
    private void fixedPlaceTunnels(int chunkX, int chunkZ, byte[] blocks, double x, double y, double z, float baseWidth, float yaw, float pitch, int tunnel, int tunnelCount, double widthHeightRatio, Random var17) {
        double var18 = (double)(chunkX * 16 + 8);
        double var20 = (double)(chunkZ * 16 + 8);
        float var22 = 0.0F;
        float var23 = 0.0F;
        if (tunnelCount <= 0) {
            int var24 = this.range * 16 - 16;
            tunnelCount = var24 - var17.nextInt(var24 / 4);
        }

        boolean var60 = false;
        if (tunnel == -1) {
            tunnel = tunnelCount / 2;
            var60 = true;
        }

        int var25 = tunnel < tunnelCount / 4 * 3 ? tunnel + var17.nextInt((tunnelCount - tunnel) / 2) + (tunnelCount - tunnel) / 4 : -1;

        for(boolean var26 = var17.nextInt(6) == 0; tunnel < tunnelCount; ++tunnel) {
            double var27 = 1.5D + (double)(MathHelper.sin((float)tunnel * 3.1415927F / (float)tunnelCount) * baseWidth * 1.0F);
            double var29 = var27 * widthHeightRatio;
            float var31 = MathHelper.cos(pitch);
            float var32 = MathHelper.sin(pitch);
            x += (double)(MathHelper.cos(yaw) * var31);
            y += (double)var32;
            z += (double)(MathHelper.sin(yaw) * var31);
            if (var26) {
                pitch *= 0.92F;
            } else {
                pitch *= 0.7F;
            }

            pitch += var23 * 0.1F;
            yaw += var22 * 0.1F;
            var23 *= 0.9F;
            var22 *= 0.75F;
            var23 += (var17.nextFloat() - var17.nextFloat()) * var17.nextFloat() * 2.0F;
            var22 += (var17.nextFloat() - var17.nextFloat()) * var17.nextFloat() * 4.0F;
            if (!var60 && tunnel == var25 && baseWidth > 1.0F) {
                this.fixedPlaceTunnels(chunkX, chunkZ, blocks, x, y, z, (float)(0.75D * var27 + 0.25D * var27 * (double)var17.nextFloat() - 1.5D), yaw - 1.5707964F, pitch / 3.0F, tunnel, tunnelCount, 1.0D, new Random(var17.nextLong()));
                this.fixedPlaceTunnels(chunkX, chunkZ, blocks, x, y, z, (float)(0.75D * var27 + 0.25D * var27 * (double)var17.nextFloat() - 1.5D), yaw + 1.5707964F, pitch / 3.0F, tunnel, tunnelCount, 1.0D, new Random(var17.nextLong()));
                return;
            }

            if (var60 || var17.nextInt(4) != 0) {
                double var33 = x - var18;
                double var35 = z - var20;
                double var37 = (double)(tunnelCount - tunnel);
                double var39 = (double)(baseWidth + 2.0F + 16.0F);
                if (var33 * var33 + var35 * var35 > (var37 + var39) * (var37 + var39)) {
                    return;
                }

                if (x >= var18 - 16.0D - var27 * 2.0D && z >= var20 - 16.0D - var27 * 2.0D && x <= var18 + 16.0D + var27 * 2.0D && z <= var20 + 16.0D + var27 * 2.0D) {
                    int var41 = MathHelper.floor(x - var27) - chunkX * 16 - 1;
                    int var42 = MathHelper.floor(x + var27) - chunkX * 16 + 1;
                    int var43 = MathHelper.floor(y - var29) - 1;
                    int var44 = MathHelper.floor(y + var29) + 1;
                    int var45 = MathHelper.floor(z - var27) - chunkZ * 16 - 1;
                    int var46 = MathHelper.floor(z + var27) - chunkZ * 16 + 1;
                    if (var41 < 0) {
                        var41 = 0;
                    }

                    if (var42 > 16) {
                        var42 = 16;
                    }

                    if (var43 < Math.max(stationapi_getWorld().getBottomY() + 1, 1)) {
                        var43 = Math.max(stationapi_getWorld().getBottomY() + 1, 1);
                    }

                    if (var44 > Math.min(stationapi_getWorld().getTopY() - 8, 120)) {
                        var44 = Math.min(stationapi_getWorld().getTopY() - 8, 120);
                    }

                    if (var45 < 0) {
                        var45 = 0;
                    }

                    if (var46 > 16) {
                        var46 = 16;
                    }

                    boolean var47 = false;

                    int var48;
                    int var49;
                    for(var48 = var41; !var47 && var48 < var42; ++var48) {
                        for(int var50 = var45; !var47 && var50 < var46; ++var50) {
                            for(int var51 = var44 + 1; !var47 && var51 >= var43 - 1; --var51) {
                                var49 = ((var48 * 16 + var50) * net.modificationstation.stationapi.api.util.math.MathHelper.smallestEncompassingPowerOfTwo(stationapi_getWorld().getHeight()) + var51) - stationapi_getWorld().getBottomY();
                                if (var51 >= 0 && var51 < 128) {
                                    if (blocks[var49] == Block.FLOWING_WATER.id || blocks[var49] == Block.WATER.id) {
                                        var47 = true;
                                    }

                                    if (var51 != var43 - 1 && var48 != var41 && var48 != var42 - 1 && var50 != var45 && var50 != var46 - 1) {
                                        var51 = var43;
                                    }
                                }
                            }
                        }
                    }

                    if (!var47) {
                        for(var48 = var41; var48 < var42; ++var48) {
                            double var61 = ((double)(var48 + chunkX * 16) + 0.5D - x) / var27;

                            for(var49 = var45; var49 < var46; ++var49) {
                                double var52 = ((double)(var49 + chunkZ * 16) + 0.5D - z) / var27;
                                int var54 = ((var48 * 16 + var49) * net.modificationstation.stationapi.api.util.math.MathHelper.smallestEncompassingPowerOfTwo(stationapi_getWorld().getHeight()) + var44) - stationapi_getWorld().getBottomY();
                                boolean var55 = false;
                                if (var61 * var61 + var52 * var52 < 1.0D) {
                                    for(int var56 = var44 - 1; var56 >= var43; --var56) {
                                        double var57 = ((double)var56 + 0.5D - y) / var29;
                                        if (var57 > -0.7D && var61 * var61 + var57 * var57 + var52 * var52 < 1.0D) {
                                            byte var59 = blocks[var54];
                                            if (var59 == Block.GRASS_BLOCK.id) {
                                                var55 = true;
                                            }

                                            if (var59 == Block.STONE.id || var59 == Block.DIRT.id|| var59 == Block.GRASS_BLOCK.id) {
                                                if (var56 < 10) {
                                                    blocks[var54] = (byte)Block.FLOWING_LAVA.id;
                                                } else {
                                                    blocks[var54] = 0;
                                                    if (var55 && blocks[var54 - 1] == Block.DIRT.id) {
                                                        blocks[var54 - 1] = (byte)Block.GRASS_BLOCK.id;
                                                    }
                                                }
                                            }
                                        }

                                        --var54;
                                    }
                                }
                            }
                        }

                        if (var60) {
                            break;
                        }
                    }
                }
            }
        }

    }
}