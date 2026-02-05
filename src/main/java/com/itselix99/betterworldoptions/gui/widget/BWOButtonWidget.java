package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.interfaces.BWOScreen;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BWOButtonWidget extends ButtonWidget {
    protected final Object parent;
    protected final TranslationStorage translation = TranslationStorage.getInstance();
    public final OptionEntry option;
    protected final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    public int selected;

    private long tooltipHoverStart = -1L;
    private boolean tooltipVisible = false;
    private static final long TOOLTIP_DELAY_MS = 1000L;

    public BWOButtonWidget(int id, int x, int y, int width, int height, String text, OptionEntry option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, Object parent) {
        super(id, x, y, width, height, text);
        this.option = option;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.parent = parent;
        if (option instanceof StringOptionEntry stringOption) {
            String worldType = this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
            if (stringOption.optionType == OptionType.GENERAL_OPTION) {
                if (!stringOption.worldTypeDefaultValue.isEmpty() && stringOption.worldTypeDefaultValue.containsKey(worldType)) {
                    this.bwoWorldPropertiesStorage.resetGeneralOptionToDefaultValue(option);
                } else {
                    this.selected = (this.selected) % stringOption.stringList.size();
                    this.bwoWorldPropertiesStorage.setSelectedValue(this.option.name, this.option.optionType, (this.bwoWorldPropertiesStorage.getSelectedValue(option.name, option.optionType)) % stringOption.stringList.size());
                    this.bwoWorldPropertiesStorage.setStringOptionValue(this.option.name, this.option.optionType, stringOption.stringList.get(this.bwoWorldPropertiesStorage.getSelectedValue(this.option.name, this.option.optionType)));
                }

                this.text = this.translation.get(this.option.displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(this.option.name, this.option.optionType);
            }

            this.selected = bwoWorldPropertiesStorage.getSelectedValue(option.name, option.optionType);
        }

        if (option.parentOption instanceof BooleanOptionEntry && !this.bwoWorldPropertiesStorage.getBooleanOptionValue(option.parentOption.name, option.parentOption.optionType)) {
            this.active = false;
        }
    }

    public BWOButtonWidget(int id, int x, int y, String text, OptionEntry option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, Object parent) {
        super(id, x, y, 150, 20, text);
        this.option = option;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.parent = parent;
        if (option instanceof StringOptionEntry stringOption) {
            String worldType = this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
            if (stringOption.optionType == OptionType.GENERAL_OPTION) {
                if (!stringOption.worldTypeDefaultValue.isEmpty() && stringOption.worldTypeDefaultValue.containsKey(worldType)) {
                    this.bwoWorldPropertiesStorage.resetGeneralOptionToDefaultValue(option);
                } else {
                    this.selected = (this.selected) % stringOption.stringList.size();
                    this.bwoWorldPropertiesStorage.setSelectedValue(this.option.name, this.option.optionType, (this.bwoWorldPropertiesStorage.getSelectedValue(option.name, option.optionType)) % stringOption.stringList.size());
                    this.bwoWorldPropertiesStorage.setStringOptionValue(this.option.name, this.option.optionType, stringOption.stringList.get(this.bwoWorldPropertiesStorage.getSelectedValue(this.option.name, this.option.optionType)));
                }

                this.text = this.translation.get(this.option.displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(this.option.name, this.option.optionType);
            }

            this.selected = bwoWorldPropertiesStorage.getSelectedValue(option.name, option.optionType);
        }

        if (option.parentOption instanceof BooleanOptionEntry && !this.bwoWorldPropertiesStorage.getBooleanOptionValue(option.parentOption.name, option.parentOption.optionType)) {
            this.active = false;
        }
    }

    public void onButtonClicked() {
        if (this.option instanceof StringOptionEntry stringOption) {
            List<String> stringList = stringOption.stringList;
            String worldType = this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
            if (stringOption.optionType == OptionType.GENERAL_OPTION && !stringOption.worldTypeDefaultValue.isEmpty() && stringOption.worldTypeDefaultValue.containsKey(worldType)) {
                stringList = stringOption.worldTypeDefaultValue.get(worldType);
            }

            this.selected = (this.selected + 1) % stringList.size();
            this.bwoWorldPropertiesStorage.setSelectedValue(this.option.name, this.option.optionType, this.selected);
            this.bwoWorldPropertiesStorage.setStringOptionValue(this.option.name, this.option.optionType, stringList.get(this.bwoWorldPropertiesStorage.getSelectedValue(this.option.name, this.option.optionType)));
            this.text = this.translation.get(this.option.displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(this.option.name, this.option.optionType);
        } else if (this.option instanceof BooleanOptionEntry) {
            boolean booleanOptionValue = this.bwoWorldPropertiesStorage.getBooleanOptionValue(this.option.name, this.option.optionType);
            this.bwoWorldPropertiesStorage.setBooleanOptionValue(this.option.name, this.option.optionType, !booleanOptionValue);
            this.text  = this.translation.get(this.option.displayName) + " " + (this.bwoWorldPropertiesStorage.getBooleanOptionValue(this.option.name, this.option.optionType) ? this.translation.get("options.on") : this.translation.get("options.off"));

            if (!this.bwoWorldPropertiesStorage.getBooleanOptionValue(this.option.name, this.option.optionType) && !this.option.dependentOptions.isEmpty()) {
                this.bwoWorldPropertiesStorage.resetDependentOptionsToDefaultValue(this.option);

                if (this.parent instanceof BWOScreen bwoScreen) {
                    for (BWOButtonWidget bwoButtonWidget : bwoScreen.bwo_getBWOButtonsList()) {
                        if (this.option.dependentOptions.contains(bwoButtonWidget.option)) {
                            bwoButtonWidget.active = false;

                            if (bwoButtonWidget.option instanceof StringOptionEntry) {
                                bwoButtonWidget.text = this.translation.get(bwoButtonWidget.option.displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(bwoButtonWidget.option.name, bwoButtonWidget.option.optionType);
                                bwoButtonWidget.selected = this.bwoWorldPropertiesStorage.getSelectedValue(bwoButtonWidget.option.name, bwoButtonWidget.option.optionType);
                            } else if (bwoButtonWidget.option instanceof BooleanOptionEntry) {
                                bwoButtonWidget.text = this.translation.get(bwoButtonWidget.option.displayName) + " " + (this.bwoWorldPropertiesStorage.getBooleanOptionValue(bwoButtonWidget.option.name, bwoButtonWidget.option.optionType) ? this.translation.get("options.on") : this.translation.get("options.off"));
                            } else if (bwoButtonWidget.option instanceof IntOptionEntry) {
                                bwoButtonWidget.text = this.translation.get(bwoButtonWidget.option.displayName) + " " + this.bwoWorldPropertiesStorage.getIntOptionValue(bwoButtonWidget.option.name, bwoButtonWidget.option.optionType);
                            }
                        }
                    }
                }
            } else if (!this.option.dependentOptions.isEmpty()) {
                if (this.parent instanceof BWOScreen bwoScreen) {
                    for (BWOButtonWidget bwoButtonWidget : bwoScreen.bwo_getBWOButtonsList()) {
                        if (this.option.dependentOptions.contains(bwoButtonWidget.option)) {
                            bwoButtonWidget.active = true;
                        }
                    }
                }
            }
        }
    }

    public void drawTooltip(Minecraft minecraft, int mouseX, int mouseY) {
        boolean hovered = this.isMouseOver(minecraft, mouseX, mouseY);

        if (!hovered) {
            this.tooltipHoverStart = -1L;
            this.tooltipVisible = false;
            return;
        }

        long now = System.currentTimeMillis();

        if (this.tooltipHoverStart == -1L) {
            this.tooltipHoverStart = now;
            return;
        }

        if (!this.tooltipVisible) {
            if (now - this.tooltipHoverStart < TOOLTIP_DELAY_MS) {
                return;
            }
            this.tooltipVisible = true;
        }

        if (this.visible && this.option.description != null) {
            String[] lines = new String[this.option.description.length];
            int maxWidth = 0;

            for (int i = 0; i < this.option.description.length; i++) {
                lines[i] = this.translation.get(this.option.description[i]);
                maxWidth = Math.max(maxWidth, minecraft.textRenderer.getWidth(lines[i]));
            }

            int lineHeight = 10;
            int padding = 2;
            int tooltipHeight = lines.length * lineHeight + padding;

            int x = mouseX + 12;
            int y = mouseY - 12;

            this.fillGradient(x - 3, y - 4, x + maxWidth + padding, y + tooltipHeight, -1073741824, -1073741824);

            this.fillGradient(x - 3, y - 4, x + maxWidth + padding, y - 3, 1347420415, 1347420415);
            this.fillGradient(x - 3, y + tooltipHeight - 1, x + maxWidth + padding, y + tooltipHeight, 1347420415, 1347420415);
            this.fillGradient(x - 4, y - 4, x - 3, y + tooltipHeight, 1347420415, 1347420415);
            this.fillGradient(x + maxWidth + padding, y - 4, x + maxWidth + padding + 1, y + tooltipHeight, 1347420415, 1347420415);

            for (int i = 0; i < lines.length; i++) {
                minecraft.textRenderer.drawWithShadow(lines[i], x, y + i * lineHeight, 16777215);
            }
        }
    }
}