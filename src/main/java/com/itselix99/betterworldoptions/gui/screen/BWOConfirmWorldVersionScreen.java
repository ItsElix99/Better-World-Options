package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;

@Environment(EnvType.CLIENT)
public class BWOConfirmWorldVersionScreen extends ConfirmScreen {
    private final Minecraft minecraft;
    private final String leftButtonText;
    private final String rightButtonText;
    private final String worldName;
    private final String name;
    private final long seed;

    public BWOConfirmWorldVersionScreen(Screen parent, Minecraft minecraft, String message1, String message2, String leftButtonText, String rightButtonText, String worldName, String name, long seed) {
        super(parent, message1, message2, leftButtonText, rightButtonText, 0);
        this.minecraft = minecraft;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
        this.worldName = worldName;
        this.name = name;
        this.seed = seed;
    }

    public void init() {
        this.buttons.add(new OptionButtonWidget(0, this.width / 2 - 155 + 0, this.height / 6 + 96, this.leftButtonText));
        this.buttons.add(new OptionButtonWidget(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.rightButtonText));
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.id == 0) {
            BWOWorldPropertiesStorage.BWOWorldVersion = "0.2.0";
        } else if (button.id == 1) {
            BWOWorldPropertiesStorage.BWOWorldVersion = "0.3.0";
        }

        this.minecraft.startGame(this.worldName, this.name, this.seed);
        this.minecraft.setScreen(null);
    }
}