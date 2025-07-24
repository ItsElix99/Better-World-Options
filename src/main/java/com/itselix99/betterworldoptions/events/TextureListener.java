package com.itselix99.betterworldoptions.events;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;

import static com.itselix99.betterworldoptions.BetterWorldOptions.NAMESPACE;

public class TextureListener {
    public static int alphaGrassBlockTop;
    public static int alphaGrassBlockSide;
    public static int alphaLeaves;
    public static int alphaTallGrass;
    public static int alphaFern;
    public static int alphaDiamondBlockBottom;
    public static int alphaDiamondBlockSide;
    public static int alphaGoldBlockBottom;
    public static int alphaGoldBlockSide;
    public static int alphaIronBlockBottom;
    public static int alphaIronBlockSide;
    public static int alphaCobblestone;

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        ExpandableAtlas terrainAtlas = Atlases.getTerrain();

        alphaGrassBlockTop = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_grass_block_top")).index;
        alphaGrassBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_grass_block_side")).index;
        alphaLeaves = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_leaves")).index;
        alphaTallGrass = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_tallgrass")).index;
        alphaFern = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_fern")).index;
        alphaDiamondBlockBottom = terrainAtlas.addTexture(NAMESPACE.id("block/diamond_block_bottom")).index;
        alphaDiamondBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/diamond_block_side")).index;
        alphaGoldBlockBottom = terrainAtlas.addTexture(NAMESPACE.id("block/gold_block_bottom")).index;
        alphaGoldBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/gold_block_side")).index;
        alphaIronBlockBottom = terrainAtlas.addTexture(NAMESPACE.id("block/iron_block_bottom")).index;
        alphaIronBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/iron_block_side")).index;
        alphaCobblestone = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_cobblestone")).index;
    }
}