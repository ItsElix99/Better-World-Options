package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.MTRandom;
import com.itselix99.betterworldoptions.world.worldtypes.mcpe.util.math.noise.OctavePerlinNoiseSamplerMCPE;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin {
    @Unique private OctavePerlinNoiseSamplerMCPE mcpeTemperatureSampler;
    @Unique private OctavePerlinNoiseSamplerMCPE mcpeDownfallSampler;
    @Unique private OctavePerlinNoiseSamplerMCPE mcpeWeirdnessSampler;
    @Unique private String worldType;

    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void bwo_initMCPEBiomeSource(World world, CallbackInfo ci) {
        this.mcpeTemperatureSampler = new OctavePerlinNoiseSamplerMCPE(new MTRandom((int) (world.getSeed() * 9871L)), 4);
        this.mcpeDownfallSampler = new OctavePerlinNoiseSamplerMCPE(new MTRandom((int) (world.getSeed() * 39811L)), 4);
        this.mcpeWeirdnessSampler = new OctavePerlinNoiseSamplerMCPE(new MTRandom((int) (world.getSeed() * 543321L)), 2);
        this.worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
    }

    @Environment(EnvType.CLIENT)
    @WrapOperation(
            method = "getTemperature",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;sample([DDDIIDDD)[D"
            )
    )
    private double[] bwo_changeTemperatureNoiseInMCPE(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE")) {
            return this.mcpeTemperatureSampler.create(map, (int) x, (int) z, width, depth, d, e, f);
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
    private double[] bwo_changeTemperatureNoiseInMCPE2(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE")) {
            return this.mcpeTemperatureSampler.create(map, (int) x, (int) z, width, depth, d, e, f);
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
    private double[] bwo_changeWeirdnessNoiseInMCPE(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE")) {
            return this.mcpeWeirdnessSampler.create(map, (int) x, (int) z, width, depth, d, e, f);
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
    private double[] bwo_changeTemperatureNoiseInMCPE3(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE")) {
            return this.mcpeTemperatureSampler.create(map, (int) x, (int) z, width, width, d, e, f);
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
    private double[] bwo_changeDownfallNoiseInMCPE(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE")) {
            return this.mcpeDownfallSampler.create(map, (int) x, (int) z, width, width, d, e, f);
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
    private double[] bwo_changeWeirdnessNoiseInMCPE2(OctaveSimplexNoiseSampler sampler, double[] map, double x, double z, int width, int depth, double d, double e, double f, Operation<double[]> original) {
        if (this.worldType.equals("MCPE")) {
            return this.mcpeWeirdnessSampler.create(map, (int) x, (int) z, width, width, d, e, f);
        } else {
            return original.call(sampler, map, x, z, width, depth, d, e, f);
        }
    }
}