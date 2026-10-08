package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private Character character;
    private OrthographicCamera camera;
    private Level level;
    private int currentLevel = 1;
    private static final float BACKGROUND_HEIGHT_FACTOR = 1.3f;
    private static final float BACKGROUND_WIDTH_FACTOR = 1.2f;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        character = new Character(game);
        level = LevelManager.loadLevel(currentLevel);
        character.setPosition(150, 700);
        level.getMusic().play();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(character.getX(), character.getY() + 150, 0);
        camera.update();
    }

    // ===========================================
    //                Niveaux
    // ===========================================

    private void changeLevel(int levelNumber, boolean enterFromRight) {
        level.getMusic().stop();
        level.dispose();
        level = LevelManager.loadLevel(levelNumber);

        float startX = enterFromRight ? level.getWidth() - 150 : 50;
        character.setPosition(startX, 800);
        level.getMusic().play();
    }

    private void checkLevelChange() {
        Body body = character.getBody();

        if (body.x + body.w / 2f >= level.getWidth()) {
            currentLevel++;
            changeLevel(currentLevel, false);
        } else if (body.x - body.w / 2f <= 0) {
            if (currentLevel > 1) {
                currentLevel--;
                changeLevel(currentLevel, true);
            } else {
                body.x = body.w / 2f;   // mur invisible au bord du niveau 1
            }
        }
    }

    // ===========================================
    //                CAMERA
    // ===========================================

    private void updateCamera() {
        float halfW = camera.viewportWidth / 2f;
        float halfH = camera.viewportHeight / 2f;

        float targetX = character.getX();
        float targetY = character.getY() + camera.viewportHeight / 3f;

        targetX = Math.max(halfW, Math.min(level.getWidth() - halfW, targetX));
        targetY = Math.max(halfH, Math.min(level.getHeight() - halfH, targetY));

        float t = 5f * Gdx.graphics.getDeltaTime();
        camera.position.x += (targetX - camera.position.x) * t;
        camera.position.y += (targetY - camera.position.y) * t;
        camera.update();
    }

    // ===========================================
    //                  RENDER
    // ===========================================

    @Override
    public void render(float delta) {
        // Evite les gros sauts de physique si une frame est très longue
        delta = Math.min(delta, 1 / 30f);

        character.update(delta, level.getCollision());
        checkLevelChange();
        updateCamera();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderBackground();
        for (int i = 1; i < level.getLayers().size; i++) {
            Texture t = level.getLayers().get(i);
            batch.draw(t, 0, 0, level.getWidth(), level.getHeight());
        }
        character.render(batch);
        batch.end();
    }

    // ===========================================
    //             RENDU Background
    // ===========================================

    private void renderBackground() {
        Texture background = level.getBackground();

        float viewW = camera.viewportWidth;
        float viewH = camera.viewportHeight;
        float viewLeft = camera.position.x - viewW / 2f;
        float viewBottom = camera.position.y - viewH / 2f;

        float bgWidth = viewW * BACKGROUND_WIDTH_FACTOR;
        float bgHeight = viewH * BACKGROUND_HEIGHT_FACTOR;
        float extraX = bgWidth - viewW;
        float extraY = bgHeight - viewH;

        float minX = viewW / 2f, maxX = level.getWidth() - viewW / 2f;
        float minY = viewH / 2f, maxY = level.getHeight() - viewH / 2f;

        float tx = (maxX > minX) ? (camera.position.x - minX) / (maxX - minX) : 0f;
        float ty = (maxY > minY) ? (camera.position.y - minY) / (maxY - minY) : 0f;
        tx = Math.max(0f, Math.min(1f, tx));
        ty = Math.max(0f, Math.min(1f, ty));

        batch.draw(background, viewLeft - tx * extraX, viewBottom - ty * extraY, bgWidth, bgHeight);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override public void pause() {

    }
    @Override public void resume() {

    }
    @Override public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        character.dispose();
        level.dispose();
    }
}