package com.itselix99.betterworldoptions.world.worldtypes.indev223.feature;

import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;

public class IndevFeatures {
    public static void placeSpawnBuilding(World world) {
        int var1 = world.getProperties().getSpawnX();
        int var2 = world.getProperties().getSpawnY();
        int var3 = world.getProperties().getSpawnZ();

        for(int var4 = var1 - 3; var4 <= var1 + 3; ++var4) {
            for(int var5 = var2 - 2; var5 <= var2 + 2; ++var5) {
                for(int var6 = var3 - 3; var6 <= var3 + 3; ++var6) {
                    int blockId = var5 < var2 - 1 ? Block.OBSIDIAN.id : 0;
                    if (var4 == var1 - 3 || var6 == var3 - 3 || var4 == var1 + 3 || var6 == var3 + 3 || var5 == var2 - 2 || var5 == var2 + 2) {
                        blockId = Block.STONE.id;
                        if (var5 >= var2 - 1) {
                            blockId = Block.PLANKS.id;
                        }
                    }

                    if (var6 == var3 - 3 && var4 == var1 && var5 >= var2 - 1 && var5 <= var2) {
                        blockId = 0;
                    }

                    world.setBlock(var4, var5, var6, blockId);
                }
            }
        }

        world.setBlock(var1 - 3 + 1, var2, var3, Block.TORCH.id);
        world.setBlock(var1 + 3 - 1, var2, var3, Block.TORCH.id);
    }



    public static void placeLakes(Random random, byte[] blocks, String theme) {
        int liquid = (Objects.equals(theme, "Hell")) ? Block.LAVA.id : Block.WATER.id;

        int totalAttempts = (16 * 16 * 128) / 1000;

        for (int attempt = 0; attempt < totalAttempts; attempt++) {
            int x = random.nextInt(16);
            int y = random.nextInt(128);
            int z = random.nextInt(16);

            int index = (x * 16 + z) * 128 + y;

            if (blocks[index] == 0) {
                long lakeSize = placeLake(x, y, z, 0, Block.WATER.id, blocks);

                if (lakeSize > 0 && lakeSize < 640) {
                    placeLake(x, y, z, Block.WATER.id, liquid, blocks);
                } else {
                    placeLake(x, y, z, Block.WATER.id, 0, blocks);
                }
            }
        }
    }


