package com.itselix99.betterworldoptions.events;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

import static com.itselix99.betterworldoptions.BetterWorldOptions.MOD_ID;

public class CreativeListener {
    public static CreativeTab BWOBlocks;

    @EventListener
    public void onTabInit(TabRegistryEvent event) {
        BWOBlocks = new SimpleTab(MOD_ID.id("bwo"), new ItemStack(BetterWorldOptions.ALPHA_LEAVES)); // Making tab
        event.register(BWOBlocks);

        BWOBlocks.addItem(new ItemStack(BetterWorldOptions.ALPHA_LEAVES));
    }
}