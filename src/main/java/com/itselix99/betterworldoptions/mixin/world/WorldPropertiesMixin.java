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
import net.minecraft.world.WorldProperties;
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
                if (!worldType.worldTypeOptions.isEmpty() && worldType.worldTypeOptions.containsKey(optionName)) {
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
                if (!worldType.worldTypeOptions.isEmpty() && worldType.worldTypeOptions.containsKey(optionName)) {
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
                if (!worldType.worldTypeOptions.isEmpty() && worldType.worldTypeOptions.containsKey(optionName)) {
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

    @Unique
    private String bwo_getStringOrDefault(NbtCompound nbt, OptionEntry option) {
        return !nbt.contains(option.name) ? ((StringOptionEntry) option).defaultValue : nbt.getString(option.name);
    }

    @Unique
    private boolean bwo_getBooleanOrDefault(NbtCompound nbt, OptionEntry option) {
        return !nbt.contains(option.name) ? ((BooleanOptionEntry) option).defaultValue : nbt.getBoolean(option.name);
    }

    @Unique
    private int bwo_getIntOrDefault(NbtCompound nbt, OptionEntry option) {
        return !nbt.contains(option.name) ? ((IntOptionEntry) option).defaultValue : nbt.getInt(option.name);
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void bwo_loadFromNbt(NbtCompound nbt, CallbackInfo ci) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = new BWOWorldPropertiesStorage();

        NbtCompound betterWorldOptionsTag = nbt.getCompound("BetterWorldOptions");

        for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
            OptionEntry generalOption = GeneralOptions.getOptionByName(option.name);

            String worldType = this.bwo_getStringOrDefault(betterWorldOptionsTag, GeneralOptions.getOptionByName("WorldType"));
            WorldTypeEntry worldTypeEntry = WorldTypes.getWorldTypeByName(worldType);

            if (generalOption.save && (generalOption.compatibleWorldTypes.contains("All") || generalOption.compatibleWorldTypes.contains(worldType) || generalOption.compatibleWorldTypes.contains("Overworld") && !worldTypeEntry.isDimension)) {
                if (option instanceof StringOptionStorage) {
                    String value = this.bwo_getStringOrDefault(betterWorldOptionsTag, generalOption);

                    if (generalOption.parentOption != null && !this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption.parentOption)) {
                        continue;
                    }

                    this.generalOptions.put(option.name, new StringOptionStorage(option.name, value));
                } else if (option instanceof BooleanOptionStorage) {
                    boolean value = this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption);

                    if (!generalOption.dependentOptions.isEmpty() && !value) {
                        continue;
                    } else if (generalOption.parentOption != null && !this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption.parentOption)) {
                        continue;
                    }

                    this.generalOptions.put(option.name, new BooleanOptionStorage(option.name, value));
                } else if (option instanceof IntOptionStorage) {
                    int value = this.bwo_getIntOrDefault(betterWorldOptionsTag, generalOption);

                    if (generalOption.parentOption != null && !this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption.parentOption)) {
                        continue;
                    }

                    this.generalOptions.put(option.name, new IntOptionStorage(option.name, value));
                }
            }
        }

        bwoWorldPropertiesStorage.setOptionsMap(this.generalOptions, OptionType.GENERAL_OPTION);

        Map<String, OptionEntry> worldTypeOptions = WorldTypes.getWorldTypeByName(this.bwo_getStringOrDefault(betterWorldOptionsTag, GeneralOptions.getOptionByName("WorldType"))).worldTypeOptions;
        if (!worldTypeOptions.isEmpty()) {
            NbtCompound worldTypeOptionsTag = betterWorldOptionsTag.getCompound("WorldTypeOptions");

            Map<String, OptionStorage> worldTypeOptionsMap = new LinkedHashMap<>();

            for (OptionEntry option : worldTypeOptions.values()) {
                if (option instanceof StringOptionEntry) {
                    String value = this.bwo_getStringOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.name));

                    if (worldTypeOptions.get(option.name).parentOption != null && !this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.name).parentOption)) {
                        continue;
                    }

                    worldTypeOptionsMap.put(option.name, new StringOptionStorage(option.name, value));
                } else if (option instanceof BooleanOptionEntry) {
                    boolean value = this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.name));

                    if (!worldTypeOptions.get(option.name).dependentOptions.isEmpty() && !value) {
                        continue;
                    } else if (worldTypeOptions.get(option.name).parentOption != null && !this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.name).parentOption)) {
                        continue;
                    }

                    worldTypeOptionsMap.put(option.name, new BooleanOptionStorage(option.name, value));
                } else if (option instanceof IntOptionEntry) {
                    int value = this.bwo_getIntOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.name));

                    if (worldTypeOptions.get(option.name).parentOption != null && !this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.name).parentOption)) {
                        continue;
                    }

                    worldTypeOptionsMap.put(option.name, new IntOptionStorage(option.name, value));
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

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void bwo_initBWOProperties(long seed, String name, CallbackInfo ci) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
            OptionEntry generalOption = GeneralOptions.getOptionByName(option.name);

            String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);
            WorldTypeEntry worldTypeEntry = WorldTypes.getWorldTypeByName(worldType);

            if (generalOption.save && (generalOption.compatibleWorldTypes.contains("All") || generalOption.compatibleWorldTypes.contains(worldType) || generalOption.compatibleWorldTypes.contains("Overworld") && !worldTypeEntry.isDimension)) {
                if (generalOption.parentOption != null && !bwoWorldPropertiesStorage.getBooleanOptionValue(generalOption.parentOption.name, generalOption.optionType)) {
                    continue;
                }

                if (option instanceof StringOptionStorage stringOption) {
                    this.generalOptions.put(option.name, new StringOptionStorage(option.name, stringOption.value));
                } else if (option instanceof BooleanOptionStorage booleanOption) {
                    if (!generalOption.dependentOptions.isEmpty() && !booleanOption.value) {
                        continue;
                    }

                    this.generalOptions.put(option.name, new BooleanOptionStorage(option.name, booleanOption.value));
                } else if (option instanceof IntOptionStorage intOption) {
                    this.generalOptions.put(option.name, new IntOptionStorage(option.name, intOption.value));
                }
            }
        }

        WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION));

        if (!worldType.worldTypeOptions.isEmpty()) {
            for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                if (worldType.worldTypeOptions.get(option.name).parentOption != null && !bwoWorldPropertiesStorage.getBooleanOptionValue(worldType.worldTypeOptions.get(option.name).parentOption.name, worldType.worldTypeOptions.get(option.name).optionType)) {
                    continue;
                }

                if (option instanceof StringOptionStorage stringOption) {
                    this.worldTypeOptions.put(option.name, new StringOptionStorage(option.name, stringOption.value));
                } else if (option instanceof BooleanOptionStorage booleanOption) {
                    if (!worldType.worldTypeOptions.get(option.name).dependentOptions.isEmpty() && !booleanOption.value) {
                        continue;
                    }

                    this.worldTypeOptions.put(option.name, new BooleanOptionStorage(option.name, booleanOption.value));
                } else if (option instanceof IntOptionStorage intOption) {
                    this.worldTypeOptions.put(option.name, new IntOptionStorage(option.name, intOption.value));
                }
            }
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/WorldProperties;)V", at = @At("TAIL"))
    private void bwo_copyProperties(WorldProperties worldProperties, CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) worldProperties;

        this.generalOptions = bwoProperties.bwo_getOptionsMap(OptionType.GENERAL_OPTION);
        this.worldTypeOptions = bwoProperties.bwo_getOptionsMap(OptionType.WORLD_TYPE_OPTION);
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void bwo_updateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        NbtCompound betterWorldOptionsTag = new NbtCompound();
        NbtCompound worldTypeOptionsTag = new NbtCompound();

        for (OptionStorage option : this.generalOptions.values()) {
            if (option instanceof StringOptionStorage stringOption) {
                betterWorldOptionsTag.putString(option.name, stringOption.value);
            } else if (option instanceof BooleanOptionStorage booleanOption) {
                betterWorldOptionsTag.putBoolean(option.name, booleanOption.value);
            } else if (option instanceof IntOptionStorage intOption) {
                betterWorldOptionsTag.putInt(option.name, intOption.value);
            }
        }

        if (!WorldTypes.getWorldTypeByName(bwo_getWorldType()).worldTypeOptions.isEmpty()) {
            for (OptionStorage option : this.worldTypeOptions.values()) {
                if (option instanceof StringOptionStorage stringOption) {
                    worldTypeOptionsTag.putString(option.name, stringOption.value);
                } else if (option instanceof BooleanOptionStorage booleanOption) {
                    worldTypeOptionsTag.putBoolean(option.name, booleanOption.value);
                } else if (option instanceof IntOptionStorage intOption) {
                    worldTypeOptionsTag.putInt(option.name, intOption.value);
                }
            }

            betterWorldOptionsTag.put("WorldTypeOptions", worldTypeOptionsTag);
        }

        betterWorldOptionsTag.putString("BWOVersion", "0.4.0");
        nbt.put("BetterWorldOptions", betterWorldOptionsTag);
    }
}


