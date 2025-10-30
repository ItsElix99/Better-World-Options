package com.itselix99.betterworldoptions.world.worldtypes.infdev420;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.feature.OldOreFeature;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.util.math.noise.OctavePerlinNoiseSamplerInfdev420;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
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
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.util.Random;

public class Infdev420ChunkGenerator implements ChunkSource {
    private final Random random;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen1;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen2;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen3;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen4;
    private final OctavePerlinNoiseSamplerInfdev420 noiseGen5;
    private final OctavePerlinNoiseSamplerInfdev420 forestNoise;
    private final World world;
    private final Generator cave = new CaveWorldCarver();
    private final Generator ravine = new RavineWorldCarver();
    private double[] noiseArray;
    private double[] noise3;
    private double[] noise1;
    private double[] noise2;
    private Biome[] biomes;
    private OverworldChunkGenerator defaultChunkGenerator;

    private final String worldType;
    private final boolean oldFeatures;
    private final String theme;

    public Infdev420ChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        new Random(seed);
        this.worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        this.oldFeatures = ((BWOProperties) this.world.getProperties()).bwo_isOldFeatures();
        this.theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();

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
                case "Hell" -> BetterWorldOptions.Infdev.setFogColor(1049600);
                case "Paradise" -> BetterWorldOptions.Infdev.setFogColor(13033215);
                case "Woods" -> BetterWorldOptions.Infdev.setFogColor(5069403);
                default -> BetterWorldOptions.Infdev.setFogColor(11587839);
            }
        }

        this.noiseGen1 = new OctavePerlinNoiseSamplerInfdev420(this.random, 16);
        this.noiseGen2 = new OctavePerlinNoiseSamplerInfdev420(this.random, 16);
        this.noiseGen3 = new OctavePerlinNoiseSamplerInfdev420(this.random, 8);
        this.noiseGen4 = new OctavePerlinNoiseSamplerInfdev420(this.random, 4);
        this.noiseGen5 = new OctavePerlinNoiseSamplerInfdev420(this.random, 4);
        new OctavePerlinNoiseSamplerInfdev420(this.random, 5);
        this.forestNoise = new OctavePerlinNoiseSamplerInfdev420(this.random, 5);
        this.defaultChunkGenerator = new OverworldChunkGenerator(world, seed);
    }

    public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
        int var10003 = chunkX << 2;
        int var8 = chunkZ << 2;
        int var7 = var10003;
        double[] var6 = this.noiseArray;
        Infdev420ChunkGenerator var71 = this;
        int vertical = Config.BWOConfig.world.worldHeightLimit.getIntValue() / 8;

        if(var6 == null) {
            var6 = new double[5 * 5 * (vertical + 1)];
        }

        this.noise3 = this.noiseGen3.create(this.noise3, var7, 0, var8, 5, vertical + 1, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.noise1 = this.noiseGen1.create(this.noise1, var7, 0, var8, 5, vertical + 1, 5, 684.412D, 684.412D, 684.412D);
        this.noise2 = this.noiseGen2.create(this.noise2, var7, 0, var8, 5, vertical + 1, 5, 684.412D, 684.412D, 684.412D);
        var7 = 0;

        for(var8 = 0; var8 < 5; ++var8) {
            for(int var9 = 0; var9 < 5; ++var9) {
                for(int var10 = 0; var10 < vertical + 1; ++var10) {
                    double var63 = ((double)var10 - 8.5D) * 12.0D;
                    if(var63 < 0.0D) {
                        var63 *= 2.0D;
                    }

                    double var65 = var71.noise1[var7] / 512.0D;
                    double var67 = var71.noise2[var7] / 512.0D;
                    double var69 = (var71.noise3[var7] / 10.0D + 1.0D) / 2.0D;
                    double var61;
                    if(var69 < 0.0D) {
                        var61 = var65;
                    } else if(var69 > 1.0D) {
                        var61 = var67;
                    } else {
                        var61 = var65 + (var67 - var65) * var69;
                    }

                    var61 -= var63;
                    var6[var7] = var61;
                    ++var7;
                }
            }
        }

        this.noiseArray = var6;

        int var72;
        int var73;
        for(var72 = 0; var72 < 4; ++var72) {
            for(var73 = 0; var73 < 4; ++var73) {
                for(var7 = 0; var7 < vertical; ++var7) {
                    double var75 = this.noiseArray[(var72 * 5 + var73) * (vertical + 1) + var7];
                    double var77 = this.noiseArray[(var72 * 5 + var73 + 1) * (vertical + 1) + var7];
                    double var12 = this.noiseArray[((var72 + 1) * 5 + var73) * (vertical + 1) + var7];
                    double var14 = this.noiseArray[((var72 + 1) * 5 + var73 + 1) * (vertical + 1) + var7];
                    double var16 = this.noiseArray[(var72 * 5 + var73) * (vertical + 1) + var7 + 1];
                    double var18 = this.noiseArray[(var72 * 5 + var73 + 1) * (vertical + 1) + var7 + 1];
                    double var20 = this.noiseArray[((var72 + 1) * 5 + var73) * (vertical + 1) + var7 + 1];
                    double var22 = this.noiseArray[((var72 + 1) * 5 + var73 + 1) * (vertical + 1) + var7 + 1];

                    for(int var24 = 0; var24 < 8; ++var24) {
                        double var25 = (double)var24 / 8.0D;
                        double var27 = var75 + (var16 - var75) * var25;
                        double var29 = var77 + (var18 - var77) * var25;
                        double var31 = var12 + (var20 - var12) * var25;
                        double var33 = var14 + (var22 - var14) * var25;

                        for(int var82 = 0; var82 < 4; ++var82) {
                            double var36 = (double)var82 / 4.0D;
                            double var38 = var27 + (var31 - var27) * var36;
                            double var40 = var29 + (var33 - var29) * var36;

                            int shiftY = MathHelper.ceilLog2(Config.BWOConfig.world.worldHeightLimit.getIntValue());
                            int shiftXZ = shiftY + 4;
                            int var26 = var82 + (var72 << 2) << shiftXZ | (var73 << 2) << shiftY | (var7 << 3) + var24;

                            for(int var35 = 0; var35 < 4; ++var35) {
                                double var44 = (double)var35 / 4.0D;
                                double var46 = var38 + (var40 - var38) * var44;
                                double var53 = temperatures[(var72 * 4 + var82) * 16 + var73 * 4 + var35];
                                double temp = this.theme.equals("Winter") ? 1.1D : 0.5D;
                                int var83 = 0;
                                if((var7 << 3) + var24 < 64) {
                                    if (!this.theme.equals("Hell") && (var53 < temp && !this.oldFeatures || this.theme.equals("Winter")) && var7 * 8 + var24 >= 63) {
                                        var83 = Block.ICE.id;
                                    } else {
                                        var83 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                                    }
                                }

                                if(var46 > 0.0D) {
                                    var83 = Block.STONE.id;
                                }

                                blocks[var26] = (byte)var83;
                                var26 += Config.BWOConfig.world.worldHeightLimit.getIntValue();
                            }
                        }
                    }
                }
            }
        }

        for(var72 = 0; var72 < 16; ++var72) {
            for(var73 = 0; var73 < 16; ++var73) {
                double var74 = (chunkX << 4) + var72;
                double var76 = (chunkZ << 4) + var73;
                Biome var82 = biomes[var72 + var73 * 16];
                boolean var13 = this.noiseGen4.create(var74 * (1.0D / 32.0D), var76 * (1.0D / 32.0D), 0.0D) + this.random.nextDouble() * 0.2D > 0.0D;
                boolean var78 = this.noiseGen4.create(var76 * (1.0D / 32.0D), 109.0134D, var74 * (1.0D / 32.0D)) + this.random.nextDouble() * 0.2D > 3.0D;
                int var15 = (int)(this.noiseGen5.sample(var74 * (1.0D / 32.0D) * 2.0D, var76 * (1.0D / 32.0D) * 2.0D) / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                int var17 = -1;
                int var80;
                int var19;

                if (!this.oldFeatures) {
                    var80 = this.theme.equals("Hell") ? (var82.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var82.topBlockId) : var82.topBlockId;
                    var19 = var82.soilBlockId;
                } else {
                    var80 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                    var19 = Block.DIRT.id;
                }

                for(int var81 = Config.BWOConfig.world.worldHeightLimit.getIntValue() - 1; var81 >= 0; --var81) {
                    int var79 = (var73 * 16 + var72) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var81;
                    if(blocks[var79] == 0) {
                        var17 = -1;
                    } else if(blocks[var79] == Block.STONE.id) {
                        if(var17 == -1) {
                            if(var15 <= 0) {
                                var80 = 0;
                                var19 = (byte)Block.STONE.id;
                            } else if(var81 >= 60 && var81 <= 65) {
                                if (!this.oldFeatures) {
                                    var80 = this.theme.equals("Hell") ? (var82.topBlockId == Block.GRASS_BLOCK.id ? Block.DIRT.id : var82.topBlockId) : var82.topBlockId;
                                    var19 = var82.soilBlockId;
                                } else {
                                    var80 = this.theme.equals("Hell") ? Block.DIRT.id : Block.GRASS_BLOCK.id;
                                    var19 = Block.DIRT.id;
                                }
                                if(var78) {
                                    var80 = 0;
                                }

                                if(var78) {
                                    var19 = Block.GRAVEL.id;
                                }

                                if(var13) {
                                    var80 = this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : Block.SAND.id;
                                }

                                if(var13) {
                                    var19 = this.theme.equals("Hell") ? Block.DIRT.id : Block.SAND.id;
                                }
                            }

                            if(var81 < 64 && var80 == 0) {
                                var80 = this.theme.equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                            }

                            var17 = var15;
                            if(var81 >= 63) {
                                blocks[var79] = (byte)var80;
                            } else {
                                blocks[var79] = (byte)var19;
                            }
                        } else if(var17 > 0) {
                            --var17;
                            blocks[var79] = (byte)var19;
                            if (var17 == 0 && var19 == Block.SAND.id && !this.oldFeatures) {
                                var17 = this.random.nextInt(4);
                                var19 = (byte)Block.SANDSTONE.id;
                            }
                        }
                    }

                    --var79;
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

        if (!this.oldFeatures) {
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

            int var5;
            int var6;
            int var7;
            for(z = 0; z < 20; ++z) {
                var5 = var4 + this.random.nextInt(16);
                var6 = this.random.nextInt(128);
                var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.COAL_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            for(z = 0; z < 10; ++z) {
                var5 = var4 + this.random.nextInt(16);
                var6 = this.random.nextInt(64);
                var7 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.IRON_ORE.id)).generate(this.world, this.random, var5, var6, var7);
            }

            if(this.random.nextInt(2) == 0) {
                z = var4 + this.random.nextInt(16);
                var5 = this.random.nextInt(32);
                var6 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.GOLD_ORE.id)).generate(this.world, this.random, z, var5, var6);
            }

            if(this.random.nextInt(8) == 0) {
                z = var4 + this.random.nextInt(16);
                var5 = this.random.nextInt(16);
                var6 = x + this.random.nextInt(16);
                (new OldOreFeature(Block.DIAMOND_ORE.id)).generate(this.world, this.random, z, var5, var6);
            }

            z = (int)(this.forestNoise.sample((double)var4 * 0.05D, (double)x * 0.05D) - this.random.nextDouble());
            if (this.theme.equals("Woods")) {
                if(z <= 4) {
                    z = 10;
                }
            } else {
                if (z < 0) {
                    z = 0;
                }
            }

            LargeOakTreeFeature var9 = new LargeOakTreeFeature();
            if(this.random.nextInt(100) == 0) {
                ++z;
            }

            for(var6 = 0; var6 < z; ++var6) {
                var7 = var4 + this.random.nextInt(16) + 8;
                int var8 = x + this.random.nextInt(16) + 8;
                var9.prepare(1.0D, 1.0D, 1.0D);
                var9.generate(this.world, this.random, var7, this.world.getTopY(var7, var8), var8);
            }

            if (this.theme.equals("Paradise")) {
                for(z = 0; z < 12; ++z) {
                    var5 = var4 + this.random.nextInt(16) + 8;
                    var6 = this.random.nextInt(128);
                    var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.DANDELION.id)).generate(this.world, this.random, var5, var6, var7);
                }

                for (z = 0; z < 12; z++) {
                    var5 = var4 + this.random.nextInt(16) + 8;
                    var6 = this.random.nextInt(128);
                    var7 = x + this.random.nextInt(16) + 8;
                    (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var5, var6, var7);
                }
            }

            for(var5 = var4 + 8; var5 < var4 + 8 + 16; ++var5) {
                for(var7 = x + 8; var7 < x + 8 + 16; ++var7) {
                    var6 = this.world.getTopSolidBlockY(var5, var7);
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
