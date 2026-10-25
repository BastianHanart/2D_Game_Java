package com.mygame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Représente les villageois, personnages non joueur et pacifique du jeu; gère leur position, leur vitesse, leurs attaques, leur gestion de vie et leurs collisions avec la carte.
*/
public class Villageois {
    private static final int START_X = 50, START_Y = 20;   // coin haut-gauche du 1er sprite
    private static final int SPRITE_W = 500, SPRITE_H = 320;
    private static final int GAP_X = -25, GAP_Y = 18;         // espace entre deux sprites
    private static final int COLS = 5;                      // sprites par ligne
    private static final int VARIANTS = 15;                  // nombre total de sprites

    // Texture partagée par tous les villageois (chargée une seule fois)
    private static Texture sheetWalk;
    private static int refCount = 0;
    private final TextureRegion idleFrame;
    private boolean facingRight = true;

    protected final Body body = new Body();

    /**
    * @param game défini la partie dans laquelle nous nous trouvons.
    * @param variant défini le numéro du villageois 
    * @see Game
    */
    public Villageois(Game game, int variant) {

        if (sheetWalk == null) sheetWalk = new Texture("PNJ.png");
        refCount++;

        int v = Math.floorMod(variant, VARIANTS);   // reste toujours dans la plage valide
        int col = v % COLS;
        int row = v / COLS;

        idleFrame = new TextureRegion(sheetWalk,
                START_X + col * (SPRITE_W + GAP_X),
                START_Y + row * (SPRITE_H + GAP_Y),
                SPRITE_W, SPRITE_H);
    }

    /**
    * @return la hitbox des PNJ
    */
    public Rectangle getHitbox() {
        return body.rect();
    }

    /**
    * @return la position horizontale du PNJ
    */
    public float getX() {
        return body.x;
    }

    /**
    * @return la position verticale du PNJ
    */
    public float getY() {
        return body.y;
    }

    /**
    * @param x défini nouvelle position horizontale du PNJ
    * @param y défini nouvelle position verticale du PNJ
    */
    public void setPosition(float x, float y) {
        body.x = x;
        body.y = y;
        body.vy = 0;
        body.onGround = false;
    }

    /**
    * @param delta défini delta des éléments
    * @param map défini collision du niveau actuel
    * @param player défini joueur
    * @see CollisionMap
    * @see Character
    */
    public void update(float delta, CollisionMap map, Character player) {
        float dx = 0f;
        body.move(map, dx, delta, false);

        // Se tourne vers le joueur
        float diff = player.getX() - body.x;
        if (Math.abs(diff) > 5f) {          // zone morte pour éviter le clignotement
            facingRight = diff > 0;
        }

        body.x = map.clampX(body.x, body.w / 2f);
    }

    /**
    * @param batch défini le sprite du personnage.
    * @see SpriteBatch
    */
    public void render(SpriteBatch batch) {

        TextureRegion frame = idleFrame;

        float spriteWidth = 325f;
        float spriteHeight = 200f;

         if (facingRight) {
            batch.draw(frame, body.x - spriteWidth / 2f, body.y, spriteWidth, spriteHeight);
        } 
        else {
            batch.draw(frame, body.x + spriteWidth / 2.6f, body.y, -spriteWidth, spriteHeight);
        }
    }

    public void dispose() {
        refCount--;
        if (refCount <= 0 && sheetWalk != null) {
            sheetWalk.dispose();
            sheetWalk = null;
            refCount = 0;
        }
    }
}