package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.gij.components.SpriteComponent;

public class Water implements Poolable {
	SpriteComponent sprite;
	float x, y, ySpeed;
	boolean dead;
	
	public Water() {
		sprite = new SpriteComponent(Consts.atlas.findRegion("water"), 4);
		sprite.setFrameRate(2);
		reset();
	}
	
	public void update() {
		y += ySpeed;
		ySpeed -= 0.08f;
		if (y + ySpeed < 92) {
			dead = true;
		}
		sprite.update();
	}
	
	public void draw(SpriteBatch batch) {
		sprite.drawCentered(batch, x, y);
	}

	public void reset() {
		dead = false;
		sprite.setFrame(MathUtils.random(3));
		ySpeed = -3;
	}
}
