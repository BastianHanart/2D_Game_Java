package com.mygame;

import java.util.EnumSet;

/**
 * Représente l'arbre de compétences du joueur 
*/
public class SkillTree {
    private final EnumSet<Talent> unlocked = EnumSet.noneOf(Talent.class);
    private int points = 0;

    /**
    * @return les points d'XP
    */
    public int getPoints() {
        return points;
    }

    public void addPoints(int n) {
        points += n;
    }

    /**
     * @param t talent possédé
    * @return les points d'XP
    */
    public boolean has(Talent t) {
        return unlocked.contains(t);
    }

    /**
     * @param t talent à débloquer
     * @return true si possibité de débloquer
    */
    public boolean canUnlock(Talent t) {
        if (has(t) || points < t.cost) {
            return false;
        }

        for (Talent prerequisite : t.prerequisite) {
            if (!has(prerequisite)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param t talent à débloquer
     * @return true
    */
    public boolean unlock(Talent t) {
        if (!canUnlock(t)) {
            return false;
        }

        points -= t.cost;
        unlocked.add(t);
        return true;
    }

    public void reset() { unlocked.clear(); points = 0; }
    public String[] unlockedNames() {
        return unlocked.stream().map(Enum::name).toArray(String[]::new);
    }

    /**
     * @param names noms des talents à récupérer
     * @param pts coûts du talents
    */
    public void restore(String[] names, int pts) {
        unlocked.clear();
        for (String n : names) {
            try { unlocked.add(Talent.valueOf(n)); } catch (IllegalArgumentException ignored) {}
        }
        points = pts;
    }
}