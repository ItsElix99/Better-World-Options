package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.gui.screen.BWOConfirmWorldVersionScreen;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin extends Screen {

    @Shadow protected abstract String getWorldName(int index);

    @Inject(
            method = "selectWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;startGame(Ljava/lang/String;Ljava/lang/String;J)V"
            ),
            cancellable = true
    )
    private void bwo_worldVersionScreen(int var1, CallbackInfo ci, @Local(ordinal = 0) String worldName) {
        FlattenedWorldStorage flattenedWorldStorage = (FlattenedWorldStorage) this.minecraft.getWorldStorageSource();
        NbtCompound worldTag = flattenedWorldStorage.getWorldTag(worldName);

        if (worldTag != null && worldTag.contains("WorldType") && !worldTag.contains("BetterWorldOptions") && flattenedWorldStorage.needsConversion(worldName)) {
            this.minecraft.setScreen(new BWOConfirmWorldVersionScreen(SelectWorldScreen.class.cast(this), this.minecraft, "Which version of Better World Options was this world created in?", "", "0.2.x", "0.3.x", worldName, this.getWorldName(var1), 0L));
            ci.cancel();
        }
    }
}