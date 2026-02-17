package com.itselix99.betterworldoptions.api.chunk;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.mixin.world.AlphaWorldStorageAccessor;
import com.itselix99.betterworldoptions.mixin.world.DimensionDataAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldStorage;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class FiniteChunkGenerator extends BWOChunkGenerator {
    private File fullWorldFile;
    private RandomAccessFile fullWorldRandomAccessFile;
    private FileChannel fullWorldChannel;
    protected MappedByteBuffer fullWorldBlocks;

    private File lightingFile;
    private RandomAccessFile lightingRandomAccessFile;
    private FileChannel lightingChannel;
    protected MappedByteBuffer lighting;

    private boolean storageActive = false;

    private int[] floodFillBlocks;
    private int[] heightMap;

    private BitSet processedChunks;
    private File progressFile;

    private int phaseBar;
    private int phases;

    private int totalChunks;
    private int copiedChunks;

    private String title;
    private String currentStage;
    private int phasePercentage;

    public FiniteChunkGenerator(World world, long seed) {
        super(world, seed);

        if (this.finiteWorld && ((BWOProperties) world.getProperties()).bwo_isPregeneratingFiniteWorld()) {
            WorldStorage worldStorage = ((DimensionDataAccessor) world).getDimensionData();
            File dir = ((AlphaWorldStorageAccessor) worldStorage).getDir();

            int maxChunkX = this.sizeX / 16;
            int maxChunkZ = this.sizeZ / 16;
            this.totalChunks = maxChunkX * maxChunkZ;

            this.fullWorldFile = new File(dir, "fullWorld.tmp");
            this.lightingFile = new File(dir, "lighting.tmp");
            this.progressFile = new File(dir, "copiedChunks.progress");

            this.processedChunks = new BitSet(this.totalChunks);

            if (this.progressFile.exists()) {
                try (FileInputStream in = new FileInputStream(this.progressFile)) {
                    this.processedChunks = BitSet.valueOf(in.readAllBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            this.copiedChunks = this.processedChunks.cardinality();

            try {
                long totalSize = (long) this.sizeX * this.sizeZ * 64;

                this.fullWorldRandomAccessFile = new RandomAccessFile(this.fullWorldFile, "rw");
                this.fullWorldRandomAccessFile.setLength(totalSize);
                this.fullWorldChannel = this.fullWorldRandomAccessFile.getChannel();
                this.fullWorldBlocks = this.fullWorldChannel.map(FileChannel.MapMode.READ_WRITE, 0, totalSize);

                this.lightingRandomAccessFile = new RandomAccessFile(this.lightingFile, "rw");
                this.lightingRandomAccessFile.setLength(totalSize);
                this.lightingChannel = this.lightingRandomAccessFile.getChannel();
                this.lighting = this.lightingChannel.map(FileChannel.MapMode.READ_WRITE, 0, totalSize);

                this.storageActive = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.floodFillBlocks = new int[256 * 256 * 64];
        }
    }

    protected void pregenerateTerrain() {
    }

    protected void copyFullWorldToChunks(int chunkX, int chunkZ, byte[] blocks, int bottomBlock) {
        if (this.storageActive) {
            int startX = chunkX * 16;
            int startZ = chunkZ * 16;

            int worldHeight = 64;
            int chunkHeight = Config.BWOConfig.world.worldHeightLimit.getIntValue();
            int yOffset = 32;

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int worldX = startX + x;
                    int worldZ = startZ + z;

                    for (int y = 0; y < chunkHeight; y++) {
                        int chunkIndex = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;

                        int worldY = y - yOffset;
                        if (worldX >= 0 && worldX < this.sizeX && worldZ >= 0 && worldZ < this.sizeZ) {
                            if (worldY >= 0 && worldY < worldHeight) {
                                int worldIndex = (worldY * this.sizeZ + worldZ) * this.sizeX + worldX;
                                blocks[chunkIndex] = this.fullWorldBlocks.get(worldIndex);
                            } else if (y < yOffset) {
                                blocks[chunkIndex] = (byte) bottomBlock;
                            }
                        } else {
                            blocks[chunkIndex] = 0;
                        }
                    }
                }
            }

            int index = this.getChunkIndex(chunkX, chunkZ);

            if (index >= 0 && !this.processedChunks.get(index)) {
                this.processedChunks.set(index);
                this.copiedChunks++;
                this.saveProgress();

                if (this.copiedChunks >= this.totalChunks) {
                    try {
                        this.finishStorage();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private int getChunkIndex(int chunkX, int chunkZ) {
        int maxChunkX = this.sizeX / 16;
        int maxChunkZ = this.sizeZ / 16;

        if (chunkX < 0 || chunkZ < 0 || chunkX >= maxChunkX || chunkZ >= maxChunkZ) {
            return -1;
        }

        return chunkZ * maxChunkX + chunkX;
    }

    private void saveProgress() {
        try (FileOutputStream out = new FileOutputStream(this.progressFile)) {
            out.write(this.processedChunks.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void finishStorage() throws Exception {
        try {
            this.shutdownStorage();
        } finally {
            this.fullWorldFile.delete();
            this.lightingFile.delete();
            this.progressFile.delete();

            System.gc();
            ((BWOProperties) this.world.getProperties()).bwo_setPregeneratingFiniteWorld(false);
            this.storageActive = false;
        }
    }

    public void shutdownStorage() throws Exception {
        try {
            if (this.fullWorldBlocks != null) {
                this.storageActive = false;
                this.fullWorldBlocks.force();
                this.lighting.force();

                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                Object unsafe = unsafeField.get(null);
                Method invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
                invokeCleaner.invoke(unsafe, this.fullWorldBlocks);
                invokeCleaner.invoke(unsafe, this.lighting);
            }
        } finally {
            this.fullWorldChannel.close();
            this.fullWorldRandomAccessFile.close();

            this.lightingChannel.close();
            this.lightingRandomAccessFile.close();

            System.gc();
        }
    }

    protected void placeBlockOnDirt(int blockId) {
        if (blockId == Block.DIRT.id) return;

        for(int x = 0; x < this.sizeX; ++x) {
            this.setPhasePercentage((float)x * 100.0F / (float)(this.sizeX - 1));

            for(int y = 0; y < 64; ++y) {
                for (int z = 0; z < this.sizeZ; ++z) {
                    if ((this.fullWorldBlocks.get((y * this.sizeZ + z) * this.sizeX + x) & 255) == Block.DIRT.id && this.getBrightness(x, y + 1, z) >= 4) {
                        this.fullWorldBlocks.put((y * this.sizeZ + z) * this.sizeX + x, (byte) blockId);
                    }
                }
            }
        }
    }

    protected void generateTrees() {
        int var2 = this.sizeX * this.sizeZ * 64 / 80000;

        for(int var3 = 0; var3 < var2; ++var3) {
            if(var3 % 100 == 0) {
                this.setPhasePercentage((float)var3 * 100.0F / (float)(var2 - 1));
            }

            int var4 = this.random.nextInt(this.sizeX);
            int var5 = this.random.nextInt(64);
            int var6 = this.random.nextInt(this.sizeZ);

            for(int var7 = 0; var7 < 25; ++var7) {
                int var8 = var4;
                int var9 = var5;
                int var10 = var6;

                for(int var11 = 0; var11 < 20; ++var11) {
                    var8 += this.random.nextInt(12) - this.random.nextInt(12);
                    var9 += this.random.nextInt(3) - this.random.nextInt(6);
                    var10 += this.random.nextInt(12) - this.random.nextInt(12);
                    if(var8 >= 0 && var9 >= 0 && var10 >= 0 && var8 < this.sizeX && var9 < 64 && var10 < this.sizeZ) {
                        this.generateTrees(var8, var9, var10);
                    }
                }
            }
        }

    }

    private boolean generateTrees(int var1, int var2, int var3) {
        int var4 = this.random.nextInt(3) + 4;
        boolean var5 = true;
        if(var2 > 0 && var2 + var4 + 1 <= 64) {
            int var6;
            int var8;
            int var9;
            int var10;
            for(var6 = var2; var6 <= var2 + 1 + var4; ++var6) {
                byte var7 = 1;
                if(var6 == var2) {
                    var7 = 0;
                }

                if(var6 >= var2 + 1 + var4 - 2) {
                    var7 = 2;
                }

                for(var8 = var1 - var7; var8 <= var1 + var7 && var5; ++var8) {
                    for(var9 = var3 - var7; var9 <= var3 + var7 && var5; ++var9) {
                        if(var8 >= 0 && var6 >= 0 && var9 >= 0 && var8 < this.sizeX && var6 < 64 && var9 < this.sizeZ) {
                            var10 = this.fullWorldBlocks.get((var6 * this.sizeZ + var9) * this.sizeX + var8) & 255;
                            if(var10 != 0) {
                                var5 = false;
                            }
                        } else {
                            var5 = false;
                        }
                    }
                }
            }

            if(!var5) {
                return false;
            } else {
                var6 = this.fullWorldBlocks.get(((var2 - 1) * this.sizeZ + var3) * this.sizeX + var1) & 255;
                if((var6 == Block.GRASS_BLOCK.id || var6 == Block.DIRT.id) && var2 < 64 - var4 - 1) {
                    this.fullWorldBlocks.put(((var2 - 1) * this.sizeZ + var3) * this.sizeX + var1, (byte) Block.DIRT.id);

                    int var13;
                    for(var13 = var2 - 3 + var4; var13 <= var2 + var4; ++var13) {
                        var8 = var13 - (var2 + var4);
                        var9 = 1 - var8 / 2;

                        for(var10 = var1 - var9; var10 <= var1 + var9; ++var10) {
                            int var12 = var10 - var1;

                            for(var6 = var3 - var9; var6 <= var3 + var9; ++var6) {
                                int var11 = var6 - var3;
                                if((Math.abs(var12) != var9 || Math.abs(var11) != var9 || this.random.nextInt(2) != 0 && var8 != 0) && !Block.BLOCKS_OPAQUE[this.fullWorldBlocks.get((var13 * this.sizeZ + var6) * this.sizeX + var10) & 255]) {
                                    this.fullWorldBlocks.put((var13 * this.sizeZ + var6) * this.sizeX + var10, (byte) Block.LEAVES.id);
                                }
                            }
                        }
                    }

                    for(var13 = 0; var13 < var4; ++var13) {
                        if(!Block.BLOCKS_OPAQUE[this.fullWorldBlocks.get(((var2 + var13) * this.sizeZ + var3) * this.sizeX + var1) & 255]) {
                            this.fullWorldBlocks.put(((var2 + var13) * this.sizeZ + var3) * this.sizeX + var1, (byte) Block.LOG.id);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    protected void generateFlowersAndMushrooms(PlantBlock var2, int var3) {
        var3 = (int)((long)this.sizeX * (long)this.sizeZ * (long)64 * (long)var3 / 1600000L);

        for(int var4 = 0; var4 < var3; ++var4) {
            if(var4 % 100 == 0) {
                this.setPhasePercentage((float)var4 * 100.0F / (float)(var3 - 1));
            }

            int var5 = this.random.nextInt(this.sizeX);
            int var6 = this.random.nextInt(64);
            int var7 = this.random.nextInt(this.sizeZ);

            for(int var8 = 0; var8 < 10; ++var8) {
                int var9 = var5;
                int var10 = var6;
                int var11 = var7;

                for(int var12 = 0; var12 < 10; ++var12) {
                    var9 += this.random.nextInt(4) - this.random.nextInt(4);
                    var10 += this.random.nextInt(2) - this.random.nextInt(2);
                    var11 += this.random.nextInt(4) - this.random.nextInt(4);
                    if(var9 >= 0 && var11 >= 0 && var10 > 0 && var9 < this.sizeX && var11 < this.sizeZ && var10 < 64 && (this.fullWorldBlocks.get((var10 * this.sizeZ + var11) * this.sizeX + var9) & 255) == 0 && (var2 instanceof MushroomPlantBlock ? this.canGrowMushroom(var9, var10, var11) : this.canGrow(var9, var10, var11))) {
                        this.fullWorldBlocks.put((var10 * this.sizeZ + var11) * this.sizeX + var9, (byte) var2.id);
                    }
                }
            }
        }

    }

    private boolean canGrow(int var1, int var2, int var3) {
        return (this.getBrightness(var1, var2, var3) >= 8 || this.getBrightness(var1, var2, var3) >= 4 && this.hasSkyLight(var1, var2, var3)) && this.canPlantOnTop(this.fullWorldBlocks.get(((var2 - 1) * this.sizeZ + var3) * this.sizeX + var1) & 255);
    }

    private boolean canGrowMushroom(int var1, int var2, int var3) {
        if(this.getBrightness(var1, var2, var3) <= 13) {
            int id = this.fullWorldBlocks.get(((var2 - 1) * this.sizeZ + var3) * this.sizeX + var1) & 255;
            if(Block.BLOCKS_OPAQUE[id]) {
                return true;
            }
        }

        return false;
    }

    private boolean canPlantOnTop(int var1) {
        return var1 == Block.GRASS_BLOCK.id || var1 == Block.DIRT.id || var1 == Block.FARMLAND.id;
    }

    protected int generateOres(int var1, int var2, int var3, int var4) {
        int var5 = 0;
        byte var26 = (byte)var1;
        int var6 = this.sizeX;
        int var7 = this.sizeZ;
        int var8 = 64;
        var2 = var6 * var7 * var8 / 256 / 64 * var2 / 100;

        for(int var9 = 0; var9 < var2; ++var9) {
            this.setPhasePercentage((float)var9 * 100.0F / (float)(var2 - 1));
            float var10 = this.random.nextFloat() * (float)var6;
            float var11 = this.random.nextFloat() * (float)var8;
            float var12 = this.random.nextFloat() * (float)var7;
            if(var11 <= (float)var4) {
                int var13 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)var3 / 100.0F);
                float var14 = this.random.nextFloat() * (float)Math.PI * 2.0F;
                float var15 = 0.0F;
                float var16 = this.random.nextFloat() * (float)Math.PI * 2.0F;
                float var17 = 0.0F;

                for(int var18 = 0; var18 < var13; ++var18) {
                    var10 += MathHelper.sin(var14) * MathHelper.cos(var16);
                    var12 += MathHelper.cos(var14) * MathHelper.cos(var16);
                    var11 += MathHelper.sin(var16);
                    var14 += var15 * 0.2F;
                    var15 *= 0.9F;
                    var15 += this.random.nextFloat() - this.random.nextFloat();
                    var16 += var17 * 0.5F;
                    var16 *= 0.5F;
                    var17 *= 0.9F;
                    var17 += this.random.nextFloat() - this.random.nextFloat();
                    float var19 = MathHelper.sin((float)var18 * (float)Math.PI / (float)var13) * (float)var3 / 100.0F + 1.0F;

                    for(int var20 = (int)(var10 - var19); var20 <= (int)(var10 + var19); ++var20) {
                        for(int var21 = (int)(var11 - var19); var21 <= (int)(var11 + var19); ++var21) {
                            for(int var22 = (int)(var12 - var19); var22 <= (int)(var12 + var19); ++var22) {
                                float var23 = (float)var20 - var10;
                                float var24 = (float)var21 - var11;
                                float var25 = (float)var22 - var12;
                                var23 = var23 * var23 + var24 * var24 * 2.0F + var25 * var25;
                                if(var23 < var19 * var19 && var20 > 0 && var21 > 0 && var22 > 0 && var20 < this.sizeX - 1 && var21 < 64 - 1 && var22 < this.sizeZ - 1) {
                                    int var27 = (var21 * this.sizeZ + var22) * this.sizeX + var20;
                                    if(this.fullWorldBlocks.get(var27) == Block.STONE.id) {
                                        this.fullWorldBlocks.put(var27, var26);
                                        ++var5;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return var5;
    }

    protected void placeLakes() {
        int var1 = Block.WATER.id;
        if(this.theme.equals("Hell")) {
            var1 = Block.LAVA.id;
        }

        int var2 = this.sizeX * this.sizeZ * 64 / 1000;

        for(int var3 = 0; var3 < var2; ++var3) {
            if(var3 % 100 == 0) {
                this.setPhasePercentage((float)var3 * 100.0F / (float)(var2 - 1));
            }

            int var4 = this.random.nextInt(this.sizeX);
            int var5 = this.random.nextInt(64);
            int var6 = this.random.nextInt(this.sizeZ);
            if(this.fullWorldBlocks.get((var5 * this.sizeZ + var6) * this.sizeX + var4) == 0) {
                long var7 = this.floodFill(var4, var5, var6, 0, 255);

                if(var7 > 0L && var7 < 640L) {
                    this.floodFill(var4, var5, var6, 255, var1);
                } else {
                    this.floodFill(var4, var5, var6, 255, 0);
                }
            }
        }

        this.setPhasePercentage(100.0F);
    }

    protected void placeUndergroundLakes() {
        int var1 = this.sizeX * this.sizeZ * 64 / 2000;
        int var2 = 30;

        for(int var3 = 0; var3 < var1; ++var3) {
            if(var3 % 100 == 0) {
                this.setPhasePercentage((float)var3 * 100.0F / (float)(var1 - 1));
            }

            int var4 = this.random.nextInt(this.sizeX);
            int var5 = Math.min(Math.min(this.random.nextInt(var2), this.random.nextInt(var2)), Math.min(this.random.nextInt(var2), this.random.nextInt(var2)));
            int var6 = this.random.nextInt(this.sizeZ);
            if(this.fullWorldBlocks.get((var5 * this.sizeZ + var6) * this.sizeX + var4) == 0) {
                long var7 = this.floodFill(var4, var5, var6, 0, 255);

                if(var7 > 0L && var7 < 640L) {
                    this.floodFill(var4, var5, var6, 255, Block.LAVA.id);
                } else {
                    this.floodFill(var4, var5, var6, 255, 0);
                }
            }
        }

        this.setPhasePercentage(100.0F);
    }

    protected long floodFill(int var1, int var2, int var3, int var4, int var5) {
        byte var6 = (byte)var5;
        byte var22 = (byte)var4;
        ArrayList<int[]> var7 = new ArrayList<>();
        byte var8 = 0;
        int var23 = var8 + 1;
        this.floodFillBlocks[0] = (var2 * this.sizeZ + var3) * this.sizeX + var1;
        long var14 = 0L;
        var1 = this.sizeX * this.sizeZ;

        while(var23 > 0) {
            --var23;
            var2 = this.floodFillBlocks[var23];
            if(var23 == 0 && !var7.isEmpty()) {
                this.floodFillBlocks = var7.remove(var7.size() - 1);
                var23 = this.floodFillBlocks.length;
            }

            int var16 = var2 % this.sizeX;
            int var27 = var2 / this.sizeX;

            var3 = var27 % this.sizeZ;
            int var13 = var27 / this.sizeZ;

            int var17;
            for(var17 = var16; var16 > 0 && this.fullWorldBlocks.get(var2 - 1) == var22; --var2) {
                --var16;
            }

            while(var17 < this.sizeX && this.fullWorldBlocks.get(var2 + var17 - var16) == var22) {
                ++var17;
            }

            int var28 = var2 / this.sizeX;
            int var18 = var28 % this.sizeZ;
            int var19 = var28 / this.sizeZ;
            if(var5 == 255 && (var16 == 0 || var17 == this.sizeX - 1 || var13 == 0 || var13 == 64 - 1 || var3 == 0 || var3 == this.sizeZ - 1)) {
                return -1L;
            }

            if(var18 != var3 || var19 != var13) {
                System.out.println("Diagonal flood!?");
            }

            boolean var24 = false;
            boolean var25 = false;
            boolean var20 = false;
            var14 += (long)(var17 - var16);

            for(var16 = var16; var16 < var17; ++var16) {
                this.fullWorldBlocks.put(var2, var6);
                boolean var21;
                if(var3 > 0) {
                    var21 = this.fullWorldBlocks.get(var2 - this.sizeX) == var22;
                    if(var21 && !var24) {
                        if(var23 == this.floodFillBlocks.length) {
                            var7.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[256 * 256 * 64];
                            var23 = 0;
                        }

                        this.floodFillBlocks[var23++] = var2 - this.sizeX;
                    }

                    var24 = var21;
                }

                if(var3 < this.sizeZ - 1) {
                    var21 = this.fullWorldBlocks.get(var2 + this.sizeX) == var22;
                    if(var21 && !var25) {
                        if(var23 == this.floodFillBlocks.length) {
                            var7.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[256 * 256 * 64];
                            var23 = 0;
                        }

                        this.floodFillBlocks[var23++] = var2 + this.sizeX;
                    }

                    var25 = var21;
                }

                if(var13 > 0) {
                    byte var26 = this.fullWorldBlocks.get(var2 - var1);
                    if((var6 == Block.FLOWING_LAVA.id || var6 == Block.LAVA.id) && (var26 == Block.FLOWING_WATER.id || var26 == Block.WATER.id)) {
                        this.fullWorldBlocks.put(var2 - var1, (byte)Block.STONE.id);
                    }

                    var21 = var26 == var22;
                    if(var21 && !var20) {
                        if(var23 == this.floodFillBlocks.length) {
                            var7.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[256 * 256 * 64];
                            var23 = 0;
                        }

                        this.floodFillBlocks[var23++] = var2 - var1;
                    }

                    var20 = var21;
                }

                ++var2;
            }
        }

        return var14;
    }

    protected void calculateLighting(int var1, int var2, int var3) {
        this.heightMap = new int[var1 * var3];
		Arrays.fill(this.heightMap, var2);
        int var5 = 15;

        if (this.theme.equals("Hell")) {
            var5 = 7;
        } else if (this.theme.equals("Woods")) {
            var5 = 12;
        }

        for(var3 = 0; var3 < this.sizeX; ++var3) {
            this.setPhasePercentage((float)var3 * 100.0F / (float)(this.sizeX - 1));

            for(int var12 = 0; var12 < this.sizeZ; ++var12) {
                int var13;
                for(var13 = var2 - 1; var13 > 0 && Block.BLOCKS_LIGHT_OPACITY[(this.fullWorldBlocks.get((var13 * this.sizeZ + var12) * this.sizeX + var3) & 255)] == 0; --var13) {
                }

                this.heightMap[var3 + var12 * this.sizeX] = var13 + 1;

                for(var13 = 0; var13 < var2; ++var13) {
                    int var6 = (var13 * this.sizeZ + var12) * this.sizeX + var3;
                    int var7 = this.heightMap[var3 + var12 * this.sizeX];
                    var7 = var13 >= var7 ? var5 : 0;
                    byte var14 = this.fullWorldBlocks.get(var6);
                    if(var7 < Block.BLOCKS_LIGHT_LUMINANCE[var14]) {
                        var7 = Block.BLOCKS_LIGHT_LUMINANCE[var14];
                    }

                    this.lighting.put(var6, (byte)((this.lighting.get(var6) & 240) + var7));
                }
            }
        }
    }

    protected final byte getBrightness(int var1, int var2, int var3) {
        if(var1 < 0) {
            var1 = 0;
        } else if(var1 >= this.sizeX) {
            var1 = this.sizeX - 1;
        }

        if(var2 < 0) {
            var2 = 0;
        } else if(var2 >= 64) {
            var2 = 64 - 1;
        }

        if(var3 < 0) {
            var3 = 0;
        } else if(var3 >= this.sizeZ) {
            var3 = this.sizeZ - 1;
        }

        return this.fullWorldBlocks.get((var2 * this.sizeZ + var3) * this.sizeX + var1) == Block.SLAB.id ? (var2 < 64 - 1 ? (byte)(this.lighting.get(((var2 + 1) * this.sizeZ + var3) * this.sizeX + var1) & 15) : 15) : (byte)(this.lighting.get((var2 * this.sizeZ + var3) * this.sizeX + var1) & 15);
    }

    protected final boolean hasSkyLight(int var1, int var2, int var3) {
        if(this.heightMap[var1 + var3 * this.sizeX] <= var2) {
            return true;
        } else {
            while(var2 < 64) {
                if(Block.BLOCKS_OPAQUE[this.fullWorldBlocks.get(((var2 * this.sizeZ + var3) * this.sizeX + var1) & 255)]) {
                    return false;
                }

                ++var2;
            }

            return true;
        }
    }

    protected void initLoading(String title, int phases) {
        this.title = title;
        this.phases = phases;

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BetterWorldOptions.getMinecraft().progressRenderer.progressStart(this.title);
        }

        BetterWorldOptions.LOGGER.info(this.title);
    }

    public void setCurrentStage(String stage) {
        if (!stage.equals(this.currentStage)) {
            BetterWorldOptions.LOGGER.info(stage);
        }

        this.currentStage = stage;

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BetterWorldOptions.getMinecraft().progressRenderer.progressStage(this.currentStage + "..");
        }

        ++this.phaseBar;
        this.setPhasePercentage(0.0F);
    }

    protected void setPhasePercentage(float var1) {
        this.phasePercentage = (int)(((float)(this.phaseBar - 1) + var1 / 100.0F) * 100.0F / (float)this.phases);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BetterWorldOptions.getMinecraft().progressRenderer.progressStagePercentage(this.phasePercentage);
        }
    }
}