package com.itselix99.betterworldoptions.world.worldtypes.indev223.feature;

import com.itselix99.betterworldoptions.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.OakTreeFeature;

import java.util.*;

public class IndevFeatures {
    public static void placeSpawnBuilding(World world) {
        int var1 = world.getProperties().getSpawnX();
        int var2 = world.getProperties().getSpawnY() + 2;
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

    public static void placeTopBlockOnDirt(World world, int x, int z, Biome biome, String theme) {
        int topBlockId;

        if (biome != null) {
            if (theme.equals("Hell") && !(biome.topBlockId == Block.GRASS_BLOCK.id)) {
                return;
            }

            topBlockId = theme.equals("Hell") ? Block.DIRT.id : biome.topBlockId;
        } else {
            if (theme.equals("Hell")) {
                return;
            }

            topBlockId = Block.GRASS_BLOCK.id;
        }
        for(int var1 = x + 8; var1 < x + 8 + 16; ++var1) {
            for(int var2 = 0; var2 < 128; ++var2) {
                for (int var3 = z + 8; var3 < z + 8 + 16; ++var3) {
                    if (world.getBlockId(var1, var2, var3) == Block.DIRT.id && world.getLightLevel(var1, var2 + 1, var3) >= 4) {
                        if (!(world.getBlockId(var1, var2 + 1, var3) == Block.ICE.id) && !world.getMaterial(var1, var2 + 1, var3).isFluid()) {
                            world.setBlockWithoutNotifyingNeighbors(var1, var2, var3, topBlockId);
                        }
                    }
                }
            }
        }
    }

    public static void generateTrees(World world, Random random, int chance, int x, int z) {
        for(int var1 = 0; var1 < chance; ++var1) {
            int var2 = x + random.nextInt(16) + 8 + (random.nextInt(12) - random.nextInt(12));
            int var3 = z + random.nextInt(16) + 8 + (random.nextInt(12) - random.nextInt(12));
            int var4 = world.getTopY(var2, var3);
            OakTreeFeature treeFeature = new OakTreeFeature();
            treeFeature.prepare(1.0F, 1.0F, 1.0F);
            treeFeature.generate(world, random, var2, var4, var3);
        }
    }

    public static void generatePlant(World world, Random random, PlantBlock plant, int chance, int x, int z) {
        for(int var1 = 0; var1 < chance; ++var1) {
            int var2 = x + random.nextInt(16) + 8 + (random.nextInt(4) - random.nextInt(4));
            int var3 = z + random.nextInt(16) + 8 + (random.nextInt(4) - random.nextInt(4));
            int var4 = world.getTopY(var2, var3);
            if (world.isAir(var2, var4, var3) && Block.BLOCKS[plant.id].canGrow(world, var2, var4, var3)) {
                world.setBlockWithoutNotifyingNeighbors(var2, var4, var3, plant.id);
            }
        }
    }

    public static void placeOre(Random random, int ore, int chance, int minY, int maxY, byte[] blocks) {
        ore = (byte)ore;
        chance = 16 * 16 * 128 / 256 / 64 * chance / 100;

        for(int var1 = 0; var1 < chance; ++var1) {
            float var2 = random.nextFloat() * (float) 16;
            float var3 = random.nextFloat() * (float) 128;
            float var4 = random.nextFloat() * (float) 16;
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

    public static void placeLakes(Random random, byte[] blocks, String theme) {
        int liquid = (theme.equals("Hell")) ? Block.LAVA.id : Block.WATER.id;
        int totalAttempts = (16 * 16 * 128) / 1000;

        for (int attempt = 0; attempt < totalAttempts; attempt++) {
            int x = random.nextInt(16);
            int y = random.nextInt(128);
            int z = random.nextInt(16);

            int index = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;

            if (blocks[index] == 0) {
                long lakeSize = floodFill(x, y, z, 0, 255, blocks);

                if (lakeSize > 0L && lakeSize < 640L) {
                    floodFill(x, y, z, 255, liquid, blocks);
                } else {
                    floodFill(x, y, z, 255, 0, blocks);
                }
            }
        }
    }

    public static void placeUndergroundLakes(Random random, byte[] blocks) {
        int totalAttempts = (16 * 16 * 128) / 2000;
        int maxLakeY = 60;

        for (int attempt = 0; attempt < totalAttempts; attempt++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int y = Math.min(Math.min(random.nextInt(maxLakeY), random.nextInt(maxLakeY)), Math.min(random.nextInt(maxLakeY), random.nextInt(maxLakeY)));

            int index = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;
            if (blocks[index] == 0) {
                long lakeSize = floodFill(x, y, z, 0, 255, blocks);

                if (lakeSize > 0L && lakeSize < 640L) {
                    floodFill(x, y, z, 255, Block.LAVA.id, blocks);
                } else {
                    floodFill( x, y, z, 255, 0, blocks);
                }
            }
        }
    }

    public static long floodFill(int startX, int startY, int startZ, int targetBlock, int fillBlock, byte[] blocks) {
        byte fill = (byte) fillBlock;
        byte target = (byte) targetBlock;

        int height = Config.BWOConfig.world.worldHeightLimit.getIntValue();

        final int WIDTH = 16;
        final int DEPTH = 16;

        final int X_STRIDE = height * 16;
        final int Z_STRIDE = height;
        final int Y_STRIDE = 1;

        ArrayList<int[]> stackPool = new ArrayList<>();
        int[] stack = new int[WIDTH * DEPTH * height];
        int stackSize = 0;

        int startIndex = (startX * 16 + startZ) * height + startY;
        stack[stackSize++] = startIndex;

        long filled = 0L;

        while (stackSize > 0) {
            int index = stack[--stackSize];

            if (stackSize == 0 && !stackPool.isEmpty()) {
                stack = stackPool.remove(stackPool.size() - 1);
                stackSize = stack.length;
            }

            int y = index % height;
            int xz = index / height;
            int z = xz & 15;
            int x = xz >> 4;

            int leftX = x;
            int scanIndex = index;

            while (leftX > 0 && blocks[scanIndex - X_STRIDE] == target) {
                scanIndex -= X_STRIDE;
                --leftX;
            }

            int rightX = leftX;
            int cursor = scanIndex;

            while (rightX < WIDTH && blocks[cursor] == target) {
                cursor += X_STRIDE;
                ++rightX;
            }

            if (fillBlock == 255 && (leftX == 0 || rightX == WIDTH || y == 0 || y == height - 1 || z == 0 || z == DEPTH - 1)) {
                return -1L;
            }

            boolean checkNegZ = false;
            boolean checkPosZ = false;
            boolean checkNegY = false;

            filled += (rightX - leftX);

            cursor = scanIndex;

            for (int xi = leftX; xi < rightX; xi++) {
                blocks[cursor] = fill;

                boolean match;

                if (z > 0) {
                    int i = cursor - Z_STRIDE;
                    match = blocks[i] == target;
                    if (match && !checkNegZ) {
                        if (stackSize == stack.length) {
                            stackPool.add(stack);
                            stack = new int[WIDTH * DEPTH * height];
                            stackSize = 0;
                        }
                        stack[stackSize++] = i;
                    }
                    checkNegZ = match;
                }

                if (z < DEPTH - 1) {
                    int i = cursor + Z_STRIDE;
                    match = blocks[i] == target;
                    if (match && !checkPosZ) {
                        if (stackSize == stack.length) {
                            stackPool.add(stack);
                            stack = new int[WIDTH * DEPTH * height];
                            stackSize = 0;
                        }
                        stack[stackSize++] = i;
                    }
                    checkPosZ = match;
                }

                if (y > 0) {
                    int i = cursor - Y_STRIDE;
                    byte below = blocks[i];

                    if ((fill == Block.FLOWING_LAVA.id || fill == Block.LAVA.id) && (below == Block.FLOWING_WATER.id || below == Block.WATER.id)) {
                        blocks[i] = (byte) Block.STONE.id;
                    }

                    match = below == target;
                    if (match && !checkNegY) {
                        if (stackSize == stack.length) {
                            stackPool.add(stack);
                            stack = new int[WIDTH * DEPTH * height];
                            stackSize = 0;
                        }
                        stack[stackSize++] = i;
                    }
                    checkNegY = match;
                }

                cursor += X_STRIDE;
            }
        }

        return filled;
    }
}