package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import net.minecraft.world.World;

public abstract class Layer {
    private long localWorldSeed;
    protected Layer parent;
    private long chunkSeed;
    private long seed;
    protected static AdvUpdBiomeProperties advUpdBiomeProperties;

    public static Layer[] init(World world) {
        advUpdBiomeProperties = (AdvUpdBiomeProperties) ((BWOWorld) world).bwo_getBiomeProperties();
        IslandLayer var2 = new IslandLayer(1L);
        FuzzyZoomLayer var9 = new FuzzyZoomLayer(2000L, var2);
        AddIslandLayer var10 = new AddIslandLayer(1L, var9);
        NormalZoomLayer var11 = new NormalZoomLayer(2001L, var10);
        var10 = new AddIslandLayer(2L, var11);
        var11 = new NormalZoomLayer(2002L, var10);
        var10 = new AddIslandLayer(3L, var11);
        var11 = new NormalZoomLayer(2003L, var10);
        var10 = new AddIslandLayer(3L, var11);
        var11 = new NormalZoomLayer(2004L, var10);
        var10 = new AddIslandLayer(3L, var11);
        byte var3 = 4;
        if (((BWOProperties)world.getProperties()).bwo_getOptionValue("Large Biomes", OptionType.WORLD_TYPE_OPTION, false)) {
            var3 = 6;
        }

        Layer var4 = NormalZoomLayer.zoom(1000L, var10, 0);
        RiverInitLayer var12 = new RiverInitLayer(100L, var4);
        var4 = NormalZoomLayer.zoom(1000L, var12, var3 + 2);
        RiverLayer var13 = new RiverLayer(1L, var4);
        SmoothLayer var14 = new SmoothLayer(1000L, var13);
        Layer var5 = NormalZoomLayer.zoom(1000L, var10, 0);
        BiomeInitLayer var15 = new BiomeInitLayer(200L, var5);
        Layer var16 = NormalZoomLayer.zoom(1000L, var15, 2);
        Layer var6 = new TemperatureLayer(var16);
        Layer var7 = new DownfallLayer(var16);

        for(int var8 = 0; var8 < var3; ++var8) {
            var16 = new NormalZoomLayer(1000 + var8, var16);
            if(var8 == 0) {
                var16 = new AddIslandLayer(3L, var16);
            }

            SmoothZoomLayer var17 = new SmoothZoomLayer(1000 + var8, var6);
            var6 = new TemperatureBlendLayer(var17, var16, var8);
            SmoothZoomLayer var21 = new SmoothZoomLayer(1000 + var8, var7);
            var7 = new DownfallBlendLayer(var21, var16, var8);
        }

        SmoothLayer var18 = new SmoothLayer(1000L, var16);
        OceanRiverMixerLayer var20 = new OceanRiverMixerLayer(100L, var18, var14);
        Layer var19 = SmoothZoomLayer.zoom(1000L, var6, 2);
        Layer var22 = SmoothZoomLayer.zoom(1000L, var7, 2);
        VoronoiZoomLayer var23 = new VoronoiZoomLayer(10L, var20);
        var20.setLocalWorldSeed(world.getSeed());
        var19.setLocalWorldSeed(world.getSeed());
        var22.setLocalWorldSeed(world.getSeed());
        var23.setLocalWorldSeed(world.getSeed());
        return new Layer[]{var20, var23, var19, var22};
    }

    public Layer(long seed) {
        this.seed = seed;
        this.seed *= this.seed * 6364136223846793005L + 1442695040888963407L;
        this.seed += seed;
        this.seed *= this.seed * 6364136223846793005L + 1442695040888963407L;
        this.seed += seed;
        this.seed *= this.seed * 6364136223846793005L + 1442695040888963407L;
        this.seed += seed;
    }

    public void setLocalWorldSeed(long worldSeed) {
        this.localWorldSeed = worldSeed;
        if (this.parent != null) {
            this.parent.setLocalWorldSeed(worldSeed);
        }

        this.localWorldSeed *= this.localWorldSeed * 6364136223846793005L + 1442695040888963407L;
        this.localWorldSeed += this.seed;
        this.localWorldSeed *= this.localWorldSeed * 6364136223846793005L + 1442695040888963407L;
        this.localWorldSeed += this.seed;
        this.localWorldSeed *= this.localWorldSeed * 6364136223846793005L + 1442695040888963407L;
        this.localWorldSeed += this.seed;
    }

    public void setChunkSeed(long chunkX, long chunkZ) {
        this.chunkSeed = this.localWorldSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += chunkX;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += chunkZ;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += chunkX;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += chunkZ;
    }

    protected int nextInt(int bound) {
        int var2 = (int)((this.chunkSeed >> 24) % (long)bound);
        if (var2 < 0) {
            var2 += bound;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.localWorldSeed;
        return var2;
    }

    public abstract int[] getArea(int x, int z, int sizeX, int sizeZ);
}