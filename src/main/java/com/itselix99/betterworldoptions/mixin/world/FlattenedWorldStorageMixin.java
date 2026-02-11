package com.itselix99.betterworldoptions.mixin.world;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.storage.RegionWorldStorageSource;
import net.minecraft.world.storage.WorldSaveInfo;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FlattenedWorldStorage.class)
public abstract class FlattenedWorldStorageMixin extends RegionWorldStorageSource {

    public FlattenedWorldStorageMixin(File file) {
        super(file);
    }

    @Shadow public abstract NbtCompound getWorldTag(String worldFolder);

    @ModifyArgs(
            method = "getAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/storage/WorldSaveInfo;<init>(Ljava/lang/String;Ljava/lang/String;JJZ)V"
            )
    )
    private void bwo_checkNeedsConvert(Args args, @Local(name = "worldTag") NbtCompound worldTag) {
        if (worldTag != null && worldTag.contains("WorldType") && !worldTag.contains("BetterWorldOptions")) {
            args.set(4, true);
        }
    }

    @ModifyReturnValue(
            method = "needsConversion",
            at = @At(value = "RETURN")
    )
    private boolean bwo_needsConversion(boolean original, String world) {
        NbtCompound worldTag = this.getWorldTag(world);

        if (worldTag != null && worldTag.contains("WorldType") && !worldTag.contains("BetterWorldOptions")) {
            return true;
        }

        return original;
    }

    @ModifyReturnValue(
            method = "convert",
            at = @At(value = "RETURN")
    )
    private boolean bwo_convert(boolean original, String worldFolder, LoadingDisplay progress) {
        NbtCompound worldTag = this.getWorldTag(worldFolder);

        if (worldTag != null && worldTag.contains("WorldType") && !worldTag.contains("BetterWorldOptions")) {
            return bwo_convertWorldToNewerBWO(worldFolder, progress);
        }

        return original;
    }

    @Unique
    private boolean bwo_convertWorldToNewerBWO(String worldFolder, LoadingDisplay progress) {
        File world = new File(this.dir, worldFolder);

        progress.progressStage("Converting world to Better World Options 0.4.0");

        NbtCompound newWorldTag = new NbtCompound();
        NbtCompound dataTag = this.getWorldTag(worldFolder);
        NbtCompound betterWorldOptionsTag = new NbtCompound();
        NbtCompound worldTypeOptionsTag = new NbtCompound();

        betterWorldOptionsTag.putString("WorldType", dataTag.getString("WorldType"));
        betterWorldOptionsTag.putBoolean("Hardcore", dataTag.getBoolean("Hardcore"));
        betterWorldOptionsTag.putString("SingleBiome", dataTag.getString("SingleBiome"));
        betterWorldOptionsTag.putString("Theme", dataTag.getString("Theme"));
        betterWorldOptionsTag.putBoolean("OldFeatures", dataTag.getBoolean("OldFeatures"));

        if (dataTag.getString("WorldType").equals("Flat")) {
            worldTypeOptionsTag.putBoolean("Superflat", dataTag.getBoolean("Superflat"));
        } else if (dataTag.getString("WorldType").equals("Farlands")) {
            betterWorldOptionsTag.putString("WorldType", "Default");
            betterWorldOptionsTag.putBoolean("Farlands", true);
            betterWorldOptionsTag.putString("FarlandsShape", "Linear");
            betterWorldOptionsTag.putInt("FarlandsDistance", 8);
        } else if (dataTag.getString("WorldType").equals("Skylands")) {
            worldTypeOptionsTag.putBoolean("SkyDimension", true);
        }

        if (dataTag.getString("WorldType").equals("Indev 223") || dataTag.getString("WorldType").equals("MCPE")) {
            if (dataTag.getString("WorldType").equals("Indev 223")) {
                betterWorldOptionsTag.putString("Shape", dataTag.getString("IndevShape"));
                betterWorldOptionsTag.putString("FiniteType", "Custom");
                worldTypeOptionsTag.putString("IndevWorldType", dataTag.getString("IndevWorldType"));
                worldTypeOptionsTag.putBoolean("GenerateIndevHouse", dataTag.getBoolean("GenerateIndevHouse"));
            }

            betterWorldOptionsTag.putBoolean("FiniteWorld", !dataTag.getBoolean("InfiniteWorld"));
            betterWorldOptionsTag.putInt("SizeX", dataTag.getInt("WorldSizeX"));
            betterWorldOptionsTag.putInt("SizeZ", dataTag.getInt("WorldSizeZ"));
        }

        if (dataTag.getString("WorldType").equals("Flat") || dataTag.getString("WorldType").equals("Skylands") || dataTag.getString("WorldType").equals("Indev 223")) {
            betterWorldOptionsTag.put("WorldTypeOptions", worldTypeOptionsTag);
        }

        dataTag.put("BetterWorldOptions", betterWorldOptionsTag);
        newWorldTag.put("Data", dataTag);

        try {
            File levelDatNew = new File(world, "level.dat_new");
            File levelDatOld = new File(world, "level.dat_old");
            File levelDat = new File(world, "level.dat");

            NbtIo.writeCompressed(newWorldTag, new FileOutputStream(levelDatNew));

            if (levelDatOld.exists()) {
                levelDatOld.delete();
            }
            levelDat.renameTo(levelDatOld);

            if (levelDat.exists()) {
                levelDat.delete();
            }
            levelDatNew.renameTo(levelDat);

            if (levelDatNew.exists()) {
                levelDatNew.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Inject(
            method = "getAll",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z",
                    shift = Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void bwo_setWorldTypeAndHardcore(CallbackInfoReturnable<List> info, ArrayList worlds, File[] var2, int var3, int var4, File worldPath, String worldFolder, net.minecraft.world.WorldProperties data, NbtCompound worldTag, boolean requiresUpdating, String worldName) {
        WorldSaveInfo worldSaveInfo = (WorldSaveInfo)worlds.get(worlds.size() - 1);
        String worldType = ((BWOProperties)data).bwo_getWorldType();
        boolean isHardcore = ((BWOProperties)data).bwo_isHardcore();
        ((BWOProperties)worldSaveInfo).bwo_setWorldType(worldType);
        ((BWOProperties)worldSaveInfo).bwo_setHardcore(isHardcore);
    }
}
