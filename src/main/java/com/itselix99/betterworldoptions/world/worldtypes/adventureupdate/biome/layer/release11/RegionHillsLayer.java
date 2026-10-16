package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release11;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.Release11BiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.ReleaseBiomeProperties;
import net.minecraft.world.biome.Biome;

public class RegionHillsLayer extends Layer {
	public RegionHillsLayer(long seed, Layer parent) {
		super(seed);
		this.parent = parent;
	}

	public int[] getArea(int x, int z, int sizeX, int sizeZ) {
		int[] var5 = this.parent.getArea(x - 1, z - 1, sizeX + 2, sizeZ + 2);
		int[] var6 = IntCache.get(sizeX * sizeZ);

		for(int var7 = 0; var7 < sizeZ; ++var7) {
			for(int var8 = 0; var8 < sizeX; ++var8) {
				this.setChunkSeed(var8 + x, var7 + z);
				int var9 = var5[var8 + 1 + (var7 + 1) * (sizeX + 2)];
				if(this.nextInt(3) == 0) {
					int var10 = var9;
					if(var9 == advUpdBiomeProperties.getBiomeId(Biome.DESERT)) {
						var10 = advUpdBiomeProperties.getBiomeId(Release11BiomeProperties.DESERT_HILLS);
					} else if(var9 == advUpdBiomeProperties.getBiomeId(Biome.FOREST)) {
						var10 = advUpdBiomeProperties.getBiomeId(Release11BiomeProperties.FOREST_HILLS);
					} else if(var9 == advUpdBiomeProperties.getBiomeId(Biome.TAIGA)) {
						var10 = advUpdBiomeProperties.getBiomeId(Release11BiomeProperties.TAIGA_HILLS);
					} else if(var9 == advUpdBiomeProperties.getBiomeId(Biome.PLAINS)) {
						var10 = advUpdBiomeProperties.getBiomeId(Biome.FOREST);
					} else if(var9 == advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.ICE_PLAINS)) {
						var10 = advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.ICE_MOUNTAINS);
					}

					if(var10 != var9) {
						int var11 = var5[var8 + 1 + (var7 + 1 - 1) * (sizeX + 2)];
						int var12 = var5[var8 + 1 + 1 + (var7 + 1) * (sizeX + 2)];
						int var13 = var5[var8 + 1 - 1 + (var7 + 1) * (sizeX + 2)];
						int var14 = var5[var8 + 1 + (var7 + 1 + 1) * (sizeX + 2)];
						if(var11 == var9 && var12 == var9 && var13 == var9 && var14 == var9) {
							var6[var8 + var7 * sizeX] = var10;
						} else {
							var6[var8 + var7 * sizeX] = var9;
						}
					} else {
						var6[var8 + var7 * sizeX] = var9;
					}
				} else {
					var6[var8 + var7 * sizeX] = var9;
				}
			}
		}

		return var6;
	}
}