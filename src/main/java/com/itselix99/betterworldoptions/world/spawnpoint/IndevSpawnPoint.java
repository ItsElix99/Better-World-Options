package com.itselix99.betterworldoptions.world.spawnpoint;

import com.itselix99.betterworldoptions.api.chunk.FiniteChunkGenerator;
import com.itselix99.betterworldoptions.api.structure.LootEntry;
import com.itselix99.betterworldoptions.api.worldtype.SpawnPoint;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.block.Block;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;

import java.util.Random;

public class IndevSpawnPoint extends SpawnPoint {
    public IndevSpawnPoint(BWOProperties bwoProperties) {
        super(bwoProperties);
    }

    public void init(World world, Dimension dimension, Random random, WorldProperties properties) {
        if (((BWOWorld) world).bwo_getChunkGenerator() instanceof FiniteChunkGenerator finiteChunkGenerator && this.finiteWorld) {
            finiteChunkGenerator.setCurrentStage("Spawning");
        }

        boolean isValidSpawnArea = false;
        int attempts = 0;
        int x = 0;
        int y = 64;
        int z = 0;

        while (!isValidSpawnArea) {
            if (!this.finiteWorld) {
                x += random.nextInt(64) - random.nextInt(64);
                z += random.nextInt(64) - random.nextInt(64);
            } else {
                ++attempts;
                x = random.nextInt(this.width / 2) + this.width / 4;
                z = random.nextInt(this.length / 2) + this.length / 4;
            }

            y = world.getTopSolidBlockY(x, z) + 1;

            if(attempts >= 1000000) {
                break;
            }

            if (!this.starterHouse && dimension.isValidSpawnPoint(x, z) || this.isValidSpawnArea(world, x, y, z)) {
                isValidSpawnArea = true;
            }
        }

        properties.setSpawn(x, y - 1, z);
    }

    protected void placeStarterHouse(World world, Random random, int x, int y, int z) {
        if (this.oldFeatures) {
            for (int var6 = x - 3; var6 <= x + 3; ++var6) {
                for (int var7 = y - 2; var7 <= y + 2; ++var7) {
                    for (int var8 = z - 3; var8 <= z + 3; ++var8) {
                        int var9 = var7 < y - 1 ? Block.OBSIDIAN.id : 0;
                        if (var6 == x - 3 || var8 == z - 3 || var6 == x + 3 || var8 == z + 3 || var7 == y - 2 || var7 == y + 2) {
                            var9 = Block.STONE.id;
                            if (var7 >= y - 1) {
                                var9 = Block.PLANKS.id;
                            }
                        }

                        if (var8 == z - 3 && var6 == x && var7 >= y - 1 && var7 <= y) {
                            var9 = 0;
                        }

                        world.setBlock(var6, var7, var8, var9);
                    }
                }
            }

            world.setBlock(x - 3 + 1, y, z, Block.TORCH.id);
            world.setBlock(x + 3 - 1, y, z, Block.TORCH.id);

            if (this.bonusChest) {
                world.setBlock(x + 2, y - 1, z - 1, Block.CHEST.id);
                ChestBlockEntity var10 = (ChestBlockEntity) world.getBlockEntity(x + 2, y - 1, z - 1);

                if (var10 != null) {
                    LootEntry.addLoot(random, this.lootEntries, var10, 10);
                }

            }
        } else {
            super.placeStarterHouse(world, random, x, y, z);
        }
    }

    protected boolean isValidSpawnArea(World world, int centerX, int centerY, int centerZ) {
        for (int x = centerX - 3; x <= centerX + 3; ++x) {
            for (int y = centerY - 1; y <= centerY + 2; ++y) {
                for (int z = centerZ - 5; z <= centerZ + 3; ++z) {
                    if (world.getMaterial(x, y, z).isSolid()) {
                        return false;
                    }
                }
            }
        }

        int floorY = centerY - 2;
        for (int x = centerX - 3; x <= centerX + 3; ++x) {
            for (int z = centerZ - 5; z <= centerZ + 3; ++z) {
                if (!Block.BLOCKS_OPAQUE[world.getBlockId(x, floorY, z)]) {
                    return false;
                }
            }
        }

        return true;
    }
}