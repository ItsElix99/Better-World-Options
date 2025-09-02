package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends TransparentBlock {

    public LeavesBlockMixin(int id, int textureId, Material material, boolean transparent) {
        super(id, textureId, material, transparent);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side, int meta) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();

        if (worldGenerationOptions.oldFeatures && worldGenerationOptions.oldTextures) {
            WorldType.WorldTypeEntry worldType = WorldType.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldType)).toList().get(0);

            if ((!worldType.NAME.equals("MCPE") && (meta & 3) == 0) || (worldType.NAME.equals("MCPE") && ((meta & 3) == 0 || (meta & 3) == 2))) {
                if (this.renderSides) {
                    return worldType.OLD_TEXTURES.get("Leaves") != null ? worldType.OLD_TEXTURES.get("Leaves") : original;
                } else {
                    return worldType.OLD_TEXTURES.get("LeavesOpaque") != null ? worldType.OLD_TEXTURES.get("LeavesOpaque") : original;
                }
            }
        }

        return original;
    }

//    @Environment(EnvType.CLIENT)
//    @ModifyReturnValue(method = "getColorMultiplier", at = @At("RETURN"))
//    public int getColorMultiplier(int original, BlockView blockView, int x, int y, int z) {
//        int var5 = blockView.getBlockMeta(x, y, z);
//        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();
//
//        if (worldGenerationOptions.oldFeatures && worldGenerationOptions.oldTextures) {
//            WorldType.WorldTypeEntry worldType = WorldType.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldType)).toList().get(0);
//
//            if (worldType.NAME.equals("MCPE")) {
//                if (((var5 & 3) == 0 || (var5 & 3) == 2)) {
//                    return 16777215;
//                }
//            }
//        }
//
//        return original;
//    }
}