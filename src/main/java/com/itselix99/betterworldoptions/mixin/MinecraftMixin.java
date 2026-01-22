package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.options.storage.StringOptionStorage;
import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public World world;
    @Shadow public ClientPlayerEntity player;

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;difficulty:I",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            )
    )
    private void bwo_hardcoreDifficulty(World world, int difficulty, Operation<Void> original) {
        if (((BWOProperties) world.getProperties()).bwo_isHardcore()) {
            original.call(world, 3);
        } else {
            original.call(world, difficulty);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwo_notChangeGamemodeInHardcore(CallbackInfo ci) {
        if (this.world != null && ((BWOProperties) this.world.getProperties()).bwo_isHardcore() && this.player != null && CompatMods.BHCreativeLoaded()) {
            this.player.creative_setCreative(false);
        }
    }

    @WrapOperation(
            method = "respawnPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/Dimension;hasWorldSpawn()Z"
            )
    )
    private boolean bwo_respawnPlayerInOtherDimensions(Dimension instance, Operation<Boolean> original) {
        String worldType = ((BWOProperties) this.world.getProperties()).bwo_getWorldType();
        if (worldType.equals("Aether")) {
            return true;
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(
            method = "startGame",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;J)Lnet/minecraft/world/World;"
            )
    )
    private World bwo_startGameInOtherDimensions(WorldStorage storage, String name, long seed, Operation<World> original) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

        String worldType = ((StringOptionStorage) bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION)).value;
        if (worldType.equals("Nether") && storage.loadProperties() == null) {
            return new World(storage, name, seed, Dimension.fromId(-1));
        } else if (worldType.equals("Skylands") && storage.loadProperties() == null) {
            return new World(storage, name, seed, Dimension.fromId(1));
        } else if (worldType.equals("Aether") && storage.loadProperties() == null) {
            return new World(storage, name, seed, CompatMods.startWorldInAether());
        }

        return original.call(storage, name, seed);
    }

    @ModifyArgs(
            method = "changeDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ClientPlayerEntity;setPositionAndAnglesKeepPrevAngles(DDDFF)V",
                    ordinal = 1
            )
    )
    private void bwo_fixSpawnInFiniteWorld(Args args) {
        BWOProperties bwoProperties = (BWOProperties) this.world.getProperties();
        boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);

        if (finiteWorld) {
            double sizeX = (double) bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION) / 2;
            double sizeZ = (double) bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION) / 2;
            sizeX = this.world.random.nextDouble(0, sizeX);
            sizeZ = this.world.random.nextDouble(0, sizeZ);

            args.set(0, sizeX);
            args.set(2, sizeZ);
        }
    }

    @ModifyArgs(
            method = "changeDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ClientPlayerEntity;setPositionAndAnglesKeepPrevAngles(DDDFF)V",
                    ordinal = 2
            )
    )
    private void bwo_fixSpawnInFiniteWorld2(Args args) {
        BWOProperties bwoProperties = (BWOProperties) this.world.getProperties();
        boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);

        if (finiteWorld && this.player.dimensionId == 0) {
            double sizeX = (double) bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION) / 2;
            double sizeZ = (double) bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION) / 2;
            sizeX = this.world.random.nextDouble(0, sizeX);
            sizeZ = this.world.random.nextDouble(0, sizeZ);

            args.set(0, sizeX);
            args.set(2, sizeZ);
        }
    }
}