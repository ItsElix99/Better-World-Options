package com.itselix99.betterworldoptions.world.structures.village;

import java.util.ArrayList;
import java.util.Random;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.api.structure.StructureFeature;
import com.itselix99.betterworldoptions.api.structure.StructurePiece;
import com.itselix99.betterworldoptions.api.structure.StructureStart;
import com.itselix99.betterworldoptions.interfaces.BWOBiomeSource;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.world.World;

public class VillageStructure extends StructureFeature {
    private static Structure village;

    protected boolean isFeatureChunk(int chunkX, int chunkZ) {
        if (village == null) {
            village = Structure.getStructureById(BetterWorldOptions.NAMESPACE.id("village"));
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
        Random var7 = ((BWOWorld) this.world).bwo_setRandomSeed(var5, var6, 10387312);
        var5 *= var1;
        var6 *= var1;
        var5 += var7.nextInt(var1 - var2);
        var6 += var7.nextInt(var1 - var2);
        if (var3 == var5 && var4 == var6) {
            return ((BWOBiomeSource) this.world.method_1781()).bwo_isBiomeWithin(this.world, var3 * 16 + 8, var4 * 16 + 8, 0, village.getValidBiomes());
        }

        return false;
    }

    protected StructureStart createStructure(int x, int z) {
        return new Start(this.world, this.random, x, z);
    }

    static class Start extends StructureStart {
        private final boolean valid;

        public Start(World world, Random random, int chunkX, int chunkZ) {
            int var1 = 0;
            ArrayList<VillagePieces.VillagePieceWeight> var2 = VillagePieces.getPieceWeights(random, var1);
            VillagePieces.Start var3 = new VillagePieces.Start(world.dimension.biomeSource, 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, var2, var1);
            this.pieces.add(var3);
            var3.addChildren(var3, this.pieces, random);
            ArrayList<StructurePiece> var4 = var3.roadPieces;
            ArrayList<StructurePiece> var5 = var3.buildingPieces;

            while(!var4.isEmpty() || !var5.isEmpty()) {
                if (!var4.isEmpty()) {
                    int var6 = random.nextInt(var4.size());
                    StructurePiece var7 = var4.remove(var6);
                    var7.addChildren(var3, this.pieces, random);
                } else {
                    int var8 = random.nextInt(var5.size());
                    StructurePiece var9 = var5.remove(var8);
                    var9.addChildren(var3, this.pieces, random);
                }
            }

            this.findBounds();
            int var10 = 0;

            for(StructurePiece var11 : this.pieces) {
                if (!(var11 instanceof VillagePieces.RoadPiece)) {
                    ++var10;
                }
            }

            this.valid = var10 > 2;
        }

        public boolean isValid() {
            return this.valid;
        }
    }
}