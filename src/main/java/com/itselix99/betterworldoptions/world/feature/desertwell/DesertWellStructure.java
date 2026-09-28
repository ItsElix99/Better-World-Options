package com.itselix99.betterworldoptions.world.feature.desertwell;

import com.itselix99.betterworldoptions.api.structure.Structure;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class DesertWellStructure extends Structure {

    public DesertWellStructure(Identifier id, String name, boolean decoration) {
        super(id, name, decoration);
    }

    public void decorate(World world, Random random, Biome biome, int x, int z) {
        if (this.getValidBiomes().contains(biome)) {
            if (random.nextInt(1000) == 0) {
                int var1 = x + random.nextInt(16) + 8;
                int var2 = z + random.nextInt(16) + 8;
                DesertWellFeature var3 = new DesertWellFeature();
                var3.generate(world, random, var1, world.getTopY(var1, var2) + 1, var2);
            }
        }

    }
}