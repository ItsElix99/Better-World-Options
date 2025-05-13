package com.itselix99.betterworldoptions.mixin;


import com.itselix99.betterworldoptions.interfaces.WorldProperties;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Shadow protected String title = "Options";
    @Shadow private GameOptions options;
    @Shadow private static Option[] RENDER_OPTIONS;

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        TranslationStorage var1 = TranslationStorage.getInstance();
        this.title = var1.get("options.title");
        int var2 = 0;

        for(Option var6 : RENDER_OPTIONS) {
            if (!var6.isSlider()) {
                OptionButtonWidget var7 = new OptionButtonWidget(var6.getId(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), this.options.getString(var6));
                if (var6 == Option.DIFFICULTY && this.minecraft.world != null && ((WorldProperties) this.minecraft.world.getProperties()).bwo_getHardcore()) {
                    var7.active = false;
                    var7.text = var1.get("options.difficulty") + ": " + var1.get("options.difficulty.hardcore");
                }
                this.buttons.add(var7);
            } else {
                this.buttons.add(new SliderWidget(var6.getId(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), var6, this.options.getString(var6), this.options.getFloat(var6)));
            }

            ++var2;
        }

        this.buttons.add(new ButtonWidget(101, this.width / 2 - 100, this.height / 6 + 96 + 12, var1.get("options.video")));
        this.buttons.add(new ButtonWidget(100, this.width / 2 - 100, this.height / 6 + 120 + 12, var1.get("options.controls")));
        this.buttons.add(new ButtonWidget(200, this.width / 2 - 100, this.height / 6 + 168, var1.get("gui.done")));
    }
}