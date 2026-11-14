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
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(TallPlantBlock.class)
public class TallPlantBlockMixin {

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int bwo_getOldTextureTallPlant(int original, int side, int meta) {
        Minecraft minecraft = BetterWorldOptions.getMinecraft();

        if (minecraft != null) {
            BWOWorld bwoWorld = (BWOWorld) minecraft.world;

            if (bwoWorld != null) {
                BWOProperties bwoProperties = (BWOProperties) minecraft.world.getProperties();
                BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

                WorldType worldType = bwoWorld.bwo_getWorldType();
                boolean oldFeatures = bwoProperties.bwo_isOldFeatures();

                if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
                    if (meta == 1) {
                        return worldType.getOldTexture("Grass", original);
                    } else if (meta == 2) {
                        return worldType.getOldTexture("Fern", original);
                    } else {
                        return meta == 0 ? worldType.getOldTexture("Fern", original) : worldType.getOldTexture("Grass", original);
                    }
                }
            }
        }

        return original;
    }
}