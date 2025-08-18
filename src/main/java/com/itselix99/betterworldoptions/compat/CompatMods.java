package com.itselix99.betterworldoptions.compat;

import com.matthewperiut.aether.gen.dim.AetherDimension;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.dimension.Dimension;

public class CompatMods {

    public static boolean BHCreativeLoaded() {
        return FabricLoader.getInstance().isModLoaded("bhcreative");
    }

    public static boolean AetherLoaded() {
        return FabricLoader.getInstance().isModLoaded("aether");
    }

    public static boolean bnbLoaded() {
        return FabricLoader.getInstance().isModLoaded("bnb");
    }

    public static Dimension startWorldInAether() {
        return new AetherDimension(2);
    }
}