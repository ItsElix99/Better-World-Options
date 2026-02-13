package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GrassBlock.class)
public class GrassBlockMixin {

    @ModifyReturnValue(method = "getTextureId", at = @At("RETURN"))
    public int bwo_getOldTextureGrassBlock(int original, BlockView blockView, int x, int y, int z, int side) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
        boolean oldFeatures = bwoWorldPropertiesStorage.getBooleanOptionValue("OldFeatures", OptionType.GENERAL_OPTION);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (side == 1) {
                return WorldTypes.getOldTexture(worldType, "GrassBlockTop", original);
            } else if (side == 0) {
                return original;
            } else {
                Material var6 = blockView.getMaterial(x, y + 1, z);
                return var6 != Material.SNOW_LAYER && var6 != Material.SNOW_BLOCK ? WorldTypes.getOldTexture(worldType, "GrassBlockSide", original) : original;
            }
        }

        return original;
    }
}