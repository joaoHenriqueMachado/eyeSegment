package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ToolsTable extends Table {
    private ImageButton brush;
    private ImageButton eraser;
    private ImageButton move;
    private ImageButton hide_show;
    private ImageButton undo;
    private ImageButton.ImageButtonStyle undoOnStyle;
    private ImageButton.ImageButtonStyle undoOffStyle;
    private ImageButton redo;
    private ImageButton.ImageButtonStyle redoOnStyle;
    private ImageButton.ImageButtonStyle redoOffStyle;
    private ImageButton settings;
    private ImageButton.ImageButtonStyle settingsStyle;

    public ToolsTable(final Skin skins, final MainScreen mainScreen){
        // Brush button
        ImageButton.ImageButtonStyle brushStyle = new ImageButton.ImageButtonStyle();
        brushStyle.imageUp = skins.getDrawable("brushOff");
        brushStyle.imageChecked = skins.getDrawable("brush");
        brushStyle.imageDown = skins.getDrawable("brush");
        brush = new ImageButton(skins.getDrawable("brush"));
        brush.setStyle(brushStyle);

        // Eraser button
        eraser = new ImageButton(skins.getDrawable("eraser"));
        ImageButton.ImageButtonStyle eraserStyle = new ImageButton.ImageButtonStyle();
        eraserStyle.imageUp = skins.getDrawable("eraserOff");
        eraserStyle.imageChecked = skins.getDrawable("eraser");
        eraserStyle.imageDown = skins.getDrawable("eraser");
        eraser.setStyle(eraserStyle);

        // Move button
        move = new ImageButton(skins.getDrawable("move"));
        ImageButton.ImageButtonStyle moveStyle = new ImageButton.ImageButtonStyle();
        moveStyle.imageUp = skins.getDrawable("moveOff");
        moveStyle.imageDown = skins.getDrawable("move");
        moveStyle.imageChecked = skins.getDrawable("move");
        move.setStyle(moveStyle);

        // Hide and Show button
        // This button hides the drawing while pressed
        hide_show = new ImageButton(skins.getDrawable("show"));
        ImageButton.ImageButtonStyle hide_showStyle = new ImageButton.ImageButtonStyle();
        hide_showStyle.imageUp = skins.getDrawable("show");
        hide_showStyle.imageDown = skins.getDrawable("hide");
        hide_show.setStyle(hide_showStyle);

        undoOffStyle = new ImageButton.ImageButtonStyle();
        undoOffStyle.imageUp = skins.getDrawable("undoOff");
        undoOnStyle = new ImageButton.ImageButtonStyle();
        undoOnStyle.imageUp = skins.getDrawable("undoOn");
        undo = new ImageButton(skins.getDrawable("undoOff"));
        undo.setStyle(undoOffStyle);

        redoOffStyle = new ImageButton.ImageButtonStyle();
        redoOffStyle.imageUp = skins.getDrawable("redoOff");
        redoOnStyle = new ImageButton.ImageButtonStyle();
        redoOnStyle.imageUp = skins.getDrawable("redoOn");
        redo = new ImageButton(skins.getDrawable("redoOff"));
        redo.setStyle(redoOffStyle);

        settings = new ImageButton(skins.getDrawable("settings"));

        this.setBounds(0, 0, 72, Gdx.graphics.getHeight());
        this.background(skins.getDrawable("whiteBackground"));
        this.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        this.add(move).padBottom(24);
        this.row();
        this.add(brush).padBottom(24);
        this.row();
        this.add(eraser).padBottom(24);
        this.row();
        this.add(undo).padBottom(24);
        this.row();
        this.add(redo).padBottom(24);
        this.row();
        this.add(hide_show).padBottom(24);
        this.row();
        this.add(settings);
        this.row();
        this.align(Align.center);

        // Creating a button group, so only one of the tools will be active at the same time
        ButtonGroup<ImageButton> toolsGroup = new ButtonGroup<>(brush, eraser, move);
        toolsGroup.setMaxCheckCount(1);
        toolsGroup.setMinCheckCount(0);
        toolsGroup.setUncheckLast(true);

        move.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(move.isChecked()){
                    mainScreen.getDrawingProcessor().setOptionIndex(1);
                    //Gdx.graphics.setCursor(handCursor);
                }
                else {
                    mainScreen.getDrawingProcessor().setOptionIndex(0);
                }
                super.clicked(event, x, y);
            }
        });

        brush.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(brush.isChecked()){
                    //Gdx.graphics.setCursor(crosshairCursor);
                    mainScreen.getDrawingProcessor().setOptionIndex(2);
                }
                else {
                    //Gdx.graphics.setCursor(pointerCursor);
                    mainScreen.getDrawingProcessor().setOptionIndex(0);
                }
                super.clicked(event, x, y);
            }
        });

        eraser.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(eraser.isChecked()){
                    mainScreen.getDrawingProcessor().setOptionIndex(3);
                    //Gdx.graphics.setCursor(crosshairCursor);
                }
                else {
                    mainScreen.getDrawingProcessor().setOptionIndex(0);
                    //Gdx.graphics.setCursor(pointerCursor);
                }
                super.clicked(event, x, y);
            }
        });

        hide_show.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getMainTable().hideDrawing();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getMainTable().showDrawing();
                super.touchUp(event, x, y, pointer, button);
            }
        });

        undo.addListener(new ClickListener(){
            Pixmap lastState;
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!mainScreen.getUndoStack().isEmpty()){
                    lastState = mainScreen.getUndoStack().pop();

                    Pixmap redoState = new Pixmap(mainScreen.getPixmap().getWidth(), mainScreen.getPixmap().getHeight(), mainScreen.getPixmap().getFormat());
                    redoState.drawPixmap(mainScreen.getPixmap(), 0, 0);
                    mainScreen.getRedoStack().addElement(redoState);

                    mainScreen.getPixmap().drawPixmap(lastState, 0,0);
                    mainScreen.getMainTable().updatePixmap();

                    lastState.dispose();
                    redo.setStyle(redoOnStyle);
                    if(mainScreen.getUndoStack().isEmpty()){
                        undo.setStyle(undoOffStyle);
                    }
                }
                super.clicked(event, x, y);
            }
        });
        redo.addListener(new ClickListener(){
            Pixmap lastState;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!mainScreen.getRedoStack().isEmpty()){
                    lastState = mainScreen.getRedoStack().pop();

                    Pixmap undoState = new Pixmap(mainScreen.getPixmap().getWidth(), mainScreen.getPixmap().getHeight(), mainScreen.getPixmap().getFormat());
                    undoState.drawPixmap(mainScreen.getPixmap(), 0, 0);
                    mainScreen.getUndoStack().addElement(undoState);

                    mainScreen.getPixmap().drawPixmap(lastState, 0,0);
                    mainScreen.getMainTable().updatePixmap();

                    undo.setStyle(undoOnStyle);
                    if(mainScreen.getRedoStack().isEmpty()){
                        redo.setStyle(redoOffStyle);
                    }
                }
                super.clicked(event, x, y);
            }
        });

        settings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.getMainStage().addActor(mainScreen.getSettingsWindow());
                super.clicked(event, x, y);
            }
        });
    }

    public ImageButton getMove() {
        return move;
    }

    public ImageButton getBrush() {
        return brush;
    }

    public ImageButton getEraser() {
        return eraser;
    }

    public ImageButton getUndo() {
        return undo;
    }

    public ImageButton.ImageButtonStyle getUndoOnStyle() {
        return undoOnStyle;
    }

    public ImageButton.ImageButtonStyle getUndoOffStyle() {
        return undoOffStyle;
    }

    public ImageButton getRedo() {
        return redo;
    }

    public ImageButton.ImageButtonStyle getRedoOffStyle() {
        return redoOffStyle;
    }

    public ImageButton getSettings() {
        return settings;
    }
}