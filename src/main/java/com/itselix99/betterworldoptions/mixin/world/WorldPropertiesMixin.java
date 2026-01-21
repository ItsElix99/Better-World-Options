package com.itselix99.betterworldoptions.mixin.world;

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
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(net.minecraft.world.WorldProperties.class)
public class WorldPropertiesMixin implements BWOProperties {
    @Unique private Map<String, OptionStorage> generalOptions = new LinkedHashMap<>();
    @Unique private Map<String, OptionStorage> worldTypeOptions = new LinkedHashMap<>();

    @Override public void bwo_setWorldType(String name) {
        this.generalOptions.put("WorldType", new StringOptionStorage("WorldType", name));
    }

    @Override public String bwo_getWorldType() {
        return bwo_getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
    }

    @Override public void bwo_setHardcore(boolean hardcore) {
        this.generalOptions.put("Hardcore", new BooleanOptionStorage("Hardcore", hardcore));
    }

    @Override public boolean bwo_isHardcore() {
        return bwo_getBooleanOptionValue("Hardcore", OptionType.GENERAL_OPTION);
    }

    @Override public boolean bwo_isOldFeatures() {
        return bwo_getBooleanOptionValue("OldFeatures", OptionType.GENERAL_OPTION);
    }

    @Override public String bwo_getSingleBiome() {
        return bwo_getStringOptionValue("SingleBiome", OptionType.GENERAL_OPTION);
    }

    @Override public String bwo_getTheme() {
        return bwo_getStringOptionValue("Theme", OptionType.GENERAL_OPTION);
    }

    @Override
    public String bwo_getStringOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((StringOptionStorage) this.generalOptions.getOrDefault(optionName, new StringOptionStorage(optionName, ((StringOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            if (!this.worldTypeOptions.isEmpty()) {
                WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.bwo_getWorldType());
                if (worldType.worldTypeOptions.containsKey(optionName)) {
                    return ((StringOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new StringOptionStorage(optionName, ((StringOptionEntry) worldType.worldTypeOptions.get(optionName)).defaultValue))).value;
                }
            }
        }

        return "";
    }

    @Override
    public boolean bwo_getBooleanOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((BooleanOptionStorage) this.generalOptions.getOrDefault(optionName, new BooleanOptionStorage(optionName, ((BooleanOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            if (!this.worldTypeOptions.isEmpty()) {
                WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.bwo_getWorldType());
                if (worldType.worldTypeOptions.containsKey(optionName)) {
                    return ((BooleanOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new BooleanOptionStorage(optionName, ((BooleanOptionEntry) worldType.worldTypeOptions.get(optionName)).defaultValue))).value;
                }
            }
        }

        return false;
    }

    @Override
    public int bwo_getIntOptionValue(String optionName, OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return ((IntOptionStorage) this.generalOptions.getOrDefault(optionName, new IntOptionStorage(optionName, ((IntOptionEntry) GeneralOptions.getOptionByName(optionName)).defaultValue))).value;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            if (!this.worldTypeOptions.isEmpty()) {
                WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(this.bwo_getWorldType());
                if (worldType.worldTypeOptions.containsKey(optionName)) {
                    return ((IntOptionStorage) this.worldTypeOptions.getOrDefault(optionName, new IntOptionStorage(optionName, ((IntOptionEntry) worldType.worldTypeOptions.get(optionName)).defaultValue))).value;
                }
            }
        }

        return 0;
    }

    @Override
    public Map<String, OptionStorage> bwo_getOptionsMap(OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return this.generalOptions;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            return this.worldTypeOptions;
        }

        return null;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void bwo_loadFromNbt(NbtCompound nbt, CallbackInfo ci) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = new BWOWorldPropertiesStorage();

        if (nbt.contains("BetterWorldOptions")) {
            NbtCompound betterWorldOptionsNbt = nbt.getCompound("BetterWorldOptions");

            for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
                if (GeneralOptions.getOptionByName(option.name).save) {
                    if (option instanceof StringOptionStorage) {
                        this.generalOptions.put(option.name, new StringOptionStorage(option.name, betterWorldOptionsNbt.getString(option.name)));
                    } else if (option instanceof BooleanOptionStorage) {
                        this.generalOptions.put(option.name, new BooleanOptionStorage(option.name, betterWorldOptionsNbt.getBoolean(option.name)));
                    } else if (option instanceof IntOptionStorage) {
                        this.generalOptions.put(option.name, new IntOptionStorage(option.name, betterWorldOptionsNbt.getInt(option.name)));
                    }
                }
            }

            bwoWorldPropertiesStorage.setOptionsMap(this.generalOptions, OptionType.GENERAL_OPTION);

            Map<String, OptionEntry> worldTypeOptions = WorldTypes.getWorldTypeByName(betterWorldOptionsNbt.getString("WorldType")).worldTypeOptions;
            if (worldTypeOptions != null) {
                NbtCompound worldTypeOptionsNbt = betterWorldOptionsNbt.getCompound("WorldTypeOptions");

                Map<String, OptionStorage> worldTypeOptionsMap = new LinkedHashMap<>();

                for (OptionEntry option : worldTypeOptions.values()) {
                    if (option instanceof StringOptionEntry) {
                        worldTypeOptionsMap.put(option.name, new StringOptionStorage(option.name, worldTypeOptionsNbt.getString(option.name)));
                    } else if (option instanceof BooleanOptionEntry) {
                        worldTypeOptionsMap.put(option.name, new BooleanOptionStorage(option.name, worldTypeOptionsNbt.getBoolean(option.name)));
                    } else if (option instanceof IntOptionEntry) {
                        worldTypeOptionsMap.put(option.name, new IntOptionStorage(option.name, worldTypeOptionsNbt.getInt(option.name)));
                    }
                }

                bwoWorldPropertiesStorage.setOptionsMap(worldTypeOptionsMap, OptionType.WORLD_TYPE_OPTION);

                for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                    if (option instanceof StringOptionStorage stringOption) {
                        this.worldTypeOptions.put(option.name, new StringOptionStorage(option.name, stringOption.value));
                    } else if (option instanceof BooleanOptionStorage booleanOption) {
                        this.worldTypeOptions.put(option.name, new BooleanOptionStorage(option.name, booleanOption.value));
                    } else if (option instanceof IntOptionStorage intOption) {
                        this.worldTypeOptions.put(option.name, new IntOptionStorage(option.name, intOption.value));
                    }
                }

                bwoWorldPropertiesStorage.setOptionsMap(this.worldTypeOptions, OptionType.WORLD_TYPE_OPTION);
            }

            BWOWorldPropertiesStorage.setInstance(bwoWorldPropertiesStorage);
        }
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void bwo_initBWOProperties(long seed, String name, CallbackInfo ci) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
            if (GeneralOptions.getOptionByName(option.name).save) {
                if (option instanceof StringOptionStorage stringOption) {
                    this.generalOptions.put(option.name, new StringOptionStorage(option.name, stringOption.value));
                } else if (option instanceof BooleanOptionStorage booleanOption) {
                    this.generalOptions.put(option.name, new BooleanOptionStorage(option.name, booleanOption.value));
                } else if (option instanceof IntOptionStorage intOption) {
                    this.generalOptions.put(option.name, new IntOptionStorage(option.name, intOption.value));
                }
            }
        }

