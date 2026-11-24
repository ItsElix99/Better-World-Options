package com.itselix99.betterworldoptions.api;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;

public class MissingBlocks {
    public static Block ironBars = Block.FENCE;
    public static Block glassPane = Block.GLASS;
    private static Block mycelium = Block.GRASS_BLOCK;
    public static Block brownMushroomBlock = Block.WOOL;
    public static Block redMushroomBlock = Block.WOOL;
    public static Block lilyPad = null;
    public static Block vine = null;
    public static Block sandstoneStairs = Block.SANDSTONE;

    public static void setMyceliumBlock(Block myceliumBlock) {
        mycelium = myceliumBlock;
        BetterWorldOptions.MUSHROOM_ISLAND.topBlockId = (byte) myceliumBlock.id;
        BetterWorldOptions.MUSHROOM_ISLAND.setGrassColorProvider(BiomeColorsImpl.DEFAULT_GRASS_COLOR);
        BetterWorldOptions.MUSHROOM_ISLAND_SHORE.topBlockId = (byte) myceliumBlock.id;
        BetterWorldOptions.MUSHROOM_ISLAND_SHORE.setGrassColorProvider(BiomeColorsImpl.DEFAULT_GRASS_COLOR);
    }

    public static Block getMyceliumBlock() {
        return mycelium;
    }
}