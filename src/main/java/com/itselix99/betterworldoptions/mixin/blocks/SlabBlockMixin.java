package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlabBlock.class)
public class SlabBlockMixin extends Block {

    public SlabBlockMixin(int id, int textureId, Material material) {
        super(id, textureId, material);
    }

    @ModifyReturnValue(method = "getTexture(II)I", at = @At("RETURN"))
    public int getTexture(int original, int side, int meta) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();

        if (worldGenerationOptions.oldFeatures && worldGenerationOptions.oldTextures) {
            WorldType.WorldTypeEntry worldType = WorldType.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldType)).toList().get(0);

            return meta == 3 ? (worldType.OLD_TEXTURES.get("Cobblestone") != null ? worldType.OLD_TEXTURES.get("Cobblestone") : original) : original;
        }

        return original;
    }
}