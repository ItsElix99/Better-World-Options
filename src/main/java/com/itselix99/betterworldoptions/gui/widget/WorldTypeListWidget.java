package com.itselix99.betterworldoptions.gui.widget;

import com.itselix99.betterworldoptions.gui.ScreenStateCache;
import com.itselix99.betterworldoptions.gui.screen.CreateWorldTypeScreen;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;

public class WorldTypeListWidget extends EntryListWidget {
    private final Minecraft minecraft;
    private final CreateWorldTypeScreen parentGui;

    public WorldTypeListWidget(CreateWorldTypeScreen var1) {
        super(var1.mc, var1.width, var1.height, 32, var1.height - 64, 36);
        this.minecraft = var1.mc;
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
    protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
        WorldTypeList.WorldTypeEntry var6 = WorldTypeList.getList().get(index);
        if (var6.ICON != null) {
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId(var6.ICON));
        } else {
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/unknown_pack.png"));
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startQuads();
        tessellator.color(16777215);
        tessellator.vertex(x, y + i, 0.0F, 0.0F, 1.0F);
        tessellator.vertex(x + 32, y + i, 0.0F, 1.0F, 1.0F);
        tessellator.vertex(x + 32, y, 0.0F, 1.0F, 0.0F);
        tessellator.vertex(x, y, 0.0F, 0.0F, 0.0F);
        tessellator.draw();
        this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.DISPLAY_NAME, x + 32 + 2, y + 1, 16777215);
        if(var6.DESCRIPTION != null) {
            this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.DESCRIPTION, x + 32 + 2, y + 12, 8421504);
        }

        if(var6.DESCRIPTION_2 != null) {
            this.parentGui.drawTextWithShadow(this.parentGui.mc.textRenderer, var6.DESCRIPTION_2, x + 32 + 2, y + 12 + 10, 8421504);
        }

    }
}