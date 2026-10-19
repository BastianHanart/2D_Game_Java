package com.mygame;

public class GameState {
    public static final SkillTree Skills = new SkillTree();

    public static int niveau = 1;
    public static int xp = 0;
    public static int currentSlot = 0;
    public static int currentLevel = 1;
    public static float playTime = 0f;

    public static int xpRequis() {
        return 10;
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