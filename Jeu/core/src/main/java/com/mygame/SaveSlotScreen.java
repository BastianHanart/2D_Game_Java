package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Représente les slots de sauvegarde lorsque qu'on veut lancer une partie
*/
public class SaveSlotScreen implements Screen {
    private static final float slot_height = 200f;
    private static final float gap = 20f;
    private static final float delete_width = 110f;
    private final Game game;
    private ShapeRenderer shapes;
    private Texture background;
    private Texture slotBackground;
    private SpriteBatch batch;
    private BitmapFont font;
    private final SaveData[] data = new SaveData[SaveManager.slots];
    private int confirmDelete = -1;


    /**
    * @param game défini la partie dans laquelle les différentes sauvegarde vont être affichées.
    * @see Game
    */
    public SaveSlotScreen(Game game) {
        this.game = game;
        background = new Texture("MenuBackground.png");
        slotBackground = new Texture("fond_de_slot.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);

        for (int i = 0; i < SaveManager.slots; i++) {
            data[i] = SaveManager.read(i + 1);
        }
    }

    private float slotW() { 
        return Math.min(900f, Gdx.graphics.getWidth() * 0.9f);
    }
    private float slotX() {
        return (Gdx.graphics.getWidth() - slotW()) / 2f;
    }
    private float slotY(int i) {
        float total = SaveManager.slots * slot_height + (SaveManager.slots - 1) * gap;
        float top = (Gdx.graphics.getHeight() + total) / 2f;
        return top - (i + 1) * slot_height - i * gap;
    }

    private void start(int i) {
        int slot = i + 1;

        if (data[i] == null) {
            GameState.reset();
            GameState.currentSlot = slot;
            game.setScreen(new GameScreen(game, null));
        } else {
            SaveManager.apply(slot, data[i]);
            game.setScreen(new GameScreen(game, data[i]));
        }
    }

    /**
    * @param delta défini délai de tous les éléments
    */
    @Override
    public void render(float delta) {
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        if (Gdx.input.justTouched()) {
            boolean hit = false;
            for (int i = 0; i < SaveManager.slots; i++) {
                float x = slotX(), y = slotY(i);
                if (mx < x || mx > x + slotW() || my < y || my > y + slot_height) continue;
                hit = true;
                boolean onDelete = data[i] != null && mx >= x + slotW() - delete_width;
                if (onDelete) {
                    if (confirmDelete == i) {
                        SaveManager.delete(i + 1);
                        data[i] = null;
                        confirmDelete = -1;
                    } else {
                        confirmDelete = i;
                    }
                } else {
                    start(i);
                    return;
                }
            }
            if (!hit) confirmDelete = -1;
        }

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (int i = 0; i < SaveManager.slots; i++) {
            float x = slotX();
            float y = slotY(i);
            float w = slotW();
            batch.draw(slotBackground, x, y, w, slot_height);
        }
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (int i = 0; i < SaveManager.slots; i++) {
            float x = slotX(), y = slotY(i), w = slotW();
            SaveData d = data[i];
            font.setColor(Color.WHITE);
            if (d == null) {
                font.draw(batch, "Sauvegarde " + (i + 1) + "  -  Nouvelle partie", x + 80, y + slot_height / 2f + 15);
            } else {
                int h = (int) (d.playTime / 3600), m = (int) (d.playTime % 3600 / 60);
                font.draw(batch, "Sauvegarde " + (i + 1) + "  -  Zone " + d.level + "  -  Niv. " + d.playerLevel, x + 80, y + slot_height - 60);
                font.getData().setScale(1.2f);
                font.draw(batch, String.format("%dh%02d  |  %s", h, m, fmt.format(new Date(d.timestamp))), x + 80, y + 100);
                font.getData().setScale(2f);

                font.draw(batch, confirmDelete == i ? "Sûr ?" : "X", x + w - delete_width - 10, y + slot_height / 2f + 15);
            }
        }
        font.draw(batch, "Echap : retour", 20, Gdx.graphics.getHeight() - 20);
        batch.end();
    }
    /**
    * Resize le visuel
    * @param w largeur du l'affichage
    * @param h hauteur du l'affichage
    */
    @Override public void resize(int w, int h) {}
    /**
    * Met pause aux jeu
    */
    @Override public void pause() {}
    @Override public void resume() {}

    /**
    * Cache le visuel
    */
    @Override public void hide() {}

    /**
    * Supprime les boutons de sauvegarde
    */
    @Override
    public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
        background.dispose();
        slotBackground.dispose();
    }
}