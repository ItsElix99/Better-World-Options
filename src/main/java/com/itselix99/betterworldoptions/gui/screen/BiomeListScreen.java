package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;

import java.util.List;

public class BiomeListScreen extends Screen {
    protected Screen parent;
    protected String title = "Select Biome";
    private final WorldGenerationOptions worldGenerationOptions;
    private final TranslationStorage translation = TranslationStorage.getInstance();

    private BiomeListWidget biomeListWidget;
    private ButtonWidget buttonSelect;
    private ButtonWidget allBiomesButton;
    private Biome selectedBiome;

    public BiomeListScreen(Screen parent, WorldGenerationOptions worldGenerationOptions) {
        this.parent = parent;
        this.worldGenerationOptions = worldGenerationOptions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        this.biomeListWidget = new BiomeListWidget(this);
        this.biomeListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.buttonSelect = new ButtonWidget(0, this.width / 2 + 5, this.height - 28, 150, 20, this.translation.get("gui.cancel")));
        this.buttons.add(this.allBiomesButton = new ButtonWidget(1, this.width / 2 - 155, this.height - 28, 150, 20, this.translation.get("selectWorld.allBiomes")));
        if (!this.worldGenerationOptions.singleBiome.equals("Off")) {
            selectedBiome = OverworldBiomeProviderImpl.getInstance().getBiomes().stream().filter(biome -> biome.name.equals(this.worldGenerationOptions.singleBiome)).toList().get(0);
        } else {
            this.allBiomesButton.active = false;
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.minecraft.setScreen(this.parent);
            } else if (button.id == 1) {
                if (!this.worldGenerationOptions.singleBiome.equals("Off")) {
                    this.worldGenerationOptions.singleBiome = "Off";
                    selectedBiome = null;
                    button.active = false;
                    this.buttonSelect.text = this.translation.get("gui.done");
                }
            }
        }
    }

    @Override
    public void render(int var1, int var2, float var3) {
        this.biomeListWidget.render(var1, var2, var3);
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(var1, var2, var3);
    }

    private void selectBiome(Biome var1) {
        this.selectedBiome = var1;
    }

    @Environment(EnvType.CLIENT)
    class BiomeListWidget extends EntryListWidget {
        public BiomeListWidget(BiomeListScreen biomeListScreen) {
            super(biomeListScreen.minecraft, biomeListScreen.width, biomeListScreen.height, 32, biomeListScreen.height - 55 + 4, 13);
        }

        protected int getEntryCount() {
            List<Biome> var1 = OverworldBiomeProviderImpl.getInstance().getBiomes().stream().toList();
            return var1.size();
        }

        protected void entryClicked(int index, boolean doubleClick) {
            List<Biome> var3 = OverworldBiomeProviderImpl.getInstance().getBiomes().stream().toList();
            BiomeListScreen.this.selectBiome(var3.get(index));

            if (!var3.get(index).name.equals(WorldGenerationOptions.getInstance().singleBiome)) {
                BiomeListScreen.this.worldGenerationOptions.singleBiome = var3.get(index).name;
                BiomeListScreen.this.allBiomesButton.active = true;
                BiomeListScreen.this.buttonSelect.text = BiomeListScreen.this.translation.get("gui.done");
            }
        }

        protected boolean isSelectedEntry(int index) {
            List<Biome> var2 = OverworldBiomeProviderImpl.getInstance().getBiomes().stream().toList();
            return BiomeListScreen.this.selectedBiome == var2.get(index);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 13;
        }

        protected void renderBackground() {
            BiomeListScreen.this.renderBackground();
        }

        protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
            Biome var6 = OverworldBiomeProviderImpl.getInstance().getBiomes().stream().toList().get(index);
            BiomeListScreen.this.drawCenteredTextWithShadow(BiomeListScreen.this.textRenderer, var6.name, BiomeListScreen.this.width / 2, y + 1, 16777215);
        }
    }
}