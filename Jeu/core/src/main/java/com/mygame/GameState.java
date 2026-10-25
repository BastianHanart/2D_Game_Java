package com.mygame;

public class GameState {
    public static final SkillTree Skills = new SkillTree();

    public static int niveau = 1;
    public static int xp = 0;
    public static int currentSlot = 0;
    public static int currentLevel = 1;
    public static float playTime = 0f;

    // +15 XP par niveau (20, 35, 50, 65...) puis bloqué à 100 max
    public static int xpRequis() {
        return Math.min(20 + 15 * (niveau - 1), 100);
    }

    public static boolean addXp(int montant) {
        xp += montant;
        boolean LevelUp = false;
        while (xp >= xpRequis()) {
            xp -= xpRequis();
            niveau++;
            Skills.addPoints(1);
            LevelUp = true;
        }
        return LevelUp;
    }

    public static void reset() {
        niveau = 1; xp = 0; playTime = 0f; currentLevel = 1;
        Skills.reset();
    }
}