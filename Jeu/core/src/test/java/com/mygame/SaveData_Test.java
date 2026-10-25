package com.mygame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaveData_Test {

    @Test
    void versionParDefautEstUn() {
        SaveData data = new SaveData();

        assertEquals(1, data.version);
    }

    @Test
    void niveauParDefautEstUn() {
        SaveData data = new SaveData();

        assertEquals(1, data.level);
        assertEquals(1, data.playerLevel);
    }

    @Test
    void potionDeVieParDefautEstUne() {
        SaveData data = new SaveData();

        assertEquals(1, data.health_potion);
    }

    @Test
    void experienceParDefautEstNulle() {
        SaveData data = new SaveData();

        assertEquals(0, data.xp);
    }

    @Test
    void pointsDeTalentParDefautSontNuls() {
        SaveData data = new SaveData();

        assertEquals(0, data.points);
    }

    @Test
    void talentsSontVidesAuDepart() {
        SaveData data = new SaveData();

        assertNotNull(data.talents);
        assertEquals(0, data.talents.length);
    }

    @Test
    void positionParDefautEstNulle() {
        SaveData data = new SaveData();

        assertEquals(0f, data.x, 0.001f);
        assertEquals(0f, data.y, 0.001f);
    }

    @Test
    void pointsDeVieParDefautSontNuls() {
        SaveData data = new SaveData();

        assertEquals(0f, data.hp, 0.001f);
    }

    @Test
    void tempsDeJeuParDefautEstNul() {
        SaveData data = new SaveData();

        assertEquals(0f, data.playTime, 0.001f);
    }

    @Test
    void timestampParDefautEstNul() {
        SaveData data = new SaveData();

        assertEquals(0L, data.timestamp);
    }

    @Test
    void lesChampsPeuventEtreRenseignes() {
        SaveData data = new SaveData();

        data.level = 3;
        data.x = 120f;
        data.y = 250f;
        data.hp = 75f;
        data.xp = 8;
        data.points = 2;
        data.talents = new String[]{"Dash"};

        assertEquals(3, data.level);
        assertEquals(120f, data.x, 0.001f);
        assertEquals(250f, data.y, 0.001f);
        assertEquals(75f, data.hp, 0.001f);
        assertEquals(8, data.xp);
        assertEquals(2, data.points);
        assertArrayEquals(new String[]{"Dash"}, data.talents);
    }
}