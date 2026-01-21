package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.IntOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;

import java.util.*;

@Environment(EnvType.CLIENT)
public class WorldTypeListScreen extends Screen {
    protected Screen parent;
    private final TranslationStorage translation = TranslationStorage.getInstance();
    protected String title = this.translation.get("selectWorldType.title");
    private final BWOWorldPropertiesStorage bwoWorldPropertiesStorage;

    private WorldTypeListWidget worldTypeListWidget;
    private ButtonWidget doneButton;
    private static WorldTypeEntry selectedWorldType;

    public WorldTypeListScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.worldTypeListWidget = new WorldTypeListWidget(this);
        this.worldTypeListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.doneButton = new ButtonWidget(0, this.width / 2 - 75, this.height - 28, 150, 20, translation.get("gui.cancel")));

        StringOptionStorage optionStorage = (StringOptionStorage) this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION);
        selectedWorldType = WorldTypes.getWorldTypeByName(optionStorage.value);
    }

    protected void buttonClicked(ButtonWidget button) {
        if (button.active && button.visible) {
            if (button.id == 0) {
                this.minecraft.setScreen(this.parent);
            }
        }
    }

    public void render(int var1, int var2, float var3) {
        this.worldTypeListWidget.render(var1, var2, var3);
        this.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(var1, var2, var3);
    }

    public static void selectWorldType(WorldTypeEntry var1) {
        selectedWorldType = var1;
    }

    @Environment(EnvType.CLIENT)
    class WorldTypeListWidget extends EntryListWidget {
        public WorldTypeListWidget(WorldTypeListScreen worldTypeListScreen) {
            super(worldTypeListScreen.minecraft, worldTypeListScreen.width, worldTypeListScreen.height, 32, worldTypeListScreen.height - 55 + 4, 36);
        }

        protected int getEntryCount() {
            List<WorldTypeEntry> var1 = WorldTypes.getList();
            return var1.size();
        }

        protected void entryClicked(int index, boolean doubleClick) {
            List<WorldTypeEntry> var3 = WorldTypes.getList();
            WorldTypeListScreen.selectWorldType(var3.get(index));

            StringOptionStorage optionStorage = (StringOptionStorage) WorldTypeListScreen.this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION);
            if (!optionStorage.value.equals(var3.get(index).name)) {
                WorldTypeListScreen.this.bwoWorldPropertiesStorage.setOptionValue("WorldType", OptionType.GENERAL_OPTION, new StringOptionStorage("WorldType", var3.get(index).name));

                Map<String, OptionEntry> worldTypeOptions = WorldTypes.getWorldTypeByName(var3.get(index).name).worldTypeOptions;
                if (worldTypeOptions != null) {
                    Map<String, OptionStorage> worldTypeOptionsMap = new LinkedHashMap<>();

                    for(OptionEntry option : worldTypeOptions.values()) {
                        if (option instanceof StringOptionEntry stringOption) {
                            worldTypeOptionsMap.put(stringOption.name, new StringOptionStorage(stringOption.name, stringOption.defaultValue));

                            if (stringOption.stringList != null) {
                                WorldTypeListScreen.this.bwoWorldPropertiesStorage.setSelectedValue(option.name, OptionType.WORLD_TYPE_OPTION, 0);
                            }
                        } else if (option instanceof BooleanOptionEntry booleanOption) {
                            worldTypeOptionsMap.put(booleanOption.name, new BooleanOptionStorage(booleanOption.name, booleanOption.defaultValue));
                        } else if (option instanceof IntOptionEntry intOption) {
                            worldTypeOptionsMap.put(intOption.name, new IntOptionStorage(intOption.name, intOption.defaultValue));
                        }
                    }

                    WorldTypeListScreen.this.bwoWorldPropertiesStorage.setOptionsMap(worldTypeOptionsMap, OptionType.WORLD_TYPE_OPTION);
                }

                WorldTypeListScreen.this.doneButton.text = WorldTypeListScreen.this.translation.get("gui.done");
            }
        }

        protected boolean isSelectedEntry(int index) {
            List<WorldTypeEntry> var2 = WorldTypes.getList();
            return WorldTypeListScreen.selectedWorldType == var2.get(index);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 36;
        }

        protected void renderBackground() {
            WorldTypeListScreen.this.renderBackground();
        }

        protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
            WorldTypeEntry var1 = WorldTypes.getList().get(index);

            GL11.glBindTexture(3553, WorldTypeListScreen.this.minecraft.textureManager.getTextureId(Objects.requireNonNullElse(var1.icon, "/gui/unknown_pack.png")));

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.startQuads();
            tessellator.color(16777215);
            tessellator.vertex(x, y + i, 0.0F, 0.0F, 1.0F);
            tessellator.vertex(x + 32, y + i, 0.0F, 1.0F, 1.0F);
            tessellator.vertex(x + 32, y, 0.0F, 1.0F, 0.0F);
            tessellator.vertex(x, y, 0.0F, 0.0F, 0.0F);
            tessellator.draw();

            WorldTypeListScreen.this.drawTextWithShadow(WorldTypeListScreen.this.minecraft.textRenderer, var1.displayName, x + 32 + 2, y + 1, 16777215);

            for (int var2 = 0; var2 < var1.description.length; ++var2) {
                WorldTypeListScreen.this.drawTextWithShadow(WorldTypeListScreen.this.minecraft.textRenderer, var1.description[var2], x + 32 + 2, y + (12 * (var2 + 1)), 8421504);
            }
        }
    }
}