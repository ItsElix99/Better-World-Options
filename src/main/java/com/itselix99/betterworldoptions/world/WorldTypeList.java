package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.world.worldtypes.FarlandsChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.FlatChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.alpha.AlphaChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.EarlyBetaChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.EarlyInfdevChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.Indev223ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.Infdev415ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.Infdev420ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev611.Infdev611ChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;

import java.util.ArrayList;
import java.util.List;

public class WorldTypeList {
    private static List<WorldTypeEntry> WORLD_TYPE_LIST = new ArrayList<>();

    static {
        WorldTypeEntry Default = new WorldTypeEntry();
        Default.OVERWORLD_CHUNK_GENERATOR = OverworldChunkGenerator.class;
        Default.DISPLAY_NAME = "Default";
        Default.NAME = Default.DISPLAY_NAME;
        Default.DESCRIPTION = "Minecraft's default world generator";
        Default.LIGHTING_MODE = "Overworld";
        Default.SKY_DISABLED = false;
        Default.BIOME = null;
        Default.BLOCK_TO_SPAWN_ON = Block.SAND.id;
        WORLD_TYPE_LIST.add(Default);

        WorldTypeEntry Nether = new WorldTypeEntry();
        Nether.OVERWORLD_CHUNK_GENERATOR = OverworldChunkGenerator.class;
        Nether.DISPLAY_NAME = "Nether";
        Nether.NAME = Nether.DISPLAY_NAME;
        Nether.DESCRIPTION = "Start the world in the Nether dimension";
        Nether.LIGHTING_MODE = "Overworld";
        Nether.SKY_DISABLED = false;
        Nether.BIOME = null;
        Nether.BLOCK_TO_SPAWN_ON = Block.SAND.id;
        WORLD_TYPE_LIST.add(Nether);

        WorldTypeEntry Skylands = new WorldTypeEntry();
        Skylands.OVERWORLD_CHUNK_GENERATOR = OverworldChunkGenerator.class;
        Skylands.DISPLAY_NAME = "Skylands";
        Skylands.NAME = Skylands.DISPLAY_NAME;
        Skylands.DESCRIPTION = "Start the world on the floating islands";
        Skylands.LIGHTING_MODE = "Overworld";
        Skylands.SKY_DISABLED = false;
        Skylands.BIOME = null;
        Skylands.BLOCK_TO_SPAWN_ON = Block.SAND.id;
        WORLD_TYPE_LIST.add(Skylands);

        WorldTypeEntry Flat = new WorldTypeEntry();
        Flat.OVERWORLD_CHUNK_GENERATOR = FlatChunkGenerator.class;
        Flat.DISPLAY_NAME = "Flat";
        Flat.NAME = Flat.DISPLAY_NAME;
        Flat.DESCRIPTION = "A completely flat world, perfect for building";
        Flat.LIGHTING_MODE = "Overworld";
        Flat.SKY_DISABLED = false;
        Flat.BIOME = null;
        Flat.BLOCK_TO_SPAWN_ON = Block.GRASS_BLOCK.id;
        WORLD_TYPE_LIST.add(Flat);

        WorldTypeEntry Farlands = new WorldTypeEntry();
        Farlands.OVERWORLD_CHUNK_GENERATOR = FarlandsChunkGenerator.class;
        Farlands.DISPLAY_NAME = "Farlands";
        Farlands.NAME = Farlands.DISPLAY_NAME;
        Farlands.DESCRIPTION = "Explore the Farlands, where terrain gen";
        Farlands.DESCRIPTION_2 = "becomes distorted and unpredictable";
        Farlands.LIGHTING_MODE = "Overworld";
        Farlands.SKY_DISABLED = false;
        Farlands.BIOME = null;
        Farlands.BLOCK_TO_SPAWN_ON = Block.GRASS_BLOCK.id;
        WORLD_TYPE_LIST.add(Farlands);

        WorldTypeEntry EarlyBeta = new WorldTypeEntry();
        EarlyBeta.OVERWORLD_CHUNK_GENERATOR = EarlyBetaChunkGenerator.class;
        EarlyBeta.DISPLAY_NAME = "Beta 1.1_02";
        EarlyBeta.NAME = EarlyBeta.DISPLAY_NAME;
        EarlyBeta.DESCRIPTION = "Start the world with Beta 1.1_02 generation";
        EarlyBeta.LIGHTING_MODE = "Overworld";
        EarlyBeta.SKY_DISABLED = false;
        EarlyBeta.BIOME = null;
        EarlyBeta.BLOCK_TO_SPAWN_ON = Block.SAND.id;
        WORLD_TYPE_LIST.add(EarlyBeta);

        WorldTypeEntry Alpha = new WorldTypeEntry();
        Alpha.OVERWORLD_CHUNK_GENERATOR = AlphaChunkGenerator.class;
        Alpha.DISPLAY_NAME = "Alpha 1.1.2_01";
        Alpha.NAME = Alpha.DISPLAY_NAME;
        Alpha.DESCRIPTION = "Start the world with Alpha 1.1.2_01 generation";
        Alpha.LIGHTING_MODE = "Overworld";
        Alpha.SKY_DISABLED = false;
        Alpha.BIOME = null;
        Alpha.BLOCK_TO_SPAWN_ON = Block.SAND.id;
        WORLD_TYPE_LIST.add(Alpha);

        WorldTypeEntry Infdev611 = new WorldTypeEntry();
        Infdev611.OVERWORLD_CHUNK_GENERATOR = Infdev611ChunkGenerator.class;
        Infdev611.DISPLAY_NAME = "Infdev 20100611";
        Infdev611.NAME = "Infdev 611";
        Infdev611.DESCRIPTION = "Start the world with Infdev 611 generation";
        Infdev611.LIGHTING_MODE = "Overworld";
        Infdev611.SKY_DISABLED = false;
        Infdev611.BIOME = null;
        Infdev611.BLOCK_TO_SPAWN_ON = Block.GRASS_BLOCK.id;
        WORLD_TYPE_LIST.add(Infdev611);

        WorldTypeEntry Infdev420 = new WorldTypeEntry();
        Infdev420.OVERWORLD_CHUNK_GENERATOR = Infdev420ChunkGenerator.class;
        Infdev420.DISPLAY_NAME = "Infdev 20100420";
        Infdev420.NAME = "Infdev 420";
        Infdev420.DESCRIPTION = "Start the world with Infdev 420 generation";
        Infdev420.LIGHTING_MODE = "Overworld";
        Infdev420.SKY_DISABLED = false;
        Infdev420.BIOME = null;
        Infdev420.BLOCK_TO_SPAWN_ON = Block.GRASS_BLOCK.id;
        WORLD_TYPE_LIST.add(Infdev420);

        WorldTypeEntry Infdev415 = new WorldTypeEntry();
        Infdev415.OVERWORLD_CHUNK_GENERATOR = Infdev415ChunkGenerator.class;
        Infdev415.DISPLAY_NAME = "Infdev 20100415";
        Infdev415.NAME = "Infdev 415";
        Infdev415.DESCRIPTION = "Start the world with Infdev 415 generation";
        Infdev415.LIGHTING_MODE = "Overworld";
        Infdev415.SKY_DISABLED = false;
        Infdev415.BIOME = null;
        Infdev415.BLOCK_TO_SPAWN_ON = Block.GRASS_BLOCK.id;
        WORLD_TYPE_LIST.add(Infdev415);

        WorldTypeEntry EarlyInfdev = new WorldTypeEntry();
        EarlyInfdev.OVERWORLD_CHUNK_GENERATOR = EarlyInfdevChunkGenerator.class;
        EarlyInfdev.DISPLAY_NAME = "Early Infdev";
        EarlyInfdev.NAME = EarlyInfdev.DISPLAY_NAME;
        EarlyInfdev.DESCRIPTION = "Start the world with Infdev 227-325 gen";
        EarlyInfdev.LIGHTING_MODE = "Overworld";
        EarlyInfdev.SKY_DISABLED = false;
        EarlyInfdev.BIOME = null;
        EarlyInfdev.BLOCK_TO_SPAWN_ON = Block.GRASS_BLOCK.id;
        WORLD_TYPE_LIST.add(EarlyInfdev);

        WorldTypeEntry Indev223 = new WorldTypeEntry();
        Indev223.OVERWORLD_CHUNK_GENERATOR = Indev223ChunkGenerator.class;
        Indev223.DISPLAY_NAME = "Indev 20100223";
        Indev223.NAME = "Indev 223";
        Indev223.DESCRIPTION = "Start the world with Indev 223 generation";
        Indev223.LIGHTING_MODE = "Overworld";
        Indev223.SKY_DISABLED = false;
        Indev223.BIOME = null;
        Indev223.BLOCK_TO_SPAWN_ON = Block.GRASS_BLOCK.id;
        WORLD_TYPE_LIST.add(Indev223);

        if (CompatMods.AetherLoaded()) {
            WorldTypeEntry Aether = new WorldTypeEntry();
            Aether.OVERWORLD_CHUNK_GENERATOR = OverworldChunkGenerator.class;
            Aether.DISPLAY_NAME = "Aether";
            Aether.NAME = Aether.DISPLAY_NAME;
            Aether.DESCRIPTION = "Start the world in a hostile paradise";
            Aether.LIGHTING_MODE = "Overworld";
            Aether.SKY_DISABLED = false;
            Aether.BIOME = null;
            Aether.BLOCK_TO_SPAWN_ON = 0;
            WORLD_TYPE_LIST.add(Aether);
        }
    }

