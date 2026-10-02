package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Character {

    // ===========================================
    //                 STATS
    // ===========================================

    protected int life = 100;
    protected int damage = 1;
    protected int range = 200;
    protected int luck = 0;
    private boolean hasHit = false;
    protected float critical_rate = 0.0f;
    protected float critical_damage = 2;
    private boolean attacking = false;
    private float attackTime = 0f;
    private final float attackDuration = 0.2f;
    private float speed;
    private float runSpeed;

    // ===========================================
    //              DEPLACEMENT
    // ===========================================

    boolean moving_left = false;
    boolean moving_right = false;
    private boolean dashing = false;
    private float dashEndX;
    private float dashSpeed = 1000f;
    private boolean facingRight = true;
    private float dashCooldown = 1.0f;
    private float dashCooldownTimer = 0f;

    // ===========================================
    //                SPRITE
    // ===========================================

    private Texture spriteSheet_marche;
    private TextureRegion dashRightFrame;
    private TextureRegion dashLeftFrame;
    private Texture spriteSheet_jump;
    private Texture spriteSheet_dash;
    private Animation<TextureRegion> walkRightAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private TextureRegion[] jumpRightFrames;
    private TextureRegion[] jumpLeftFrames;
    private float landingAnimationTime = 0f;
    private final float landingAnimationDuration = 0.2f;
    private float animationTime = 0f;

    // ===========================================
    //               POSITION
    // ===========================================

    private float x;
    private float y;

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // ===========================================
    //                 SAUT
    // ===========================================

    private boolean doubleJumpUnlocked = false;
    private int maxJumps = 1;
    private int jumpsLeft;

    public void unlockDoubleJump() {
        doubleJumpUnlocked = true;
        maxJumps = 2;
    }

    // ===========================================
    //                PHYSIQUE
    // ===========================================

    private float verticalSpeed;
    private float gravity;
    private float jumpForce;
    private boolean onGround;

    // ===========================================
    //                HITBOX
    // ===========================================

    private int width;
    private int height;

    // ===========================================
    //              CONSTRUCTEUR
    // ===========================================

    public Character() {
        // ------ Sprite ------ //
        spriteSheet_marche = new Texture("Personnage_Marche.png");
        spriteSheet_jump = new Texture("Personnage_Jump.png");
        spriteSheet_dash = new Texture("Personnage_Dash.png");
        TextureRegion[][] tmp_dash = TextureRegion.split(spriteSheet_dash, 165, 134);
        dashRightFrame = tmp_dash[0][0];
        dashLeftFrame = tmp_dash[0][1];
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet_marche,100,134);
        TextureRegion[] rightFrames = new TextureRegion[3];
        TextureRegion[] leftFrames = new TextureRegion[3];
        TextureRegion[][] tmp_jump = TextureRegion.split(spriteSheet_jump, 103, 134); 
        jumpRightFrames = new TextureRegion[3];
        jumpLeftFrames = new TextureRegion[3];

        for (int i = 0; i < 3; i++) {
            jumpRightFrames[i] = tmp_jump[0][i];
        }

        for (int i = 0; i < 3; i++) {
            jumpLeftFrames[i] = tmp_jump[0][i + 3];
        }

        for (int i = 0; i < 3; i++) {
            rightFrames[i] = tmp[0][i];
        }

        for (int i = 0; i < 3; i++) {
            leftFrames[i] = tmp[0][i + 3];
        }

        walkRightAnimation = new Animation<>(
            0.15f,
            rightFrames
        );

        walkLeftAnimation = new Animation<>(
            0.15f,
            leftFrames
        );

        walkRightAnimation.setPlayMode(
            Animation.PlayMode.LOOP
        );

        walkLeftAnimation.setPlayMode(
            Animation.PlayMode.LOOP
        );

        // Position initiale
        x = 300;
        y = 100;

        // Vitesse
        speed = 200;
        runSpeed = 400;

        // Physique
        verticalSpeed = 0;
        gravity = -3250;
        jumpForce = 1300;

        // Sol
        onGround = false;

        // Sauts
        maxJumps = 1;
        jumpsLeft = maxJumps;

        // HitBox
        width = 85;
        height = 130;
    }

    // ===========================================
    //                  UPDATE
    // ===========================================

    public void update(float delta) {

        // ===========================================
        //             DEPLACEMENTS
        // ===========================================

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moving_left = true;
            moving_right = false;
            facingRight = false;

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                x -= runSpeed * delta;
            } else {
                x -= speed * delta;
            }
        } else {
            moving_left = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moving_right = true;
            moving_left = false;
            facingRight = true;

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                x += runSpeed * delta;
            } else {
                x += speed * delta;
            }
        } else {
            moving_right = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !dashing && dashCooldownTimer <= 0) {
            dashing = true;

            if (facingRight) {
                dashEndX = x + 200;
            } else {
                dashEndX = x - 200;
            }
            dashCooldownTimer = dashCooldown;
        }

        if (dashCooldownTimer > 0) {
            dashCooldownTimer -= delta;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && !attacking) {
            attacking = true;
            hasHit = false;
            attackTime = attackDuration;
        }

        if (dashing) {
        
            if (facingRight) {
                x += dashSpeed * delta;
            
                if (x >= dashEndX) {
                    x = dashEndX;
                    dashing = false;
                }
            
            } else {
                x -= dashSpeed * delta;
            
                if (x <= dashEndX) {
                    x = dashEndX;
                    dashing = false;
                }
            }
        }

        if (attacking) {
            attackTime -= delta;

            if (attackTime <= 0) {
                attackTime = 0;
                attacking = false;
            }
        }

        if (moving_left || moving_right) {
            animationTime += delta;
        } else {
            animationTime = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && jumpsLeft > 0) {
            verticalSpeed = jumpForce;
            jumpsLeft--;
            onGround = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            unlockDoubleJump();
        }

        verticalSpeed += gravity * delta;
        y += verticalSpeed * delta;

        if (landingAnimationTime > 0) {
            landingAnimationTime -= delta;
        }
    }

    // ===========================================
    //                  HITBOX
    // ===========================================

    public Rectangle getHitbox() {
        return new Rectangle(x,y,width,height);
    }

    public Rectangle getAttackHitbox() {
        if (!attacking) {
            return new Rectangle(0, 0, 0, 0);
        }

        if (facingRight) {
            return new Rectangle(
                x + width,
                y + 20,
                range,
                height - 40
            );
        } else {
            return new Rectangle(
                x - range,
                y + 20,
                range,
                height - 40
            );
        }
    }

    public int getDamage() {
        return damage;
    }

    public boolean isAttacking() {
        return attacking;
    }

    // ===========================================
    //             VITESSE VERTICALE
    // ===========================================

    public float getVerticalSpeed() {
        return verticalSpeed;
    }

    public void setVerticalSpeed(float verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    // ===========================================
    //                  SOL
    // ===========================================

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        // Détection de l'atterrissage
        if (onGround && !this.onGround) {
            landingAnimationTime = landingAnimationDuration;
        }

        this.onGround = onGround;

        if (onGround) {
            jumpsLeft = maxJumps;
        }
    }

    // ===========================================
    //                  RENDU
    // ===========================================

    public void render(SpriteBatch batch) {

        TextureRegion currentFrame;

        // ===========================================
        //                DASH
        // ===========================================

        if (dashing) {

            if (facingRight) {
                currentFrame = dashRightFrame;
            } else {
                currentFrame = dashLeftFrame;
            }

        // ===========================================
        //           ANIMATION DE SAUT
        // ===========================================

        } else if (!onGround) {

            if (facingRight) {

                if (verticalSpeed > 0) {
                    currentFrame = jumpRightFrames[0];
                    this.width = 55;
                    this.height = 115;
                } else {
                    currentFrame = jumpRightFrames[1];
                    this.width = 55;
                    this.height = 115;
                }

            } else {

                if (verticalSpeed > 0) {
                    currentFrame = jumpLeftFrames[0];
                    this.width = 55;
                    this.height = 115;
                } else {
                    currentFrame = jumpLeftFrames[1];
                    this.width = 55;
                    this.height = 115;
                }
            }

        // ===========================================
        //              ATTERRISSAGE
        // ===========================================

        } else if (landingAnimationTime > 0) {

            if (facingRight) {
                currentFrame = jumpRightFrames[2];
            } else {
                currentFrame = jumpLeftFrames[2];
            }

        // ===========================================
        //              ANIMATION MARCHE
        // ===========================================

        } else if (moving_right) {

            this.width = 55;
            this.height = 115;
            currentFrame = walkRightAnimation.getKeyFrame(animationTime);

        } else if (moving_left) {

            this.width = 55;
            this.height = 115;
            currentFrame = walkLeftAnimation.getKeyFrame(animationTime);

        // ===========================================
        //                    IDLE
        // ===========================================

        } else {

            if (facingRight) {
                currentFrame = walkRightAnimation.getKeyFrames()[1];
            } else {
                currentFrame = walkLeftAnimation.getKeyFrames()[1];
            }
        }

        batch.draw(currentFrame, x, y);
    }

    // ===========================================
    //                 DISPOSE
    // ===========================================

    public void dispose() {
        spriteSheet_marche.dispose();
        spriteSheet_jump.dispose();
        spriteSheet_dash.dispose();
    }
}