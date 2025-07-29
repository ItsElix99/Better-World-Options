package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.BWOConfig;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverworldChunkGenerator.class)
public abstract class OverworldChunkGeneratorMixin implements ChunkSource {
    @Shadow private World world;
    @Unique private Generator ravine = new RavineWorldCarver();

    @Inject(method = "getChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/Generator;place(Lnet/minecraft/world/chunk/ChunkSource;Lnet/minecraft/world/World;II[B)V"))
    private void ravineGeneration(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir, @Local byte[] var3) {
        if (BWOConfig.WORLD_CONFIG.ravineGeneration) {
            this.ravine.place(this, this.world, chunkX, chunkZ, var3);
        }
    }
}