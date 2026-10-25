package com.mygame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Représente l'affichage de l'arbre de compétences du joueur 
*/
public class SkillTreeScreen implements Screen {
    private static final float image_width = 2750f;
    private static final float image_height = 1536f;
    private static final float node_radius = 48f;

    private static final Color locked = new Color(0f, 0f, 0f, 0.55f);
    private static final Color available = new Color(0.3f, 1f, 0.4f, 1f);
    private static final Color unlocked = new Color(1f, 0.82f, 0.2f, 1f);
    private static final Color unlocked_fill = new Color(1f, 0.82f, 0.2f, 0.3f);

    private final Game game;
    private final Screen retour;
    private final SkillTree tree = GameState.Skills;
    private final GlyphLayout layout = new GlyphLayout();
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture background;
    private float time;

    /**
     * @param game partie dans laquelles l'affichage se fait
     * @param retour écran des compétences
    */
    public SkillTreeScreen(Game game, Screen retour) {
        this.game = game;
        this.retour = retour;
    }

    @Override public void show() {
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        background = new Texture("BackgroundTalentTree.jpeg");
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private float screenX(Talent t) {
        return t.x / image_width * Gdx.graphics.getWidth();
    }

    private float screenY(Talent t) {
        return Gdx.graphics.getHeight() - t.y / image_height * Gdx.graphics.getHeight();
    }

    private float screenRadius() {
        return node_radius * Math.min(Gdx.graphics.getWidth() / image_width, Gdx.graphics.getHeight() / image_height);
    }

    private void ring(float cx, float cy, float r, float thickness) {
        int segments = 48;
        for (int i = 0; i < segments; i++) {
            float a1 = MathUtils.PI2 * i / segments;
            float a2 = MathUtils.PI2 * (i + 1) / segments;
            shapes.rectLine(
                cx + MathUtils.cos(a1) * r, cy + MathUtils.sin(a1) * r,
                cx + MathUtils.cos(a2) * r, cy + MathUtils.sin(a2) * r,
                thickness
            );
        }
    }

    /**
     * @param delta délai des éléments
    */
    @Override public void render(float delta) {
        time += delta;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Settings.TalentTree)) {
            game.setScreen(retour);
            return;
        }

        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        float radius = screenRadius();
        Talent survole = null;

        for (Talent t : Talent.values()) {
            float dx = mx - screenX(t);
            float dy = my - screenY(t);
            if (dx * dx + dy * dy <= radius * radius) survole = t;
        }

        if (survole != null && Gdx.input.justTouched()) tree.unlock(survole);

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        float thickness = Math.max(2f, radius * 0.12f);
        float pulse = 0.6f + 0.4f * MathUtils.sin(time * 4f);

        for (Talent t : Talent.values()) {
            float cx = screenX(t);
            float cy = screenY(t);
            if (tree.has(t)) {
                shapes.setColor(unlocked_fill);
                shapes.circle(cx, cy, radius, 48);
                shapes.setColor(unlocked);
                ring(cx, cy, radius, thickness);
            } else if (tree.canUnlock(t)) {
                shapes.setColor(available.r, available.g, available.b, pulse);
                ring(cx, cy, radius + thickness * pulse, thickness);
            } else {
                shapes.setColor(locked);
                shapes.circle(cx, cy, radius, 48);
            }
            if (t == survole) {
                shapes.setColor(Color.WHITE);
                ring(cx, cy, radius + thickness * 2.5f, thickness * 0.6f);
            }
        }
        shapes.end();
        float scale = Gdx.graphics.getHeight() / 1080f;
        batch.begin();
        font.getData().setScale(1.1f * scale);

        for (Talent t : Talent.values()) {
            font.setColor(tree.has(t) ? unlocked : Color.WHITE);
            layout.setText(font, t.name);
            font.draw(batch, t.name, screenX(t) - layout.width / 2f, screenY(t) - radius - 8 * scale);
        }

        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f * scale);
        font.draw(batch, "Points : " + tree.getPoints() + "   (Echap pour revenir)", 40 * scale, Gdx.graphics.getHeight() - 70 * scale);
        batch.end();

        if (survole != null) drawTooltip(survole, mx, my, scale);

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawTooltip(Talent t, float mx, float my, float scale) {
        StringBuilder text = new StringBuilder(t.name).append("\nCout : ").append(t.cost);
        if (!t.prerequisite.isEmpty()) {
            text.append("\nRequiert :");
            for (Talent p : t.prerequisite) text.append(' ').append(p.name);
        }
        if (tree.has(t)) text.append("\nDebloque");
        else if (tree.canUnlock(t)) text.append("\nClic pour debloquer");
        else if (tree.getPoints() < t.cost) text.append("\nPas assez de points");

        font.getData().setScale(1.2f * scale);
        layout.setText(font, text);
        float pad = 10 * scale;
        float w = layout.width + pad * 2;
        float h = layout.height + pad * 2;
        float x = Math.min(mx + 20 * scale, Gdx.graphics.getWidth() - w);
        float y = Math.max(my - 20 * scale, h);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.08f, 0.02f, 0.04f, 0.85f);
        shapes.rect(x, y - h, w, h);
        shapes.end();
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, layout, x + pad, y - pad);
        batch.end();
    }

    @Override public void resize(int w, int h) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
        shapes.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
        shapes.updateMatrices();
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override public void hide() {
        dispose();
    }

    @Override public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
        background.dispose();
    }
}
