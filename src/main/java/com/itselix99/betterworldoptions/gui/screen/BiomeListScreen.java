package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.BiomeProperties;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BiomeListScreen extends Screen {
    protected Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = this.translation.get("selectBiome.title");
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    private final WorldType worldType;
    private final BiomeProperties biomeProperties;

    private BiomeListWidget biomeListWidget;
    private ButtonWidget doneButton;
    private ButtonWidget allBiomesButton;
    private Biome selectedBiome;

    public BiomeListScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.worldType = WorldType.getWorldTypeById(Identifier.of(bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "")));
        this.biomeProperties = this.worldType.getBiomeProperties();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.biomeListWidget = new BiomeListWidget(this);
        this.biomeListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.doneButton = new ButtonWidget(0, this.width / 2 + 5, this.height - 28, 150, 20, this.translation.get("gui.cancel")));
        this.buttons.add(this.allBiomesButton = new ButtonWidget(1, this.width / 2 - 155, this.height - 28, 150, 20, this.translation.get("selectBiome.allBiomes")));

        String currentBiome = this.bwoWorldPropertiesStorage.getOptionValue("SingleBiome", OptionType.GENERAL_OPTION, "");
        Biome biome = this.biomeProperties.getAllBiomesForSingleBiome().stream().filter(biome1 -> biome1.name.equals(currentBiome)).findFirst().orElse(null);
        if (!currentBiome.equals("Off") && biome != null) {
            selectedBiome = biome;
        } else {
            this.allBiomesButton.active = false;
        }
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.minecraft.setScreen(this.parent);
            } else if (button.id == 1) {
                String currentBiome = this.bwoWorldPropertiesStorage.getOptionValue("SingleBiome", OptionType.GENERAL_OPTION, "");
                if (!currentBiome.equals("Off")) {
                    this.bwoWorldPropertiesStorage.setOptionValue("SingleBiome", OptionType.GENERAL_OPTION, "Off");
                    selectedBiome = null;
                    button.active = false;
                    this.doneButton.text = this.translation.get("gui.done");
                }
            }
        }
    }

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
            List<Biome> var1 = BiomeListScreen.this.biomeProperties.getAllBiomesForSingleBiome();
            return var1.size();
        }

        protected void entryClicked(int index, boolean doubleClick) {
            List<Biome> var3 = BiomeListScreen.this.biomeProperties.getAllBiomesForSingleBiome();
            BiomeListScreen.this.selectBiome(var3.get(index));

            String currentBiome = BiomeListScreen.this.bwoWorldPropertiesStorage.getOptionValue("SingleBiome", OptionType.GENERAL_OPTION, "");
            if (!var3.get(index).name.equals(currentBiome)) {
                BiomeListScreen.this.bwoWorldPropertiesStorage.setOptionValue("SingleBiome", OptionType.GENERAL_OPTION, var3.get(index).name);
                BiomeListScreen.this.allBiomesButton.active = true;
                BiomeListScreen.this.doneButton.text = BiomeListScreen.this.translation.get("gui.done");
            }
        }

        protected boolean isSelectedEntry(int index) {
            List<Biome> var2 = BiomeListScreen.this.biomeProperties.getAllBiomesForSingleBiome();
            return BiomeListScreen.this.selectedBiome == var2.get(index);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 13;
        }

        protected void renderBackground() {
            BiomeListScreen.this.renderBackground();
        }

        protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
            Biome var6 = BiomeListScreen.this.biomeProperties.getAllBiomesForSingleBiome().get(index);
            BiomeListScreen.this.drawCenteredTextWithShadow(BiomeListScreen.this.textRenderer, var6.name, BiomeListScreen.this.width / 2, y + 1, 16777215);
        }
    }
}