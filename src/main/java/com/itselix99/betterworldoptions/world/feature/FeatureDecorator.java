package com.itselix99.betterworldoptions.world.feature;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.feature.adventureupdate.*;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.Release11BiomeProperties;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class FeatureDecorator {
    protected World world;
    protected Random random;
    protected int x;
    protected int z;
    protected final Biome biome;
    protected Theme theme;
    protected Identifier beta181WorldType = BetterWorldOptions.NAMESPACE.id("beta_1.8.1");
    protected Feature clayPatch = new ClayPatchFeature(4);
    protected Feature sandPatch;
    protected Feature gravelPatch;
    protected Feature dirtVein;
    protected Feature gravelVein;
    protected Feature coalOreVein;
    protected Feature ironOreVein;
    protected Feature goldOreVein;
    protected Feature redstoneOreVein;
    protected Feature diamondOreVein;
    protected Feature lapisOreVein;
    protected Feature yellowFlower;
    protected Feature redFlower;
    protected Feature brownMushroom;
    protected Feature redMushroom;
    protected Feature hugeRedMushroom;
    protected Feature sugarcane;
    protected Feature cactus;
    protected Feature lilyPad;
    protected OakTreeFeature tree = new OakTreeFeature();
    protected LargeOakTreeFeature largeTree = new LargeOakTreeFeature();
    protected BirchTreeFeature birchTree = new BirchTreeFeature();
    protected SwampTreeFeature swampTree = new SwampTreeFeature();
    public int lilyPadAttempts;
    public int treeAttempts;
    public int flowerAttempts;
    public int grassAttempts;
    public int deadBushAttempts;
    public int mushroomAttempts;
    public int sugarcaneAttempts;
    public int cactusAttempts;
    public int gravelPatchAttempts;
    public int sandPatchAttempts;
    public int clayPatchAttempts;
    public int hugeMushroomAttempts;

    public FeatureDecorator(Biome biome) {
        this.sandPatch = new BlockPatchFeature(7, Block.SAND.id);
        this.gravelPatch = new BlockPatchFeature(6, Block.GRAVEL.id);
        this.dirtVein = new AdvUpdOreFeature(Block.DIRT.id, 32);
        this.gravelVein = new AdvUpdOreFeature(Block.GRAVEL.id, 32);
        this.coalOreVein = new AdvUpdOreFeature(Block.COAL_ORE.id, 16);
        this.ironOreVein = new AdvUpdOreFeature(Block.IRON_ORE.id, 8);
        this.goldOreVein = new AdvUpdOreFeature(Block.GOLD_ORE.id, 8);
        this.redstoneOreVein = new AdvUpdOreFeature(Block.REDSTONE_ORE.id, 7);
        this.diamondOreVein = new AdvUpdOreFeature(Block.DIAMOND_ORE.id, 7);
        this.lapisOreVein = new AdvUpdOreFeature(Block.LAPIS_ORE.id, 6);
        this.yellowFlower = new PlantPatchFeature(Block.DANDELION.id);
        this.redFlower = new PlantPatchFeature(Block.ROSE.id);
        this.brownMushroom = new PlantPatchFeature(Block.BROWN_MUSHROOM.id);
        this.redMushroom = new PlantPatchFeature(Block.RED_MUSHROOM.id);
        this.hugeRedMushroom = new HugeMushroomFeature();
        this.sugarcane = new SugarCanePatchFeature();
        this.cactus = new CactusPatchFeature();
        this.lilyPad = new LilyPadFeature();
        this.lilyPadAttempts = 0;
        this.treeAttempts = 0;
        this.flowerAttempts = 2;
        this.grassAttempts = 1;
        this.deadBushAttempts = 0;
        this.mushroomAttempts = 0;
        this.sugarcaneAttempts = 0;
        this.cactusAttempts = 0;
        this.gravelPatchAttempts = 1;
        this.sandPatchAttempts = 3;
        this.clayPatchAttempts = 1;
        this.hugeMushroomAttempts = 0;
        this.biome = biome;
    }

    public Feature getRandomTreeFeature(Biome biome, Random random) {
        if (biome == Biome.FOREST || biome == Release11BiomeProperties.FOREST_HILLS) {
            if (random.nextInt(5) == 0) {
                return this.birchTree;
            } else {
                return random.nextInt(10) == 0 ? this.largeTree : this.tree;
            }
        } else if (biome == Release11BiomeProperties.TAIGA_HILLS) {
            return random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
        } else if (biome == Biome.SWAMPLAND) {
            return this.swampTree;
        }

        return biome.getRandomTreeFeature(random);
    }

    public void decorate(World world, Random random, int x, int z) {
        if (this.world != null) {
            throw new RuntimeException("Already decorating!!");
        } else {
            this.world = world;
            this.random = random;
            this.x = x;
            this.z = z;
            this.theme = ((BWOWorld) world).bwo_getTheme();
            this.decorate();
            this.world = null;
            this.random = null;
        }
    }

    private void decorate() {
        this.placeVeins();

        for(int var1 = 0; var1 < this.sandPatchAttempts; ++var1) {
            int var2 = this.x + this.random.nextInt(16) + 8;
            int var3 = this.z + this.random.nextInt(16) + 8;
            this.sandPatch.generate(this.world, this.random, var2, ((BWOWorld) this.world).bwo_getSurfaceHeight(var2, var3), var3);
        }

        for(int var4 = 0; var4 < this.clayPatchAttempts; ++var4) {
            int var5 = this.x + this.random.nextInt(16) + 8;
            int var6 = this.z + this.random.nextInt(16) + 8;
            this.clayPatch.generate(this.world, this.random, var5, ((BWOWorld) this.world).bwo_getSurfaceHeight(var5, var6), var6);
        }

        for(int var7 = 0; var7 < this.gravelPatchAttempts; ++var7) {
            int var8 = this.x + this.random.nextInt(16) + 8;
            int var9 = this.z + this.random.nextInt(16) + 8;
            this.sandPatch.generate(this.world, this.random, var8, ((BWOWorld) this.world).bwo_getSurfaceHeight(var8, var9), var9);
        }

        int var10 = this.treeAttempts;
        if (this.theme.isDenseWoods()) {
            var10 = 10;
        }

        if (this.random.nextInt(10) == 0) {
            ++var10;
        }

        for(int var11 = 0; var11 < var10; ++var11) {
            int var12 = this.x + this.random.nextInt(16) + 8;
            int var13 = this.z + this.random.nextInt(16) + 8;
            Feature var14 = this.getRandomTreeFeature(this.biome, this.random);
            var14.prepare(1.0F, 1.0F, 1.0F);
            var14.generate(this.world, this.random, var12, this.world.getTopY(var12, var13), var13);
        }

        if (!((BWOWorld) this.world).bwo_getWorldType().getId().equals(this.beta181WorldType)) {
            for (int var15 = 0; var15 < this.hugeMushroomAttempts; ++var15) {
                int var16 = this.x + this.random.nextInt(16) + 8;
                int var17 = this.z + this.random.nextInt(16) + 8;
                this.hugeRedMushroom.generate(this.world, this.random, var16, this.world.getTopY(var16, var17), var17);
            }
        }

        if (this.theme.isFloweryLand()) {
            for (int var18 = 0; var18 < 24; ++var18) {
                int var19 = this.x + this.random.nextInt(16) + 8;
                int var20 = this.random.nextInt(128);
                int var21 = this.z + this.random.nextInt(16) + 8;
                var20 = this.getTopY2(var20, var19, var21, 32);
                this.yellowFlower.generate(this.world, this.random, var19, var20, var21);
                if (this.random.nextInt(4) == 0) {
                    var19 = this.x + this.random.nextInt(16) + 8;
                    var20 = this.random.nextInt(128);
                    var21 = this.z + this.random.nextInt(16) + 8;
                    var20 = this.getTopY2(var20, var19, var21, 32);
                    new PlantPatchFeature(Block.ROSE.id).generate(this.world, this.random, var19, var20, var21);
                }
            }
        } else {
            for (int var18 = 0; var18 < this.flowerAttempts; ++var18) {
                int var19 = this.x + this.random.nextInt(16) + 8;
                int var20 = this.random.nextInt(128);
                int var21 = this.z + this.random.nextInt(16) + 8;
                var20 = this.getTopY2(var20, var19, var21, 32);
                this.yellowFlower.generate(this.world, this.random, var19, var20, var21);
                if (this.random.nextInt(4) == 0) {
                    var19 = this.x + this.random.nextInt(16) + 8;
                    var20 = this.random.nextInt(128);
                    var21 = this.z + this.random.nextInt(16) + 8;
                    var20 = this.getTopY2(var20, var19, var21, 32);
                    if (((BWOWorld) this.world).bwo_getWorldType().getId().equals(this.beta181WorldType)) {
                        this.yellowFlower.generate(this.world, this.random, var19, var20, var21);
                    } else {
                        this.redFlower.generate(this.world, this.random, var19, var20, var21);
                    }
                }
            }
        }

        for(int var22 = 0; var22 < this.grassAttempts; ++var22) {
            int var23 = 1;
            int var24 = this.x + this.random.nextInt(16) + 8;
            int var25 = this.random.nextInt(128);
            int var26 = this.z + this.random.nextInt(16) + 8;
            var25 = this.getTopY(var25, var24, var26, 2);
            (new GrassPatchFeature(Block.GRASS.id, var23)).generate(this.world, this.random, var24, var25, var26);
        }

        for(int var27 = 0; var27 < this.deadBushAttempts; ++var27) {
            int var28 = this.x + this.random.nextInt(16) + 8;
            int var29 = this.random.nextInt(128);
            int var30 = this.z + this.random.nextInt(16) + 8;
            var29 = this.getTopY(var29, var28, var30, 2);
            (new DeadBushPatchFeature(Block.DEAD_BUSH.id)).generate(this.world, this.random, var28, var29, var30);
        }

        if (!((BWOWorld) this.world).bwo_getWorldType().getId().equals(this.beta181WorldType)) {
            for (int var31 = 0; var31 < this.lilyPadAttempts; ++var31) {
                int var32 = this.x + this.random.nextInt(16) + 8;
                int var33 = this.z + this.random.nextInt(16) + 8;

                int var34;
                for (var34 = this.getTopY(this.random.nextInt(128), var32, var33, 2); var34 > 0 && this.world.getBlockId(var32, var34 - 1, var33) == 0; --var34) {
                }

                this.lilyPad.generate(this.world, this.random, var32, var34, var33);
            }
        }

        for(int var35 = 0; var35 < this.mushroomAttempts; ++var35) {
            if (this.random.nextInt(4) == 0) {
                int var36 = this.x + this.random.nextInt(16) + 8;
                int var37 = this.z + this.random.nextInt(16) + 8;
                int var38 = this.world.getTopY(var36, var37);
                this.brownMushroom.generate(this.world, this.random, var36, var38, var37);
            }

            if (this.random.nextInt(8) == 0) {
                int var39 = this.x + this.random.nextInt(16) + 8;
                int var40 = this.z + this.random.nextInt(16) + 8;
                int var41 = this.getTopY(this.random.nextInt(128), var39, var40, 2);
                this.redMushroom.generate(this.world, this.random, var39, var41, var40);
            }
        }

        if (this.random.nextInt(4) == 0) {
            int var42 = this.x + this.random.nextInt(16) + 8;
            int var43 = this.random.nextInt(128);
            int var44 = this.z + this.random.nextInt(16) + 8;
            var43 = this.getTopY(var43, var42, var44, 2);
            this.brownMushroom.generate(this.world, this.random, var42, var43, var44);
        }

        if (this.random.nextInt(8) == 0) {
            int var45 = this.x + this.random.nextInt(16) + 8;
            int var46 = this.random.nextInt(128);
            int var47 = this.z + this.random.nextInt(16) + 8;
            var46 = this.getTopY(var46, var45, var47, 2);
            this.redMushroom.generate(this.world, this.random, var45, var46, var47);
        }

        for(int var48 = 0; var48 < this.sugarcaneAttempts; ++var48) {
            int var49 = this.x + this.random.nextInt(16) + 8;
            int var50 = this.z + this.random.nextInt(16) + 8;
            int var51 = this.getTopY(this.random.nextInt(128), var49, var50, 2);
            this.sugarcane.generate(this.world, this.random, var49, var51, var50);
        }

        for(int var52 = 0; var52 < 10; ++var52) {
            int var53 = this.x + this.random.nextInt(16) + 8;
            int var54 = this.random.nextInt(128);
            int var55 = this.z + this.random.nextInt(16) + 8;
            var54 = this.getTopY(var54, var53, var55, 2);
            this.sugarcane.generate(this.world, this.random, var53, var54, var55);
        }

        if (this.random.nextInt(32) == 0) {
            int var56 = this.x + this.random.nextInt(16) + 8;
            int var57 = this.random.nextInt(128);
            int var58 = this.z + this.random.nextInt(16) + 8;
            var57 = this.getTopY(var57, var56, var58, 2);
            (new PumpkinPatchFeature()).generate(this.world, this.random, var56, var57, var58);
        }

        for(int var59 = 0; var59 < this.cactusAttempts; ++var59) {
            int var60 = this.x + this.random.nextInt(16) + 8;
            int var61 = this.random.nextInt(128);
            int var62 = this.z + this.random.nextInt(16) + 8;
            var61 = this.getTopY(var61, var60, var62, 2);
            this.cactus.generate(this.world, this.random, var60, var61, var62);
        }

        for(int var63 = 0; var63 < 50; ++var63) {
            int var64 = this.x + this.random.nextInt(16) + 8;
            int var65 = this.random.nextInt(this.random.nextInt(this.world.getTopY() - 8) + 8);
            int var66 = this.z + this.random.nextInt(16) + 8;
            (new SpringFeature(this.theme.getFlowingLiquidBlock())).generate(this.world, this.random, var64, var65, var66);
        }

        for(int var67 = 0; var67 < 20; ++var67) {
            int var68 = this.x + this.random.nextInt(16) + 8;
            int var69 = this.random.nextInt(this.random.nextInt(this.random.nextInt(this.world.getTopY() - 16) + 8) + 8);
            int var70 = this.z + this.random.nextInt(16) + 8;
            (new SpringFeature(Block.FLOWING_LAVA.id)).generate(this.world, this.random, var68, var69, var70);
        }

    }

    protected void placeVeinLinear(int count, Feature vein, int minHeight, int maxHeight) {
        for(int var5 = 0; var5 < count; ++var5) {
            int var6 = this.x + this.random.nextInt(16);
            int var7 = this.random.nextInt(maxHeight - minHeight) + minHeight;
            int var8 = this.z + this.random.nextInt(16);
            vein.generate(this.world, this.random, var6, var7, var8);
        }

    }

    protected void placeVeinTriangular(int count, Feature vein, int minHeight, int maxHeight) {
        for(int var5 = 0; var5 < count; ++var5) {
            int var6 = this.x + this.random.nextInt(16);
            int var7 = this.random.nextInt(maxHeight) + this.random.nextInt(maxHeight) + (minHeight - maxHeight);
            int var8 = this.z + this.random.nextInt(16);
            vein.generate(this.world, this.random, var6, var7, var8);
        }

    }

    protected void placeVeins() {
        this.placeVeinLinear(20, this.dirtVein, 0, this.world.getTopY());
        this.placeVeinLinear(10, this.gravelVein, 0, this.world.getTopY());
        this.placeVeinLinear(20, this.coalOreVein, 0, 128);
        this.placeVeinLinear(20, this.ironOreVein, 0, 64);
        this.placeVeinLinear(2, this.goldOreVein, 0, 32);
        this.placeVeinLinear(8, this.redstoneOreVein, 0, 16);
        this.placeVeinLinear(1, this.diamondOreVein, 0, 16);
        this.placeVeinTriangular(1, this.lapisOreVein, 16, 16);
    }

    protected int getTopY(int height, int x, int z, int heightMultiplier) {
        if (this.world.getHeight() > 128) {
            return this.random.nextInt(this.world.getTopY(x, z) * heightMultiplier);
        }

        return height;
    }

    protected int getTopY2(int height, int x, int z, int heightOffset) {
        if (this.world.getHeight() > 128) {
            return this.random.nextInt(this.world.getTopY(x, z) + heightOffset);
        }

        return height;
    }
}