package com.itselix99.betterworldoptions.interfaces;

public interface BWOProperties {
    void bwo_setWorldType(String name);
    void bwo_setHardcore(boolean hardcore);

    String bwo_getWorldType();
    boolean bwo_isHardcore();
    boolean bwo_isOldFeatures();
    String bwo_getSingleBiome();
    String bwo_getTheme();
    boolean bwo_isSuperflat();

    String bwo_getIndevWorldType();
    String bwo_getIndevShape();
    int bwo_getWorldSizeX();
    int bwo_getWorldSizeZ();
    boolean bwo_isGenerateIndevHouse();
    boolean bwo_isInfiniteWorld();
}
