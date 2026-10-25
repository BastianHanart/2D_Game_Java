package com.mygame;

import com.badlogic.gdx.Game;

/**
 * Représente la gestion du jeu lors de son lancement
*/
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}