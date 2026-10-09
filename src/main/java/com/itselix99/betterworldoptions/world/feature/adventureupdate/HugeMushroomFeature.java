package com.itselix99.betterworldoptions.world.feature.adventureupdate;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class HugeMushroomFeature extends Feature {
	private int variant = -1;
	private static int myceliumBlockId = Block.GRASS_BLOCK.id;
	private static int[] mushroomBlockId = new int[]{Block.WOOL.id, Block.WOOL.id};

	public HugeMushroomFeature(int var1) {
		this.variant = var1;
	}

	public HugeMushroomFeature() {
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int var6 = random.nextInt(2);
		if(this.variant >= 0) {
			var6 = this.variant;
		}

		int var7 = random.nextInt(3) + 4;
		boolean var8 = true;
		if(y >= 1 && y + var7 + 1 <= world.getTopY()) {
			int var9;
			int var11;
			int var12;
			int var13;
			for(var9 = y; var9 <= y + 1 + var7; ++var9) {
				byte var10 = 3;
				if(var9 == y) {
					var10 = 0;
				}

				for(var11 = x - var10; var11 <= x + var10 && var8; ++var11) {
					for(var12 = z - var10; var12 <= z + var10 && var8; ++var12) {
						if(var9 >= world.getBottomY() && var9 < world.getTopY()) {
							var13 = world.getBlockId(var11, var9, var12);
							if(var13 != 0 && var13 != Block.LEAVES.id) {
								var8 = false;
							}
						} else {
							var8 = false;
						}
					}
				}
			}

			if(!var8) {
				return false;
			} else {
				var9 = world.getBlockId(x, y - 1, z);
				if(var9 != Block.DIRT.id && var9 != Block.GRASS_BLOCK.id && var9 != myceliumBlockId) {
					return false;
				} else if(!Block.BROWN_MUSHROOM.canPlaceAt(world, x, y, z)) {
					return false;
				} else {
					world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.DIRT.id);
					int var16 = y + var7;
					if(var6 == 1) {
						var16 = y + var7 - 3;
					}

					for(var11 = var16; var11 <= y + var7; ++var11) {
						var12 = 1;
						if(var11 < y + var7) {
							++var12;
						}

						if(var6 == 0) {
							var12 = 3;
						}

						for(var13 = x - var12; var13 <= x + var12; ++var13) {
							for(int var14 = z - var12; var14 <= z + var12; ++var14) {
								int var15 = 5;
								if(var13 == x - var12) {
									--var15;
								}

								if(var13 == x + var12) {
									++var15;
								}

								if(var14 == z - var12) {
									var15 -= 3;
								}

								if(var14 == z + var12) {
									var15 += 3;
								}

								if(var6 == 0 || var11 < y + var7) {
									if((var13 == x - var12 || var13 == x + var12) && (var14 == z - var12 || var14 == z + var12)) {
										continue;
									}

									if(var13 == x - (var12 - 1) && var14 == z - var12) {
										var15 = 1;
									}

									if(var13 == x - var12 && var14 == z - (var12 - 1)) {
										var15 = 1;
									}

									if(var13 == x + (var12 - 1) && var14 == z - var12) {
										var15 = 3;
									}

									if(var13 == x + var12 && var14 == z - (var12 - 1)) {
										var15 = 3;
									}

									if(var13 == x - (var12 - 1) && var14 == z + var12) {
										var15 = 7;
									}

									if(var13 == x - var12 && var14 == z + (var12 - 1)) {
										var15 = 7;
									}

									if(var13 == x + (var12 - 1) && var14 == z + var12) {
										var15 = 9;
									}

									if(var13 == x + var12 && var14 == z + (var12 - 1)) {
										var15 = 9;
									}
								}

								if(var15 == 5 && var11 < y + var7) {
									var15 = 0;
								}

								if((var15 != 0 || y >= y + var7 - 1) && !Block.BLOCKS_OPAQUE[world.getBlockId(var13, var11, var14)]) {
									int mushroomBlockID = mushroomBlockId[var6];
									if (mushroomBlockID == Block.WOOL.id) {
										var15 = var6 == 0 ? 12 : 14;
									}

									world.setBlockWithoutNotifyingNeighbors(var13, var11, var14, mushroomBlockID, var15);
								}
							}
						}
					}

					for(var11 = 0; var11 < var7; ++var11) {
						var12 = world.getBlockId(x, y + var11, z);
						if(!Block.BLOCKS_OPAQUE[var12]) {
							int mushroomBlockID = mushroomBlockId[var6];
							int metadata = 10;

							if (mushroomBlockID == Block.WOOL.id) {
								metadata = 0;
							}

							world.setBlockWithoutNotifyingNeighbors(x, y + var11, z, mushroomBlockID, metadata);
						}
					}

					return true;
				}
			}
		} else {
			return false;
		}
	}

	public static void setMyceliumBlock(int blockId) {
		myceliumBlockId = blockId;
	}

	public static void setMushroomBlock(int brownBlockId, int redBlockId) {
		mushroomBlockId[0] = brownBlockId;
		mushroomBlockId[1] = redBlockId;
	}
}