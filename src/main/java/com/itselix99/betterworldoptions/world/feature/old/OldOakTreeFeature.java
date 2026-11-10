package com.itselix99.betterworldoptions.world.feature.old;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class OldOakTreeFeature extends Feature {
	public boolean generate(World world, Random random, int x, int y, int z) {
		int var6 = random.nextInt(3) + 4;
		boolean var7 = true;
		if(y > world.getBottomY() && y + var6 + 1 <= world.getTopY()) {
			int var8;
			int var10;
			int var11;
			int var12;
			for(var8 = y; var8 <= y + 1 + var6; ++var8) {
				byte var9 = 1;
				if(var8 == y) {
					var9 = 0;
				}

				if(var8 >= y + 1 + var6 - 2) {
					var9 = 2;
				}

				for(var10 = x - var9; var10 <= x + var9 && var7; ++var10) {
					for(var11 = z - var9; var11 <= z + var9 && var7; ++var11) {
						if(var8 >= world.getBottomY() && var8 < world.getTopY()) {
							var12 = world.getBlockId(var10, var8, var11);
							if(var12 != 0 && var12 != Block.LEAVES.id) {
								var7 = false;
							}
						} else {
							var7 = false;
						}
					}
				}
			}

			if(!var7) {
				return false;
			} else {
				var8 = world.getBlockId(x, y - 1, z);
				if((var8 == Block.GRASS_BLOCK.id || var8 == Block.DIRT.id) && y < world.getTopY() - var6 - 1) {
					world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.DIRT.id);

					int var15;
					for(var15 = y - 3 + var6; var15 <= y + var6; ++var15) {
						var10 = var15 - (y + var6);
						var11 = 1 - var10 / 2;

						for(var12 = x - var11; var12 <= x + var11; ++var12) {
							int var14 = var12 - x;

							for(var8 = z - var11; var8 <= z + var11; ++var8) {
								int var13 = var8 - z;
								if((Math.abs(var14) != var11 || Math.abs(var13) != var11 || random.nextInt(2) != 0 && var10 != 0) && !Block.BLOCKS_OPAQUE[world.getBlockId(var12, var15, var8)]) {
									world.setBlockWithoutNotifyingNeighbors(var12, var15, var8, Block.LEAVES.id);
								}
							}
						}
					}

					for(var15 = 0; var15 < var6; ++var15) {
						int var16 = world.getBlockId(x, y + var15, z);
						if(var16 == 0 || var16 == Block.LEAVES.id) {
							world.setBlockWithoutNotifyingNeighbors(x, y + var15, z, Block.LOG.id);
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}