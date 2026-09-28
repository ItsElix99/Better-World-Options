package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.interfaces.BWOBiomeSource;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(BiomeSource.class)
public abstract class BiomeSourceMixin implements BWOBiomeSource {
    @Shadow public abstract Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth);

    @Shadow public abstract Biome[] getBiomesInArea(int x, int z, int width, int depth);

    @Shadow public abstract Biome getBiome(int x, int z);

    public boolean bwo_isBiomeWithin(World world, int x, int z, int range, List<Biome> biomes) {
        ChunkSource chunkGenerator = ((BWOWorld) world).bwo_getChunkGenerator();

        if (chunkGenerator instanceof BWOChunkGenerator bwoChunkGenerator && bwoChunkGenerator.useSurfaceHeightForStructures()) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            long chunkPos = ChunkPos.toLong(chunkX, chunkZ);
            int[] heights = bwoChunkGenerator.getSurfaceHeightCache().getOrDefault(chunkPos, null);
            if (heights != null) {
                int localX = x & 15;
                int localZ = z & 15;

                int surfaceY = heights[localX + localZ * 16];
                return biomes.contains(this.getBiome(x, z)) && surfaceY > bwoChunkGenerator.getMinSurfaceHeight();
            }
        } else {
            return biomes.contains(this.getBiome(x, z));
        }

        return false;
    }

    public BlockPos bwo_findBiome(int x, int z, int range, List<Biome> biomes, Random random) {
        int var1 = x - range >> 2;
        int var2 = z - range >> 2;
        int var3 = x + range >> 2;
        int var4 = z + range >> 2;

        int var5 = var3 - var1 + 1;
        int var6 = var4 - var2 + 1;

        Biome[] var7 = this.getBiomesInArea(var1, var2, var5, var6);
        BlockPos var8 = null;
        int var9 = 0;

        for(int var10 = 0; var10 < var7.length; ++var10) {
            int var11 = var1 + var10 % var5 << 2;
            int var12 = var2 + var10 / var5 << 2;
            Biome var13 = var7[var10];

            if (biomes.contains(var13) && (var8 == null || random.nextInt(var9 + 1) == 0)) {
                var8 = new BlockPos(var11, 0, var12);
                ++var9;
            }
        }

        return var8;
    }
}