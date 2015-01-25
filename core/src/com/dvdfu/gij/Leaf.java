package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.gij.components.SpriteComponent;

public class Leaf implements Poolable {
	SpriteComponent sprite;
	float x, y, xSpeed, ySpeed;
	boolean dead;
	
	public Leaf() {
		sprite = new SpriteComponent(Consts.atlas.findRegion("petal"), 6);
		sprite.setFrameRate(6);
		reset();
	}
	
	public void update() {
		x += xSpeed;
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
	
	public void setPlayer(int n) {
		if (n == 1) {
			sprite.setImage(Consts.atlas.findRegion("petal"), 6);
			return;
		}
		sprite.setImage(Consts.atlas.findRegion("petal_red"), 6);
	}

	public void reset() {
		dead = false;
		sprite.setFrame(MathUtils.random(3));
		ySpeed = MathUtils.random(1f, 3f);
		xSpeed = MathUtils.random(-1f, 1f);
	}
}
