package com.dvdfu.gij;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.gij.components.GamepadComponent;
import com.dvdfu.gij.components.GamepadComponent.Button;
import com.dvdfu.gij.components.SpriteComponent;

public class Player {
	Level level;
	enum Actions {
		MOVE_LEFT, MOVE_RIGHT, AXE, WATER, SPROUT, NULL
	}
	boolean ready;
	LinkedList<Actions> actionQueue;
	Actions actionCurrent;
	int timer;
	int xCell;
	public float x;
	int player;
	int fruit;
	final int moveTime = 40;
	float cursorTime;
	boolean useless;
	
	SpriteComponent sprite;
	SpriteComponent iconUnknown;
	SpriteComponent iconMoveLeft;
	SpriteComponent iconMoveRight;
	SpriteComponent iconSprout;
	SpriteComponent iconPublic;
	SpriteComponent iconSelected;
	SpriteComponent iconWater;
	SpriteComponent iconAxe;
	
	Button keyRight;
	Button keyLeft;
	Button keySprout;
	Button keyAxe;
	Button keyWater;
	Button keyUndo;
	Button keyReady;
	
	Text t;
	Text fruitsText;
	
	public Player(Level level, int player) {
		this.level = level;
		this.player = player;
		sprite = new SpriteComponent(Consts.atlas.findRegion("hand"));
		fruitsText = new Text();
		fruitsText.font = Consts.SmallFont;
		fruitsText.centered = true;
		fruitsText.text = "0";
		if (player == 1) {
			sprite.setColor(0.5f, 1, 0.7f);
			fruitsText.color.set(0.5f, 1, 0.7f, 1);
			xCell = 2;
		} else {
			sprite.setColor(1, 0.7f, 0.5f);
			fruitsText.color.set(1, 0.7f, 0.5f, 1);
			xCell = level.width - 3;
		}
		iconUnknown = new SpriteComponent(Consts.atlas.findRegion("icon_unknown"));
		iconMoveLeft = new SpriteComponent(Consts.atlas.findRegion("icon_move_left"));
		iconMoveRight = new SpriteComponent(Consts.atlas.findRegion("icon_move_right"));
		iconSprout = new SpriteComponent(Consts.atlas.findRegion("icon_sprout"));
		iconWater = new SpriteComponent(Consts.atlas.findRegion("icon_water"));
		iconAxe = new SpriteComponent(Consts.atlas.findRegion("icon_axe"));
		iconPublic = new SpriteComponent(Consts.atlas.findRegion("icon_public"));
		iconSelected = new SpriteComponent(Consts.atlas.findRegion("icon_selected"));
		actionQueue = new LinkedList<Actions>();
		t = new Text();
		t.font = Consts.SmallFont;
		t.centered = true;
		keyRight = GamepadComponent.Button.RIGHT;
		keyLeft = GamepadComponent.Button.LEFT;
		keySprout = GamepadComponent.Button.TRI;
		keyAxe = GamepadComponent.Button.CRO;
		keyWater = GamepadComponent.Button.CIR;
		keyUndo = GamepadComponent.Button.L;
		keyReady = GamepadComponent.Button.R;
		actionCurrent = Actions.NULL;
		x = Consts.width / 2 + (xCell - level.width / 2f) * 32;
	}
	
	public void update() {
		if (cursorTime > 1) {
			cursorTime--;
		} else {
			cursorTime += 0.01f;
		}
		t.text = "P" + player + " Ready!";
		switch (level.state) {
		case GARDEN_TEXT:
			break;
		case PERFORMING:
			if (!ready) handleAction();
			break;
		case QUEUING:
			handleInput();
			break;
		case ROUND_TEXT:
			break;
		case VICTORY_TEXT:
			break;
		default:
			break;
		}
	}
	
