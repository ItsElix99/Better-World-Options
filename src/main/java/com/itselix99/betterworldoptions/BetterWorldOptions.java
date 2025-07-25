package com.itselix99.betterworldoptions;

import com.itselix99.betterworldoptions.block.AlphaLeavesBlock;
import com.itselix99.betterworldoptions.event.TextureListener;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;

public class BetterWorldOptions {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE = Null.get();

    public static Biome IndevNormal;
    public static Biome IndevHell;
    public static Biome IndevParadise;
    public static Biome IndevWoods;
    public static Biome EarlyInfdev;
    public static Biome Infdev;
    public static Biome Alpha;
    public static Biome WinterAlpha;

    public static Block ALPHA_LEAVES;

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        ALPHA_LEAVES = new AlphaLeavesBlock(NAMESPACE.id("alpha_leaves"), TextureListener.alphaLeaves).setHardness(0.2F).setOpacity(1).setSoundGroup(Block.DIRT_SOUND_GROUP).setTranslationKey(NAMESPACE, "alpha_leaves").disableTrackingStatistics().ignoreMetaUpdates();
    }

    @EventListener
    public void registerBiomes(BiomeRegisterEvent event) {
        IndevWoods = BiomeBuilder.start("Indev Woods")
                .fogColor(5069403)
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
        IndevParadise = BiomeBuilder.start("Indev Paradise")
                .fogColor(13033215)
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
        IndevHell = BiomeBuilder.start("Indev Hell")
                .fogColor(1049600)
                .precipitation(false)
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
        IndevNormal = BiomeBuilder.start("Indev Normal")
                .fogColor(16777215)
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