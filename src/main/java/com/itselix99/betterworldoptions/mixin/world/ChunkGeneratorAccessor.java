package com.itselix99.betterworldoptions.mixin.world;

import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.chunk.ChunkSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkCache.class)
public interface ChunkGeneratorAccessor {
    @Accessor("generator")
    ChunkSource getChunkGenerator();
}