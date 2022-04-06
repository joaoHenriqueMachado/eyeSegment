package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SegmentationTable extends Table {
    Image selectedImage;
    OrthographicCamera camera;

    public SegmentationTable (Image selectedImage, OrthographicCamera camera){
        this.selectedImage = selectedImage;
        this.camera = camera;

        // Setting table size and adding the image to it
        this.setWidth(selectedImage.getWidth());
        this.setHeight(selectedImage.getHeight());
        this.add(selectedImage);

        // Positioning the table in the screen
        this.setPosition((Gdx.graphics.getWidth() - selectedImage.getWidth())/2, (Gdx.graphics.getHeight() - selectedImage.getHeight())/2 + 16);

        // Adjusting camera zoom, so the entire image fits the screen
        if(Gdx.graphics.getHeight() < Gdx.graphics.getWidth())
            camera.zoom = selectedImage.getHeight() / (Gdx.graphics.getHeight() - 32) ;
        else
            camera.zoom = selectedImage.getWidth() / Gdx.graphics.getWidth() ;
    }

    public void updateImage(Image updated){
        this.clear();
        selectedImage = updated;

        // Setting table size and adding the image to it
        //this.setWidth(selectedImage.getWidth());
        //this.setHeight(selectedImage.getHeight());
        this.add(selectedImage);

        if(Gdx.graphics.getHeight() < Gdx.graphics.getWidth())
            camera.zoom = this.getHeight() / (Gdx.graphics.getHeight() - 32) ;
        else
            camera.zoom = this.getWidth() / Gdx.graphics.getWidth() ;
        camera.position.set(this.getX() + (this.getWidth())/2, this.getY() + this.getHeight()/2 , 0 );
        this.move(0, 16);
    }

    public void move(float x, float y){
        this.setPosition(this.getX() + x, this.getY() + y);
    }
}
