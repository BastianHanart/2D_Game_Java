package com.mygame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SkillTreeScreen implements Screen {
    private static final float Node = 80f;

    private final Game game;
    private final Screen retour;
    private final SkillTree tree = GameState.Skills;
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;

    public SkillTreeScreen(Game game, Screen retour) {
        this.game = game;
        this.retour = retour;
    }

    @Override public void show() {
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override public void render(float delta) {
        // --- Entrées ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(retour);
            return;
        }
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        Talent survole = null;
        for (Talent t : Talent.values()) {
            if (mx >= t.x && mx <= t.x + Node && my >= t.y && my <= t.y + Node) survole = t;
        }
        if (survole != null && Gdx.input.justTouched()) tree.unlock(survole);

        // --- Dessin ---
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (Talent t : Talent.values()) {
            if (t.prerequisite != null) {
                shapes.setColor(Color.GRAY);
                shapes.rectLine(t.prerequisite.x + Node/2, t.prerequisite.y + Node/2, t.x + Node/2, t.y + Node/2, 4);
            }
        }
        for (Talent t : Talent.values()) {
            if (tree.has(t)) shapes.setColor(Color.GOLD);
            else if (tree.canUnlock(t)) shapes.setColor(Color.FOREST);
            else shapes.setColor(Color.DARK_GRAY);
            shapes.rect(t.x, t.y, Node, Node);
        }
        shapes.end();

        batch.begin();
        font.draw(batch, "Points :" + tree.getPoints() + "(Echap pour revenir)", 20, Gdx.graphics.getHeight() - 20);
        for (Talent t : Talent.values()) {
            font.draw(batch, t.name, t.x, t.y - 5);
        }
        font.getData().setScale(1.5f);
        if (survole != null) font.draw(batch, "Cost : " + String.valueOf(survole.cost), mx + 15, my - 15);
        batch.end();
    }

    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { shapes.dispose(); batch.dispose(); font.dispose(); }
}