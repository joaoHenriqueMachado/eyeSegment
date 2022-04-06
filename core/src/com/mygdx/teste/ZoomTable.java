package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ZoomTable extends Table {
    ImageButton zoomIn;
    ImageButton zoomOut;
    ImageButton adjustToScreen;
    TextField zoomDisplay;
    Teste application;
    MainScreen mainScreen;

    public ZoomTable(final Skin skins, final Teste application, final MainScreen mainScreen){
        this.background(skins.getDrawable("whiteBackground"));
        this.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        this.setBounds(72, 0, Gdx.graphics.getWidth(), 48);
        this.left();
        this.application = application;
        this.mainScreen = mainScreen;

        // Creating and adding zoom in button to the table
        zoomIn = new ImageButton(skins.getDrawable("zoom_in"));
        this.add(zoomIn).padRight(16).padLeft(16);

        // Creating and adding zoom display text field to the table
        zoomDisplay = new TextField(String.format("%.2f", (1 / mainScreen.getCamera().zoom) * 100 ), application.getResources().getTextFieldStyle());
        zoomDisplay.setAlignment(Align.center);
        this.add(zoomDisplay).width(64).height(32).padRight(8);

        // Adding a percentage text at the side of the zoom display
        Label.LabelStyle labelStyle = new Label.LabelStyle(application.getFonts().getRobotoRegular24(), Color.WHITE);
        Label percentageText = new Label("%", labelStyle);
        this.add(percentageText).width(percentageText.getWidth()).padRight(16);

        // Creating and adding zoom out button to the table
        zoomOut = new ImageButton(skins.getDrawable("zoom_out"));
        this.add(zoomOut).padRight(16);

        // Creating and adding adjust to screen button to the table
        // This button will adjust the zoom of the image, so it fits the screen
        adjustToScreen = new ImageButton(skins.getDrawable("adjust"));
        this.add(adjustToScreen);

        /* Zoom section */

        zoomIn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(mainScreen.getCamera().zoom > 0.1f){
                    mainScreen.getCamera().zoom -= 0.05f;
                    zoomDisplay.setText(String.format("%.2f", (1 / mainScreen.getCamera().zoom) * 100));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        adjustToScreen.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Gdx.graphics.getHeight() < Gdx.graphics.getWidth())
                    mainScreen.getCamera().zoom = mainScreen.getMainTable().getHeight() / (Gdx.graphics.getHeight() - 56) ;
                else
                    mainScreen.getCamera().zoom = mainScreen.getMainTable().getWidth() / Gdx.graphics.getWidth() ;
                mainScreen.getCamera().position.set(mainScreen.getMainTable().getX() + (mainScreen.getMainTable().getWidth())/2, mainScreen.getMainTable().getY() + mainScreen.getMainTable().getHeight()/2 , 0 );
                mainScreen.getMainTable().move(18, 4);

                zoomDisplay.setText(String.format("%.2f", (1 / mainScreen.getCamera().zoom) * 100));
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        zoomOut.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(mainScreen.getCamera().zoom <= 1.95f){
                    mainScreen.getCamera().zoom += 0.05f;
                }
                else{
                    if(Gdx.graphics.getHeight() < Gdx.graphics.getWidth())
                        mainScreen.getCamera().zoom = mainScreen.getMainTable().getHeight() / (Gdx.graphics.getHeight() - 56) ;
                    else
                        mainScreen.getCamera().zoom = mainScreen.getMainTable().getWidth() / Gdx.graphics.getWidth() ;
                    mainScreen.getCamera().position.set(mainScreen.getMainTable().getX() + (mainScreen.getMainTable().getWidth())/2, mainScreen.getMainTable().getY() + mainScreen.getMainTable().getHeight()/2 , 0 );
                    mainScreen.getMainTable().move(18, 4);
                }
                zoomDisplay.setText(String.format("%.2f", (1 / mainScreen.getCamera().zoom) * 100));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        zoomDisplay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(zoomDisplay.isDisabled()){
                    zoomDisplay.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        zoomDisplay.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        String text = textField.getText();
                        text = text.replaceAll(",", ".");

                        mainScreen.getCamera().zoom = 1 / (Float.parseFloat(text) / 100 );

                        text = text.replaceAll("\\.", ",");
                        textField.setText(text);
                    } catch (Exception e) {
                        System.out.println("Only number entries are accepted");
                        zoomDisplay.setText(String.format("%.2f", (1 / mainScreen.getCamera().zoom) * 100));
                        e.printStackTrace();
                    }
                    zoomDisplay.setDisabled(true);
                }
            }
        });
    }

    public void updateZoomDisplay(){
        zoomDisplay.setText(String.format("%.2f", (1 / mainScreen.getCamera().zoom) * 100));
    }
}
