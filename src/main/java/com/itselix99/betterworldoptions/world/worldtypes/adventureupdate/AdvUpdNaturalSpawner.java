package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate;

import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.util.EntitySpawnGroupWeightedPicker;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.EntitySpawnGroup;

import java.util.List;
import java.util.Random;

public final class AdvUpdNaturalSpawner {

    private static boolean isValidSpawnPos(SpawnGroup spawnGroup, World world, int x, int y, int z) {
        if (spawnGroup.getSpawnMaterial() == Material.WATER) {
            return world.getMaterial(x, y, z).isFluid() && !world.shouldSuffocate(x, y + 1, z);
        } else {
            return world.shouldSuffocate(x, y - 1, z) && !world.shouldSuffocate(x, y, z) && !world.getMaterial(x, y, z).isFluid() && !world.shouldSuffocate(x, y + 1, z);
        }
    }

    private static void postSpawnEntity(LivingEntity entity, World world, float x, float y, float z) {
        if (entity instanceof SpiderEntity && world.random.nextInt(100) == 0) {
            SkeletonEntity var5 = new SkeletonEntity(world);
            var5.setPositionAndAnglesKeepPrevAngles(x, y, z, entity.yaw, 0.0F);
            world.spawnEntity(var5);
            var5.setVehicle(entity);
        } else if (entity instanceof SheepEntity) {
            ((SheepEntity)entity).setColor(SheepEntity.generateDefaultColor(world.random));
        }

    }

    public static void populateChunk(World world, Biome biome, int chunkCenterX, int chunkCenterZ, int rangeX, int rangeZ, Random random) {
        List<EntitySpawnGroup> var7 = biome.getSpawnableEntities(SpawnGroup.CREATURE);
        if (!var7.isEmpty()) {
            while(random.nextFloat() < 0.1F) {
                EntitySpawnGroup var8 = EntitySpawnGroupWeightedPicker.pick(world.random, var7);
                int var9 = 4 + random.nextInt(1 + 4 - 4);
                int var10 = chunkCenterX + random.nextInt(rangeX);
                int var11 = chunkCenterZ + random.nextInt(rangeZ);
                int var12 = var10;
                int var13 = var11;

                for(int var14 = 0; var14 < var9; ++var14) {
                    boolean var15 = false;

                    for(int var16 = 0; !var15 && var16 < 4; ++var16) {
                        int var17 = ((BWOWorld) world).bwo_getSurfaceHeight(var10, var11);
                        if (isValidSpawnPos(SpawnGroup.CREATURE, world, var10, var17, var11)) {
                            float var18 = (float)var10 + 0.5F;
                            float var19 = (float)var17;
                            float var20 = (float)var11 + 0.5F;

                            MobEntity var21;
                            try {
                                var21 = (MobEntity)var8.clazz.getConstructor(World.class).newInstance(world);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }

                            var21.setPositionAndAngles(var18, var19, var20, random.nextFloat() * 360.0F, 0.0F);
                            world.spawnEntity(var21);
                            postSpawnEntity(var21, world, var18, var19, var20);
                            var15 = true;
                        }

                        var10 += random.nextInt(5) - random.nextInt(5);

                        for(var11 += random.nextInt(5) - random.nextInt(5); var10 < chunkCenterX || var10 >= chunkCenterX + rangeX || var11 < chunkCenterZ || var11 >= chunkCenterZ + rangeX; var11 = var13 + random.nextInt(5) - random.nextInt(5)) {
                            var10 = var12 + random.nextInt(5) - random.nextInt(5);
                        }
                    }
                }
            }

        }
    }
}