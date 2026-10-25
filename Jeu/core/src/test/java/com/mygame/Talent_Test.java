package com.mygame;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class Talent_Test {

    @Test
    void enumContientDixTalents() {
        assertEquals(10, Talent.values().length);
    }

    @Test
    void dashCouteUnPoint() {
        assertEquals(1, Talent.Dash.cost);
    }

    @Test
    void doubleJumpCouteDeuxPoints() {
        assertEquals(2, Talent.DoubleJump.cost);
    }

    @Test
    void dashNaAucunPrerequis() {
        assertTrue(Talent.Dash.prerequisite.isEmpty());
    }

    @Test
    void doubleJumpNecessiteDash() {
        assertEquals(
            Collections.singletonList(Talent.Dash),
            Talent.DoubleJump.prerequisite
        );
    }

    @Test
    void hpMaxDeuxNecessiteHpMaxUn() {
        assertEquals(
            Collections.singletonList(Talent.HPMax_1),
            Talent.HPMax_2.prerequisite
        );
    }

    @Test
    void damageDeuxNecessiteDamageUn() {
        assertEquals(
            Collections.singletonList(Talent.Damage_1),
            Talent.Damage_2.prerequisite
        );
    }

    @Test
    void damageTroisNecessiteDamageDeuxEtDoubleJump() {
        assertEquals(
            Arrays.asList(Talent.Damage_2, Talent.DoubleJump),
            Talent.Damage_3.prerequisite
        );
    }

    @Test
    void tousLesTalentsOntUnCoutPositif() {
        for (Talent talent : Talent.values()) {
            assertTrue(
                talent.cost > 0,
                talent.name() + " doit avoir un coût positif"
            );
        }
    }

    @Test
    void tousLesTalentsOntUnNomNonVide() {
        for (Talent talent : Talent.values()) {
            assertNotNull(talent.name);
            assertFalse(talent.name.trim().isEmpty());
        }
    }
}