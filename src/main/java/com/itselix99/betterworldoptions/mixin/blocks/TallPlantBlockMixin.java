package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.TallPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(TallPlantBlock.class)
public class TallPlantBlockMixin {

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int bwo_getOldTextureTallPlant(int original, int side, int meta) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
        boolean oldFeatures = bwoWorldPropertiesStorage.getBooleanOptionValue("OldFeatures", OptionType.GENERAL_OPTION);

        if (oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (meta == 1) {
                return WorldTypes.getOldTexture(worldType, "Grass", original);
            } else if (meta == 2) {
                return WorldTypes.getOldTexture(worldType, "Fern", original);
            } else {
                return meta == 0 ? WorldTypes.getOldTexture(worldType, "Fern", original) : WorldTypes.getOldTexture(worldType, "Grass", original);
            }
        }

        return original;
    }
}