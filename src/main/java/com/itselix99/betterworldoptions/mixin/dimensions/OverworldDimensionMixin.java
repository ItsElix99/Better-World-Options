package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.config.Config;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverworldDimension.class)
public class OverworldDimensionMixin extends Dimension implements StationDimension {

    @Override
    public int getHeight() {
        return Config.BWOConfig.world.worldHeightLimit.getIntValue();
    }
}