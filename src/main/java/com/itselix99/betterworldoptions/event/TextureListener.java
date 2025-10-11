package com.itselix99.betterworldoptions.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;

import static com.itselix99.betterworldoptions.BetterWorldOptions.NAMESPACE;

public class TextureListener {
    public static int invisibleBedrock;

    public static int alphaGrassBlockTop;
    public static int alphaGrassBlockSide;

    public static int alphaCobblestone;

    public static int alphaIronBlock;
    public static int alphaIronBlockSide;
    public static int alphaIronBlockBottom;

    public static int alphaGoldBlock;
    public static int alphaGoldBlockSide;
    public static int alphaGoldBlockBottom;

    public static int alphaDiamondBlock;
    public static int alphaDiamondBlockSide;
    public static int alphaDiamondBlockBottom;

    public static int alphaTallGrass;
    public static int alphaFern;

    public static int alphaLeaves;
    public static int alphaLeavesOpaque;

    public static int infdevBricksBlock;

    public static int mcpeGrassBlockSide;

    public static int mcpeRose;

    public static int mcpeLeaves;
    public static int mcpeLeavesOpaque;

    public static int mcpeIceBlock;

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        ExpandableAtlas terrainAtlas = Atlases.getTerrain();

        invisibleBedrock = terrainAtlas.addTexture(NAMESPACE.id("block/invisible_bedrock")).index;

        alphaGrassBlockTop = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_grass_block_top")).index;
        alphaGrassBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_grass_block_side")).index;

        alphaCobblestone = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_cobblestone")).index;

        alphaIronBlock = terrainAtlas.addTexture(NAMESPACE.id("block/iron_block")).index;
        alphaIronBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/iron_block_side")).index;
        alphaIronBlockBottom = terrainAtlas.addTexture(NAMESPACE.id("block/iron_block_bottom")).index;

        alphaGoldBlock = terrainAtlas.addTexture(NAMESPACE.id("block/gold_block")).index;
        alphaGoldBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/gold_block_side")).index;
        alphaGoldBlockBottom = terrainAtlas.addTexture(NAMESPACE.id("block/gold_block_bottom")).index;

        alphaDiamondBlock = terrainAtlas.addTexture(NAMESPACE.id("block/diamond_block")).index;
        alphaDiamondBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/diamond_block_side")).index;
        alphaDiamondBlockBottom = terrainAtlas.addTexture(NAMESPACE.id("block/diamond_block_bottom")).index;

        alphaTallGrass = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_tallgrass")).index;
        alphaFern = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_fern")).index;

        alphaLeaves = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_leaves")).index;
        alphaLeavesOpaque = terrainAtlas.addTexture(NAMESPACE.id("block/alpha_leaves_opaque")).index;

        infdevBricksBlock = terrainAtlas.addTexture(NAMESPACE.id("block/infdev_bricks_block")).index;

        mcpeGrassBlockSide = terrainAtlas.addTexture(NAMESPACE.id("block/mcpe_grass_block_side")).index;

        mcpeRose = terrainAtlas.addTexture(NAMESPACE.id("block/mcpe_rose")).index;

        mcpeLeaves = terrainAtlas.addTexture(NAMESPACE.id("block/mcpe_leaves")).index;
        mcpeLeavesOpaque = terrainAtlas.addTexture(NAMESPACE.id("block/mcpe_leaves_opaque")).index;

        mcpeIceBlock = terrainAtlas.addTexture(NAMESPACE.id("block/mcpe_ice_block")).index;
    }
}