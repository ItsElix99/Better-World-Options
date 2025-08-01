package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.gui.widget.WorldTypeListWidget;
import com.itselix99.betterworldoptions.gui.ScreenStateCache;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.Minecraft;

public class CreateWorldTypeScreen extends Screen {
    protected Screen parentScreen;
    protected String screenTitle = "Select World Type";
    private WorldTypeListWidget worldTypeListWidget;
    private ButtonWidget buttonSelect;
    public Minecraft mc;

    public CreateWorldTypeScreen(Screen parent) {
        this.parentScreen = parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        this.mc = this.minecraft;
        TranslationStorage translation = TranslationStorage.getInstance();
        this.worldTypeListWidget = new WorldTypeListWidget(this);
        this.worldTypeListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.buttonSelect = new ButtonWidget(0, this.width / 2 - 75, this.height - 28, 150, 20, translation.get("gui.cancel")));
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                try {
                    WorldTypeList.setWorldType(WorldTypeList.getList().get(ScreenStateCache.lastWorldType));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                this.mc.setScreen(this.parentScreen);
            } else {
                TranslationStorage var1 = TranslationStorage.getInstance();
                this.worldTypeListWidget.buttonClicked(button);
                this.buttonSelect.text = var1.get("gui.done");
            }
        }
    }

    @Override
    public void render(int var1, int var2, float var3) {
        this.worldTypeListWidget.render(var1, var2, var3);
        this.drawCenteredTextWithShadow(this.textRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.render(var1, var2, var3);
    }

    public static void onElementSelected(CreateWorldTypeScreen var0, int var1) {
        ScreenStateCache.lastWorldType = var1;
    }

    public static ButtonWidget getSelectButton(CreateWorldTypeScreen var0) {
        return var0.buttonSelect;
    }
}