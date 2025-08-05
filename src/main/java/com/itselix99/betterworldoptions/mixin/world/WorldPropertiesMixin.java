package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(net.minecraft.world.WorldProperties.class)
public class WorldPropertiesMixin implements BWOProperties {
    @Unique private String worldType;
    @Unique private boolean hardcore;
    @Unique private boolean betaFeatures;
    @Unique private String theme;
    @Unique private String singleBiome;

    @Unique private String indevWorldType;
    @Unique private String shape;
    @Unique private String size;
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

    @Override public boolean bwo_getBetaFeatures() {
        return this.betaFeatures;
    }
    @Override public String bwo_getSingleBiome() {
        return this.singleBiome;
    }
    @Override public String bwo_getTheme() {
        return this.theme;
    }

    @Override public String bwo_getIndevWorldType() {
        return this.indevWorldType;
    }
    @Override public String bwo_getShape() {
        return this.shape;
    }
    @Override public String bwo_getSize() {
        return this.size;
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
        this.betaFeatures = nbt.getBoolean("BetaFeatures");
        this.singleBiome = nbt.getString("SingleBiome");
        this.theme = nbt.getString("Theme");

        WorldSettings.Textures.setBetaFeaturesTextures(this.betaFeatures);

        if (!bwo_getBetaFeatures() && bwo_getWorldType().equals("MCPE")) {
            WorldSettings.Textures.setMcpe(true);
        } else {
            WorldSettings.Textures.setMcpe(false);
        }

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            this.indevWorldType = nbt.getString("IndevWorldType");
            this.shape = nbt.getString("Shape");
            this.size = nbt.getString("Size");
            this.generateIndevHouse = nbt.getBoolean("GenerateIndevHouse");
            this.infiniteWorld = nbt.getBoolean("InfiniteWorld");
        }
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void newWorld(long seed, String name, CallbackInfo ci) {
        this.worldType = WorldSettings.World.getWorldTypeName();

        if (WorldSettings.GameMode.getGameMode().equals("Hardcore")) {
            this.hardcore = true;
        }

        this.betaFeatures = WorldSettings.GameMode.isBetaFeatures();
        this.singleBiome = WorldSettings.World.getSingleBiome() == null ? "All Biomes" : WorldSettings.World.getSingleBiome().name;
        this.theme = WorldSettings.World.getTheme();

        WorldSettings.Textures.setBetaFeaturesTextures(this.betaFeatures);

        if (!bwo_getBetaFeatures() && bwo_getWorldType().equals("MCPE")) {
            WorldSettings.Textures.setMcpe(true);
        } else {
            WorldSettings.Textures.setMcpe(false);
        }

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            this.indevWorldType = WorldSettings.IndevWorld.getIndevWorldType();
            this.shape = WorldSettings.IndevWorld.getShape();
            this.size = WorldSettings.IndevWorld.getSize();
            this.generateIndevHouse = WorldSettings.IndevWorld.isGenerateIndevHouse();
            this.infiniteWorld = WorldSettings.IndevWorld.isInfiniteWorld();
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/WorldProperties;)V", at = @At("TAIL"))
    private void onCopyConstructor(net.minecraft.world.WorldProperties source, CallbackInfo ci) {
        this.worldType = ((BWOProperties) source).bwo_getWorldType();
        this.hardcore = ((BWOProperties) source).bwo_isHardcore();
        this.betaFeatures = ((BWOProperties) source).bwo_getBetaFeatures();
        this.singleBiome = ((BWOProperties) source).bwo_getSingleBiome();
        this.theme = ((BWOProperties) source).bwo_getTheme();

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            this.indevWorldType = ((BWOProperties) source).bwo_getIndevWorldType();
            this.shape = ((BWOProperties) source).bwo_getShape();
            this.size = ((BWOProperties) source).bwo_getSize();
            this.generateIndevHouse = ((BWOProperties) source).bwo_isGenerateIndevHouse();
            this.infiniteWorld = ((BWOProperties) source).bwo_isInfiniteWorld();
        }
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void onUpdateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        nbt.putString("WorldType", this.worldType);
        nbt.putBoolean("Hardcore", this.hardcore);
        nbt.putBoolean("BetaFeatures", this.betaFeatures);
        nbt.putString("SingleBiome", this.singleBiome);
        nbt.putString("Theme", this.theme);

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            nbt.putString("IndevWorldType", this.indevWorldType);
            nbt.putString("Shape", this.shape);
            nbt.putString("Size", this.size);
            nbt.putBoolean("GenerateIndevHouse", this.generateIndevHouse);
            nbt.putBoolean("InfiniteWorld", this.infiniteWorld);
        }
    }
}


