package com.itselix99.betterworldoptions.gui.screen;

import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Identifier;
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
    private static WorldType selectedWorldType;

    public WorldTypeListScreen(Screen parent, BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        this.parent = parent;
        this.bwoWorldPropertiesStorage = bwoWorldPropertiesStorage;

        BWOWorldPropertiesStorage.initDimensionWorldTypes();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.worldTypeListWidget = new WorldTypeListWidget(this);
        this.worldTypeListWidget.registerButtons(this.buttons, 4, 5);
        this.buttons.add(this.doneButton = new ButtonWidget(0, this.width / 2 - 75, this.height - 28, 150, 20, translation.get("gui.cancel")));

        String currentWorldType = this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");
        selectedWorldType = WorldType.getWorldTypeById(Identifier.of(currentWorldType));
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

    public static void selectWorldType(WorldType var1) {
        selectedWorldType = var1;
    }

    @Environment(EnvType.CLIENT)
    class WorldTypeListWidget extends EntryListWidget {
        public WorldTypeListWidget(WorldTypeListScreen worldTypeListScreen) {
            super(worldTypeListScreen.minecraft, worldTypeListScreen.width, worldTypeListScreen.height, 32, worldTypeListScreen.height - 55 + 4, 36);
        }

        protected int getEntryCount() {
            List<WorldType> var1 = WorldType.getWorldTypeList();
            return var1.size();
        }

        protected void entryClicked(int index, boolean doubleClick) {
            List<WorldType> var3 = WorldType.getWorldTypeList();
            WorldTypeListScreen.selectWorldType(var3.get(index));

            String currentWorldType = WorldTypeListScreen.this.bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");
            if (!currentWorldType.equals(var3.get(index).getId().toString())) {
                WorldTypeListScreen.this.bwoWorldPropertiesStorage.setOptionValue("WorldType", OptionType.GENERAL_OPTION, var3.get(index).getId().toString());

                Map<String, Option<?>> worldTypeOptions = WorldType.getWorldTypeById(var3.get(index).getId()).getWorldTypeOptions();
                if (worldTypeOptions != null) {
                    Map<String, OptionStorage<?>> worldTypeOptionsMap = new LinkedHashMap<>();

                    for(Option<?> option : worldTypeOptions.values()) {
                        if (option instanceof StringOption stringOption) {
                            worldTypeOptionsMap.put(stringOption.getName(), new OptionStorage<>(stringOption.getName(), stringOption.getDefaultValue()));

                            if (stringOption.getValues(Identifier.of("")) != null) {
                                WorldTypeListScreen.this.bwoWorldPropertiesStorage.setSelectedValue(option.getName(), OptionType.WORLD_TYPE_OPTION, stringOption.getOrdinalDefaultValue());
                            }
                        } else if (option instanceof BooleanOption booleanOption) {
                            worldTypeOptionsMap.put(booleanOption.getName(), new OptionStorage<>(booleanOption.getName(), booleanOption.getDefaultValue()));
                        } else if (option instanceof IntOption intOption) {
                            worldTypeOptionsMap.put(intOption.getName(), new OptionStorage<>(intOption.getName(), intOption.getDefaultValue()));
                        }
                    }

                    WorldTypeListScreen.this.bwoWorldPropertiesStorage.setOptionsMap(worldTypeOptionsMap, OptionType.WORLD_TYPE_OPTION);
                }

                WorldTypeListScreen.this.doneButton.text = WorldTypeListScreen.this.translation.get("gui.done");
            }
        }

        protected boolean isSelectedEntry(int index) {
            List<WorldType> var2 = WorldType.getWorldTypeList();
            return WorldTypeListScreen.selectedWorldType == var2.get(index);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 36;
        }

        protected void renderBackground() {
            WorldTypeListScreen.this.renderBackground();
        }

        protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
            WorldType var1 = WorldType.getWorldTypeList().get(index);

            GL11.glBindTexture(3553, WorldTypeListScreen.this.minecraft.textureManager.getTextureId(Objects.requireNonNullElse(var1.getIcon(), "/gui/unknown_pack.png")));

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.startQuads();
            tessellator.color(16777215);
            tessellator.vertex(x, y + i, 0.0F, 0.0F, 1.0F);
            tessellator.vertex(x + 32, y + i, 0.0F, 1.0F, 1.0F);
            tessellator.vertex(x + 32, y, 0.0F, 1.0F, 0.0F);
            tessellator.vertex(x, y, 0.0F, 0.0F, 0.0F);
            tessellator.draw();

            WorldTypeListScreen.this.drawTextWithShadow(WorldTypeListScreen.this.minecraft.textRenderer, var1.getName(), x + 32 + 2, y + 1, 16777215);

            if (var1.getDescription() != null) {
                for (int var2 = 0; var2 < var1.getDescription().length; ++var2) {
                    WorldTypeListScreen.this.drawTextWithShadow(WorldTypeListScreen.this.minecraft.textRenderer, var1.getDescription()[var2], x + 32 + 2, y + (12 * (var2 + 1)), 8421504);
                }
            }
        }
    }
}