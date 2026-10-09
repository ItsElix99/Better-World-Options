package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.*;

public class BWOWorldPropertiesStorage {
    private static BWOWorldPropertiesStorage INSTANCE = new BWOWorldPropertiesStorage();
    private static boolean initDimensionWorldTypes = false;

    private Map<String, OptionStorage<?>> generalOptions = new LinkedHashMap<>();
    private Map<String, OptionStorage<?>> worldTypeOptions = new LinkedHashMap<>();
    private Map<Identifier, Boolean> structures = new LinkedHashMap<>();

    private final Map<String, Integer> selectedGeneralOption = new LinkedHashMap<>();
    private final Map<String, Integer> selectedWorldTypeOption = new LinkedHashMap<>();

    public boolean isBWOServer = false;

    public boolean oldTextures;

    public static String BWOWorldVersion = null;

    public BWOWorldPropertiesStorage() {
        List<Option<?>> generalOptionsList = GeneralOptions.getGeneralOptionsList();

        for(Option<?> generalOption : generalOptionsList) {
            if (generalOption instanceof StringOption stringGeneralOption) {
                this.generalOptions.put(generalOption.getName(), new OptionStorage<>(stringGeneralOption.getName(), stringGeneralOption.getDefaultValue()));

                if (stringGeneralOption.getValues(Identifier.of("")) != null) {
                    this.selectedGeneralOption.put(generalOption.getName(), stringGeneralOption.getOrdinalDefaultValue());
                }
            } else if (generalOption instanceof BooleanOption booleanGeneralOption) {
                this.generalOptions.put(generalOption.getName(), new OptionStorage<>(booleanGeneralOption.getName(), booleanGeneralOption.getDefaultValue()));
            } else if (generalOption instanceof IntOption intGeneralOption) {
                this.generalOptions.put(generalOption.getName(), new OptionStorage<>(intGeneralOption.getName(), intGeneralOption.getDefaultValue()));
            }
        }

        for (Structure structure : Structure.getStructuresList()) {
            this.structures.put(structure.getId(), false);
        }
    }

    public static BWOWorldPropertiesStorage setInstance(BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        return INSTANCE = bwoWorldPropertiesStorage;
    }

    public static BWOWorldPropertiesStorage getInstance() {
        return INSTANCE;
    }

