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
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 0
            )
    )
    private boolean bwo_disableDifficultyButtonInHardcore(List<Object> buttons, Object widget, Operation<Boolean> original) {
        if (widget instanceof OptionButtonWidget button) {
            if (button.getOption() == Option.DIFFICULTY && this.minecraft.world != null && ((BWOProperties) this.minecraft.world.getProperties()).bwo_isHardcore()) {
                TranslationStorage var1 = TranslationStorage.getInstance();
                button.active = false;
                button.text = var1.get("options.difficulty") + ": " + var1.get("options.difficulty.hardcore");
            }
        }

        return original.call(buttons, widget);
    }
}