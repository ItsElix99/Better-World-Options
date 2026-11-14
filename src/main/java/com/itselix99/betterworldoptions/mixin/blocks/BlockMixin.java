package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow @Final @Mutable public final int id;

    public BlockMixin(int id) {
        this.id = id;
    }

    @ModifyReturnValue(method = "getTexture*", at = @At("RETURN"))
    public int bwo_getOldTexture(int original, int side) {
        Minecraft minecraft = BetterWorldOptions.getMinecraft();

        if (minecraft != null) {
            BWOWorld bwoWorld = (BWOWorld) minecraft.world;

            if (bwoWorld != null) {
                BWOProperties bwoProperties = (BWOProperties) minecraft.world.getProperties();
                BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

                WorldType worldType = bwoWorld.bwo_getWorldType();
                boolean oldFeatures = bwoProperties.bwo_isOldFeatures();

                if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
                    if (this.id == 4) {
                        return worldType.getOldTexture("Cobblestone", original);
                    } else if (this.id == 45) {
                        return worldType.getOldTexture("BrickBlock", original);
                    } else if (this.id == 38) {
                        return worldType.getOldTexture("Rose", original);
                    } else if (this.id == 79) {
                        return worldType.getOldTexture("IceBlock", original);
                    }
                }
            }
        }

        return original;
    }
}