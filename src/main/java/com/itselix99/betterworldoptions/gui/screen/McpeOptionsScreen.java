package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.world.WorldSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class McpeOptionsScreen extends Screen {
    protected final Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = translation.get("selectWorld.mcpeOptions");

    private ButtonWidget betaFeaturesButton;
    private ButtonWidget sizeButton;
    private ButtonWidget themeButton;
    private ButtonWidget singleBiomeButton;
    private ButtonWidget infiniteWorldButton;

    public McpeOptionsScreen(Screen parent) {
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.add(this.betaFeaturesButton = new ButtonWidget(0, this.width / 2 - 100, this.height / 4, this.translation.get("selectWorld.betaFeatures") + " " + (WorldSettings.GameMode.isBetaFeatures() ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(this.sizeButton = new ButtonWidget(3, this.width / 2 - 100, this.height / 4 + 24, this.translation.get("indevOptions.size") + " " + WorldSettings.IndevWorld.getSize() + " " + WorldSettings.IndevWorld.getSizeInNumber()));
        this.buttons.add(this.themeButton = new ButtonWidget(4, this.width / 2 - 100, this.height / 4 + 48, this.translation.get("selectWorld.theme") + " " + WorldSettings.World.getTheme()));
        this.buttons.add(this.singleBiomeButton = new ButtonWidget(5, this.width / 2 - 100, this.height / 4 + 72, this.translation.get("selectWorld.singleBiome") + " " + (WorldSettings.World.getSingleBiome() != null ? WorldSettings.World.getSingleBiome().name : this.translation.get("options.off"))));
        this.buttons.add(this.infiniteWorldButton = new ButtonWidget(8, this.width / 2 - 100, this.height / 4 + 96, this.translation.get("indevOptions.infiniteWorld") + " " + (WorldSettings.IndevWorld.isInfiniteWorld() ? this.translation.get("options.on") : this.translation.get("options.off"))));
        this.buttons.add(new ButtonWidget(9, this.width / 2 - 100, this.height / 6 + 168, this.translation.get("gui.done")));
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                WorldSettings.GameMode.setBetaFeatures(!WorldSettings.GameMode.isBetaFeatures());

                if (!WorldSettings.GameMode.isBetaFeatures() && WorldSettings.World.getSingleBiome() != null) {
                    WorldSettings.World.setSingleBiome(null);
                }

                this.betaFeaturesButton.text = this.translation.get("selectWorld.betaFeatures") + " " + (WorldSettings.GameMode.isBetaFeatures() ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 3) {
                if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Normal" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Huge");
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Huge" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Gigantic");
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Gigantic" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Enormous");
                } else if (this.sizeButton.text.equals(this.translation.get("indevOptions.size") + " " + "Enormous" + " " + WorldSettings.IndevWorld.getSizeInNumber())) {
                    WorldSettings.IndevWorld.setSize("Normal");
                }

                button.text = this.translation.get("indevOptions.size") + " " + WorldSettings.IndevWorld.getSize() + " " + WorldSettings.IndevWorld.getSizeInNumber();
            } else if (button.id == 4) {
                if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Normal")) {
                    WorldSettings.World.setTheme("Hell");
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Hell")) {
                    WorldSettings.World.setTheme("Paradise");
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Paradise")) {
                    WorldSettings.World.setTheme("Woods");
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Woods")) {
                    WorldSettings.World.setTheme("Winter");
                } else if (this.themeButton.text.equals(this.translation.get("selectWorld.theme") + " " + "Winter")) {
                    WorldSettings.World.setTheme("Normal");
                }

                button.text = this.translation.get("selectWorld.theme") + " " + WorldSettings.World.getTheme();
            } else if (button.id == 5) {
                this.minecraft.setScreen(new BiomeListScreen(this));
            } else if (button.id == 8) {
                WorldSettings.IndevWorld.setInfiniteWorld(!WorldSettings.IndevWorld.isInfiniteWorld());
                button.text = this.translation.get("indevOptions.infiniteWorld") + " " + (WorldSettings.IndevWorld.isInfiniteWorld() ? this.translation.get("options.on") : this.translation.get("options.off"));
            } else if (button.id == 9) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);

        this.infiniteWorldButton.text = this.translation.get("indevOptions.infiniteWorld") + " " + (WorldSettings.IndevWorld.isInfiniteWorld() ? this.translation.get("options.on") : this.translation.get("options.off"));
        this.singleBiomeButton.text = this.translation.get("selectWorld.singleBiome") + " " + (WorldSettings.World.getSingleBiome() != null ? WorldSettings.World.getSingleBiome().name : this.translation.get("options.off"));

        this.singleBiomeButton.active = WorldSettings.GameMode.isBetaFeatures();
        this.sizeButton.active = !WorldSettings.IndevWorld.isInfiniteWorld();

        super.render(mouseX, mouseY, delta);
    }
}