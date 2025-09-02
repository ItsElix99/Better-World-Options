package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldType;
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
    public int getTexture(int original, int side) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();

        if (worldGenerationOptions.oldFeatures && worldGenerationOptions.oldTextures) {
            WorldType.WorldTypeEntry worldType = WorldType.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldType)).toList().get(0);

            if (side == 1) {
                if (this.id == 57) {
                    return worldType.OLD_TEXTURES.get("DiamondBlockTop") != null ? worldType.OLD_TEXTURES.get("DiamondBlockTop") : original;
                } else if (this.id == 41) {
                    return worldType.OLD_TEXTURES.get("GoldBlockTop") != null ? worldType.OLD_TEXTURES.get("GoldBlockTop") : original;
                } else if (this.id == 42) {
                    return worldType.OLD_TEXTURES.get("IronBlockTop") != null ? worldType.OLD_TEXTURES.get("IronBlockTop") : original;
                }
            } else if (side == 0) {
                if (this.id == 57) {
                    return worldType.OLD_TEXTURES.get("DiamondBlockBottom") != null ? worldType.OLD_TEXTURES.get("DiamondBlockBottom") : original;
                } else if (this.id == 41) {
                    return worldType.OLD_TEXTURES.get("GoldBlockBottom") != null ? worldType.OLD_TEXTURES.get("GoldBlockBottom") : original;
                } else if (this.id == 42) {
                    return worldType.OLD_TEXTURES.get("IronBlockBottom") != null ? worldType.OLD_TEXTURES.get("IronBlockBottom") : original;
                }
            } else {
                if (this.id == 57) {
                    return worldType.OLD_TEXTURES.get("DiamondBlockSide") != null ? worldType.OLD_TEXTURES.get("DiamondBlockSide") : original;
                } else if (this.id == 41) {
                    return worldType.OLD_TEXTURES.get("GoldBlockSide") != null ? worldType.OLD_TEXTURES.get("GoldBlockSide") : original;
                } else if (this.id == 42) {
                    return worldType.OLD_TEXTURES.get("IronBlockSide") != null ? worldType.OLD_TEXTURES.get("IronBlockSide") : original;
                }
            }
        }

        return original;
    }
}