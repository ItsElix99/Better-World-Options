package com.itselix99.betterworldoptions.mixin.chunk;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.storage.ChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
    @Shadow private ServerWorld world;
    @Unique private String worldType;
    @Unique private String theme;
    @Unique private boolean oldFeatures;
    @Unique private boolean superflat;
    @Unique private boolean finiteWorld;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_init(ServerWorld world, ChunkStorage storage, ChunkSource generator, CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.theme = bwoProperties.bwo_getTheme();
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
            if (this.worldType.equals(BetterWorldOptions.NAMESPACE.id("flat").toString()) && !this.superflat) {
                if (this.theme.equals("Winter")) {
                    int blockX = x * 16;
                    int blockZ = z * 16;

                    for(int var1 = blockX + 8; var1 < blockX + 8 + 16; ++var1) {
                        for(int var2 = blockZ + 8; var2 < blockZ + 8 + 16; ++var2) {
                            int var3 = this.world.getTopSolidBlockY(var1, var2);
                            if (var3 > 0 && var3 < this.world.dimension.getHeight() && this.world.isAir(var1, var3, var2) && this.world.getMaterial(var1, var3 - 1, var2).blocksMovement() && this.world.getMaterial(var1, var3 - 1, var2) != Material.ICE) {
                                this.world.setBlock(var1, var3, var2, Block.SNOW.id);
                            }
                        }
                    }
                }
                return true;
            } else if (this.finiteWorld && this.oldFeatures && this.worldType.equals(BetterWorldOptions.NAMESPACE.id("mcpe").toString())) {
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