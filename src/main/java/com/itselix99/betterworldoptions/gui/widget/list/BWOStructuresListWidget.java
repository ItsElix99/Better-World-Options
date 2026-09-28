package com.itselix99.betterworldoptions.gui.widget.list;

import com.google.common.collect.Lists;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.gui.widget.BWOButtonWidget;
import com.itselix99.betterworldoptions.gui.widget.BWOSliderWidget;
import com.itselix99.betterworldoptions.interfaces.BWOScreen;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BWOStructuresListWidget extends EntryListWidgetButtons implements BWOScreen {
    public final List<Entry> entries = Lists.newArrayList();
    private final TranslationStorage translation = TranslationStorage.getInstance();
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    public List<ButtonWidget> buttons;

    public BWOStructuresListWidget(Minecraft minecraft, int width, int height, int yStart, int yEnd, int itemHeight, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, Structure... structures) {
        super(minecraft, width, height, yStart, yEnd, itemHeight);
        this.centerAlongY = false;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.buttons = new ArrayList<>();
        if (structures != null) {
            for (int var1 = 0; var1 < structures.length; var1 += 2) {
                Structure structure = structures[var1];
                Structure structure2 = var1 < structures.length - 1 ? structures[var1 + 1] : null;
                ButtonWidget buttonWidget = this.createWidget(width / 2 - 155, structure, var1);
                ButtonWidget buttonWidget2 = this.createWidget(width / 2 - 155 + 160, structure2, var1 + 1);
                this.entries.add(new Entry(buttonWidget, buttonWidget2, bwoWorldPropertiesStorage));
            }
        }

    }

    private ButtonWidget createWidget(int x, Structure structure, int id) {
        if (structure == null) {
            return null;
        } else {
            Identifier worldType = Identifier.of(this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, ""));
            ButtonWidget button = new ButtonWidget(id, x, 0, 150, 20, structure.getName() + ": " + (this.bwoWorldPropertiesStorage.isGenerateStructure(structure.getId()) ? this.translation.get("options.on") : this.translation.get("options.off")));

            if (structure.getIncompatibleWorldTypes().contains(worldType)) {
                button.active = false;
            }

            this.buttons.add(button);
            return button;
        }
    }

    public Entry getEntry(int i) {
        return this.entries.get(i);
    }

    protected int getEntryCount() {
        return this.entries.size();
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 32;
    }

    public List<ButtonWidget> bwo_getButtonsList() {
        return this.buttons;
    }

    @Environment(EnvType.CLIENT)
    public static class Entry implements EntryListWidgetButtons.Entry {
        private final Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        private final TranslationStorage translation = TranslationStorage.getInstance();
        private final ButtonWidget left;
        private final ButtonWidget right;
        private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;

        public Entry(ButtonWidget left, ButtonWidget right, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
            this.left = left;
            this.right = right;
            this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        }

        public void tick() {
        }

        public void keyPressed(char character, int keyCode) {
        }

        public void render(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (this.left != null) {
                this.left.y = y;
                this.left.render(this.minecraft, mouseX, mouseY);
            }

            if (this.right != null) {
                this.right.y = y;
                this.right.render(this.minecraft, mouseX, mouseY);
            }
        }

        public void renderTooltip(int mouseX, int mouseY) {
        }

        public boolean mouseClicked(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY) {
            boolean clicked = false;

            if (this.left != null) {
                if (this.left.isMouseOver(this.minecraft, mouseX, mouseY)) {
                    Structure structure = Structure.getStructuresList().get(this.left.id);
                    this.bwoWorldPropertiesStorage.setGenerateStructure(structure.getId(), !this.bwoWorldPropertiesStorage.isGenerateStructure(structure.getId()));
                    this.left.text = structure.getName() + ": " + (this.bwoWorldPropertiesStorage.isGenerateStructure(structure.getId()) ? this.translation.get("options.on") : this.translation.get("options.off"));
                    clicked = true;
                }
            }

            if (this.right != null) {
                if (this.right.isMouseOver(this.minecraft, mouseX, mouseY)) {
                    Structure structure = Structure.getStructuresList().get(this.right.id);
                    this.bwoWorldPropertiesStorage.setGenerateStructure(structure.getId(), !this.bwoWorldPropertiesStorage.isGenerateStructure(structure.getId()));
                    this.right.text = structure.getName() + ": " + (this.bwoWorldPropertiesStorage.isGenerateStructure(structure.getId()) ? this.translation.get("options.on") : this.translation.get("options.off"));
                    clicked = true;
                }
            }

            return clicked;
        }


        public void mouseReleased(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY) {
            if (this.left != null) {
                this.left.mouseReleased(mouseX, mouseY);
            }

            if (this.right != null) {
                this.right.mouseReleased(mouseX, mouseY);
            }

        }

        public void renderOutOfBounds(int index, int x, int y, float tickDelta) {
        }
    }
}