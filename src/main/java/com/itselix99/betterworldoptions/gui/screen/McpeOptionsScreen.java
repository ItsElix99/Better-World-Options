package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

@Environment(EnvType.CLIENT)
public class McpeOptionsScreen extends Screen {
    protected final Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = translation.get("selectWorld.mcpeOptions");
    private final WorldGenerationOptions worldGenerationOptions;

    private ButtonWidget oldFeaturesButton;
    private ButtonWidget sizeButton;
    private ButtonWidget themeButton;
    private ButtonWidget singleBiomeButton;
    private ButtonWidget infiniteWorldButton;

    public McpeOptionsScreen(Screen parent, WorldGenerationOptions worldGenerationOptions) {
        this.parent = parent;
        this.worldGenerationOptions = worldGenerationOptions;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.add(this.oldFeaturesButton = new ButtonWidget(0, this.width / 2 - 100, this.height / 4, this.translation.get("selectWorld.oldFeatures") + " " + (this.worldGenerationOptions.oldFeatures ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(this.sizeButton = new ButtonWidget(1, this.width / 2 - 100, this.height / 4 + 24, this.translation.get("indevOptions.size") + " " + this.worldGenerationOptions.size + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ));
        this.buttons.add(this.themeButton = new ButtonWidget(2, this.width / 2 - 100, this.height / 4 + 48, this.translation.get("selectWorld.theme") + " " + this.worldGenerationOptions.theme));
        this.buttons.add(this.singleBiomeButton = new ButtonWidget(3, this.width / 2 - 100, this.height / 4 + 72, this.translation.get("selectWorld.singleBiome") + " " + (!this.worldGenerationOptions.singleBiome.equals("Off") ? this.worldGenerationOptions.singleBiome : this.translation.get("options.off"))));
        this.buttons.add(this.infiniteWorldButton = new ButtonWidget(4, this.width / 2 - 100, this.height / 4 + 96, this.translation.get("indevOptions.infiniteWorld") + " " + (this.worldGenerationOptions.infiniteWorld ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(5, this.width / 2 - 100, this.height / 6 + 168, this.translation.get("gui.done")));
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.worldGenerationOptions.oldFeatures = !this.worldGenerationOptions.oldFeatures;

                this.oldFeaturesButton.text = this.translation.get("selectWorld.oldFeatures") + " " + (this.worldGenerationOptions.oldFeatures ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 1) {
                if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Normal" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Huge";
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Huge" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Gigantic";
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Gigantic" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Enormous";
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Enormous" + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ)) {
                    this.worldGenerationOptions.size = "Normal";
                }

                this.worldGenerationOptions.setSizeXZ();
                button.text = this.translation.get("indevOptions.size") + " " + this.worldGenerationOptions.size + " " + this.worldGenerationOptions.worldSizeX + "x" + this.worldGenerationOptions.worldSizeZ;
            } else if (button.id == 2) {
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
            } else if (button.id == 3) {
                this.minecraft.setScreen(new BiomeListScreen(this, this.worldGenerationOptions));
            } else if (button.id == 4) {
                this.worldGenerationOptions.infiniteWorld = !this.worldGenerationOptions.infiniteWorld;
                button.text = this.translation.get("indevOptions.infiniteWorld") + " " + (this.worldGenerationOptions.infiniteWorld ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 5) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);

        this.infiniteWorldButton.text = this.translation.get("indevOptions.infiniteWorld") + " " + (this.worldGenerationOptions.infiniteWorld ? this.translation.get("options.on") : this.translation.get("options.off"));
        this.singleBiomeButton.text = this.translation.get("selectWorld.singleBiome") + " " + (!this.worldGenerationOptions.singleBiome.equals("Off") ? this.worldGenerationOptions.singleBiome : this.translation.get("options.off"));

        this.sizeButton.active = !this.worldGenerationOptions.infiniteWorld;

        super.render(mouseX, mouseY, delta);
    }
}