package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow @Final @Mutable public final int id;

    public BlockMixin(int id) {
        this.id = id;
    }

    @ModifyReturnValue(method = "getTexture*", at = @At("RETURN"))
    public int bwo_getOldTexture(int original, int side) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
        boolean oldFeatures = bwoWorldPropertiesStorage.getBooleanOptionValue("OldFeatures", OptionType.GENERAL_OPTION);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (this.id == 4) {
                return WorldTypes.getOldTexture(worldType, "Cobblestone", original);
            }else if (this.id == 45) {
                return WorldTypes.getOldTexture(worldType, "BrickBlock", original);
            } else if (this.id == 38) {
                return WorldTypes.getOldTexture(worldType, "Rose", original);
            } else if (this.id == 79) {
                return WorldTypes.getOldTexture(worldType, "IceBlock", original);
            }
        }

        return original;
    }
}