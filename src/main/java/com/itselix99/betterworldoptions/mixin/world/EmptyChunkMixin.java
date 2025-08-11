package com.itselix99.betterworldoptions.mixin.world;


import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmptyChunk.class)
public class EmptyChunkMixin extends Chunk {

    public EmptyChunkMixin(World world, int x, int z) {
        super(world, x, z);
    }

    @ModifyReturnValue(method = "getBlockId", at = @At("RETURN"))
    private int invisibleBedrock(int original) {
        return BetterWorldOptions.INVISIBLE_BEDROCK.id;
    }
}