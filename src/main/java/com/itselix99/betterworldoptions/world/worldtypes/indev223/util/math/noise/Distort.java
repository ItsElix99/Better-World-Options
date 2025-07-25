package com.itselix99.betterworldoptions.world.worldtypes.indev223.util.math.noise;

public class Distort extends NoiseSamplerIndev223 {
    private final NoiseSamplerIndev223 source;
    private final NoiseSamplerIndev223 distort;

    public Distort(NoiseSamplerIndev223 source, NoiseSamplerIndev223 distort) {
        this.source = source;
        this.distort = distort;
    }

    public final double create(double x, double y) {
        return this.source.create(x + this.distort.create(x, y), y);
    }
}