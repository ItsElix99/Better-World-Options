package com.itselix99.betterworldoptions.world.chunk;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EmptyFlattenedChunk extends FlattenedChunk {

    public EmptyFlattenedChunk(World world, int xPos, int zPos) {
        super(world, xPos, zPos);
    }

    public boolean chunkPosEquals(int x, int z) {
        return x == this.x && z == this.z;
    }

    public int getHeight(int x, int z) {
        return 0;
    }

    public void method_857() {
    }

    @Environment(EnvType.CLIENT)
    public void populateHeightMapOnly() {
    }

    public void populateHeightMap() {
    }

    public void populateBlockLight() {
    }

    public int getBlockId(int x, int y, int z) {
        return BetterWorldOptions.INVISIBLE_BEDROCK.id;
    }

    public boolean setBlock(int x, int y, int z, int rawId, int meta) {
        return true;
    }

    public boolean setBlock(int x, int y, int z, int rawId) {
        return true;
    }

    public int getBlockMeta(int x, int y, int z) {
        return 0;
    }

    public void setBlockMeta(int x, int y, int z, int meta) {
    }

    public int getLight(LightType lightType, int x, int y, int z) {
        return 0;
    }

    public void setLight(LightType lightType, int x, int y, int z, int value) {
    }

    public int getLight(int x, int y, int z, int ambientDarkness) {
        return 0;
    }

    public void addEntity(Entity entity) {
    }

    public void removeEntity(Entity entity) {
    }

    public void removeEntity(Entity entity, int chunkSlice) {
    }

    public boolean isAboveMaxHeight(int x, int y, int z) {
        return false;
    }

    public BlockEntity getBlockEntity(int x, int y, int z) {
        return null;
    }

    public void addBlockEntity(BlockEntity blockEntity) {
    }

    public void setBlockEntity(int localX, int y, int localZ, BlockEntity blockEntity) {
    }

    public void removeBlockEntityAt(int localX, int y, int localZ) {
    }

    public void load() {
    }

    public void unload() {
    }

    public void markDirty() {
    }

    public void collectOtherEntities(Entity except, Box box, List result) {
    }

    public void collectEntitiesByClass(Class entityClass, Box box, List result) {
    }

    public boolean shouldSave(boolean saveEntities) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public int loadFromPacket(byte[] bytes, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int offset) {
        int var9 = maxX - minX;
        int var10 = maxY - minY;
        int var11 = maxZ - minZ;
        int var12 = var9 * var10 * var11;
        return var12 + var12 / 2 * 3;
    }

    @Environment(EnvType.SERVER)
    public int toPacket(byte[] bytes, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int offset) {
        int var9 = maxX - minX;
        int var10 = maxY - minY;
        int var11 = maxZ - minZ;
        int var12 = var9 * var10 * var11;
        int var13 = var12 + var12 / 2 * 3;
        Arrays.fill(bytes, offset, offset + var13, (byte)0);
        return var13;
    }

    public Random getSlimeRandom(long scrambler) {
        return new Random(this.world.getSeed() + ((long) this.x * this.x * 4987142) + ((long) this.x * 5947611) + ((long) this.z * this.z) * 4392871L + ((long) this.z * 389711) ^ scrambler);
    }

    public boolean isEmpty() {
        return true;
    }
}