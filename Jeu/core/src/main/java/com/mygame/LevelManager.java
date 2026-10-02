package com.mygame;

public class LevelManager {
    public static Level loadLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createLevel1();

            case 2:
                return createLevel2();

            case 3:
                return createLevel3();

            default:
                return createLevel1();
        }
    }

    private static Level createLevel1() {
        Level level = new Level(3000f, 1500f, "Music/Map1.mp3");
        // ------ Sol ------ //
        level.addPlatform(new Platform(0, 0, 3000, 100));
        // ------ Plateforme 1 ------ //
        level.addPlatform(new Platform(300, 300, 300, 50));
        // ------ Plateforme 2 ------ //
        level.addPlatform(new Platform(900, 450, 300, 50));
        level.addPlatform(new Platform(1500, 250, 400, 50));
        level.addPlatform(new Platform(300, 600, 300, 50));
        level.addPlatform(new Platform(900, 750, 300, 50));
        level.addPlatform(new Platform(300, 900, 300, 50));
        level.addPlatform(new Platform(1500, 750, 300, 50));
        return level;
    }

    private static Level createLevel2() {
        Level level = new Level(4000, 1500, "Music/Map2.mp3");
        level.addPlatform(new Platform(0, 0, 4000, 100));
        level.addPlatform(new Platform(500, 300, 300, 50));
        level.addPlatform(new Platform(1200, 500, 300, 50));
        return level;
    }

    private static Level createLevel3() {
        Level level = new Level(5000, 1500, "Music/Map3.mp3");
        level.addPlatform(new Platform(0, 0, 5000, 100));
        return level;
    }
}