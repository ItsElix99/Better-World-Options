package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class BWOSliderWidget extends BWOButtonWidget {
    protected boolean dragging = false;
    private int minValue;
    private int maxValue;
    protected float value = 1.0F;
    private int stepsCount;

    public BWOSliderWidget(int id, int x, int y, String text, OptionEntry option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, Object parent) {
        super(id, x, y, text, option, bwoWorldPropertiesStorage, parent);
        this.text = this.translation.get(this.option.displayName) + " " + this.bwoWorldPropertiesStorage.getIntOptionValue(this.option.name, this.option.optionType);
        if (option instanceof IntOptionEntry intOption) {
            this.minValue = intOption.minValue;
            this.maxValue = intOption.maxValue;
            this.stepsCount = (this.maxValue - this.minValue) / intOption.step;
            this.setValue(this.bwoWorldPropertiesStorage.getIntOptionValue(this.option.name, this.option.optionType));
        }
    }

    protected int getYImage(boolean hovered) {
        return 0;
    }

    public void setValue(float realValue) {
        IntOptionEntry intOption = (IntOptionEntry) this.option;

        realValue = Math.max(this.minValue, Math.min(this.maxValue, realValue));
        int stepIndex = Math.round((realValue - this.minValue) / intOption.step);
        stepIndex = Math.max(0, Math.min(this.stepsCount, stepIndex));
        this.value = (float) stepIndex / (float) this.stepsCount;
    }

    private void updateValueFromMouse(int mouseX) {
        IntOptionEntry intOption = (IntOptionEntry) this.option;

        float rawValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
        rawValue = Math.max(0.0F, Math.min(1.0F, rawValue));

        int stepIndex = Math.round(rawValue * this.stepsCount);
        stepIndex = Math.max(0, Math.min(this.stepsCount, stepIndex));

        this.value = (float) stepIndex / (float) this.stepsCount;
        int real = this.minValue + stepIndex * intOption.step;

        this.bwoWorldPropertiesStorage.setIntOptionValue(this.option.name, option.optionType, real);

        this.text = this.translation.get(this.option.displayName) + " " + real;
    }

    protected void renderBackground(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                updateValueFromMouse(mouseX);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexture(this.x + (int)(this.value * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexture(this.x + (int)(this.value * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    public void onButtonClicked() {
        if (Mouse.isButtonDown(0)) {
            this.dragging = true;
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        if (!Mouse.isButtonDown(0)) {
            this.dragging = false;
        }
    }
}