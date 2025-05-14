package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.WorldProperties.class)
public class WorldPropertiesMixin implements BWOProperties {
    @Unique
    private String worldType;

    @Unique
    private boolean hardcore;

    @Override
    public void bwo_setWorldType(String name) {
        this.worldType = name;
    }

    @Override
    public String bwo_getWorldType() {
        return this.worldType;
    }

    @Override
    public void bwo_setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
    }

    @Override
    public boolean bwo_getHardcore() {
        return this.hardcore;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void onLoadFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.worldType = nbt.getString("WorldType");
        this.hardcore = nbt.getBoolean("Hardcore");
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void newWorld(long seed, String name, CallbackInfo ci) {
        this.hardcore = WorldSettings.hardcore;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/WorldProperties;)V", at = @At("TAIL"))
    private void onCopyConstructor(net.minecraft.world.WorldProperties source, CallbackInfo ci) {
        this.worldType = ((BWOProperties) source).bwo_getWorldType();
        this.hardcore = ((BWOProperties) source).bwo_getHardcore();
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void onUpdateProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        nbt.putString("WorldType", worldType);
        nbt.putBoolean("Hardcore", hardcore);
    }
}


