package com.itselix99.betterworldoptions.world.feature.adventureupdate;

import com.itselix99.betterworldoptions.api.MissingBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class LilyPadFeature extends Feature {
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		for(int var6 = 0; var6 < 10; ++var6) {
			int var7 = var3 + var2.nextInt(8) - var2.nextInt(8);
			int var8 = var4 + var2.nextInt(4) - var2.nextInt(4);
			int var9 = var5 + var2.nextInt(8) - var2.nextInt(8);
			if(var1.isAir(var7, var8, var9)) {
				if (MissingBlocks.lilyPad != null && MissingBlocks.lilyPad.canPlaceAt(var1, var7, var8, var9)) {
					var1.setBlockWithoutNotifyingNeighbors(var7, var8, var9, MissingBlocks.lilyPad.id);
				} else {
					var1.setBlockWithoutNotifyingNeighbors(var7, var8, var9, 0);
				}
			}
		}

		return true;
	}
}