package com.itselix99.betterworldoptions.mixin.player;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(ClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {

    @Shadow protected Minecraft minecraft;

    public ClientPlayerEntityMixin(World world) {
        super(world);
    }

    @ModifyArgs(
            method = "respawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;respawnPlayer(ZI)V"
            )
    )
    private void respawnInOthersDimensions(Args args) {
        String worldType = ((BWOProperties) this.minecraft.world.getProperties()).bwo_getWorldType();

        if (Objects.equals(worldType, "Nether")) {
            args.set(1, -1);
        } else if (Objects.equals(worldType, "Skylands")) {
            args.set(1, 1);
        } else if (Objects.equals(worldType, "Aether")) {
            args.set(1, 2);
        }
    }
}