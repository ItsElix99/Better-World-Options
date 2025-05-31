package com.itselix99.betterworldoptions.mixin.blocks;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Method;

@Mixin(value = LeavesBlock.class, priority = 1500)
public class LeavesBlockMixin extends TransparentBlock {

    public LeavesBlockMixin(int id, int textureId, Material material, boolean transparent) {
        super(id, textureId, material, transparent);
    }

    @Unique
    private boolean isVBEModPresent() {
        return FabricLoader.getInstance().isModLoaded("vbe");
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, BlockState replacedState) {
        String worldType = ((BWOProperties) world.getProperties()).bwo_getWorldType();

        if ((worldType.equals("Alpha 1.1.2_01") || worldType.equals("Infdev 420") || worldType.equals("Infdev 415") || worldType.equals("Early Infdev"))) {
            if (!((BWOProperties) world.getProperties()).bwo_getBetaFeatures() && isVBEModPresent()) {
                BlockState state = BetterWorldOptions.ALPHA_LEAVES.getDefaultState();
                try {
                    Class<?> levelUtilClass = Class.forName("paulevs.vbe.utils.LevelUtil");
                    Method setBlockSilent = levelUtilClass.getMethod("setBlockSilent", World.class, int.class, int.class, int.class, BlockState.class);
                    setBlockSilent.invoke(null, world, x, y, z, state);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}