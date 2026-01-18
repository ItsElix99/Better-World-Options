package com.itselix99.betterworldoptions.event;

import com.itselix99.betterworldoptions.config.BWOServerConfig;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

public class BWOServerListener {

    @EventListener
    public void onInit(InitEvent event) {
        BWOServerConfig.loadOptions();
    }
}