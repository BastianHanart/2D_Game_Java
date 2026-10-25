package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Représente les Fantômes, un type de monstre présent dans le jeu; gère leur position, leur vitesse, leurs attaques, leur gestion de vie et les collisions avec la carte.
*/
public class Fantome extends Monster {

    private final Texture sheetWalk;
    private final TextureRegion idleFrame;
    private final TextureRegion walkFrame;
    private final TextureRegion attackFrame1;
    private final TextureRegion attackFrame2;

    private static final float DETECTION_RANGE = 850f;
    private static final float MOVE_SPEED = 200f;
    private static final float DETECTION_SPEED = 500f;
    private static final float ATTACK_RANGE = 150f;
    private static final float ATTACK_COOLDOWN = 1.7f;
    private static final float ATTACK_DURATION = 0.4f;
    private static final int ATTACK_DAMAGE = 15;

    private float attackTimer = 0f;
    private float attackAnimationTimer = 0f;

    private boolean attacking = false;
    private boolean attackDamageApplied = false;

    private boolean moving = false;
    private boolean facingRight = true;
    private float idleTimer = 0f;
    private float idleDuration = 0f;

    private boolean idleMoving = false;
    private static final float IDLE_MAX_Y_OFFSET = 350f;   // hauteur max au-dessus du sol de départ

    private float groundY;               // altitude minimale (position de départ)
    private boolean groundInitialized = false;

    private float idleDirX = 0f;
    private float idleDirY = 0f;


    /**
    * @param game défini la partie dans laquelle le monstre va être créée.
    * @see Game
    */
    public Fantome(Game game) {
        super(20, 20);

        sheetWalk = new Texture("Monstres.png");

        idleFrame = new TextureRegion(sheetWalk, 50, 220, 100, 120);
        walkFrame = new TextureRegion(sheetWalk, 160, 220, 100, 120);

        // Frames 3 et 4 de l'animation du squelette
        attackFrame1 = new TextureRegion(sheetWalk, 260, 220, 130, 130);
        attackFrame2 = new TextureRegion(sheetWalk, 390, 220, 120, 120);

        body.w = 55;
        body.h = 115;
    }

    private void updateIdle(float delta) {

        idleTimer -= delta;

        if (idleTimer <= 0f) {

            if (idleMoving) {
                // Pause
                idleMoving = false;
                idleDuration = 1.0f;
                idleDirX = 0f;
                idleDirY = 0f;
            } else {
                // Nouvelle direction aléatoire (n'importe quel angle)
                idleMoving = true;
                float angle = (float) (Math.random() * Math.PI * 2);
                idleDirX = (float) Math.cos(angle);
                idleDirY = (float) Math.sin(angle);
                idleDuration = 0.5f + (float) Math.random();
            }
            idleTimer = idleDuration;
        }

        if (idleMoving && Math.abs(idleDirX) > 0.1f) {
            facingRight = idleDirX > 0;
        }

        moving = idleMoving;
    }


    private TextureRegion getAttackFrame() {
        if (attackAnimationTimer > ATTACK_DURATION / 2f) {
            return attackFrame1;
        }

        return attackFrame2;
    }

    /**
    * @param delta défini le delai de chaque attaque.
    * @param map défini les collisions de la map avec lesquelles le monstre intéragi.
    * @param player défini le joueur.
    */
    public void update(float delta, CollisionMap map, Character player) {

        // Mémorise la hauteur de départ comme "sol" du fantôme
        if (!groundInitialized) {
            groundY = body.y;
            groundInitialized = true;
        }

        attackTimer = Math.max(0f, attackTimer - delta);

        float distanceX = player.getX() - body.x;
        float distanceY = player.getY() - body.y;

        boolean playerDetected = Math.abs(distanceX) <= DETECTION_RANGE && Math.abs(distanceY) <= DETECTION_RANGE;

        float dx = 0f;
        float dy = 0f;
        moving = false;

        // --- Animation et dégâts de l'attaque ---
        if (attacking) {
            attackAnimationTimer -= delta;

            if (!attackDamageApplied && attackAnimationTimer <= ATTACK_DURATION / 2f) {
                if (Math.abs(distanceX) <= ATTACK_RANGE + player.getBody().w / 2f
                        && Math.abs(distanceY) <= 100f) {
                    player.takeDamage(ATTACK_DAMAGE);
                }
                attackDamageApplied = true;
            }

            if (attackAnimationTimer <= 0f) {
                attacking = false;
            }
        }

        // --- Décision : attaquer, poursuivre ou flotter ---
        if (playerDetected) {

            facingRight = distanceX >= 0;

            if (Math.abs(distanceX) <= ATTACK_RANGE && Math.abs(distanceY) <= 100f) {

                if (!attacking && attackTimer <= 0f) {
                    attacking = true;
                    attackAnimationTimer = ATTACK_DURATION;
                    attackTimer = ATTACK_COOLDOWN;
                    attackDamageApplied = false;
                }

            } else if (!attacking) {

                // Poursuite en 2D : direction normalisée vers le joueur
                float dist = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                if (dist > 0f) {
                    dx = (distanceX / dist) * DETECTION_SPEED * delta;
                    dy = (distanceY / dist) * DETECTION_SPEED * delta;
                }
                moving = true;
            }

        } else if (!attacking) {

            updateIdle(delta);

            if (idleMoving) {
                dx = idleDirX * MOVE_SPEED * delta;
                dy = idleDirY * MOVE_SPEED * delta;
            }
        }

        // --- Déplacement direct (aucune gravité) ---
        body.x += dx;
        body.y += dy;

        // Limites horizontales du niveau
        body.x = map.clampX(body.x, body.w / 2f);

        // En idle, il reste dans une zone de hauteur raisonnable
        if (!playerDetected) {
            float maxY = groundY + IDLE_MAX_Y_OFFSET;
            if (body.y > maxY) {
                body.y = maxY;
            }
        }
    }

    /**
    * @param batch défini le sprite du monstre.
    * @see SpriteBatch
    */
    public void render(SpriteBatch batch) {

        TextureRegion frame;

        if (attacking) {
            frame = getAttackFrame();
        } else if (moving) {
            frame = walkFrame;
        } else {
            frame = idleFrame;
        }

        float spriteWidth = 200f;
        float spriteHeight = 240f;

        if (facingRight) {
            batch.draw(frame, body.x - spriteWidth / 2f, body.y, spriteWidth, spriteHeight);
        } 
        else {
            batch.draw(frame, body.x + spriteWidth / 2f, body.y, -spriteWidth, spriteHeight);
        }
    }

    /**
    * Supprime de l'écran le monstre
    */
    public void dispose() {
        sheetWalk.dispose();
    }
}