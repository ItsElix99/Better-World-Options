package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.world.WorldSettings;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WorldTypeListScreen extends Screen {
    protected Screen parent;
    protected String title = "Select World Type";
    private final TranslationStorage translation = TranslationStorage.getInstance();
    private WorldTypeListWidget worldTypeListWidget;
    private ButtonWidget buttonSelect;
    private static WorldTypeList.WorldTypeEntry selectedWorldType = WorldTypeList.getList().get(0);

    public WorldTypeListScreen(Screen parent) {
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.worldTypeListWidget = new WorldTypeListWidget(this);
        this.worldTypeListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.buttonSelect = new ButtonWidget(0, this.width / 2 - 75, this.height - 28, 150, 20, translation.get("gui.cancel")));
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    @Override
    public void render(int var1, int var2, float var3) {
        this.worldTypeListWidget.render(var1, var2, var3);
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(var1, var2, var3);
    }

    public static void selectWorldType(WorldTypeList.WorldTypeEntry var1) {
        selectedWorldType = var1;
    }

    @Environment(EnvType.CLIENT)
    class WorldTypeListWidget extends EntryListWidget {
        public WorldTypeListWidget(WorldTypeListScreen worldTypeListScreen) {
            super(worldTypeListScreen.minecraft, worldTypeListScreen.width, worldTypeListScreen.height, 32, worldTypeListScreen.height - 55 + 4, 36);
        }

        protected int getEntryCount() {
            List<WorldTypeList.WorldTypeEntry> var1 = WorldTypeList.getList();
            return var1.size();
        }

        protected void entryClicked(int index, boolean doubleClick) {
            List<WorldTypeList.WorldTypeEntry> var3 = WorldTypeList.getList();
            WorldTypeListScreen.selectWorldType(var3.get(index));

            if (!WorldSettings.World.getWorldTypeName().equals(var3.get(index).NAME)) {
                try {
                    WorldTypeList.setWorldType(var3.get(index));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                WorldTypeListScreen.this.buttonSelect.text = WorldTypeListScreen.this.translation.get("gui.done");
            }
        }

        protected boolean isSelectedEntry(int index) {
            List<WorldTypeList.WorldTypeEntry> var2 = WorldTypeList.getList();
            return WorldTypeListScreen.selectedWorldType == var2.get(index);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 36;
        }

        protected void renderBackground() {
            WorldTypeListScreen.this.renderBackground();
        }

        protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
            WorldTypeList.WorldTypeEntry var6 = WorldTypeList.getList().get(index);

            if (var6.ICON != null) {
                GL11.glBindTexture(3553, WorldTypeListScreen.this.minecraft.textureManager.getTextureId(var6.ICON));
            } else {
                GL11.glBindTexture(3553, WorldTypeListScreen.this.minecraft.textureManager.getTextureId("/gui/unknown_pack.png"));
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.startQuads();
            tessellator.color(16777215);
            tessellator.vertex(x, y + i, 0.0F, 0.0F, 1.0F);
            tessellator.vertex(x + 32, y + i, 0.0F, 1.0F, 1.0F);
            tessellator.vertex(x + 32, y, 0.0F, 1.0F, 0.0F);
            tessellator.vertex(x, y, 0.0F, 0.0F, 0.0F);
            tessellator.draw();
            WorldTypeListScreen.this.drawTextWithShadow(WorldTypeListScreen.this.minecraft.textRenderer, var6.DISPLAY_NAME, x + 32 + 2, y + 1, 16777215);
            if(var6.DESCRIPTION != null) {
                WorldTypeListScreen.this.drawTextWithShadow(WorldTypeListScreen.this.minecraft.textRenderer, var6.DESCRIPTION, x + 32 + 2, y + 12, 8421504);
            }

            if(var6.DESCRIPTION_2 != null) {
                WorldTypeListScreen.this.drawTextWithShadow(WorldTypeListScreen.this.minecraft.textRenderer, var6.DESCRIPTION_2, x + 32 + 2, y + 12 + 10, 8421504);
            }
        }
    }
}