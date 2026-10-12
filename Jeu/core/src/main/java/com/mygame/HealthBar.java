package com.mygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthBar {
    private Texture FullHealthBar;
    private Texture EmptyHealthBar;
    private int srcWidth;
    private int srcHeight;
    private float widthHealthBar;
    private float heightHealthBar;
    
    public float getHeightHealthBar() {
        return heightHealthBar;
    }

    public HealthBar(){
        FullHealthBar = new Texture("FullHealthBar.png");
        EmptyHealthBar = new Texture("EmptyHealthBar.png");        
        srcWidth = EmptyHealthBar.getWidth();
        srcHeight = EmptyHealthBar.getHeight();
        widthHealthBar = srcWidth / 2f;
        heightHealthBar = srcHeight / 2f;
    }

    public void draw(SpriteBatch batch, float x, float y, float HP, float HPMax){
        float lifePercent = HP / HPMax;
        if (lifePercent < 0) lifePercent = 0;
        if (lifePercent > 1) lifePercent = 1;

        batch.draw(EmptyHealthBar, x, y, widthHealthBar, heightHealthBar);
        batch.draw(FullHealthBar, x, y, widthHealthBar * lifePercent, heightHealthBar,0, 0, (int)(srcWidth * lifePercent), srcHeight, false, false);
    }

    public void dispose() {
        FullHealthBar.dispose();
        EmptyHealthBar.dispose();
    }
}

