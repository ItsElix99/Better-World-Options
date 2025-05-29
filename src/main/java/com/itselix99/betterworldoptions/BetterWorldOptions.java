package com.itselix99.betterworldoptions;

import com.itselix99.betterworldoptions.blocks.AlphaLeavesBlock;
import com.itselix99.betterworldoptions.events.TextureListener;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;

public class BetterWorldOptions {
    @Entrypoint.Namespace
    public static Namespace MOD_ID = Null.get();

    public static Biome EarlyInfdev;
    public static Biome Infdev;
    public static Biome Alpha;
    public static Biome WinterAlpha;

    public static Block ALPHA_LEAVES;

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        ALPHA_LEAVES = new AlphaLeavesBlock(Identifier.of(MOD_ID, "alpha_leaves"), TextureListener.alphaLeaves).setHardness(0.2F).setOpacity(1).setSoundGroup(Block.DIRT_SOUND_GROUP).setTranslationKey(MOD_ID, "alpha_leaves").disableTrackingStatistics().ignoreMetaUpdates();
    }

    @EventListener
    public void registerBiomes(BiomeRegisterEvent event) {
        EarlyInfdev = BiomeBuilder.start("Early Infdev")
                .fogColor(11842815)
                .snow(false)
                .hostileEntity(SpiderEntity.class, 10)
                .hostileEntity(ZombieEntity.class, 10)
                .hostileEntity(SkeletonEntity.class, 10)
                .hostileEntity(CreeperEntity.class, 10)
                .hostileEntity(SlimeEntity.class, 10)
                .passiveEntity(SheepEntity.class, 12)
                .passiveEntity(PigEntity.class, 10)
                .passiveEntity(ChickenEntity.class, 10)
                .passiveEntity(CowEntity.class, 8)
                .passiveEntity(WolfEntity.class, 2)
                .waterEntity(SquidEntity.class, 10)
                .build();
        Infdev = BiomeBuilder.start("Infdev")
                .fogColor(11587839)
                .snow(false)
                .hostileEntity(SpiderEntity.class, 10)
                .hostileEntity(ZombieEntity.class, 10)
                .hostileEntity(SkeletonEntity.class, 10)
                .hostileEntity(CreeperEntity.class, 10)
                .hostileEntity(SlimeEntity.class, 10)
                .passiveEntity(SheepEntity.class, 12)
                .passiveEntity(PigEntity.class, 10)
                .passiveEntity(ChickenEntity.class, 10)
                .passiveEntity(CowEntity.class, 8)
                .passiveEntity(WolfEntity.class, 2)
                .waterEntity(SquidEntity.class, 10)
                .build();
        Alpha = BiomeBuilder.start("Alpha")
                .fogColor(12638463)
                .snow(false)
                .hostileEntity(SpiderEntity.class, 10)
                .hostileEntity(ZombieEntity.class, 10)
                .hostileEntity(SkeletonEntity.class, 10)
                .hostileEntity(CreeperEntity.class, 10)
                .hostileEntity(SlimeEntity.class, 10)
                .passiveEntity(SheepEntity.class, 12)
                .passiveEntity(PigEntity.class, 10)
                .passiveEntity(ChickenEntity.class, 10)
                .passiveEntity(CowEntity.class, 8)
                .passiveEntity(WolfEntity.class, 2)
                .waterEntity(SquidEntity.class, 10)
                .build();
        WinterAlpha = BiomeBuilder.start("Winter Alpha")
                .fogColor(12638463)
                .snow(true)
                .hostileEntity(SpiderEntity.class, 10)
                .hostileEntity(ZombieEntity.class, 10)
                .hostileEntity(SkeletonEntity.class, 10)
                .hostileEntity(CreeperEntity.class, 10)
                .hostileEntity(SlimeEntity.class, 10)
                .passiveEntity(SheepEntity.class, 12)
                .passiveEntity(PigEntity.class, 10)
                .passiveEntity(ChickenEntity.class, 10)
                .passiveEntity(CowEntity.class, 8)
                .passiveEntity(WolfEntity.class, 2)
                .waterEntity(SquidEntity.class, 10)
                .build();
    }
}