package com.mygame;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {

    private Monster monster;

    @BeforeEach
    void setUp() {
        // Le Game n'est jamais utilisé par la logique testée : null suffit
        monster = new Monster(null, 10, 25);
    }

    @Test
    void initialiseLaVieEtLXP() {
        assertEquals(10, monster.getLife());
        assertEquals(25, monster.getXPReward());
        assertFalse(monster.isDead());
    }

    @Test
    void takeDamageReduitLaVie() {
        monster.takeDamage(4);
        assertEquals(6, monster.getLife());
        assertFalse(monster.isDead());
    }

    @Test
    void meurtQuandLaVieAtteintZero() {
        monster.takeDamage(10);
        assertEquals(0, monster.getLife());
        assertTrue(monster.isDead());
    }

    @Test
    void laVieNeDescendPasSousZero() {
        monster.takeDamage(999);
        assertEquals(0, monster.getLife());
        assertTrue(monster.isDead());
    }

    @Test
    void plusieursCoupsCumulesTuent() {
        monster.takeDamage(3);
        monster.takeDamage(3);
        monster.takeDamage(4);
        assertTrue(monster.isDead());
    }

    @Test
    void setPositionPlaceLeMonstreEtReinitialiseLaPhysique() {
        monster.body.vy = 300f;
        monster.body.onGround = true;

        monster.setPosition(120f, 340f);

        assertEquals(120f, monster.getX(), 0.001f);
        assertEquals(340f, monster.getY(), 0.001f);
        assertEquals(0f, monster.body.vy, 0.001f);
        assertFalse(monster.body.onGround);
    }

    @Test
    void getHitboxSuitLaPosition() {
        monster.setPosition(100f, 50f);
        Rectangle hitbox = monster.getHitbox();

        assertEquals(100f - monster.body.w / 2f, hitbox.x, 0.001f);
        assertEquals(50f, hitbox.y, 0.001f);
        assertEquals(monster.body.w, hitbox.width, 0.001f);
        assertEquals(monster.body.h, hitbox.height, 0.001f);
    }

    @Test
    void hitboxChevaucheUneAttaqueProche() {
        monster.setPosition(100f, 50f);
        assertTrue(monster.getHitbox().overlaps(new Rectangle(90, 60, 20, 20)));
        assertFalse(monster.getHitbox().overlaps(new Rectangle(500, 500, 20, 20)));
    }
}