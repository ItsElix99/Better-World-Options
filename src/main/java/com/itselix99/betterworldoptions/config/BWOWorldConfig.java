package com.itselix99.betterworldoptions.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class BWOWorldConfig {
    @ConfigEntry(name = "World height limit", multiplayerSynced = true, minValue = 128, maxValue = 512, requiresRestart = true)
    public WorldHeightConfigEnum worldHeightLimit = WorldHeightConfigEnum.H256;

    @ConfigEntry(name = "Allow generation of these features with Beta Features disabled")
    public Boolean allowGenWithBetaFeaturesOff = false;

    @ConfigEntry(name = "Ravine generation")
    public Boolean ravineGeneration = false;
}