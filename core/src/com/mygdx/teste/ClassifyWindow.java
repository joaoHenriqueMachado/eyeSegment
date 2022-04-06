package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ClassifyWindow {

    private Label classifierLabel;
    private TextButton optionsButton;
    private SelectBox<String> selectBox;
    private Table classifierTable;
    private String[] values;

    private TextButton cancelButton;
    private TextButton acceptButton;
    private Table actionButtonTable;

    private TextButton loadModelButton;
    private TextButton saveModelButton;
    private TextButton classifyImageButton;
    private TextButton loadClassificationOutput;

    public ClassifyWindow(final Teste application){
        values = application.getClassifier().getNames();
        classifierLabel = new Label("Classifier:", application.getResources().getTextStyle());
        classifierLabel.setAlignment(Align.left);
        optionsButton = new TextButton("Classifier Options", application.getResources().getButtonStyle());
        loadModelButton = new TextButton("Load model", application.getResources().getButtonStyle());
        saveModelButton = new TextButton("Save model", application.getResources().getButtonStyle());
        classifyImageButton = new TextButton("Classify", application.getResources().getButtonStyle());
        loadClassificationOutput = new TextButton("Load output", application.getResources().getButtonStyle());
        selectBox = new SelectBox<>(application.getResources().getSelectBoxStyle());
        selectBox.setItems(values);
        acceptButton = new TextButton("Build model", application.getResources().getButtonStyle());
        classifierTable = new Table();

        classifierTable.setBackground(application.getResources().getSkin().getDrawable("BlackBackground"));
        classifierTable.setBounds((float) Gdx.graphics.getWidth()/2 - 200, (float)Gdx.graphics.getHeight()/2 - 150, 400, 300);
        classifierTable.top().left();
        classifierTable.add(classifierLabel).left().pad(0, 16,0 ,16);
        classifierTable.add(selectBox).height(32).width(156).row();
        classifierTable.add(optionsButton).width(156).height(36).pad(16, 16,0 ,16);
        classifierTable.add(acceptButton).width(156).height(36).pad(16, 16,0 ,16).row();
        classifierTable.add(loadModelButton).width(156).height(36).pad(16, 16,0 ,16);
        classifierTable.add(saveModelButton).width(156).height(36).pad(16, 16,0 ,16).row();
        classifierTable.add(classifyImageButton).width(156).height(36).pad(16, 16,0 ,16);
        classifierTable.add(loadClassificationOutput).width(156).height(36).pad(16, 16,0 ,16);

        cancelButton = new TextButton("Close", application.getResources().getCloseStyle());
        actionButtonTable = new Table();
        //actionButtonTable.setBackground(application.getResources().getSkin().getDrawable("BlackBackground"));
        actionButtonTable.setBounds((float) Gdx.graphics.getWidth()/2 - 200, (float)Gdx.graphics.getHeight()/2 - 150, 400, 96);
        actionButtonTable.center();
        actionButtonTable.add(cancelButton);

        loadModelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getClassifier().loadModel(selectBox.getSelectedIndex());
                super.clicked(event, x, y);
            }
        });

        saveModelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getClassifier().saveModel(selectBox.getSelectedIndex());
                super.clicked(event, x, y);
            }
        });

        cancelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                classifierTable.remove();
                actionButtonTable.remove();
                super.clicked(event, x, y);
            }
        });

        loadClassificationOutput.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!application.getClassifier().getClassifyThread().isAlive() && application.getClassifier().getOutput() != null){
                    Pixmap undoState = new Pixmap(application.getMainScreen().getPixmap().getWidth(), application.getMainScreen().getPixmap().getHeight(), application.getMainScreen().getPixmap().getFormat());
                    undoState.drawPixmap(application.getMainScreen().getPixmap(), 0, 0);
                    application.getMainScreen().getUndoStack().addElement(undoState);
                    application.getMainScreen().getPixmap().drawPixmap(application.getClassifier().getOutput(), 0, 0);
                    application.getMainScreen().getMainTable().updatePixmap();
                }else{
                    System.out.println("Thread running or empty output");
                }
                super.clicked(event, x, y);
            }
        });

        classifyImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getClassifier().classifyImage(selectBox.getSelectedIndex());

                /*
                if(classifyThread == null || !classifyThread.isAlive()){
                    classifyThread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                Classifier classifier = classifiers[selectBox.getSelectedIndex()];
                                if(application.getFilters().isFiltered()){
                                    application.getFilters().cleanImages();
                                }
                                application.getFilters().setGroundTruth(application.getMainScreen().getPixmap());
                                application.getFilters().createHessianFeatures();

                                // Extracting features and creating instances
                                Instances instances = application.getDataset().createClassificationDataset("classify");
                                for(int i = 0; i < application.getMainScreen().getPixmap().getWidth(); i++){
                                    for(int j = 0; j < application.getMainScreen().getPixmap().getHeight(); j++){
                                        double[] values = application.getFilters().extractFeaturesWithoutClass(i, j);
                                        application.getDataset().addNewInstance(values, instances);
                                    }
                                }

                                // Running classification and creating output image
                                Pixmap output = new Pixmap(application.getMainScreen().getPixmap().getWidth(),application.getMainScreen().getPixmap().getHeight(), application.getMainScreen().getPixmap().getFormat());
                                output.setColor(Color.WHITE);
                                for(int i = 0; i < application.getMainScreen().getPixmap().getWidth(); i++){
                                    for(int j = 0; j < application.getMainScreen().getPixmap().getHeight(); j++){
                                        double pred = classifier.classifyInstance(instances.get(i * application.getMainScreen().getPixmap().getHeight() + j));
                                        if(Math.round(pred) == 1){
                                            output.drawPixel(i, j);
                                        }
                                    }
                                }
                                // Exporting output image to a file
                                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                                int returnValue = application.getResources().getJsfc().showSaveDialog(null);
                                File f = application.getResources().getJsfc().getSelectedFile();
                                if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
                                    try{
                                        FileHandle fh;
                                        fh = new FileHandle(f.getAbsolutePath() + ".png");
                                        Pixmap blackWhite = new Pixmap(output.getWidth(), output.getHeight(), Pixmap.Format.RGB888);
                                        blackWhite.drawPixmap(output, 0, 0);
                                        PixmapIO.writePNG(fh, blackWhite);
                                        blackWhite.dispose();
                                        output.dispose();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            super.run();
                        }
                    };
                    classifyThread.start();
                }
                else{
                    System.err.println("Thread already running");
                }*/
                super.clicked(event, x, y);
            }
        });

        acceptButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getClassifier().buildClassifier(selectBox.getSelectedIndex());
                /*
                if(application.getDataset().getInstances() != null && !application.getDataset().getInstances().isEmpty()){
                    // Cloning original dataset
                    testDataset = new Instances(application.getDataset().getInstances());

                    // Filtering data

                    if(buildThread == null || !buildThread.isAlive()){
                        buildThread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    classifiers[selectBox.getSelectedIndex()].buildClassifier(testDataset);
                                    //Evaluation evaluation = new Evaluation(testDataset);
                                    //evaluation.crossValidateModel(classifiers[selectBox.getSelectedIndex()], testDataset, 10, new Random(1));
                                    System.out.println("Classifier built");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                super.run();
                            }
                        };
                        buildThread.start();
                    }
                    else{
                        System.err.println("Thread already running");
                    }
                }
                else{
                    System.err.println("Empty or not found dataset");
                }*/
                super.clicked(event, x, y);
            }
        });
    }

    public Table getClassifierTable() {
        return classifierTable;
    }

    public Table getActionButtonTable() {
        return actionButtonTable;
    }

}
