package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Settings {

    // =====================================================
    // TOUCHES
    // =====================================================

    public static int leftKey = Input.Keys.A;

    public static int rightKey = Input.Keys.D;

    public static int jumpKey = Input.Keys.W;

    public static int downKey = Input.Keys.S;

    public static int dashKey = Input.Keys.SPACE;

    public static int second_attackKey = Input.Keys.E;

    public static int health_potion_key = Input.Keys.H;

    public static int damage_potion_key = Input.Keys.J;

    public static int menuKey = Input.Keys.ESCAPE;

    public static int TalentTree = Input.Keys.M;

    public static int runKey = Input.Keys.SHIFT_LEFT;

    // =====================================================
    // AFFICHAGE
    // =====================================================

    public static void setFullscreen() {
        Gdx.graphics.setFullscreenMode(
            Gdx.graphics.getDisplayMode()
        );
    }

    public static void setWindowed() {
        Gdx.graphics.setWindowedMode(1280,720);
    }
}
