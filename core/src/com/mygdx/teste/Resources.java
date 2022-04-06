package com.mygdx.teste;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import javax.swing.filechooser.FileNameExtensionFilter;

public class Resources {
    private Skin skin;
    private TextureAtlas textureAtlas;
    private Pixmap textFieldCursor;
    private TextField.TextFieldStyle textFieldStyle;
    private Label.LabelStyle textFieldLabelStyle;
    private Label.LabelStyle titleStyle;
    private Label.LabelStyle subtitleStyle;
    private Label.LabelStyle textStyle;
    private TextButton.TextButtonStyle navBarStyle;
    private Window.WindowStyle navBarWindowStyle;
    private JSystemFileChooser jsfc;
    private FileNameExtensionFilter filter;
    private FileNameExtensionFilter model_filter;
    private ImageButton.ImageButtonStyle checkboxStyle;
    private TextButton.TextButtonStyle blueButtonStyle;
    private TextButton.TextButtonStyle closeStyle;
    private NinePatchDrawable button9Drawable;
    private TextButton.TextButtonStyle buttonStyle;
    private Window.WindowStyle windowStyle;
    private int navigationButtonWidth;
    private int navigationButtonHeight;
    private int navigationButtonPad;

    private SelectBox.SelectBoxStyle selectBoxStyle;
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;
    private List.ListStyle listStyle;

    public Resources(Fonts fonts){
        textureAtlas = new TextureAtlas("skins.atlas");
        skin = new Skin();
        skin.addRegions(textureAtlas);
        textFieldLabelStyle = new Label.LabelStyle(fonts.getRobotoRegular16(), Color.WHITE);
        titleStyle = new Label.LabelStyle(fonts.getRobotoBold24(), Color.WHITE);
        subtitleStyle = new Label.LabelStyle(fonts.getRobotoBold18(), Color.WHITE);
        textStyle = new Label.LabelStyle(fonts.getRobotoRegular16(), Color.WHITE);
        Label oneCharSizeCalibrationThrowAway = new Label("|", textFieldLabelStyle);
        textFieldCursor = new Pixmap((int) oneCharSizeCalibrationThrowAway.getWidth(),
                (int) oneCharSizeCalibrationThrowAway.getHeight(),
                Pixmap.Format.RGB888);
        textFieldCursor.setColor(Color.WHITE);
        textFieldCursor.fill();
        textFieldStyle  = new TextField.TextFieldStyle(fonts.getRobotoRegular16(), Color.WHITE, new Image(new Texture(textFieldCursor)).getDrawable(), null, skin.getDrawable("GrayBackground"));
        textFieldStyle.cursor.setMinWidth(2f);
        navBarStyle = new TextButton.TextButtonStyle();
        navBarStyle.down = skin.getDrawable("GrayBackground");
        navBarStyle.font = fonts.getRobotoRegular16();
        navBarWindowStyle = new Window.WindowStyle(fonts.getRobotoRegular16(), Color.WHITE, skin.getDrawable("whiteBackground"));

        jsfc = new JSystemFileChooser();
        filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp", "tif");
        model_filter = new FileNameExtensionFilter("Model object file", "model");
        jsfc.setFileFilter(filter);
        jsfc.getFileSystemView().getHomeDirectory();

        checkboxStyle = new ImageButton.ImageButtonStyle();
        checkboxStyle.imageUp = skin.getDrawable("checkboxOff");
        checkboxStyle.imageChecked = skin.getDrawable("checkboxOn");

        closeStyle = new TextButton.TextButtonStyle();
        closeStyle.up = skin.getDrawable("Close");
        closeStyle.down = skin.getDrawable("Down");
        closeStyle.font = fonts.getRobotoRegular16();

        button9Drawable = new NinePatchDrawable(skin.getPatch("gray_bg_with_border"));
        buttonStyle = new TextButton.TextButtonStyle(button9Drawable, button9Drawable, button9Drawable, fonts.getRobotoRegular16());

        blueButtonStyle = new TextButton.TextButtonStyle();
        blueButtonStyle.up = skin.getDrawable("StartSegmentation");
        blueButtonStyle.down = skin.getDrawable("Down");
        blueButtonStyle.font = fonts.getRobotoRegular16();

        windowStyle = new Window.WindowStyle(fonts.getRobotoRegular16(), Color.WHITE, skin.getDrawable("BlackBackground"));
        navigationButtonWidth = 196;
        navigationButtonHeight = 32;
        navigationButtonPad = 4;

        listStyle = new List.ListStyle();
        listStyle.over = skin.getDrawable("dark_gray_bg");
        listStyle.font = fonts.getRobotoRegular16();
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.WHITE;;
        listStyle.selection = skin.getDrawable("GrayBackground");
        scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = skin.getDrawable("dark_gray_bg");
        selectBoxStyle = new SelectBox.SelectBoxStyle(fonts.getRobotoRegular16(), Color.WHITE, skin.getDrawable("dark_gray_bg"), scrollPaneStyle, listStyle);
    }

    public Skin getSkin() {
        return skin;
    }

    public Window.WindowStyle getWindowStyle() {
        return windowStyle;
    }

    public ImageButton.ImageButtonStyle getCheckboxStyle() {
        return checkboxStyle;
    }

    public TextButton.TextButtonStyle getBlueButtonStyle() {
        return blueButtonStyle;
    }

    public TextButton.TextButtonStyle getCloseStyle() {
        return closeStyle;
    }

    public TextButton.TextButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    public Pixmap getTextFieldCursor() {
        return textFieldCursor;
    }

    public Label.LabelStyle getTextFieldLabelStyle() {
        return textFieldLabelStyle;
    }

    public TextField.TextFieldStyle getTextFieldStyle() {
        return textFieldStyle;
    }

    public Label.LabelStyle getTitleStyle() {
        return titleStyle;
    }

    public Label.LabelStyle getSubtitleStyle() {
        return subtitleStyle;
    }

    public Label.LabelStyle getTextStyle() {
        return textStyle;
    }

    public TextButton.TextButtonStyle getNavBarStyle() {
        return navBarStyle;
    }

    public Window.WindowStyle getNavBarWindowStyle() {
        return navBarWindowStyle;
    }

    public JSystemFileChooser getJsfc() {
        return jsfc;
    }

    public FileNameExtensionFilter getFilter() {
        return filter;
    }

    public FileNameExtensionFilter getModel_filter() {
        return model_filter;
    }

    public int getNavigationButtonWidth() {
        return navigationButtonWidth;
    }

    public int getNavigationButtonHeight() {
        return navigationButtonHeight;
    }

    public int getNavigationButtonPad() {
        return navigationButtonPad;
    }

    public SelectBox.SelectBoxStyle getSelectBoxStyle() {
        return selectBoxStyle;
    }
}
