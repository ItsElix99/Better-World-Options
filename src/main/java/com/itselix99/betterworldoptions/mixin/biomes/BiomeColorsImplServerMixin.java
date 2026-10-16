package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
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
        MinecraftServer minecraftServer = BetterWorldOptions.getMinecraftServer();
        WorldType worldType = ((BWOWorld) minecraftServer.getWorld(0)).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome == null && oldFeaturesProperties.defaultGrassColor != null && minecraftServer.getWorld(0).dimension.id == 0) {
            return oldFeaturesProperties.defaultGrassColor.getColor(null, 0, 0);
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
        MinecraftServer minecraftServer = BetterWorldOptions.getMinecraftServer();
        WorldType worldType = ((BWOWorld) minecraftServer.getWorld(0)).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome == null && oldFeaturesProperties.defaultLeavesColor != null && minecraftServer.getWorld(0).dimension.id == 0) {
            return oldFeaturesProperties.defaultLeavesColor.getColor(null, 0, 0);
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
        MinecraftServer minecraftServer = BetterWorldOptions.getMinecraftServer();
        WorldType worldType = ((BWOWorld) minecraftServer.getWorld(0)).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraftServer.getWorld(0).getProperties()).bwo_isOldFeatures();
        Theme theme = ((BWOWorld) minecraftServer.getWorld(0)).bwo_getTheme();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (minecraftServer.getWorld(0).dimension.id == 0) {
            if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome == null && oldFeaturesProperties.defaultFogColor != null && (theme.getFogColor() == -1)) {
                return oldFeaturesProperties.defaultFogColor.getColor(null, 0, 0);
            } else if (theme.getFogColor() != -1) {
                return theme.getFogColor();
            }
        }

        return original;
    }
}