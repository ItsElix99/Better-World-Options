package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.event.TextureListener;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends TransparentBlock {

    public LeavesBlockMixin(int id, int textureId, Material material, boolean transparent) {
        super(id, textureId, material, transparent);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side, int meta) {
        if (!WorldSettings.Textures.isBetaFeaturesTextures()) {
            if (!WorldSettings.Textures.isMcpe() && !((meta & 1) == 1 || (meta & 2) == 2)) {
                if (this.renderSides) {
                    return TextureListener.alphaLeaves;
                } else {
                    return TextureListener.alphaLeavesOpaque;
                }
            } else if (WorldSettings.Textures.isMcpe() && !((meta & 1) == 1)) {
                if (this.renderSides) {
                    return TextureListener.mcpeLeaves;
                } else {
                    return TextureListener.mcpeLeavesOpaque;
                }
            }
        }

        return original;
    }

    @ModifyReturnValue(method = "getColorMultiplier", at = @At("RETURN"))
    public int getColorMultiplier(int original, BlockView blockView, int x, int y, int z) {
        int var5 = blockView.getBlockMeta(x, y, z);
        @Deprecated Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();

        if (worldType.equals("MCPE") && !betaFeatures) {
            if (!((var5 & 1) == 1)) {
                return 16777215;
            }
        }

        return original;
    }
}