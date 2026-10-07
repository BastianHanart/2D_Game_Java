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

    // private static Level createLevel1() {
    //     Level level = new Level(3000f, 1500f, "Music/Map1.mp3", "windrise-background-4k.png");
    //     // ------ Sol ------ //
    //     level.addPlatform(new Platform(0, 0, 3000, 100));
    //     // ------ Plateforme 1 ------ //
    //     level.addPlatform(new Platform(300, 300, 300, 50));
    //     // ------ Plateforme 2 ------ //
    //     level.addPlatform(new Platform(900, 450, 300, 50));
    //     level.addPlatform(new Platform(1500, 250, 400, 50));
    //     level.addPlatform(new Platform(300, 600, 300, 50));
    //     level.addPlatform(new Platform(900, 750, 300, 50));
    //     level.addPlatform(new Platform(300, 900, 300, 50));
    //     level.addPlatform(new Platform(1500, 750, 300, 50));
    //     return level;
    // }

    // private static Level createLevel2() {
    //     Level level = new Level(4000, 1500, "Music/Map2.mp3", "windrise-background-4k.png");
    //     level.addPlatform(new Platform(0, 0, 4000, 100));
    //     level.addPlatform(new Platform(500, 300, 300, 50));
    //     level.addPlatform(new Platform(1200, 500, 300, 50));
    //     return level;
    // }

    // private static Level createLevel3() {
    //     Level level = new Level(5000, 1500, "Music/Map3.mp3", "windrise-background-4k.png");
    //     level.addPlatform(new Platform(0, 0, 5000, 100));
    //     return level;
    // }

    private static Level loadFromLDtk(String levelName, String music) {
        String dir = "Map_2Dgame/simplified/" + levelName + "/";

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

        float width = cols * GRID * SCALE;
        float height = rows * GRID * SCALE;
        Level level = new Level(width, height, music,"windrise-background-4k.png", dir + "Background.png", dir + "Decor.png");

        float cell = GRID * SCALE;
        for (int y = 0; y < rows; y++) {
            int x = 0;
            while (x < cols) {
                if (grid[y][x] == 1) {
                    int start = x;
                    while (x < cols && grid[y][x] == 1) x++;
                    int run = x - start;
                    
                    float py = height - (y + 1) * cell;
                    level.addPlatform(new Platform(start * cell, py, run * cell, cell));
                } else {
                    x++;
                }
            }
        }
        return level;
    }

}