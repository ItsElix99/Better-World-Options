package com.itselix99.betterworldoptions.world.feature.notchHouse;

import com.itselix99.betterworldoptions.api.structure.Structure;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class NotchHouseStructure extends Structure {

    public NotchHouseStructure(Identifier id, String name, boolean decoration) {
        super(id, name, decoration);
    }

    public void decorate(World world, Random random, Biome biome, int x, int z) {
        if (random.nextInt(20) == 0) {
            for (int var1 = 0; var1 < 4; ++var1) {
                int var2 = x + random.nextInt(16) + 8;
                int var3 = z + random.nextInt(16) + 8;
                NotchHouseFeature var4 = new NotchHouseFeature();
                var4.generate(world, random, var2, world.getTopY(var2, var3), var3);
            }
        }

    }
}