        if (WorldTypes.getWorldTypeByName(((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value).worldTypeOptions != null) {
            for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                if (option instanceof StringOptionStorage stringOption) {
                    this.worldTypeOptions.put(option.name, new StringOptionStorage(option.name, stringOption.value));
                } else if (option instanceof BooleanOptionStorage booleanOption) {
                    this.worldTypeOptions.put(option.name, new BooleanOptionStorage(option.name, booleanOption.value));
                } else if (option instanceof IntOptionStorage intOption) {
                    this.worldTypeOptions.put(option.name, new IntOptionStorage(option.name, intOption.value));
                }
            }
        }
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void bwo_updateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        NbtCompound betterWorldOptionsNbt = new NbtCompound();
        NbtCompound worldTypeOptionsNbt = new NbtCompound();

        if (this.generalOptions.isEmpty()) {
            this.generalOptions = BWOWorldPropertiesStorage.getInstance().getOptionsMap(OptionType.GENERAL_OPTION);
        }

        for (OptionStorage option : this.generalOptions.values()) {
            if (option instanceof StringOptionStorage stringOption) {
                betterWorldOptionsNbt.putString(option.name, stringOption.value);
            } else if (option instanceof BooleanOptionStorage booleanOption) {
                betterWorldOptionsNbt.putBoolean(option.name, booleanOption.value);
            } else if (option instanceof IntOptionStorage intOption) {
                betterWorldOptionsNbt.putInt(option.name, intOption.value);
            }
        }

        if (WorldTypes.getWorldTypeByName(bwo_getWorldType()).worldTypeOptions != null) {
            for (OptionStorage option : this.worldTypeOptions.values()) {
                if (option instanceof StringOptionStorage stringOption) {
                    worldTypeOptionsNbt.putString(option.name, stringOption.value);
                } else if (option instanceof BooleanOptionStorage booleanOption) {
                    worldTypeOptionsNbt.putBoolean(option.name, booleanOption.value);
                } else if (option instanceof IntOptionStorage intOption) {
                    worldTypeOptionsNbt.putInt(option.name, intOption.value);
                }
            }

            betterWorldOptionsNbt.put("WorldTypeOptions", worldTypeOptionsNbt);
        }

        betterWorldOptionsNbt.putString("BWOVersion", "0.4.0");
        nbt.put("BetterWorldOptions", betterWorldOptionsNbt);
    }
}


