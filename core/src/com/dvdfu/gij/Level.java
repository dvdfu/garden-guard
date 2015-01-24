package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.Player.Actions;
import com.dvdfu.gij.components.GamepadComponent;

public class Level {
	final int width;
	int turn;
	boolean movePhase;
	Player p1;
	Player p2;
	Cell[] cells;
	int performTimer;
	boolean performing;
	Text txRound;
	GamepadComponent gp;

	public Level() {
		p1 = new Player(this);
		p2 = new Player(this);
		p1.player = 1;
		p2.player = 2;
		width = 5;
		cells = new Cell[width];
		for (int i = 0; i < width; i++) {
			cells[i] = new Cell(this, i);
		}
		movePhase = true;
		txRound = new Text();
		gp = new GamepadComponent();
	}

	public void update() {
		p1.update();
		p2.update();
		if (movePhase) {
			if (p1.ready && p2.ready) movePhase = false;
		}
		if (!movePhase) {
			if (!p1.moveQueue.isEmpty()) {
				perform(p1, p1.moveQueue.removeFirst());
				perform(p2, p2.moveQueue.removeFirst());
			}
			movePhase = true;
			p1.newRound();
			p2.newRound();
			turn++;
			txRound.text = "Round " + turn;
		}
		gp.update();
	}
	
	public void perform(Player player, Actions move) {
		performTimer = 0;
		performing = true;
		switch (move) {
		case MOVE_RIGHT:
			if (player.xCell < width - 1) player.xCell++;
			break;
		case MOVE_LEFT:
			if (player.xCell > 0) player.xCell--;
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
		p2.draw(batch);
	}
}
