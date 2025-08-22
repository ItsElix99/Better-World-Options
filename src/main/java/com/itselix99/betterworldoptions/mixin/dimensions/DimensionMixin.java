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
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Mixin(Dimension.class)
public class DimensionMixin implements StationDimension {
    @Shadow public World world;

    @WrapOperation(method = "initBiomeSource", at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;)Lnet/minecraft/world/biome/source/BiomeSource;"))
    private BiomeSource initBiomeSource(World world, Operation<BiomeSource> original) {
        String worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) world.getProperties()).bwo_isOldFeatures();
        String singleBiome = ((BWOProperties) world.getProperties()).bwo_getSingleBiome();
        String theme = ((BWOProperties) world.getProperties()).bwo_getTheme();

        if (oldFeatures && !worldType.equals("MCPE")) {
            if (theme.equals("Hell")) {
                return new FixedBiomeSource(BetterWorldOptions.IndevHell, 1.0D, 0.0D);
            } else if (theme.equals("Paradise")) {
                return new FixedBiomeSource(BetterWorldOptions.IndevParadise, 1.0D, 0.5D);
            } else if (theme.equals("Woods")) {
                return new FixedBiomeSource(BetterWorldOptions.IndevWoods, 1.0D, 0.5D);
            } else if (worldType.equals("Flat")) {
                return new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.4D);
            } else if (worldType.equals("Alpha 1.1.2_01")) {
                return new FixedBiomeSource(BetterWorldOptions.Alpha, 1.0D, 0.5D);
            } else if (worldType.equals("Infdev 611") || worldType.equals("Infdev 420") || worldType.equals("Infdev 415")) {
                return new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
            } else if (worldType.equals("Early Infdev")) {
                return new FixedBiomeSource(BetterWorldOptions.EarlyInfdev, 1.0D, 0.5D);
            } else if (worldType.equals("Indev 223")) {
                return new FixedBiomeSource(BetterWorldOptions.IndevNormal, 1.0D, 0.5D);
            }
        } else {
            if (!singleBiome.equals("Off")) {
                Biome biome = OverworldBiomeProviderImpl.getInstance().getBiomes().stream().filter(biome1 -> biome1.name.equals(singleBiome)).toList().get(0);
                double[] climate = this.getClimateForBiome(biome);

                return new FixedBiomeSource(biome, climate[0], climate[1]);
            }
        }

        return original.call(world);
    }

    @Unique
    private double[] getClimateForBiome(Biome biome) {
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
