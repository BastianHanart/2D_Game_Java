package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Level {
    private Array<Platform> platforms;
    private float width;
    private float height;
    private Music music;
    private Array<Texture> layers = new Array<>();

    public Level(float width, float height, String musicPath, String... layerPaths) {
        this.width = width;
        this.height = height;
        platforms = new Array<>();
        music = com.badlogic.gdx.Gdx.audio.newMusic(
            com.badlogic.gdx.Gdx.files.internal(musicPath)
        );
        music.setLooping(true);
        music.setVolume(0.5f);
        for (String path : layerPaths) {
            Texture t = new Texture(Gdx.files.internal(path));
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            layers.add(t);
        }
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
        return layers.get(0);
    }

    public Array<Texture> getLayers() { return layers; }

    public void dispose() {
        music.dispose();
        for (Texture t : layers) t.dispose();
    }
}