package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(net.minecraft.world.WorldProperties.class)
public class WorldPropertiesMixin implements BWOProperties {
    @Unique private Map<String, OptionStorage<?>> generalOptions = new LinkedHashMap<>();
    @Unique private Map<String, OptionStorage<?>> worldTypeOptions = new LinkedHashMap<>();

    @Unique private boolean pregeneratingFiniteWorld;

    @Override public void bwo_setWorldType(String name) {
        this.generalOptions.put("WorldType", new OptionStorage<>("WorldType", name));
    }

    @Override public String bwo_getWorldType() {
        return bwo_getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");
    }

    @Override public void bwo_setHardcore(boolean hardcore) {
        this.generalOptions.put("Hardcore", new OptionStorage<>("Hardcore", hardcore));
    }

    @Override public boolean bwo_isHardcore() {
        return bwo_getOptionValue("Hardcore", OptionType.GENERAL_OPTION, false);
    }

    @Override public boolean bwo_isOldFeatures() {
        return bwo_getOptionValue("OldFeatures", OptionType.GENERAL_OPTION, false);
    }

    @Override public String bwo_getSingleBiome() {
        return bwo_getOptionValue("SingleBiome", OptionType.GENERAL_OPTION, "");
    }

    @Override public String bwo_getTheme() {
        return bwo_getOptionValue("Theme", OptionType.GENERAL_OPTION, "");
    }

    @Override public void bwo_setPregeneratingFiniteWorld(boolean isDone) {
        this.pregeneratingFiniteWorld = isDone;
    }

    @Override public boolean bwo_isPregeneratingFiniteWorld() {
        return this.pregeneratingFiniteWorld;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T bwo_getOptionValue(String optionName, OptionType optionType, T fallback) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return (T) this.generalOptions.getOrDefault(optionName, new OptionStorage<>(optionName, GeneralOptions.getGeneralOptionByName(optionName).getDefaultValue())).getValue();
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            WorldType worldType = WorldType.getWorldTypeById(Identifier.of(this.bwo_getWorldType()));
            if (!worldType.getWorldTypeOptions().isEmpty() && worldType.getWorldTypeOptions().containsKey(optionName)) {
                return (T) this.worldTypeOptions.getOrDefault(optionName, new OptionStorage<>(optionName, worldType.getWorldTypeOptions().get(optionName).getDefaultValue())).getValue();
            }
        }

