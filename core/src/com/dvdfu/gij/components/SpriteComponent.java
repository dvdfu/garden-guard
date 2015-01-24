package com.dvdfu.gij.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class SpriteComponent {
	protected ImageComponent image;
	protected boolean animates;
	protected float alpha;
	protected float angle;
	protected Color color;
	protected float originX, originY;
	protected float scaleX, scaleY;
	protected float width, height;
	protected int frame, frameRate, count;
	
	public SpriteComponent(TextureRegion tex) {
		this(new ImageComponent(tex));
	}
	
	public SpriteComponent(TextureRegion tex, int width) {
		this(new ImageComponent(tex, width));
		animates = true;
	}
	
	public SpriteComponent(ImageComponent image) {
		setImage(image);
		alpha = 1;
		color = new Color(1, 1, 1, 1);
		frameRate = 15;
	}
	
	public void update() {
		if (animates) {
			count++;
			if (count >= frameRate) {
				count = 0;
				frame++;
				frame %= image.getLength();
			}
		}
	}
	
	public void draw(SpriteBatch batch, float x, float y) {
		Color c = batch.getColor();
		batch.setColor(color.r, color.g, color.b, alpha);
		batch.draw(image.getFrame(frame), x, y, originX, originY, width, height, scaleX, scaleY, angle);
		batch.setColor(c);
	}
	
	public void drawCentered(SpriteBatch batch, float x, float y) {
		draw(batch, x - width / 2, y - height / 2);
	}
	
	public void drawOrigin(SpriteBatch batch, float x, float y) {
		draw(batch, x - originX, y - originY);
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void setAngle(float angle) {
		this.angle = angle * MathUtils.radDeg;
	}
	
	public void setAnimates(boolean animates) {
		this.animates = animates;
	}
	
	public void setColor(float r, float g, float b) {
		color.r = r;
		color.g = g;
		color.b = b;
	}
	
	public void setFrame(int frame) {
		this.frame = frame;
		count = 0;
	}
	
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}
	
	public void setImage(TextureRegion tex) {
		setImage(new ImageComponent(tex));
	}
	
	public void setImage(TextureRegion tex, int width) {
		setImage(new ImageComponent(tex, width));
		animates = true;
	}
	
	public void setImage(ImageComponent image) {
		this.image = image;
		frame = 0;
		count = 0;
		width = image.getWidth();
		height = image.getHeight();
		originX = width / 2;
		originY = height / 2;
		scaleX = 1;
		scaleY = 1;
	}
	
	public void setOrigin(float originX, float originY) {
		this.originX = originX;
		this.originY = originY;
	}
	
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
}