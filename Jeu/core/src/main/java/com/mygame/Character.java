package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Character {
        
    private Game game;
    private SkillTree skills = GameState.Skills;
    // ===========================================
    //                 STATS
    // ===========================================

    protected float HPMax = 100;
    protected float HP = 100;
    protected int damage = 5;
    protected int range = 200;

    public float getHPMax() { 
        return this.HPMax; 
    }
    public float getHP() { 
        return this.HP; 
    }

    // ---------- Constantes ----------
    private static final float WALK_SPEED = 400f;
    private static final float RUN_SPEED = 800f;
    private static final float JUMP_FORCE = 1300f;
    private static final float BODY_W = 55f;
    private static final float BODY_H = 115f;
    private static final float SPRITE_SCALE = 1.5f;

    private static final float DASH_SPEED = 2200f;
    private static final float DASH_DURATION = 0.15f;
    private static final float DASH_COOLDOWN = 1.0f;
    private static final float ATTACK_DURATION = 0.2f;
    private static final float LANDING_DURATION = 0.2f;

    // ---------- Etat ----------
    private final Body body = new Body();
    private boolean facingRight = true;
    private boolean movingLeft, movingRight;

    private boolean dashing;
    private float dashTimer, dashCooldownTimer;


    // =========================================== \\
    //                 SAUT                        \\
    // =========================================== \\

    private int maxJumps = 1;
    private int jumpsLeft = 1;

    private float landingTimer;
    private float animationTime;


    // =========================================== \\
    //                 ATTAQUE                     \\
    // =========================================== \\

    private boolean attacking;
    private float attackTimer;

    // =========================================== \\
    //                SPRITE                       \\
    // =========================================== \\

    private final Texture sheetWalk, sheetJump, sheetAttack, sheetDash;
    private final Animation<TextureRegion> walkRight, walkLeft;
    private final TextureRegion[] jumpRight = new TextureRegion[3];
    private final TextureRegion[] jumpLeft = new TextureRegion[3];
    private final TextureRegion attackRight, attackLeft, dashRight, dashLeft;

    // =========================================== \\
    //              CONSTRUCTEUR                   \\
    // =========================================== \\

    public Character(Game game) {
        this.game = game;
        sheetWalk = new Texture("Personnage_Marche.png");
        TextureRegion[][] walk = TextureRegion.split(sheetWalk, 100, 134);
        TextureRegion[] rf = new TextureRegion[3];
        TextureRegion[] lf = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            rf[i] = walk[0][i];
            lf[i] = walk[0][i + 3];
        }
        walkRight = new Animation<>(0.15f, rf);
        walkLeft = new Animation<>(0.15f, lf);
        walkRight.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        sheetJump = new Texture("Personnage_Jump.png");
        TextureRegion[][] jump = TextureRegion.split(sheetJump, 103, 134);
        for (int i = 0; i < 3; i++) {
            jumpRight[i] = jump[0][i];
            jumpLeft[i] = jump[0][i + 3];
        }

        sheetAttack = new Texture("Personnage_Attaque.png");
        TextureRegion[][] atk = TextureRegion.split(sheetAttack, 230, 134);
        attackRight = atk[0][0];
        attackLeft = atk[0][1];

        sheetDash = new Texture("Personnage_Dash.png");
        TextureRegion[][] dash = TextureRegion.split(sheetDash, 150, 134);
        dashRight = dash[0][0];
        dashLeft = dash[0][1];

        body.w = BODY_W;
        body.h = BODY_H;

        recalculerStats();
        HP = HPMax;  
    }

    // =========================================== \\
    //                POSITION                     \\
    // =========================================== \\

    public Body getBody() { 
        return body; 
    }
    public float getX() { 
        return body.x; 
    }
    public float getY() {
        return body.y;
    }

    public void setPosition(float x, float y) {
        body.x = x;
        body.y = y;
        body.vy = 0;
    }

    public boolean isDead() {
        return HP <= 0;
    }

    public void recalculerStats() {
        HPMax  = 100 + (skills.has(Talent.HPMax_1) ? 10 : 0) + (skills.has(Talent.HPMax_2) ? 20 : 0) + (skills.has(Talent.HPMax_3) ? 30 : 0) + (skills.has(Talent.HPMax_4) ? 40 : 0) + (skills.has(Talent.HPMax_5) ? 50 : 0);
        damage = 5 + (skills.has(Talent.Damage_1) ? 3 : 0) + (skills.has(Talent.Damage_2) ? 5 : 0) + (skills.has(Talent.Damage_3) ? 10 : 0) + (skills.has(Talent.Damage_4) ? 15 : 0) + (skills.has(Talent.Damage_5) ? 20 : 0);
        maxJumps = skills.has(Talent.DoubleJump) ? 2 : 1;
    }

    // =========================================== \\
    //                  UPDATE                     \\
    // =========================================== \\

    public void update(float delta, CollisionMap map) {

        if (this.HP > this.HPMax){
            this.HP = this.HPMax;
        }
        if (this.HP < 0){
            this.HP = 0;
        }

        // --- Timers ---
        dashCooldownTimer = Math.max(0f, dashCooldownTimer - delta);
        landingTimer = Math.max(0f, landingTimer - delta);

        if (attacking) {
            attackTimer -= delta;
            if (attackTimer <= 0f) attacking = false;
        }
        if (dashing) {
            dashTimer -= delta;
            if (dashTimer <= 0f) dashing = false;
        }

        // --- Actions ---
        boolean attackPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        boolean second_attackPressed = Gdx.input.isKeyJustPressed(Settings.second_attackKey);
        
        if ((attackPressed || second_attackPressed) && !attacking && !dashing) {
            attacking = true;
            attackTimer = ATTACK_DURATION;
        }

        if (skills.has(Talent.Dash) && Gdx.input.isKeyJustPressed(Settings.dashKey) && !dashing && dashCooldownTimer <= 0f){
            dashing = true;
            dashTimer = DASH_DURATION;
            dashCooldownTimer = DASH_COOLDOWN;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            this.HP -= 10;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            this.HP += 10;
        }


        if (Gdx.input.isKeyJustPressed(Settings.menuKey)) {
            game.setScreen(new MenuScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Settings.TalentTree)) {
            game.setScreen(new SkillTreeScreen(game, game.getScreen()));
            return;
        }
        
        // =========================================== \\
        //                 DEPLACEMENT                 \\
        // =========================================== \\

        boolean left = Gdx.input.isKeyPressed(Settings.leftKey);
        boolean right = Gdx.input.isKeyPressed(Settings.rightKey);
        boolean run = Gdx.input.isKeyPressed(Settings.runKey);
        float speed = run ? RUN_SPEED : WALK_SPEED;

        float dx = 0f;
        movingLeft = false;
        movingRight = false;

        if (dashing) {
            dx = (facingRight ? DASH_SPEED : -DASH_SPEED) * delta;
            body.vy = 0;

        }
        else if (!attacking) {
            if (left && !right) {
                dx = -speed * delta;
                facingRight = false;
                movingLeft = true;
            } 
            else if (right && !left) {
                dx = speed * delta;
                facingRight = true;
                movingRight = true;
            }
        }
        else if (attacking) { 
            if (left && !right) {
                dx = (-speed * delta) / 1.5f;
                facingRight = false;
                movingLeft = true;
            } 
            else if (right && !left) {
                dx = (speed * delta) / 1.5f;
                facingRight = true;
                movingRight = true;
            }
        }

        // --- Saut ---
        if (Gdx.input.isKeyJustPressed(Settings.jumpKey) && jumpsLeft > 0) {
            body.jump(JUMP_FORCE);
            jumpsLeft--;
        }

    // =========================================== \\
    //                   CORPS                     \\
    // =========================================== \\
        body.w = BODY_W;
        body.h = BODY_H;

        // --- Physique (murs, sol, pentes, plateformes) ---
        boolean wasOnGround = body.onGround;
        body.move(map, dx, delta, Gdx.input.isKeyPressed(Settings.downKey));

        if (body.onGround) {
            jumpsLeft = maxJumps;
            if (!wasOnGround) landingTimer = LANDING_DURATION;
        }

        // --- Animation de marche ---
        if (movingLeft || movingRight) animationTime += delta;
        else animationTime = 0f;
    }

    // =========================================== \\
    //                  HITBOX                     \\
    // =========================================== \\

    /** Zone où le personnage peut recevoir des dégâts. */
    public Rectangle getHurtbox() { 
        return body.rect(); 
    }

    /** Zone qui blesse les monstres (seulement pendant l'attaque). */
    public Rectangle getAttackBox() {
        float ax = facingRight ? body.x + body.w / 2f : body.x - body.w / 2f - range;
        return new Rectangle(ax, body.y + 20f, range, body.h - 40f);
    }

    public boolean isAttacking() { 
        return attacking; 
    }
    public int getDamage() {
        return damage; 
    }

    public void takeDamage(int damage) {
        this.HP -= damage;

        if (this.HP < 0) {
            this.HP = 0;
        }
        System.out.println("Joueur touché ! Vie restante : " + this.HP);
    }

    // =========================================== \\
    //                  RENDU                      \\
    // =========================================== \\

    public void render(SpriteBatch batch) {
        TextureRegion frame;

        if (dashing) {
            frame = facingRight ? dashRight : dashLeft;
        } 
        else if (attacking) {
            frame = facingRight ? attackRight : attackLeft;
        } 
        else if (!body.onGround) {
            boolean rising = body.vy > 0;
            if (facingRight) 
                frame = rising ? jumpRight[0] : jumpRight[1];
            else 
                frame = rising ? jumpLeft[2] : jumpLeft[1];
        } 
        else if (landingTimer > 0f) {
            frame = facingRight ? jumpRight[2] : jumpLeft[0];
        } 
        else if (movingRight) {
            frame = walkRight.getKeyFrame(animationTime);
        } 
        else if (movingLeft) {
            frame = walkLeft.getKeyFrame(animationTime);
        } 
        else {
            frame = facingRight ? walkRight.getKeyFrames()[1] : walkLeft.getKeyFrames()[1];
        }

        float w = frame.getRegionWidth() * SPRITE_SCALE;
        float h = frame.getRegionHeight() * SPRITE_SCALE;
        // Sprite centré sur les pieds
        batch.draw(frame, body.x - w / 2f, body.y, w, h);
    }

    public void dispose() {
        sheetWalk.dispose();
        sheetJump.dispose();
        sheetAttack.dispose();
        sheetDash.dispose();
    }
}