package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.WorldProperties;

import java.util.Set;

public class WorldGenerationOptions {
    private static WorldGenerationOptions INSTANCE = new WorldGenerationOptions();

    public static final Set<String> allowBetaFeaturesWorldTypes = Set.of(
            "Alpha 1.1.2_01", "Infdev 611", "Infdev 420", "Infdev 415", "Early Infdev", "Indev 223", "MCPE"
    );

    public String worldTypeName;
    public boolean hardcore;
    public boolean betaFeatures;
    public String theme;
    public String singleBiome;

    public String indevWorldType;
    public String indevShape;
    public boolean generateIndevHouse;

    public String size;
    public int worldSizeX;
    public int worldSizeZ;
    public boolean infiniteWorld;

    public boolean oldTextures;

    public WorldGenerationOptions() {
        INSTANCE = this;

        boolean server = FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;

        this.worldTypeName = server ? Config.BWOConfig.server.worldType : "Default";
        this.hardcore = server ? Config.BWOConfig.server.hardcore : false;
        this.betaFeatures = server ? Config.BWOConfig.server.betaFeatures : true;
        this.theme = server ? Config.BWOConfig.server.theme : "Normal";
        this.singleBiome = server ? Config.BWOConfig.server.singleBiome : "Off";

        this.indevWorldType = server ? Config.BWOConfig.server.indevWorldType : "Island";
        this.indevShape = server ? Config.BWOConfig.server.indevShape : "Square";
        this.generateIndevHouse = server ? Config.BWOConfig.server.generateIndevHouse : true;

        this.size = server ? Config.BWOConfig.server.worldSize : "Normal";
        this.setSizeXZ();
        this.infiniteWorld = server ? Config.BWOConfig.server.infiniteWorld : false;

        this.oldTextures = false;
    }

    public WorldGenerationOptions(WorldProperties properties) {
        INSTANCE = this;

        this.worldTypeName = ((BWOProperties) properties).bwo_getWorldType();
        this.hardcore = ((BWOProperties) properties).bwo_isHardcore();
        this.betaFeatures = ((BWOProperties) properties).bwo_getBetaFeatures();
        this.theme = ((BWOProperties) properties).bwo_getTheme();
        this.singleBiome = ((BWOProperties) properties).bwo_getSingleBiome();

        this.indevWorldType = ((BWOProperties) properties).bwo_getIndevWorldType();
        this.indevShape = ((BWOProperties) properties).bwo_getShape();
        this.generateIndevHouse = ((BWOProperties) properties).bwo_isGenerateIndevHouse();

        this.worldSizeX = ((BWOProperties) properties).bwo_getWorldSizeX();
        this.worldSizeZ = ((BWOProperties) properties).bwo_getWorldSizeZ();
        this.infiniteWorld = ((BWOProperties) properties).bwo_isInfiniteWorld();

        this.oldTextures = false;
    }

    public static WorldGenerationOptions getInstance() {
        return INSTANCE;
    }

    public void setSizeXZ() {
        if (this.indevShape.equals("Long")) {
            switch (this.size) {
                case "Small" -> {
                    this.worldSizeX = 256;
                    this.worldSizeZ = 64;
                }
                case "Normal" -> {
                    this.worldSizeX = 512;
                    this.worldSizeZ = 128;
                }
                case "Huge" -> {
                    this.worldSizeX = 1024;
                    this.worldSizeZ = 256;
                }
                case "Gigantic" -> {
                    this.worldSizeX = 2048;
                    this.worldSizeZ = 512;
                }
                case "Enormous" -> {
                    this.worldSizeX = 4096;
                    this.worldSizeZ = 1024;
                }
            }
        } else {
            switch (this.size) {
                case "Small" -> {
                    this.worldSizeX = 128;
                    this.worldSizeZ = 128;
                }
                case "Normal" -> {
                    this.worldSizeX = 256;
                    this.worldSizeZ = 256;
                }
                case "Huge" -> {
                    this.worldSizeX = 512;
                    this.worldSizeZ = 512;
                }
                case "Gigantic" -> {
                    this.worldSizeX = 1024;
                    this.worldSizeZ = 1024;
                }
                case "Enormous" -> {
                    this.worldSizeX = 2048;
                    this.worldSizeZ = 2048;
                }
            }
        }
    }

    public void resetFiniteOptions() {
        this.indevWorldType = "Island";
        this.indevShape = "Square";
        this.generateIndevHouse = true;
        this.size = "Normal";
        this.infiniteWorld = false;
    }

    public void resetIndevOptions() {
        this.indevWorldType = "Island";
        this.indevShape = "Square";
        this.generateIndevHouse = true;
    }
}