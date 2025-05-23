package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.fabricmc.loader.api.FabricLoader;
import com.itselix99.betterworldoptions.gui.CreateWorldTypeScreen;
import com.itselix99.betterworldoptions.gui.ScreenStateCache;
import com.itselix99.betterworldoptions.world.WorldSettings;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.CharacterUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.WorldStorageSource;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;
import java.util.Random;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin extends Screen {
    @Unique final Screen parent;
    @Unique TextFieldWidget worldNameField;
    @Unique TextFieldWidget seedField;
    @Unique private String worldSaveName;
    @Unique boolean moreOptions;
    @Unique private ButtonWidget gamemodeButton;
    @Unique private ButtonWidget moreWorldOptions;
    @Unique private ButtonWidget worldTypeButton;
    @Unique private ButtonWidget betaFeaturesButton;
    @Unique private boolean creative = false;
    @Shadow private boolean creatingLevel;
    @Unique private boolean isFlat = false;

    public CreateWorldScreenMixin(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void tick() {
        this.worldNameField.tick();
        this.seedField.tick();
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    public void init(CallbackInfo ci) {
        ci.cancel();
        TranslationStorage var1 = TranslationStorage.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(0, this.width / 2 - 155, this.height - 28, 150, 20, var1.get("selectWorld.create")));
        this.buttons.add(new ButtonWidget(1, this.width / 2 + 5, this.height - 28, 150, 20, var1.get("gui.cancel")));
        this.buttons.add(this.gamemodeButton = new ButtonWidget(2, this.width / 2 - 75, 100, 150, 20, var1.get("selectWorld.gamemode")+ " "+ var1.get("title.bhcreative.selectWorld.survival")));
        this.buttons.add(this.moreWorldOptions = new ButtonWidget(3, this.width / 2 - 75, 172, 150, 20, updateMoreOptionsButtonText()));
        this.buttons.add(this.worldTypeButton = new ButtonWidget(4, this.width / 2 - 155, 100, 150, 20, var1.get("selectWorld.worldtype")));
        this.buttons.add(this.betaFeaturesButton = new ButtonWidget(5, this.width / 2 + 5, 100, 150, 20, var1.get("selectWorld.betaFeatures")+ " " + var1.get("options.on")));
        this.worldNameField = new TextFieldWidget(this, this.textRenderer, this.width / 2 - 100, 60, 200, 20, getWorldName());
        this.worldNameField.focused = true;
        this.worldNameField.setMaxLength(32);
        this.seedField = new TextFieldWidget(this, this.textRenderer, this.width / 2 - 100, 60, 200, 20, getSeed());
        this.getSaveDirectoryNames();
        WorldSettings.addChangeListener(this::updateWorldTypeButtonText);
        updateGamemodeButtonText();
        updateWorldTypeButtonText();
        updateBetaFeaturesButtonText();
        this.moreOptions = ScreenStateCache.wasInMoreOptions;
        this.seedField.setFocused(this.moreOptions);
        this.worldNameField.setFocused(!this.moreOptions);
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
        String gamemodeLabel = translation.get("selectWorld.gamemode");
        String text;
        if (isBHCreativeModPresent()) {
            if (!WorldSettings.hardcore && !this.creative) {
                text = translation.get("selectWorld.gamemode.survival");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            } else if (WorldSettings.hardcore && !this.creative) {
                text = translation.get("selectWorld.gamemode.hardcore");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            } else if (!WorldSettings.hardcore) {
                text = translation.get("title.bhcreative.selectWorld.creative");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            }
        } else {
            if (!WorldSettings.hardcore) {
                text = translation.get("selectWorld.gamemode.survival");
                this.gamemodeButton.text = gamemodeLabel + " " + text;
            } else {
                text = translation.get("selectWorld.gamemode.hardcore");
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
        if (WorldSettings.name == null || "Default".equals(WorldSettings.name) || "Nether".equals(WorldSettings.name) || "Skylands".equals(WorldSettings.name) || "Farlands".equals(WorldSettings.name) || "Beta 1.1_02".equals(WorldSettings.name)) {
            betaFeaturesButton.active = false;
            WorldSettings.betaFeatures = true;
            betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.on");
        } else if ("Flat".equals(WorldSettings.name)) {
            betaFeaturesButton.active = true;
            WorldSettings.betaFeatures = false;
            isFlat = true;
            betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.off");
        } else {
            betaFeaturesButton.active = true;
            if (isFlat) {
                isFlat = false;
                WorldSettings.betaFeatures = true;
            }
            if (WorldSettings.betaFeatures) {
                betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.on");
            } else {
                betaFeaturesButton.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.off");
            }

        }
    }


    @Unique
    private boolean isBHCreativeModPresent() {
        return FabricLoader.getInstance().isModLoaded("bhcreative");
    }


    @Unique
    private void getSaveDirectoryNames() {
        this.worldSaveName = this.worldNameField.getText().trim();

        for(char var4 : CharacterUtils.INVALID_CHARS_WORLD_NAME) {
            this.worldSaveName = this.worldSaveName.replace(var4, '_');
        }

        if (MathHelper.isNullOrEmpty(this.worldSaveName)) {
            this.worldSaveName = "World";
        }

        this.worldSaveName = getWorldSaveName(this.minecraft.getWorldStorageSource(), this.worldSaveName);
    }

    @Shadow
    public static String getWorldSaveName(WorldStorageSource storageSource, String worldName) {
        while(storageSource.method_1004(worldName) != null) {
            worldName = worldName + "-";
        }

        return worldName;
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.minecraft.setScreen(null);
                if (this.creatingLevel) return;

                this.creatingLevel = true;

                long seed = new Random().nextLong();
                String seedText = this.seedField.getText();
                if (!MathHelper.isNullOrEmpty(seedText)) {
                    try {
                        seed = Long.parseLong(seedText);
                    } catch (NumberFormatException e) {
                        seed = seedText.hashCode();
                    }
                }

                this.minecraft.interactionManager = new SingleplayerInteractionManager(this.minecraft);
                this.minecraft.startGame(this.worldSaveName, this.worldNameField.getText(), seed);
                this.minecraft.setScreen(null);

                if (isBHCreativeModPresent()) {
                    if (minecraft.player != null) {
                        minecraft.player.creative_setCreative(this.creative);
                    }
                }

                ScreenStateCache.lastEnteredSeed = null;
                ScreenStateCache.lastEnteredWorldName = null;
                ScreenStateCache.wasInMoreOptions = false;
            } else if (button.id == 1) {
                this.minecraft.setScreen(new SelectWorldScreen(this.parent));
                ScreenStateCache.lastEnteredSeed = null;
                ScreenStateCache.lastEnteredWorldName = null;
                ScreenStateCache.wasInMoreOptions = false;
                if (!(WorldTypeList.worldtypeList == null)) {
                    WorldTypeList.selectWorldType(WorldTypeList.worldtypeList.get(0));
                }
                WorldSettings.resetHardcore();
            } else if (button.id == 2) {
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
            } else if (button.id == 3) {
                TranslationStorage translation = TranslationStorage.getInstance();
                this.moreOptions = !this.moreOptions;
                this.moreWorldOptions.text = this.moreOptions ? translation.get("gui.done") : translation.get("selectWorld.moreWorldOptions");
                this.seedField.setFocused(this.moreOptions);
                this.worldNameField.setFocused(!this.moreOptions);
            } else if (button.id == 4) {
                ScreenStateCache.lastEnteredSeed = this.seedField.getText();
                ScreenStateCache.lastEnteredWorldName = this.worldNameField.getText();
                ScreenStateCache.wasInMoreOptions = this.moreOptions;

                this.minecraft.setScreen(new CreateWorldTypeScreen(this));
            } else if (button.id == 5) {
                if (WorldSettings.betaFeatures) {
                    WorldSettings.betaFeatures = false;
                    updateBetaFeaturesButtonText();
                } else {
                    WorldSettings.betaFeatures = true;
                    updateBetaFeaturesButtonText();
                }
            }
        }
        ci.cancel();
    }


    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressed(char character, int keyCode, CallbackInfo ci) {
        TextFieldWidget activeField = this.moreOptions ? this.seedField : this.worldNameField;
        activeField.keyPressed(character, keyCode);

        if (character == '\r') {
            this.buttonClicked((ButtonWidget)this.buttons.get(0));
            ci.cancel();
        }

        ((ButtonWidget)this.buttons.get(0)).active = !this.worldNameField.getText().isEmpty();
        this.getSaveDirectoryNames();
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TranslationStorage var4 = TranslationStorage.getInstance();
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.create"), this.width / 2, 20, 16777215);
        if(!this.moreOptions) {
            this.worldTypeButton.visible = false;
            this.betaFeaturesButton.visible = false;
            this.gamemodeButton.visible = true;
            this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
            this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.resultFolder") + " " + this.worldSaveName, this.width / 2 - 100, 85, 10526880);
            if (isBHCreativeModPresent()) {
                if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gamemode.survival.info"), this.width / 2, 122, 10526880);
                } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gamemode.hardcore.info"), this.width / 2, 122, 10526880);
                } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Creative")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gamemode.creative.info"), this.width / 2, 122, 10526880);
                }
            } else {
                if (Objects.equals(this.gamemodeButton.text, "Game Mode: Survival")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gamemode.survival.info"), this.width / 2, 122, 10526880);
                } else if (Objects.equals(this.gamemodeButton.text, "Game Mode: Hardcore")) {
                    this.drawCenteredTextWithShadow(this.textRenderer, var4.get("selectWorld.gamemode.hardcore.info"), this.width / 2, 122, 10526880);
                }
            }
            this.worldNameField.render();
        } else {
            this.worldTypeButton.visible = true;
            this.betaFeaturesButton.visible = true;
            this.gamemodeButton.visible = false;
            this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.enterSeed"), this.width / 2 - 100, 47, 10526880);
            this.drawTextWithShadow(this.textRenderer, var4.get("selectWorld.seedInfo"), this.width / 2 - 100, 85, 10526880);
            this.seedField.render();
        }
        super.render(mouseX, mouseY, delta);
    }
}