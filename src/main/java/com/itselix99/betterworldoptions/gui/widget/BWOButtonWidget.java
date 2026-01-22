package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

@Environment(EnvType.CLIENT)
public class BWOButtonWidget extends ButtonWidget {
    private final TranslationStorage translation = TranslationStorage.getInstance();
    private final OptionEntry option;
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    public int selected;

    public BWOButtonWidget(int id, int x, int y, int width, int height, String text, OptionEntry option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        super(id, x, y, width, height, text);
        this.option = option;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        if (option instanceof StringOptionEntry stringOption && stringOption.stringList != null) {
            this.selected = bwoWorldPropertiesStorage.getSelectedValue(option.name, option.optionType);
        }
    }

    public BWOButtonWidget(int id, int x, int y, String text, OptionEntry option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        super(id, x, y, 150, 20, text);
        this.option = option;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        if (option instanceof StringOptionEntry stringOption && stringOption.stringList != null) {
            this.selected = bwoWorldPropertiesStorage.getSelectedValue(option.name, option.optionType);
        }
    }

    public void onButtonClicked() {
        if (this.option instanceof StringOptionEntry stringOption && stringOption.stringList != null) {
            this.selected = (this.selected + 1) % stringOption.stringList.size();
            this.bwoWorldPropertiesStorage.setSelectedValue(this.option.name, this.option.optionType, this.selected);
            this.bwoWorldPropertiesStorage.setStringOptionValue(this.option.name, this.option.optionType, stringOption.stringList.get(this.bwoWorldPropertiesStorage.getSelectedValue(this.option.name, this.option.optionType)));
            this.text = this.translation.get(this.option.displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(this.option.name, this.option.optionType);
        } else if (this.option instanceof BooleanOptionEntry) {
            boolean booleanOptionValue = this.bwoWorldPropertiesStorage.getBooleanOptionValue(this.option.name, this.option.optionType);
            this.bwoWorldPropertiesStorage.setBooleanOptionValue(this.option.name, this.option.optionType, !booleanOptionValue);
            this.text  = this.translation.get(this.option.displayName) + " " + (this.bwoWorldPropertiesStorage.getBooleanOptionValue(this.option.name, this.option.optionType) ? this.translation.get("options.on") : this.translation.get("options.off"));
        }
    }
}