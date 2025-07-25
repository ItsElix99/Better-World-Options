package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWOGetDirectoryName;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(World.class)
public abstract class WorldMixin implements BWOGetDirectoryName {
    @Shadow public Random random;
    @Shadow public boolean newWorld;
    @Shadow protected WorldProperties properties;
    @Shadow @Final @Mutable public final Dimension dimension;
    @Shadow @Final @Mutable protected final WorldStorage dimensionData;

    @Shadow public abstract int getBlockId(int x, int y, int z);
    @Shadow public abstract boolean setBlock(int x, int y, int z, int blockId);
    @Shadow public abstract boolean isAir(int x, int y, int z);
    @Shadow public abstract WorldProperties getProperties();
    @Shadow public abstract Material getMaterial(int x, int y, int z);


    protected WorldMixin(Dimension dimension, WorldStorage dimensionData) {
        this.dimension = dimension;
        this.dimensionData = dimensionData;
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/Dimension;setWorld(Lnet/minecraft/world/World;)V"
            )
    )
    private void beforeSetWorld(WorldStorage storage, String name, long seed, Dimension dimension, CallbackInfo ci) throws NoSuchMethodException {
        if (this.newWorld) {
            ((BWOProperties) this.properties).bwo_setWorldType(WorldSettings.World.getWorldTypeName());
        } else {
            this.properties.setName(name);
            WorldTypeList.selectWorldType(((BWOProperties) this.properties).bwo_getWorldType());
        }
    }

    @WrapOperation(method = "initializeSpawnPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProperties;setSpawn(III)V"))
    private void initializeSpawnPointIndev(WorldProperties instance, int x, int y, int z, Operation<Void> original) {
        String worldType = ((BWOProperties) instance).bwo_getWorldType();
        boolean generateIndevHouse = ((BWOProperties) instance).bwo_isGenerateIndevHouse();
        boolean infinite = ((BWOProperties) instance).bwo_isInfinite();

        if (worldType.equals("Indev 223")) {
            boolean isValidSpawnArea = false;
            int var1 = 0;
            int var2 = 0;
            int var3 = 0;

            while (!isValidSpawnArea) {
                if (infinite) {
                    var1 += this.random.nextInt(64) - this.random.nextInt(64);
                    var3 += this.random.nextInt(64) - this.random.nextInt(64);
                } else {
                    var1 = this.random.nextInt(this.getSizeX(instance) / 2) + this.getSizeX(instance) / 4;
                    var3 = this.random.nextInt(this.getSizeZ(instance) / 2) + this.getSizeZ(instance) / 4;
                }

                var2 = random.nextInt(64, 67);
                System.out.println(var2);
                if (!generateIndevHouse && this.dimension.isValidSpawnPoint(var1, var3) || this.isValidSpawnArea(var1, var2, var3)) {
                    isValidSpawnArea = true;
                }
            }

            original.call(instance, var1, var2 + 1, var3);
        } else {
            original.call(instance, x, y, z);
        }
    }

    @Unique
    private boolean isValidSpawnArea(int centerX, int centerY, int centerZ) {
        for (int x = centerX - 3; x <= centerX + 3; x++) {
            for (int y = centerY; y <= centerY + 3; y++) {
                for (int z = centerZ - 3; z <= centerZ + 3; z++) {
                    if (this.getMaterial(x, y, z).isSolid()) {
                        return false;
                    }
                }
            }
        }

        int floorY = centerY - 1;
        for (int x = centerX - 3; x <= centerX + 3; x++) {
            for (int z = centerZ - 5; z <= centerZ + 3; z++) {
                if (!Block.BLOCKS_OPAQUE[this.getBlockId(x, floorY, z)]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Unique
    private int getSizeX(WorldProperties properties) {
        String shape = ((BWOProperties) properties).bwo_getShape();
        String size = ((BWOProperties) properties).bwo_getSize();
        int sizeX = 0;

        if (shape.equals("Long")) {
            sizeX = switch (size) {
                case "Small" -> 256;
                case "Normal" -> 512;
                case "Huge" -> 1024;
                case "Very Huge" -> 2048;
                default -> sizeX;
            };
        } else {
            sizeX = switch (size) {
                case "Small" -> 128;
                case "Normal" -> 256;
                case "Huge" -> 512;
                case "Very Huge" -> 1024;
                default -> sizeX;
            };
        }

        return sizeX;
    }

    @Unique
    private int getSizeZ(WorldProperties properties) {
        String shape = ((BWOProperties) properties).bwo_getShape();
        String size = ((BWOProperties) properties).bwo_getSize();
        int sizeZ = 0;

        if (shape.equals("Long")) {
            sizeZ = switch (size) {
                case "Small" -> 64;
                case "Normal" -> 128;
                case "Huge" -> 256;
                case "Very Huge" -> 512;
                default -> sizeZ;
            };
        } else {
            sizeZ = switch (size) {
                case "Small" -> 128;
                case "Normal" -> 256;
                case "Huge" -> 512;
                case "Very Huge" -> 1024;
                default -> sizeZ;
            };
        }

        return sizeZ;
    }

    @ModifyReturnValue(
            method = "getAmbientDarkness",
            at = @At("RETURN")
    )
    private int modifySkylight(int original) {
        System.out.println(original);
        String worldType = ((BWOProperties) this.getProperties()).bwo_getWorldType();
        String indevTheme = ((BWOProperties) this.getProperties()).bwo_getTheme();
        boolean betaFeatures = ((BWOProperties) this.getProperties()).bwo_getBetaFeatures();

        if (worldType.equals("Indev 223") && !betaFeatures) {
            if (indevTheme.equals("Hell")) {
                if (original < 9) {
                    return 9;
                } else if (original == 11) {
                    return 12;
                }
            } else if (indevTheme.equals("Woods")) {
                if (original < 4) {
                    return 4;
                }
            }
        }

        return original;
    }

    @WrapOperation(
            method = "getCloudColor(F)Lnet/minecraft/util/math/Vec3d;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;worldTimeMask:J"
            )
    )
    private long indevHellCloudsColor(World world, Operation<Long> original) {
        String worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
        String indevTheme = ((BWOProperties) world.getProperties()).bwo_getTheme();
        boolean betaFeatures = ((BWOProperties) world.getProperties()).bwo_getBetaFeatures();

        if (worldType.equals("Indev 223") && !betaFeatures) {
            if (indevTheme.equals("Hell")) {
                return 2164736;
            } else if (indevTheme.equals("Paradise")) {
                return 15658751;
            } else if (indevTheme.equals("Woods")) {
                return 5069403;
            }
        }

        return original.call(world);
    }


    @Override
    public WorldStorage bwo_getDimensionData() {
        return this.dimensionData;
    }
}


