package com.itselix99.betterworldoptions;

import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.block.InvisibleBedrock;
import com.itselix99.betterworldoptions.event.TextureListener;
import com.itselix99.betterworldoptions.network.BWOWorldPropertiesStoragePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BetterWorldOptions implements ModInitializer {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE = Null.get();

    public static Logger LOGGER = LogManager.getLogger("Better World Options");

    public static Biome Alpha;
    public static Biome Infdev;
    public static Biome EarlyInfdev;
    public static Biome Indev;

    public static Biome OCEAN;
    public static Biome EXTREME_HILLS;
    public static Biome RIVER;
    public static Biome FROZEN_OCEAN;
    public static Biome FROZEN_RIVER;
    public static Biome ICE_PLAINS;
    public static Biome ICE_MOUNTAINS;
    public static Biome MUSHROOM_ISLAND;
    public static Biome MUSHROOM_ISLAND_SHORE;

    public static Block INVISIBLE_BEDROCK;

    @Override
    public void onInitialize() {
        BiomeAPI.init(null, 0);
        this.initOldTextures();

        Structure village = Structure.getStructureById(NAMESPACE.id("village"));
        Structure notchVillage = Structure.getStructureById(NAMESPACE.id("notchVillage"));

        List<Biome> villageBiomes = new ArrayList<>(getOldBiomes());
        villageBiomes.add(ICE_PLAINS);

        village.addBiomes(villageBiomes);
        notchVillage.addBiomes(villageBiomes);
    }

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        INVISIBLE_BEDROCK = new InvisibleBedrock(NAMESPACE.id("invisible_bedrock"), TextureListener.invisibleBedrock, Material.AIR).setUnbreakable().setResistance(6000000.0F).setSoundGroup(Block.STONE_SOUND_GROUP).setTranslationKey(NAMESPACE, "invisible_bedrock").disableTrackingStatistics();
    }

    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("bwo_world_properties_storage_packet"), BWOWorldPropertiesStoragePacket.TYPE);
    }

    @EventListener
    public void registerBiomes(BiomeRegisterEvent event) {
        Alpha = BiomeBuilder.start("Alpha").fogColor(12638463).grassAndLeavesColor(16777215).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).passiveEntity(SheepEntity.class, 12).passiveEntity(PigEntity.class, 10).passiveEntity(ChickenEntity.class, 10).passiveEntity(CowEntity.class, 8).passiveEntity(WolfEntity.class, 2).waterEntity(SquidEntity.class, 10).build();
        Infdev = BiomeBuilder.start("Infdev").fogColor(11587839).grassAndLeavesColor(16777215).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).passiveEntity(SheepEntity.class, 12).passiveEntity(PigEntity.class, 10).passiveEntity(ChickenEntity.class, 10).passiveEntity(CowEntity.class, 8).passiveEntity(WolfEntity.class, 2).waterEntity(SquidEntity.class, 10).build();
        EarlyInfdev = BiomeBuilder.start("Early Infdev").fogColor(11842815).grassAndLeavesColor(16777215).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).passiveEntity(SheepEntity.class, 12).passiveEntity(PigEntity.class, 10).passiveEntity(ChickenEntity.class, 10).passiveEntity(CowEntity.class, 8).passiveEntity(WolfEntity.class, 2).waterEntity(SquidEntity.class, 10).build();
        Indev = BiomeBuilder.start("Indev").fogColor(16777215).grassAndLeavesColor(16777215).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).passiveEntity(SheepEntity.class, 12).passiveEntity(PigEntity.class, 10).passiveEntity(ChickenEntity.class, 10).passiveEntity(CowEntity.class, 8).passiveEntity(WolfEntity.class, 2).waterEntity(SquidEntity.class, 10).build();

        OCEAN = BiomeBuilder.start("Ocean").hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).waterEntity(SquidEntity.class, 10).build();
        EXTREME_HILLS = BiomeBuilder.start("Extreme Hills").hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).passiveEntity(SheepEntity.class, 12).passiveEntity(PigEntity.class, 10).passiveEntity(ChickenEntity.class, 10).passiveEntity(CowEntity.class, 8).waterEntity(SquidEntity.class, 10).build();
        RIVER = BiomeBuilder.start("River").hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).waterEntity(SquidEntity.class, 10).build();
        FROZEN_OCEAN = BiomeBuilder.start("Frozen Ocean").snow(true).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).waterEntity(SquidEntity.class, 10).build();
        FROZEN_RIVER = BiomeBuilder.start("Frozen River").snow(true).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).waterEntity(SquidEntity.class, 10).build();
        ICE_PLAINS = BiomeBuilder.start("Ice Plains").snow(true).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).passiveEntity(SheepEntity.class, 12).passiveEntity(PigEntity.class, 10).passiveEntity(ChickenEntity.class, 10).passiveEntity(CowEntity.class, 8).waterEntity(SquidEntity.class, 10).build();
        ICE_MOUNTAINS = BiomeBuilder.start("Ice Mountains").snow(true).hostileEntity(SpiderEntity.class, 10).hostileEntity(ZombieEntity.class, 10).hostileEntity(SkeletonEntity.class, 10).hostileEntity(CreeperEntity.class, 10).hostileEntity(SlimeEntity.class, 10).passiveEntity(SheepEntity.class, 12).passiveEntity(PigEntity.class, 10).passiveEntity(ChickenEntity.class, 10).passiveEntity(CowEntity.class, 8).waterEntity(SquidEntity.class, 10).build();
        MUSHROOM_ISLAND = BiomeBuilder.start("Mushroom Island").grassColor(12823220).build();
        MUSHROOM_ISLAND_SHORE = BiomeBuilder.start("Mushroom Island Shore").grassColor(12823220).build();
    }

    public static List<Biome> getOldBiomes() {
        return new ArrayList<>(Arrays.asList(Alpha, Infdev, EarlyInfdev, Indev));
    }

    @Environment(EnvType.CLIENT)
    public static Minecraft getMinecraft() {
        return (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
    }

    private void initOldTextures() {
        WorldType Alpha120 = WorldType.getWorldTypeById(NAMESPACE.id("alpha_1.2.0"));
        Alpha120.addOldTexture("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Alpha120.addOldTexture("Cobblestone", TextureListener.alphaCobblestone);

        WorldType Alpha112 = WorldType.getWorldTypeById(NAMESPACE.id("alpha_1.1.2_01"));
        Alpha112.addOldTexture("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        Alpha112.addOldTexture("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        Alpha112.addOldTexture("Cobblestone", TextureListener.alphaCobblestone);
        Alpha112.addOldTexture("IronBlockTop", TextureListener.alphaIronBlock);
        Alpha112.addOldTexture("IronBlockSide", TextureListener.alphaIronBlockSide);
        Alpha112.addOldTexture("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        Alpha112.addOldTexture("GoldBlockTop", TextureListener.alphaGoldBlock);
        Alpha112.addOldTexture("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        Alpha112.addOldTexture("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        Alpha112.addOldTexture("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        Alpha112.addOldTexture("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        Alpha112.addOldTexture("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        Alpha112.addOldTexture("Grass", TextureListener.alphaTallGrass);
        Alpha112.addOldTexture("Fern", TextureListener.alphaFern);
        Alpha112.addOldTexture("Leaves", TextureListener.alphaLeaves);
        Alpha112.addOldTexture("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        Alpha112.addOldTexture("FurnaceTop", Block.STONE.textureId);

        WorldType Infdev611 = WorldType.getWorldTypeById(NAMESPACE.id("infdev_20100611"));
        addOldTexturesForInfdevAndIndev(Infdev611);

        WorldType Infdev420 = WorldType.getWorldTypeById(NAMESPACE.id("infdev_20100420"));
        addOldTexturesForInfdevAndIndev(Infdev420);

        WorldType Infdev415 = WorldType.getWorldTypeById(NAMESPACE.id("infdev_20100415"));
        addOldTexturesForInfdevAndIndev(Infdev415);

        WorldType EarlyInfdev = WorldType.getWorldTypeById(NAMESPACE.id("early_infdev"));
        addOldTexturesForInfdevAndIndev(EarlyInfdev);

        WorldType Indev223 = WorldType.getWorldTypeById(NAMESPACE.id("indev_20100223"));
        addOldTexturesForInfdevAndIndev(Indev223);

        WorldType MCPE = WorldType.getWorldTypeById(NAMESPACE.id("mcpe"));
        MCPE.addOldTexture("GrassBlockSide", TextureListener.mcpeGrassBlockSide);
        MCPE.addOldTexture("Leaves", TextureListener.alphaLeaves);
        MCPE.addOldTexture("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        MCPE.addOldTexture("Rose", TextureListener.mcpeRose);
        MCPE.addOldTexture("IceBlock", TextureListener.mcpeIceBlock);
    }

    private void addOldTexturesForInfdevAndIndev(WorldType worldType) {
        worldType.addOldTexture("GrassBlockTop", TextureListener.alphaGrassBlockTop);
        worldType.addOldTexture("GrassBlockSide", TextureListener.alphaGrassBlockSide);
        worldType.addOldTexture("Cobblestone", TextureListener.alphaCobblestone);
        worldType.addOldTexture("IronBlockTop", TextureListener.alphaIronBlock);
        worldType.addOldTexture("IronBlockSide", TextureListener.alphaIronBlockSide);
        worldType.addOldTexture("IronBlockBottom", TextureListener.alphaIronBlockBottom);
        worldType.addOldTexture("GoldBlockTop", TextureListener.alphaGoldBlock);
        worldType.addOldTexture("GoldBlockSide", TextureListener.alphaGoldBlockSide);
        worldType.addOldTexture("GoldBlockBottom", TextureListener.alphaGoldBlockBottom);
        worldType.addOldTexture("DiamondBlockTop", TextureListener.alphaDiamondBlock);
        worldType.addOldTexture("DiamondBlockSide", TextureListener.alphaDiamondBlockSide);
        worldType.addOldTexture("DiamondBlockBottom", TextureListener.alphaDiamondBlockBottom);
        worldType.addOldTexture("Grass", TextureListener.alphaTallGrass);
        worldType.addOldTexture("Fern", TextureListener.alphaFern);
        worldType.addOldTexture("Leaves", TextureListener.alphaLeaves);
        worldType.addOldTexture("LeavesOpaque", TextureListener.alphaLeavesOpaque);
        worldType.addOldTexture("FurnaceTop", Block.STONE.textureId);
        worldType.addOldTexture("BrickBlock", TextureListener.infdevBricksBlock);
    }
}