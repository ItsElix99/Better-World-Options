package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.TallPlantBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(TallPlantBlock.class)
public class TallPlantBlockMixin {

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int bwo_getOldTextureTallPlant(int original, int side, int meta) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");
        boolean oldFeatures = bwoWorldPropertiesStorage.getOptionValue("OldFeatures", OptionType.GENERAL_OPTION, false);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (meta == 1) {
                return WorldType.getOldTexture(Identifier.of(worldType), "Grass", original);
            } else if (meta == 2) {
                return WorldType.getOldTexture(Identifier.of(worldType), "Fern", original);
            } else {
                return meta == 0 ? WorldType.getOldTexture(Identifier.of(worldType), "Fern", original) : WorldType.getOldTexture(Identifier.of(worldType), "Grass", original);
            }
        }

        return original;
    }
}