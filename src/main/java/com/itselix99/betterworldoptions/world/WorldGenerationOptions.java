package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;

import java.util.Set;

public class WorldGenerationOptions {
    private static WorldGenerationOptions INSTANCE = new WorldGenerationOptions();

    public static final Set<String> allowOldFeaturesWorldTypes = Set.of("Alpha 1.1.2_01", "Infdev 611", "Infdev 420", "Infdev 415", "Early Infdev", "Indev 223", "MCPE");
    public static final Set<String> disableThemeWorldTypes = Set.of("Nether", "Skylands", "Aether");

    public static Set<Biome> defaultBiomesSetSnow = Set.of(Biome.RAINFOREST, Biome.SWAMPLAND, Biome.SEASONAL_FOREST, Biome.FOREST, Biome.SAVANNA, Biome.SHRUBLAND, Biome.DESERT, Biome.PLAINS);
    public static Set<Biome> defaultBiomesSetPrecipitation = Set.of(Biome.RAINFOREST, Biome.SWAMPLAND, Biome.SEASONAL_FOREST, Biome.FOREST, Biome.SAVANNA, Biome.SHRUBLAND, Biome.TAIGA, Biome.PLAINS, Biome.TUNDRA);
    public static Set<Biome> defaultBiomesSetPrecipitationNoSnow = Set.of(Biome.TAIGA, Biome.TUNDRA, Biome.ICE_DESERT);

    public String worldTypeName;
    public boolean hardcore;
    public boolean oldFeatures;
    public String theme;
    public String singleBiome;
    public boolean superflat;

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
        this.oldFeatures = server ? Config.BWOConfig.server.oldFeatures : false;
        this.singleBiome = server ? Config.BWOConfig.server.singleBiome : "Off";
        this.theme = server ? Config.BWOConfig.server.theme : "Normal";
        this.superflat = server ? Config.BWOConfig.server.superflat : false;

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
        this.oldFeatures = ((BWOProperties) properties).bwo_isOldFeatures();
        this.singleBiome = ((BWOProperties) properties).bwo_getSingleBiome();
        this.theme = ((BWOProperties) properties).bwo_getTheme();
        this.superflat = ((BWOProperties) properties).bwo_isSuperflat();

        this.indevWorldType = ((BWOProperties) properties).bwo_getIndevWorldType();
        this.indevShape = ((BWOProperties) properties).bwo_getShape();
        this.generateIndevHouse = ((BWOProperties) properties).bwo_isGenerateIndevHouse();

        this.worldSizeX = ((BWOProperties) properties).bwo_getWorldSizeX();
        this.worldSizeZ = ((BWOProperties) properties).bwo_getWorldSizeZ();
        this.infiniteWorld = ((BWOProperties) properties).bwo_isInfiniteWorld();

        this.oldTextures = ((BWOProperties) properties).bwo_isOldFeatures();
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

    public static double[] getClimateForBiome(Biome biome) {
        if (biome == Biome.TUNDRA) {
            return new double[]{0.05D, 0.2D};
        } else if (biome == Biome.SAVANNA) {
            return new double[]{0.7D, 0.1D};
        } else if (biome == Biome.DESERT) {
            return new double[]{1.0D, 0.05D};
        } else if (biome == Biome.SWAMPLAND) {
            return new double[]{0.6D, 0.8D};
        } else if (biome == Biome.TAIGA) {
            return new double[]{0.4D, 0.4D};
        } else if (biome == Biome.SHRUBLAND) {
            return new double[]{0.8D, 0.3D};
        } else if (biome == Biome.FOREST) {
            return new double[]{0.8D, 0.6D};
        } else if (biome == Biome.PLAINS) {
            return new double[]{1.0D, 0.4D};
        } else if (biome == Biome.SEASONAL_FOREST) {
            return new double[]{1.0D, 0.7D};
        } else if (biome == Biome.RAINFOREST) {
            return new double[]{1.0D, 0.85D};
        } else if (biome == Biome.ICE_DESERT) {
            return new double[]{0.0D, 0.0D};
        }

        return new double[]{0.5D, 0.5D};
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