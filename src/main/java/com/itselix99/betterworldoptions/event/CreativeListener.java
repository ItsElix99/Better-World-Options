package com.itselix99.betterworldoptions.event;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

import static com.itselix99.betterworldoptions.BetterWorldOptions.NAMESPACE;

public class CreativeListener {
    public static CreativeTab BWOBlocks;

    @EventListener
    public void onTabInit(TabRegistryEvent event) {
        BWOBlocks = new SimpleTab(NAMESPACE.id("bwo"), new ItemStack(BetterWorldOptions.ALPHA_LEAVES));
        event.register(BWOBlocks);

        BWOBlocks.addItem(new ItemStack(BetterWorldOptions.ALPHA_LEAVES));
    }
}