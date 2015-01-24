package com.dvdfu.gij;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Consts {
	public static TextureAtlas atlas;

	public Consts() {
		AssetManager manager = new AssetManager();
		atlas = new TextureAtlas();
		
		manager.load("img/images.atlas", TextureAtlas.class);
		manager.finishLoading();
		atlas = manager.get("img/images.atlas", TextureAtlas.class);
	}
}
