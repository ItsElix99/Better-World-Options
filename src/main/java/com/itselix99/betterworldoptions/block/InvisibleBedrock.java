package com.itselix99.betterworldoptions.block;

import com.itselix99.betterworldoptions.event.TextureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class InvisibleBedrock extends TemplateBlock {

    public InvisibleBedrock(Identifier identifier, int j, Material material) {
        super(identifier, j, material);
    }

    @Environment(EnvType.CLIENT)
    public Box getBoundingBox(World world, int x, int y, int z) {
        return super.getBoundingBox(world, 0, 0, 0);
    }

    public int getTexture(int side, int meta) {
        return TextureListener.invisibleBedrock;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isOpaque() {
        return false;
    }

    public int getDroppedItemCount(Random random) {
        return 0;
    }

    @Environment(EnvType.CLIENT)
    public int getRenderLayer() {
        return -1;
    }
}