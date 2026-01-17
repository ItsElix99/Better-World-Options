package com.itselix99.betterworldoptions.gui.widget;

import com.google.common.collect.Lists;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BWOOptionListWidget extends EntryListWidgetButtons {
    public final List<Entry> entries = Lists.newArrayList();
    private final TranslationStorage translation = TranslationStorage.getInstance();
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;

    public BWOOptionListWidget(Minecraft minecraft, int width, int height, int yStart, int yEnd, int itemHeight, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, OptionEntry... options) {
        super(minecraft, width, height, yStart, yEnd, itemHeight);
        this.centerAlongY = false;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;

        for(int var1 = 0; var1 < options.length; var1 += 2) {
            OptionEntry option = options[var1];
            OptionEntry option2 = var1 < options.length - 1 ? options[var1 + 1] : null;
            BWOButtonWidget buttonWidget = this.createWidget(width / 2 - 155, 0, option);
            BWOButtonWidget buttonWidget2 = this.createWidget(width / 2 - 155 + 160, 0, option2);
            this.entries.add(new Entry(buttonWidget, buttonWidget2));
        }

    }

    private BWOButtonWidget createWidget(int x, int y, OptionEntry option) {
        if (option == null) {
            return null;
        } else {
            int var1 = option.id;
            BWOButtonWidget button;
            if (option instanceof StringOptionEntry stringOption && stringOption.stringList != null) {
                StringOptionStorage stringOptionStorage = (StringOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue(option.name, option.optionType);
                button = new BWOButtonWidget(var1, x, y, this.translation.get(option.displayName) + " " + stringOptionStorage.value, option, this.bwoWorldPropertiesStorage);
                return button;
            } else if (option instanceof BooleanOptionEntry booleanOption) {
                BooleanOptionStorage booleanOptionStorage = (BooleanOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue(option.name, option.optionType);
                button = new BWOButtonWidget(var1, x, y, this.translation.get(option.displayName) + " " + (booleanOptionStorage.value ? this.translation.get("options.on") : this.translation.get("options.off")), option, this.bwoWorldPropertiesStorage);

                if (option.name.equals("OldFeatures")) {
                    if (!WorldTypes.getWorldTypePropertyValue(((StringOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value, "Enable Old Features")) {
                        this.bwoWorldPropertiesStorage.setOptionValue(option.name, option.optionType, new BooleanOptionStorage(option.name, false));
                        button.active = false;
                        button.text = this.translation.get(option.displayName) + " " + (((BooleanOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue(option.name, option.optionType)).value ? this.translation.get("options.on") : this.translation.get("options.off"));
                    }
                }

                if (booleanOption.worldTypeDefaultValue != null) {
                    for (Map.Entry<String, Boolean> entry : booleanOption.worldTypeDefaultValue.entrySet()) {
                        if (entry.getKey().equals(((StringOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value)) {
                            this.bwoWorldPropertiesStorage.setOptionValue(option.name, option.optionType, new BooleanOptionStorage(option.name, entry.getValue()));
                            button.active = false;
                            button.text = this.translation.get(option.displayName) + " " + (((BooleanOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue(option.name, option.optionType)).value ? this.translation.get("options.on") : this.translation.get("options.off"));
                        }
                    }
                }

                return button;
            }
        }

        return null;
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

    @Environment(EnvType.CLIENT)
    public static class Entry implements EntryListWidgetButtons.Entry {
        private final Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        private final BWOButtonWidget left;
        private final BWOButtonWidget right;

        public Entry(BWOButtonWidget left, BWOButtonWidget right) {
            this.left = left;
            this.right = right;
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

        public boolean mouseClicked(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY) {
            if (this.left != null && this.left.isMouseOver(this.minecraft, mouseX, mouseY)) {
                this.left.onButtonClicked();
                return true;
            } else if (this.right != null && this.right.isMouseOver(this.minecraft, mouseX, mouseY)) {
                this.right.onButtonClicked();
                return true;
            }

            return false;
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