package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.event.TextureListener;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends TransparentBlock {

    public LeavesBlockMixin(int id, int textureId, Material material, boolean transparent) {
        super(id, textureId, material, transparent);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side, int meta) {
        if (!WorldSettings.GameMode.isBetaFeaturesTextures() && !((meta & 1) == 1 || (meta & 2) == 2)) {
            if (this.renderSides) {
                return TextureListener.alphaLeaves;
            } else {
                return TextureListener.alphaLeavesOpaque;
            }

        } else {
            return original;
        }
    }
}