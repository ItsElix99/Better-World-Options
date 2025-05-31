package com.itselix99.betterworldoptions.mixin.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.storage.WorldSaveInfo;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FlattenedWorldStorage.class)
public class FlattenedWorldStorageMixin {

    @Inject(
            method = "getAll",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z",
                    shift = Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void getAll(CallbackInfoReturnable<List> info, ArrayList worlds, File[] var2, int var3, int var4, File worldPath, String worldFolder, net.minecraft.world.WorldProperties data, NbtCompound worldTag, boolean requiresUpdating, String worldName) {
        WorldSaveInfo worldSaveInfo = (WorldSaveInfo)worlds.get(worlds.size() - 1);
        String worldType = ((BWOProperties)data).bwo_getWorldType();
        boolean isHardcore = ((BWOProperties)data).bwo_getHardcore();
        ((BWOProperties)worldSaveInfo).bwo_setWorldType(worldType);
        ((BWOProperties)worldSaveInfo).bwo_setHardcore(isHardcore);
    }
}
