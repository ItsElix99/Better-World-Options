package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.WorldProperties.class)
public class WorldPropertiesMixin implements BWOProperties {
    @Unique private String worldType;
    @Unique private boolean hardcore;
    @Unique private boolean oldFeatures;
    @Unique private String theme;
    @Unique private String singleBiome;
    @Unique private boolean superflat;

    @Unique private String indevWorldType;
    @Unique private String indevShape;
    @Unique private int worldSizeX;
    @Unique private int worldSizeZ;
    @Unique private boolean generateIndevHouse;
    @Unique private boolean infiniteWorld;

    @Override public void bwo_setWorldType(String name) {
        this.worldType = name;
    }
    @Override public String bwo_getWorldType() {
        return this.worldType;
    }

    @Override public void bwo_setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
    }
    @Override public boolean bwo_isHardcore() {
        return this.hardcore;
    }

    @Override public boolean bwo_isOldFeatures() {
        return this.oldFeatures;
    }
    @Override public String bwo_getSingleBiome() {
        return this.singleBiome;
    }
    @Override public String bwo_getTheme() {
        return this.theme;
    }
    @Override public boolean bwo_isSuperflat() {
        return this.superflat;
    }

    @Override public String bwo_getIndevWorldType() {
        return this.indevWorldType;
    }
    @Override public String bwo_getIndevShape() {
        return this.indevShape;
    }
    @Override public int bwo_getWorldSizeX() {
        return this.worldSizeX;
    }
    @Override public int bwo_getWorldSizeZ() {
        return this.worldSizeZ;
    }
    @Override public boolean bwo_isGenerateIndevHouse() {
        return this.generateIndevHouse;
    }
    @Override public boolean bwo_isInfiniteWorld() {
        return this.infiniteWorld;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void onLoadFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.worldType = nbt.getString("WorldType");
        this.hardcore = nbt.getBoolean("Hardcore");
        this.oldFeatures = nbt.getBoolean("OldFeatures");
        this.singleBiome = nbt.getString("SingleBiome");
        this.theme = nbt.getString("Theme");

        if (bwo_getWorldType().equals("Flat")) {
            this.superflat = nbt.getBoolean("Superflat");
        }

        if (bwo_getWorldType().equals("Indev 223") || bwo_getWorldType().equals("MCPE")) {
            if (bwo_getWorldType().equals("Indev 223")) {
                this.indevWorldType = nbt.getString("IndevWorldType");
                this.indevShape = nbt.getString("IndevShape");
                this.generateIndevHouse = nbt.getBoolean("GenerateIndevHouse");
            }

            this.infiniteWorld = nbt.getBoolean("InfiniteWorld");

            if (!bwo_isInfiniteWorld()) {
                this.worldSizeX = nbt.getInt("WorldSizeX");
                this.worldSizeZ = nbt.getInt("WorldSizeZ");
            }
        }
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void newWorld(long seed, String name, CallbackInfo ci) {
        WorldGenerationOptions worldGenerationOptions = WorldGenerationOptions.getInstance();
        this.worldType = worldGenerationOptions.worldType;
        this.hardcore = worldGenerationOptions.hardcore;
        this.oldFeatures = worldGenerationOptions.oldFeatures;
        this.singleBiome = worldGenerationOptions.singleBiome;
        this.theme = worldGenerationOptions.theme;

        if (bwo_getWorldType().equals("Flat")) {
            this.superflat = worldGenerationOptions.superflat;
        }

        if (bwo_getWorldType().equals("Indev 223") || bwo_getWorldType().equals("MCPE")) {
            if (bwo_getWorldType().equals("Indev 223")) {
                this.indevWorldType = worldGenerationOptions.indevWorldType;
                this.indevShape = worldGenerationOptions.indevShape;
                this.generateIndevHouse = worldGenerationOptions.generateIndevHouse;
            }

            this.infiniteWorld = worldGenerationOptions.infiniteWorld;

            if (!bwo_isInfiniteWorld()) {
                this.worldSizeX = worldGenerationOptions.worldSizeX;
                this.worldSizeZ = worldGenerationOptions.worldSizeZ;
            }
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/WorldProperties;)V", at = @At("TAIL"))
    private void onCopyConstructor(net.minecraft.world.WorldProperties source, CallbackInfo ci) {
        this.worldType = ((BWOProperties) source).bwo_getWorldType();
        this.hardcore = ((BWOProperties) source).bwo_isHardcore();
        this.oldFeatures = ((BWOProperties) source).bwo_isOldFeatures();
        this.singleBiome = ((BWOProperties) source).bwo_getSingleBiome();
        this.theme = ((BWOProperties) source).bwo_getTheme();

        if (bwo_getWorldType().equals("Flat")) {
            this.superflat = ((BWOProperties) source).bwo_isSuperflat();
        }

        if (bwo_getWorldType().equals("Indev 223") || bwo_getWorldType().equals("MCPE")) {
            if (bwo_getWorldType().equals("Indev 223")) {
                this.indevWorldType = ((BWOProperties) source).bwo_getIndevWorldType();
                this.indevShape = ((BWOProperties) source).bwo_getIndevShape();
                this.generateIndevHouse = ((BWOProperties) source).bwo_isGenerateIndevHouse();
            }

            this.infiniteWorld = ((BWOProperties) source).bwo_isInfiniteWorld();

            if (!bwo_isInfiniteWorld()) {
                this.worldSizeX = ((BWOProperties) source).bwo_getWorldSizeX();
                this.worldSizeZ = ((BWOProperties) source).bwo_getWorldSizeZ();
            }
        }
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void onUpdateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        nbt.putString("WorldType", this.worldType);
        nbt.putBoolean("Hardcore", this.hardcore);
        nbt.putBoolean("OldFeatures", this.oldFeatures);
        nbt.putString("SingleBiome", this.singleBiome);
        nbt.putString("Theme", this.theme);

        if (bwo_getWorldType().equals("Flat")) {
            nbt.putBoolean("Superflat", this.superflat);
        }

        if (bwo_getWorldType().equals("Indev 223") || bwo_getWorldType().equals("MCPE")) {
            if (bwo_getWorldType().equals("Indev 223")) {
                nbt.putString("IndevWorldType", this.indevWorldType);
                nbt.putString("IndevShape", this.indevShape);
                nbt.putBoolean("GenerateIndevHouse", this.generateIndevHouse);
            }

            nbt.putBoolean("InfiniteWorld", this.infiniteWorld);

            if (!bwo_isInfiniteWorld()) {
                nbt.putInt("WorldSizeX", this.worldSizeX);
                nbt.putInt("WorldSizeZ", this.worldSizeZ);
            }
        }
    }
}


