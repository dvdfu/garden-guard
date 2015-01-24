package com.dvdfu.gij;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Consts {
	public static TextureAtlas atlas;
	public static final BitmapFont BigFont = new BitmapFont(Gdx.files.internal("data/test.fnt"));
	public static final BitmapFont SmallFont = new BitmapFont(Gdx.files.internal("data/test.fnt"));

	public Consts() {
		AssetManager manager = new AssetManager();
		atlas = new TextureAtlas();
		
		manager.load("img/images.atlas", TextureAtlas.class);
		manager.finishLoading();
		atlas = manager.get("img/images.atlas", TextureAtlas.class);
	}
}
