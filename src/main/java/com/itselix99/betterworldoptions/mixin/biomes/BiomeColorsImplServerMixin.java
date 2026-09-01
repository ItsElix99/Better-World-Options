package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.util.Identifier;
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
    private static int bwo_modifyGrassColor(int original) {
        MinecraftServer minecraftServer = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();

        if (oldFeatures && worldType.equals(BetterWorldOptions.NAMESPACE.id("mcpe").toString()) && minecraftServer.getWorld(0).dimension.id == 0) {
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
    private static int bwo_modifyLeavesColor(int original) {
        MinecraftServer minecraftServer = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();

        if (oldFeatures && worldType.equals(BetterWorldOptions.NAMESPACE.id("mcpe").toString()) && minecraftServer.getWorld(0).dimension.id == 0) {
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
    private static int bwo_modifyFogColor(int original) {
        MinecraftServer minecraftServer = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();
        Theme theme = Theme.getThemeById(Identifier.of(((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_getTheme()));
        OldFeaturesProperties oldFeaturesProperties = WorldType.getOldFeaturesProperties(Identifier.of(worldType));

        if (minecraftServer.getWorld(0).dimension.id == 0) {
            if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldFeaturesBiomeSupplier.get() == null && oldFeaturesProperties.defaultFogColor != -1 && (theme.getFogColor() == -1)) {
                return oldFeaturesProperties.defaultFogColor;
            } else if (theme.getFogColor() != -1) {
                return theme.getFogColor();
            }
        }

        return original;
    }
}