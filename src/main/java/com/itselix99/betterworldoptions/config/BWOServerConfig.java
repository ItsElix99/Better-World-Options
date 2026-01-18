package com.itselix99.betterworldoptions.config;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.entry.BooleanOptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import com.itselix99.betterworldoptions.api.options.entry.StringOptionEntry;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

            for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
                if (option instanceof StringOptionStorage) {
                    StringOptionEntry stringGeneralOption = (StringOptionEntry) GeneralOptions.getOptionByName(option.name);
                    String defaultValue = stringGeneralOption.defaultValue;
                    bwoWorldPropertiesStorage.setOptionValue(option.name, OptionType.GENERAL_OPTION, new StringOptionStorage(option.name, generalProps.getProperty(option.name, defaultValue)));
                } else if (option instanceof BooleanOptionStorage) {
                    BooleanOptionEntry booleanGeneralOption = (BooleanOptionEntry) GeneralOptions.getOptionByName(option.name);
                    boolean defaultValue = booleanGeneralOption.defaultValue;

                    if (booleanGeneralOption.worldTypeDefaultValue != null) {
                        for (Map.Entry<String, Boolean> entry : booleanGeneralOption.worldTypeDefaultValue.entrySet()) {
                            if (entry.getKey().equals(((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value)) {
                                defaultValue = entry.getValue();
                            }
                        }
                    }

                    bwoWorldPropertiesStorage.setOptionValue(option.name, OptionType.GENERAL_OPTION, new BooleanOptionStorage(option.name, Boolean.parseBoolean(generalProps.getProperty(option.name, String.valueOf(defaultValue)))));
                }
            }

            Properties worldTypeProps = new LinkedProperties();
            FileInputStream in2 = new FileInputStream(worldTypeOptionsConfigFile);
            worldTypeProps.load(in2);
            in2.close();

            WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value);

            if (worldType.worldTypeOptions != null) {
                Map<String, OptionStorage> worldTypeOptions = new HashMap<>();

                for (OptionEntry optionEntry : worldType.worldTypeOptions.values()) {
                    if (optionEntry instanceof StringOptionEntry stringOptionEntry) {
                        worldTypeOptions.put(optionEntry.name, new StringOptionStorage(optionEntry.name, stringOptionEntry.defaultValue));
                    } else if (optionEntry instanceof BooleanOptionEntry booleanOptionEntry) {
                        worldTypeOptions.put(optionEntry.name, new BooleanOptionStorage(optionEntry.name, booleanOptionEntry.defaultValue));
                    }
                }

                bwoWorldPropertiesStorage.setOptionsMap(worldTypeOptions, OptionType.WORLD_TYPE_OPTION);

                for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                    if (option instanceof StringOptionStorage) {
                        StringOptionEntry stringWorldTypeOption = (StringOptionEntry) worldType.worldTypeOptions.get(option.name);
                        String defaultValue = stringWorldTypeOption.defaultValue;
                        bwoWorldPropertiesStorage.setOptionValue(option.name, OptionType.WORLD_TYPE_OPTION, new StringOptionStorage(option.name, worldTypeProps.getProperty(option.name, defaultValue)));
                    } else if (option instanceof BooleanOptionStorage) {
                        BooleanOptionEntry booleanGeneralOption = (BooleanOptionEntry) worldType.worldTypeOptions.get(option.name);
                        boolean defaultValue = booleanGeneralOption.defaultValue;
                        bwoWorldPropertiesStorage.setOptionValue(option.name, OptionType.WORLD_TYPE_OPTION, new BooleanOptionStorage(option.name, Boolean.parseBoolean(worldTypeProps.getProperty(option.name, String.valueOf(defaultValue)))));
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
                for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.GENERAL_OPTION).values()) {
                    if (option instanceof StringOptionStorage stringGeneralOption) {
                        props.setProperty(option.name, stringGeneralOption.value);
                    } else if (option instanceof BooleanOptionStorage booleanGeneralOption) {
                        props.setProperty(option.name, String.valueOf(booleanGeneralOption.value));
                    }
                }

                FileOutputStream out = new FileOutputStream(generalOptionsConfigFile);
                storeWithoutTimestamp(props, out, "Better World Options - General Options");
                out.close();
            } else if (optionType == OptionType.WORLD_TYPE_OPTION) {
                WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value);

                if (worldType.worldTypeOptions != null) {
                    for (OptionStorage option : bwoWorldPropertiesStorage.getOptionsMap(OptionType.WORLD_TYPE_OPTION).values()) {
                        if (option instanceof StringOptionStorage stringWorldTypeOption) {
                            props.setProperty(option.name, stringWorldTypeOption.value);
                        } else if (option instanceof BooleanOptionStorage booleanWorldTypeOption) {
                            props.setProperty(option.name, String.valueOf(booleanWorldTypeOption.value));
                        }
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