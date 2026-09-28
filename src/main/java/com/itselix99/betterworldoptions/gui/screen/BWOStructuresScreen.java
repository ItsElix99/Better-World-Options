package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.gui.widget.list.BWOStructuresListWidget;
import com.itselix99.betterworldoptions.gui.widget.list.EntryListWidgetButtons;
import com.itselix99.betterworldoptions.interfaces.BWOScreen;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Identifier;

@Environment(EnvType.CLIENT)
public class BWOStructuresScreen extends Screen {
    private final Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = this.translation.get("mapFeatures.title");
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    private EntryListWidgetButtons listWidget;

    public BWOStructuresScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
    }

    @Override
    public void tick() {
        this.listWidget.tick();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.clear();
        if (this.listWidget != null) ((BWOStructuresListWidget)this.listWidget).entries.clear();
        this.buttons.add(new ButtonWidget(0, this.width / 2 - 105, 20, 100, 20, this.translation.get("mapFeatures.enableAll")));
        this.buttons.add(new ButtonWidget(1, this.width / 2 + 5, 20, 100, 20, this.translation.get("mapFeatures.disableAll")));
        this.buttons.add(new ButtonWidget(10000, this.width / 2 - 100, this.height - 27, this.translation.get("gui.done")));
        Structure[] structures;
        int i = 0;

        structures = new Structure[Structure.getStructuresList().size()];
        for (Structure structure : Structure.getStructuresList()) {
            structures[i] = structure;
            ++i;
        }

        this.listWidget = new BWOStructuresListWidget(this.minecraft, this.width, this.height, 44, this.height - 32, 25, this.bwoWorldPropertiesStorage, structures);
    }

    public void onMouseEvent() {
        super.onMouseEvent();
        this.listWidget.handleMouse();
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active) {
            Identifier worldType = Identifier.of(this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, ""));
            if (button.id == 0) {
                for (int i = 0; i < Structure.getStructuresList().size(); ++i) {
                    Structure structure = Structure.getStructuresList().get(i);
                    if (!structure.getIncompatibleWorldTypes().contains(worldType)) {
                        this.bwoWorldPropertiesStorage.setGenerateStructure(structure.getId(), true);
                    }
                    ((BWOScreen) this.listWidget).bwo_getButtonsList().get(i).text = structure.getName() + ": " + (this.bwoWorldPropertiesStorage.isGenerateStructure(structure.getId()) ? this.translation.get("options.on") : this.translation.get("options.off"));
                }
            } else if (button.id == 1) {
                for (int i = 0; i < Structure.getStructuresList().size(); ++i) {
                    Structure structure = Structure.getStructuresList().get(i);
                    if (!structure.getIncompatibleWorldTypes().contains(worldType)) {
                        this.bwoWorldPropertiesStorage.setGenerateStructure(structure.getId(), false);
                    }
                    ((BWOScreen) this.listWidget).bwo_getButtonsList().get(i).text = structure.getName() + ": " + (this.bwoWorldPropertiesStorage.isGenerateStructure(structure.getId()) ? this.translation.get("options.on") : this.translation.get("options.off"));
                }
            } else if (button.id == 10000) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    protected void keyPressed(char character, int keyCode) {
        this.listWidget.keyPressed(character, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.listWidget.mouseClicked(mouseX, mouseY, button);
    }

    protected void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        this.listWidget.mouseReleased(mouseX, mouseY, button);
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.listWidget.render(mouseX, mouseY, delta);
        this.listWidget.renderTooltip(mouseX, mouseY);
        this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get(this.title), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, delta);
    }
}