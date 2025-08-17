package com.itselix99.betterworldoptions.config;

import com.itselix99.betterworldoptions.config.enums.WalkingAnimConfigEnum;
import com.itselix99.betterworldoptions.config.enums.WorldHeightConfigEnum;
import net.glasslauncher.mods.gcapi3.api.ConfigCategory;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class Config {
    @ConfigRoot(value = "betterworldoptions", visibleName = "Better World Options")
    public static final BWOConfig BWOConfig = new BWOConfig();

    public static class BWOConfig {
        @ConfigCategory(name = "World")
        public BWOWorld world = new BWOWorld();

        @ConfigCategory(name = "Environment", hidden = true)
        public BWOEnvironment environment = new BWOEnvironment();

        @ConfigCategory(name = "Others")
        public BWOOthers others = new BWOOthers();

        @ConfigCategory(name = "Server", hidden = true, comment = "")
        public BWOServer server = new BWOServer();
    }

    public static class BWOWorld {
        @ConfigEntry(name = "World height limit", multiplayerSynced = true, requiresRestart = true)
        public WorldHeightConfigEnum worldHeightLimit = WorldHeightConfigEnum.H128;

        @ConfigEntry(name = "Allow generation of these features with Beta Features disabled", multiplayerSynced = true)
        public Boolean allowGenWithBetaFeaturesOff = false;

        @ConfigEntry(name = "Ravine generation", multiplayerSynced = true)
        public Boolean ravineGeneration = false;
    }

    public static class BWOEnvironment {

    }

    public static class BWOOthers {
        @ConfigEntry(name = "Player walking animation")
        public WalkingAnimConfigEnum walkingAnim = WalkingAnimConfigEnum.DEFAULT;
    }

    public static class BWOServer {
        @ConfigEntry(
                name = "World Type",
                multiplayerSynced = true,
                comment = "Allowed world types: Default, Nether, Skylands, Flat, Farlands, Alpha 1.2.0, Alpha 1.1.2_01, Indev 611, Indev 420, Indev 415, Early Infdev, Indev 223, MCPE, Aether"
        )
        public String worldType = "Default";

        @ConfigEntry(
                name = "Hardcore",
                multiplayerSynced = true
        )
        public Boolean hardcore = false;

        @ConfigEntry(
                name = "Beta Features",
                multiplayerSynced = true
        )
        public Boolean betaFeatures = true;

        @ConfigEntry(
                name = "Theme",
                multiplayerSynced = true,
                comment = "Allowed themes: Normal, Hell, Paradise, Woods, Winter"
        )
        public String theme = "Normal";

        @ConfigEntry(
                name = "Single Biome",
                multiplayerSynced = true,
                comment = "This option is available only for Indev 223 and MCPE"
        )
        public String singleBiome = "Off";

        @ConfigEntry(
                name = "Indev World Type",
                multiplayerSynced = true,
                comment = "Allowed types: Island, Floating, Flat, Inland"
        )
        public String indevWorldType = "Island";

        @ConfigEntry(
                name = "Indev Shape",
                multiplayerSynced = true,
                comment = "Allowed shapes: Square and Long"
        )
        public String indevShape = "Island";

        @ConfigEntry(
                name = "Generate indev house",
                multiplayerSynced = true
        )
        public Boolean generateIndevHouse = true;

        @ConfigEntry(
                name = "World Size",
                multiplayerSynced = true,
                comment = "This option is available only for Indev 223 and MCPE. Allowed sizes: Small, Normal, Huge, Gigantic, Enormous"
        )
        public String worldSize = "Normal";

        @ConfigEntry(
                name = "Infinite World",
                multiplayerSynced = true
        )
        public Boolean infiniteWorld = false;
    }
}