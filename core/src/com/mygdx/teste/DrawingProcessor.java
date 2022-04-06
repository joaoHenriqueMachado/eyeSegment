package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.Stack;

public class DrawingProcessor implements InputProcessor {
    Stage stage;
    int brushRadius;
    MainTable mainTable;
    Pixmap pixmap;
    int optionIndex;
    int eraserRadius;
    Color brushColor;
    Vector2 lastTouch;
    boolean insideTexture;

    Stack<Pixmap> undoStack;
    Stack<Pixmap> redoStack;
    ToolsTable toolsTable;

    public DrawingProcessor(Stage stage, ToolsTable toolsTable , MainTable mainTable, Stack<Pixmap> undoStack, Stack<Pixmap> redoStack){
        this.stage = stage;
        this.mainTable = mainTable;
        this.optionIndex = 0;
        this.brushRadius = 0;
        this.eraserRadius = 0;
        this.brushColor = Color.WHITE;
        this.lastTouch = new Vector2();
        this.undoStack = undoStack;
        this.redoStack = redoStack;
        this.toolsTable = toolsTable;
        this.insideTexture = false;
    }

    public Color getBrushColor() {
        return brushColor;
    }

    public int getEraserRadius() {
        return eraserRadius;
    }

    public int getBrushRadius() {
        return brushRadius;
    }

    /* Setters */
    public void setBrushRadius(int brushRadius) {
        this.brushRadius = brushRadius;
    }

    public void setEraserRadius(int eraserRadius) {
        this.eraserRadius = eraserRadius;
    }

    public void setBrushColor(Color brushColor) {
        this.brushColor = brushColor;
    }

    public void setOptionIndex(int optionIndex) {
        this.optionIndex = optionIndex;
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    /* ------------------------------------------------- */

    /* Move function */
    // This function is only called when the option index is set to 1 and the user touch or drag in the screen
    private void move(int screenX, int screenY){
        this.mainTable.move(screenX - lastTouch.x, lastTouch.y - screenY);
        lastTouch.set(screenX, screenY);
    }

    /* ------------------------------------------------- */

    /* Brush function */
    // This function is only called when the option index is set to 2 and the user touch or drag in the screen
    private void brush (int screenX, int screenY, Vector3 texturePosition, Vector3 imageDimension){
        // This validation will look if the position of the touch is inside the picture region
        int xPosition = (int)((screenX - texturePosition.x) * mainTable.camera.zoom);
        int yPosition = (int)((screenY - (Gdx.graphics.getHeight() - texturePosition.y - imageDimension.y )) * mainTable.camera.zoom);


        if((texturePosition.x <= screenX && screenX <= texturePosition.x + imageDimension.x) && (Gdx.graphics.getHeight() - texturePosition.y - imageDimension.y <= screenY && screenY <= Gdx.graphics.getHeight() - texturePosition.y)){

            // Drawing in the pixmap
            pixmap.setColor(brushColor);
            pixmap.fillCircle(xPosition , yPosition, brushRadius);
            mainTable.updatePixmap();
            insideTexture = true;
        }
    }

    /* ------------------------------------------------- */

    /* Eraser function */
    // This function is only called when the option index is set to 3 and the user touch or drag in the screen
    private void eraser(int screenX, int screenY, Vector3 texturePosition, Vector3 imageDimension){
        int xPosition = (int)((screenX - texturePosition.x) * mainTable.camera.zoom);
        int yPosition = (int)((screenY - (Gdx.graphics.getHeight() - texturePosition.y - imageDimension.y )) * mainTable.camera.zoom);

        // This validation will look if the position of the touch is inside the picture region
        if((texturePosition.x <= screenX && screenX <= texturePosition.x + imageDimension.x) && (Gdx.graphics.getHeight() - texturePosition.y - imageDimension.y <= screenY && screenY <= Gdx.graphics.getHeight() - texturePosition.y)){

            // Drawing in the pixmap
            pixmap.setColor(0,0,0, 0);
            pixmap.fillCircle(xPosition , yPosition, eraserRadius);
            mainTable.updatePixmap();
            insideTexture = true;
        }
    }

    /* ------------------------------------------------- */

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        mainTable.validate();
        Vector3 texturePosition = stage.getCamera().project(new Vector3(mainTable.pixImage.localToStageCoordinates(
                new Vector2(mainTable.pixImage.getX(), mainTable.pixImage.getY())), 0));

        Vector3 imageDimension = new Vector3(mainTable.pixImage.getWidth() / mainTable.camera.zoom , mainTable.pixImage.getHeight() / mainTable.camera.zoom , 0);

        if(optionIndex == 1) {
            lastTouch.set(screenX, screenY);
            move(screenX, screenY);
        }
        else if(optionIndex == 2) {
            Pixmap undoState = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
            undoState.drawPixmap(pixmap, 0, 0);
            undoStack.addElement(undoState);
            brush(screenX, screenY, texturePosition, imageDimension);

            if(!insideTexture){
                if(!undoStack.isEmpty())
                    undoStack.pop();
            }
            insideTexture = false;
        }
        else if(optionIndex == 3){
            Pixmap undoState = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
            undoState.drawPixmap(pixmap, 0, 0);
            undoStack.addElement(undoState);
            eraser(screenX,screenY, texturePosition, imageDimension);

            if(!insideTexture){
                if(!undoStack.isEmpty())
                    undoStack.pop();
            }
            insideTexture = false;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Pixmap aux;

        if(optionIndex == 2 || optionIndex == 3){
            if(!undoStack.isEmpty()){
                toolsTable.getUndo().setStyle(toolsTable.getUndoOnStyle());
                while(!redoStack.isEmpty()){
                    aux = redoStack.pop();
                    aux.dispose();
                }
                toolsTable.getRedo().setStyle(toolsTable.getRedoOffStyle());
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        mainTable.validate();

        Vector3 texturePosition = stage.getCamera().project(new Vector3(mainTable.pixImage.localToStageCoordinates(
                new Vector2(mainTable.pixImage.getX(), mainTable.pixImage.getY())), 0));

        Vector3 imageDimension = new Vector3(mainTable.pixImage.getWidth() / mainTable.camera.zoom , mainTable.pixImage.getHeight() / mainTable.camera.zoom , 0);
        
        if(optionIndex == 1)
            move(screenX, screenY);
        else if(optionIndex == 2) {
            brush(screenX, screenY, texturePosition, imageDimension);
        }
        else if(optionIndex == 3){
            eraser(screenX,screenY, texturePosition, imageDimension);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
