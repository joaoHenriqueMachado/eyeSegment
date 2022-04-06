package com.mygdx.teste;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import filters.border.HessianFilter;
import filters.border.LaplacianFilter;
import filters.sharpen.SimpleSharpen;
import filters.vessel.FrangiFilter;
import image.Image;

public class Filters {
    private Image sourceImg;
    private Pixmap groundTruth;

    private HessianFilter[] hessianFilters;
    private Image[] hessianResults;

    private FrangiFilter frangi1, frangi2, frangi3;
    private Image frangiImg1, frangiImg2, frangiImg3;

    private LaplacianFilter lap1, lap2;
    private Image lap1Image, lap2Image;

    private SimpleSharpen simpleSharpen;
    private Image sharpImg;

    public Filters ()  {
        sourceImg = null;
    }

    public void loadSourceImg (String path) throws Exception {
        if(sourceImg != null){
            try {
                sourceImg.dispose();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        sourceImg = new Image(path);
        try{
            sourceImg.convertToGray(1, 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGroundTruth(Pixmap groundTruth) {
        this.groundTruth = groundTruth;
    }

    public void createHessianFeatures() {
        if(sourceImg != null){
            hessianFilters = new HessianFilter[11];
            for(int i = 0; i < hessianFilters.length; i++){
                hessianFilters[i] = new HessianFilter();
            }

            // Setting operation type for each hessian filter
            hessianFilters[0].setOperationType(HessianFilter.HessianOperationType.TYPE_DETERMINANT);
            hessianFilters[1].setOperationType(HessianFilter.HessianOperationType.TYPE_ELEMENT0_0);
            hessianFilters[2].setOperationType(HessianFilter.HessianOperationType.TYPE_ELEMENT0_1);
            hessianFilters[3].setOperationType(HessianFilter.HessianOperationType.TYPE_ELEMENT1_0);
            hessianFilters[4].setOperationType(HessianFilter.HessianOperationType.TYPE_ELEMENT1_1);
            hessianFilters[5].setOperationType(HessianFilter.HessianOperationType.TYPE_FIRST_EIGENVALUE);
            hessianFilters[6].setOperationType(HessianFilter.HessianOperationType.TYPE_GAMMA_NORM_SQUARE_EIGENVALUE_DIFFERENCE);
            hessianFilters[7].setOperationType(HessianFilter.HessianOperationType.TYPE_MODULE);
            hessianFilters[8].setOperationType(HessianFilter.HessianOperationType.TYPE_ORIENTATION);
            hessianFilters[9].setOperationType(HessianFilter.HessianOperationType.TYPE_SECOND_EIGENVALUE);
            hessianFilters[10].setOperationType(HessianFilter.HessianOperationType.TYPE_TRACE);

            // Applying filters, creating new images for each feature
            hessianResults = new Image[11];
            for(int i = 0; i < hessianResults.length; i++){
                if(hessianResults[i] != null)
                    hessianResults[i].dispose();
                hessianResults[i] = sourceImg.clone().applyFilter(hessianFilters[i]);
            }
        }
        else{
            System.err.println("Source image null");
        }
    }

    public void createFrangiFeatures() {
        if(sourceImg != null){
            // Setting operation type for each frangi filter
            frangi1 = new FrangiFilter();frangi1.setMinSigma(1);frangi1.setMaxSigma(1);
            frangi2 = new FrangiFilter();frangi2.setMinSigma(1);frangi2.setMaxSigma(2);
            frangi3 = new FrangiFilter();frangi3.setMinSigma(2);frangi3.setMaxSigma(3);

            if(frangiImg1 != null)
                frangiImg1.dispose();
            frangiImg1 = sourceImg.clone().applyFilter(frangi2);
            if(frangiImg2 != null)
                frangiImg2.dispose();
            frangiImg2 = sourceImg.clone().applyFilter(frangi2);
            if(frangiImg3 != null)
                frangiImg3.dispose();
            frangiImg3 = sourceImg.clone().applyFilter(frangi3);

        }
        else{
            System.err.println("Source image null");
        }
    }

    public void createLaplacianFeatures(){
        if(sourceImg != null) {
            //Laplacian filter settings
            lap1 = new LaplacianFilter();
            lap2 = new LaplacianFilter();

            lap2.setKernelSize(20);
            lap2.setSpreadX(2);
            lap2.setSpreadY(2);
            if(lap1Image != null)
                lap1Image.dispose();
            lap1Image = sourceImg.clone().applyFilter(lap1);
            if(lap2Image != null)
                lap2Image.dispose();
            lap2Image = sourceImg.clone().applyFilter(lap2);
        }
        else{
            System.err.println("Source image null");
        }
    }

    public void createSharpenFeatures(){
        if(sourceImg != null) {
            //Sharpness filter settings
            simpleSharpen = new SimpleSharpen();
            if(sharpImg != null)
                sharpImg.dispose();
            sharpImg = sourceImg.clone().applyFilter(simpleSharpen);
        }
        else{
            System.err.println("Source image null");
        }
    }

    public double[] extractFeatures(int x, int y) {
        double[] values = new double[hessianResults.length + 7];
        int i = 0;

        for (Image hessianResult : hessianResults) {
            values[i] = hessianResult.getPixel(x, y);
            i++;
        }
        values[i] = frangiImg1.getPixel(x,y);i++;
        values[i] = frangiImg2.getPixel(x,y);i++;
        values[i] = frangiImg3.getPixel(x,y);i++;
        values[i] = lap1Image.getPixel(x,y);i++;
        values[i] = lap2Image.getPixel(x,y);i++;
        values[i] = sharpImg.getPixel(x,y);

        // Extracting ground-truth pixel
        Color gt_color = new Color(groundTruth.getPixel(x, y));
        if (gt_color.a != 0){
            values[values.length - 1] = 1;
        }
        else{
            values[values.length - 1] = 0;
        }
        return values;
    }

    public double[] extractFeaturesWithoutClass(int x, int y) {
        double[] values = new double[hessianResults.length + 6];
        int i = 0;

        for (Image hessianResult : hessianResults) {
            values[i] = hessianResult.getPixel(x, y);
            i++;
        }
        values[i] = frangiImg1.getPixel(x,y);i++;
        values[i] = frangiImg2.getPixel(x,y);i++;
        values[i] = frangiImg3.getPixel(x,y);i++;
        values[i] = lap1Image.getPixel(x,y);i++;
        values[i] = lap2Image.getPixel(x,y);i++;
        values[i] = sharpImg.getPixel(x,y);
        return values;
    }


}
