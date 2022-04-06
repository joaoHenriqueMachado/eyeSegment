package com.mygdx.teste;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import filters.connectivity.Connectivity;
import filters.connectivity.SmartRodriguesConnectivity;
import filters.vessel.FrangiFilter;
import image.Image;
import java.io.File;
import java.io.IOException;

public class SegmentationScreen implements Screen {
    MainScreen mainScreen;
    Pixmap gt;
    Pixmap texture;
    Teste application;
    Stage stage;
    Stage pictureStage;
    Viewport viewport;
    final OrthographicCamera camera = new OrthographicCamera();
    Image gtImg;
    static Image img;
    static Image mask = null;
    static Image con0;
    static Image con1;
    static SegmentationOptions segmentationOptions;
    MoveProcessor moveProcessor;

    Table bottomBar;
    ImageButton moveButton;
    ImageButton.ImageButtonStyle moveStyle;
    ImageButton zoomIn;
    ImageButton zoomOut;
    ImageButton adjustToScreen;
    TextField zoomDisplay;
    ImageButton compareBtn;
    ComparisonWindow comparisonWindow;
    ChangeListener closeComparisonWindowListener;
    Label imageLabel;
    ImageButton saveButton;

    static boolean hasMask = false;
    static boolean connSelected;
    static boolean smartConnSelected;

    static Pixmap frangiPixmap;
    static boolean frangiSegmentation = false;
    static boolean connSegmentation = false;
    static boolean smartConnSegmentation = false;
    static Pixmap connectivityPixmap;
    static Pixmap smartConnectivityPixmap;
    static com.badlogic.gdx.scenes.scene2d.ui.Image frangiImage;
    static com.badlogic.gdx.scenes.scene2d.ui.Image connectivityImage;
    static com.badlogic.gdx.scenes.scene2d.ui.Image smartConnectivityImage;
    static String path = "C:\\Users\\Joao Henrique\\Desktop\\UNIVERSIDADE\\Iniciação\\Teste\\core\\output\\";

    TextButton back2Draw;
    Table nextButtonTable;
    ImageButton nextButton;
    Table prevButtonTable;
    ImageButton prevButton;
    
    SegmentationTable segmentationTable;
    DoublyLinkedList imageList;
    static Thread imageSegmentation;

