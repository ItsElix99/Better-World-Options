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
import net.minecraft.block.GrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GrassBlock.class)
public class GrassBlockMixin {

    @ModifyReturnValue(method = "getTextureId", at = @At("RETURN"))
    public int bwo_getOldTextureGrassBlock(int original, BlockView blockView, int x, int y, int z, int side) {
        Minecraft minecraft = BetterWorldOptions.getMinecraft();

        if (minecraft != null) {
            BWOWorld bwoWorld = (BWOWorld) minecraft.world;

            if (bwoWorld != null) {
                BWOProperties bwoProperties = (BWOProperties) minecraft.world.getProperties();
                BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

                WorldType worldType = bwoWorld.bwo_getWorldType();
                boolean oldFeatures = bwoProperties.bwo_isOldFeatures();

                if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
                    if (side == 1) {
                        return worldType.getOldTexture("GrassBlockTop", original);
                    } else if (side == 0) {
                        return original;
                    } else {
                        Material var6 = blockView.getMaterial(x, y + 1, z);
                        return var6 != Material.SNOW_LAYER && var6 != Material.SNOW_BLOCK ? worldType.getOldTexture("GrassBlockSide", original) : original;
                    }
                }
            }
        }

        return original;
    }
}