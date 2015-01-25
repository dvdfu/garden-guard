package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.gij.components.SpriteComponent;

public class Cloud {
	float x, y, xSpeed;
	SpriteComponent sprite;

	public Cloud(int type) {
		float r = MathUtils.random(Consts.width, Consts.width * 2);
		switch (type) {
		case 1:
			sprite = new SpriteComponent(Consts.atlas.findRegion("cloud1"));
			xSpeed = MathUtils.random(0.2f, 0.3f);
			x = r;
			y = 228;
			break;
		case 2:
			sprite = new SpriteComponent(Consts.atlas.findRegion("cloud2"));
			xSpeed = MathUtils.random(0.1f, 0.2f);
			x = r * 2;
			y = 212;
			break;
		case 3:
			sprite = new SpriteComponent(Consts.atlas.findRegion("cloud3"));
			xSpeed = MathUtils.random(0.05f, 0.1f);
			x = r * 3;
			y = 196;
			break;
		}
	}
	
	public void update() {
		x += xSpeed;
		while (x > Consts.width) {
			x -= Consts.width;
		}
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch, x, y);
		sprite.draw(batch, x - Consts.width, y);
	}
}
