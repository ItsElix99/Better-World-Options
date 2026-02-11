package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.gui.screen.BWOMoreOptionsScreen;
import com.itselix99.betterworldoptions.gui.screen.BiomeListScreen;
import com.itselix99.betterworldoptions.gui.widget.BWOButtonWidget;
import com.itselix99.betterworldoptions.gui.widget.ButtonWidgetWithIcon;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.itselix99.betterworldoptions.gui.screen.WorldTypeListScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = CreateWorldScreen.class, priority = 1100)
public class CreateWorldScreenMixin extends Screen {
    @Shadow private TextFieldWidget worldNameField;
    @Shadow private TextFieldWidget seedField;
    @Unique private TranslationStorage translation = TranslationStorage.getInstance();

    @Unique private BWOWorldPropertiesStorage bwoWorldPropertiesStorage = new BWOWorldPropertiesStorage();

    @Unique private ButtonWidget gamemodeButton;
    @Unique private ButtonWidget generateStructuresButton;
    @Unique private ButtonWidget worldTypeButton;
    @Unique private ButtonWidget singleBiomeButton;
    @Unique private ButtonWidget themeButton;
    @Unique private ButtonWidget generalOptionsButton;

    @Unique private boolean moreOptions = false;
    @Unique private String lastEnteredWorldName = translation.get("selectWorld.newWorld");
    @Unique private String lastEnteredSeed = "";

    @Unique private List<String> gamemode = new ArrayList<>(Arrays.asList("Survival", "Hardcore"));
    @Unique int selectedGamemode = 0;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_addCreativeSupport(CallbackInfo ci) {
        if (CompatMods.BHCreativeLoaded()) {
            this.gamemode.add("Creative");
        }
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    private boolean bwo_wrapAddButton(List<Object> buttons, Object widget, Operation<Boolean> original) {
        if (widget instanceof ButtonWidget button) {
            if (button.id == 0) {
                ButtonWidget newCreateWorldButton = new ButtonWidget(button.id, this.width / 2 - 155, this.height - 28, 150, 20, button.text);
                return original.call(buttons, newCreateWorldButton);
            } else if (button.id == 1) {
                ButtonWidget newCancelButton = new ButtonWidget(button.id, this.width / 2 + 5, this.height - 28, 150, 20, button.text);
                return original.call(buttons, newCancelButton);
            }
        }

        return original.call(buttons, widget);
    }

    @ModifyArgs(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;<init>(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/font/TextRenderer;IIIILjava/lang/String;)V",
                    ordinal = 0
            )
    )
    private void bwo_modifyWorldNameFieldText(Args args) {
        args.set(6, this.lastEnteredWorldName);
    }

