package com.mygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

class BodyPhysics_Test {

    private Body body;
    private CollisionMap map;

    @BeforeEach
    void setUp() {
        body = new Body();

        int[][] grid = {
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0},
            {1, 1, 1, 1}
        };

        map = new CollisionMap(grid, 100f);

        body.x = 150f;
        body.y = 100f;
    }

    @Test
    void sautAnnuleEtatAuSol() {
        body.onGround = true;

        body.jump(500f);

        assertFalse(body.onGround);
        assertEquals(500f, body.vy, 0.001f);
    }

    @Test
    void sautSousUnPlafondBloqueLaMontee() {
        body.vy = 500f;

        body.move(map, 0f, 0.016f, false);

        assertEquals(100f, body.y, 0.001f);
        assertEquals(0f, body.vy, 0.001f);
        assertFalse(body.onGround);
    }

    @Test
    void tombeEtAtterritSurLeSol() {
        body.y = 110f;
        body.vy = -100f;

        body.move(map, 0f, 0.1f, false);

        assertEquals(100f, body.y, 0.001f);
        assertEquals(0f, body.vy, 0.001f);
        assertTrue(body.onGround);
    }

    @Test
    void lePersonnageNeTraversePasLeSolEnTombant() {
        body.y = 110f;
        body.vy = -500f;

        body.move(map, 0f, 0.1f, false);

        assertEquals(100f, body.y, 0.001f);
        assertTrue(body.onGround);
    }

    @Test
    void mouvementSansTempsEcouleNeChangePasLaPosition() {
        CollisionMap emptyMap = new CollisionMap(new int[][] {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 100f);

        body.y = 100f;
        body.vy = 100f;

        emptyMap = new CollisionMap(new int[][] {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 100f);

        body.move(emptyMap, 0f, 0f, false);

        assertEquals(100f, body.y, 0.001f);
        assertEquals(100f, body.vy, 0.001f);
    }

    @Test
    void mouvementHorizontalSansDeplacementNeChangePasX() {
        body.move(map, 0f, 0.016f, false);

        assertEquals(150f, body.x, 0.001f);
    }

    @Test
    void rectSuitLesDimensionsPersonnalisees() {
        body.x = 200f;
        body.y = 50f;
        body.w = 80f;
        body.h = 120f;

        Rectangle rect = body.rect();

        assertEquals(160f, rect.x, 0.001f);
        assertEquals(50f, rect.y, 0.001f);
        assertEquals(80f, rect.width, 0.001f);
        assertEquals(120f, rect.height, 0.001f);
    }
}