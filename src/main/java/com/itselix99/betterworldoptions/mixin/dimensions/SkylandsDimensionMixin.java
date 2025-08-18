package com.itselix99.betterworldoptions.mixin.dimensions;

import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.SkylandsDimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkylandsDimension.class)
public class SkylandsDimensionMixin extends Dimension implements StationDimension {

    @Override
    public int getHeight() {
        return 128;
    }
}