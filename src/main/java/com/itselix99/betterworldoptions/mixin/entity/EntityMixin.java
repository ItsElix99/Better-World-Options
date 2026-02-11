package com.itselix99.betterworldoptions.mixin.entity;

import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public World world;
    @Shadow public double x;
    @Shadow public double z;
    @Shadow public double velocityX;
    @Shadow public double velocityZ;

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/Entity;firstTick:Z"
            )
    )
    private void bwo_pushEntityBackIntoWorld(CallbackInfo ci) {
        BWOProperties bwoProperties = (BWOProperties) this.world.getProperties();
        boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);

        if (finiteWorld) {
            double var1;

            int[] sizeLimits = BWOChunkGenerator.getSizeLimits();
            if (sizeLimits != null) {
                double minX = sizeLimits[0];
                double maxX = sizeLimits[1];
                double minZ = sizeLimits[2];
                double maxZ = sizeLimits[3];

                if (this.x < minX - 8.0D) {
                    var1 = (minX - 8.0D) - this.x;
                    this.velocityX += var1 * 0.001D;
                }

                if (this.z < minZ - 8.0D) {
                    var1 = (minZ - 8.0D) - this.z;
                    this.velocityZ += var1 * 0.001D;
                }

                if (this.x > maxX + 8.0D) {
                    var1 = this.x - (maxX + 8.0D);
                    this.velocityX -= var1 * 0.001D;
                }

                if (this.z > maxZ + 8.0D) {
                    var1 = this.z - (maxZ + 8.0D);
                    this.velocityZ -= var1 * 0.001D;
                }
            }
        }
    }
}