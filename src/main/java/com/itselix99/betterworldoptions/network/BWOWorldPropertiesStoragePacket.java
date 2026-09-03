package com.itselix99.betterworldoptions.network;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.OptionStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BWOWorldPropertiesStoragePacket extends Packet implements ManagedPacket<BWOWorldPropertiesStoragePacket> {
    public static final PacketType<BWOWorldPropertiesStoragePacket> TYPE = PacketType.builder(true, false, BWOWorldPropertiesStoragePacket::new).build();

    private Map<String, OptionStorage<?>> generalOptions = new LinkedHashMap<>();
    private Map<String, OptionStorage<?>> worldTypeOptions = new LinkedHashMap<>();
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

                OptionStorage<?> option = null;

                switch (valueType) {
                    case "String" -> option = new OptionStorage<>(generalOptionName, stream.readUTF());
                    case "Boolean" -> option = new OptionStorage<>(generalOptionName, stream.readBoolean());
                    case "Int" -> option = new OptionStorage<>(generalOptionName, stream.readInt());
                }

                this.generalOptions.put(generalOptionName, option);
            }

            this.worldTypeOptionsBoolean = stream.readBoolean();

            if (this.worldTypeOptionsBoolean) {
                int worldTypeOptionsCount = stream.readInt();

                for(int i = 0; i < worldTypeOptionsCount; i++) {
                    String worldTypeOptionName = stream.readUTF();
                    String valueType = stream.readUTF();

                    OptionStorage<?> option = null;

                    switch (valueType) {
                        case "String" -> option = new OptionStorage<>(worldTypeOptionName, stream.readUTF());
                        case "Boolean" -> option = new OptionStorage<>(worldTypeOptionName, stream.readBoolean());
                        case "Int" -> option = new OptionStorage<>(worldTypeOptionName, stream.readInt());
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

            for (OptionStorage<?> generalOptions : this.generalOptions.values()) {
                stream.writeUTF(generalOptions.name());

                if (generalOptions.getValue() instanceof String) {
                    stream.writeUTF("String");
                    stream.writeUTF((String) generalOptions.getValue());
                } else if (generalOptions.getValue() instanceof Boolean) {
                    stream.writeUTF("Boolean");
                    stream.writeBoolean((boolean) generalOptions.getValue());
                } else if (generalOptions.getValue() instanceof Integer) {
                    stream.writeUTF("Int");
                    stream.writeInt((int) generalOptions.getValue());
                }
            }

            WorldType worldType = WorldType.getWorldTypeById(Identifier.of((String) this.generalOptions.get("WorldType").getValue()));
            stream.writeBoolean(!worldType.getWorldTypeOptions().isEmpty());

            if (!worldType.getWorldTypeOptions().isEmpty()) {
                stream.writeInt(this.worldTypeOptions.size());

                for (OptionStorage<?> worldTypeOptions : this.worldTypeOptions.values()) {
                    stream.writeUTF(worldTypeOptions.name());

                    if (worldTypeOptions.getValue() instanceof String) {
                        stream.writeUTF("String");
                        stream.writeUTF((String) worldTypeOptions.getValue());
                    } else if (worldTypeOptions.getValue() instanceof Boolean) {
                        stream.writeUTF("Boolean");
                        stream.writeBoolean((boolean) worldTypeOptions.getValue());
                    } else if (worldTypeOptions.getValue() instanceof Integer) {
                        stream.writeUTF("Int");
                        stream.writeInt((int) worldTypeOptions.getValue());
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

        for (OptionStorage<?> generalOption : this.generalOptions.values()) {
            size += generalOption.name().length();

            if (generalOption.getValue() instanceof String) {
                size += "String".length();
                size += ((String) generalOption.getValue()).length();
            } else if (generalOption.getValue() instanceof Boolean) {
                size += "Boolean".length();
                size += 1;
            } else if (generalOption.getValue() instanceof Integer) {
                size += "Int".length();
                size += 4;
            }
        }

        size += 1;

        if (this.worldTypeOptionsBoolean) {
            size += 4;

            for (OptionStorage<?> worldTypeOption : this.worldTypeOptions.values()) {
                size += worldTypeOption.name().length();

                if (worldTypeOption.getValue() instanceof String) {
                    size += "String".length();
                    size += ((String) worldTypeOption.getValue()).length();
                } else if (worldTypeOption.getValue() instanceof Boolean) {
                    size += "Boolean".length();
                    size += 1;
                } else if (worldTypeOption.getValue() instanceof Integer) {
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