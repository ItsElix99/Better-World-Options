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

        @ConfigCategory(name = "Environment")
        public BWOEnvironment environment = new BWOEnvironment();

        @ConfigCategory(name = "Player")
        public BWOPlayer player = new BWOPlayer();
    }

    public static class BWOWorld {
        @ConfigEntry(
                name = "World height limit",
                multiplayerSynced = true,
                requiresRestart = true,
                comment = "0 = 128, 1 = 256, 2 = 512"
        )
        public WorldHeightConfigEnum worldHeightLimit = WorldHeightConfigEnum.H128;

        @ConfigEntry(name = "Fix terrain gen in Default world type", description = "Fix terrain gen above height 128 in Default world type (may cause incompatibilities with other mods)", multiplayerSynced = true)
        public Boolean fixTerrainGenDefault = false;

        @ConfigEntry(name = "Cave Fix", multiplayerSynced = true)
        public Boolean caveFix = true;

        @ConfigEntry(name = "Beach Fix", multiplayerSynced = true)
        public Boolean beachFix = true;

        @ConfigEntry(name = "Allow generation of these features with Old Features enabled", multiplayerSynced = true)
        public Boolean allowGenWithOldFeaturesOn = false;

        @ConfigEntry(name = "Ravine generation", multiplayerSynced = true)
        public Boolean ravineGeneration = false;
    }

    public static class BWOEnvironment {
        @ConfigEntry(name = "Always Snow in Winter Worlds")
        public Boolean alwaysSnowInWinterWorlds = true;

        @ConfigEntry(name = "Old Textures and Sky", description = "Use old textures and sky when Old Features is enabled")
        public Boolean oldTexturesAndSky = true;
    }

    public static class BWOPlayer {
        @ConfigEntry(name = "Player walking animation")
        public WalkingAnimConfigEnum walkingAnim = WalkingAnimConfigEnum.DEFAULT;
    }
}