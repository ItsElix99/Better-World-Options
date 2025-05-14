package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.interfaces.GetDirectoryName;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.minecraft.world.World;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin implements GetDirectoryName {
    @Shadow public boolean newWorld;
    @Shadow protected net.minecraft.world.WorldProperties properties;
    @Shadow @Final @Mutable protected final WorldStorage dimensionData;

    protected WorldMixin(WorldStorage dimensionData) {
        this.dimensionData = dimensionData;
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/Dimension;setWorld(Lnet/minecraft/world/World;)V"
            )
    )
    private void beforeSetWorld(WorldStorage storage, String name, long seed, Dimension dimension, CallbackInfo ci) {
        if (this.newWorld) {
            ((BWOProperties) this.properties).bwo_setWorldType(WorldSettings.getName());
        } else {
            this.properties.setName(name);
            WorldTypeList.selectForWorldLoad(((BWOProperties) this.properties).bwo_getWorldType());
        }
    }

    @Override
    public WorldStorage bwo_getDimensionData() {
        return this.dimensionData;
    }
}


