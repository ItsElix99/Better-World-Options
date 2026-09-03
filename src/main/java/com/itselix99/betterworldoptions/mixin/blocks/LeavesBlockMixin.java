package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends TransparentBlock {

    public LeavesBlockMixin(int id, int textureId, Material material, boolean transparent) {
        super(id, textureId, material, transparent);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int bwo_getOldTextureLeaves(int original, int side, int meta) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");
        boolean oldFeatures = bwoWorldPropertiesStorage.getOptionValue("OldFeatures", OptionType.GENERAL_OPTION, false);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            WorldType worldTypeEntry = WorldType.getWorldTypeById(Identifier.of(worldType));

            if ((!worldTypeEntry.getId().toString().equals(BetterWorldOptions.NAMESPACE.id("mcpe").toString()) && (meta & 3) == 0) || (worldTypeEntry.getId().toString().equals(BetterWorldOptions.NAMESPACE.id("mcpe").toString()) && ((meta & 3) == 0 || (meta & 3) == 2))) {
                if (this.renderSides) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "Leaves", original);
                } else {
                    return WorldType.getOldTexture(Identifier.of(worldType), "LeavesOpaque", original);
                }
            }
        }

        return original;
    }
}