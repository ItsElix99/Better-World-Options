package com.itselix99.betterworldoptions.world.chunk;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class BWOLimitChunk extends FlattenedChunk {
    protected final String mode;
    private final BWOProperties bwoProperties;

    public BWOLimitChunk(World world, int xPos, int zPos, String mode) {
        super(world, xPos, zPos);
        this.bwoProperties = (BWOProperties) this.world.getProperties();
        this.empty = true;
        this.mode = mode;
    }

    public int getBlockId(int x, int y, int z) {
        if (Block.BLOCKS[super.getBlockId(x, y, z)] instanceof BlockWithEntity) {
            return super.getBlockId(x, y, z);
        }

        if (this.mode != null) {
            switch (this.mode) {
                case "Island" -> {
                    if (y > 63) {
                        return 0;
                    } else if (y >= 55) {
                        return this.bwoProperties.bwo_getTheme().equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                    } else if (y == 54) {
                        return Block.DIRT.id;
                    } else {
                        return Block.BEDROCK.id;
                    }
                }
                case "Floating" -> {
                    if (y >= 2) {
                        return 0;
                    } else if (y == 1) {
                        return this.bwoProperties.bwo_getTheme().equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                    } else {
                        return Block.BEDROCK.id;
                    }
                }
                case "Inland", "Flat" -> {
                    if (y >= 65) {
                        return 0;
                    } else if (y == 64) {
                        return Block.GRASS_BLOCK.id;
                    } else {
                        return Block.BEDROCK.id;
                    }
                }
                case "MCPE" -> {
                    return BetterWorldOptions.INVISIBLE_BEDROCK.id;
                }
                case "LCE" -> {
                    int var1 = this.world.random.nextInt(5);
                    if (y > 63) {
                        return 0;
                    } else if (y > 55) {
                        return this.bwoProperties.bwo_getTheme().equals("Hell") ? Block.LAVA.id : Block.WATER.id;
                    } else if (y > var1) {
                        return Block.STONE.id;
                    } else {
                        return Block.BEDROCK.id;
                    }
                }
            }
        }

        return 0;
    }

    public int getLight(int x, int y, int z, int light) {
        if (this.mode.equals("MCPE")) {
            return 0;
        }

        return super.getLight(x, y, z, light);
    }
}