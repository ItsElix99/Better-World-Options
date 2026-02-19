package com.itselix99.betterworldoptions.event;

import com.itselix99.betterworldoptions.config.BWOServerConfig;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

public class BWOServerListener {

    @EventListener
    public void onInit(InitEvent event) {
        BWOServerConfig.loadOptions();
        BWOWorldPropertiesStorage.BWOWorldVersion = "0.3.0";
    }
}