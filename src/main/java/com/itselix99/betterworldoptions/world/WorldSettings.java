package com.itselix99.betterworldoptions.world;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.Set;

public class WorldSettings {
    public static class World {
        private static Constructor<? extends ChunkSource> CHUNK_GENERATOR = null;
        private static String DISPLAY_WORLD_TYPE_NAME = "Default";
        private static String WORLD_TYPE_NAME = "Default";
        private static String LIGHTING_MODE = "Overworld";
        private static boolean SKY_DISABLED = false;
        private static Biome SINGLE_BIOME = null;
        private static int BLOCK_TO_SPAWN_ON = Block.SAND.id;

        public static void setChunkGenerator(Constructor<? extends ChunkSource> chunkGenerator) {
            CHUNK_GENERATOR = chunkGenerator;
        }

        public static void setDisplayWorldTypeName(String displayWorldTypeName) {
            DISPLAY_WORLD_TYPE_NAME = displayWorldTypeName;
        }

        public static void setWorldTypeName(String worldTypeName) {
            WORLD_TYPE_NAME = worldTypeName;
        }

        public static void setLightingMode(String lightingMode) {
            LIGHTING_MODE = lightingMode;
        }

        public static void setSingleBiome(Biome singleBiome) {
            SINGLE_BIOME = singleBiome;
        }

        public static void setSkyDisabled(boolean skyDisabled) {
            SKY_DISABLED = skyDisabled;
        }

        public static void setBlockToSpawnOn(int blockToSpawnOn) {
            BLOCK_TO_SPAWN_ON = blockToSpawnOn;
        }

        public static Constructor<? extends ChunkSource> getChunkGenerator() {
            return CHUNK_GENERATOR;
        }

        public static String getDisplayWorldTypeName() {
            return DISPLAY_WORLD_TYPE_NAME;
        }

        public static String getWorldTypeName() {
            return WORLD_TYPE_NAME;
        }

        public static String getLightingMode() {
            return LIGHTING_MODE;
        }

        public static boolean isSkyDisabled() {
            return SKY_DISABLED;
        }

        public static Biome getSingleBiome() {
            return SINGLE_BIOME;
        }

        public static int getBlockToSpawnOn() {
            return BLOCK_TO_SPAWN_ON;
        }
    }

    public static class GameMode {
        private static boolean HARDCORE = false;
        private static boolean BETA_FEATURES = true;
        private static boolean BETA_FEATURES_TEXTURES = true;

        private static final Set<String> NON_BETA_FEATURES_WORLD_TYPES = Set.of(
                "Default", "Nether", "Skylands", "Farlands", "Alpha 1.2.0", "Aether"
        );

        private static final Set<String> BETA_FEATURES_WORLD_TYPES = Set.of(
                "Alpha 1.1.2_01", "Infdev 611", "Infdev 420", "Infdev 415", "Early Infdev", "Indev 223"
        );

        public static void setHardcore(boolean hardcore) {
            HARDCORE = hardcore;
        }

        public static void setBetaFeatures(boolean betaFeatures) {
            BETA_FEATURES = betaFeatures;
        }

        public static void setBetaFeaturesTextures(boolean betaFeaturesTextures) {
            BETA_FEATURES_TEXTURES = betaFeaturesTextures;
        }

        public static boolean isHardcore() {
            return HARDCORE;
        }

        public static boolean isBetaFeatures() {
            return BETA_FEATURES;
        }

        public static boolean isBetaFeaturesTextures() {
            return BETA_FEATURES_TEXTURES;
        }

        public static boolean getNonBetaFeaturesWorldTypes() {
            return NON_BETA_FEATURES_WORLD_TYPES.contains(WorldSettings.World.getWorldTypeName());
        }

        public static boolean isBetaFeaturesWorldTypes(String worldType) {
            return BETA_FEATURES_WORLD_TYPES.contains(worldType);
        }
    }

