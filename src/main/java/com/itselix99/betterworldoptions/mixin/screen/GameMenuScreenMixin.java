package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.world.WorldSettings;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 1) {
            WorldSettings.resetSettings();
        }
    }
}