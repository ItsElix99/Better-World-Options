package com.itselix99.betterworldoptions.mixin.render;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow private Minecraft client;
    @ModifyVariable(
            method = "renderSnow",
            at = @At("STORE"),
            ordinal = 1
    )
    private float bwo_alwaysSnowInWinterWorlds(float original) {
        if (((BWOProperties) this.client.world.getProperties()).bwo_getTheme().equals("Winter")) {
            if (Config.BWOConfig.environment.alwaysSnowInWinterWorlds) {
                return 1.0F;
            }
        }

        return original;
    }
}