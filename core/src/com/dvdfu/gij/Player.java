package com.dvdfu.gij;

import java.util.LinkedList;

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
	float x;
	int player;
	int fruit;
	final int moveTime = 30;
	
	SpriteComponent sprite;
	SpriteComponent menuTray;
	SpriteComponent iconUnknown;
	SpriteComponent iconMoveLeft;
	SpriteComponent iconMoveRight;
	SpriteComponent iconSprout;
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
	
	public Player(Level level, int player) {
		this.level = level;
		this.player = player;
		sprite = new SpriteComponent(Consts.atlas.findRegion("hand"));
		if (player == 1) {
			sprite.setColor(0.5f, 1, 0.7f);
		} else {
			sprite.setColor(1, 0.7f, 0.5f);
		}
		menuTray = new SpriteComponent(Consts.atlas.findRegion("test"));
		menuTray.setSize(28, 200);
		iconUnknown = new SpriteComponent(Consts.atlas.findRegion("icon_unknown"));
		iconMoveLeft = new SpriteComponent(Consts.atlas.findRegion("icon_move_left"));
		iconMoveRight = new SpriteComponent(Consts.atlas.findRegion("icon_move_right"));
		iconSprout = new SpriteComponent(Consts.atlas.findRegion("icon_sprout"));
		iconWater = new SpriteComponent(Consts.atlas.findRegion("icon_water"));
		iconAxe = new SpriteComponent(Consts.atlas.findRegion("icon_axe"));
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
		xCell = player == 1 ? 3 : level.width - 4;
		x = Consts.width / 2 + (xCell - level.width / 2f) * 32;
	}
	
	public void update() {
		t.text = "P" + player + " Ready!";
		switch (level.state) {
		case GET_READY_TEXT:
			break;
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
		case WAITING:
			break;
		default:
			break;
		}
	}
	
	public void handleInput() {
		if (level.gp.keyPressed(player - 1, keyRight)) {
			addMove(Actions.MOVE_RIGHT);
		}
		if (level.gp.keyPressed(player - 1, keyLeft)) {
			addMove(Actions.MOVE_LEFT);
		}
		if (level.gp.keyPressed(player - 1, keySprout)) {
			addMove(Actions.SPROUT);
		}
		if (level.gp.keyPressed(player - 1, keyAxe)) {
			addMove(Actions.AXE);
		}
		if (level.gp.keyPressed(player - 1, keyWater)) {
			addMove(Actions.WATER);
		}
		if (level.gp.keyPressed(player - 1, keyUndo)) {
			removeMove();
		}
		if (doneMoves() && level.gp.keyPressed(player - 1, keyReady)) {
			ready = true;
		}
	}
	
	public void newRound() {
		ready = false;
		actionQueue.clear();
	}
	
	public void addMove(Actions move) {
		if (doneMoves()) return;
		actionQueue.add(move);
	}
	
	public void removeMove() {
		if (ready) return;
		if (actionQueue.isEmpty()) return;
		actionQueue.removeLast();
	}
	
	public boolean doneMoves() {
		return actionQueue.size() == level.turn + 2;
	}
	
	public void startAction(Actions action) {
		ready = false;
		actionCurrent = action;
		Cell cell = level.cells[xCell];
		switch (action) {
		case AXE:
			timer = moveTime;
			break;
		case WATER:
			timer = moveTime;
			break;
		case SPROUT:
			cell.setState(this, Cell.State.SPROUT);
			timer = moveTime;
			break;
		case MOVE_LEFT:
			timer = moveTime;
			break;
		case MOVE_RIGHT:
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
				if (cell.state == Cell.State.SPROUT) {
					cell.setState(cell.owner, Cell.State.TREE);
				}
				level.switchPlayer();
				ready = true;
			} else {
				timer--;
				Water w = level.waterPool.obtain();
				w.x = x + 8 + MathUtils.random(16);
				w.y = 192;
				level.water.add(w);
			}
			break;
		case AXE:
			if (timer == 0) {
				if (cell.state == Cell.State.TREE) {
					cell.setState(this, Cell.State.EMPTY);
				}
				level.switchPlayer();
				ready = true;
			} else {
				timer--;
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
				xCell--;
				x = Consts.width / 2 + (xCell - level.width / 2f) * 32;
			} else {
				timer--;
				float tx = MathUtils.lerp(xCell, xCell - 1, 1f * (moveTime - timer) / moveTime);
				x = Consts.width / 2 + (tx - level.width / 2f) * 32;
			}
			break;
		case MOVE_RIGHT:
			if (timer == 0) {
				level.switchPlayer();
				ready = true;
				xCell++;
				x = Consts.width / 2 + (xCell - level.width / 2f) * 32;
			} else {
				timer--;
				float tx = MathUtils.lerp(xCell, xCell + 1, 1f * (moveTime - timer) / moveTime);
				x = Consts.width / 2 + (tx - level.width / 2f) * 32;
			}
			break;
		default:
			break;
		}
	}
	
	public void draw(SpriteBatch batch) {
		int xOffset = player == 1? 100: Consts.width - 100;
		if (player == 1 && xCell == level.p2.xCell) {
			sprite.draw(batch, x, 192 + 16);
		} else {
			sprite.draw(batch, x, 192);
		}
		
		int yOffset = (level.turn + 2) * 26 + 2 ;
		menuTray.draw(batch, xOffset - menuTray.getWidth() / 2, Consts.height - yOffset);
		if ((level.state == Level.State.GARDEN_TEXT || level.state == Level.State.QUEUING) && ready) {
			t.draw(batch, xOffset, Consts.height - yOffset - 16);
		}
		
		for (int i = 2; i < level.turn + 2; i++) {
			iconSelected.draw(batch, xOffset - iconSelected.getWidth() / 2, Consts.height - 2 - iconSelected.getHeight() - i * 26);
		}
		
		for (int i = 0; i < actionQueue.size(); i++) {
			SpriteComponent icon = iconUnknown;
			if (level.state == Level.State.PERFORMING || i < 2) {
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
			icon.draw(batch, xOffset - icon.getWidth() / 2, Consts.height - 2 - icon.getHeight() - i * 26);
		}
	}
}
