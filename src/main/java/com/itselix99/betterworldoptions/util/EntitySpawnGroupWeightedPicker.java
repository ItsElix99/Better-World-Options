package com.itselix99.betterworldoptions.util;

import net.minecraft.world.biome.EntitySpawnGroup;

import java.util.List;
import java.util.Random;

public class EntitySpawnGroupWeightedPicker {
    public static int getTotalWeight(List<EntitySpawnGroup> list) {
        int var2 = 0;

        for(EntitySpawnGroup entitySpawnGroup : list) {
            var2 += entitySpawnGroup.amount;
        }

        return var2;
    }

    public static EntitySpawnGroup pick(Random random, List<EntitySpawnGroup> list, int totalWeight) {
        if (totalWeight <= 0) {
            throw new IllegalArgumentException();
        } else {
            int var4 = random.nextInt(totalWeight);

            for(EntitySpawnGroup entitySpawnGroup : list) {
                var4 -= entitySpawnGroup.amount;
                if (var4 < 0) {
                    return entitySpawnGroup;
                }
            }

            return null;
        }
    }

    public static EntitySpawnGroup pick(Random random, List<EntitySpawnGroup> list) {
        return pick(random, list, getTotalWeight(list));
    }
}