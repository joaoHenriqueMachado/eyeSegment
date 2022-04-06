package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainTable extends Table {
    Image texImage;
    Pixmap pixmap;
    Image pixImage;
    Stack stack;
    OrthographicCamera camera;
    Texture pixTexture;
    Teste application;

    public MainTable(Teste application){
        this.application = application;
    }

    public void setTable (Texture texture, Pixmap pixmap, OrthographicCamera camera){
        this.clear();
        this.setWidth(texture.getWidth());
        this.setHeight(texture.getHeight());
        this.pixmap = pixmap;
        this.camera = camera;

        stack = new Stack(); // The stack allows positioning two or more elements one above the other, so the pixmap and drawing will be in the same position

        // Creating images from the textures, so we can add them to the table
        texImage = new Image(texture);
        pixTexture = new Texture(pixmap);
        pixImage = new Image(pixTexture);

        // Adding images to the pixmap
        stack.add(texImage);
        stack.add(pixImage);

        this.add(stack);

        // Positioning the table in the screen
        this.setPosition((Gdx.graphics.getWidth() - texImage.getWidth())/2 + 18, (Gdx.graphics.getHeight() - texImage.getHeight())/2 + 4);

        // Adjusting camera zoom, so the entire image fits the screen
        if(Gdx.graphics.getHeight() < Gdx.graphics.getWidth())
            camera.zoom = (float)texture.getHeight() / (Gdx.graphics.getHeight() - 56);
        else
            camera.zoom = (float)texture.getWidth() / Gdx.graphics.getWidth();
    }

    // This function resets the elements of the table when the pixmap is changed
    public void updatePixmap() {
        //first, we clear the elements added to the stage
        this.clear();

        // dispose the old texture and create a new one
        pixTexture.dispose(); // disposing the texture from the memory, avoiding high memory usage
        pixTexture = new Texture(pixmap);
        pixImage = new Image(pixTexture);

        // and finally, add the elements to the stage again
        stack.clear();
        stack.add(texImage);
        stack.add(pixImage);
        this.add(stack);
    }

    public void hideDrawing(){
        this.clear();

        stack.clear();
        stack.add(texImage);
        this.add(stack);
    }

    public void showDrawing(){
        this.clear();

        stack.clear();
        stack.add(texImage);
        stack.add(pixImage);
        this.add(stack);
    }

    public void resetTexture(Texture updated){
        this.clear();

        texImage = new Image(updated);
        stack.clear();
        stack.add(texImage);
        stack.add(pixImage);
        this.add(stack);
    }

    public void move(float x, float y){
        this.setPosition(this.getX() + x, this.getY() + y);
    }
}
