package com.itselix99.betterworldoptions.api.theme;

import net.modificationstation.stationapi.api.util.Identifier;

public class WoodsTheme extends Theme {

    public WoodsTheme(Identifier id, String name, String icon, String[] description) {
        super(id, name, icon, description, 7699847, 5069403, 5069403);
        this.denseWoods = true;
    }

    public WoodsTheme(Identifier id, String name, String icon, String[] description, int skyColor, int fogColor, int cloudsColor) {
        super(id, name, icon, description, skyColor, fogColor, cloudsColor);
        this.denseWoods = true;
    }

    public int changeAmbientDarkness(int defaultValue) {
        if (defaultValue < 4) {
            return 4;
        }

        return defaultValue;
    }
}