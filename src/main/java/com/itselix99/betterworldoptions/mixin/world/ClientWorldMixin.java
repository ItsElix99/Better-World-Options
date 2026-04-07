package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.chunk.MultiplayerChunkCache;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Shadow private MultiplayerChunkCache chunkCache;

    @WrapOperation
            (
                    method = "updateChunk",
                    at = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/world/chunk/MultiplayerChunkCache;loadChunk(II)Lnet/minecraft/world/chunk/Chunk;"
                    )
            )
    public Chunk updateChunk(MultiplayerChunkCache instance, int chunkX, int chunkZ, Operation<Chunk> original) {
        return ((BWOWorld) this.chunkCache).bwo_loadFiniteWorldLimitChunk(chunkX, chunkZ);
    }
}