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
	final int size = 9;
	int turnWin = 6;
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
	
	Array<Particle> particles;
	Pool<Particle> particlePool;
	
	SpriteComponent buttonTri;
	SpriteComponent buttonCir;
	SpriteComponent buttonCro;
	SpriteComponent buttonDPad;
	SpriteComponent buttonL;
	SpriteComponent buttonR;
	
	boolean celebration;

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
		cells = new Cell[size];
		for (int i = 0; i < size; i++) {
			cells[i] = new Cell(this, i);
		}
		cells[0].setState(p1, Cell.State.SPROUT);
		cells[size - 1].setState(p2, Cell.State.SPROUT);
		switchState(State.ROUND_TEXT);
		gp = new GamepadComponent();
		particles = new Array<Particle>();
		particlePool = new Pool<Particle>() {
			protected Particle newObject() {
				return new Particle();
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
			Consts.count.play();
			boolean fruit = false;
			Cell cell;
			for (int i = 0; i < size; i++) {
				cell = cells[i];
				if (cell.state == Cell.State.TREE || cell.state == Cell.State.SPROUT) {
					fruit = true;
					cell.owner.fruit += cell.getPointValue();
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
			Consts.round.play();
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
				celebration = true;
			} else if (diff == 0) {
				screenLabel.text = "DRAW GAME!";
			} else {
				screenLabel.text = "PLAYER 2 WINS!";
				celebration = true;
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
					pT.startAction(pT.actionQueue.get(turnMove / 2));
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
		if (celebration) {
			Particle p = particlePool.obtain();
			p.setType(Particle.Type.STAR);
			p.x = Consts.width / 2;
			p.y = Consts.height / 2 + 128;
			particles.add(p);
		}
		for (int i = 0; i < particles.size; i++) {
			particles.get(i).update();
			if (particles.get(i).dead) {
				particlePool.free(particles.get(i));
				particles.removeIndex(i);
				i--;
			}
		}
		for (int i = 0; i < size; i++) {
			cells[i].update();
		}
		p1.update();
		p2.update();
		gp.update();
	}
	
	public void switchPlayer() {
		if (pT.equals(p1)) {
			pT = p2;
			return;
		}
		pT = p1;
	}

	public void draw(SpriteBatch batch) {
		for (int i = 0; i < size; i++) {
			cells[i].draw(batch);
		}
		for (Particle p : particles) {
			p.draw(batch);
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
		if (state == State.PERFORMING) {
			if (pT.equals(p1)) {
				screenLabel.color.set(0.5f, 1, 0.7f, 1);
			} else {
				screenLabel.color.set(1, 0.7f, 0.5f, 1);
			}
			screenLabel.text = "P" + pT.player + " used " + pT.actionCurrent.toString();
			screenLabel.draw(batch, Consts.width / 2, Consts.height / 2 + 128);
			screenLabel.color.set(1, 1, 1, 1);
			if (pT.useless) {
				screenLabel.text = "(it was useless)";
				screenLabel.draw(batch, Consts.width / 2, Consts.height / 2 + 112);
			}
		}
		if (state == State.COUNTING) {
			for (int i = 0; i < size; i++) {
				Cell c = cells[i];
				if (c.state == Cell.State.TREE || c.state == Cell.State.SPROUT) {
					if (c.owner.equals(p1)) {
						incLabel.color.set(0.5f, 1, 0.7f, 1);
					} else {
						incLabel.color.set(1, 0.7f, 0.5f, 1);
					}
					incLabel.text = "+" + c.getPointValue();
					incLabel.draw(batch, Consts.width / 2 + (i - size / 2) * 32, 176 - stateTimer / 6);
				}
			}
		}
	}
	
	public int movesThisTurn() {
		return turn + 2;
	}
}
