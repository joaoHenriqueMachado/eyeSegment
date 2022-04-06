package com.mygdx.teste;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;

public class Dataset {
    private Instances instances;
    private ArrayList<Attribute> attributesInfo;
    private Attribute[] attributes;

    public Dataset (){
        attributesInfo = null;
        attributes = null;
        instances = null;
    }

    public void createDataset (String name){
        attributesInfo = new ArrayList<>(18);
        attributes = new Attribute[18];

        // create repetition structure
        for(int i = 0; i < 17; i++){
            attributes[i] = new Attribute("feature" + i);
            attributesInfo.add(attributes[i]);
        }
        attributes[17] = new Attribute("ground-truth");
        attributesInfo.add(attributes[17]);

        instances = new Instances(name, attributesInfo, 0);
        instances.setClassIndex(instances.numAttributes() - 1);
    }

    public Instances createClassificationDataset (String name){
        attributesInfo = new ArrayList<>(18);
        attributes = new Attribute[18];

        // create repetition structure
        for(int i = 0; i < 17; i++){
            attributes[i] = new Attribute("feature" + i);
            attributesInfo.add(attributes[i]);
        }
        attributes[17] = new Attribute("ground-truth");
        attributesInfo.add(attributes[17]);

        Instances instances = new Instances(name, attributesInfo, 0);
        instances.setClassIndex(instances.numAttributes() - 1);

        return instances;
    }
/*
    public void addNewInstance (double origPixel, double hessianPixel, double sharpPixel, double frangiPixel, double laplacianPixel, double gtPix){
        double[] instanceValue = new double[instances.numAttributes()];
        instanceValue[0] = origPixel;
        instanceValue[1] = hessianPixel;
        instanceValue[2] = sharpPixel;
        instanceValue[3] = frangiPixel;
        instanceValue[4] = laplacianPixel;
        instanceValue[5] = gtPix;
        instances.add(new DenseInstance(1.0, instanceValue));
    }
*/
    public void addNewInstance (double[] values){
        instances.add(new DenseInstance(1.0, values));
    }
    public void addNewInstance (double[] values, Instances instances){
        instances.add(new DenseInstance(1.0, values));
    }

    public void printDataset (){
        System.out.println("\nDataset:\n");
        System.out.println(instances);
    }

    public void saveDataset (File directory) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(directory.getAbsolutePath() + "//" + instances.relationName() + ".arff"));
        writer.write(instances.toString());
        writer.close();
    }

    public void loadDataset (File directory) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(directory));
        instances = new Instances(reader);
        instances.setClassIndex(instances.numAttributes() - 1);
    }

    public Instances getInstances() {
        return instances;
    }
}
