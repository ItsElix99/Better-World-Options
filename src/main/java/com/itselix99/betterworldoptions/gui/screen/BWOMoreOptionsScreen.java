package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.gui.widget.*;
import com.itselix99.betterworldoptions.interfaces.BWOScreen;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Identifier;

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

    private final WorldType worldType;

    private BWOSliderWidget sizeXSlider;
    private BWOSliderWidget sizeZSlider;

    private final List<int[]> sizeSquare = new ArrayList<>(Arrays.asList(new int[]{128, 128}, new int[]{256, 256}, new int[]{512, 512}, new int[]{864, 864}, new int[]{1024, 1024}, new int[]{3072, 3072}, new int[]{5120, 5120}));
    private final List<int[]> sizeLong = new ArrayList<>(Arrays.asList(new int[]{256, 64}, new int[]{512, 128}, new int[]{1024, 256}, new int[]{1728, 432}, new int[]{2048, 512}, new int[]{6144, 1536}, new int[]{10240, 2560}));

    public BWOMoreOptionsScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
        this.worldType = WorldType.getWorldTypeById(Identifier.of(this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "")));
    }

    @Override
    public void tick() {
        this.listWidget.tick();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.buttons.clear();
        if (this.listWidget != null) ((BWOOptionListWidget)this.listWidget).entries.clear();
        this.buttons.add(new ButtonWidget(0, this.width / 2 - 155, 20, 100, 20, this.translation.get("bwoMoreOptions.button.general")));
        ButtonWidget worldTypeOptionsButton;
        this.buttons.add(worldTypeOptionsButton = new ButtonWidget(1, this.width / 2 - 50, 20, 100, 20, this.translation.get("bwoMoreOptions.button.worldType")));
        ButtonWidget finiteWorldOptionsButton;
        this.buttons.add(finiteWorldOptionsButton = new ButtonWidget(2, this.width / 2 + 55, 20, 100, 20, this.translation.get("bwoMoreOptions.button.finiteWorld")));
        this.buttons.add(new ButtonWidget(10000, this.width / 2 - 100, this.height - 27, this.translation.get("gui.done")));
        Option<?>[] options = null;
        int i = 0;


        if (this.worldType.getWorldTypeOptions().isEmpty()) {
            worldTypeOptionsButton.active = false;
        }

        if (this.worldType.isDimension()) {
            finiteWorldOptionsButton.active = false;
        }

        switch (this.optionsPage.get(this.selectedPage)) {
            case "General Options" -> {
                this.title = this.translation.get("bwoMoreOptions.title.generalOptions");
                options = new Option[GeneralOptions.getGeneralOptionsList().size()];
                for (Option<?> generalOptions : GeneralOptions.getGeneralOptionsList()) {
                    if (generalOptions.isVisible()) {
                        options[i] = generalOptions;
                        ++i;
                    }
                }
            }
            case "World Type Options" -> {
                this.title = this.translation.get("bwoMoreOptions.title.worldTypeOptions");
                if (!this.worldType.getWorldTypeOptions().isEmpty()) {
                    options = new Option[this.worldType.getWorldTypeOptions().size()];
                    for (Option<?> option : this.worldType.getWorldTypeOptions().values()) {
                        if (option.isVisible()) {
                            options[i] = option;
                            ++i;
                        }
                    }
                }
            }
            case "Finite World Options" -> {
                List<Option<?>> generalOptions = GeneralOptions.getGeneralOptionsList();
                boolean finiteWorld = this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(5).getName(), OptionType.GENERAL_OPTION, false);

                this.title = this.translation.get("bwoMoreOptions.title.finiteWorldOptions");
                this.buttons.add(new BWOButtonWidget(10, this.width / 2 - 155, 48, this.translation.get(generalOptions.get(5).getDisplayName()) + " " + (finiteWorld ? this.translation.get("options.on") : this.translation.get("options.off")), generalOptions.get(5), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(new BWOButtonWidget(11, this.width / 2 + 5, 48, 150, 20, this.translation.get(generalOptions.get(7).getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(7).getName(), OptionType.GENERAL_OPTION, 0), generalOptions.get(7), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(new BWOButtonWidget(12, this.width / 2 - 155, 73, this.translation.get(generalOptions.get(6).getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(6).getName(), OptionType.GENERAL_OPTION, ""), generalOptions.get(6), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(new BWOButtonWidget(13, this.width / 2 + 5, 73, this.translation.get(generalOptions.get(8).getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(8).getName(), OptionType.GENERAL_OPTION, ""), generalOptions.get(8), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(this.sizeXSlider = new BWOSliderWidget(14, this.width / 2 - 155, 98, this.translation.get(generalOptions.get(9).getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(9).getName(), OptionType.GENERAL_OPTION, 0), generalOptions.get(9), this.bwoWorldPropertiesStorage, this));
                this.buttons.add(this.sizeZSlider = new BWOSliderWidget(15, this.width / 2 + 5, 98, this.translation.get(generalOptions.get(10).getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(10).getName(), OptionType.GENERAL_OPTION, 0), generalOptions.get(10), this.bwoWorldPropertiesStorage, this));

                for (Object button : this.buttons) {
                    if (button instanceof BWOButtonWidget bwoButtonWidget) {
                        this.bwoButtons.add(bwoButtonWidget);
                    }
                }

                this.sizeXSlider.active = finiteWorld;
                this.sizeZSlider.active = finiteWorld;
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
            List<Option<?>> generalOptions = GeneralOptions.getGeneralOptionsList();

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
                boolean finiteWorld = this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(5).getName(), OptionType.GENERAL_OPTION, false);

                if (!finiteWorld) {
                    this.sizeXSlider.text = this.translation.get(generalOptions.get(9).getDisplayName()) + " " +  ((IntOption) generalOptions.get(9)).getDefaultValue();
                    this.sizeZSlider.text = this.translation.get(generalOptions.get(10).getDisplayName()) + " " + ((IntOption) generalOptions.get(10)).getDefaultValue();

                    this.sizeXSlider.active = false;
                    this.sizeZSlider.active = false;
                } else {
                    this.sizeXSlider.active = true;
                    this.sizeZSlider.active = true;
                }
            } else if (button.id == 11 || button.id == 13) {
                int selectedSize = this.bwoWorldPropertiesStorage.getSelectedValue(generalOptions.get(7).getName(), OptionType.GENERAL_OPTION);
                String shape = this.bwoWorldPropertiesStorage.getOptionValue(generalOptions.get(8).getName(), OptionType.GENERAL_OPTION, "");
                int[] size;

                if (shape.equals("Long")) {
                    size = this.sizeLong.get(selectedSize);
                } else {
                    size = this.sizeSquare.get(selectedSize);
                }

                this.bwoWorldPropertiesStorage.setOptionValue(generalOptions.get(9).getName(), OptionType.GENERAL_OPTION, size[0]);
                this.bwoWorldPropertiesStorage.setOptionValue(generalOptions.get(10).getName(), OptionType.GENERAL_OPTION, size[1]);

                this.sizeXSlider.text = this.translation.get(generalOptions.get(9).getDisplayName()) + " " + size[0];
                this.sizeZSlider.text = this.translation.get(generalOptions.get(10).getDisplayName()) + " " + size[1];

                this.sizeXSlider.setValue(size[0]);
                this.sizeZSlider.setValue(size[1]);
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

        if (this.optionsPage.get(this.selectedPage).equals("Finite World Options")) {
            boolean finiteWorld = this.bwoWorldPropertiesStorage.getOptionValue("FiniteWorld", OptionType.GENERAL_OPTION, false);
            int width = this.bwoWorldPropertiesStorage.getOptionValue("Width", OptionType.GENERAL_OPTION, 0);
            int length = this.bwoWorldPropertiesStorage.getOptionValue("Length", OptionType.GENERAL_OPTION, 0);

            if (this.worldType.isPregenerateFiniteWorld() && finiteWorld && width * length * 64 > 268435456) {
                this.drawCenteredTextWithShadow(this.textRenderer, this.translation.get("bwoMoreOptions.pregenerateFiniteWorld.info"), this.width / 2, 122, 16777215);
            }
        }

        super.render(mouseX, mouseY, delta);
    }

    public List<BWOButtonWidget> bwo_getBWOButtonsList() {
        return this.bwoButtons;
    }
}