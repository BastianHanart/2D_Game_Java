package com.mygame;

import com.badlogic.gdx.math.Rectangle;

/**
 * Représente le corps physique d'un personnage; gère sa position, sa vitesse verticale, la gravité et les collisions avec la carte.
*/
public class Body {
    public float x, y;              // centre des pieds
    public float w = 55, h = 115;   // hitbox du corps : change-la quand tu veux
    public float vy;
    public boolean onGround;

    private static final float GRAVITY = -3250f;
    private static final float STEP_DOWN = 60f;  // "colle" au sol en descente de pente

    /**
    * @param force permet de définir la vitesse de pousser du saut.
    */
    public void jump(float force) { vy = force; onGround = false; }

    /**
    * @param map permet de gérer les collisions et les plateformes du niveau actuel.
    * @see CollisionMap
    * @param dx déplacement horizontal.
    * @param dt déplacement verticale.
    * @param dropThrough permet de savoir si on traverse ou non les élements (plateforme oui, sol non).
    */
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
    /**
    * @return un nouveau rectangle de collision qui permet de définir les "dimensions" des éléments.
    */
    public Rectangle rect() { return new Rectangle(x - w / 2f, y, w, h); }
}