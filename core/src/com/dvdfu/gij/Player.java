package com.dvdfu.gij;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.gij.components.GamepadComponent;
import com.dvdfu.gij.components.GamepadComponent.Button;
import com.dvdfu.gij.components.SpriteComponent;

public class Player {
	enum Actions {
		MOVE_LEFT, MOVE_RIGHT, ATTACK_LEFT, ATTACK_RIGHT, FOCUS, GUARD
	}
	boolean ready;
	boolean doneMoves;
	LinkedList<Actions> moveQueue;
	Level level;
	SpriteComponent sprite;
	SpriteComponent iconUnknown;
	int xCell;
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
		sprite = new SpriteComponent(Consts.atlas.findRegion("player"));
		iconUnknown = new SpriteComponent(Consts.atlas.findRegion("icon_unknown"));
		moveQueue = new LinkedList<Actions>();
		t = new Text("P1 Ready!");
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
		if (level.movePhase) {
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
				addMove(Actions.ATTACK_RIGHT);
			}
			if (level.gp.keyPressed(player - 1, keyLeft)) {
				addMove(Actions.ATTACK_LEFT);
			}
		}
		if (level.gp.keyPressed(player - 1, keyGuard)) {
			addMove(Actions.GUARD);
		}
		if (level.gp.keyPressed(player - 1, keyFocus)) {
			addMove(Actions.FOCUS);
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
	
	public void draw(SpriteBatch batch) {
		int xOffset = player == 1? 0: 200;
		sprite.draw(batch, xCell * 32, 16);
		for (int i = 0; i < moveQueue.size(); i++) {
			iconUnknown.draw(batch, xOffset, 160 - i * 16);
		}
		
		if (ready) {
			t.draw(batch, xOffset, 0);
		}
	}
}
