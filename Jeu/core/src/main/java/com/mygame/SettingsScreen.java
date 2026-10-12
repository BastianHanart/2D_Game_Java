package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private final Game game;
    private Stage stage;
    private Skin skin;
    private Texture background;

    // Permet de savoir si on attend une nouvelle touche
    private boolean waitingForKey = false;

    // Touche actuellement en train d'être modifiée
    private String keyToChange = "";

    // Bouton actuellement utilisé pour changer une touche
    private TextButton waitingButton;
    private ScrollPane scrollPane;
    private TextButton backButton;

    public SettingsScreen(Game game) {
        this.game = game;

        // =====================================================
        //                    BACKGROUND
        // =====================================================

        background = new Texture("backgroundsettings.jpeg");

        // =====================================================
        //                       STAGE
        // =====================================================

        stage = new Stage(new ScreenViewport());

        // Le Stage reçoit les clics et la molette
        Gdx.input.setInputProcessor(stage);

        // =====================================================
        //                        SKIN
        // =====================================================

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);
        createSkin();

        // =====================================================
        //                    CONSTRUCTION
        // =====================================================

        createInterface();
    }

    // =========================================================
    //                         SKIN
    // =========================================================


    private void createSkin() {

        // =====================================================
        // POLICE
        // =====================================================

        BitmapFont font = new BitmapFont();
        skin.add("default-font",font);

        // =====================================================
        // TEXTURE DU BOUTON
        // =====================================================

        Texture buttonTexture = new Texture("removebg(1).png");
        Drawable buttonDrawable = new TextureRegionDrawable( new TextureRegion(buttonTexture));

        // =====================================================
        // STYLE DU BOUTON
        // =====================================================

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

        // Etat normal
        buttonStyle.up = buttonDrawable;

        // Etat quand la souris passe dessus
        buttonStyle.over = buttonDrawable;

        // Etat quand on clique
        buttonStyle.down = buttonDrawable;

        // Police
        buttonStyle.font = font;
        skin.add("default",buttonStyle);

        // =====================================================
        // STYLE DES LABELS
        // =====================================================

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        skin.add("default",labelStyle);
    }


    // =========================================================
    //                    INTERFACE
    // =========================================================

    private void createInterface() {

        // =====================================================
        // PANNEAU CENTRAL
        // =====================================================

        Table panel = new Table();
        panel.pad(35);
        panel.top();

        // =====================================================
        // TITRE
        // =====================================================

        Label title =new Label("PARAMETRES",skin);
        title.setFontScale(2.5f);
        panel.add(title).padBottom(40).row();

        // =====================================================
        // SECTION TOUCHES
        // =====================================================

        Label controlsTitle = new Label("TOUCHES",skin);
        controlsTitle.setFontScale(1.5f);
        panel.add(controlsTitle).padBottom(25).row();

        // =====================================================
        // TOUCHES
        // =====================================================

        panel.add(createKeyRow("GAUCHE",Settings.leftKey,"left")).width(600).height(70).padBottom(10).row();
        panel.add(createKeyRow("DROITE",Settings.rightKey,"right")).width(600).height(70).padBottom(10).row();
        panel.add(createKeyRow("TOMBER",Settings.downKey,"down")).width(600).height(70).padBottom(10).row();
        panel.add(createKeyRow("SAUT",Settings.jumpKey,"jump")).width(600).height(70).padBottom(10).row();
        panel.add(createKeyRow("DASH",Settings.dashKey,"dash")).width(600).height(70).padBottom(10).row();
        panel.add(createKeyRow("ATTAQUE",Settings.attackKey,"attack")).width(600).height(70).padBottom(10).row();
        panel.add(createKeyRow("MENU",Settings.menuKey,"menu")).width(600).height(70).padBottom(10).row();
        panel.add(createKeyRow("COURSE",Settings.runKey,"run")).width(600).height(70).padBottom(30).row();

        // =====================================================
        // SECTION AFFICHAGE
        // =====================================================

        Label displayTitle =
            new Label(
                "AFFICHAGE",
                skin
            );

        displayTitle.setFontScale(1.5f);
        panel.add(displayTitle).padBottom(25).row();

        // -----------------------------------------------------
        // PLEIN ECRAN
        // -----------------------------------------------------

        TextButton fullscreenButton =
            new TextButton(
                "PLEIN ECRAN",
                skin
            );

        fullscreenButton.getLabel().setFontScale(1.2f);
        fullscreenButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(
                    ChangeEvent event,
                    Actor actor
                ) {
                    Settings.setFullscreen();
                }
            }
        );

        panel.add(fullscreenButton).width(600).height(70).padBottom(10).row();

        // -----------------------------------------------------
        // FENETRE
        // -----------------------------------------------------

        TextButton windowedButton =
            new TextButton(
                "FENETRE",
                skin
            );
        windowedButton.getLabel().setFontScale(1.2f);
        windowedButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(
                    ChangeEvent event,
                    Actor actor
                ) {
                    Settings.setWindowed();
                }
            }
        );

        panel.add(windowedButton).width(600).height(70).padBottom(30).row();

        // =====================================================
        // RETOUR
        // =====================================================

        // RETOUR (hors du scroll, toujours visible)
        backButton = new TextButton("RETOUR", skin);
        backButton.getLabel().setFontScale(1.2f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        stage.addActor(backButton);

        // SCROLLPANE
        scrollPane = new ScrollPane(panel);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        stage.addActor(scrollPane);
        stage.setScrollFocus(scrollPane);

        updateLayout(stage.getViewport().getWorldWidth(),
             stage.getViewport().getWorldHeight());
    }


    // =========================================================
    //                    LIGNE DE TOUCHE
    // =========================================================

    private void updateLayout(float width, float height) {
        // Zone sombre du cadre, en proportion de la fenêtre
        float areaW = width * 0.55f;
        float areaH = height * 0.50f;
        float areaX = (width - areaW) / 2f;
        float areaY = height * 0.19f;   // bas de la zone (origine en bas à gauche)
        scrollPane.setBounds(areaX, areaY, areaW, areaH);
        // Bouton RETOUR centré sur la pierre du bas
        float btnW = 600f, btnH = 70f;
        backButton.setSize(btnW, btnH);
        backButton.setPosition((width - btnW) / 2f, height * 0.06f);
    }

    private Table createKeyRow(
        String name,
        int key,
        final String keyName
    ) {

        Table row = new Table();

        // -----------------------------------------------------
        // NOM
        // -----------------------------------------------------

        Label label = new Label(name,skin);
        label.setFontScale(1.2f);
        row.add(label).left().expandX().padLeft(20);

        // -----------------------------------------------------
        // BOUTON
        // -----------------------------------------------------

        final TextButton keyButton = new TextButton(getKeyName(key),skin);

        keyButton.getLabel().setFontScale(1.1f);

        row.add(keyButton).width(200).height(60).right().padRight(20);

        // -----------------------------------------------------
        // CLIC
        // -----------------------------------------------------

        keyButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(
                    ChangeEvent event,
                    Actor actor
                ) {

                    startChangingKey(
                        keyName,
                        keyButton
                    );
                }
            }
        );
        return row;
    }

    // =========================================================
    //                CHANGEMENT DE TOUCHE
    // =========================================================

    private void startChangingKey(
        String keyName,
        TextButton button
    ) {
        waitingForKey = true;
        keyToChange = keyName;
        waitingButton = button;
        button.setText("APPUYEZ...");
    }

    // =========================================================
    //                     NOM TOUCHE
    // =========================================================

    private String getKeyName(int key) {
        return Input.Keys.toString(key);
    }

    // =========================================================
    //                      INPUT
    // =========================================================

    @Override
    public void render(float delta) {

        // -----------------------------------------------------
        // BACKGROUND
        // -----------------------------------------------------

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // -----------------------------------------------------
        // DESSIN DU BACKGROUND
        // -----------------------------------------------------

        stage.getBatch().begin();
        stage.getBatch().draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.getBatch().end();

        // -----------------------------------------------------
        // TOUCHE EN ATTENTE
        // -----------------------------------------------------

        if (waitingForKey) {
            checkForKey();
        }

        // -----------------------------------------------------
        // STAGE
        // -----------------------------------------------------

        stage.act(delta);
        stage.draw();
    }

    // =========================================================
    //              DETECTION NOUVELLE TOUCHE
    // =========================================================

    private void checkForKey() {
        for (int key = 0; key < 256; key++) {
            if (
                Gdx.input.isKeyJustPressed(key)
            ) {

                // ---------------------------------------------
                // GAUCHE
                // ---------------------------------------------

                if (
                    keyToChange.equals("left")
                ) {
                    Settings.leftKey = key;
                }

                // ---------------------------------------------
                // DROITE
                // ---------------------------------------------

                else if (
                    keyToChange.equals("right")
                ) {
                    Settings.rightKey = key;
                }

                // ---------------------------------------------
                // SAUT
                // ---------------------------------------------

                else if (
                    keyToChange.equals("jump")
                ) {
                    Settings.jumpKey = key;
                }

                // ---------------------------------------------
                // DASH
                // ---------------------------------------------

                else if (
                    keyToChange.equals("dash")
                ) {
                    Settings.dashKey = key;
                }

                // ---------------------------------------------
                // ATTAQUE
                // ---------------------------------------------

                else if (
                    keyToChange.equals("attack")
                ) {
                    Settings.attackKey = key;
                }

                // ---------------------------------------------
                // MENU
                // ---------------------------------------------

                else if (
                    keyToChange.equals("menu")
                ) {
                    Settings.menuKey = key;
                }

                // ---------------------------------------------
                // COURSE
                // ---------------------------------------------

                else if (
                    keyToChange.equals("run")
                ) {
                    Settings.runKey = key;
                }

                // ---------------------------------------------
                // FIN
                // ---------------------------------------------

                waitingForKey = false;
                keyToChange = "";
                if (waitingButton != null) {
                    waitingButton.setText(
                        getKeyName(key)
                    );
                    waitingButton = null;
                }
                break;
            }
        }
    }

    // =========================================================
    //                       RESIZE
    // =========================================================

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        updateLayout(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
    }

    // =========================================================
    //                       DISPOSE
    // =========================================================

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        background.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
