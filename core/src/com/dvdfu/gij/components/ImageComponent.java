package com.dvdfu.gij.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageComponent {
	protected TextureRegion[] frames;
	protected int length;
	protected int width;
	protected int height;
	
	public ImageComponent(TextureRegion reg) {
		this(reg, reg.getRegionWidth());
	}

	public ImageComponent(TextureRegion reg, int width) {
		this.width = width;
		height = reg.getRegionHeight();
		length = reg.getRegionWidth() / width;
		frames = new TextureRegion[length];
		for (int i = 0; i < length; i++) {
			frames[i] = new TextureRegion(reg, i * width, 0, width, height);
		}
	}
	
	public TextureRegion getFrame() {
		return getFrame(0);
	}

	public TextureRegion getFrame(int frame) {
		while (frame < 0) {
			frame += length;
		}
		return frames[frame % length];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getLength() {
		return length;
	}
}