package com.itselix99.betterworldoptions.mixin.screen;


import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.option.Option;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Unique private OptionButtonWidget difficultyButton;

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    private boolean getDifficultyButton(List<Object> buttons, Object widget, Operation<Boolean> original) {
        if (widget instanceof OptionButtonWidget button) {
            if (button.getOption() == Option.DIFFICULTY) {
                this.difficultyButton = button;
            }
        }
        return original.call(buttons, widget);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void disableDifficultyButton(CallbackInfo ci) {
        TranslationStorage var1 = TranslationStorage.getInstance();
        if (this.minecraft.world != null && ((BWOProperties) this.minecraft.world.getProperties()).bwo_isHardcore()) {
            difficultyButton.active = false;
            difficultyButton.text = var1.get("options.difficulty") + ": " + var1.get("options.difficulty.hardcore");
        }
    }
}