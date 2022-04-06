package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import image.Image;

public class ComparisonWindow extends Window {
    TextButton closeWindow;
    CompareImages compareImages;

    Label TNLabel;
    TextField TNField;
    Label TPLabel;
    TextField TPField;
    Label FNLabel;
    TextField FNField;
    Label FPLabel;
    TextField FPField;
    Label TPRLabel;
    TextField TPRField;
    Label FPRLabel;
    TextField FPRField;
    Label ACCLabel;
    TextField ACCField;

    public ComparisonWindow(String title, WindowStyle style) {
        super(title, style);
    }

    public void createWindow(ChangeListener closeWindowListener, SegmentationScreen segScreen, Image img, Image gt, Image mask){
        closeWindow = new TextButton("Close", segScreen.application.getResources().getNavBarStyle());
        closeWindow.addListener(closeWindowListener);

        compareImages = new CompareImages();
        if(mask == null)
            compareImages.compareFullImage(gt, img);
        else
            compareImages.compare(gt, img, mask);

        TNLabel = new Label("True Negatives", getTitleLabel().getStyle());
        TNField = new TextField(Integer.toString(compareImages.getTrueNeg()), segScreen.application.getResources().getTextFieldStyle());
        TPLabel = new Label("True Positives", getTitleLabel().getStyle());
        TPField = new TextField(Integer.toString(compareImages.getTruePos()), segScreen.application.getResources().getTextFieldStyle());
        FNLabel = new Label("False Negatives", getTitleLabel().getStyle());
        FNField = new TextField(Integer.toString(compareImages.getFalseNeg()), segScreen.application.getResources().getTextFieldStyle());
        FPLabel = new Label("False Positives", getTitleLabel().getStyle());
        FPField = new TextField(Integer.toString(compareImages.getFalsePos()), segScreen.application.getResources().getTextFieldStyle());
        TPRLabel = new Label("True Positive Rate", getTitleLabel().getStyle());
        TPRField = new TextField(Float.toString(compareImages.getTpr()), segScreen.application.getResources().getTextFieldStyle());
        FPRLabel = new Label("False Positive Rate", getTitleLabel().getStyle());
        FPRField = new TextField(Float.toString(compareImages.getFpr()), segScreen.application.getResources().getTextFieldStyle());
        ACCLabel = new Label("Accuracy", getTitleLabel().getStyle());
        ACCField = new TextField(Float.toString(compareImages.getAcc()), segScreen.application.getResources().getTextFieldStyle());

        TNField.setAlignment(Align.center);
        TPField.setAlignment(Align.center);
        FNField.setAlignment(Align.center);
        FPField.setAlignment(Align.center);
        TPRField.setAlignment(Align.center);
        FPRField.setAlignment(Align.center);
        ACCField.setAlignment(Align.center);

        this.setBounds((float) Gdx.graphics.getWidth()/2 - 200, (float)Gdx.graphics.getHeight()/2 - 300, 400, 600);
        this.left();
        this.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        this.add(TPLabel).align(Align.left).padLeft(48).padRight(24).padBottom(16).left();
        this.add(TPField).left().padBottom(16).row();
        this.add(TNLabel).align(Align.left).padLeft(48).padRight(24).padBottom(16).left();
        this.add(TNField).left().padBottom(16).row();
        this.add(FPLabel).align(Align.left).padLeft(48).padRight(24).padBottom(16).left();
        this.add(FPField).left().padBottom(16).row();
        this.add(FNLabel).align(Align.left).padLeft(48).padRight(24).padBottom(16).left();
        this.add(FNField).left().padBottom(16).row();
        this.add(TPRLabel).align(Align.left).padLeft(48).padRight(24).padBottom(16).left();
        this.add(TPRField).left().padBottom(16).row();
        this.add(FPRLabel).align(Align.left).padLeft(48).padRight(24).padBottom(16).left();
        this.add(FPRField).left().padBottom(16).row();
        this.add(ACCLabel).align(Align.left).padLeft(48).padRight(24).padBottom(16).left();
        this.add(ACCField).left().padBottom(16).row();
        this.add(closeWindow).width(128).padBottom(16);
    }
}
