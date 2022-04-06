package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {
    final private BitmapFont robotoRegular24;
    final private BitmapFont robotoBold24;
    final private BitmapFont robotoRegular16;
    final private BitmapFont robotoBold16;
    final private BitmapFont robotoRegular18;
    final private BitmapFont robotoBold18;
    private boolean loaded;


    public Fonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 16;
        robotoRegular16 = generator.generateFont(parameter);
        parameter.size = 18;
        robotoRegular18 = generator.generateFont(parameter);
        parameter.size = 24;
        robotoRegular24 = generator.generateFont(parameter);
        generator.dispose();

        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("Roboto/Roboto-Bold.ttf"));
        parameter.size = 16;
        robotoBold16 = generator2.generateFont(parameter);
        parameter.size = 18;
        robotoBold18 = generator2.generateFont(parameter);
        parameter.size = 24;
        robotoBold24 = generator2.generateFont(parameter);
        generator2.dispose();

        loaded = true;
    }

    public BitmapFont getRobotoBold16() {
        return robotoBold16;
    }

    public BitmapFont getRobotoRegular18() {
        return robotoRegular18;
    }

    public BitmapFont getRobotoBold24() {
        return robotoBold24;
    }

    public BitmapFont getRobotoRegular16() {
        return robotoRegular16;
    }

    public BitmapFont getRobotoBold18() {
        return robotoBold18;
    }

    public BitmapFont getRobotoRegular24() {
        return robotoRegular24;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
