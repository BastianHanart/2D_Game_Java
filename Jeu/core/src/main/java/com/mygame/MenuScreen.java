package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Texture background;
    private Texture playButton;
    private Texture menuButton;
    private Texture exitButton;

    public MenuScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("MenuBackground.png");
        playButton = new Texture("BoutonPlay.png");
        menuButton = new Texture("BoutonMenu.png");
        exitButton = new Texture("BoutonExit.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float buttonWidth = 450;
        float buttonHeight = 200;
        float buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float button_Play_Y = 700;
        float button_Menu_Y = 450;
        float button_Exit_Y = 200;
        batch.draw(playButton, buttonX, button_Play_Y, buttonWidth, buttonHeight);
        batch.draw(menuButton, buttonX, button_Menu_Y, buttonWidth, buttonHeight);
        batch.draw(exitButton, buttonX, button_Exit_Y, buttonWidth, buttonHeight);
        batch.end();

        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Play_Y && mouseY <= button_Play_Y + buttonHeight) {
                game.setScreen(new GameScreen(game));
            }
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Exit_Y && mouseY <= button_Exit_Y + buttonHeight) {
                Gdx.app.exit();
            }
        }
    }
    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        playButton.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}