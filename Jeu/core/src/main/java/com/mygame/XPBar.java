package com.mygame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class XPBar {
    private static final float WIDTH = 250f;
    private static final float HEIGHT = 12f;
    private static final Color FOND = new Color(0.15f, 0.15f, 0.15f, 0.8f);
    private static final Color XP_COLOR = new Color(0.3f, 0.6f, 1f, 1f);

    private Texture pixel;
    private BitmapFont font;

    public float getHeight() {
        return HEIGHT;
    }

    public XPBar() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();
        font = new BitmapFont();
        font.getData().setScale(1.3f);
    }

    public void draw(SpriteBatch batch, float x, float y) {
        float xpPercent = (float) GameState.xp / GameState.xpRequis();
        if (xpPercent < 0) xpPercent = 0;
        if (xpPercent > 1) xpPercent = 1;

        batch.setColor(FOND);
        batch.draw(pixel, x, y, WIDTH, HEIGHT);
        batch.setColor(XP_COLOR);
        batch.draw(pixel, x, y, WIDTH * xpPercent, HEIGHT);
        batch.setColor(Color.WHITE);

        font.setColor(Color.WHITE);
        font.draw(batch, "Niv " + GameState.niveau + "  " + GameState.xp + "/" + GameState.xpRequis() + " XP", x + WIDTH + 10, y + HEIGHT);
    }

    public void dispose() {
        pixel.dispose();
        font.dispose();
    }
}
