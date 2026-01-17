package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.FurnaceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(FurnaceBlock.class)
public class FurnaceBlockMixin {

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int bwo_getOldTextureFurnace(int original, int side) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = ((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value;
        boolean oldFeatures = ((BooleanOptionStorage) bwoWorldPropertiesStorage.getOptionValue("OldFeatures", OptionType.GENERAL_OPTION)).value;

        if (oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (side == 1) {
                return WorldTypes.getOldTexture(worldType, "FurnaceTop", original);
            }
        }

        return original;
    }
}