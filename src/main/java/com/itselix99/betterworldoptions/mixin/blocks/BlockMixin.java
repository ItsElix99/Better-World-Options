package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.util.Identifier;
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

        String worldType = bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");
        boolean oldFeatures = bwoWorldPropertiesStorage.getOptionValue("OldFeatures", OptionType.GENERAL_OPTION, false);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (this.id == 4) {
                return WorldType.getOldTexture(Identifier.of(worldType), "Cobblestone", original);
            }else if (this.id == 45) {
                return WorldType.getOldTexture(Identifier.of(worldType), "BrickBlock", original);
            } else if (this.id == 38) {
                return WorldType.getOldTexture(Identifier.of(worldType), "Rose", original);
            } else if (this.id == 79) {
                return WorldType.getOldTexture(Identifier.of(worldType), "IceBlock", original);
            }
        }

        return original;
    }
}