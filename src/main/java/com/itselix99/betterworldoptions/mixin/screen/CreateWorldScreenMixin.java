package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.world.WorldTypeList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.loader.api.FabricLoader;
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
    @Shadow private String worldSaveName;
    @Unique boolean moreOptions;
    @Unique private ButtonWidget gamemodeButton;
    @Unique private ButtonWidget moreWorldOptions;
    @Unique private ButtonWidget worldTypeButton;
    @Unique private ButtonWidget betaFeaturesButton;
    @Unique private ButtonWidget winterModeButton;
    @Unique private boolean creative = false;
    @Unique private boolean isFlat = false;

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
        if (isBHCreativeModPresent()) {
            this.buttons.remove(1);
        }

        this.buttons.add(this.gamemodeButton = new ButtonWidget(3, this.width / 2 - 75, 100, 150, 20, var1.get("selectWorld.gameMode")+ " "+ var1.get("title.bhcreative.selectWorld.survival")));
        this.buttons.add(this.moreWorldOptions = new ButtonWidget(4, this.width / 2 - 75, 172, 150, 20, updateMoreOptionsButtonText()));
        this.buttons.add(this.worldTypeButton = new ButtonWidget(5, this.width / 2 - 155, 100, 150, 20, var1.get("selectWorld.worldtype")));
        this.buttons.add(this.betaFeaturesButton = new ButtonWidget(6, this.width / 2 + 5, 100, 150, 20, var1.get("selectWorld.betaFeatures")+ " " + var1.get("options.on")));
        this.buttons.add(this.winterModeButton = new ButtonWidget(7, this.width / 2 - 75, 150, 150, 20, var1.get("selectWorld.winterMode") + " " + var1.get("options.off")));
        if (!Objects.equals(WorldSettings.worldTypeName, "Alpha 1.1.2_01")) {
            this.winterModeButton.visible = false;
        }
        WorldSettings.addChangeListener(this::updateWorldTypeButtonText);
        updateGamemodeButtonText();
        updateWorldTypeButtonText();
        updateBetaFeaturesButtonText();
        updateWinterModeButtonText();
        this.moreOptions = ScreenStateCache.wasInMoreOptions;
        this.seedField.setFocused(this.moreOptions);
        this.worldNameField.setFocused(!this.moreOptions);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void fixCancelButtonY(CallbackInfo ci) {
        if (isBHCreativeModPresent()) {
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

    @Unique
    private void updateGamemodeButtonText() {
        TranslationStorage translation = TranslationStorage.getInstance();
        String gamemodeLabel = translation.get("selectWorld.gameMode");
        String text;
        if (isBHCreativeModPresent()) {
            if (!WorldSettings.hardcore && !this.creative) {
                text = translation.get("selectWorld.gameMode.survival");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            } else if (WorldSettings.hardcore && !this.creative) {
                text = translation.get("selectWorld.gameMode.hardcore");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            } else if (!WorldSettings.hardcore) {
                text = translation.get("title.bhcreative.selectWorld.creative");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            }
        } else {
            if (!WorldSettings.hardcore) {
                text = translation.get("selectWorld.gameMode.survival");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            } else {
                text = translation.get("selectWorld.gameMode.hardcore");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            }
        }
    }

    @Unique
    public void updateWorldTypeButtonText() {
        TranslationStorage translation = TranslationStorage.getInstance();

        this.worldTypeButton.text = translation.get("selectWorld.worldtype") + " " +
                Objects.requireNonNullElse(WorldSettings.getName(), "Default");
    }

    @Unique
    private String updateMoreOptionsButtonText() {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (this.moreOptions) {
            return translation.get("gui.done");
        } else {
            return translation.get("selectWorld.moreWorldOptions");
        }
    }

    @Unique
    private void updateBetaFeaturesButtonText() {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (WorldSettings.worldTypeName == null || "Default".equals(WorldSettings.worldTypeName) || "Nether".equals(WorldSettings.worldTypeName) || "Skylands".equals(WorldSettings.worldTypeName) || "Farlands".equals(WorldSettings.worldTypeName) || "Beta 1.1_02".equals(WorldSettings.worldTypeName) || "Aether".equals(WorldSettings.worldTypeName)) {
            betaFeaturesButton.active = false;
            WorldSettings.betaFeatures = true;
            betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.on");
        } else if ("Flat".equals(WorldSettings.worldTypeName) && !isFlat) {
            isFlat = true;
            betaFeaturesButton.active = true;
            WorldSettings.betaFeatures = false;
            betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.off");
        } else {
            betaFeaturesButton.active = true;
            if (isFlat) {
                isFlat = false;
                WorldSettings.betaFeatures = true;
            } else if (WorldSettings.betaFeatures && WorldSettings.alphaSnowCovered) {
                WorldSettings.alphaSnowCovered = false;
                updateWinterModeButtonText();
            }
            if (WorldSettings.betaFeatures) {
                betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.on");
            } else {
                betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.off");
            }

        }
    }

    @Unique
    private void updateWinterModeButtonText() {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (WorldSettings.alphaSnowCovered) {
            this.winterModeButton.text = translation.get("selectWorld.winterMode") + " " + translation.get("options.on");
        } else {
            this.winterModeButton.text = translation.get("selectWorld.winterMode") + " " + translation.get("options.off");
        }
    }


    @Unique
    private boolean isBHCreativeModPresent() {
        return FabricLoader.getInstance().isModLoaded("bhcreative");
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                if (isBHCreativeModPresent()) {
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
                if (!(WorldTypeList.worldtypeList == null)) {
                    WorldTypeList.selectWorldType(WorldTypeList.worldtypeList.get(0));
                }
                WorldSettings.resetBooleans();
            } else if (button.id == 3) {
                if (isBHCreativeModPresent()) {
                    if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                        WorldSettings.hardcore = true;
                        updateGamemodeButtonText();
                    } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                        WorldSettings.hardcore = false;
                        this.creative = true;
                        updateGamemodeButtonText();
                    } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Creative")) {
                        this.creative = false;
                        updateGamemodeButtonText();
                    }
                } else {
                    if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                        WorldSettings.hardcore = true;
                        updateGamemodeButtonText();
                    } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                        WorldSettings.hardcore = false;
                        updateGamemodeButtonText();
                    }
                }
            } else if (button.id == 4) {
                TranslationStorage translation = TranslationStorage.getInstance();
                this.moreOptions = !this.moreOptions;
                this.moreWorldOptions.text = this.moreOptions ? translation.get("gui.done") : translation.get("selectWorld.moreWorldOptions");
                this.seedField.setFocused(this.moreOptions);
                this.worldNameField.setFocused(!this.moreOptions);
            } else if (button.id == 5) {
                ScreenStateCache.lastEnteredSeed = this.seedField.getText();
                ScreenStateCache.lastEnteredWorldName = this.worldNameField.getText();
                ScreenStateCache.wasInMoreOptions = this.moreOptions;

                this.minecraft.setScreen(new CreateWorldTypeScreen(this));
            } else if (button.id == 6) {
                if (WorldSettings.betaFeatures) {
                    WorldSettings.betaFeatures = false;
                    updateBetaFeaturesButtonText();
                } else {
                    WorldSettings.betaFeatures = true;
                    updateBetaFeaturesButtonText();
                }
            } else if (button.id == 7) {
                if (WorldSettings.alphaSnowCovered) {
                    WorldSettings.alphaSnowCovered = false;
                    updateWinterModeButtonText();
                } else {
                    WorldSettings.alphaSnowCovered = true;
                    updateWinterModeButtonText();
                }
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
            this.gamemodeButton.visible = true;
            if (Objects.equals(WorldSettings.worldTypeName, "Alpha 1.1.2_01")) {
                this.winterModeButton.visible = false;
            }
            if (isBHCreativeModPresent()) {
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
            this.betaFeaturesButton.visible = true;
            this.gamemodeButton.visible = false;
            if (Objects.equals(WorldSettings.worldTypeName, "Alpha 1.1.2_01")) {
                this.winterModeButton.visible = true;
                if (WorldSettings.betaFeatures) {
                    this.winterModeButton.active = false;
                } else {
                    this.winterModeButton.active = true;
                }
            }
            if (WorldSettings.betaFeatures) {
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.line1"), this.width / 2 + 5, 122, 10526880);
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.line2"), this.width / 2 + 5, 134, 10526880);
            } else {
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.disabled.line1"), this.width / 2 + 5, 122, 10526880);
                this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.betaFeatures.info.disabled.line2"), this.width / 2 + 5, 134, 10526880);
            }
        }
        super.render(mouseX, mouseY, delta);
    }
}