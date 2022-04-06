package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;

public class WelcomeScreen implements Screen{

    Stage welcomeStage;
    Viewport viewport;

    TextButton button;
    TextButton.TextButtonStyle buttonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    Texture texture;
    Teste application;
    Table welcomeTable;
    NinePatchDrawable buttonOn;
    NinePatchDrawable buttonOff;

    public WelcomeScreen(final Teste application){
        this.application = application;

        //camera and viewport
        welcomeStage = new Stage();
        viewport = new ScreenViewport(welcomeStage.getCamera());
        welcomeStage.setViewport(viewport);

        //button
        font = application.getFonts().getRobotoRegular16();
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonOff = new NinePatchDrawable(application.getResources().getSkin().getPatch("ButtonOff"));
        buttonOn = new NinePatchDrawable(application.getResources().getSkin().getPatch("ButtonOn"));
        buttonStyle.up = buttonOff;
        button = new TextButton("Open an image", buttonStyle);

        button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                application.getResources().getJsfc().setFileFilter(application.getResources().getFilter());
                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                int returnValue = application.getResources().getJsfc().showOpenDialog(null);
                if(returnValue == JSystemFileChooser.APPROVE_OPTION){
                    File selectedFile = application.getResources().getJsfc().getSelectedFile();
                    texture = new Texture(selectedFile.getAbsolutePath());
                    try {
                        application.getFilters().loadSourceImg(selectedFile.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    application.mainScreen.loadTexture(texture);
                    application.setScreen(application.getMainScreen());
                }
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                buttonStyle.up = buttonOn;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                buttonStyle.up = buttonOff;
            }
        });

        welcomeTable = new Table();
        welcomeTable.setFillParent(true);
        welcomeTable.center();
        welcomeTable.add(button).width(196).height(64);
        welcomeStage.addActor(welcomeTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(welcomeStage);
    }

    @Override
    public void resize(int width, int height) {
        welcomeStage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1647f, 0.1647f, 0.1647f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        welcomeStage.getCamera().update();
        welcomeStage.act(delta);
        welcomeStage.draw();
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
        buttonAtlas.dispose();
        welcomeStage.dispose();
        skin.dispose();
        font.dispose();
        texture.dispose();
        welcomeStage.dispose();
    }
}

