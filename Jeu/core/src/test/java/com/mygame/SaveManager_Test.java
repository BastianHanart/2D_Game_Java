package com.mygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaveManager_Test {

    @BeforeEach
    void setUp() {
        GameState.reset();
        GameState.currentSlot = 0;
    }

    @AfterEach
    void tearDown() {
        GameState.reset();
        GameState.currentSlot = 0;
    }

    @Test
    void applyChargeLesInformationsGenerales() {
        SaveData data = new SaveData();

        data.level = 3;
        data.playerLevel = 5;
        data.xp = 7;
        data.playTime = 120f;

        SaveManager.apply(2, data);

        assertEquals(2, GameState.currentSlot);
        assertEquals(3, GameState.currentLevel);
        assertEquals(5, GameState.niveau);
        assertEquals(7, GameState.xp);
        assertEquals(120f, GameState.playTime, 0.001f);
    }

    @Test
    void applyRestaureLesTalentsEtLesPoints() {
        SaveData data = new SaveData();

        data.talents = new String[] {
            "Dash",
            "DoubleJump"
        };
        data.points = 4;

        SaveManager.apply(1, data);

        assertTrue(GameState.Skills.has(Talent.Dash));
        assertTrue(GameState.Skills.has(Talent.DoubleJump));
        assertEquals(4, GameState.Skills.getPoints()); 
    }

    @Test
    void applyRemplaceLesAnciensTalents() {
        GameState.Skills.unlock(Talent.Dash);
        assertTrue(GameState.Skills.has(Talent.Dash));

        SaveData data = new SaveData();
        data.talents = new String[] {"HPMax_1"};
        data.points = 2;

        SaveManager.apply(1, data);

        assertFalse(GameState.Skills.has(Talent.Dash));
        assertTrue(GameState.Skills.has(Talent.HPMax_1));
        assertEquals(2, GameState.Skills.getPoints());
    }

    @Test
    void saveCurrentNeFaitRienSansEmplacementSelectionne() {
        GameState.currentSlot = 0;

        assertDoesNotThrow(() ->
            SaveManager.saveCurrent(null, 1)
        );
    }
}