package com.itselix99.betterworldoptions.world.structures.desertPyramid;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.api.structure.StructureFeature;
import com.itselix99.betterworldoptions.api.structure.StructureStart;
import com.itselix99.betterworldoptions.interfaces.BWOBiomeSource;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.structures.TemplePieces;
import net.minecraft.world.World;

import java.util.Random;

public class DesertPyramidStructure extends StructureFeature {
    private static Structure desertPyramid;

    protected boolean isFeatureChunk(int chunkX, int chunkZ) {
        if (desertPyramid == null) {
            desertPyramid = Structure.getStructureById(BetterWorldOptions.NAMESPACE.id("desertPyramid"));
        }

        int var1 = 32;
        int var2 = 8;
        int var3 = chunkX;
        int var4 = chunkZ;
        if (chunkX < 0) {
            chunkX -= var1 - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= var1 - 1;
        }

        int var5 = chunkX / var1;
        int var6 = chunkZ / var1;
        Random var7 = ((BWOWorld) this.world).bwo_setRandomSeed(var5, var6, 14357617);
        var5 *= var1;
        var6 *= var1;
        var5 += var7.nextInt(var1 - var2);
        var6 += var7.nextInt(var1 - var2);
        if (var3 == var5 && var4 == var6) {
            return ((BWOBiomeSource) this.world.method_1781()).bwo_isBiomeWithin(this.world, var3 * 16 + 8, var4 * 16 + 8, 0, desertPyramid.getValidBiomes());
        }

        return false;
    }

    protected StructureStart createStructure(int x, int z) {
        return new Start(this.world, this.random, x, z);
    }

    public static class Start extends StructureStart {
        public Start(World world, Random random, int chunkX, int chunkZ) {
            TemplePieces.DesertPyramid var1 = new TemplePieces.DesertPyramid(random, chunkX * 16, chunkZ * 16);
            this.pieces.add(var1);
            this.findBounds();
        }
    }
}