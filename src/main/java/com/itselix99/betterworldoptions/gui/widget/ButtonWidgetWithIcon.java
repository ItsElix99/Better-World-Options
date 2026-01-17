package com.itselix99.betterworldoptions.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ButtonWidgetWithIcon extends ButtonWidget {
    public String icon;

    public ButtonWidgetWithIcon(int id, int x, int y, String icon) {
        super(id, x, y, 20, 20, "");
        this.icon = icon;
    }

    public void render(Minecraft minecraft, int mouseX, int mouseY) {
        super.render(minecraft, mouseX, mouseY);
        if (this.visible) {
            Tessellator tessellator = Tessellator.INSTANCE;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId(this.icon));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            byte var1 = 16;
            int var2 = this.x + (this.width - var1) / 2;
            int var3 = this.y + (this.height - var1) / 2;
            tessellator.startQuads();
            tessellator.color(16777215);
            tessellator.vertex(var2, var3 + var1, 0.0D, 0.0D, 1.0D);
            tessellator.vertex(var2 + var1, var3 + var1, 0.0D, 1.0D, 1.0D);
            tessellator.vertex(var2 + var1, var3, 0.0D, 1.0D, 0.0D);
            tessellator.vertex(var2, var3, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }
    }
}