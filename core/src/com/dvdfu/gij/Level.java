package com.dvdfu.gij;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.GamepadComponent;

public class Level {
	enum State {
		ROUND_TEXT, GET_READY_TEXT, QUEUING, GARDEN_TEXT, PERFORMING, WAITING
	}
	State state;
	int stateTimer;
	final int width = 10;
	int turn;
	Player p1;
	Player p2;
	Player pT;
	Cell[] cells;
	GamepadComponent gp;
	Text screenLabel;

	public Level() {
		p1 = new Player(this, 1);
		p2 = new Player(this, 2);
		pT = p1;
		screenLabel = new Text();
		screenLabel.centered = true;
//		screenLabel.font.scale(1);
//		screenLabel.bordered = false;
		cells = new Cell[width];
		for (int i = 0; i < width; i++) {
			cells[i] = new Cell(this, i);
		}
		turn = 1;
		switchState(State.ROUND_TEXT);
		gp = new GamepadComponent();
	}
	
	public void switchState(State state) {
		switch (state) {
		case GET_READY_TEXT:
			stateTimer = 60;
			screenLabel.text = "Get Ready...";
			break;
		case GARDEN_TEXT:
			stateTimer = 60;
			screenLabel.text = "Garden!";
			break;
		case PERFORMING:
			stateTimer = 0;
			break;
		case QUEUING:
			break;
		case ROUND_TEXT:
			p1.newRound();
			p2.newRound();
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
	
	public void handleState() {
		switch (state) {
		case GET_READY_TEXT:
			handleTimer();
			break;
		case GARDEN_TEXT:
			handleTimer();
			break;
		case PERFORMING:
			if (pT.ready) {
				if (pT.actionQueue.isEmpty()) {
					switchPlayer();
				} else {
					perform(pT, pT.actionQueue.removeFirst());
					System.out.println("p " + stateTimer);
				}
			}
			if (p1.actionQueue.isEmpty() && p2.actionQueue.isEmpty() && p1.ready && p2.ready) {
				turn++;
				switchState(State.ROUND_TEXT);
				return;
			}
			break;
		case QUEUING:
			if (p1.ready && p2.ready) switchState(State.GARDEN_TEXT);
			break;
		case ROUND_TEXT:
			handleTimer();
			break;
		case WAITING:
			break;
		default:
			break;
		}
	}
	
	public void handleTimer() {
		if (stateTimer > 0) {
			stateTimer--;
		} else {
			timeUp();
		}
	}
	
	public void timeUp() {
		switch (state) {
		case GET_READY_TEXT:
			switchState(State.QUEUING);
			break;
		case GARDEN_TEXT:
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
		handleState();
		p1.update();
		p2.update();
		gp.update();
	}
	
	public void perform(Player player, Player.Actions action) {
		switch (action) {
		case AXE:
			break;
		case MOVE_LEFT:
			if (player.xCell > 0) {
				player.startAction(action);
			} else {
				stateTimer++;
			}
			break;
		case MOVE_RIGHT:
			if (player.xCell < width - 1) {
				player.startAction(action);
			} else {
				stateTimer++;
			}
			break;
		case NULL:
			break;
		case SPROUT:
			break;
		case WATER:
			break;
		default:
			break;
		}
	}
	
	public void switchPlayer() {
		if (pT.equals(p1)) {
			pT = p2;
			return;
		}
		pT = p1;
	}

	public void draw(SpriteBatch batch) {
		if (state == State.GET_READY_TEXT || state == State.GARDEN_TEXT || state == State.ROUND_TEXT) {
			screenLabel.draw(batch, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4 + 80);
		}
		for (int i = 0; i < width; i++) {
			cells[i].draw(batch);
		}
		p1.draw(batch);
		p2.draw(batch);
	}
}
