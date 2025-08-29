package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.gui.screen.IndevOptionsScreen;
import com.itselix99.betterworldoptions.gui.screen.McpeOptionsScreen;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(value = CreateWorldScreen.class, priority = 1100)
public abstract class CreateWorldScreenMixin extends Screen {
    @Shadow private final Screen parent;
    @Shadow private TextFieldWidget worldNameField;
    @Shadow private TextFieldWidget seedField;
    @Unique private TranslationStorage translation = TranslationStorage.getInstance();

    @Unique private WorldGenerationOptions worldGenerationOptions = new WorldGenerationOptions();

    @Unique private ButtonWidget gamemodeButton;
    @Unique private ButtonWidget moreWorldOptionsButton;
    @Unique private ButtonWidget worldTypeButton;
    @Unique private ButtonWidget oldFeaturesButton;
    @Unique private ButtonWidget themeButton;
    @Unique private ButtonWidget worldTypeOptionsButton;
    @Unique private ButtonWidget superflatButton;

    @Unique private String gamemode = "Survival";
    @Unique private boolean moreOptions = false;
    @Unique private String lastEnteredWorldName = translation.get("selectWorld.newWorld");
    @Unique private String lastEnteredSeed = "";

    public CreateWorldScreenMixin(Screen parent) {
        this.parent = parent;
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    private boolean wrapAddButton(List<Object> buttons, Object widget, Operation<Boolean> original) {
        if (widget instanceof ButtonWidget button) {
            if (button.id == 0) {
                ButtonWidget customButton = new ButtonWidget(button.id, this.width / 2 - 155, this.height - 28, 150, 20, button.text);
                return original.call(buttons, customButton);
            } else if (button.id == 1) {
                ButtonWidget customButton = new ButtonWidget(button.id, this.width / 2 + 5, this.height - 28, 150, 20, button.text);
                return original.call(buttons, customButton);
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
    private void modifyWorldNameField(Args args) {
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
    private void modifySeedField(Args args) {
        args.set(3, 60);
        args.set(6, this.lastEnteredSeed);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (CompatMods.BHCreativeLoaded()) {
            this.buttons.remove(1);
        }

        this.buttons.add(this.gamemodeButton = new ButtonWidget(10, this.width / 2 - 75, 100, 150, 20, this.translation.get("selectWorld.gameMode") + " " + this.gamemode));
        this.buttons.add(this.moreWorldOptionsButton = new ButtonWidget(11, this.width / 2 - 75, 172, 150, 20, this.moreOptions ? this.translation.get("gui.done") : this.translation.get("selectWorld.moreWorldOptions")));
        this.buttons.add(this.worldTypeButton = new ButtonWidget(12, this.width / 2 - 155, 100, 150, 20, this.translation.get("selectWorld.worldtype") + " " + this.worldGenerationOptions.worldTypeName));
        this.buttons.add(this.oldFeaturesButton = new ButtonWidget(13, this.width / 2 + 5, 100, 150, 20, this.translation.get("selectWorld.oldFeatures") + " " + (this.worldGenerationOptions.oldFeatures ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(this.themeButton = new ButtonWidget(14, this.width / 2 - 75, 150, 150, 20, this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme));
        this.buttons.add(this.worldTypeOptionsButton = new ButtonWidget(15, this.width / 2 + 5, 100, 150, 20, this.getWorldTypeOptionsButtonName()));
        this.buttons.add(this.superflatButton = new ButtonWidget(16, this.width / 2 + 5, 100, 150, 20, this.translation.get("selectWorld.superflat") + " " + (this.worldGenerationOptions.superflat ? this.translation.get("options.on") : this.translation.get("options.off"))));

        if (this.worldGenerationOptions.worldTypeName.equals("MCPE")) {
            this.worldGenerationOptions.resetIndevOptions();
        } else if (!this.worldGenerationOptions.worldTypeName.equals("Indev 223")) {
            this.worldGenerationOptions.resetFiniteOptions();
        }

        if (WorldGenerationOptions.disableThemeWorldTypes.contains(this.worldGenerationOptions.worldTypeName)) {
            this.themeButton.active = false;

            if (!this.worldGenerationOptions.theme.equals("Normal")) {
                this.worldGenerationOptions.theme = "Normal";
            }

            this.themeButton.text = this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme;
        } else {
            this.themeButton.active = true;
        }

        if (!WorldGenerationOptions.allowOldFeaturesWorldTypes.contains(this.worldGenerationOptions.worldTypeName)) {
            this.oldFeaturesButton.active = false;

            if (this.worldGenerationOptions.worldTypeName.equals("Alpha 1.2.0")) {
                this.worldGenerationOptions.oldFeatures = true;
            } else {
                if (this.worldGenerationOptions.oldFeatures) {
                    this.worldGenerationOptions.oldFeatures = false;
                }
            }

            this.oldFeaturesButton.text = this.translation.get("selectWorld.oldFeatures") + " " + (this.worldGenerationOptions.oldFeatures ? this.translation.get("options.on") : this.translation.get("options.off"));
        } else {
            this.oldFeaturesButton.active = true;
        }

        if (this.worldGenerationOptions.worldTypeName.equals("Indev 223") || this.worldGenerationOptions.worldTypeName.equals("MCPE")) {
            this.oldFeaturesButton.visible = false;
            this.themeButton.visible = false;
        } else if (this.worldGenerationOptions.worldTypeName.equals("Flat")) {
            this.oldFeaturesButton.visible = false;
        }

        this.seedField.setFocused(this.moreOptions);
        this.worldNameField.setFocused(!this.moreOptions);
    }

    @Unique
    private String getWorldTypeOptionsButtonName() {
        if (this.worldGenerationOptions.worldTypeName.equals("Indev 223")) {
            return translation.get("selectWorld.indevOptions");
        } else if (this.worldGenerationOptions.worldTypeName.equals("MCPE")) {
            return translation.get("selectWorld.mcpeOptions");
        } else {
            return "";
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void fixCancelButtonY(CallbackInfo ci) {
        if (CompatMods.BHCreativeLoaded()) {
            ((ButtonWidget) this.buttons.get(1)).y = this.height - 28;
        }
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
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
                this.moreWorldOptionsButton.text = this.moreOptions ? this.translation.get("gui.done") : this.translation.get("selectWorld.moreWorldOptions");

                this.seedField.setFocused(this.moreOptions);
                this.worldNameField.setFocused(!this.moreOptions);
            } else if (button.id == 12) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new WorldTypeListScreen(this, this.worldGenerationOptions));
            } else if (button.id == 13) {
                this.worldGenerationOptions.oldFeatures = !this.worldGenerationOptions.oldFeatures;
                this.worldGenerationOptions.oldTextures = this.worldGenerationOptions.oldFeatures;

                this.oldFeaturesButton.text = this.translation.get("selectWorld.oldFeatures") + " " + (this.worldGenerationOptions.oldFeatures ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 14) {
                switch (this.themeButton.text) {
                    case "Theme: Normal" -> this.worldGenerationOptions.theme = "Hell";
                    case "Theme: Hell" -> this.worldGenerationOptions.theme = "Paradise";
                    case "Theme: Paradise" -> this.worldGenerationOptions.theme = "Woods";
                    case "Theme: Woods" -> this.worldGenerationOptions.theme = "Winter";
                    case "Theme: Winter" -> this.worldGenerationOptions.theme = "Normal";
                }

                this.themeButton.text = this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme;
            } else if (button.id == 15) {
                this.lastEnteredWorldName = this.worldNameField.getText();
                this.lastEnteredSeed = this.seedField.getText();

                if (this.worldGenerationOptions.worldTypeName.equals("Indev 223")) {
                    this.minecraft.setScreen(new IndevOptionsScreen(this, this.worldGenerationOptions));
                } else if (this.worldGenerationOptions.worldTypeName.equals("MCPE")) {
                    this.minecraft.setScreen(new McpeOptionsScreen(this, this.worldGenerationOptions));
                }
            } else if (button.id == 16) {
                this.worldGenerationOptions.superflat = !this.worldGenerationOptions.superflat;

                this.superflatButton.text = this.translation.get("selectWorld.superflat") + " " + (this.worldGenerationOptions.superflat ? this.translation.get("options.on") : this.translation.get("options.off"));
            }
        }
    }

    @WrapOperation(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;keyPressed(CI)V"
            )
    )
    private void keyPressed(TextFieldWidget instance, char character, int keyCode, Operation<Void> original) {
        TextFieldWidget activeField = this.moreOptions ? this.seedField : this.worldNameField;
        original.call(activeField, character, keyCode);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"
            )
    )
    private void createWorldText(CreateWorldScreen instance, TextRenderer textRenderer, String text, int centerX, int y, int color, Operation<Void> original) {
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
    private void enterNameText(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
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
    private void resultFolderText(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
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
    private void enterSeedText(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
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
    private void seedInfoText(CreateWorldScreen instance, TextRenderer textRenderer, String text, int x, int y, int color, Operation<Void> original) {
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
    private void worldNameFieldRender(TextFieldWidget instance, Operation<Void> original) {
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
    private void seedFieldRender(TextFieldWidget instance, Operation<Void> original) {
        if(this.moreOptions) {
            original.call(instance);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(!this.moreOptions) {
            this.worldTypeButton.visible = false;
            this.oldFeaturesButton.visible = false;
            this.worldTypeOptionsButton.visible = false;
            this.themeButton.visible = false;
            this.superflatButton.visible = false;
            this.gamemodeButton.visible = true;

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
        } else {
            this.worldTypeButton.visible = true;
            this.gamemodeButton.visible = false;

            if (this.worldGenerationOptions.worldTypeName.equals("Indev 223") || this.worldGenerationOptions.worldTypeName.equals("MCPE")) {
                this.oldFeaturesButton.visible = false;
                this.themeButton.visible = false;
                this.worldTypeOptionsButton.visible = true;
                this.superflatButton.visible = false;
            } else if (this.worldGenerationOptions.worldTypeName.equals("Flat")) {
                this.oldFeaturesButton.visible = false;
            } else {
                this.oldFeaturesButton.visible = true;
                this.themeButton.visible = true;
                this.worldTypeOptionsButton.visible = false;
                this.superflatButton.visible = false;
            }

            if (this.oldFeaturesButton.visible) {
                this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.oldFeatures.info.line1"), this.width / 2 + 5, 122, 10526880);
                this.drawTextWithShadow(this.textRenderer, this.translation.get("selectWorld.oldFeatures.info.line2"), this.width / 2 + 5, 134, 10526880);
            }
        }
        super.render(mouseX, mouseY, delta);
    }
}