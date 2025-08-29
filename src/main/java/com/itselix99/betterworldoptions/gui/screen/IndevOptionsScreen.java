package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

@Environment(EnvType.CLIENT)
public class IndevOptionsScreen extends Screen {
    protected final Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = translation.get("selectWorld.indevOptions");
    private final WorldGenerationOptions worldGenerationOptions;

    private ButtonWidget oldFeaturesButton;
    private ButtonWidget shapeButton;
    private ButtonWidget sizeButton;
    private ButtonWidget themeButton;
    private ButtonWidget singleBiomeButton;
    private ButtonWidget infiniteWorldButton;

    public IndevOptionsScreen(Screen parent, WorldGenerationOptions worldGenerationOptions) {
        this.parent = parent;
        this.worldGenerationOptions = worldGenerationOptions;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.add(this.oldFeaturesButton = new ButtonWidget(0, this.width / 2 - 155, this.height / 4, 310, 20, this.translation.get("selectWorld.oldFeatures") + " " + (this.worldGenerationOptions.oldFeatures ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(1, this.width / 2 - 155, this.height / 4 + 24, 150, 20, this.translation.get("indevOptions.type") + " " + this.worldGenerationOptions.indevWorldType));
        this.buttons.add(this.shapeButton = new ButtonWidget(2, this.width / 2 + 5, this.height / 4 + 24, 150, 20, this.translation.get("indevOptions.shape") + " " + this.worldGenerationOptions.indevShape));
        this.buttons.add(this.sizeButton = new ButtonWidget(3, this.width / 2 - 155, this.height / 4 + 48, 150, 20, this.translation.get("indevOptions.size") + " " + this.worldGenerationOptions.size + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ));
        this.buttons.add(this.themeButton = new ButtonWidget(4, this.width / 2 + 5, this.height / 4 + 48, 150, 20, this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme));
        this.buttons.add(this.singleBiomeButton = new ButtonWidget(5, this.width / 2 - 155, this.height / 4 + 72, 150, 20, this.translation.get("selectWorld.singleBiome") + " " + (!this.worldGenerationOptions.singleBiome.equals("0ff") ? this.worldGenerationOptions.singleBiome : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(6, this.width / 2 + 5, this.height / 4 + 72, 150, 20, this.translation.get("indevOptions.indevHouse") + " " + (this.worldGenerationOptions.generateIndevHouse ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(this.infiniteWorldButton = new ButtonWidget(7, this.width / 2 - 155, this.height / 4 + 96, 310, 20, this.translation.get("indevOptions.infiniteWorld") + " " + (this.worldGenerationOptions.infiniteWorld ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(8, this.width / 2 - 100, this.height / 6 + 168, this.translation.get("gui.done")));
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.worldGenerationOptions.oldFeatures = !this.worldGenerationOptions.oldFeatures;
                this.worldGenerationOptions.oldTextures = this.worldGenerationOptions.oldFeatures;

                if (this.worldGenerationOptions.oldFeatures && !this.worldGenerationOptions.singleBiome.equals("Off")) {
                    this.worldGenerationOptions.singleBiome = "0ff";
                }

                this.oldFeaturesButton.text = this.translation.get("selectWorld.oldFeatures") + " " + (this.worldGenerationOptions.oldFeatures ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 1) {
                if (button.text.equals(this.translation.get("indevOptions.type") + " " + "Island")) {
                    this.worldGenerationOptions.indevWorldType = "Floating";
                    if (this.worldGenerationOptions.infiniteWorld) this.worldGenerationOptions.infiniteWorld = false;
                } else if (button.text.equals(this.translation.get("indevOptions.type") + " " + "Floating")) {
                    this.worldGenerationOptions.indevWorldType = "Flat";
                } else if (button.text.equals(this.translation.get("indevOptions.type") + " " + "Flat")) {
                    this.worldGenerationOptions.indevWorldType = "Inland";
                } else if (button.text.equals(this.translation.get("indevOptions.type") + " " + "Inland")) {
                    this.worldGenerationOptions.indevWorldType = "Island";
                    if (this.worldGenerationOptions.infiniteWorld) this.worldGenerationOptions.infiniteWorld = false;
                }

                button.text = this.translation.get("indevOptions.type") + " " + this.worldGenerationOptions.indevWorldType;
            } else if (button.id == 2) {
                if (this.shapeButton.text.equals(this.translation.get("indevOptions.shape") + " " + "Square")) {
                    this.worldGenerationOptions.indevShape = "Long";
                } else if (this.shapeButton.text.equals(this.translation.get("indevOptions.shape") + " " + "Long")) {
                    this.worldGenerationOptions.indevShape = "Square";
                }

                this.shapeButton.text = this.translation.get("indevOptions.shape") + " " + this.worldGenerationOptions.indevShape;
            } else if (button.id == 3) {
                if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Normal" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Huge";
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Huge" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Gigantic";
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Gigantic" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Enormous";
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Enormous" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Small";
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Small" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Normal";
                }
            } else if (button.id == 4) {
                if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Normal")) {
                    this.worldGenerationOptions.theme = "Hell";
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Hell")) {
                    this.worldGenerationOptions.theme = "Paradise";
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Paradise")) {
                    this.worldGenerationOptions.theme = "Woods";
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Woods")) {
                    this.worldGenerationOptions.theme = "Winter";
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Winter")) {
                    this.worldGenerationOptions.theme = "Normal";
                }

                button.text = this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme;
            } else if (button.id == 5) {
                this.minecraft.setScreen(new BiomeListScreen(this, this.worldGenerationOptions));
            } else if (button.id == 6) {
                this.worldGenerationOptions.generateIndevHouse = !this.worldGenerationOptions.generateIndevHouse;
                button.text = this.translation.get("indevOptions.indevHouse") + " " + (this.worldGenerationOptions.generateIndevHouse ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 7) {
                this.worldGenerationOptions.infiniteWorld = !this.worldGenerationOptions.infiniteWorld;
                button.text = this.translation.get("indevOptions.infiniteWorld") + " " + (this.worldGenerationOptions.infiniteWorld ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 8) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);

        if (this.worldGenerationOptions.indevWorldType.equals("Island") || this.worldGenerationOptions.indevWorldType.equals("Floating")) {
            this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get("indevOptions.infiniteWorld.info"), this.width / 2, this.infiniteWorldButton.y + 22, 10526880);
        }

        this.worldGenerationOptions.setSizeXZ();
        this.sizeButton.text = this.translation.get("indevOptions.size") + " " + this.worldGenerationOptions.size + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ;
        this.infiniteWorldButton.text = this.translation.get("indevOptions.infiniteWorld") + " " + (this.worldGenerationOptions.infiniteWorld ? this.translation.get("options.on") : this.translation.get("options.off"));
        this.singleBiomeButton.text = this.translation.get("selectWorld.singleBiome") + " " + (!this.worldGenerationOptions.singleBiome.equals("Off") ? this.worldGenerationOptions.singleBiome : this.translation.get("options.off"));

        this.singleBiomeButton.active = !this.worldGenerationOptions.oldFeatures;

        this.infiniteWorldButton.active = this.worldGenerationOptions.indevWorldType.equals("Flat") || this.worldGenerationOptions.indevWorldType.equals("Inland");

        if (this.worldGenerationOptions.infiniteWorld) {
            this.sizeButton.active = false;
            this.shapeButton.active = false;
        } else {
            this.sizeButton.active = true;
            this.shapeButton.active = true;
        }

        super.render(mouseX, mouseY, delta);
    }
}