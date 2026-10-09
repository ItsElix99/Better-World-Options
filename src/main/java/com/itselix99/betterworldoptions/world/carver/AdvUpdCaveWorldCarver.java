package com.itselix99.betterworldoptions.world.carver;

import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Generator;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;

import java.util.Random;

public class AdvUpdCaveWorldCarver extends Generator implements CaveGenBaseImpl {
	private World world;

	public void stationapi_setWorld(World world) {
		this.world = world;
	}

	public World stationapi_getWorld() {
		if (this.world == null) {
			throw new RuntimeException("stationapi_world is null, use CaveGenBaseImpl.stationapi_setWorld in your custom ChunkSource constructor to fix.");
		}
		return this.world;
	}

	protected void place(long seed, int chunkX, int chunkZ, byte[] blocks, double x, double y, double z) {
		this.placeTunnels(seed, chunkX, chunkZ, blocks, x, y, z, 1.0F + this.random.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void placeTunnels(long seed, int chunkX, int chunkZ, byte[] blocks, double x, double y, double z, float baseWidth, float yaw, float pitch, int tunnel, int tunnelCount, double widthHeightRatio) {
		double var19 = chunkX * 16 + 8;
		double var21 = chunkZ * 16 + 8;
		float var23 = 0.0F;
		float var24 = 0.0F;
		Random var25 = new Random(seed);
		if(tunnelCount <= 0) {
			int var26 = this.range * 16 - 16;
			tunnelCount = var26 - var25.nextInt(var26 / 4);
		}

		boolean var54 = false;
		if(tunnel == -1) {
			tunnel = tunnelCount / 2;
			var54 = true;
		}

		int var27 = var25.nextInt(tunnelCount / 2) + tunnelCount / 4;

		for(boolean var28 = var25.nextInt(6) == 0; tunnel < tunnelCount; ++tunnel) {
			double var29 = 1.5D + (double)(MathHelper.sin((float)tunnel * (float)Math.PI / (float)tunnelCount) * baseWidth * 1.0F);
			double var31 = var29 * widthHeightRatio;
			float var33 = MathHelper.cos(pitch);
			float var34 = MathHelper.sin(pitch);
			x += MathHelper.cos(yaw) * var33;
			y += var34;
			z += MathHelper.sin(yaw) * var33;
			if(var28) {
				pitch *= 0.92F;
			} else {
				pitch *= 0.7F;
			}

			pitch += var24 * 0.1F;
			yaw += var23 * 0.1F;
			var24 *= 0.9F;
			var23 *= 12.0F / 16.0F;
			var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
			var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;
			if(!var54 && tunnel == var27 && baseWidth > 1.0F && tunnelCount > 0) {
				this.placeTunnels(var25.nextLong(), chunkX, chunkZ, blocks, x, y, z, var25.nextFloat() * 0.5F + 0.5F, yaw - (float)Math.PI * 0.5F, pitch / 3.0F, tunnel, tunnelCount, 1.0D);
				this.placeTunnels(var25.nextLong(), chunkX, chunkZ, blocks, x, y, z, var25.nextFloat() * 0.5F + 0.5F, yaw + (float)Math.PI * 0.5F, pitch / 3.0F, tunnel, tunnelCount, 1.0D);
				return;
			}

			if(var54 || var25.nextInt(4) != 0) {
				double var35 = x - var19;
				double var37 = z - var21;
				double var39 = tunnelCount - tunnel;
				double var41 = baseWidth + 2.0F + 16.0F;
				if(var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41) {
					return;
				}

				if(x >= var19 - 16.0D - var29 * 2.0D && z >= var21 - 16.0D - var29 * 2.0D && x <= var19 + 16.0D + var29 * 2.0D && z <= var21 + 16.0D + var29 * 2.0D) {
					int var55 = MathHelper.floor(x - var29) - chunkX * 16 - 1;
					int var36 = MathHelper.floor(x + var29) - chunkX * 16 + 1;
					int var56 = MathHelper.floor(y - var31) - 1;
					int var38 = MathHelper.floor(y + var31) + 1;
					int var57 = MathHelper.floor(z - var29) - chunkZ * 16 - 1;
					int var40 = MathHelper.floor(z + var29) - chunkZ * 16 + 1;
					if(var55 < 0) {
						var55 = 0;
					}

					if(var36 > 16) {
						var36 = 16;
					}

					if(var56 < Math.max(this.world.getBottomY() + 1, 1)) {
						var56 = Math.max(this.world.getBottomY() + 1, 1);
					}

					if(var38 > Math.min(this.world.getTopY() - 8, 120)) {
						var38 = Math.min(this.world.getTopY() - 8, 120);
					}

					if(var57 < 0) {
						var57 = 0;
					}

					if(var40 > 16) {
						var40 = 16;
					}

					boolean var58 = false;

					int var42;
					int var45;
					for(var42 = var55; !var58 && var42 < var36; ++var42) {
						for(int var43 = var57; !var58 && var43 < var40; ++var43) {
							for(int var44 = var38 + 1; !var58 && var44 >= var56 - 1; --var44) {
								var45 = (var42 * 16 + var43) * this.world.getHeight() + var44;
								if(var44 >= this.world.getBottomY() && var44 < this.world.getTopY()) {
									if(blocks[var45] == Block.FLOWING_WATER.id || blocks[var45] == Block.WATER.id) {
										var58 = true;
									}

									if(var44 != var56 - 1 && var42 != var55 && var42 != var36 - 1 && var43 != var57 && var43 != var40 - 1) {
										var44 = var56;
									}
								}
							}
						}
					}

					if(!var58) {
						for(var42 = var55; var42 < var36; ++var42) {
							double var59 = ((double)(var42 + chunkX * 16) + 0.5D - x) / var29;

							for(var45 = var57; var45 < var40; ++var45) {
								double var46 = ((double)(var45 + chunkZ * 16) + 0.5D - z) / var29;
								int var48 = (var42 * 16 + var45) * this.world.getHeight() + var38;
								boolean var49 = false;
								if(var59 * var59 + var46 * var46 < 1.0D) {
									for(int var50 = var38 - 1; var50 >= var56; --var50) {
										double var51 = ((double)var50 + 0.5D - y) / var31;
										if(var51 > -0.7D && var59 * var59 + var51 * var51 + var46 * var46 < 1.0D) {
											byte var53 = blocks[var48];
											if(var53 == Block.GRASS_BLOCK.id) {
												var49 = true;
											}

											if(var53 == Block.STONE.id || var53 == Block.DIRT.id || var53 == Block.GRASS_BLOCK.id) {
												if(var50 < this.world.getBottomY() + 10) {
													blocks[var48] = (byte)Block.FLOWING_LAVA.id;
												} else {
													blocks[var48] = 0;
													if(var49 && blocks[var48 - 1] == Block.DIRT.id) {
														blocks[var48 - 1] = this.world.method_1781().getBiome(var42 + chunkX * 16, var45 + chunkZ * 16).topBlockId;
													}
												}
											}
										}

										--var48;
									}
								}
							}
						}

						if(var54) {
							break;
						}
					}
				}
			}
		}

	}

	protected void place(World world, int startChunkX, int startChunkZ, int chunkX, int chunkZ, byte[] blocks) {
		int var7 = this.random.nextInt(this.random.nextInt(this.random.nextInt(40) + 1) + 1);
		if(this.random.nextInt(15) != 0) {
			var7 = 0;
		}

		for(int var8 = 0; var8 < var7; ++var8) {
			double var9 = startChunkX * 16 + this.random.nextInt(16);
			double var11 = this.random.nextInt(this.random.nextInt(world.getTopY() - 8) + 8);
			double var13 = startChunkZ * 16 + this.random.nextInt(16);
			int var15 = 1;
			if(this.random.nextInt(4) == 0) {
				this.place(this.random.nextLong(), chunkX, chunkZ, blocks, var9, var11, var13);
				var15 += this.random.nextInt(4);
			}

			for(int var16 = 0; var16 < var15; ++var16) {
				float var17 = this.random.nextFloat() * (float)Math.PI * 2.0F;
				float var18 = (this.random.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float var19 = this.random.nextFloat() * 2.0F + this.random.nextFloat();
				if(this.random.nextInt(10) == 0) {
					var19 *= this.random.nextFloat() * this.random.nextFloat() * 3.0F + 1.0F;
				}

				this.placeTunnels(this.random.nextLong(), chunkX, chunkZ, blocks, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D);
			}
		}

	}
}
