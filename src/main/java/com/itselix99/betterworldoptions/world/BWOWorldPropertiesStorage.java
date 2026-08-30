package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.IntOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.IntOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.event.TextureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.*;

public class BWOWorldPropertiesStorage {
    private static BWOWorldPropertiesStorage INSTANCE = new BWOWorldPropertiesStorage();
    private static boolean init = false;
    private static boolean initDimensionWorldTypes = false;

    private Map<String, OptionStorage> generalOptions = new LinkedHashMap<>();
    private Map<String, OptionStorage> worldTypeOptions = new LinkedHashMap<>();

    private final Map<String, Integer> selectedGeneralOption = new LinkedHashMap<>();
    private final Map<String, Integer> selectedWorldTypeOption = new LinkedHashMap<>();

    private static HashMap<Biome, double[]> biomeClimateMap;

    public boolean isBWOServer = false;

    public boolean oldTextures;

    public static String BWOWorldVersion = null;

    public BWOWorldPropertiesStorage() {
        if (!init) {
            this.initBiomeClimateAndOldTextures();
            init = true;
        }

        List<OptionEntry> generalOptionsList = GeneralOptions.getList();

        for(OptionEntry generalOption : generalOptionsList) {
            if (generalOption instanceof StringOptionEntry stringGeneralOption) {
                this.generalOptions.put(generalOption.name, new StringOptionStorage(stringGeneralOption.name, stringGeneralOption.defaultValue));

                if (stringGeneralOption.stringList != null) {
                    this.selectedGeneralOption.put(generalOption.name, stringGeneralOption.ordinalDefaultValue);
                }
            } else if (generalOption instanceof BooleanOptionEntry booleanGeneralOption) {
                this.generalOptions.put(generalOption.name, new BooleanOptionStorage(booleanGeneralOption.name, booleanGeneralOption.defaultValue));
            } else if (generalOption instanceof IntOptionEntry intGeneralOption) {
                this.generalOptions.put(generalOption.name, new IntOptionStorage(intGeneralOption.name, intGeneralOption.defaultValue));
            }
        }
    }

    public static BWOWorldPropertiesStorage setInstance(BWOWorldPropertiesStorage bwoWorldPropertiesStorage) {
        return INSTANCE = bwoWorldPropertiesStorage;
    }

    public static BWOWorldPropertiesStorage getInstance() {
        return INSTANCE;
    }

