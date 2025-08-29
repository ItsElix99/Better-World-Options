package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.world.WorldType;
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
    private final WorldGenerationOptions worldGenerationOptions;
    private final TranslationStorage translation = TranslationStorage.getInstance();

    private WorldTypeListWidget worldTypeListWidget;
    private ButtonWidget doneButton;
    private static WorldType.WorldTypeEntry selectedWorldType;

    public WorldTypeListScreen(Screen parent, WorldGenerationOptions worldGenerationOptions) {
        this.parent = parent;
        this.worldGenerationOptions = worldGenerationOptions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.worldTypeListWidget = new WorldTypeListWidget(this);
        this.worldTypeListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.doneButton = new ButtonWidget(0, this.width / 2 - 75, this.height - 28, 150, 20, translation.get("gui.cancel")));
        selectedWorldType = WorldType.getList().stream().filter(worldTypeEntry -> worldTypeEntry.NAME.equals(this.worldGenerationOptions.worldTypeName)).toList().get(0);
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

    public static void selectWorldType(WorldType.WorldTypeEntry var1) {
        selectedWorldType = var1;
    }

    @Environment(EnvType.CLIENT)
    class WorldTypeListWidget extends EntryListWidget {
        public WorldTypeListWidget(WorldTypeListScreen worldTypeListScreen) {
            super(worldTypeListScreen.minecraft, worldTypeListScreen.width, worldTypeListScreen.height, 32, worldTypeListScreen.height - 55 + 4, 36);
        }

        protected int getEntryCount() {
            List<WorldType.WorldTypeEntry> var1 = WorldType.getList();
            return var1.size();
        }

        protected void entryClicked(int index, boolean doubleClick) {
            List<WorldType.WorldTypeEntry> var3 = WorldType.getList();
            WorldTypeListScreen.selectWorldType(var3.get(index));

            if (!WorldTypeListScreen.this.worldGenerationOptions.worldTypeName.equals(var3.get(index).NAME)) {
                WorldTypeListScreen.this.worldGenerationOptions.worldTypeName = var3.get(index).NAME;

                WorldTypeListScreen.this.doneButton.text = WorldTypeListScreen.this.translation.get("gui.done");
            }
        }

        protected boolean isSelectedEntry(int index) {
            List<WorldType.WorldTypeEntry> var2 = WorldType.getList();
            return WorldTypeListScreen.selectedWorldType == var2.get(index);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 36;
        }

        protected void renderBackground() {
            WorldTypeListScreen.this.renderBackground();
        }

        protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
            WorldType.WorldTypeEntry var6 = WorldType.getList().get(index);

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