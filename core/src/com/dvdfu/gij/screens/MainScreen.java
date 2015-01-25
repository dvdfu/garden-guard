package com.dvdfu.gij.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.Consts;
import com.dvdfu.gij.Level;
import com.dvdfu.gij.MainGame;
import com.dvdfu.gij.components.SpriteComponent;

public class MainScreen extends AbstractScreen {
	SpriteBatch batch;
	OrthographicCamera camera;
	Level level;

	SpriteComponent soilOut;
	SpriteComponent sky;
	SpriteComponent background;

	public MainScreen(MainGame game) {
		super(game);
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Consts.width * 2, Consts.height * 2);
		camera.position.set(Consts.width / 2, Consts.height / 2, 0);
		level = new Level();

		soilOut = new SpriteComponent(Consts.atlas.findRegion("soil_out"));
		soilOut.setSize(Consts.width, 96);
		sky = new SpriteComponent(Consts.atlas.findRegion("sky"));
		sky.setSize(Consts.width, Consts.height);
		background = new SpriteComponent(Consts.atlas.findRegion("background"));
	}

	public void render(float delta) {
		level.update();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch, 0, 0);
		soilOut.draw(batch, 0, 0);
		level.draw(batch);
		batch.end();
	}

	public void resize(int width, int height) {
	}

	public void show() {
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}

}