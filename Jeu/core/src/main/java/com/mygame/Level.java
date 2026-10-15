package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Level {
    private final float width, height;
    private final Music music;
    private final Array<Texture> layers = new Array<>();
    private CollisionMap collision;
    private final Array<Monster> monsters = new Array<>();
    private final java.util.Map<String, com.badlogic.gdx.math.Vector2> spawns = new java.util.HashMap<>();


    public Level(float width, float height, String musicPath, String... layerPaths) {
        this.width = width;
        this.height = height;
        music = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        music.setLooping(true);
        music.setVolume(0.5f);
        for (String path : layerPaths) {
            Texture t = new Texture(Gdx.files.internal(path));
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            layers.add(t);
        }
    }

    public Array<Monster> getMonsters() {
        return monsters;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public void setCollision(CollisionMap collision) { 
        this.collision = collision; 
    }
    public CollisionMap getCollision() { 
        return collision; 
    }

    public void addSpawn(String type, float x, float y) {
        spawns.put(type, new com.badlogic.gdx.math.Vector2(x, y));
    }

    public com.badlogic.gdx.math.Vector2 getSpawn(String type) {
        return spawns.get(type); // peut être null
    }

    public float getWidth() { 
        return width; 
    }
    public float getHeight() { 
        return height; }
    public Music getMusic() { 
        return music;
    }
    public Texture getBackground() { 
        return layers.get(0); 
    }
    public Array<Texture> getLayers() { 
        return layers; 
    }

    public void dispose() {
        music.dispose();
        for (Texture t : layers) t.dispose();
    }
}