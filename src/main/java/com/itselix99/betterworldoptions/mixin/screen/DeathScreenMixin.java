package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOGetDirectoryName;
import com.itselix99.betterworldoptions.world.WorldSettings;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.world.storage.WorldStorage;
import net.minecraft.world.storage.WorldStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {
    @Unique private ButtonWidget deleteWorldButton;

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.buttons.add(this.deleteWorldButton = new ButtonWidget(3, this.width / 2 - 100, this.height / 4 + 96, translation.get("deathScreen.deleteWorld")));
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 3) {
            if (this.minecraft.world != null && ((BWOProperties) this.minecraft.world.getProperties()).bwo_isHardcore()) {
                WorldStorage worldStorage = ((BWOGetDirectoryName)this.minecraft.world).bwo_getDimensionData();
                String getDirectoryName = ((BWOGetDirectoryName) worldStorage).bwo_getDirectoryName();
                this.minecraft.setWorld(null, "Deleting world");
                WorldStorageSource getSave = this.minecraft.getWorldStorageSource();
                getSave.flush();
                getSave.delete(getDirectoryName);
                this.minecraft.setScreen(new TitleScreen());
                WorldSettings.resetSettings();
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TranslationStorage translation = TranslationStorage.getInstance();
        boolean hardcore = ((BWOProperties) this.minecraft.world.getProperties()).bwo_isHardcore();
        if (hardcore) {
            ((ButtonWidget) this.buttons.get(0)).active = false;
            ((ButtonWidget) this.buttons.get(0)).visible = false;
            ((ButtonWidget) this.buttons.get(1)).active = false;
            ((ButtonWidget) this.buttons.get(1)).visible = false;
            this.drawCenteredTextWithShadow(this.textRenderer, translation.get("deathScreen.hardcore.info"), this.width / 2, 144, 16777215);
        } else {
            this.deleteWorldButton.active = false;
            this.deleteWorldButton.visible = false;
        }
        super.render(mouseX, mouseY, delta);
    }
}