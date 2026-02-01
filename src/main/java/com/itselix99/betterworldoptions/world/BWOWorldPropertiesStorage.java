package com.itselix99.betterworldoptions.world;

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
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.event.TextureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class BWOWorldPropertiesStorage {
    private static BWOWorldPropertiesStorage INSTANCE = new BWOWorldPropertiesStorage();
    private static boolean initOldTextures = false;

    private Map<String, OptionStorage> generalOptions = new LinkedHashMap<>();
    private Map<String, OptionStorage> worldTypeOptions = new LinkedHashMap<>();

    private final Map<String, Integer> selectedGeneralOption = new LinkedHashMap<>();
    private final Map<String, Integer> selectedWorldTypeOption = new LinkedHashMap<>();

    public boolean isBWOServer = false;

    public boolean oldTextures;

    public BWOWorldPropertiesStorage() {
        this.initOldTextures();

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
            WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));
            if (worldType.worldTypeOptions != null && worldType.worldTypeOptions.containsKey(optionName)) {
                return ((StringOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new StringOptionStorage(optionName, ((StringOptionEntry) worldType.worldTypeOptions.get(optionName)).defaultValue))).value;
            }
        }

        return "";
    }

    public boolean getBooleanOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((BooleanOptionStorage) this.generalOptions.getOrDefault(optionName, new BooleanOptionStorage(optionName, ((BooleanOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION){
            WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));
            if (worldType.worldTypeOptions != null && worldType.worldTypeOptions.containsKey(optionName)) {
                return ((BooleanOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new BooleanOptionStorage(optionName, ((BooleanOptionEntry) worldType.worldTypeOptions.get(optionName)).defaultValue))).value;
            }
        }

        return false;
    }

    public int getIntOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((IntOptionStorage) this.generalOptions.getOrDefault(optionName, new IntOptionStorage(optionName, ((IntOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION){
            WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));
            if (worldType.worldTypeOptions != null && worldType.worldTypeOptions.containsKey(optionName)) {
                return ((IntOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new IntOptionStorage(optionName, ((IntOptionEntry) worldType.worldTypeOptions.get(optionName)).defaultValue))).value;
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

    @Environment(EnvType.CLIENT)
    public void setOldTextures(boolean bl) {
        this.oldTextures = bl;
    }

    private void initOldTextures() {
        if (!initOldTextures) {
            initOldTextures = true;

            WorldTypeEntry Alpha120 = WorldTypes.getWorldTypeByName("Alpha 1.2.0");
            Alpha120.oldTextures.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
            Alpha120.oldTextures.put("Cobblestone", TextureListener.alphaCobblestone);

            WorldTypeEntry Alpha112 = WorldTypes.getWorldTypeByName("Alpha 1.1.2_01");
            Alpha112.oldTextures.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
            Alpha112.oldTextures.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
            Alpha112.oldTextures.put("Cobblestone", TextureListener.alphaCobblestone);
            Alpha112.oldTextures.put("IronBlockTop", TextureListener.alphaIronBlock);
            Alpha112.oldTextures.put("IronBlockSide", TextureListener.alphaIronBlockSide);
            Alpha112.oldTextures.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
            Alpha112.oldTextures.put("GoldBlockTop", TextureListener.alphaGoldBlock);
            Alpha112.oldTextures.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
            Alpha112.oldTextures.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
            Alpha112.oldTextures.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
            Alpha112.oldTextures.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
            Alpha112.oldTextures.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
            Alpha112.oldTextures.put("Grass", TextureListener.alphaTallGrass);
            Alpha112.oldTextures.put("Fern", TextureListener.alphaFern);
            Alpha112.oldTextures.put("Leaves", TextureListener.alphaLeaves);
            Alpha112.oldTextures.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
            Alpha112.oldTextures.put("FurnaceTop", Block.STONE.textureId);

            WorldTypeEntry Infdev611 = WorldTypes.getWorldTypeByName("Infdev 611");
            addOldTexturesForInfdevAndIndev(Infdev611);

            WorldTypeEntry Infdev420 = WorldTypes.getWorldTypeByName("Infdev 420");
            addOldTexturesForInfdevAndIndev(Infdev420);

            WorldTypeEntry Infdev415 = WorldTypes.getWorldTypeByName("Infdev 415");
            addOldTexturesForInfdevAndIndev(Infdev415);

            WorldTypeEntry EarlyInfdev = WorldTypes.getWorldTypeByName("Early Infdev");
            addOldTexturesForInfdevAndIndev(EarlyInfdev);

            WorldTypeEntry Indev223 = WorldTypes.getWorldTypeByName("Indev 223");
            addOldTexturesForInfdevAndIndev(Indev223);

            WorldTypeEntry MCPE = WorldTypes.getWorldTypeByName("MCPE");
            MCPE.oldTextures.put("GrassBlockSide", TextureListener.mcpeGrassBlockSide);
            MCPE.oldTextures.put("Leaves", TextureListener.alphaLeaves);
            MCPE.oldTextures.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
            MCPE.oldTextures.put("Rose", TextureListener.mcpeRose);
            MCPE.oldTextures.put("IceBlock", TextureListener.mcpeIceBlock);
        }
    }

    private void addOldTexturesForInfdevAndIndev(WorldTypeEntry worldType) {
        worldType.oldTextures.put("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        worldType.oldTextures.put("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        worldType.oldTextures.put("Cobblestone", TextureListener.alphaCobblestone);
        worldType.oldTextures.put("IronBlockTop", TextureListener.alphaIronBlock);
        worldType.oldTextures.put("IronBlockSide", TextureListener.alphaIronBlockSide);
        worldType.oldTextures.put("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        worldType.oldTextures.put("GoldBlockTop", TextureListener.alphaGoldBlock);
        worldType.oldTextures.put("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        worldType.oldTextures.put("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        worldType.oldTextures.put("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        worldType.oldTextures.put("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        worldType.oldTextures.put("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        worldType.oldTextures.put("Grass", TextureListener.alphaTallGrass);
        worldType.oldTextures.put("Fern", TextureListener.alphaFern);
        worldType.oldTextures.put("Leaves", TextureListener.alphaLeaves);
        worldType.oldTextures.put("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        worldType.oldTextures.put("FurnaceTop", Block.STONE.textureId);
        worldType.oldTextures.put("BrickBlock", TextureListener.infdevBricksBlock);
    }

    public static double[] getClimateForBiome(Biome biome) {
        if (biome == Biome.TUNDRA) {
            return new double[]{0.05D, 0.2D};
        } else if (biome == Biome.SAVANNA) {
            return new double[]{0.7D, 0.1D};
        } else if (biome == Biome.DESERT) {
            return new double[]{1.0D, 0.05D};
        } else if (biome == Biome.SWAMPLAND) {
            return new double[]{0.6D, 0.8D};
        } else if (biome == Biome.TAIGA) {
            return new double[]{0.4D, 0.4D};
        } else if (biome == Biome.SHRUBLAND) {
            return new double[]{0.8D, 0.3D};
        } else if (biome == Biome.FOREST) {
            return new double[]{0.8D, 0.6D};
        } else if (biome == Biome.PLAINS) {
            return new double[]{1.0D, 0.4D};
        } else if (biome == Biome.SEASONAL_FOREST) {
            return new double[]{1.0D, 0.7D};
        } else if (biome == Biome.RAINFOREST) {
            return new double[]{1.0D, 0.85D};
        } else if (biome == Biome.ICE_DESERT) {
            return new double[]{0.0D, 0.0D};
        }

        return new double[]{0.5D, 0.5D};
    }
}