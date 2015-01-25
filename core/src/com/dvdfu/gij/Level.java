package com.dvdfu.gij;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.dvdfu.gij.components.GamepadComponent;
import com.dvdfu.gij.components.SpriteComponent;

public class Level {
	public enum State {
		ROUND_TEXT, QUEUING, GARDEN_TEXT, PERFORMING, COUNTING, VICTORY_TEXT
	}
	public State state;
	int stateTimer;
	final int width = 9;
	int turnWin = 8;
	int turn;
	int turnMove;
	public Player p1;
	public Player p2;
	public Player pT;
	Cell[] cells;
	GamepadComponent gp;
	Text screenLabel;
	Text incLabel;
	Text instrLabel;
	
	Array<Water> water;
	Pool<Water> waterPool;
	Array<Leaf> leaves;
	Pool<Leaf> leafPool;
	Array<Wood> wood;
	Pool<Wood> woodPool;
	
	SpriteComponent buttonTri;
	SpriteComponent buttonCir;
	SpriteComponent buttonCro;
	SpriteComponent buttonDPad;
	SpriteComponent buttonL;
	SpriteComponent buttonR;

	public Level() {
		p1 = new Player(this, 1);
		p2 = new Player(this, 2);
		pT = p1;
		screenLabel = new Text();
		screenLabel.centered = true;
		incLabel = new Text("+1");
		incLabel.centered = true;
		incLabel.font = Consts.SmallFont;
		instrLabel = new Text();
		instrLabel.font = Consts.SmallFont;
		// screenLabel.font.scale(1);
		// screenLabel.bordered = false;
		cells = new Cell[width];
		for (int i = 0; i < width; i++) {
			cells[i] = new Cell(this, i);
		}
		cells[0].setState(p1, Cell.State.SPROUT);
		cells[width - 1].setState(p2, Cell.State.SPROUT);
		switchState(State.ROUND_TEXT);
		gp = new GamepadComponent();
		water = new Array<Water>();
		waterPool = new Pool<Water>() {
			protected Water newObject() {
				return new Water();
			}
		};
		leaves = new Array<Leaf>();
		leafPool = new Pool<Leaf>() {
			protected Leaf newObject() {
				return new Leaf();
			}
		};
		wood = new Array<Wood>();
		woodPool = new Pool<Wood>() {
			protected Wood newObject() {
				return new Wood();
			}
		};
		
		buttonTri = new SpriteComponent(Consts.atlas.findRegion("button_tri"));
		buttonCir = new SpriteComponent(Consts.atlas.findRegion("button_cir"));
		buttonCro = new SpriteComponent(Consts.atlas.findRegion("button_cro"));
		buttonDPad = new SpriteComponent(Consts.atlas.findRegion("button_dpad"));
		buttonL = new SpriteComponent(Consts.atlas.findRegion("button_l1"));
		buttonR = new SpriteComponent(Consts.atlas.findRegion("button_r1"));
	}
	
	public void switchState(State state) {
		this.state = state;
		switch (state) {
		case COUNTING:
			boolean fruit = false;
			Cell cell;
			for (int i = 0; i < width; i++) {
				cell = cells[i];
				if (cell.state == Cell.State.TREE || cell.state == Cell.State.SPROUT) {
					fruit = true;
					cell.owner.fruit += cell.getValue();
				}
			}
			p1.fruitsText.text = "" + p1.fruit;
			p2.fruitsText.text = "" + p2.fruit;
			if (fruit) {
				stateTimer = 120;
			} else {
				if (turn == turnWin) {
					switchState(State.VICTORY_TEXT);
				} else {
					switchState(State.ROUND_TEXT);
				}
			}
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
			stateTimer = 80;
			if (turn == turnWin) {
				screenLabel.text = "FINAL ROUND!";
			} else {
				screenLabel.text = "ROUND " + turn + " / " + turnWin;
			}
			break;
		case VICTORY_TEXT:
			int diff = p1.fruit - p2.fruit;
			if (diff > 0) {
				screenLabel.text = "PLAYER 1 WINS!";
			} else if (diff == 0) {
				screenLabel.text = "DRAW GAME!";
			} else {
				screenLabel.text = "PLAYER 2 WINS!";
			}
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
		case VICTORY_TEXT:
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
			if (turn == turnWin) {
				switchState(State.VICTORY_TEXT);
			} else {
				switchState(State.ROUND_TEXT);
			}
			break;
		case GARDEN_TEXT:
			switchState(State.PERFORMING);
			break;
		case PERFORMING:
			break;
		case QUEUING:
			break;
		case ROUND_TEXT:
			switchState(State.QUEUING);
			break;
		case VICTORY_TEXT:
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
		for (int i = 0; i < leaves.size; i++) {
			leaves.get(i).update();
			if (leaves.get(i).dead) {
				leafPool.free(leaves.get(i));
				leaves.removeIndex(i);
				i--;
			}
		}
		for (int i = 0; i < wood.size; i++) {
			wood.get(i).update();
			if (wood.get(i).dead) {
				woodPool.free(wood.get(i));
				wood.removeIndex(i);
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
		for (int i = 0; i < width; i++) {
			cells[i].draw(batch);
		}
		for (Leaf l : leaves) {
			l.draw(batch);
		}
		for (Wood w : wood) {
			w.draw(batch);
		}
		for (Water w : water) {
			w.draw(batch);
		}
		p1.draw(batch);
		p2.draw(batch);
		if (state == State.QUEUING) {
			int xx = 48;
			instrLabel.color.set(0.5f, 1, 0.7f, 1);
			instrLabel.text = "Sprout";
			instrLabel.draw(batch, xx, 70);
			buttonTri.draw(batch, xx - 18, 70 - 12);
			instrLabel.color.set(1, 0.7f, 0.5f, 1);
			instrLabel.text = "Water";
			instrLabel.draw(batch, xx, 54);
			buttonCir.draw(batch, xx - 18, 54 - 12);
			instrLabel.color.set(0.5f, 0.7f, 1, 1);
			instrLabel.text = "Axe";
			instrLabel.draw(batch, xx, 38);
			buttonCro.draw(batch, xx - 18, 38 - 12);
			
			xx = 128;
			instrLabel.color.set(1, 1, 1, 1);
			instrLabel.text = "Move";
			instrLabel.draw(batch, xx, 70);
			buttonDPad.draw(batch, xx - 18, 70 - 12);
			instrLabel.text = "Undo";
			instrLabel.draw(batch, xx, 54);
			buttonL.draw(batch, xx - 18, 54 - 12);
			instrLabel.text = "Ready";
			instrLabel.draw(batch, xx, 38);
			buttonR.draw(batch, xx - 18, 38 - 12);
		}
		if (state == State.GARDEN_TEXT ||
				state == State.ROUND_TEXT ||
				state == State.VICTORY_TEXT) {
				screenLabel.draw(batch, Consts.width / 2, Consts.height / 2 + 128);
			}
			if (state == State.COUNTING) {
				for (int i = 0; i < width; i++) {
					Cell c = cells[i];
					if (c.state == Cell.State.TREE || c.state == Cell.State.SPROUT) {
						if (c.owner.equals(p1)) {
							incLabel.color.set(0.5f, 1, 0.7f, 1);
						} else {
							incLabel.color.set(1, 0.7f, 0.5f, 1);
						}
						incLabel.text = "+" + c.getValue();
						incLabel.draw(batch, Consts.width / 2 + (i - width / 2) * 32, 176 - stateTimer / 6);
					}
				}
			}
	}
	
	public int movesThisTurn() {
		return turn + 2;
	}
}
