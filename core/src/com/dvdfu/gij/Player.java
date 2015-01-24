package com.dvdfu.gij;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	int xCell;
	int timer;
	
	int RIGHT;
	int LEFT;
	int MOVE;
	int ATTACK;
	int GUARD;
	int FOCUS;
	int REMOVE;
	int READY;
	
	public Player(Level level) {
		this.level = level;
		sprite = new SpriteComponent(Consts.atlas.findRegion("player"));
		moveQueue = new LinkedList<Actions>();
	}
	
	public void setPlayer(int player) {
		if (player == 1) {
			RIGHT = Input.Keys.RIGHT;
			LEFT = Input.Keys.LEFT;
			MOVE = Input.Keys.M;
			ATTACK = Input.Keys.A;
			GUARD = Input.Keys.G;
			FOCUS = Input.Keys.F;
			REMOVE = Input.Keys.BACKSPACE;
			READY = Input.Keys.ENTER;
		} else {
			
		}
	}
	
	public void update() {
		if (level.movePhase) {
			handleInput();
		}
	}
	
	public void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				addMove(Actions.MOVE_RIGHT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				addMove(Actions.MOVE_LEFT);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				addMove(Actions.ATTACK_RIGHT);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				addMove(Actions.ATTACK_LEFT);
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			addMove(Actions.GUARD);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
			addMove(Actions.FOCUS);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
			removeMove();
		}
		if (doneMoves && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			ready = true;
		}
		System.out.println("-------------");
		System.out.println(moveQueue);
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
		sprite.draw(batch, xCell * 32, 16);
		
	}
}
