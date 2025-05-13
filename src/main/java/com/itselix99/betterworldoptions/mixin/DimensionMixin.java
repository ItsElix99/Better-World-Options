package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.world.WorldSettings;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Dimension.class)
public class DimensionMixin {
    @Shadow public BiomeSource biomeSource;
    @Shadow public boolean isNether;
    @Shadow public boolean evaporatesWater;
    @Shadow public boolean hasCeiling;
    @Shadow public float[] lightLevelToLuminance;
    @Shadow public World world;

    @Inject(method = "initBiomeSource", at = @At("HEAD"), cancellable = true)
    protected void initBiomeSource(CallbackInfo ci) {
        if (WorldSettings.customBiome != null) {
            this.biomeSource = new FixedBiomeSource(WorldSettings.customBiome, 1.0D, 0.5D);
            ci.cancel();
        }

        if (WorldSettings.skyDisabled) {
            this.hasCeiling = true;
        }

        if (WorldSettings.customBiome == Biome.HELL) {
            this.isNether = true;
            this.evaporatesWater = true;
        }
    }

    @Inject(method = "initBrightnessTable", at = @At("HEAD"), cancellable = true)
    private void injectBrightnessTable(CallbackInfo ci) {
        if (this.lightLevelToLuminance == null) {
            this.lightLevelToLuminance = new float[16];
        }

        float ambientLight;
        if (WorldSettings.lightingMode == 1) {
            ambientLight = 0.1F;
        } else {
            ambientLight = 0.05F;
        }

        for (int i = 0; i <= 15; ++i) {
            float brightness = 1.0F - (float)i / 15.0F;
            this.lightLevelToLuminance[i] = (1.0F - brightness) / (brightness * 3.0F + 1.0F) * (1.0F - ambientLight) + ambientLight;
        }
        ci.cancel();
    }

    @Inject(method = "createChunkGenerator", at = @At("HEAD"), cancellable = true)
    private void injectCreateChunkGenerator(CallbackInfoReturnable<ChunkSource> cir) {
        if (WorldSettings.chunkProviderConstructor != null) {
            try {
                ChunkSource chunkSource = WorldSettings.chunkProviderConstructor.newInstance(this.world, this.world.getSeed());
                cir.setReturnValue(chunkSource);
            } catch (Exception e) {
                e.printStackTrace();
                cir.setReturnValue(new OverworldChunkGenerator(this.world, this.world.getSeed()));
            }
            cir.cancel();
        }
    }

    @Inject(method = "isValidSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void injectIsValidSpawnPoint(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        int blockId = this.world.getSpawnBlockId(x, z);

        boolean valid = (blockId != Block.BEDROCK.id && blockId != 0) &&
                (WorldSettings.blockToSpawnOn <= 0 ||
                        blockId == WorldSettings.blockToSpawnOn ||
                        Block.BLOCKS[blockId].material.isSolid());

        cir.setReturnValue(valid);
        cir.cancel();
    }
}
