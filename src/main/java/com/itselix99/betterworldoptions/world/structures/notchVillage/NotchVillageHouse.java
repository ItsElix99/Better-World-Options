package com.itselix99.betterworldoptions.world.structures.notchVillage;

import com.itselix99.betterworldoptions.api.structure.StructureBox;
import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import com.itselix99.betterworldoptions.world.structures.village.VillagePieces;
import net.minecraft.block.Block;
import net.minecraft.entity.mob.PigZombieEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class NotchVillageHouse extends VillagePieces.VillagePiece {
   private int hpos = -1;
   private final int width;
   private final int height;
   private final int depth;
   private final int doorX;
   private final int doorZ;
   private final int doorMeta;
   private boolean spawnPigZombie = true;

   public NotchVillageHouse(int generationDepth, Random random, StructureBox bounds, int facing, int width, int height, int depth, int doorX, int doorZ, int doorMeta) {
      super(generationDepth);
      this.facing = facing;
      this.bounds = bounds;
      this.width = width;
      this.height = height;
      this.depth = depth;
      this.doorX = doorX;
      this.doorZ = doorZ;
      this.doorMeta = doorMeta;
   }

   public static void createNotchVillageHouse(List<StructurePiece> pieces, Random random, int chunkX, int chunkZ) {
      NotchVillageHouse var1 = NotchVillageHouse.of(pieces, random, (chunkX << 4) + random.nextInt(80) - random.nextInt(80), 64, (chunkZ << 4) + random.nextInt(80) - random.nextInt(80), random.nextInt(4), 0);
      if (var1 != null) {
         pieces.add(var1);
      }
   }

   public static NotchVillageHouse of(List<StructurePiece> pieces, Random random, int x, int y, int z, int facing, int generationDepth) {
      int width = random.nextInt(7) + 7;
      int height = 4 + random.nextInt(3) / 2;
      int depth = random.nextInt(7) + 7;

      if (facing < 2) {
         depth += 2;
      } else {
         width += 2;
      }

      int structureX = x - width / 2;
      int structureZ = z - depth / 2;

      if (facing == 0) {
         ++structureX;
         --width;
      } else if (facing == 1) {
         --width;
      } else if (facing == 2) {
         ++structureZ;
         --depth;
      } else {
         --depth;
      }

      int roofRise;

      if (facing < 2) {
         roofRise = (width - 1) / 2;
      } else {
         roofRise = (depth - 1) / 2;
      }

      int totalHeight = height + roofRise;

      int doorX = random.nextInt(width - 4) + 2;
      int doorZ = random.nextInt(depth - 4) + 2;
      int doorMeta;

      if (facing == 0) {
         doorX = 0;
         doorMeta = 0;
      } else if (facing == 1) {
         doorX = width - 1;
         doorMeta = 2;
      } else if (facing == 2) {
         doorZ = 0;
         doorMeta = 1;
      } else {
         doorZ = depth - 1;
         doorMeta = 3;
      }

      StructureBox bounds = StructureBox.orient(structureX, y, structureZ, 0, 0, 0, width, totalHeight, depth, facing);
      return isValidBounds(bounds) && StructurePiece.getIntersectingPiece(pieces, bounds) == null ? new NotchVillageHouse(generationDepth, random, bounds, facing, width, height, depth, doorX, doorZ, doorMeta) : null;
   }

   protected void spawnPigZombie(World world) {
      if (this.spawnPigZombie) {
         double entityX = this.transformX(this.width / 2, this.depth / 2) + 0.5D;
         double entityY = this.transformY(1) + 0.5D;
         double entityZ = this.transformZ(this.width / 2, this.depth / 2) + 0.5D;

         PigZombieEntity pigZombie = new PigZombieEntity(world);
         pigZombie.setPositionAndAngles(entityX, entityY, entityZ, 0.0F, 0.0F);

         world.spawnEntity(pigZombie);
         this.spawnPigZombie = false;
      }
   }

   public boolean postProcess(World world, Random random, StructureBox bounds) {
      if (this.hpos < 0) {
         this.hpos = this.updateHeight(world, bounds);
         if (this.hpos < 0) {
            return true;
         }

         this.bounds.move(0, this.hpos - this.bounds.maxY + (this.bounds.maxY - this.bounds.minY), 0);
      }

      int minX = 0;
      int maxX = this.width - 1;
      int minZ = 0;
      int maxZ = this.depth - 1;

      if (this.facing >= 2) {
         ++minX;
         --maxX;
      } else {
         ++minZ;
         --maxZ;
      }

      for (int x = 0; x < this.width; ++x) {
         for (int z = 0; z < this.depth; ++z) {
            int distanceA;
            int distanceB;

            if (this.facing < 2) {
               distanceA = x;
               distanceB = this.width - 1 - x;
            } else {
               distanceA = z;
               distanceB = this.depth - 1 - z;
            }

            int roofRise = Math.min(distanceA, distanceB);
            int columnHeight = this.height + roofRise;

            for (int y = -1; y < columnHeight; ++y) {
               int block = -1;

               if (y == columnHeight - 1) {
                  block = Block.PLANKS.id;
               } else if (x >= minX && x <= maxX && z >= minZ && z <= maxZ) {
                  block = 0;

                  if (y == -1 || y == columnHeight - 1 || x == minX || z == minZ || x == maxX || z == maxZ) {
                     int worldY = this.transformY(y);

                     if (worldY <= this.transformY(0) + random.nextInt(3)) {
                        block = Block.MOSSY_COBBLESTONE.id;
                     } else {
                        block = Block.COBBLESTONE.id;
                     }
                  }
               }

               if (block >= 0) {
                  this.setBlockWithMetadata(world, block, 0, x, y, z, bounds);
               }
            }
         }
      }

      this.setBlockWithMetadata(world, 0, 0, this.doorX, 0, this.doorZ, bounds);
      this.setBlockWithMetadata(world, 0, 0, this.doorX, 1, this.doorZ, bounds);

      this.placeWoodenDoor(world, bounds, random, this.doorX, 0, this.doorZ, this.postProcessBlockMetadata(Block.DOOR.id, this.doorMeta));

      for (int i = 0; i < (this.width * 2 + this.depth * 2) * 3; ++i) {
         int windowX = random.nextInt(this.width - 4) + 2;
         int windowZ = random.nextInt(this.depth - 4) + 2;
         int side = random.nextInt(4);

         if (side == 0) {
            windowX = minX;
         }

         if (side == 1) {
            windowX = maxX;
         }

         if (side == 2) {
            windowZ = minZ;
         }

         if (side == 3) {
            windowZ = maxZ;
         }

         int worldX = this.transformX(windowX, windowZ);
         int worldY = this.transformY(1);
         int worldZ = this.transformZ(windowX, windowZ);

         if (world.shouldSuffocate(worldX, worldY, worldZ)) {
            int count = 0;

            if (world.shouldSuffocate(worldX - 1, worldY, worldZ) && world.shouldSuffocate(worldX + 1, worldY, worldZ)) {
               ++count;
            }

            if (world.shouldSuffocate(worldX, worldY, worldZ - 1) && world.shouldSuffocate(worldX, worldY, worldZ + 1)) {
               ++count;
            }

            if (count == 1) {
               this.setBlockWithMetadata(world, Block.GLASS.id, 0, windowX, 1, windowZ, bounds);
            }
         }
      }

      int wallWidth = maxX - minX;
      int wallDepth = maxZ - minZ;

      for (int i = 0; i < wallWidth * 2 + wallDepth * 2; ++i) {
         int torchX = minX + random.nextInt(wallWidth - 1) + 1;
         int torchZ = minZ + random.nextInt(wallDepth - 1) + 1;

         int worldX = this.transformX(torchX, torchZ);
         int worldY = this.transformY(2);
         int worldZ = this.transformZ(torchX, torchZ);

         if (world.getBlockId(worldX, worldY, worldZ) == 0) {
            int count = 0;


            if (world.shouldSuffocate(worldX - 1, worldY, worldZ)) {
               ++count;
            }

            if (world.shouldSuffocate(worldX + 1, worldY, worldZ)) {
               ++count;
            }

            if (world.shouldSuffocate(worldX, worldY, worldZ - 1)) {
               ++count;
            }

            if (world.shouldSuffocate(worldX, worldY, worldZ + 1)) {
               ++count;
            }

            if (count == 1) {
               this.setBlockWithMetadata(world, Block.TORCH.id, 0, torchX, 2, torchZ, bounds);
            }
         }
      }

      this.spawnPigZombie(world);

      for (int x = 0; x < this.width; ++x) {
         for (int z = 0; z < this.depth; ++z) {
            if (x >= minX && z >= minZ && x <= maxX && z <= maxZ) {
               this.fillColumnDown(world, Block.MOSSY_COBBLESTONE.id, 0, x, -2, z, bounds);
            }
         }
      }

      return true;
   }
}