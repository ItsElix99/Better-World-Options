package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.event.TextureListener;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.OreStorageBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OreStorageBlock.class)
public class OreStorageBlockMixin extends Block {
    public OreStorageBlockMixin(int id, Material material) {
        super(id, material);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side) {
        if (!WorldSettings.GameMode.isBetaFeaturesTextures()) {
            if (side == 1) {
                return this.textureId;
            } else if (side == 0) {
                if (this.id == 57) {
                    return TextureListener.alphaDiamondBlockBottom;
                } else if (this.id == 41) {
                    return TextureListener.alphaGoldBlockBottom;
                } else if (this.id == 42) {
                    return TextureListener.alphaIronBlockBottom;
                } else {
                    return this.textureId;
                }
            } else {
                if (this.id == 57) {
                    return TextureListener.alphaDiamondBlockSide;
                } else if (this.id == 41) {
                    return TextureListener.alphaGoldBlockSide;
                } else if (this.id == 42) {
                    return TextureListener.alphaIronBlockSide;
                } else {
                    return this.textureId;
                }
            }
        } else {
            return original;
        }
    }
}