package com.mygdx.teste;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class MoveProcessor implements InputProcessor {
    SegmentationTable table;
    Vector2 lastTouch;
    Boolean active;

    public MoveProcessor(SegmentationTable table){
        this.table = table;
        this.lastTouch = new Vector2();
        this.active = false;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    /* Move function */
    private void move(int screenX, int screenY){
        this.table.move(screenX - lastTouch.x, lastTouch.y - screenY);
        lastTouch.set(screenX, screenY);
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        table.validate();
        if(active){
            lastTouch.set(screenX, screenY);
            move(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        table.validate();
        if(active)
            move(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
