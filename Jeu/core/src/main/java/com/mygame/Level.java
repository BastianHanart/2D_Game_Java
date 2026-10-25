package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Représente la gestion des différents niveau, de quels manière ils vont être gérés
*/
public class Level {
    private final float width, height;
    private final Music music;
    private final Array<Texture> layers = new Array<>();
    private CollisionMap collision;
    private final Array<Monster> monsters = new Array<>();
    private final Array<Villageois> PNJ = new Array<>();
    private final java.util.Map<String, com.badlogic.gdx.math.Vector2> spawns = new java.util.HashMap<>();


    /**
    * @param width longueur du niveau
    * @param height hauteur du niveau
    * @param musicPath musique du niveau
    * @param layerPaths différentes couches du niveau (visuel, fond, collision)
    */
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

    /**
    * @return tous les monstres présents dans le niveau
    * @see Monster
    */
    public Array<Monster> getMonsters() {
        return monsters;
    }

    /**
    * @return tous les villageois présents dans le niveau
    * @see Villageois
    */
    public Array<Villageois> getVillageois() {
        return PNJ;
    }

    /**
    * @param monster Nouveau monstre à être ajouté
    * @see Monster
    */
    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    /**
    * @param pnj Nouveau villageois à être ajouté
    * @see Villageois
    */
    public void addVillageois(Villageois pnj) {
        PNJ.add(pnj);
    }

    /**
    * @param collision Collision de la map
    * @see CollisionMap
    */
    public void setCollision(CollisionMap collision) { 
        this.collision = collision; 
    }

    /**
    * @return toutes les collisions du niveau
    * @see CollisionMap
    */
    public CollisionMap getCollision() { 
        return collision; 
    }

    /**
    * @param type type de spawn du joueur (point d'apparition, droite / gauche du niveau)
    * @param x position horizontale du spawn
    * @param y position verticale du spawn
    */
    public void addSpawn(String type, float x, float y) {
        spawns.put(type, new com.badlogic.gdx.math.Vector2(x, y));
    }

    /**
    * @param type type de spawn du joueur (point d'apparition, droite / gauche du niveau)
    * @return toutes les collisions du niveau
    */
    public com.badlogic.gdx.math.Vector2 getSpawn(String type) {
        return spawns.get(type); // peut être null
    }

    /**
    * @return la taille du niveau
    */
    public float getWidth() { 
        return width; 
    }

    /**
    * @return la hauteur du niveau
    */
    public float getHeight() { 
        return height; 
    }

    /**
    * @return la musique du niveau
    */
    public Music getMusic() { 
        return music;
    }

    /**
    * @return la fond du niveau
    */
    public Texture getBackground() { 
        return layers.get(0); 
    }

    /**
    * @return les couches du niveaux
    */
    public Array<Texture> getLayers() { 
        return layers; 
    }

    /**
    * Supprime la musique et le visuel du niveau
    */
    public void dispose() {
        music.dispose();
        for (Texture t : layers) t.dispose();
    }
}