package com.itselix99.betterworldoptions.mixin.world;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraft.world.chunk.storage.RegionIo;
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
        RegionIo.flush();
        File world = new File(this.dir, worldFolder);

        NbtCompound newWorldTag = new NbtCompound();
        NbtCompound dataTag = this.getWorldTag(worldFolder);
        NbtCompound betterWorldOptionsTag = new NbtCompound();
        NbtCompound worldTypeOptionsTag = new NbtCompound();

        progress.progressStage("Converting world to Better World Options 0.4.0");

        if (BWOWorldPropertiesStorage.BWOWorldVersion.equals("0.2.0")) {
            betterWorldOptionsTag.putString("WorldType", dataTag.getString("WorldType"));
            betterWorldOptionsTag.putBoolean("Hardcore", dataTag.getBoolean("Hardcore"));
            betterWorldOptionsTag.putBoolean("OldFeatures", !dataTag.getBoolean("BetaFeatures"));

            if (dataTag.getString("WorldType").equals("Beta 1.1_02")) {
                betterWorldOptionsTag.putString("WorldType", "Alpha 1.2.0");
                betterWorldOptionsTag.putBoolean("OldFeatures", true);
            } else if (dataTag.getString("WorldType").equals("Alpha 1.1.2_01")) {
                betterWorldOptionsTag.putString("Theme", dataTag.getBoolean("SnowCovered") ? "Winter" : "Normal");
            } else if (dataTag.getString("WorldType").equals("Flat")) {
                betterWorldOptionsTag.putBoolean("OldFeatures", false);
                worldTypeOptionsTag.putBoolean("Superflat", dataTag.getBoolean("BetaFeatures"));
            } else if (dataTag.getString("WorldType").equals("Farlands")) {
                betterWorldOptionsTag.putString("WorldType", "Default");
                betterWorldOptionsTag.putBoolean("Farlands", true);
                betterWorldOptionsTag.putString("FarlandsShape", "Linear");
                betterWorldOptionsTag.putInt("FarlandsDistance", 8);
            } else if (dataTag.getString("WorldType").equals("Skylands")) {
                worldTypeOptionsTag.putBoolean("SkyDimension", true);
            }

            if (dataTag.getString("WorldType").equals("Flat") || dataTag.getString("WorldType").equals("Skylands")) {
                betterWorldOptionsTag.put("WorldTypeOptions", worldTypeOptionsTag);
            }

            dataTag.put("BetterWorldOptions", betterWorldOptionsTag);
            newWorldTag.put("Data", dataTag);

            if (!dataTag.getBoolean("BetaFeatures")) {
                File regionFolder = new File(world, "region");
                File[] regionFiles = regionFolder.listFiles((dir, name) -> name.endsWith(".mcr"));

                if (regionFiles != null) {
                    int totalRegions = regionFiles.length;
                    int processedRegions = 0;

                    for (File regionFile : regionFiles) {
                        convertRegion(regionFile, progress, processedRegions, totalRegions);
                        processedRegions++;
                    }
                }
            }
        } else if (BWOWorldPropertiesStorage.BWOWorldVersion.equals("0.3.0")) {
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
                    betterWorldOptionsTag.putString("FiniteWorldType", "Custom");
                    betterWorldOptionsTag.putBoolean("PregeneratingFiniteWorld", false);
                    worldTypeOptionsTag.putString("IndevWorldType", dataTag.getString("IndevWorldType"));
                    worldTypeOptionsTag.putBoolean("GenerateIndevHouse", dataTag.getBoolean("GenerateIndevHouse"));
                }

                betterWorldOptionsTag.putBoolean("FiniteWorld", !dataTag.getBoolean("InfiniteWorld"));
                betterWorldOptionsTag.putInt("Width", dataTag.getInt("WorldSizeX"));
                betterWorldOptionsTag.putInt("Length", dataTag.getInt("WorldSizeZ"));
            }

            if (dataTag.getString("WorldType").equals("Flat") || dataTag.getString("WorldType").equals("Skylands") || dataTag.getString("WorldType").equals("Indev 223")) {
                betterWorldOptionsTag.put("WorldTypeOptions", worldTypeOptionsTag);
            }

            dataTag.put("BetterWorldOptions", betterWorldOptionsTag);
            newWorldTag.put("Data", dataTag);
        }

        BWOWorldPropertiesStorage.BWOWorldVersion = null;

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

    @Unique
    private void convertRegion(File regionFile, LoadingDisplay progress, int currentRegion, int totalRegions) {
        try {
            RegionFile region = new RegionFile(regionFile);

            int totalChunks = 32 * 32;
            int processedChunks = 0;

            for (int chunkX = 0; chunkX < 32; chunkX++) {
                for (int chunkZ = 0; chunkZ < 32; chunkZ++) {
                    if (!region.hasChunkData(chunkX, chunkZ)) {
                        processedChunks++;
                        continue;
                    }

                    DataInputStream input = region.getChunkInputStream(chunkX, chunkZ);
                    if (input == null) {
                        processedChunks++;
                        continue;
                    }

                    NbtCompound chunkTag = NbtIo.read(input);
                    input.close();

                    NbtCompound level = chunkTag.getCompound("Level");
                    NbtList sections = level.getList("stationapi:sections");

                    boolean modified = false;

                    for (int i = 0; i < sections.size(); ++i) {
                        NbtElement entry = sections.get(i);
                        if (entry instanceof NbtCompound nbtCompound) {
                            NbtCompound blockStates = nbtCompound.getCompound("block_states");
                            NbtList palette = blockStates.getList("palette");

                            for (int i2 = 0; i2 < palette.size(); ++i2) {
                                NbtElement entry2 = palette.get(i2);
                                if (entry2 instanceof NbtCompound nbtCompound2) {
                                    if (nbtCompound2.getString("Name").equals("betterworldoptions:alpha_leaves")) {
                                        nbtCompound2.putString("Name", "minecraft:leaves");
                                        modified = true;
                                    }
                                }
                            }
                        }
                    }

                    if (modified) {
                        DataOutputStream output = region.getChunkOutputStream(chunkX, chunkZ);
                        NbtIo.write(chunkTag, output);
                        output.close();
                    }

                    processedChunks++;

                    float regionProgress = (float) processedChunks / (float) totalChunks;
                    float totalProgress = ((float) currentRegion + regionProgress) / (float) totalRegions;

                    progress.progressStagePercentage((int) (totalProgress * 100.0F));
                }
            }

            region.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
