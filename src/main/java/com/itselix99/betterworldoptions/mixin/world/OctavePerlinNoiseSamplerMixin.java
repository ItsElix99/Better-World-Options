package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWONoise;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.storage.WorldSaveInfo;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mixin(OctavePerlinNoiseSampler.class)
public class OctavePerlinNoiseSamplerMixin implements BWONoise {
    @Shadow private PerlinNoiseSampler[] octaveSamplers;
    @Shadow private int octaves;


    @Override
    public double bwo_generateNoise(double x, double y, double z) {
        double var5 = 0.0D;
        double var7 = 1.0D;

        for(int var9 = 0; var9 < this.octaves; ++var9) {
            var5 += this.octaveSamplers[var9].sample(x * var7, y * var7, z * var7) / var7;
            var7 /= 2.0D;
        }

        return var5;
    }
}
