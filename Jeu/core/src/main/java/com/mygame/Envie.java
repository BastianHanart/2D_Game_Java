package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Représente le péché capital Envie, le premier boss et un type de monstre présent dans le jeu; gère sa position, sa vitesse, ses attaques, sa gestion de vie et ses collisions avec la carte.
*/
public class Envie extends Monster {

    private static final int LIFE_MAX = 300;
    private static final float DETECTION_RANGE = 1400f;
    private static final float WALK_SPEED = 400f;
    private static final float RUN_SPEED = 800f;
    private static final float JUMP_FORCE = 1300f;
    private static final int MAX_JUMPS = 2;

    private static final float DASH_SPEED = 2200f;
    private static final float DASH_DURATION = 0.15f;
    private static final float DASH_COOLDOWN = 2f;
    private static final int DASH_DAMAGE = 10;

    private static final float ATTACK_RANGE = 200f;
    private static final float ATTACK_DURATION = 0.5f;
    private static final float ATTACK_COOLDOWN = 1f;
    private static final int ATTACK_DAMAGE = 15;

    private static final float SUMMON_COOLDOWN = 8f;
    private static final int MAX_SUMMONS = 4;

    private final Game game;
    private final Texture sheetWalk;
    private final TextureRegion idleFrame;
    private final TextureRegion attackFrame;

    private final Texture barFull, barEmpty;

    private boolean facingRight = false;
    private int jumpsLeft = MAX_JUMPS;
    private float lastX;
    private float stuckTimer;
    private int lastLife = LIFE_MAX;

    private boolean dashing;
    private boolean dashDamageApplied;
    private float dashTimer, dashCooldown;

    private boolean attacking;
    private boolean attackDamageApplied;
    private float attackTimer, attackCooldown;

    private float summonCooldown = SUMMON_COOLDOWN / 2f;
    private final Array<Monster> summons = new Array<>();
    private boolean summonHit;

    /**
    * @param game défini dans quelle partie le monstre est crée.
    * @see CollisionMap
    */
    public Envie(Game game) {
        super(LIFE_MAX, 100);
        this.game = game;
        sheetWalk = new Texture("Envie.png");
        idleFrame = new TextureRegion(sheetWalk, 50, 10, 240, 160);
        attackFrame = new TextureRegion(sheetWalk, 250, 410, 240, 160);
        barFull = new Texture("FullHealthBar.png");
        barEmpty = new Texture("EmptyHealthBar.png");
        body.w = 360;
        body.h = 240;
    }

    /**
    * @param delta défini le délai des actions.
    * @param map défini les collision du niveau actuel.
    * @param player défini le joueur.
    * @see Character
    * @see CollisionMap
    */
    public void update(float delta, CollisionMap map, Character player) {
        dashCooldown = Math.max(0f, dashCooldown - delta);
        attackCooldown = Math.max(0f, attackCooldown - delta);
        summonCooldown = Math.max(0f, summonCooldown - delta);
        float distanceX = player.getX() - body.x;
        float distanceY = player.getY() - body.y;
        boolean playerDetected = Math.abs(distanceX) <= DETECTION_RANGE && Math.abs(distanceY) <= DETECTION_RANGE;

        if (getLife() < lastLife && !dashing && dashCooldown <= 0f && Math.random() < 0.3) {
            startDash(distanceX < 0);
        }
        lastLife = getLife();

        if (attacking) {
            attackTimer -= delta;
            if (!attackDamageApplied && attackTimer <= ATTACK_DURATION / 2f) {
                if (getAttackBox().overlaps(player.getHurtbox())) {
                    player.takeDamage(ATTACK_DAMAGE);
                }
                attackDamageApplied = true;
            }
            if (attackTimer <= 0f) attacking = false;
        }

        float dx = 0f;

        if (dashing) {
            dashTimer -= delta;
            dx = (facingRight ? DASH_SPEED : -DASH_SPEED) * delta;
            body.vy = 0;
            if (!dashDamageApplied && getHitbox().overlaps(player.getHurtbox())) {
                player.takeDamage(DASH_DAMAGE);
                dashDamageApplied = true;
            }
            if (dashTimer <= 0f) dashing = false;
        }
        else if (playerDetected) {
            facingRight = distanceX >= 0;
            float dir = facingRight ? 1f : -1f;

            if (summonCooldown <= 0f && summons.size + 2 <= MAX_SUMMONS) {
                summon(map);
            }

            if (Math.abs(distanceX) <= ATTACK_RANGE && Math.abs(distanceY) <= 120f) {
                if (!attacking && attackCooldown <= 0f) {
                    attacking = true;
                    attackDamageApplied = false;
                    attackTimer = ATTACK_DURATION;
                    attackCooldown = ATTACK_COOLDOWN;
                }
            }
            else if (Math.abs(distanceX) > 400f && dashCooldown <= 0f) {
                startDash(facingRight);
            }
            else {
                float speed = Math.abs(distanceX) > 600f ? RUN_SPEED : WALK_SPEED;
                dx = dir * speed * delta;
            }

            if (distanceY > 150f && (body.onGround || body.vy < 0)) {
                jump();
            }
        }

        body.move(map, dx, delta, false);
        body.x = map.clampX(body.x, body.w / 2f);
        if (body.onGround) jumpsLeft = MAX_JUMPS;

        if (dx != 0f && !dashing && Math.abs(body.x - lastX) < 1f) stuckTimer += delta;
        else stuckTimer = 0f;
        if (stuckTimer > 0.15f) {
            jump();
            stuckTimer = 0f;
        }
        lastX = body.x;

        updateSummons(delta, map, player);
    }

