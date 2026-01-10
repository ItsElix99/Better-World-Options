package com.itselix99.betterworldoptions.api.worldtype;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.event.TextureListener;
import com.itselix99.betterworldoptions.world.worldtypes.AltOverworldChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.FlatChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.alpha112.Alpha112ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.Alpha120ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.EarlyInfdevChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.Indev223ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.Infdev415ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.Infdev420ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev611.Infdev611ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.MCPEChunkGenerator;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class WorldType {
    private static final List<WorldTypeEntry> WORLD_TYPE_LIST = new ArrayList<>();

    public static List<WorldTypeEntry> getList() {
        return WORLD_TYPE_LIST;
    }

    public static WorldTypeEntry getWorldTypeByName(String worldTypeName) {
        return getList().stream().filter(worldTypeEntry -> worldTypeName.equals(worldTypeEntry.NAME)).toList().get(0);
    }

    public static boolean getWorldTypePropertyValue(String worldTypeName, String propertyName) {
        return getWorldTypeByName(worldTypeName).PROPERTIES.get(propertyName);
    }

    static {
        WorldTypeEntry Default = new WorldTypeEntry();
        Default.DISPLAY_NAME = "Default";
        Default.NAME = Default.DISPLAY_NAME;
        Default.ICON = "/assets/betterworldoptions/gui/default.png";
        Default.DESCRIPTION = "Minecraft's default world generator";

        Default.PROPERTIES.put("Enable Structures", true);
        Default.PROPERTIES.put("Enable Single Biome", true);
        Default.PROPERTIES.put("Enable Themes", true);
        Default.PROPERTIES.put("Enable Old Features", false);
        Default.PROPERTIES.put("Old Features Has Biomes", false);
        Default.PROPERTIES.put("Enable Finite World", true);

        WORLD_TYPE_LIST.add(Default);

        WorldTypeEntry Amplified = new WorldTypeEntry();
        Amplified.OVERWORLD_CHUNK_GENERATOR = AltOverworldChunkGenerator.class;
        Amplified.DISPLAY_NAME = "Amplified";
        Amplified.NAME = Amplified.DISPLAY_NAME;
        Amplified.ICON = "/assets/betterworldoptions/gui/default.png";
        Amplified.DESCRIPTION = "Minecraft's default world generator";
        Amplified.DESCRIPTION_2 = "but AMPLIFIED";

        Amplified.PROPERTIES.put("Enable Structures", true);
        Amplified.PROPERTIES.put("Enable Single Biome", true);
        Amplified.PROPERTIES.put("Enable Themes", true);
        Amplified.PROPERTIES.put("Enable Old Features", false);
        Amplified.PROPERTIES.put("Old Features Has Biomes", false);
        Amplified.PROPERTIES.put("Enable Finite World", true);

        WORLD_TYPE_LIST.add(Amplified);

        WorldTypeEntry Nether = new WorldTypeEntry();
        Nether.DISPLAY_NAME = "Nether";
        Nether.NAME = Nether.DISPLAY_NAME;
        Nether.ICON = "/assets/betterworldoptions/gui/nether.png";
        Nether.DESCRIPTION = "Start the world in the Nether";
        Nether.DESCRIPTION_2 = "dimension";

        Nether.PROPERTIES.put("Enable Structures", false);
        Nether.PROPERTIES.put("Enable Single Biome", false);
        Nether.PROPERTIES.put("Enable Themes", false);
        Nether.PROPERTIES.put("Enable Old Features", false);
        Nether.PROPERTIES.put("Old Features Has Biomes", false);
        Nether.PROPERTIES.put("Enable Finite World", false);

        WORLD_TYPE_LIST.add(Nether);

        WorldTypeEntry Skylands = new WorldTypeEntry();
        Skylands.DISPLAY_NAME = "Skylands";
        Skylands.NAME = Skylands.DISPLAY_NAME;
        Skylands.ICON = "/assets/betterworldoptions/gui/skylands.png";
        Skylands.DESCRIPTION = "Start the world on the floating";
        Skylands.DESCRIPTION_2 = "islands";

        Skylands.PROPERTIES.put("Enable Structures", false);
        Skylands.PROPERTIES.put("Enable Single Biome", false);
        Skylands.PROPERTIES.put("Enable Themes", false);
        Skylands.PROPERTIES.put("Enable Old Features", false);
        Skylands.PROPERTIES.put("Old Features Has Biomes", false);
        Skylands.PROPERTIES.put("Enable Finite World", false);

        WORLD_TYPE_LIST.add(Skylands);

        WorldTypeEntry Flat = new WorldTypeEntry();
        Flat.OVERWORLD_CHUNK_GENERATOR = FlatChunkGenerator.class;
        Flat.DISPLAY_NAME = "Flat";
        Flat.NAME = Flat.DISPLAY_NAME;
        Flat.ICON = "/assets/betterworldoptions/gui/flat.png";
        Flat.DESCRIPTION = "A completely flat world, perfect for";
        Flat.DESCRIPTION_2 = "building";

        Flat.PROPERTIES.put("Enable Structures", true);
        Flat.PROPERTIES.put("Enable Single Biome", true);
        Flat.PROPERTIES.put("Enable Themes", true);
        Flat.PROPERTIES.put("Enable Old Features", false);
        Flat.PROPERTIES.put("Old Features Has Biomes", false);
        Flat.PROPERTIES.put("Enable Finite World", true);

        WORLD_TYPE_LIST.add(Flat);

        WorldTypeEntry Farlands = new WorldTypeEntry();
        Farlands.OVERWORLD_CHUNK_GENERATOR = AltOverworldChunkGenerator.class;
        Farlands.DISPLAY_NAME = "Farlands";
        Farlands.NAME = Farlands.DISPLAY_NAME;
        Farlands.ICON = "/assets/betterworldoptions/gui/farlands.png";
        Farlands.DESCRIPTION = "Explore the Farlands, where terrain";
        Farlands.DESCRIPTION_2 = "generation becomes distorted";

        Farlands.PROPERTIES.put("Enable Structures", true);
        Farlands.PROPERTIES.put("Enable Single Biome", true);
        Farlands.PROPERTIES.put("Enable Themes", true);
        Farlands.PROPERTIES.put("Enable Old Features", false);
        Farlands.PROPERTIES.put("Old Features Has Biomes", false);
        Farlands.PROPERTIES.put("Enable Finite World", true);

        WORLD_TYPE_LIST.add(Farlands);

        WorldTypeEntry Alpha120 = new WorldTypeEntry();
        Alpha120.OVERWORLD_CHUNK_GENERATOR = Alpha120ChunkGenerator.class;
        Alpha120.DISPLAY_NAME = "Alpha 1.2.0";
        Alpha120.NAME = Alpha120.DISPLAY_NAME;
        Alpha120.ICON = "/assets/betterworldoptions/gui/alpha_1.2.0.png";
        Alpha120.DESCRIPTION = "Start the world with Alpha 1.2.0";
        Alpha120.DESCRIPTION_2 = "generation";

        Alpha120.PROPERTIES.put("Enable Structures", true);
        Alpha120.PROPERTIES.put("Enable Single Biome", true);
        Alpha120.PROPERTIES.put("Enable Themes", true);
        Alpha120.PROPERTIES.put("Enable Old Features", true);
        Alpha120.PROPERTIES.put("Old Features Has Biomes", true);
        Alpha120.PROPERTIES.put("Enable Finite World", true);

        Alpha120.OLD_TEXTURES.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Alpha120.OLD_TEXTURES.put("Cobblestone", TextureListener.alphaCobblestone);

        WORLD_TYPE_LIST.add(Alpha120);

        WorldTypeEntry Alpha112 = new WorldTypeEntry();
        Alpha112.OVERWORLD_CHUNK_GENERATOR = Alpha112ChunkGenerator.class;
        Alpha112.DISPLAY_NAME = "Alpha 1.1.2_01";
        Alpha112.NAME = Alpha112.DISPLAY_NAME;
        Alpha112.ICON = "/assets/betterworldoptions/gui/alpha_1.1.2_01.png";
        Alpha112.DESCRIPTION = "Start the world with Alpha 1.1.2_01";
        Alpha112.DESCRIPTION_2 = "generation";

        Alpha112.PROPERTIES.put("Enable Structures", true);
        Alpha112.PROPERTIES.put("Enable Single Biome", true);
        Alpha112.PROPERTIES.put("Enable Themes", true);
        Alpha112.PROPERTIES.put("Enable Old Features", true);
        Alpha112.PROPERTIES.put("Old Features Has Biomes", false);
        Alpha112.PROPERTIES.put("Enable Finite World", true);

        Alpha112.OLD_TEXTURES.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        Alpha112.OLD_TEXTURES.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Alpha112.OLD_TEXTURES.put("Cobblestone", TextureListener.alphaCobblestone);
        Alpha112.OLD_TEXTURES.put("IronBlockTop", TextureListener.alphaIronBlock);
        Alpha112.OLD_TEXTURES.put("IronBlockSide", TextureListener.alphaIronBlockSide);
        Alpha112.OLD_TEXTURES.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        Alpha112.OLD_TEXTURES.put("GoldBlockTop", TextureListener.alphaGoldBlock);
        Alpha112.OLD_TEXTURES.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        Alpha112.OLD_TEXTURES.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        Alpha112.OLD_TEXTURES.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        Alpha112.OLD_TEXTURES.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        Alpha112.OLD_TEXTURES.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        Alpha112.OLD_TEXTURES.put("Grass", TextureListener.alphaTallGrass);
        Alpha112.OLD_TEXTURES.put("Fern", TextureListener.alphaFern);
        Alpha112.OLD_TEXTURES.put("Leaves", TextureListener.alphaLeaves);
        Alpha112.OLD_TEXTURES.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        Alpha112.OLD_TEXTURES.put("FurnaceTop", Block.STONE.textureId);

        WORLD_TYPE_LIST.add(Alpha112);

        WorldTypeEntry Infdev611 = new WorldTypeEntry();
        Infdev611.OVERWORLD_CHUNK_GENERATOR = Infdev611ChunkGenerator.class;
        Infdev611.DISPLAY_NAME = "Infdev 20100611";
        Infdev611.NAME = "Infdev 611";
        Infdev611.ICON = "/assets/betterworldoptions/gui/infdev_20100611.png";
        Infdev611.DESCRIPTION = "Start the world with Infdev 611";
        Infdev611.DESCRIPTION_2 = "generation";

        Infdev611.PROPERTIES.put("Enable Structures", true);
        Infdev611.PROPERTIES.put("Enable Single Biome", true);
        Infdev611.PROPERTIES.put("Enable Themes", true);
        Infdev611.PROPERTIES.put("Enable Old Features", true);
        Infdev611.PROPERTIES.put("Old Features Has Biomes", false);
        Infdev611.PROPERTIES.put("Enable Finite World", true);

        Infdev611.OLD_TEXTURES.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        Infdev611.OLD_TEXTURES.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Infdev611.OLD_TEXTURES.put("Cobblestone", TextureListener.alphaCobblestone);
        Infdev611.OLD_TEXTURES.put("IronBlockTop", TextureListener.alphaIronBlock);
        Infdev611.OLD_TEXTURES.put("IronBlockSide", TextureListener.alphaIronBlockSide);
        Infdev611.OLD_TEXTURES.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        Infdev611.OLD_TEXTURES.put("GoldBlockTop", TextureListener.alphaGoldBlock);
        Infdev611.OLD_TEXTURES.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        Infdev611.OLD_TEXTURES.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        Infdev611.OLD_TEXTURES.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        Infdev611.OLD_TEXTURES.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        Infdev611.OLD_TEXTURES.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        Infdev611.OLD_TEXTURES.put("Grass", TextureListener.alphaTallGrass);
        Infdev611.OLD_TEXTURES.put("Fern", TextureListener.alphaFern);
        Infdev611.OLD_TEXTURES.put("Leaves", TextureListener.alphaLeaves);
        Infdev611.OLD_TEXTURES.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        Infdev611.OLD_TEXTURES.put("FurnaceTop", Block.STONE.textureId);
        Infdev611.OLD_TEXTURES.put("BrickBlock", TextureListener.infdevBricksBlock);

        WORLD_TYPE_LIST.add(Infdev611);

        WorldTypeEntry Infdev420 = new WorldTypeEntry();
        Infdev420.OVERWORLD_CHUNK_GENERATOR = Infdev420ChunkGenerator.class;
        Infdev420.DISPLAY_NAME = "Infdev 20100420";
        Infdev420.NAME = "Infdev 420";
        Infdev420.ICON = "/assets/betterworldoptions/gui/infdev_20100420.png";
        Infdev420.DESCRIPTION = "Start the world with Infdev 420";
        Infdev420.DESCRIPTION_2 = "generation";

        Infdev420.PROPERTIES.put("Enable Structures", true);
        Infdev420.PROPERTIES.put("Enable Single Biome", true);
        Infdev420.PROPERTIES.put("Enable Themes", true);
        Infdev420.PROPERTIES.put("Enable Old Features", true);
        Infdev420.PROPERTIES.put("Old Features Has Biomes", false);
        Infdev420.PROPERTIES.put("Enable Finite World", true);

        Infdev420.OLD_TEXTURES.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        Infdev420.OLD_TEXTURES.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Infdev420.OLD_TEXTURES.put("Cobblestone", TextureListener.alphaCobblestone);
        Infdev420.OLD_TEXTURES.put("IronBlockTop", TextureListener.alphaIronBlock);
        Infdev420.OLD_TEXTURES.put("IronBlockSide", TextureListener.alphaIronBlockSide);
        Infdev420.OLD_TEXTURES.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        Infdev420.OLD_TEXTURES.put("GoldBlockTop", TextureListener.alphaGoldBlock);
        Infdev420.OLD_TEXTURES.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        Infdev420.OLD_TEXTURES.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        Infdev420.OLD_TEXTURES.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        Infdev420.OLD_TEXTURES.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        Infdev420.OLD_TEXTURES.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        Infdev420.OLD_TEXTURES.put("Grass", TextureListener.alphaTallGrass);
        Infdev420.OLD_TEXTURES.put("Fern", TextureListener.alphaFern);
        Infdev420.OLD_TEXTURES.put("Leaves", TextureListener.alphaLeaves);
        Infdev420.OLD_TEXTURES.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        Infdev420.OLD_TEXTURES.put("FurnaceTop", Block.STONE.textureId);
        Infdev420.OLD_TEXTURES.put("BrickBlock", TextureListener.infdevBricksBlock);

        WORLD_TYPE_LIST.add(Infdev420);

        WorldTypeEntry Infdev415 = new WorldTypeEntry();
        Infdev415.OVERWORLD_CHUNK_GENERATOR = Infdev415ChunkGenerator.class;
        Infdev415.DISPLAY_NAME = "Infdev 20100415";
        Infdev415.NAME = "Infdev 415";
        Infdev415.ICON = "/assets/betterworldoptions/gui/infdev_20100415.png";
        Infdev415.DESCRIPTION = "Start the world with Infdev 415";
        Infdev415.DESCRIPTION_2 = "generation";

        Infdev415.PROPERTIES.put("Enable Structures", true);
        Infdev415.PROPERTIES.put("Enable Single Biome", true);
        Infdev415.PROPERTIES.put("Enable Themes", true);
        Infdev415.PROPERTIES.put("Enable Old Features", true);
        Infdev415.PROPERTIES.put("Old Features Has Biomes", false);
        Infdev415.PROPERTIES.put("Enable Finite World", true);

        Infdev415.OLD_TEXTURES.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        Infdev415.OLD_TEXTURES.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Infdev415.OLD_TEXTURES.put("Cobblestone", TextureListener.alphaCobblestone);
        Infdev415.OLD_TEXTURES.put("IronBlockTop", TextureListener.alphaIronBlock);
        Infdev415.OLD_TEXTURES.put("IronBlockSide", TextureListener.alphaIronBlockSide);
        Infdev415.OLD_TEXTURES.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        Infdev415.OLD_TEXTURES.put("GoldBlockTop", TextureListener.alphaGoldBlock);
        Infdev415.OLD_TEXTURES.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        Infdev415.OLD_TEXTURES.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        Infdev415.OLD_TEXTURES.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        Infdev415.OLD_TEXTURES.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        Infdev415.OLD_TEXTURES.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        Infdev415.OLD_TEXTURES.put("Grass", TextureListener.alphaTallGrass);
        Infdev415.OLD_TEXTURES.put("Fern", TextureListener.alphaFern);
        Infdev415.OLD_TEXTURES.put("Leaves", TextureListener.alphaLeaves);
        Infdev415.OLD_TEXTURES.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        Infdev415.OLD_TEXTURES.put("FurnaceTop", Block.STONE.textureId);
        Infdev415.OLD_TEXTURES.put("BrickBlock", TextureListener.infdevBricksBlock);

        WORLD_TYPE_LIST.add(Infdev415);

        WorldTypeEntry EarlyInfdev = new WorldTypeEntry();
        EarlyInfdev.OVERWORLD_CHUNK_GENERATOR = EarlyInfdevChunkGenerator.class;
        EarlyInfdev.DISPLAY_NAME = "Early Infdev";
        EarlyInfdev.NAME = EarlyInfdev.DISPLAY_NAME;
        EarlyInfdev.ICON = "/assets/betterworldoptions/gui/early_infdev.png";
        EarlyInfdev.DESCRIPTION = "Start the world with Infdev 227-325";
        EarlyInfdev.DESCRIPTION_2 = "generation";

        EarlyInfdev.PROPERTIES.put("Enable Structures", true);
        EarlyInfdev.PROPERTIES.put("Enable Single Biome", true);
        EarlyInfdev.PROPERTIES.put("Enable Themes", true);
        EarlyInfdev.PROPERTIES.put("Enable Old Features", true);
        EarlyInfdev.PROPERTIES.put("Old Features Has Biomes", false);
        EarlyInfdev.PROPERTIES.put("Enable Finite World", true);

        EarlyInfdev.OLD_TEXTURES.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        EarlyInfdev.OLD_TEXTURES.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        EarlyInfdev.OLD_TEXTURES.put("Cobblestone", TextureListener.alphaCobblestone);
        EarlyInfdev.OLD_TEXTURES.put("IronBlockTop", TextureListener.alphaIronBlock);
        EarlyInfdev.OLD_TEXTURES.put("IronBlockSide", TextureListener.alphaIronBlockSide);
        EarlyInfdev.OLD_TEXTURES.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        EarlyInfdev.OLD_TEXTURES.put("GoldBlockTop", TextureListener.alphaGoldBlock);
        EarlyInfdev.OLD_TEXTURES.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        EarlyInfdev.OLD_TEXTURES.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        EarlyInfdev.OLD_TEXTURES.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        EarlyInfdev.OLD_TEXTURES.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        EarlyInfdev.OLD_TEXTURES.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        EarlyInfdev.OLD_TEXTURES.put("Grass", TextureListener.alphaTallGrass);
        EarlyInfdev.OLD_TEXTURES.put("Fern", TextureListener.alphaFern);
        EarlyInfdev.OLD_TEXTURES.put("Leaves", TextureListener.alphaLeaves);
        EarlyInfdev.OLD_TEXTURES.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        EarlyInfdev.OLD_TEXTURES.put("FurnaceTop", Block.STONE.textureId);
        EarlyInfdev.OLD_TEXTURES.put("BrickBlock", TextureListener.infdevBricksBlock);

        WORLD_TYPE_LIST.add(EarlyInfdev);

        WorldTypeEntry Indev223 = new WorldTypeEntry();
        Indev223.OVERWORLD_CHUNK_GENERATOR = Indev223ChunkGenerator.class;
        Indev223.DISPLAY_NAME = "Indev 20100223";
        Indev223.NAME = "Indev 223";
        Indev223.ICON = "/assets/betterworldoptions/gui/indev_20100223.png";
        Indev223.DESCRIPTION = "Start the world with Indev 223";
        Indev223.DESCRIPTION_2 = "generation";

        Indev223.PROPERTIES.put("Enable Structures", true);
        Indev223.PROPERTIES.put("Enable Single Biome", true);
        Indev223.PROPERTIES.put("Enable Themes", true);
        Indev223.PROPERTIES.put("Enable Old Features", true);
        Indev223.PROPERTIES.put("Old Features Has Biomes", false);
        Indev223.PROPERTIES.put("Enable Finite World", true);

        Indev223.OLD_TEXTURES.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        Indev223.OLD_TEXTURES.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Indev223.OLD_TEXTURES.put("Cobblestone", TextureListener.alphaCobblestone);
        Indev223.OLD_TEXTURES.put("IronBlockTop", TextureListener.alphaIronBlock);
        Indev223.OLD_TEXTURES.put("IronBlockSide", TextureListener.alphaIronBlockSide);
        Indev223.OLD_TEXTURES.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        Indev223.OLD_TEXTURES.put("GoldBlockTop", TextureListener.alphaGoldBlock);
        Indev223.OLD_TEXTURES.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        Indev223.OLD_TEXTURES.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        Indev223.OLD_TEXTURES.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        Indev223.OLD_TEXTURES.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        Indev223.OLD_TEXTURES.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        Indev223.OLD_TEXTURES.put("Grass", TextureListener.alphaTallGrass);
        Indev223.OLD_TEXTURES.put("Fern", TextureListener.alphaFern);
        Indev223.OLD_TEXTURES.put("Leaves", TextureListener.alphaLeaves);
        Indev223.OLD_TEXTURES.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        Indev223.OLD_TEXTURES.put("FurnaceTop", Block.STONE.textureId);
        Indev223.OLD_TEXTURES.put("BrickBlock", TextureListener.infdevBricksBlock);

        WORLD_TYPE_LIST.add(Indev223);

        WorldTypeEntry MCPE = new WorldTypeEntry();
        MCPE.OVERWORLD_CHUNK_GENERATOR = MCPEChunkGenerator.class;
        MCPE.DISPLAY_NAME = "MCPE";
        MCPE.NAME = MCPE.DISPLAY_NAME;
        MCPE.ICON = "/assets/betterworldoptions/gui/mcpe.png";
        MCPE.DESCRIPTION = "Start the world with MCPE 0.1.0-0.8.1";
        MCPE.DESCRIPTION_2 = "generation";

        MCPE.PROPERTIES.put("Enable Structures", true);
        MCPE.PROPERTIES.put("Enable Single Biome", true);
        MCPE.PROPERTIES.put("Enable Themes", true);
        MCPE.PROPERTIES.put("Enable Old Features", true);
        MCPE.PROPERTIES.put("Old Features Has Biomes", true);
        MCPE.PROPERTIES.put("Enable Finite World", true);

        MCPE.OLD_TEXTURES.put("GrassBlockSide", TextureListener.mcpeGrassBlockSide);
        MCPE.OLD_TEXTURES.put("Leaves", TextureListener.alphaLeaves);
        MCPE.OLD_TEXTURES.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        MCPE.OLD_TEXTURES.put("Rose", TextureListener.mcpeRose);
        MCPE.OLD_TEXTURES.put("IceBlock", TextureListener.mcpeIceBlock);

        WORLD_TYPE_LIST.add(MCPE);

        if (CompatMods.AetherLoaded()) {
            WorldTypeEntry Aether = new WorldTypeEntry();
            Aether.DISPLAY_NAME = "Aether";
            Aether.NAME = Aether.DISPLAY_NAME;
            Aether.ICON = "/assets/betterworldoptions/gui/aether.png";
            Aether.DESCRIPTION = "Start the world in a hostile paradise";

            Aether.PROPERTIES.put("Enable Structures", false);
            Aether.PROPERTIES.put("Enable Single Biome", false);
            Aether.PROPERTIES.put("Enable Themes", false);
            Aether.PROPERTIES.put("Enable Old Features", false);
            Aether.PROPERTIES.put("Old Features Has Biomes", false);
            Aether.PROPERTIES.put("Enable Finite World", false);

            WORLD_TYPE_LIST.add(Aether);
        }
    }
}