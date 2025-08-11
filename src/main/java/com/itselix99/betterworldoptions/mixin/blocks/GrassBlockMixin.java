package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.event.TextureListener;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrassBlock.class)
public class GrassBlockMixin extends Block {
    public GrassBlockMixin(int id, Material material) {
        super(id, material);
    }

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getTextureId", at = @At("RETURN"))
    public int getTextureId(int original, BlockView blockView, int x, int y, int z, int side) {
        @Deprecated Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();

        if (WorldSettings.GameMode.isBetaFeaturesWorldTypes(worldType) && !betaFeatures && !WorldSettings.Textures.isBetaFeaturesTextures()) {
            if (side == 1) {
                return worldType.equals("MCPE") ? 0 :TextureListener.alphaGrassBlockTop;
            } else if (side == 0) {
                return 2;
            } else {
                Material var6 = blockView.getMaterial(x, y + 1, z);
                return var6 != Material.SNOW_LAYER && var6 != Material.SNOW_BLOCK ? TextureListener.alphaGrassBlockSide : 68;
            }
        } else if(worldType.equals("Alpha 1.2.0") && minecraft.world.dimension.id == 0) {
            if (side == 1) {
                return 0;
            } else if (side == 0) {
                return 2;
            } else {
                Material var6 = blockView.getMaterial(x, y + 1, z);
                return var6 != Material.SNOW_LAYER && var6 != Material.SNOW_BLOCK ? TextureListener.alphaGrassBlockSide : 68;
            }
        } else {
            return original;
        }
    }
}