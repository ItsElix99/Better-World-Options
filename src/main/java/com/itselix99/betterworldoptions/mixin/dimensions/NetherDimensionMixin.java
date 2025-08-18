package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.NetherDimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherDimension.class)
public class NetherDimensionMixin extends Dimension implements StationDimension {

    @Override
    public int getHeight() {
        return !CompatMods.bnbLoaded() ? 128 : super.getHeight();
    }

    @ModifyReturnValue(method = "hasWorldSpawn", at = @At("RETURN"))
    private boolean netherHasWorldSpawn(boolean original) {
        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();

        if (worldType.equals("Nether")) {
            return true;
        } else {
            return original;
        }

    }
}