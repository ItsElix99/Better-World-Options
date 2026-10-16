package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source;

import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release11.Release11Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.Release11BiomeProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Release11BiomeSource extends AdvUpdBiomeSource {

    protected Release11BiomeSource() {
        super();
        this.biomesForSpawnPoint.clear();
        this.biomesForSpawnPoint.add(Biome.FOREST);
        this.biomesForSpawnPoint.add(Biome.PLAINS);
        this.biomesForSpawnPoint.add(Biome.TAIGA);
        this.biomesForSpawnPoint.add(Release11BiomeProperties.TAIGA_HILLS);
        this.biomesForSpawnPoint.add(Release11BiomeProperties.FOREST_HILLS);
    }

    public Release11BiomeSource(World world) {
        this();
        Layer[] var2 = Release11Layer.init(world);
        this.noiseBiomes = var2[0];
        this.biomes = var2[1];
        this.temperatures = var2[2];
        this.downfalls = var2[3];
    }
}