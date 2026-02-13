package com.itselix99.betterworldoptions.event;

import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.glasslauncher.mods.gcapi3.api.PreConfigSavedListener;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;
import org.simpleyaml.configuration.ConfigurationSection;

@EventListener
public class ConfigListener implements PreConfigSavedListener {

    @Override
    public void onPreConfigSaved(int var1, GlassYamlFile var2, GlassYamlFile var3) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        ConfigurationSection newEnvironment = var3.getConfigurationSection("environment");
        ConfigurationSection oldEnvironment = var2.getConfigurationSection("environment");

        if (minecraft != null && newEnvironment != null && oldEnvironment != null) {
            if (minecraft.world != null && newEnvironment.getBoolean("oldTexturesAndSky") != oldEnvironment.getBoolean("oldTexturesAndSky")) {
                BWOProperties bwoProperties = (BWOProperties) minecraft.world.getProperties();
                OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(bwoProperties.bwo_getWorldType());

                if (bwoProperties.bwo_isOldFeatures() && minecraft.world.dimension.biomeSource instanceof FixedBiomeSource) {
                    if (!newEnvironment.getBoolean("oldTexturesAndSky")) {
                        minecraft.world.dimension.biomeSource = new FixedBiomeSource(Biome.FOREST, 0.8D, 0.6D);
                    } else if (oldFeaturesProperties != null && oldFeaturesProperties.oldFeaturesBiomeSupplier.get() != null) {
                        minecraft.world.dimension.biomeSource = new FixedBiomeSource(oldFeaturesProperties.oldFeaturesBiomeSupplier.get(), 1.0D, 0.5D);
                    }
                }

                minecraft.worldRenderer.reload();
            }
        }
    }
}