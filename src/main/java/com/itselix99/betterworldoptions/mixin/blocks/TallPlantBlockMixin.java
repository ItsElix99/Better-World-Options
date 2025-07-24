package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.events.TextureListener;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TallPlantBlock.class)
public class TallPlantBlockMixin extends PlantBlock {
    public TallPlantBlockMixin(int id, int textureId) {
        super(id, textureId);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side, int meta) {
        @Deprecated Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();

        if (WorldSettings.GameMode.isBetaFeaturesWorldTypes(worldType) && !betaFeatures && !WorldSettings.GameMode.isBetaTexturesTextures()) {
            if (meta == 1) {
                return TextureListener.alphaTallGrass;
            } else if (meta == 2) {
                return TextureListener.alphaFern;
            } else {
                return meta == 0 ? TextureListener.alphaFern : TextureListener.alphaTallGrass;
            }
        } else {
            return original;
        }
    }
}