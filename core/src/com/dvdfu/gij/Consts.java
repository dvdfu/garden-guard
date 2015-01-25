package com.dvdfu.gij;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Consts {
	public static TextureAtlas atlas;
	public static final BitmapFont BigFont = new BitmapFont(Gdx.files.internal("data/test.fnt"));
	public static final BitmapFont SmallFont = new BitmapFont(Gdx.files.internal("data/test.fnt"));
	public static int width = 683;
	public static int height = 384;
	public static Sound chop;
	public static Sound count;
	public static Sound ready;
	public static Sound round;
	public static Sound select;
	public static Sound sprout;
	public static Sound tree;
	public static Sound undo;
	public static Sound water;

	public Consts() {
		AssetManager manager = new AssetManager();
		atlas = new TextureAtlas();
		manager.load("img/images.atlas", TextureAtlas.class);
		manager.load("sfx/chop.wav", Sound.class);
		manager.load("sfx/count.wav", Sound.class);
		manager.load("sfx/ready.wav", Sound.class);
		manager.load("sfx/round.wav", Sound.class);
		manager.load("sfx/select.wav", Sound.class);
		manager.load("sfx/sprout.wav", Sound.class);
		manager.load("sfx/tree.wav", Sound.class);
		manager.load("sfx/undo.wav", Sound.class);
		manager.load("sfx/water.wav", Sound.class);
		manager.finishLoading();
		atlas = manager.get("img/images.atlas", TextureAtlas.class);
		chop = manager.get("sfx/chop.wav", Sound.class);
		count = manager.get("sfx/count.wav", Sound.class);
		ready = manager.get("sfx/ready.wav", Sound.class);
		round = manager.get("sfx/round.wav", Sound.class);
		select = manager.get("sfx/select.wav", Sound.class);
		sprout = manager.get("sfx/sprout.wav", Sound.class);
		tree = manager.get("sfx/tree.wav", Sound.class);
		undo = manager.get("sfx/undo.wav", Sound.class);
		water = manager.get("sfx/water.wav", Sound.class);
	}
}
