package com.mygame;

import java.util.EnumSet;

public class SkillTree {
    private final EnumSet<Talent> unlocked = EnumSet.noneOf(Talent.class);
    private int points = 0;

    public int getPoints() {
        return points;
    }

    public void addPoints(int n) {
        points += n; 
    }

    public boolean has(Talent t) {
        return unlocked.contains(t);
    }

    public boolean canUnlock(Talent t) {
        return !has(t) && points >= t.cost && (t.prerequisite == null || has(t.prerequisite));
    }

    public boolean unlock(Talent t) {
        if (!canUnlock(t)) return false;
        points -= t.cost;
        unlocked.add(t);
        return true;
    }
}