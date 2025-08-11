package com.itselix99.betterworldoptions.block;

import com.itselix99.betterworldoptions.event.TextureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class InvisibleBedrock extends TemplateBlock {

    public InvisibleBedrock(Identifier identifier, int j, Material material) {
        super(identifier, j, material);
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
        return 2;
    }
}