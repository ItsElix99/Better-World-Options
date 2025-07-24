package com.itselix99.betterworldoptions.world.worldtypes.indev223.math.noise;

public class Distort extends NoiseSamplerIndev223 {
    private NoiseSamplerIndev223 source;
    private NoiseSamplerIndev223 distort;

    public Distort(NoiseSamplerIndev223 source, NoiseSamplerIndev223 distort) {
        this.source = source;
        this.distort = distort;
    }

    public final double create(double x, double y) {
        return this.source.create(x + this.distort.create(x, y), y);
    }
}