package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class SegmentationWindow extends Window {
    private ImageButton[] checkbox = new ImageButton[3];
    private ImageButton filterSettings;
    private TextButton startSegmentation;
    private Label titleSW;
    private Label filtersLabel;
    private Label frangiLabel;
    private Label connectivityLabel;
    private Label smartConnectivityLabel;
    private Label filterSettingsLabel;
    private TextButton closeButton;

    public SegmentationWindow(String title, WindowStyle style, final Teste application, ChangeListener closeWindow) {
        super(title, style);

        for(int i = 0; i < 3; i++){
            checkbox[i] = new ImageButton(application.getResources().getSkin().getDrawable("checkboxOff"));
            checkbox[i].setStyle(application.getResources().getCheckboxStyle());
        }
        filterSettings = new ImageButton(application.getResources().getSkin().getDrawable("config"));
        titleSW = new Label("Segmentation Options", application.getResources().getTitleStyle());
        filtersLabel = new Label("Filters", application.getResources().getSubtitleStyle());
        frangiLabel = new Label("Frangi", application.getResources().getTextStyle());
        connectivityLabel = new Label("Connectivity", application.getResources().getTextStyle());
        smartConnectivityLabel = new Label("Smart Connectivity", application.getResources().getTextStyle());
        filterSettingsLabel = new Label("Filter Settings", application.getResources().getTextStyle());
        startSegmentation = new TextButton("    Start segmentation", application.getResources().getBlueButtonStyle());
        closeButton = new TextButton("Close", application.getResources().getCloseStyle());
        //closeButton.setStyle(application.getResources().getCloseStyle());

        this.setBounds((float) Gdx.graphics.getWidth()/2 - 200, (float)Gdx.graphics.getHeight()/2 - 256, 400, 512);
        this.add(titleSW).width(128).padBottom(32).padRight(-38).row();
        this.add(filtersLabel).width(128).padBottom(8).padRight(-40).row();
        this.add(checkbox[0]).padRight(56).padBottom(8);
        this.add(frangiLabel).align(Align.left).padBottom(8).row();
        this.add(checkbox[1]).padRight(56).padBottom(8);
        this.add(connectivityLabel).align(Align.left).padBottom(8).row();
        this.add(checkbox[2]).padRight(56).padBottom(8);
        this.add(smartConnectivityLabel).align(Align.left).padBottom(8).row();
        this.add(filterSettings).padRight(56).padBottom(8);
        this.add(filterSettingsLabel).align(Align.left).row();
        this.add(startSegmentation).width(256).colspan(2).padTop(36).row();
        this.add(closeButton).width(256).padTop(16).colspan(2).row();

        checkbox[1].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(checkbox[1].isChecked()){
                    checkbox[0].setChecked(true);
                }
                super.clicked(event, x, y);
            }
        });
        checkbox[2].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(checkbox[2].isChecked()){
                    checkbox[0].setChecked(true);
                }
                super.clicked(event, x, y);
            }
        });
        closeButton.addListener(closeWindow);
        filterSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getMainScreen().getMainStage().addActor(application.getMainScreen().getFilterSettingsWindow());
                super.clicked(event, x, y);
            }
        });
    }
    public void setSegmentationListener(final Teste application){
        startSegmentation.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!application.getMainScreen().getTexture().getTextureData().isPrepared()) {
                    application.getMainScreen().getTexture().getTextureData().prepare();
                }
                application.getMainScreen().getNavigationBar().getSegmentationButton().setChecked(false);
                application.getMainScreen().getNavigationBar().getSegmentationWindow().remove();
                if(checkbox[0].isChecked()){
                    if(application.getSegmentationScreen() != null){
                        application.getSegmentationScreen().dispose();
                        application.setSegmentationScreen(null);
                    }
                    application.setSegmentationScreen(new SegmentationScreen(application, application.getMainScreen().getTexture().getTextureData().consumePixmap(), application.getMainScreen().createBlackWhitePixmap(application.getMainScreen().getPixmap()), application.getMainScreen().segmentationOptions, checkbox[1].isChecked(), checkbox[2].isChecked()));
                    application.setScreen(application.getSegmentationScreen());
                    application.getMainScreen().getInputMultiplexer().removeProcessor(application.getMainScreen().getMainStage());
                    application.getMainScreen().getInputMultiplexer().removeProcessor(application.getMainScreen().getDrawingProcessor());
                    checkbox[0].setChecked(false);
                    checkbox[1].setChecked(false);
                    checkbox[2].setChecked(false);
                }
                else{
                    System.out.println("Please select a option at the Segmentation Menu");
                }
                super.clicked(event, x, y);
            }
        });
    }
}
