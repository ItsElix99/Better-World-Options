package com.itselix99.betterworldoptions.api.structure;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.collection.WeightedPicker;

import java.util.Arrays;
import java.util.Random;

public class LootEntry extends WeightedPicker.Entry {
    private final ItemStack item;
    private final int minItemChance;
    private final int maxItemChance;

    public LootEntry(int item, int itemMetadata, int minItemChance, int maxItemChance, int weight) {
        super(weight);
        this.item = new ItemStack(item, 1, itemMetadata);
        this.minItemChance = minItemChance;
        this.maxItemChance = maxItemChance;
    }

    public LootEntry(ItemStack item, int minItemChance, int maxItemChance, int weight) {
        super(weight);
        this.item = item;
        this.minItemChance = minItemChance;
        this.maxItemChance = maxItemChance;
    }

    public static void addLoot(Random random, LootEntry[] entries, Inventory inventory, int amount) {
        for(int var4 = 0; var4 < amount; ++var4) {
            LootEntry var5 = WeightedPicker.getRandom(random, Arrays.asList(entries));
            int var6 = var5.minItemChance + random.nextInt(var5.maxItemChance - var5.minItemChance + 1);
            if(var5.item.getMaxCount() >= var6) {
                ItemStack var9 = var5.item.copy();
                var9.count = var6;
                inventory.setStack(random.nextInt(inventory.size()), var9);
            } else {
                for(int var7 = 0; var7 < var6; ++var7) {
                    ItemStack var8 = var5.item.copy();
                    var8.count = 1;
                    inventory.setStack(random.nextInt(inventory.size()), var8);
                }
            }
        }

    }

    public static void addLoot(Random random, LootEntry[] entries, DispenserBlockEntity dispenser, int amount) {
        for(int var4 = 0; var4 < amount; ++var4) {
            LootEntry var5 = WeightedPicker.getRandom(random, Arrays.asList(entries));
            int var6 = var5.minItemChance + random.nextInt(var5.maxItemChance - var5.minItemChance + 1);
            if(var5.item.getMaxCount() >= var6) {
                ItemStack var9 = var5.item.copy();
                var9.count = var6;
                dispenser.setStack(random.nextInt(dispenser.size()), var9);
            } else {
                for(int var7 = 0; var7 < var6; ++var7) {
                    ItemStack var8 = var5.item.copy();
                    var8.count = 1;
                    dispenser.setStack(random.nextInt(dispenser.size()), var8);
                }
            }
        }

    }

    public static LootEntry[] addAll(LootEntry[] entries, LootEntry... moreEntries) {
        LootEntry[] var2 = new LootEntry[entries.length + moreEntries.length];
        int var3 = 0;

        for(int var4 = 0; var4 < entries.length; ++var4) {
            var2[var3++] = entries[var4];
        }

        LootEntry[] var8 = moreEntries;
        int var5 = moreEntries.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            LootEntry var7 = var8[var6];
            var2[var3++] = var7;
        }

        return var2;
    }
}