	public void handleInput() {
		if (player == 1) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
				addMove(Actions.MOVE_RIGHT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
				addMove(Actions.MOVE_LEFT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
				addMove(Actions.SPROUT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
				addMove(Actions.AXE);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
				addMove(Actions.WATER);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
				removeMove();
			}
			if (doneMoves() && Gdx.input.isKeyJustPressed(Input.Keys.W)) {
				ready = true;
				Consts.ready.play();
			}
		} else {
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				addMove(Actions.MOVE_RIGHT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				addMove(Actions.MOVE_LEFT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				addMove(Actions.SPROUT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH)) {
				addMove(Actions.AXE);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.SEMICOLON)) {
				addMove(Actions.WATER);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
				removeMove();
			}
			if (doneMoves() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
				ready = true;
				Consts.ready.play();
			}
		}
//		if (level.gp.keyPressed(player - 1, keyRight)) {
//			addMove(Actions.MOVE_RIGHT);
//		}
//		if (level.gp.keyPressed(player - 1, keyLeft)) {
//			addMove(Actions.MOVE_LEFT);
//		}
//		if (level.gp.keyPressed(player - 1, keySprout)) {
//			addMove(Actions.SPROUT);
//		}
//		if (level.gp.keyPressed(player - 1, keyAxe)) {
//			addMove(Actions.AXE);
//		}
//		if (level.gp.keyPressed(player - 1, keyWater)) {
//			addMove(Actions.WATER);
//		}
//		if (level.gp.keyPressed(player - 1, keyUndo)) {
//			removeMove();
//		}
//		if (doneMoves() && level.gp.keyPressed(player - 1, keyReady)) {
//			ready = true;
//			Consts.ready.play();
//		}
	}
	
	public void newRound() {
		ready = false;
		actionQueue.clear();
	}
	
	public void addMove(Actions move) {
		if (doneMoves()) return;
		Consts.select.play();
		actionQueue.add(move);
	}
	
	public void removeMove() {
		if (actionQueue.size() <= level.movesThisTurn() / 2) return;
		if (ready) return;
		if (actionQueue.isEmpty()) return;
		Consts.undo.play();
		actionQueue.removeLast();
	}
	
	public boolean doneMoves() {
		return actionQueue.size() == level.movesThisTurn();
	}
	
	public void startAction(Actions action) {
		ready = false;
		actionCurrent = action;
		Cell cell = level.cells[xCell];
		switch (action) {
		case AXE:
			useless = true;
			cell.slash();
			if (cell.state == Cell.State.TREE) {
				if (cell.owner.xCell != cell.x || cell.owner.equals(this)) {
					useless = false;
					int i = cell.x - 1;
					while (i >= 0 && level.cells[i].state == Cell.State.TREE && level.cells[i].owner == cell.owner) {
						level.cells[i].slash();
						i--;
					}
					i = cell.x + 1;
					while (i < level.width && level.cells[i].state == Cell.State.TREE && level.cells[i].owner == cell.owner) {
						level.cells[i].slash();
						i++;
					}
				}
			} else if (cell.state == Cell.State.TRUNK) {
				cell.slash();
				int i = cell.x - 1;
				while (i >= 0 && level.cells[i].state == Cell.State.TRUNK) {
					level.cells[i].slash();
					i--;
				}
				i = cell.x + 1;
				while (i < level.width && level.cells[i].state == Cell.State.TRUNK) {
					level.cells[i].slash();
					i++;
				}
			}
			timer = moveTime;
			break;
		case WATER:
			useless = true;
			if (cell.state == Cell.State.SPROUT) {
				useless = false;
			}
			Cell oCell;
			if (cell.x > 0) {
				oCell = level.cells[cell.x - 1];
				if (oCell.state == Cell.State.SPROUT) {
					useless = false;
				}
			}
			if (cell.x < level.width - 1) {
				oCell = level.cells[cell.x + 1];
				if (oCell.state == Cell.State.SPROUT) {
					useless = false;
				}
			}
			timer = moveTime;
			break;
		case SPROUT:
			useless = true;
			if (cell.state != Cell.State.TREE && cell.state != Cell.State.TRUNK) {
				useless = false;
				cell.setState(this, Cell.State.SPROUT);
			}
			timer = moveTime;
			break;
		case MOVE_LEFT:
			useless = xCell <= 0;
			timer = moveTime;
			break;
		case MOVE_RIGHT:
			useless = xCell > level.width - 2;
			timer = moveTime;
			break;
		case NULL:
			level.switchPlayer();
			ready = true;
			break;
		default:
			break;
		}
	}
	
	public void handleAction() {
		Cell cell = level.cells[xCell];
		switch (actionCurrent) {
		case WATER:
			if (timer == 0) {
				level.switchPlayer();
				ready = true;
			} else {
				if (timer == moveTime / 2) {
					if (!useless) {
						Consts.tree.play();
					}
					if (cell.state == Cell.State.SPROUT) {
						cell.setState(cell.owner, Cell.State.TREE);
					}
					Cell oCell;
					if (cell.x > 0) {
						oCell = level.cells[cell.x - 1];
						if (oCell.state == Cell.State.SPROUT) {
							oCell.setState(oCell.owner, Cell.State.TREE);
						}
					}
					if (cell.x < level.width - 1) {
						oCell = level.cells[cell.x + 1];
						if (oCell.state == Cell.State.SPROUT) {
							oCell.setState(oCell.owner, Cell.State.TREE);
						}
					}
				}
				if (timer % 5 == 0) {
					long id = Consts.water.play();
					Consts.water.setPitch(id, MathUtils.lerp(0.5f, 1f, 1f * timer / moveTime));
				}
				timer--;
				for (int i = 0; i < 2; i++) {
					Water w = level.waterPool.obtain();
					w.x = x + 8 + MathUtils.random(16);
					w.y = 192;
					level.water.add(w);
				}
			}
			break;
		case AXE:
			if (timer == 0) {
				level.switchPlayer();
				ready = true;
			} else {
				timer--;
				if (cell.state == Cell.State.TREE) {
					for (int i = 0; i < 2; i++) {
						Leaf l = level.leafPool.obtain();
						l.x = x + 16;
						l.y = 128;
						l.setPlayer(cell.owner.player);
						level.leaves.add(l);
					}
				}
			}
			break;
		case SPROUT:
			if (timer == 0) {
				level.switchPlayer();
				ready = true;
			} else {
				timer--;
			}
			break;
		case MOVE_LEFT:
			if (timer == 0) {
				level.switchPlayer();
				ready = true;
				if (!useless) {
					xCell--;
					x = Consts.width / 2 + (xCell - level.width / 2f) * 32;
				}
			} else {
				timer--;
				if (!useless) {
					float tx = MathUtils.lerp(xCell, xCell - 1, 1f * (moveTime - timer) / moveTime);
					x = Consts.width / 2 + (tx - level.width / 2f) * 32;
				}
			}
			break;
		case MOVE_RIGHT:
			if (timer == 0) {
				level.switchPlayer();
				ready = true;
				if (!useless) {
					xCell++;
					x = Consts.width / 2 + (xCell - level.width / 2f) * 32;
				}
			} else {
				timer--;
				if (!useless) {
					float tx = MathUtils.lerp(xCell, xCell + 1, 1f * (moveTime - timer) / moveTime);
					x = Consts.width / 2 + (tx - level.width / 2f) * 32;
				}
			}
			break;
		default:
			break;
		}
	}
	
	public void draw(SpriteBatch batch) {
		float yOffset = (player == 1 && xCell == level.p2.xCell)? 40 : 0;
		fruitsText.draw(batch, x + 16, 192 + yOffset + 40);
		yOffset += 4 * MathUtils.sin(cursorTime * MathUtils.PI2);
		sprite.draw(batch, x, 192 + yOffset);

		int xOffset = player == 1? 160: Consts.width - 160;
		yOffset = Consts.height - 2 - 24;
		if ((level.state == Level.State.GARDEN_TEXT || level.state == Level.State.QUEUING) && ready) {
			t.draw(batch, xOffset + (player == 1 ? -64 : 64), yOffset - actionQueue.size() * 26  + 13);
		}
		for (int i = 0; i < level.movesThisTurn(); i++) {
			if (i < level.movesThisTurn() / 2) {
				iconPublic.draw(batch, xOffset - iconPublic.getWidth() / 2, yOffset - iconPublic.getHeight() - i * 26);
			} else {
				iconSelected.draw(batch, xOffset - iconSelected.getWidth() / 2, yOffset - iconSelected.getHeight() - i * 26);
			}
		}
		
		for (int i = 0; i < actionQueue.size(); i++) {
			SpriteComponent icon = iconUnknown;
			if ((level.state != Level.State.QUEUING && level.state != Level.State.GARDEN_TEXT) || i < level.movesThisTurn() / 2) {
				switch (actionQueue.get(i)) {
				case AXE:
					icon = iconAxe;
					break;
				case WATER:
					icon = iconWater;
					break;
				case SPROUT:
					icon = iconSprout;
					break;
				case MOVE_LEFT:
					icon = iconMoveLeft;
					break;
				case MOVE_RIGHT:
					icon = iconMoveRight;
					break;
				default:
					break;
				}
			}
			
			icon.draw(batch, xOffset - icon.getWidth() / 2, yOffset - icon.getHeight() - i * 26);
		}
	}
}
