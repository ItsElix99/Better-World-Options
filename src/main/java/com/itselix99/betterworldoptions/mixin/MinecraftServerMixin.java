package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.api.chunk.FiniteChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.mixin.world.ServerChunkGeneratorAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract ServerWorld getWorld(int dimensionId);

    @Inject(
            method = "shutdown",
            at = @At(value = "HEAD")
    )
    private void bwo_shutdownFiniteWorldStorage(CallbackInfo ci) {
        if (this.getWorld(0) != null) {
            BWOProperties bwoProperties = (BWOProperties) this.getWorld(0).getProperties();
            boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);

            if (((ServerChunkGeneratorAccessor) this.getWorld(0).getChunkSource()).getChunkGenerator() instanceof FiniteChunkGenerator finiteChunkGenerator && finiteWorld && bwoProperties.bwo_isPregeneratingFiniteWorld()) {
                try {
                    finiteChunkGenerator.shutdownStorage();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}