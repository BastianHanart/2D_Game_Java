package com.mygame;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BodyTest {

    private CollisionMap map;
    private Body body;

    @BeforeEach
    void setUp() {
        int[][] grid = {
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {1, 1, 1, 1}
        };
        map = new CollisionMap(grid, 100f);
        body = new Body();
        body.x = 150f;
        body.y = 100f;   // pieds posés sur le sol (y = 100)
    }

    @Test
    void jumpDonneUneVitesseVerticale() {
        body.onGround = true;
        body.jump(500f);
        assertEquals(500f, body.vy, 0.001f);
        assertFalse(body.onGround);
    }

    @Test
    void restePoseSurLeSol() {
        body.move(map, 0f, 0.016f, false);
        assertTrue(body.onGround);
        assertEquals(100f, body.y, 0.001f);
        assertEquals(0f, body.vy, 0.001f);
    }

    @Test
    void tombeDansLeVide() {
        body.y = 250f;
        body.move(map, 0f, 0.1f, false);
        assertFalse(body.onGround);
        assertTrue(body.y < 250f);
        assertTrue(body.vy < 0f);
    }

    @Test
    void estBloqueParUnMur() {
        body.move(map, 100f, 0.016f, false);
        // mur colonne 2 (x = 200) moins la demi-largeur 27.5
        assertEquals(172.49f, body.x, 0.05f);
    }

    @Test
    void rectCorrespondALaHitbox() {
        body.x = 100f;
        body.y = 50f;
        Rectangle r = body.rect();
        assertEquals(72.5f, r.x, 0.001f);
        assertEquals(50f, r.y, 0.001f);
        assertEquals(55f, r.width, 0.001f);
        assertEquals(115f, r.height, 0.001f);
        assertTrue(r.overlaps(new Rectangle(90, 60, 10, 10)));
    }
}