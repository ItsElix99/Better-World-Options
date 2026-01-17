package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWONoise;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.block.Block;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlantPatchFeature;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(OverworldChunkGenerator.class)
public abstract class OverworldChunkGeneratorMixin implements ChunkSource {
    @Shadow private World world;
    @Shadow private Random random;
    @Shadow private OctavePerlinNoiseSampler perlinNoise2;
    @Shadow private OctavePerlinNoiseSampler perlinNoise3;
    @Unique private Generator ravine = new RavineWorldCarver();
    @Unique private String worldType;
    @Unique private String theme;
    @Unique private boolean infiniteWorld;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_initBWOProperties(World world, long seed, CallbackInfo ci) {
        this.worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
        this.theme = ((BWOProperties) world.getProperties()).bwo_getTheme();
        this.infiniteWorld = true;

        ((BWOWorld) this.world).bwo_setSnow(this.theme.equals("Winter"));
        ((BWOWorld) this.world).bwo_setPrecipitation(!this.theme.equals("Hell") && !this.theme.equals("Paradise"));
    }

//    @ModifyVariable(
//            method = "buildTerrain",
//            at = @At("HEAD"),
//            ordinal = 0,
//            argsOnly = true
//    )
//    private int modifyChunkXForFarlands(int chunkX) {
//        if (this.worldType.equals("Farlands")) {
//            if (chunkX >= 8) {
//                return chunkX + 784426;
//            } else {
//                return chunkX - 784426;
//            }
//        }
//        return chunkX;
//    }

    @ModifyConstant(method = "buildTerrain", constant = @Constant(doubleValue = 0.5D, ordinal = 0))
    private double bwo_changeTempInWinterTheme(double constant) {
        if (this.theme.equals("Winter")) {
            return 1.1D;
        }

        return constant;
    }

