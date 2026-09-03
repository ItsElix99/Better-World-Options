package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ThemesListScreen extends Screen {
    protected Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = this.translation.get("selectTheme.title");
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;

    private ThemesListWidget themesListWidget;
    private ButtonWidget doneButton;
    private static Theme selectedTheme;

    public ThemesListScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.themesListWidget = new ThemesListWidget(this);
        this.themesListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.doneButton = new ButtonWidget(0, this.width / 2 - 75, this.height - 28, 150, 20, translation.get("gui.cancel")));

        String currentTheme = this.bwoWorldPropertiesStorage.getOptionValue("Theme", OptionType.GENERAL_OPTION, "");
        selectedTheme = Theme.getThemeById(Identifier.of(currentTheme));
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    public void render(int var1, int var2, float var3) {
        this.themesListWidget.render(var1, var2, var3);
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(var1, var2, var3);
    }

    public static void selectTheme(Theme var1) {
        selectedTheme = var1;
    }

    @Environment(EnvType.CLIENT)
    class ThemesListWidget extends EntryListWidget {
        public ThemesListWidget(ThemesListScreen themesListScreen) {
            super(themesListScreen.minecraft, themesListScreen.width, themesListScreen.height, 32, themesListScreen.height - 55 + 4, 36);
        }

        protected int getEntryCount() {
            List<Theme> var1 = Theme.getThemesList();
            return var1.size();
        }

        protected void entryClicked(int index, boolean doubleClick) {
            List<Theme> var3 = Theme.getThemesList();
            ThemesListScreen.selectTheme(var3.get(index));

            String currentTheme = ThemesListScreen.this.bwoWorldPropertiesStorage.getOptionValue("Theme", OptionType.GENERAL_OPTION, "");
            if (!currentTheme.equals(var3.get(index).getId().toString())) {
                ThemesListScreen.this.bwoWorldPropertiesStorage.setOptionValue("Theme", OptionType.GENERAL_OPTION, var3.get(index).getId().toString());
                ThemesListScreen.this.doneButton.text = ThemesListScreen.this.translation.get("gui.done");
            }
        }

        protected boolean isSelectedEntry(int index) {
            List<Theme> var2 = Theme.getThemesList();
            return ThemesListScreen.selectedTheme == var2.get(index);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 36;
        }

        protected void renderBackground() {
            ThemesListScreen.this.renderBackground();
        }

        protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
            Theme var1 = Theme.getThemesList().get(index);

            GL11.glBindTexture(3553, ThemesListScreen.this.minecraft.textureManager.getTextureId(Objects.requireNonNullElse(var1.getIcon(), "/gui/unknown_pack.png")));

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.startQuads();
            tessellator.color(16777215);
            tessellator.vertex(x, y + i, 0.0F, 0.0F, 1.0F);
            tessellator.vertex(x + 32, y + i, 0.0F, 1.0F, 1.0F);
            tessellator.vertex(x + 32, y, 0.0F, 1.0F, 0.0F);
            tessellator.vertex(x, y, 0.0F, 0.0F, 0.0F);
            tessellator.draw();

            ThemesListScreen.this.drawTextWithShadow(ThemesListScreen.this.minecraft.textRenderer, var1.getName(), x + 32 + 2, y + 1, 16777215);

            if (var1.getDescription() != null) {
                for (int var2 = 0; var2 < var1.getDescription().length; ++var2) {
                    ThemesListScreen.this.drawTextWithShadow(ThemesListScreen.this.minecraft.textRenderer, var1.getDescription()[var2], x + 32 + 2, y + (12 * (var2 + 1)), 8421504);
                }
            }
        }
    }
}