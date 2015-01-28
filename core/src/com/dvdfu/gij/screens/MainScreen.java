package com.dvdfu.gij.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dvdfu.gij.Cloud;
import com.dvdfu.gij.Consts;
import com.dvdfu.gij.Level;
import com.dvdfu.gij.MainGame;
import com.dvdfu.gij.components.SpriteComponent;

public class MainScreen extends AbstractScreen {
	SpriteBatch batch;
	OrthographicCamera camera;
	Level level;
	Viewport viewport;

	SpriteComponent soilOut;
	SpriteComponent sky;
	SpriteComponent background;
	Cloud cloud1, cloud2, cloud3;

	public MainScreen(MainGame game) {
		super(game);
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport = new ScreenViewport(camera);
		camera.position.set(Consts.width / 2, Consts.height / 2, 0);
		level = new Level();

		soilOut = new SpriteComponent(Consts.atlas.findRegion("soil_out"));
		soilOut.setSize(Consts.width, 96);
		sky = new SpriteComponent(Consts.atlas.findRegion("sky"));
		sky.setSize(Consts.width, Consts.height);
		background = new SpriteComponent(Consts.atlas.findRegion("background"));
		cloud1 = new Cloud(1);
		cloud2 = new Cloud(2);
		cloud3 = new Cloud(3);
	}

	public void render(float delta) {
		level.update();
		cloud1.update();
		cloud2.update();
		cloud3.update();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch, 0, 0);
		cloud3.draw(batch);
		cloud2.draw(batch);
		cloud1.draw(batch);
		soilOut.draw(batch, 0, 0);
		level.draw(batch);
		batch.end();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(width / 4, height / 4, 0);
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