package com.itselix99.betterworldoptions.gui;

import com.itselix99.betterworldoptions.compat.CompatMods;
import com.itselix99.betterworldoptions.world.WorldSettings;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;


public class UpdateButtonText {
    public static boolean isFlat = false;

    public static void updateGamemodeButtonText(ButtonWidget button, boolean creative) {
        TranslationStorage translation = TranslationStorage.getInstance();
        String gamemodeLabel = translation.get("selectWorld.gameMode");
        if (CompatMods.BHCreativeLoaded()) {
            if (!WorldSettings.GameMode.isHardcore() && !creative) {
                button.text = gamemodeLabel + " " + translation.get("selectWorld.gameMode.survival");
            } else if (WorldSettings.GameMode.isHardcore() && !creative) {
                button.text = gamemodeLabel + " " + translation.get("selectWorld.gameMode.hardcore");
            } else if (!WorldSettings.GameMode.isHardcore()) {
                button.text = gamemodeLabel + " " + translation.get("title.bhcreative.selectWorld.creative");
            }
        } else {
            button.text = gamemodeLabel + " " + (!WorldSettings.GameMode.isHardcore() ? translation.get("selectWorld.gameMode.survival") : translation.get("selectWorld.gameMode.hardcore"));
        }
    }

    public static void updateWorldTypeButtonText(ButtonWidget button) {
        TranslationStorage translation = TranslationStorage.getInstance();
        button.text = translation.get("selectWorld.worldtype") + " " + WorldSettings.World.getWorldTypeName();
    }

    public static String updateMoreOptionsButtonText(boolean moreOptions) {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (moreOptions) {
            return translation.get("gui.done");
        } else {
            return translation.get("selectWorld.moreWorldOptions");
        }
    }

    public static void updateBetaFeaturesButtonText(ButtonWidget button, ButtonWidget winterModeButton) {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (WorldSettings.GameMode.getNonBetaFeaturesWorldTypes()) {
            button.active = false;
            WorldSettings.GameMode.setBetaFeatures(true);
            button.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.on");
        } else if ("Flat".equals(WorldSettings.World.getWorldTypeName()) && !isFlat) {
            isFlat = true;
            button.active = true;
            WorldSettings.GameMode.setBetaFeatures(false);
            button.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.off");
        } else {
            button.active = true;
            if (isFlat) {
                isFlat = false;
                WorldSettings.GameMode.setBetaFeatures(true);
            } else if (WorldSettings.GameMode.isBetaFeatures() && WorldSettings.AlphaWorld.isSnowCovered()) {
                WorldSettings.AlphaWorld.setSnowCovered(false);
                updateWinterModeButtonText(winterModeButton);
            }
            if (WorldSettings.GameMode.isBetaFeatures()) {
                button.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.on");
            } else {
                button.text = translation.get("selectWorld.betaFeatures")+ " " + translation.get("options.off");
            }
        }
    }

    public static void updateIndevBetaFeaturesButtonText(ButtonWidget button) {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (WorldSettings.GameMode.isBetaFeatures()) {
            WorldSettings.GameMode.setBetaFeatures(false);
            button.text = translation.get("selectWorld.betaFeatures") + " " + translation.get("options.off");
        } else {
            WorldSettings.GameMode.setBetaFeatures(true);
            button.text = translation.get("selectWorld.betaFeatures") + " " + translation.get("options.on");
        }
    }

    public static void updateWinterModeButtonText(ButtonWidget button) {
        TranslationStorage translation = TranslationStorage.getInstance();
        if (WorldSettings.AlphaWorld.isSnowCovered()) {
            button.text = translation.get("selectWorld.winterMode") + " " + translation.get("options.on");
        } else {
            button.text = translation.get("selectWorld.winterMode") + " " + translation.get("options.off");
        }
    }
}