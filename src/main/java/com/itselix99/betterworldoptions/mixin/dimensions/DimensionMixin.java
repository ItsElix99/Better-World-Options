package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.BiomeProperties;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.world.worldtypes.AltOverworldChunkGenerator;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.InvocationTargetException;

@Mixin(Dimension.class)
public class DimensionMixin {
    @Shadow public World world;

    @WrapOperation(method = "initBiomeSource", at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;)Lnet/minecraft/world/biome/source/BiomeSource;"))
    private BiomeSource bwo_initBiomeSource(World world, Operation<BiomeSource> original) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        WorldType worldType = ((BWOWorld) world).bwo_getWorldType();
        Theme theme = ((BWOWorld) world).bwo_getTheme();
        boolean oldFeatures = bwoProperties.bwo_isOldFeatures();
        BiomeProperties biomeProperties = ((BWOWorld) world).bwo_getBiomeProperties();
        String singleBiome = bwoProperties.bwo_getSingleBiome();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome != null) {
            if (Config.BWOConfig.environment.oldTexturesAndSky) {
                if (FabricLoaderImpl.INSTANCE.getEnvironmentType() == EnvType.CLIENT) {
                    oldFeaturesProperties.oldBiome.setGrassColorProvider(oldFeaturesProperties.defaultGrassColor);
                    oldFeaturesProperties.oldBiome.setLeavesColor(oldFeaturesProperties.defaultLeavesColor);
                    oldFeaturesProperties.oldBiome.setFogColor(theme.getFogColor() != 1 ? theme.getFogColor() : oldFeaturesProperties.defaultFogColor.getColor(null, 0, 0));
                }
            } else {
                if (FabricLoaderImpl.INSTANCE.getEnvironmentType() == EnvType.CLIENT) {
                    oldFeaturesProperties.oldBiome.setGrassColorProvider(BiomeColorsImpl.DEFAULT_GRASS_COLOR);
                    oldFeaturesProperties.oldBiome.setLeavesColor(BiomeColorsImpl.DEFAULT_LEAVES_COLOR);
                    oldFeaturesProperties.oldBiome.setFogColor(BiomeColorsImpl.DEFAULT_FOG_COLOR);
                }
            }

            return worldType.getFixedBiomeSource().getDeclaredConstructor(Biome.class, double.class, double.class).newInstance(oldFeaturesProperties.oldBiome, 1.0D, 0.5D);
        } else if (!singleBiome.equals("Off") && !singleBiome.isEmpty()) {
            Biome biome = biomeProperties.getAllBiomes().stream().filter(biome1 -> biome1.name.equals(singleBiome)).findFirst().orElse(null);

            if (biome != null) {
                float[] tempAndDownfall = biomeProperties.getTemperatureAndDownfall(biome);
                return worldType.getFixedBiomeSource().getDeclaredConstructor(Biome.class, double.class, double.class).newInstance(biome, tempAndDownfall[0], tempAndDownfall[1]);
            }
        }

        return worldType.getBiomeSource().getDeclaredConstructor(World.class).newInstance(world);
    }

    @ModifyReturnValue(method = "createChunkGenerator", at = @At("RETURN"))
    public ChunkSource bwo_createChunkGenerator(ChunkSource original) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        WorldType worldType = ((BWOWorld) this.world).bwo_getWorldType();
        Class<? extends ChunkSource> chunkGenerator;

        if (worldType.getId().equals(BetterWorldOptions.NAMESPACE.id("default")) && Config.BWOConfig.world.fixTerrainGenDefault) {
            return new AltOverworldChunkGenerator(this.world, this.world.getSeed());
        } else {
            chunkGenerator = worldType.getChunkGenerator(0);
            return chunkGenerator.getDeclaredConstructor(World.class, long.class).newInstance(this.world, this.world.getSeed());
        }
    }

    @ModifyReturnValue(method = "isValidSpawnPoint", at = @At("RETURN"))
    public boolean bwo_isValidSpawnPoint(boolean original, @Local(ordinal = 2) int var3) {
        WorldType worldType = ((BWOWorld) this.world).bwo_getWorldType();
        Theme theme = ((BWOWorld) this.world).bwo_getTheme();
        BiomeProperties biomeProperties = ((BWOWorld) this.world).bwo_getBiomeProperties();
        String singleBiome = ((BWOProperties) this.world.getProperties()).bwo_getSingleBiome();

        if (!singleBiome.equals("Off") && !singleBiome.isEmpty()) {
            Biome biome = biomeProperties.getAllBiomes().stream().filter(biome1 -> biome1.name.equals(singleBiome)).findFirst().orElse(null);
            return var3 == (biome != null ? biome.topBlockId : worldType.getBlockToSpawn());
        } else if (theme.getBlockToSpawn() != -1) {
            return var3 == theme.getBlockToSpawn();
        } else {
            return var3 == worldType.getBlockToSpawn();
        }
    }

    @ModifyReturnValue(method = "getTimeOfDay", at = @At("RETURN"))
    public float bwo_stopTimeInParadiseTheme(float original, long time, float tickDelta) {
        Theme theme = ((BWOWorld) this.world).bwo_getTheme();

        if (theme.isStopTime()) {
            return 0.0F;
        }

        return original;
    }

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getBackgroundColor", at = @At(value = "RETURN", ordinal = 0))
    public float[] bwo_removeSunriseAndSunsetColors(float[] original) {
        WorldType worldType = ((BWOWorld) this.world).bwo_getWorldType();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeaturesProperties != null && !oldFeaturesProperties.sunriseAndSunsetColors) {
            return null;
        }

        return original;
    }
}
