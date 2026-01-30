package com.itselix99.betterworldoptions.mixin.world;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkCache.class)
public class ChunkCacheMixin {
    @Unique private String worldType;
    @Unique private boolean superflat;
    @Unique private boolean finiteWorld;
    @Unique private String finiteType;
    @Unique private int sizeX;
    @Unique private int sizeZ;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_init(World world, ChunkStorage storage, ChunkSource generator, CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.superflat = bwoProperties.bwo_getBooleanOptionValue("Superflat", OptionType.WORLD_TYPE_OPTION);
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);
        this.sizeX = bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION);
        this.sizeZ = bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION);
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

            if (this.worldType.equals("Indev 223")) {
                if (blockX < 0 || blockX >= this.sizeX || blockZ < 0 || blockZ >= this.sizeZ) {
                    return true;
                }
            } else if (this.finiteType.equals("MCPE")) {
                if (this.worldType.equals("Early Infdev")) {
                    if (blockX < -this.sizeX / 2 || blockX >= this.sizeX / 2 || blockZ < -this.sizeZ / 2 || blockZ >= this.sizeZ / 2) {
                        return true;
                    }
                } else {
                    if (blockX < 0 || blockX >= this.sizeX || blockZ < 0 || blockZ >= this.sizeZ) {
                        return true;
                    }
                }
            } else {
                if (this.finiteType.equals("LCE")) {
                    if (blockX < -this.sizeX / 2 || blockX >= this.sizeX / 2 || blockZ < -this.sizeZ / 2 || blockZ >= this.sizeZ / 2) {
                        return true;
                    }
                } else {
                    if (blockX < -((double) this.sizeX / 2) * 1.2D || blockX >= ((double) this.sizeX / 2) * 1.2D || blockZ < -((double) this.sizeZ / 2) * 1.2D || blockZ >= ((double) this.sizeZ / 2) * 1.2D) {
                        return true;
                    }
                }
            }
        }

        return original.call(chunk);
    }
}