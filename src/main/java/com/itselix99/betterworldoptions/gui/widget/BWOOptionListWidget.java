package com.itselix99.betterworldoptions.gui.widget;

import com.google.common.collect.Lists;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.IntOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BWOOptionListWidget extends EntryListWidgetButtons {
    public final List<Entry> entries = Lists.newArrayList();
    private final TranslationStorage translation = TranslationStorage.getInstance();
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    private final Screen parent;

    public BWOOptionListWidget(Screen parent, Minecraft minecraft, int width, int height, int yStart, int yEnd, int itemHeight, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, OptionEntry... options) {
        super(minecraft, width, height, yStart, yEnd, itemHeight);
        this.parent = parent;
        this.centerAlongY = false;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;

        if (options != null) {
            for (int var1 = 0; var1 < options.length; var1 += 2) {
                OptionEntry option = options[var1];
                OptionEntry option2 = var1 < options.length - 1 ? options[var1 + 1] : null;
                DrawContext buttonWidget = this.createWidget(width / 2 - 155, 0, option);
                DrawContext buttonWidget2 = this.createWidget(width / 2 - 155 + 160, 0, option2);
                this.entries.add(new Entry(buttonWidget, buttonWidget2));
            }
        }

    }

    private DrawContext createWidget(int x, int y, OptionEntry option) {
        if (option == null) {
            return null;
        } else {
            int var1 = option.id;
            DrawContext button;
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
                        ((BWOButtonWidget) button).active = false;
                        ((BWOButtonWidget) button).text = this.translation.get(option.displayName) + " " + (((BooleanOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue(option.name, option.optionType)).value ? this.translation.get("options.on") : this.translation.get("options.off"));
                    }
                }

                if (booleanOption.worldTypeDefaultValue != null) {
                    for (Map.Entry<String, Boolean> entry : booleanOption.worldTypeDefaultValue.entrySet()) {
                        if (entry.getKey().equals(((StringOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value)) {
                            this.bwoWorldPropertiesStorage.setOptionValue(option.name, option.optionType, new BooleanOptionStorage(option.name, entry.getValue()));
                            ((BWOButtonWidget) button).active = false;
                            ((BWOButtonWidget) button).text = this.translation.get(option.displayName) + " " + (((BooleanOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue(option.name, option.optionType)).value ? this.translation.get("options.on") : this.translation.get("options.off"));
                        }
                    }
                }

                return button;
            } else if (option instanceof IntOptionEntry) {
                IntOptionStorage intOptionStorage = (IntOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue(option.name, option.optionType);
                button = new BWOTextFieldWidget(this.parent, this.minecraft.textRenderer, x, y, 150, 20, String.valueOf(intOptionStorage.value), option, this.bwoWorldPropertiesStorage);
                ((BWOTextFieldWidget) button).setMaxLength(32);
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
        private final DrawContext left;
        private final DrawContext right;

        public Entry(DrawContext left, DrawContext right) {
            this.left = left;
            this.right = right;
        }

        public void tick() {
            if (this.left != null && this.left instanceof BWOTextFieldWidget leftBWOTextField) {
                leftBWOTextField.tick();
            }

            if (this.right != null && this.right instanceof BWOTextFieldWidget rightBWOTextField) {
                rightBWOTextField.tick();
            }
        }

        public void keyPressed(char character, int keyCode) {
            if (this.left != null && this.left instanceof BWOTextFieldWidget leftBWOTextField) {
                leftBWOTextField.keyPressed(character, keyCode);
            }

            if (this.right != null && this.right instanceof BWOTextFieldWidget rightBWOTextField) {
                rightBWOTextField.keyPressed(character, keyCode);
            }
        }

        public void render(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (this.left != null) {
                if (this.left instanceof BWOButtonWidget leftBWOButton) {
                    leftBWOButton.y = y;
                    leftBWOButton.render(this.minecraft, mouseX, mouseY);
                } else if (this.left instanceof BWOTextFieldWidget leftBWOTextField) {
                    leftBWOTextField.y = y;
                    leftBWOTextField.render();
                }
            }

            if (this.right != null) {
                if (this.right instanceof BWOButtonWidget rightBWOButton) {
                    rightBWOButton.y = y;
                    rightBWOButton.render(this.minecraft, mouseX, mouseY);
                } else if (this.right instanceof BWOTextFieldWidget rightBWOTextField) {
                    rightBWOTextField.y = y;
                    rightBWOTextField.render();
                }
            }

        }

        public boolean mouseClicked(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY) {
            boolean clicked = false;

            if (this.left != null) {
                if (this.left instanceof BWOButtonWidget leftBWOButton && leftBWOButton.isMouseOver(this.minecraft, mouseX, mouseY)) {
                    leftBWOButton.onButtonClicked();
                    clicked = true;
                }

                if (this.left instanceof BWOTextFieldWidget leftBWOTextField) {
                    leftBWOTextField.mouseClicked(mouseX, mouseY, button);
                    clicked = true;
                }
            }

            if (this.right != null) {
                if (this.right instanceof BWOButtonWidget rightBWOButton && rightBWOButton.isMouseOver(this.minecraft, mouseX, mouseY)) {
                    rightBWOButton.onButtonClicked();
                    clicked = true;
                }

                if (this.right instanceof BWOTextFieldWidget rightBWOTextField) {
                    rightBWOTextField.mouseClicked(mouseX, mouseY, button);
                    clicked = true;
                }
            }

            return clicked;
        }


        public void mouseReleased(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY) {
            if (this.left != null) {
                if (this.left instanceof BWOButtonWidget leftBWOButton) {
                    leftBWOButton.mouseReleased(mouseX, mouseY);
                }
            }

            if (this.right != null) {
                if (this.right instanceof BWOButtonWidget rightBWOButton) {
                    rightBWOButton.mouseReleased(mouseX, mouseY);
                }
            }

        }

        public void renderOutOfBounds(int index, int x, int y, float tickDelta) {
        }
    }
}