    ClickListener changeScreen = new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y) {
            imageSegmentation.interrupt();
            application.setScreen(mainScreen);
            super.clicked(event, x, y);
        }
    };

    private Image convertPixmapToImage (Pixmap pix, String fileName, String filePath) {
        Image img = null;
        try{
            FileHandle fh;
            fh = new FileHandle(filePath + fileName);
            PixmapIO.writePNG(fh, pix);
            img = new Image(filePath + fileName);
        }catch (Exception e){
            System.out.println("Error exporting");
        }
        return img;
    }

    private static Pixmap convertImageToPixmap(Image img, String filePath, String fileName) throws Exception {
        Pixmap result;
        img.exportImage(filePath + fileName);
        Texture tex = new Texture(filePath + fileName);
        result = convertTextureToPixmap(tex);
        return result;
    }

    private static Pixmap convertTextureToPixmap(Texture tex){
        Pixmap pix = new Pixmap (tex.getWidth(), tex.getHeight(), Pixmap.Format.RGB888);
        if (!tex.getTextureData().isPrepared()) {
            tex.getTextureData().prepare();
        }
        pix.drawPixmap(tex.getTextureData().consumePixmap(), 0, 0);

        return pix;
    }

    private void updatePixmap() throws Exception {
        if(frangiSegmentation){
            frangiPixmap = convertImageToPixmap(img, path, "frangi.png");
            frangiImage.setDrawable(new SpriteDrawable(new Sprite(new Texture(frangiPixmap))));
            frangiSegmentation = false;
        }
        if(connSegmentation){
            connectivityPixmap = convertImageToPixmap(con0, path, "conn.png");
            connectivityImage.setDrawable(new SpriteDrawable(new Sprite(new Texture(connectivityPixmap))));
            connSegmentation = false;
        }
        if(smartConnSegmentation){
            smartConnectivityPixmap = convertImageToPixmap(con1, path, "smartConn.png");
            smartConnectivityImage.setDrawable(new SpriteDrawable(new Sprite(new Texture(smartConnectivityPixmap))));
            smartConnSegmentation = false;
        }
    }

    public Teste getApplication() {
        return application;
    }

    public SegmentationScreen (final Teste application, Pixmap texture, Pixmap gt, SegmentationOptions segmentationOptions, Boolean connSelected, Boolean smartConnSelected){
        this.application = application;
        this.texture = texture;
        this.gt = gt;
        SegmentationScreen.segmentationOptions = segmentationOptions;
        imageList = new DoublyLinkedList();
        SegmentationScreen.connSelected = connSelected;
        SegmentationScreen.smartConnSelected = smartConnSelected;

        stage = new Stage();
        viewport = new ScreenViewport(stage.getCamera());
        stage.setViewport(viewport);
        mainScreen = application.getMainScreen();

        try {
            img = convertPixmapToImage(texture, "img.png", path);
            gtImg = convertPixmapToImage(this.gt, "gt.png", path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        frangiPixmap = texture;
        frangiImage = new com.badlogic.gdx.scenes.scene2d.ui.Image(new Texture(frangiPixmap));
        imageList.insertLast(frangiImage, "    Frangi   ", img);
        if(connSelected){
            connectivityPixmap = texture;
            connectivityImage = new com.badlogic.gdx.scenes.scene2d.ui.Image(new Texture(connectivityPixmap));
            imageList.insertLast(connectivityImage, "    Connectivity   ", con0);
        }
        if(smartConnSelected){
            smartConnectivityPixmap = texture;
            smartConnectivityImage = new com.badlogic.gdx.scenes.scene2d.ui.Image(new Texture(smartConnectivityPixmap));
            imageList.insertLast(smartConnectivityImage, "    SmartConnectivity   ", con1);
        }
        System.out.println(application.getMainScreen().getMaskPath());
        try {
            if(!application.getMainScreen().getMaskPath().equals("")){
                mask = new Image(application.getMainScreen().getMaskPath());
                hasMask = true;
            }
        } catch (IOException e) {
            hasMask = false;
            e.printStackTrace();
            System.out.println("Mask not applied");
        }

        imageSegmentation = new Thread(){
            @Override
            public void run() {
                if(img != null){
                    try {
                        img.convertToGray(1,8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FrangiFilter frangi = new FrangiFilter();
                    frangi.setMinSigma(SegmentationScreen.segmentationOptions.getMinSigma());
                    frangi.setBeta(SegmentationScreen.segmentationOptions.getBetaX(), SegmentationScreen.segmentationOptions.getBetaY());
                    frangi.setMaxSigma(SegmentationScreen.segmentationOptions.getMaxSigma());

                    img.applyFilter(frangi);
                    if(hasMask){
                        mask.erode(Image.MorphologyConstants.STRUCT_PRIMARY, SegmentationScreen.segmentationOptions.getMaskErosion());
                        img.maskImage(mask);
                    }
                    img.threshold(SegmentationScreen.segmentationOptions.getFrangiThreshold());
                    frangiSegmentation = true;

                    if(SegmentationScreen.connSelected){
                        con0 = img.clone();
                        Connectivity connFilter = new Connectivity();
                        con0.applyFilter(connFilter);
                        con0.threshold(SegmentationScreen.segmentationOptions.getConnThreshold())
                                .setTitle("Connectivity filter");
                        connSegmentation = true;
                    }
                    if(SegmentationScreen.smartConnSelected){
                        con1 = img.clone();
                        SmartRodriguesConnectivity smartConn = new SmartRodriguesConnectivity();
                        con1.applyFilter(smartConn);

                        con1.setTitle("Smart connectivity filter + erosion")
                                .threshold(SegmentationScreen.segmentationOptions.getSmartConnThreshold()).erode(Image.MorphologyConstants.STRUCT_SUP, 1);
                        smartConnSegmentation = true;
                    }
                }
                System.out.println("End");
                super.run();
            }
        };

        try {
            imageSegmentation.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        segmentationTable = new SegmentationTable(imageList.getHead().getImage(), camera);
        moveProcessor = new MoveProcessor(segmentationTable);
        
        bottomBar = new Table();
        bottomBar.setBounds(0, 0, Gdx.graphics.getWidth(), 32);
        bottomBar.left();
        bottomBar.background(application.getMainScreen().getSkins().getDrawable("whiteBackground"));
        bottomBar.setColor(0.1020f, 0.1020f, 0.1020f, 1);

        moveButton = new ImageButton(application.getMainScreen().getSkins().getDrawable("move"));
        moveStyle = new ImageButton.ImageButtonStyle();
        moveStyle.imageUp = application.getMainScreen().getSkins().getDrawable("moveOff");
        moveStyle.imageDown = application.getMainScreen().getSkins().getDrawable("move");
        moveStyle.imageChecked = application.getMainScreen().getSkins().getDrawable("move");
        moveButton.setStyle(moveStyle);
        bottomBar.add(moveButton).width(16).height(16).padRight(8).padLeft(24);


        // Creating and adding zoom in button to the table
        zoomIn = new ImageButton(application.getMainScreen().getSkins().getDrawable("zoomIn"));
        bottomBar.add(zoomIn).width(24).height(24).padRight(8);

        // Creating and adding zoom display text field to the table
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(application.getMainScreen().getFont(), Color.WHITE, null, null, null);
        zoomDisplay = new TextField(String.format("%.2f", (1 / camera.zoom) * 100 ), textFieldStyle);
        zoomDisplay.setAlignment(Align.center);
        bottomBar.add(zoomDisplay).bottom().width(60).center();

        // Adding a percentage text at the side of the zoom display
        Label.LabelStyle labelStyle = new Label.LabelStyle(application.getMainScreen().getFont(), Color.WHITE);
        Label percentageText = new Label("%", labelStyle);
        bottomBar.add(percentageText).width(10).center().padRight(8);

        // Creating and adding zoom out button to the table
        zoomOut = new ImageButton(application.getMainScreen().getSkins().getDrawable("zoomOut"));
        bottomBar.add(zoomOut).width(24).height(24).padRight(8);

        // Creating and adding adjust to screen button to the table
        // This button will adjust the zoom of the image, so it fits the screen
        adjustToScreen = new ImageButton(application.getMainScreen().getSkins().getDrawable("adjustScreen"));
        bottomBar.add(adjustToScreen).width(24).height(24).padRight(8);

        saveButton = new ImageButton(application.getMainScreen().getSkins().getDrawable("diskette"));
        bottomBar.add(saveButton).width(16).height(16).padRight(8);

        compareBtn = new ImageButton(mainScreen.getSkins().getDrawable("compare"));
        bottomBar.add(compareBtn).width(24).height(24);

        imageLabel = new Label(imageList.getHead().getImageName(), application.getResources().getTextFieldLabelStyle());
        bottomBar.add(imageLabel);

        back2Draw = new TextButton("   Return to the edit window   ", application.getResources().getNavBarStyle());
        back2Draw.addListener(changeScreen);
        bottomBar.add(back2Draw);

        nextButtonTable = new Table();
        nextButtonTable.setBounds(Gdx.graphics.getWidth() - 172, 32, 172, Gdx.graphics.getHeight() - 32);
        nextButtonTable.center();
        nextButtonTable.setBackground(application.getMainScreen().getSkins().getDrawable("whiteBackground"));
        nextButtonTable.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        nextButton = new ImageButton(mainScreen.getSkins().getDrawable("right-arrow"));
        nextButtonTable.add(nextButton).width(64).height(64);

        prevButtonTable = new Table();
        prevButtonTable.setBounds(0, 32, 172, Gdx.graphics.getHeight() - 32);
        prevButtonTable.center();
        prevButtonTable.setBackground(application.getMainScreen().getSkins().getDrawable("whiteBackground"));
        prevButtonTable.setColor(0.1020f, 0.1020f, 0.1020f, 1);
        prevButton = new ImageButton(mainScreen.getSkins().getDrawable("left-arrow"));
        prevButtonTable.add(prevButton).width(64).height(64);
        pictureStage = new Stage(new ScreenViewport(camera));

        /* Listeners */

        nextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                imageList.setHead(imageList.getHead().getNext());
                imageLabel.setText(imageList.getHead().getImageName());
                segmentationTable.updateImage(imageList.getHead().getImage());
                super.clicked(event, x, y);
            }
        });

        prevButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                imageList.setHead(imageList.getHead().getPrev());
                imageLabel.setText(imageList.getHead().getImageName());
                segmentationTable.updateImage(imageList.getHead().getImage());
                super.clicked(event, x, y);
            }
        });

        moveButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                moveProcessor.setActive(moveButton.isChecked());
                super.clicked(event, x, y);
            }
        });

        zoomIn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(camera.zoom > 0.1f){
                    camera.zoom -= 0.1020f;
                    zoomDisplay.setText(String.format("%.2f", (1 / camera.zoom) * 100));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        adjustToScreen.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Gdx.graphics.getHeight() < Gdx.graphics.getWidth())
                    camera.zoom = segmentationTable.getHeight() / (Gdx.graphics.getHeight() - 32) ;
                else
                    camera.zoom = segmentationTable.getWidth() / Gdx.graphics.getWidth() ;
                camera.position.set(segmentationTable.getX() + (segmentationTable.getWidth())/2, segmentationTable.getY() + segmentationTable.getHeight()/2 , 0 );
                segmentationTable.move(0, 16);

                zoomDisplay.setText(String.format("%.2f", (1 / camera.zoom) * 100));
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        zoomOut.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(camera.zoom <= 1.9f){
                    camera.zoom += 0.1020f;
                    zoomDisplay.setText(String.format("%.2f", (1 / camera.zoom) * 100));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        zoomDisplay.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13){
                    String text = textField.getText();
                    text = text.replaceAll(",", ".");

                    camera.zoom = 1 / (Float.parseFloat(text) / 100 );

                    text = text.replaceAll("\\.", ",");
                    textField.setText(text);
                }
            }
        });

        saveButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.getResources().getJsfc().getFileSystemView().getHomeDirectory();
                int returnValue = application.getResources().getJsfc().showSaveDialog(null);
                if(returnValue == JSystemFileChooser.APPROVE_OPTION) {
                    try{
                        File f = application.getResources().getJsfc().getSelectedFile();
                        imageList.getHead().getExportImage().exportImage(f.getAbsolutePath() + ".png");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.clicked(event, x, y);
            }
        });

        closeComparisonWindowListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                comparisonWindow.remove();
            }
        };

        compareBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                comparisonWindow = new ComparisonWindow("", mainScreen.getWindowStyle());
                comparisonWindow.createWindow(closeComparisonWindowListener, getApplication().getSegmentationScreen(), img, gtImg, mask);
                stage.addActor(comparisonWindow);
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void show() {
        pictureStage.addActor(segmentationTable);
        stage.addActor(bottomBar);
        stage.addActor(nextButtonTable);
        stage.addActor(prevButtonTable);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(moveProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        if(imageSegmentation.isInterrupted()){
            imageSegmentation.start();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        try {
            updatePixmap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pictureStage.getCamera().update();
        pictureStage.act(delta);
        pictureStage.draw();

        stage.getCamera().update();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        pictureStage.getViewport().update(width, height, false);
        bottomBar.setWidth(Gdx.graphics.getWidth());
        prevButtonTable.setHeight(Gdx.graphics.getHeight() - 32);
        nextButtonTable.setHeight(Gdx.graphics.getHeight() - 32);
        nextButtonTable.setPosition(Gdx.graphics.getWidth() - 172, 32);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        if(imageSegmentation.isInterrupted()){
            imageSegmentation.start();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if(!imageSegmentation.isInterrupted()){
            try{
                Thread.sleep(1);
                imageSegmentation.interrupt();
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stage.dispose();
        pictureStage.dispose();
        frangiPixmap.dispose();
        if(connSelected){
            connectivityPixmap.dispose();
        }
        if(con0 != null){
            con0.dispose();
        }
        if(smartConnSelected){
            smartConnectivityPixmap.dispose();
        }
        if(con1 != null){
            con1.dispose();
        }
        gt.dispose();
        gtImg.dispose();
        img.dispose();
    }
}
