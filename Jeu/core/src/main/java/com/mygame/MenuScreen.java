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


        // =========================================== \\
        //                   Souris                    \\
        // =========================================== \\
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // --- Multiplication taille du bouton quand la souris passe par dessus ---
        float hoverScale = 1.10f;

        // =========================================== \\
        //                Bouton Play                  \\
        // =========================================== \\
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Play_Y && mouseY <= button_Play_Y + buttonHeight) {

            // --- Nouvelle taille du bouton ---
            float newWidth = buttonWidth * hoverScale;
            float newHeight = buttonHeight * hoverScale;

            // --- On "repositionne" pour éviter que le bouton ne se décale vers le bas à droite à cause de la multilication de sa taille ---
            float newX = buttonX - (newWidth - buttonWidth) / 2;
            float newY = button_Play_Y - (newHeight - buttonHeight) / 2;

            // --- On dessine le bouton ---
            batch.draw(playButton, newX, newY, newWidth, newHeight);
        } 
        else
            batch.draw(playButton, buttonX, button_Play_Y, buttonWidth, buttonHeight);

        // =========================================== \\
        //                Bouton Menu                  \\
        // =========================================== \\
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Menu_Y && mouseY <= button_Menu_Y + buttonHeight) {

             // --- Nouvelle taille du bouton ---
            float newWidth = buttonWidth * hoverScale;
            float newHeight = buttonHeight * hoverScale;

            // --- On "repositionne" pour éviter que le bouton ne se décale vers le bas à droite à cause de la multilication de sa taille ---
            float newX = buttonX - (newWidth - buttonWidth) / 2;
            float newY = button_Menu_Y - (newHeight - buttonHeight) / 2;

            // --- On dessine le bouton ---
            batch.draw(menuButton, newX, newY, newWidth, newHeight);
        } 
        else
            batch.draw(menuButton, buttonX, button_Menu_Y, buttonWidth, buttonHeight);

        // =========================================== \\
        //                Bouton Exit                  \\
        // =========================================== \\
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Exit_Y && mouseY <= button_Exit_Y + buttonHeight) {

            // --- Nouvelle taille du bouton ---
            float newWidth = buttonWidth * hoverScale;
            float newHeight = buttonHeight * hoverScale;

            // --- On "repositionne" pour éviter que le bouton ne se décale vers le bas à droite à cause de la multilication de sa taille ---
            float newX = buttonX - (newWidth - buttonWidth) / 2;
            float newY = button_Exit_Y - (newHeight - buttonHeight) / 2;

            // --- On dessine le bouton ---
            batch.draw(exitButton, newX, newY, newWidth, newHeight);
        } 
        else
            batch.draw(exitButton, buttonX, button_Exit_Y, buttonWidth, buttonHeight);
        batch.end();

        // =========================================== \\
        //               Appuie Bouton                 \\
        // =========================================== \\

        // --- Si il y a eu un clic sur la page ---
        if (Gdx.input.justTouched()) {

            // --- On récupère la position du clic ---
            mouseX = Gdx.input.getX();
            mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // --- Si le clic se trouve sur l'un des boutons, alors on fait l'action du dit-bouton ---            
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Play_Y && mouseY <= button_Play_Y + buttonHeight) {
                game.setScreen(new GameScreen(game));
            }
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Exit_Y && mouseY <= button_Exit_Y + buttonHeight) {
                Gdx.app.exit();
            }
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= button_Menu_Y && mouseY <= button_Menu_Y + buttonHeight) {
                game.setScreen(new SettingsScreen(game));
            }
        }
    }
    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        playButton.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}