package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private Character character;
    private Array<Monster> monsters;
    private Array<Villageois> villageois;
    private OrthographicCamera camera;
    private Level level;
    private int currentLevel = 1;
    private static final float BACKGROUND_HEIGHT_FACTOR = 1.3f;
    private static final float BACKGROUND_WIDTH_FACTOR = 1.2f;
    private HealthBar healthBar;
    private boolean playerAttackHit = false;

    private final SaveData loaded;

    public GameScreen(Game game) { this(game, null); }

    public GameScreen(Game game, SaveData loaded) {
        this.game = game;
        this.loaded = loaded;
        if (loaded != null) currentLevel = loaded.level;
    }

    @Override
    public void show() {
        if (character != null) {
            Gdx.input.setInputProcessor(null);
            level.getMusic().play();
            return;
        }
        Gdx.input.setInputProcessor(null);
        batch = new SpriteBatch();
        character = new Character(game);
        level = LevelManager.loadLevel(currentLevel, game);
        healthBar = new HealthBar();
        if (loaded != null) {
            character.setPosition(loaded.x, loaded.y);
            character.setHP(loaded.hp);
        } else {
            character.setPosition(150, 700);
        }
        level.getMusic().play();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(character.getX(), character.getY() + 150, 0);
        camera.update();
        monsters = level.getMonsters();
        SaveManager.saveCurrent(character, currentLevel);
        villageois = level.getVillageois();
    }

    // ===========================================
    //                Niveaux
    // ===========================================

    private void changeLevel(int levelNumber, boolean enterFromRight) {
        level.getMusic().stop();
        level.dispose();
        level = LevelManager.loadLevel(levelNumber, game);
        monsters = level.getMonsters();
        villageois = level.getVillageois();

        placePlayer(enterFromRight ? "Right" : "Left");
        updateCamera(true);
        level.getMusic().play();
        SaveManager.saveCurrent(character, currentLevel);
    }

    private void checkLevelChange() {
        Body body = character.getBody();

        if (body.x + body.w / 2f >= level.getWidth()) {
            currentLevel = currentLevel + 1;
            changeLevel(currentLevel, false);
        } else if (body.x - body.w / 2f <= 0) {
            if (currentLevel > 1) {
                currentLevel = currentLevel - 1;
                changeLevel(currentLevel, true);
            } else {
                body.x = body.w / 2f;   // mur invisible au bord du niveau 1
            }
        }
    }

    private void placePlayer(String spawnType) {
        com.badlogic.gdx.math.Vector2 p = level.getSpawn(spawnType);
        if (p != null) {
            character.setPosition(p.x, p.y);
        } else {
            character.setPosition(150, 700); // secours si le point n'existe pas
        }
    }

    // ===========================================
    //                CAMERA
    // ===========================================

    private void updateCamera(boolean snap) {
        float halfW = camera.viewportWidth / 2f;
        float halfH = camera.viewportHeight / 2f;

        float targetX = character.getX();
        float targetY = character.getY() + camera.viewportHeight / 3f;

        targetX = Math.max(halfW, Math.min(level.getWidth() - halfW, targetX));
        targetY = Math.max(halfH, Math.min(level.getHeight() - halfH, targetY));
        if (snap) {
                camera.position.set(targetX, targetY, 0);
        } else {
        float t = 5f * Gdx.graphics.getDeltaTime();
        camera.position.x += (targetX - camera.position.x) * t;
        camera.position.y += (targetY - camera.position.y) * t;
        }
        camera.update();
    }

    // ===========================================
    //                  RENDER
    // ===========================================

    @Override
    public void render(float delta) {
        GameState.playTime += delta;
        // Evite les gros sauts de physique si une frame est très longue
        delta = Math.min(delta, 1 / 30f);

        if (!character.isDead()) {
            character.update(delta, level.getCollision());
        }

        else {
            level.getMusic().stop();
            game.setScreen(new MenuScreen(game));
        }

        if (!character.isAttacking()) {
            playerAttackHit = false;
        }

        for (Monster monster : monsters) {
            if (!monster.isDead()
                    && character.isAttacking()
                    && !playerAttackHit
                    && character.getAttackBox().overlaps(monster.getHitbox())) {

                monster.takeDamage(character.getDamage());
                playerAttackHit = true;
                if (monster.isDead()) {
                    boolean niveauGagne = GameState.addXp(monster.getXPReward());
                    if (niveauGagne) {
                        character.recalculerStats();
                        SaveManager.saveCurrent(character, currentLevel);
                    }
                }
            }
        }

        for (Monster monster : monsters) {
            if (!monster.isDead() && monster instanceof Squelette) {
                ((Squelette) monster).update(delta, level.getCollision(), character);
            }
            if (!monster.isDead() && monster instanceof Fantome) {
                ((Fantome) monster).update(delta, level.getCollision(), character);
            }
            if (!monster.isDead() && monster instanceof Envie) {
                ((Envie) monster).update(delta, level.getCollision(), character);
            }
        }

        for (Villageois pnj : villageois) {
            pnj.update(delta, level.getCollision(), character);
        }

        checkLevelChange();
        updateCamera(false);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderBackground();
        for (int i = 1; i < level.getLayers().size; i++) {
            Texture t = level.getLayers().get(i);
            batch.draw(t, 0, 0, level.getWidth(), level.getHeight());
        }
        for (Monster monster : monsters) {
            if (!monster.isDead() && monster instanceof Squelette) {
                ((Squelette) monster).render(batch);
            }
            else if (!monster.isDead() && monster instanceof Fantome) {
                ((Fantome) monster).render(batch);
            }
            else if (!monster.isDead() && monster instanceof Envie) {
                ((Envie) monster).render(batch);
            }
        }
        for (Villageois pnj : villageois) {
            pnj.render(batch);
        }
        character.render(batch);
        float viewLeft = camera.position.x - camera.viewportWidth / 2f;
        float viewTop = camera.position.y + camera.viewportHeight / 2f;
        float margeX = 20;
        float margeY = 20;
        float posX = viewLeft + margeX;
        float posY = viewTop - margeY - healthBar.getHeightHealthBar();
        healthBar.draw(batch, posX, posY, character.getHP(), character.getHPMax());
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
        if (level != null) level.getMusic().pause();

        if (character != null) SaveManager.saveCurrent(character, currentLevel);
    }

    @Override
    public void dispose() {
        batch.dispose();
        character.dispose();
        for (Monster monster : monsters) {
            if (monster instanceof Squelette) {
                ((Squelette) monster).dispose();
            }
            if (monster instanceof Fantome) {
                ((Fantome) monster).dispose();
            }
            if (monster instanceof Envie) {
                ((Envie) monster).dispose();
            }
        }
        for (Villageois pnj : villageois)
            pnj.dispose();
        level.dispose();
    }
}