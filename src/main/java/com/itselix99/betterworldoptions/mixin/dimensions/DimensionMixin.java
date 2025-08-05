package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.BWOConfig;
import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

@Mixin(Dimension.class)
public class DimensionMixin implements StationDimension {
    @Shadow public BiomeSource biomeSource;
    @Shadow public boolean isNether;
    @Shadow public boolean evaporatesWater;
    @Shadow public World world;

    @Inject(method = "initBiomeSource", at = @At("HEAD"), cancellable = true)
    protected void initBiomeSource(CallbackInfo ci) {
        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();
        String theme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();
        String singleBiome = ((BWOProperties) this.world.getProperties()).bwo_getSingleBiome();

        if (worldType.equals("Flat") && !betaFeatures) {
            this.biomeSource = new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.5D);
        } else if (worldType.equals("Alpha 1.1.2_01") && !betaFeatures) {
            if (theme.equals("Winter")) {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.WinterAlpha, 0.0D, 0.5D);
            } else {
                this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Alpha, 1.0D, 0.5D);
            }
        } else if (worldType.equals("Infdev 611") && !betaFeatures) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
        } else if (worldType.equals("Infdev 420") && !betaFeatures) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
        } else if (worldType.equals("Infdev 415") && !betaFeatures) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.Infdev, 1.0D, 0.5D);
        } else if (worldType.equals("Early Infdev") && !betaFeatures) {
            this.biomeSource = new FixedBiomeSource(BetterWorldOptions.EarlyInfdev, 1.0D, 0.5D);
        } else if (worldType.equals("Indev 223")) {
            if (!betaFeatures) {
                switch (theme) {
                    case "Hell" -> this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevHell, 1.0D, 0.0D);
                    case "Normal" -> this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevNormal, 1.0D, 0.5D);
                    case "Paradise" -> this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevParadise, 1.0D, 0.5D);
                    case "Woods" -> this.biomeSource = new FixedBiomeSource(BetterWorldOptions.IndevWoods, 1.0D, 0.5D);
                }
            } else {
                switch (singleBiome) {
                    case "Rainforest" -> this.biomeSource = new FixedBiomeSource(Biome.RAINFOREST, 1.0D, 1.0D);
                    case "Swampland" -> this.biomeSource = new FixedBiomeSource(Biome.SWAMPLAND, 0.6D, 0.6D);
                    case "Seasonal Forest" -> this.biomeSource = new FixedBiomeSource(Biome.SEASONAL_FOREST, 1.0D, 0.8D);
                    case "Forest" -> this.biomeSource = new FixedBiomeSource(Biome.FOREST, 0.7D, 0.5D);
                    case "Savanna" -> this.biomeSource = new FixedBiomeSource(Biome.SAVANNA, 0.8D, 0.1D);
                    case "Shrubland" -> this.biomeSource = new FixedBiomeSource(Biome.SHRUBLAND, 0.6D, 0.3D);
                    case "Taiga" -> this.biomeSource = new FixedBiomeSource(Biome.TAIGA, 0.3D, 0.4D);
                    case "Desert" -> this.biomeSource = new FixedBiomeSource(Biome.DESERT, 1.0D, 0.1D);
                    case "Plains" -> this.biomeSource = new FixedBiomeSource(Biome.PLAINS, 1.0D, 0.4D);
                    case "Ice Desert" -> this.biomeSource = new FixedBiomeSource(Biome.ICE_DESERT, 0.0D, 0.1D);
                    case "Tundra" -> this.biomeSource = new FixedBiomeSource(Biome.TUNDRA, 0.0D, 1.0D);
                    case "All Biomes" -> this.biomeSource = new BiomeSource(this.world);
                }
            }
        } else if (!singleBiome.equals("All Biomes")) {
            this.biomeSource = new FixedBiomeSource(WorldSettings.World.getSingleBiome(), 0.0D, 0.5D);
        } else {
            this.biomeSource = new BiomeSource(this.world);
        }

        if (WorldSettings.World.getSingleBiome() == Biome.HELL) {
            this.isNether = true;
            this.evaporatesWater = true;
        }
        ci.cancel();
    }

    @ModifyReturnValue(method = "createChunkGenerator", at = @At("RETURN"))
    public ChunkSource createChunkGenerator(ChunkSource original) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (WorldSettings.World.getChunkGenerator() != null) {
            return WorldSettings.World.getChunkGenerator().newInstance(this.world, this.world.getSeed());
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "isValidSpawnPoint", at = @At("RETURN"))
    private boolean injectIsValidSpawnPoint(boolean original, int x, int z) {
        int var3 = this.world.getSpawnBlockId(x, z);

        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        String indevTheme = ((BWOProperties) this.world.getProperties()).bwo_getTheme();
        String betaTheme = ((BWOProperties) this.world.getProperties()).bwo_getSingleBiome();
        boolean betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();

        if (WorldSettings.World.getBlockToSpawnOn() != 0) {
            if (worldType.equals("Indev 223")) {
                if (!betaFeatures) {
                    if (indevTheme.equals("Hell")) {
                        return var3 == Block.DIRT.id;
                    }
                } else {
                    if (betaTheme.equals("Hell")) {
                        return var3 == Block.DIRT.id;
                    } else if (betaTheme.equals("Desert") || betaTheme.equals("Ice Desert")) {
                        return var3 == Block.SAND.id;
                    }
                }
            }

            return var3 == WorldSettings.World.getBlockToSpawnOn();
        } else {
            return original;
        }
    }

    @Override
    public int getHeight() {
        return BWOConfig.WORLD_CONFIG.worldHeightLimit.getIntValue();
    }
}
