package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawContext {
    @Shadow private Minecraft minecraft;

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",
                    ordinal = 1
            )
    )
    private void renderHardcoreHearts(int target, int texture, Operation<Void> original) {
        if (((BWOProperties) this.minecraft.world.getProperties()).bwo_isHardcore()) {
            int hardcoreHearts = this.minecraft.textureManager.getTextureId("/assets/betterworldoptions/stationapi/textures/gui/iconsWithHardcoreHearts.png");
            original.call(target, hardcoreHearts);
        } else {
            original.call(target, texture);
        }
    }
}
