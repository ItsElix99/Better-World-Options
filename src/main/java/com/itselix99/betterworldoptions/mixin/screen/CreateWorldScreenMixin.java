package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.gui.screen.IndevOptionsScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.itselix99.betterworldoptions.gui.screen.WorldTypeListScreen;
import com.itselix99.betterworldoptions.world.WorldSettings;
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
import java.util.Objects;

@Mixin(value = CreateWorldScreen.class, priority = 1100)
public abstract class CreateWorldScreenMixin extends Screen {
    @Shadow private final Screen parent;
    @Shadow private TextFieldWidget worldNameField;
    @Shadow private TextFieldWidget seedField;
    @Unique private static TranslationStorage translation = TranslationStorage.getInstance();

    @Unique private ButtonWidget gamemodeButton;
    @Unique private ButtonWidget moreWorldOptions;
    @Unique private ButtonWidget worldTypeButton;
    @Unique private ButtonWidget betaFeaturesButton;
    @Unique private ButtonWidget themeButton;
    @Unique private ButtonWidget indevOptionsButton;

    @Unique private static boolean moreOptions;
    @Unique private static String lastEnteredWorldName = translation.get("selectWorld.newWorld");
    @Unique private static String lastEnteredSeed = "";

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
        args.set(6, lastEnteredWorldName);
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
        args.set(6, lastEnteredSeed);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (CompatMods.BHCreativeLoaded()) {
            this.buttons.remove(1);
        }

        this.buttons.add(this.gamemodeButton = new ButtonWidget(10, this.width / 2 - 75, 100, 150, 20, translation.get("selectWorld.gameMode") + " " + WorldSettings.GameMode.getGameMode()));
        this.buttons.add(this.moreWorldOptions = new ButtonWidget(11, this.width / 2 - 75, 172, 150, 20, moreOptions ? translation.get("gui.done") : translation.get("selectWorld.moreWorldOptions")));
        this.buttons.add(this.worldTypeButton = new ButtonWidget(12, this.width / 2 - 155, 100, 150, 20, translation.get("selectWorld.worldtype") + " " + WorldSettings.World.getWorldTypeName()));
        this.buttons.add(this.betaFeaturesButton = new ButtonWidget(13, this.width / 2 + 5, 100, 150, 20, translation.get("selectWorld.betaFeatures") + " " + (WorldSettings.GameMode.isBetaFeatures() ? translation.get("options.on") : translation.get("options.off"))));
        this.buttons.add(this.themeButton = new ButtonWidget(14, this.width / 2 - 75, 150, 150, 20, translation.get("selectWorld.theme") + " " + WorldSettings.World.getTheme()));
        this.buttons.add(this.indevOptionsButton = new ButtonWidget(15, this.width / 2 + 5, 100, 150, 20, translation.get("selectWorld.indevOptions")));

        if (WorldSettings.GameMode.getNonBetaFeaturesWorldTypes()) {
            this.betaFeaturesButton.active = false;

            if (!WorldSettings.GameMode.isBetaFeatures()) {
                WorldSettings.GameMode.setBetaFeatures(true);
            }
        } else {
            this.betaFeaturesButton.active = true;
        }

