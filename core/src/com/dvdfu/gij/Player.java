package com.dvdfu.gij;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.GamepadComponent;
import com.dvdfu.gij.components.GamepadComponent.Button;
import com.dvdfu.gij.components.SpriteComponent;

public class Player {
	enum Actions {
		MOVE_LEFT, MOVE_RIGHT, AXE, WATER, SPROUT, NULL
	}
	boolean ready;
	boolean doneMoves;
	LinkedList<Actions> moveQueue;
	Actions movePerform;
	Level level;
	SpriteComponent sprite;
	SpriteComponent iconUnknown;
	SpriteComponent iconMoveLeft;
	SpriteComponent iconMoveRight;
	SpriteComponent iconSprout;
	int xCell;
	float x;
	int timer;
	int player;
	
	Button keyRight;
	Button keyLeft;
	Button keyMove;
	Button keyAttack;
	Button keyGuard;
	Button keyFocus;
	Button keyUndo;
	Button keyReady;
	Text t;
	
	public Player(Level level) {
		this.level = level;
		sprite = new SpriteComponent(Consts.atlas.findRegion("test"));
		iconUnknown = new SpriteComponent(Consts.atlas.findRegion("icon_unknown"));
		iconMoveLeft = new SpriteComponent(Consts.atlas.findRegion("icon_move_left"));
		iconMoveRight = new SpriteComponent(Consts.atlas.findRegion("icon_move_right"));
		iconSprout = new SpriteComponent(Consts.atlas.findRegion("icon_sprout"));
		moveQueue = new LinkedList<Actions>();
		t = new Text();
		t.font = Consts.SmallFont;
		t.centered = true;
		keyRight = GamepadComponent.Button.RIGHT;
		keyLeft = GamepadComponent.Button.LEFT;
		keyMove = GamepadComponent.Button.CIR;
		keyAttack = GamepadComponent.Button.CRO;
		keyGuard = GamepadComponent.Button.SQU;
		keyFocus = GamepadComponent.Button.TRI;
		keyUndo = GamepadComponent.Button.L;
		keyReady = GamepadComponent.Button.R;
	}
	
	public void update() {
		System.out.println(moveQueue);
		t.text = "P" + player + " Ready!";
		if (level.state == Level.State.QUEUING) {
			handleInput();
		}
	}
	
	public void handleInput() {
		if (level.gp.keyDown(player - 1, keyMove)) {
			if (level.gp.keyPressed(player - 1, keyRight)) {
				addMove(Actions.MOVE_RIGHT);
			}
			if (level.gp.keyPressed(player - 1, keyLeft)) {
				addMove(Actions.MOVE_LEFT);
			}
		}
		if (level.gp.keyDown(player - 1, keyAttack)) {
			if (level.gp.keyPressed(player - 1, keyRight)) {
				addMove(Actions.WATER);
			}
			if (level.gp.keyPressed(player - 1, keyLeft)) {
				addMove(Actions.AXE);
			}
		}
		if (level.gp.keyPressed(player - 1, keyFocus)) {
			addMove(Actions.SPROUT);
		}
		if (level.gp.keyPressed(player - 1, keyUndo)) {
			removeMove();
		}
		if (doneMoves && level.gp.keyPressed(player - 1, keyReady)) {
			ready = true;
		}
	}
	
	public void newRound() {
		ready = false;
		doneMoves = false;
		moveQueue.clear();
	}
	
	public void addMove(Actions move) {
		if (doneMoves) return;
		moveQueue.add(move);
		if (moveQueue.size() == level.turn + 2) {
			doneMoves = true;
		}
	}
	
	public void removeMove() {
		if (moveQueue.isEmpty()) return;
		moveQueue.removeLast();
		doneMoves = moveQueue.size() == level.turn + 2;
	}
	
	public void perform(Actions action) {
		switch (action) {
		case AXE:
			break;
		case WATER:
			break;
		case SPROUT:
			break;
		case MOVE_LEFT:
			break;
		case MOVE_RIGHT:
			break;
		default:
			break;
		}
	}
	
	public void draw(SpriteBatch batch) {
		float drawX = Gdx.graphics.getWidth() / 4 + (x - level.width / 2f) * 32;
		
		int xOffset = player == 1? 0: 200;
		sprite.draw(batch, drawX, 160);
		
		for (int i = 0; i < moveQueue.size(); i++) {
			SpriteComponent icon = iconUnknown;
			switch (moveQueue.get(i)) {
			case AXE:
			case WATER:
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
			icon.draw(batch, xOffset, 300 - i * 26);
		}
		
		if (ready) {
			t.draw(batch, xOffset, 160);
		}
	}
}
