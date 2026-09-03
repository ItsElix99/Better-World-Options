package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
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

    private Map<String, OptionStorage<?>> generalOptions = new LinkedHashMap<>();
    private Map<String, OptionStorage<?>> worldTypeOptions = new LinkedHashMap<>();

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