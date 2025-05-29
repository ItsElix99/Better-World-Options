package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.events.TextureListener;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TallPlantBlock.class)
public class TallPlantBlockMixin extends PlantBlock {
    public TallPlantBlockMixin(int id, int textureId) {
        super(id, textureId);
    }

    /**
     * @author ItsElix99
     * @reason Add alpha tallgrass and fern textures
     */
    @Overwrite
    public int getTexture(int side, int meta) {
        @Deprecated Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();

        if ((worldType.equals("Beta 1.1_02") || worldType.equals("Alpha 1.1.2_01") || worldType.equals("Infdev 420") || worldType.equals("Infdev 415") || worldType.equals("Early Infdev")) && !betaFeatures) {
            if (meta == 1) {
                return TextureListener.alphaTallGrass;
            } else if (meta == 2) {
                return TextureListener.alphaFern;
            } else {
                return meta == 0 ? this.textureId + 16 : TextureListener.alphaTallGrass;
            }
        } else {
            if (meta == 1) {
                return this.textureId;
            } else if (meta == 2) {
                return this.textureId + 16 + 1;
            } else {
                return meta == 0 ? this.textureId + 16 : this.textureId;
            }
        }
    }
}