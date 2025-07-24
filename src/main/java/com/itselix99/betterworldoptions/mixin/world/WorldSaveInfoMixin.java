package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.storage.WorldSaveInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldSaveInfo.class)
public abstract class WorldSaveInfoMixin implements BWOProperties {
    @Unique private String worldType;
    @Unique private boolean hardcore;

    @Override public void bwo_setWorldType(String name) {
        this.worldType = name;
    }

    @Override public String bwo_getWorldType() { return this.worldType; }

    @Override public void bwo_setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
    }

    @Override public boolean bwo_getHardcore() {
        return this.hardcore;
    }

}