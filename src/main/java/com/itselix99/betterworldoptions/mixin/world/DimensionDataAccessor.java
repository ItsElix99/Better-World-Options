package com.itselix99.betterworldoptions.mixin.world;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(World.class)
public interface DimensionDataAccessor {
    @Accessor("dimensionData")
    WorldStorage getDimensionData();
}