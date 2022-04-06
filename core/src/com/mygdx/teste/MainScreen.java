package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.*;

import java.util.Stack;

public class MainScreen implements Screen{

    Teste application;
    Texture texture;
    Texture invertedTexture;
    Texture gtTexture;
    Stage mainStage;
    Viewport viewport;

    MainTable mainTable;
    NavigationBar navigationBar;

    Pixmap pixmap;
    String maskPath = "";

    Stage pictureStage;
    static OrthographicCamera camera = new OrthographicCamera();

    Skin skins;
    TextureAtlas skinsAtlas;
    BitmapFont font;

    InputMultiplexer inputMultiplexer;
    DrawingProcessor drawingProcessor;

    Stack<Pixmap> undoStack;
    Stack<Pixmap> redoStack;

    // Cursor
    Cursor pointerCursor;
    Cursor crosshairCursor;
    Pixmap pointerPixmap;
    Pixmap crosshairPixmap;

    //Invert Colors
    TextButton invertColor;
    Pixmap inverted;

    Window.WindowStyle windowStyle;

    SegmentationOptions segmentationOptions;
    FilterSettingsWindow filterSettingsWindow;

    ToolsTable toolsTable;
    SettingsWindow settingsWindow;
    ZoomTable zoomTable;

    public MainScreen(Teste app){
        this.application = app;
        camera = new OrthographicCamera();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.pointerPixmap = new Pixmap(Gdx.files.internal("cursor.png"));
        this.crosshairPixmap = new Pixmap(Gdx.files.internal("crosshair.png"));
        this.pointerCursor = Gdx.graphics.newCursor(pointerPixmap, 8, 4);
        this.crosshairCursor = Gdx.graphics.newCursor(crosshairPixmap, crosshairPixmap.getWidth()/2, crosshairPixmap.getWidth()/2);
        segmentationOptions = new SegmentationOptions();
        Gdx.graphics.setCursor(pointerCursor);
        font = application.getFonts().getRobotoRegular16();
        inputMultiplexer = new InputMultiplexer();

        /* Adding a texture atlas to a skin */
        // The skins will be used to create the elements of the UI, like buttons and background of elements and cursors as well

        skinsAtlas = new TextureAtlas("images.pack");
        skins = new Skin();
        skins.addRegions(skinsAtlas);

        Label oneCharSizeCalibrationThrowAway = new Label("|", application.getResources().getTextStyle());
        Pixmap cursorColor = new Pixmap((int) oneCharSizeCalibrationThrowAway.getWidth() + 2,
                (int) oneCharSizeCalibrationThrowAway.getHeight(),
                Pixmap.Format.RGB888);
        cursorColor.setColor(Color.WHITE);
        cursorColor.fill();

        /* ----- Segmentation Window ------ */

        windowStyle = new Window.WindowStyle(application.getFonts().getRobotoRegular16(), Color.WHITE, application.getResources().getSkin().getDrawable("BlackBackground"));
        filterSettingsWindow = new FilterSettingsWindow("", windowStyle, application);

        /* UI and Picture stages creation and viewport setting */

        mainStage = new Stage();
        pictureStage = new Stage(new ScreenViewport(camera));
        viewport = new ScreenViewport(mainStage.getCamera());
        mainStage.setViewport(viewport);
        mainTable = new MainTable(application);

        /* ------------------------------------------------- */

        navigationBar = new NavigationBar(this, application);

        toolsTable = new ToolsTable(application.getResources().getSkin(), this);
        drawingProcessor = new DrawingProcessor(pictureStage, toolsTable, mainTable, undoStack, redoStack);

        settingsWindow = new SettingsWindow("", windowStyle, this);
        zoomTable = new ZoomTable(application.getResources().getSkin(), application,this);

        /* Listeners */
        invertColor = new TextButton("    Invert Color", application.getResources().getNavBarStyle());
        ClickListener invertListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(invertedTexture == null){
                    invertedTexture = getTexture();
                    setTexture(invertTexture(getTexture()));
                }
                else{
                    Texture aux = invertedTexture;
                    invertedTexture = getTexture();
                    setTexture(aux);
                }
                mainTable.resetTexture(getTexture());
                super.clicked(event, x, y);
            }
        };
        invertColor.addListener(invertListener);
    }

    public Pixmap createBlackWhitePixmap (Pixmap original){
        Pixmap result = new Pixmap(original.getWidth(), original.getHeight(), Pixmap.Format.RGB888);
        for(int i = 0; i < original.getWidth(); i++){
            for(int j = 0; j < original.getHeight(); j++){
                if(original.getPixel(i, j) != 0x00000000)
                    result.drawPixel(i, j, 0xFFFFFFFF);
            }
        }
        return result;
    }

    public void clearScreen(){
        if(this.texture != null)
            this.texture.dispose();
        if(this.gtTexture != null)
            this.gtTexture.dispose();
        if(this.invertedTexture != null)
            this.invertedTexture.dispose();
        if(pixmap != null)
            pixmap.dispose();
        undoStack.clear();
        toolsTable.getUndo().setStyle(toolsTable.getUndoOffStyle());
        redoStack.clear();
        toolsTable.getRedo().setStyle(toolsTable.getRedoOffStyle());
    }

    public void loadTexture(Texture texture){
        this.texture = texture;
        pixmap = new Pixmap(texture.getWidth(), texture.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        mainTable.setTable(texture, pixmap, camera);
        drawingProcessor.setPixmap(pixmap);
        zoomTable.updateZoomDisplay();
    }

    private Texture invertTexture(Texture texture){
        inverted = new Pixmap(texture.getWidth(), texture.getHeight(), Pixmap.Format.RGBA8888);
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        inverted.drawPixmap(texture.getTextureData().consumePixmap(), 0, 0);
        Color color = new Color();
        Color pixColor = new Color();

        for(int i = 0; i < inverted.getWidth(); i++ ) {
            for (int j = 0; j < inverted.getHeight(); j++) {
                color.set(1,1,1,1);
                pixColor.set(inverted.getPixel(i,j));
                color.sub(pixColor.r,pixColor.g,pixColor.b,0);
                inverted.setColor(color);
                inverted.drawPixel(i, j);
            }
        }

        return new Texture(inverted);
    }

    public DrawingProcessor getDrawingProcessor() {
        return drawingProcessor;
    }

    public MainTable getMainTable() {
        return mainTable;
    }

    public Stack<Pixmap> getUndoStack() {
        return undoStack;
    }

    public Stack<Pixmap> getRedoStack() {
        return redoStack;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getMaskPath() {
        return maskPath;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Skin getSkins() {
        return skins;
    }

    public Window.WindowStyle getWindowStyle(){
        return windowStyle;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public SettingsWindow getSettingsWindow(){
        return settingsWindow;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Texture getGtTexture() {
        return gtTexture;
    }

    public void setGtTexture(Texture gtTexture) {
        this.gtTexture = gtTexture;
    }

    public void setMaskPath(String maskPath) {
        this.maskPath = maskPath;
    }

    public ToolsTable getToolsTable(){
        return toolsTable;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public FilterSettingsWindow getFilterSettingsWindow() {
        return filterSettingsWindow;
    }

    @Override
    public void show() {

        // Adding elements to the stage
        pictureStage.addActor(mainTable);
        mainStage.addActor(toolsTable);
        mainStage.addActor(navigationBar);
        mainStage.addActor(zoomTable);

        /* ------------------------------------------------- */

        /* Input processors */
        inputMultiplexer.addProcessor(mainStage);
        inputMultiplexer.addProcessor(drawingProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        /* ------------------------------------------------- */

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pictureStage.getCamera().update();
        pictureStage.act(delta);
        pictureStage.draw();

        mainStage.getCamera().update();
        mainStage.act(delta);
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
        pictureStage.getViewport().update(width, height, false);
        camera.update();
        toolsTable.setHeight(Gdx.graphics.getHeight());
        zoomTable.setWidth(Gdx.graphics.getWidth());
        settingsWindow.resize();
        navigationBar.resize();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        mainStage.clear();
        pictureStage.clear();
        inputMultiplexer.clear();
    }

    @Override
    public void dispose() {
        mainStage.dispose();
        texture.dispose();
        invertedTexture.dispose();
        pixmap.dispose();
        skinsAtlas.dispose();
        skins.dispose();
        pictureStage.dispose();
        pointerCursor.dispose();
        crosshairCursor.dispose();
        pointerPixmap.dispose();
        crosshairPixmap.dispose();
        inverted.dispose();
        gtTexture.dispose();
    }
}
