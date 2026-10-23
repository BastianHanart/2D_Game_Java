package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveManager {

    public static final int slots = 5;

    private static FileHandle file(int slot) {
        return Gdx.files.local("saves/slot" + slot + ".json");
    }

    private static Json json() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        return json;
    }

    public static boolean exists(int slot) {
        return file(slot).exists();
    }

    public static SaveData read(int slot) {
        FileHandle f = file(slot);
        if (!f.exists()) return null;
        try {
            return json().fromJson(SaveData.class, f.readString());
        } catch (Exception e) {
            Gdx.app.error("SAVE", "Slot " + slot + " illisible", e);
            return null;
        }
    }

    public static void write(int slot, SaveData data) {
        try {
            FileHandle tmp = Gdx.files.local("saves/slot" + slot + ".tmp");
            tmp.writeString(json().prettyPrint(data), false);
            tmp.moveTo(file(slot));
        } catch (Exception e) {
            Gdx.app.error("SAVE", "Echec de l'ecriture du slot " + slot, e);
        }
    }

    public static void delete(int slot) {
        file(slot).delete();
    }

    public static void saveCurrent(Character player, int levelNumber) {
        int slot = GameState.currentSlot;
        if (slot < 1) return;
        SaveData data = new SaveData();
        data.level = levelNumber;
        data.x = player.getX();
        data.y = player.getY();
        data.y = player.getHP_Potion();
        data.hp = player.isDead() ? player.getHPMax() : player.getHP();
        data.playerLevel = GameState.niveau;
        data.xp = GameState.xp;
        data.points = GameState.Skills.getPoints();
        data.talents = GameState.Skills.unlockedNames();
        data.playTime = GameState.playTime;
        data.timestamp = System.currentTimeMillis();
        write(slot, data);
    }

    // ==================================== \\
    //              Chargement              \\
    // ==================================== \\
    
    public static void apply(int slot, SaveData data) {
        GameState.currentSlot = slot;
        GameState.currentLevel = data.level;
        GameState.niveau = data.playerLevel;
        GameState.xp = data.xp;
        GameState.playTime = data.playTime;
        GameState.Skills.restore(data.talents, data.points);
    }
}