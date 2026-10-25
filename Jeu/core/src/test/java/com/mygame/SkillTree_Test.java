package com.mygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkillTree_Test {

    private SkillTree skills;

    @BeforeEach
    void setUp() {
        skills = new SkillTree();
    }

    @Test
    void arbreCommenceAvecZeroPoint() {
        assertEquals(0, skills.getPoints());
    }

    @Test
    void ajouterDesPointsAugmenteLeSolde() {
        skills.addPoints(5);

        assertEquals(5, skills.getPoints());
    }

    @Test
    void plusieursAjoutsDePointsSeCumulent() {
        skills.addPoints(2);
        skills.addPoints(3);

        assertEquals(5, skills.getPoints());
    }

    @Test
    void aucunTalentNestDebloqueAuDepart() {
        for (Talent talent : Talent.values()) {
            assertFalse(skills.has(talent));
        }
    }

    @Test
    void impossibleDeDebloquerUnTalentSansPoints() {
        assertFalse(skills.canUnlock(Talent.Dash));
    }

    @Test
    void impossibleDeDebloquerUnTalentSansPointsAvecUnlock() {
        assertFalse(skills.unlock(Talent.Dash));
        assertFalse(skills.has(Talent.Dash));
    }

    @Test
    void talentSansPrerequisPeutEtreDebloque() {
        skills.addPoints(1);

        assertTrue(skills.canUnlock(Talent.Dash));
    }

    @Test
    void debloquerDashConsommeUnPoint() {
        skills.addPoints(2);

        assertTrue(skills.unlock(Talent.Dash));

        assertTrue(skills.has(Talent.Dash));
        assertEquals(1, skills.getPoints());
    }

    @Test
    void debloquerUnTalentNonAccessibleNeConsommePasDePoints() {
        skills.addPoints(10);

        assertFalse(skills.unlock(Talent.DoubleJump));

        assertEquals(10, skills.getPoints());
        assertFalse(skills.has(Talent.DoubleJump));
    }

    @Test
    void impossibleDeDebloquerDeuxFoisLeMemeTalent() {
        skills.addPoints(2);

        assertTrue(skills.unlock(Talent.Dash));
        assertFalse(skills.unlock(Talent.Dash));

        assertEquals(1, skills.getPoints());
    }

    @Test
    void doubleJumpNecessiteDash() {
        skills.addPoints(2);

        assertFalse(skills.canUnlock(Talent.DoubleJump));
    }

    @Test
    void doubleJumpNecessiteDeuxPointsDisponiblesApresDash() {
        skills.addPoints(2);
        assertTrue(skills.unlock(Talent.Dash));

        // Il ne reste qu'un point.
        assertFalse(skills.canUnlock(Talent.DoubleJump));
        assertEquals(1, skills.getPoints());
    }

    @Test
    void doubleJumpPeutEtreDebloqueApresDashAvecAssezDePoints() {
        skills.addPoints(3);

        assertTrue(skills.unlock(Talent.Dash));
        assertTrue(skills.canUnlock(Talent.DoubleJump));
        assertTrue(skills.unlock(Talent.DoubleJump));

        assertTrue(skills.has(Talent.DoubleJump));
        assertEquals(0, skills.getPoints());
    }

    @Test
    void hpMaxDeuxNecessiteHpMaxUn() {
        skills.addPoints(3);

        assertFalse(skills.canUnlock(Talent.HPMax_2));

        assertTrue(skills.unlock(Talent.HPMax_1));
        assertTrue(skills.canUnlock(Talent.HPMax_2));
    }

    @Test
    void hpMaxDeuxConsommeDeuxPoints() {
        skills.addPoints(3);
        skills.unlock(Talent.HPMax_1);

        assertTrue(skills.unlock(Talent.HPMax_2));

        assertEquals(0, skills.getPoints());
        assertTrue(skills.has(Talent.HPMax_2));
    }

    @Test
    void hpMaxTroisNecessiteHpMaxDeux() {
        skills.addPoints(5);

        assertFalse(skills.canUnlock(Talent.HPMax_3));

        assertTrue(skills.unlock(Talent.HPMax_1));
        assertTrue(skills.unlock(Talent.HPMax_2));

        assertTrue(skills.canUnlock(Talent.HPMax_3));
    }

    @Test
    void damageDeuxNecessiteDamageUn() {
        skills.addPoints(3);

        assertFalse(skills.canUnlock(Talent.Damage_2));

        assertTrue(skills.unlock(Talent.Damage_1));
        assertTrue(skills.canUnlock(Talent.Damage_2));
    }

    @Test
    void damageTroisNecessiteSesDeuxPrerequis() {
        skills.addPoints(20);

        // On débloque Damage I puis Damage II.
        assertTrue(skills.unlock(Talent.Damage_1));
        assertTrue(skills.unlock(Talent.Damage_2));

        // Damage III nécessite également DoubleJump.
        assertFalse(skills.canUnlock(Talent.Damage_3));

        assertTrue(skills.unlock(Talent.Dash));
        assertTrue(skills.unlock(Talent.DoubleJump));

        assertTrue(skills.canUnlock(Talent.Damage_3));
    }

    @Test
    void damageTroisNePeutPasEtreDebloqueAvecUnSeulPrerequis() {
        skills.addPoints(20);

        assertTrue(skills.unlock(Talent.Damage_1));
        assertTrue(skills.unlock(Talent.Damage_2));

        assertFalse(skills.unlock(Talent.Damage_3));
        assertFalse(skills.has(Talent.Damage_3));
    }

    @Test
    void unlockedNamesEstVideAuDepart() {
        assertEquals(0, skills.unlockedNames().length);
    }

    @Test
    void unlockedNamesContientLesTalentsDebloques() {
        skills.addPoints(2);
        skills.unlock(Talent.Dash);

        List<String> names = Arrays.asList(skills.unlockedNames());

        assertTrue(names.contains("Dash"));
        assertEquals(1, names.size());
    }

    @Test
    void resetEffaceTousLesTalents() {
        skills.addPoints(3);
        skills.unlock(Talent.Dash);

        skills.reset();

        assertFalse(skills.has(Talent.Dash));
        assertEquals(0, skills.unlockedNames().length);
    }

    @Test
    void resetEffaceLesPointsRestants() {
        skills.addPoints(5);

        skills.reset();

        assertEquals(0, skills.getPoints());
    }

    @Test
    void restoreRestaureLesPoints() {
        skills.restore(new String[0], 7);

        assertEquals(7, skills.getPoints());
    }

    @Test
    void restoreRestaureUnTalentDebloque() {
        skills.restore(new String[]{"Dash"}, 4);

        assertTrue(skills.has(Talent.Dash));
        assertEquals(4, skills.getPoints());
    }

    @Test
    void restoreIgnoreUnNomDeTalentInconnu() {
        skills.restore(new String[]{"Dash", "TalentInexistant"}, 3);

        assertTrue(skills.has(Talent.Dash));
        assertEquals(3, skills.getPoints());
    }

    @Test
    void restoreRemplaceLesAnciensTalents() {
        skills.addPoints(1);
        skills.unlock(Talent.Dash);

        skills.restore(new String[]{"HPMax_1"}, 2);

        assertFalse(skills.has(Talent.Dash));
        assertTrue(skills.has(Talent.HPMax_1));
        assertEquals(2, skills.getPoints());
    }

    @Test
    void restoreAccepteUneListeVide() {
        skills.addPoints(3);
        skills.unlock(Talent.Dash);

        skills.restore(new String[0], 0);

        assertEquals(0, skills.unlockedNames().length);
        assertEquals(0, skills.getPoints());
    }

    @Test
    void restorePeutRestaurerPlusieursTalents() {
        skills.restore(
            new String[]{"Dash", "HPMax_1", "Damage_1"},
            5
        );

        assertTrue(skills.has(Talent.Dash));
        assertTrue(skills.has(Talent.HPMax_1));
        assertTrue(skills.has(Talent.Damage_1));
        assertEquals(5, skills.getPoints());
    }
}