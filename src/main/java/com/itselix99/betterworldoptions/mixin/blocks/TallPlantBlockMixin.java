package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TallPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TallPlantBlock.class)
public class TallPlantBlockMixin extends PlantBlock {
    public TallPlantBlockMixin(int id, int textureId) {
        super(id, textureId);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int getTexture(int original, int side, int meta) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();

        if (worldGenerationOptions.oldFeatures && worldGenerationOptions.oldTextures) {
            WorldType.WorldTypeEntry worldType = WorldType.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldTypeName)).toList().get(0);

            if (meta == 1) {
                return worldType.OLD_TEXTURES.get("Grass") != null ? worldType.OLD_TEXTURES.get("Grass") : original;
            } else if (meta == 2) {
                return worldType.OLD_TEXTURES.get("Fern") != null ? worldType.OLD_TEXTURES.get("Fern") : original;
            } else {
                return meta == 0 ? (worldType.OLD_TEXTURES.get("Fern") != null ? worldType.OLD_TEXTURES.get("Fern") : original) : (worldType.OLD_TEXTURES.get("Grass") != null ? worldType.OLD_TEXTURES.get("Grass") : original);
            }
        }

        return original;
    }
}