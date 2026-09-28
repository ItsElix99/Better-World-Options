package com.itselix99.betterworldoptions.interfaces;

import com.itselix99.betterworldoptions.gui.widget.BWOButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.modificationstation.stationapi.api.util.Util;

import java.util.List;

public interface BWOScreen {
    default List<BWOButtonWidget> bwo_getBWOButtonsList() {
        return Util.assertImpl();
    }

    default List<ButtonWidget> bwo_getButtonsList() {
        return Util.assertImpl();
    }
}