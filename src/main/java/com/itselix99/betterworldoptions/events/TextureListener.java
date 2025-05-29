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

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        ExpandableAtlas terrainAtlas = Atlases.getTerrain();

        alphaGrassBlockTop = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_grass_block_top")).index;
        alphaGrassBlockSide = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_grass_block_side")).index;
        alphaLeaves = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_leaves")).index;
        alphaTallGrass = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_tallgrass")).index;
        alphaFern = terrainAtlas.addTexture(Identifier.of(MOD_ID, "block/alpha_fern")).index;
    }
}