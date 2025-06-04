package com.itselix99.betterworldoptions.mixin.blocks;

import java.util.Objects;
import java.util.Random;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.world.worldtypes.alpha112.feature.LargeOakTreeFeatureAlpha112;
import com.itselix99.betterworldoptions.world.worldtypes.alpha112.feature.OakTreeFeatureAlpha112;
import com.itselix99.betterworldoptions.world.worldtypes.earlyinfdev.feature.OakTreeFeatureEarlyInfdev;
import com.itselix99.betterworldoptions.world.worldtypes.infdev415.feature.LargeOakTreeFeatureInfdev415;
import com.itselix99.betterworldoptions.world.worldtypes.infdev420.feature.LargeOakTreeFeatureInfdev420;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin extends PlantBlock {

    public SaplingBlockMixin(int id, int textureId) {
        super(id, textureId);
    }

    @Inject(
            method = "generate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OakTreeFeature;<init>()V",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void generate(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        int var6 = world.getBlockMeta(x, y, z) & 3;
        Feature var7;
        if (Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Alpha 1.1.2_01")) {
            if (!((BWOProperties) world.getProperties()).bwo_getBetaFeatures()) {
                var7 = new OakTreeFeatureAlpha112();
                if (random.nextInt(10) == 0) {
                    var7 = new LargeOakTreeFeatureAlpha112();
                }
            } else {
                var7 = new OakTreeFeature();
                if (random.nextInt(10) == 0) {
                    var7 = new LargeOakTreeFeature();
                }
            }
        } else if (Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Infdev 415")) {
            if (!((BWOProperties) world.getProperties()).bwo_getBetaFeatures()) {
                var7 = new LargeOakTreeFeatureInfdev415();
            } else {
                var7 = new LargeOakTreeFeature();
            }
        } else if (Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Infdev 420")) {
            if (!((BWOProperties) world.getProperties()).bwo_getBetaFeatures()) {
                var7 = new LargeOakTreeFeatureInfdev420();
            } else {
                var7 = new LargeOakTreeFeature();
            }
        } else if (Objects.equals(((BWOProperties) world.getProperties()).bwo_getWorldType(), "Early Infdev")) {
            if (!((BWOProperties) world.getProperties()).bwo_getBetaFeatures()) {
                var7 = new OakTreeFeatureEarlyInfdev();
            } else {
                var7 = new OakTreeFeature();
            }
        } else {
            var7 = new OakTreeFeature();
            if (random.nextInt(10) == 0) {
                var7 = new LargeOakTreeFeature();
            }
        }

        if (!var7.generate(world, random, x, y, z)) {
            world.setBlockWithoutNotifyingNeighbors(x, y, z, this.id, var6);
        }
        ci.cancel();
    }
}