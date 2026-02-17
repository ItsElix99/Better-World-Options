package com.itselix99.betterworldoptions.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public abstract class EntryListWidgetButtons extends ListWidget {
    public EntryListWidgetButtons(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraft, width, height, top, bottom, itemHeight);
    }

    protected void entryClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
    }

    protected boolean isSelectedEntry(int index) {
        return false;
    }

    protected void renderBackground() {
    }

    protected void renderEntry(int index, int x, int y, int height, int mouseX, int mouseY, float tickDelta) {
        this.getEntry(index).render(index, x, y, this.getRowWidth(), height, mouseX, mouseY, this.isMouseInList(mouseY) && this.getEntryAt(mouseX, mouseY) == index, tickDelta);
    }

    protected void renderEntryOutOfBounds(int index, int x, int y, float tickDelta) {
        this.getEntry(index).renderOutOfBounds(index, x, y, tickDelta);
    }

    public void tick() {
        for(int var1 = 0; var1 < this.getEntryCount(); ++var1) {
            this.getEntry(var1).tick();
        }
    }

    public void keyPressed(char character, int keyCode) {
        for(int var1 = 0; var1 < this.getEntryCount(); ++var1) {
            this.getEntry(var1).keyPressed(character, keyCode);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseInList(mouseY)) {
            int var1 = this.getEntryAt(mouseX, mouseY);
            if (var1 >= 0) {
                int var2 = this.left + this.width / 2 - this.getRowWidth() / 2 + 2;
                int var3 = this.top + 4 - this.getScrollAmount() + var1 * this.itemHeight + this.headerHeight;
                int var4 = mouseX - var2;
                int var5 = mouseY - var3;
                if (this.getEntry(var1).mouseClicked(var1, mouseX, mouseY, button, var4, var5)) {
                    this.setScrolling(false);
                }
            }
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        for(int var1 = 0; var1 < this.getEntryCount(); ++var1) {
            int var2 = this.left + this.width / 2 - this.getRowWidth() / 2 + 2;
            int var3 = this.top + 4 - this.getScrollAmount() + var1 * this.itemHeight + this.headerHeight;
            int var4 = mouseX - var2;
            int var5 = mouseY - var3;
            this.getEntry(var1).mouseReleased(var1, mouseX, mouseY, button, var4, var5);
        }

        this.setScrolling(true);
    }

    public void renderTooltip(int mouseX, int mouseY) {
        if (this.isMouseInList(mouseY)) {
            int var1 = this.getEntryAt(mouseX, mouseY);
            if (var1 >= 0) {
                this.getEntry(var1).renderTooltip(mouseX, mouseY);
            }
        }

    }

    public abstract Entry getEntry(int index);

    @Environment(EnvType.CLIENT)
    public interface Entry {
        void tick();

        void keyPressed(char character, int keyCode);

        void renderOutOfBounds(int index, int x, int y, float tickDelta);

        void render(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta);

        void renderTooltip(int mouseX, int mouseY);

        boolean mouseClicked(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY);

        void mouseReleased(int index, int mouseX, int mouseY, int button, int entryMouseX, int entryMouseY);
    }
}