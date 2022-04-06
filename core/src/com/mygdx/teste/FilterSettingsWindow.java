package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class FilterSettingsWindow extends Window {
    private SegmentationOptions segmentationOptions;
    private Label minSigmaLabel;
    private Label maxSigmaLabel;
    private Label betaXLabel;
    private Label betaYLabel;
    private Label frangiThresholdLabel;
    private Label connThresholdLabel;
    private Label smartConnThresholdLabel;
    private Label maskErosionLabel;
    private TextButton closeButton;

    public FilterSettingsWindow (String title, WindowStyle style, final Teste application){
        super(title, style);

        minSigmaLabel = new Label("Min Sigma", application.getResources().getTextStyle());
        maxSigmaLabel = new Label("Max Sigma", application.getResources().getTextStyle());
        betaXLabel = new Label("Beta X", application.getResources().getTextStyle());
        betaYLabel = new Label("Beta Y", application.getResources().getTextStyle());
        frangiThresholdLabel = new Label("Frangi Threshold", application.getResources().getTextStyle());
        connThresholdLabel = new Label("Conn Threshold", application.getResources().getTextStyle());
        smartConnThresholdLabel = new Label("SmartConn Threshold", application.getResources().getTextStyle());
        maskErosionLabel = new Label("Mask erosion", application.getResources().getTextStyle());

        segmentationOptions = new SegmentationOptions();
        final TextField minSigmaField = new TextField(Float.toString(segmentationOptions.getMinSigma()), application.getResources().getTextFieldStyle());
        final TextField maxSigmaField = new TextField(Float.toString(segmentationOptions.getMaxSigma()), application.getResources().getTextFieldStyle());
        final TextField betaXField = new TextField(Float.toString(segmentationOptions.getBetaX()), application.getResources().getTextFieldStyle());
        final TextField betaYField = new TextField(Float.toString(segmentationOptions.getBetaY()), application.getResources().getTextFieldStyle());
        final TextField frangiThresholdField = new TextField(Integer.toString(segmentationOptions.getFrangiThreshold()), application.getResources().getTextFieldStyle());
        final TextField connThresholdField = new TextField(Integer.toString(segmentationOptions.getConnThreshold()), application.getResources().getTextFieldStyle());
        final TextField smartConnThresholdField = new TextField(Integer.toString(segmentationOptions.getSmartConnThreshold()), application.getResources().getTextFieldStyle());
        final TextField maskErosionField = new TextField(Integer.toString(segmentationOptions.getMaskErosion()), application.getResources().getTextFieldStyle());

        minSigmaField.setAlignment(Align.center);
        maxSigmaField.setAlignment(Align.center);
        betaXField.setAlignment(Align.center);
        betaYField.setAlignment(Align.center);
        frangiThresholdField.setAlignment(Align.center);
        connThresholdField.setAlignment(Align.center);
        smartConnThresholdField.setAlignment(Align.center);
        maskErosionField.setAlignment(Align.center);

        minSigmaField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(minSigmaField.isDisabled()){
                    minSigmaField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        maxSigmaField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(maxSigmaField.isDisabled()){
                    maxSigmaField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        betaXField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(betaXField.isDisabled()){
                    betaXField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        betaYField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(betaYField.isDisabled()){
                    betaYField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        frangiThresholdField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(frangiThresholdField.isDisabled()){
                    frangiThresholdField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        connThresholdField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(connThresholdField.isDisabled()){
                    connThresholdField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        smartConnThresholdField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(smartConnThresholdField.isDisabled()){
                    smartConnThresholdField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });
        maskErosionField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(maskErosionField.isDisabled()){
                    maskErosionField.setDisabled(false);
                }
                super.clicked(event, x, y);
            }
        });

        minSigmaField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        String text = textField.getText();
                        text = text.replaceAll(",", ".");

                        segmentationOptions.setMinSigma(Float.parseFloat(text));

                        text = text.replaceAll("\\.", ",");
                        textField.setText(text);
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Float.toString(segmentationOptions.getMinSigma()));
                        e.printStackTrace();
                    }
                    minSigmaField.setDisabled(true);
                }
            }
        });

        maxSigmaField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        String text = textField.getText();
                        text = text.replaceAll(",", ".");

                        segmentationOptions.setMaxSigma(Float.parseFloat(text));

                        text = text.replaceAll("\\.", ",");
                        textField.setText(text);
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Float.toString(segmentationOptions.getMaxSigma()));
                        e.printStackTrace();
                    }
                    maxSigmaField.setDisabled(true);
                }
            }
        });

        betaXField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        String text = textField.getText();
                        text = text.replaceAll(",", ".");

                        segmentationOptions.setBetaX(Float.parseFloat(text));

                        text = text.replaceAll("\\.", ",");
                        textField.setText(text);
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Float.toString(segmentationOptions.getBetaX()));
                        e.printStackTrace();
                    }
                    betaXField.setDisabled(true);
                }
            }
        });

        betaYField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        String text = textField.getText();
                        text = text.replaceAll(",", ".");

                        segmentationOptions.setBetaY(Float.parseFloat(text));

                        text = text.replaceAll("\\.", ",");
                        textField.setText(text);
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Float.toString(segmentationOptions.getBetaY()));
                        e.printStackTrace();
                    }
                    betaYField.setDisabled(true);
                }
            }
        });

        frangiThresholdField.setTextFieldListener (new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        segmentationOptions.setFrangiThreshold(Integer.parseInt(textField.getText()));
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Integer.toString(segmentationOptions.getFrangiThreshold()));
                        e.printStackTrace();
                    }
                    frangiThresholdField.setDisabled(true);
                }
            }
        });

        connThresholdField.setTextFieldListener (new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        segmentationOptions.setConnThreshold(Integer.parseInt(textField.getText()));
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Integer.toString(segmentationOptions.getConnThreshold()));
                        e.printStackTrace();
                    }
                    connThresholdField.setDisabled(true);
                }
            }
        });

        smartConnThresholdField.setTextFieldListener (new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        segmentationOptions.setSmartConnThreshold(Integer.parseInt(textField.getText()));
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Integer.toString(segmentationOptions.getSmartConnThreshold()));
                        e.printStackTrace();
                    }
                    smartConnThresholdField.setDisabled(true);
                }
            }
        });

        maskErosionField.setTextFieldListener (new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    try{
                        segmentationOptions.setMaskErosion(Integer.parseInt(textField.getText()));
                    } catch (Exception e) {
                        System.out.println("Please enter a number");
                        textField.setText(Integer.toString(segmentationOptions.getMaskErosion()));
                        e.printStackTrace();
                    }
                    maskErosionField.setDisabled(true);
                }
            }
        });

        closeButton = new TextButton("Close", application.getResources().getCloseStyle());
        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getMainScreen().getFilterSettingsWindow().remove();
                super.clicked(event, x, y);
            }
        });
        this.setBounds( Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/2 - 300, 400, 600);
        this.left();
        this.add(minSigmaLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(minSigmaField).left().padBottom(16).row();
        this.add(maxSigmaLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(maxSigmaField).left().padBottom(16).row();
        this.add(betaXLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(betaXField).left().padBottom(16).row();
        this.add(betaYLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(betaYField).left().padBottom(16).row();
        this.add(frangiThresholdLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(frangiThresholdField).left().padBottom(16).row();
        this.add(connThresholdLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(connThresholdField).left().padBottom(16).row();
        this.add(smartConnThresholdLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(smartConnThresholdField).left().padBottom(16).row();
        this.add(maskErosionLabel).align(Align.left).padLeft(24).padRight(24).padBottom(16).left();
        this.add(maskErosionField).left().padBottom(16).row();
        this.add(closeButton).width(150).padBottom(16).colspan(2);
    }

}
