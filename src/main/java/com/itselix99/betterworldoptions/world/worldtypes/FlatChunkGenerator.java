package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.Random;

public class FlatChunkGenerator implements ChunkSource {
    private final Random random;
    private final World world;
    public OctavePerlinNoiseSampler forestNoise;
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private double[] temperatures;
    private Biome[] biomes;

    private final boolean betaFeatures;
    private final String theme;

    public FlatChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        this.betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();

        ((BWOWorld) this.world).bwo_setSnow(this.theme.equals("Winter"));

        if (this.betaFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        }

        this.forestNoise = new OctavePerlinNoiseSampler(this.random, 8);
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

                        if (y >= 58 && var1 == Biome.DESERT || var1 == Biome.ICE_DESERT) {
                            blockId = Block.SANDSTONE.id;
                        }
                    } else if (y <= 63) {
                        blockId = var1.soilBlockId;
                    } else if (y == 64) {
                        blockId = this.theme.equals("Hell") ? (byte) (var1.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var1.topBlockId) : var1.topBlockId;
                    } else
                        blockId = 0;

                    int index = (x * 16 + z) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + y;
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

        if (this.betaFeatures) {
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (this.betaFeatures) {
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
        if (!this.betaFeatures) {
            for (int var1 = 0; var1 < 16; var1++) {
                int worldX = x * 16 + var1;

                for (int var2 = 0; var2 < 16; var2++) {
                    int worldZ = z * 16 + var2;
                    if (worldX < 0 || worldX >= 16 || worldZ < 0 || worldZ >= 16) {
                        return;
                    }
                }
            }
        } else {
            SandBlock.fallInstantly = true;
            int var4 = x * 16;
            int var5 = z * 16;
            Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
            this.random.setSeed(this.world.getSeed());
            long var7 = this.random.nextLong() / 2L * 2L + 1L;
            long var9 = this.random.nextLong() / 2L * 2L + 1L;
            this.random.setSeed((long)x * var7 + (long)z * var9 ^ this.world.getSeed());
            double var11;
            if (this.random.nextInt(4) == 0) {
                int var13 = var4 + this.random.nextInt(16) + 8;
                int var14 = this.random.nextInt(128);
                int var15 = var5 + this.random.nextInt(16) + 8;
                (new LakeFeature(this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id)).generate(this.world, this.random, var13, var14, var15);
            }

            if (this.random.nextInt(8) == 0) {
                int var26 = var4 + this.random.nextInt(16) + 8;
                int var38 = this.random.nextInt(this.random.nextInt(120) + 8);
                int var50 = var5 + this.random.nextInt(16) + 8;
                if (var38 < 64 || this.random.nextInt(10) == 0) {
                    (new LakeFeature(Block.LAVA.id)).generate(this.world, this.random, var26, var38, var50);
                }
            }

            for(int var27 = 0; var27 < 8; ++var27) {
                int var39 = var4 + this.random.nextInt(16) + 8;
                int var51 = this.random.nextInt(128);
                int var16 = var5 + this.random.nextInt(16) + 8;
                (new DungeonFeature()).generate(this.world, this.random, var39, var51, var16);
            }

            for(int var28 = 0; var28 < 10; ++var28) {
                int var40 = var4 + this.random.nextInt(16);
                int var52 = this.random.nextInt(128);
                int var63 = var5 + this.random.nextInt(16);
                (new ClayOreFeature(32)).generate(this.world, this.random, var40, var52, var63);
            }

            for(int var29 = 0; var29 < 20; ++var29) {
                int var41 = var4 + this.random.nextInt(16);
                int var53 = this.random.nextInt(128);
                int var64 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.DIRT.id, 32)).generate(this.world, this.random, var41, var53, var64);
            }

            for(int var30 = 0; var30 < 10; ++var30) {
                int var42 = var4 + this.random.nextInt(16);
                int var54 = this.random.nextInt(128);
                int var65 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.GRAVEL.id, 32)).generate(this.world, this.random, var42, var54, var65);
            }

            for(int var31 = 0; var31 < 20; ++var31) {
                int var43 = var4 + this.random.nextInt(16);
                int var55 = this.random.nextInt(128);
                int var66 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.COAL_ORE.id, 16)).generate(this.world, this.random, var43, var55, var66);
            }

            for(int var32 = 0; var32 < 20; ++var32) {
                int var44 = var4 + this.random.nextInt(16);
                int var56 = this.random.nextInt(64);
                int var67 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.IRON_ORE.id, 8)).generate(this.world, this.random, var44, var56, var67);
            }

            for(int var33 = 0; var33 < 2; ++var33) {
                int var45 = var4 + this.random.nextInt(16);
                int var57 = this.random.nextInt(32);
                int var68 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.GOLD_ORE.id, 8)).generate(this.world, this.random, var45, var57, var68);
            }

            for(int var34 = 0; var34 < 8; ++var34) {
                int var46 = var4 + this.random.nextInt(16);
                int var58 = this.random.nextInt(16);
                int var69 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.REDSTONE_ORE.id, 7)).generate(this.world, this.random, var46, var58, var69);
            }

            for(int var35 = 0; var35 < 1; ++var35) {
                int var47 = var4 + this.random.nextInt(16);
                int var59 = this.random.nextInt(16);
                int var70 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.DIAMOND_ORE.id, 7)).generate(this.world, this.random, var47, var59, var70);
            }

            for(int var36 = 0; var36 < 1; ++var36) {
                int var48 = var4 + this.random.nextInt(16);
                int var60 = this.random.nextInt(16) + this.random.nextInt(16);
                int var71 = var5 + this.random.nextInt(16);
                (new OreFeature(Block.LAPIS_ORE.id, 6)).generate(this.world, this.random, var48, var60, var71);
            }

            var11 = 0.5F;
            int var37 = (int)((this.forestNoise.sample((double)var4 * var11, (double)var5 * var11) / (double)8.0F + this.random.nextDouble() * (double)4.0F + (double)4.0F) / (double)3.0F);
            int var49 = 0;
            if (this.random.nextInt(10) == 0) {
                ++var49;
            }

            if (var6 == Biome.FOREST) {
                var49 += var37 + 5;
            }

            if (var6 == Biome.RAINFOREST) {
                var49 += var37 + 5;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var49 += var37 + 2;
            }

            if (var6 == Biome.TAIGA) {
                var49 += var37 + 5;
            }

            if (var6 == Biome.DESERT) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                } else {
                    var49 -= 20;
                }
            }

            if (var6 == Biome.TUNDRA) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                } else {
                    var49 -= 20;
                }
            }

            if (var6 == Biome.PLAINS) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                } else {
                    var49 -= 20;
                }
            }

            if (var6 == Biome.SWAMPLAND || var6 == Biome.SHRUBLAND || var6 == Biome.SAVANNA) {
                if (this.theme.equals("Woods")) {
                    var49 += var37 + 5;
                }
            }

            for(int var61 = 0; var61 < var49; ++var61) {
                int var72 = var4 + this.random.nextInt(16) + 8;
                int var17 = var5 + this.random.nextInt(16) + 8;
                Feature var18 = var6.getRandomTreeFeature(this.random);
                var18.prepare(1.0F, 1.0F, 1.0F);
                var18.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
            }

            byte var62 = (byte) (this.theme.equals("Paradise") ? 8 : 0);
            if (var6 == Biome.FOREST) {
                var62 = 2;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var62 = 4;
            }

            if (var6 == Biome.TAIGA) {
                var62 = 2;
            }

            if (var6 == Biome.PLAINS) {
                var62 = 3;
            }

            for(int var73 = 0; var73 < var62; ++var73) {
                int var76 = var4 + this.random.nextInt(16) + 8;
                int var85 = this.random.nextInt(128);
                int var19 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var76, var85, var19);
            }

            byte var74 = 0;
            if (var6 == Biome.FOREST) {
                var74 = 2;
            }

            if (var6 == Biome.RAINFOREST) {
                var74 = 10;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var74 = 2;
            }

            if (var6 == Biome.TAIGA) {
                var74 = 1;
            }

            if (var6 == Biome.PLAINS) {
                var74 = 10;
            }

            for(int var77 = 0; var77 < var74; ++var77) {
                byte var86 = 1;
                if (var6 == Biome.RAINFOREST && this.random.nextInt(3) != 0) {
                    var86 = 2;
                }

                int var97 = var4 + this.random.nextInt(16) + 8;
                int var20 = this.random.nextInt(128);
                int var21 = var5 + this.random.nextInt(16) + 8;
                (new GrassPatchFeature(Block.GRASS.id, var86)).generate(this.world, this.random, var97, var20, var21);
            }

            var74 = 0;
            if (var6 == Biome.DESERT) {
                var74 = 2;
            }

            for(int var78 = 0; var78 < var74; ++var78) {
                int var87 = var4 + this.random.nextInt(16) + 8;
                int var98 = this.random.nextInt(128);
                int var108 = var5 + this.random.nextInt(16) + 8;
                (new DeadBushPatchFeature(Block.DEAD_BUSH.id)).generate(this.world, this.random, var87, var98, var108);
            }

            if (this.theme.equals("Paradise")) {
                for (int var120 = 0; var120 < var62; var120++) {
                    int var79 = var4 + this.random.nextInt(16) + 8;
                    int var88 = this.random.nextInt(128);
                    int var99 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
                }
            } else {
                if (this.random.nextInt(2) == 0) {
                    int var79 = var4 + this.random.nextInt(16) + 8;
                    int var88 = this.random.nextInt(128);
                    int var99 = var5 + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
                }
            }

            if (this.random.nextInt(4) == 0) {
                int var80 = var4 + this.random.nextInt(16) + 8;
                int var89 = this.random.nextInt(128);
                int var100 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.BROWN_MUSHROOM.id)).generate(this.world, this.random, var80, var89, var100);
            }

            if (this.random.nextInt(8) == 0) {
                int var81 = var4 + this.random.nextInt(16) + 8;
                int var90 = this.random.nextInt(128);
                int var101 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.RED_MUSHROOM.id)).generate(this.world, this.random, var81, var90, var101);
            }

            for(int var82 = 0; var82 < 10; ++var82) {
                int var91 = var4 + this.random.nextInt(16) + 8;
                int var102 = this.random.nextInt(128);
                int var109 = var5 + this.random.nextInt(16) + 8;
                (new SugarCanePatchFeature()).generate(this.world, this.random, var91, var102, var109);
            }

            if (this.random.nextInt(32) == 0) {
                int var83 = var4 + this.random.nextInt(16) + 8;
                int var92 = this.random.nextInt(128);
                int var103 = var5 + this.random.nextInt(16) + 8;
                (new PumpkinPatchFeature()).generate(this.world, this.random, var83, var92, var103);
            }

            int var84 = 0;
            if (var6 == Biome.DESERT) {
                var84 += 10;
            }

            for(int var93 = 0; var93 < var84; ++var93) {
                int var104 = var4 + this.random.nextInt(16) + 8;
                int var110 = this.random.nextInt(128);
                int var114 = var5 + this.random.nextInt(16) + 8;
                (new CactusPatchFeature()).generate(this.world, this.random, var104, var110, var114);
            }

            for(int var94 = 0; var94 < 50; ++var94) {
                int var105 = var4 + this.random.nextInt(16) + 8;
                int var111 = this.random.nextInt(this.random.nextInt(120) + 8);
                int var115 = var5 + this.random.nextInt(16) + 8;
                (new SpringFeature(this.theme.equals("Hell") ? Block.FLOWING_LAVA.id : Block.FLOWING_WATER.id)).generate(this.world, this.random, var105, var111, var115);
            }

            for(int var95 = 0; var95 < 20; ++var95) {
                int var106 = var4 + this.random.nextInt(16) + 8;
                int var112 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
                int var116 = var5 + this.random.nextInt(16) + 8;
                (new SpringFeature(Block.FLOWING_LAVA.id)).generate(this.world, this.random, var106, var112, var116);
            }

            this.temperatures = this.world.method_1781().create(this.temperatures, var4 + 8, var5 + 8, 16, 16);

            for(int var96 = var4 + 8; var96 < var4 + 8 + 16; ++var96) {
                for(int var107 = var5 + 8; var107 < var5 + 8 + 16; ++var107) {
                    int var113 = var96 - (var4 + 8);
                    int var117 = var107 - (var5 + 8);
                    int var22 = this.world.getTopSolidBlockY(var96, var107);
                    double var23 = this.temperatures[var113 * 16 + var117] - (double)(var22 - 64) / (double)64.0F * 0.3;
                    float temp = this.theme.equals("Winter") ? 1.1F : 0.5F;
                    if (!this.theme.equals("Hell") && var23 < (double)temp && var22 > 0 && var22 < 128 && this.world.isAir(var96, var22, var107) && this.world.getMaterial(var96, var22 - 1, var107).blocksMovement() && this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
                        this.world.setBlock(var96, var22, var107, Block.SNOW.id);
                    }
                }
            }

            SandBlock.fallInstantly = false;
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
