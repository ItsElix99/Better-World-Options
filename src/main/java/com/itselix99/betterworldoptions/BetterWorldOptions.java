package com.itselix99.betterworldoptions;

import com.itselix99.betterworldoptions.block.InvisibleBedrock;
import com.itselix99.betterworldoptions.event.TextureListener;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

    public static Block INVISIBLE_BEDROCK;

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        INVISIBLE_BEDROCK = new InvisibleBedrock(NAMESPACE.id("invisible_bedrock"), TextureListener.invisibleBedrock, Material.STONE).setUnbreakable().setResistance(6000000.0F).setSoundGroup(Block.STONE_SOUND_GROUP).setTranslationKey(NAMESPACE, "invisible_bedrock").disableTrackingStatistics();
    }

    @EventListener
    public void registerBiomes(BiomeRegisterEvent event) {
        IndevWoods = BiomeBuilder.start("Indev Woods")
                .fogColor(5069403)
                .snow(false)
                .grassAndLeavesColor(16777215)
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
                .grassAndLeavesColor(16777215)
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
                .grassAndLeavesColor(16777215)
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
                .grassAndLeavesColor(16777215)
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
                .grassAndLeavesColor(16777215)
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
                .grassAndLeavesColor(16777215)
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
                .grassAndLeavesColor(16777215)
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