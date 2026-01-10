package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.compat.CompatMods;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CreateWorldScreen.class, priority = 1100)
public class RemoveBHCreativeGamemodeButton extends Screen {

    @Inject(method = "init", at = @At("TAIL"))
    private void bwo_removeBHCreativeGamemodeButton(CallbackInfo ci) {
        if (CompatMods.BHCreativeLoaded()) {
            TranslationStorage translation = TranslationStorage.getInstance();
            this.buttons.removeIf(button -> button instanceof ButtonWidget && ((ButtonWidget) button).text.equals(translation.get("title.bhcreative.selectWorld.survival")));
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bwo_fixPosYCancelButton(CallbackInfo ci) {
        if (CompatMods.BHCreativeLoaded()) {
            TranslationStorage translation = TranslationStorage.getInstance();
            ButtonWidget cancelButton = (ButtonWidget) this.buttons.stream().filter(button -> button instanceof ButtonWidget && ((ButtonWidget) button).text.equals(translation.get("gui.cancel"))).toList().get(0);
            cancelButton.y = this.height - 28;
        }
    }
}