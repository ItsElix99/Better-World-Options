package com.itselix99.betterworldoptions.world;

import com.itselix99.betterworldoptions.api.structure.Structure;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.world.gen.WorldGenEvent;

public class BWOChunkDecorationListener {

    @EventListener
    public void decorate(WorldGenEvent.ChunkDecoration event) {
        if (event.world.dimension.id == 0) {
            for (Structure structure : Structure.getStructuresList()) {
                if (structure.isDecoration() && ((BWOProperties) event.world.getProperties()).bwo_isGenerateStructure(structure.getId())) {
                    structure.decorate(event.world, event.random, event.biome, event.x, event.z);
                }
            }
        }
    }
}