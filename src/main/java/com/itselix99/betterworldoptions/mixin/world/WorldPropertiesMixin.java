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

    @Unique private boolean snowCovered;

    @Unique private String indevWorldType;
    @Unique private String shape;
    @Unique private String size;
    @Unique private String theme;
    @Unique private String betaTheme;
    @Unique private boolean indevDimesions;
    @Unique private boolean generateIndevHouse;
    @Unique private boolean infinite;

    @Override public void bwo_setWorldType(String name) {
        this.worldType = name;
    }
    @Override public String bwo_getWorldType() {
        return this.worldType;
    }

    @Override public void bwo_setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
    }
    @Override public boolean bwo_getHardcore() {
        return this.hardcore;
    }
    @Override public boolean bwo_getBetaFeatures() {
        return this.betaFeatures;
    }

    @Override public boolean bwo_getSnowCovered() {
        return this.snowCovered;
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
    @Override public String bwo_getBetaTheme() {
        return this.betaTheme;
    }
    @Override public String bwo_getTheme() {
        return this.theme;
    }
    @Override public boolean bwo_isIndevDimensions() {
        return this.indevDimesions;
    }
    @Override public boolean bwo_isGenerateIndevHouse() {
        return this.generateIndevHouse;
    }
    @Override public boolean bwo_isInfinite() {
        return this.infinite;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void onLoadFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.worldType = nbt.getString("WorldType");
        this.hardcore = nbt.getBoolean("Hardcore");
        this.betaFeatures = nbt.getBoolean("BetaFeatures");
        WorldSettings.Textures.setBetaFeaturesTextures(this.betaFeatures);

        if (!bwo_getBetaFeatures() && bwo_getWorldType().equals("MCPE")) {
            WorldSettings.Textures.setMcpe(true);
        } else {
            WorldSettings.Textures.setMcpe(false);
        }

        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            this.snowCovered = nbt.getBoolean("SnowCovered");
        }

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            this.indevWorldType = nbt.getString("IndevWorldType");
            this.shape = nbt.getString("Shape");
            this.size = nbt.getString("Size");

            if (this.betaFeatures) {
                this.betaTheme = nbt.getString("BetaTheme");
            } else {
                this.theme = nbt.getString("Theme");
            }

            this.indevDimesions = nbt.getBoolean("IndevDimensions");
            this.generateIndevHouse = nbt.getBoolean("GenerateIndevHouse");
            this.infinite = nbt.getBoolean("Infinite");
        }
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void newWorld(long seed, String name, CallbackInfo ci) {
        this.worldType = WorldSettings.World.getWorldTypeName();
        this.hardcore = WorldSettings.GameMode.isHardcore();
        this.betaFeatures = WorldSettings.GameMode.isBetaFeatures();
        WorldSettings.Textures.setBetaFeaturesTextures(this.betaFeatures);

        if (!bwo_getBetaFeatures() && bwo_getWorldType().equals("MCPE")) {
            WorldSettings.Textures.setMcpe(true);
        } else {
            WorldSettings.Textures.setMcpe(false);
        }

        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            this.snowCovered = WorldSettings.AlphaWorld.isSnowCovered();
        }

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            this.indevWorldType = WorldSettings.IndevWorld.getIndevWorldType();
            this.shape = WorldSettings.IndevWorld.getShape();
            this.size = WorldSettings.IndevWorld.getSize();

            if (this.betaFeatures) {
                this.betaTheme = WorldSettings.IndevWorld.getBetaTheme();
            } else {
                this.theme = WorldSettings.IndevWorld.getTheme();
            }

            this.indevDimesions = WorldSettings.IndevWorld.isIndevDimensions();
            this.generateIndevHouse = WorldSettings.IndevWorld.isGenerateIndevHouse();
            this.infinite = WorldSettings.IndevWorld.isInfinite();
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/WorldProperties;)V", at = @At("TAIL"))
    private void onCopyConstructor(net.minecraft.world.WorldProperties source, CallbackInfo ci) {
        this.worldType = ((BWOProperties) source).bwo_getWorldType();
        this.hardcore = ((BWOProperties) source).bwo_getHardcore();
        this.betaFeatures = ((BWOProperties) source).bwo_getBetaFeatures();

        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            this.snowCovered = ((BWOProperties) source).bwo_getSnowCovered();
        }

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            this.indevWorldType = ((BWOProperties) source).bwo_getIndevWorldType();
            this.shape = ((BWOProperties) source).bwo_getShape();
            this.size = ((BWOProperties) source).bwo_getSize();

            if (this.betaFeatures) {
                this.betaTheme = ((BWOProperties) source).bwo_getBetaTheme();
            } else {
                this.theme = ((BWOProperties) source).bwo_getTheme();
            }

            this.indevDimesions = ((BWOProperties) source).bwo_isIndevDimensions();
            this.generateIndevHouse = ((BWOProperties) source).bwo_isGenerateIndevHouse();
            this.infinite = ((BWOProperties) source).bwo_isInfinite();
        }
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void onUpdateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        nbt.putString("WorldType", this.worldType);
        nbt.putBoolean("Hardcore", this.hardcore);
        nbt.putBoolean("BetaFeatures", this.betaFeatures);

        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            nbt.putBoolean("SnowCovered", this.snowCovered);
        }

        if (Objects.equals(bwo_getWorldType(), "Indev 223")) {
            nbt.putString("IndevWorldType", this.indevWorldType);
            nbt.putString("Shape", this.shape);
            nbt.putString("Size", this.size);

            if (this.betaFeatures) {
                nbt.putString("BetaTheme", this.betaTheme);
            } else {
                nbt.putString("Theme", this.theme);
            }

            nbt.putBoolean("IndevDimensions", this.indevDimesions);
            nbt.putBoolean("GenerateIndevHouse", this.generateIndevHouse);
            nbt.putBoolean("Infinite", this.infinite);
        }
    }
}