    public static class AlphaWorld {
        private static boolean SNOW_COVERED = false;

        public static void setSnowCovered(boolean snowCovered) {
            SNOW_COVERED = snowCovered;
        }

        public static boolean isSnowCovered() {
            return SNOW_COVERED;
        }
    }

    public static class IndevWorld {
        private static String INDEV_WORLD_TYPE = "Island";
        private static String SHAPE = "Square";
        private static String SIZE = "Normal";
        private static String THEME = "Normal";
        private static String BETA_THEME = "All Biomes";
        private static boolean INDEV_DIMENSIONS = false;
        private static boolean GENERATE_INDEV_HOUSE = true;
        private static boolean INFINITE = false;

        public static void setIndevWorldType(String indevWorldType) {
            INDEV_WORLD_TYPE = indevWorldType;
        }

        public static void setShape(String shape) {
            SHAPE = shape;
        }

        public static void setSize(String size) {
            SIZE = size;
        }

        public static void setTheme(String theme) {
            THEME = theme;
        }

        public static void setBetaTheme(String betaTheme) {
            BETA_THEME = betaTheme;
        }

        public static void setIndevDimensions(boolean indevDimensions) {
            INDEV_DIMENSIONS = indevDimensions;
        }

        public static void setGenerateIndevHouse(boolean generateIndevHouse) {
            GENERATE_INDEV_HOUSE = generateIndevHouse;
        }

        public static void setInfinite(boolean infinite) {
            INFINITE = infinite;
        }

        public static String getIndevWorldType() {
            return INDEV_WORLD_TYPE;
        }

        public static String getShape() {
            return SHAPE;
        }

        public static String getSize() {
            return SIZE;
        }

        public static String getSizeInNumber() {
            if (Objects.equals(getShape(), "Long")) {
                if (Objects.equals(getSize(), "Small")) {
                    return "256x64";
                } else if (Objects.equals(getSize(), "Normal")) {
                    return "512x128";
                } else if (Objects.equals(getSize(), "Huge")) {
                    return "1024x256";
                } else if (Objects.equals(getSize(), "Very Huge")) {
                    return "2048x512";
                } else {
                    return "";
                }
            } else {
                if (Objects.equals(getSize(), "Small")) {
                    return "128x128";
                } else if (Objects.equals(getSize(), "Normal")) {
                    return "256x256";
                } else if (Objects.equals(getSize(), "Huge")) {
                    return "512x512";
                } else if (Objects.equals(getSize(), "Very Huge")) {
                    return "1024x1024";
                } else {
                    return "";
                }
            }
        }

        public static String getTheme() {
            return THEME;
        }

        public static String getBetaTheme() {
            return BETA_THEME;
        }

        public static boolean isIndevDimensions() {
            return INDEV_DIMENSIONS;
        }

        public static boolean isGenerateIndevHouse() {
            return GENERATE_INDEV_HOUSE;
        }

        public static boolean isInfinite() {
            return INFINITE;
        }
    }

    public static void resetSettings() {
        World.setChunkGenerator(null);
        World.setDisplayWorldTypeName("Default");
        World.setWorldTypeName("Default");
        World.setLightingMode("Overworld");
        World.setSkyDisabled(false);
        World.setSingleBiome(null);
        World.setBlockToSpawnOn(Block.SAND.id);

        GameMode.setHardcore(false);
        GameMode.setBetaFeatures(true);
        GameMode.setBetaFeaturesTextures(true);

        AlphaWorld.setSnowCovered(false);

        IndevWorld.setIndevWorldType("Normal");
        IndevWorld.setShape("Square");
        IndevWorld.setSize("Normal");
        IndevWorld.setTheme("Normal");
        IndevWorld.setBetaTheme("All Biomes");
        IndevWorld.setIndevDimensions(false);
        IndevWorld.setGenerateIndevHouse(true);
        IndevWorld.setInfinite(false);
    }
}
