package com.itselix99.betterworldoptions.events;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class TextureListener {
    @Entrypoint.Namespace
    public static Namespace MOD_ID = Null.get();

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

        alphaGrassBlockTop = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_grass_block_top")).index;
        alphaGrassBlockSide = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_grass_block_side")).index;
        alphaLeaves = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_leaves")).index;
        alphaTallGrass = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_tallgrass")).index;
        alphaFern = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_fern")).index;
        alphaDiamondBlockBottom = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/diamond_block_bottom")).index;
        alphaDiamondBlockSide = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/diamond_block_side")).index;
        alphaGoldBlockBottom = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/gold_block_bottom")).index;
        alphaGoldBlockSide = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/gold_block_side")).index;
        alphaIronBlockBottom = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/iron_block_bottom")).index;
        alphaIronBlockSide = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/iron_block_side")).index;
        alphaCobblestone = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_cobblestone")).index;
    }
}