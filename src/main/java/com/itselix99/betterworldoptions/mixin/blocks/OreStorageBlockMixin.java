package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.OreStorageBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(OreStorageBlock.class)
public class OreStorageBlockMixin extends Block {
    public OreStorageBlockMixin(int id, Material material) {
        super(id, material);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int bwo_getOldTextureOreStorage(int original, int side) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = ((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value;
        boolean oldFeatures = ((BooleanOptionStorage) bwoWorldPropertiesStorage.getOptionValue("OldFeatures", OptionType.GENERAL_OPTION)).value;

        if (oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (side == 1) {
                if (this.id == 57) {
                    return WorldTypes.getOldTexture(worldType, "DiamondBlockTop", original);
                } else if (this.id == 41) {
                    return WorldTypes.getOldTexture(worldType, "GoldBlockTop", original);
                } else if (this.id == 42) {
                    return WorldTypes.getOldTexture(worldType, "IronBlockTop", original);
                }
            } else if (side == 0) {
                if (this.id == 57) {
                    return WorldTypes.getOldTexture(worldType, "DiamondBlockBottom", original);
                } else if (this.id == 41) {
                    return WorldTypes.getOldTexture(worldType, "GoldBlockBottom", original);
                } else if (this.id == 42) {
                    return WorldTypes.getOldTexture(worldType, "IronBlockBottom", original);
                }
            } else {
                if (this.id == 57) {
                    return WorldTypes.getOldTexture(worldType, "DiamondBlockSide", original);
                } else if (this.id == 41) {
                    return WorldTypes.getOldTexture(worldType, "GoldBlockSide", original);
                } else if (this.id == 42) {
                    return WorldTypes.getOldTexture(worldType, "IronBlockSide", original);
                }
            }
        }

        return original;
    }
}