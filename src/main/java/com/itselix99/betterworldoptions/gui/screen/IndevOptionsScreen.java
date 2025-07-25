package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.gui.UpdateButtonText;
import com.itselix99.betterworldoptions.world.WorldSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class IndevOptionsScreen extends Screen {
    private final Screen parent;
    protected String title = "Indev Options";
    private final TranslationStorage translation = TranslationStorage.getInstance();
    private ButtonWidget betaFeaturesButton;
    private ButtonWidget shapeButton;
    private ButtonWidget sizeButton;
    private ButtonWidget themeButton;
    private ButtonWidget betaThemeButton;
    private ButtonWidget infiniteButton;

    public IndevOptionsScreen(Screen parent) {
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.add(this.betaFeaturesButton = new ButtonWidget(0, this.width / 2 - 155, this.height / 4, 310, 20, this.translation.get("selectWorld.betaFeatures") + " " + (WorldSettings.GameMode.isBetaFeatures() ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(1, this.width / 2 - 155, this.height / 4 + 24, 150, 20, this.translation.get("indevOptions.type") + " " + WorldSettings.IndevWorld.getIndevWorldType()));
        this.buttons.add(this.shapeButton = new ButtonWidget(2, this.width / 2 + 5, this.height / 4 + 24, 150, 20, this.translation.get("indevOptions.shape") + " " + WorldSettings.IndevWorld.getShape()));
        this.buttons.add(this.sizeButton = new ButtonWidget(3, this.width / 2 - 155, this.height / 4 + 48, 150, 20, this.translation.get("indevOptions.size") + " " + WorldSettings.IndevWorld.getSize() + " " + WorldSettings.IndevWorld.getSizeInNumber()));
        this.buttons.add(this.themeButton = new ButtonWidget(4, this.width / 2 + 5, this.height / 4 + 48, 150, 20, this.translation.get("indevOptions.theme") + " " + WorldSettings.IndevWorld.getTheme()));
        this.buttons.add(this.betaThemeButton = new ButtonWidget(5, this.width / 2 + 5, this.height / 4 + 48, 150, 20, this.translation.get("indevOptions.betaTheme") + " " + WorldSettings.IndevWorld.getBetaTheme()));
        this.buttons.add(new ButtonWidget(6, this.width / 2 - 155, this.height / 4 + 72, 150, 20, this.translation.get("indevOptions.indevDimensions") + " " + (WorldSettings.IndevWorld.isIndevDimensions() ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(7, this.width / 2 + 5, this.height / 4 + 72, 150, 20, this.translation.get("indevOptions.indevHouse") + " " + (WorldSettings.IndevWorld.isGenerateIndevHouse() ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(this.infiniteButton = new ButtonWidget(8, this.width / 2 - 155, this.height / 4 + 96, 310, 20, this.translation.get("indevOptions.infinite") + " " + (WorldSettings.IndevWorld.isInfinite() ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(9, this.width / 2 - 100, this.height / 6 + 168, this.translation.get("gui.done")));
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                UpdateButtonText.updateIndevBetaFeaturesButtonText(this.betaFeaturesButton);
            } else if (button.id == 1) {
                if (Objects.equals(button.text, this.translation.get("indevOptions.type") + " " + "Island")) {
                    WorldSettings.IndevWorld.setIndevWorldType("Floating");
                    if (WorldSettings.IndevWorld.isInfinite()) WorldSettings.IndevWorld.setInfinite(false);
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.type") + " " + "Floating")) {
                    WorldSettings.IndevWorld.setIndevWorldType("Flat");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.type") + " " + "Flat")) {
                    WorldSettings.IndevWorld.setIndevWorldType("Inland");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.type") + " " + "Inland")) {
                    WorldSettings.IndevWorld.setIndevWorldType("Island");
                    if (WorldSettings.IndevWorld.isInfinite()) WorldSettings.IndevWorld.setInfinite(false);
                }

                button.text = this.translation.get("indevOptions.type") + " " + WorldSettings.IndevWorld.getIndevWorldType();
            } else if (button.id == 2) {
                if (Objects.equals(button.text, this.translation.get("indevOptions.shape") + " " + "Square")) {
                    WorldSettings.IndevWorld.setShape("Long");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.shape") + " " + "Long")) {
                    WorldSettings.IndevWorld.setShape("Square");
                }

                button.text = this.translation.get("indevOptions.shape") + " " + WorldSettings.IndevWorld.getShape();
            } else if (button.id == 3) {
                if (Objects.equals(button.text, this.translation.get("indevOptions.size") + " " + "Normal" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Huge");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.size") + " " + "Huge" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Very Huge");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.size") + " " + "Very Huge" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Small");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.size") + " " + "Small" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Normal");
                }
            } else if (button.id == 4) {
                if (Objects.equals(button.text, this.translation.get("indevOptions.theme") + " " + "Normal")) {
                    WorldSettings.IndevWorld.setTheme("Hell");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.theme") + " " + "Hell")) {
                    WorldSettings.IndevWorld.setTheme("Paradise");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.theme") + " " + "Paradise")) {
                    WorldSettings.IndevWorld.setTheme("Woods");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.theme") + " " + "Woods")) {
                    WorldSettings.IndevWorld.setTheme("Normal");
                }

                button.text = this.translation.get("indevOptions.theme") + " " + WorldSettings.IndevWorld.getTheme();
            } else if (button.id == 5) {
                if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "All Biomes")) {
                    WorldSettings.IndevWorld.setBetaTheme("Rainforest");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Rainforest")) {
                    WorldSettings.IndevWorld.setBetaTheme("Swampland");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Swampland")) {
                    WorldSettings.IndevWorld.setBetaTheme("Seasonal Forest");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Seasonal Forest")) {
                    WorldSettings.IndevWorld.setBetaTheme("Forest");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Forest")) {
                    WorldSettings.IndevWorld.setBetaTheme("Savanna");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Savanna")) {
                    WorldSettings.IndevWorld.setBetaTheme("Shrubland");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Shrubland")) {
                    WorldSettings.IndevWorld.setBetaTheme("Taiga");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Taiga")) {
                    WorldSettings.IndevWorld.setBetaTheme("Desert");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Desert")) {
                    WorldSettings.IndevWorld.setBetaTheme("Plains");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Plains")) {
                    WorldSettings.IndevWorld.setBetaTheme("Ice Desert");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Ice Desert")) {
                    WorldSettings.IndevWorld.setBetaTheme("Tundra");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Tundra")) {
                    WorldSettings.IndevWorld.setBetaTheme("Hell");
                } else if (Objects.equals(button.text, this.translation.get("indevOptions.betaTheme") + " " + "Hell")) {
                    WorldSettings.IndevWorld.setBetaTheme("All Biomes");
                }

                button.text = this.translation.get("indevOptions.betaTheme") + " " + WorldSettings.IndevWorld.getBetaTheme();
            } else if (button.id == 6) {
                WorldSettings.IndevWorld.setIndevDimensions(!WorldSettings.IndevWorld.isIndevDimensions());
                button.text = this.translation.get("indevOptions.indevDimensions") + " " + (WorldSettings.IndevWorld.isIndevDimensions() ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 7) {
                WorldSettings.IndevWorld.setGenerateIndevHouse(!WorldSettings.IndevWorld.isGenerateIndevHouse());
                button.text = this.translation.get("indevOptions.indevHouse") + " " + (WorldSettings.IndevWorld.isGenerateIndevHouse() ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 8) {
                WorldSettings.IndevWorld.setInfinite(!WorldSettings.IndevWorld.isInfinite());
                button.text = this.translation.get("indevOptions.infinite") + " " + (WorldSettings.IndevWorld.isInfinite() ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 9) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);

        if (Objects.equals(WorldSettings.IndevWorld.getIndevWorldType(), "Island") || Objects.equals(WorldSettings.IndevWorld.getIndevWorldType(), "Floating")) {
            this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get("indevOptions.infinite.info"), this.width / 2, this.infiniteButton.y + 22, 10526880);
        }

        this.sizeButton.text = this.translation.get("indevOptions.size") + " " + WorldSettings.IndevWorld.getSize() + " " + WorldSettings.IndevWorld.getSizeInNumber();
        this.infiniteButton.text = this.translation.get("indevOptions.infinite") + " " + (WorldSettings.IndevWorld.isInfinite() ? this.translation.get("options.on") : this.translation.get("options.off"));

        if (WorldSettings.GameMode.isBetaFeatures()) {
            this.betaThemeButton.visible = true;
            this.themeButton.visible = false;
        } else {
            this.betaThemeButton.visible = false;
            this.themeButton.visible = true;
        }

        this.infiniteButton.active = Objects.equals(WorldSettings.IndevWorld.getIndevWorldType(), "Flat") || Objects.equals(WorldSettings.IndevWorld.getIndevWorldType(), "Inland");

        if (WorldSettings.IndevWorld.isInfinite()) {
            this.sizeButton.active = false;
            this.shapeButton.active = false;
        } else {
            this.sizeButton.active = true;
            this.shapeButton.active = true;
        }

        super.render(mouseX, mouseY, delta);
    }
}