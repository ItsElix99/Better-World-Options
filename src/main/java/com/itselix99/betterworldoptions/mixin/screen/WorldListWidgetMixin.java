package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.llamalad7.mixinextras.sugar.Local;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.storage.WorldSaveInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(targets = "net.minecraft.client.gui.screen.world.SelectWorldScreen$WorldListWidget")
public class WorldListWidgetMixin {
    @Shadow @Final SelectWorldScreen field_2444;

    @Inject(
            method = "renderEntry",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/SelectWorldScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void worldTypeAndHardcoreText(int index, int x, int y, int i, Tessellator arg, CallbackInfo ci, WorldSaveInfo worldSaveInfo, String worldName, String var8, long var9, String var11) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) worldSaveInfo).bwo_getWorldType();
        boolean isHardcore = ((BWOProperties) worldSaveInfo).bwo_isHardcore();

        if (worldType.isEmpty()) {
            worldType = "Default";
        }

        if (isHardcore) {
            if (CompatMods.BHCreativeLoaded()) {
                int offset = minecraft.textRenderer.getWidth(worldName) + 60;

                this.field_2444.drawTextWithShadow(minecraft.textRenderer, "Hardcore", x + offset, y + 1, 16711680);
            } else {
                int offset = minecraft.textRenderer.getWidth(worldName) + 6;

                this.field_2444.drawTextWithShadow(minecraft.textRenderer, "Hardcore", x + offset, y + 1, 16711680);
            }
        }
        this.field_2444.drawTextWithShadow(minecraft.textRenderer, "World Type:" + " " + worldType, x + 2, y + 12 + 10, 8421504);
    }

    @ModifyArgs(
            method = "renderEntry",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/SelectWorldScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2
            )
    )
    private void changeConversionTextX(Args args, @Local(argsOnly = true, ordinal = 1) int x, @Local WorldSaveInfo worldSaveInfo) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) worldSaveInfo).bwo_getWorldType();

        if (worldType.isEmpty()) {
            worldType = "Default";
        }
        int offset = minecraft.textRenderer.getWidth("World Type:" + " " + worldType) + 10;

        args.set(2, x + offset);
    }
}