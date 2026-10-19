package com.mygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CollisionMapTest {

    // Grille 3 lignes x 4 colonnes, cellules de 100 -> hauteur 300
    // Sol plein en bas (ligne 2), mur plein en colonne 2
    private CollisionMap map;

    @BeforeEach
    void setUp() {
        int[][] grid = {
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {1, 1, 1, 1}
        };
        map = new CollisionMap(grid, 100f);
    }

    @Test
    void isSolidDetecteLeSol() {
        assertTrue(map.isSolid(50, 50));
    }

    @Test
    void isSolidFalseDansLeVide() {
        assertFalse(map.isSolid(50, 250));
    }

    @Test
    void isSolidFalseHorsDeLaCarte() {
        assertFalse(map.isSolid(-10, 50));
        assertFalse(map.isSolid(5000, 50));
    }

    @Test
    void clampXLimiteAuxBords() {
        assertEquals(10f, map.clampX(-5f, 10f), 0.001f);
        assertEquals(390f, map.clampX(1000f, 10f), 0.001f);
        assertEquals(200f, map.clampX(200f, 10f), 0.001f);
    }

    @Test
    void findGroundTrouveLeSol() {
        float ground = map.findGround(150f, 90f, 150f, 120f, false);
        assertEquals(100f, ground, 0.001f);
    }

    @Test
    void findGroundRenvoieNoGroundDansLeVide() {
        float ground = map.findGround(150f, 200f, 250f, 250f, false);
        assertEquals(CollisionMap.NO_GROUND, ground);
    }

    @Test
    void findGroundSurUnePente() {
        CollisionMap slope = new CollisionMap(new int[][] {{2}}, 100f);
        // pente montante, milieu de la case -> moitié de la hauteur
        assertEquals(50f, slope.findGround(50f, 0f, 100f, 60f, false), 0.001f);
    }

    @Test
    void plateformeTraversableSeulementDepuisLeHaut() {
        CollisionMap plat = new CollisionMap(new int[][] {{8}}, 100f);
        // venu d'en haut : on se pose dessus
        assertEquals(100f, plat.findGround(50f, 50f, 150f, 120f, false), 0.001f);
        // dropThrough : on traverse
        assertEquals(CollisionMap.NO_GROUND, plat.findGround(50f, 50f, 150f, 120f, true));
        // venu d'en bas : on traverse
        assertEquals(CollisionMap.NO_GROUND, plat.findGround(50f, 50f, 150f, 50f, false));
    }

    @Test
    void moveXSansDeplacementRendLaMemePosition() {
        assertEquals(150f, map.moveX(150f, 0f, 100f, 10f, 115f), 0.001f);
    }

    @Test
    void moveXLibre() {
        assertEquals(60f, map.moveX(50f, 10f, 100f, 10f, 115f), 0.001f);
    }

    @Test
    void moveXBloqueParUnMur() {
        float x = map.moveX(150f, 100f, 100f, 10f, 115f);
        assertEquals(189.99f, x, 0.01f);   // colle le mur de la colonne 2
    }
}