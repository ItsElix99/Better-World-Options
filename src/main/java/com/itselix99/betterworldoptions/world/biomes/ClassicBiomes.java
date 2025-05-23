package com.itselix99.betterworldoptions.world.biomes;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;

public class ClassicBiomes {
    public static Biome EarlyInfdev;
    public static Biome Infdev;
    public static Biome Alpha;

    @EventListener
    public void registerBiomes(BiomeRegisterEvent event) {
        EarlyInfdev = BiomeBuilder.start("Early Infdev")
                .grassColor(10485589)
                .leavesColor(5242667)
                .fogColor(11842815)
                .snow(false)
                .build();
        Infdev = BiomeBuilder.start("Infdev")
                .grassColor(10485589)
                .leavesColor(5242667)
                .fogColor(11587839)
                .snow(false)
                .build();
        Alpha = BiomeBuilder.start("Alpha")
                .grassColor(10485589)
                .leavesColor(5242667)
                .fogColor(12638463)
                .snow(false)
                .build();
    }
}