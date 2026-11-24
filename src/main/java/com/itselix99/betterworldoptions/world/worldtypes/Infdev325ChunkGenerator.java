package com.itselix99.betterworldoptions.world.worldtypes;

import com.itselix99.betterworldoptions.util.noise.BWOOctavePerlinNoiseSampler;
import net.minecraft.world.World;

public class Infdev325ChunkGenerator extends Infdev227ChunkGenerator {
    public Infdev325ChunkGenerator(World world, long seed) {
        super(world, seed);
        this.betterDecoration = true;

        setSizeLimits(0, this.width, 0, this.length);
    }

    protected void initNoise(World world, long seed) {
        this.minLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.maxLimitPerlinNoise = new BWOOctavePerlinNoiseSampler(this.random, 16, true);
        this.perlinNoise1 = new BWOOctavePerlinNoiseSampler(this.random, 8, true);
        this.perlinNoise2 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.perlinNoise3 = new BWOOctavePerlinNoiseSampler(this.random, 4, true);
        this.scaleNoise = new BWOOctavePerlinNoiseSampler(this.random, 5, true);
        this.forestNoise = new BWOOctavePerlinNoiseSampler(this.random, 5, true);
    }

    protected boolean generateObsidianWall() {
        return false;
    }
}