package com.mygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Représente la gestion des plateforme dans les niveaux.
*/
public class Platform {
    private Rectangle hitbox;
    private Texture texture;
    private float top_y;

    /**
    * @param x position horizontale de la plateforme
    * @param y position verticale de la plateforme
    * @param width largeur de la plateforme
    * @param height hauteur de la plateforme
    */
    public Platform(float x, float y, float width, float height) {
        this.top_y = y + height;
        hitbox = new Rectangle(x, y, width, height);
        texture = new Texture("platform.png");
    }

    /**
    * @return le haut de la plateforme
    */
    public float getTop_y(){
        return this.top_y;
    }

    /**
    * @return le hitbox de la plateforme
    */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
    * @param batch défini le sprite de la plateforme.
    * @see SpriteBatch
    */
    public void render(SpriteBatch batch) {
        batch.draw(
            texture,
            hitbox.x,
            hitbox.y,
            hitbox.width,
            hitbox.height
        );
    }

    /**
    * Supprime de l'écran la plateforme
    */
    public void dispose() {
        texture.dispose();
    }
}