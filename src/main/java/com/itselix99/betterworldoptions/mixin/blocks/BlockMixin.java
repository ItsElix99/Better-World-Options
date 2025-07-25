package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.event.TextureListener;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow @Final @Mutable public final int id;

    public BlockMixin(int id) {
        this.id = id;
    }

    @ModifyReturnValue(method = "getTexture*", at = @At("RETURN"))
    public int getTexture(int original, int side) {
        if (!WorldSettings.GameMode.isBetaFeaturesTextures()) {
            if (this.id == 4) {
                return TextureListener.alphaCobblestone;
            } else {
                return original;
            }
        } else {
            return original;
        }
    }
}