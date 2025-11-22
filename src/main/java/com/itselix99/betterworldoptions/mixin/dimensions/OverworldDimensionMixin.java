package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverworldDimension.class)
public class OverworldDimensionMixin extends Dimension implements StationDimension {

    @Override
    public int getHeight() {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();
        if (this.world.isRemote && !worldGenerationOptions.isBWOServer) {
            return 128;
        }

        return Config.BWOConfig.world.worldHeightLimit.getIntValue();
    }
}