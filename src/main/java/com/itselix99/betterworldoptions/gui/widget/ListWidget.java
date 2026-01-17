package com.itselix99.betterworldoptions.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public abstract class ListWidget {
    protected final Minecraft minecraft;
    protected int width;
    protected int height;
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    protected final int itemHeight;
    protected int mouseX;
    protected int mouseY;
    protected boolean centerAlongY = true;
    protected int mouseYStart = -2;
    protected float scrollSpeedMultiplier;
    protected float scrollAmount;
    protected int pos = -1;
    protected long time;
    protected boolean visible = true;
    protected boolean renderSelectionHighlight = true;
    protected boolean renderHeader;
    protected int headerHeight;
    private boolean scrolling = true;

    public ListWidget(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
        this.itemHeight = itemHeight;
        this.left = 0;
        this.right = width;
    }

    protected abstract int getEntryCount();

    protected abstract void entryClicked(int index, boolean doubleClick, int mouseX, int mouseY);

    protected abstract boolean isSelectedEntry(int index);

    protected int getEntriesHeight() {
        return this.getEntryCount() * this.itemHeight + this.headerHeight;
    }

    protected abstract void renderBackground();

    protected void renderEntryOutOfBounds(int index, int x, int y, float tickDelta) {
    }

    protected abstract void renderEntry(int index, int x, int y, int height, int mouseX, int mouseY, float tickDelta);

    protected void renderHeader(int x, int y, Tessellator tessellator) {
    }

    protected void headerClicked(int x, int y) {
    }

    protected void renderDecorations(int mouseX, int mouseY) {
    }

    public int getEntryAt(int x, int y) {
        int var1 = this.left + this.width / 2 - this.getRowWidth() / 2;
        int var2 = this.left + this.width / 2 + this.getRowWidth() / 2;
        int var3 = y - this.top - this.headerHeight + (int)this.scrollAmount - 4;
        int var4 = var3 / this.itemHeight;
        return x < this.getScrollbarPosition() && x >= var1 && x <= var2 && var4 >= 0 && var3 >= 0 && var4 < this.getEntryCount() ? var4 : -1;
    }

    public static int clamp(int x, int min, int max) {
        if (x < min) {
            return min;
        } else {
            return x > max ? max : x;
        }
    }

    private static float clamp(float x, float min, float max) {
        if (x < min) {
            return min;
        } else {
            return x > max ? max : x;
        }
    }

    protected void capScrolling() {
        this.scrollAmount = clamp(this.scrollAmount, 0.0F, (float)this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getEntriesHeight() - (this.bottom - this.top - 4));
    }

    public int getScrollAmount() {
        return (int)this.scrollAmount;
    }

    public boolean isMouseInList(int mouseY) {
        return mouseY >= this.top && mouseY <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    public void render(int mouseX, int mouseY, float tickDelta) {
        if (this.visible) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.renderBackground();
            int var1 = this.getScrollbarPosition();
            int var2 = var1 + 6;
            this.capScrolling();
            GL11.glDisable(2896);
            GL11.glDisable(2912);
            Tessellator var3 = Tessellator.INSTANCE;
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/background.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float var4 = 32.0F;
            var3.startQuads();
            var3.color(2105376);
            var3.vertex(this.left, this.bottom, 0.0F, (float)this.left / var4, (float)(this.bottom + (int)this.scrollAmount) / var4);
            var3.vertex(this.right, this.bottom, 0.0F, (float)this.right / var4, (float)(this.bottom + (int)this.scrollAmount) / var4);
            var3.vertex(this.right, this.top, 0.0F, (float)this.right / var4, (float)(this.top + (int)this.scrollAmount) / var4);
            var3.vertex(this.left, this.top, 0.0F, (float)this.left / var4, (float)(this.top + (int)this.scrollAmount) / var4);
            var3.draw();
            int var5 = this.left + this.width / 2 - this.getRowWidth() / 2 + 2;
            int var6 = this.top + 4 - (int)this.scrollAmount;
            if (this.renderHeader) {
                this.renderHeader(var5, var6, var3);
            }

            this.renderList(var5, var6, mouseX, mouseY, tickDelta);
            GL11.glDisable(2929);
            this.renderBackground(0, this.top, 255, 255);
            this.renderBackground(this.bottom, this.height, 255, 255);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3008);
            GL11.glShadeModel(7425);
            GL11.glDisable(3553);
            var3.startQuads();
            var3.color(0, 0);
            var3.vertex(this.left, this.top + 4, 0.0F, 0.0F, 1.0F);
            var3.vertex(this.right, this.top + 4, 0.0F, 1.0F, 1.0F);
            var3.color(0, 255);
            var3.vertex(this.right, this.top, 0.0F, 1.0F, 0.0F);
            var3.vertex(this.left, this.top, 0.0F, 0.0F, 0.0F);
            var3.draw();
            var3.startQuads();
            var3.color(0, 255);
            var3.vertex(this.left, this.bottom, 0.0F, 0.0F, 1.0F);
            var3.vertex(this.right, this.bottom, 0.0F, 1.0F, 1.0F);
            var3.color(0, 0);
            var3.vertex(this.right, this.bottom - 4, 0.0F, 1.0F, 0.0F);
            var3.vertex(this.left, this.bottom - 4, 0.0F, 0.0F, 0.0F);
            var3.draw();
            int var7 = this.getMaxScroll();
            if (var7 > 0) {
                int var8 = (this.bottom - this.top) * (this.bottom - this.top) / this.getEntriesHeight();
                var8 = clamp(var8, 32, this.bottom - this.top - 8);
                int var9 = (int)this.scrollAmount * (this.bottom - this.top - var8) / var7 + this.top;
                if (var9 < this.top) {
                    var9 = this.top;
                }

                var3.startQuads();
                var3.color(0, 255);
                var3.vertex(var1, this.bottom, 0.0F, 0.0F, 1.0F);
                var3.vertex(var2, this.bottom, 0.0F, 1.0F, 1.0F);
                var3.vertex(var2, this.top, 0.0F, 1.0F, 0.0F);
                var3.vertex(var1, this.top, 0.0F, 0.0F, 0.0F);
                var3.draw();
                var3.startQuads();
                var3.color(8421504, 255);
                var3.vertex(var1, var9 + var8, 0.0F, 0.0F, 1.0F);
                var3.vertex(var2, var9 + var8, 0.0F, 1.0F, 1.0F);
                var3.vertex(var2, var9, 0.0F, 1.0F, 0.0F);
                var3.vertex(var1, var9, 0.0F, 0.0F, 0.0F);
                var3.draw();
                var3.startQuads();
                var3.color(12632256, 255);
                var3.vertex(var1, var9 + var8 - 1, 0.0F, 0.0F, 1.0F);
                var3.vertex(var2 - 1, var9 + var8 - 1, 0.0F, 1.0F, 1.0F);
                var3.vertex(var2 - 1, var9, 0.0F, 1.0F, 0.0F);
                var3.vertex(var1, var9, 0.0F, 0.0F, 0.0F);
                var3.draw();
            }

            this.renderDecorations(mouseX, mouseY);
            GL11.glEnable(3553);
            GL11.glShadeModel(7424);
            GL11.glEnable(3008);
            GL11.glDisable(3042);
        }
    }

    private static long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public void handleMouse() {
        if (this.isMouseInList(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                int var1 = (this.width - this.getRowWidth()) / 2;
                int var2 = (this.width + this.getRowWidth()) / 2;
                int var3 = this.mouseY - this.top - this.headerHeight + (int)this.scrollAmount - 4;
                int var4 = var3 / this.itemHeight;
                if (var4 < this.getEntryCount() && this.mouseX >= var1 && this.mouseX <= var2 && var4 >= 0 && var3 >= 0) {
                    this.entryClicked(var4, false, this.mouseX, this.mouseY);
                    this.pos = var4;
                } else if (this.mouseX >= var1 && this.mouseX <= var2 && var3 < 0) {
                    this.headerClicked(this.mouseX - var1, this.mouseY - this.top + (int)this.scrollAmount - 4);
                }
            }

            if (Mouse.isButtonDown(0) && this.isScrolling()) {
                if (this.mouseYStart == -1) {
                    boolean var5 = true;
                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        int var6 = (this.width - this.getRowWidth()) / 2;
                        int var7 = (this.width + this.getRowWidth()) / 2;
                        int var8 = this.mouseY - this.top - this.headerHeight + (int)this.scrollAmount - 4;
                        int var9 = var8 / this.itemHeight;
                        if (var9 < this.getEntryCount() && this.mouseX >= var6 && this.mouseX <= var7 && var9 >= 0 && var8 >= 0) {
                            boolean var10 = var9 == this.pos && getTime() - this.time < 250L;
                            this.entryClicked(var9, var10, this.mouseX, this.mouseY);
                            this.pos = var9;
                            this.time = getTime();
                        } else if (this.mouseX >= var6 && this.mouseX <= var7 && var8 < 0) {
                            this.headerClicked(this.mouseX - var6, this.mouseY - this.top + (int)this.scrollAmount - 4);
                            var5 = false;
                        }

                        int var11 = this.getScrollbarPosition();
                        int var12 = var11 + 6;
                        if (this.mouseX >= var11 && this.mouseX <= var12) {
                            this.scrollSpeedMultiplier = -1.0F;
                            int var13 = this.getMaxScroll();
                            if (var13 < 1) {
                                var13 = 1;
                            }

                            int var14 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getEntriesHeight());
                            var14 = clamp(var14, 32, this.bottom - this.top - 8);
                            this.scrollSpeedMultiplier /= (float)(this.bottom - this.top - var14) / (float)var13;
                        } else {
                            this.scrollSpeedMultiplier = 1.0F;
                        }

                        if (var5) {
                            this.mouseYStart = this.mouseY;
                        } else {
                            this.mouseYStart = -2;
                        }
                    } else {
                        this.mouseYStart = -2;
                    }
                } else if (this.mouseYStart >= 0) {
                    this.scrollAmount -= (float)(this.mouseY - this.mouseYStart) * this.scrollSpeedMultiplier;
                    this.mouseYStart = this.mouseY;
                }
            } else {
                this.mouseYStart = -1;
            }

            int var16 = Mouse.getEventDWheel();
            if (var16 != 0) {
                if (var16 > 0) {
                    var16 = -1;
                } else if (var16 < 0) {
                    var16 = 1;
                }

                this.scrollAmount += (float)(var16 * this.itemHeight / 2);
            }

        }
    }

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    public boolean isScrolling() {
        return this.scrolling;
    }

    public int getRowWidth() {
        return 220;
    }

    protected void renderList(int x, int y, int mouseX, int mouseY, float tickDelta) {
        int var1 = this.getEntryCount();
        Tessellator var2 = Tessellator.INSTANCE;

        for(int var3 = 0; var3 < var1; ++var3) {
            int var4 = y + var3 * this.itemHeight + this.headerHeight;
            int var5 = this.itemHeight - 4;
            if (var4 > this.bottom || var4 + var5 < this.top) {
                this.renderEntryOutOfBounds(var3, x, var4, tickDelta);
            }

            if (this.renderSelectionHighlight && this.isSelectedEntry(var3)) {
                int var6 = this.left + (this.width / 2 - this.getRowWidth() / 2);
                int var7 = this.left + this.width / 2 + this.getRowWidth() / 2;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(3553);
                var2.startQuads();
                var2.color(8421504);
                var2.vertex(var6, var4 + var5 + 2, 0.0F, 0.0F, 1.0F);
                var2.vertex(var7, var4 + var5 + 2, 0.0F, 1.0F, 1.0F);
                var2.vertex(var7, var4 - 2, 0.0F, 1.0F, 0.0F);
                var2.vertex(var6, var4 - 2, 0.0F, 0.0F, 0.0F);
                var2.vertex(var6 + 1, var4 + var5 + 1, 0.0F, 0.0F, 1.0F);
                var2.vertex(var7 - 1, var4 + var5 + 1, 0.0F, 1.0F, 1.0F);
                var2.vertex(var7 - 1, var4 - 1, 0.0F, 1.0F, 0.0F);
                var2.vertex(var6 + 1, var4 - 1, 0.0F, 0.0F, 0.0F);
                var2.draw();
                GL11.glEnable(3553);
            }

            this.renderEntry(var3, x, var4, var5, mouseX, mouseY, tickDelta);
        }

    }

    protected int getScrollbarPosition() {
        return this.width / 2 + 124;
    }

    private void renderBackground(int i, int j, int k, int l) {
        Tessellator var1 = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var2 = 32.0F;
        var1.startQuads();
        var1.color(4210752, l);
        var1.vertex(0.0F, j, 0.0F, 0.0F, (float)j / var2);
        var1.vertex(this.width, j, 0.0F, (float)this.width / var2, (float)j / var2);
        var1.color(4210752, k);
        var1.vertex(this.width, i, 0.0F, (float)this.width / var2, (float)i / var2);
        var1.vertex(0.0F, i, 0.0F, 0.0F, (float)i / var2);
        var1.draw();
    }

    public void setX(int minX) {
        this.left = minX;
        this.right = minX + this.width;
    }
}