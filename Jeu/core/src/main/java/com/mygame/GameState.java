package com.mygame;

/**
 * Représente l'état du jeu : le niveau actuel, les niveaux du joeueurn le temps de jeu etc..
*/
public class GameState {
    public static final SkillTree Skills = new SkillTree();

    public static int niveau = 1;
    public static int xp = 0;
    public static int currentSlot = 0;
    public static int currentLevel = 1;
    public static float playTime = 0f;

    /**
    * @return l'XP requis pour le passer au niveau suivant
    */
    public static int xpRequis() {
        return Math.min(20 + 15 * (niveau - 1), 100);
    }

    /**
     * @param montant défini le montant d'XP à ajouter au joueur
     * @return true si on passe au niveau suivant
     */
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

    /**
    * Reset les stats de la partie
    */
    public static void reset() {
        niveau = 1; xp = 0; playTime = 0f; currentLevel = 1;
        Skills.reset();
    }
}