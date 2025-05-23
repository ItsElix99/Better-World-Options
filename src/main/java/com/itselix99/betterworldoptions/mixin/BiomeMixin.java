package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.interfaces.CustomRandomTreeFeature;
import com.itselix99.betterworldoptions.world.biomes.ClassicBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Biome.class)
public class BiomeMixin implements CustomRandomTreeFeature {

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    public void getSkyColor(float par1, CallbackInfoReturnable<Integer> cir) {
        if (this.equals(ClassicBiomes.EarlyInfdev)) {
            cir.setReturnValue(200);
        } else if (this.equals(ClassicBiomes.Infdev)) {
            cir.setReturnValue(10079487);
        } else if (this.equals(ClassicBiomes.Alpha)) {
            cir.setReturnValue(8961023);
        }
    }

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) { return new LargeOakTreeFeature(); }

    @Override
    public Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random) {
        return new OakTreeFeature();
    }
}