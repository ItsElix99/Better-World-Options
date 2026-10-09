package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.*;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import net.minecraft.world.World;

public abstract class ReleaseLayer extends Layer {

    public static Layer[] init(World world) {
        advUpdBiomeProperties = (AdvUpdBiomeProperties) ((BWOWorld) world).bwo_getBiomeProperties();
        IslandLayer var2 = new IslandLayer(1L);
        FuzzyZoomLayer var10 = new FuzzyZoomLayer(2000L, var2);
        ReleaseAddIslandLayer var11 = new ReleaseAddIslandLayer(1L, var10);
        NormalZoomLayer var12 = new NormalZoomLayer(2001L, var11);
        var11 = new ReleaseAddIslandLayer(2L, var12);
        AddSnowyBiomesLayer var13 = new AddSnowyBiomesLayer(2L, var11);
        var12 = new NormalZoomLayer(2002L, var13);
        var11 = new ReleaseAddIslandLayer(3L, var12);
        var12 = new NormalZoomLayer(2003L, var11);
        var11 = new ReleaseAddIslandLayer(4L, var12);
        AddMushroomIslandLayer var16 = new AddMushroomIslandLayer(5L, var11);
        byte var3 = 4;
        if (((BWOProperties)world.getProperties()).bwo_getOptionValue("Large Biomes", OptionType.WORLD_TYPE_OPTION, false)) {
            var3 = 6;
        }

        Layer var4 = NormalZoomLayer.zoom(1000L, var16, 0);
        RiverInitLayer var14 = new RiverInitLayer(100L, var4);
        var4 = NormalZoomLayer.zoom(1000L, var14, var3 + 2);
        RiverLayer var15 = new RiverLayer(1L, var4);
        SmoothLayer var17 = new SmoothLayer(1000L, var15);
        Layer var5 = NormalZoomLayer.zoom(1000L, var16, 0);
        ReleaseBiomeInitLayer var18 = new ReleaseBiomeInitLayer(200L, var5);
        Layer var19 = NormalZoomLayer.zoom(1000L, var18, 2);
        Layer var6 = new TemperatureLayer(var19);
        Layer var7 = new DownfallLayer(var19);

        for(int var8 = 0; var8 < var3; ++var8) {
            var19 = new NormalZoomLayer(1000 + var8, var19);
            if(var8 == 0) {
                var19 = new ReleaseAddIslandLayer(3L, var19);
            }

            if(var8 == 0) {
                var19 = new ShoreLayer(1000L, var19);
            }

            SmoothZoomLayer var20 = new SmoothZoomLayer(1000 + var8, var6);
            var6 = new TemperatureBlendLayer(var20, var19, var8);
            SmoothZoomLayer var22 = new SmoothZoomLayer(1000 + var8, var7);
            var7 = new DownfallBlendLayer(var22, var19, var8);
        }

        SmoothLayer var23 = new SmoothLayer(1000L, var19);
        ReleaseOceanRiverMixerLayer var24 = new ReleaseOceanRiverMixerLayer(100L, var23, var17);
        Layer var21 = SmoothZoomLayer.zoom(1000L, var6, 2);
        Layer var25 = SmoothZoomLayer.zoom(1000L, var7, 2);
        VoronoiZoomLayer var9 = new VoronoiZoomLayer(10L, var24);
        var24.setLocalWorldSeed(world.getSeed());
        var21.setLocalWorldSeed(world.getSeed());
        var25.setLocalWorldSeed(world.getSeed());
        var9.setLocalWorldSeed(world.getSeed());
        return new Layer[]{var24, var9, var21, var25, var24};
    }

    public ReleaseLayer(long seed) {
        super(seed);
    }
}