package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.world.worldtypes.FarlandsChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.FlatChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.alpha112.Alpha112_ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.beta11.Beta11_ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.EarlyInfdevChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.Infdev415ChunkGenerator;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.Infdev420ChunkGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;

import java.util.ArrayList;
import java.util.List;

public class WorldTypeList {
    public static boolean inited = false;
    public static List<WorldTypeEntry> worldtypeList;

    private static void init() {
        worldtypeList = new ArrayList<>();
        WorldTypeList.WorldTypeEntry var1 = new WorldTypeList.WorldTypeEntry();
        var1.loadedClass = OverworldChunkGenerator.class;
        var1.name = "Default";
        var1.desc = "Minecraft's default world generator";
        var1.lightingMode = 0;
        var1.noSky = false;
        var1.biome = null;
        var1.blockToSpawnOn = Block.SAND.id;
        worldtypeList.add(var1);

        WorldTypeList.WorldTypeEntry var2 = new WorldTypeList.WorldTypeEntry();
        var2.loadedClass = OverworldChunkGenerator.class;
        var2.name = "Nether";
        var2.desc = "Start the world in the Nether dimension";
        var2.lightingMode = 0;
        var2.noSky = false;
        var2.biome = null;
        var2.blockToSpawnOn = Block.SAND.id;
        worldtypeList.add(var2);

        WorldTypeList.WorldTypeEntry var3 = new WorldTypeList.WorldTypeEntry();
        var3.loadedClass = OverworldChunkGenerator.class;
        var3.name = "Skylands";
        var3.desc = "Start the world on the floating islands";
        var3.lightingMode = 0;
        var3.noSky = false;
        var3.biome = null;
        var3.blockToSpawnOn = Block.GRASS_BLOCK.id;
        worldtypeList.add(var3);

        WorldTypeList.WorldTypeEntry var4 = new WorldTypeList.WorldTypeEntry();
        var4.loadedClass = FlatChunkGenerator.class;
        var4.name = "Flat";
        var4.desc = "A completely flat world, perfect for building";
        var4.lightingMode = 1;
        var4.noSky = false;
        var4.biome = null;
        var4.blockToSpawnOn = Block.GRASS_BLOCK.id;
        worldtypeList.add(var4);

        WorldTypeList.WorldTypeEntry var5 = new WorldTypeList.WorldTypeEntry();
        var5.loadedClass = FarlandsChunkGenerator.class;
        var5.name = "Farlands";
        var5.desc = "Explore the Farlands, where terrain gen";
        var5.desc2 = "becomes distorted and unpredictable";
        var5.lightingMode = 0;
        var5.noSky = false;
        var5.biome = null;
        var5.blockToSpawnOn = Block.GRASS_BLOCK.id;
        worldtypeList.add(var5);

        WorldTypeList.WorldTypeEntry var6 = new WorldTypeList.WorldTypeEntry();
        var6.loadedClass = Beta11_ChunkGenerator.class;
        var6.name = "Beta 1.1_02";
        var6.desc = "Start the world with Beta 1.1_02 generation";
        var6.lightingMode = 0;
        var6.noSky = false;
        var6.biome = null;
        var6.blockToSpawnOn = Block.SAND.id;
        worldtypeList.add(var6);

        WorldTypeList.WorldTypeEntry var7 = new WorldTypeList.WorldTypeEntry();
        var7.loadedClass = Alpha112_ChunkGenerator.class;
        var7.name = "Alpha 1.1.2_01";
        var7.desc = "Start the world with Alpha 1.1.2_01 generation";
        var7.lightingMode = 0;
        var7.noSky = false;
        var7.biome = null;
        var7.blockToSpawnOn = Block.GRASS_BLOCK.id;
        worldtypeList.add(var7);

        WorldTypeList.WorldTypeEntry var8 = new WorldTypeList.WorldTypeEntry();
        var8.loadedClass = Infdev420ChunkGenerator.class;
        var8.name = "Infdev 420";
        var8.desc = "Start the world with Infdev 420 generation";
        var8.lightingMode = 0;
        var8.noSky = false;
        var8.biome = null;
        var8.blockToSpawnOn = Block.GRASS_BLOCK.id;
        worldtypeList.add(var8);

        WorldTypeList.WorldTypeEntry var9 = new WorldTypeList.WorldTypeEntry();
        var9.loadedClass = Infdev415ChunkGenerator.class;
        var9.name = "Infdev 415";
        var9.desc = "Start the world with Infdev 415 generation";
        var9.lightingMode = 0;
        var9.noSky = false;
        var9.biome = null;
        var9.blockToSpawnOn = Block.GRASS_BLOCK.id;
        worldtypeList.add(var9);

        WorldTypeList.WorldTypeEntry var10 = new WorldTypeList.WorldTypeEntry();
        var10.loadedClass = EarlyInfdevChunkGenerator.class;
        var10.name = "Early Infdev";
        var10.desc = "Start the world with Infdev 227-325 gen";
        var10.lightingMode = 0;
        var10.noSky = false;
        var10.biome = null;
        var10.blockToSpawnOn = Block.GRASS_BLOCK.id;
        worldtypeList.add(var10);

        if (isAetherModPresent()) {
            WorldTypeList.WorldTypeEntry var11 = new WorldTypeList.WorldTypeEntry();
            var11.loadedClass = OverworldChunkGenerator.class;
            var11.name = "Aether";
            var11.desc = "Start the world in a hostile paradise";
            var11.lightingMode = 0;
            var11.noSky = false;
            var11.biome = null;
            var11.blockToSpawnOn = 0;
            worldtypeList.add(var11);
        }
    }

    private static boolean isAetherModPresent() {
        return FabricLoader.getInstance().isModLoaded("aether");
    }

    public static void selectForWorldLoad(String worldTypeName) {
        if (!inited) {
            inited = true;
            init();
        }

        for (WorldTypeEntry var2 : worldtypeList) {
            if (var2.name.equals(worldTypeName)) {
                selectWorldType(var2);
                return;
            }
        }
    }


    public static void selectWorldType(WorldTypeEntry worldType) {
        try {
            WorldSettings.setName(worldType.name);
            WorldSettings.chunkGenerator = worldType.loadedClass.getConstructor(World.class, long.class);
            WorldSettings.skyDisabled = worldType.noSky;
            WorldSettings.lightingMode = worldType.lightingMode;
            WorldSettings.singleBiome = worldType.biome;
            WorldSettings.blockToSpawnOn = worldType.blockToSpawnOn;
        } catch (NoSuchMethodException var2) {
            System.out.println(worldType.loadedClass.getName() + " must have a public constructor that takes (World, long)");
            selectWorldType(worldtypeList.get(0));
        } catch (SecurityException var3) {
            System.out.println(worldType.loadedClass.getName() + " must have a *public* constructor that takes (World, long)");
            selectWorldType(worldtypeList.get(0));
        }
    }

    public static List<WorldTypeEntry> getList() {
        if (worldtypeList == null) {
            inited = true;
            init();
        }
        return worldtypeList;
    }

    public static class WorldTypeEntry {
        public Class<? extends ChunkSource> loadedClass;
        public String name;
        public String desc;
        public String desc2;
        public int lightingMode;
        public boolean noSky;
        public Biome biome;
        public int blockToSpawnOn;
    }
}