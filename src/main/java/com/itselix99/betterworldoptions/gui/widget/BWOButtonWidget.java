package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.interfaces.BWOScreen;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BWOButtonWidget extends ButtonWidget {
    protected final Object parent;
    protected final TranslationStorage translation = TranslationStorage.getInstance();
    public final Option<?> option;
    protected final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    public int selected;

    private long tooltipHoverStart = -1L;
    private boolean tooltipVisible = false;
    private static final long TOOLTIP_DELAY_MS = 1000L;

    public BWOButtonWidget(int id, int x, int y, int width, int height, String text, Option<?> option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, Object parent) {
        super(id, x, y, width, height, text);
        this.option = option;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.parent = parent;

        if (option instanceof StringOption stringOption) {
            String worldType = this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");

            if (stringOption.getOptionType() == OptionType.GENERAL_OPTION) {
                if (!stringOption.getWorldTypeValues().isEmpty()) {
                    if (stringOption.getValues(Identifier.of(worldType)).size() < 2) this.active = false;
                }
            }

            this.selected = this.bwoWorldPropertiesStorage.getSelectedValue(option.getName(), option.getOptionType());
        }

        if (option.getParentOption() instanceof BooleanOption && !this.bwoWorldPropertiesStorage.getOptionValue(option.getParentOption().getName(), option.getParentOption().getOptionType(), false)) {
            this.active = false;
        }
    }

    public BWOButtonWidget(int id, int x, int y, String text, Option<?> option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, Object parent) {
        super(id, x, y, 150, 20, text);
        this.option = option;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.parent = parent;

        if (option instanceof StringOption stringOption) {
            String worldType = this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");

            if (stringOption.getOptionType() == OptionType.GENERAL_OPTION) {
                if (!stringOption.getWorldTypeValues().isEmpty()) {
                    if (stringOption.getValues(Identifier.of(worldType)).size() < 2) this.active = false;
                }
            }

            this.selected = this.bwoWorldPropertiesStorage.getSelectedValue(option.getName(), option.getOptionType());
        }

        if (option.getParentOption() instanceof BooleanOption && !this.bwoWorldPropertiesStorage.getOptionValue(option.getParentOption().getName(), option.getParentOption().getOptionType(), false)) {
            this.active = false;
        }
    }

    public void onButtonClicked() {
        String worldType = this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");

        if (this.option instanceof StringOption stringOption) {
            List<String> stringList = stringOption.getValues(Identifier.of(worldType));

            this.selected = (this.selected + 1) % stringList.size();
            this.bwoWorldPropertiesStorage.setSelectedValue(this.option.getName(), this.option.getOptionType(), this.selected);
            this.bwoWorldPropertiesStorage.setOptionValue(this.option.getName(), this.option.getOptionType(), stringList.get(this.bwoWorldPropertiesStorage.getSelectedValue(this.option.getName(), this.option.getOptionType())));
            this.text = this.translation.get(this.option.getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(this.option.getName(), this.option.getOptionType(), "");
        } else if (this.option instanceof BooleanOption) {
            boolean booleanOptionValue = this.bwoWorldPropertiesStorage.getOptionValue(this.option.getName(), this.option.getOptionType(), false);
            this.bwoWorldPropertiesStorage.setOptionValue(this.option.getName(), this.option.getOptionType(), !booleanOptionValue);
            this.text  = this.translation.get(this.option.getDisplayName()) + " " + (this.bwoWorldPropertiesStorage.getOptionValue(this.option.getName(), this.option.getOptionType(), false) ? this.translation.get("options.on") : this.translation.get("options.off"));

            if (!this.bwoWorldPropertiesStorage.getOptionValue(this.option.getName(), this.option.getOptionType(), false) && !this.option.getDependentOptions().isEmpty()) {
                this.bwoWorldPropertiesStorage.resetDependentOptionsToDefaultValue(this.option);

                if (this.parent instanceof BWOScreen bwoScreen) {
                    for (BWOButtonWidget bwoButtonWidget : bwoScreen.bwo_getBWOButtonsList()) {
                        if (this.option.getDependentOptions().contains(bwoButtonWidget.option)) {
                            bwoButtonWidget.active = false;

                            if (bwoButtonWidget.option instanceof StringOption) {
                                bwoButtonWidget.text = this.translation.get(bwoButtonWidget.option.getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(bwoButtonWidget.option.getName(), bwoButtonWidget.option.getOptionType(), "");
                                bwoButtonWidget.selected = this.bwoWorldPropertiesStorage.getSelectedValue(bwoButtonWidget.option.getName(), bwoButtonWidget.option.getOptionType());
                            } else if (bwoButtonWidget.option instanceof BooleanOption) {
                                bwoButtonWidget.text = this.translation.get(bwoButtonWidget.option.getDisplayName()) + " " + (this.bwoWorldPropertiesStorage.getOptionValue(bwoButtonWidget.option.getName(), bwoButtonWidget.option.getOptionType(), false) ? this.translation.get("options.on") : this.translation.get("options.off"));
                            } else if (bwoButtonWidget.option instanceof IntOption) {
                                bwoButtonWidget.text = this.translation.get(bwoButtonWidget.option.getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(bwoButtonWidget.option.getName(), bwoButtonWidget.option.getOptionType(), 0);
                            }
                        }
                    }
                }
            } else if (!this.option.getDependentOptions().isEmpty()) {
                if (this.parent instanceof BWOScreen bwoScreen) {
                    for (BWOButtonWidget bwoButtonWidget : bwoScreen.bwo_getBWOButtonsList()) {
                        if (this.option.getDependentOptions().contains(bwoButtonWidget.option)) {
                            if (bwoButtonWidget.option instanceof StringOption stringOption && stringOption.getValues(Identifier.of(worldType)).size() < 2) continue;

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

        if (this.visible && this.option.getDescription() != null) {
            String[] lines = new String[this.option.getDescription().length];
            int maxWidth = 0;

            for (int i = 0; i < this.option.getDescription().length; i++) {
                lines[i] = this.translation.get(this.option.getDescription()[i]);
                maxWidth = Math.max(maxWidth, minecraft.textRenderer.getWidth(lines[i]));
            }

            int lineHeight = 10;
            int padding = 2;
            int tooltipHeight = lines.length * lineHeight + padding;

            int x = mouseX + 12;
            int y = mouseY + 12;

            if (this.parent instanceof ListWidget listWidget) {
                if (x > listWidget.width / 2) {
                    x = mouseX - 6 - maxWidth;
                }
            }

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