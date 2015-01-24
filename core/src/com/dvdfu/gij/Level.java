package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.Player.Actions;

public class Level {
	final int width;
	int turn;
	boolean movePhase;
	Player p1;
	Player p2;
	Cell[] cells;
	int performTimer;

	public Level() {
		p1 = new Player(this);
		p2 = new Player(this);
		p1.setPlayer(1);
		p2.setPlayer(1);
		width = 5;
		cells = new Cell[width];
		for (int i = 0; i < width; i++) {
			cells[i] = new Cell(this, i);
		}
		movePhase = true;
	}

	public void update() {
		p1.update();
		if (movePhase) {
			if (p1.ready) movePhase = false;
		}
		if (!movePhase) {
			while (!p1.moveQueue.isEmpty()) {
				perform(p1, p1.moveQueue.removeFirst());
			}
			movePhase = true;
			p1.newRound();
			turn++;
		}
	}
	
	public void perform(Player player, Actions move) {
		switch (move) {
		case MOVE_RIGHT:
			player.xCell++;
			break;
		case MOVE_LEFT:
			player.xCell--;
			break;
		default:
			break;
		}
	}
	
	public void finishPerform(Player player, Actions move) {
		
	}

	public void draw(SpriteBatch batch) {
		for (int i = 0; i < width; i++) {
			cells[i].draw(batch);
		}
		p1.draw(batch);
//		p2.draw(batch);
	}
}
