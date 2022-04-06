package com.mygdx.teste;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import image.Image;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import java.io.File;

public class Classifiers {
    private Classifier[] classifiers;
    private RandomForest randomForest;
    private REPTree repTree;
    private J48 j48;

    private Thread classifyThread;
    private Thread buildThread;
    private Teste application;
    private String[] names;
    private Pixmap output;
    private Instances instances;

    public Classifiers (Teste application){
        this.application = application;
        randomForest = new RandomForest();
        repTree = new REPTree();
        j48 = new J48();

        classifiers = new Classifier[]{randomForest, repTree, j48};
        /*
        names = new String[classifiers.length];
        for(int i = 0; i < classifiers.length; i++){
            names[i] = classifiers[i].toString();
        }*/
        names = new String[]{" Random Forest ", " REPTree ", " J48 Graft "};
        instances = application.getDataset().createClassificationDataset("Classification");
    }

    public Classifier[] getClassifiers() {
        return classifiers;
    }

    public void classifyImage (final int classifierIndex){
        if(classifyThread == null || !classifyThread.isAlive()){
            classifyThread = new Thread(){
                @Override
                public void run() {
                    try {
                        Classifier classifier = classifiers[classifierIndex];

                        //application.getFilters().cleanImages();
                        application.getFilters().createHessianFeatures();
                        application.getFilters().createFrangiFeatures();
                        application.getFilters().createLaplacianFeatures();
                        application.getFilters().createSharpenFeatures();

                        // Extracting features and creating instances
                        instances.delete();
                        for(int i = 0; i < application.getMainScreen().getPixmap().getWidth(); i++){
                            for(int j = 0; j < application.getMainScreen().getPixmap().getHeight(); j++){
                                double[] values = application.getFilters().extractFeaturesWithoutClass(i, j);
                                application.getDataset().addNewInstance(values, instances);
                            }
                        }

                        // Running classification and creating output image
                        if(output != null){
                            output.dispose();
                        }

                        Image mask = null;
                        if(!application.getMainScreen().getMaskPath().equals("")){
                            mask = new Image(application.getMainScreen().getMaskPath());
                        }

                        output = new Pixmap(application.getMainScreen().getPixmap().getWidth(),application.getMainScreen().getPixmap().getHeight(), application.getMainScreen().getPixmap().getFormat());
                        for(int i = 0; i < application.getMainScreen().getPixmap().getWidth(); i++){
                            for(int j = 0; j < application.getMainScreen().getPixmap().getHeight(); j++){
                                double pred = classifier.classifyInstance(instances.get(i * output.getHeight() + j));
                                if(Math.round(pred) == 1 && mask != null){
                                    output.setColor(Color.WHITE);
                                    output.drawPixel(i, j);
                                }
                            }
                        }
                        System.out.println("Done");
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
        }
    }

    public void buildClassifier (final int classifierIndex){
        if(application.getDataset().getInstances() != null && !application.getDataset().getInstances().isEmpty()){
            // Cloning original dataset
            final Instances testDataset = new Instances(application.getDataset().getInstances());

            // Filtering data

            if(buildThread == null || !buildThread.isAlive()){
                buildThread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            classifiers[classifierIndex].buildClassifier(testDataset);
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
        }
    }

    public void loadModel(final int classifierIndex){
        application.getResources().getJsfc().setFileFilter(application.getResources().getModel_filter());
        application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
        int returnValue = application.getResources().getJsfc().showOpenDialog(null);
        if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
            File selectedFile = application.getResources().getJsfc().getSelectedFile();
            try {
                classifiers[classifierIndex]  = (Classifier) weka.core.SerializationHelper.read(selectedFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveModel(final int classifierIndex){
        application.getResources().getJsfc().setFileFilter(application.getResources().getModel_filter());
        application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
        int returnValue = application.getResources().getJsfc().showSaveDialog(null);
        if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
            File selectedFile = application.getResources().getJsfc().getSelectedFile();
            try {
                weka.core.SerializationHelper.write(selectedFile.getAbsolutePath(), classifiers[classifierIndex]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getNames (){
        return names;
    }

    public Pixmap getOutput() {
        return output;
    }

    public Thread getClassifyThread() {
        return classifyThread;
    }
}
