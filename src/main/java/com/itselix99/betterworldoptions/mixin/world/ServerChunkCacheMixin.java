package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.storage.ChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
    @Unique private String worldType;
    @Unique private boolean superflat;
    @Unique private boolean finiteWorld;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_init(ServerWorld world, ChunkStorage storage, ChunkSource generator, CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.superflat = bwoProperties.bwo_getBooleanOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION);
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
    }

    @WrapOperation(
            method = "decorate",
            at = @At
                    (
                            value = "FIELD",
                            target = "Lnet/minecraft/world/chunk/Chunk;terrainPopulated:Z",
                            ordinal = 0
                    )
    )
    private boolean bwo_cancelDecorateInFiniteAndFlatWorld(Chunk chunk, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) int x, @Local(ordinal = 1, argsOnly = true) int z) {
        if (this.worldType.equals("Flat") && !this.superflat) {
            return true;
        } else if (this.finiteWorld) {
            int blockX = x * 16;
            int blockZ = z * 16;
            int[] sizeLimits = BWOChunkGenerator.getSizeLimits();

            if (sizeLimits != null) {
                if (blockX < sizeLimits[0] || blockX >= sizeLimits[1] || blockZ < sizeLimits[2] || blockZ >= sizeLimits[3]) {
                    return true;
                }
            }
        }

        return original.call(chunk);
    }
}