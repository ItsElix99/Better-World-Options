package com.itselix99.betterworldoptions.api.theme;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.util.Identifier;

public class HellTheme extends Theme {

    public HellTheme(Identifier id, String name, String icon, String[] description) {
        super(id, name, icon, description, 1049600, 1049600, 2164736);
        this.surfaceLiquidBlock = Block.LAVA.id;
        this.liquidBlock = Block.LAVA.id;
        this.flowingLiquidBlock = Block.FLOWING_LAVA.id;
        this.topBlock = Block.DIRT.id;
        this.soilBlock = Block.DIRT.id;
        this.beachTopBlock = Block.GRASS_BLOCK.id;
        this.beachSoilBlock = Block.DIRT.id;
        this.hot = true;
        this.rain = false;
        this.blockToSpawn = Block.DIRT.id;
    }

    public HellTheme(Identifier id, String name, String icon, String[] description, int skyColor, int fogColor, int cloudsColor) {
        super(id, name, icon, description, skyColor, fogColor, cloudsColor);
        this.surfaceLiquidBlock = Block.LAVA.id;
        this.liquidBlock = Block.LAVA.id;
        this.flowingLiquidBlock = Block.FLOWING_LAVA.id;
        this.topBlock = Block.DIRT.id;
        this.soilBlock = Block.DIRT.id;
        this.beachTopBlock = Block.GRASS_BLOCK.id;
        this.beachSoilBlock = Block.DIRT.id;
        this.hot = true;
        this.rain = false;
        this.blockToSpawn = Block.DIRT.id;
    }

    public int changeAmbientDarkness(int defaultValue) {
        if (defaultValue < 9) {
            return 9;
        } else if (defaultValue == 11) {
            return 12;
        }

        return defaultValue;
    }
}