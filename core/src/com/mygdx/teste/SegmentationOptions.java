package com.mygdx.teste;

public class SegmentationOptions {
    float minSigma;
    float betaX;
    float betaY;
    float maxSigma;
    int maskErosion;
    int frangiThreshold;
    int connThreshold;
    int smartConnThreshold;

    public SegmentationOptions(){
        minSigma = 0.9f;
        maxSigma = 3.5f;
        betaX = 1.1f;
        betaY = 2.3f;
        maskErosion = 12;
        frangiThreshold = 26;
        connThreshold = 3;
        smartConnThreshold = 3;
    }

    public void setBetaX(float betaX) {
        this.betaX = betaX;
    }

    public void setBetaY(float betaY) {
        this.betaY = betaY;
    }

    public void setConnThreshold(int connThreshold) {
        this.connThreshold = connThreshold;
    }

    public void setFrangiThreshold(int frangiThreshold) {
        this.frangiThreshold = frangiThreshold;
    }

    public void setMaskErosion(int maskErosion) {
        this.maskErosion = maskErosion;
    }

    public void setMaxSigma(float maxSigma) {
        this.maxSigma = maxSigma;
    }

    public void setMinSigma(float minSigma) {
        this.minSigma = minSigma;
    }

    public void setSmartConnThreshold(int smartConnThreshold) {
        this.smartConnThreshold = smartConnThreshold;
    }

    public float getBetaX() {
        return betaX;
    }

    public float getBetaY() {
        return betaY;
    }

    public float getMaxSigma() {
        return maxSigma;
    }

    public float getMinSigma() {
        return minSigma;
    }

    public int getConnThreshold() {
        return connThreshold;
    }

    public int getFrangiThreshold() {
        return frangiThreshold;
    }

    public int getMaskErosion() {
        return maskErosion;
    }

    public int getSmartConnThreshold() {
        return smartConnThreshold;
    }
}
