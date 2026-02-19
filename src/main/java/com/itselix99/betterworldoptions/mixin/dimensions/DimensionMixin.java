package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.world.worldtypes.AltOverworldChunkGenerator;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Mixin(Dimension.class)
public class DimensionMixin {
    @Shadow public World world;

    @WrapOperation(method = "initBiomeSource", at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;)Lnet/minecraft/world/biome/source/BiomeSource;"))
    private BiomeSource bwo_initBiomeSource(World world, Operation<BiomeSource> original) {
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        String worldType = bwoProperties.bwo_getWorldType();
        boolean oldFeatures = bwoProperties.bwo_isOldFeatures();
        String singleBiome = bwoProperties.bwo_getSingleBiome();
        boolean superflat = bwoProperties.bwo_getBooleanOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION);
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);

        if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldFeaturesBiomeSupplier.get() != null) {
            if (Config.BWOConfig.environment.oldTexturesAndSky) {
                return new FixedBiomeSource(oldFeaturesProperties.oldFeaturesBiomeSupplier.get(), 1.0D, 0.5D);
            } else {
                return new FixedBiomeSource(Biome.FOREST, 0.8D, 0.6D);
            }
        } else if (worldType.equals("Flat") && !superflat) {
            return new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.4D);
        } else if (!singleBiome.equals("Off") && !singleBiome.isEmpty()) {
            List<Biome> biomesList = OverworldBiomeProviderImpl.getInstance().getBiomes().stream().filter(biome1 -> biome1.name.equals(singleBiome)).toList();

            if (!biomesList.isEmpty()) {
                Biome biome = biomesList.get(0);
                double[] climate = BWOWorldPropertiesStorage.getClimateForBiome(biome);

                return new FixedBiomeSource(biome, climate[0], climate[1]);
            }
        }

        return original.call(world);
    }

    @ModifyReturnValue(method = "createChunkGenerator", at = @At("RETURN"))
    public ChunkSource bwo_createChunkGenerator(ChunkSource original) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();
        WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));
        Class<? extends ChunkSource> chunkGenerator;

        if (worldType.name.equals("Default") && Config.BWOConfig.world.fixTerrainGenDefault) {
            return new AltOverworldChunkGenerator(this.world, this.world.getSeed());
        } else if (worldType.overworldChunkGenerator != null) {
            chunkGenerator = worldType.overworldChunkGenerator;
            return chunkGenerator.getDeclaredConstructor(World.class, long.class).newInstance(this.world, this.world.getSeed());
        }

        return original;
    }

    @ModifyReturnValue(method = "getTimeOfDay", at = @At("RETURN"))
    public float bwo_stopTimeInParadiseTheme(float original, long time, float tickDelta) {
        String theme = ((BWOProperties) world.getProperties()).bwo_getTheme();

        if (theme.equals("Paradise")) {
            return 0.0F;
        }

        return original;
    }

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getBackgroundColor", at = @At(value = "RETURN", ordinal = 0))
    public float[] bwo_removeSunriseAndSunsetColors(float[] original) {
        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeaturesProperties != null && !oldFeaturesProperties.sunriseAndSunsetColors) {
            return null;
        }

        return original;
    }
}
