package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GrassBlock.class)
public class GrassBlockMixin extends Block {
    public GrassBlockMixin(int id, Material material) {
        super(id, material);
    }

    @ModifyReturnValue(method = "getTextureId", at = @At("RETURN"))
    public int getTextureId(int original, BlockView blockView, int x, int y, int z, int side) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();

        if (worldGenerationOptions.oldFeatures && worldGenerationOptions.oldTextures) {
            WorldTypeEntry worldType = WorldType.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldType)).toList().get(0);

            if (side == 1) {
                return worldType.OLD_TEXTURES.get("GrassBlockTop") != null ? worldType.OLD_TEXTURES.get("GrassBlockTop") : original;
            } else if (side == 0) {
                return original;
            } else {
                Material var6 = blockView.getMaterial(x, y + 1, z);
                return var6 != Material.SNOW_LAYER && var6 != Material.SNOW_BLOCK ? (worldType.OLD_TEXTURES.get("GrassBlockSide") != null ? worldType.OLD_TEXTURES.get("GrassBlockSide") : original) : original;
            }
        }

        return original;
    }
}