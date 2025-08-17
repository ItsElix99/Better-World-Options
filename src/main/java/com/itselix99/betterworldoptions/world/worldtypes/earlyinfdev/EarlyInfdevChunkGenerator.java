package com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev;

import java.util.Random;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.carver.CaveWorldCarverEarlyInfdev;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.util.math.noise.OctavePerlinNoiseSamplerEarlyInfdev;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class EarlyInfdevChunkGenerator implements ChunkSource {
    private final Random random;
    private final Random brickRandom = new Random();
    private final OctavePerlinNoiseSamplerEarlyInfdev noiseGen1;
    private final OctavePerlinNoiseSamplerEarlyInfdev noiseGen2;
    private final OctavePerlinNoiseSamplerEarlyInfdev noiseGen3;
    private final OctavePerlinNoiseSamplerEarlyInfdev noiseGen4;
    private final OctavePerlinNoiseSamplerEarlyInfdev noiseGen5;
    private final OctavePerlinNoiseSamplerEarlyInfdev noiseGen6;
    public final OctavePerlinNoiseSamplerEarlyInfdev forestNoise;
    private final World world;
    private final Generator cave = new CaveWorldCarver();
    private final Generator caveEarlyInfdev = new CaveWorldCarverEarlyInfdev();
    private final Generator ravine = new RavineWorldCarver();
    private Biome[] biomes;
    private double[] temperatures;

    private final String worldType;
    private final boolean betaFeatures;
    private final String theme;

    public EarlyInfdevChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        this.worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        this.betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();

        if (this.theme.equals("Winter")) {
            if (!this.betaFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(true);
            }
        } else {
            if (!this.betaFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(false);
            }
        }

        if (!this.betaFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        }

        this.noiseGen1 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 16);
        this.noiseGen2 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 16);
        this.noiseGen3 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 4);
        this.noiseGen5 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 4);
        this.noiseGen6 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 5);
        this.forestNoise = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 5);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        chunkX <<= 4;
        chunkZ <<= 4;
        int var5 = 0;

        for (int var6 = chunkX; var6 < chunkX + 16; ++var6) {
            for (int var7 = chunkZ; var7 < chunkZ + 16; ++var7) {
                int var8 = var6 / 1024;
                int var9 = var7 / 1024;
                float var10 = (float)(this.noiseGen1.create(var6 / 0.03125D, 0.0D, var7 / 0.03125D) - this.noiseGen2.create(var6 / 0.015625D, 0.0D, var7 / 0.015625D)) / 512.0F / 4.0F;
                float var11 = (float)this.noiseGen5.sample(var6 / 4.0F, var7 / 4.0F);
                float var12 = (float)this.noiseGen6.sample(var6 / 8.0F, var7 / 8.0F) / 8.0F;
                var11 = var11 > 0.0F
                        ? (float)(this.noiseGen3.sample(var6 * 0.51428568F, var7 * 0.51428568F) * var12 / 4.0D)
                        : (float)(this.noiseGen4.sample(var6 * 0.25714284F, var7 * 0.25714284F) * var12);
                int var13 = (int)(var10 + 64.0F + var11);
                if ((float)this.noiseGen5.sample(var6, var7) < 0.0F) {
                    var13 = var13 / 2 << 1;
                    if ((float)this.noiseGen5.sample((double) var6 / 5, (double) var7 / 5) < 0.0F) {
                        ++var13;
                    }
                }

                for (int var14 = 0; var14 < Config.BWOConfig.world.worldHeightLimit.getIntValue(); ++var14) {
                    int index = (var6 - chunkX) * 16 + var7 - chunkZ;
                    Biome var18 = biomes[index];
                    double var19 = temperatures[index];
                    double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                    int var15 = 0;

                    if ((var6 == 0 || var7 == 0) && var14 <= var13 + 2) {
                        var15 = Block.OBSIDIAN.id;
                    } else if (var14 == var13 + 1 && var13 >= 64 && Math.random() < 0.02D && !this.betaFeatures) {
                        int index2 = (index) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + (var14 - 1);
                        if (blocks[index2] == Block.GRASS_BLOCK.id || blocks[index2] == Block.DIRT.id) {
                            var15 = Block.DANDELION.id;
                            if (this.theme.equals("Paradise") && this.random.nextInt(2) == 0) {
                                var15 = Block.ROSE.id;
                            }
                        }
                    } else if (var14 == var13 && var13 >= 64) {
                        if (this.betaFeatures) {
                            var15 = this.theme.equals("Hell") ? (var18.topBlockId != Block.SAND.id ? Block.DIRT.id : var18.topBlockId) : var18.topBlockId;
                        } else {
                            var15 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                        }
                    } else if (var14 <= var13 - 2) {
                        var15 = Block.STONE.id;

                        if (this.betaFeatures && var18 == Biome.DESERT || var18 == Biome.ICE_DESERT) {
                            if (var13 - var14 <= 3) {
                                var15 = Block.SANDSTONE.id;
                            }
                        }
                    } else if (var14 <= var13) {
                        if (this.betaFeatures) {
                            var15 = var18.soilBlockId;
                        } else {
                            var15 = Block.DIRT.id;
                        }
                    } else if (var14 <= 64) {
                        if (!this.theme.equals("Hell") && (var19 < temp && this.betaFeatures || this.theme.equals("Winter")) && var14 >= 63) {
                            var15 = Block.ICE.id;
                        } else {
                            var15 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                        }
                    }

                    this.brickRandom.setSeed(var8 + var9 * 13871L);
                    int var16 = (var8 << 10) + Config.BWOConfig.world.worldHeightLimit.getIntValue() + this.brickRandom.nextInt(512);
                    int var17 = (var9 << 10) + Config.BWOConfig.world.worldHeightLimit.getIntValue() + this.brickRandom.nextInt(512);
                    var16 = Math.abs(var6 - var16);
                    var17 = Math.abs(var7 - var17);
                    if (var17 > var16) var16 = var17;
                    var16 = (Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1) - var16;
                    if (var16 < var13) var16 = var13;
                    if (var14 <= var16 && (var15 == 0 || var15 == Block.WATER.id || var15 == Block.LAVA.id)) {
                        var15 = Block.BRICKS.id;
                    }
                    if (var15 < 0) var15 = 0;

                    blocks[var5++] = (byte) var15;
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
        double[] var5 = this.world.method_1781().temperatureMap;
        this.buildTerrain(chunkX, chunkZ, var3, this.biomes, var5);

        if (this.betaFeatures){
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        } else {
            this.caveEarlyInfdev.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (this.betaFeatures || Config.BWOConfig.world.allowGenWithBetaFeaturesOff) {
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
            this.random.setSeed((long)x * 318279123L + (long)z * 919871212L);
            int var4 = x << 4;
            x = z << 4;

            for(int var61 = 0; var61 < 20; ++var61) {
                int var5 = var4 + this.random.nextInt(16);
                int var6 = this.random.nextInt(128);
                int var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.COAL_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            for(int var62 = 0; var62 < 10; ++var62) {
                int var5 = var4 + this.random.nextInt(16);
                int var6 = this.random.nextInt(64);
                int var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.IRON_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            if (this.random.nextInt(2) == 0) {
                z = var4 + this.random.nextInt(16);
                int var5 = this.random.nextInt(32);
                int var6 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.GOLD_ORE.id)).generate(this.world, this.random, z, var5, var6);
            }

            if (this.random.nextInt(8) == 0) {
                z = var4 + this.random.nextInt(16);
                int var5 = this.random.nextInt(16);
                int var6 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.DIAMOND_ORE.id)).generate(this.world, this.random, z, var5, var6);
            }

            z = (int)this.forestNoise.sample((double)var4 * (double)0.0625F, (double)x * (double)0.0625F) << 3;
            OakTreeFeature var9 = new OakTreeFeature();

            if (this.theme.equals("Woods") && z <= 0) {
                z = 20;
            }

            for(int var63 = 0; var63 < z; ++var63) {
                int var5 = var4 + this.random.nextInt(16);
                int var7 = x + this.random.nextInt(16);
                var9.prepare(1.0F, 1.0F, 1.0F);
                var9.generate(this.world, this.random, var5, this.world.getTopY(var5, var7), var7);
            }

            for(int var5 = var4 + 8; var5 < var4 + 8 + 16; ++var5) {
                for(int var7 = x + 8; var7 < x + 8 + 16; ++var7) {
                    int var6 = this.world.getTopSolidBlockY(var5, var7);
                    if(this.theme.equals("Winter") && var6 > 0 && var6 < 128 && this.world.getBlockId(var5, var6, var7) == 0 && this.world.getMaterial(var5, var6 - 1, var7).isSolid() && this.world.getMaterial(var5, var6 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var5, var6, var7, Block.SNOW.id);
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
                Feature var18 = ((BWOWorld) var6).bwo_getRandomTreeFeatureEarlyInfdev(this.random);
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