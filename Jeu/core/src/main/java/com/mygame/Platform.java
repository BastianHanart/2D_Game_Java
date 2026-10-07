package com.mygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private Rectangle hitbox;
    private Texture texture;
    private float top_y;

    public Platform(float x, float y, float width, float height) {
        this.top_y = y + height;
        hitbox = new Rectangle(x, y, width, height);
        texture = new Texture("platform.png");
    }

    public float getTop_y(){
        return this.top_y;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void render(SpriteBatch batch) {
        batch.draw(
            texture,
            hitbox.x,
            hitbox.y,
            hitbox.width,
            hitbox.height
        );
    }

    public void dispose() {
        texture.dispose();
    }
}