    private void jump() {
        if (jumpsLeft > 0) {
            body.jump(JUMP_FORCE);
            jumpsLeft--;
        }
    }

    private void startDash(boolean right) {
        facingRight = right;
        dashing = true;
        dashDamageApplied = false;
        dashTimer = DASH_DURATION;
        dashCooldown = DASH_COOLDOWN;
        attacking = false;
    }

    /**
    * @return la zone d'attaque du boss.
    * @see Body
    */
    public Rectangle getAttackBox() {
        float ax = facingRight ? body.x + body.w / 2f : body.x - body.w / 2f - ATTACK_RANGE;
        return new Rectangle(ax, body.y + 20f, ATTACK_RANGE, body.h - 40f);
    }

    // =========================================== \\
    //               INVOCATIONS                   \\
    // =========================================== \\

    private void summon(CollisionMap map) {
        summonCooldown = SUMMON_COOLDOWN;

        Squelette left = new Squelette(game);
        left.setPosition(map.clampX(body.x - 250f, body.w), body.y + 50f);
        summons.add(left);

        Fantome right = new Fantome(game);
        right.setPosition(map.clampX(body.x + 250f, body.w), body.y + 200f);
        summons.add(right);
    }

    private void updateSummons(float delta, CollisionMap map, Character player) {
        if (!player.isAttacking()) summonHit = false;

        for (int i = summons.size - 1; i >= 0; i--) {
            Monster m = summons.get(i);

            if (!summonHit && player.isAttacking() && player.getAttackBox().overlaps(m.getHitbox())) {
                m.takeDamage(player.getDamage());
                summonHit = true;
            }

            if (m.isDead()) {
                disposeSummon(m);
                summons.removeIndex(i);
            }
            else if (m instanceof Squelette) ((Squelette) m).update(delta, map, player);
            else if (m instanceof Fantome) ((Fantome) m).update(delta, map, player);
        }
    }

    private void disposeSummon(Monster m) {
        if (m instanceof Squelette) ((Squelette) m).dispose();
        else if (m instanceof Fantome) ((Fantome) m).dispose();
    }

    // =========================================== \\
    //                  RENDU                      \\
    // =========================================== \\

    /**
    * @param batch défini le sprite du boss.
    * @see SpriteBatch
    */
    public void render(SpriteBatch batch) {
        for (Monster m : summons) {
            if (m instanceof Squelette) ((Squelette) m).render(batch);
            else if (m instanceof Fantome) ((Fantome) m).render(batch);
        }
        TextureRegion frame = null;

        if (attacking){
            frame = attackFrame;
        }
        else{
            frame = idleFrame;
        }

        float size = 230f;
        if (facingRight) {
            batch.draw(frame, body.x - size / 2f, body.y, body.w, body.h);
        } else {
            batch.draw(frame, body.x + size / 2f, body.y, -body.w, body.h);
        }

        float barW = 200f;
        float barH = barW * barEmpty.getHeight() / barEmpty.getWidth();
        float barX = body.x - barW / 2f;
        float barY = body.y + size + 20f;
        float lifePercent = (float) getLife() / LIFE_MAX;

        batch.draw(barEmpty, barX, barY, barW, barH);
        batch.draw(barFull, barX, barY, barW * lifePercent, barH,
                0, 0, (int) (barFull.getWidth() * lifePercent), barFull.getHeight(), false, false);
    }

    public void dispose() {
        for (Monster m : summons) disposeSummon(m);
        summons.clear();
        sheetWalk.dispose();
        barFull.dispose();
        barEmpty.dispose();
    }
}
