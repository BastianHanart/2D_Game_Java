package com.mygame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;

public class SkillTreeScreen implements Screen {
    private static final float Node = 80f;

    private final Game game;
    private final Screen retour;
    private final SkillTree tree = GameState.Skills;
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture background;

    public SkillTreeScreen(Game game, Screen retour) {
        this.game = game;
        this.retour = retour;
        background = new Texture("BackgroundTalentTree.jpeg");

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

        // 1. Dessiner les connexions entre les talents
        for (Talent t : Talent.values()) {
            for (Talent prerequisite : t.prerequisite) {
                shapes.setColor(Color.GRAY);
                shapes.rectLine(
                    prerequisite.x + Node / 2,
                    prerequisite.y + Node / 2,
                    t.x + Node / 2,
                    t.y + Node / 2,
                    4
                );
            }
        }

        // 2. Dessiner les nodes
        for (Talent t : Talent.values()) {
            if (tree.has(t)) {
                shapes.setColor(Color.GOLD);        // Talent débloqué
            } else if (tree.canUnlock(t)) {
                shapes.setColor(Color.FOREST);      // Talent disponible
            } else {
                shapes.setColor(Color.DARK_GRAY);   // Talent verrouillé
            }
        
            shapes.rect(t.x, t.y, Node, Node);
        }

        shapes.end();

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (Talent t : Talent.values()) {
            font.draw(batch, t.name, t.x, t.y - 5);
            font.draw(batch, "Cost: " + t.cost, t.x, t.y - 20);
        }

        font.getData().setScale(1f);

        font.draw(
            batch,
            "Points : " + tree.getPoints() + " (Echap pour revenir)",
            20,
            Gdx.graphics.getHeight() - 20
        );

        for (Talent t : Talent.values()) {
            font.draw(batch, t.name, t.x, t.y - 5);
        }

        if (survole != null) {
            font.getData().setScale(1.5f);
            font.draw(
                batch,
                "Cost : " + survole.cost,
                mx + 15,
                my - 15
            );
            font.getData().setScale(1f);
        }

        batch.end();
    }

    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    
    @Override public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
        background.dispose();
    }
}