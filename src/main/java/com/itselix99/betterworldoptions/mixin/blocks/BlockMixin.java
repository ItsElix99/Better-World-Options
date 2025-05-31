package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.events.TextureListener;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public class BlockMixin {
    @Shadow public int textureId;
    @Shadow @Final @Mutable public final int id;

    public BlockMixin(int id) {
        this.id = id;
    }

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getTextureId", at = @At("RETURN"))
    private int getTexture(int original) {
        if (!WorldSettings.isBetaFeatures) {
            if (this.id == 4) {
                this.textureId = TextureListener.alphaCobblestone;
                return this.textureId;
            } else {
                return original;
            }
        } else {
            if (this.id == 4) {
                this.textureId = 16;
                return this.textureId;
            }
        }
        return original;
    }
}