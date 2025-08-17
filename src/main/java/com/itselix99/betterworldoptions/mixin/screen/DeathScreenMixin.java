package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.mixin.world.AlphaWorldStorageAccessor;
import com.itselix99.betterworldoptions.mixin.world.DimensionDataAccessor;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.world.World;
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
    @Unique private World world;
    @Unique private boolean hardcore;
    @Unique private TranslationStorage translation = TranslationStorage.getInstance();

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.world = this.minecraft.world;
        this.hardcore = ((BWOProperties) this.world.getProperties()).bwo_isHardcore();

        this.buttons.add(this.deleteWorldButton = new ButtonWidget(3, this.width / 2 - 100, this.height / 4 + 96, this.world.isRemote ? "Disconnect" : this.translation.get("deathScreen.deleteWorld")));
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 3) {
            if (this.hardcore) {
                if (!this.world.isRemote) {
                    this.minecraft.setWorld(null, "Deleting world");
                    WorldStorage worldStorage = ((DimensionDataAccessor) this.world).getDimensionData();
                    WorldStorageSource getSave = this.minecraft.getWorldStorageSource();
                    getSave.flush();
                    getSave.delete(((AlphaWorldStorageAccessor) worldStorage).getDir().getName());
                } else {
                    this.world.disconnect();
                    this.minecraft.setWorld(null);
                }

                this.minecraft.setScreen(new TitleScreen());
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (this.hardcore) {
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