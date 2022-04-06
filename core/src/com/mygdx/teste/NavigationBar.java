package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NavigationBar extends Table {
    private TextButton fileButton;
    private TextButton editButton;
    private TextButton imageButton;
    private TextButton datasetButton;
    private TextButton classifyButton;
    private TextButton segmentationButton;
    private TextButton aboutButton;
    private TextButton helpButton;

    private FileWindow fileWindow;
    private DatasetWindow datasetWindow;
    private ClassifyWindow classifyWindow;
    private SegmentationWindow segmentationWindow;
    private ChangeListener closeSegmentationWindowListener;

    // Track the position of the windows to keep them on the right place
    private Vector2 fileButtonCoordinates;
    private Vector2 datasetButtonCoordinates;

    public NavigationBar(final MainScreen mainScreen, final Teste application){
        fileButton = new TextButton("File", application.getResources().getNavBarStyle());
        editButton = new TextButton("Edit", application.getResources().getNavBarStyle());
        imageButton = new TextButton("Image", application.getResources().getNavBarStyle());
        datasetButton = new TextButton("Dataset", application.getResources().getNavBarStyle());
        classifyButton = new TextButton("Classify", application.getResources().getNavBarStyle());
        segmentationButton = new TextButton("Segmentation", application.getResources().getNavBarStyle());
        aboutButton = new TextButton("About", application.getResources().getNavBarStyle());
        helpButton = new TextButton("Help", application.getResources().getNavBarStyle());

        this.setBounds(0, Gdx.graphics.getHeight() - application.getResources().getNavigationButtonHeight(), Gdx.graphics.getWidth(), application.getResources().getNavigationButtonHeight());
        this.left();
        this.background(application.getResources().getSkin().getDrawable("whiteBackground"));
        this.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        this.add(fileButton).width(fileButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());
        //this.add(editButton).width(editButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());
        //this.add(imageButton).width(imageButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());
        this.add(datasetButton).width(datasetButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());
        this.add(classifyButton).width(classifyButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());
        this.add(segmentationButton).width(segmentationButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());
        //this.add(aboutButton).width(aboutButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());
        //this.add(helpButton).width(helpButton.getLabel().getWidth() + 24).height(application.getResources().getNavigationButtonHeight());

        fileButtonCoordinates = new Vector2(localToStageCoordinates(new Vector2(fileButton.getX(), fileButton.getY())));
        fileWindow = new FileWindow("", application.getResources().getNavBarWindowStyle(), application, fileButtonCoordinates.x, fileButtonCoordinates.y - fileButton.getHeight());

        datasetButtonCoordinates = new Vector2(localToStageCoordinates(new Vector2(datasetButton.getX(), datasetButton.getY())));
        datasetWindow = new DatasetWindow("", application.getResources().getNavBarWindowStyle(), application, datasetButtonCoordinates.x, datasetButtonCoordinates.y - datasetButton.getHeight());

        classifyWindow = new ClassifyWindow(application);

        closeSegmentationWindowListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                segmentationWindow.remove();
            }
        };
        segmentationWindow = new SegmentationWindow("", application.getResources().getWindowStyle(), application, closeSegmentationWindowListener);
        segmentationWindow.setSegmentationListener(application);

        fileButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(fileButton.isChecked()){
                    fileButtonCoordinates.set(localToStageCoordinates(new Vector2(fileButton.getX(), fileButton.getY())));
                    fileWindow.setPosition(fileButtonCoordinates.x, fileButtonCoordinates.y - fileWindow.getHeight());
                    mainScreen.getMainStage().addActor(fileWindow);
                }
                else{
                    fileWindow.remove();
                }
                super.clicked(event, x, y);
            }
        });

        datasetButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(datasetButton.isChecked()){
                    datasetButtonCoordinates.set(localToStageCoordinates(new Vector2(datasetButton.getX(), datasetButton.getY())));
                    datasetWindow.setPosition(datasetButtonCoordinates.x, datasetButtonCoordinates.y - datasetWindow.getHeight());
                    mainScreen.getMainStage().addActor(datasetWindow);
                }
                else{
                    datasetWindow.remove();
                }
                super.clicked(event, x, y);
            }
        });

        classifyButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.getMainStage().addActor(classifyWindow.getClassifierTable());
                mainScreen.getMainStage().addActor(classifyWindow.getActionButtonTable());
                super.clicked(event, x, y);
            }
        });

        segmentationButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.getMainStage().addActor(segmentationWindow);
                super.clicked(event, x, y);
            }
        });
    }

    public void resize(){
        this.setWidth(Gdx.graphics.getWidth());
        this.setPosition(0,Gdx.graphics.getHeight() - this.getHeight());
        segmentationWindow.setPosition( Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/2 - 256);
    }

    public SegmentationWindow getSegmentationWindow() {
        return segmentationWindow;
    }

    public TextButton getSegmentationButton() {
        return segmentationButton;
    }

    public TextButton getFileButton() {
        return fileButton;
    }

    public FileWindow getFileWindow() {
        return fileWindow;
    }

    public TextButton getDatasetButton() {
        return datasetButton;
    }

    public DatasetWindow getDatasetWindow() {
        return datasetWindow;
    }
}
