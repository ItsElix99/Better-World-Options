package com.itselix99.betterworldoptions.interfaces;

public interface BWOProperties {
    void bwo_setWorldType(String name);
    void bwo_setHardcore(boolean hardcore);

    String bwo_getWorldType();
    boolean bwo_getHardcore();
    boolean bwo_getBetaFeatures();

    boolean bwo_getSnowCovered();

    String bwo_getIndevWorldType();
    String bwo_getShape();
    String bwo_getSize();
    String bwo_getBetaTheme();
    String bwo_getTheme();
    boolean bwo_isIndevDimensions();
    boolean bwo_isGenerateIndevHouse();
    boolean bwo_isInfinite();
}
