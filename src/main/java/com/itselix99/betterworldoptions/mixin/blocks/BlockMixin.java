package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow @Final @Mutable public final int id;

    public BlockMixin(int id) {
        this.id = id;
    }

    @ModifyReturnValue(method = "getTexture*", at = @At("RETURN"))
    public int getTexture(int original, int side) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();

        if (worldGenerationOptions != null && !worldGenerationOptions.betaFeatures && worldGenerationOptions.oldTextures) {
            WorldTypeList.WorldTypeEntry worldType = WorldTypeList.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(worldGenerationOptions.worldTypeName)).toList().get(0);

            if (this.id == 4) {
                return worldType.OLD_TEXTURES.get("Cobblestone") != null ? worldType.OLD_TEXTURES.get("Cobblestone") : original;
            }else if (this.id == 45) {
                return worldType.OLD_TEXTURES.get("BrickBlock") != null ? worldType.OLD_TEXTURES.get("BrickBlock") : original;
            } else if (this.id == 38) {
                return worldType.OLD_TEXTURES.get("Rose") != null ? worldType.OLD_TEXTURES.get("Rose") : original;
            } else if (this.id == 79) {
                return worldType.OLD_TEXTURES.get("IceBlock") != null ? worldType.OLD_TEXTURES.get("IceBlock") : original;
            }
        }

        return original;
    }
}