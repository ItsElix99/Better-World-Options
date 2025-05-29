package com.itselix99.betterworldoptions.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class WorldSettings {
    public static String name = "Default";
    public static Constructor<? extends ChunkSource> chunkProviderConstructor = null;
    public static boolean skyDisabled = false;
    public static Biome customBiome = null;
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
        return name;
    }

    public static void setName(String newName) {
        if (!newName.equals(name)) {
            name = newName;
            notifyChange();
        }
    }

    public static void resetHardcore() {
        if (hardcore) {
            hardcore = false;
        }
    }
}
