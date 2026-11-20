package com.itselix99.betterworldoptions.world.feature;

import com.itselix99.betterworldoptions.api.structure.LootEntry;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class BonusChestFeature extends Feature {
	private final LootEntry[] lootEntries;
	private final int amountOfLoot;

	public BonusChestFeature(LootEntry[] lootEntries, int amountOfLoot) {
		this.lootEntries = lootEntries;
		this.amountOfLoot = amountOfLoot;
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
        while(true) {
			int var11 = world.getBlockId(x, y, z);
			if(var11 != 0 && var11 != Block.LEAVES.id || y <= 1) {
				if(y < 1) {
					return false;
				} else {
					++y;

					for(int var6 = 0; var6 < 4; ++var6) {
						int var7 = x + random.nextInt(4) - random.nextInt(4);
						int var8 = y + random.nextInt(3) - random.nextInt(3);
						int var9 = z + random.nextInt(4) - random.nextInt(4);
						if(world.isAir(var7, var8, var9) && this.hasSolidTop(world, var7, var8 - 1, var9)) {
							if(world.isAir(var7 - 1, var8, var9) && this.hasSolidTop(world, var7 - 1, var8 - 1, var9)) {
								world.setBlock(var7 - 1, var8, var9, Block.TORCH.id, 0);
							}

							if(world.isAir(var7 + 1, var8, var9) && this.hasSolidTop(world, var7 - 1, var8 - 1, var9)) {
								world.setBlock(var7 + 1, var8, var9, Block.TORCH.id, 0);
							}

							if(world.isAir(var7, var8, var9 - 1) && this.hasSolidTop(world, var7 - 1, var8 - 1, var9)) {
								world.setBlock(var7, var8, var9 - 1, Block.TORCH.id, 0);
							}

							if(world.isAir(var7, var8, var9 + 1) && this.hasSolidTop(world, var7 - 1, var8 - 1, var9)) {
								world.setBlock(var7, var8, var9 + 1, Block.TORCH.id, 0);
							}

							world.setBlock(var7, var8, var9, Block.CHEST.id, 0);
							ChestBlockEntity var10 = (ChestBlockEntity)world.getBlockEntity(var7, var8, var9);
							if(var10 != null) {
								LootEntry.addLoot(random, this.lootEntries, var10, this.amountOfLoot);
							}

							return true;
						}
					}

					return false;
				}
			}

			--y;
		}
	}

	private boolean hasSolidTop(World world, int x, int y, int z) {
		Block block = Block.BLOCKS[world.getBlockId(x, y, z)];
		return this.hasSolidTop(block, world.getBlockMeta(x, y, z));
	}

	private boolean hasSolidTop(Block block, int metadata) {
		if (block == null) {
			return false;
		} else if (block.material.suffocates() && block.isFullCube()) {
			return true;
		} else if (block instanceof StairsBlock) {
			return (metadata & 4) == 4;
		} else if (block instanceof SlabBlock) {
			return (metadata & 8) == 8;
		} else if (block instanceof SnowyBlock) {
			return (metadata & 7) == 7;
		} else {
			return false;
		}
	}
}