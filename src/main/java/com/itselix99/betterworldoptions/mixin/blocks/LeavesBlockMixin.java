package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.material.Material;
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

        String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
        boolean oldFeatures = bwoWorldPropertiesStorage.getBooleanOptionValue("OldFeatures", OptionType.GENERAL_OPTION);

        if (oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            WorldTypeEntry worldTypeEntry = WorldTypes.getWorldTypeByName(worldType);

            if ((!worldTypeEntry.name.equals("MCPE") && (meta & 3) == 0) || (worldTypeEntry.name.equals("MCPE") && ((meta & 3) == 0 || (meta & 3) == 2))) {
                if (this.renderSides) {
                    return WorldTypes.getOldTexture(worldType, "Leaves", original);
                } else {
                    return WorldTypes.getOldTexture(worldType, "LeavesOpaque", original);
                }
            }
        }

        return original;
    }
}