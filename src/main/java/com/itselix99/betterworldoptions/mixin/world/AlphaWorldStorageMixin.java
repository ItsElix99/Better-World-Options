package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWOGetDirectoryName;
import net.minecraft.world.storage.AlphaWorldStorage;
import net.minecraft.world.storage.WorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.File;

@Mixin(AlphaWorldStorage.class)
public abstract class AlphaWorldStorageMixin implements WorldStorage, BWOGetDirectoryName {
    @Unique private String dirName;

    @Inject(method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/File;mkdirs()Z",
                    shift = At.Shift.AFTER
            )
    )
    public void setDirectoryName(File savesDir, String name, boolean createPlayerDataDir, CallbackInfo ci) {
        this.dirName = name;
    }

    @Override
    public String bwo_getDirectoryName() {
        return dirName;
    }
}