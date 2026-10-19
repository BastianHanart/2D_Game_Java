package com.mygame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Villageois {
    private final Texture sheetWalk;
    private final TextureRegion idleFrame;
    // private final TextureRegion walkFrame;
    private Game game;

    protected final Body body = new Body();


    public Villageois(Game game) {
        this.game = game;
        sheetWalk = new Texture("PNJ.png");

        idleFrame = new TextureRegion(sheetWalk, 50, 50, 500, 290);
        // walkFrame = new TextureRegion(sheetWalk, 160, 25, 100, 120);
    }

    public Rectangle getHitbox() {
        return body.rect();
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
        body.onGround = false;
    }

    public void update(float delta, CollisionMap map, Character player) {
        float dx = 0f;
        body.move(map, dx, delta, false);

        // Empêche le squelette de sortir du niveau.
        body.x = map.clampX(body.x, body.w / 2f);
    }

    public void render(SpriteBatch batch) {

        TextureRegion frame = idleFrame;

        float spriteWidth = 345f;
        float spriteHeight = 200f;

        // if (facingRight) {
        //     batch.draw(frame, body.x - spriteWidth / 2f, body.y, spriteWidth, spriteHeight);
        // } 
        // else {
        //     batch.draw(frame, body.x + spriteWidth / 2f, body.y, -spriteWidth, spriteHeight);
        // }
        batch.draw(frame, body.x - spriteWidth / 2f, body.y, spriteWidth, spriteHeight);
    }

    public void dispose() {
        sheetWalk.dispose();
    }
}