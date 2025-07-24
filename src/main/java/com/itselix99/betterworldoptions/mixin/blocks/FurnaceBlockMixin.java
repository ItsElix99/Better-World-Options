package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin extends BlockWithEntity {

    public FurnaceBlockMixin(int i, Material material) {
        super(i, material);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side) {
        if (side == 1) {
            if (!WorldSettings.GameMode.isBetaTexturesTextures()) {
                return 1;
            } else {
                return this.textureId + 17;
            }
        }
        return original;
    }
}