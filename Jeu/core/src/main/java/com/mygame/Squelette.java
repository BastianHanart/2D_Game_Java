package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Squelette extends Monster {

    private final Texture sheetWalk;
    private final TextureRegion idleFrame;
    private final TextureRegion walkFrame;
    private final TextureRegion attackFrame1;
    private final TextureRegion attackFrame2;

    private static final float DETECTION_RANGE = 600f;
    private static final float MOVE_SPEED = 200f;
    private static final float DETECTION_SPEED = 400f;
    private static final float ATTACK_RANGE = 120f;
    private static final float ATTACK_COOLDOWN = 1.2f;
    private static final float ATTACK_DURATION = 0.8f;
    private static final int ATTACK_DAMAGE = 10;

    private float attackTimer = 0f;
    private float attackAnimationTimer = 0f;

    private boolean attacking = false;
    private boolean attackDamageApplied = false;

    private boolean moving = false;
    private boolean facingRight = true;
    private float idleTimer = 0f;
    private float idleDuration = 0f;

    private boolean idleMoving = false;

    private int idleDirection = 1;

    public Squelette(Game game) {
        super(game, 10);

        sheetWalk = new Texture("Monstres.png");

        idleFrame = new TextureRegion(sheetWalk, 50, 25, 100, 120);
        walkFrame = new TextureRegion(sheetWalk, 160, 25, 100, 120);

        // Frames 3 et 4 de l'animation du squelette
        attackFrame1 = new TextureRegion(sheetWalk, 260, 25, 130, 130);
        attackFrame2 = new TextureRegion(sheetWalk, 390, 25, 120, 120);

        body.w = 55;
        body.h = 115;
    }

    private void updateIdle(float delta) {

        idleTimer -= delta;

        if (idleTimer <= 0f) {

            if (idleMoving) {
                idleMoving = false;
                idleDuration = 1.5f;
                idleDirection = 0;

            } 
            else {
                idleMoving = true;
                idleDirection = Math.random() < 0.5 ? -1 : 1;
                idleDuration = 0.2f + (float) Math.random();
            }
            idleTimer = idleDuration;
        }

        if (idleMoving) {
            facingRight = idleDirection == 1;
        }

        moving = idleMoving;
    }


    private TextureRegion getAttackFrame() {
        if (attackAnimationTimer > ATTACK_DURATION / 2f) {
            return attackFrame1;
        }

        return attackFrame2;
    }

    public void update(float delta, CollisionMap map, Character player) {

    // --- Mise à jour du délai entre les attaques ---
    attackTimer = Math.max(0f, attackTimer - delta);

    float distanceX = player.getX() - body.x;
    float distanceY = player.getY() - body.y;

    boolean playerDetected =
        Math.abs(distanceX) <= DETECTION_RANGE && Math.abs(distanceY) <= 450f;

    float dx = 0f;
    moving = false;

    // --- Animation et dégâts de l'attaque ---
    if (attacking) {

        attackAnimationTimer -= delta;

        if (!attackDamageApplied && attackAnimationTimer <= ATTACK_DURATION / 2f) {

            if (Math.abs(distanceX) <= ATTACK_RANGE + player.getBody().w / 2f && Math.abs(distanceY) <= 100f) {

                player.takeDamage(ATTACK_DAMAGE);
            }

            attackDamageApplied = true;
        }

        // Fin de l'animation d'attaque.
        if (attackAnimationTimer <= 0f) {
            attacking = false;
        }
    }

    // --- Décision : attaquer, poursuivre ou se reposer ---
    if (playerDetected) {

        facingRight = distanceX >= 0;

        if (Math.abs(distanceX) <= ATTACK_RANGE
                && Math.abs(distanceY) <= 100f) {

            // Le squelette attaque s'il n'est pas déjà en train
            // d'attaquer et si son délai est écoulé.
            if (!attacking && attackTimer <= 0f) {

                attacking = true;
                attackAnimationTimer = ATTACK_DURATION;
                attackTimer = ATTACK_COOLDOWN;
                attackDamageApplied = false;
            }

        } else if (!attacking) {

            // Le joueur est trop loin : le squelette le poursuit.
            dx = (facingRight ? 1f : -1f) * DETECTION_SPEED * delta;
            moving = true;
        }

    } else if (!attacking) {

        // Le joueur n'est plus détecté : comportement habituel.
        updateIdle(delta);

        if (idleMoving) {
            dx = idleDirection * MOVE_SPEED * delta;
        }
    }

    // --- Physique ---
    body.move(map, dx, delta, false);

    // Empêche le squelette de sortir du niveau.
    body.x = map.clampX(body.x, body.w / 2f);
}

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

    public void dispose() {
        sheetWalk.dispose();
    }
}