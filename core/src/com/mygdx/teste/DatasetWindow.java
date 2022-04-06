package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class DatasetWindow extends Window {
    final private TextButton createDatasetButton;
    final private TextButton importDatasetButton;
    final private TextButton saveDatasetButton;
    final private TextButton addToDatasetButton;
    final private int numButtons;

    final private Table insertNameTable;
    final private Label insertNameLabel;
    final private TextField insertNameField;

    final private Table buttonsTable;
    final private TextButton cancelButton;
    final private TextButton okButton;

    private Thread extractThread;

    public DatasetWindow(String title, WindowStyle style, final Teste application, float posX, float posY){
        super(title, style);

        this.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        numButtons = 4;
        this.setBounds(posX, posY, application.getResources().getNavigationButtonWidth(), numButtons * (application.getResources().getNavigationButtonHeight() + application.getResources().getNavigationButtonPad()));

        createDatasetButton = new TextButton("   Create dataset", application.getResources().getNavBarStyle());
        importDatasetButton = new TextButton("   Import dataset", application.getResources().getNavBarStyle());
        saveDatasetButton = new TextButton("   Save dataset to a file", application.getResources().getNavBarStyle());
        addToDatasetButton = new TextButton("   Add image to dataset", application.getResources().getNavBarStyle());

        this.left();
        createDatasetButton.getLabel().setAlignment(Align.left);
        importDatasetButton.getLabel().setAlignment(Align.left);
        saveDatasetButton.getLabel().setAlignment(Align.left);
        addToDatasetButton.getLabel().setAlignment(Align.left);

        this.add(createDatasetButton).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad()).row();
        this.add(importDatasetButton).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad()).row();
        this.add(saveDatasetButton).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad()).row();
        this.add(addToDatasetButton).width(application.getResources().getNavigationButtonWidth()).height(application.getResources().getNavigationButtonHeight()).padBottom(application.getResources().getNavigationButtonPad());

        // Insert Name Window
        insertNameTable = new Table();
        insertNameTable.setBackground(application.getResources().getSkin().getDrawable("BlackBackground"));
        insertNameTable.setBounds((float)Gdx.graphics.getWidth()/2 - 150,(float)Gdx.graphics.getHeight()/2,300,150);

        insertNameLabel = new Label("Insert dataset name", application.getResources().getTitleStyle());
        insertNameLabel.setAlignment(Align.center);

        insertNameField = new TextField("", application.getResources().getTextFieldStyle());
        insertNameField.setAlignment(Align.center);

        insertNameTable.align(Align.top);
        insertNameTable.add(insertNameLabel).width(196).row();
        insertNameTable.add(insertNameField).width(196).padTop(8);

        buttonsTable = new Table();
        buttonsTable.setBackground(application.getResources().getSkin().getDrawable("BlackBackground"));
        buttonsTable.setBounds((float)Gdx.graphics.getWidth()/2 - 150,(float)Gdx.graphics.getHeight()/2,300,64);
        cancelButton = new TextButton("Cancel", application.getResources().getCloseStyle());
        okButton = new TextButton("Ok", application.getResources().getCloseStyle());
        buttonsTable.align(Align.center);
        buttonsTable.add(okButton).width(96).height(32).padRight(16);
        buttonsTable.add(cancelButton).width(96).height(32).padLeft(16);

        okButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!insertNameField.getText().equals("")){
                    application.getDataset().createDataset(insertNameField.getText());
                    insertNameTable.remove();
                    buttonsTable.remove();
                }
                else{
                    System.err.println("Empty string");
                }
                super.clicked(event, x, y);
            }
        });

        cancelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                insertNameTable.remove();
                buttonsTable.remove();
                super.clicked(event, x, y);
            }
        });

        createDatasetButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getMainScreen().getMainStage().addActor(insertNameTable);
                application.getMainScreen().getMainStage().addActor(buttonsTable);
                super.clicked(event, x, y);
            }
        });

        importDatasetButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getResources().getJsfc().setFileFilter(application.getResources().getFilter());
                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                int returnValue = application.getResources().getJsfc().showOpenDialog(null);
                if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
                    File selectedFile = application.getResources().getJsfc().getSelectedFile();
                    try {
                        application.getDataset().loadDataset(selectedFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                application.getMainScreen().getNavigationBar().getDatasetWindow().remove();
                application.getMainScreen().getNavigationBar().getDatasetButton().setChecked(false);
                super.clicked(event, x, y);
            }
        });

        saveDatasetButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(application.getDataset().getInstances() != null) {
                    application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                    application.getResources().getJsfc().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnValue = application.getResources().getJsfc().showSaveDialog(null);
                    File directory = application.getResources().getJsfc().getSelectedFile();
                    if (returnValue == JSystemFileChooser.APPROVE_OPTION) {
                        try {
                            application.getDataset().saveDataset(directory);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    application.getMainScreen().getNavigationBar().getDatasetWindow().remove();
                    application.getMainScreen().getNavigationBar().getDatasetButton().setChecked(false);
                }
                else{
                    System.err.println("Dataset not created");
                }
                super.clicked(event, x, y);
            }
        });

        addToDatasetButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(application.getDataset().getInstances() != null){
                    if(extractThread == null || !extractThread.isAlive()){
                        extractThread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    application.getFilters().setGroundTruth(application.getMainScreen().getPixmap());
                                    application.getFilters().createHessianFeatures();
                                    application.getFilters().createFrangiFeatures();
                                    application.getFilters().createLaplacianFeatures();
                                    application.getFilters().createSharpenFeatures();

                                    for(int i = 0; i < application.getMainScreen().getPixmap().getWidth(); i++){
                                        for(int j = 0; j < application.getMainScreen().getPixmap().getHeight(); j++){
                                            double[] values = application.getFilters().extractFeatures(i, j);
                                            application.getDataset().addNewInstance(values);
                                        }
                                    }
                                    System.out.println("Features extracted, new instances added");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                super.run();
                            }
                        };
                        extractThread.start();
                    }
                    else{
                        System.err.println("Thread already running");
                    }
                }
                else{
                    System.err.println("Dataset not created");
                }
                super.clicked(event, x, y);
            }
        });
    }
}
