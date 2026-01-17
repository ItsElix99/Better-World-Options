package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.world.worldtypes.AltOverworldChunkGenerator;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
        String worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) world.getProperties()).bwo_isOldFeatures();
        String singleBiome = ((BWOProperties) world.getProperties()).bwo_getSingleBiome();
        boolean superflat = ((BWOProperties) world.getProperties()).bwo_getBooleanOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION);

        if (oldFeatures && !worldType.equals("MCPE")) {
            switch (worldType) {
                case "Alpha 1.1.2_01" -> {
                    return new FixedBiomeSource(BetterWorldOptions.Alpha, 1.0D, 0.5D);
                }
                case "Infdev 611", "Infdev 420", "Infdev 415" -> {
                    return new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
                }
                case "Early Infdev" -> {
                    return new FixedBiomeSource(BetterWorldOptions.EarlyInfdev, 1.0D, 0.5D);
                }
                case "Indev 223" -> {
                    return new FixedBiomeSource(BetterWorldOptions.Indev, 1.0D, 0.5D);
                }
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
        WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value);
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
}
