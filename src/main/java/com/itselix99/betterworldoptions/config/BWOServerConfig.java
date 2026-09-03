package com.itselix99.betterworldoptions.config;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOption;
import com.itselix99.betterworldoptions.api.options.entry.IntOption;
import com.itselix99.betterworldoptions.api.options.entry.Option;
import com.itselix99.betterworldoptions.api.options.entry.StringOption;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Environment(EnvType.SERVER)
public class BWOServerConfig {
    private static final File generalOptionsConfigFile = new File(".", "config/betterworldoptions/general_options.cfg");
    private static final File worldTypeOptionsConfigFile = new File(".", "config/betterworldoptions/world_type_options.cfg");

    public static void loadOptions() {
        try {
            if (!generalOptionsConfigFile.exists()) {
                generalOptionsConfigFile.getParentFile().mkdirs();
                createOrSaveConfigFile(OptionType.GENERAL_OPTION);
            }

            if (!worldTypeOptionsConfigFile.exists()) {
                worldTypeOptionsConfigFile.getParentFile().mkdirs();
                createOrSaveConfigFile(OptionType.WORLD_TYPE_OPTION);
            }

            Properties generalProps = new LinkedProperties();
            FileInputStream in = new FileInputStream(generalOptionsConfigFile);
            generalProps.load(in);
            in.close();

            BWOWorldPropertiesStorage bwoWorldPropertiesStorage = new BWOWorldPropertiesStorage();

            for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
                if (option.getValue() instanceof String) {
                    StringOption stringGeneralOption = (StringOption) GeneralOptions.getGeneralOptionByName(option.name());
                    String defaultValue = stringGeneralOption.getDefaultValue();

                    if (!stringGeneralOption.getWorldTypeValues().isEmpty()) {
                        List<String> stringList = stringGeneralOption.getValues(Identifier.of(generalProps.getProperty("WorldType", WorldType.defaultWorldType.getId().toString())));

                        if (!stringList.contains(generalProps.getProperty(option.name(), defaultValue))) {
                            defaultValue = stringList.get(0);
                        }
                    }

                    bwoWorldPropertiesStorage.setOptionValue(option.name(), OptionType.GENERAL_OPTION, generalProps.getProperty(option.name(), defaultValue));
                } else if (option.getValue() instanceof Boolean) {
                    BooleanOption booleanGeneralOption = (BooleanOption) GeneralOptions.getGeneralOptionByName(option.name());
                    boolean defaultValue = booleanGeneralOption.getDefaultValue();

                    if (!booleanGeneralOption.getWorldTypeValues().isEmpty()) {
                        defaultValue = booleanGeneralOption.getValues(Identifier.of(generalProps.getProperty("WorldType", WorldType.defaultWorldType.getId().toString())));
                    }

                    bwoWorldPropertiesStorage.setOptionValue(option.name(), OptionType.GENERAL_OPTION, Boolean.parseBoolean(generalProps.getProperty(option.name(), String.valueOf(defaultValue))));
                } else if (option.getValue() instanceof Integer) {
                    IntOption intGeneralOption = (IntOption) GeneralOptions.getGeneralOptionByName(option.name());
                    int defaultValue = intGeneralOption.getDefaultValue();
                    int loadedValue = Integer.parseInt(generalProps.getProperty(option.name(), String.valueOf(defaultValue)));

                    if (!(loadedValue >= intGeneralOption.getMinValue() && loadedValue <= intGeneralOption.getMaxValue())) {
                        loadedValue = defaultValue;
                    }

                    bwoWorldPropertiesStorage.setOptionValue(option.name(), OptionType.GENERAL_OPTION, loadedValue);
                }
            }

            Properties worldTypeProps = new LinkedProperties();
            FileInputStream in2 = new FileInputStream(worldTypeOptionsConfigFile);
            worldTypeProps.load(in2);
            in2.close();

            WorldType worldType = WorldType.getWorldTypeById(Identifier.of(bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "")));

