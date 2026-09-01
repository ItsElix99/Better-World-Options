package com.itselix99.betterworldoptions.api.theme;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Theme {
    private static final List<Theme> themesList = new ArrayList<>();
    public static final Theme defaultTheme = new Theme(BetterWorldOptions.NAMESPACE.id("normal"), "Normal", "", new String[]{});

    private final Identifier id;
    private final String name;
    private final String icon;
    private final String[] description;
    protected int skyColor = -1;
    protected int fogColor = -1;
    protected int cloudsColor = -1;
    protected int surfaceLiquidBlock = Block.ICE.id;
    protected int liquidBlock = Block.WATER.id;
    protected int flowingLiquidBlock = Block.FLOWING_WATER.id;
    protected int topBlock = -1;
    protected int soilBlock = -1;
    protected int beachTopBlock = Block.SAND.id;
    protected int beachSoilBlock = Block.SAND.id;
    protected boolean denseWoods = false;
    protected boolean floweryLand = false;
    protected boolean hot = false;
    protected boolean cold = false;
    protected boolean stopTime = false;
    protected boolean rain = true;
    protected int blockToSpawn = -1;

    public Theme(Identifier id, String name, String icon, String[] description) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        themesList.add(this);
    }

    public Theme(Identifier id, String name, String icon, String[] description, int skyColor, int fogColor, int cloudsColor) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.skyColor = skyColor;
        this.fogColor = fogColor;
        this.cloudsColor = cloudsColor;
        themesList.add(this);
    }

    public void setSkyColor(int skyColor) {
        this.skyColor = skyColor;
    }

    public void setFogColor(int fogColor) {
        this.fogColor = fogColor;
    }

    public void setCloudsColor(int cloudsColor) {
        this.cloudsColor = cloudsColor;
    }

    public void setSurfaceLiquidBlock(int surfaceLiquidBlockId) {
        this.surfaceLiquidBlock = surfaceLiquidBlockId;
    }

    public void setLiquidBlock(int liquidBlockId) {
        this.liquidBlock = liquidBlockId;
    }

    public void setFlowingLiquidBlock(int flowingLiquidBlockId) {
        this.flowingLiquidBlock = flowingLiquidBlockId;
    }

    public void setTopBlock(int topBlockId) {
        this.topBlock = topBlockId;
    }

    public void setSoilBlock(int soilBlockId) {
        this.soilBlock = soilBlockId;
    }

    public void setBeachTopBlock(int beachTopBlockId) {
        this.beachTopBlock = beachTopBlockId;
    }

    public void setBeachSoilBlock(int beachSoilBlockId) {
        this.beachSoilBlock = beachSoilBlockId;
    }

    public void setDenseWoods(boolean denseWoods) {
        this.denseWoods = denseWoods;
    }

    public void setFloweryLand(boolean floweryLand) {
        this.floweryLand = floweryLand;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public void setCold(boolean cold) {
        this.cold = cold;
    }

    public void setStopTime(boolean stopTime) {
        this.stopTime = stopTime;
    }

    public void setRain(boolean rain) {
        this.rain = rain;
    }

    public void setBlockToSpawn(int blockId) {
        this.blockToSpawn = blockId;
    }

    public Identifier getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.name;
    }

    public String[] getDescription() {
        return this.description;
    }

    public int getSkyColor() {
        return this.skyColor;
    }

    public int getFogColor() {
        return this.fogColor;
    }

    public int getCloudsColor() {
        return this.cloudsColor;
    }

    public int getSurfaceLiquidBlock() {
        return this.surfaceLiquidBlock;
    }

    public int getLiquidBlock() {
        return this.liquidBlock;
    }

    public int getFlowingLiquidBlock() {
        return this.flowingLiquidBlock;
    }

    public int getTopBlock() {
        return this.topBlock;
    }

    public int getSoilBlock() {
        return this.soilBlock;
    }

    public int getBeachTopBlock() {
        return this.beachTopBlock;
    }

    public int getBeachSoilBlock() {
        return this.beachSoilBlock;
    }

    public boolean isDenseWoods() {
        return this.denseWoods;
    }

    public boolean isFloweryLand() {
        return this.floweryLand;
    }

    public boolean isHot() {
        return this.hot;
    }

    public boolean isCold() {
        return this.cold;
    }

    public boolean isStopTime() {
        return this.stopTime;
    }

    public boolean allowRain() {
        return this.rain;
    }

    public int getBlockToSpawn() {
        return this.blockToSpawn;
    }

    public int changeAmbientDarkness(int defaultValue) {
        return defaultValue;
    }

    public void tick(World world) {
    }

    public static List<Theme> getThemesList() {
        return themesList;
    }

    public static Theme getThemeById(Identifier id) {
        return getThemesList().stream().filter(theme -> id.equals(theme.id)).findFirst().orElse(defaultTheme);
    }

    static {
        Theme Hell = new HellTheme(BetterWorldOptions.NAMESPACE.id("hell"), "Hell", "", new String[]{});

        Theme Paradise = new Theme(BetterWorldOptions.NAMESPACE.id("paradise"), "Paradise", "", new String[]{}, 13033215, 13033215, 15658751);
        Paradise.setFloweryLand(true);
        Paradise.setStopTime(true);
        Paradise.setRain(false);

        Theme Woods = new WoodsTheme(BetterWorldOptions.NAMESPACE.id("woods"), "Woods", "", new String[]{});

        Theme Winter = new Theme(BetterWorldOptions.NAMESPACE.id("winter"), "Winter", "", new String[]{});
        Winter.setCold(true);
    }
}