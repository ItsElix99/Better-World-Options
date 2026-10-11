package com.itselix99.betterworldoptions.mixin.render;

import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
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
        Theme theme = ((BWOWorld) this.client.world).bwo_getTheme();
        if (theme.isCold()) {
            if (Config.BWOConfig.environment.alwaysSnowInWinterWorlds) {
                return 1.0F;
            }
        }

        return original;
    }
}