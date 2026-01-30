package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.feature.IndevFeatures;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(World.class)
public abstract class WorldMixin implements BWOWorld {
    @Shadow public Random random;
    @Shadow @Final @Mutable public final Dimension dimension;
    @Shadow public int difficulty;

    @Shadow public abstract int getBlockId(int x, int y, int z);
    @Shadow public abstract boolean setBlock(int x, int y, int z, int blockId);
    @Shadow public abstract WorldProperties getProperties();
    @Shadow public abstract Material getMaterial(int x, int y, int z);

    protected WorldMixin(Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public void bwo_setSnow(boolean bl) {
        OverworldBiomeProviderImpl.getInstance()
                .getBiomes()
                .stream()
                .filter(biome -> BWOWorldPropertiesStorage.defaultBiomesSetSnow.contains(biome))
                .forEach(biome -> biome.setSnow(bl));
    }

    @Override
    public void bwo_setPrecipitation(boolean bl) {
        OverworldBiomeProviderImpl.getInstance()
                .getBiomes()
                .stream()
                .filter(biome -> BWOWorldPropertiesStorage.defaultBiomesSetPrecipitation.contains(biome))
                .forEach(biome -> biome.setPrecipitation(bl));

        if (!bl) {
            OverworldBiomeProviderImpl.getInstance()
                    .getBiomes()
                    .stream()
                    .filter(biome -> BWOWorldPropertiesStorage.defaultBiomesSetPrecipitationNoSnow.contains(biome))
                    .forEach(biome -> biome.setSnow(false));
        } else {
            OverworldBiomeProviderImpl.getInstance()
                    .getBiomes()
                    .stream()
                    .filter(biome -> BWOWorldPropertiesStorage.defaultBiomesSetPrecipitationNoSnow.contains(biome))
                    .forEach(biome -> biome.setSnow(true));
        }
    }

    @Override
    public void bwo_oldBiomeSetSnow(String worldType, boolean bl) {
        switch (worldType) {
            case "Alpha 1.1.2_01" -> BetterWorldOptions.Alpha.setSnow(bl);
            case "Infdev 611", "Infdev 420", "Infdev 415" -> BetterWorldOptions.Infdev.setSnow(bl);
            case "Early Infdev" -> BetterWorldOptions.EarlyInfdev.setSnow(bl);
            case "Indev 223" -> BetterWorldOptions.Indev.setSnow(bl);
        }
    }

    @Override
    public void bwo_oldBiomeSetPrecipitation(String worldType, boolean bl) {
        switch (worldType) {
            case "Alpha 1.1.2_01" -> BetterWorldOptions.Alpha.setPrecipitation(bl);
            case "Infdev 611", "Infdev 420", "Infdev 415" -> BetterWorldOptions.Infdev.setPrecipitation(bl);
            case "Early Infdev" -> BetterWorldOptions.EarlyInfdev.setPrecipitation(bl);
            case "Indev 223" -> BetterWorldOptions.Indev.setPrecipitation(bl);
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V", at = @At("TAIL"))
    private void bwo_setOldTexturesChangingDimension(World world, Dimension dimension, CallbackInfo ci) {
        BWOWorldPropertiesStorage.getInstance().setOldTextures(dimension.id == 0 && ((BWOProperties) world.getProperties()).bwo_isOldFeatures());
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V", at = @At("TAIL"))
    private void bwo_setOldTexturesSetWorld(WorldStorage worldStorage, String name, long seed, Dimension dimension, CallbackInfo ci) {
        BWOWorldPropertiesStorage.getInstance().setOldTextures(this.getProperties().getDimensionId() == 0 && ((BWOProperties) this.getProperties()).bwo_isOldFeatures());
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V", at = @At("TAIL"))
    private void bwo_setOldTexturesSetWorld2(WorldStorage worldStorage, String name, Dimension dimension, long seed, CallbackInfo ci) {
        BWOWorldPropertiesStorage.getInstance().setOldTextures(this.getProperties().getDimensionId() == 0 && ((BWOProperties) this.getProperties()).bwo_isOldFeatures());
    }

    @WrapOperation(method = "initializeSpawnPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProperties;setSpawn(III)V"))
    private void bwo_initializeSpawnPoint(WorldProperties properties, int x, int y, int z, Operation<Void> original) {
        BWOProperties bwoProperties = (BWOProperties) properties;
        String worldType = bwoProperties.bwo_getWorldType();
        boolean generateIndevHouse = bwoProperties.bwo_getBooleanOptionValue("GenerateIndevHouse", OptionType.WORLD_TYPE_OPTION);
        boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        String finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);
        int sizeX = bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION);
        int sizeZ = bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION);

        if (worldType.equals("Indev 223")) {
            boolean isValidSpawnArea = false;
            int var1 = 0;
            int var2 = 0;
            int var3 = 0;

            while (!isValidSpawnArea) {
                if (!finiteWorld) {
                    var1 += this.random.nextInt(64) - this.random.nextInt(64);
                    var3 += this.random.nextInt(64) - this.random.nextInt(64);
                } else {
                    var1 = this.random.nextInt(sizeX / 2) + sizeX / 4;
                    var3 = this.random.nextInt(sizeZ / 2) + sizeZ / 4;
                }

                var2 = random.nextInt(64, 67);
                if (!generateIndevHouse && this.dimension.isValidSpawnPoint(var1, var3) || this.isValidSpawnArea(var1, var2, var3)) {
                    isValidSpawnArea = true;
                }
            }

            original.call(properties, var1, var2 - 1, var3);

            if (generateIndevHouse) {
                IndevFeatures.placeSpawnBuilding(World.class.cast(this));
            }
        } else if (finiteType.equals("MCPE")) {
            int var1 = sizeX / 2;
            int var2 = 64;
            int var3;

            for (var3 = sizeZ / 2; !this.dimension.isValidSpawnPoint(var1, var3); var3 += this.random.nextInt(64) - this.random.nextInt(64)) {
                var1 += this.random.nextInt(64) - this.random.nextInt(64);
            }

            original.call(properties, var1, var2, var3);
        } else {
            original.call(properties, x, y, z);
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

    @ModifyReturnValue(
            method = "getAmbientDarkness",
            at = @At("RETURN")
    )
    private int bwo_modifySkylight(int original) {
        String theme = ((BWOProperties) this.getProperties()).bwo_getTheme();

        if (this.dimension.id == 0) {
            if (theme.equals("Hell")) {
                if (original < 9) {
                    return 9;
                } else if (original == 11) {
                    return 12;
                }
            } else if (theme.equals("Woods")) {
                if (original < 4) {
                    return 4;
                }
            }
        }

        return original;
    }

    @Environment(EnvType.CLIENT)
    @WrapOperation(
            method = "getCloudColor(F)Lnet/minecraft/util/math/Vec3d;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;worldTimeMask:J"
            )
    )
    private long bwo_themeCloudsColor(World world, Operation<Long> original) {
        String theme = ((BWOProperties) world.getProperties()).bwo_getTheme();

        if (world.dimension.id == 0) {
            switch (theme) {
                case "Hell" -> {
                    return 2164736;
                }
                case "Paradise" -> {
                    return 15658751;
                }
                case "Woods" -> {
                    return 5069403;
                }
            }
        }


        return original.call(world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void bwo_hardcoreDifficulty(CallbackInfo ci) {
        if (((BWOProperties) this.getProperties()).bwo_isHardcore() && this.difficulty < 3) {
            this.difficulty = 3;
        }
    }

    @ModifyReturnValue(method = "getRainGradient", at = @At("RETURN"))
    private float bwo_noRainGradientInHellAndParadise(float original) {
        String theme = ((BWOProperties) this.getProperties()).bwo_getTheme();

        if (this.dimension.id == 0) {
            if (theme.equals("Hell") || theme.equals("Paradise")) {
                return 0.0F;
            }
        }

        return original;
    }

    @ModifyReturnValue(method = "getThunderGradient", at = @At("RETURN"))
    private float bwo_noThunderGradientInHellAndParadise(float original) {
        String theme = ((BWOProperties) this.getProperties()).bwo_getTheme();

        if (this.dimension.id == 0) {
            if (theme.equals("Hell") || theme.equals("Paradise")) {
                return 0.0F;
            }
        }

        return original;
    }
}


