package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Unit {
	Level level;
	int xCell;
	
	public Unit(Level level) {
		this.level = level;
	}
	
	public abstract void draw(SpriteBatch batch);
	
	public float xDraw() {
		return Consts.width / 2 + (xCell - level.size / 2f) * 32;
	}
}
