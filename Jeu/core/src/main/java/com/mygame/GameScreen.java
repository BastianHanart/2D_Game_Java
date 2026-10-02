package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Character character;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Level level;
    private int currentLevel = 1;
    private boolean side;
    private boolean TpAllowed = true;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        character = new Character();
        level = LevelManager.loadLevel(1);
        level.getMusic().play();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(character.getX(), character.getY(), 0);
        camera.update();
    }

    // ===========================================
    //              COLLISIONS
    // ===========================================

    private void checkPlatformCollisions() {
        Rectangle player = character.getHitbox();

        for (Platform platform : level.getPlatforms()) {
            Rectangle ground = platform.getHitbox();
            boolean horizontalCollision = player.x + player.width > ground.x && player.x < ground.x + ground.width;
            boolean falling = character.getVerticalSpeed() <= 0;
            boolean landing = player.y <= ground.y + ground.height && player.y >= ground.y + ground.height - 20;

            if (horizontalCollision && falling && landing) {
                character.setY(ground.y + ground.height);
                character.setVerticalSpeed(0);
                character.setOnGround(true);
                break;
            }
        }
    }

    private void changeLevel(int levelNumber) {
        level.getMusic().stop();
        level.dispose();
        level = LevelManager.loadLevel(levelNumber);
        // ------ Position de départ du joueur ------ //
        if (side == false){
            character.setX(50);
            character.setY(200);
        }
        else{
            character.setX(level.getWidth() - 150);
            character.setY(200);
        }

        level.getMusic().play();
    }
    
    private void checkLevelChange() {
        float playerRight = character.getX() + character.getHitbox().width;

        if (playerRight >= level.getWidth() && TpAllowed == true) {
            currentLevel++;
            side = false;
            changeLevel(currentLevel);
        }
        if (character.getX() <= 0 && currentLevel > 1 && TpAllowed == true) {
            currentLevel--;
            side = true;
            changeLevel(currentLevel);
        }
    }

    // ===========================================
    //                CAMERA
    // ===========================================

    private void updateCamera() {
        float halfWidth = camera.viewportWidth / 2f;
        float halfHeight = camera.viewportHeight / 2f;
        float targetX = character.getX();
        float targetY = character.getY();
        // ------ Limites de la map ------ //
        targetX = Math.max(halfWidth, targetX);
        targetX = Math.min(level.getWidth() - halfWidth, targetX);
        targetY = Math.max(halfHeight, targetY);
        targetY = Math.min(level.getHeight() - halfHeight, targetY);
        // ------ Mouvement de la caméra ------ //
        float cameraSpeed = 5f;
        camera.position.x += (targetX - camera.position.x) * cameraSpeed * Gdx.graphics.getDeltaTime();
        camera.position.y += (targetY - camera.position.y) * cameraSpeed * Gdx.graphics.getDeltaTime();
        camera.update();
    }

    // ===========================================
    //              RENDU MAP
    // ===========================================

    private void renderTestMap() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Platform platform : level.getPlatforms()) {
            Rectangle rectangle = platform.getHitbox();
            shapeRenderer.setColor(0.3f,0.3f,0.3f,1);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        shapeRenderer.end();
    }

    // ===========================================
    //                  RENDER
    // ===========================================

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        character.update(delta);
        checkPlatformCollisions();
        checkLevelChange();
        updateCamera();
        renderTestMap();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        character.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        character.dispose();
    }
}