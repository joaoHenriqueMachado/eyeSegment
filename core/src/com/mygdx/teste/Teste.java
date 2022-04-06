package com.mygdx.teste;

import com.badlogic.gdx.Game;


public class Teste extends Game {
	WelcomeScreen welcomeScreen;
	MainScreen mainScreen;
	SegmentationScreen segmentationScreen;
	Fonts fonts;
	Resources resources;
	Dataset dataset;
	Filters filters;
	private Classifiers classifier;

	public MainScreen getMainScreen() {
		return mainScreen;
	}

	public SegmentationScreen getSegmentationScreen() {
		return segmentationScreen;
	}

	public void setSegmentationScreen(SegmentationScreen segmentationScreen) {
		this.segmentationScreen = segmentationScreen;
	}

	public Filters getFilters() {
		return filters;
	}

	public WelcomeScreen getWelcomeScreen() {
		return welcomeScreen;
	}

	public Fonts getFonts() {
		return fonts;
	}

	public Resources getResources() {
		return resources;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public Classifiers getClassifier() {
		return classifier;
	}

	@Override
	public void create(){
		fonts = new Fonts();
		resources = new Resources(fonts);
		filters = new Filters();
		dataset = new Dataset();
		classifier = new Classifiers(this);
		welcomeScreen = new WelcomeScreen(this);
		mainScreen = new MainScreen(this);
		setScreen(welcomeScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}

