package com.dvdfu.gij;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.Player.Actions;
import com.dvdfu.gij.components.GamepadComponent;

public class Level {
	enum State {
		ROUND_TEXT, GET_READY_TEXT, QUEUING, GO_TEXT, PERFORMING, WAITING
	}
	State state;
	int stateTimer;
	final int width;
	int turn;
	Player p1;
	Player p2;
	Cell[] cells;
	GamepadComponent gp;
	Text screenLabel;

	public Level() {
		p1 = new Player(this);
		p2 = new Player(this);
		screenLabel = new Text();
		screenLabel.centered = true;
		screenLabel.font.scale(1);
		screenLabel.bordered = false;
		p1.player = 1;
		p2.player = 2;
		width = 10;
		cells = new Cell[width];
		for (int i = 0; i < width; i++) {
			cells[i] = new Cell(this, i);
		}
		switchState(State.ROUND_TEXT);
		turn = 1;
		gp = new GamepadComponent();
	}
	
	public void switchState(State state) {
		switch (state) {
		case GET_READY_TEXT:
			stateTimer = 30;
			screenLabel.text = "GET READY!";
			break;
		case GO_TEXT:
			stateTimer = 30;
			screenLabel.text = "GO!!!";
			break;
		case PERFORMING:
			stateTimer = 30;
			break;
		case QUEUING:
			break;
		case ROUND_TEXT:
			stateTimer = 60;
			screenLabel.text = "Round " + turn;
			break;
		case WAITING:
			break;
		default:
			break;
		}
		this.state = state;
	}
	
	public void timeUp() {
		switch (state) {
		case GET_READY_TEXT:
			switchState(State.QUEUING);
			break;
		case GO_TEXT:
			switchState(State.PERFORMING);
			break;
		case PERFORMING:
			break;
		case QUEUING:
			break;
		case ROUND_TEXT:
			switchState(State.GET_READY_TEXT);
			break;
		case WAITING:
			break;
		default:
			break;
		}
	}

	public void update() {
		p1.update();
		p2.update();
		if (stateTimer > 0) {
			stateTimer--;
		} else {
			timeUp();
		}
		if (state == State.QUEUING) {
			if (p1.ready && p2.ready) switchState(State.GO_TEXT);
		}
		if (state == State.PERFORMING) {
//			if (stateTimer == 0) {
//				if (!p1.moveQueue.isEmpty()) {
//					perform(p1, p1.moveQueue.removeFirst());
//					// perform(p2, p2.moveQueue.removeFirst());
//					stateTimer = 30;
//				} else {
//					switchState(State.QUEUING);
//					stateTimer = 0;
//					p1.newRound();
//					p2.newRound();
//					turn++;
//					screenLabel.text = "Round " + turn;
//				}
//			} else {
//				timer--;
//				if (timer == 0) {
//					finishPerform(p1);
//					finishPerform(p2);
//				}
//			}
//			if (performing) {
//				handlePerform(p1);
//				handlePerform(p2);
//			}
		}
		gp.update();
	}
	
	public void handlePerform(Player player) {
		switch (player.movePerform) {
		case AXE:
			break;
		case WATER:
			break;
		case SPROUT:
			break;
		case MOVE_LEFT:
//			player.x = MathUtils.lerp((player.xCell + 1) * 32, (player.xCell) * 32, (30 - timer) / 30f);
			break;
		case MOVE_RIGHT:
//			player.x = MathUtils.lerp((player.xCell - 1) * 32, (player.xCell) * 32, (30 - timer) / 30f);
			break;
		default:
			break;
		}
	}
	
	public void perform(Player player, Actions move) {
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
	
	public void finishPerform(Player player) {
		
	}

	public void draw(SpriteBatch batch) {
		screenLabel.draw(batch, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
		for (int i = 0; i < width; i++) {
			cells[i].draw(batch);
		}
		p1.draw(batch);
		p2.draw(batch);
	}
}
