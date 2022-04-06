package com.mygdx.teste;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.io.File;

public class FileWindow extends Window {
    private TextButton saveButton;
    private TextButton closeButton;
    private TextButton openButton;
    private TextButton importMask;
    private TextButton importSegmentation;
    private int numButtons;

    public FileWindow(String title, WindowStyle style, final Teste application, float posX, float posY) {
        super(title, style);
        this.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        numButtons = 5;
        this.setBounds(posX, posY, application.getResources().getNavigationButtonWidth(), numButtons * (application.getResources().getNavigationButtonHeight() + application.getResources().getNavigationButtonPad()));
        saveButton = new TextButton("   Save", application.getResources().getNavBarStyle());
        openButton = new TextButton("   Open", application.getResources().getNavBarStyle());
        closeButton = new TextButton("   Close", application.getResources().getNavBarStyle());
        importMask = new TextButton("   Import Mask", application.getResources().getNavBarStyle());
        importSegmentation = new TextButton("   Import Segmentation", application.getResources().getNavBarStyle());

        this.left();
        openButton.getLabel().setAlignment(Align.left);
        closeButton.getLabel().setAlignment(Align.left);
        saveButton.getLabel().setAlignment(Align.left);
        importMask.getLabel().setAlignment(Align.left);
        importSegmentation.getLabel().setAlignment(Align.left);
        this.add(openButton).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad()).row();
        this.add(closeButton).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad()).row();
        this.add(saveButton).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad()).row();
        this.add(importMask).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad()).row();
        this.add(importSegmentation).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad());

        openButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getResources().getJsfc().setFileFilter(application.getResources().getFilter());
                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                int returnValue = application.getResources().getJsfc().showOpenDialog(null);
                if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
                    File selectedFile = application.getResources().getJsfc().getSelectedFile();
                    application.getMainScreen().clearScreen();
                    application.getMainScreen().loadTexture(new Texture(selectedFile.getAbsolutePath()));
                    if(!application.getMainScreen().getTexture().getTextureData().isPrepared()){
                        application.getMainScreen().getTexture().getTextureData().prepare();
                    }
                    try {
                        application.getFilters().loadSourceImg(selectedFile.getAbsolutePath());
                        System.out.println("New image loaded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                application.getMainScreen().getNavigationBar().getFileWindow().remove();
                application.getMainScreen().getNavigationBar().getFileButton().setChecked(false);
                super.clicked(event, x, y);
            }
        });

        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setScreen(application.getWelcomeScreen());
                application.getMainScreen().getNavigationBar().getFileButton().setChecked(false);
                application.getMainScreen().clearScreen();
            }
        });

        saveButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                int returnValue = application.getResources().getJsfc().showSaveDialog(null);
                File f = application.getResources().getJsfc().getSelectedFile();
                if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
                    try{
                        FileHandle fh;
                        fh = new FileHandle(f.getAbsolutePath() + ".png");
                        Pixmap blackWhite = new Pixmap(application.getMainScreen().getPixmap().getWidth(), application.getMainScreen().getPixmap().getHeight(), Pixmap.Format.RGB888);
                        blackWhite.drawPixmap(application.getMainScreen().getPixmap(), 0, 0);
                        PixmapIO.writePNG(fh, blackWhite);
                        blackWhite.dispose();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                application.getMainScreen().getNavigationBar().getFileWindow().remove();
                application.getMainScreen().getNavigationBar().getFileButton().setChecked(false);
                super.clicked(event, x, y);
            }
        });

        importMask.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getResources().getJsfc().setFileFilter(application.getResources().getFilter());
                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                int result = application.getResources().getJsfc().showOpenDialog(null);
                if(result == JSystemFileChooser.APPROVE_OPTION){
                    File selectedFile = application.getResources().getJsfc().getSelectedFile();
                    application.getMainScreen().setMaskPath(selectedFile.getAbsolutePath());
                }
                application.getMainScreen().getNavigationBar().getFileWindow().remove();
                application.getMainScreen().getNavigationBar().getFileButton().setChecked(false);
                super.clicked(event, x, y);
            }
        });

        importSegmentation.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Pixmap aux = null;

                application.getResources().getJsfc().setFileFilter(application.getResources().getFilter());
                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                int returnValue = application.getResources().getJsfc().showOpenDialog(null);
                if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
                    File selectedFile = application.getResources().getJsfc().getSelectedFile();
                    if(application.getMainScreen().getGtTexture() != null){
                        application.getMainScreen().getGtTexture().dispose();
                    }
                    application.getMainScreen().setGtTexture(new Texture(selectedFile.getAbsolutePath()));
                    if(!application.getMainScreen().getGtTexture().getTextureData().isPrepared()){
                        application.getMainScreen().getGtTexture().getTextureData().prepare();
                    }
                    Pixmap undoState = new Pixmap(application.getMainScreen().getPixmap().getWidth(), application.getMainScreen().getPixmap().getHeight(), application.getMainScreen().getPixmap().getFormat());
                    undoState.drawPixmap(application.getMainScreen().getPixmap(), 0, 0);
                    application.getMainScreen().getUndoStack().addElement(undoState);
                    application.getMainScreen().getToolsTable().getUndo().setStyle(application.getMainScreen().getToolsTable().getUndoOnStyle());
                    while(!application.getMainScreen().getRedoStack().isEmpty()){
                        aux = application.getMainScreen().getRedoStack().pop();
                        aux.dispose();
                    }
                    application.getMainScreen().getToolsTable().getRedo().setStyle(application.getMainScreen().getToolsTable().getRedoOffStyle());
                    aux = new Pixmap(application.getMainScreen().getPixmap().getWidth(), application.getMainScreen().getPixmap().getHeight(), application.getMainScreen().getPixmap().getFormat());
                    aux.drawPixmap(application.getMainScreen().getGtTexture().getTextureData().consumePixmap(), 0, 0);

                    for(int i = 0; i < application.getMainScreen().getPixmap().getWidth(); i++){
                        for(int j = 0; j < application.getMainScreen().getPixmap().getHeight(); j++){
                            if(aux.getPixel(i, j) != aux.getPixel(0, 0)){
                                application.getMainScreen().getPixmap().drawPixel(i, j, aux.getPixel(i,j));
                            }
                        }
                    }
                    application.getMainScreen().getMainTable().updatePixmap();
                }
                application.getMainScreen().getNavigationBar().getFileWindow().remove();
                application.getMainScreen().getNavigationBar().getFileButton().setChecked(false);
                super.clicked(event, x, y);
            }
        });
    }
}