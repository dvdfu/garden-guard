package com.dvdfu.gij;

import java.awt.Color;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.gij.components.SpriteComponent;

public class Particle implements Poolable {
	public enum Type { NULL, WOOD, LEAF_GREEN, LEAF_RED, WATER, STAR }
	public Type type;
	public float x, y;
	public boolean dead;

	private SpriteComponent sprite;
	private float xSpeed, ySpeed, yAccel;
	private float timer;
	
	public Particle() {
		type = Type.NULL;
		sprite = new SpriteComponent(Consts.atlas.findRegion("test"));
		reset();
	}
	
	public void update() {
		switch (type) {
		case LEAF_GREEN:
		case LEAF_RED:
			if (y + ySpeed < 96) {
				dead = true;
			}
			break;
		case NULL:
			break;
		case STAR:
			if (timer == 180) {
				dead = true;
			} else {
				timer++;
			}
			break;
		case WATER:
			if (y + ySpeed < 96) {
				dead = true;
			}
			break;
		case WOOD:
			if (y + ySpeed < 96) {
				if (timer >= 60) {
					dead = true;
				} else {
					y = 96;
					ySpeed *= -0.7f;
				}
			}
			timer++;
			break;
		default:
			break;
		}
		x += xSpeed;
		y += ySpeed;
		ySpeed += yAccel;
		sprite.update();
	}
	
	public void draw(SpriteBatch batch) {
		sprite.drawCentered(batch, x, y);
	}
	
	public void setType(Type type) {
		this.type = type;
		switch (type) {
		case LEAF_GREEN:
		case LEAF_RED:
			if (type == Type.LEAF_GREEN) {
				sprite = new SpriteComponent(Consts.atlas.findRegion("petal"), 6);
			} else {
				sprite = new SpriteComponent(Consts.atlas.findRegion("petal_red"), 6);
			}
			sprite.setFrameRate(6);
			sprite.setFrame(MathUtils.random(3));
			yAccel = -0.04f;
			ySpeed = MathUtils.random(2f);
			xSpeed = MathUtils.random(-0.5f, 1f);
			break;
		case NULL:
			break;
		case STAR:
			sprite = new SpriteComponent(Consts.atlas.findRegion("sparkle"), 16);
			sprite.setFrameRate(15);
			sprite.setFrame(MathUtils.random(5));
			Color c = Color.getHSBColor(MathUtils.random(), MathUtils.random(0.5f, 1f), 1);
			sprite.setColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
			yAccel = -0.08f;
			ySpeed = MathUtils.random(-1f, 3f);
			xSpeed = MathUtils.random(-3f, 3f);
			break;
		case WATER:
			sprite = new SpriteComponent(Consts.atlas.findRegion("water"), 4);
			sprite.setFrameRate(2);
			yAccel = -0.12f;
			ySpeed = MathUtils.random(-2f);
			xSpeed = MathUtils.random(-1f, 1f);
			break;
		case WOOD:
			sprite.setImage(Consts.atlas.findRegion("wood"), 6);
			sprite.setFrameRate(6);
			sprite.setFrame(MathUtils.random(3));
			yAccel = -0.08f;
			ySpeed = MathUtils.random(1f, 3f);
			xSpeed = MathUtils.random(-1f, 1f);
			break;
		default:
			break;
		}
		timer = 0;
		dead = false;
	}

	public void reset() {}
}
