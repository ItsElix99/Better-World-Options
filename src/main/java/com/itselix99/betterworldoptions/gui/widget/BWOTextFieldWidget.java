package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;

@Environment(EnvType.CLIENT)
public class BWOTextFieldWidget extends TextFieldWidget {
    private final OptionEntry option;
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;
    private final TextRenderer textRenderer;
    public int x;
    public int y;
    private final int width;
    private final int height;
    private int focusedTicks;

    public BWOTextFieldWidget(Screen parent, TextRenderer textRenderer, int x, int y, int width, int height, String text, OptionEntry option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        super(parent, textRenderer, x, y, width, height, text);
        this.textRenderer = textRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.option = option;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
    }

    public void tick() {
        ++this.focusedTicks;
    }

    public void keyPressed(char character, int keyCode) {
        if (this.enabled && this.focused) {
            if (character == 22) {
                String clipboard = Screen.getClipboard();
                if (clipboard == null) clipboard = "";

                clipboard = clipboard.replaceAll("[^0-9]", "");

                int space = 32 - this.getText().length();
                if (space > clipboard.length()) space = clipboard.length();

                if (space > 0) {
                    this.setText(this.getText() + clipboard.substring(0, space));
                }
            }

            if (keyCode == 14 && !this.getText().isEmpty()) {
                this.setText(this.getText().substring(0, this.getText().length() - 1));
            }

            if (Character.isDigit(character) && (this.getText().length() < 32)) {
                this.setText(this.getText() + character);
            }

            IntOptionEntry intOptionEntry = (IntOptionEntry) this.option;

            if (!this.getText().isEmpty()) {
                int value = Integer.parseInt(this.getText());

                if (value >= intOptionEntry.minValue && value <= intOptionEntry.maxValue) {
                    this.bwoWorldPropertiesStorage.setIntOptionValue(this.option.name, this.option.optionType, value);
                }
            }
        }
    }


    public boolean isMouseOver(Minecraft minecraft, int mouseX, int mouseY) {
        return this.enabled && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean var4 = isMouseOver(null, mouseX, mouseY);
        this.setFocused(var4);
    }

    public void setFocused(boolean focused) {
        if (focused && !this.focused) {
            this.focusedTicks = 0;
        }

        this.focused = focused;
    }

    public void render() {
        this.fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
        this.fill(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
        if (this.enabled) {
            boolean var1 = this.focused && this.focusedTicks / 6 % 2 == 0;
            this.drawTextWithShadow(this.textRenderer, this.getText() + (var1 ? "_" : ""), this.x + 4, this.y + (this.height - 8) / 2, 14737632);
        } else {
            this.drawTextWithShadow(this.textRenderer, this.getText(), this.x + 4, this.y + (this.height - 8) / 2, 7368816);
        }

    }
}