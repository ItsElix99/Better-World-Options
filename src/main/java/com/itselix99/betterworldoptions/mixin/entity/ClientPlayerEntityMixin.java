package com.itselix99.betterworldoptions.mixin.entity;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
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
    private void bwo_respawnInOtherDimensions(Args args) {
        BWOProperties bwoProperties = (BWOProperties) this.minecraft.world.getProperties();
        WorldType worldType = ((BWOWorld) this.minecraft.world).bwo_getWorldType();
        boolean skyDimension = bwoProperties.bwo_getOptionValue("SkyDimension", OptionType.WORLD_TYPE_OPTION, false);

        if (worldType.getId().equals(BetterWorldOptions.NAMESPACE.id("nether"))) {
            args.set(1, -1);
        } else if (worldType.getId().equals(BetterWorldOptions.NAMESPACE.id("skylands")) && skyDimension) {
            args.set(1, 1);
        } else if (worldType.isDimension()) {
            args.set(1, worldType.getDimensionId());
        }
    }
}