    @WrapOperation(
            method = "buildTerrain",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 0
            )
    )
    private int bwo_replaceIceWithLavaInHellTheme(Block block, Operation<Integer> original) {
        return this.theme.equals("Hell") ? Block.LAVA.id : original.call(block);
    }

    @WrapOperation(
            method = "buildTerrain",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 1
            )
    )
    private int bwo_replaceWaterWithLavaInHellTheme(Block block, Operation<Integer> original) {
        return this.theme.equals("Hell") ? Block.LAVA.id : original.call(block);
    }

    @ModifyVariable(
            method = "buildSurfaces",
            at = @At("STORE"),
            ordinal = 0
    )
    private Biome bwo_beachFixBiomeIndex(Biome original, @Local(ordinal = 0, argsOnly = true)Biome[] biomes, @Local(name = "var8")int var8, @Local(name = "var9")int var9) {
        if (Config.BWOConfig.world.beachFix) {
            return biomes[var9 + var8 * 16];
        }

        return original;
    }

    @ModifyVariable(
            method = "buildSurfaces",
            at = @At("STORE"),
            name = "var11"
    )
    private int bwo_fixSandBeach(int original, @Local(ordinal = 0, argsOnly = true)int chunkX, @Local(ordinal = 1, argsOnly = true)int chunkZ, @Local(name = "var6")double var6, @Local(name = "var8")int var8, @Local(name = "var9")int var9) {
        if (Config.BWOConfig.world.beachFix) {
            double x = (chunkX << 4) + var8;
            double z = (chunkZ << 4) + var9;
            return ((BWONoise)this.perlinNoise2).bwo_generateNoise(x * var6, z * var6, 0.0D) + this.random.nextDouble() * 0.2 > (double)0.0F ? 1 : 0;
        }

        return original;
    }

    @ModifyVariable(
            method = "buildSurfaces",
            at = @At("STORE"),
            name = "var12"
    )
    private int bwo_fixGravelBeach(int original, @Local(ordinal = 0, argsOnly = true)int chunkX, @Local(ordinal = 1, argsOnly = true)int chunkZ, @Local(name = "var6")double var6, @Local(name = "var8")int var8, @Local(name = "var9")int var9) {
        if (Config.BWOConfig.world.beachFix) {
            double x = (chunkX << 4) + var8;
            double z = (chunkZ << 4) + var9;
            return ((BWONoise)this.perlinNoise2).bwo_generateNoise(z * var6, var6, x * var6) + this.random.nextDouble() * 0.2 > (double)3.0F ? 1 : 0;
        }

        return original;
    }

    @ModifyVariable(
            method = "buildSurfaces",
            at = @At("STORE"),
            name = "var13"
    )
    private int bwo_fixDepthBuffer(int original, @Local(ordinal = 0, argsOnly = true)int chunkX, @Local(ordinal = 1, argsOnly = true)int chunkZ, @Local(name = "var6")double var6, @Local(name = "var8")int var8, @Local(name = "var9")int var9) {
        if (Config.BWOConfig.world.beachFix) {
            double x = (chunkX << 4) + var8;
            double z = (chunkZ << 4) + var9;
            return (int)(this.perlinNoise3.sample(x * var6 * 2.0D, z * var6 * 2.0D) / (double)3.0F + (double)3.0F + this.random.nextDouble() * (double)0.25F);
        }

        return original;
    }

    @ModifyVariable(
            method = "buildSurfaces",
            at = @At("STORE"),
            name = "var18"
    )
    private int bwo_beachFixIndex(int original, @Local(name = "var8")int var8, @Local(name = "var9")int var9, @Local(name = "var17")int var17) {
        if (Config.BWOConfig.world.beachFix) {
            return (var8 * 16 + var9) * MathHelper.smallestEncompassingPowerOfTwo(this.world.getHeight()) + var17;
        }

        return original;
    }

    @WrapOperation(
            method = "buildSurfaces",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 6
            )
    )
    private int bwo_fixIceNearBeach(Block block, Operation<Integer> original, @Local(name = "var5")int var5, @Local(ordinal = 0)Biome var10, @Local(name = "var17")int var17) {
        if (Config.BWOConfig.world.beachFix) {
            if (!this.theme.equals("Hell") && (this.theme.equals("Winter") || (var10 == Biome.TAIGA || var10 == Biome.TUNDRA || var10 == Biome.ICE_DESERT)) && var17 >= var5 - 1) {
                return (byte) (Block.ICE.id);
            } else {
                return original.call(block);
            }
        }

        return original.call(block);
    }

    @ModifyExpressionValue(
            method = "buildSurfaces",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/biome/Biome;topBlockId:B"
            )
    )
    private byte bwo_replaceTopBlockWithDirtInHellTheme(byte original) {
        return (byte) (this.theme.equals("Hell") ? (original != Block.SAND.id ? Block.DIRT.id : original) : original);
    }

    @WrapOperation(
            method = "buildSurfaces",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 4
            )
    )
    private int bwo_replaceSandWithGrassBlockInHellTheme(Block block, Operation<Integer> original) {
        return (byte) (this.theme.equals("Hell") ? Block.GRASS_BLOCK.id : original.call(block));
    }

    @WrapOperation(
            method = "buildSurfaces",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 5
            )
    )
    private int bwo_replaceSandWithDirtInHellTheme(Block block, Operation<Integer> original) {
        return (byte) (this.theme.equals("Hell") ? Block.DIRT.id : original.call(block));
    }

    @WrapOperation(
            method = "buildSurfaces",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 6
            )
    )
    private int bwo_replaceWaterWithLavaInHellTheme2(Block block, Operation<Integer> original) {
        return (byte) (this.theme.equals("Hell") ? Block.LAVA.id : original.call(block));
    }

    @Inject(
            method = "getChunk",
            at = @At
                    (
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/gen/Generator;place(Lnet/minecraft/world/chunk/ChunkSource;Lnet/minecraft/world/World;II[B)V"
                    )
    )
    private void bwo_ravineGeneration(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir, @Local byte[] var3) {
        if (Config.BWOConfig.world.ravineGeneration) {
            this.ravine.place(this, this.world, chunkX, chunkZ, var3);
        }
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 0
            )
    )
    private int bwo_replaceWaterWithLavaInHellTheme3(Block block, Operation<Integer> original) {
        return this.theme.equals("Hell") ? Block.LAVA.id : original.call(block);
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctavePerlinNoiseSampler;sample(DD)D",
                    ordinal = 0
            )
    )
    private double bwo_captureForestNoise(OctavePerlinNoiseSampler instance, double x, double z, Operation<Double> original, @Share("var37") LocalDoubleRef var37) {
        var37.set(((original.call(instance, x, z) / (double)8.0F + this.random.nextDouble() * (double)4.0F + (double)4.0F) / (double)3.0F));
        return original.call(instance, x, z);
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 0, ordinal = 10)
    )
    private int bwo_moreTreesInWoodsTheme(int original, @Local(ordinal = 0) Biome var6, @Share("var37") LocalDoubleRef var37) {
        if (this.theme.equals("Woods")) {
            int var38 = (int) (var37.get());
            if (var6 == Biome.DESERT || var6 == Biome.TUNDRA || var6 == Biome.PLAINS) {
                return 20 + (var38 + 5);
            } else if (var6 == Biome.SWAMPLAND || var6 == Biome.SHRUBLAND || var6 == Biome.SAVANNA) {
                return var38 + 5;
            }
        }

        return original;
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/biome/Biome;getRandomTreeFeature(Ljava/util/Random;)Lnet/minecraft/world/gen/feature/Feature;",
                    ordinal = 0
            )
    )
    private Feature bwo_infdevTrees(Biome instance, Random random, Operation<Feature> original) {
        if (this.worldType.equals("Infdev 415") || this.worldType.equals("Infdev 420")) {
            return ((BWOWorld) instance).bwo_getRandomTreeFeatureInfdev(random);
        }

        return original.call(instance, random);
    }

    @ModifyConstant(method = "decorate", constant = @Constant(intValue = 0, ordinal = 12))
    private int bwo_moreDandelionInParadiseTheme(int constant) {
        return this.theme.equals("Paradise") ? 8 : constant;
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/PlantPatchFeature;generate(Lnet/minecraft/world/World;Ljava/util/Random;III)Z",
                    ordinal = 1
            )

    )
    private boolean bwo_cancelRose(PlantPatchFeature instance, World world, Random random, int x, int y, int z, Operation<Boolean> original, @Local Biome var6, @Local(ordinal = 2) int var4, @Local(ordinal = 3) int var5) {
        if (this.theme.equals("Paradise")) {
            return false;
        }

        return original.call(instance, world, random, x, y, z);
    }

    @Inject(method = "decorate", at = @At("TAIL"))
    private void bwo_moreRoseInParadiseTheme(ChunkSource source, int x, int z, CallbackInfo ci, @Local Biome var6, @Local(ordinal = 2) int var4, @Local(ordinal = 3) int var5) {
        if (this.theme.equals("Paradise")) {
            byte var62 = 8;
            if (var6 == Biome.FOREST) {
                var62 += 2;
            }

            if (var6 == Biome.SEASONAL_FOREST) {
                var62 += 4;
            }

            if (var6 == Biome.TAIGA) {
                var62 += 2;
            }

            if (var6 == Biome.PLAINS) {
                var62 += 3;
            }

            for (int var120 = 0; var120 < var62; var120++) {
                int var79 = var4 + this.random.nextInt(16) + 8;
                int var88 = this.random.nextInt(128);
                int var99 = var5 + this.random.nextInt(16) + 8;
                (new PlantPatchFeature(Block.ROSE.id)).generate(this.world, this.random, var79, var88, var99);
            }
        }
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlock(IIII)Z",
                    ordinal = 0
            )
    )
    private boolean bwo_removeSnowInHellTheme(World world, int x, int y, int z, int blockId, Operation<Boolean> original) {
        if (this.theme.equals("Hell")) {
            return false;
        }

        return original.call(world, x, y, z, blockId);
    }

    @ModifyConstant(method = "decorate", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
    private double bwo_changeTempInWinterTheme2(double constant) {
        if (this.theme.equals("Winter")) {
            return 1.1D;
        }

        return constant;
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 10
            )
    )
    private int bwo_replaceFlowingWaterWithFlowingLavaInHellTheme(Block block, Operation<Integer> original) {
        return this.theme.equals("Hell") ? Block.FLOWING_LAVA.id : original.call(block);
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8),
            slice = @Slice(
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/gen/feature/LakeFeature;generate(Lnet/minecraft/world/World;Ljava/util/Random;III)Z",
                            ordinal = 0
                    )
            )
    )
    private int bwo_removeOffsetInIndevFiniteWorld(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8, ordinal = 3)
    )
    private int bwo_removeOffsetInIndevFiniteWorld2(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8, ordinal = 5)
    )
    private int bwo_removeOffsetInIndevFiniteWorld3(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 10
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 14
                    )
            )
    )
    private int bwo_removeOffsetInIndevFiniteWorld4(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 42
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/gen/feature/PlantPatchFeature;generate(Lnet/minecraft/world/World;Ljava/util/Random;III)Z",
                            ordinal = 2
                    )
            )
    )
    private int bwo_removeOffsetInIndevFiniteWorld5(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 63
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 77
                    )
            )
    )
    private int bwo_removeOffsetInIndevFiniteWorld6(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 78
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 81
                    )
            )
    )
    private int bwo_removeOffsetInIndevFiniteWorld7(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8, ordinal = 39)
    )
    private int bwo_removeOffsetInIndevFiniteWorld8(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "intValue=16",
                            ordinal = 58
                    )
            )
    )
    private int bwo_removeOffsetInIndevFiniteWorld9(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }
}