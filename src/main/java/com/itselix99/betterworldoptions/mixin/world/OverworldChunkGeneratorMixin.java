package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.BWOConfig;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.Generator;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
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
    @Unique private Generator ravine = new RavineWorldCarver();
    @Unique private String worldType;
    @Unique private String theme;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initWorldTypeAndTheme(World world, long seed, CallbackInfo ci) {
        this.worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();
        this.theme = ((BWOProperties) world.getProperties()).bwo_getTheme();

        ((BWOWorld) this.world).bwo_setSnow(this.theme.equals("Winter"));
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
        if (BWOConfig.WORLD_CONFIG.ravineGeneration) {
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

    @ModifyExpressionValue(
            method = "decorate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/noise/OctavePerlinNoiseSampler;sample(DD)D",
                    shift = At.Shift.AFTER
            )
    )
    private double captureVar37(double original, @Share("var37") LocalIntRef var37) {
        var37.set((int) original);
        return original;
    }

    @ModifyConstant(method = "decorate", constant = @Constant(intValue = 0, ordinal = 10))
    private int woodsTheme(int original, @Local Biome var6 , @Share("var37") LocalIntRef var37) {
        if (this.theme.equals("Woods")) {
            if (var6 == Biome.DESERT || var6 == Biome.TUNDRA || var6 == Biome.PLAINS || var6 == Biome.SWAMPLAND || var6 == Biome.SHRUBLAND || var6 == Biome.SAVANNA) {
                return var37.get() + 25;
            }
        }

        return original;
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
    private void paradiseThemeRose(ChunkSource x, int z, int par3, CallbackInfo ci, @Local Biome var6, @Local(ordinal = 2) int var4, @Local(ordinal = 3) int var5) {
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
}