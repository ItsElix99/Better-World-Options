package com.itselix99.betterworldoptions.interfaces;

public interface BWOProperties {
    void bwo_setWorldType(String name);
    String bwo_getWorldType();

    void bwo_setHardcore(boolean hardcore);
    boolean bwo_getHardcore();

    void bwo_setBetaFeatures(boolean betaFeatures);
    boolean bwo_getBetaFeatures();

    void bwo_setSnowCovered(boolean snowCovered);
    boolean bwo_getSnowCovered();
}
