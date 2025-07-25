package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.itselix99.betterworldoptions.world.worldtypes.indev223.feature.IndevFeatures;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public World world;
    @Shadow public TextureManager textureManager;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void setGrassColor(CallbackInfo ci) {
        if (this.world != null) {
            String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
            boolean betaFeatures = ((BWOProperties) this.world.getProperties()).bwo_getBetaFeatures();

            if (WorldSettings.GameMode.isBetaFeaturesWorldTypes(worldType) && !betaFeatures && !WorldSettings.GameMode.isBetaFeaturesTextures()) {
                GrassColors.setColorMap(this.textureManager.getColors("/assets/betterworldoptions/stationapi/textures/misc/grasscolor.png"));
                FoliageColors.setColorMap(this.textureManager.getColors("/assets/betterworldoptions/stationapi/textures/misc/foliagecolor.png"));
            } else {
                GrassColors.setColorMap(this.textureManager.getColors("/misc/grasscolor.png"));
                FoliageColors.setColorMap(this.textureManager.getColors("/misc/foliagecolor.png"));
            }
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;difficulty:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void hardcoreDifficulty(World instance, int difficulty, Operation<Void> original) {
        if (((BWOProperties) this.world.getProperties()).bwo_getHardcore()) {
            original.call(instance, 3);
        } else {
            original.call(instance, difficulty);
        }
    }

    @WrapOperation(
            method = "respawnPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/Dimension;hasWorldSpawn()Z"
            )
    )
    private boolean respawnPlayerInOtherDimensions(Dimension instance, Operation<Boolean> original) {
        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        if (Objects.equals(worldType, "Aether")) {
            return true;
        } else {
            return original.call(instance);
        }
    }

    @Inject(
            method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerEntity;)V",
            at = @At(
                    value = "TAIL"
            )
    )
    private void indevSpawnBuilding(World world, String message, PlayerEntity player, CallbackInfo ci) {
        if (world != null && Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Indev 223") && ((BWOProperties) world.getProperties()).bwo_isGenerateIndevHouse()) {
            IndevFeatures.placeSpawnBuilding(world);
        }
    }

    @Inject(method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("TAIL"))
    private void dimensionBetaFeaturesTextures(World world, String message, PlayerEntity player, CallbackInfo ci) {
        if (world != null && !(world.dimension.id == 0 && !((BWOProperties) world.getProperties()).bwo_getBetaFeatures())) {
            WorldSettings.GameMode.setBetaFeaturesTextures(true);
        } else {
            WorldSettings.GameMode.setBetaFeaturesTextures(false);
        }
    }

    @WrapOperation(
            method = "startGame",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;J)Lnet/minecraft/world/World;"
            )
    )
    private World startGameInOtherDimensions(WorldStorage storage, String name, long seed, Operation<World> original) {
        if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Nether") && storage.loadProperties() == null) {
            return new World(storage, name, seed, Dimension.fromId(-1));
        } else if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Skylands") && storage.loadProperties() == null) {
            return new World(storage, name, seed, Dimension.fromId(1));
        } else if (Objects.equals(WorldSettings.World.getWorldTypeName(), "Aether") && storage.loadProperties() == null) {
            return new World(storage, name, seed, CompatMods.startWorldInAether());
        } else return original.call(storage, name, seed);
    }
}