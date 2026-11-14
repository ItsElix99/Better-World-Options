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
import net.minecraft.block.OreStorageBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(OreStorageBlock.class)
public class OreStorageBlockMixin extends Block {
    public OreStorageBlockMixin(int id, Material material) {
        super(id, material);
    }

    @ModifyReturnValue(method = "getTexture", at = @At("RETURN"))
    public int bwo_getOldTextureOreStorage(int original, int side) {
        Minecraft minecraft = BetterWorldOptions.getMinecraft();

        if (minecraft != null) {
            BWOWorld bwoWorld = (BWOWorld) minecraft.world;

            if (bwoWorld != null) {
                BWOProperties bwoProperties = (BWOProperties) minecraft.world.getProperties();
                BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();

                WorldType worldType = bwoWorld.bwo_getWorldType();
                boolean oldFeatures = bwoProperties.bwo_isOldFeatures();

                if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && bwoWorldPropertiesStorage.oldTextures) {
                    if (side == 1) {
                        if (this.id == 57) {
                            return worldType.getOldTexture("DiamondBlockTop", original);
                        } else if (this.id == 41) {
                            return worldType.getOldTexture("GoldBlockTop", original);
                        } else if (this.id == 42) {
                            return worldType.getOldTexture("IronBlockTop", original);
                        }
                    } else if (side == 0) {
                        if (this.id == 57) {
                            return worldType.getOldTexture("DiamondBlockBottom", original);
                        } else if (this.id == 41) {
                            return worldType.getOldTexture("GoldBlockBottom", original);
                        } else if (this.id == 42) {
                            return worldType.getOldTexture("IronBlockBottom", original);
                        }
                    } else {
                        if (this.id == 57) {
                            return worldType.getOldTexture("DiamondBlockSide", original);
                        } else if (this.id == 41) {
                            return worldType.getOldTexture("GoldBlockSide", original);
                        } else if (this.id == 42) {
                            return worldType.getOldTexture("IronBlockSide", original);
                        }
                    }
                }
            }
        }

        return original;
    }
}