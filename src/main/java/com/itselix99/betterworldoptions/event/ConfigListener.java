package com.itselix99.betterworldoptions.event;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.glasslauncher.mods.gcapi3.api.PreConfigSavedListener;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.simpleyaml.configuration.ConfigurationSection;

@EventListener
public class ConfigListener implements PreConfigSavedListener {

    @Override
    public void onPreConfigSaved(int var1, GlassYamlFile var2, GlassYamlFile var3) {
        if (FabricLoaderImpl.INSTANCE.getEnvironmentType() == EnvType.CLIENT) {
            ConfigurationSection newEnvironment = var3.getConfigurationSection("environment");
            ConfigurationSection oldEnvironment = var2.getConfigurationSection("environment");

            if (BetterWorldOptions.getMinecraft() != null && newEnvironment != null && oldEnvironment != null) {
                if (BetterWorldOptions.getMinecraft().world != null && newEnvironment.getBoolean("oldTexturesAndSky") != oldEnvironment.getBoolean("oldTexturesAndSky")) {
                    BWOProperties bwoProperties = (BWOProperties) BetterWorldOptions.getMinecraft().world.getProperties();
                    Theme theme = ((BWOWorld) BetterWorldOptions.getMinecraft().world).bwo_getTheme();
                    OldFeaturesProperties oldFeaturesProperties = ((BWOWorld) BetterWorldOptions.getMinecraft().world).bwo_getWorldType().getOldFeaturesProperties();

                    if (bwoProperties.bwo_isOldFeatures() && BetterWorldOptions.getMinecraft().world.dimension.biomeSource instanceof FixedBiomeSource) {
                        if (!newEnvironment.getBoolean("oldTexturesAndSky")) {
                            if (oldFeaturesProperties != null && oldFeaturesProperties.oldBiome != null) {
                                oldFeaturesProperties.oldBiome.setGrassColorProvider(BiomeColorsImpl.DEFAULT_GRASS_COLOR);
                                oldFeaturesProperties.oldBiome.setLeavesColor(BiomeColorsImpl.DEFAULT_LEAVES_COLOR);
                                oldFeaturesProperties.oldBiome.setFogColor(BiomeColorsImpl.DEFAULT_FOG_COLOR);
                            }
                        } else if (oldFeaturesProperties != null && oldFeaturesProperties.oldBiome != null) {
                            oldFeaturesProperties.oldBiome.setGrassColorProvider(oldFeaturesProperties.defaultGrassColor);
                            oldFeaturesProperties.oldBiome.setLeavesColor(oldFeaturesProperties.defaultLeavesColor);
                            oldFeaturesProperties.oldBiome.setFogColor(theme.getFogColor() != 1 ? theme.getFogColor() : oldFeaturesProperties.defaultFogColor.getColor(null, 0, 0));
                        }
                    }

                    BetterWorldOptions.getMinecraft().worldRenderer.reload();
                }
            }
        }
    }
}