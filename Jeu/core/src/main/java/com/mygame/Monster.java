package com.mygame;

import com.badlogic.gdx.math.Rectangle;

/**
 * Représente la class de tous les monstres présent dans le jeu; gère leur position, leur vitesse, leurs attaques, leur gestion de vie et les collisions avec la carte.
*/
public class Monster {

    protected final Body body = new Body();

    private int life;
    protected int XPReward = 10;

    /**
    * @param life défini la vie du monstre à sa création.
    * @param XP défini le nombre d'XP donné au joueur à sa mort.
    */
    public Monster(int life, int XP) {
        this.XPReward = XP;
        this.life = life;
    }

    /**
    * @return la hitbox du montre via la fonction rect() dans Body
    * @see Body
    */
    public Rectangle getHitbox() {
        return body.rect();
    }

    /**
    * @param damage défini les dégats subit par le monstre via l'attaque du joueur.
    */
    public void takeDamage(int damage) {
        life -= damage;

        if (life < 0) {
            life = 0;
        }
    }

    /**
    * @return si true si le monstre est mort
    */
    public boolean isDead() {
        return life <= 0;
    }

    /**
    * @return la position horizontale du monstre
    */
    public float getX() {
        return body.x;
    }

    /**
    * @return la position verticale du monstre
    */
    public float getY() {
        return body.y;
    }

    /**
    * @return la vie du monstre
    */
    public int getLife() {
        return life;
    }

    /**
    * @return l'XP obtenu du monstre quand on le tue
    */
    public int getXPReward() {
        return XPReward;
    }

    /**
    * @param x défini la nouvelle position horizontale du monstre.
    * @param y défini la nouvelle position verticale du monstre.
    */
    public void setPosition(float x, float y) {
        body.x = x;
        body.y = y;
        body.vy = 0;
        body.onGround = false;
    }
}