package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import net.minecraft.world.biome.RainforestBiome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(RainforestBiome.class)
public class RainforestBiomeMixin {

    @ModifyReturnValue(method = "getRandomTreeFeature", at = @At(value = "RETURN"))
    public Feature bwo_getRandomTreeFeatureByWorldType(Feature original, @Local(ordinal = 0, argsOnly = true) Random random) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();
        String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);

        if (worldType.equals("Infdev 415") || worldType.equals("Infdev 420")) {
            return random.nextInt(3) == 0 ? new OakTreeFeature() : new LargeOakTreeFeature();
        } else {
            return original;
        }
    }
}