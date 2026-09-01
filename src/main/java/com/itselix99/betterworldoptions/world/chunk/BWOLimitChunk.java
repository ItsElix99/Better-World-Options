package com.itselix99.betterworldoptions.world.chunk;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class BWOLimitChunk extends FlattenedChunk {
    protected final String mode;
    private final BWOProperties bwoProperties;
    private final Theme theme;

    public BWOLimitChunk(World world, int xPos, int zPos, String mode) {
        super(world, xPos, zPos);
        this.bwoProperties = (BWOProperties) this.world.getProperties();
        this.theme = Theme.getThemeById(Identifier.of(this.bwoProperties.bwo_getTheme()));
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
                        return this.theme.getLiquidBlock();
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
                        return this.theme.getLiquidBlock();
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
                        return this.theme.getLiquidBlock();
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

    public boolean setBlock(int x, int y, int z, int blockId, int meta) {
        if (Block.BLOCKS[blockId] instanceof BlockWithEntity) {
            return super.setBlock(x, y, z, blockId, meta);
        }

        return false;
    }

    public boolean setBlock(int x, int y, int z, int blockId) {
        if (Block.BLOCKS[blockId] instanceof BlockWithEntity) {
            return super.setBlock(x, y, z, blockId);
        }

        return false;
    }

    public BlockEntity getBlockEntity(int x, int y, int z) {
        BlockEntity blockEntity = super.getBlockEntity(x, y, z);
        super.setBlock(x, y, z, 0);
        return blockEntity;
    }
}