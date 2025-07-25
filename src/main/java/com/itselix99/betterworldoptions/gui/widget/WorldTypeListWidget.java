package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.gui.ScreenStateCache;
import com.itselix99.betterworldoptions.gui.screen.CreateWorldTypeScreen;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;

public class WorldTypeListWidget extends EntryListWidget {
    final CreateWorldTypeScreen parentGui;

    public WorldTypeListWidget(CreateWorldTypeScreen var1) {
        super(var1.mc, var1.width, var1.height, 32, var1.height - 64, 36);
        this.parentGui = var1;
    }

    @Override
    protected int getEntryCount() {
        return WorldTypeList.getList().size();
    }

    @Override
    protected void entryClicked(int var1, boolean var2) {
        TranslationStorage translation = TranslationStorage.getInstance();
        CreateWorldTypeScreen.onElementSelected(this.parentGui, var1);
        CreateWorldTypeScreen.getSelectButton(this.parentGui).text = translation.get("gui.done");
        try {
            WorldTypeList.setWorldType(WorldTypeList.getList().get(var1));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean isSelectedEntry(int var1) {
        return ScreenStateCache.lastWorldType == var1;
    }

    @Override
    protected int getEntriesHeight() {
        return WorldTypeList.getList().size() * 36;
    }

    @Override
    protected void renderBackground() {
        this.parentGui.renderBackground();
    }

    @Override
    protected void renderEntry(int var1, int var2, int var3, int var4, Tessellator var5) {
        WorldTypeList.WorldTypeEntry var6 = WorldTypeList.getList().get(var1);
        this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.DISPLAY_NAME, var2 + 2, var3 + 1, 16777215);
        if(var6.DESCRIPTION != null) {
            this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.DESCRIPTION, var2 + 2, var3 + 12, 8421504);
        }

        if(var6.DESCRIPTION_2 != null) {
            this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.DESCRIPTION_2, var2 + 2, var3 + 12 + 10, 8421504);
        }

    }
}