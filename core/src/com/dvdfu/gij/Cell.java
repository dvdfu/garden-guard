package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.SpriteComponent;

public class Cell {
	enum State {
		EMPTY, SPROUT, TREE, TRUNK
	}
	State state;
	Level level;
	int x;
	Player owner;
	
	SpriteComponent soil;
	SpriteComponent soilTop;
	SpriteComponent sprout;
	SpriteComponent tree;
	SpriteComponent trunk;
	SpriteComponent slash;
	boolean drawSlash;
	boolean golden;
	
	public Cell(Level level, int x) {
		this.level = level;
		this.x = x;
		soil = new SpriteComponent(Consts.atlas.findRegion("soil"));
		soilTop = new SpriteComponent(Consts.atlas.findRegion("soil_top"));
		sprout = new SpriteComponent(Consts.atlas.findRegion("sprout"));
		tree = new SpriteComponent(Consts.atlas.findRegion("tree"));
		trunk = new SpriteComponent(Consts.atlas.findRegion("trunk"));
		slash = new SpriteComponent(Consts.atlas.findRegion("slash"), 48);
		slash.setOrigin(8, 0);
		slash.setFrameRate(2);
		state = State.EMPTY;
		golden = x == level.width / 2;
	}
	
	public void draw(SpriteBatch batch) {
		if (golden) {
			soil.setColor(1, 1, 0);
			soilTop.setColor(1, 1, 0);
		}
		float drawX = Consts.width / 2 + (x - level.width / 2f) * 32;
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
			if (sprout.getFrame() == 5) {
				Consts.sprout.play();
				if (owner.equals(level.p1)) {
					sprout.setImage(Consts.atlas.findRegion("sprout"));
				} else {
					sprout.setImage(Consts.atlas.findRegion("sprout_red"));
				}
			} else {
				sprout.update();
			}
			sprout.draw(batch, drawX, 96);
			break;
		case TREE:
			tree.drawOrigin(batch, drawX, 96);
			break;
		case TRUNK:
			trunk.draw(batch, drawX, 96);
			break;
		default:
			break;
		}
		if (drawSlash) {
			slash.update();
			slash.drawOrigin(batch, drawX, 96);
			if (slash.getFrame() == 7) {
				drawSlash = false;
				if (state == State.TREE) setState(this.owner, State.TRUNK);
				else if (state == State.TRUNK) setState(this.owner, State.EMPTY);
				Consts.chop.play();
				for (int i = 0; i < 20; i++) {
					Wood l = level.woodPool.obtain();
					l.x = drawX + 16;
					l.y = 96;
					level.wood.add(l);
				}
			}
		}
	}
	
	public void slash() {
		drawSlash = true;
		slash.setFrame(0);
	}
	
	public void setState(Player owner, Cell.State state) {
		this.state = state;
		this.owner = owner;
		switch (state) {
		case EMPTY:
			break;
		case SPROUT:
			if (owner.equals(level.p1)) {
				sprout.setImage(Consts.atlas.findRegion("sprout_open"), 32);
				sprout.setFrameRate(3);
			} else {
				sprout.setImage(Consts.atlas.findRegion("sprout_open_red"), 32);
				sprout.setFrameRate(3);
			}
			break;
		case TREE:
			if (owner.equals(level.p1)) {
				tree.setImage(Consts.atlas.findRegion("tree"));
			} else {
				tree.setImage(Consts.atlas.findRegion("tree_red"));
			}
			tree.setOrigin(8, 0);
			break;
		case TRUNK:
			break;
		default:
			break;
		}
	}
	
	public int getValue() {
		int v = 0;
		if (state == State.SPROUT) v = 1;
		if (state == State.TREE) v = 2;
		if (golden) v++;
		return v;
	}
}
