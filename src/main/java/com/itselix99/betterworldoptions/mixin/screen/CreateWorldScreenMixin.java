package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.gui.IndevOptionsScreen;
import com.itselix99.betterworldoptions.gui.UpdateButtonText;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.itselix99.betterworldoptions.gui.CreateWorldTypeScreen;
import com.itselix99.betterworldoptions.gui.ScreenStateCache;
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
    @Unique private boolean moreOptions;
    @Unique private ButtonWidget gamemodeButton;
    @Unique private ButtonWidget moreWorldOptions;
    @Unique private ButtonWidget worldTypeButton;
    @Unique private ButtonWidget betaFeaturesButton;
    @Unique private ButtonWidget winterModeButton;
    @Unique private ButtonWidget indevOptionsButton;
    @Unique private boolean creative = false;

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
        args.set(6,getWorldName());
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
        args.set(6,getSeed());
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        TranslationStorage var1 = TranslationStorage.getInstance();
        if (CompatMods.BHCreativeLoaded()) {
            this.buttons.remove(1);
        }

        this.buttons.add(this.gamemodeButton = new ButtonWidget(10, this.width / 2 - 75, 100, 150, 20, var1.get("selectWorld.gameMode") + " " + var1.get("title.bhcreative.selectWorld.survival")));
        this.buttons.add(this.moreWorldOptions = new ButtonWidget(11, this.width / 2 - 75, 172, 150, 20, UpdateButtonText.updateMoreOptionsButtonText(this.moreOptions)));
        this.buttons.add(this.worldTypeButton = new ButtonWidget(12, this.width / 2 - 155, 100, 150, 20, var1.get("selectWorld.worldtype") + " " + WorldSettings.World.getWorldTypeName()));
        this.buttons.add(this.betaFeaturesButton = new ButtonWidget(13, this.width / 2 + 5, 100, 150, 20, var1.get("selectWorld.betaFeatures") + " " + var1.get("options.on")));
        this.buttons.add(this.winterModeButton = new ButtonWidget(14, this.width / 2 - 75, 150, 150, 20, var1.get("selectWorld.winterMode") + " " + var1.get("options.off")));
        this.buttons.add(this.indevOptionsButton = new ButtonWidget(15, this.width / 2 + 5, 100, 150, 20, var1.get("selectWorld.indevOptions")));

        if (!Objects.equals(WorldSettings.World.getWorldTypeName(), "Alpha 1.1.2_01")) {
            this.winterModeButton.visible = false;
        }

        if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Indev 223")) {
            this.betaFeaturesButton.visible = false;
        }

        UpdateButtonText.updateGamemodeButtonText(this.gamemodeButton, this.creative);
        UpdateButtonText.updateWorldTypeButtonText(this.worldTypeButton);
        UpdateButtonText.updateBetaFeaturesButtonText(this.betaFeaturesButton, this.winterModeButton);
        UpdateButtonText.updateWinterModeButtonText(this.winterModeButton);

        this.moreOptions = ScreenStateCache.wasInMoreOptions;
        this.seedField.setFocused(this.moreOptions);
        this.worldNameField.setFocused(!this.moreOptions);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void fixCancelButtonY(CallbackInfo ci) {
        if (CompatMods.BHCreativeLoaded()) {
            ((ButtonWidget) this.buttons.get(1)).y = this.height - 28;
        }
    }

    @Unique
    private String getWorldName() {
        if (ScreenStateCache.lastEnteredWorldName != null) {
            return ScreenStateCache.lastEnteredWorldName;
        } else {
            TranslationStorage translation = TranslationStorage.getInstance();
            return translation.get("selectWorld.newWorld");
        }
    }

    @Unique
    private String getSeed() {
        return Objects.requireNonNullElse(ScreenStateCache.lastEnteredSeed, "");
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                if (CompatMods.BHCreativeLoaded()) {
                    if (minecraft.player != null) {
                        minecraft.player.creative_setCreative(this.creative);
                    }
                }

                ScreenStateCache.lastEnteredSeed = null;
                ScreenStateCache.lastEnteredWorldName = null;
                ScreenStateCache.wasInMoreOptions = false;
                ScreenStateCache.lastWorldType = 0;
            } else if (button.id == 1) {
                ScreenStateCache.lastEnteredSeed = null;
                ScreenStateCache.lastEnteredWorldName = null;
                ScreenStateCache.wasInMoreOptions = false;
                ScreenStateCache.lastWorldType = 0;
                WorldSettings.resetSettings();
            } else if (button.id == 10) {
                if (CompatMods.BHCreativeLoaded()) {
                    if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                        WorldSettings.GameMode.setHardcore(true);
                        UpdateButtonText.updateGamemodeButtonText(this.gamemodeButton, this.creative);
                    } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                        WorldSettings.GameMode.setHardcore(false);
                        this.creative = true;
                        UpdateButtonText.updateGamemodeButtonText(this.gamemodeButton, this.creative);
                    } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Creative")) {
                        this.creative = false;
                        UpdateButtonText.updateGamemodeButtonText(this.gamemodeButton, this.creative);
                    }
                } else {
                    if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                        WorldSettings.GameMode.setHardcore(true);
                        UpdateButtonText.updateGamemodeButtonText(this.gamemodeButton, this.creative);
                    } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                        WorldSettings.GameMode.setHardcore(false);
                        UpdateButtonText.updateGamemodeButtonText(this.gamemodeButton, this.creative);
                    }
                }
            } else if (button.id == 11) {
                TranslationStorage translation = TranslationStorage.getInstance();
                this.moreOptions = !this.moreOptions;
                this.moreWorldOptions.text = this.moreOptions ? translation.get("gui.done") : translation.get("selectWorld.moreWorldOptions");
                this.seedField.setFocused(this.moreOptions);
                this.worldNameField.setFocused(!this.moreOptions);
            } else if (button.id == 12) {
                ScreenStateCache.lastEnteredSeed = this.seedField.getText();
                ScreenStateCache.lastEnteredWorldName = this.worldNameField.getText();
                ScreenStateCache.wasInMoreOptions = this.moreOptions;

                this.minecraft.setScreen(new CreateWorldTypeScreen(this));
            } else if (button.id == 13) {
                if (WorldSettings.GameMode.isBetaFeatures()) {
                    WorldSettings.GameMode.setBetaFeatures(false);
                    UpdateButtonText.updateBetaFeaturesButtonText(this.betaFeaturesButton, this.winterModeButton);
                } else {
                    WorldSettings.GameMode.setBetaFeatures(true);
                    UpdateButtonText.updateBetaFeaturesButtonText(this.betaFeaturesButton, this.winterModeButton);
                }
            } else if (button.id == 14) {
                if (WorldSettings.AlphaWorld.isSnowCovered()) {
                    WorldSettings.AlphaWorld.setSnowCovered(false);
                    UpdateButtonText.updateBetaFeaturesButtonText(this.betaFeaturesButton, this.winterModeButton);
                } else {
                    WorldSettings.AlphaWorld.setSnowCovered(true);
                    UpdateButtonText.updateBetaFeaturesButtonText(this.betaFeaturesButton, this.winterModeButton);
                }
            } else if (button.id == 15) {
                ScreenStateCache.lastEnteredSeed = this.seedField.getText();
                ScreenStateCache.lastEnteredWorldName = this.worldNameField.getText();
                ScreenStateCache.wasInMoreOptions = this.moreOptions;

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
        TranslationStorage var4 = TranslationStorage.getInstance();
        if(!this.moreOptions) {
            this.worldTypeButton.visible = false;
            this.betaFeaturesButton.visible = false;
            this.indevOptionsButton.visible = false;
            this.winterModeButton.visible = false;
            this.gamemodeButton.visible = true;

            if (CompatMods.BHCreativeLoaded()) {
                if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.survival.info"), this.width / 2, 122, 10526880);
                } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                    this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line1"), this.width / 2 - 100, 122, 10526880);
                    this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line2"), this.width / 2 - 100, 134, 10526880);
                } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Creative")) {
                    this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.creative.info.line1"), this.width / 2 - 100, 122, 10526880);
                    this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.creative.info.line2"), this.width / 2 - 100, 134, 10526880);
                }
            } else {
                if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.survival.info"), this.width / 2, 122, 10526880);
                } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                    this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line1"), this.width / 2 - 100, 122, 10526880);
                    this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.gameMode.hardcore.info.line2"), this.width / 2 - 100, 134, 10526880);
                }
            }
        } else {
            this.worldTypeButton.visible = true;
            this.gamemodeButton.visible = false;

            if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Alpha 1.1.2_01")) {
                this.winterModeButton.visible = true;
                this.winterModeButton.active = !WorldSettings.GameMode.isBetaFeatures();
            }

            if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Indev 223")) {
                this.betaFeaturesButton.visible = false;
                this.indevOptionsButton.visible = true;
            } else {
                this.betaFeaturesButton.visible = true;
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