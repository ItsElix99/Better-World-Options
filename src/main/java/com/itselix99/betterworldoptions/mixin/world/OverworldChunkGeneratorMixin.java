package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.config.Config;
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
    @Shadow public OctavePerlinNoiseSampler forestNoise;
    @Unique private Generator ravine = new RavineWorldCarver();
    @Unique private String worldType;
    @Unique private String theme;
    @Unique private boolean infiniteWorld;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initBWOProperties(World world, long seed, CallbackInfo ci) {
        this.worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
        this.theme = ((BWOProperties) world.getProperties()).bwo_getTheme();
        this.infiniteWorld = ((BWOProperties) world.getProperties()).bwo_isInfiniteWorld();

        ((BWOWorld) this.world).bwo_setSnow(this.theme.equals("Winter"));
        ((BWOWorld) this.world).bwo_setPrecipitation(!this.theme.equals("Hell") && !this.theme.equals("Paradise"));
    }

    @ModifyVariable(
            method = "buildTerrain",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private int modifyChunkXForFarlands(int chunkX) {
        if (this.worldType.equals("Farlands")) {
            if (chunkX >= 8) {
                return chunkX + 784426;
            } else {
                return chunkX - 784426;
            }
        }
        return chunkX;
    }

    @WrapOperation(
            method = "buildTerrain",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 0
            )
    )
    private int modifylavaBlock1(Block block, Operation<Integer> original) {
        return this.theme.equals("Hell") ? Block.LAVA.id : original.call(block);
    }

    @ModifyConstant(method = "buildTerrain", constant = @Constant(doubleValue = 0.5D, ordinal = 0))
    private double winterThemeIce(double constant) {
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
                    ordinal = 1
            )
    )
    private int modifylavaBlock2(Block block, Operation<Integer> original) {
        return this.theme.equals("Hell") ? Block.LAVA.id : original.call(block);
    }

    @ModifyExpressionValue(
            method = "buildSurfaces",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/biome/Biome;topBlockId:B"
            )
    )
    private byte modifyTopBlockId(byte original) {
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
    private int modifySand1(Block block, Operation<Integer> original) {
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
    private int modifySand2(Block block, Operation<Integer> original) {
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
    private int modifyWater1(Block block, Operation<Integer> original) {
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
    private void ravineGeneration(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir, @Local byte[] var3) {
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
    private int modifyWater2(Block block, Operation<Integer> original) {
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
    private double captureVar37(OctavePerlinNoiseSampler instance, double x, double z, Operation<Double> original, @Share("var37") LocalDoubleRef var37) {
        var37.set(((original.call(instance, x, z) / (double)8.0F + this.random.nextDouble() * (double)4.0F + (double)4.0F) / (double)3.0F));
        return original.call(instance, x, z);
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 0, ordinal = 10)
    )
    private int woodsThemeTrees(int original, @Local(ordinal = 0) Biome var6, @Share("var37") LocalDoubleRef var37) {
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
    private Feature infdevTrees(Biome instance, Random random, Operation<Feature> original) {
        if (this.worldType.equals("Infdev 415") || this.worldType.equals("Infdev 420")) {
            return ((BWOWorld) instance).bwo_getRandomTreeFeatureInfdev(random);
        }

        return original.call(instance, random);
    }

    @ModifyConstant(method = "decorate", constant = @Constant(intValue = 0, ordinal = 12))
    private int paradiseTheme(int constant) {
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
    private boolean cancelRose(PlantPatchFeature instance, World world, Random random, int x, int y, int z, Operation<Boolean> original, @Local Biome var6, @Local(ordinal = 2) int var4, @Local(ordinal = 3) int var5) {
        if (this.theme.equals("Paradise")) {
            return false;
        }

        return original.call(instance, world, random, x, y, z);
    }

    @Inject(method = "decorate", at = @At("TAIL"))
    private void paradiseThemeRose(ChunkSource source, int x, int z, CallbackInfo ci, @Local Biome var6, @Local(ordinal = 2) int var4, @Local(ordinal = 3) int var5) {
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
    private boolean cancelSnow(World world, int x, int y, int z, int blockId, Operation<Boolean> original) {
        if (this.theme.equals("Hell")) {
            return false;
        }

        return original.call(world, x, y, z, blockId);
    }

    @ModifyConstant(method = "decorate", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
    private double winterThemeSnow(double constant) {
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
    private int modifyFlowingWater(Block block, Operation<Integer> original) {
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
    private int removeOffsetInIndevFiniteWorld(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8, ordinal = 3)
    )
    private int removeOffsetInIndevFiniteWorld2(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8, ordinal = 5)
    )
    private int removeOffsetInIndevFiniteWorld3(int original) {
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
    private int removeOffsetInIndevFiniteWorld4(int original) {
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
    private int removeOffsetInIndevFiniteWorld5(int original) {
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
    private int removeOffsetInIndevFiniteWorld6(int original) {
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
    private int removeOffsetInIndevFiniteWorld7(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }

    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 8, ordinal = 39)
    )
    private int removeOffsetInIndevFiniteWorld8(int original) {
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
    private int removeOffsetInIndevFiniteWorld9(int original) {
        if (this.worldType.equals("Indev 223") && !this.infiniteWorld) {
            return 0;
        }

        return original;
    }
}