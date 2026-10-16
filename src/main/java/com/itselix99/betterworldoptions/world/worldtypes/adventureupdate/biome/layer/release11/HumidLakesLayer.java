package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release11;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import net.minecraft.world.biome.Biome;

public class HumidLakesLayer extends Layer {
	public HumidLakesLayer(long seed, Layer parent) {
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
				if(var9 == advUpdBiomeProperties.getBiomeId(Biome.SWAMPLAND) && this.nextInt(6) == 0) {
					var6[var8 + var7 * sizeX] = advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.RIVER);
				} else {
					var6[var8 + var7 * sizeX] = var9;
				}
			}
		}

		return var6;
	}
}