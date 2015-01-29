package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.SpriteComponent;

public class Cell extends Unit {
	public enum State {
		EMPTY, SPROUT, TREE, TRUNK
	}
	public State state;
	public Player owner;
	public boolean golden;
	
	private SpriteComponent soil;
	private SpriteComponent soilTop;
	private SpriteComponent sprout;
	private SpriteComponent tree;
	private SpriteComponent trunk;
	private SpriteComponent slash;

	private boolean drawSlash;
	
	public Cell(Level level, int xCell) {
		super(level);
		this.xCell = xCell;
		state = State.EMPTY;
		
		soil = new SpriteComponent(Consts.atlas.findRegion("soil"));
		soilTop = new SpriteComponent(Consts.atlas.findRegion("soil_top"));
		sprout = new SpriteComponent(Consts.atlas.findRegion("sprout"));
		sprout.setFrameRate(4);
		tree = new SpriteComponent(Consts.atlas.findRegion("tree"));
		trunk = new SpriteComponent(Consts.atlas.findRegion("trunk"));
		slash = new SpriteComponent(Consts.atlas.findRegion("slash"), 48);
		slash.setOrigin(8, 0);
		slash.setFrameRate(2);
		
		golden = xCell == level.size / 2;
	}
	
	public void update() {
		if (state == Cell.State.SPROUT) {
			sprout.update();
			if (sprout.getTimesPlayed() > 0) {
				sprout.reset();
				Consts.sprout.play();
				if (owner.equals(level.p1)) {
					sprout.setImage(Consts.atlas.findRegion("sprout"));
				} else {
					sprout.setImage(Consts.atlas.findRegion("sprout_red"));
				}
			}
		}
		
		if (drawSlash) {
			slash.update();
			if (slash.getTimesPlayed() > 0) {
				slash.reset();
				drawSlash = false;
				if (state == State.TREE || state == State.TRUNK) {
					for (int i = 0; i < 20; i++) {
						Particle p = level.particlePool.obtain();
						p.setType(Particle.Type.WOOD);
						p.x = xDraw() + 16;
						p.y = 96;
						level.particles.add(p);
					}
					if (state == State.TREE) {
						for (int i = 0; i < 40; i++) {
							Particle p = level.particlePool.obtain();
							p.setType(owner.player == 1 ? Particle.Type.LEAF_GREEN : Particle.Type.LEAF_RED);
							p.x = xDraw() + 16;
							p.y = 128;
							level.particles.add(p);
						}
					}
				}
				if (state == State.TREE) {
					setState(this.owner, State.TRUNK);
				}
				else if (state == State.TRUNK) {
					setState(this.owner, State.EMPTY);
				}
				Consts.chop.play();
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		switch (state) {
		case SPROUT:
			sprout.draw(batch, xDraw(), 96);
			break;
		case TREE:
			tree.drawOrigin(batch, xDraw(), 96);
			break;
		case TRUNK:
			trunk.draw(batch, xDraw(), 96);
			break;
		default:
			break;
		}
		
		if (drawSlash) {
			slash.drawOrigin(batch, xDraw(), 96);
		}
		
		if (golden) {
			soil.setColor(1, 1, 0);
			soilTop.setColor(1, 1, 0);
		}
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				soilTop.draw(batch, xDraw(), i * 32);
			} else {
				soil.draw(batch, xDraw(), i * 32);
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
		case SPROUT:
			if (owner.equals(level.p1)) {
				sprout.setImage(Consts.atlas.findRegion("sprout_open"), 32);
			} else {
				sprout.setImage(Consts.atlas.findRegion("sprout_open_red"), 32);
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
	
	public void applyAction(Player player, Player.Actions action) {
		switch (action) {
		case AXE:
			break;
		case MOVE_LEFT:
		case MOVE_RIGHT:
		case NULL:
			break;
		case SPROUT:
			break;
		case WATER:
			break;
		}
	}
	
	public int getPointValue() {
		int v = 0;
		if (state == State.SPROUT) v = 1;
		if (state == State.TREE) v = 2;
		if (golden) v *= 2;
		return v;
	}
}
