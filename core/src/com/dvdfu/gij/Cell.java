package com.dvdfu.gij;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.SpriteComponent;

public class Cell {
	enum State {
		EMPTY, SPROUT,
	}
	State state;
	Level level;
	int x;
	Player owner;
	
	SpriteComponent soil;
	SpriteComponent soilTop;
	
	public Cell(Level level, int x) {
		this.level = level;
		this.x = x;
		soil = new SpriteComponent(Consts.atlas.findRegion("soil"));
		soilTop = new SpriteComponent(Consts.atlas.findRegion("soil_top"));
		state = State.EMPTY;
	}
	
	public void draw(SpriteBatch batch) {
		float drawX = Gdx.graphics.getWidth() / 4 + (x - level.width / 2f) * 32;
		soil.setColor(1, 1, 1);
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				soilTop.draw(batch, drawX, i * 32);
			} else {
				soil.draw(batch, drawX, i * 32);
			}
		}
	}
}
