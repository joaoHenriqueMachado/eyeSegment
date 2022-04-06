package com.mygdx.teste.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.teste.Teste;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "EyeSegment - edit and run segmentations (alpha build 0.01)";
		config.width = 1280;
		config.height = 720;
		config.resizable = true;
		new LwjglApplication(new Teste(), config);
	}
}