    public void setOptionsMap(Map<String, OptionStorage<?>> options, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            this.generalOptions = new LinkedHashMap<>(options);
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            this.worldTypeOptions = new LinkedHashMap<>(options);
        }
    }

    public Map<String, OptionStorage<?>> getOptionsMap(OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return this.generalOptions;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            return this.worldTypeOptions;
        }

        return null;
    }

    public <T> void setOptionValue(String optionName, OptionType optionType, T value) {
        OptionStorage<T> storage = new OptionStorage<>(optionName, value);

        if (optionType == OptionType.GENERAL_OPTION) {
            this.generalOptions.put(optionName, storage);
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            this.worldTypeOptions.put(optionName, storage);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getOptionValue(String optionName, OptionType optionType, T fallback) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return (T) this.generalOptions.getOrDefault(optionName, new OptionStorage<>(optionName, GeneralOptions.getGeneralOptionByName(optionName).getDefaultValue())).value();
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            WorldType worldType = WorldType.getWorldTypeById(Identifier.of(this.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "")));
            if (!worldType.getWorldTypeOptions().isEmpty() && worldType.getWorldTypeOptions().containsKey(optionName)) {
                return (T) this.worldTypeOptions.getOrDefault(optionName, new OptionStorage<>(optionName, worldType.getWorldTypeOptions().get(optionName).getDefaultValue())).value();
            }
        }

        return fallback;
    }

    public void setSelectedValue(String optionName, OptionType optionType, int value) {
        if (optionType == OptionType.GENERAL_OPTION) {
            this.selectedGeneralOption.put(optionName, value);
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            this.selectedWorldTypeOption.put(optionName, value);
        }
    }

    public int getSelectedValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return this.selectedGeneralOption.get(optionName);
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            return this.selectedWorldTypeOption.get(optionName);
        }

        return 0;
    }

    public void setStructuresMap(Map<Identifier, Boolean> structures) {
        this.structures = new LinkedHashMap<>(structures);
    }

    public void setGenerateStructure(Identifier id, boolean generate) {
        this.structures.put(id, generate);
    }

    public boolean isGenerateStructure(Identifier id) {
        return this.structures.get(id);
    }

    public Map<Identifier, Boolean> getStructures() {
        return this.structures;
    }

    public void resetGeneralOptionToDefaultValue(Option<?> option) {
        if (option.getOptionType() == OptionType.GENERAL_OPTION) {
            if (option instanceof StringOption stringOption) {
                this.generalOptions.put(stringOption.getName(), new OptionStorage<>(stringOption.getName(), stringOption.getDefaultValue()));
                this.setSelectedValue(stringOption.getName(), stringOption.getOptionType(), stringOption.getOrdinalDefaultValue());
            } else if (option instanceof BooleanOption booleanOption) {
                this.generalOptions.put(booleanOption.getName(), new OptionStorage<>(booleanOption.getName(), booleanOption.getDefaultValue()));

                if (!option.getDependentOptions().isEmpty()) {
                    this.resetDependentOptionsToDefaultValue(option);
                }
            } else if (option instanceof IntOption intOption) {
                this.generalOptions.put(intOption.getName(), new OptionStorage<>(intOption.getName(), intOption.getDefaultValue()));
            }
        }
    }

    public void resetDependentOptionsToDefaultValue(Option<?> option) {
        if (option.getOptionType() == OptionType.GENERAL_OPTION) {
            for (Option<?> linkedOption : option.getDependentOptions()) {
                if (linkedOption instanceof StringOption stringOption) {
                    this.generalOptions.put(stringOption.getName(), new OptionStorage<>(stringOption.getName(), stringOption.getDefaultValue()));
                    this.setSelectedValue(stringOption.getName(), stringOption.getOptionType(), stringOption.getOrdinalDefaultValue());
                } else if (linkedOption instanceof BooleanOption booleanOption) {
                    this.generalOptions.put(booleanOption.getName(), new OptionStorage<>(booleanOption.getName(), booleanOption.getDefaultValue()));
                } else if (linkedOption instanceof IntOption intOption) {
                    this.generalOptions.put(intOption.getName(), new OptionStorage<>(intOption.getName(), intOption.getDefaultValue()));
                }
            }
        } else if (option.getOptionType() == OptionType.WORLD_TYPE_OPTION) {
            for (Option<?> linkedOption : option.getDependentOptions()) {
                if (linkedOption instanceof StringOption stringOption) {
                    this.worldTypeOptions.put(stringOption.getName(), new OptionStorage<>(stringOption.getName(), stringOption.getDefaultValue()));
                    this.setSelectedValue(stringOption.getName(), stringOption.getOptionType(), stringOption.getOrdinalDefaultValue());
                } else if (linkedOption instanceof BooleanOption booleanOption) {
                    this.worldTypeOptions.put(booleanOption.getName(), new OptionStorage<>(booleanOption.getName(), booleanOption.getDefaultValue()));
                } else if (linkedOption instanceof IntOption intOption) {
                    this.worldTypeOptions.put(intOption.getName(), new OptionStorage<>(intOption.getName(), intOption.getDefaultValue()));
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void setOldTextures(boolean bl) {
        this.oldTextures = bl;
    }

    public static void initDimensionWorldTypes() {
        if (!initDimensionWorldTypes) {
            for (DimensionContainer<?> dimensionContainer : DimensionRegistry.INSTANCE.serialView.values()) {
                if (dimensionContainer.getLegacyID() > 1) {
                    String modName = DimensionRegistry.INSTANCE.getIdByLegacyId(dimensionContainer.getLegacyID()).get().getNamespace().toString();
                    if (modName.startsWith("mod_")) modName = modName.substring(4);

                    Map<String, String[]>[] dimensionWorldTypeInfo = WorldType.getDimensionWorldTypeInfo();
                    String[] info = dimensionWorldTypeInfo[0].getOrDefault(modName, new String[]{modName, modName, null});
                    String[] desc = dimensionWorldTypeInfo[1].getOrDefault(modName, null);

                    WorldType worldType = new WorldType(Identifier.of(Namespace.of(DimensionRegistry.INSTANCE.getIdByLegacyId(dimensionContainer.getLegacyID()).get().getNamespace().toString()), info[0]), info[1], info[2], desc);
                    worldType.setIsDimension(true);
                    worldType.setDimensionId(dimensionContainer.getLegacyID());

                    for (Structure structure : Structure.getStructuresList()) {
                        structure.addIncompatibleWorldType(Identifier.of(Namespace.of(DimensionRegistry.INSTANCE.getIdByLegacyId(dimensionContainer.getLegacyID()).get().getNamespace().toString()), info[0]));
                    }
                }
            }

            initDimensionWorldTypes = true;
        }
    }
}