    public void setOptionsMap(Map<String, OptionStorage> options, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            this.generalOptions = new LinkedHashMap<>(options);
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            this.worldTypeOptions = new LinkedHashMap<>(options);
        }
    }

    public Map<String, OptionStorage> getOptionsMap(OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return this.generalOptions;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            return this.worldTypeOptions;
        }

        return null;
    }

    public void setStringOptionValue(String optionName, OptionType optionType, String value) {
        if (optionType == OptionType.GENERAL_OPTION) {
            this.generalOptions.put(optionName, new StringOptionStorage(optionName, value));
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            this.worldTypeOptions.put(optionName, new StringOptionStorage(optionName, value));
        }
    }

    public void setBooleanOptionValue(String optionName, OptionType optionType, boolean value) {
        if (optionType == OptionType.GENERAL_OPTION) {
            this.generalOptions.put(optionName, new BooleanOptionStorage(optionName, value));
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            this.worldTypeOptions.put(optionName, new BooleanOptionStorage(optionName, value));
        }
    }

    public void setIntOptionValue(String optionName, OptionType optionType, int value) {
        if (optionType == OptionType.GENERAL_OPTION) {
            this.generalOptions.put(optionName, new IntOptionStorage(optionName, value));
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            this.worldTypeOptions.put(optionName, new IntOptionStorage(optionName, value));
        }
    }

    public String getStringOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((StringOptionStorage) this.generalOptions.getOrDefault(optionName, new StringOptionStorage(optionName, ((StringOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION){
            WorldType worldType = WorldType.getWorldTypeById(Identifier.of(this.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION)));
            if (!worldType.getWorldTypeOptions().isEmpty() && worldType.getWorldTypeOptions().containsKey(optionName)) {
                return ((StringOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new StringOptionStorage(optionName, ((StringOptionEntry) worldType.getWorldTypeOptions().get(optionName)).defaultValue))).value;
            }
        }

        return "";
    }

    public boolean getBooleanOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((BooleanOptionStorage) this.generalOptions.getOrDefault(optionName, new BooleanOptionStorage(optionName, ((BooleanOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION){
            WorldType worldType = WorldType.getWorldTypeById(Identifier.of(this.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION)));
            if (!worldType.getWorldTypeOptions().isEmpty() && worldType.getWorldTypeOptions().containsKey(optionName)) {
                return ((BooleanOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new BooleanOptionStorage(optionName, ((BooleanOptionEntry) worldType.getWorldTypeOptions().get(optionName)).defaultValue))).value;
            }
        }

        return false;
    }

    public int getIntOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((IntOptionStorage) this.generalOptions.getOrDefault(optionName, new IntOptionStorage(optionName, ((IntOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION){
            WorldType worldType = WorldType.getWorldTypeById(Identifier.of(this.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION)));
            if (!worldType.getWorldTypeOptions().isEmpty() && worldType.getWorldTypeOptions().containsKey(optionName)) {
                return ((IntOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new IntOptionStorage(optionName, ((IntOptionEntry) worldType.getWorldTypeOptions().get(optionName)).defaultValue))).value;
            }
        }

        return 0;
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

    public void resetGeneralOptionToDefaultValue(OptionEntry option) {
        if (option.optionType == OptionType.GENERAL_OPTION) {
            if (option instanceof StringOptionEntry stringOption) {
                this.generalOptions.put(stringOption.name, new StringOptionStorage(stringOption.name, stringOption.defaultValue));
                this.setSelectedValue(stringOption.name, stringOption.optionType, stringOption.ordinalDefaultValue);
            } else if (option instanceof BooleanOptionEntry booleanOption) {
                this.generalOptions.put(booleanOption.name, new BooleanOptionStorage(booleanOption.name, booleanOption.defaultValue));

                if (!option.dependentOptions.isEmpty()) {
                    this.resetDependentOptionsToDefaultValue(option);
                }
            } else if (option instanceof IntOptionEntry intOption) {
                this.generalOptions.put(intOption.name, new IntOptionStorage(intOption.name, intOption.defaultValue));
            }
        }
    }

    public void resetDependentOptionsToDefaultValue(OptionEntry option) {
        if (option.optionType == OptionType.GENERAL_OPTION) {
            for (OptionEntry linkedOption : option.dependentOptions) {
                if (linkedOption instanceof StringOptionEntry stringOption) {
                    this.generalOptions.put(stringOption.name, new StringOptionStorage(stringOption.name, stringOption.defaultValue));
                    this.setSelectedValue(stringOption.name, stringOption.optionType, stringOption.ordinalDefaultValue);
                } else if (linkedOption instanceof BooleanOptionEntry booleanOption) {
                    this.generalOptions.put(booleanOption.name, new BooleanOptionStorage(booleanOption.name, booleanOption.defaultValue));
                } else if (linkedOption instanceof IntOptionEntry intOption) {
                    this.generalOptions.put(intOption.name, new IntOptionStorage(intOption.name, intOption.defaultValue));
                }
            }
        } else if (option.optionType == OptionType.WORLD_TYPE_OPTION) {
            for (OptionEntry linkedOption : option.dependentOptions) {
                if (linkedOption instanceof StringOptionEntry stringOption) {
                    this.worldTypeOptions.put(stringOption.name, new StringOptionStorage(stringOption.name, stringOption.defaultValue));
                    this.setSelectedValue(stringOption.name, stringOption.optionType, stringOption.ordinalDefaultValue);
                } else if (linkedOption instanceof BooleanOptionEntry booleanOption) {
                    this.worldTypeOptions.put(booleanOption.name, new BooleanOptionStorage(booleanOption.name, booleanOption.defaultValue));
                } else if (linkedOption instanceof IntOptionEntry intOption) {
                    this.worldTypeOptions.put(intOption.name, new IntOptionStorage(intOption.name, intOption.defaultValue));
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
                }
            }

            initDimensionWorldTypes = true;
        }
    }

    private void initBiomeClimateAndOldTextures() {
        if (biomeClimateMap == null) {
            biomeClimateMap = new HashMap<>();
        }

        biomeClimateMap.put(Biome.TUNDRA, new double[]{0.05D, 0.2D});
        biomeClimateMap.put(Biome.SAVANNA, new double[]{0.7D, 0.1D});
        biomeClimateMap.put(Biome.DESERT, new double[]{1.0D, 0.05D});
        biomeClimateMap.put(Biome.SWAMPLAND, new double[]{0.6D, 0.8D});
        biomeClimateMap.put(Biome.TAIGA, new double[]{0.4D, 0.4D});
        biomeClimateMap.put(Biome.SHRUBLAND, new double[]{0.8D, 0.3D});
        biomeClimateMap.put(Biome.FOREST, new double[]{0.8D, 0.6D});
        biomeClimateMap.put(Biome.PLAINS, new double[]{1.0D, 0.4D});
        biomeClimateMap.put(Biome.SEASONAL_FOREST, new double[]{1.0D, 0.7D});
        biomeClimateMap.put(Biome.RAINFOREST, new double[]{1.0D, 0.85D});
        biomeClimateMap.put(Biome.ICE_DESERT, new double[]{0.05D, 0.05D});

        WorldType Alpha120 = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("alpha_1.2.0"));
        Alpha120.addOldTexture("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Alpha120.addOldTexture("Cobblestone", TextureListener.alphaCobblestone);

        WorldType Alpha112 = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("alpha_1.1.2_01"));
        Alpha112.addOldTexture("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        Alpha112.addOldTexture("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Alpha112.addOldTexture("Cobblestone", TextureListener.alphaCobblestone);
        Alpha112.addOldTexture("IronBlockTop", TextureListener.alphaIronBlock);
        Alpha112.addOldTexture("IronBlockSide", TextureListener.alphaIronBlockSide);
        Alpha112.addOldTexture("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        Alpha112.addOldTexture("GoldBlockTop", TextureListener.alphaGoldBlock);
        Alpha112.addOldTexture("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        Alpha112.addOldTexture("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        Alpha112.addOldTexture("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        Alpha112.addOldTexture("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        Alpha112.addOldTexture("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        Alpha112.addOldTexture("Grass", TextureListener.alphaTallGrass);
        Alpha112.addOldTexture("Fern", TextureListener.alphaFern);
        Alpha112.addOldTexture("Leaves", TextureListener.alphaLeaves);
        Alpha112.addOldTexture("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        Alpha112.addOldTexture("FurnaceTop", Block.STONE.textureId);

        WorldType Infdev611 = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("infdev_20100611"));
        addOldTexturesForInfdevAndIndev(Infdev611);

        WorldType Infdev420 = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("infdev_20100420"));
        addOldTexturesForInfdevAndIndev(Infdev420);

        WorldType Infdev415 = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("infdev_20100415"));
        addOldTexturesForInfdevAndIndev(Infdev415);

        WorldType EarlyInfdev = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("early_infdev"));
        addOldTexturesForInfdevAndIndev(EarlyInfdev);

        WorldType Indev223 = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("indev_20100223"));
        addOldTexturesForInfdevAndIndev(Indev223);

        WorldType MCPE = WorldType.getWorldTypeById(BetterWorldOptions.NAMESPACE.id("mcpe"));
        MCPE.addOldTexture("GrassBlockSide", TextureListener.mcpeGrassBlockSide);
        MCPE.addOldTexture("Leaves", TextureListener.alphaLeaves);
        MCPE.addOldTexture("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        MCPE.addOldTexture("Rose", TextureListener.mcpeRose);
        MCPE.addOldTexture("IceBlock", TextureListener.mcpeIceBlock);
    }

    private void addOldTexturesForInfdevAndIndev(WorldType worldType) {
        worldType.addOldTexture("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        worldType.addOldTexture("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        worldType.addOldTexture("Cobblestone", TextureListener.alphaCobblestone);
        worldType.addOldTexture("IronBlockTop", TextureListener.alphaIronBlock);
        worldType.addOldTexture("IronBlockSide", TextureListener.alphaIronBlockSide);
        worldType.addOldTexture("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        worldType.addOldTexture("GoldBlockTop", TextureListener.alphaGoldBlock);
        worldType.addOldTexture("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        worldType.addOldTexture("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        worldType.addOldTexture("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        worldType.addOldTexture("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        worldType.addOldTexture("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        worldType.addOldTexture("Grass", TextureListener.alphaTallGrass);
        worldType.addOldTexture("Fern", TextureListener.alphaFern);
        worldType.addOldTexture("Leaves", TextureListener.alphaLeaves);
        worldType.addOldTexture("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        worldType.addOldTexture("FurnaceTop", Block.STONE.textureId);
        worldType.addOldTexture("BrickBlock", TextureListener.infdevBricksBlock);
    }

    public static double[] getClimateForBiome(Biome biome) {
        return biomeClimateMap.getOrDefault(biome, new double[]{0.5D, 0.5D});
    }
}