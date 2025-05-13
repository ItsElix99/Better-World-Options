package com.itselix99.betterworldoptions.mixin;

import net.fabricmc.loader.api.FabricLoader;
import com.itselix99.betterworldoptions.interfaces.WorldProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.storage.WorldSaveInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;


@Mixin(targets = "net.minecraft.client.gui.screen.world.SelectWorldScreen$WorldListWidget", priority = 100)
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
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true
    )
    protected void renderEntry(int i, int x, int y, int l, Tessellator arg, CallbackInfo ci, WorldSaveInfo worldSaveInfo, String levelName, String var8, long var9, String var11) {
        @Deprecated
        Minecraft minecraft = (Minecraft)FabricLoader.getInstance().getGameInstance();
        String worldType = ((WorldProperties) worldSaveInfo).bwo_getWorldType();
        if (Objects.equals(worldType, "")) {
            worldType = "Default";
        }

        if (isBHCreativeModPresent()) {
            int offset = minecraft.textRenderer.getWidth(levelName) + 6;
            this.field_2444.drawTextWithShadow(minecraft.textRenderer, "[", x + offset, y + 1, 16777215);
            offset += minecraft.textRenderer.getWidth("[");
            boolean isCreative = worldSaveInfo.creative_isCreative();
            boolean isHardcore = ((WorldProperties) worldSaveInfo).bwo_getHardcore();
            String gameMode = isCreative ? "Creative" : (isHardcore ? "Hardcore" : "Survival");
            int color = isCreative ? '\uffff' : (isHardcore ? 16711680 : '\uff00');
            this.field_2444.drawTextWithShadow(minecraft.textRenderer, gameMode, x + offset, y + 1, color);
            offset += minecraft.textRenderer.getWidth(gameMode);
            this.field_2444.drawTextWithShadow(minecraft.textRenderer, "]", x + offset, y + 1, 16777215);
        } else {
            int offset = minecraft.textRenderer.getWidth(levelName) + 6;
            this.field_2444.drawTextWithShadow(minecraft.textRenderer, "[", x + offset, y + 1, 16777215);
            offset += minecraft.textRenderer.getWidth("[");
            boolean isHardcore = ((WorldProperties) worldSaveInfo).bwo_getHardcore();
            String gameMode = isHardcore ? "Hardcore" : "Survival";
            int color = isHardcore ? 16711680 : '\uff00';
            this.field_2444.drawTextWithShadow(minecraft.textRenderer, gameMode, x + offset, y + 1, color);
            offset += minecraft.textRenderer.getWidth(gameMode);
            this.field_2444.drawTextWithShadow(minecraft.textRenderer, "]", x + offset, y + 1, 16777215);
        }
        this.field_2444.drawTextWithShadow(minecraft.textRenderer, "World Type:" + " " + worldType, x + 2, y + 12 + 10, 8421504);
        ci.cancel();
    }

    @Unique
    private boolean isBHCreativeModPresent() {
        return FabricLoader.getInstance().isModLoaded("bhcreative");
    }
}