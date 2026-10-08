package com.mygame;

import com.badlogic.gdx.math.Rectangle;

public class Body {
    public float x, y;              // centre des pieds
    public float w = 55, h = 115;   // hitbox du corps : change-la quand tu veux
    public float vy;
    public boolean onGround;

    private static final float GRAVITY = -3250f;
    private static final float STEP_DOWN = 60f;  // "colle" au sol en descente de pente

    public void jump(float force) { vy = force; onGround = false; }

    /** dx = déplacement horizontal voulu cette frame, en pixels. */
    public void move(CollisionMap map, float dx, float dt, boolean dropThrough) {
        // 1) Horizontal : murs
        x = map.moveX(x, dx, y, w / 2f, h);

        // 2) Vertical : gravité puis sol
        float prevY = y;
        vy += GRAVITY * dt;
        y += vy * dt;

        if (vy > 0) {                                   // on monte : plafond
            onGround = false;
            if (map.isSolid(x, y + h)) { y = prevY; vy = 0; }
        } else {                                        // on tombe ou on marche
            float minY = onGround ? y - STEP_DOWN : y;
            float maxY = Math.max(prevY, y) + CollisionMap.STEP;
            float ground = map.findGround(x, minY, maxY, prevY, dropThrough);
            if (ground != CollisionMap.NO_GROUND) {
                y = ground; vy = 0; onGround = true;
            } else {
                onGround = false;
            }
        }
    }

    public Rectangle rect() { return new Rectangle(x - w / 2f, y, w, h); }
}