package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private Monster monster;
    private static final float BACKGROUND_HEIGHT_FACTOR = 1.3f;
    private static final float BACKGROUND_WIDTH_FACTOR = 1.2f;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        monster = new Monster(600, 200);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        character = new Character();
        level = LevelManager.loadLevel(1);
        character.setX(50);
        character.setY(800);
        level.getMusic().play();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(character.getX(), character.getY() + 150, 0);
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
            character.setY(800);
        }
        else{
            character.setX(level.getWidth() - 150);
            character.setY(800);
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

    // private void checkAttackCollisions() {

    //     if (!character.isAttacking()) {
    //         return;
    //     }

    //     if (character.getAttackHitbox().overlaps(monster.getHitbox())) {
    //         monster.takeDamage(character.getDamage());
    //     }
    // }

    // ===========================================
    //                CAMERA
    // ===========================================

    private void updateCamera() {
        float halfWidth = camera.viewportWidth / 2f;
        float halfHeight = camera.viewportHeight / 2f;
        float targetX = character.getX();
        float targetY = character.getY() + camera.viewportHeight / 3f;
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

    // private void renderTestMap() {
    //     shapeRenderer.setProjectionMatrix(camera.combined);
    //     shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    //     for (Platform platform : level.getPlatforms()) {
    //         Rectangle rectangle = platform.getHitbox();
    //         shapeRenderer.setColor(0.3f,0.3f,0.3f,1);
    //         shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    //     }
    //     shapeRenderer.end();
    // }

    // ===========================================
    //                  RENDER
    // ===========================================

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        character.update(delta);
        checkPlatformCollisions();
        // checkAttackCollisions();
        checkLevelChange();
        updateCamera();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderBackground();
        for (int i = 1; i < level.getLayers().size; i++) {
            Texture t = level.getLayers().get(i);
            batch.draw(t, 0, 0, level.getWidth(), level.getHeight());
        }
        batch.end();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        character.render(batch);
        batch.end();
    }


    private void renderBackground() {
        Texture background = level.getBackground();

        float viewW = camera.viewportWidth;
        float viewH = camera.viewportHeight;
        float viewLeft = camera.position.x - viewW / 2f;
        float viewBottom = camera.position.y - viewH / 2f;

        // Fond plus grand que l'écran
        float bgWidth = viewW * BACKGROUND_WIDTH_FACTOR;
        float bgHeight = viewH * BACKGROUND_HEIGHT_FACTOR;
        float extraX = bgWidth - viewW;
        float extraY = bgHeight - viewH;

        // Plage de déplacement de la caméra (la même que dans updateCamera)
        float minX = viewW / 2f;
        float maxX = level.getWidth() - viewW / 2f;
        float minY = viewH / 2f;
        float maxY = level.getHeight() - viewH / 2f;

        // Progression : 0 = tout à gauche / en bas, 1 = tout à droite / en haut
        float tx = (maxX > minX) ? (camera.position.x - minX) / (maxX - minX) : 0f;
        float ty = (maxY > minY) ? (camera.position.y - minY) / (maxY - minY) : 0f;
        tx = Math.max(0f, Math.min(1f, tx));
        ty = Math.max(0f, Math.min(1f, ty));

        // À gauche : le bord gauche du fond touche le bord gauche de l'écran.
        // À droite : le bord droit du fond touche le bord droit de l'écran.
        float bgX = viewLeft - tx * extraX;
        float bgY = viewBottom - ty * extraY;

        batch.draw(background, bgX, bgY, bgWidth, bgHeight);
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