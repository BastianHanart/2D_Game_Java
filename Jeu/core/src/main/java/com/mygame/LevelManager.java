package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Représente la gestion du niveau actuel : Musique, visuels, collisions
*/
public class LevelManager {


    /**
    * @param levelNumber nombre du niveau actuel.
    * @param game défini la partie dans laquelle nous nous trouvons.
    */
    public static Level loadLevel(int levelNumber, Game game) {
        switch (levelNumber) {
            case 1:  
                return loadFromLDtk("Level_1", 16, 128f, "Music/Map1.mp3", "windrise-background-4k.png", "Decor.png", game);
            case 2:  
                return loadFromLDtk("Level_2", 20, 20f, "Music/Map2.mp3", "Fond_niveau_2.jpeg", "Ville.png", game);
            case 3:  
                return loadFromLDtk("Level_3", 20, 25f, "Music/Map3.mp3", "Fond_Boss.jpeg", "Foret.png", game);
            case 4:
                return loadFromLDtk("Level_4", 20, 20f, "Music/MusicBoss.mp3", "Fond_Boss.jpeg", "Foret.png", game);
            default: 
                return loadFromLDtk("Level_1", 16, 128f, "Music/Map1.mp3", "windrise-background-4k.png", "Decor.png", game);
        }
    }

    private static Level loadFromLDtk(String levelName, int gridSize, float cell, String music, String fond, String Decor, Game game) {
        float scale = cell / gridSize;
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

        Level level = new Level(cols * cell, rows * cell, music, fond, dir + "Background.png", dir + Decor);
        level.setCollision(new CollisionMap(grid, cell));

        JsonValue root = new JsonReader().parse(
                Gdx.files.internal(dir + "data.json")
        );

        Texture decorTex = level.getLayers().peek();
        Gdx.app.log("SCALE", "png=" + decorTex.getWidth() + "px, monde=" + level.getWidth()
                + "px, agrandissement x" + (level.getWidth() / decorTex.getWidth()));

        JsonValue entities = root.get("entities");

        if (entities != null) {
            JsonValue spawnsJson = entities.get("PlayerSpawn");
            if (spawnsJson != null) {
                for (JsonValue s : spawnsJson) {
                    String type = s.get("customFields").getString("SpawnType");

                    float x = (s.getFloat("x") + s.getFloat("width") / 2f) * scale;
                    float y = (root.getFloat("height") - (s.getFloat("y") + s.getFloat("height"))) * scale;

                    Gdx.app.log("SPAWN2", type + " json x=" + s.getFloat("x") + " y=" + s.getFloat("y")
            + " | levelW=" + root.getFloat("width") + " levelH=" + root.getFloat("height")
            + " | monde=" + level.getWidth() + "x" + level.getHeight()
            + " | -> x=" + x + " y=" + y);


                    level.addSpawn(type, x, y);
                }
            }
            JsonValue enemies = entities.get("Enemy");

            int pnjCount = 0; 

            if (enemies != null) {
                for (JsonValue enemy : enemies) {

                    String type = enemy.get("customFields").getString("EntityType");
                    scale = level.getWidth() / root.getFloat("width");

                    float x = enemy.getFloat("x") * scale;
                    float y = (root.getFloat("height") - enemy.getFloat("y") - enemy.getFloat("height")) * scale;

                    Monster monster = null;
                    Villageois villageois = null;
                    Envie boss = null;

                    if (type.equals("Squelette")) {
                        monster = new Squelette(game);
                    } 
                    else if (type.equals("Fantome")) {
                        monster = new Fantome(game);
                    }
                    else if (type.equals("PNJ")) {
                        JsonValue fields = enemy.get("customFields");
                        int variant = fields.getInt("Variant", pnjCount);   // champ LDtk, sinon automatique
                        villageois = new Villageois(game, variant);
                        pnjCount++;
                    }

                    else if (type.equals("Boss")) {
                        boss = new Envie(game);
                    }

                    if (monster != null) {
                        monster.setPosition(x, y);
                        level.addMonster(monster);
                    }

                    if (villageois != null) {
                        villageois.setPosition(x, y);
                        level.addVillageois(villageois);
                    }

                    if (boss != null) {
                        boss.setPosition(x, y);
                        level.addMonster(boss);
                    }
                }
            }
        }
        return level;
    }
}