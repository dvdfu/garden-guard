package com.dvdfu.gij;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.SpriteComponent;

public class Cell {
	enum State {
		EMPTY, SPROUT, TREE
	}
	State state;
	Level level;
	int x;
	Player owner;
	
	SpriteComponent soil;
	SpriteComponent soilTop;
	SpriteComponent sprout;
	
	public Cell(Level level, int x) {
		this.level = level;
		this.x = x;
		soil = new SpriteComponent(Consts.atlas.findRegion("soil"));
		soilTop = new SpriteComponent(Consts.atlas.findRegion("soil_top"));
		sprout = new SpriteComponent(Consts.atlas.findRegion("sprout"));
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
		switch (state) {
		case EMPTY:
			break;
		case SPROUT:
			sprout.draw(batch, drawX, 96);
			break;
		case TREE:
			break;
		default:
			break;
		}
	}
	
	public void setState(Player owner, Cell.State state) {
		this.state = state;
		this.owner = owner;
		switch (state) {
		case EMPTY:
			break;
		case SPROUT:
			break;
		case TREE:
			break;
		default:
			break;
		}
	}
}
