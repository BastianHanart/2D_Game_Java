package com.mygame;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Level {
    private Array<Platform> platforms;
    private float width;
    private float height;
    private Music music;
    private Texture background;

    public Level(float width, float height, String musicPath, String bgPath) {
        this.width = width;
        this.height = height;
        platforms = new Array<>();
        music = com.badlogic.gdx.Gdx.audio.newMusic(
            com.badlogic.gdx.Gdx.files.internal(musicPath)
        );
        music.setLooping(true);
        music.setVolume(0.5f);
        background = new Texture(com.badlogic.gdx.Gdx.files.internal(bgPath)); 
    }

    public void addPlatform(Platform platform) {
        platforms.add(platform);
    }

    public Array<Platform> getPlatforms() {
        return platforms;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Music getMusic() {
        return music;
    }

    public Texture getBackground() { 
        return background; 
    } 

    public void dispose() {
        music.dispose();
        background.dispose();
    }
}