package com.itselix99.betterworldoptions.network;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.BooleanOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.IntOptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypeEntry;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BWOWorldPropertiesStoragePacket extends Packet implements ManagedPacket<BWOWorldPropertiesStoragePacket> {
    public static final PacketType<BWOWorldPropertiesStoragePacket> TYPE = PacketType.builder(true, false, BWOWorldPropertiesStoragePacket::new).build();

    private Map<String, OptionStorage> generalOptions = new LinkedHashMap<>();
    private Map<String, OptionStorage> worldTypeOptions = new LinkedHashMap<>();
    private boolean worldTypeOptionsBoolean;

    public BWOWorldPropertiesStoragePacket() {
    }

    public BWOWorldPropertiesStoragePacket(BWOProperties bwoProperties) {
        this.generalOptions = bwoProperties.bwo_getOptionsMap(OptionType.GENERAL_OPTION);
        this.worldTypeOptions = bwoProperties.bwo_getOptionsMap(OptionType.WORLD_TYPE_OPTION);
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            int generalOptionsCount = stream.readInt();

            for(int i = 0; i < generalOptionsCount; i++) {
                String generalOptionName = stream.readUTF();
                String valueType = stream.readUTF();

                OptionStorage option = null;

                switch (valueType) {
                    case "String" -> option = new StringOptionStorage(generalOptionName, stream.readUTF());
                    case "Boolean" -> option = new BooleanOptionStorage(generalOptionName, stream.readBoolean());
                    case "Int" -> option = new IntOptionStorage(generalOptionName, stream.readInt());
                }

                this.generalOptions.put(generalOptionName, option);
            }

            this.worldTypeOptionsBoolean = stream.readBoolean();

            if (this.worldTypeOptionsBoolean) {
                int worldTypeOptionsCount = stream.readInt();

                for(int i = 0; i < worldTypeOptionsCount; i++) {
                    String worldTypeOptionName = stream.readUTF();
                    String valueType = stream.readUTF();

                    OptionStorage option = null;

                    switch (valueType) {
                        case "String" -> option = new StringOptionStorage(worldTypeOptionName, stream.readUTF());
                        case "Boolean" -> option = new BooleanOptionStorage(worldTypeOptionName, stream.readBoolean());
                        case "Int" -> option = new IntOptionStorage(worldTypeOptionName, stream.readInt());
                    }

                    this.worldTypeOptions.put(worldTypeOptionName, option);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(this.generalOptions.size());

            for (OptionStorage generalOptions : this.generalOptions.values()) {
                stream.writeUTF(generalOptions.name);

                if (generalOptions instanceof StringOptionStorage stringGeneralOptions) {
                    stream.writeUTF("String");
                    stream.writeUTF(stringGeneralOptions.value);
                } else if (generalOptions instanceof BooleanOptionStorage booleanGeneralOptions) {
                    stream.writeUTF("Boolean");
                    stream.writeBoolean(booleanGeneralOptions.value);
                } else if (generalOptions instanceof IntOptionStorage intGeneralOptions) {
                    stream.writeUTF("Int");
                    stream.writeInt(intGeneralOptions.value);
                }
            }

            WorldTypeEntry worldType = WorldTypes.getWorldTypeByName(((StringOptionStorage) this.generalOptions.get("WorldType")).value);
            stream.writeBoolean(worldType.worldTypeOptions != null);

            if (worldType.worldTypeOptions != null) {
                stream.writeInt(this.worldTypeOptions.size());

                for (OptionStorage worldTypeOptions : this.worldTypeOptions.values()) {
                    stream.writeUTF(worldTypeOptions.name);

                    if (worldTypeOptions instanceof StringOptionStorage stringWorldTypeOptions) {
                        stream.writeUTF("String");
                        stream.writeUTF(stringWorldTypeOptions.value);
                    } else if (worldTypeOptions instanceof BooleanOptionStorage booleanWorldTypeOptions) {
                        stream.writeUTF("Boolean");
                        stream.writeBoolean(booleanWorldTypeOptions.value);
                    } else if (worldTypeOptions instanceof IntOptionStorage intWorldTypeOptions) {
                        stream.writeUTF("Int");
                        stream.writeInt(intWorldTypeOptions.value);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        this.handleClient(networkHandler);
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = new BWOWorldPropertiesStorage();
        bwoWorldPropertiesStorage.isBWOServer = true;

        bwoWorldPropertiesStorage.setOptionsMap(this.generalOptions, OptionType.GENERAL_OPTION);
        bwoWorldPropertiesStorage.setOptionsMap(this.worldTypeOptions, OptionType.WORLD_TYPE_OPTION);
        BWOWorldPropertiesStorage.setInstance(bwoWorldPropertiesStorage);
    }

    @Override
    public int size() {
        int size = 0;

        size += 4;

        for (OptionStorage generalOption : this.generalOptions.values()) {
            size += generalOption.name.length();

            if (generalOption instanceof StringOptionStorage stringGeneralOption) {
                size += "String".length();
                size += stringGeneralOption.value.length();
            } else if (generalOption instanceof BooleanOptionStorage) {
                size += "Boolean".length();
                size += 1;
            } else if (generalOption instanceof IntOptionStorage) {
                size += "Int".length();
                size += 4;
            }
        }

        size += 1;

        if (this.worldTypeOptionsBoolean) {
            size += 4;

            for (OptionStorage worldTypeOption : this.worldTypeOptions.values()) {
                size += worldTypeOption.name.length();

                if (worldTypeOption instanceof StringOptionStorage stringWorldTypeOption) {
                    size += "String".length();
                    size += stringWorldTypeOption.value.length();
                } else if (worldTypeOption instanceof BooleanOptionStorage) {
                    size += "Boolean".length();
                    size += 1;
                } else if (worldTypeOption instanceof IntOptionStorage) {
                    size += "Int".length();
                    size += 4;
                }
            }
        }

        return size;
    }



    @Override
    public @NotNull PacketType<BWOWorldPropertiesStoragePacket> getType() {
        return TYPE;
    }
}