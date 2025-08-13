package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin extends BlockWithEntity {

    public FurnaceBlockMixin(int i, Material material) {
        super(i, material);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();

        if (worldGenerationOptions != null && !worldGenerationOptions.betaFeatures && worldGenerationOptions.oldTextures) {
            WorldTypeList.WorldTypeEntry worldType = WorldTypeList.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldTypeName)).toList().get(0);

            if (side == 1) {
                return worldType.OLD_TEXTURES.get("FurnaceTop") != null ? worldType.OLD_TEXTURES.get("FurnaceTop") : original;
            }
        }

        return original;
    }
}