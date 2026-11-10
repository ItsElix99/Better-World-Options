package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.interfaces.BWOBiomeSource;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(FixedBiomeSource.class)
public class FixedBiomeSourceMixin implements BWOBiomeSource {
    @Shadow private Biome biome;

    public BlockPos bwo_findBiome(int x, int z, int range, List<Biome> biomes, Random random) {
        return biomes.contains(this.biome) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }

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
                return biomes.contains(this.biome) && surfaceY > bwoChunkGenerator.getMinSurfaceHeight();
            }
        } else {
            return biomes.contains(this.biome);
        }

        return false;
    }
}