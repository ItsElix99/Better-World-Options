package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.platform.Lighting;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawContext {
    @Shadow private static ItemRenderer ITEM_RENDERER = new ItemRenderer();
    @Shadow private List messages = new ArrayList();
    @Shadow private Random random = new Random();
    @Shadow private Minecraft minecraft;
    @Shadow private int ticks = 0;
    @Shadow private String overlayMessage = "";
    @Shadow private int overlayRemaining = 0;
    @Shadow private boolean overlayTinted = false;
    @Shadow float vignetteDarkness = 1.0F;

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void render(float tickDelta, boolean screenOpen, int mouseX, int mouseY , CallbackInfo ci) {
        ScreenScaler var5 = new ScreenScaler(this.minecraft.options, this.minecraft.displayWidth, this.minecraft.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        TextRenderer var8 = this.minecraft.textRenderer;
        this.minecraft.gameRenderer.setupHudRender();
        GL11.glEnable(3042);
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.renderVignette(this.minecraft.player.getBrightnessAtEyes(tickDelta), var6, var7);
        }

        ItemStack var9 = this.minecraft.player.inventory.getArmorStack(3);
        if (!this.minecraft.options.thirdPerson && var9 != null && var9.itemId == Block.PUMPKIN.id) {
            this.renderPumpkinOverlay(var6, var7);
        }

        float var10 = this.minecraft.player.lastScreenDistortion + (this.minecraft.player.screenDistortion - this.minecraft.player.lastScreenDistortion) * tickDelta;
        if (var10 > 0.0F) {
            this.renderPortalOverlay(var10, var6, var7);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/gui.png"));
        PlayerInventory var11 = this.minecraft.player.inventory;
        this.zOffset = -90.0F;
        this.drawTexture(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
        this.drawTexture(var6 / 2 - 91 - 1 + var11.selectedSlot * 20, var7 - 22 - 1, 0, 22, 24, 22);
        if (((BWOProperties) this.minecraft.world.getProperties()).bwo_getHardcore()) {
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/assets/betterworldoptions/textures/gui/iconsWithHardcoreHearts.png"));
        } else {
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/icons.png"));
            ci.cancel();
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(775, 769);
        this.drawTexture(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(3042);
        boolean var12 = this.minecraft.player.hearts / 3 % 2 == 1;
        if (this.minecraft.player.hearts < 10) {
            var12 = false;
        }

        int var13 = this.minecraft.player.health;
        int var14 = this.minecraft.player.lastHealth;
        this.random.setSeed(this.ticks * 312871L);
        if (this.minecraft.interactionManager.canBeRendered()) {
            int var15 = this.minecraft.player.getTotalArmorDurability();

            for (int var16 = 0; var16 < 10; ++var16) {
                int var17 = var7 - 32;
                if (var15 > 0) {
                    int var18 = var6 / 2 + 91 - var16 * 8 - 9;
                    if (var16 * 2 + 1 < var15) {
                        this.drawTexture(var18, var17, 34, 9, 9, 9);
                    }

                    if (var16 * 2 + 1 == var15) {
                        this.drawTexture(var18, var17, 25, 9, 9, 9);
                    }

                    if (var16 * 2 + 1 > var15) {
                        this.drawTexture(var18, var17, 16, 9, 9, 9);
                    }
                }

                byte var40 = 0;
                if (var12) {
                    var40 = 1;
                }

                int var19 = var6 / 2 - 91 + var16 * 8;
                if (var13 <= 4) {
                    var17 += this.random.nextInt(2);
                }

                byte var20 = 0;
                if (((WorldProperties) this.minecraft.world.getProperties()).bwo_getHardcore()) {
                    var20 = 5;
                }

                this.drawTexture(var19, var17, 16 + var40 * 9, 9 * var20, 9, 9);
                if (var12) {
                    if (var16 * 2 + 1 < var14) {
                        this.drawTexture(var19, var17, 70, 9 * var20, 9, 9);
                    }

                    if (var16 * 2 + 1 == var14) {
                        this.drawTexture(var19, var17, 79, 9 * var20, 9, 9);
                    }
                }

                if (var16 * 2 + 1 < var13) {
                    this.drawTexture(var19, var17, 52, 9 * var20, 9, 9);
                }

                if (var16 * 2 + 1 == var13) {
                    this.drawTexture(var19, var17, 61, 9 * var20, 9, 9);
                }
            }

            if (this.minecraft.player.isInFluid(Material.WATER)) {
                int var29 = (int) Math.ceil((double) (this.minecraft.player.air - 2) * (double) 10.0F / (double) 300.0F);
                int var34 = (int) Math.ceil((double) this.minecraft.player.air * (double) 10.0F / (double) 300.0F) - var29;

                for (int var41 = 0; var41 < var29 + var34; ++var41) {
                    if (var41 < var29) {
                        this.drawTexture(var6 / 2 - 91 + var41 * 8, var7 - 32 - 9, 16, 18, 9, 9);
                    } else {
                        this.drawTexture(var6 / 2 - 91 + var41 * 8, var7 - 32 - 9, 25, 18, 9, 9);
                    }
                }
            }
        }

        GL11.glDisable(3042);
        GL11.glEnable(32826);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.turnOn();
        GL11.glPopMatrix();

        for (int var24 = 0; var24 < 9; ++var24) {
            int var30 = var6 / 2 - 90 + var24 * 20 + 2;
            int var35 = var7 - 16 - 3;
            this.renderHotbarItem(var24, var30, var35, tickDelta);
        }

        Lighting.turnOff();
        GL11.glDisable(32826);
        if (this.minecraft.player.getSleepTimer() > 0) {
            GL11.glDisable(2929);
            GL11.glDisable(3008);
            int var25 = this.minecraft.player.getSleepTimer();
            float var31 = (float) var25 / 100.0F;
            if (var31 > 1.0F) {
                var31 = 1.0F - (float) (var25 - 100) / 10.0F;
            }

            int var36 = (int) (220.0F * var31) << 24 | 1052704;
            this.fill(0, 0, var6, var7, var36);
            GL11.glEnable(3008);
            GL11.glEnable(2929);
        }

        if (this.minecraft.options.debugHud) {
            GL11.glPushMatrix();
            if (Minecraft.failedSessionCheckTime > 0L) {
                GL11.glTranslatef(0.0F, 32.0F, 0.0F);
            }

            var8.drawWithShadow("Minecraft Beta 1.7.3 (" + this.minecraft.debugText + ")", 2, 2, 16777215);
            var8.drawWithShadow(this.minecraft.getRenderChunkDebugInfo(), 2, 12, 16777215);
            var8.drawWithShadow(this.minecraft.getRenderEntityDebugInfo(), 2, 22, 16777215);
            var8.drawWithShadow(this.minecraft.getWorldDebugInfo(), 2, 32, 16777215);
            var8.drawWithShadow(this.minecraft.getChunkSourceDebugInfo(), 2, 42, 16777215);
            long var26 = Runtime.getRuntime().maxMemory();
            long var37 = Runtime.getRuntime().totalMemory();
            long var46 = Runtime.getRuntime().freeMemory();
            long var21 = var37 - var46;
            String var23 = "Used memory: " + var21 * 100L / var26 + "% (" + var21 / 1024L / 1024L + "MB) of " + var26 / 1024L / 1024L + "MB";
            this.drawTextWithShadow(var8, var23, var6 - var8.getWidth(var23) - 2, 2, 14737632);
            var23 = "Allocated memory: " + var37 * 100L / var26 + "% (" + var37 / 1024L / 1024L + "MB)";
            this.drawTextWithShadow(var8, var23, var6 - var8.getWidth(var23) - 2, 12, 14737632);
            this.drawTextWithShadow(var8, "x: " + this.minecraft.player.x, 2, 64, 14737632);
            this.drawTextWithShadow(var8, "y: " + this.minecraft.player.y, 2, 72, 14737632);
            this.drawTextWithShadow(var8, "z: " + this.minecraft.player.z, 2, 80, 14737632);
            this.drawTextWithShadow(var8, "f: " + (MathHelper.floor((double) (this.minecraft.player.yaw * 4.0F / 360.0F) + (double) 0.5F) & 3), 2, 88, 14737632);
            GL11.glPopMatrix();
        }

        if (this.overlayRemaining > 0) {
            float var27 = (float) this.overlayRemaining - tickDelta;
            int var32 = (int) (var27 * 256.0F / 20.0F);
            if (var32 > 255) {
                var32 = 255;
            }

            if (var32 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) (var6 / 2), (float) (var7 - 48), 0.0F);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                int var38 = 16777215;
                if (this.overlayTinted) {
                    var38 = Color.HSBtoRGB(var27 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                var8.draw(this.overlayMessage, -var8.getWidth(this.overlayMessage) / 2, -4, var38 + (var32 << 24));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }

        byte var28 = 10;
        boolean var33 = false;
        if (this.minecraft.currentScreen instanceof ChatScreen) {
            var28 = 20;
            var33 = true;
        }

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, (float) (var7 - 48), 0.0F);

        for (int var39 = 0; var39 < this.messages.size() && var39 < var28; ++var39) {
            if (((ChatHudLine) this.messages.get(var39)).age < 200 || var33) {
                double var42 = (double) ((ChatHudLine) this.messages.get(var39)).age / (double) 200.0F;
                var42 = (double) 1.0F - var42;
                var42 *= 10.0F;
                if (var42 < (double) 0.0F) {
                    var42 = 0.0F;
                }

                if (var42 > (double) 1.0F) {
                    var42 = 1.0F;
                }

                var42 *= var42;
                int var20 = (int) ((double) 255.0F * var42);
                if (var33) {
                    var20 = 255;
                }

                if (var20 > 0) {
                    byte var47 = 2;
                    int var22 = -var39 * 9;
                    String var49 = ((ChatHudLine) this.messages.get(var39)).text;
                    this.fill(var47, var22 - 1, var47 + 320, var22 + 8, var20 / 2 << 24);
                    GL11.glEnable(3042);
                    var8.drawWithShadow(var49, var47, var22, 16777215 + (var20 << 24));
                }
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        ci.cancel();
    }

    @Shadow
    private void renderPumpkinOverlay(int i, int j) {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("%blur%/misc/pumpkinblur.png"));
        Tessellator var3 = Tessellator.INSTANCE;
        var3.startQuads();
        var3.vertex(0.0F, j, -90.0F, 0.0F, 1.0F);
        var3.vertex(i, j, -90.0F, 1.0F, 1.0F);
        var3.vertex(i, 0.0F, -90.0F, 1.0F, 0.0F);
        var3.vertex(0.0F, 0.0F, -90.0F, 0.0F, 0.0F);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Shadow
    private void renderVignette(float f, int i, int j) {
        f = 1.0F - f;
        if (f < 0.0F) {
            f = 0.0F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        this.vignetteDarkness = (float)((double)this.vignetteDarkness + (double)(f - this.vignetteDarkness) * 0.01);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(0, 769);
        GL11.glColor4f(this.vignetteDarkness, this.vignetteDarkness, this.vignetteDarkness, 1.0F);
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("%blur%/misc/vignette.png"));
        Tessellator var4 = Tessellator.INSTANCE;
        var4.startQuads();
        var4.vertex(0.0F, j, -90.0F, 0.0F, 1.0F);
        var4.vertex(i, j, -90.0F, 1.0F, 1.0F);
        var4.vertex(i, 0.0F, -90.0F, 1.0F, 0.0F);
        var4.vertex(0.0F, 0.0F, -90.0F, 0.0F, 0.0F);
        var4.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(770, 771);
    }

    @Shadow
    private void renderPortalOverlay(float f, int i, int j) {
        if (f < 1.0F) {
            f *= f;
            f *= f;
            f = f * 0.8F + 0.2F;
        }

        GL11.glDisable(3008);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/terrain.png"));
        float var4 = (float)(Block.NETHER_PORTAL.textureId % 16) / 16.0F;
        float var5 = (float)(Block.NETHER_PORTAL.textureId / 16) / 16.0F;
        float var6 = (float)(Block.NETHER_PORTAL.textureId % 16 + 1) / 16.0F;
        float var7 = (float)(Block.NETHER_PORTAL.textureId / 16 + 1) / 16.0F;
        Tessellator var8 = Tessellator.INSTANCE;
        var8.startQuads();
        var8.vertex(0.0F, j, -90.0F, var4, var7);
        var8.vertex(i, j, -90.0F, var6, var7);
        var8.vertex(i, 0.0F, -90.0F, var6, var5);
        var8.vertex(0.0F, 0.0F, -90.0F, var4, var5);
        var8.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Shadow
    private void renderHotbarItem(int slot, int x, int y, float f) {
        ItemStack var5 = this.minecraft.player.inventory.main[slot];
        if (var5 != null) {
            float var6 = (float)var5.bobbingAnimationTime - f;
            if (var6 > 0.0F) {
                GL11.glPushMatrix();
                float var7 = 1.0F + var6 / 5.0F;
                GL11.glTranslatef((float)(x + 8), (float)(y + 12), 0.0F);
                GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
            }

            ITEM_RENDERER.renderGuiItem(this.minecraft.textRenderer, this.minecraft.textureManager, var5, x, y);
            if (var6 > 0.0F) {
                GL11.glPopMatrix();
            }

            ITEM_RENDERER.renderGuiItemDecoration(this.minecraft.textRenderer, this.minecraft.textureManager, var5, x, y);
        }
    }
}
