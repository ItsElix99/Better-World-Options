package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.OreStorageBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;
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

        String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
        boolean oldFeatures = bwoWorldPropertiesStorage.getBooleanOptionValue("OldFeatures", OptionType.GENERAL_OPTION);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
            if (side == 1) {
                if (this.id == 57) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "DiamondBlockTop", original);
                } else if (this.id == 41) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "GoldBlockTop", original);
                } else if (this.id == 42) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "IronBlockTop", original);
                }
            } else if (side == 0) {
                if (this.id == 57) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "DiamondBlockBottom", original);
                } else if (this.id == 41) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "GoldBlockBottom", original);
                } else if (this.id == 42) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "IronBlockBottom", original);
                }
            } else {
                if (this.id == 57) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "DiamondBlockSide", original);
                } else if (this.id == 41) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "GoldBlockSide", original);
                } else if (this.id == 42) {
                    return WorldType.getOldTexture(Identifier.of(worldType), "IronBlockSide", original);
                }
            }
        }

        return original;
    }
}