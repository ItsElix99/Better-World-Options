package com.itselix99.betterworldoptions.api.worldtype;

import com.itselix99.betterworldoptions.api.options.entry.OptionEntry;
import net.minecraft.world.chunk.ChunkSource;

import java.util.HashMap;
import java.util.Map;

public class WorldTypeEntry {
    public Class<? extends ChunkSource> overworldChunkGenerator;
    public String displayName;
    public String name;
    public String icon;
    public String[] description;
    public Map<String, Integer> oldTextures = new HashMap<>();
    public Map<String, Boolean> properties = new HashMap<>();
    public Map<String, OptionEntry> worldTypeOptions;
}