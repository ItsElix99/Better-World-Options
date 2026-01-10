package com.itselix99.betterworldoptions.api.worldtype;

import net.minecraft.world.chunk.ChunkSource;

import java.util.HashMap;
import java.util.Map;

public class WorldTypeEntry {
    public Class<? extends ChunkSource> OVERWORLD_CHUNK_GENERATOR;
    public String DISPLAY_NAME;
    public String NAME;
    public String ICON;
    public String DESCRIPTION;
    public String DESCRIPTION_2;
    public Map<String, Integer> OLD_TEXTURES = new HashMap<>();
    public Map<String, Boolean> PROPERTIES = new HashMap<>();
}