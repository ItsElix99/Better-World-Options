package com.itselix99.betterworldoptions.mixin.blocks;

import java.util.Objects;
import java.util.Random;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin extends PlantBlock {

    public SaplingBlockMixin(int id, int textureId) {
        super(id, textureId);
    }

    @WrapOperation(
            method = "generate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/Feature;generate(Lnet/minecraft/world/World;Ljava/util/Random;III)Z"
            )
    )
    private boolean wrapFeatureGenerate(Feature originalFeature, World world, Random random, int x, int y, int z, Operation<Boolean> original) {
        Feature var7;
        if (Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Alpha 1.1.2_01")) {
            var7 = new OakTreeFeature();
            if (random.nextInt(10) == 0) {
                var7 = new LargeOakTreeFeature();
            }
        } else if (Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Infdev 415") || Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Infdev 420")) {
            var7 = new LargeOakTreeFeature();
        } else if (Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Early Infdev") || Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Infdev 611")) {
            var7 = new OakTreeFeature();
        } else {
            var7 = originalFeature;
        }

        return original.call(var7, world, random, x, y, z);
    }
}