package com.itselix99.betterworldoptions.mixin.player;

import com.itselix99.betterworldoptions.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
@Environment(EnvType.CLIENT)
public class BipedEntityModelMixin extends EntityModel {
    @Shadow public ModelPart head;
    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;

    @ModifyConstant(
            method = "setAngles",
            constant = @Constant(floatValue = 0.5F, ordinal = 0)
    )
    private float increaseRightArmSpeed(float constant) {
        if (Config.BWOConfig.player.walkingAnim.toString().equals("Infdev") || Config.BWOConfig.player.walkingAnim.toString().equals("Classic")) {
            return 1.0F;
        }

        return constant;
    }

    @ModifyConstant(
            method = "setAngles",
            constant = @Constant(floatValue = 0.5F, ordinal = 1)
    )
    private float increaseLeftArmSpeed(float constant) {
        if (Config.BWOConfig.player.walkingAnim.toString().equals("Infdev") || Config.BWOConfig.player.walkingAnim.toString().equals("Classic")) {
            return 1.0F;
        }

        return constant;
    }

    @Inject(method = "setAngles", at = @At("TAIL"))
    private void playerWalkingAnimation(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale, CallbackInfo ci) {
        if (Config.BWOConfig.player.walkingAnim.toString().equals("Infdev") || Config.BWOConfig.player.walkingAnim.toString().equals("Classic")) {
            this.rightArm.roll = (MathHelper.cos(limbAngle * 0.2312F) + 1.0F) * limbDistance;
            this.leftArm.roll = (MathHelper.cos(limbAngle * 0.2812F) - 1.0F) * limbDistance;
        }

        if (Config.BWOConfig.player.walkingAnim.toString().equals("Classic")) {
            this.head.yaw = headYaw / 57.29578F + (float)Math.sin((double)(limbAngle * 1.3334F) * 0.23D) * limbDistance;
            this.head.pitch = headPitch / 57.29578F + (float)Math.sin((double)(limbAngle * 1.3334F) * 0.1D) * 0.8F / 1.5F * limbDistance;
        }
    }

}