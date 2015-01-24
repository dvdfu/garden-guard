package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.SpriteComponent;

public class Cell {
	Level level;
	int x;
	
	SpriteComponent sprite;
	
	public Cell(Level level, int x) {
		this.level = level;
		this.x = x;
		sprite = new SpriteComponent(Consts.atlas.findRegion("test"));
		
	}
	
	public void draw(SpriteBatch batch) {
		sprite.setColor(1, 1, 1);
		sprite.draw(batch, x * 32, 0);
	}
}