            if (!worldType.getWorldTypeOptions().isEmpty()) {
                Map<String, OptionStorage<?>> worldTypeOptions = new HashMap<>();

                for (Option<?> option : worldType.getWorldTypeOptions().values()) {
                    if (option instanceof StringOption stringOptionEntry) {
                        worldTypeOptions.put(option.getName(), new OptionStorage<>(option.getName(), stringOptionEntry.getDefaultValue()));
                    } else if (option instanceof BooleanOption booleanOptionEntry) {
                        worldTypeOptions.put(option.getName(), new OptionStorage<>(option.getName(), booleanOptionEntry.getDefaultValue()));
                    } else if (option instanceof IntOption intOptionEntry) {
                        worldTypeOptions.put(option.getName(), new OptionStorage<>(option.getName(), intOptionEntry.getDefaultValue()));
                    }
                }

                bwoWorldPropertiesStorage.setOptionsMap(worldTypeOptions, OptionType.WORLD_TYPE_OPTION);

                for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                    if (option.getValue() instanceof String) {
                        StringOption stringWorldTypeOption = (StringOption) worldType.getWorldTypeOptions().get(option.name());
                        String defaultValue = stringWorldTypeOption.getDefaultValue();
                        bwoWorldPropertiesStorage.setOptionValue(option.name(), OptionType.WORLD_TYPE_OPTION, worldTypeProps.getProperty(option.name(), defaultValue));
                    } else if (option.getValue() instanceof Boolean) {
                        BooleanOption booleanWorldTypeOption = (BooleanOption) worldType.getWorldTypeOptions().get(option.name());
                        boolean defaultValue = booleanWorldTypeOption.getDefaultValue();
                        bwoWorldPropertiesStorage.setOptionValue(option.name(), OptionType.WORLD_TYPE_OPTION, Boolean.parseBoolean(worldTypeProps.getProperty(option.name(), String.valueOf(defaultValue))));
                    } else if (option.getValue() instanceof Integer) {
                        IntOption intWorldTypeOption = (IntOption) worldType.getWorldTypeOptions().get(option.name());
                        int defaultValue = intWorldTypeOption.getDefaultValue();
                        int loadedValue = Integer.parseInt(worldTypeProps.getProperty(option.name(), String.valueOf(defaultValue)));

                        if (!(loadedValue >= intWorldTypeOption.getMinValue() && loadedValue <= intWorldTypeOption.getMaxValue())) {
                            loadedValue = defaultValue;
                        }

                        bwoWorldPropertiesStorage.setOptionValue(option.name(), OptionType.WORLD_TYPE_OPTION, loadedValue);
                    }
                }
            }

            BWOWorldPropertiesStorage.setInstance(bwoWorldPropertiesStorage);
            createOrSaveConfigFile(OptionType.GENERAL_OPTION);
            createOrSaveConfigFile(OptionType.WORLD_TYPE_OPTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createOrSaveConfigFile(OptionType optionType) {
        try {
            Properties props = new LinkedProperties();
            BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

            if (optionType == OptionType.GENERAL_OPTION) {
                for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
                    Option<?> optionEntry = GeneralOptions.getGeneralOptionByName(option.name());

                    if (optionEntry.allowSave()) {
                        props.setProperty(option.name(), String.valueOf(option.getValue()));
                    }
                }

                FileOutputStream out = new FileOutputStream(generalOptionsConfigFile);
                storeWithoutTimestamp(props, out, "Better World Options - General Options");
                out.close();
            } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
                WorldType worldType = WorldType.getWorldTypeById(Identifier.of(bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "")));

                if (!worldType.getWorldTypeOptions().isEmpty()) {
                    for (OptionStorage<?> option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                        props.setProperty(option.name(), String.valueOf(option.getValue()));
                    }
                }

                FileOutputStream out = new FileOutputStream(worldTypeOptionsConfigFile);
                storeWithoutTimestamp(props, out, "Better World Options - World Type Options");
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void storeWithoutTimestamp(Properties props, OutputStream out, String comment) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        if (comment != null && !comment.isEmpty()) {
            writer.write("# " + comment);
            writer.newLine();
        }

        for (Object keyObj : props.keySet()) {
            String key = (String) keyObj;
            String value = props.getProperty(key);

            if (key.startsWith("#")) {
                writer.newLine();
                writer.write(key);
            } else {
                writer.write(key + "=" + value);
            }
            writer.newLine();
        }
        writer.flush();
    }
}