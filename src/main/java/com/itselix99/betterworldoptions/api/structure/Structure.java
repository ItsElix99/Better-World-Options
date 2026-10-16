package com.itselix99.betterworldoptions.api.structure;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.world.feature.desertwell.DesertWellStructure;
import com.itselix99.betterworldoptions.world.feature.notchHouse.NotchHouseStructure;
import com.itselix99.betterworldoptions.world.structures.TemplePieces;
import com.itselix99.betterworldoptions.world.structures.mineshaft.MineshaftStructure;
import com.itselix99.betterworldoptions.world.structures.desertPyramid.DesertPyramidStructure;
import com.itselix99.betterworldoptions.world.structures.notchVillage.NotchVillageStructure;
import com.itselix99.betterworldoptions.world.structures.village.VillagePieces;
import com.itselix99.betterworldoptions.world.structures.village.VillageStructure;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Structure {
    private static final List<Structure> structuresList = new ArrayList<>();

    private final Identifier id;
    private final String name;
    private StructureFeature structureFeature;
    private boolean decoration = false;
    private boolean largeStructure = false;
    private final ArrayList<Biome> biomes = new ArrayList<>();
    private final ArrayList<Identifier> incompatibleWorldTypes = new ArrayList<>();

    public Structure(Identifier id, String name, StructureFeature structureFeature) {
        this.id = id;
        this.name = name;
        this.structureFeature = structureFeature;
        structuresList.add(this);
    }

    public Structure(Identifier id, String name, boolean decoration) {
        this.id = id;
        this.name = name;
        this.decoration = decoration;
        structuresList.add(this);
    }

    public void setStructureFeature(StructureFeature structureFeature) {
        this.structureFeature = structureFeature;
    }

    public void setLargeStructure(boolean largeStructure) {
        this.largeStructure = largeStructure;
    }

    public static void setMissingBlock(int target, int original) {
        if (VillagePieces.blocks.containsKey(target)) {
            VillagePieces.blocks.put(target, original);
        }

        if (TemplePieces.blocks.containsKey(target)) {
            TemplePieces.blocks.put(target, original);
        }
    }

    public Identifier getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public StructureFeature getStructureFeature() {
        return this.structureFeature;
    }

    public boolean isDecoration() {
        return this.decoration;
    }

    public boolean isLargeStructure() {
        return this.largeStructure;
    }

    public List<Biome> getValidBiomes() {
        return this.biomes;
    }

    public List<Identifier> getIncompatibleWorldTypes() {
        return this.incompatibleWorldTypes;
    }

    public static List<Structure> getStructuresList() {
        return structuresList;
    }

    public static Structure getStructureById(Identifier id) {
        return getStructuresList().stream().filter(structure -> id.equals(structure.id)).findFirst().orElse(null);
    }

    public void addBiome(Biome biome) {
        this.biomes.add(biome);
    }

    public void addBiomes(List<Biome> biomes) {
        this.biomes.addAll(biomes);
    }

    public void addIncompatibleWorldType(Identifier id) {
        this.incompatibleWorldTypes.add(id);
    }

    public void decorate(World world, Random random, Biome biome, int x, int z) {
    }

    static {
        Structure Mineshaft = new Structure(BetterWorldOptions.NAMESPACE.id("mineshaft"), "Mineshaft", new MineshaftStructure());
        Mineshaft.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("nether"));
        Mineshaft.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("skylands"));

        Structure Village = new Structure(BetterWorldOptions.NAMESPACE.id("village"), "Village", new VillageStructure());
        Village.setLargeStructure(true);
        Village.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("nether"));
        Village.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("skylands"));
        Village.addBiomes(new ArrayList<>(Arrays.asList(Biome.PLAINS, Biome.DESERT, Biome.SAVANNA, Biome.TUNDRA)));

        Structure DesertPyramid = new Structure(BetterWorldOptions.NAMESPACE.id("desertPyramid"), "Desert Pyramid", new DesertPyramidStructure());
        DesertPyramid.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("nether"));
        DesertPyramid.addBiome(Biome.DESERT);

        Structure DesertWell = new DesertWellStructure(BetterWorldOptions.NAMESPACE.id("desertWell"), "Desert Well", true);
        DesertWell.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("nether"));
        DesertWell.addBiome(Biome.DESERT);

        Structure NotchVillage = new Structure(BetterWorldOptions.NAMESPACE.id("notchVillage"), "Notch Village", new NotchVillageStructure());
        NotchVillage.setLargeStructure(true);
        NotchVillage.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("nether"));
        NotchVillage.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("skylands"));
        NotchVillage.addBiomes(new ArrayList<>(Arrays.asList(Biome.PLAINS, Biome.DESERT, Biome.SAVANNA, Biome.TUNDRA)));

//        Structure NotchHouse = new NotchHouseStructure(BetterWorldOptions.NAMESPACE.id("notchHouse"), "Notch House", true);
//        NotchHouse.addIncompatibleWorldType(BetterWorldOptions.NAMESPACE.id("nether"));
    }
}