package com.mygame;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;

public class Level {
    private Array<Platform> platforms;
    private float width;
    private float height;
    private Music music;

    public Level(float width, float height, String musicPath) {
        this.width = width;
        this.height = height;
        platforms = new Array<>();
        music = com.badlogic.gdx.Gdx.audio.newMusic(
            com.badlogic.gdx.Gdx.files.internal(musicPath)
        );
        music.setLooping(true);
        music.setVolume(0.5f);
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

    public void dispose() {
        music.dispose();
    }
}