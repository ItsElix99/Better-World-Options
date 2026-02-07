package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.storage.ChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkCache.class)
public class ChunkCacheMixin {
    @Shadow private World world;
    @Unique private String worldType;
    @Unique private boolean oldFeatures;
    @Unique private boolean superflat;
    @Unique private boolean finiteWorld;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_init(World world, ChunkStorage storage, ChunkSource generator, CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.oldFeatures = bwoProperties.bwo_isOldFeatures();
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
        if (this.world.dimension.id == 0) {
            if (this.worldType.equals("Flat") && !this.superflat) {
                return true;
            } else if (this.finiteWorld && this.oldFeatures && this.worldType.equals("MCPE")) {
                int blockX = x * 16;
                int blockZ = z * 16;
                int[] sizeLimits = BWOChunkGenerator.getSizeLimits();

                if (sizeLimits != null) {
                    if (blockX < sizeLimits[0] || blockX >= sizeLimits[1] || blockZ < sizeLimits[2] || blockZ >= sizeLimits[3]) {
                        return true;
                    }
                }
            }
        }

        return original.call(chunk);
    }
}