    @ModifyArgs(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;<init>(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/font/TextRenderer;IIIILjava/lang/String;)V",
                    ordinal = 1
            )
    )
    private void bwo_modifyPosYAndSeedFieldText(Args args) {
        args.set(3, 60);
        args.set(6, this.lastEnteredSeed);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    private void bwo_initButtons(CallbackInfo ci) {
        List<OptionEntry> generalOptions = GeneralOptions.getList();

        WorldTypeEntry worldTypeEntry = WorldTypes.getWorldTypeByName(this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));
        for (OptionEntry generalOption : generalOptions) {
            if (generalOption.compatibleWorldTypes.contains("Overworld")) {
                if (worldTypeEntry.isDimension) {
                    this.bwoWorldPropertiesStorage.resetGeneralOptionToDefaultValue(generalOption);
                }
            } else if (!generalOption.compatibleWorldTypes.contains("All") && !generalOption.compatibleWorldTypes.contains(worldTypeEntry.name)) {
                this.bwoWorldPropertiesStorage.resetGeneralOptionToDefaultValue(generalOption);
            }
        }

        this.buttons.add(this.gamemodeButton = new ButtonWidget(10, this.width / 2 - 75, 100, 150, 20, this.translation.get("selectWorld.gameMode") + " " + this.gamemode.get(this.selectedGamemode)));
        this.buttons.add(new ButtonWidget(11, this.width / 2 - 75, 172, 150, 20, this.moreOptions ? this.translation.get("gui.done") : this.translation.get("selectWorld.moreWorldOptions")));
        this.buttons.add(this.generateStructuresButton = new ButtonWidget(12, this.width / 2 - 155, 100, 150, 20, this.translation.get("selectWorld.mapFeatures") + " " + this.translation.get("options.off")));
        this.buttons.add(this.worldTypeButton = new ButtonWidget(13, this.width / 2 + 5, 100, 150, 20, this.translation.get("selectWorld.worldtype") + " " + this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION)));
        this.buttons.add(this.singleBiomeButton = new ButtonWidget(14, this.width / 2 - 155, 150, 150, 20, this.translation.get("selectWorld.singleBiome") + " " + (!this.bwoWorldPropertiesStorage.getStringOptionValue("SingleBiome", OptionType.GENERAL_OPTION).equals("Off") ? this.bwoWorldPropertiesStorage.getStringOptionValue("SingleBiome", OptionType.GENERAL_OPTION) : this.translation.get("options.off"))));
        this.buttons.add(this.themeButton = new BWOButtonWidget(15, this.width / 2 + 5, 150, 150, 20, this.translation.get(generalOptions.get(3).displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue("Theme", OptionType.GENERAL_OPTION), generalOptions.get(3), this.bwoWorldPropertiesStorage, this));
        this.buttons.add(this.generalOptionsButton = new ButtonWidgetWithIcon(16, this.width / 2 + 160, 100, "/assets/betterworldoptions/stationapi/textures/gui/settings_icon.png"));
        this.generateStructuresButton.active = false;

        if (this.moreOptions) {
            this.gamemodeButton.visible = false;
            this.generateStructuresButton.visible = true;
            this.worldTypeButton.visible = true;
            this.singleBiomeButton.visible = true;
            this.themeButton.visible = true;
            this.generalOptionsButton.visible = true;
        } else {
            this.gamemodeButton.visible = true;
            this.generateStructuresButton.visible = false;
            this.worldTypeButton.visible = false;
            this.singleBiomeButton.visible = false;
            this.themeButton.visible = false;
            this.generalOptionsButton.visible = false;
        }

        String worldType = this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);
        OptionEntry singleBiomeOption = generalOptions.get(2);

        if (oldFeaturesProperties != null && !oldFeaturesProperties.oldFeaturesHasVanillaBiomes && this.bwoWorldPropertiesStorage.getBooleanOptionValue("OldFeatures", OptionType.GENERAL_OPTION) || singleBiomeOption.compatibleWorldTypes.contains("Overworld") && worldTypeEntry.isDimension) {
            this.bwoWorldPropertiesStorage.setStringOptionValue("SingleBiome", OptionType.GENERAL_OPTION, "Off");
            this.singleBiomeButton.active = false;
            String singleBiome = this.bwoWorldPropertiesStorage.getStringOptionValue("SingleBiome", OptionType.GENERAL_OPTION);
            this.singleBiomeButton.text = this.translation.get("selectWorld.singleBiome") + " " + (!singleBiome.equals("Off") ? singleBiome : this.translation.get("options.off"));
        }
    }

    @Inject(
            method = "buttonClicked",
            at = @At
                    (
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V",
                            ordinal = 1
                    )
    )
    private void bwo_setCurrentBWOWorldProperties(ButtonWidget button, CallbackInfo ci) {
        BWOWorldPropertiesStorage.setInstance(this.bwoWorldPropertiesStorage);
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    private void bwo_buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.active && button.visible) {
            if (button instanceof BWOButtonWidget) {
                ((BWOButtonWidget) button).onButtonClicked();
            }

            if (button.id == 0) {
                if (CompatMods.BHCreativeLoaded()) {
                    if (minecraft.player != null) {
                        minecraft.player.creative_setCreative(this.gamemode.get(this.selectedGamemode).equals("Creative"));
                    }
                }
            } else if (button.id == 10) {
                this.selectedGamemode = (this.selectedGamemode + 1) % this.gamemode.size();
                this.bwoWorldPropertiesStorage.setBooleanOptionValue("Hardcore", OptionType.GENERAL_OPTION, this.gamemode.get(this.selectedGamemode).equals("Hardcore"));
                button.text = this.translation.get("selectWorld.gameMode") + " " + this.gamemode.get(this.selectedGamemode);
            } else if (button.id == 11) {
                this.moreOptions = !this.moreOptions;

                if (this.moreOptions) {
                    this.gamemodeButton.visible = false;
                    this.generateStructuresButton.visible = true;
                    this.worldTypeButton.visible = true;
                    this.singleBiomeButton.visible = true;
                    this.themeButton.visible = true;
                    this.generalOptionsButton.visible = true;
                } else {
                    this.gamemodeButton.visible = true;
                    this.generateStructuresButton.visible = false;
                    this.worldTypeButton.visible = false;
                    this.singleBiomeButton.visible = false;
                    this.themeButton.visible = false;
                    this.generalOptionsButton.visible = false;
                }

                button.text = this.moreOptions ? this.translation.get("gui.done") : this.translation.get("selectWorld.moreWorldOptions");
            } else if (button.id == 13) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new WorldTypeListScreen(this, this.bwoWorldPropertiesStorage));
            } else if (button.id == 14) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new BiomeListScreen(this, this.bwoWorldPropertiesStorage));
            } else if (button.id == 16) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new BWOMoreOptionsScreen(this, this.bwoWorldPropertiesStorage));
            }
        }
    }

    @Redirect(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;keyPressed(CI)V"
            )
    )
    private void bwo_redirectKeyPressed(TextFieldWidget instance, char character, int keyCode) {
        if (this.worldNameField.focused && !this.moreOptions) {
            this.worldNameField.keyPressed(character, keyCode);
        } else if (this.seedField.focused && this.moreOptions) {
            this.seedField.keyPressed(character, keyCode);
        }
    }

    @Redirect(
            method = "mouseClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;mouseClicked(III)V"
            )
    )
    private void bwo_redirectMouseClicked(TextFieldWidget instance, int mouseX, int mouseY, int button) {
        if (!this.moreOptions) {
            this.worldNameField.mouseClicked(mouseX, mouseY, button);
        } else {
            this.seedField.mouseClicked(mouseX, mouseY, button);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"
            )
    )
    private void bwo_modifyPosYCreateWorldText(CreateWorldScreen instance, TextRenderer textRenderer, String text, int centerX, int y, int color, Operation<Void> original) {
        original.call(instance, textRenderer, text, centerX, 20, color);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 0
            )
    )
    private void bwo_renderEnterNameTextWhenNotMoreOptions(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
        if(!this.moreOptions) {
            original.call(instance, textRenderer, text, x, y, color);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 1
            )
    )
    private void bwo_renderResultFolderTextWhenNotMoreOptions(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
        if(!this.moreOptions) {
            original.call(instance, textRenderer, text, x, y, color);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2
            )
    )
    private void bwo_renderEnterSeedTextWhenMoreOptions(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
        if(this.moreOptions) {
            original.call(instance, textRenderer, text, x, 47, color);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 3
            )
    )
    private void bwo_renderSeedInfoTextWhenMoreOptions(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
        if(this.moreOptions) {
            original.call(instance, textRenderer, text, x, 85, color);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;render()V",
                    ordinal = 0
            )
    )
    private void bwo_renderWorldNameFieldWhenNotMoreOptions(TextFieldWidget instance, Operation<Void> original) {
        if(!this.moreOptions) {
            original.call(instance);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;render()V",
                    ordinal = 1
            )
    )
    private void bwo_renderSeedFieldWhenMoreOptions(TextFieldWidget instance, Operation<Void> original) {
        if(this.moreOptions) {
            original.call(instance);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void bwo_renderText(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(this.moreOptions) {
            if (this.generateStructuresButton.visible) {
                this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, 10526880);
            }
        } else {
            if (CompatMods.BHCreativeLoaded()) {
                if (this.gamemodeButton.text.equals(this.translation.get("selectWorld.gameMode") + " " + "Survival")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.survival.line1"), this.width / 2, 122, 10526880);
                } else if (this.gamemodeButton.text.equals(this.translation.get("selectWorld.gameMode") + " " + "Hardcore")) {
                    this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.line1"), this.width / 2 - 100, 122, 10526880);
                    this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.line2"), this.width / 2 - 100, 134, 10526880);
                } else if (this.gamemodeButton.text.equals(this.translation.get("selectWorld.gameMode") + " " + "Creative")) {
                    this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.creative.line1"), this.width / 2 - 100, 122, 10526880);
                    this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.creative.line2"), this.width / 2 - 100, 134, 10526880);
                }
            } else {
                if (this.gamemodeButton.text.equals(this.translation.get("selectWorld.gameMode") + " " + "Survival")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.survival.line1"), this.width / 2, 122, 10526880);
                } else if (this.gamemodeButton.text.equals(this.translation.get("selectWorld.gameMode") + " " + "Hardcore")) {
                    this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.line1"), this.width / 2 - 100, 122, 10526880);
                    this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.line2"), this.width / 2 - 100, 134, 10526880);
                }
            }
        }
    }
}