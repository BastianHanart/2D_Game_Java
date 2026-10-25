package com.mygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameState_Test {

    @BeforeEach
    void setUp() {
        // Réinitialise l'état global avant chaque test.
        GameState.reset();
    }

    @Test
    void xpRequisEstDeDix() {
        assertEquals(10, GameState.xpRequis());
    }

    @Test
    void resetInitialiseLeNiveauAUn() {
        assertEquals(1, GameState.niveau);
    }

    @Test
    void resetInitialiseLExperienceAZero() {
        assertEquals(0, GameState.xp);
    }

    @Test
    void resetInitialiseLeNiveauActuelAUn() {
        assertEquals(1, GameState.currentLevel);
    }

    @Test
    void resetInitialiseLeTempsDeJeuAZero() {
        assertEquals(0f, GameState.playTime, 0.001f);
    }

    @Test
    void moinsDeDixXpNeFaitPasMonterDeNiveau() {
        boolean levelUp = GameState.addXp(5);

        assertFalse(levelUp);
        assertEquals(1, GameState.niveau);
        assertEquals(5, GameState.xp);
    }

    @Test
    void exactementDixXpFaitMonterDeNiveau() {
        boolean levelUp = GameState.addXp(10);

        assertTrue(levelUp);
        assertEquals(2, GameState.niveau);
        assertEquals(0, GameState.xp);
    }

    @Test
    void lePassageDeNiveauDonneUnPointDeTalent() {
        int pointsAvant = GameState.Skills.getPoints();

        GameState.addXp(10);

        assertEquals(pointsAvant + 1, GameState.Skills.getPoints());
    }

    @Test
    void lexperienceRestanteEstConserveeApresUnNiveau() {
        GameState.addXp(13);

        assertEquals(2, GameState.niveau);
        assertEquals(3, GameState.xp);
    }

    @Test
    void lexperiencePeutEtreAccumuleeSurPlusieursGains() {
        GameState.addXp(4);
        GameState.addXp(3);

        assertEquals(1, GameState.niveau);
        assertEquals(7, GameState.xp);
    }

    @Test
    void plusieursGainsPeuventFaireMonterDeNiveau() {
        GameState.addXp(4);
        boolean levelUp = GameState.addXp(6);

        assertTrue(levelUp);
        assertEquals(2, GameState.niveau);
        assertEquals(0, GameState.xp);
    }

    @Test
    void unGainDeVingtCinqXpFaitMonterDeDeuxNiveaux() {
        boolean levelUp = GameState.addXp(25);

        assertTrue(levelUp);
        assertEquals(3, GameState.niveau);
        assertEquals(5, GameState.xp);
    }

    @Test
    void plusieursNiveauxDonnesPlusieursPointsDeTalent() {
        GameState.addXp(30);

        assertEquals(4, GameState.niveau);
        assertEquals(3, GameState.Skills.getPoints());
    }

    @Test
    void unGainNulNeChangePasLaProgression() {
        boolean levelUp = GameState.addXp(0);

        assertFalse(levelUp);
        assertEquals(1, GameState.niveau);
        assertEquals(0, GameState.xp);
        assertEquals(0, GameState.Skills.getPoints());
    }

    @Test
    void unGainInferieurAuSeuilNeDonnePasDePointDeTalent() {
        GameState.addXp(9);

        assertEquals(0, GameState.Skills.getPoints());
    }

    @Test
    void addXpRetourneFalseSansPassageDeNiveau() {
        assertFalse(GameState.addXp(9));
    }

    @Test
    void addXpRetourneTrueLorsDuPassageDeNiveau() {
        assertTrue(GameState.addXp(10));
    }

    @Test
    void lesGainsSuccessifsConserventLeBonNombreDePoints() {
        GameState.addXp(10);
        GameState.addXp(10);
        GameState.addXp(10);

        assertEquals(4, GameState.niveau);
        assertEquals(3, GameState.Skills.getPoints());
    }

    @Test
    void resetEffaceLExperienceEtLesNiveauxGagnes() {
        GameState.addXp(25);

        GameState.reset();

        assertEquals(1, GameState.niveau);
        assertEquals(0, GameState.xp);
    }

    @Test
    void resetEffaceLesPointsDeTalent() {
        GameState.addXp(30);

        GameState.reset();

        assertEquals(0, GameState.Skills.getPoints());
    }

    @Test
    void resetRemetLeTempsDeJeuAZero() {
        GameState.playTime = 120.5f;

        GameState.reset();

        assertEquals(0f, GameState.playTime, 0.001f);
    }

    @Test
    void resetEffaceLesTalentsDebloques() {
        GameState.Skills.addPoints(1);
        assertTrue(GameState.Skills.unlock(Talent.Dash));

        GameState.reset();

        assertFalse(GameState.Skills.has(Talent.Dash));
        assertEquals(0, GameState.Skills.getPoints());
    }
}