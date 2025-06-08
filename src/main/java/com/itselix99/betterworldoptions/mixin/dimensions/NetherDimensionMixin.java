package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.NetherDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(NetherDimension.class)
public class NetherDimensionMixin extends Dimension {

    @ModifyReturnValue(method = "hasWorldSpawn", at = @At("RETURN"))
    private boolean netherHasWorldSpawn(boolean original) {
        if (Objects.equals(((BWOProperties) this.world.getProperties()).bwo_getWorldType(), "Nether")) {
            return true;
        } else {
            return original;
        }

    }
}