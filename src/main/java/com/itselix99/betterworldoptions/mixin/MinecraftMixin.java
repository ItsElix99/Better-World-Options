package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.util.Objects;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public World world;
    @Shadow public GameOptions options;

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;isRemote:Z"
            )
    )
    private void hardcoreDifficulty(CallbackInfo ci) {
        if (((BWOProperties) this.world.getProperties()).bwo_getHardcore()) {
            this.world.difficulty = 3;
        } else {
            this.world.difficulty = this.options.difficulty;
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
        if (Objects.equals(WorldSettings.name, "Nether") && storage.loadProperties() == null) {
            return new World(storage, name, seed, Dimension.fromId(-1));
        } else if (Objects.equals(WorldSettings.name, "Skylands") && storage.loadProperties() == null) {
            return new World(storage, name, seed, Dimension.fromId(1));
        } else if (Objects.equals(WorldSettings.name, "Aether") && storage.loadProperties() == null) {
            try {
                Class<?> aether = Class.forName("com.matthewperiut.aether.gen.dim.AetherDimension");

                Constructor<?> constructor = aether.getConstructor(int.class);
                Object AetherDimension = constructor.newInstance(2);

                if (AetherDimension instanceof Dimension) {
                    return new World(storage, name, seed, (Dimension) AetherDimension);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else return original.call(storage, name, seed);
    }
}