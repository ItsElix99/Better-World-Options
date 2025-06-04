package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawContext {
    @Shadow private Minecraft minecraft;

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",
                    ordinal = 1
            )
    )
    private void render(int target, int texture) {
        if (((BWOProperties) this.minecraft.world.getProperties()).bwo_getHardcore()) {
            GL11.glBindTexture(target, this.minecraft.textureManager.getTextureId("/assets/betterworldoptions/stationapi/textures/gui/iconsWithHardcoreHearts.png"));
        } else {
            GL11.glBindTexture(target, texture);
        }
    }
}
