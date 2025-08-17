package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Mixin(Dimension.class)
public class DimensionMixin implements StationDimension {
    @Shadow public BiomeSource biomeSource;
    @Shadow public World world;

    @Unique private String worldType;
    @Unique private boolean betaFeatures;
    @Unique private String singleBiome;
    @Unique private String theme;

    @Inject(method = "initBiomeSource", at = @At("HEAD"), cancellable = true)
    protected void initBiomeSource(CallbackInfo ci) {
        this.worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        this.betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
        this.singleBiome = ((BWOProperties) this.world.getProperties()).bwo_getSingleBiome();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();

        if (!this.betaFeatures && !this.worldType.equals("MCPE")) {
            if (this.theme.equals("Hell")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevHell, 1.0D, 0.0D);
            } else if (this.theme.equals("Paradise")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevParadise, 1.0D, 0.5D);
            } else if (this.theme.equals("Woods")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevWoods, 1.0D, 0.5D);
            } else if (this.worldType.equals("Flat")) {
                this.biomeSource = new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.5D);
            } else if (this.worldType.equals("Alpha 1.1.2_01")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Alpha, 1.0D, 0.5D);
            } else if (this.worldType.equals("Infdev 611") || this.worldType.equals("Infdev 420") || this.worldType.equals("Infdev 415")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
            } else if (this.worldType.equals("Early Infdev")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.EarlyInfdev, 1.0D, 0.5D);
            } else if (this.worldType.equals("Indev 223")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevNormal, 1.0D, 0.5D);
            }
        } else {
            if (!this.singleBiome.equals("Off")) {
                switch (this.singleBiome) {
                    case "Rainforest" -> this.biomeSource = new FixedBiomeSource(Biome.RAINFOREST, 1.0D, 1.0D);
                    case "Swampland" -> this.biomeSource = new FixedBiomeSource(Biome.SWAMPLAND, 0.6D, 0.6D);
                    case "Seasonal Forest" -> this.biomeSource = new FixedBiomeSource(Biome.SEASONAL_FOREST, 1.0D, 0.8D);
                    case "Forest" -> this.biomeSource = new FixedBiomeSource(Biome.FOREST, 0.7D, 0.5D);
                    case "Savanna" -> this.biomeSource = new FixedBiomeSource(Biome.SAVANNA, 0.8D, 0.1D);
                    case "Shrubland" -> this.biomeSource = new FixedBiomeSource(Biome.SHRUBLAND, 0.6D, 0.3D);
                    case "Taiga" -> this.biomeSource = new FixedBiomeSource(Biome.TAIGA, 0.3D, 0.4D);
                    case "Desert" -> this.biomeSource = new FixedBiomeSource(Biome.DESERT, 1.0D, 0.1D);
                    case "Plains" -> this.biomeSource = new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.4D);
                    case "Ice Desert" -> this.biomeSource = new FixedBiomeSource(Biome.ICE_DESERT, 0.0D, 0.1D);
                    case "Tundra" -> this.biomeSource = new FixedBiomeSource(Biome.TUNDRA, 0.0D, 1.0D);
                }
            } else {
                this.biomeSource = new BiomeSource(this.world);
            }
        }

        ci.cancel();
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
        if (this.theme.equals("Paradise")) {
            return 0.0F;
        }

        return original;
    }

    @Override
    public int getHeight() {
        return Config.BWOConfig.world.worldHeightLimit.getIntValue();
    }
}
