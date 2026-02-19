package com.itselix99.betterworldoptions.event;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.glasslauncher.mods.gcapi3.api.PreConfigSavedListener;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;
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
                    OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(bwoProperties.bwo_getWorldType());

                    if (bwoProperties.bwo_isOldFeatures() && BetterWorldOptions.getMinecraft().world.dimension.biomeSource instanceof FixedBiomeSource) {
                        if (!newEnvironment.getBoolean("oldTexturesAndSky")) {
                            BetterWorldOptions.getMinecraft().world.dimension.biomeSource = new FixedBiomeSource(Biome.FOREST, 0.8D, 0.6D);
                        } else if (oldFeaturesProperties != null && oldFeaturesProperties.oldFeaturesBiomeSupplier.get() != null) {
                            BetterWorldOptions.getMinecraft().world.dimension.biomeSource = new FixedBiomeSource(oldFeaturesProperties.oldFeaturesBiomeSupplier.get(), 1.0D, 0.5D);
                        }
                    }

                    BetterWorldOptions.getMinecraft().worldRenderer.reload();
                }
            }
        }
    }
}