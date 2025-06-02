package com.itselix99.betterworldoptions.gui;

import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;

public class CreateWorldTypeSlot extends EntryListWidget {
    final CreateWorldTypeScreen parentGui;

    public CreateWorldTypeSlot(CreateWorldTypeScreen var1) {
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
        WorldTypeList.selectWorldType(WorldTypeList.worldtypeList.get(var1));
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
        this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.name, var2 + 2, var3 + 1, 16777215);
        if(var6.desc != null) {
            this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.desc, var2 + 2, var3 + 12, 8421504);
        }

        if(var6.desc2 != null) {
            this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.desc2, var2 + 2, var3 + 12 + 10, 8421504);
        }

    }
}