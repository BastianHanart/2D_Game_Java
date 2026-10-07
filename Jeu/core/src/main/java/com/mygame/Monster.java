package com.mygame;

import com.badlogic.gdx.math.Rectangle;

public class Monster {

    private float x;
    private float y;

    private int width = 55;
    private int height = 115;

    private int life = 10;

    public Monster(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public void takeDamage(int damage) {
        life -= damage;

        if (life < 0) {
            life = 0;
        }

        System.out.println("Monster touché ! Vie restante : " + life);
    }

    public boolean isDead() {
        return life <= 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getLife() {
        return life;
    }
}