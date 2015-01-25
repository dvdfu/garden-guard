package com.dvdfu.gij;

import java.awt.Color;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.gij.components.SpriteComponent;

public class Star implements Poolable {
	SpriteComponent sprite;
	float x, y, xSpeed, ySpeed, timer;
	boolean dead;
	
	public Star() {
		sprite = new SpriteComponent(Consts.atlas.findRegion("sparkle"), 16);
		sprite.setFrameRate(15);
		reset();
	}
	
	public void update() {
		x += xSpeed;
		y += ySpeed;
		if (timer == 180) {
			dead = true;
		} else {
			timer++;
		}
		ySpeed -= 0.08f;
		sprite.update();
	}
	
	public void draw(SpriteBatch batch) {
		sprite.drawCentered(batch, x, y);
	}

	public void reset() {
		dead = false;
		sprite.setFrame(MathUtils.random(5));
		Color c = Color.getHSBColor(MathUtils.random(), MathUtils.random(0.5f, 1f), 1);
		sprite.setColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
		xSpeed = MathUtils.random(-3f, 3f);
		ySpeed = MathUtils.random(-1f, 3f);
		timer = 0;
	}
}
