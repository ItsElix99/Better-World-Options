package com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev;

import java.util.Random;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.chunk.EmptyFlattenedChunk;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.util.math.noise.OctavePerlinNoiseSamplerEarlyInfdev;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
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
    private final Generator ravine = new RavineWorldCarver();
    private Biome[] biomes;
    private OverworldChunkGenerator defaultChunkGenerator;

    private final String worldType;
    private final boolean oldFeatures;
    private final String theme;
    private final boolean finiteWorld;
    private final String finiteType;
    private final int sizeX;
    private final int sizeZ;

    public EarlyInfdevChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.worldType = bwoProperties.bwo_getWorldType();
        this.oldFeatures = bwoProperties.bwo_isOldFeatures();
        this.theme = bwoProperties.bwo_getTheme();
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);
        this.sizeX = bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION);
        this.sizeZ = bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION);

        if (this.theme.equals("Winter")) {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(true);
            }
        } else {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetSnow(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setSnow(false);
            }
        }

        if (this.theme.equals("Hell") || this.theme.equals("Paradise")) {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetPrecipitation(this.worldType, false);
            } else {
                ((BWOWorld) this.world).bwo_setPrecipitation(false);
            }
        } else {
            if (this.oldFeatures) {
                ((BWOWorld) this.world).bwo_oldBiomeSetPrecipitation(this.worldType, true);
            } else {
                ((BWOWorld) this.world).bwo_setPrecipitation(true);
            }
        }

        if (!this.oldFeatures) {
            ((CaveGenBaseImpl) this.cave).stationapi_setWorld(world);
        } else {
            switch (this.theme) {
                case "Hell" -> BetterWorldOptions.EarlyInfdev.setFogColor(1049600);
                case "Paradise" -> BetterWorldOptions.EarlyInfdev.setFogColor(13033215);
                case "Woods" -> BetterWorldOptions.EarlyInfdev.setFogColor(5069403);
                default -> BetterWorldOptions.EarlyInfdev.setFogColor(11842815);
            }
        }

        this.noiseGen1 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 16);
        this.noiseGen2 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 16);
        this.noiseGen3 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 4);
        this.noiseGen5 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 4);
        this.noiseGen6 = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 5);
        new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 3);
        new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 3);
        new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 3);
        this.forestNoise = new OctavePerlinNoiseSamplerEarlyInfdev(this.random, 5);
        this.defaultChunkGenerator = new OverworldChunkGenerator(world, seed);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        chunkX <<= 4;
        chunkZ <<= 4;
        int var5 = 0;
        double scale = 0.03125D;

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

                boolean sandBeach = this.noiseGen4.sample(var6 * scale, var7 * scale) + this.random.nextDouble() * 0.2D > 0.0D;
                boolean gravelBeach = this.noiseGen4.sample(var7 * scale, var6 * scale) + this.random.nextDouble() * 0.2D > 3.0D;

                for (int var14 = 0; var14 < Config.BWOConfig.world.worldHeightLimit.getIntValue(); ++var14) {
                    int index = (var6 - chunkX) * 16 + var7 - chunkZ;
                    Biome var18 = biomes[index];
                    double var19 = temperatures[index];
                    double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                    int var15 = 0;

                    if ((var6 == 0 || var7 == 0) && var14 <= var13 + 2) {
                        var15 = Block.OBSIDIAN.id;
                    } else if (var14 == var13 && var13 >= 64) {
                        if (!this.oldFeatures) {
                            var15 = this.theme.equals("Hell") ? (var18.topBlockId != Block.SAND.id ? Block.DIRT.id : var18.topBlockId) : var18.topBlockId;
                        } else {
                            var15 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                        }
                    } else if (var14 == var13 + 1 && var13 >= 64 && Math.random() < 0.02D && this.oldFeatures) {
                        int index2 = (index) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + (var14 - 1);
                        if (blocks[index2] == Block.GRASS_BLOCK.id || blocks[index2] == Block.DIRT.id) {
                            var15 = Block.DANDELION.id;
                            if (this.theme.equals("Paradise") && this.random.nextInt(2) == 0) {
                                var15 = Block.ROSE.id;
                            }
                        }
                    } else if (var14 <= var13 - 2) {
                        var15 = Block.STONE.id;

                        if (!this.oldFeatures && ((var18 == Biome.DESERT || var18 == Biome.ICE_DESERT))) {
                            if (var13 - var14 <= 3) {
                                var15 = Block.SANDSTONE.id;
                            }
                        }
                    } else if (var14 <= var13) {
                        if (!this.oldFeatures) {
                            var15 = var18.soilBlockId;
                        } else {
                            var15 = Block.DIRT.id;
                        }
                    } else if (var14 <= 64) {
                        if (!this.theme.equals("Hell") && (var19 < temp && !this.oldFeatures || this.theme.equals("Winter")) && var14 > 63) {
                            var15 = Block.ICE.id;
                        } else {
                            var15 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                        }
                    }

                    if (!this.oldFeatures && var14 <= var13 && var13 >= 61 && var13 <= 65) {
                        if (sandBeach) {
                            if (var15 == (this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id)) {
                                if (var14 == var13) {
                                    var15 = this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id;
                                } else {
                                    var15 = this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id;
                                }
                            } else if (var15 == Block.DIRT.id) {
                                var15 = this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id;
                            }
                        }

                        if (gravelBeach && (var15 == Block.DIRT.id || var15 == Block.GRASS_BLOCK.id || var15 == Block.SAND.id)) {
                            var15 = Block.GRAVEL.id;
                        }
                    }

                    this.brickRandom.setSeed(var8 + var9 * 13871L);
                    int var16 = (var8 << 10) + 128 + this.brickRandom.nextInt(512);
                    int var17 = (var9 << 10) + 128 + this.brickRandom.nextInt(512);
                    var16 = Math.abs(var6 - var16);
                    var17 = Math.abs(var7 - var17);
                    if (var17 > var16) var16 = var17;
                    var16 = (128 - 1) - var16;
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

        if (!this.oldFeatures){
            this.cave.place(this, this.world, chunkX, chunkZ, var3);
        }

        if (Config.BWOConfig.world.ravineGeneration) {
            if (!this.oldFeatures || Config.BWOConfig.world.allowGenWithOldFeaturesOn) {
                this.ravine.place(this, this.world, chunkX, chunkZ, var3);
            }
        }

        FlattenedChunk flattenedChunk = new FlattenedChunk(this.world, chunkX, chunkZ);
        flattenedChunk.fromLegacy(var3);
        flattenedChunk.populateHeightMap();

        if (this.finiteWorld) {
            int blockX = chunkX * 16;
            int blockZ = chunkZ * 16;

            if (blockX < -this.sizeX / 2 || blockX >= this.sizeX / 2 || blockZ < -this.sizeZ / 2 || blockZ >= this.sizeZ / 2) {
                return new EmptyFlattenedChunk(this.world, chunkX, chunkZ);
            }
        }

        return flattenedChunk;
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public void decorate(ChunkSource source, int x, int z) {
        if (this.oldFeatures) {
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

            if (this.theme.equals("Woods") && z <= 4) {
                z = 10;
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
                    if(this.theme.equals("Winter") && var6 > 0 && var6 < this.world.dimension.getHeight() && this.world.getBlockId(var5, var6, var7) == 0 && this.world.getMaterial(var5, var6 - 1, var7).isSolid() && this.world.getMaterial(var5, var6 - 1, var7) != Material.ICE) {
                        this.world.setBlock(var5, var6, var7, Block.SNOW.id);
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