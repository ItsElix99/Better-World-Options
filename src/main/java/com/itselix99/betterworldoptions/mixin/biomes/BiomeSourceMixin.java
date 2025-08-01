package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.MTRandom;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin {
    @Unique private OctavePerlinNoiseSampler mcpeTemperatureSampler;
    @Unique private OctavePerlinNoiseSampler mcpeDownfallSampler;
    @Unique private OctavePerlinNoiseSampler mcpeWeirdnessSampler;
    @Shadow public double[] temperatureMap;
    @Shadow public double[] downfallMap;
    @Shadow public double[] weirdnessMap;
    @Unique private World world;
    @Unique private String worldType;
    @Unique private boolean betaFeatures;

    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void captureWorld(World world, CallbackInfo ci) {
        this.world = world;
        this.mcpeTemperatureSampler = new OctavePerlinNoiseSampler(new MTRandom((int) (this.world.getSeed() * 9871L)), 4);
        this.mcpeDownfallSampler = new OctavePerlinNoiseSampler(new MTRandom((int) (this.world.getSeed() * 39811L)), 4);
        this.mcpeWeirdnessSampler = new OctavePerlinNoiseSampler(new MTRandom((int) (this.world.getSeed() * 543321L)), 2);
        this.worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        this.betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
    }

    @Environment(EnvType.CLIENT)
    @WrapOperation(
            method = "getTemperature",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;sample([DDDIIDDD)[D"
            )
    )
    private double[] changeTemperatureMap(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE") && !this.betaFeatures) {
            return this.mcpeTemperatureSampler.create(map, (int) x, (int) z, 1, 1, 0.025F, 0.025F, 0.5F);
        } else {
            return original.call(sampler, map, x, z, width, depth, d, e, f);
        }
    }

    @WrapOperation(
            method = "create",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;sample([DDDIIDDD)[D",
                    ordinal = 0
            )
    )
    private double[] changeTemperatureMap2(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE") && !this.betaFeatures) {
            return this.mcpeTemperatureSampler.create(map, (int) x, (int) z, width, depth, 0.025F, 0.025F, 0.25F);
        } else {
            return original.call(sampler, map, x, z, width, depth, d, e, f);
        }
    }

    @WrapOperation(
            method = "create",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;sample([DDDIIDDD)[D",
                    ordinal = 1
            )
    )
    private double[] changeWeirdnessMap(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE") && !this.betaFeatures) {
            return this.mcpeWeirdnessSampler.create(this.weirdnessMap, (int) x, (int) z, width, depth, 0.25F, 0.25F, 0.5882352941176471);
        } else {
            return original.call(sampler, map, x, z, width, depth, d, e, f);
        }
    }

    @WrapOperation(
            method = "getBiomesInArea([Lnet/minecraft/world/biome/Biome;IIII)[Lnet/minecraft/world/biome/Biome;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;sample([DDDIIDDD)[D",
                    ordinal = 0
            )
    )
    private double[] changeTemperatureMap3(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE") && !this.betaFeatures) {
            return this.mcpeTemperatureSampler.create(this.temperatureMap, (int) x, (int) z, width, width, 0.025F, 0.025F, 0.25F);
        } else {
            return original.call(sampler, map, x, z, width, depth, d, e, f);
        }
    }

    @WrapOperation(
            method = "getBiomesInArea([Lnet/minecraft/world/biome/Biome;IIII)[Lnet/minecraft/world/biome/Biome;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;sample([DDDIIDDD)[D",
                    ordinal = 1
            )
    )
    private double[] changeDownfallMap(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE") && !this.betaFeatures) {
            return this.mcpeDownfallSampler.create(this.downfallMap, (int) x, (int) z, width, width, 0.05F, 0.05F, 0.3333333333333333);
        } else {
            return original.call(sampler, map, x, z, width, depth, d, e, f);
        }
    }

    @WrapOperation(
            method = "getBiomesInArea([Lnet/minecraft/world/biome/Biome;IIII)[Lnet/minecraft/world/biome/Biome;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;sample([DDDIIDDD)[D",
                    ordinal = 2
            )
    )
    private double[] changeWeirdnessMap2(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE") && !this.betaFeatures) {
            return this.mcpeWeirdnessSampler.create(this.weirdnessMap, (int) x, (int) z, width, width, 0.25F, 0.25F, 0.5882352941176471);
        } else {
            return original.call(sampler, map, x, z, width, depth, d, e, f);
        }
    }
}