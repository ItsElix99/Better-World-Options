package com.itselix99.betterworldoptions.network;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
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

public class WorldGenerationOptionsPacket extends Packet implements ManagedPacket<WorldGenerationOptionsPacket> {
    public static final PacketType<WorldGenerationOptionsPacket> TYPE = PacketType.builder(true, false, WorldGenerationOptionsPacket::new).build();

    private String worldType;
    private boolean hardcore;
    private boolean oldFeatures;
    private String singleBiome;
    private String theme;
    private boolean superflat;

    private String indevWorldType;
    private String indevShape;
    private boolean generateIndevHouse;

    private int worldSizeX;
    private int worldSizeZ;
    private boolean infiniteWorld;

    public WorldGenerationOptionsPacket() {
    }

    public WorldGenerationOptionsPacket(BWOProperties properties) {
        this.worldType = properties.bwo_getWorldType();
        this.hardcore = properties.bwo_isHardcore();
        this.oldFeatures = properties.bwo_isOldFeatures();
        this.singleBiome = properties.bwo_getSingleBiome();
        this.theme = properties.bwo_getTheme();

        if (this.worldType.equals("Flat")) {
            this.superflat = properties.bwo_isSuperflat();
        }

        if (this.worldType.equals("Indev 223") || this.worldType.equals("MCPE")) {
            if (this.worldType.equals("Indev 223")) {
                this.indevWorldType = properties.bwo_getIndevWorldType();
                this.indevShape = properties.bwo_getIndevShape();
                this.generateIndevHouse = properties.bwo_isGenerateIndevHouse();
            }

            this.worldSizeX = properties.bwo_getWorldSizeX();
            this.worldSizeZ = properties.bwo_getWorldSizeZ();
            this.infiniteWorld = properties.bwo_isInfiniteWorld();
        }
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.worldType = stream.readUTF();
            this.hardcore = stream.readBoolean();
            this.oldFeatures = stream.readBoolean();
            this.singleBiome = stream.readUTF();
            this.theme = stream.readUTF();

            if (this.worldType.equals("Flat")) {
                this.superflat = stream.readBoolean();
            }

            if (this.worldType.equals("Indev 223") || this.worldType.equals("MCPE")) {
                if (this.worldType.equals("Indev 223")) {
                    this.indevWorldType = stream.readUTF();
                    this.indevShape = stream.readUTF();
                    this.generateIndevHouse = stream.readBoolean();
                }


                this.worldSizeX = stream.readInt();
                this.worldSizeZ = stream.readInt();
                this.infiniteWorld = stream.readBoolean();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeUTF(this.worldType);
            stream.writeBoolean(this.hardcore);
            stream.writeBoolean(this.oldFeatures);
            stream.writeUTF(this.singleBiome);
            stream.writeUTF(this.theme);

            if (this.worldType.equals("Flat")) {
                stream.writeBoolean(this.superflat);
            }

            if (this.worldType.equals("Indev 223") || this.worldType.equals("MCPE")) {
                if (this.worldType.equals("Indev 223")) {
                    stream.writeUTF(this.indevWorldType);
                    stream.writeUTF(this.indevShape);
                    stream.writeBoolean(this.generateIndevHouse);
                }

                stream.writeInt(this.worldSizeX);
                stream.writeInt(this.worldSizeZ);
                stream.writeBoolean(infiniteWorld);
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
        WorldGenerationOptions worldGenerationOptions = new WorldGenerationOptions();

        worldGenerationOptions.worldType = this.worldType;
        worldGenerationOptions.hardcore = this.hardcore;
        worldGenerationOptions.oldFeatures = this.oldFeatures;
        worldGenerationOptions.singleBiome = this.singleBiome;
        worldGenerationOptions.theme = this.theme;

        if (worldGenerationOptions.worldType.equals("Flat")) {
            worldGenerationOptions.superflat = this.superflat;
        }

        if (worldGenerationOptions.worldType.equals("Indev 223") || worldGenerationOptions.worldType.equals("MCPE")) {
            if (worldGenerationOptions.worldType.equals("Indev 223")) {
                worldGenerationOptions.indevWorldType = this.indevWorldType;
                worldGenerationOptions.indevShape = this.indevShape;
                worldGenerationOptions.generateIndevHouse = this.generateIndevHouse;
            }


            worldGenerationOptions.worldSizeX = this.worldSizeX;
            worldGenerationOptions.worldSizeZ = this.worldSizeZ;
            worldGenerationOptions.infiniteWorld = this.infiniteWorld;
        }
    }

    @Override
    public int size() {
        return calculateSize();
    }

    private int calculateSize() {
        if (this.worldType != null) {
            int size = this.worldType.length() + 2 + this.singleBiome.length() + this.theme.length();

            if (this.worldType.equals("Flat")) {
                size += 1;
            }

            if (this.worldType.equals("Indev 223") || this.worldType.equals("MCPE")) {
                if (this.worldType.equals("Indev 223")) {
                    size += this.indevWorldType.length() + this.indevShape.length() + 1;
                }

                size += 9;
            }

            return size;
        } else {
            return 0;
        }
    }

    @Override
    public @NotNull PacketType<WorldGenerationOptionsPacket> getType() {
        return TYPE;
    }
}