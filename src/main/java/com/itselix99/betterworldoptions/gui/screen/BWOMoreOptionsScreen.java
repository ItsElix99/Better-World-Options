package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.gui.widget.BWOButtonWidget;
import com.itselix99.betterworldoptions.gui.widget.BWOTextFieldWidget;
import com.itselix99.betterworldoptions.gui.widget.EntryListWidgetButtons;
import com.itselix99.betterworldoptions.gui.widget.BWOOptionListWidget;
import com.itselix99.betterworldoptions.interfaces.BWOScreen;
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
public class BWOMoreOptionsScreen extends Screen implements BWOScreen {
    private final Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = this.translation.get("bwoMoreOptions.title.generalOptions");
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    private EntryListWidgetButtons listWidget;
    private final List<String> optionsPage = new ArrayList<>(Arrays.asList("General Options", "World Type Options", "Finite World Options"));
    private int selectedPage = 0;
    public List<BWOButtonWidget> bwoButtons = new ArrayList<>();

    private BWOButtonWidget sizeButton;
    private BWOButtonWidget finiteTypeButton;
    private BWOButtonWidget shapeButton;
    private BWOTextFieldWidget sizeXField;
    private BWOTextFieldWidget sizeZField;

    private final List<int[]> sizeSquare = new ArrayList<>(Arrays.asList(new int[]{128, 128}, new int[]{256, 256}, new int[]{512, 512}, new int[]{864, 864}, new int[]{1024, 1024}, new int[]{3072, 3072}, new int[]{5120, 5120}));
    private final List<int[]> sizeLong = new ArrayList<>(Arrays.asList(new int[]{256, 64}, new int[]{512, 128}, new int[]{1024, 256}, new int[]{1728, 432}, new int[]{2048, 512}, new int[]{6144, 1536}, new int[]{10240, 2560}));

