package com.dvdfu.gij;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Text {
	String text;
	BitmapFont font;
	Color color;
	boolean centered;
	boolean bordered;
	float x, y;

	public Text() {
		this("");
	}

	public Text(String text) {
		this.text = text;
		font = Consts.Test;
		color = new Color(1, 1, 1, 1);
		font.setColor(color);
		bordered = true;
	}

	public float getWidth() {
		return font.getBounds(text).width;
	}

	public float getHeight() {
		return font.getBounds(text).height;
	}

	public void draw(Batch batch) {
		if (centered) {
			draw(batch, x - getWidth() / 2, y + getHeight() / 2);
		} else {
			draw(batch, x, y);
		}
	}

	public void draw(Batch batch, float x, float y) {
		if (bordered) {
			font.setColor(0, 0, 0, 1);
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					font.draw(batch, text, x + i, y + j);
				}
			}
			font.setColor(color);
		}
		font.draw(batch, text, x, y);
	}
}
