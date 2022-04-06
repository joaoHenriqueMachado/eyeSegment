package com.mygdx.teste;

import image.Image;

public class CompareImages {
    int trueNeg;
    int truePos;
    int falseNeg;
    int falsePos;
    float tpr; // true positive rate
    float fpr; // false positive rate
    float acc; // accuracy


    public CompareImages (){
        this.falseNeg = 0;
        this.falsePos = 0;
        this.trueNeg = 0;
        this.truePos = 0;
        this.tpr = 0;
        this.fpr = 0;
        this.acc = 0;
    }

    public int getFalseNeg() {
        return falseNeg;
    }

    public int getFalsePos() {
        return falsePos;
    }

    public int getTrueNeg() {
        return trueNeg;
    }

    public int getTruePos() {
        return truePos;
    }

    public float getAcc() {
        return acc;
    }

    public float getFpr() {
        return fpr;
    }

    public float getTpr() {
        return tpr;
    }

    public void cleanVariables (){
        this.falseNeg = 0;
        this.falsePos = 0;
        this.trueNeg = 0;
        this.truePos = 0;
        this.tpr = 0;
        this.fpr = 0;
        this.acc = 0;
    }

    public void compare(Image gt, Image img, Image mask){
        int i;
        int j;

        for(i = 0; i < gt.getWidth(); i++){
            for(j = 0; j < gt.getHeight(); j++){
                if(mask.getPixel(i, j) != 0){
                    if(gt.getPixel(i, j) == 0) {
                        if (img.getPixel(i, j) == 0)
                            this.trueNeg++;
                        else
                            this.falsePos++;
                    }
                    else{
                        if(img.getPixel(i, j) == 0){
                            this.falseNeg++;
                        }
                        else
                            this.truePos++;
                    }
                }
            }
        }
        tpr = (float)(this.truePos) / (this.truePos + this.falseNeg);
        fpr = (float)(this.falsePos) / (this.falsePos + this.trueNeg);
        acc = (float)(this.truePos + this.trueNeg) / (this.truePos + this.trueNeg + this.falsePos + this.falseNeg);

        System.out.println("TP\tTN\tFP\tFN\tTPR\tFPR\tACC");
        System.out.println(this.truePos + "\t" + this.trueNeg + "\t" + this.falsePos + "\t" + this.falseNeg + "\t" + this.tpr + "\t" + this.fpr + "\t" + this.acc);
    }

    public void compareFullImage(Image gt, Image img){
        int i;
        int j;

        for(i = 0; i < gt.getWidth(); i++){
            for(j = 0; j < gt.getHeight(); j++){
                if(gt.getPixel(i, j) == 0) {
                    if (img.getPixel(i, j) == 0)
                        this.trueNeg++;
                    else
                        this.falsePos++;
                }
                else{
                    if(img.getPixel(i, j) == 0){
                        this.falseNeg++;
                    }
                    else
                        this.truePos++;
                }
            }
        }
        tpr = (float)(this.truePos) / (this.truePos + this.falseNeg);
        fpr = (float)(this.falsePos) / (this.falsePos + this.trueNeg);
        acc = (float)(this.truePos + this.trueNeg) / (this.truePos + this.trueNeg + this.falsePos + this.falseNeg);

        System.out.println("TP\tTN\tFP\tFN\tTPR\tFPR\tACC");
        System.out.println(this.truePos + "\t" + this.trueNeg + "\t" + this.falsePos + "\t" + this.falseNeg + "\t" + this.tpr + "\t" + this.fpr + "\t" + this.acc);
    }
}
