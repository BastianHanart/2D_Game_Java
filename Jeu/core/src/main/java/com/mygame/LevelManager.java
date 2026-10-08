package com.mygame;

import com.badlogic.gdx.Gdx;

public class LevelManager {

    private static final int GRID = 32;
    private static final float SCALE = 4f;

    public static Level loadLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:  
                return loadFromLDtk("Level_1", "Music/Map1.mp3");
            case 2:  
                return loadFromLDtk("Level_1", "Music/Map2.mp3");
            case 3:  
                return loadFromLDtk("Level_1", "Music/Map3.mp3");
            default: 
                return loadFromLDtk("Level_1", "Music/Map1.mp3");
        }
    }

    private static Level loadFromLDtk(String levelName, String music) {
        String dir = "Map_2Dgame/simplified/" + levelName + "/";

        // Lecture de la grille de collision
        String[] lines = Gdx.files.internal(dir + "Collision.csv").readString().trim().split("\\r?\n");
        int rows = lines.length;
        int cols = lines[0].split(",").length;
        int[][] grid = new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            String[] cells = lines[y].trim().split(",");
            for (int x = 0; x < cols; x++) {
                grid[y][x] = Integer.parseInt(cells[x].trim());
            }
        }

        float cell = GRID * SCALE;
        Level level = new Level(cols * cell, rows * cell, music,"windrise-background-4k.png", dir + "Background.png", dir + "Decor.png");
        level.setCollision(new CollisionMap(grid, cell));
        return level;
    }
}