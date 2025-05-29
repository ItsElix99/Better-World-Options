package com.itselix99.betterworldoptions.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class WorldSettings {
    public static String worldTypeName = "Default";
    public static Constructor<? extends ChunkSource> chunkGenerator = null;
    public static boolean skyDisabled = false;
    public static Biome singleBiome = null;
    public static int lightingMode = 0;
    public static int blockToSpawnOn = 0;
    public static boolean hardcore = false;
    public static boolean betaFeatures = true;

    public static boolean alphaSnowCovered = false;

    private static final List<Runnable> changeListeners = new ArrayList<>();

    public static void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    private static void notifyChange() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    public static String getName() {
        return worldTypeName;
    }

    public static void setName(String newName) {
        if (!newName.equals(worldTypeName)) {
            worldTypeName = newName;
            notifyChange();
        }
    }

    public static void resetBooleans() {
        if (hardcore) {
            hardcore = false;
        }
        if (alphaSnowCovered) {
            alphaSnowCovered = false;
        }
    }
}
