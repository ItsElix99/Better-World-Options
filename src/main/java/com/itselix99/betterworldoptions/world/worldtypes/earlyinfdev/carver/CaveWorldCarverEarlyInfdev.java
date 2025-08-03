package com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.carver;

import com.itselix99.betterworldoptions.BWOConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Generator;

import java.util.Random;

public class CaveWorldCarverEarlyInfdev extends Generator {
    protected void place(World world, int startChunkX, int startChunkZ, int chunkX, int chunkZ, byte[] blocks) {
        Random var1 = new Random(world.getSeed() + (long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        float var2 = var1.nextFloat() * (float) Math.PI;

        double var3 = (chunkX * 16 + 8) + MathHelper.sin(var2) * 7.0F;
        double var4 = (chunkX * 16 + 8) - MathHelper.sin(var2) * 7.0F;
        double var5 = (chunkZ * 16 + 8) + MathHelper.cos(var2) * 7.0F;
        double var6 = (chunkZ * 16 + 8) - MathHelper.cos(var2) * 7.0F;

        int caveYBase = var1.nextInt(64) + 16;
        double var7 = caveYBase + var1.nextInt(8);
        double var8 = caveYBase + var1.nextInt(8);

        double var9 = var1.nextDouble() * 4.0 + 2.0;
        double var10 = var1.nextDouble() * 0.6;
        long var11 = var1.nextLong();

        var1.setSeed(var11);

        final int yMax = BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue();

        for (int i = 0; i <= 16; ++i) {
            double t = i / 16.0;
            double px = var3 + (var4 - var3) * t;
            double py = var7 + (var8 - var7) * t;
            double pz = var5 + (var6 - var5) * t;

            double sinFactor = MathHelper.sin((float) t * (float) Math.PI);
            double radius = (sinFactor * var9 + 1.0);

            double flatnessThreshold = var1.nextDouble() * var10 + (1.0 - var10);

            int xMin = Math.max((int)(px - radius / 2), chunkX * 16);
            int xMax = Math.min((int)(px + radius / 2), chunkX * 16 + 15);
            int yMin = Math.max((int)(py - radius / 2), 0);
            int yMaxL = Math.min((int)(py + radius / 2), yMax - 1);
            int zMin = Math.max((int)(pz - radius / 2), chunkZ * 16);
            int zMax = Math.min((int)(pz + radius / 2), chunkZ * 16 + 15);

            for (int x = xMin; x <= xMax; ++x) {
                int localX = x - chunkX * 16;
                for (int y = yMin; y <= yMaxL; ++y) {
                    for (int z = zMin; z <= zMax; ++z) {
                        int localZ = z - chunkZ * 16;

                        double dx = (x + 0.5 - px) / (radius / 2.0);
                        double dy = (y + 0.5 - py) / (radius / 2.0);
                        double dz = (z + 0.5 - pz) / (radius / 2.0);

                        double distSq = dx * dx + dy * dy + dz * dz;
                        if (distSq < flatnessThreshold) {
                            int index = (localX * 16 + localZ) * BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() + y;
                            byte block = blocks[index];
                            if (block == Block.WATER.id || block == Block.FLOWING_WATER.id) {
                                return;
                            }
                        }
                    }
                }
            }
        }

        var1.setSeed(var11);

        for (int i = 0; i <= 16; ++i) {
            double t = i / 16.0;
            double px = var3 + (var4 - var3) * t;
            double py = var7 + (var8 - var7) * t;
            double pz = var5 + (var6 - var5) * t;

            double sinFactor = MathHelper.sin((float) t * (float) Math.PI);
            double radius = (sinFactor * var9 + 1.0);

            double flatnessThreshold = var1.nextDouble() * var10 + (1.0 - var10);

            int xMin = Math.max((int)(px - radius / 2), chunkX * 16);
            int xMax = Math.min((int)(px + radius / 2), chunkX * 16 + 15);
            int yMin = Math.max((int)(py - radius / 2), 0);
            int yMaxL = Math.min((int)(py + radius / 2), yMax - 1);
            int zMin = Math.max((int)(pz - radius / 2), chunkZ * 16);
            int zMax = Math.min((int)(pz + radius / 2), chunkZ * 16 + 15);

            for (int x = xMin; x <= xMax; ++x) {
                int localX = x - chunkX * 16;
                for (int y = yMin; y <= yMaxL; ++y) {
                    for (int z = zMin; z <= zMax; ++z) {
                        int localZ = z - chunkZ * 16;

                        double dx = (x + 0.5 - px) / (radius / 2.0);
                        double dy = (y + 0.5 - py) / (radius / 2.0);
                        double dz = (z + 0.5 - pz) / (radius / 2.0);

                        double distSq = dx * dx + dy * dy + dz * dz;
                        if (distSq < flatnessThreshold) {
                            int index = (localX * 16 + localZ) * BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue() + y;
                            byte block = blocks[index];
                            if (block == Block.STONE.id || block == Block.DIRT.id || block == Block.GRASS_BLOCK.id) {
                                blocks[index] = 0;
                            }
                        }
                    }
                }
            }
        }
    }
}