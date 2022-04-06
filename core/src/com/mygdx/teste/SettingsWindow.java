package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class SettingsWindow extends Window {
    private TextButton close;
    // brush settings
    private Label settings;

    // brush size
    private Label brushSizeLabel;
    private TextField brushSize;
    private ImageButton increaseBrushSize;
    private ImageButton decreaseBrushSize;

    // brush opacity
    private Label brushOpacityLabel;
    private TextField brushOpacity;
    private ImageButton increaseBrushOpacity;
    private ImageButton decreaseBrushOpacity;

    // brush color
    private Label brushColorLabel;
    private ImageButton red;
    private ImageButton white;
    private ImageButton black;
    private ImageButton blue;
    private ImageButton purple;

    // eraser settings
    private Label eraserSize;
    private ImageButton decreaseEraserSize;
    private ImageButton increaseEraserSize;
    private TextField eraserSizeField;
    private Vector2 windowCoordinates;

    private MainScreen mainScreen;

    public SettingsWindow(String title, WindowStyle style, final MainScreen mainScreen){
        super(title, style);

        this.mainScreen = mainScreen;
        close = new TextButton("Close", mainScreen.application.getResources().getCloseStyle());

        settings = new Label("Editor Settings", mainScreen.application.getResources().getTitleStyle());
        settings.setAlignment(Align.left);
        brushSizeLabel = new Label("Brush size", getTitleLabel().getStyle());
        brushSizeLabel.setAlignment(Align.left);
        brushSize = new TextField(Integer.toString(mainScreen.getDrawingProcessor().brushRadius), mainScreen.application.getResources().getTextFieldStyle());
        brushSize.setAlignment(Align.center);
        brushSize.setTextFieldFilter(brushSize.getTextFieldFilter());


        brushSize.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(brushSize.isDisabled()){
                    brushSize.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        TextField.TextFieldListener brushSizeListener = new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        mainScreen.getDrawingProcessor().setBrushRadius(Integer.parseInt(textField.getText()));
                    } catch (Exception e) {
                        System.out.println("Only number entries are accepted");
                        textField.setText(Float.toString(mainScreen.getDrawingProcessor().getBrushRadius()));
                        e.printStackTrace();
                    }
                    textField.setDisabled(true);
                }
            }
        };
        brushSize.setTextFieldListener(brushSizeListener);

        increaseBrushSize = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("plus"));
        decreaseBrushSize = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("remove"));

        increaseBrushSize.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getDrawingProcessor().brushRadius++;
                brushSize.setText(Integer.toString(mainScreen.getDrawingProcessor().brushRadius));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        decreaseBrushSize.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mainScreen.getDrawingProcessor().brushRadius > 0){
                    mainScreen.getDrawingProcessor().brushRadius--;
                    brushSize.setText(Integer.toString(mainScreen.getDrawingProcessor().brushRadius));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        brushOpacityLabel = new Label("Opacity", getTitleLabel().getStyle());
        brushOpacityLabel.setAlignment(Align.left);
        brushOpacity = new TextField(Float.toString(mainScreen.getDrawingProcessor().getBrushColor().a), mainScreen.application.getResources().getTextFieldStyle());
        brushOpacity.setAlignment(Align.center);
        brushOpacity.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(brushOpacity.isDisabled()){
                    brushOpacity.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        brushOpacity.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        String text = textField.getText();
                        text = text.replaceAll(",", ".");

                        mainScreen.getDrawingProcessor().setBrushColor(new Color( mainScreen.getDrawingProcessor().getBrushColor().r,  mainScreen.getDrawingProcessor().getBrushColor().g,  mainScreen.getDrawingProcessor().getBrushColor().b, Float.parseFloat(brushOpacity.getText())));

                        text = text.replaceAll("\\.", ",");
                        textField.setText(text);
                    } catch (Exception e) {
                        System.out.println("Only number entries are accepted");
                        textField.setText(Float.toString( mainScreen.getDrawingProcessor().getBrushColor().a));
                        e.printStackTrace();
                    }
                    textField.setDisabled(true);
                }
            }
        });

        increaseBrushOpacity = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("plus"));
        decreaseBrushOpacity = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("remove"));

        increaseBrushOpacity.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(mainScreen.getDrawingProcessor().getBrushColor().a <= 1){
                    mainScreen.getDrawingProcessor().setBrushColor(new Color(mainScreen.getDrawingProcessor().getBrushColor().r, mainScreen.getDrawingProcessor().getBrushColor().g, mainScreen.getDrawingProcessor().getBrushColor().b, mainScreen.getDrawingProcessor().getBrushColor().a + (float)0.01));
                    brushOpacity.setText(String.format("%.2f", mainScreen.getDrawingProcessor().getBrushColor().a));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        decreaseBrushOpacity.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(mainScreen.getDrawingProcessor().getBrushColor().a >= 0.02){
                    mainScreen.getDrawingProcessor().setBrushColor(new Color(mainScreen.getDrawingProcessor().getBrushColor().r, mainScreen.getDrawingProcessor().getBrushColor().g, mainScreen.getDrawingProcessor().getBrushColor().b, mainScreen.getDrawingProcessor().getBrushColor().a - (float)0.01));
                    brushOpacity.setText(String.format("%.2f", mainScreen.getDrawingProcessor().getBrushColor().a));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        brushColorLabel = new Label("Color", getTitleLabel().getStyle());
        brushColorLabel.setAlignment(Align.left);
        red = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("RedButton"));
        white = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("WhiteButton"));
        black = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("BlackButton"));
        blue = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("BlueButton"));
        purple = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("PurpleButton"));

        red.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getDrawingProcessor().setBrushColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, mainScreen.getDrawingProcessor().getBrushColor().a));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        white.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getDrawingProcessor().setBrushColor(new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, mainScreen.getDrawingProcessor().getBrushColor().a));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        blue.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getDrawingProcessor().setBrushColor(new Color(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, mainScreen.getDrawingProcessor().getBrushColor().a));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        purple.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getDrawingProcessor().setBrushColor(new Color(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, mainScreen.getDrawingProcessor().getBrushColor().a));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        black.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getDrawingProcessor().setBrushColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, mainScreen.getDrawingProcessor().getBrushColor().a));
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        eraserSize = new Label("Eraser size", getTitleLabel().getStyle());
        eraserSize.setAlignment(Align.left);

        decreaseEraserSize = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("remove"));
        decreaseEraserSize.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mainScreen.getDrawingProcessor().eraserRadius > 0){
                    mainScreen.getDrawingProcessor().eraserRadius--;
                    eraserSizeField.setText(Integer.toString(mainScreen.getDrawingProcessor().eraserRadius));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        increaseEraserSize = new ImageButton(mainScreen.application.getResources().getSkin().getDrawable("plus"));
        increaseEraserSize.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScreen.getDrawingProcessor().eraserRadius++;
                eraserSizeField.setText(Integer.toString(mainScreen.getDrawingProcessor().eraserRadius));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        eraserSizeField = new TextField(Integer.toString(mainScreen.getDrawingProcessor().getEraserRadius()), mainScreen.application.getResources().getTextFieldStyle());
        eraserSizeField.setAlignment(Align.center);
        eraserSizeField.setTextFieldFilter(eraserSizeField.getTextFieldFilter());
        eraserSizeField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        mainScreen.getDrawingProcessor().setEraserRadius(Integer.parseInt(textField.getText()));
                    } catch (Exception e) {
                        System.out.println("Only number entries are accepted");
                        textField.setText(Integer.toString(mainScreen.getDrawingProcessor().getEraserRadius()));
                        e.printStackTrace();
                    }
                    textField.setDisabled(true);
                }
            }
        });

        close.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.getSettingsWindow().remove();
                super.clicked(event, x, y);
            }
        });


        this.windowCoordinates = localToStageCoordinates(new Vector2(mainScreen.getToolsTable().getSettings().getX(), mainScreen.getToolsTable().getSettings().getY()));
        this.setBounds(windowCoordinates.x, Gdx.graphics.getHeight() - windowCoordinates.y, 304, 400);
        this.left().padLeft(48);
        this.setBackground(mainScreen.application.getResources().getSkin().getDrawable("BlackBackground"));
        this.add(settings).width(32).height(32).row();
        this.add(brushSizeLabel).width(32).height(32).row();
        this.add(decreaseBrushSize).width(32).height(32);
        this.add(brushSize).width(96).height(32);
        this.add(increaseBrushSize).width(32).height(32).row();
        this.add(brushColorLabel).width(32).height(32).row();
        this.add(white).width(32).height(32);
        this.add(red).width(32).height(32).padLeft(-48);
        this.add(purple).width(32).height(32).padLeft(-96);
        this.add(blue).width(32).height(32).padLeft(-48);
        this.add(black).width(32).height(32).row();
        this.add(brushOpacityLabel).width(32).height(32).row();
        this.add(decreaseBrushOpacity).width(32).height(32);
        this.add(brushOpacity).width(96).height(32);
        this.add(increaseBrushOpacity).width(32).height(32).row();
        this.add(eraserSize).width(32).height(32).row();
        this.add(decreaseEraserSize).width(32).height(32);
        this.add(eraserSizeField).width(96).height(32);
        this.add(increaseEraserSize).width(32).height(32).row();
        this.add(close).width(96).height(32).colspan(3).padTop(32).padLeft(-48);
    }

    public void resize(){
        this.setPosition(mainScreen.getToolsTable().getWidth() + 4 , Gdx.graphics.getHeight()/2 - 200);
    }

}

