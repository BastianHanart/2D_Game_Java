package com.mygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Représente la barre de vie du joueur
*/
public class HealthBar {
    private Texture FullHealthBar;
    private Texture EmptyHealthBar;
    private int srcWidth;
    private int srcHeight;
    private float widthHealthBar;
    private float heightHealthBar;
    
    /**
    * @return la barre de vie du joueur
    */
    public float getHeightHealthBar() {
        return heightHealthBar;
    }

    /**
    * Créer et dessine la barre de vie
    */
    public HealthBar(){
        FullHealthBar = new Texture("FullHealthBar.png");
        EmptyHealthBar = new Texture("EmptyHealthBar.png");        
        srcWidth = EmptyHealthBar.getWidth();
        srcHeight = EmptyHealthBar.getHeight();
        widthHealthBar = srcWidth / 2f;
        heightHealthBar = srcHeight / 2f;
    }

    /**
     * @param batch Sprite de la barre de vie
     * @param x position horizontale de la barre de vie
     * @param y position verticale de la barre de vie
     * @param HP défini les HP actuel du joueur
     * @param HPMax défini les HP max du joueur
     * @see SpriteBatch
     */
    public void draw(SpriteBatch batch, float x, float y, float HP, float HPMax){
        float lifePercent = HP / HPMax;
        if (lifePercent < 0) lifePercent = 0;
        if (lifePercent > 1) lifePercent = 1;

        batch.draw(EmptyHealthBar, x, y, widthHealthBar, heightHealthBar);
        batch.draw(FullHealthBar, x, y, widthHealthBar * lifePercent, heightHealthBar,0, 0, (int)(srcWidth * lifePercent), srcHeight, false, false);
    }

    /**
    * Supprime de l'écran la barre de vie lorsque qu'on sort d'un écran ou elle ne doit plus être afficher
    */
    public void dispose() {
        FullHealthBar.dispose();
        EmptyHealthBar.dispose();
    }
}

