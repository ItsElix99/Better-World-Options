package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.SERVER)
@Mixin(BiomeColorsImpl.class)
public class BiomeColorsImplServerMixin {

    @ModifyReturnValue(
            method = "lambda$static$0",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private static int modifyGrassColor(int original) {
        MinecraftServer minecraftServer = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();

        if (oldFeatures && worldType.equals("MCPE") && minecraftServer.getWorld(0).dimension.id == 0) {
            return 3381555;
        }

        return original;
    }

    @ModifyReturnValue(
            method = "lambda$static$1",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private static int modifyLeavesColor(int original) {
        MinecraftServer minecraftServer = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();

        if (oldFeatures && worldType.equals("MCPE") && minecraftServer.getWorld(0).dimension.id == 0) {
            return 6396257;
        }

        return original;
    }

    @ModifyReturnValue(
            method = "lambda$static$2",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private static int modifyFogColor(int original) {
        MinecraftServer minecraftServer = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();
        String theme = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getTheme();
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);

        if (minecraftServer.getWorld(0).dimension.id == 0) {
            if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldFeaturesBiomeSupplier.get() == null && oldFeaturesProperties.defaultFogColor != 1 && (theme.equals("Normal") || theme.equals("Winter"))) {
                return oldFeaturesProperties.defaultFogColor;
            } else if (theme.equals("Hell")) {
                return 1049600;
            } else if (theme.equals("Paradise")) {
                return 13033215;
            } else if (theme.equals("Woods")) {
                return 5069403;
            }
        }

        return original;
    }
}