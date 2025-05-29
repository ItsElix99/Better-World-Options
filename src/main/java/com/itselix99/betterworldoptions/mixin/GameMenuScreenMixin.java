package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.world.WorldSettings;
import com.itselix99.betterworldoptions.world.WorldTypeList;
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
            if (WorldTypeList.worldtypeList != null) {
                WorldTypeList.selectWorldType(WorldTypeList.worldtypeList.get(0));
            }
            WorldSettings.resetBooleans();
        }
    }
}