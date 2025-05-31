package com.itselix99.betterworldoptions.mixin.screen;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.GetDirectoryName;
import com.itselix99.betterworldoptions.world.WorldSettings;
import com.itselix99.betterworldoptions.world.WorldTypeList;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.world.storage.WorldStorage;
import net.minecraft.world.storage.WorldStorageSource;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.buttons.clear();
        if (((BWOProperties) this.minecraft.world.getProperties()).bwo_getHardcore()) {
            this.buttons.add(new ButtonWidget(1, this.width / 2 - 100, this.height / 4 + 96, translation.get("deathScreen.deleteWorld")));
        } else {
            this.buttons.add(new ButtonWidget(1, this.width / 2 - 100, this.height / 4 + 72, "Respawn"));
            this.buttons.add(new ButtonWidget(2, this.width / 2 - 100, this.height / 4 + 96, "Title menu"));
            if (this.minecraft.session == null) {
                ((ButtonWidget)this.buttons.get(1)).active = false;
            }
        }
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    protected void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 1) {
            if (this.minecraft.world != null && ((BWOProperties) this.minecraft.world.getProperties()).bwo_getHardcore()) {
                WorldStorage worldStorage = ((GetDirectoryName)this.minecraft.world).bwo_getDimensionData();
                String getDirectoryName = ((GetDirectoryName) worldStorage).bwo_getDirectoryName();
                this.minecraft.setWorld(null, "Deleting world");
                WorldStorageSource getSave = this.minecraft.getWorldStorageSource();
                getSave.flush();
                getSave.delete(getDirectoryName);
                this.minecraft.setScreen(new TitleScreen());
                if (!(WorldTypeList.worldtypeList == null)) {
                    WorldTypeList.selectWorldType(WorldTypeList.worldtypeList.get(0));
                }
                WorldSettings.resetBooleans();
            } else {
                this.minecraft.player.respawn();
                this.minecraft.setScreen(null);
                WorldSettings.resetBooleans();
            }
        }

        if (button.id == 2) {
            this.minecraft.setWorld(null);
            this.minecraft.setScreen(new TitleScreen());
            if (!(WorldTypeList.worldtypeList == null)) {
                WorldTypeList.selectWorldType(WorldTypeList.worldtypeList.get(0));
            }
            WorldSettings.resetBooleans();
        }
        ci.cancel();
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TranslationStorage translation = TranslationStorage.getInstance();
        this.fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        boolean hardcore = ((BWOProperties) this.minecraft.world.getProperties()).bwo_getHardcore();
        this.drawCenteredTextWithShadow(this.textRenderer, "Game over!", this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();
        this.drawCenteredTextWithShadow(this.textRenderer, "Score: &e" + this.minecraft.player.getScore(), this.width / 2, 100, 16777215);
        if (hardcore) {
            this.drawCenteredTextWithShadow(this.textRenderer, translation.get("deathScreen.hardcore.info"), this.width / 2, 144, 16777215);
        }
        super.render(mouseX, mouseY, delta);
    }
}