package com.itselix99.betterworldoptions.api.structure;

import net.modificationstation.stationapi.api.util.collection.WeightedPicker;

public class LootEntry extends WeightedPicker.Entry {
    public int item;
    public int itemMetadata;
    public int minItemChance;
    public int maxItemChance;

    public LootEntry(int item, int itemMetadata, int minItemChance, int maxItemChance, int weight) {
        super(weight);
        this.item = item;
        this.itemMetadata = itemMetadata;
        this.minItemChance = minItemChance;
        this.maxItemChance = maxItemChance;
    }
}