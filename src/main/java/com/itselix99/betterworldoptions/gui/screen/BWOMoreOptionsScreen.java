package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.gui.widget.EntryListWidgetButtons;
import com.itselix99.betterworldoptions.gui.widget.BWOOptionListWidget;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BWOMoreOptionsScreen extends Screen {
    private final Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = this.translation.get("bwoMoreOptions.title.generalOptions");
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    private EntryListWidgetButtons listWidget;
    private final List<String> optionsPage = new ArrayList<>(Arrays.asList("General Options", "World Type Options"));
    private int selectedPage = 0;

    public BWOMoreOptionsScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.clear();
        if (this.listWidget != null) ((BWOOptionListWidget)this.listWidget).entries.clear();
        this.buttons.add(new ButtonWidget(0, this.width / 2 - 155, 20, 150, 20, this.translation.get("bwoMoreOptions.title.generalOptions")));
        ButtonWidget worldTypeOptionsButton;
        this.buttons.add(worldTypeOptionsButton = new ButtonWidget(1, this.width / 2 + 5, 20, 150, 20, this.translation.get("bwoMoreOptions.title.worldTypeOptions")));
        this.buttons.add(new ButtonWidget(10000, this.width / 2 - 100, this.height - 27, this.translation.get("gui.done")));
        OptionEntry[] options = null;
        int i = 0;

        StringOptionStorage optionStorage = (StringOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION);
        WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(optionStorage.value);

        if (worldType.worldTypeOptions == null) {
            worldTypeOptionsButton.active = false;
        }

        if (this.optionsPage.get(this.selectedPage).equals("General Options")) {
            this.title = this.translation.get("bwoMoreOptions.title.generalOptions");
            options = new OptionEntry[GeneralOptions.getList().size()];
            for (OptionEntry generalOptions : GeneralOptions.getList()) {
                if (generalOptions.visible) {
                    options[i] = generalOptions;
                    ++i;
                }
            }
        } else if (this.optionsPage.get(this.selectedPage).equals("World Type Options")) {
            this.title = this.translation.get("bwoMoreOptions.title.worldTypeOptions");
            if (worldType.worldTypeOptions != null) {
                options = new OptionEntry[worldType.worldTypeOptions.size()];
                for (OptionEntry option : worldType.worldTypeOptions.values()) {
                    if (option.visible) {
                        options[i] = option;
                        ++i;
                    }
                }
            }
        }

        if (options != null) {
            this.listWidget = new BWOOptionListWidget(this.minecraft, this.width, this.height, 44, this.height - 32, 25, this.bwoWorldPropertiesStorage, options);
        }
    }

    public void onMouseEvent() {
        super.onMouseEvent();
        this.listWidget.handleMouse();
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active) {
            if (button.id == 0) {
                this.selectedPage = 0;
                this.init();
            } else if (button.id == 1) {
                this.selectedPage = 1;
                this.init();
            } else if (button.id == 10000) {
                this.minecraft.setScreen(this.parent);
            }
        }
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
        this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get(this.title), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, delta);
    }
}