package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(SlabBlock.class)
public class SlabBlockMixin {

    @ModifyReturnValue(method = "getTexture(II)I", at = @At("RETURN"))
    public int bwo_getOldTextureSlab(int original, int side, int meta) {
        Minecraft minecraft = BetterWorldOptions.getMinecraft();

        if (minecraft != null) {
            BWOWorld bwoWorld = (BWOWorld) minecraft.world;

            if (bwoWorld != null) {
                BWOProperties bwoProperties = (BWOProperties) minecraft.world.getProperties();
                BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

                WorldType worldType = bwoWorld.bwo_getWorldType();
                boolean oldFeatures = bwoProperties.bwo_isOldFeatures();

                if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
                    return meta == 3 ? worldType.getOldTexture("Cobblestone", original) : original;
                }
            }
        }

        return original;
    }
}