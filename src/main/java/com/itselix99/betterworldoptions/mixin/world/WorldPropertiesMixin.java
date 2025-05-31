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

    @Override public void bwo_setBetaFeatures(boolean betaFeatures) {
        this.betaFeatures = betaFeatures;
    }

    @Override public boolean bwo_getBetaFeatures() {
        return this.betaFeatures;
    }

    @Override public void bwo_setSnowCovered(boolean snowCovered) {
            this.snowCovered = snowCovered;
    }

    @Override public boolean bwo_getSnowCovered() {
        return this.snowCovered;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void onLoadFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.worldType = nbt.getString("WorldType");
        this.hardcore = nbt.getBoolean("Hardcore");
        this.betaFeatures = nbt.getBoolean("BetaFeatures");
        WorldSettings.isBetaFeatures = this.betaFeatures;
        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            this.snowCovered = nbt.getBoolean("SnowCovered");
        }
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void newWorld(long seed, String name, CallbackInfo ci) {
        this.worldType = WorldSettings.worldTypeName;
        this.hardcore = WorldSettings.hardcore;
        this.betaFeatures = WorldSettings.betaFeatures;
        WorldSettings.isBetaFeatures = this.betaFeatures;
        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            this.snowCovered = WorldSettings.alphaSnowCovered;
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/WorldProperties;)V", at = @At("TAIL"))
    private void onCopyConstructor(net.minecraft.world.WorldProperties source, CallbackInfo ci) {
        this.worldType = ((BWOProperties) source).bwo_getWorldType();
        this.hardcore = ((BWOProperties) source).bwo_getHardcore();
        this.betaFeatures = ((BWOProperties) source).bwo_getBetaFeatures();
        WorldSettings.isBetaFeatures = this.betaFeatures;
        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            this.snowCovered = ((BWOProperties) source).bwo_getSnowCovered();
        }
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void onUpdateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        nbt.putString("WorldType", worldType);
        nbt.putBoolean("Hardcore", hardcore);
        nbt.putBoolean("BetaFeatures", betaFeatures);
        if (Objects.equals(bwo_getWorldType(), "Alpha 1.1.2_01")) {
            nbt.putBoolean("SnowCovered", snowCovered);
        }
    }
}