    public static void selectWorldType(String worldTypeName) throws NoSuchMethodException {
        for (WorldTypeEntry var2 : WORLD_TYPE_LIST) {
            if (var2.NAME.equals(worldTypeName)) {
                setWorldType(var2);
                return;
            }
        }
    }


    public static void setWorldType(WorldTypeEntry worldType) throws NoSuchMethodException {
        WorldSettings.World.setChunkGenerator(worldType.OVERWORLD_CHUNK_GENERATOR.getConstructor(World.class, long.class));
        WorldSettings.World.setDisplayWorldTypeName(worldType.DISPLAY_NAME);
        WorldSettings.World.setWorldTypeName(worldType.NAME);
        WorldSettings.World.setLightingMode(worldType.LIGHTING_MODE);
        WorldSettings.World.setSkyDisabled(worldType.SKY_DISABLED);
        WorldSettings.World.setSingleBiome(worldType.BIOME);
        WorldSettings.World.setBlockToSpawnOn(worldType.BLOCK_TO_SPAWN_ON);
    }

    public static List<WorldTypeEntry> getList() {
        return WORLD_TYPE_LIST;
    }

    public static class WorldTypeEntry {
        public Class<? extends ChunkSource> OVERWORLD_CHUNK_GENERATOR;
        public Class<? extends ChunkSource> NETHER_CHUNK_GENERATOR;
        public Class<? extends ChunkSource> SKYLANDS_CHUNK_GENERATOR;
        public String DISPLAY_NAME;
        public String NAME;
        public String DESCRIPTION;
        public String DESCRIPTION_2;
        public String LIGHTING_MODE;
        public boolean SKY_DISABLED;
        public Biome BIOME;
        public int BLOCK_TO_SPAWN_ON;
    }
}