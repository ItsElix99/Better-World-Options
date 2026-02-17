package com.itselix99.betterworldoptions.gui.widget;

import com.google.common.collect.Lists;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.interfaces.BWOScreen;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BWOOptionListWidget extends EntryListWidgetButtons implements BWOScreen {
    public final List<Entry> entries = Lists.newArrayList();
    private final TranslationStorage translation = TranslationStorage.getInstance();
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    public List<BWOButtonWidget> bwoButtons;

    public BWOOptionListWidget(Minecraft minecraft, int width, int height, int yStart, int yEnd, int itemHeight, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, OptionEntry... options) {
        super(minecraft, width, height, yStart, yEnd, itemHeight);
        this.centerAlongY = false;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.bwoButtons = new ArrayList<>();
        if (options != null) {
            for (int var1 = 0; var1 < options.length; var1 += 2) {
                OptionEntry option = options[var1];
                OptionEntry option2 = var1 < options.length - 1 ? options[var1 + 1] : null;
                BWOButtonWidget buttonWidget = this.createWidget(width / 2 - 155, option);
                BWOButtonWidget buttonWidget2 = this.createWidget(width / 2 - 155 + 160, option2);
                this.entries.add(new Entry(buttonWidget, buttonWidget2));
            }
        }

    }

    private BWOButtonWidget createWidget(int x, OptionEntry option) {
        if (option == null) {
            return null;
        } else {
            int var1 = option.id;
            BWOButtonWidget button;
            if (option instanceof StringOptionEntry) {
                String stringOptionValue = this.bwoWorldPropertiesStorage.getStringOptionValue(option.name, option.optionType);
                button = new BWOButtonWidget(var1, x, 0, this.translation.get(option.displayName) + " " + stringOptionValue, option, this.bwoWorldPropertiesStorage, this);
            } else if (option instanceof BooleanOptionEntry booleanOption) {
                boolean booleanOptionValue = this.bwoWorldPropertiesStorage.getBooleanOptionValue(option.name, option.optionType);
                button = new BWOButtonWidget(var1, x, 0, this.translation.get(option.displayName) + " " + (booleanOptionValue ? this.translation.get("options.on") : this.translation.get("options.off")), option, this.bwoWorldPropertiesStorage, this);

                if (!booleanOption.worldTypeDefaultValue.isEmpty() && booleanOption.worldTypeDefaultValue.containsKey(this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION))) {
                    this.bwoWorldPropertiesStorage.setBooleanOptionValue(option.name, option.optionType, booleanOption.worldTypeDefaultValue.get(this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION)));
                    button.active = false;
                    button.text = this.translation.get(option.displayName) + " " + (this.bwoWorldPropertiesStorage.getBooleanOptionValue(option.name, option.optionType) ? this.translation.get("options.on") : this.translation.get("options.off"));
                }

            } else if (option instanceof IntOptionEntry) {
                button = new BWOSliderWidget(var1, x, 0, option.displayName, option, this.bwoWorldPropertiesStorage, this);
            } else {
                return null;
            }

            WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));

            if (option.compatibleWorldTypes.contains("Overworld")) {
                if (worldType.isDimension) {
                    this.bwoWorldPropertiesStorage.resetGeneralOptionToDefaultValue(option);
                    button.active = false;
                }
            } else if (!option.compatibleWorldTypes.contains("All") && !option.compatibleWorldTypes.contains(worldType.name)) {
                this.bwoWorldPropertiesStorage.resetGeneralOptionToDefaultValue(option);
                button.active = false;
            }

            this.bwoButtons.add(button);
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

    public List<BWOButtonWidget> bwo_getBWOButtonsList() {
        return this.bwoButtons;
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

        public void tick() {
//            if (this.left != null && this.left instanceof BWOTextFieldWidget leftBWOTextField) {
//                leftBWOTextField.tick();
//            }
//
//            if (this.right != null && this.right instanceof BWOTextFieldWidget rightBWOTextField) {
//                rightBWOTextField.tick();
//            }
        }

        public void keyPressed(char character, int keyCode) {
//            if (this.left != null && this.left instanceof BWOTextFieldWidget leftBWOTextField) {
//                leftBWOTextField.keyPressed(character, keyCode);
//            }
//
//            if (this.right != null && this.right instanceof BWOTextFieldWidget rightBWOTextField) {
//                rightBWOTextField.keyPressed(character, keyCode);
//            }
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
            if (this.left != null) {
                this.left.drawTooltip(this.minecraft, mouseX, mouseY);
            }

            if (this.right != null) {
                this.right.drawTooltip(this.minecraft, mouseX, mouseY);
            }
        }

        public boolean mouseClicked(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY) {
            boolean clicked = false;

            if (this.left != null) {
                if (this.left.isMouseOver(this.minecraft, mouseX, mouseY)) {
                    this.left.onButtonClicked();
                    clicked = true;
                }
            }

            if (this.right != null) {
                if (this.right.isMouseOver(this.minecraft, mouseX, mouseY)) {
                    this.right.onButtonClicked();
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