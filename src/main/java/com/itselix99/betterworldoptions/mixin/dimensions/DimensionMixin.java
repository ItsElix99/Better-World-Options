package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Mixin(Dimension.class)
public class DimensionMixin implements StationDimension {
    @Shadow public World world;

    @WrapOperation(method = "initBiomeSource", at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;)Lnet/minecraft/world/biome/source/BiomeSource;"))
    private BiomeSource initBiomeSource(World world, Operation<BiomeSource> original) {
        String worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) world.getProperties()).bwo_getBetaFeatures();
        String singleBiome = ((BWOProperties) world.getProperties()).bwo_getSingleBiome();
        String theme = ((BWOProperties) world.getProperties()).bwo_getTheme();

        if (!betaFeatures && !worldType.equals("MCPE")) {
            if (theme.equals("Hell")) {
                new FixedBiomeSource(BetterWorldOptions.IndevHell, 1.0D, 0.0D);
            } else if (theme.equals("Paradise")) {
                new FixedBiomeSource(BetterWorldOptions.IndevParadise, 1.0D, 0.5D);
            } else if (theme.equals("Woods")) {
                new FixedBiomeSource(BetterWorldOptions.IndevWoods, 1.0D, 0.5D);
            } else if (worldType.equals("Flat")) {
                new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.5D);
            } else if (worldType.equals("Alpha 1.1.2_01")) {
                new FixedBiomeSource(BetterWorldOptions.Alpha, 1.0D, 0.5D);
            } else if (worldType.equals("Infdev 611") || worldType.equals("Infdev 420") || worldType.equals("Infdev 415")) {
                new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
            } else if (worldType.equals("Early Infdev")) {
                new FixedBiomeSource(BetterWorldOptions.EarlyInfdev, 1.0D, 0.5D);
            } else if (worldType.equals("Indev 223")) {
                new FixedBiomeSource(BetterWorldOptions.IndevNormal, 1.0D, 0.5D);
            }
        } else {
            if (!singleBiome.equals("Off")) {
                switch (singleBiome) {
                    case "Rainforest" -> new FixedBiomeSource(Biome.RAINFOREST, 1.0D, 1.0D);
                    case "Swampland" -> new FixedBiomeSource(Biome.SWAMPLAND, 0.6D, 0.6D);
                    case "Seasonal Forest" -> new FixedBiomeSource(Biome.SEASONAL_FOREST, 1.0D, 0.8D);
                    case "Forest" -> new FixedBiomeSource(Biome.FOREST, 0.7D, 0.5D);
                    case "Savanna" -> new FixedBiomeSource(Biome.SAVANNA, 0.8D, 0.1D);
                    case "Shrubland" -> new FixedBiomeSource(Biome.SHRUBLAND, 0.6D, 0.3D);
                    case "Taiga" -> new FixedBiomeSource(Biome.TAIGA, 0.3D, 0.4D);
                    case "Desert" -> new FixedBiomeSource(Biome.DESERT, 1.0D, 0.1D);
                    case "Plains" -> new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.4D);
                    case "Ice Desert" -> new FixedBiomeSource(Biome.ICE_DESERT, 0.0D, 0.1D);
                    case "Tundra" -> new FixedBiomeSource(Biome.TUNDRA, 0.0D, 1.0D);
                }
            }
        }

        return original.call(world);
    }

    @ModifyReturnValue(method = "createChunkGenerator", at = @At("RETURN"))
    public ChunkSource createChunkGenerator(ChunkSource original) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();
        List<WorldTypeList.WorldTypeEntry> worldTypeList = WorldTypeList.getList();
        Class<? extends ChunkSource> chunkGenerator = null;

        if (worldTypeList.stream().filter(worldTypeEntry -> worldGenerationOptions.worldTypeName.equals(worldTypeEntry.NAME)).toList().get(0).OVERWORLD_CHUNK_GENERATOR != null) {
            chunkGenerator = worldTypeList.stream().filter(worldTypeEntry -> worldGenerationOptions.worldTypeName.equals(worldTypeEntry.NAME)).toList().get(0).OVERWORLD_CHUNK_GENERATOR;
            return chunkGenerator.getDeclaredConstructor(World.class, long.class).newInstance(this.world, this.world.getSeed());
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "getTimeOfDay", at = @At("RETURN"))
    public float paradiseThemeGetTimeOfDay(float original, long time, float tickDelta) {
        String theme = ((BWOProperties) world.getProperties()).bwo_getTheme();

        if (theme.equals("Paradise")) {
            return 0.0F;
        }

        return original;
    }

    @Override
    public int getHeight() {
        return Config.BWOConfig.world.worldHeightLimit.getIntValue();
    }
}
