package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWONoise;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.carver.RavineWorldCarver;
import com.itselix99.betterworldoptions.world.chunk.BWOLimitChunk;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
import net.minecraft.world.gen.feature.PlantPatchFeature;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
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
    @Unique private String theme;
    @Unique private boolean finiteWorld;
    @Unique private String finiteWorldType;
    @Unique private int width;
    @Unique private int length;
    @Unique private boolean farlands;
    @Unique protected String farlandsShape;
    @Unique protected int farlandsDistance;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_initBWOProperties(World world, long seed, CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.theme = bwoProperties.bwo_getTheme();
        this.finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteWorldType = bwoProperties.bwo_getStringOptionValue("FiniteWorldType", OptionType.GENERAL_OPTION);
        this.width = bwoProperties.bwo_getIntOptionValue("Width", OptionType.GENERAL_OPTION);
        this.length = bwoProperties.bwo_getIntOptionValue("Length", OptionType.GENERAL_OPTION);
        this.farlands = bwoProperties.bwo_getBooleanOptionValue("Farlands", OptionType.GENERAL_OPTION);
        this.farlandsShape = bwoProperties.bwo_getStringOptionValue("FarlandsShape", OptionType.GENERAL_OPTION);
        this.farlandsDistance = bwoProperties.bwo_getIntOptionValue("FarlandsDistance", OptionType.GENERAL_OPTION) / 2;

        if (this.finiteWorldType.equals("MCPE")) {
            BWOChunkGenerator.setSizeLimits(0, this.width, 0, this.length);
        } else {
            BWOChunkGenerator.setSizeLimits(-this.width / 2, this.width / 2, -this.length / 2, this.length / 2);
        }
    }

    @ModifyVariable(
            method = "buildTerrain",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private int bwo_setFarlandsChunkX(int chunkX) {
        if (this.farlands) {
            if (this.farlandsShape.equals("Linear")) {
                if (chunkX > this.farlandsDistance) chunkX += 784426;
                if (chunkX < -this.farlandsDistance) chunkX -= 784426;
            } else if (this.farlandsShape.equals("Square")) {
                if (chunkX > this.farlandsDistance) chunkX += 784426;
                if (chunkX < -this.farlandsDistance) chunkX -= 784426;
            }
        }

        return chunkX;
    }

    @ModifyVariable(
            method = "buildTerrain",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true
    )
    private int bwo_setFarlandsChunkZ(int chunkZ) {
        if (this.farlands) {
            if (this.farlandsShape.equals("Square")) {
                if (chunkZ > this.farlandsDistance) chunkZ += 784426;
                if (chunkZ < -this.farlandsDistance) chunkZ -= 784426;
            }
        }

        return chunkZ;
    }

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
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
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
                    ordinal = 1,
                    opcode = Opcodes.GETFIELD
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
                    ordinal = 6,
                    opcode = Opcodes.GETFIELD
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
                    target = "Lnet/minecraft/world/biome/Biome;topBlockId:B",
                    opcode = Opcodes.GETFIELD
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
                    ordinal = 4,
                    opcode = Opcodes.GETFIELD
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
                    ordinal = 5,
                    opcode = Opcodes.GETFIELD
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
                    ordinal = 6,
                    opcode = Opcodes.GETFIELD
            )
    )
    private int bwo_replaceWaterWithLavaInHellTheme2(Block block, Operation<Integer> original) {
        return (byte) (this.theme.equals("Hell") ? Block.LAVA.id : original.call(block));
    }

    @Inject(
            method = "buildSurfaces",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=0",
                    ordinal = 4
            )
    )
    private void bwo_lceFiniteWorldLimit(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, CallbackInfo ci, @Local (name = "var8") int var8, @Local (name = "var9") int var9, @Local (name = "var10") Biome var10, @Local (name = "var17") int var17) {
        if (this.finiteWorld && this.finiteWorldType.equals("LCE")) {
            double x2 = (chunkX << 4) + var8;
            double z2 = (chunkZ << 4) + var9;
            int index = (var8 * 16 + var9) * Config.BWOConfig.world.worldHeightLimit.getIntValue() + var17;
            double minX = -this.width / 2.0D;
            double maxX = this.width / 2.0D - 1.0D;
            double minZ = -this.length / 2.0D;
            double maxZ = this.length / 2.0D - 1.0D;

            boolean limit = (x2 == minX && z2 >= minZ && z2 <= maxZ) || (x2 == maxX && z2 >= minZ && z2 <= maxZ) || (z2 == minZ && x2 >= minX && x2 <= maxX) || (z2 == maxZ && x2 >= minX && x2 <= maxX);
            if (var17 <= 55) {
                if (limit) {
                    if (var17 >= 53) {
                        blocks[index] = var10.soilBlockId;
                    } else {
                        blocks[index] = (byte) Block.STONE.id;
                    }
                }
            }
        }
    }


    @ModifyConstant(
            method = "buildSurfaces",
            constant = @Constant(intValue = -1, ordinal = 0)
    )
    private int bwo_lceFiniteWorldLimit2(int constant, @Local (ordinal = 0, argsOnly = true) int chunkX, @Local (ordinal = 1, argsOnly = true) int chunkZ, @Local (name = "var8") int var8, @Local (name = "var9") int var9) {
        if (this.finiteWorld && this.finiteWorldType.equals("LCE")) {
            double x2 = (chunkX << 4) + var8;
            double z2 = (chunkZ << 4) + var9;
            double minX = -this.width / 2.0D;
            double maxX = this.width / 2.0D - 1.0D;
            double minZ = -this.length / 2.0D;
            double maxZ = this.length / 2.0D - 1.0D;

            boolean limit = (x2 == minX && z2 >= minZ && z2 <= maxZ) || (x2 == maxX && z2 >= minZ && z2 <= maxZ) || (z2 == minZ && x2 >= minX && x2 <= maxX) || (z2 == maxZ && x2 >= minX && x2 <= maxX);
            if (limit) {
                return -2;
            }

        }

        return constant;
    }

    @ModifyConstant(
            method = "buildSurfaces",
            constant = @Constant(intValue = -1, ordinal = 1)
    )
    private int bwo_lceFiniteWorldLimit3(int constant, @Local (ordinal = 0, argsOnly = true) int chunkX, @Local (ordinal = 1, argsOnly = true) int chunkZ, @Local (name = "var8") int var8, @Local (name = "var9") int var9, @Local (name = "var17") int var17) {
        if (this.finiteWorld && this.finiteWorldType.equals("LCE")) {
            double x2 = (chunkX << 4) + var8;
            double z2 = (chunkZ << 4) + var9;
            double minX = -this.width / 2.0D;
            double maxX = this.width / 2.0D - 1.0D;
            double minZ = -this.length / 2.0D;
            double maxZ = this.length / 2.0D - 1.0D;

            boolean limit = (x2 == minX && z2 >= minZ && z2 <= maxZ) || (x2 == maxX && z2 >= minZ && z2 <= maxZ) || (z2 == minZ && x2 >= minX && x2 <= maxX) || (z2 == maxZ && x2 >= minX && x2 <= maxX);
            if (limit && var17 <= 55) {
                return -2;
            }

        }

        return constant;
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

    @ModifyReturnValue(method = "getChunk", at = @At(value = "RETURN"))
    private Chunk bwo_finiteWorld(Chunk original, @Local(ordinal = 0, argsOnly = true) int chunkX,  @Local(ordinal = 1, argsOnly = true) int chunkZ, @Local (ordinal = 0) byte[] blocks) {
        if (this.finiteWorld) {
            int blockX = chunkX * 16;
            int blockZ = chunkZ * 16;

            int minX;
            int maxX;
            int minZ;
            int maxZ;

            if (this.finiteWorldType.equals("MCPE")) {
                minX = 0;
                maxX = this.width;
                minZ = 0;
                maxZ = this.length;
            } else {
                minX = -this.width / 2;
                maxX = this.width / 2;
                minZ = -this.length / 2;
                maxZ = this.length / 2;
            }

            if (blockX < minX || blockX >= maxX || blockZ < minZ || blockZ >= maxZ) {
                BWOLimitChunk bwoLimitChunk = new BWOLimitChunk(this.world, chunkX, chunkZ, this.finiteWorldType);
                bwoLimitChunk.fromLegacy(blocks);
                bwoLimitChunk.populateHeightMap();
                return bwoLimitChunk;
            }
        }

        return original;
    }

    @Inject(
            method = "generateHeightMap",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=2",
                    ordinal = 1
            )
    )
    private void bwo_getIslandOffset(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ, CallbackInfoReturnable<double[]> cir, @Local(ordinal = 9) int var17, @Local(ordinal = 11) int var19, @Share("islandOffset") LocalDoubleRef islandOffset) {
        double worldEdgeFactor = 1.0D;

        int nx = (x + var17) * 4;
        int nz = (z + var19) * 4;

        double dx = Math.abs(nx);
        double dz = Math.abs(nz);

        int halfSizeX = this.width / 2;
        int halfSizeZ = this.length / 2;

        double limitX = halfSizeX + 18.0D;
        double limitZ = halfSizeZ + 18.0D;

        if (halfSizeX == 32) limitX += 12.0D;
        if (halfSizeZ == 32) limitZ += 12.0D;

        double falloff = 50.0D;

        if (this.finiteWorldType.equals("LCE")) {
            double edgeX = limitX - dx;
            double edgeZ = limitZ - dz;

            double factorX = edgeX / falloff;
            double factorZ = edgeZ / falloff;

            factorX = Math.max(0.0D, Math.min(1.0D, factorX));
            factorZ = Math.max(0.0D, Math.min(1.0D, factorZ));

            worldEdgeFactor = Math.min(factorX, factorZ);
        } else if (this.finiteWorldType.equals("Island")) {
            falloff = 100.0D;

            double nxNorm = dx / limitX;
            double nzNorm = dz / limitZ;

            double radial = Math.sqrt(nxNorm * nxNorm + nzNorm * nzNorm);
            double falloffRadial = falloff / (Math.sqrt(limitX * limitX + limitZ * limitZ));

            double start = 1.0D - falloffRadial;

            double t = (radial - start) / (1.0D - start);
            t = Math.max(0.0D, Math.min(1.0D, t));

            worldEdgeFactor = 1.0D - t;
        }

        islandOffset.set(-200.0D * (1.0D - worldEdgeFactor));
    }

    @ModifyVariable(
            method = "generateHeightMap",
            at = @At(value = "STORE"),
            name = "var34",
            ordinal = 1
    )
    private double bwo_applyIslandOffset(double original, @Share("islandOffset") LocalDoubleRef islandOffset) {
        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
            return original + islandOffset.get();
        }

        return original;
    }

    @ModifyVariable(
            method = "generateHeightMap",
            at = @At(value = "STORE"),
            name = "var34",
            ordinal = 2
    )
    private double bwo_applyIslandOffset2(double original, @Share("islandOffset") LocalDoubleRef islandOffset) {
        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
            return original + islandOffset.get();
        }

        return original;
    }

    @ModifyVariable(
            method = "generateHeightMap",
            at = @At(value = "STORE"),
            ordinal = 10
    )
    private double bwo_applyIslandOffset3(double original, @Share("islandOffset") LocalDoubleRef islandOffset) {
        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
            return original + islandOffset.get();
        }

        return original;
    }

    @ModifyVariable(
            method = "generateHeightMap",
            at = @At(value = "STORE"),
            ordinal = 11
    )
    private double bwo_applyIslandOffset4(double original, @Share("islandOffset") LocalDoubleRef islandOffset) {
        if (this.finiteWorld && !this.finiteWorldType.equals("MCPE")){
            return original + islandOffset.get();
        }

        return original;
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
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

    @ModifyConstant(method = "decorate", constant = @Constant(intValue = 0, ordinal = 12))
    private int bwo_moreDandelionInParadiseTheme(int constant) {
        return this.theme.equals("Paradise") ? 24 : constant;
    }

    @Inject(method = "decorate", at = @At("TAIL"))
    private void bwo_moreRoseInParadiseTheme(ChunkSource source, int x, int z, CallbackInfo ci, @Local Biome var6, @Local(ordinal = 2) int var4, @Local(ordinal = 3) int var5) {
        if (this.theme.equals("Paradise")) {
            byte var62 = 24;
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

    @ModifyConstant(method = "decorate", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
    private double bwo_changeTempInHellOrWinterTheme(double constant) {
        if (this.theme.equals("Winter")) {
            return 1.1D;
        } else if (this.theme.equals("Hell")) {
            return -1.1D;
        }

        return constant;
    }

    @WrapOperation(
            method = "decorate",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I",
                    ordinal = 10,
                    opcode = Opcodes.GETFIELD
            )
    )
    private int bwo_replaceFlowingWaterWithFlowingLavaInHellTheme(Block block, Operation<Integer> original) {
        return this.theme.equals("Hell") ? Block.FLOWING_LAVA.id : original.call(block);
    }
}