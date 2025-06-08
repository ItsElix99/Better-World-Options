package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Mixin(Dimension.class)
public class DimensionMixin {
    @Shadow public BiomeSource biomeSource;
    @Shadow public boolean isNether;
    @Shadow public boolean evaporatesWater;
    @Shadow public boolean hasCeiling;
    @Shadow public World world;

    @Inject(method = "initBiomeSource", at = @At("HEAD"), cancellable = true)
    protected void initBiomeSource(CallbackInfo ci) {
        if (WorldSettings.singleBiome != null) {
            this.biomeSource = new FixedBiomeSource(WorldSettings.singleBiome, 1.0D, 0.5D);
        } else if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getWorldType(), "Flat") && !((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            this.biomeSource = new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.5D);
        } else if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getWorldType(), "Early Infdev") && !((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.EarlyInfdev, 1.0D, 0.5D);
        } else if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getWorldType(), "Infdev 415") && !((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
        } else if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getWorldType(), "Infdev 420") && !((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
        } else if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getWorldType(), "Infdev 611") && !((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
        } else if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getWorldType(), "Alpha 1.1.2_01") && !((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures()) {
            if (((BWOProperties) this.world.getProperties()).bwo_getSnowCovered()) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.WinterAlpha, 0.0D, 0.5D);
            } else {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Alpha, 1.0D, 0.5D);
            }
        } else {
            this.biomeSource = new BiomeSource(this.world);
        }

        if (WorldSettings.skyDisabled) {
            this.hasCeiling = true;
        }

        if (WorldSettings.singleBiome == Biome.HELL) {
            this.isNether = true;
            this.evaporatesWater = true;
        }
        ci.cancel();
    }

    @ModifyVariable(
            method = "initBrightnessTable",
            at = @At("LOAD"),
            ordinal = 0
    )
    private float modifyBrightnessBase(float original) {
        if (WorldSettings.lightingMode == 1) {
            return 0.1F;
        }
        return original;
    }

    @ModifyReturnValue(method = "createChunkGenerator", at = @At("RETURN"))
    public ChunkSource createChunkGenerator(ChunkSource original) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (WorldSettings.chunkGenerator != null) {
            return WorldSettings.chunkGenerator.newInstance(this.world, this.world.getSeed());
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "isValidSpawnPoint", at = @At("RETURN"))
    private boolean injectIsValidSpawnPoint(boolean original, int x, int z) {
        int var3 = this.world.getSpawnBlockId(x, z);
        if (WorldSettings.blockToSpawnOn != 0) {
            return var3 == WorldSettings.blockToSpawnOn;
        } else {
            return original;
        }
    }
}
