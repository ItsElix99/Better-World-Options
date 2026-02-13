package com.itselix99.betterworldoptions.mixin.render;

import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow private World world;

    @WrapOperation(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glBlendFunc(II)V",
                    ordinal = 1
            )
    )
    public void bwo_oldStars(int sfactor, int dfactor, Operation<Void> original) {
        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeaturesProperties != null && oldFeaturesProperties.oldStars) {
            original.call(dfactor, dfactor);
        }
    }

    @WrapOperation(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V",
                    ordinal = 0
            )
    )
    public void bwo_oldStars2(float red, float green, float blue, float alpha, Operation<Void> original) {
        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeaturesProperties != null && oldFeaturesProperties.oldStars) {
            original.call(alpha, alpha, alpha, alpha);
        }
    }
}