package com.itselix99.betterworldoptions;

import com.itselix99.betterworldoptions.config.BWOEnvironmentConfig;
import com.itselix99.betterworldoptions.config.BWOWorldConfig;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class BWOConfig {
    @ConfigRoot(value = "world", visibleName = "World", index = 0)
    public static final BWOWorldConfig WORLD_CONFIG = new BWOWorldConfig();

    @ConfigRoot(value = "environment", visibleName = "Environment", index = 1)
    public static final BWOEnvironmentConfig ENVIRONMENT_CONFIG = new BWOEnvironmentConfig();
}