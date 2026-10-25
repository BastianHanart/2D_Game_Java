package com.mygame;

import com.badlogic.gdx.math.MathUtils;

/**
 * Représente les collisions sur les différents niveaux; gère leurs position, les réactions avec des entités
*/
public class CollisionMap {
    public static final float NO_GROUND = Float.NEGATIVE_INFINITY;
    public static final int SOLID = 1, ONE_WAY = 8;
    public static final float STEP = 50f;   // plus petite hauteur qu'on grimpe sans sauter

    // Hauteur de la surface (0..1 de la case) au bord gauche / droit
    private static final float[][] SURFACE = {
        null,
        {1f, 1f},     // 1 plein
        {0f, 1f},     // 2 pente 45° montante
        {1f, 0f},     // 3 pente 45° descendante
        {0f, .5f},    // 4 douce montante (1re moitié)
        {.5f, 1f},    // 5 douce montante (2e moitié)
        {1f, .5f},    // 6 douce descendante (1re moitié)
        {.5f, 0f},    // 7 douce descendante (2e moitié)
        {1f, 1f},     // 8 plateforme traversable
    };

    private final int[][] grid;   // grid[ligne][colonne], ligne 0 = en haut (comme LDtk)
    private final int rows, cols;
    private final float cell, height;

    /**
    * @param grid permet de définir la grille 2D entière des collisions du niveau actuel.
    * @param cell permet de définir chaque cellule de colission du niveau actuel.
    */
    public CollisionMap(int[][] grid, float cell) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.cell = cell;
        this.height = rows * cell;
    }

    private int tileAt(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) return 0;
        return grid[row][col];
    }

    /** Y monde de la surface de cette case à l'abscisse x (ou NO_GROUND). */
    private float surfaceY(int col, int row, float x) {
        int t = tileAt(col, row);
        if (t <= 0 || t >= SURFACE.length) return NO_GROUND;
        float u = MathUtils.clamp((x - col * cell) / cell, 0f, 1f);
        float h = SURFACE[t][0] + (SURFACE[t][1] - SURFACE[t][0]) * u;
        return height - (row + 1) * cell + h * cell;
    }

    /**
    * @param x permet de définir la position horizontale de la collision.
    * @param minY permet de définir la collission la plus haute.
    * @param maxY permet de définir la collission la plus basse.
    * @param prevFeetY permet de définir chaque cellule de colission du niveau actuel.
    * @param dropThrough permet de savoir si on traverse ou non les élements (plateforme oui, sol non).
    * @return un nombre flottant de la position veticale de la première plateforme sur laquelle on va s'arreter et se poser.
    */
    public float findGround(float x, float minY, float maxY, float prevFeetY, boolean dropThrough) {
        int col = (int) Math.floor(x / cell);
        int rowTop = (int) Math.floor((height - maxY) / cell);
        int rowBot = (int) Math.floor((height - minY) / cell);
        float best = NO_GROUND;

        for (int row = rowTop; row <= rowBot; row++) {
            float y = surfaceY(col, row, x);
            if (y == NO_GROUND || y < minY || y > maxY) continue;
            if (tileAt(col, row) == ONE_WAY && (dropThrough || y > prevFeetY + 1f)) continue;
            if (y > best) best = y;
        }
        return best;
    }

    /**
    * @param x permet de définir la position horizontale de l'entité.
    * @param dx permet de définir la vitesse horizontale de l'entité.
    * @param feetY permet de définir la position vertical du bas de l'entité.
    * @param halfW permet de définir le milieu horizontale de l'entité.
    * @param bodyH permet de définir la hauteur du personnage.
    * @return un nombre flottant de la nouvelle position horizontale du joueur.
    */
    public float moveX(float x, float dx, float feetY, float halfW, float bodyH) {
        if (dx == 0) return x;
        float nx = x + dx;
        // Le corps commence STEP au-dessus des pieds : les pentes ne sont jamais des murs
        float bottom = feetY + STEP, top = feetY + bodyH;
        if (top <= bottom) return nx;

        float edge = nx + (dx > 0 ? halfW : -halfW);
        int col = (int) Math.floor(edge / cell);
        int rowTop = (int) Math.floor((height - top) / cell);
        int rowBot = (int) Math.floor((height - bottom - 0.01f) / cell);

        for (int row = rowTop; row <= rowBot; row++) {
            if (tileAt(col, row) == SOLID) {
                return dx > 0 ? col * cell - halfW - 0.01f
                              : (col + 1) * cell + halfW + 0.01f;
            }
        }
        return nx;
    }
    
    /**
    * @param wx position horizontale de l'élement en dessous de l'entité.
    * @param wy position vertical de l'élement en dessous de l'entité.
    * @return true si l'élément en dessous du joueur est solide (donc collision).
    */
    public boolean isSolid(float wx, float wy) {
        return tileAt((int) Math.floor(wx / cell), (int) Math.floor((height - wy) / cell)) == SOLID;
    }

    /**
    * @param x position horizontale de l'entité.
    * @param halfW Moitié de la largeur de l'entité.
    * @return un clamp (limite d'une valeur entre un minimum et un maximum) de la valeur x
    */
    public float clampX(float x, float halfW) {
        float minX = halfW;
        float maxX = cols * cell - halfW;

        return MathUtils.clamp(x, minX, maxX);
    }
}