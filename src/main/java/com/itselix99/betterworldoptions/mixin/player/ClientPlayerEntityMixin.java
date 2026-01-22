package com.itselix99.betterworldoptions.mixin.player;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public class ClientPlayerEntityMixin {
    @Shadow protected Minecraft minecraft;

    @ModifyArgs(
            method = "respawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;respawnPlayer(ZI)V"
            )
    )
    private void bwo_respawnInOthersDimensions(Args args) {
        BWOProperties bwoProperties = (BWOProperties) this.minecraft.world.getProperties();
        String worldType = bwoProperties.bwo_getWorldType();
        boolean skyDimension = bwoProperties.bwo_getBooleanOptionValue("SkyDimension", OptionType.WORLD_TYPE_OPTION);

        if (worldType.equals("Nether")) {
            args.set(1, -1);
        } else if (worldType.equals("Skylands") && skyDimension) {
            args.set(1, 1);
        } else if (worldType.equals("Aether")) {
            args.set(1, 2);
        }
    }
}