package com.mygdx.teste;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import filters.vessel.FrangiFilter;
import image.Image;

public class SegmentationThread extends Thread {
    Image img;
    Image mask;
    Pixmap frangiPixmap;
    com.badlogic.gdx.scenes.scene2d.ui.Image frangiImage;
    String path;
    Stage segmentationStage;

    private Pixmap convertTextureToPixmap (Texture tex){
        Pixmap pix = new Pixmap (tex.getWidth(), tex.getHeight(), Pixmap.Format.RGBA8888);
        if (!tex.getTextureData().isPrepared()) {
            tex.getTextureData().prepare();
        }
        pix.drawPixmap(tex.getTextureData().consumePixmap(), 0, 0);

        return pix;
    }

    private Pixmap convertImageToPixmap (Image img, String filePath, String fileName) throws Exception {
        Pixmap result;
        img.exportImage(filePath + fileName);
        Texture tex = new Texture(filePath + fileName);
        result = convertTextureToPixmap(tex);
        return result;
    }

    public SegmentationThread(Image img, Image mask, Pixmap frangiPixmap, com.badlogic.gdx.scenes.scene2d.ui.Image frangiImage, String path, Stage segmentationStage ){
        this.img = img;
        this.mask = mask;
        this.frangiImage = frangiImage;
        this.frangiPixmap = frangiPixmap;
        this.path = path;
        this.segmentationStage = segmentationStage;
    }

    @Override
    public void run() {
        if(img != null){
            try{
                img.convertToGray(1,8);
            }catch (Exception e){
                System.err.println("Error");
            }
            mask.erode(Image.MorphologyConstants.STRUCT_PRIMARY, 4);
            FrangiFilter frangi = new FrangiFilter();
            frangi.setMinSigma(0.001f);
            frangi.setBeta(0.66f, 0.85f);
            frangi.setMaxSigma(2.95f);

            img.applyFilter(frangi);
            img.maskImage(mask);
            img.threshold(1);

            try {
                synchronized (segmentationStage){
                    frangiPixmap = convertImageToPixmap(img, path, "frangi.png");
                    frangiImage.setDrawable(new SpriteDrawable(new Sprite(new Texture(frangiPixmap))));
                }

            } catch (Exception e) {
                System.out.println("Cheguei aqui");
                e.printStackTrace();
            }

        }
        super.run();
    }
}
