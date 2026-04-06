package com.itselix99.betterworldoptions.compat;

import net.fabricmc.loader.api.FabricLoader;

public class CompatMods {
    public static boolean BHCreativeLoaded() {
        return FabricLoader.getInstance().isModLoaded("bhcreative");
    }
}