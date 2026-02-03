package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class BWOSliderWidget extends BWOButtonWidget {
    protected boolean dragging = false;
    private int minValue;
    private int maxValue;
    protected float value = 1.0F;

    public BWOSliderWidget(int id, int x, int y, String text, OptionEntry option, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        super(id, x, y, text, option, bwoWorldPropertiesStorage);
        this.text = this.translation.get(this.option.displayName) + " " + this.bwoWorldPropertiesStorage.getIntOptionValue(this.option.name, this.option.optionType);
        if (option instanceof IntOptionEntry intOption) {
            this.minValue = intOption.minValue;
            this.maxValue = intOption.maxValue;
            this.setValue(this.bwoWorldPropertiesStorage.getIntOptionValue(this.option.name, this.option.optionType));
        }
    }

    protected int getYImage(boolean hovered) {
        return 0;
    }

    private void setValue(float realValue) {
        realValue = Math.max(this.minValue, Math.min(this.maxValue, realValue));
        this.value = (realValue - this.minValue) / (this.maxValue - this.minValue);
    }

    private float getRealValue() {
        float real = this.minValue + this.value * (this.maxValue - this.minValue);
        return Math.round(real);
    }

    private void updateValueFromMouse(Minecraft minecraft, int mouseX) {
        this.value = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
        this.value = Math.max(0.0F, Math.min(1.0F, this.value));

        float real = getRealValue();

        this.bwoWorldPropertiesStorage.setIntOptionValue(this.option.name, option.optionType, (int) real);

        this.text = this.translation.get(this.option.displayName) + " " + this.bwoWorldPropertiesStorage.getIntOptionValue(this.option.name, this.option.optionType);
    }

    protected void renderBackground(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                updateValueFromMouse(minecraft, mouseX);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexture(this.x + (int)(this.value * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexture(this.x + (int)(this.value * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    public boolean isMouseOver(Minecraft minecraft, int mouseX, int mouseY) {
        if (super.isMouseOver(minecraft, mouseX, mouseY)) {
            updateValueFromMouse(minecraft, mouseX);
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }
}