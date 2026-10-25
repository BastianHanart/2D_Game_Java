package com.mygame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Représente l'affichage des potions tout au long du jeu
*/
public class PotionHUD {
    private static final float ICON_HEIGHT = 48f;
    private static final float SPACING = 30f;
    private static final Color BUFF_COLOR = new Color(1f, 0.6f, 0.15f, 1f);

    private Texture healthPotion;
    private Texture damagePotion;
    private BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private float iconWidth;

    /**
    * @return le hauteur de l'affichage
    */
    public float getHeight() {
        return ICON_HEIGHT;
    }

    public PotionHUD() {
        healthPotion = new Texture("HealthPotion.png");
        damagePotion = new Texture("DamagePotion.png");
        iconWidth = ICON_HEIGHT * healthPotion.getWidth() / healthPotion.getHeight();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
    }

    /**
    * @param batch défini le sprite de la plateforme.
    * @param x position horizontale de la plateforme.
    * @param y position verticale de la plateforme.
    * @param character défini le joueur.
    * @see SpriteBatch
    * @see Character
    */
    public void draw(SpriteBatch batch, float x, float y, Character character) {
        float bottom = y - ICON_HEIGHT;
        float textY = bottom + ICON_HEIGHT / 2f + font.getCapHeight() / 2f;

        batch.draw(healthPotion, x, bottom, iconWidth, ICON_HEIGHT);
        x += iconWidth + 6f;
        x = drawText(batch, "x" + character.getHP_Potion() + " [" + Input.Keys.toString(Settings.health_potion_key) + "]", x, textY, Color.WHITE);
        x += SPACING;

        float buff = character.getDamageBuffTimer();
        boolean active = buff > 0f;
        if (active && buff < 5f && (int) (buff * 4f) % 2 == 0) {
            batch.setColor(1f, 1f, 1f, 0.4f);
        }
        batch.draw(damagePotion, x, bottom, iconWidth, ICON_HEIGHT);
        batch.setColor(Color.WHITE);
        x += iconWidth + 6f;
        x = drawText(batch, "x" + character.getDamage_Potion() + " [" + Input.Keys.toString(Settings.damage_potion_key) + "]", x, textY, Color.WHITE);

        if (active) {
            int pourcent = Math.round((Character.DAMAGE_POTION_MULTIPLIER - 1f) * 100f);
            drawText(batch, "  +" + pourcent + "% degats " + (int) Math.ceil(buff) + "s", x, textY, BUFF_COLOR);
        }
    }

    private float drawText(SpriteBatch batch, String text, float x, float y, Color color) {
        font.setColor(color);
        layout.setText(font, text);
        font.draw(batch, layout, x, y);
        return x + layout.width;
    }

    /**
    * Supprime de l'écran l'affichage de l'HUD
    */
    public void dispose() {
        healthPotion.dispose();
        damagePotion.dispose();
        font.dispose();
    }
}
