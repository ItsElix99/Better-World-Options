package com.itselix99.betterworldoptions.mixin.chunk;

import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.chunk.ChunkSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerChunkCache.class)
public interface ServerChunkGeneratorAccessor {
    @Accessor("generator")
    ChunkSource getChunkGenerator();
}