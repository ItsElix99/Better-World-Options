package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
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

    public BWOSliderWidget(int id, int x, int y, String text, Option<?> option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage, Object parent) {
        super(id, x, y, text, option, bwoWorldPropertiesStorage, parent);
        this.text = this.translation.get(this.option.getDisplayName()) + " " + this.bwoWorldPropertiesStorage.getOptionValue(this.option.getName(), this.option.getOptionType(), 0);
        if (option instanceof IntOption intOption) {
            this.minValue = intOption.getMinValue();
            this.maxValue = intOption.getMaxValue();
            this.stepsCount = (this.maxValue - this.minValue) / intOption.getStep();
            this.setValue(this.bwoWorldPropertiesStorage.getOptionValue(this.option.getName(), this.option.getOptionType(), 0));
        }
    }

    protected int getYImage(boolean hovered) {
        return 0;
    }

    public void setValue(float realValue) {
        IntOption intOption = (IntOption) this.option;

        realValue = Math.max(this.minValue, Math.min(this.maxValue, realValue));
        int stepIndex = Math.round((realValue - this.minValue) / intOption.getStep());
        stepIndex = Math.max(0, Math.min(this.stepsCount, stepIndex));
        this.value = (float) stepIndex / (float) this.stepsCount;
    }

    private void updateValueFromMouse(int mouseX) {
        IntOption intOption = (IntOption) this.option;

        float rawValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
        rawValue = Math.max(0.0F, Math.min(1.0F, rawValue));

        int stepIndex = Math.round(rawValue * this.stepsCount);
        stepIndex = Math.max(0, Math.min(this.stepsCount, stepIndex));

        this.value = (float) stepIndex / (float) this.stepsCount;
        int real = this.minValue + stepIndex * intOption.getStep();

        this.bwoWorldPropertiesStorage.setOptionValue(this.option.getName(), option.getOptionType(), real);

        this.text = this.translation.get(this.option.getDisplayName()) + " " + real;
    }

    protected void renderBackground(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.updateValueFromMouse(mouseX);
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