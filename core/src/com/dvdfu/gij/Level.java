package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.gij.Player.Actions;
import com.dvdfu.gij.components.GamepadComponent;

public class Level {
	enum State {
		ROUND_ANNOUNCEMENT, PERFORM
	}
	State state;
	final int width;
	int turn;
	boolean movePhase;
	Player p1;
	Player p2;
	Cell[] cells;
	int timer;
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
		switchState(State.ROUND_ANNOUNCEMENT);
	}
	
	public void switchState(State state) {
		switch (state) {
		case ROUND_ANNOUNCEMENT:
			timer = 30;
			break;
		default:
			break;
		
		}
		this.state = state;
	}
	
	public void handleState() {
		
	}
	
	public void timer() {
		switch (state) {
		case PERFORM:
			break;
		case ROUND_ANNOUNCEMENT:
			break;
		default:
			break;
		}
	}

	public void update() {
		p1.update();
		p2.update();
		if (movePhase) {
			if (p1.ready && p2.ready) movePhase = false;
		}
		if (!movePhase) {
			if (timer == 0) {
				if (!p1.moveQueue.isEmpty()) {
					perform(p1, p1.moveQueue.removeFirst());
					perform(p2, p2.moveQueue.removeFirst());
					timer = 30;
				} else {
					movePhase = true;
					timer = 0;
					performing = false;
					p1.newRound();
					p2.newRound();
					turn++;
					txRound.text = "Round " + turn;
				}
			} else {
				timer--;
			}
			if (performing) {
				handlePerform(p1);
				handlePerform(p2);
			}
		}
		gp.update();
	}
	
	public void handlePerform(Player player) {
		switch (player.movePerform) {
		case ATTACK_LEFT:
			break;
		case ATTACK_RIGHT:
			break;
		case FOCUS:
			break;
		case GUARD:
			break;
		case MOVE_LEFT:
			player.x = MathUtils.lerp((player.xCell + 1) * 32, (player.xCell) * 32, (30 - timer) / 30f);
			break;
		case MOVE_RIGHT:
			player.x = MathUtils.lerp((player.xCell - 1) * 32, (player.xCell) * 32, (30 - timer) / 30f);
			break;
		default:
			break;
		}
	}
	
	public void perform(Player player, Actions move) {
		performing = true;
		player.movePerform = move;
		switch (move) {
		case MOVE_RIGHT:
			if (player.xCell < width - 1) {
				player.xCell++;
			} else {
				player.movePerform = Player.Actions.NULL;
			}
			break;
		case MOVE_LEFT:
			if (player.xCell > 0) {
				player.xCell--;
			} else {
				player.movePerform = Player.Actions.NULL;
			}
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