    public static void placeUndergroundLakes(Random random, byte[] blocks) {
        int totalAttempts = (16 * 16 * 128) / 2000;
        int maxLakeY = 64;

        for (int attempt = 0; attempt < totalAttempts; attempt++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);

            int y = Math.min(
                    Math.min(random.nextInt(maxLakeY), random.nextInt(maxLakeY)),
                    Math.min(random.nextInt(maxLakeY), random.nextInt(maxLakeY))
            );

            int index = (x * 16 + z) * 128 + y;

            if (blocks[index] == 0) {
                long lakeSize = placeLake(x, y, z, 0, Block.WATER.id, blocks);

                if (lakeSize > 0 && lakeSize < 640) {
                    placeLake(x, y, z, Block.WATER.id, Block.LAVA.id, blocks);
                } else {
                    placeLake(x, y, z, Block.WATER.id, 0, blocks);
                }
            }
        }
    }

    public static long placeLake(int startX, int startY, int startZ, int matchId, int liquidId, byte[] blocks) {
        final int sizeX = 16;
        final int sizeY = 128;
        final int sizeZ = 16;
        final int MAX_FILL = 640;

        byte target = (byte) matchId;
        byte liquid = (byte) liquidId;

        if (startX < 0 || startX >= sizeX || startY < 0 || startY >= sizeY || startZ < 0 || startZ >= sizeZ)
            return 0;

        int startIndex = (startX * sizeZ + startZ) * sizeY + startY;
        if (blocks[startIndex] != target)
            return 0;

        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{startX, startY, startZ});

        long filled = 0L;

        while (!queue.isEmpty()) {
            int[] pos = queue.pollLast();
            int x = pos[0];
            int y = pos[1];
            int z = pos[2];

            int minZ = z;
            int maxZ = z;

            while (minZ > 0 && blocks[(x * sizeZ + (minZ - 1)) * sizeY + y] == target) minZ--;
            while (maxZ < sizeZ - 1 && blocks[(x * sizeZ + (maxZ + 1)) * sizeY + y] == target) maxZ++;

            for (int zz = minZ; zz <= maxZ; zz++) {
                int idx = (x * sizeZ + zz) * sizeY + y;

                if (blocks[idx] == target) {
                    blocks[idx] = liquid;
                    filled++;
                }

                if (filled >= MAX_FILL) {
                    return filled;
                }

                if (y > 0) {
                    int below = (x * sizeZ + zz) * sizeY + (y - 1);
                    if (blocks[below] == target) queue.add(new int[]{x, y - 1, zz});
                }
                if (y < sizeY - 1) {
                    int above = (x * sizeZ + zz) * sizeY + (y + 1);
                    if (blocks[above] == target) queue.add(new int[]{x, y + 1, zz});
                }

                if (x > 0) {
                    int left = ((x - 1) * sizeZ + zz) * sizeY + y;
                    if (blocks[left] == target) queue.add(new int[]{x - 1, y, zz});
                }
                if (x < sizeX - 1) {
                    int right = ((x + 1) * sizeZ + zz) * sizeY + y;
                    if (blocks[right] == target) queue.add(new int[]{x + 1, y, zz});
                }
            }
        }

        return filled;
    }

    public static void placeOre(Random random, int ore, int chance, int minY, int maxY, byte[] blocks) {
        ore = (byte)ore;
        chance = 16 * 16 * 128 / 256 / 64 * chance / 100;

        for(int var1 = 0; var1 < chance; ++var1) {
            float var2 = random.nextFloat() * 16;
            float var3 = random.nextFloat() * 128;
            float var4 = random.nextFloat() * 16;
            if (!(var3 > (float)maxY)) {
                int var5 = (int)((random.nextFloat() + random.nextFloat()) * 75.0F * (float)minY / 100.0F);
                float var6 = random.nextFloat() * (float)Math.PI * 2.0F;
                float var7 = 0.0F;
                float var8 = random.nextFloat() * (float)Math.PI * 2.0F;
                float var9 = 0.0F;

                for(int var10 = 0; var10 < var5; ++var10) {
                    var2 += MathHelper.sin(var6) * MathHelper.cos(var8);
                    var4 += MathHelper.cos(var6) * MathHelper.cos(var8);
                    var3 += MathHelper.sin(var8);
                    var6 += var7 * 0.2F;
                    var7 = (var7 * 0.9F) + (random.nextFloat() - random.nextFloat());
                    var8 = (var8 + var9 * 0.5F) * 0.5F;
                    var9 = (var9 * 0.9F) + (random.nextFloat() - random.nextFloat());
                    float var11 = MathHelper.sin((float)var10 * (float)Math.PI / (float)var5) * (float)minY / 100.0F + 1.0F;

                    for(int var12 = (int)(var2 - var11); var12 <= (int)(var2 + var11); ++var12) {
                        for(int var13 = (int)(var3 - var11); var13 <= (int)(var3 + var11); ++var13) {
                            for(int var14 = (int)(var4 - var11); var14 <= (int)(var4 + var11); ++var14) {
                                float var15 = (float)var12 - var2;
                                float var16 = (float)var13 - var3;
                                float var17 = (float)var14 - var4;
                                if (var15 * var15 + var16 * var16 * 2.0F + var17 * var17 < var11 * var11 && var12 > 0 && var13 > 0 && var14 > 0 && var12 < 16 - 1 && var13 < 128 - 1 && var14 < 16 - 1) {
                                    int var18 = (var13 * 16 + var14) * 16 + var12;
                                    if (blocks[var18] == Block.STONE.id) {
                                        blocks[var18] = (byte)ore;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void placePlant(World world, Random random, PlantBlock plant, int chance, int x, int z) {
        chance = (int)((long)16 * (long)16 * (long)128 * (long)chance / 1600000L);

        for(int i = 0; i < chance; ++i) {
            int var1 = x + random.nextInt(16);
            int var2 = random.nextInt(128);
            int var3 = z + random.nextInt(16);

            for(int var4 = 0; var4 < 10; ++var4) {
                int var5 = var1;
                int var6 = var2;
                int var7 = var3;

                for(int var8 = 0; var8 < 10; ++var8) {
                    var5 += random.nextInt(4) - random.nextInt(4);
                    var6 += random.nextInt(2) - random.nextInt(2);
                    var7 += random.nextInt(4) - random.nextInt(4);
                    if (world.isAir(var5, var6, var7) && Block.BLOCKS[plant.id].canGrow(world, var5, var6, var7)) {
                        world.setBlockWithoutNotifyingNeighbors(var5, var6, var7, plant.id);
                    }
                }
            }
        }
    }
}