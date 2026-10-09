package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source.AdvUpdBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Generator.class)
public abstract class GeneratorMixin {
    @Shadow protected Random random;
    @Shadow protected int range;

    @Shadow protected abstract void place(World world, int startChunkX, int startChunkZ, int chunkX, int chunkZ, byte[] blocks);

    @Inject(method = "place(Lnet/minecraft/world/chunk/ChunkSource;Lnet/minecraft/world/World;II[B)V", at = @At(value = "HEAD"), cancellable = true)
    public void bwo_setSeed(ChunkSource source, World world, int chunkX, int chunkZ, byte[] blocks, CallbackInfo ci) {
        if (world.method_1781() instanceof AdvUpdBiomeSource) {
            int var6 = this.range;
            this.random.setSeed(world.getSeed());
            long var7 = this.random.nextLong();
            long var9 = this.random.nextLong();

            for(int var11 = chunkX - var6; var11 <= chunkX + var6; ++var11) {
                for(int var12 = chunkZ - var6; var12 <= chunkZ + var6; ++var12) {
                    long var13 = (long)var11 * var7;
                    long var15 = (long)var12 * var9;
                    this.random.setSeed(var13 ^ var15 ^ world.getSeed());
                    this.place(world, var11, var12, chunkX, chunkZ, blocks);
                }
            }

            ci.cancel();
        }
    }
}