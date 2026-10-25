package com.mygame;

/**
 * Représente tous les éléments sauvegarder lorsqu'on quitte la partie en cours
*/
public class SaveData {
    public int version = 1;
    public int level = 1;
    public int health_potion = 1;
    public int damage_potion = 0;
    public float x, y;
    public float hp;
    public int playerLevel = 1;
    public int xp = 0;
    public int points = 0;
    public String[] talents = new String[0];
    public float playTime;
    public long timestamp;
    
    /**
     * Sauvegarde des élements
    */
    public SaveData() {}
}