package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.Random;

public class FlatChunkGenerator implements ChunkSource {
    private final Random random;
    private final World world;
    public OctavePerlinNoiseSampler forestNoise;
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private Biome[] biomes;
    private OverworldChunkGenerator defaultChunkGenerator;

    private final boolean superflat;
    private final String theme;

    public FlatChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        this.superflat = ((BWOProperties) this.world.getProperties()).bwo_isSuperflat();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();

        ((BWOWorld) this.world).bwo_setSnow(this.theme.equals("Winter"));
        ((BWOWorld) this.world).bwo_setPrecipitation(!this.theme.equals("Hell") && !this.theme.equals("Paradise"));

        if (this.superflat) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        }

        this.forestNoise = new OctavePerlinNoiseSampler(this.random, 8);
        this.defaultChunkGenerator = new OverworldChunkGenerator(world, seed);
    }

    public void buildTerrain(byte[] blocks, Biome[] biomes) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < Config.BWOConfig.world.worldHeightLimit.getIntValue(); ++y) {
                    int blockId;
                    Biome var1 = biomes[x + z * 16];

                    if (y == 0) {
                        blockId = Block.BEDROCK.id;
                    } else if (y <= 60) {
                        blockId = Block.STONE.id;

                        if (y >= 59 && var1 == Biome.DESERT || var1 == Biome.ICE_DESERT) {
                            blockId = Block.SANDSTONE.id;
                        }
                    } else if (y <= 63) {
                        blockId = var1.soilBlockId;
                    } else if (y == 64) {
                        blockId = this.theme.equals("Hell") ? (byte) (var1.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var1.topBlockId) : var1.topBlockId;
                    } else
                        blockId = 0;

                    int index = (z * 16 + x) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;
                    blocks[index] = (byte) blockId;
                }
            }
        }
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        this.random.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] var3 = new byte[16 * Config.BWOConfig.world.worldHeightLimit.getIntValue() * 16];
        this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        this.buildTerrain(var3, this.biomes);

        if (this.superflat) {
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (this.superflat) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();
        return flattenedChunk;
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (!this.superflat) {
            int var4 = x * 16;
            int var5 = z * 16;

            for(int var10 = var4 + 8; var10 < var4 + 8 + 16; ++var10) {
                for(int var12 = var5 + 8; var12 < var5 + 8 + 16; ++var12) {
                    int var11 = this.world.getTopSolidBlockY(var10, var12);
                    if(this.theme.equals("Winter") && var11 > 0 && var11 < this.world.dimension.getHeight() && this.world.getBlockId(var10, var11, var12) == 0 && this.world.getMaterial(var10, var11 - 1, var12).isSolid() && this.world.getMaterial(var10, var11 - 1, var12) != Material.ICE) {
                        this.world.setBlock(var10, var11, var12, Block.SNOW.id);
                    }
                }
            }
        } else {
            this.defaultChunkGenerator.decorate(source, x, z);
        }
    }

    public boolean save(boolean saveEntities, LoadingDisplay display) {
        return true;
    }

    public boolean tick() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    public String getDebugInfo() {
        return "RandomLevelSource";
    }
}
