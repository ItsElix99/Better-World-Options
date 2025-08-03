package com.itselix99.betterworldoptions;

import com.itselix99.betterworldoptions.config.BWOEnvironmentConfig;
import com.itselix99.betterworldoptions.config.BWOOthersConfig;
import com.itselix99.betterworldoptions.config.BWOWorldConfig;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class BWOConfig {
    @ConfigRoot(value = "world", visibleName = "World", index = 1)
    public static final BWOWorldConfig WORLD_CONFIG = new BWOWorldConfig();

    @ConfigRoot(value = "environment", visibleName = "Environment", index = 2)
    public static final BWOEnvironmentConfig ENVIRONMENT_CONFIG = new BWOEnvironmentConfig();

    @ConfigRoot(value = "others", visibleName = "Others", index = 3)
    public static final BWOOthersConfig OTHERS_CONFIG = new BWOOthersConfig();
}