        return fallback;
    }

    @Override
    public Map<String, OptionStorage<?>> bwo_getOptionsMap(OptionType optionType) {
        if (optionType == OptionType.GENERAL_OPTION) {
            return this.generalOptions;
        } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
            return this.worldTypeOptions;
        }

        return null;
    }

    @Unique
    private String bwo_getStringOrDefault(NbtCompound nbt, Option<?> option) {
        return !nbt.contains(option.getName()) ? ((StringOption) option).getDefaultValue() : nbt.getString(option.getName());
    }

    @Unique
    private boolean bwo_getBooleanOrDefault(NbtCompound nbt, Option<?> option) {
        return !nbt.contains(option.getName()) ? ((BooleanOption) option).getDefaultValue() : nbt.getBoolean(option.getName());
    }

    @Unique
    private int bwo_getIntOrDefault(NbtCompound nbt, Option<?> option) {
        return !nbt.contains(option.getName()) ? ((IntOption) option).getDefaultValue() : nbt.getInt(option.getName());
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void bwo_loadFromNbt(NbtCompound nbt, CallbackInfo ci) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = new BWOWorldPropertiesStorage();

        NbtCompound betterWorldOptionsTag = nbt.getCompound("BetterWorldOptions");

        String worldType = this.bwo_getStringOrDefault(betterWorldOptionsTag, GeneralOptions.getGeneralOptionByName("WorldType"));
        WorldType worldTypeEntry = WorldType.getWorldTypeById(Identifier.of(worldType));

        if (this.bwo_getBooleanOrDefault(betterWorldOptionsTag, GeneralOptions.getGeneralOptionByName("FiniteWorld")) && worldTypeEntry.isPregenerateFiniteWorld()) {
            this.pregeneratingFiniteWorld = betterWorldOptionsTag.getBoolean("PregeneratingFiniteWorld");
        }

        for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
            Option<?> generalOption = GeneralOptions.getGeneralOptionByName(option.name());

            if (generalOption.allowSave() && (generalOption.getCompatibleType().equals("All") || generalOption.getCompatibleWorldTypes().contains(Identifier.of(worldType)) || generalOption.getCompatibleType().equals("Overworld") && !worldTypeEntry.isDimension())) {
                if (option.getValue() instanceof String) {
                    String value = this.bwo_getStringOrDefault(betterWorldOptionsTag, generalOption);

                    if (generalOption.getParentOption() != null && !this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption.getParentOption())) {
                        continue;
                    }

                    this.generalOptions.put(option.name(), new OptionStorage<>(option.name(), value));
                } else if (option.getValue() instanceof Boolean) {
                    boolean value = this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption);

                    if (!generalOption.getDependentOptions().isEmpty() && !value) {
                        continue;
                    } else if (generalOption.getParentOption() != null && !this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption.getParentOption())) {
                        continue;
                    }

                    this.generalOptions.put(option.name(), new OptionStorage<>(option.name(), value));
                } else if (option.getValue() instanceof Integer) {
                    int value = this.bwo_getIntOrDefault(betterWorldOptionsTag, generalOption);

                    if (generalOption.getParentOption() != null && !this.bwo_getBooleanOrDefault(betterWorldOptionsTag, generalOption.getParentOption())) {
                        continue;
                    }

                    this.generalOptions.put(option.name(), new OptionStorage<>(option.name(), value));
                }
            }
        }

        bwoWorldPropertiesStorage.setOptionsMap(this.generalOptions, OptionType.GENERAL_OPTION);

        Map<String, Option<?>> worldTypeOptions = WorldType.getWorldTypeById(Identifier.of(this.bwo_getStringOrDefault(betterWorldOptionsTag, GeneralOptions.getGeneralOptionByName("WorldType")))).getWorldTypeOptions();
        if (!worldTypeOptions.isEmpty()) {
            NbtCompound worldTypeOptionsTag = betterWorldOptionsTag.getCompound("WorldTypeOptions");

            Map<String, OptionStorage<?>> worldTypeOptionsMap = new LinkedHashMap<>();

            for (Option<?> option : worldTypeOptions.values()) {
                if (option instanceof StringOption) {
                    String value = this.bwo_getStringOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.getName()));

                    if (worldTypeOptions.get(option.getName()).getParentOption() != null && !this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.getName()).getParentOption())) {
                        continue;
                    }

                    worldTypeOptionsMap.put(option.getName(), new OptionStorage<>(option.getName(), value));
                } else if (option instanceof BooleanOption) {
                    boolean value = this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.getName()));

                    if (!worldTypeOptions.get(option.getName()).getDependentOptions().isEmpty() && !value) {
                        continue;
                    } else if (worldTypeOptions.get(option.getName()).getParentOption() != null && !this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.getName()).getParentOption())) {
                        continue;
                    }

                    worldTypeOptionsMap.put(option.getName(), new OptionStorage<>(option.getName(), value));
                } else if (option instanceof IntOption) {
                    int value = this.bwo_getIntOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.getName()));

                    if (worldTypeOptions.get(option.getName()).getParentOption() != null && !this.bwo_getBooleanOrDefault(worldTypeOptionsTag, worldTypeOptions.get(option.getName()).getParentOption())) {
                        continue;
                    }

                    worldTypeOptionsMap.put(option.getName(), new OptionStorage<>(option.getName(), value));
                }
            }

            bwoWorldPropertiesStorage.setOptionsMap(worldTypeOptionsMap, OptionType.WORLD_TYPE_OPTION);

            for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                this.worldTypeOptions.put(option.name(), new OptionStorage<>(option.name(), option.getValue()));
            }

            bwoWorldPropertiesStorage.setOptionsMap(this.worldTypeOptions, OptionType.WORLD_TYPE_OPTION);
        }

        BWOWorldPropertiesStorage.setInstance(bwoWorldPropertiesStorage);
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void bwo_initBWOProperties(long seed, String name, CallbackInfo ci) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");
        WorldType worldTypeEntry = WorldType.getWorldTypeById(Identifier.of(worldType));

        if (bwoWorldPropertiesStorage.getOptionValue("FiniteWorld", OptionType.GENERAL_OPTION, false) && worldTypeEntry.isPregenerateFiniteWorld()) {
            this.pregeneratingFiniteWorld = true;
        }

        for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
            Option<?> generalOption = GeneralOptions.getGeneralOptionByName(option.name());

            if (generalOption.allowSave() && (generalOption.getCompatibleType().equals("All") || generalOption.getCompatibleWorldTypes().contains(Identifier.of(worldType)) || generalOption.getCompatibleType().equals("Overworld") && !worldTypeEntry.isDimension())) {
                if (generalOption.getParentOption() != null && !bwoWorldPropertiesStorage.getOptionValue(generalOption.getParentOption().getName(), generalOption.getOptionType(), false)) {
                    continue;
                }

                OptionStorage<?> value = new OptionStorage<>(option.name(), option.getValue());
                if (option.getValue() instanceof String) {
                    this.generalOptions.put(option.name(), value);
                } else if (option.getValue() instanceof Boolean) {
                    if (!generalOption.getDependentOptions().isEmpty() && !(boolean) option.getValue()) {
                        continue;
                    }

                    this.generalOptions.put(option.name(), value);
                } else if (option.getValue() instanceof Integer) {
                    this.generalOptions.put(option.name(), value);
                }
            }
        }

        if (!worldTypeEntry.getWorldTypeOptions().isEmpty()) {
            for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                if (worldTypeEntry.getWorldTypeOptions().get(option.name()).getParentOption() != null && !bwoWorldPropertiesStorage.getOptionValue(worldTypeEntry.getWorldTypeOptions().get(option.name()).getParentOption().getName(), worldTypeEntry.getWorldTypeOptions().get(option.name()).getOptionType(), false)) {
                    continue;
                }

                OptionStorage<?> value = new OptionStorage<>(option.name(), option.getValue());
                if (option.getValue() instanceof String) {
                    this.worldTypeOptions.put(option.name(), value);
                } else if (option.getValue() instanceof Boolean) {
                    if (!worldTypeEntry.getWorldTypeOptions().get(option.name()).getDependentOptions().isEmpty() && !(boolean) option.getValue()) {
                        continue;
                    }

                    this.worldTypeOptions.put(option.name(), value);
                } else if (option.getValue() instanceof Integer) {
                    this.worldTypeOptions.put(option.name(), value);
                }
            }
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/WorldProperties;)V", at = @At("TAIL"))
    private void bwo_copyProperties(WorldProperties worldProperties, CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) worldProperties;

        this.pregeneratingFiniteWorld = bwoProperties.bwo_isPregeneratingFiniteWorld();

        this.generalOptions = bwoProperties.bwo_getOptionsMap(OptionType.GENERAL_OPTION);
        this.worldTypeOptions = bwoProperties.bwo_getOptionsMap(OptionType.WORLD_TYPE_OPTION);
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void bwo_updateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        NbtCompound betterWorldOptionsTag = new NbtCompound();
        NbtCompound worldTypeOptionsTag = new NbtCompound();

        WorldType worldTypeEntry = WorldType.getWorldTypeById(Identifier.of(this.bwo_getWorldType()));

        if (this.bwo_getOptionValue("FiniteWorld", OptionType.GENERAL_OPTION, false) && worldTypeEntry.isPregenerateFiniteWorld()) {
            betterWorldOptionsTag.putBoolean("PregeneratingFiniteWorld", this.pregeneratingFiniteWorld);
        }

        for (OptionStorage<?> option : this.generalOptions.values()) {
            if (option.getValue() instanceof String) {
                betterWorldOptionsTag.putString(option.name(), (String) option.getValue());
            } else if (option.getValue() instanceof Boolean) {
                betterWorldOptionsTag.putBoolean(option.name(), (boolean) option.getValue());
            } else if (option.getValue() instanceof Integer) {
                betterWorldOptionsTag.putInt(option.name(), (int) option.getValue());
            }
        }

        if (!WorldType.getWorldTypeById(Identifier.of(bwo_getWorldType())).getWorldTypeOptions().isEmpty()) {
            for (OptionStorage<?> option : this.worldTypeOptions.values()) {
                if (option.getValue() instanceof String) {
                    worldTypeOptionsTag.putString(option.name(), (String) option.getValue());
                } else if (option.getValue() instanceof Boolean) {
                    worldTypeOptionsTag.putBoolean(option.name(), (boolean) option.getValue());
                } else if (option.getValue() instanceof Integer) {
                    worldTypeOptionsTag.putInt(option.name(), (int) option.getValue());
                }
            }

            betterWorldOptionsTag.put("WorldTypeOptions", worldTypeOptionsTag);
        }

        betterWorldOptionsTag.putString("BWOVersion", "0.4.0");
        nbt.put("BetterWorldOptions", betterWorldOptionsTag);
    }
}


