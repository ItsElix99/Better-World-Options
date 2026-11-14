package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release11;

import com.itselix99.betterworldoptions.interfaces.BWOBiome;
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
					if(var9 == ((BWOBiome) Biome.DESERT).bwo_getBiomeId()) {
						var10 = ((BWOBiome) Release11BiomeProperties.DESERT_HILLS).bwo_getBiomeId();
					} else if(var9 == ((BWOBiome) Biome.FOREST).bwo_getBiomeId()) {
						var10 = ((BWOBiome) Release11BiomeProperties.FOREST_HILLS).bwo_getBiomeId();
					} else if(var9 == ((BWOBiome) Biome.TAIGA).bwo_getBiomeId()) {
						var10 = ((BWOBiome) Release11BiomeProperties.TAIGA_HILLS).bwo_getBiomeId();
					} else if(var9 == ((BWOBiome) Biome.PLAINS).bwo_getBiomeId()) {
						var10 = ((BWOBiome) Biome.FOREST).bwo_getBiomeId();
					} else if(var9 == ((BWOBiome) ReleaseBiomeProperties.ICE_PLAINS).bwo_getBiomeId()) {
						var10 = ((BWOBiome) ReleaseBiomeProperties.ICE_MOUNTAINS).bwo_getBiomeId();
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