    public BWOMoreOptionsScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
    }

    @Override
    public void tick() {
        this.listWidget.tick();
        if (this.sizeXField != null) {
            this.sizeXField.tick();
        }

        if (this.sizeZField != null) {
            this.sizeZField.tick();
        }
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.clear();
        if (this.listWidget != null) ((BWOOptionListWidget)this.listWidget).entries.clear();
        this.buttons.add(new ButtonWidget(0, this.width / 2 - 155, 20, 100, 20, this.translation.get("bwoMoreOptions.button.general")));
        ButtonWidget worldTypeOptionsButton;
        this.buttons.add(worldTypeOptionsButton = new ButtonWidget(1, this.width / 2 - 50, 20, 100, 20, this.translation.get("bwoMoreOptions.button.worldType")));
        this.buttons.add(new ButtonWidget(2, this.width / 2 + 55, 20, 100, 20, this.translation.get("bwoMoreOptions.button.finiteWorld")));
        this.buttons.add(new ButtonWidget(10000, this.width / 2 - 100, this.height - 27, this.translation.get("gui.done")));
        OptionEntry[] options = null;
        int i = 0;

        WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));

        if (worldType.worldTypeOptions.isEmpty()) {
            worldTypeOptionsButton.active = false;
        }

        switch (this.optionsPage.get(this.selectedPage)) {
            case "General Options" -> {
                this.title = this.translation.get("bwoMoreOptions.title.generalOptions");
                options = new OptionEntry[GeneralOptions.getList().size()];
                for (OptionEntry generalOptions : GeneralOptions.getList()) {
                    if (generalOptions.visible) {
                        options[i] = generalOptions;
                        ++i;
                    }
                }
            }
            case "World Type Options" -> {
                this.title = this.translation.get("bwoMoreOptions.title.worldTypeOptions");
                if (!worldType.worldTypeOptions.isEmpty()) {
                    options = new OptionEntry[worldType.worldTypeOptions.size()];
                    for (OptionEntry option : worldType.worldTypeOptions.values()) {
                        if (option.visible) {
                            options[i] = option;
                            ++i;
                        }
                    }
                }
            }
            case "Finite World Options" -> {
                List<OptionEntry> generalOptions = GeneralOptions.getList();
                boolean finiteWorld = this.bwoWorldPropertiesStorage.getBooleanOptionValue(generalOptions.get(5).name, OptionType.GENERAL_OPTION);

                this.title = this.translation.get("bwoMoreOptions.title.finiteWorldOptions");
                this.buttons.add(new BWOButtonWidget(10, this.width / 2 - 155, 48, this.translation.get(generalOptions.get(5).displayName) + " " + (finiteWorld ? this.translation.get("options.on") : this.translation.get("options.off")), generalOptions.get(5), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(this.sizeButton = new BWOButtonWidget(11, this.width / 2 + 5, 48, 150, 20, this.translation.get(generalOptions.get(7).displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(generalOptions.get(7).name, OptionType.GENERAL_OPTION), generalOptions.get(7), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(this.finiteTypeButton = new BWOButtonWidget(12, this.width / 2 - 155, 73, this.translation.get(generalOptions.get(6).displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(generalOptions.get(6).name, OptionType.GENERAL_OPTION), generalOptions.get(6), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(this.shapeButton = new BWOButtonWidget(13, this.width / 2 + 5, 73, this.translation.get(generalOptions.get(8).displayName) + " " + this.bwoWorldPropertiesStorage.getStringOptionValue(generalOptions.get(8).name, OptionType.GENERAL_OPTION), generalOptions.get(8), this.bwoWorldPropertiesStorage, this));
                this.sizeXField = new BWOTextFieldWidget(this, this.minecraft.textRenderer, this.width / 2 - 155, 99, 150, 20, String.valueOf(this.bwoWorldPropertiesStorage.getIntOptionValue(generalOptions.get(9).name, OptionType.GENERAL_OPTION)), generalOptions.get(9), this.bwoWorldPropertiesStorage);
                this.sizeZField = new BWOTextFieldWidget(this, this.minecraft.textRenderer, this.width / 2 + 5, 99, 150, 20, String.valueOf(this.bwoWorldPropertiesStorage.getIntOptionValue(generalOptions.get(10).name, OptionType.GENERAL_OPTION)), generalOptions.get(10), this.bwoWorldPropertiesStorage);

                for (Object button : this.buttons) {
                    if (button instanceof BWOButtonWidget bwoButtonWidget) {
                        this.bwoButtons.add(bwoButtonWidget);
                    }
                }

                this.sizeXField.enabled = finiteWorld;
                this.sizeZField.enabled = finiteWorld;
            }
        }

        this.listWidget = new BWOOptionListWidget(this.minecraft, this.width, this.height, 44, this.height - 32, 25, this.bwoWorldPropertiesStorage, options);
    }

    public void onMouseEvent() {
        super.onMouseEvent();
        this.listWidget.handleMouse();
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active) {
            List<OptionEntry> generalOptions = GeneralOptions.getList();

            if (button instanceof BWOButtonWidget bwoButton) {
                bwoButton.onButtonClicked();
            }

            if (button.id == 0) {
                this.selectedPage = 0;
                this.init();
            } else if (button.id == 1) {
                this.selectedPage = 1;
                this.init();
            } else if (button.id == 2) {
                this.selectedPage = 2;
                this.init();
            } else if (button.id == 10) {
                boolean finiteWorld = this.bwoWorldPropertiesStorage.getBooleanOptionValue(generalOptions.get(5).name, OptionType.GENERAL_OPTION);

                if (!finiteWorld) {
                    this.sizeXField.setText(String.valueOf(((IntOptionEntry) generalOptions.get(9)).defaultValue));
                    this.sizeZField.setText(String.valueOf(((IntOptionEntry) generalOptions.get(10)).defaultValue));

                    this.sizeXField.enabled = false;
                    this.sizeZField.enabled = false;
                } else {
                    this.sizeXField.enabled = true;
                    this.sizeZField.enabled = true;
                }
            } else if (button.id == 11 || button.id == 13) {
                int selectedSize = this.bwoWorldPropertiesStorage.getSelectedValue(generalOptions.get(7).name, OptionType.GENERAL_OPTION);
                String shape = this.bwoWorldPropertiesStorage.getStringOptionValue(generalOptions.get(8).name, OptionType.GENERAL_OPTION);
                int[] size;

                if (shape.equals("Long")) {
                    size = this.sizeLong.get(selectedSize);
                } else {
                    size = this.sizeSquare.get(selectedSize);
                }

                this.bwoWorldPropertiesStorage.setIntOptionValue(generalOptions.get(9).name, OptionType.GENERAL_OPTION, size[0]);
                this.bwoWorldPropertiesStorage.setIntOptionValue(generalOptions.get(10).name, OptionType.GENERAL_OPTION, size[1]);

                this.sizeXField.setText(String.valueOf(size[0]));
                this.sizeZField.setText(String.valueOf(size[1]));
            } else if (button.id == 10000) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    protected void keyPressed(char character, int keyCode) {
        this.listWidget.keyPressed(character, keyCode);
        if (this.sizeXField != null && this.sizeXField.focused) {
            this.sizeXField.keyPressed(character, keyCode);
        } else if (this.sizeZField != null) {
            this.sizeZField.keyPressed(character, keyCode);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.listWidget.mouseClicked(mouseX, mouseY, button);
        if (this.sizeXField != null) {
            this.sizeXField.mouseClicked(mouseX, mouseY, button);
        }

        if (this.sizeZField != null) {
            this.sizeZField.mouseClicked(mouseX, mouseY, button);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        this.listWidget.mouseReleased(mouseX, mouseY, button);
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.listWidget.render(mouseX, mouseY, delta);
        this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get(this.title), this.width / 2, 5, 16777215);
        if (this.optionsPage.get(this.selectedPage).equals("Finite World Options")) {
            List<OptionEntry> generalOptions = GeneralOptions.getList();
            if (this.sizeXField != null) {
                this.sizeXField.render();
                this.drawTextWithShadow(this.textRenderer, this.translation.get(generalOptions.get(9).displayName) + " min: " + ((IntOptionEntry) generalOptions.get(9)).minValue + " max: " + ((IntOptionEntry) generalOptions.get(9)).maxValue, this.sizeXField.x, this.sizeXField.y + 25, 10526880);
            }

            if (this.sizeZField != null) {
                this.sizeZField.render();
                this.drawTextWithShadow(this.textRenderer, this.translation.get(generalOptions.get(10).displayName) + " min: " + ((IntOptionEntry) generalOptions.get(10)).minValue + " max: " + ((IntOptionEntry) generalOptions.get(10)).maxValue, this.sizeZField.x, this.sizeZField.y + 25, 10526880);
            }
        }

        super.render(mouseX, mouseY, delta);
    }

    public List<BWOButtonWidget> bwo_getBWOButtonsList() {
        return this.bwoButtons;
    }
}