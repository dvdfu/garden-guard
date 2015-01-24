package com.dvdfu.gij;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.dvdfu.gij.components.GamepadComponent;
import com.dvdfu.gij.components.SpriteComponent;

public class Level {
	enum State {
		ROUND_TEXT, GET_READY_TEXT, QUEUING, GARDEN_TEXT, PERFORMING, WAITING, COUNTING
	}
	State state;
	int stateTimer;
	final int width = 10;
	int turn;
	int turnMove;
	Player p1;
	Player p2;
	Player pT;
	Cell[] cells;
	GamepadComponent gp;
	Text screenLabel;
	Text incLabel;
	SpriteComponent instr;
	
	Array<Water> water;
	Pool<Water> waterPool;

	public Level() {
		p1 = new Player(this, 1);
		p2 = new Player(this, 2);
		pT = p1;
		screenLabel = new Text();
		screenLabel.centered = true;
		incLabel = new Text("+1");
		incLabel.centered = true;
//		screenLabel.font.scale(1);
//		screenLabel.bordered = false;
		cells = new Cell[width];
		for (int i = 0; i < width; i++) {
			cells[i] = new Cell(this, i);
		}
		switchState(State.ROUND_TEXT);
		gp = new GamepadComponent();
		water = new Array<Water>();
		waterPool = new Pool<Water>() {
			protected Water newObject() {
				return new Water();
			}
		};
		instr = new SpriteComponent(Consts.atlas.findRegion("instr"));
	}
	
	public void switchState(State state) {
		this.state = state;
		switch (state) {
		case COUNTING:
			boolean fruit = false;
			Cell cell;
			for (int i = 0; i < width; i++) {
				cell = cells[i];
				if (cell.state == Cell.State.TREE) {
					fruit = true;
					if (cell.owner.equals(p1)) {
						p1.fruit++;
					} else {
						p2.fruit++;
					}
				}
			}
			p1.fruitsText.text = "" + p1.fruit;
			p2.fruitsText.text = "" + p2.fruit;
			if (fruit) {
				stateTimer = 120;
			} else {
				switchState(State.ROUND_TEXT);
			}
			break;
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
			turnMove = 0;
			break;
		case QUEUING:
			break;
		case ROUND_TEXT:
			turn++;
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
	}
	
	public void handleState() {
		switch (state) {
		case COUNTING:
			handleTimer();
			break;
		case GET_READY_TEXT:
			handleTimer();
			break;
		case GARDEN_TEXT:
			handleTimer();
			break;
		case PERFORMING:
			if (turnMove >= movesThisTurn() * 2 && p1.ready && p2.ready) {
				switchState(State.COUNTING);
				return;
			}
			if (pT.ready) {
				if (turnMove / 2 > pT.actionQueue.size()) {
					switchPlayer();
				} else {
					perform(pT, pT.actionQueue.get(turnMove / 2));
					turnMove++;
				}
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
		case COUNTING:
			switchState(State.ROUND_TEXT);
			break;
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
		for (int i = 0; i < water.size; i++) {
			water.get(i).update();
			if (water.get(i).dead) {
				waterPool.free(water.get(i));
				water.removeIndex(i);
				i--;
			}
		}
		p1.update();
		p2.update();
		gp.update();
	}
	
	public void perform(Player player, Player.Actions action) {
		switch (action) {
		case AXE:
			player.startAction(action);
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
			player.startAction(action);
			break;
		case SPROUT:
			player.startAction(action);
			break;
		case WATER:
			player.startAction(action);
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
		if (state == State.COUNTING) {
			for (int i = 0; i < width; i++) {
				if (cells[i].state == Cell.State.TREE) {
					if (cells[i].owner.equals(p1)) {
						incLabel.color.set(0.5f, 1, 0.7f, 1);
					} else {
						incLabel.color.set(1, 0.7f, 0.5f, 1);
					}
					incLabel.draw(batch, Consts.width / 2 + (i + 0.5f - width / 2) * 32, 176);
				}
			}
		}
		if (state == State.QUEUING) {
			// instr.drawCentered(batch, Consts.width / 2, Consts.height / 2);
		}
		for (Water w : water) {
			w.draw(batch);
		}
		p1.draw(batch);
		p2.draw(batch);
	}
	
	public int movesThisTurn() {
		return turn + 2;
	}
}
