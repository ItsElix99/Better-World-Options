package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.gui.screen.BiomeListScreen;
import com.itselix99.betterworldoptions.gui.widget.ButtonWidgetWithIcon;
import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldType;
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

import java.util.List;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin extends Screen {
    @Shadow private TextFieldWidget worldNameField;
    @Shadow private TextFieldWidget seedField;
    @Unique private TranslationStorage translation = TranslationStorage.getInstance();

    @Unique private WorldGenerationOptions worldGenerationOptions = new WorldGenerationOptions();

    @Unique private ButtonWidget gamemodeButton;
    @Unique private ButtonWidget moreWorldOptionsButton;
    @Unique private ButtonWidget generateStructuresButton;
    @Unique private ButtonWidget worldTypeButton;
    @Unique private ButtonWidget singleBiomeButton;
    @Unique private ButtonWidget themeButton;
    @Unique private ButtonWidget generalOptionsButton;

    @Unique private String gamemode = "Survival";
    @Unique private boolean moreOptions = false;
    @Unique private String lastEnteredWorldName = translation.get("selectWorld.newWorld");
    @Unique private String lastEnteredSeed = "";

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
        this.buttons.add(this.gamemodeButton = new ButtonWidget(10, this.width / 2 - 75, 100, 150, 20, this.translation.get("selectWorld.gameMode") + " " + this.gamemode));
        this.buttons.add(this.moreWorldOptionsButton = new ButtonWidget(11, this.width / 2 - 75, 172, 150, 20, this.moreOptions ? this.translation.get("gui.done") : this.translation.get("selectWorld.moreWorldOptions")));
        this.buttons.add(this.generateStructuresButton = new ButtonWidget(12, this.width / 2 - 155, 100, 150, 20, this.translation.get("selectWorld.mapFeatures") + " " + this.translation.get("options.off")));
        this.buttons.add(this.worldTypeButton = new ButtonWidget(13, this.width / 2 + 5, 100, 150, 20, this.translation.get("selectWorld.worldtype") + " " + this.worldGenerationOptions.worldType));
        this.buttons.add(this.singleBiomeButton = new ButtonWidget(14, this.width / 2 - 155, 150, 150, 20, this.translation.get("selectWorld.singleBiome") + " " + (!this.worldGenerationOptions.singleBiome.equals("Off") ? this.worldGenerationOptions.singleBiome : this.translation.get("options.off"))));
        this.buttons.add(this.themeButton = new ButtonWidget(15, this.width / 2 + 5, 150, 150, 20, this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme));
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

        if (this.worldGenerationOptions.worldType.equals("MCPE")) {
            this.worldGenerationOptions.resetIndevOptions();
        } else if (!this.worldGenerationOptions.worldType.equals("Indev 223")) {
            this.worldGenerationOptions.resetFiniteOptions();
        }

        if (!WorldType.getWorldTypePropertyValue(this.worldGenerationOptions.worldType, "Old Features Has Biomes") && this.worldGenerationOptions.oldFeatures && !this.worldGenerationOptions.singleBiome.equals("Off")) {
            this.worldGenerationOptions.singleBiome = "Off";
        }

        if (!WorldType.getWorldTypePropertyValue(this.worldGenerationOptions.worldType, "Enable Themes")) {
            this.themeButton.active = false;

            if (!this.worldGenerationOptions.theme.equals("Normal")) {
                this.worldGenerationOptions.theme = "Normal";
            }

            this.themeButton.text = this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme;
        } else {
            this.themeButton.active = true;
        }
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    private void bwo_buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                if (CompatMods.BHCreativeLoaded()) {
                    if (minecraft.player != null && this.gamemode.equals("Creative")) {
                        minecraft.player.creative_setCreative(true);
                    }
                }
            } else if (button.id == 10) {
                if (CompatMods.BHCreativeLoaded()) {
                    switch (this.gamemodeButton.text) {
                        case "Game Mode: Survival" -> {
                            this.gamemode = "Hardcore";
                            this.worldGenerationOptions.hardcore = true;
                        }
                        case "Game Mode: Hardcore" -> {
                            this.gamemode = "Creative";
                            this.worldGenerationOptions.hardcore = false;
                        }
                        case "Game Mode: Creative" -> this.gamemode = "Survival";
                    }
                } else {
                    switch (this.gamemodeButton.text) {
                        case "Game Mode: Survival" -> {
                            this.gamemode = "Hardcore";
                            this.worldGenerationOptions.hardcore = true;
                        }
                        case "Game Mode: Hardcore" -> {
                            this.gamemode = "Survival";
                            this.worldGenerationOptions.hardcore = false;
                        }
                    }
                }

                this.gamemodeButton.text = this.translation.get("selectWorld.gameMode") + " " + this.gamemode;
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

                this.moreWorldOptionsButton.text = this.moreOptions ? this.translation.get("gui.done") : this.translation.get("selectWorld.moreWorldOptions");
            } else if (button.id == 13) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new WorldTypeListScreen(this, this.worldGenerationOptions));
            } else if (button.id == 14) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new BiomeListScreen(this, this.worldGenerationOptions));
            } else if (button.id == 15) {
                switch (this.themeButton.text) {
                    case "Theme: Normal" -> this.worldGenerationOptions.theme = "Hell";
                    case "Theme: Hell" -> this.worldGenerationOptions.theme = "Paradise";
                    case "Theme: Paradise" -> this.worldGenerationOptions.theme = "Woods";
                    case "Theme: Woods" -> this.worldGenerationOptions.theme = "Winter";
                    case "Theme: Winter" -> this.worldGenerationOptions.theme = "Normal";
                }

                this.themeButton.text = this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme;
            } else if (button.id == 16) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                //this.minecraft.setScreen();
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
    private void bwo_renderGamemodeAndOldFeaturesText(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(this.moreOptions) {
            if (this.generateStructuresButton.visible) {
                this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, 10526880);
            }
        } else {
            if (CompatMods.BHCreativeLoaded()) {
                switch (this.gamemodeButton.text) {
                    case "Game Mode: Survival" -> this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.survival.info"), this.width / 2, 122, 10526880);
                    case "Game Mode: Hardcore" -> {
                        this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.info.line1"), this.width / 2 - 100, 122, 10526880);
                        this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.info.line2"), this.width / 2 - 100, 134, 10526880);
                    }
                    case "Game Mode: Creative" -> {
                        this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.creative.info.line1"), this.width / 2 - 100, 122, 10526880);
                        this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.creative.info.line2"), this.width / 2 - 100, 134, 10526880);
                    }
                }
            } else {
                switch (this.gamemodeButton.text) {
                    case "Game Mode: Survival" -> this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.survival.info"), this.width / 2, 122, 10526880);
                    case "Game Mode: Hardcore" -> {
                        this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.info.line1"), this.width / 2 - 100, 122, 10526880);
                        this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.gameMode.hardcore.info.line2"), this.width / 2 - 100, 134, 10526880);
                    }
                }
            }
        }
    }
}