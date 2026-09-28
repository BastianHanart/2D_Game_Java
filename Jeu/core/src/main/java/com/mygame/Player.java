package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    private Texture texture;
    private float x;
    private float y;
    private float speed;

    public Player() {
        texture = new Texture("libgdx.png");

        x = 300;
        y = 200;

        speed = 200;
    }

    public void update(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * delta;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }

    public boolean checkCollision(Rectangle obstacle) {
        Rectangle playerRectangle = new Rectangle(
            x,
            y,
            texture.getWidth(),
            texture.getHeight()
        );

        return playerRectangle.overlaps(obstacle);
    }
}