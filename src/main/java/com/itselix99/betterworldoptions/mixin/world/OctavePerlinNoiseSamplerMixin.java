package com.itselix99.betterworldoptions.mixin.world;

import com.itselix99.betterworldoptions.interfaces.BWONoise;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OctavePerlinNoiseSampler.class)
public class OctavePerlinNoiseSamplerMixin implements BWONoise {
    @Shadow private PerlinNoiseSampler[] octaveSamplers;
    @Shadow private int octaves;

    @Override
    public double bwo_sample(double x, double y, double z) {
        double var7 = 0.0D;
        double var9 = 1.0D;

        for(int var11 = 0; var11 < this.octaves; ++var11) {
            var7 += this.octaveSamplers[var11].sample(x * var9, y * var9, z * var9) / var9;
            var9 /= 2.0D;
        }

        return var7;
    }
}
