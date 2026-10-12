package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class LevelManager {

private static final int GRID = 16;     // vraie taille de grille LDtk
private static final float SCALE = 8f;  // 16 * 8 = 128 = ta cellule actuelle

public static Level loadLevel(int levelNumber, Game game){
        switch (levelNumber) {
            case 1:  
                return loadFromLDtk("Level_1", "Music/Map1.mp3", game);
            case 2:  
                return loadFromLDtk("Level_1", "Music/Map2.mp3", game);
            case 3:  
                return loadFromLDtk("Level_1", "Music/Map3.mp3", game);
            default: 
                return loadFromLDtk("Level_1", "Music/Map1.mp3", game);
        }
    }

    private static Level loadFromLDtk(String levelName, String music, Game game) {
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

        JsonValue root = new JsonReader().parse(
                Gdx.files.internal(dir + "data.json")
        );

        JsonValue entities = root.get("entities");

        if (entities != null) {
            JsonValue enemies = entities.get("Enemy");

            if (enemies != null) {
                for (JsonValue enemy : enemies) {

                    String type = enemy.get("customFields").getString("EntityType");

                    float x = enemy.getFloat("x") * SCALE;
                    float y = (root.getFloat("height") - enemy.getFloat("y") - enemy.getFloat("height")) * SCALE;

                    Monster monster = null;

                    if (type.equals("Squelette")) {
                        monster = new Squelette(game);
                    } else if (type.equals("Fantome")) {
                        monster = new Fantome(game);
                    }

                    if (monster != null) {
                        Gdx.app.log("SPAWN", type
                        + " json x=" + enemy.getFloat("x") + " y=" + enemy.getFloat("y")
                        + " w=" + enemy.getFloat("width") + " h=" + enemy.getFloat("height")
                        + " | levelW=" + root.getFloat("width") + " levelH=" + root.getFloat("height")
                        + " | cols*GRID=" + (cols * GRID) + " rows*GRID=" + (rows * GRID));
                        monster.setPosition(x, y);
                        level.addMonster(monster);
                    }
                }
            }
        }
        return level;
    }
}