        if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Indev 223")) {
            this.betaFeaturesButton.visible = false;
            this.themeButton.visible = false;
        }

        this.seedField.setFocused(moreOptions);
        this.worldNameField.setFocused(!moreOptions);
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
                    if (minecraft.player != null && WorldSettings.GameMode.getGameMode().equals("Creative")) {
                        minecraft.player.creative_setCreative(true);
                    }
                }

                moreOptions = false;
                lastEnteredWorldName = translation.get("selectWorld.newWorld");
                lastEnteredSeed = "";
            } else if (button.id == 1) {
                moreOptions = false;
                lastEnteredWorldName = translation.get("selectWorld.newWorld");
                lastEnteredSeed = "";
                WorldSettings.resetSettings();
            } else if (button.id == 10) {
                if (CompatMods.BHCreativeLoaded()) {
                    switch (this.gamemodeButton.text) {
                        case "Game Mode: Survival" -> WorldSettings.GameMode.setGameMode("Hardcore");
                        case "Game Mode: Hardcore" -> WorldSettings.GameMode.setGameMode("Creative");
                        case "Game Mode: Creative" -> WorldSettings.GameMode.setGameMode("Survival");
                    }
                } else {
                    switch (this.gamemodeButton.text) {
                        case "Game Mode: Survival" -> WorldSettings.GameMode.setGameMode("Hardcore");
                        case "Game Mode: Hardcore" -> WorldSettings.GameMode.setGameMode("Survival");
                    }
                }

                this.gamemodeButton.text = translation.get("selectWorld.gameMode") + " " + WorldSettings.GameMode.getGameMode();
            } else if (button.id == 11) {
                moreOptions = !moreOptions;
                this.moreWorldOptions.text = moreOptions ? translation.get("gui.done") : translation.get("selectWorld.moreWorldOptions");

                this.seedField.setFocused(moreOptions);
                this.worldNameField.setFocused(!moreOptions);
            } else if (button.id == 12) {
                lastEnteredWorldName = this.worldNameField.getText();
                lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new WorldTypeListScreen(this));
            } else if (button.id == 13) {
                WorldSettings.GameMode.setBetaFeatures(!WorldSettings.GameMode.isBetaFeatures());

                this.betaFeaturesButton.text = translation.get("selectWorld.betaFeatures") + " " + (WorldSettings.GameMode.isBetaFeatures() ? translation.get("options.on") : translation.get("options.off"));
            } else if (button.id == 14) {
                switch (this.themeButton.text) {
                    case "Theme: Normal" -> WorldSettings.World.setTheme("Hell");
                    case "Theme: Hell" -> WorldSettings.World.setTheme("Paradise");
                    case "Theme: Paradise" -> WorldSettings.World.setTheme("Woods");
                    case "Theme: Woods" -> WorldSettings.World.setTheme("Winter");
                    case "Theme: Winter" -> WorldSettings.World.setTheme("Normal");
                }

                this.themeButton.text = translation.get("selectWorld.theme") + " " + WorldSettings.World.getTheme();
            } else if (button.id == 15) {
                lastEnteredWorldName = this.worldNameField.getText();
                lastEnteredSeed = this.seedField.getText();

                this.minecraft.setScreen(new IndevOptionsScreen(this));
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
        TextFieldWidget activeField = moreOptions ? this.seedField : this.worldNameField;
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
        if(!moreOptions) {
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
        if(!moreOptions) {
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
        if(moreOptions) {
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
        if(moreOptions) {
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
        if(!moreOptions) {
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
        if(moreOptions) {
            original.call(instance);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TranslationStorage var4 = TranslationStorage.getInstance();
        if(!moreOptions) {
            this.worldTypeButton.visible = false;
            this.betaFeaturesButton.visible = false;
            this.indevOptionsButton.visible = false;
            this.themeButton.visible = false;
            this.gamemodeButton.visible = true;

            if (CompatMods.BHCreativeLoaded()) {
                switch (this.gamemodeButton.text) {
                    case "Game Mode: Survival" -> this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.survival.info"), this.width / 2, 122, 10526880);
                    case "Game Mode: Hardcore" -> {
                        this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line1"), this.width / 2 - 100, 122, 10526880);
                        this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line2"), this.width / 2 - 100, 134, 10526880);
                    }
                    case "Game Mode: Creative" -> {
                        this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.creative.info.line1"), this.width / 2 - 100, 122, 10526880);
                        this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.creative.info.line2"), this.width / 2 - 100, 134, 10526880);
                    }
                }
            } else {
                switch (this.gamemodeButton.text) {
                    case "Game Mode: Survival" -> this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.survival.info"), this.width / 2, 122, 10526880);
                    case "Game Mode: Hardcore" -> {
                        this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line1"), this.width / 2 - 100, 122, 10526880);
                        this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line2"), this.width / 2 - 100, 134, 10526880);
                    }
                }
            }
        } else {
            this.worldTypeButton.visible = true;
            this.gamemodeButton.visible = false;

            if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Indev 223")) {
                this.betaFeaturesButton.visible = false;
                this.themeButton.visible = false;
                this.indevOptionsButton.visible = true;
            } else {
                this.betaFeaturesButton.visible = true;
                this.themeButton.visible = true;
                this.indevOptionsButton.visible = false;
            }

            if (WorldSettings.GameMode.isBetaFeatures() && !Objects.equals(WorldSettings.World.getWorldTypeName(), "Indev 223")) {
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.line1"), this.width / 2 + 5, 122, 10526880);
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.line2"), this.width / 2 + 5, 134, 10526880);
            } else if (!Objects.equals(WorldSettings.World.getWorldTypeName(), "Indev 223")) {
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.disabled.line1"), this.width / 2 + 5, 122, 10526880);
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.disabled.line2"), this.width / 2 + 5, 134, 10526880);
            }
        }
        super.render(mouseX, mouseY, delta);
    }
}