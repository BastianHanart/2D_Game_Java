package com.mygame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Monster {

    protected final Body body = new Body();

    private int life;
    private Game game;

    public Monster(Game game, int life) {
        this.game = game;
        this.life = life;
    }

    public Rectangle getHitbox() {
        return body.rect();
    }

    public void takeDamage(int damage) {
        life -= damage;

        if (life < 0) {
            life = 0;
        }
    }

    public boolean isDead() {
        return life <= 0;
    }

    public float getX() {
        return body.x;
    }

    public float getY() {
        return body.y;
    }

    public int getLife() {
        return life;
    }

    public void setPosition(float x, float y) {
        body.x = x;
        body.y = y;
        body.vy = 0;
        body.onGround = false;
    }
}