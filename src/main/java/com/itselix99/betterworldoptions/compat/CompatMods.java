package com.itselix99.betterworldoptions.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.dimension.Dimension;

import java.lang.reflect.Constructor;

public class CompatMods {

    public static boolean BHCreativeLoaded() {
        return FabricLoader.getInstance().isModLoaded("bhcreative");
    }

    public static boolean AetherLoaded() {
        return FabricLoader.getInstance().isModLoaded("aether");
    }

    public static Dimension startWorldInAether() {
        try {
            Class<?> getDimension = Class.forName("com.matthewperiut.aether.gen.dim.AetherDimension");
            Constructor<?> AetherDimension = getDimension.getConstructor(int.class);
            return (Dimension) AetherDimension.newInstance(2);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}