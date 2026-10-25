package com.mygame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionMapEdgeCases_Test {

    @Test
    void penteDescendanteEstDetectee() {
        CollisionMap map = new CollisionMap(
            new int[][] {{3}},
            100f
        );

        float ground = map.findGround(
            25f, 0f, 100f, 0f, false
        );

        assertEquals(75f, ground, 0.001f);
    }

    @Test
    void penteDouceMontanteEstDetectee() {
        CollisionMap map = new CollisionMap(
            new int[][] {{4}},
            100f
        );

        float ground = map.findGround(
            50f, 0f, 100f, 0f, false
        );

        assertEquals(25f, ground, 0.001f);
    }

    @Test
    void deuxiemePartieDePenteDouceEstDetectee() {
        CollisionMap map = new CollisionMap(
            new int[][] {{5}},
            100f
        );

        float ground = map.findGround(
            50f, 0f, 100f, 0f, false
        );

        assertEquals(75f, ground, 0.001f);
    }

    @Test
    void penteDouceDescendanteEstDetectee() {
        CollisionMap map = new CollisionMap(
            new int[][] {{6}},
            100f
        );

        float ground = map.findGround(
            50f, 0f, 100f, 0f, false
        );

        assertEquals(75f, ground, 0.001f);
    }

    @Test
    void deuxiemePartieDePenteDescendanteEstDetectee() {
        CollisionMap map = new CollisionMap(
            new int[][] {{7}},
            100f
        );

        float ground = map.findGround(
            50f, 0f, 100f, 0f, false
        );

        assertEquals(25f, ground, 0.001f);
    }

    @Test
    void tuileInconnueNeFournitPasDeSol() {
        CollisionMap map = new CollisionMap(
            new int[][] {{9}},
            100f
        );

        float ground = map.findGround(
            50f, 0f, 100f, 0f, false
        );

        assertEquals(CollisionMap.NO_GROUND, ground);
    }

    @Test
    void deplacementVersLaGaucheEstBloqueParUnMur() {
        int[][] grid = {
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
        };

        CollisionMap map = new CollisionMap(grid, 100f);

        float x = map.moveX(
            250f, -100f, 100f, 10f, 115f
        );

        assertEquals(210.01f, x, 0.01f);
    }

    @Test
    void deplacementSansObstacleEstAutorise() {
        CollisionMap map = new CollisionMap(
            new int[][] {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
            },
            100f
        );

        float x = map.moveX(
            100f, 20f, 100f, 10f, 115f
        );

        assertEquals(120f, x, 0.001f);
    }

    @Test
    void corpsTropPetitPourLaCollisionHorizontaleAvance() {
        CollisionMap map = new CollisionMap(
            new int[][] {{1, 1, 1}},
            100f
        );

        float x = map.moveX(
            100f, 20f, 0f, 10f, 40f
        );

        assertEquals(120f, x, 0.001f);
    }

    @Test
    void caseSitueeHorsDeLaCarteNestPasSolide() {
        CollisionMap map = new CollisionMap(
            new int[][] {
                {1, 1},
                {1, 1}
            },
            100f
        );

        assertFalse(map.isSolid(-1f, 50f));
        assertFalse(map.isSolid(250f, 50f));
    }

    @Test
    void clampXConserveUnePositionDejaValide() {
        CollisionMap map = new CollisionMap(
            new int[][] {{0, 0, 0, 0}},
            100f
        );

        assertEquals(
            200f,
            map.clampX(200f, 10f),
            0.001f
        );
    }
}