package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.storage.WorldSaveInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldSaveInfo.class)
public class WorldSaveInfoMixin implements BWOProperties {
    @Unique private String worldType;
    @Unique private boolean hardcore;
    @Unique private boolean betaFeatures;
    @Unique private boolean snowCovered;

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
}