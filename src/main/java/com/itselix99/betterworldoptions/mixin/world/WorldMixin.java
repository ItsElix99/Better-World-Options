package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.BiomeProperties;
import com.itselix99.betterworldoptions.api.chunk.FiniteChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.mixin.chunk.ChunkGeneratorAccessor;
import com.itselix99.betterworldoptions.mixin.chunk.ServerChunkGeneratorAccessor;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.source.AdvUpdBiomeSource;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.feature.InfiniteIndevFeatures;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

@Mixin(World.class)
public abstract class WorldMixin implements BWOWorld, StationFlatteningWorld {
    @Shadow public Random random;
    @Shadow @Final public Dimension dimension;
    @Shadow public int difficulty;
    @Shadow protected WorldProperties properties;
    @Shadow protected ChunkSource chunkSource;
    @Shadow public boolean eventProcessingEnabled;

    @Unique private WorldType worldType;
    @Unique private Theme theme;
    @Unique private BiomeProperties biomeProperties;

    @Shadow public abstract int getBlockId(int x, int y, int z);
    @Shadow public abstract WorldProperties getProperties();
    @Shadow public abstract Material getMaterial(int x, int y, int z);
    @Shadow public abstract int getTopSolidBlockY(int x, int z);
    @Shadow public abstract Chunk getChunkFromPos(int x, int z);
    @Shadow public abstract BiomeSource method_1781();
    @Shadow public abstract long getSeed();

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/Dimension;setWorld(Lnet/minecraft/world/World;)V"
            )
    )
    private void bwo_init(CallbackInfo ci) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BWOProperties bwoProperties = (BWOProperties) this.properties;
        this.worldType = WorldType.getWorldTypeById(Identifier.of(bwoProperties.bwo_getWorldType()));
        this.theme = Theme.getThemeById(Identifier.of(bwoProperties.bwo_getTheme()));
        this.biomeProperties = this.worldType.getBiomeProperties().getClass().getDeclaredConstructor().newInstance();

        BiomeProperties.resetDefaultSnowAndPrecipitation();

        boolean oldFeatures =  bwoProperties.bwo_isOldFeatures();
        OldFeaturesProperties oldFeaturesProperties = this.worldType.getOldFeaturesProperties();

        if (oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome != null) {
            Biome oldBiome = oldFeaturesProperties.oldBiome;

            oldBiome.setSnow(this.theme.isCold());
            oldBiome.setPrecipitation(this.theme.allowRain());
            oldBiome.setFogColor(this.theme.getFogColor() != 1 ? this.theme.getFogColor() : oldFeaturesProperties.defaultFogColor.getColor(null, 0, 0));
        } else {
            if (this.theme.isCold()) {
                this.biomeProperties.getAllBiomes().forEach(biome -> biome.setSnow(true));
                this.biomeProperties.getAllBiomes().forEach(biome -> biome.setPrecipitation(this.biomeProperties.getDefaultPrecipitation(this.biomeProperties.getId(), biome)));
            } else if (!this.theme.allowRain()) {
                this.biomeProperties.getAllBiomes().forEach(biome -> biome.setSnow(this.theme.isHot() ? false : this.biomeProperties.getDefaultSnow(this.biomeProperties.getId(), biome)));
                this.biomeProperties.getAllBiomes().forEach(biome -> biome.setPrecipitation(false));
            } else {
                this.biomeProperties.getAllBiomes().forEach(biome -> biome.setSnow(this.biomeProperties.getDefaultSnow(this.biomeProperties.getId(), biome)));
                this.biomeProperties.getAllBiomes().forEach(biome -> biome.setPrecipitation(this.biomeProperties.getDefaultPrecipitation(this.biomeProperties.getId(), biome)));
            }
        }

    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V"
            },
            at = @At("TAIL")
    )
    private void bwo_setOldTextures(CallbackInfo ci) {
        BWOWorldPropertiesStorage.getInstance().setOldTextures(this.dimension.id == 0 && ((BWOProperties) this.getProperties()).bwo_isOldFeatures());
    }

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V"
            },
            at = @At("HEAD")
    )
    private static void bwo_initDimensionWorldTypes(CallbackInfo ci) {
        BWOWorldPropertiesStorage.initDimensionWorldTypes();
    }

    @Inject(method = "initializeSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void bwo_initializeSpawnPoint(CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) properties;

        boolean finiteWorld = bwoProperties.bwo_getOptionValue("FiniteWorld", OptionType.GENERAL_OPTION, false);
        String finiteWorldType = bwoProperties.bwo_getOptionValue("FiniteWorldType", OptionType.GENERAL_OPTION, "");
        int width = bwoProperties.bwo_getOptionValue("Width", OptionType.GENERAL_OPTION, 0);
        int length = bwoProperties.bwo_getOptionValue("Length", OptionType.GENERAL_OPTION, 0);

        boolean farlands = bwoProperties.bwo_getOptionValue("Farlands", OptionType.GENERAL_OPTION, false);
        String farlandsShape = bwoProperties.bwo_getOptionValue("FarlandsShape", OptionType.GENERAL_OPTION, "");
        int farlandsDistance = bwoProperties.bwo_getOptionValue("FarlandsDistance", OptionType.GENERAL_OPTION, 0) / 2;

        boolean generateIndevHouse = bwoProperties.bwo_getOptionValue("GenerateIndevHouse", OptionType.WORLD_TYPE_OPTION, false);

        if (this.worldType.getId().equals(BetterWorldOptions.NAMESPACE.id("indev_20100223"))) {
            this.eventProcessingEnabled = true;
            if (this.bwo_getChunkGenerator() instanceof FiniteChunkGenerator finiteChunkGenerator && finiteWorld) {
                finiteChunkGenerator.setCurrentStage("Spawning");
            }

            boolean isValidSpawnArea = false;
            int attempts = 0;
            int var1 = 0;
            int var2 = 64;
            int var3 = 0;

            while (!isValidSpawnArea) {
                if (!finiteWorld) {
                    var1 += this.random.nextInt(64) - this.random.nextInt(64);
                    var3 += this.random.nextInt(64) - this.random.nextInt(64);
                } else {
                    ++attempts;
                    var1 = this.random.nextInt(width / 2) + width / 4;
                    var3 = this.random.nextInt(length / 2) + length / 4;
                }

                var2 = this.getTopSolidBlockY(var1, var3);

                if(attempts >= 1000000) {
                    break;
                }

                if (!generateIndevHouse && this.dimension.isValidSpawnPoint(var1, var3) || this.bwo_isValidSpawnArea(var1, var2, var3)) {
                    isValidSpawnArea = true;
                }
            }

            this.properties.setSpawn(var1, var2 - 1, var3);
            this.eventProcessingEnabled = false;
            if (generateIndevHouse && isValidSpawnArea) {
                InfiniteIndevFeatures.placeSpawnBuilding(World.class.cast(this));
            }
            ci.cancel();
        } else if (farlands) {
            this.eventProcessingEnabled = true;
            int var1 = 0;
            int var2 = 64;

            int min = -farlandsDistance * 16;
            int max = (farlandsDistance * 16) + 15;

            int var3;
            for(var3 = 0; !this.dimension.isValidSpawnPoint(var1, var3); var3 += this.random.nextInt(64) - this.random.nextInt(64)) {
                var1 += this.random.nextInt(64) - this.random.nextInt(64);

                if (farlandsShape.equals("Linear")) {
                    var1 = Math.max(min, Math.min(max, var1));
                } else if (farlandsShape.equals("Square")) {
                    var1 = Math.max(min, Math.min(max, var1));
                    var3 = Math.max(min, Math.min(max, var3));
                }
            }

            this.properties.setSpawn(var1, var2, var3);
            this.eventProcessingEnabled = false;
            ci.cancel();
        } else if (finiteWorld && finiteWorldType.equals("MCPE")) {
            this.eventProcessingEnabled = true;
            int var1 = width / 2;
            int var2 = 64;
            int var3;

            for (var3 = length / 2; !this.dimension.isValidSpawnPoint(var1, var3); var3 += this.random.nextInt(64) - this.random.nextInt(64)) {
                var1 += this.random.nextInt(64) - this.random.nextInt(64);
            }

            this.properties.setSpawn(var1, var2, var3);
            this.eventProcessingEnabled = false;
            ci.cancel();
        } else if (this.method_1781() instanceof AdvUpdBiomeSource advUpdBiomeSource) {
            this.eventProcessingEnabled = true;
            List<Biome> biomesList = advUpdBiomeSource.getBiomesForSpawnPoint();
            Random random = new Random(this.getSeed());
            BlockPos blockPos = advUpdBiomeSource.bwo_findBiome(0, 0, 256, biomesList, random);
            int x = 0;
            int y = 64;
            int z = 0;
            if (blockPos != null) {
                x = blockPos.x;
                z = blockPos.z;
            } else {
                System.out.println("Unable to find spawn biome");
            }

            int var1 = 0;

            while(!this.dimension.isValidSpawnPoint(x, z)) {
                x += random.nextInt(64) - random.nextInt(64);
                z += random.nextInt(64) - random.nextInt(64);
                ++var1;
                if (var1 == 1000) {
                    break;
                }
            }

            this.properties.setSpawn(x, y, z);
            this.eventProcessingEnabled = false;
            ci.cancel();
        }
    }

    @Unique
    private boolean bwo_isValidSpawnArea(int centerX, int centerY, int centerZ) {
        for (int x = centerX - 3; x <= centerX + 3; x++) {
            for (int y = centerY; y <= centerY + 3; y++) {
                for (int z = centerZ - 3; z <= centerZ + 3; z++) {
                    if (this.getMaterial(x, y, z).isSolid()) {
                        return false;
                    }
                }
            }
        }

        int floorY = centerY - 1;
        for (int x = centerX - 3; x <= centerX + 3; x++) {
            for (int z = centerZ - 5; z <= centerZ + 3; z++) {
                if (!Block.BLOCKS_OPAQUE[this.getBlockId(x, floorY, z)]) {
                    return false;
                }
            }
        }

        return true;
    }

    @ModifyReturnValue(
            method = "getAmbientDarkness",
            at = @At("RETURN")
    )
    private int bwo_modifyAmbientDarkness(int original) {
        if (this.dimension.id == 0) {
            return this.theme.changeAmbientDarkness(original);
        }

        return original;
    }

    @Environment(EnvType.CLIENT)
    @WrapOperation(
            method = "getCloudColor(F)Lnet/minecraft/util/math/Vec3d;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;worldTimeMask:J"
            )
    )
    private long bwo_themeCloudsColor(World world, Operation<Long> original) {
        if (world.dimension.id == 0) {
            return this.theme.getCloudsColor();
        }

        return original.call(world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void bwo_tickHardcoreAndBiomeCache(CallbackInfo ci) {
        if (((BWOProperties) this.getProperties()).bwo_isHardcore() && this.difficulty < 3) {
            this.difficulty = 3;
        }

        if (this.method_1781() instanceof AdvUpdBiomeSource advUpdBiomeSource) {
            advUpdBiomeSource.tick();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwo_tickTheme(CallbackInfo ci) {
        this.theme.tick(World.class.cast(this));
    }

    @WrapOperation(
            method = "manageChunkUpdatesAndEvents",
            at = @At
                    (
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/biome/Biome;canSnow()Z"
                    )
    )
    private boolean bwo_generateSnowAndIce(Biome biome, Operation<Boolean> original) {
        if (this.theme.isCold()) {
            return true;
        }

        return original.call(biome);
    }

    @WrapOperation(
            method = "manageChunkUpdatesAndEvents",
            at = @At
                    (
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/World;isRaining()Z",
                            ordinal = 1
                    )
    )
    private boolean bwo_generateSnowInColdTheme(World world, Operation<Boolean> original) {
        if (this.theme.isCold()) {
            return true;
        }

        return original.call(world);
    }

    @WrapOperation(
            method = "manageChunkUpdatesAndEvents",
            at = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/World;setBlock(IIII)Z"
                    ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isRaining()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onTick(Lnet/minecraft/world/World;IIILjava/util/Random;)V")
            )
    )
    private boolean bwo_advUpdTemperature(World instance, int x, int y, int z, int blockId, Operation<Boolean> original) {
        if (this.method_1781() instanceof AdvUpdBiomeSource advUpdBiomeSource) {
            float temperature = (float) advUpdBiomeSource.getTemperature(x, z);

            if (temperature > 0.15F) {
                return false;
            }
        }

        return original.call(instance, x, y, z, blockId);
    }

    @ModifyReturnValue(method = "getRainGradient", at = @At("RETURN"))
    private float bwo_noRainGradient(float original) {
        if (this.dimension.id == 0) {
            if (!this.theme.allowRain()) {
                return 0.0F;
            }
        }

        return original;
    }

    @ModifyReturnValue(method = "getThunderGradient", at = @At("RETURN"))
    private float bwo_noThunderGradient(float original) {
        if (this.dimension.id == 0) {
            if (!this.theme.allowRain()) {
                return 0.0F;
            }
        }

        return original;
    }

    public Random bwo_setRandomSeed(int x, int z, int seed) {
        long var1 = (long)x * 341873128712L + (long)z * 132897987541L + this.getProperties().getSeed() + (long)seed;
        this.random.setSeed(var1);
        return this.random;
    }

    public int bwo_getSurfaceHeight(int x, int z) {
        Chunk var1 = this.getChunkFromPos(x, z);
        int var2 = this.getTopY() - 1;
        x &= 15;

        for(int var3 = z & 15; var2 > this.getBottomY(); --var2) {
            int var4 = var1.getBlockId(x, var2, var3);
            if (var4 != 0 && Block.BLOCKS[var4].material.blocksMovement() && Block.BLOCKS[var4].material != Material.LEAVES) {
                return var2 + 1;
            }
        }

        return -1;
    }

    public ChunkSource bwo_getChunkGenerator() {
        if (FabricLoaderImpl.INSTANCE.getEnvironmentType() == EnvType.CLIENT) {
            return ((ChunkGeneratorAccessor) this.chunkSource).getChunkGenerator();
        } else {
            return ((ServerChunkGeneratorAccessor) this.chunkSource).getChunkGenerator();
        }
    }

    public WorldType bwo_getWorldType() {
        return this.worldType;
    }

    public Theme bwo_getTheme() {
        return this.theme;
    }

    public BiomeProperties bwo_getBiomeProperties() {
        return this.biomeProperties;
    }

    public int bwo_getSeaLevel() {
        return this.worldType.getSeaLevel();
    }
}


