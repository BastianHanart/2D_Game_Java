package com.mygame;

import com.badlogic.gdx.math.MathUtils;

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
     * Meilleur sol sous les pieds, entre minY et maxY.
     * Les plateformes (8) ne comptent que si on était AU-DESSUS à la frame précédente.
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

    /** Déplace horizontalement en s'arrêtant contre les blocs pleins. Retourne le nouveau x. */
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

    public boolean isSolid(float wx, float wy) {
        return tileAt((int) Math.floor(wx / cell), (int) Math.floor((height - wy) / cell)) == SOLID;
    }

    public float clampX(float x, float halfW) {
        float minX = halfW;
        float maxX = cols * cell - halfW;

        return MathUtils.clamp(x, minX, maxX);
    }
}